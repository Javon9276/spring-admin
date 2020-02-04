package cn.javon.api.sys.service.impl;

import cn.javon.api.core.context.UserContext;
import cn.javon.api.sys.dao.RoleRepository;
import cn.javon.api.sys.entity.Role;
import cn.javon.api.sys.service.RoleService;
import cn.javon.api.sys.trans.RoleTransfer;
import cn.javon.api.sys.vo.RoleSearchVo;
import cn.javon.api.sys.vo.RoleVo;
import cn.javon.core.Constant;
import cn.javon.core.log.AuditLog;
import cn.javon.core.utils.StringUtil;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.util.ArrayList;
import java.util.List;

/**
 * 角色业务处理接口实现类
 *
 * @author Javon
 */
@Service
@Transactional(rollbackFor = Exception.class)
public class RoleServiceImpl implements RoleService {

    @Autowired
    private RoleRepository roleRepository;

    final RoleTransfer roleTransfer = new RoleTransfer();

    @AuditLog(operate = "查询所有角色列表")
    @Override
    public List<RoleVo> findAll() {
        return this.roleTransfer.toVo(this.roleRepository.findAll());
    }

    @AuditLog(operate = "分页查询角色列表")
    @Override
    public Page<RoleVo> findPage(final RoleSearchVo searchVo) {
        Pageable pageable = PageRequest.of(searchVo.getPage() - 1, searchVo.getSize(),
                Sort.Direction.fromString(searchVo.getOrder()), searchVo.getSort());
        Page<Role> roles = this.roleRepository.findAll(new Specification<Role>() {
            @Override
            public Predicate toPredicate(Root<Role> root, CriteriaQuery<?> query, CriteriaBuilder criteriaBuilder) {
                final List<Predicate> list = new ArrayList<Predicate>();
                if (StringUtil.isNotEmpty(searchVo.getKeyword())) {
                    List<Predicate> keywords = new ArrayList<Predicate>();
                    keywords.add(criteriaBuilder.like(root.get("code").as(String.class), "%" + searchVo.getKeyword() + "%"));
                    keywords.add(criteriaBuilder.like(root.get("description").as(String.class), "%" + searchVo.getKeyword() + "%"));
                    Predicate[] keywordPredicate = new Predicate[keywords.size()];
                    list.add(criteriaBuilder.or(keywords.toArray(keywordPredicate)));
                }
                list.add(criteriaBuilder.equal(root.get("isDelete"), Constant.NOT_DELETE));
                Predicate[] p = new Predicate[list.size()];
                return criteriaBuilder.and(list.toArray(p));
            }
        }, pageable);
        return roles.map(this::convertToRoleVo);
    }

    private RoleVo convertToRoleVo(final Role role) {
        return this.roleTransfer.toVo(role);
    }

    @AuditLog(operate = "角色查询（根据ID查询角色信息）")
    @Override
    public RoleVo findById(final Long id) {
        return this.roleTransfer.toVo(this.roleRepository.getOne(id));
    }

    @AuditLog(operate = "角色新增")
    @Override
    public RoleVo add(final RoleVo roleVo) {
        Role role = this.roleTransfer.toPo(roleVo);
        role.setCreater(UserContext.getUserId());
        role = this.roleRepository.save(role);
        return this.roleTransfer.toVo(role);
    }

    @AuditLog(operate = "角色修改")
    @Override
    public RoleVo update(final RoleVo roleVo) {
        Role role = roleTransfer.toPo(roleVo);
        role.setUpdater(UserContext.getUserId());
        role = this.roleRepository.save(role);
        role.setVersion(role.getVersion() + 1);
        return this.roleTransfer.toVo(role);
    }

    @AuditLog(operate = "角色删除（根据ID进行硬删除）")
    @Override
    public void deleteById(Long id) {
        this.roleRepository.deleteById(id);
    }

}
