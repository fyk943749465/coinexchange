package com.bjsxt.service.impl;

import cn.hutool.core.lang.Snowflake;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.bjsxt.config.IdAutoConfiguration;
import com.bjsxt.domain.Sms;
import com.bjsxt.domain.UserAuthAuditRecord;
import com.bjsxt.domain.UserAuthInfo;
import com.bjsxt.model.UnsetPayPassword;
import com.bjsxt.model.UpdateLoginParam;
import com.bjsxt.model.UpdatePhoneParam;
import com.bjsxt.model.UserAuthForm;
import com.bjsxt.sdk.geetest.GeetestLib;
import com.bjsxt.service.SmsService;
import com.bjsxt.service.UserAuthAuditRecordService;
import com.bjsxt.service.UserAuthInfoService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import javax.annotation.Resource;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.bjsxt.domain.User;
import com.bjsxt.mapper.UserMapper;
import com.bjsxt.service.UserService;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

@Service
@Slf4j
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements UserService{

    @Autowired
    private UserAuthAuditRecordService userAuthAuditRecordService;

    @Autowired
    private GeetestLib geetestLib;

    @Autowired
    private RedisTemplate<String, Object> redisTemplate;

    @Autowired
    private Snowflake snowflake;

    @Autowired
    private UserAuthInfoService userAuthInfoService;

    @Autowired
    private StringRedisTemplate stringRedisTemplate;

    @Autowired
    private SmsService smsService;

    @Override
    public Page<User> findByPage(Page<User> page, String mobile, Long userId,
                                 String userName, String realName, Integer status, Integer reviewStatus) {
        return page(page, new LambdaQueryWrapper<User>()
                .like(!StringUtils.isEmpty(mobile), User::getMobile, mobile)
                .like(!StringUtils.isEmpty(userName), User::getUsername, userName)
                .like(!StringUtils.isEmpty(realName), User::getRealName, realName)
                .eq(userId != null, User::getId, userId)
                .eq( status != null, User::getStatus, status)
                .eq(reviewStatus != null, User::getReviewsStatus, reviewStatus)
        );
    }

    @Override
    public Page<User> fdinDirectInvitePage(Page<User> page, Long userId) {
        return page(page, new LambdaQueryWrapper<User>().eq(userId != null, User::getDirectInviteid, userId));
    }

    /**
     * 修改用户的审核状态
     *
     * @param id
     * @param authStatus
     * @param authCode
     */
    @Override
    @Transactional
    public void updateUserAuthStatus(Long id, Byte authStatus, Long authCode, String remark) {
        log.info("开始修改用户的审核状态,当前用户{},用户的审核状态{},图片的唯一code{}", id, authStatus, authCode);
        User user = getById(id);
        if (user != null) {
//            user.setAuthStatus(authStatus); // 认证的状态
            user.setReviewsStatus(authStatus.intValue()); // 审核的状态
            updateById(user); // 修改用户的状态
        }
        UserAuthAuditRecord userAuthAuditRecord = new UserAuthAuditRecord();
        userAuthAuditRecord.setUserId(id);
        userAuthAuditRecord.setStatus(authStatus);
        userAuthAuditRecord.setAuthCode(authCode);

        String usrStr = SecurityContextHolder.getContext().getAuthentication().getPrincipal().toString();
        userAuthAuditRecord.setAuditUserId(Long.valueOf(usrStr)); // 审核人的ID
        userAuthAuditRecord.setAuditUserName("---------------------------");// 审核人的名称 --> 远程调用admin-service ,没有事务
        userAuthAuditRecord.setRemark(remark);

        userAuthAuditRecordService.save(userAuthAuditRecord);
    }

    /**
     * 用户的实名认证
     *
     * @param id           用户的Id
     * @param userAuthForm 认证的表单数据
     * @return 认证的结果
     */
    @Override
    public boolean identifyVerify(Long id, UserAuthForm userAuthForm) {
        User user = getById(id);
        Assert.notNull(user, "认证的用户不存在");
        Byte authStatus = user.getAuthStatus();
        if (!authStatus.equals((byte) 0)) {
            throw new IllegalArgumentException("该用户已经认证成功了");
        }
        // 执行认证
        checkForm(userAuthForm); // 极验
        // 实名认证
        boolean check = IdAutoConfiguration.check(userAuthForm.getRealName(), userAuthForm.getIdCard());
        if (!check) {
            throw new IllegalArgumentException("该用户信息错误,请检查");
        }

        // 设置用户的认证属性
        user.setAuthtime(new Date());
        user.setAuthStatus((byte) 1);
        user.setRealName(userAuthForm.getRealName());
        user.setIdCard(userAuthForm.getIdCard());
        user.setIdCardType(userAuthForm.getIdCardType());

        return updateById(user);
    }

    /**
     *
     * @param id 用户id
     * @param imgs 图片地址
     */
    @Override
    @Transactional
    public void authUser(Long id, List<String> imgs) {
        if(CollectionUtils.isEmpty(imgs)) {
            throw new IllegalArgumentException("用户的身份认证信息为空");
        }
        User user = super.getById(id);
        if (user == null) {
            throw new IllegalArgumentException("请输入正确的userId");
        }
        Long authCode = snowflake.nextId(); // 采用雪花算法生成一个
        List<UserAuthInfo> authInfos = new ArrayList<>();
        for(int i = 0; i < imgs.size(); ++i) {
            String s = imgs.get(i);
            UserAuthInfo userAuthInfo = new UserAuthInfo();
            userAuthInfo.setUserId(id);
            userAuthInfo.setImageUrl(s);
            userAuthInfo.setSerialno(i + 1);    // 身份证的正、反面、手持
            userAuthInfo.setAuthCode(authCode); // 一组图片信息一个authcode
            authInfos.add(userAuthInfo);
        }
        userAuthInfoService.saveBatch(authInfos);
        user.setReviewsStatus(0); //将状态改变为等待审核状态
        updateById(user); // 更新用户状态

    }

    @Override
    public boolean updatePhone(Long id, UpdatePhoneParam updatePhoneParam) {
        User user = super.getById(id);
        String oldMobile = user.getMobile();
        // 从Redis中取出旧手机的验证码
        String oldMobileCode = stringRedisTemplate.opsForValue().get("SMS:VERIFY_OLD_PHONE:" + oldMobile);
        if (!updatePhoneParam.getOldValidateCode().equals(oldMobileCode)) {
            throw new IllegalArgumentException("旧手机验证码错误");
        }
        String newMobileCode = stringRedisTemplate.opsForValue().get("SMS:CHANGE_PHONE_VERIFY:" + updatePhoneParam.getNewMobilePhone());
        if (!updatePhoneParam.getNewMobilePhone().equals(newMobileCode)) {
            throw new IllegalArgumentException("新手机验证码错误");
        }
        user.setMobile(updatePhoneParam.getNewMobilePhone());
        return updateById(user);
    }

    @Override
    public boolean checkNewPhone(String mobile, String countryCode) {
        // 1. 手机号码是否被使用
        int count = count(new LambdaQueryWrapper<User>().eq(User::getMobile, mobile).eq(User::getCountryCode, countryCode));
        if (count > 0) {
            throw new IllegalArgumentException("该手机号码已经被占用");
        }
        Sms sms = new Sms();
        sms.setMobile(mobile);
        sms.setCountryCode(countryCode);
        sms.setTemplateCode("CHANGE_PHONE_VERIFY");
        return smsService.sendSms(sms);
    }

    @Override
    public boolean updateUserPwd(Long id, UpdateLoginParam updateLoginParam) {
        User user = super.getById(id);
        if (user == null) {
            throw new IllegalArgumentException("用户的id错误");
        }
        // 校验原始密码
        String oldpassword = updateLoginParam.getOldpassword();
        BCryptPasswordEncoder bCryptPasswordEncoder = new BCryptPasswordEncoder();
        boolean matches = bCryptPasswordEncoder.matches(oldpassword, user.getPassword());
        if (!matches) {
            throw new IllegalArgumentException("用户的原始密码不正确");
        }
        // 校验手机验证码 // CHANGE_LOGIN_PWD_VERIFY数据库中的模板代码
        String validateCode = updateLoginParam.getValidateCode();
        String phoneValidateCode = stringRedisTemplate.opsForValue().get("SMS:CHANGE_LOGIN_PWD_VERIFY:" + user.getMobile());
        if (!validateCode.equals(phoneValidateCode)) {
            throw new IllegalArgumentException("手机验证码错误");
        }
        user.setPassword(bCryptPasswordEncoder.encode(updateLoginParam.getNewpassword()));
        return updateById(user);
    }

    @Override
    public boolean updateUserPayPwd(Long id, UpdateLoginParam updateLoginParam) {
        User user = super.getById(id);
        if (user == null) {
            throw new IllegalArgumentException("用户的id错误");
        }
        // 校验原始密码
        String oldpassword = updateLoginParam.getOldpassword();
        BCryptPasswordEncoder bCryptPasswordEncoder = new BCryptPasswordEncoder();
        boolean matches = bCryptPasswordEncoder.matches(oldpassword, user.getPaypassword());
        if (!matches) {
            throw new IllegalArgumentException("用户的原始密码不正确");
        }
        // 校验手机验证码 // 数据库中的模板代码CHANGE_PAY_PWD_VERIFY
        String validateCode = updateLoginParam.getValidateCode();
        String phoneValidateCode = stringRedisTemplate.opsForValue().get("SMS:CHANGE_PAY_PWD_VERIFY:" + user.getMobile());
        if (!validateCode.equals(phoneValidateCode)) {
            throw new IllegalArgumentException("手机验证码错误");
        }
        user.setPaypassword(bCryptPasswordEncoder.encode(updateLoginParam.getNewpassword()));
        return updateById(user);
    }

    @Override
    public boolean unsetPayPassword(Long id, UnsetPayPassword unsetPayPassword) {
        User user = super.getById(id);
        if (user == null) {
            throw new IllegalArgumentException("用户id不存在");
        }
        String validateCode = unsetPayPassword.getValidateCode();
        String phoneValidateCode = stringRedisTemplate.opsForValue().get("SMS:FORGOT_PAY_PWD_VERIFY:" + user.getMobile());
        if (!validateCode.equals(phoneValidateCode)) {
            throw new IllegalArgumentException("输入的验证码不正确，请重新输入");
        }
        BCryptPasswordEncoder bCryptPasswordEncoder = new BCryptPasswordEncoder();

        user.setPaypassword(bCryptPasswordEncoder.encode(unsetPayPassword.getPayPassword()));
        return updateById(user);
    }

    private void checkForm(UserAuthForm userAuthForm) {
        userAuthForm.check(geetestLib, redisTemplate);
    }

    @Override
    public User getById(Serializable id) {
        User user = super.getById(id);
        if (user == null) {
            throw new IllegalArgumentException("请输入正确的用户Id");
        }
        Byte seniorAuthStatus = null; // 用户的高级认证状态
        String seniorAuthDesc = "";// 用户的高级认证未通过,原因
        Integer reviewsStatus = user.getReviewsStatus(); // 用户被审核的状态 1通过,2拒绝,0,待审核"
        if (reviewsStatus == null) { //为null 时,代表用户的资料没有上传
            seniorAuthStatus = 3;
            seniorAuthDesc = "资料未填写";
        } else {
            switch (reviewsStatus) {
                case 1:
                    seniorAuthStatus = 1;
                    seniorAuthDesc = "审核通过";
                    break;
                case 2:
                    seniorAuthStatus = 2;
                    // 查询被拒绝的原因--->审核记录里面的
                    // 时间排序,
                    List<UserAuthAuditRecord> userAuthAuditRecordList = userAuthAuditRecordService.getUserAuthAuditRecordList(user.getId());
                    if (!CollectionUtils.isEmpty(userAuthAuditRecordList)) {
                        UserAuthAuditRecord userAuthAuditRecord = userAuthAuditRecordList.get(0);
                        seniorAuthDesc = userAuthAuditRecord.getRemark();
                    }
//                    seniorAuthDesc = "原因未知"; bug所在
                    break;
                case 0:
                    seniorAuthStatus = 0;
                    seniorAuthDesc = "等待审核";
                    break;

            }
        }
        user.setSeniorAuthStatus(seniorAuthStatus);
        user.setSeniorAuthDesc(seniorAuthDesc);
        return user;
    }
}
