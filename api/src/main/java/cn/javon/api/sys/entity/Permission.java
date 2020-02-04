package cn.javon.api.sys.entity;

import cn.javon.core.model.BaseEntity;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;
import java.io.Serializable;

/**
 * 权限实体类
 *
 * @author Javon
 */
@Entity
@Getter
@Setter
@ToString
@Table(name = "sys_permission")
public class Permission extends BaseEntity implements Serializable {

    /**
     * 权限模块名称
     */
    @NotEmpty(message = "权限名称不能为空")
    @Size(max = 32, message = "权限名称长度不能大于32个字符")
    @Column(name = "name", length = 32, nullable = true)
    private String name;

    /**
     * 权限的代码/通配符,对应代码中@hasAuthority(xx)
     */
    @NotEmpty(message = "权限代码不能为空")
    @Size(max = 64, message = "权限模块名称长度不能大于64个字符")
    @Column(name = "code", length = 64, nullable = true)
    private String code;

    /**
     * 父节点
     */
    @Column(name = "parent_id", nullable = true)
    private Long parentId;

}

