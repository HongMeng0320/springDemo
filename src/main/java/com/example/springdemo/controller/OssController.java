package com.example.springdemo.controller;

import com.example.springdemo.common.Result;
import com.example.springdemo.pojo.OssPolicy;
import com.example.springdemo.service.OssService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * OSS控制器
 */
@RestController
@RequestMapping("/api/oss")
public class OssController {
    
    @Autowired
    private OssService ossService;
    
    /**
     * 获取OSS上传策略
     * @return 上传策略
     */
    @GetMapping("/policy")
    public Result<OssPolicy> getPolicy() {
        OssPolicy policy = ossService.getPolicy();
        return Result.success(policy);
    }
} 