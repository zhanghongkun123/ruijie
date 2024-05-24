package com.ruijie.rcos.rcdc.rco.module.impl.service;


import com.ruijie.rcos.rcdc.rco.module.def.api.enums.RemoteAssistState;
import com.ruijie.rcos.rcdc.rco.module.impl.dto.computer.ComputerAssistDTO;
import com.ruijie.rcos.rcdc.rco.module.impl.service.struct.RemoteAssistInfo;
import com.ruijie.rcos.sk.base.exception.BusinessException;

import java.util.UUID;

/**
 * Description: 远程协助管理器
 * Copyright: Copyright (c) 2019
 * Company: Ruijie Co., Ltd.
 * Create Time: 2019年1月23日
 *
 * @author ketb
 */
public interface ComputerRemoteAssistMgmtService {

    /**
     * 向云桌面发起远程协助请求
     *
     * @param deskId 云桌面ID
     * @param adminId 管理员ID
     * @param adminName 管理员名称
     * @throws BusinessException business exception
     */
    void assistInquire(UUID deskId, UUID adminId, String adminName) throws BusinessException;

    /**
     * 查询远程协助信息
     *
     * @param deskId 云桌面ID
     * @param adminId 管理员ID
     * @return RemoteAssistInfo 远程协助信息
     * @throws BusinessException business exception
     */
    RemoteAssistInfo queryAssisInfo(UUID deskId, UUID adminId) throws BusinessException;

    /**
     * 通告远程协助用户选择
     *
     * @param deskId 云桌面ID
     * @param userOperateType 用户操作类型 0:同意 1:拒绝
     * @throws BusinessException business exception
     */
    void notifyAssistResult(UUID deskId, int userOperateType) throws BusinessException;

    /**
     * 用户关闭远程协助
     *
     * @param deskId 云桌面ID
     */
    void userCloseAssist(UUID deskId);

    /**
     * 更新远程状态
     *
     * @param deskId 云桌面ID
     * @param state 远程协助状态
     * @throws BusinessException business exception
     */
    void updateRemoteAssistState(UUID deskId, RemoteAssistState state) throws BusinessException;

    /**
     * 从用户侧反馈启动远程协助失败
     *
     * @param deskId desk id
     */
    void userAssistStartFail(UUID deskId);

    /**
     * 管理员关闭远程协助
     *
     * @param deskId 云桌面ID
     * @param adminId 管理员ID
     * @throws BusinessException business exception
     */
    void adminCancelAssist(UUID deskId, UUID adminId) throws BusinessException;

    /**
     * 远程协助等待用户回应超时
     *
     * @param deskId 云桌面ID
     * @throws BusinessException 业务异常
     */
    void computerUserResponseExpiredTime(UUID deskId) throws BusinessException;

    /**
     * 创建vnc通道结果
     *
     * @param deskId 云桌面ID
     * @param adminId 管理员ID
     * @throws BusinessException business exception
     */
    void createVncChannelResult(UUID deskId, UUID adminId) throws BusinessException;

    /**
     * vnc心跳消息处理
     *
     * @param deskId 云桌面ID
     */
    void vncHeartbeatHandle(UUID deskId);


    /**
     * 用户同意、拒绝、超时后更新远程信息
     * 
     * @param assistDTO 当前桌面远程信息
     * @throws BusinessException 业务异常
     */
    void updateAssistInfoAfterUserConfirm(ComputerAssistDTO assistDTO) throws BusinessException;

    /**
     * 设置当前远程协助状态是否已完全关闭
     * @param deskId 远程桌面id
     * @param status 是否完全关闭
     */
    void setRemoteAssistCloseStatus(UUID deskId, Boolean status);

    /**
     * 查询当前远程协助状态是否已完成关闭
     * @param deskId 远程桌面id
     * @return false 未完全关闭
     */
    Boolean getRemoteAssistCloseStatus(UUID deskId);

    /**
     * 删除远程协助状态是否关闭的标志
     * @param deskId 远程桌面id
     */
    void removeRemoteAssistCloseStatus(UUID deskId);

}
