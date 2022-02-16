package com.bjsxt.controller;

import com.bjsxt.domain.SysMenu;
import com.bjsxt.model.R;
import com.bjsxt.model.RolePrivilegesParam;
import com.bjsxt.service.SysRolePrivilegeService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@Api(tags = "角色权限管理")
public class SysRolePrivilegeController {


    @Autowired
    private SysRolePrivilegeService sysRolePrivilegeService;

    @GetMapping("/roles_privileges")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "roleId", value = "角色")
    })
    @ApiOperation("查询角色的权限列表")
    public R<List<SysMenu>> findSysMenuAndPrivileges(Long roleId) {

        List<SysMenu> menuList = sysRolePrivilegeService.findSysMenuAndPrivileges(roleId);
        return R.ok(menuList);
    }

    @PostMapping("/grant_privileges")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "RolePrivilegesParam", value = "角色对应的权限信息")
    })
    @ApiOperation("授予角色相应的权限")
    public R grantPrivileges(@RequestBody RolePrivilegesParam rolePrivilegesParam) {
        boolean isOk = sysRolePrivilegeService.grantPrivileges(rolePrivilegesParam);
        if (isOk) {
            return R.ok();
        }
        return R.fail("给角色授权失败");
    }
}
