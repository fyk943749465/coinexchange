package com.bjsxt.service.impl;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.IncorrectResultSizeDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;
import java.util.stream.Collectors;

import static com.bjsxt.constant.LoginConstant.*;

@Service
public class UserServiceDetailsServiceImpl implements UserDetailsService {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        ServletRequestAttributes requestAttributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        String loginType = requestAttributes.getRequest().getParameter("login_type");
        if (StringUtils.isEmpty(loginType)) {
            throw new AuthenticationServiceException("please add login_type paramter");
        }
        UserDetails userDetails = null;
        String grantType = requestAttributes.getRequest().getParameter("grant_type");
        try {
            if (REFRESH_TOKEN.equals(grantType.toUpperCase())) {
                username = adjustUsername(username,  loginType);
            }
            switch (loginType) {
                case ADMIN_TYPE:
                    userDetails = loadAdminUserByUsername(username);
                    break;
                case MEMBER_TYPE:
                    userDetails = loadMemberUserByUsername(username);
                    break;
                default:
                    throw new AuthenticationServiceException(loginType + " is no supported");
            }
        } catch (IncorrectResultSizeDataAccessException e) {
            throw new UsernameNotFoundException("member: " + username + " is not exists");
        }
        return userDetails;
    }

    private String adjustUsername(String username, String loginType) {
        if (ADMIN_TYPE.equals(loginType)) {
            return jdbcTemplate.queryForObject(QUERY_ADMIN_USER_WITH_ID, String.class, username);
        }
        if (MEMBER_TYPE.equals(loginType)) {
            return jdbcTemplate.queryForObject(QUERY_MEMBER_USER_WITH_ID, String.class, username);
        }
        return username;
    }

    private UserDetails loadMemberUserByUsername(String username) {
        return jdbcTemplate.queryForObject(QUERY_MEMBER_SQL, new RowMapper<UserDetails>() {
            @Override
            public UserDetails mapRow(ResultSet resultSet, int i) throws SQLException {
                if (resultSet.wasNull()) {
                    throw new UsernameNotFoundException("member " + username + " is not exists");
                }
                long id = resultSet.getLong("id");
                String password = resultSet.getString("password");
                int status = resultSet.getInt("status");
                return new User(
                        String.valueOf(id),
                        password,
                        status == 1,
                        true,
                        true,
                        true,
                        Arrays.asList(new SimpleGrantedAuthority("ROLE_USER"))
                );
            }
        }, username, username);

    }

    private UserDetails loadAdminUserByUsername(String username) {
        return jdbcTemplate.queryForObject(QUERY_ADMIN_SQL, new RowMapper<User>() {

            @Override
            public User mapRow(ResultSet resultSet, int i) throws SQLException {
                if (resultSet.wasNull()) {
                    throw new UsernameNotFoundException("user: " + username + " is not exists");
                }
                Long id = resultSet.getLong("id");
                String password = resultSet.getString("password");
                int status = resultSet.getInt("status");
                User user = new User(String.valueOf(id),
                        password,
                        status == 1,
                        true,
                        true,
                        true,
                        getUserPermissions(id));
                return user;
            }

        }, username);
    }

    private Collection<? extends GrantedAuthority> getUserPermissions(Long id) {

        String code = jdbcTemplate.queryForObject(QUERY_ROLE_CODE_SQL, String.class, id);
        List<String> permissions = null;
        if (ADMIN_ROLE_CODE.equals(code)) {
            permissions = jdbcTemplate.queryForList(QUERY_ALL_PERMISSIONS, String.class);
        } else {
            permissions = jdbcTemplate.queryForList(QUERY_PERMISSION_SQL, String.class, id);
        }

        if (permissions == null || permissions.isEmpty()) {
            return Collections.EMPTY_LIST;
        }

        return permissions
                .stream()
                .distinct()
                .map(perm -> new SimpleGrantedAuthority(perm))
                .collect(Collectors.toSet());
    }
}
