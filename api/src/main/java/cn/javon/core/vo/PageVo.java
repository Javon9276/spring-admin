package cn.javon.core.vo;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * 分页Vo
 *
 * @author Javon
 */
@Getter
@Setter
@ToString
public class PageVo {

    private Integer page;

    private Integer size;

    private String sort = "id";

    private String order = "ASC";

}
