package com.fuyuki.backend.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.fuyuki.backend.common.api.ApiResult;
import com.fuyuki.backend.model.dto.LoginDTO;
import com.fuyuki.backend.model.dto.RegisterDTO;
import com.fuyuki.backend.model.entity.BmsPost;
import com.fuyuki.backend.model.entity.UmsUser;
import com.fuyuki.backend.model.vo.ProfileVO;
import com.fuyuki.backend.service.IBmsPostService;
import com.fuyuki.backend.service.IUmsUserService;
import org.springframework.util.Assert;
import org.springframework.util.ObjectUtils;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.validation.Valid;
import java.util.HashMap;
import java.util.Map;

import static com.fuyuki.backend.jwt.JwtUtil.USER_NAME;


@RestController
@RequestMapping("/ums/user")
public class UmsUserController extends BaseController {
    @Resource
    private IUmsUserService umsUserService;
    @Resource
    private IBmsPostService bmsPostService;

    @RequestMapping(value = "/register", method = RequestMethod.POST)
    public ApiResult<Map<String, Object>> register(@Valid @RequestBody RegisterDTO dto) {
        dto.setName(dto.getName().trim().toLowerCase());
        UmsUser user = umsUserService.executeRegister(dto);
        if (ObjectUtils.isEmpty(user)) {
            return ApiResult.failed("账号注册失败");
        }
        Map<String, Object> map = new HashMap<>(16);
        map.put("user", user);
        return ApiResult.success(map);
    }

    @RequestMapping(value = "/login", method = RequestMethod.POST)
    public ApiResult<Map<String, String>> login(@Valid @RequestBody LoginDTO dto) {
        dto.setUsername(dto.getUsername().trim().toLowerCase());
        String token = umsUserService.executeLogin(dto);
        if (ObjectUtils.isEmpty(token)) {
            return ApiResult.failed("账号密码错误");
        }
        Map<String, String> map = new HashMap<>(16);
        map.put("token", token);
        return ApiResult.success(map, "登录成功");
    }

    @RequestMapping(value = "/info", method = RequestMethod.GET)
    public ApiResult<UmsUser> getUser(@RequestHeader(value = USER_NAME) String userName) {
        UmsUser user = umsUserService.getUserByUsername(userName);
        return ApiResult.success(user);
    }

    @RequestMapping(value = "/logout", method = RequestMethod.GET)
    public ApiResult<Object> logOut() {
        return ApiResult.success(null, "注销成功");
    }

    @GetMapping("/{username}")
    public ApiResult<Map<String, Object>> getUserByName(@PathVariable("username") String username, @RequestParam(value = "pageNo", defaultValue = "1") Integer pageNo, @RequestParam(value = "size", defaultValue = "10") Integer size) {
        Map<String, Object> map = new HashMap<>(16);
        UmsUser user = umsUserService.getUserByUsername(username);
        Assert.notNull(user, "用户不存在");
        Page<BmsPost> page = bmsPostService.page(new Page<>(pageNo, size), new LambdaQueryWrapper<BmsPost>().eq(BmsPost::getUserId, user.getId()));
        ProfileVO profileVO = umsUserService.getUserProfile(user.getId());
        map.put("user", user);
        map.put("topics", page);
        map.put("profile", profileVO);
        return ApiResult.success(map);
    }

    @PostMapping("/update")
    public ApiResult<UmsUser> updateUser(@Valid @RequestBody UmsUser umsUser) {
        UmsUser user = umsUserService.getUserByUsername(umsUser.getUsername());
        Assert.notNull(user, "用户不存在");
        Assert.isTrue(user.getStatus(), "用户已被封禁，请联系管理员");
        user.setBio(umsUser.getBio());
        user.setAlias(umsUser.getAlias());
        user.setMobile(umsUser.getMobile());
        user.setEmail(umsUser.getEmail());
        umsUserService.updateById(user);
        return ApiResult.success(umsUser);
    }

    @GetMapping("/ban/{id}")
    public ApiResult<UmsUser> banUser(@RequestHeader(value = USER_NAME) String userName, @PathVariable("id") String id) {
        UmsUser user = umsUserService.getUserByUsername(userName);
        Assert.isTrue(user.getIsAdmin(), "没有权限");
        UmsUser umsUser = umsUserService.getUserByUsername(id);
        Assert.notNull(umsUser, "用户不存在");
        Assert.isTrue(umsUser.getStatus(), "用户已封禁");
        umsUser.setStatus(false);
        umsUserService.updateById(umsUser);
        return ApiResult.success(umsUser);
    }

    @GetMapping("/unban/{id}")
    public ApiResult<UmsUser> unbanUser(@RequestHeader(value = USER_NAME) String userName, @PathVariable("id") String id) {
        UmsUser user = umsUserService.getUserByUsername(userName);
        Assert.isTrue(user.getIsAdmin(), "没有权限");
        UmsUser umsUser = umsUserService.getUserByUsername(id);
        Assert.notNull(umsUser, "用户不存在");
        Assert.isTrue(!umsUser.getStatus(), "用户已解封");
        umsUser.setStatus(true);
        umsUserService.updateById(umsUser);
        return ApiResult.success(umsUser);
    }
}