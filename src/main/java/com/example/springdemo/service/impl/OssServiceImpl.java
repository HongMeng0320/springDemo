package com.example.springdemo.service.impl;

import com.aliyun.oss.OSS;
import com.aliyun.oss.common.utils.BinaryUtil;
import com.aliyun.oss.model.MatchMode;
import com.aliyun.oss.model.PolicyConditions;
import com.example.springdemo.pojo.OssPolicy;
import com.example.springdemo.service.OssService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.nio.charset.StandardCharsets;
import java.util.Date;

/**
 * 阿里云OSS服务实现类
 */
@Service
public class OssServiceImpl implements OssService {

    @Autowired
    private OSS ossClient;

    @Value("${aliyun.oss.endpoint}")
    private String endpoint;

    @Value("${aliyun.oss.bucket-name}")
    private String bucketName;

    @Value("${aliyun.oss.access-key-id}")
    private String accessKeyId;

    @Value("${aliyun.oss.access-key-secret}")
    private String accessKeySecret;

    /**
     * 获取OSS上传策略
     * @return OSS上传策略
     */
    @Override
    public OssPolicy getPolicy() {
        // 存储目录
        String dir = "avatar/";
        // 过期时间（10分钟）
        long expireTime = 10 * 60;
        long expireEndTime = System.currentTimeMillis() + expireTime * 1000;
        Date expiration = new Date(expireEndTime);

        // 设置上传策略
        PolicyConditions policyConditions = new PolicyConditions();
        // 设置上传文件大小限制（5MB）
        policyConditions.addConditionItem(PolicyConditions.COND_CONTENT_LENGTH_RANGE, 0, 5 * 1024 * 1024);
        // 设置上传目录
        policyConditions.addConditionItem(MatchMode.StartWith, PolicyConditions.COND_KEY, dir);

        // 生成策略
        String postPolicy = ossClient.generatePostPolicy(expiration, policyConditions);
        byte[] binaryData = postPolicy.getBytes(StandardCharsets.UTF_8);
        String encodedPolicy = BinaryUtil.toBase64String(binaryData);
        String postSignature = ossClient.calculatePostSignature(postPolicy);

        // 构建返回对象
        OssPolicy ossPolicy = new OssPolicy();
        ossPolicy.setAccessId(accessKeyId);
        ossPolicy.setPolicy(encodedPolicy);
        ossPolicy.setSignature(postSignature);
        ossPolicy.setDir(dir);
        ossPolicy.setHost("https://" + bucketName + "." + endpoint);
        ossPolicy.setExpire(expireEndTime / 1000);

        return ossPolicy;
    }
} 