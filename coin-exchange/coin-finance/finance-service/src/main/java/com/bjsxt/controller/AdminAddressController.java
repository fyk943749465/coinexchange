package com.bjsxt.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.bjsxt.domain.AdminAddress;
import com.bjsxt.model.R;
import com.bjsxt.service.AdminAddressService;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import springfox.documentation.annotations.ApiIgnore;

@RestController
@RequestMapping("/adminAddress")
public class AdminAddressController {

    @Autowired
    private AdminAddressService adminAddressService;

    @GetMapping
    @ApiOperation(value = "查询归集地址")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "current", value = "当前页"),
            @ApiImplicitParam(name = "size", value = "每页显示数量"),
            @ApiImplicitParam(name = "coinId", value = "币种的id")
    })
    public R<Page<AdminAddress>> findByPage(@ApiIgnore Page<AdminAddress> page, Long coinId) {
        Page<AdminAddress> adminAddressPage =  adminAddressService.findByPage(page, coinId);
        return R.ok(adminAddressPage);
    }

    @PostMapping
    @ApiOperation(value = "钱包归集地址的新增操作")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "adminAddress", value = "adminAddress 的json数据")
    })
    public R add(@RequestBody @Validated AdminAddress adminAddress) {

        boolean isOk = adminAddressService.save(adminAddress);
        if (isOk) {
            return R.ok();
        }
        return R.fail("新增失败");
    }


    @PatchMapping
    @ApiOperation(value = "钱包归集地址的新增操作")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "adminAddress", value = "adminAddress 的json数据")
    })
    public R update(@RequestBody @Validated AdminAddress adminAddress) {

        boolean isOk = adminAddressService.updateById(adminAddress);
        if (isOk) {
            return R.ok();
        }
        return R.fail("修改失败");
    }
}
