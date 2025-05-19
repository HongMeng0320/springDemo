package com.example.springdemo.utils;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

@Component
public class FileUploadUtil {

    @Value("${file.upload.path:uploads}")
    private String uploadPath;

    @Value("${file.upload.url-prefix:http://localhost:8080}")
    private String urlPrefix;

    // 允许的图片格式
    private static final List<String> ALLOWED_IMAGE_TYPES = Arrays.asList(
            "image/jpeg", "image/jpg", "image/png", "image/gif"
    );

    // 最大文件大小 (5MB)
    private static final long MAX_FILE_SIZE = 5 * 1024 * 1024;

    /**
     * 上传头像文件
     * @param userId 用户ID
     * @param file 上传的文件
     * @return 上传后的文件URL
     * @throws IOException 文件操作异常
     * @throws IllegalArgumentException 文件类型或大小不符合要求
     */
    public String uploadAvatar(Integer userId, MultipartFile file) throws IOException, IllegalArgumentException {
        System.out.println("开始上传头像文件");
        // 检查文件类型
        if (!ALLOWED_IMAGE_TYPES.contains(file.getContentType())) {
            throw new IllegalArgumentException("不支持的文件类型，仅支持JPG、JPEG、PNG、GIF格式");
        }

        // 检查文件大小
        if (file.getSize() > MAX_FILE_SIZE) {
            throw new IllegalArgumentException("文件大小超过限制，最大支持5MB");
        }

        // 创建上传目录
        String datePath = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy/MM/dd"));
        String avatarDir = uploadPath + "/avatar/" + datePath;
        Path dirPath = Paths.get(avatarDir);
        System.out.println("创建目录: " + dirPath.toAbsolutePath());
        
        if (!Files.exists(dirPath)) {
            try {
                Files.createDirectories(dirPath);
                System.out.println("目录创建成功");
            } catch (IOException e) {
                System.out.println("目录创建失败: " + e.getMessage());
                e.printStackTrace();
                throw e;
            }
        } else {
            System.out.println("目录已存在");
        }

        // 生成文件名：userId_时间戳_随机字符串.扩展名
        String originalFilename = file.getOriginalFilename();
        String extension = originalFilename != null && originalFilename.contains(".") 
            ? originalFilename.substring(originalFilename.lastIndexOf(".")) 
            : ".jpg";
        String fileName = userId + "_" + System.currentTimeMillis() + "_" + UUID.randomUUID().toString().substring(0, 8) + extension;

        // 保存文件
        String filePath = avatarDir + "/" + fileName;
        System.out.println("保存文件到: " + filePath);
        File destFile = new File(filePath);
        
        try {
            file.transferTo(destFile);
            System.out.println("文件保存成功");
        } catch (IOException e) {
            System.out.println("文件保存失败: " + e.getMessage());
            e.printStackTrace();
            throw e;
        }

        // 返回文件URL
        String fileUrl = urlPrefix + "/avatar/" + datePath + "/" + fileName;
        System.out.println("返回文件URL: " + fileUrl);
        return fileUrl;
    }
} 