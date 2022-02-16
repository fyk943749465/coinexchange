package com.bjsxt.controller;

import com.baomidou.mybatisplus.core.metadata.OrderItem;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.bjsxt.domain.SysUser;
import com.bjsxt.model.R;
import com.bjsxt.service.SysUserService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import springfox.documentation.annotations.ApiIgnore;

@RestController
@Api(tags = "员工管理")
@RequestMapping("/users")
public class SysUserController {


    @Autowired
    private SysUserService sysUserService;

    @GetMapping
    @ApiOperation("按条件分页查询员工信息")
    @ApiImplicitParams({
            @ApiImplicitParam(name ="current", value = "当前页"),
            @ApiImplicitParam(name = "size", value = "当页大小"),
            @ApiImplicitParam(name = "mobile", value = "员工手机号码"),
            @ApiImplicitParam(name = "fullname", value = "员工的姓名")
    })
    @PreAuthorize("hasAuthority('sys_user_query')")
    public R<Page<SysUser>> findUsers(@ApiIgnore Page<SysUser> page, String mobile, String fullname) {
        page.addOrder(OrderItem.desc("last_update_time"));
        Page<SysUser> users = sysUserService.findByPage(page, mobile, fullname);
        return R.ok(users);
    }

    @PostMapping
    @ApiOperation("添加用户")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "sysUser", value = "sysUser 的json数据")
    })
    @PreAuthorize("hasAuthority('sys_user_create')")
    public R addUser(@RequestBody SysUser sysUser) {

        boolean save = sysUserService.addUser(sysUser);
        if (save) {
            return R.ok("新增用户成功");
        }
        return R.fail("新增用户失败");
    }
}
