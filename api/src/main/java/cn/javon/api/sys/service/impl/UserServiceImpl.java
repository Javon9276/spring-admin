package cn.javon.api.sys.service.impl;

import cn.javon.api.core.context.UserContext;
import cn.javon.api.sys.dao.RoleRepository;
import cn.javon.api.sys.dao.UserRepository;
import cn.javon.api.sys.entity.Role;
import cn.javon.api.sys.entity.User;
import cn.javon.api.sys.service.PermissionService;
import cn.javon.api.sys.service.UserService;
import cn.javon.api.sys.trans.UserTransfer;
import cn.javon.api.sys.vo.PermissionVo;
import cn.javon.api.sys.vo.RoleVo;
import cn.javon.api.sys.vo.UserSearchVo;
import cn.javon.api.sys.vo.UserVo;
import cn.javon.core.Constant;
import cn.javon.core.log.AuditLog;
import cn.javon.core.utils.StringUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

/**
 * 用户业务处理接口实现类
 *
 * @author Javon
 */
@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private PermissionService permissionService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    final UserTransfer userTransfer = new UserTransfer();

    @Override
    public UserVo findByUsername(final String username) {
        final User user = this.userRepository.findByUsername(username);
        if (user == null) {
            throw new UsernameNotFoundException("not found this username");
        }
        final UserVo userVo = this.userTransfer.toVo(user);
        boolean isAdmin = false;
        final List<PermissionVo> permissions = new ArrayList<PermissionVo>();
        for (RoleVo roleVo : userVo.getRoles()) {
            if ("ROLE_ADMIN".equals(roleVo.getCode())) {
                isAdmin = true;
            }
            permissions.addAll(roleVo.getPermissions());
        }
        if (isAdmin) {
            // 超级管理员所有权限都有
            userVo.setPermissions(this.permissionService.findAll());
        } else {
            userVo.setPermissions(permissions);
        }
        return userVo;
    }

    @Override
    public boolean verifyPassword(final String rawPassword, final String encodedPassword) {
        return this.passwordEncoder.matches(rawPassword, encodedPassword);
    }

    @AuditLog(operate = "分页查询用户列表")
    @Override
    public Page<UserVo> findPage(final UserSearchVo searchVo) {
        Pageable pageable = PageRequest.of(searchVo.getPage() - 1, searchVo.getSize(),
                Sort.Direction.fromString(searchVo.getOrder()), searchVo.getSort());
        Page<User> users = this.userRepository.findAll(new Specification<User>() {
            @Override
            public Predicate toPredicate(Root<User> root, CriteriaQuery<?> query, CriteriaBuilder criteriaBuilder) {
                List<Predicate> list = new ArrayList<Predicate>();
                if (StringUtil.isNotEmpty(searchVo.getKeyword())) {
                    List<Predicate> keywords = new ArrayList<Predicate>();
                    keywords.add(criteriaBuilder.like(root.get("username").as(String.class), "%" + searchVo.getKeyword() + "%"));
                    keywords.add(criteriaBuilder.like(root.get("name").as(String.class), "%" + searchVo.getKeyword() + "%"));
                    Predicate[] keywordPredicate = new Predicate[keywords.size()];
                    list.add(criteriaBuilder.or(keywords.toArray(keywordPredicate)));
                }
                Predicate[] p = new Predicate[list.size()];
                return criteriaBuilder.and(list.toArray(p));
            }
        }, pageable);
        return users.map(this::convertToUserVo);
    }

    private UserVo convertToUserVo(final User user) {
        return this.userTransfer.toVo(user);
    }

    @AuditLog(operate = "用户查询（根据ID查询用户信息）")
    @Override
    public UserVo findById(final Long id) {
        return this.userTransfer.toVo(this.userRepository.getOne(id));
    }

    @AuditLog(operate = "用户新增")
    @Override
    public UserVo add(final UserVo userVo) {
        List<Role> roles = roleRepository.findAllById(userVo.getRoleIds());
        User user = this.userTransfer.toPo(userVo);
        user.setPassword(this.passwordEncoder.encode(Constant.DEFAULT_PASSWORD));
        user.setCreater(UserContext.getUserId());
        user.setRoles(new HashSet<Role>(roles));
        user = this.userRepository.save(user);
        return this.userTransfer.toVo(user);
    }

    @AuditLog(operate = "用户修改")
    @Override
    public UserVo update(UserVo userVo) {
        User user = this.userTransfer.toPo(userVo);
        List<Role> roles = this.roleRepository.findAllById(userVo.getRoleIds());
        user.setRoles(new HashSet<Role>(roles));
        user.setUpdater(UserContext.getUserId());
        user = this.userRepository.save(user);
        user.setVersion(user.getVersion() + 1);
        return this.userTransfer.toVo(user);
    }

    @AuditLog(operate = "角色删除（根据ID进行硬删除）")
    @Override
    public void deleteById(Long id) {
        this.userRepository.deleteById(id);
    }
}
