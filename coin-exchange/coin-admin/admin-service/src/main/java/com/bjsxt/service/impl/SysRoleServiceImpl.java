package com.bjsxt.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import javax.annotation.Resource;
import java.util.List;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.bjsxt.domain.SysRole;
import com.bjsxt.mapper.SysRoleMapper;
import com.bjsxt.service.SysRoleService;
import org.springframework.util.StringUtils;

@Service
public class SysRoleServiceImpl extends ServiceImpl<SysRoleMapper, SysRole> implements SysRoleService{

    @Autowired
    private SysRoleMapper sysRoleMapper;
    /**
     * 判断一个用户是否为超级的管理员
     *
     * @param userId
     * @return
     */
    @Override
    public boolean isSuperAdmin(Long userId) {
        // 当用户的角色code 为：ROLE_ADMIN 时，该用户为超级的管理员
        // 用户的id->用户的角色->该角色的Code是否为ROLE_ADMIN
        String roleCode = sysRoleMapper.getUserRoleCode(userId);
        if (!StringUtils.isEmpty(roleCode) && roleCode.equals("ROLE_ADMIN")) {
            return true;
        }
        return false;
    }

}
