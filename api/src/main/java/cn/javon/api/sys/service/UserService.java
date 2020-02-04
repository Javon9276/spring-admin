package cn.javon.api.sys.service;

import cn.javon.api.sys.vo.UserSearchVo;
import cn.javon.api.sys.vo.UserVo;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

/**
 * 用户业务处理接口
 *
 * @author Javon
 */
@Service
public interface UserService {

    /**
     * 查询用户信息（根据帐号查询用户信息）
     *
     * @param username 用户帐号
     * @return 用户信息
     */
    UserVo findByUsername(final String username);

    /**
     * 密码校验
     *
     * @param password  输入密码
     * @param password1 数据库密码
     * @return 是否一致
     */
    boolean verifyPassword(final String password, final String password1);


    /**
     * 分页查询用户列表
     *
     * @param searchVo 用户查询Vo
     * @return 用户列表
     */
    Page<UserVo> findPage(final UserSearchVo searchVo);

    /**
     * 用户查询（根据ID查询用户信息）
     *
     * @param id 用户ID
     * @return 查询对象
     */
    UserVo findById(final Long id);

    /**
     * 用户新增
     *
     * @param userVo 用户Vo
     * @return 新增对象
     */
    UserVo add(final UserVo userVo);

    /**
     * 用户修改
     *
     * @param userVo 用户Vo
     * @return 修改对象
     */
    UserVo update(final UserVo userVo);

    /**
     * 用户删除（根据ID进行硬删除）
     *
     * @param id 权限ID
     */
    void deleteById(final Long id);

}
