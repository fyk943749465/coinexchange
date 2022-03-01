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
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

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


    @PatchMapping
    @ApiOperation(value = "修改币种配置信息")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "coinConfig", value = "coinConfig的json数据")
    })
    public R updateCoinConfig(@RequestBody @Validated CoinConfig coinConfig) {
        boolean isOk = configService.updateById(coinConfig);
        if (isOk) {
            return R.ok();
        }
        return R.fail("修改失败");
    }
}
