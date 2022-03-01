package com.bjsxt.controller;

import com.bjsxt.domain.CoinConfig;
import com.bjsxt.domain.Config;
import com.bjsxt.model.R;
import com.bjsxt.service.CoinConfigService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/coinConfigs")
@Api(tags = "币种配置的控制器")
public class CoinConfigController {

    @Autowired
    private CoinConfigService configService;

    @GetMapping("/info/{coinId}")
    @ApiOperation(value = "查询币种的配置信息")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "coinId", value ="币种的id")
    })
    public R<CoinConfig> getCoinConfig(@PathVariable("coinId") Long coinId) {

        CoinConfig coinConfig = configService.findByCoinId(coinId);
        return R.ok(coinConfig);
    }
}