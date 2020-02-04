package cn.javon.api.sys.service.impl;

import cn.javon.api.core.context.UserContext;
import cn.javon.api.sys.dao.PermissionRepository;
import cn.javon.api.sys.entity.Permission;
import cn.javon.api.sys.service.PermissionService;
import cn.javon.api.sys.trans.PermissionTransfer;
import cn.javon.api.sys.vo.PermissionVo;
import cn.javon.core.log.AuditLog;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 权限业务处理接口实现类
 *
 * @author Javon
 */
@Service
@Transactional(rollbackFor = Exception.class)
public class PermissionServiceImpl implements PermissionService {

    @Autowired
    private PermissionRepository permissionRepository;

    private final PermissionTransfer permissionTransfer = new PermissionTransfer();

    @AuditLog(operate = "查询树形权限列表")
    @Override
    public List<PermissionVo> findTree() {
        List<Permission> permissions = this.permissionRepository.findAll();
        List<PermissionVo> permissionVos = this.permissionTransfer.toVo(permissions);
        return this.getTree(permissionVos);
    }

    @AuditLog(operate = "查询全部权限列表")
    @Override
    public List<PermissionVo> findAll() {
        List<Permission> permissions = this.permissionRepository.findAll();
        return this.permissionTransfer.toVo(permissions);
    }

    @AuditLog(operate = "权限查询（根据ID查询权限信息）")
    @Override
    public PermissionVo findById(final Long id) {
        return this.permissionTransfer.toVo(this.permissionRepository.getOne(id));
    }

    @AuditLog(operate = "权限新增")
    @Override
    public PermissionVo add(final PermissionVo permissionVo) {
        Permission permission = this.permissionTransfer.toPo(permissionVo);
        permission.setCreater(UserContext.getUserId());
        permission = this.permissionRepository.save(permission);
        return this.permissionTransfer.toVo(permission);
    }

    @AuditLog(operate = "权限修改")
    @Override
    public PermissionVo update(final PermissionVo permissionVo) {
        Permission permission = permissionTransfer.toPo(permissionVo);
        permission.setUpdater(UserContext.getUserId());
        permission = this.permissionRepository.save(permission);
        permission.setVersion(permission.getVersion() + 1);
        return this.permissionTransfer.toVo(permission);
    }

    @AuditLog(operate = "权限删除")
    @Override
    public void deleteById(Long id) {
        this.permissionRepository.deleteById(id);
    }

    /**
     * 树形进行遍历
     *
     * @param list 列表
     * @return
     */
    private List<PermissionVo> getTree(List<PermissionVo> list) {
        Map<Long, PermissionVo> dtoMap = new HashMap<>();
        for (PermissionVo node : list) {
            dtoMap.put(node.getId(), node);
        }
        List<PermissionVo> resultList = new ArrayList<PermissionVo>();
        for (Map.Entry<Long, PermissionVo> entry : dtoMap.entrySet()) {
            PermissionVo node = entry.getValue();
            if (node.getParentId() == 0L) {
                // 如果是顶层节点，直接添加到结果集合中
                resultList.add(node);
            } else {
                // 如果不是顶层节点，找其父节点，并且添加到父节点的子节点集合中
                if (dtoMap.get(Long.valueOf(node.getParentId())) != null) {
                    List<PermissionVo> children = dtoMap.get(Long.valueOf(node.getParentId())).getChildren();
                    if (children == null) {
                        children = new ArrayList<PermissionVo>();
                    }
                    children.add(node);
                    dtoMap.get(Long.valueOf(node.getParentId())).setChildren(children);
                }
            }
        }
        return resultList;
    }

}
