package com.ruijie.rcos.rcdc.rco.module.def.api;

import com.ruijie.rcos.rcdc.rco.module.def.api.dto.RemoteAssistInfoDTO;
import com.ruijie.rcos.rcdc.rco.module.def.api.request.ChangeAssistStateRequest;
import com.ruijie.rcos.rcdc.rco.module.def.api.request.CreateRemoteAssistInfoRequest;
import com.ruijie.rcos.rcdc.rco.module.def.api.request.RemoteAssistOtherDeskHandleRequest;
import com.ruijie.rcos.rcdc.rco.module.def.api.response.RemoteAssistInfoResponse;
import com.ruijie.rcos.sk.modulekit.api.comm.IdRequest;

/**
 * 向外开放api接口操作存放远程信息的infoMap
 * 整个系统统一维护同一份远程信息
 * 方便做远程协助功能的限制和同步
 * Description: 远程信息操作类API
 * Copyright: Copyright (c) 2019
 * Company: Ruijie Co., Ltd.
 * Create Time: 2020/2/23 10:13
 *
 * @author ketb
 */
public interface RemoteAssistInfoOperateAPI {

    /**
     * 查询当前正在的远程信息
     * @param request 请求参数
     * @return 结果
     */
    RemoteAssistInfoResponse queryRemoteAssistInfo(IdRequest request);

    /**
     * 移出远程信息
     * @param request 请求参数
     */
    void removeRemoteAssistInfo(IdRequest request);

    /**
     * 添加远程信息
     * @param request 请求参数
     */
    void createRemoteAssistInfo(CreateRemoteAssistInfoRequest request);


    /**
     * 校验其他管理员是否正在远程
     * @param request 请求参数
     */
    void remoteAssistOtherDeskHandle(RemoteAssistOtherDeskHandleRequest request);
    
    /**
     * 更新远程信息
     * @param assistInfoDTO 请求参数
     */
    void updateRemoteAssistInfo(RemoteAssistInfoDTO assistInfoDTO);

    /**
     * 重置vnc心跳
     * @param request 请求参数
     */
    void resetVncHeartBeat(IdRequest request);

     /**
     * 更新状态
     * @param request 请求参数
     */
    void changeAssistState(ChangeAssistStateRequest request);

    /**
     * 查询当前远程协助数量
     * @return 云桌面数量
     */
    int remoteAssistNum();
}
