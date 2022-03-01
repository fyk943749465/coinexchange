package com.bjsxt.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.bjsxt.domain.Coin;
import com.bjsxt.domain.CoinServer;
import com.bjsxt.model.R;
import com.bjsxt.service.CoinService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/coins")
@Api(tags = "数字货币的数据接口")
public class CoinController {

    @Autowired
    private CoinService coinService;
    // http://localhost:9528/finance/coins?name=xxx&type=btc&status=0&title=xxx&wallet_type=rgb&current=1&size=15

    @GetMapping
    @ApiOperation(value = "分页条件查询")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "current", value = "当前页"),
            @ApiImplicitParam(name = "size", value = "每页显示条数"),
            @ApiImplicitParam(name = "name", value = "币种名称"),
            @ApiImplicitParam(name = "wallet_type", value = "钱包类型"),
            @ApiImplicitParam(name = "type", value = "币种类型"),
            @ApiImplicitParam(name = "status", value = "币种状态"),
            @ApiImplicitParam(name = "title", value = "标题")
    })
    public R<Page<Coin>> findByPage(
            String name, String type, Byte status, String title,
            @RequestParam(value = "wallet_type", required = false) String walletType, Page<Coin> page) {

        Page<Coin> coinPage = coinService.findByPage(name, type, status, title, walletType, page);
        return R.ok(coinPage);
    }

    @PostMapping("/setStatus")
    @ApiOperation(value = "禁用或启用币种")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "Coin", value = "Coin 的json数据")
    })
    public R setStatus(@RequestBody Coin coin) {
        boolean isOk = coinService.updateById(coin);
        if(isOk) {
            return R.ok();
        }
        return R.fail("设置状态失败");
    }

    @GetMapping("/info/{id}")
    @ApiOperation(value = "查询币种的详细信息")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "id", value = "币种的id")
    })
    public R<Coin> info(@PathVariable("id") Long id) {

        Coin coin = coinService.getById(id);
        return R.ok(coin);
    }

    @GetMapping("/all")
    @ApiImplicitParams({
          @ApiImplicitParam(name = "status", value = "币种当前的状态")
    })
    @ApiOperation(value = "通过状态查询所有的币种信息")
    public R<List<Coin>> getCoinAll(Byte status) {
        List<Coin> coins = coinService.getCoinByStatus(status);
        return R.ok(coins);
    }


}
