package com.bjsxt.controller;

import com.baomidou.mybatisplus.core.metadata.OrderItem;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.bjsxt.domain.SysRole;
import com.bjsxt.model.R;
import com.bjsxt.service.SysRoleService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import springfox.documentation.annotations.ApiIgnore;

import java.util.Arrays;

@RestController
@RequestMapping("/roles")
@Api(tags = "角色管理")
public class SysRoleController {


    @Autowired
    private SysRoleService sysRoleService;

    @GetMapping
    @ApiOperation("条件分页查询")
    @PreAuthorize("hasAuthority('sys_role_query')")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "current", value = "当前页"),
            @ApiImplicitParam(name = "size", value = "每页显示的大小"),
            @ApiImplicitParam(name = "name", value = "角色名称")
    })
    public R<Page<SysRole>> findPage(@ApiIgnore Page<SysRole> page, String name) {
        page.addOrder(OrderItem.desc("last_update_time"));
        return R.ok(sysRoleService.findByPage(page, name));
    }

    @PostMapping
    @ApiOperation("新增角色")
    @PreAuthorize("hasAuthority('sys_role_create')")
    public R add(@RequestBody @Validated SysRole sysRole) {
        boolean save = sysRoleService.save(sysRole);
        System.out.println(save);
        if (save) {
            return R.ok();
        }
        return R.fail("新增角色失败");
    }

    @PostMapping("/delete")
    @ApiOperation("删除角色")
    @PreAuthorize("hasAuthority('sys_role_delete')")
    @ApiImplicitParams({
            @ApiImplicitParam(name="ids", value = "删除角色id集合")
    })
    public R delete(@RequestBody String[] ids) {
        if (ids == null || ids.length == 0) {
            return R.fail("删除的角色id为空");
        }
        boolean b = sysRoleService.removeByIds(Arrays.asList(ids));
        if (b) {
            return R.ok();
        }
        return R.fail("删除失败");
    }

}
