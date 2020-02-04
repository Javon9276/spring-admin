package cn.javon.api.sys.vo;

import cn.javon.core.vo.BaseVo;
import com.alibaba.fastjson.annotation.JSONField;
import lombok.ToString;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.io.Serializable;
import java.util.Collection;
import java.util.List;

/**
 * 用户Vo
 *
 * @author Javon
 */
@ToString
public class UserVo extends BaseVo implements UserDetails, Serializable {

    /**
     * 用户名
     */
    private String username;

    /**
     * 密码
     */
    private String password;

    /**
     * 昵称
     */
    private String name;

    /**
     * 邮箱
     */
    private String email;

    /**
     * 头像
     */
    private String avatar;

    /**
     * 角色列表
     */
    private List<RoleVo> roles;

    /**
     * 权限列表
     */
    private List<PermissionVo> permissions;

    /**
     * 角色Ids
     */
    @JSONField(serialize = false)
    private List<Long> roleIds;

    @JSONField(serialize = false)
    private List<SimpleGrantedAuthority> authoritys;

    @Override
    public String getUsername() {
        return this.username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    @Override
    public String getPassword() {
        return this.password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public List<RoleVo> getRoles() {
        return roles;
    }

    public void setRoles(List<RoleVo> roles) {
        this.roles = roles;
    }

    public List<PermissionVo> getPermissions() {
        return permissions;
    }

    public void setPermissions(List<PermissionVo> permissions) {
        this.permissions = permissions;
    }

    public List<Long> getRoleIds() {
        return roleIds;
    }

    public void setRoleIds(List<Long> roleIds) {
        this.roleIds = roleIds;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return authoritys;
    }


    public void setAuthoritys(List<SimpleGrantedAuthority> authoritys) {
        this.authoritys = authoritys;
    }

    @JSONField(serialize = false)
    private boolean accountNonExpired;
    @JSONField(serialize = false)
    private boolean accountNonLocked;
    @JSONField(serialize = false)
    private boolean credentialsNonExpired;
    @JSONField(serialize = false)
    private boolean enabled;

    @Override
    public boolean isAccountNonExpired() {
        // TODO Auto-generated method stub
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        // TODO Auto-generated method stub
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        // TODO Auto-generated method stub
        return true;
    }

    @Override
    public boolean isEnabled() {
        // TODO Auto-generated method stub
        return true;
    }

}
