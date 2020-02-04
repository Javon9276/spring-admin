package cn.javon.api;

import cn.javon.api.sys.dao.PermissionRepository;
import cn.javon.api.sys.dao.RoleRepository;
import cn.javon.api.sys.dao.UserRepository;
import cn.javon.api.sys.entity.Permission;
import cn.javon.api.sys.entity.Role;
import cn.javon.api.sys.entity.User;
import cn.javon.core.Constant;
import lombok.extern.slf4j.Slf4j;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.*;


@ComponentScan(basePackages = "cn.javon")
@RunWith(SpringRunner.class)
@SpringBootTest
@Slf4j
public class AdminGenerate {

    @Autowired
    private PermissionRepository permissionRepository;

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    private final List<Map<String, Object>> permissionLists = new ArrayList<Map<String, Object>>();

    @Before
    public void initData() {
        final Map<String, Object> sysPermissionMaps = new HashMap<String, Object>();
        sysPermissionMaps.put("module", "sys");
        sysPermissionMaps.put("name", "系统管理");
        sysPermissionMaps.put("subModule", new String[][]{{"user", "role", "permission"}, {"用户", "角色", "资源权限"}});
        permissionLists.add(sysPermissionMaps);
    }

    @Test
    public void generateAdmin() {
        final Set<Permission> permissions = new HashSet<Permission>();


        for (Map<String, Object> permissionMaps : permissionLists) {
            final String code = String.valueOf(permissionMaps.get("module"));
            final String name = String.valueOf(permissionMaps.get("name"));
            final String[][] subModule = (String[][]) permissionMaps.get("subModule");
            if (log.isInfoEnabled()) {
                StringBuilder sb = new StringBuilder();
                sb.append("code:");
                sb.append(code);
                sb.append(", name:");
                sb.append(name);
                sb.append(", subModule:");
                sb.append(subModule);
                log.info(sb.toString());
            }

            Permission modulePermission = new Permission();
            modulePermission.setName(name);
            modulePermission.setCode(code);
            modulePermission.setParentId(Long.valueOf(0));
            modulePermission = permissionRepository.save(modulePermission);
            permissions.add(modulePermission);

            for (int i = 0; i < subModule[0].length; i++) {

                Permission subModulePermission = new Permission();
                subModulePermission.setName(subModule[1][i]);
                subModulePermission.setCode(code + ":" + subModule[0][i]);
                subModulePermission.setParentId(modulePermission.getId());
                subModulePermission = permissionRepository.save(subModulePermission);
                permissions.add(subModulePermission);
                Long parentId = subModulePermission.getId();

                Permission selectPermission = new Permission();
                selectPermission.setName(subModule[1][i] + "选择器");
                selectPermission.setCode(code + ":" + subModule[0][i] + ":select");
                selectPermission.setParentId(parentId);
                selectPermission = permissionRepository.save(selectPermission);
                permissions.add(selectPermission);

                Permission listPermission = new Permission();
                listPermission.setName(subModule[1][i] + "列表");
                listPermission.setCode(code + ":" + subModule[0][i] + ":list");
                listPermission.setParentId(parentId);
                listPermission = permissionRepository.save(listPermission);
                permissions.add(listPermission);

                Permission infoPermission = new Permission();
                infoPermission.setName(subModule[1][i] + "详情");
                infoPermission.setCode(code + ":" + subModule[0][i] + ":info");
                infoPermission.setParentId(parentId);
                infoPermission = permissionRepository.save(infoPermission);
                permissions.add(infoPermission);

                Permission addPermission = new Permission();
                addPermission.setName(subModule[1][i] + "新增");
                addPermission.setCode(code + ":" + subModule[0][i] + ":add");
                addPermission.setParentId(parentId);
                addPermission = permissionRepository.save(addPermission);
                permissions.add(addPermission);

                Permission updatePermission = new Permission();
                updatePermission.setName(subModule[1][i] + "修改");
                updatePermission.setCode(code + ":" + subModule[0][i] + ":update");
                updatePermission.setParentId(parentId);
                updatePermission = permissionRepository.save(updatePermission);
                permissions.add(updatePermission);

                Permission deletePermission = new Permission();
                deletePermission.setName(subModule[1][i] + "删除");
                deletePermission.setCode(code + ":" + subModule[0][i] + ":delete");
                deletePermission.setParentId(parentId);
                deletePermission = permissionRepository.save(deletePermission);
                permissions.add(deletePermission);
            }
        }

        final Set<Role> roles = new HashSet<Role>();

        Role role = new Role();
        role.setCode("ROLE_ADMIN");
        role.setDescription("超级管理员");
        role.setPermissions(permissions);

        role = roleRepository.save(role);

        roles.add(role);

        final User user = new User();
        user.setUsername("admin");
        user.setPassword(this.passwordEncoder.encode(Constant.DEFAULT_PASSWORD));
        user.setName("超级管理员");
        user.setEmail("1@qq.com");
        user.setRoles(roles);

        userRepository.save(user);
    }

}
