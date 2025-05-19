package com.example.springdemo.pojo;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class UserLog {
    private Integer logId;        // 日志ID
    private Integer userId;       // 用户ID
    private String username;      // 用户名
    private String operationType; // 操作类型：register-注册，login-登录，logout-登出
    private LocalDateTime operationTime; // 操作时间
    private Integer operationResult; // 操作结果：1-成功，0-失败
    private String ipAddress;     // IP地址
    private String deviceInfo;    // 设备信息
    private String remark;        // 备注信息
} 