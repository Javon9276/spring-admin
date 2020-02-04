package cn.javon.api.sys.controller;

import cn.javon.api.core.jwt.JwtUtil;
import cn.javon.api.sys.service.UserService;
import cn.javon.api.sys.vo.UserSearchVo;
import cn.javon.api.sys.vo.UserVo;
import cn.javon.core.Constant;
import cn.javon.core.response.Result;
import cn.javon.core.response.ResultGenerator;
import cn.javon.core.utils.RedisUtil;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.HashMap;
import java.util.Map;

/**
 * 用户管理控制器
 *
 * @author Javon
 */
@RestController
@RequestMapping("/user")
@Validated
public class UserController {

    @Autowired
    private UserService userService;

    @Autowired
    private UserDetailsService userDetailsService;

    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private RedisUtil redisUtil;

    /**
     * 用户登录
     *
     * @param userVo 用户登录Vo
     * @return 用户登录凭证Token
     */
    @PostMapping("/login")
    public Result login(@RequestBody final UserVo userVo) {
        if (StringUtils.isEmpty(userVo.getUsername())) {
            return ResultGenerator.genFailedResult("用户名不能为空");
        }
        if (StringUtils.isEmpty(userVo.getPassword())) {
            return ResultGenerator.genFailedResult("password empty");
        }
        final UserVo dbUser = this.userService.findByUsername(userVo.getUsername());
        if (dbUser == null) {
            return ResultGenerator.genFailedResult("username error");
        }
        if (!this.userService.verifyPassword(userVo.getPassword(), dbUser.getPassword())) {
            return ResultGenerator.genFailedResult("password error");
        }
        return this.getToken(dbUser);
    }

    /**
     * 用户信息查询（根据用户名称查询用户信息）
     *
     * @param user 用户
     * @return 用户信息
     */
    @GetMapping("/info")
    public Result detail(final Principal user) {
        final UserVo userVo = this.userService.findByUsername(user.getName());
        return ResultGenerator.genOkResult(userVo);
    }

    /**
     * 登出
     *
     * @return
     */
    @RequestMapping("/logout")
    public Result logout() {
        return ResultGenerator.genOkResult();
    }

    /**
     * 用户列表
     *
     * @param searchVo 查询Vo
     * @return 用户列表
     */
    @PreAuthorize("hasAuthority('sys:user:list')")
    @PostMapping("/list")
    public Result list(@RequestBody final UserSearchVo searchVo) {
        final Page<UserVo> list = this.userService.findPage(searchVo);
        return ResultGenerator.genOkResult(list);
    }

    /**
     * 用户查询（根据ID查询用户信息）
     *
     * @param id 用户id
     * @return 查询对象
     */
    @PreAuthorize("hasAuthority('sys:user:info')")
    @GetMapping("/{id}")
    public Result info(@PathVariable final Long id) {
        final UserVo userVo = this.userService.findById(id);
        return ResultGenerator.genOkResult(userVo);
    }

    /**
     * 用户新增
     *
     * @param userVo 用户Vo
     * @return 新增对象
     */
    @PreAuthorize("hasAuthority('sys:user:add')")
    @PostMapping("/add")
    public Result add(@RequestBody UserVo userVo) {
        userVo = this.userService.add(userVo);
        return ResultGenerator.genOkResult(userVo);
    }

    /**
     * 用户修改
     *
     * @param userVo 用户Vo
     * @return 修改对象
     */
    @PreAuthorize("hasAuthority('sys:user:update')")
    @PostMapping("/update")
    public Result update(@RequestBody UserVo userVo) {
        userVo = this.userService.update(userVo);
        return ResultGenerator.genOkResult(userVo);
    }

    /**
     * 用户删除（根据ID进行硬删除）
     *
     * @param id 用户ID
     * @return
     */
    @PreAuthorize("hasAuthority('sys:user:delete')")
    @GetMapping("/delete/{id}")
    public Result delete(@PathVariable final Long id) {
        this.userService.deleteById(id);
        return ResultGenerator.genOkResult();
    }

    /**
     * 获得 token
     *
     * @param userVo 用户Vo
     * @return 返回token Map
     */
    private Result getToken(final UserVo userVo) {
        final String username = userVo.getUsername();
        final UserDetails userDetails = this.userDetailsService.loadUserByUsername(username);
        final String token = this.jwtUtil.sign(username, userDetails.getAuthorities());
        redisUtil.set(token, userVo, Constant.TIME_DAY_SECONDS);
        final Map<String, String> map = new HashMap<String, String>();
        map.put("token", token);
        return ResultGenerator.genOkResult(map);
    }
}
