package com.ruijie.rcos.rcdc.rco.module.impl.service.impl.connector.mq.api;

import com.ruijie.rcos.gss.base.iac.module.dto.IacAuthChangeDTO;
import com.ruijie.rcos.gss.sdk.iac.module.def.dto.qrcode.IacQrCodeConfigDTO;
import com.ruijie.rcos.rcdc.rco.module.def.api.dto.*;
import com.ruijie.rcos.sk.base.exception.BusinessException;
import com.ruijie.rcos.sk.connectkit.api.annotation.ApiAction;
import com.ruijie.rcos.sk.connectkit.api.annotation.ApiGroup;
import com.ruijie.rcos.sk.connectkit.api.annotation.mq.MQ;
import com.ruijie.rcos.sk.connectkit.api.annotation.mq.MqConsumer;

/**
 * Description: Webclient通知MQ消息生产者
 * Copyright: Copyright (c) 2022
 * Company: Ruijie Co., Ltd.
 * Create Time: 2022/06/23 11:08
 *
 * @author lihengjing
 */
@MQ
@ApiGroup("RCDC")
public interface WebClientProducerAPI {

    /**
     * 主题策略更新通知
     *
     * @param themeConfigInfoDTO 主题策略配置信息
     * @Author: zjy
     * @Date: 2022/3/7 10:23
     **/
    @ApiAction("common.themeInfo")
    void notifyThemeInfo(ThemeConfigInfoDTO themeConfigInfoDTO);

    /**
     * 远程协助状态变更通知
     *
     * @param remoteAssistStateDTO 远程协助状态详情
     * @Author: zjy
     * @Date: 2022/3/7 10:23
     **/
    @ApiAction("desktop.remoteAssistanceInfo")
    void notifyRemoteAssistanceInfo(RemoteAssistStateDTO remoteAssistStateDTO);


    /**
     * 云桌面抢占通知
     *
     * @param repeatStartVmWebclientNotifyDTO 云桌面抢占通知详情
     * @Author: zjy
     * @Date: 2022/3/7 10:23
     **/
    @ApiAction("desktop.terminalDesktopIsRobbed")
    void notifyTerminalDesktopIsRobbed(RepeatStartVmWebclientNotifyDTO repeatStartVmWebclientNotifyDTO);

    /**
     * 统一登录开关更新通知
     *
     * @param rccmServerConfigInfoDTO 主题策略配置信息
     * @Author: zjy
     * @Date: 2022/3/7 10:23
     **/
    @ApiAction("common.unifiedLogin")
    void notifyUnifiedLogin(RccmServerConfigInfoDTO rccmServerConfigInfoDTO);

    /**
     * 动态口令更新通知
     *
     * @param otpCertificationChangeDTO 动态口令配置信息
     **/
    @ApiAction("common.otpChange")
    void notifyOtpChange(OtpCertificationChangeDTO otpCertificationChangeDTO);

    /**
     * 用户被删除通知
     *
     * @param deletedUserInfoDTO 用户信息
     * @throws BusinessException 异常
     */
    @ApiAction("common.userDeleted")
    @MqConsumer(workerCount = 1)
    void notifyUserDeletedMessage(DeletedUserInfoDTO deletedUserInfoDTO) throws BusinessException;
    
    /**
     * 踢出用户登录
     *
     * @param userLogoutDTO 用户退出信息
     * @throws BusinessException 异常
     */
    @ApiAction("common.userLogout")
    @MqConsumer(workerCount = 1)
    void notifyUserLogout(UserLogoutDTO userLogoutDTO) throws BusinessException;

    /**
     * 通知客户端认证方式改变
     *
     * @param iacAuthTypeDTO 登录认证信息
     * @throws BusinessException 异常
     */
    @ApiAction("iac.refreshLoginPageInfo")
    void notifyRefreshLoginPageInfo(AuthChangeDTO iacAuthTypeDTO) throws BusinessException;

    /**
     * 通知客户端扫码方式改变
     *
     * @param qrCodeConfig 登录认证信息
     * @throws BusinessException 异常
     */
    @ApiAction("iac.notifyQrConfigUpdate")
    void notifyQrConfigUpdate(IacQrCodeConfigDTO qrCodeConfig) throws BusinessException;
}
