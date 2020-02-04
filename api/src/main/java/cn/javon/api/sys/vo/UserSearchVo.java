package cn.javon.api.sys.vo;

import cn.javon.core.vo.PageVo;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.io.Serializable;

/**
 * 用户查询Vo
 *
 * @author Javon
 */
@Getter
@Setter
@ToString
public class UserSearchVo extends PageVo {

	/**
	 * 查询关键字
	 */
	private String keyword;

}
