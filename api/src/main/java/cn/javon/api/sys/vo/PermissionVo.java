package cn.javon.api.sys.vo;

import cn.javon.core.model.TreeNode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.io.Serializable;

/**
 * 权限Vo
 *
 * @author Javon
 */
@Getter
@Setter
@ToString
public class PermissionVo extends TreeNode<PermissionVo> implements Serializable {

    /**
     * 权限模块名称
     */
    private String name;

    /**
     * 权限编码
     */
    private String code;

    /**
     * 权限父节点，一级则为0
     */
    private Long parentId;

}
