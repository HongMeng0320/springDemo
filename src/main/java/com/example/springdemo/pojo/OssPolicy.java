package com.example.springdemo.pojo;

import lombok.Data;

/**
 * OSS上传策略
 */
@Data
public class OssPolicy {
    private String accessId;     // 访问ID
    private String policy;       // 策略
    private String signature;    // 签名
    private String dir;          // 目录
    private String host;         // 主机
    private Long expire;         // 过期时间
} 