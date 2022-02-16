package com.bjsxt.service.impl;

import cn.hutool.core.collection.CollectionUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.bjsxt.domain.SysMenu;
import com.bjsxt.domain.SysPrivilege;
import com.bjsxt.model.RolePrivilegesParam;
import com.bjsxt.service.SysMenuService;
import com.bjsxt.service.SysPrivilegeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.bjsxt.mapper.SysRolePrivilegeMapper;
import com.bjsxt.domain.SysRolePrivilege;
import com.bjsxt.service.SysRolePrivilegeService;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

@Service
public class SysRolePrivilegeServiceImpl extends ServiceImpl<SysRolePrivilegeMapper, SysRolePrivilege> implements SysRolePrivilegeService{

    @Autowired
    private SysMenuService sysMenuService;

    @Autowired
    private SysPrivilegeService sysPrivilegeService;

    @Autowired
    private SysRolePrivilegeService sysRolePrivilegeService;


    @Override
    public List<SysMenu> findSysMenuAndPrivileges(Long roleId) {
        List<SysMenu> sysMenus = sysMenuService.list();
        if (CollectionUtil.isEmpty(sysMenus)) {
            return Collections.emptyList();
        }
        List<SysMenu> rootMenus = sysMenus
                .stream()
                .filter(sysMenu -> sysMenu.getParentId() == null)
                .collect(Collectors.toList());
        if (CollectionUtil.isEmpty(rootMenus)) {
            return Collections.emptyList();
        }
        List<SysMenu> subMenus = new ArrayList<>();
        for (SysMenu sysMenu : rootMenus) {
            subMenus.addAll(getSubMenus(sysMenu.getId(), roleId, sysMenus));
        }
        return subMenus;
    }

    @Transactional
    @Override
    public boolean grantPrivileges(RolePrivilegesParam rolePrivilegesParam) {
        // 删除该角色的权限
        Long roleId = rolePrivilegesParam.getRoleId();
        // 重新授予该角色权限
        sysRolePrivilegeService.remove(new LambdaQueryWrapper<SysRolePrivilege>().eq(SysRolePrivilege::getRoleId, roleId));
        List<Long> privilegeIds = rolePrivilegesParam.getPrivilegeIds();
        List<SysRolePrivilege> sysRolePrivileges = new ArrayList<>();
        if(!CollectionUtils.isEmpty(privilegeIds)) {
            privilegeIds.stream().forEach(e -> {
                SysRolePrivilege sysRolePrivilege = new SysRolePrivilege();
                sysRolePrivilege.setRoleId(roleId);
                sysRolePrivilege.setPrivilegeId(e);
                sysRolePrivileges.add(sysRolePrivilege);
            });
            return sysRolePrivilegeService.saveBatch(sysRolePrivileges);
        }
        return true;
    }

    private List<SysMenu> getSubMenus(Long parentId, Long roleId, List<SysMenu> sysMenus) {

        List<SysMenu> menus = new ArrayList<>();
        for (SysMenu sysMenu : sysMenus) {
            if (sysMenu.getParentId() == parentId) {
                List<SysPrivilege> sysPrivileges = sysPrivilegeService.getPrivilegesByMenuId(sysMenu.getId(), roleId);
                sysMenu.setPrivileges(sysPrivileges); // 当前菜单对应的权限数据
                sysMenu.setChilds(getSubMenus(sysMenu.getId(), roleId, sysMenus)); // 当前菜单对应的子菜单
                menus.add(sysMenu);
            }
        }
        return menus;
    }

}
