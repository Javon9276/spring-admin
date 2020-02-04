package cn.javon.api.sys.entity;

import cn.javon.core.model.BaseEntity;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.*;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;
import java.io.Serializable;
import java.util.Set;

/**
 * 角色实体类
 *
 * @author Javon
 */
@Entity
@Getter
@Setter
@ToString
@Table(name = "sys_role")
@NamedEntityGraph(name = "Role.Graph", attributeNodes = {@NamedAttributeNode("permissions")})
public class Role extends BaseEntity implements Serializable {

    /**
     * 角色编码
     */
    @NotEmpty(message = "角色编码不能为空")
    @Size(min = 6, message = "角色编码长度不能小于6")
    @Column(name = "code", length = 64)
    private String code;

    /**
     * 角色描述
     */
    @NotEmpty(message = "角色描述不能为空")
    @Column(name = "description")
    private String description;

    /**
     * 权限列表
     */
    @ManyToMany(cascade = {CascadeType.MERGE, CascadeType.REFRESH}, fetch = FetchType.EAGER)
    @JoinTable(name = "sys_role_permission", joinColumns = {@JoinColumn(name = "role_id")}, inverseJoinColumns = {
            @JoinColumn(name = "permission_id")})
    private Set<Permission> permissions;

}
