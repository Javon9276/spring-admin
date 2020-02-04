package cn.javon.api.sys.vo;

import cn.javon.core.vo.BaseVo;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.io.Serializable;
import java.util.List;

/**
 * 角色Vo
 *
 * @author Javon
 */
@Getter
@Setter
@ToString
public class RoleVo extends BaseVo implements Serializable {

    /**
     * 角色编码
     */
    private String code;

    /**
     * 角色描述
     */
    private String description;

    /**
     * 权限列表
     */
    private List<PermissionVo> permissions;

}
