package com.ruijie.rcos.rcdc.rco.module.impl.useruseinfo.service;

import com.ruijie.rcos.gss.sdk.iac.module.def.dto.user.IacUserDetailDTO;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.dto.userlicense.UserSessionDTO;
import com.ruijie.rcos.rcdc.rco.module.def.api.request.UserLoginRecordPageRequest;
import com.ruijie.rcos.rcdc.rco.module.def.api.dto.user.UserLoginRecordDTO;
import com.ruijie.rcos.sk.modulekit.api.comm.DefaultPageResponse;
import org.springframework.lang.Nullable;

import java.util.Date;
import java.util.List;
import java.util.UUID;


/**
 * Description: 用户登录记录
 * Copyright: Copyright (c) 2018
 * Company: Ruijie Co., Ltd.
 * Create Time: 2021/11/2 17:11
 *
 * @author zjy
 */
public interface UserLoginRecordService {

    /**
     * @Description: 分页查询
     * @Author: zjy
     * @Date: 2021/11/2 17:05
     * @param request 请求参数
     * @return : 返回值
     **/
    DefaultPageResponse<UserLoginRecordDTO> pageQuery(UserLoginRecordPageRequest request);

    /**
     * @Description: 保存登录信息
     * @Author: zjy
     * @Date: 2021/11/2 16:59
     * @param terminalId 终端id
     * @param userName 用户名
     * @return: 返回值
     **/
    void saveUserLoginInfo(String terminalId, String userName);

    /**
     *
     * @param userName 用户
     * @param terminalId 终端
     */
    void saveUserAuthInfo(String terminalId, String userName);

    /**
     * @title 记录shine连接vm成功的时间
     * @description
     * @author zjy
     * @param deskId 桌面id
     * @param connectionId 会话id
     * @updateTime 2021/11/1 10:49
     */
    void saveConnectVmSuccessTime(String deskId, Long connectionId);

    /**
     * @title 记录结束连接时间
     * @description
     * @author zjy
     * @param deskId 桌面
     * @param connectionId 会话id
     * @updateTime 2021/11/1 10:51
     */
    void saveLogoutTime(String deskId, @Nullable Long connectionId);

    /**
     * 清除指定时间前的日志
     * @param specifiedDate 指定时间
     */
    void clear(Date specifiedDate);

    /**
     * 添加登录时先添加记录进缓存，等登录成功后，再更新数据库
     * @param terminalId 终端id
     * @param userDetailDTO userDetailDTO
     */
    void addLoginCache(String terminalId, @Nullable IacUserDetailDTO userDetailDTO);

    /**
     * 补偿异常退出，但状态未更新的记录
     */
    void compensateFishedRecord();

    /**
     * 根据终端和桌面修改连接中的会话状态为未连接
     * @param terminalId 终端id
     * @param deskId 桌面id
     */
    void convertConnectingToDisconnect(String terminalId, String deskId);

    /**
     * 补偿会话状态为连接中的桌面
     * @param terminalId 终端id
     */
    void compensateTerminalConnectingRecord(String terminalId);

    /**
     * 查询最新创建的记录
     * @param createTime createTime
     * @return 记录
     */
    UserLoginRecordDTO findLastByCreateTime(Date createTime);

    /**
     * 根据桌面删除绑定终端的缓存
     * @param deskId 桌面
     */
    void deleteTerminalCacheByDeskId(String deskId);

    /**
     * 根据桌面删除远程协助缓存
     *
     * @param deskId 桌面
     */
    void deleteRemoteAssistanceCache(String deskId);

    /**
     * 处理桌面会话记录开始时间
     *
     * @param userSessionDTO 会话DTO
     */
    void handleDeskSessionRecordStartTime(UserSessionDTO userSessionDTO);

    /**
     * 处理桌面会话记录结束时间
     *
     * @param userId          用户ID
     * @param terminalId      终端ID
     * @param sessionInfoList 会话列表
     */
    void handleDeskSessionRecordEndTime(UUID userId, String terminalId, List<UserSessionDTO> sessionInfoList);
}
