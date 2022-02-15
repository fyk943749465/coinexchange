package com.bjsxt.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.bjsxt.domain.SysRolePrivilege;

import java.util.Set;

public interface SysRolePrivilegeMapper extends BaseMapper<SysRolePrivilege> {

    Set<Long> getPrivilegesByRoleId(Long roleId);
}