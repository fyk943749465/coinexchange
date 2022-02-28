package com.bjsxt.service.impl;

import org.springframework.stereotype.Service;
import javax.annotation.Resource;
import java.util.List;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.bjsxt.mapper.CoinMapper;
import com.bjsxt.domain.Coin;
import com.bjsxt.service.CoinService;
@Service
public class CoinServiceImpl extends ServiceImpl<CoinMapper, Coin> implements CoinService{

}
