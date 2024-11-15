package com.fuyuki.backend.controller;

import com.fuyuki.backend.common.api.ApiResult;
import com.fuyuki.backend.utils.MD5Utils;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;

@Controller
public class FileUploadController {

    private static final String UPLOAD_DIR = "src/static/uploads/";

    @PostMapping("/file/upload")
    @ResponseBody
    public ResponseEntity<ApiResult<String>> uploadFile(@RequestParam("file") MultipartFile file) {
        Assert.notNull(file, "文件不能为空");
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
            String fileUrl = "http://127.0.0.1:8081/uploads/" + hashFileName;
            return ResponseEntity.ok(ApiResult.success(fileUrl));
        } catch (IOException e) {
            e.printStackTrace();
            return ResponseEntity.status(500).body(ApiResult.failed("文件上传失败"));
        }
    }
}
