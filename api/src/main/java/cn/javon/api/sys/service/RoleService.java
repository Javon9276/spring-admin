package cn.javon.api.sys.service;

import cn.javon.api.sys.vo.RoleSearchVo;
import cn.javon.api.sys.vo.RoleVo;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 角色业务处理接口
 *
 * @author Javon
 */
@Service
public interface RoleService {

    /**
     * 查询所有角色列表
     *
     * @return 角色列表
     */
    List<RoleVo> findAll();

    /**
     * 分页查询角色列表
     *
     * @param searchVo 角色查询Vo
     * @return 角色列表
     */
    Page<RoleVo> findPage(final RoleSearchVo searchVo);

    /**
     * 角色查询（根据ID查询角色信息）
     *
     * @param id 角色ID
     * @return 查询对象
     */
    RoleVo findById(final Long id);

    /**
     * 角色新增
     *
     * @param roleVo 角色Vo
     * @return 新增对象
     */
    RoleVo add(final RoleVo roleVo);

    /**
     * 角色修改
     *
     * @param roleVo 角色Vo
     * @return 修改对象
     */
    RoleVo update(final RoleVo roleVo);

    /**
     * 角色删除（根据ID进行硬删除）
     *
     * @param id 权限ID
     */
    void deleteById(final Long id);

}
