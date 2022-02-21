package com.bjsxt.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.springframework.stereotype.Service;
import javax.annotation.Resource;
import java.util.List;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.bjsxt.domain.UserBank;
import com.bjsxt.mapper.UserBankMapper;
import com.bjsxt.service.UserBankService;
@Service
public class UserBankServiceImpl extends ServiceImpl<UserBankMapper, UserBank> implements UserBankService{

    @Override
    public Page<UserBank> findByPage(Page<UserBank> page, Long usrId) {
        return page(page ,new LambdaQueryWrapper<UserBank>()
                .eq(usrId != null ,UserBank::getUserId ,usrId));
    }
}