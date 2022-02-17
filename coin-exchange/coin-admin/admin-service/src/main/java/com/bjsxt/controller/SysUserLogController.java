package com.bjsxt.controller;

import com.baomidou.mybatisplus.core.metadata.OrderItem;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.bjsxt.domain.SysUserLog;
import com.bjsxt.model.R;
import com.bjsxt.service.SysUserLogService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import springfox.documentation.annotations.ApiIgnore;

@RestController
@Api(tags = "用户的操作日志记录")
@RequestMapping("/sysUserLog")
public class SysUserLogController {

    @Autowired
    private SysUserLogService sysUserLogService;

    @GetMapping
    @ApiOperation(value = "分页查询用户的操作记录")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "page", value = "分页查询")
    })
    @PreAuthorize("hasAuthority('sys_user_log_query')")
    public R findBySysUserLog(@ApiIgnore Page<SysUserLog> page) {
        page.addOrder(OrderItem.desc("created"));
        return R.ok(sysUserLogService.page(page));
    }
}
