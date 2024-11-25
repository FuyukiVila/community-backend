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
import com.fuyuki.backend.utils.MD5Utils;
import org.springframework.util.Assert;
import org.springframework.util.ObjectUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.HashMap;
import java.util.Map;

import static com.fuyuki.backend.jwt.JwtUtil.USER_NAME;


@RestController
@RequestMapping("/ums/user")
public class UmsUserController extends BaseController {

    private static final String UPLOAD_DIR = "src/static/avatar/";

    private static final String HOST = "localhost";

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
        if (user == null) {
            return ApiResult.unauthorized(null);
        }
        Page<BmsPost> page = bmsPostService.page(new Page<>(pageNo, size), new LambdaQueryWrapper<BmsPost>().eq(BmsPost::getUserId, user.getId()));
        ProfileVO profileVO = umsUserService.getUserProfile(user.getId());
        map.put("user", user);
        map.put("topics", page);
        map.put("profile", profileVO);
        return ApiResult.success(map);
    }

    @PostMapping("/update")
    public ApiResult<UmsUser> updateUser(@RequestHeader(value = USER_NAME) String userName, @Valid @RequestBody UmsUser id) {
        UmsUser operator = umsUserService.getUserByUsername(userName);
        if (operator == null) {
            return ApiResult.unauthorized(null);
        }
        UmsUser user = umsUserService.getUserByUsername(id.getUsername());
        Assert.notNull(user, "用户不存在");
        if (!operator.getIsAdmin() && operator != user) {
            return ApiResult.forbidden(null);
        }
        user.setBio(id.getBio());
        user.setAlias(id.getAlias());
        user.setMobile(id.getMobile());
        user.setEmail(id.getEmail());
        umsUserService.updateById(user);
        return ApiResult.success(id);
    }

    @GetMapping("/ban/{id}")
    public ApiResult<?> banUser(@RequestHeader(value = USER_NAME) String userName, @PathVariable("id") String id) {
        UmsUser user = umsUserService.getUserByUsername(userName);
        if (user == null || !user.getIsAdmin()) {
            return ApiResult.forbidden(null);
        }
        UmsUser umsUser = umsUserService.getUserByUsername(id);
        Assert.notNull(umsUser, "用户不存在");
        Assert.isTrue(umsUser.getStatus(), "用户已封禁");
        umsUser.setStatus(false);
        umsUserService.updateById(umsUser);
        return ApiResult.success(null, "封禁成功");
    }

    @GetMapping("/unban/{id}")
    public ApiResult<?> unbanUser(@RequestHeader(value = USER_NAME) String userName, @PathVariable("id") String id) {
        UmsUser user = umsUserService.getUserByUsername(userName);
        if (user == null || !user.getIsAdmin()) {
            return ApiResult.forbidden(null);
        }
        UmsUser umsUser = umsUserService.getUserByUsername(id);
        Assert.notNull(umsUser, "用户不存在");
        Assert.isTrue(!umsUser.getStatus(), "用户已解封");
        umsUser.setStatus(true);
        umsUserService.updateById(umsUser);
        return ApiResult.success(null, "解封成功");
    }

    @PostMapping("/upload_avatar")
    @ResponseBody
    public ApiResult<String> uploadAvatar(@RequestHeader(value = USER_NAME) String userName, @RequestParam("file") MultipartFile file, HttpServletResponse response) {
        if (file == null) {
            return ApiResult.failed("文件不能为空");
        }
        UmsUser user = umsUserService.getUserByUsername(userName);
        if (user == null) {
            return ApiResult.failed("用户不存在");
        }
        if (!user.getStatus()) {
            return ApiResult.failed("用户已被封禁，请联系管理员");
        }
        try {
            // 创建上传目录
            Path uploadPath = Paths.get(UPLOAD_DIR);
            if (!Files.exists(uploadPath)) {
                Files.createDirectories(uploadPath);
            }

            // 生成文件名的哈希值
            String originalFilename = file.getOriginalFilename();
            String hashFileName = null;
            hashFileName = MD5Utils.getStreamMD5String(file.getInputStream()) + originalFilename.substring(originalFilename.lastIndexOf("."));
            // 保存文件
            Path filePath = uploadPath.resolve(hashFileName);
            Files.copy(file.getInputStream(), filePath, StandardCopyOption.REPLACE_EXISTING);

            // 返回文件的访问路径
            String fileUrl = "http://%s:%d/avatar/%s".formatted(HOST, 8081, hashFileName);
            user.setAvatar(fileUrl);
            umsUserService.updateById(user);
            return ApiResult.success(fileUrl, "文件上传成功");
        } catch (IOException e) {
            e.printStackTrace();
            return ApiResult.failed("文件上传失败");
        }
    }

    @DeleteMapping("/delete_avatar/{id}")
    public ApiResult<?> deleteAvatar(@RequestHeader(value = USER_NAME) String userName, @PathVariable("id") String id) {
        UmsUser user = umsUserService.getUserByUsername(userName);
        if (user == null || !user.getIsAdmin()) {
            return ApiResult.forbidden(null);
        }
        UmsUser umsUser = umsUserService.getUserByUsername(id);
        Assert.notNull(umsUser, "用户不存在");
        umsUser.setAvatar("");
        umsUserService.updateById(umsUser);
        return ApiResult.success(null, "删除成功");
    }

}