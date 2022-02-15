package com.bjsxt.controller;

import com.bjsxt.domain.SysMenu;
import com.bjsxt.domain.SysRolePrivilege;
import com.bjsxt.model.R;
import com.bjsxt.service.SysMenuService;
import com.bjsxt.service.SysRolePrivilegeService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
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
}
