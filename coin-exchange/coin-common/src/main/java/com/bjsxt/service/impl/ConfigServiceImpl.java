package com.bjsxt.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import org.springframework.stereotype.Service;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.bjsxt.domain.Config;
import com.bjsxt.mapper.ConfigMapper;
import com.bjsxt.service.ConfigService;
@Service
public class ConfigServiceImpl extends ServiceImpl<ConfigMapper, Config> implements ConfigService{

    @Override
    public Config getConfigByCode(String code) {
        return getOne(new LambdaQueryWrapper<Config>().eq(Config::getCode, code));
    }
}
