package cn.javon.api.sys.entity;

import cn.javon.core.model.BaseEntity;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.*;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;
import java.io.Serializable;
import java.util.Set;

/**
 * 用户实体类
 *
 * @author Javon
 */
@Entity
@Getter
@Setter
@ToString
@Table(name = "sys_user")
@NamedEntityGraph(name = "User.Graph", attributeNodes = {
        @NamedAttributeNode(value = "roles", subgraph = "Roles.Permission.Graph")}, subgraphs = {
        @NamedSubgraph(name = "Roles.Permission.Graph", attributeNodes = {
                @NamedAttributeNode(value = "permissions")})})
public class User extends BaseEntity implements Serializable {

    /**
     * 用户名
     */
    @NotEmpty(message = "用户名不能为空")
    @Size(min = 3, message = "用户名长度不能小于3")
    @Column(name = "username", length = 64, nullable = false)
    private String username;

    /**
     * 密码
     */
    @NotEmpty(message = "密码不能为空")
    @Size(min = 6, message = "密码长度不能小于6")
    @Column(name = "password", length = 64, nullable = false)
    private String password;

    /**
     * 昵称
     */
    @NotEmpty(message = "昵称不能为空")
    @Size(min = 3, message = "昵称长度不能小于3")
    @Column(name = "name", length = 64, nullable = false)
    private String name;

    /**
     * 邮箱
     */
    @NotEmpty(message = "邮箱不能为空")
    @Email
    @Size(max = 128, message = "邮箱长度不能超过128个字符")
    @Column(name = "email", length = 128)
    private String email;

    /**
     * 头像
     */
    @Column(name = "avatar")
    private String avatar;

    /**
     * 角色列表
     */
    @ManyToMany(cascade = {CascadeType.MERGE, CascadeType.REFRESH}, fetch = FetchType.EAGER)
    @JoinTable(name = "sys_user_role", joinColumns = {@JoinColumn(name = "user_id")}, inverseJoinColumns = {
            @JoinColumn(name = "role_id")})
    private Set<Role> roles;

}
