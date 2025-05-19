package com.example.springdemo.service;

import com.example.springdemo.pojo.OssPolicy;

/**
 * 阿里云OSS服务接口
 */
public interface OssService {
    
    /**
     * 获取OSS上传策略
     * @return OSS上传策略
     */
    OssPolicy getPolicy();
} 