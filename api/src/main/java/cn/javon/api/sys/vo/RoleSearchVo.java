package cn.javon.api.sys.vo;

import cn.javon.core.vo.PageVo;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.io.Serializable;

/**
 * 角色查询Vo
 *
 * @author Javon
 */
@Getter
@Setter
@ToString
public class RoleSearchVo extends PageVo {

    /**
     * 查询关键字
     */
    private String keyword;

}
