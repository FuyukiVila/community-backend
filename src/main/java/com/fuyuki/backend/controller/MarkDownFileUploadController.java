package com.fuyuki.backend.controller;

import com.fuyuki.backend.common.api.ApiResult;
import com.fuyuki.backend.model.entity.UmsUser;
import com.fuyuki.backend.service.IUmsUserService;
import com.fuyuki.backend.utils.MD5Utils;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;

import static com.fuyuki.backend.jwt.JwtUtil.USER_NAME;

@Controller
@RequestMapping("/file")
public class MarkDownFileUploadController {

    @Resource
    private IUmsUserService umsUserService;

    private static final String UPLOAD_DIR = "src/static/uploads/";

    @PostMapping("/upload")
    @ResponseBody
    public ResponseEntity<ApiResult<String>> uploadFile(@RequestHeader(value = USER_NAME) String userName, @RequestParam("file") MultipartFile file) {
        UmsUser user = umsUserService.getUserByUsername(userName);
        if (file == null) {
            return ResponseEntity.status(500).body(ApiResult.failed("文件不能为空"));
        }
        if (user == null) {
            return ResponseEntity.status(500).body(ApiResult.failed("用户不存在"));
        }
        if (!user.getStatus()) {
            return ResponseEntity.status(500).body(ApiResult.failed("用户已被封禁，请联系管理员"));
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
            String fileUrl = "http://localhost:%d/uploads/%s".formatted(8081, hashFileName);
            return ResponseEntity.ok(ApiResult.success(fileUrl));
        } catch (IOException e) {
            e.printStackTrace();
            return ResponseEntity.status(500).body(ApiResult.failed("文件上传失败"));
        }
    }
}
