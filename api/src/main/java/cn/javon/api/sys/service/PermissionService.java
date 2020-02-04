package cn.javon.api.sys.service;

import cn.javon.api.sys.vo.PermissionVo;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 权限业务处理接口
 *
 * @author Javon
 */
@Service
public interface PermissionService {

    /**
     * 查询树形权限列表
     *
     * @return 树形ListVo
     */
    List<PermissionVo> findTree();

    /**
     * 查询所有权限列表
     *
     * @return 权限列表
     */
    List<PermissionVo> findAll();

    /**
     * 权限查询（根据ID查询权限信息）
     *
     * @param id 权限ID
     * @return 查询对象
     */
    PermissionVo findById(final Long id);

    /**
     * 权限新增
     *
     * @param permissionVo 权限Vo
     * @return 新增对象
     */
    PermissionVo add(final PermissionVo permissionVo);

    /**
     * 权限修改
     *
     * @param permissionVo 权限Vo
     * @return 修改对象
     */
    PermissionVo update(final PermissionVo permissionVo);

    /**
     * 权限删除（根据ID进行硬删除）
     *
     * @param id 权限ID
     */
    void deleteById(final Long id);

}
