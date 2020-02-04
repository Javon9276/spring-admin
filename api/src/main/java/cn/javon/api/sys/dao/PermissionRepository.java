package cn.javon.api.sys.dao;

import cn.javon.api.sys.entity.Permission;
import cn.javon.core.repository.BaseRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

/**
 * 权限数据库操作类
 *
 * @author Javon
 */
@Repository
public interface PermissionRepository extends BaseRepository<Permission, Long>, JpaSpecificationExecutor<Permission> {
}
