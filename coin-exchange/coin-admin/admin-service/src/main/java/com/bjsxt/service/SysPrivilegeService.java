package com.bjsxt.service;

import com.bjsxt.domain.SysPrivilege;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

public interface SysPrivilegeService extends IService<SysPrivilege>{


    List<SysPrivilege> getPrivilegesByMenuId(Long id, Long roleId);
}
