package com.example.springdemo.controller;

import com.example.springdemo.common.Result;
import com.example.springdemo.pojo.User;
import com.example.springdemo.service.PointService;
import com.example.springdemo.service.TokenService;
import com.example.springdemo.service.UserLogService;
import com.example.springdemo.service.UserService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Pattern;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    @Autowired
    private UserService userService;
    
    @Autowired
    private PointService pointService;
    
    @Autowired
    private TokenService tokenService;
    
    @Autowired
    private UserLogService userLogService;

    /**
     * 用户注册
     * @param user 用户信息
     * @return 注册结果
     */
    @PostMapping("/register")
    public Result register(@RequestBody User user, HttpServletRequest request) {
        // 获取客户端信息
        String ipAddress = getClientIpAddress(request);
        String deviceInfo = request.getHeader("User-Agent");
        
        // 参数校验
        if (user.getUsername() == null || user.getUsername().trim().isEmpty()) {
            // 记录注册失败日志
            userLogService.logRegister(user.getUsername(), null, false, ipAddress, deviceInfo, "用户名不能为空");
            return Result.error("用户名不能为空");
        }
        if (user.getPassword() == null || user.getPassword().trim().isEmpty()) {
            // 记录注册失败日志
            userLogService.logRegister(user.getUsername(), null, false, ipAddress, deviceInfo, "密码不能为空");
            return Result.error("密码不能为空");
        }
        
        // 用户名格式校验：3-20个字符，只允许字母、数字和下划线
        String usernamePattern = "^[a-zA-Z0-9_]{3,20}$";
        if (!Pattern.matches(usernamePattern, user.getUsername())) {
            // 记录注册失败日志
            userLogService.logRegister(user.getUsername(), null, false, ipAddress, deviceInfo, "用户名格式不正确");
            return Result.error("用户名格式不正确，只允许3-20个字符的字母、数字和下划线");
        }
        
        // 密码格式校验：8-20个字符，必须包含字母和数字
        String passwordPattern = "^(?=.*[A-Za-z])(?=.*\\d)[A-Za-z\\d]{8,20}$";
        if (!Pattern.matches(passwordPattern, user.getPassword())) {
            // 记录注册失败日志
            userLogService.logRegister(user.getUsername(), null, false, ipAddress, deviceInfo, "密码格式不正确");
            return Result.error("密码格式不正确，必须包含8-20个字符，且同时包含字母和数字");
        }
        
        // 检查用户名是否已存在
        User existingUser = userService.findByUsername(user.getUsername());
        if (existingUser != null) {
            // 记录注册失败日志
            userLogService.logRegister(user.getUsername(), null, false, ipAddress, deviceInfo, "用户名已被使用");
            return Result.error("用户名已被使用");
        }
        
        // 设置默认角色和状态
        user.setRole("user");
        user.setStatus(1);
        
        // 注册用户
        boolean success = userService.register(user);
        if (success) {
            // 为新用户初始化积分账户（默认0积分）
            pointService.initUserPoints(user.getUserId());
            
            // 记录注册成功日志
            userLogService.logRegister(user.getUsername(), user.getUserId(), true, ipAddress, deviceInfo, "注册成功");
            
            // 返回成功信息，不包含密码
            user.setPassword(null);
            Map<String, Object> data = new HashMap<>();
            data.put("userId", user.getUserId());
            data.put("username", user.getUsername());
            
            return Result.success("注册成功", data);
        } else {
            // 记录注册失败日志
            userLogService.logRegister(user.getUsername(), null, false, ipAddress, deviceInfo, "注册失败，请稍后重试");
            return Result.error("注册失败，请稍后重试");
        }
    }
    
    /**
     * 获取客户端真实IP地址
     */
    private String getClientIpAddress(HttpServletRequest request) {
        String ip = request.getHeader("X-Forwarded-For");
        if (ip == null || ip.isEmpty() || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("Proxy-Client-IP");
        }
        if (ip == null || ip.isEmpty() || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("WL-Proxy-Client-IP");
        }
        if (ip == null || ip.isEmpty() || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("HTTP_CLIENT_IP");
        }
        if (ip == null || ip.isEmpty() || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("HTTP_X_FORWARDED_FOR");
        }
        if (ip == null || ip.isEmpty() || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getRemoteAddr();
        }
        return ip;
    }
} 