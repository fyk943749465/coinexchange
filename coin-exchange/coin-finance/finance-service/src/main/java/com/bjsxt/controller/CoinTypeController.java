package com.bjsxt.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.bjsxt.domain.Coin;
import com.bjsxt.domain.CoinType;
import com.bjsxt.model.R;
import com.bjsxt.service.CoinTypeService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import springfox.documentation.annotations.ApiIgnore;

import java.util.List;

@RestController
@RequestMapping("/coinTypes")
@Api(value = "币种类别")
public class CoinTypeController {

    @Autowired
    private CoinTypeService coinTypeService;

    // ttp://localhost:9528/finance/coinTypes?code=&current=1&size=15
    @GetMapping
    @ApiOperation(value = "条件分页查询币种类型")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "current", value = "当前页"),
            @ApiImplicitParam(name = "size", value = "每页显示条数"),
            @ApiImplicitParam(name = "code", value = "币种类型")
    })
    @PreAuthorize("hasAuthority('trade_coin_type_query')")
    public R<Page<CoinType>> findByPage(@ApiIgnore Page<CoinType> page, String code) {

        Page<CoinType> pages = coinTypeService.findByPage(page, code);
        return R.ok(pages);
    }

    @PostMapping
    @ApiOperation(value = "新增货币类型")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "CoinType", value = "CoinType 的json数据")
    })
    @PreAuthorize("hasAuthority('trade_coin_type_create')")
    public R add(@RequestBody @Validated CoinType coinType) {
        boolean save = coinTypeService.save(coinType);
        if (save) {
            return R.ok();
        }
        return R.fail("新增失败");
    }


    @PatchMapping
    @ApiOperation(value = "修改货币类型")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "CoinType", value = "CoinType 的json数据")
    })
    @PreAuthorize("hasAuthority('trade_coin_type_upate')")
    public R update(@RequestBody @Validated CoinType coinType) {
        boolean save = coinTypeService.updateById(coinType);
        if (save) {
            return R.ok();
        }
        return R.fail("修改失败");
    }

    // http://localhost:9528/finance/coinTypes/setStatus
    @PostMapping("/setStatus")
    @ApiOperation(value = "修改货币状态")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "id", value = "coinType id"),
            @ApiImplicitParam(name = "status", value = "状态")
    })
    @PreAuthorize("hasAuthority('trade_coin_type_upate')")
    public R setState(@RequestBody CoinType coinType) {

        boolean save = coinTypeService.updateById(coinType);
        if (save) {
            return R.ok();
        }
        return R.fail("修改状态失败");
    }

    // http://localhost:9528/finance/coinTypes/all?status=1
    @GetMapping("/all")
    @ApiOperation(value = "查询所有的币种类型")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "status", value = "币种的状态")
    })
    @PreAuthorize("hasAuthority('trade_coin_type_query')")
    public R<List<CoinType>> findAllCoinTypeByState(Byte status) {
        List<CoinType> coinTypes = coinTypeService.listByStatus(status);
        return R.ok(coinTypes);
    }
}
