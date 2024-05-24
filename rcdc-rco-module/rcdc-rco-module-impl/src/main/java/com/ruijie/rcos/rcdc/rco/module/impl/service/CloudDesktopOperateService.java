package com.ruijie.rcos.rcdc.rco.module.impl.service;

import com.ruijie.rcos.rcdc.clouddesktop.module.def.enums.DesktopRole;
import com.ruijie.rcos.rcdc.rco.module.def.api.request.CloudDesktopStartRequest;
import com.ruijie.rcos.rcdc.rco.module.impl.entity.UserTerminalEntity;
import com.ruijie.rcos.sk.base.exception.BusinessException;

import java.util.List;
import java.util.UUID;

/**
 * Description: 云桌面操作接口
 * Copyright: Copyright (c) 2018
 * Company: Ruijie Co., Ltd.
 * Create Time: 2019/7/16
 *
 * @author Jarman
 */
public interface CloudDesktopOperateService {

    /**
     ** 启动云桌面请求
     *
     * @param request 桌面id（t_rco_user_desktop表的主键id）
     * @throws BusinessException 业务异常
     */
    void startDesktop(CloudDesktopStartRequest request) throws BusinessException;

    /**
     ** 恢复云桌面
     *
     * @param desktopId desktopId
     * @throws BusinessException 业务异常
     */
    void resumeDesktop(UUID desktopId) throws BusinessException;

    /**
     * 恢复故障云桌面
     *
     * @param desktopId desktopId
     * @throws BusinessException 业务异常
     */
    void recoverDeskFromError(UUID desktopId) throws BusinessException;

    /**
     * 解绑关联终端的云桌面
     *
     * @param userTerminalEntity 终端对象
     */
    void unbindDesktopTerminal(UserTerminalEntity userTerminalEntity);

    /**
     * 桌面和终端绑定
     *
     * @param desktopId 桌面id
     * @param terminalId 终端id
     */
    void bindDesktopTerminal(UUID desktopId, String terminalId);

    /**
     * 解绑关联终端的云桌面（删除终端时）
     *
     * @param userTerminalEntity 终端对象
     */
    void unbindDesktopTerminalForDeleteTerminal(UserTerminalEntity userTerminalEntity);

    /**
     * 编辑云桌面角色
     * 
     * @param desktopId 云桌面ID
     * @param desktopRole 云桌面角色
     * @throws BusinessException 业务异常
     */
    void editDesktopRole(UUID desktopId, DesktopRole desktopRole) throws BusinessException;

    /**
     ** 启动云桌面请求
     *
     * @param request 桌面id（t_rco_user_desktop表的主键id）
     * @throws BusinessException 业务异常
     */
    void startIdvDesktop(CloudDesktopStartRequest request) throws BusinessException;

    /**
     * 云桌面维护模式
     * @param desktopIds 云桌面id
     * @param isOpen true:开启云桌面维护模式，false:关闭云桌面维护模式
     */
    void changeDeskMaintenanceModel(List<UUID> desktopIds, Boolean isOpen);
}
