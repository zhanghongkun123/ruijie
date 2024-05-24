package com.ruijie.rcos.rcdc.rco.module.web.service;


import com.ruijie.rcos.rcdc.rco.module.def.api.dto.CloudDesktopRemoteAssistDTO;
import com.ruijie.rcos.rcdc.rco.module.web.ctrl.user.dto.RemoteAssistStateDTO;
import com.ruijie.rcos.rcdc.rco.module.web.dto.RemoteAssistDTO;
import com.ruijie.rcos.sk.base.exception.BusinessException;

/**
 * Description: 远程连接处理接口
 * Copyright: Copyright (c) 2018
 * Company: Ruijie Co., Ltd.
 * Create Time: 2023/2/15
 *
 * @author zhiweiHong
 */
public interface RemoteAssistStrategyService {

    /**
     * 基于组件名称判断是否需要处理
     * @param component 组件名称
     * @return isNeedHandle 是否需要处理
     */
    Boolean isNeedHandle(String component);


    /**
     * 查询远程协助状态
     * @param remoteAssistDTO 远程协助请求
     * @return 状态实体
     * @throws BusinessException 业务异常
     */
    RemoteAssistStateDTO queryState(RemoteAssistDTO remoteAssistDTO) throws BusinessException;


    /**
     * 查询vnc连接请求
     * @param remoteAssistDTO 远程协助请求
     * @return 状态实体
     * @throws BusinessException 业务异常
     */
    CloudDesktopRemoteAssistDTO queryVncUrl(RemoteAssistDTO remoteAssistDTO) throws BusinessException;
}
