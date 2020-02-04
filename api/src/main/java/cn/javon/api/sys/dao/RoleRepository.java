package cn.javon.api.sys.dao;

import cn.javon.api.sys.entity.Role;
import cn.javon.core.repository.BaseRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

/**
 * 角色数据库操作类
 *
 * @author Javon
 */
@Repository
public interface RoleRepository extends BaseRepository<Role, Long>, JpaSpecificationExecutor<Role> {

    /**
     * 查询全部角色
     *
     * @param var1
     * @param var2
     * @return Role分页
     */
    @Override
    @EntityGraph(value = "Role.Graph", type = EntityGraph.EntityGraphType.FETCH)
    Page<Role> findAll(Specification<Role> var1, Pageable var2);

}
