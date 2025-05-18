package com.example.springdemo.service;

import com.example.springdemo.pojo.UserLog;
import com.github.pagehelper.PageInfo;

import java.time.LocalDateTime;

public interface UserLogService {
    
    /**
     * 记录用户注册日志
     * @param username 用户名
     * @param userId 用户ID（可为空，注册失败时）
     * @param success 是否成功
     * @param ipAddress IP地址
     * @param deviceInfo 设备信息
     * @param remark 备注
     * @return 是否成功
     */
    boolean logRegister(String username, Integer userId, boolean success, String ipAddress, String deviceInfo, String remark);
    
    /**
     * 记录用户登录日志
     * @param username 用户名
     * @param userId 用户ID（可为空，登录失败时）
     * @param success 是否成功
     * @param ipAddress IP地址
     * @param deviceInfo 设备信息
     * @param remark 备注
     * @return 是否成功
     */
    boolean logLogin(String username, Integer userId, boolean success, String ipAddress, String deviceInfo, String remark);
    
    /**
     * 记录用户登出日志
     * @param username 用户名
     * @param userId 用户ID
     * @param ipAddress IP地址
     * @param deviceInfo 设备信息
     * @param remark 备注
     * @return 是否成功
     */
    boolean logLogout(String username, Integer userId, String ipAddress, String deviceInfo, String remark);
    
    /**
     * 查询用户日志
     * @param userId 用户ID
     * @param username 用户名
     * @param operationType 操作类型
     * @param startTime 开始时间
     * @param endTime 结束时间
     * @param operationResult 操作结果
     * @param pageNum 页码
     * @param pageSize 每页大小
     * @return 分页结果
     */
    PageInfo<UserLog> findByCondition(Integer userId, String username, String operationType,
                                   LocalDateTime startTime, LocalDateTime endTime, Integer operationResult,
                                   Integer pageNum, Integer pageSize);
} 