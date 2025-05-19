package com.example.springdemo.mapper;

import com.example.springdemo.pojo.UserLog;
import org.apache.ibatis.annotations.*;

import java.time.LocalDateTime;
import java.util.List;

@Mapper
public interface UserLogMapper {
    
    /**
     * 插入用户日志
     * @param userLog 用户日志
     * @return 影响行数
     */
    @Insert("INSERT INTO users_log(user_id, username, operation_type, operation_time, operation_result, ip_address, device_info, remark) " +
            "VALUES(#{userId}, #{username}, #{operationType}, #{operationTime}, #{operationResult}, #{ipAddress}, #{deviceInfo}, #{remark})")
    @Options(useGeneratedKeys = true, keyProperty = "logId", keyColumn = "log_id")
    int insert(UserLog userLog);
    
    /**
     * 根据用户ID查询日志
     * @param userId 用户ID
     * @return 日志列表
     */
    @Select("SELECT * FROM users_log WHERE user_id = #{userId} ORDER BY operation_time DESC")
    List<UserLog> findByUserId(Integer userId);
    
    /**
     * 根据用户名查询日志
     * @param username 用户名
     * @return 日志列表
     */
    @Select("SELECT * FROM users_log WHERE username = #{username} ORDER BY operation_time DESC")
    List<UserLog> findByUsername(String username);
    
    /**
     * 根据操作类型查询日志
     * @param operationType 操作类型
     * @return 日志列表
     */
    @Select("SELECT * FROM users_log WHERE operation_type = #{operationType} ORDER BY operation_time DESC")
    List<UserLog> findByOperationType(String operationType);
    
    /**
     * 根据时间范围查询日志
     * @param startTime 开始时间
     * @param endTime 结束时间
     * @return 日志列表
     */
    @Select("SELECT * FROM users_log WHERE operation_time BETWEEN #{startTime} AND #{endTime} ORDER BY operation_time DESC")
    List<UserLog> findByTimeRange(LocalDateTime startTime, LocalDateTime endTime);
    
    /**
     * 条件查询
     * @param userId 用户ID
     * @param username 用户名
     * @param operationType 操作类型
     * @param startTime 开始时间
     * @param endTime 结束时间
     * @param operationResult 操作结果
     * @return 日志列表
     */
    @Select("<script>" +
            "SELECT * FROM users_log " +
            "<where>" +
            "<if test='userId != null'> user_id = #{userId} </if>" +
            "<if test='username != null'> AND username LIKE CONCAT('%', #{username}, '%') </if>" +
            "<if test='operationType != null'> AND operation_type = #{operationType} </if>" +
            "<if test='startTime != null'> AND operation_time &gt;= #{startTime} </if>" +
            "<if test='endTime != null'> AND operation_time &lt;= #{endTime} </if>" +
            "<if test='operationResult != null'> AND operation_result = #{operationResult} </if>" +
            "</where>" +
            "ORDER BY operation_time DESC" +
            "</script>")
    List<UserLog> findByCondition(@Param("userId") Integer userId, 
                                @Param("username") String username, 
                                @Param("operationType") String operationType,
                                @Param("startTime") LocalDateTime startTime, 
                                @Param("endTime") LocalDateTime endTime,
                                @Param("operationResult") Integer operationResult);
} 