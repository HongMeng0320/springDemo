package com.example.springdemo.service.impl;

import com.example.springdemo.mapper.UserLogMapper;
import com.example.springdemo.pojo.UserLog;
import com.example.springdemo.service.UserLogService;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class UserLogServiceImpl implements UserLogService {

    @Autowired
    private UserLogMapper userLogMapper;

    @Override
    public boolean logRegister(String username, Integer userId, boolean success, String ipAddress, String deviceInfo, String remark) {
        UserLog userLog = new UserLog();
        userLog.setUserId(userId);
        userLog.setUsername(username);
        userLog.setOperationType("register");
        userLog.setOperationTime(LocalDateTime.now());
        userLog.setOperationResult(success ? 1 : 0);
        userLog.setIpAddress(ipAddress);
        userLog.setDeviceInfo(deviceInfo);
        userLog.setRemark(remark);
        
        return userLogMapper.insert(userLog) > 0;
    }

    @Override
    public boolean logLogin(String username, Integer userId, boolean success, String ipAddress, String deviceInfo, String remark) {
        UserLog userLog = new UserLog();
        userLog.setUserId(userId);
        userLog.setUsername(username);
        userLog.setOperationType("login");
        userLog.setOperationTime(LocalDateTime.now());
        userLog.setOperationResult(success ? 1 : 0);
        userLog.setIpAddress(ipAddress);
        userLog.setDeviceInfo(deviceInfo);
        userLog.setRemark(remark);
        
        return userLogMapper.insert(userLog) > 0;
    }

    @Override
    public boolean logLogout(String username, Integer userId, String ipAddress, String deviceInfo, String remark) {
        UserLog userLog = new UserLog();
        userLog.setUserId(userId);
        userLog.setUsername(username);
        userLog.setOperationType("logout");
        userLog.setOperationTime(LocalDateTime.now());
        userLog.setOperationResult(1); // 登出一般都是成功的
        userLog.setIpAddress(ipAddress);
        userLog.setDeviceInfo(deviceInfo);
        userLog.setRemark(remark);
        
        return userLogMapper.insert(userLog) > 0;
    }

    @Override
    public PageInfo<UserLog> findByCondition(Integer userId, String username, String operationType,
                                          LocalDateTime startTime, LocalDateTime endTime, Integer operationResult,
                                          Integer pageNum, Integer pageSize) {
        PageHelper.startPage(pageNum, pageSize);
        List<UserLog> logs = userLogMapper.findByCondition(userId, username, operationType, startTime, endTime, operationResult);
        return new PageInfo<>(logs);
    }
} 