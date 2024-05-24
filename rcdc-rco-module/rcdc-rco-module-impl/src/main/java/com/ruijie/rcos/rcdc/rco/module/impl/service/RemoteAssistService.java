package com.ruijie.rcos.rcdc.rco.module.impl.service;


import com.ruijie.rcos.rcdc.clouddesktop.module.def.spi.request.CbbRemoteAssistUserConfirmNotifyReqeust;
import com.ruijie.rcos.rcdc.rco.module.def.api.dto.RemoteAssistInfoDTO;
import com.ruijie.rcos.rcdc.rco.module.def.api.enums.RemoteAssistState;
import com.ruijie.rcos.rcdc.rco.module.impl.dto.CreateDeskRemoteAssistDTO;
import com.ruijie.rcos.rcdc.rco.module.impl.service.struct.RemoteAssistInfo;
import com.ruijie.rcos.sk.base.exception.BusinessException;
import org.springframework.lang.Nullable;

import java.util.UUID;

/**
 * Description: 远程协助管理器
 * Copyright: Copyright (c) 2019
 * Company: Ruijie Co., Ltd.
 * Create Time: 2019年1月23日
 * 
 * @author artom
 */
public interface RemoteAssistService {
    
    /**
     * 向云桌面发起远程协助请求
     * @param dto 远程请求的信息
     * @throws BusinessException business exception
     */
    void applyAssist(CreateDeskRemoteAssistDTO dto) throws BusinessException;
    
    /**
     * 查询远程协助信息
     * @param deskId 云桌面ID
     * @param adminId 管理员ID
     * @return RemoteAssistInfo 远程协助信息
     * @throws BusinessException business exception
     */
    RemoteAssistInfo queryAssisInfo(UUID deskId, UUID adminId) throws BusinessException;
    
    /**
     * 通告远程协助用户选择
     * @param deskId 云桌面ID
     * @param request 远程协助确认消息
     * @throws BusinessException business exception
     */
    void notifyAssistResult(UUID deskId, CbbRemoteAssistUserConfirmNotifyReqeust request) throws BusinessException;
    
    /**
     * 用户关闭远程协助
     * @param deskId 云桌面ID
     * @param operateCode 操作code
     */ 
    void userCloseAssist(UUID deskId, Integer operateCode);
    
    /**
     * 从用户侧反馈启动远程协助失败
     * @param deskId desk id
     */
    void userAssistStartFail(UUID deskId);
    
    /**
     * 管理员关闭远程协助
     * @param deskId 云桌面ID
     * @param adminId 管理员ID
     * @throws BusinessException business exception
     */
    void adminCancelAssist(UUID deskId, UUID adminId) throws BusinessException;
    
    /**
     * 云桌面关闭后远程协助的处理
     * @param deskId 云桌面ID
     */
    void deskShutdownHandle(UUID deskId);
    
    /**
     * 创建vnc通道结果
     * @param deskId 云桌面ID
     * @param adminId 管理员ID
     * @throws BusinessException business exception
     */
    void createVncChannelResult(UUID deskId, UUID adminId) throws BusinessException;
    
    /**
     * est心跳消息处理
     * @param deskId 云桌面ID
     */
    void estHeartbeatHandle(UUID deskId);

    /**
     * 查询当前正在的远程信息
     * @param deskId 桌面id
     * @return 结果
     */
    RemoteAssistInfo queryRemoteAssistInfo(UUID deskId);

    /**
     * 移出远程信息
     * @param deskId 桌面id
     */
    void removeRemoteAssistInfo(UUID deskId);

    /**
     * 添加远程信息
     * @param deskId 桌面id
     * @param info 远程信息
     */
    void createRemoteAssistInfo(UUID deskId, RemoteAssistInfo info);

    /**
     * 校验其他管理员是否正在远程
     * @param newDeskId 桌面id
     * @param adminId 管理员id
     */
    void cancelOldRemoteAssistHandle(UUID newDeskId, UUID adminId);
    
    /**
     * 更新远程信息
     * @param infoDTO 远程信息
     */
    void updateRemoteAssistInfo(RemoteAssistInfoDTO infoDTO);

    /**
     * 移出远程信息
     * @param deskId 桌面id
     */
    void resetVncHeartBeat(UUID deskId);

    /**
     * 变更状态
     * @param deskId 桌面id
     * @param state 状态
     */
    void changeAssistState(UUID deskId, RemoteAssistState state);


    /**
     * 通知 ratool 当前的远程协助状态
     * @param deskId 桌面id
     */
    void syncRemoteAssistStatus(UUID deskId);


    /**
     * 通知shine 当前的远程协助请求状态（是否有请求远程协助）
     * @param terminalId 终端id
     * @param  isRequest 是否请求
     */
    void syncRemoteAssistRequestStatus(String terminalId, Boolean isRequest);


    /**
     * 查询终端是否有请求进行远程协助
     * @param deskId 桌面id
     * @return 、
     */
    boolean hasRequestRemoteAssist(UUID deskId);


    /**
     * 查询当前远程协助数量
     * @return 云桌面数量
     */
    int remoteAssistNum();

    /**
     * 用户发起请求协助(申报故障非远程桌面远程协助)
     * @param deskId desk id
     * @param terminalId terminalId
     * @throws BusinessException business exception
     */
    void requestRemoteAssist(UUID deskId, String terminalId) throws BusinessException;


    /**
     * 用户发起请求协助(申报故障非远程桌面远程协助)
     * @param deskId desk id
     * @throws BusinessException business exception
     */
    void requestRemoteAssist(UUID deskId) throws BusinessException;


    /**
     * 用户发起请求协助(申报故障非远程桌面远程协助)
     * @param deskId desk id
     * @param terminalId terminalId
     */
    void cancelRemoteAssist(UUID deskId, String terminalId);

    /**
     * 用户发起请求协助(申报故障非远程桌面远程协助)
     * @param deskId desk id
     */
    void cancelRemoteAssist(UUID deskId);
}
