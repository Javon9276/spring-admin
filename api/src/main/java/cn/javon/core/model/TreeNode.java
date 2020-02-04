package cn.javon.core.model;

import cn.javon.core.vo.BaseVo;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.List;

/**
 * 树节点类
 *
 * @author Javon
 */
@Setter
@Getter
@ToString
public class TreeNode<T> extends BaseVo {

    /**
     * 父节点
     */
    private Long parentId;

    /**
     * 子列表
     */
    private List<T> children;

}
