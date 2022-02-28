package com.bjsxt.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.bjsxt.domain.CoinType;
import com.bjsxt.model.R;
import com.bjsxt.service.CoinTypeService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import springfox.documentation.annotations.ApiIgnore;

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
    public R<Page<CoinType>> findByPage(@ApiIgnore Page<CoinType> page, String code) {

        Page<CoinType> pages = coinTypeService.findByPage(page, code);
        return R.ok(pages);
    }
}
