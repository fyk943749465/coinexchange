package com.bjsxt.service.impl;

import cn.hutool.core.collection.CollectionUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.bjsxt.mapper.SysRolePrivilegeMapper;
import com.bjsxt.service.SysRolePrivilegeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import javax.annotation.Resource;
import java.util.Collections;
import java.util.List;
import java.util.Set;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.bjsxt.domain.SysPrivilege;
import com.bjsxt.mapper.SysPrivilegeMapper;
import com.bjsxt.service.SysPrivilegeService;
import sun.util.resources.cldr.gv.LocaleNames_gv;

@Service
public class SysPrivilegeServiceImpl extends ServiceImpl<SysPrivilegeMapper, SysPrivilege> implements SysPrivilegeService{


    @Autowired
    private SysRolePrivilegeMapper sysRolePrivilegeMapper;

    /**
     * 获取当前菜单对应的权限数据
     * 获取当前角色对应的权限数据
     * @param menuId
     * @param roleId
     * @return
     */
    @Override
    public List<SysPrivilege> getPrivilegesByMenuId(Long menuId, Long roleId) {

        // 1. 查询当前菜单对应的权限
        List<SysPrivilege> sysPrivileges = list(new LambdaQueryWrapper<SysPrivilege>().eq(SysPrivilege::getMenuId, menuId));
        if (CollectionUtil.isEmpty(sysPrivileges)) {
            return Collections.EMPTY_LIST;
        }

        for (SysPrivilege sysPrivilege : sysPrivileges) {
            Set<Long> currentRoleSysPrivilegesIds = sysRolePrivilegeMapper.getPrivilegesByRoleId(roleId);
            if (currentRoleSysPrivilegesIds.contains(sysPrivilege.getId())) {
                sysPrivilege.setOwn(1);
            }
        }
        return sysPrivileges;
    }

}
