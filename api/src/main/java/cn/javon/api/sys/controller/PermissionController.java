package cn.javon.api.sys.controller;

import cn.javon.api.sys.service.PermissionService;
import cn.javon.api.sys.vo.PermissionVo;
import cn.javon.core.response.Result;
import cn.javon.core.response.ResultGenerator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 权限管理控制器
 *
 * @author Javon
 */
@RestController
@RequestMapping("/permission")
public class PermissionController {

    @Autowired
    private PermissionService permissionService;

    /**
     * 树形权限列表
     *
     * @return 树形权限列表
     */
    @PreAuthorize("hasAuthority('sys:permission:list')")
    @PostMapping("/list")
    public Result list() {
        final List<PermissionVo> list = this.permissionService.findTree();
        return ResultGenerator.genOkResult(list);
    }

    /**
     * 权限查询（根据ID查询权限信息）
     *
     * @param id 权限id
     * @return 查询对象
     */
    @PreAuthorize("hasAuthority('sys:permission:info')")
    @GetMapping("/{id}")
    public Result info(@PathVariable final Long id) {
        final PermissionVo permissionVo = this.permissionService.findById(id);
        return ResultGenerator.genOkResult(permissionVo);
    }

    /**
     * 权限新增
     *
     * @param permissionVo 权限Vo
     * @return 新增对象
     */
    @PreAuthorize("hasAuthority('sys:permission:add')")
    @PostMapping("/add")
    public Result add(@RequestBody PermissionVo permissionVo) {
        permissionVo = this.permissionService.add(permissionVo);
        return ResultGenerator.genOkResult(permissionVo);
    }

    /**
     * 权限修改
     *
     * @param permissionVo 权限Vo
     * @return 修改对象
     */
    @PreAuthorize("hasAuthority('sys:permission:update')")
    @PostMapping("/update")
    public Result update(@RequestBody PermissionVo permissionVo) {
        permissionVo = this.permissionService.update(permissionVo);
        return ResultGenerator.genOkResult(permissionVo);
    }

    /**
     * 权限删除（根据ID进行硬删除）
     *
     * @param id 权限ID
     * @return
     */
    @PreAuthorize("hasAuthority('sys:permission:delete')")
    @GetMapping("/delete/{id}")
    public Result delete(@PathVariable final Long id) {
        this.permissionService.deleteById(id);
        return ResultGenerator.genOkResult();
    }

}
