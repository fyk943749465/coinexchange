package com.bjsxt.controller;

import com.baomidou.mybatisplus.core.metadata.OrderItem;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.bjsxt.domain.User;
import com.bjsxt.domain.UserAuthAuditRecord;
import com.bjsxt.domain.UserAuthInfo;
import com.bjsxt.dto.UserDto;
import com.bjsxt.feign.UserServiceFeign;
import com.bjsxt.model.*;
import com.bjsxt.service.UserAuthAuditRecordService;
import com.bjsxt.service.UserAuthInfoService;
import com.bjsxt.service.UserService;
import com.bjsxt.vo.UseAuthInfoVo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import springfox.documentation.annotations.ApiIgnore;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Map;

@Api(tags = "会员管理")
@RestController
@RequestMapping("/users")
public class UserController implements UserServiceFeign {

    @Autowired
    private UserService userService;

    @Autowired
    private UserAuthInfoService userAuthInfoService;

    @Autowired
    private UserAuthAuditRecordService userAuthAuditRecordService;

    @GetMapping
    @ApiOperation(value = "查询会员的列表")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "current", value = "当前页"),
            @ApiImplicitParam(name = "size", value = "每页显示的条数"),
            @ApiImplicitParam(name = "mobile", value = "会员的手机号"),
            @ApiImplicitParam(name = "userId", value = "会员的Id,精确查询"),
            @ApiImplicitParam(name = "userName", value = "会员的名称"),
            @ApiImplicitParam(name = "realName", value = "会员的真实名称"),
            @ApiImplicitParam(name = "status", value = "会员的状态")

    })
    @PreAuthorize("hasAuthority('user_query')")
    public R<Page<User>> findByPage(@ApiIgnore Page<User> page,
                                    String mobile,
                                    Long userId,
                                    String userName,
                                    String realName,
                                    Integer status
    ) {
        page.addOrder(OrderItem.desc("last_update_time"));
        Page<User> userPage = userService.findByPage(page, mobile, userId, userName, realName, status, null);
        return R.ok(userPage);
    }

    @PostMapping("/status")
    @ApiOperation(value = "修改用户的状态")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "id", value = "会员的id"),
            @ApiImplicitParam(name = "status", value = "会员的状态"),
    })
    @PreAuthorize("hasAuthority('user_update')")
    public R updateStatus(Long id, Byte status) {
        User user = new User();
        user.setId(id);
        user.setStatus(status);
        boolean updateById = userService.updateById(user);
        if (updateById) {
            return R.ok("更新成功");
        }
        return R.fail("更新失败");
    }


    @PatchMapping
    @ApiOperation(value = "修改用户")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "user", value = "会员的json数据"),
    })
    @PreAuthorize("hasAuthority('user_update')")
    public R updateStatus(@RequestBody @Validated User user) {
        boolean updateById = userService.updateById(user);
        if (updateById) {
            return R.ok("更新成功");
        }
        return R.fail("更新失败");
    }

    @GetMapping("/info")
    @ApiOperation("查询会员详情")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "id", value = "会员id")
    })
    @PreAuthorize("hasAuthority('user_query')")
    public R<User> info(Long id) {
         return R.ok(userService.getById(id));
    }

    @GetMapping("/directInvites")
    @ApiOperation(value = "查询该用户邀请的用户列表")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "current", value = "当前页"),
            @ApiImplicitParam(name = "size", value = "每页显示的条数"),
            @ApiImplicitParam(name = "userId", value = "用户id")
    })
    public R<Page<User>> getDirectInvites(@ApiIgnore Page<User> page, Long userId) {
        Page<User> userPage = userService.fdinDirectInvitePage(page, userId);
        return R.ok(userPage);
    }


    @GetMapping("/auths")
    @ApiOperation(value = "查询会员的列表")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "current", value = "当前页"),
            @ApiImplicitParam(name = "size", value = "每页显示的条数"),
            @ApiImplicitParam(name = "mobile", value = "会员的手机号"),
            @ApiImplicitParam(name = "userId", value = "会员的Id,精确查询"),
            @ApiImplicitParam(name = "userName", value = "会员的名称"),
            @ApiImplicitParam(name = "realName", value = "会员的真实名称"),
            @ApiImplicitParam(name = "reviewsStatus", value = "会员的审核状态")

    })
    @PreAuthorize("hasAuthority('user_query')")
    public R<Page<User>> findAuthByPage(@ApiIgnore Page<User> page,
                                    String mobile,
                                    Long userId,
                                    String userName,
                                    String realName,
                                    Integer reviewsStatus
    ) {
        page.addOrder(OrderItem.desc("last_update_time"));
        Page<User> userPage = userService.findByPage(page, mobile, userId, userName, realName, null, reviewsStatus);
        return R.ok(userPage);
    }


    @GetMapping("/auth/info")
    @ApiOperation(value = "查询用户的认证详情")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "id", value = "用户的Id")
    })
    public R<UseAuthInfoVo> getUseAuthInfo(Long id) {

        User user = userService.getById(id);
        List<UserAuthAuditRecord> userAuthAuditRecordList = null;
        List<UserAuthInfo> userAuthInfoList = null;
        if (user != null) {
            // 用户的审核记录
            Integer reviewsStatus = user.getReviewsStatus();
            if (reviewsStatus == null || reviewsStatus == 0) { // 待审核
                userAuthAuditRecordList = Collections.emptyList(); // 用户没有审核记录
                userAuthInfoList = userAuthInfoService.getUserAuthInfoByUserId(id);
            } else {
                userAuthAuditRecordList = userAuthAuditRecordService.getUserAuthAuditRecordList(id);
                // 查询用户的认证详情列表-> 用户的身份信息
                UserAuthAuditRecord userAuthAuditRecord = userAuthAuditRecordList.get(0);// 之前我们查询时,是按照认证的日志排序的,第0 个值,就是最近被认证的一个值
                Long authCode = userAuthAuditRecord.getAuthCode(); // 认证的唯一标识
                userAuthInfoList = userAuthInfoService.getUserAuthInfoByCode(authCode);
            }
        }
        return R.ok(new UseAuthInfoVo(user, userAuthInfoList, userAuthAuditRecordList));
    }


    /**
     * 审核的本质:
     * 在于对一组图片(唯一Code)的认可,符合条件,审核通过
     *
     * @return
     */
    @PostMapping("/auths/status")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "id", value = "用户的ID"),
            @ApiImplicitParam(name = "authStatus", value = "用户的审核状态"),
            @ApiImplicitParam(name = "authCode", value = "一组图片的唯一标识"),
            @ApiImplicitParam(name = "remark", value = "审核拒绝的原因"),
    })
    public R updateUserAuthStatus(@RequestParam(required = true) Long id,
                                  @RequestParam(required = true) Byte authStatus,
                                  @RequestParam(required = true) Long authCode,
                                  String remark) {
        // 审核: 1 修改user 里面的reviewStatus
        // 2 在authAuditRecord 里面添加一条记录

        userService.updateUserAuthStatus(id, authStatus, authCode, remark);

        return R.ok();
    }

    @GetMapping("/current/info")
    @ApiOperation(value = "获取当前登录用户对象的信息")
    public R<User> currentUserInfo() {
        // 获取到当前登录对象的一个id
        String idStr = SecurityContextHolder.getContext().getAuthentication().getPrincipal().toString();
        User user = userService.getById(Long.valueOf(idStr));
        // 屏蔽到一些用户敏感信息
        user.setPassword("****");
        user.setPaypassword("***");
        user.setAccessKeyId("****");
        user.setAccessKeySecret("******");
        return R.ok(user);
    }

    @PostMapping("/authAccount")
    @ApiOperation(value = "用户的实名认证")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "userAuthForm", value = "userAuthFormjson数据")
    })
    public R identifyCheck(@RequestBody UserAuthForm userAuthForm) {
        String idStr = SecurityContextHolder.getContext().getAuthentication().getPrincipal().toString();
        boolean isOk = userService.identifyVerify(Long.valueOf(idStr), userAuthForm);
        if (isOk) {
            return R.ok();
        }
        return R.fail("认证失败");
    }

    @PostMapping("/authUser")
    @ApiOperation(value = "用户进行高级认证")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "imgs", value = "用户的图片地址")
    })
    public R authUser(@RequestBody String[] imgs) {
        String idStr = SecurityContextHolder.getContext().getAuthentication().getPrincipal().toString();
        userService.authUser(Long.valueOf(idStr), Arrays.asList(imgs));
        return R.ok();
    }

    @PostMapping("/updatePhone")
    @ApiOperation(value = "修改手机号码")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "updatePhoneParam", value = "修改手机号码提供的json数据")
    })
    public R updatePhone(@RequestBody @Validated UpdatePhoneParam updatePhoneParam) {
        String idStr = SecurityContextHolder.getContext().getAuthentication().getPrincipal().toString();
        boolean isOk = userService.updatePhone(Long.valueOf(idStr), updatePhoneParam);
        if (isOk) {
            return R.ok();
        }
        return R.fail("修改手机号码失败");
    }

    @GetMapping("/checkTel")
    @ApiOperation(value = "检查新手机号码是否可用，若可用，则给新手机号码发送验证码")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "mobile", value = "手机号码"),
            @ApiImplicitParam(name = "countryCode", value = "国家代码")
    })
    public R checkNewPhone(@RequestParam(required = true) String mobile, @RequestParam(required = true) String countryCode) {
        boolean isOk = userService.checkNewPhone(mobile, countryCode);
        if (isOk) {
            return R.ok();
        }
        return R.fail("新手机号码已被使用");
    }

    @PostMapping("/updateLoginPassword")
    @ApiOperation(value = "修改用户的登录密码")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "updateLoginParam", value = "用户密码的json数据")
    })
    public R updateLoginPwd(@RequestBody @Validated UpdateLoginParam updateLoginParam) {
        String idStr = SecurityContextHolder.getContext().getAuthentication().getPrincipal().toString();
        boolean isOk = userService.updateUserPwd(Long.valueOf(idStr), updateLoginParam);
        return isOk ? R.ok() : R.fail("修改密码失败");
    }


    @PostMapping("/updatePayPassword")
    @ApiOperation(value = "修改用户的交易密码")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "updateLoginParam", value = "用户密码的json数据")
    })
    public R updateLoginPayPwd(@RequestBody @Validated UpdateLoginParam updateLoginParam) {
        String idStr = SecurityContextHolder.getContext().getAuthentication().getPrincipal().toString();
        boolean isOk = userService.updateUserPayPwd(Long.valueOf(idStr), updateLoginParam);
        return isOk ? R.ok() : R.fail("修改密码失败");
    }

    @PostMapping("/setPayPassword")
    @ApiOperation(value = "重新设置交易密码")
    public R setPayPassword(@RequestBody @Validated UnsetPayPassword unsetPayPassword) {

        String idStr = SecurityContextHolder.getContext().getAuthentication().getPrincipal().toString();
        boolean isOk = userService.unsetPayPassword(Long.valueOf(idStr),unsetPayPassword);
        return isOk ? R.ok() : R.fail("重置密码失败");

    }

    /**
     * 获取用户的邀请列表，不是分页查询
     */
    @GetMapping("/invites")
    public R<List<User>> getUserInvites() {
        Long userId = Long.valueOf(SecurityContextHolder.getContext().getAuthentication().getPrincipal().toString());
        List<User> invites = userService.getUserInvites(userId);
        return R.ok(invites);

    }

    /**
     * 用于admin-service里面的远程调用member-service
     * @param ids
     * @param userName
     * @param mobile
     * @return
     */
    @Override
    public Map<Long, UserDto> getBasicUsers(List<Long> ids, String userName, String mobile) {
        Map<Long, UserDto> userDtoMap = userService.getBasicUsers(ids, userName, mobile);
        return userDtoMap;
    }
}
