package com.bjsxt.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.bjsxt.domain.User;
import com.baomidou.mybatisplus.extension.service.IService;
import com.bjsxt.dto.UserDto;
import com.bjsxt.model.*;

import java.util.List;
import java.util.Map;

public interface UserService extends IService<User>{


    Page<User> findByPage(Page<User> page, String mobile, Long userId, String userName, String realName, Integer status, Integer reviewStatus);

    Page<User> fdinDirectInvitePage(Page<User> page, Long userId);

    void updateUserAuthStatus(Long id, Byte authStatus, Long authCode, String remark);

    boolean identifyVerify(Long valueOf, UserAuthForm userAuthForm);

    void authUser(Long valueOf, List<String> imgs);

    /**
     * 将旧手机号码修改为新手机号码
     * @param valueOf
     * @param updatePhoneParam
     * @return
     */
    boolean updatePhone(Long valueOf, UpdatePhoneParam updatePhoneParam);

    /**
     * 检查新手机号码是否可用
     * @param mobile
     * @param countryCode
     * @return
     */
    boolean checkNewPhone(String mobile, String countryCode);

    /**
     * 修改用的登录密码
     * @param valueOf
     * @param updateLoginParam
     * @return
     */
    boolean updateUserPwd(Long valueOf, UpdateLoginParam updateLoginParam);

    /**
     * 修改用户的交易密码
     * @param valueOf
     * @param updateLoginParam
     * @return
     */
    boolean updateUserPayPwd(Long valueOf, UpdateLoginParam updateLoginParam);

    /**
     * 重置用户密码
     * @param valueOf
     * @param unsetPayPassword
     * @return
     */
    boolean unsetPayPassword(Long valueOf, UnsetPayPassword unsetPayPassword);

    List<User> getUserInvites(Long userId);

    Map<Long, UserDto> getBasicUsers(List<Long> ids, String userName, String mobile);

    /**
     * 用户的注册
     * @param registerParam
     * @return
     */
    boolean register(RegisterParam registerParam);

    /**
     * 重置密码
     * @param unsetPasswordParam
     * @return
     */
    boolean unsetPassword(UnsetPasswordParam unsetPasswordParam);
}
