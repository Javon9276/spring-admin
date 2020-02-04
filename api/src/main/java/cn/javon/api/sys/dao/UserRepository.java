package cn.javon.api.sys.dao;

import cn.javon.api.sys.entity.User;
import cn.javon.core.repository.BaseRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

/**
 * 用户数据库操作类
 *
 * @author Javon
 */
@Repository
public interface UserRepository extends BaseRepository<User, Long>, JpaSpecificationExecutor<User> {

    /**
     * 根据用户名查询用户信息
     *
     * @param username 用户名
     * @return 用户信息
     */
    @EntityGraph(value = "User.Graph", type = EntityGraph.EntityGraphType.FETCH)
    User findByUsername(String username);

    /**
     * 用户列表查询
     *
     * @param var1
     * @param var2
     * @return 用户列表分页
     */
    @Override
    @EntityGraph(value = "User.Graph", type = EntityGraph.EntityGraphType.FETCH)
    Page<User> findAll(Specification<User> var1, Pageable var2);

}
