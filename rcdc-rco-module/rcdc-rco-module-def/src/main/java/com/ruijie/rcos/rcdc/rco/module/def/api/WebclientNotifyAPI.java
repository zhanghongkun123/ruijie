package com.ruijie.rcos.rcdc.rco.module.def.api;

import com.ruijie.rcos.gss.base.iac.module.dto.IacAuthChangeDTO;
import com.ruijie.rcos.rcdc.rco.module.def.api.dto.*;
import com.ruijie.rcos.sk.base.exception.BusinessException;

/**
 * Description: Webclient通知api
 * Copyright: Copyright (c) 2022
 * Company: Ruijie Co., Ltd.
 * Create Time: 2022/06/23 11:08
 *
 * @author lihengjing
 */
public interface WebclientNotifyAPI {

    /**
     * 通知主题策略变更
     *
     */
    void notifyThemeChange();


    /**
     * 通知远程协助状态变更
     *
     * @param isLocalRequest isLocalRequest
     * @param remoteAssistStateDTO remoteAssistStateDTO
     */
    void notifyRemoteAssistState(Boolean isLocalRequest, RemoteAssistStateDTO remoteAssistStateDTO);

    /**
     * 通知桌面被抢占
     *
     * @param isLocalRequest isLocalRequest
     * @param repeatStartVmWebclientNotifyDTO repeatStartVmWebclientNotifyDTO
     */
    void notifyTerminalDesktopIsRobbed(Boolean isLocalRequest, RepeatStartVmWebclientNotifyDTO repeatStartVmWebclientNotifyDTO);

    /**
     * 通知用户已经被删除
     * 
     * @param isLocalRequest isLocalRequest
     * @param deletedUserInfoDTO userDeletedDesktopInfoDTO
     */
    void notifyUserDeleted(Boolean isLocalRequest, DeletedUserInfoDTO deletedUserInfoDTO);

    /**
     * 通知客户端认证方式改变
     *
     * @param iacAuthTypeDTO 登录认证信息
     */
    void notifyRefreshLoginPageInfo(AuthChangeDTO iacAuthTypeDTO);

}
