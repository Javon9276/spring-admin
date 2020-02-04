package cn.javon.api.sys.controller;

import cn.javon.api.sys.service.RoleService;
import cn.javon.api.sys.vo.RoleSearchVo;
import cn.javon.api.sys.vo.RoleVo;
import cn.javon.core.response.Result;
import cn.javon.core.response.ResultGenerator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 角色管理控制器
 *
 * @author Javon
 */
@RestController
@RequestMapping("/role")
public class RoleController {

    @Autowired
    private RoleService roleService;

    /**
     * 角色选择列表
     *
     * @return 角色列表
     */
    @PreAuthorize("hasAuthority('sys:role:select')")
    @GetMapping("/select")
    public Result select() {
        List<RoleVo> list = this.roleService.findAll();
        return ResultGenerator.genOkResult(list);
    }

    /**
     * 角色列表
     * @param searchVo 查询Vo
     * @return 角色列表
     */
    @PreAuthorize("hasAuthority('sys:role:list')")
    @PostMapping("/list")
    public Result list(@RequestBody final RoleSearchVo searchVo) {
        final Page<RoleVo> list = this.roleService.findPage(searchVo);
        return ResultGenerator.genOkResult(list);
    }

    /**
     * 角色查询（根据ID查询角色信息）
     *
     * @param id 角色id
     * @return 查询对象
     */
    @PreAuthorize("hasAuthority('sys:role:info')")
    @GetMapping("/{id}")
    public Result info(@PathVariable final Long id) {
        final RoleVo roleVo = this.roleService.findById(id);
        return ResultGenerator.genOkResult(roleVo);
    }

    /**
     * 角色新增
     *
     * @param roleVo 角色Vo
     * @return 新增对象
     */
    @PreAuthorize("hasAuthority('sys:role:add')")
    @PostMapping("/add")
    public Result add(@RequestBody RoleVo roleVo) {
        roleVo = this.roleService.add(roleVo);
        return ResultGenerator.genOkResult(roleVo);
    }

    /**
     * 角色修改
     *
     * @param roleVo 角色Vo
     * @return 修改对象
     */
    @PreAuthorize("hasAuthority('sys:role:update')")
    @PostMapping("/update")
    public Result update(@RequestBody RoleVo roleVo) {
        roleVo = this.roleService.update(roleVo);
        return ResultGenerator.genOkResult(roleVo);
    }

    /**
     * 角色删除（根据ID进行硬删除）
     *
     * @param id 角色ID
     * @return
     */
    @PreAuthorize("hasAuthority('sys:role:delete')")
    @GetMapping("/delete/{id}")
    public Result delete(@PathVariable final Long id) {
        this.roleService.deleteById(id);
        return ResultGenerator.genOkResult();
    }

}
