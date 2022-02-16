package com.bjsxt.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.bjsxt.domain.SysUserRole;
import com.bjsxt.service.SysUserRoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.bjsxt.mapper.SysUserMapper;
import com.bjsxt.domain.SysUser;
import com.bjsxt.service.SysUserService;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

@Service
public class SysUserServiceImpl extends ServiceImpl<SysUserMapper, SysUser> implements SysUserService{

    @Autowired
    private SysUserRoleService sysUserRoleService;

    @Override
    public Page<SysUser> findByPage(Page<SysUser> page, String mobile, String fullname) {
        Page<SysUser> pageResult = page(page, new LambdaQueryWrapper<SysUser>()
                .like(!StringUtils.isEmpty(mobile), SysUser::getMobile, mobile)
                .like(!StringUtils.isEmpty(fullname), SysUser::getFullname, fullname)
        );
        List<SysUser> records = pageResult.getRecords();
        if (!CollectionUtils.isEmpty(records)) {
            for (SysUser record : records) {
                List<SysUserRole> userRoles = sysUserRoleService.list(new LambdaQueryWrapper<SysUserRole>().eq(SysUserRole::getUserId, record.getId()));
                String roleString = userRoles.stream().map(e -> e.getRoleId().toString()).collect(Collectors.joining(","));
                record.setRole_strings(roleString);
            }
        }
        return pageResult;
    }

    @Transactional
    @Override
    public boolean addUser(SysUser sysUser) {
        Long createUserId = Long.valueOf(SecurityContextHolder.getContext().getAuthentication().getPrincipal().toString());
        sysUser.setCreateBy(createUserId);
        String password = sysUser.getPassword();
        String encode = new BCryptPasswordEncoder().encode(password);
        sysUser.setPassword(encode);
        boolean save = save(sysUser);
        if (save) {
            String roleString = sysUser.getRole_strings();
            if (!StringUtils.isEmpty(roleString)) {
                String[] roles = roleString.split(",");
                List<SysUserRole> sysUserRoles = new ArrayList<>(roles.length);
                for (String role : roles) {
                    SysUserRole sysUserRole = new SysUserRole();
                    sysUserRole.setCreateBy(createUserId);
                    sysUserRole.setUserId(Long.valueOf(sysUser.getId()));
                    sysUserRole.setRoleId(Long.valueOf(role));
                    sysUserRoles.add(sysUserRole);
                }
                sysUserRoleService.saveBatch(sysUserRoles);
            }
            return true;
        }
        return false;
    }
}
