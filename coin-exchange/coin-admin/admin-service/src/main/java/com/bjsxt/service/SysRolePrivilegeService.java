package com.bjsxt.service;

import com.bjsxt.domain.SysMenu;
import com.bjsxt.domain.SysRolePrivilege;
import com.baomidou.mybatisplus.extension.service.IService;
import com.bjsxt.model.RolePrivilegesParam;

import java.util.List;

public interface SysRolePrivilegeService extends IService<SysRolePrivilege>{


    List<SysMenu> findSysMenuAndPrivileges(Long roleId);

    boolean grantPrivileges(RolePrivilegesParam rolePrivilegesParam);
}
