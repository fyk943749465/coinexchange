package com.bjsxt.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.bjsxt.domain.User;
import com.bjsxt.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import javax.annotation.Resource;
import java.util.List;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.bjsxt.domain.UserBank;
import com.bjsxt.mapper.UserBankMapper;
import com.bjsxt.service.UserBankService;
@Service
public class UserBankServiceImpl extends ServiceImpl<UserBankMapper, UserBank> implements UserBankService{

    @Autowired
    private UserService userService;

    @Override
    public Page<UserBank> findByPage(Page<UserBank> page, Long usrId) {
        return page(page ,new LambdaQueryWrapper<UserBank>()
                .eq(usrId != null ,UserBank::getUserId ,usrId));
    }

    @Override
    public UserBank getCurrentUserBank(Long id) {
        // 查询用户没有被禁用的银行卡
        UserBank userBank = getOne(new LambdaQueryWrapper<UserBank>()
                .eq(UserBank::getUserId, id)
                .eq(UserBank::getStatus, 1));
        return userBank;
    }

    @Override
    public boolean bind(Long userId, UserBank userBank) {
        // 在修改之前，先用支付密码判断，如果用户输入的支付密码错误，操作终止
        String payPassword = userBank.getPayPassword();
        User user = userService.getById(userId);
        BCryptPasswordEncoder bCryptPasswordEncoder = new BCryptPasswordEncoder();
        if (! bCryptPasswordEncoder.matches(payPassword, user.getPaypassword())) {
            throw new IllegalArgumentException("用户的支付密码不正确，操作终止");
        }
        Long id = userBank.getId();// 有id代表是修改操作
        if (id != null) {
            UserBank ub = getById(id);
            if (ub == null) {
                throw new IllegalArgumentException("用户的银行卡的ID输入错误");
            }
            return updateById(userBank);
        }
        // 若银行卡的id为空，则需要新创建一个
        userBank.setUserId(userId);
        return save(userBank);
    }
}
