package cn.javon.api.sys.service.impl;

import cn.javon.api.sys.service.UserService;
import cn.javon.api.sys.vo.PermissionVo;
import cn.javon.api.sys.vo.UserVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 用户验证Service
 *
 * @author Javon
 */
@Service
@Transactional(rollbackFor = Exception.class)
public class UserDetailsServiceImpl implements UserDetailsService {

    @Autowired
    private UserService userService;

    @Override
    public UserVo loadUserByUsername(final String username) {
        final UserVo user = this.userService.findByUsername(username);
        List<SimpleGrantedAuthority> simpleGrantedAuthorities = new ArrayList<SimpleGrantedAuthority>();
        List<String> permissionCodes = new ArrayList<String>();
        for (PermissionVo permission : user.getPermissions()) {
            permissionCodes.add(permission.getCode());
        }
        // 权限
        final List<SimpleGrantedAuthority> authorities = permissionCodes.stream().map(SimpleGrantedAuthority::new)
                .collect(Collectors.toList());
        // 角色
        authorities.addAll(simpleGrantedAuthorities);
        user.setAuthoritys(authorities);
        return user;
    }
}
