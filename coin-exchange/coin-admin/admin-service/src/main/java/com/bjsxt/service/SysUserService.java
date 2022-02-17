package com.bjsxt.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.bjsxt.domain.SysUser;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

public interface SysUserService extends IService<SysUser>{


    /**
     * 分页查询员工信息
     * @param page
     * @param mobile
     * @param fullname
     * @return
     */
    Page<SysUser> findByPage(Page<SysUser> page, String mobile, String fullname);

    boolean addUser(SysUser sysUser);

    boolean updateSysUser(SysUser sysUser);

    boolean deleteUsers(List<Long> asList);
}
