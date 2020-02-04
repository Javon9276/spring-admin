package cn.javon.core.vo;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.Date;

/**
 * 基础Vo类
 *
 * @author Javon
 */
@Getter
@Setter
@ToString
public class BaseVo {

    /**
     * 基础ID
     */
    private Long id;

    /**
     * 基础状态
     */
    //	@JSONField(serialize = false)
    private Integer status = 0;

    /**
     * 版本号
     */
    //	@JSONField(serialize = false)
    private Integer version;

    /**
     * 创建时间
     */
    private Date createTime;

}
