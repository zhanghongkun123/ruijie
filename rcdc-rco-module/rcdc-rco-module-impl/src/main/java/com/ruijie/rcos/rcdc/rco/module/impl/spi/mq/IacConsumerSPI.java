package com.ruijie.rcos.rcdc.rco.module.impl.spi.mq;

import com.ruijie.rcos.gss.base.iac.module.dto.*;
import com.ruijie.rcos.gss.sdk.iac.module.def.api.request.user.IacUserIdentityConfigRequest;
import com.ruijie.rcos.gss.sdk.iac.module.def.dto.IacClientAuthSecurityDTO;
import com.ruijie.rcos.sk.connectkit.api.annotation.ApiAction;
import com.ruijie.rcos.sk.connectkit.api.annotation.ApiGroup;
import com.ruijie.rcos.sk.connectkit.api.annotation.mq.MQ;
import com.ruijie.rcos.sk.connectkit.api.annotation.mq.MqConsumer;

/**
 * Description: 身份中心消息
 * Copyright: Copyright (c) 2023
 * Company: Ruijie Co., Ltd.
 * Create Time: 2023年12月19日
 *
 * @author TING
 */
@MQ
@ApiGroup("iacMq")
public interface IacConsumerSPI {

    /**
     * 使登录失效
     *
     * @param invalidLoginDTO 推送的数据对象
     */
    @ApiAction("invalidLogin")
    void invalidLogin(InvalidLoginDTO invalidLoginDTO);

    /**
     * 被相同管理员登录踢出
     *
     * @param kickOutLoginBySameAdminDTO 推送的数据对象
     */
    @ApiAction("kickOutLoginBySameAdmin")
    void kickOutLoginBySameAdmin(KickOutLoginBySameAdminDTO kickOutLoginBySameAdminDTO);

    /**
     * 管理员变更
     *
     * @param adminChangedDTO 推送的数据对象
     */
    @ApiAction("adminChanged")
    void adminChanged(IacAdminChangedDTO adminChangedDTO);

    /**
     * 接收身份中心升级日志消息,记录审计日志
     * 
     * @param logMqInfoDTO 推送的数据对象
     */
    @ApiAction("sendLogToSub")
    @MqConsumer(name = "iacCDCAuditLog")
    void recordAuditLog(IacLogMqInfoDTO logMqInfoDTO);

    /**
     * 接收身份中心MQ通知告警信息
     *
     * @param iacAddAlarmDTO 添加告警MQ信息
     */
    @ApiAction("addAlarm")
    void addAlarm(IacAddAlarmDTO iacAddAlarmDTO);

    /**
     * 接收身份中心MQ通知解除告警信息
     *
     * @param iacReleaseAlarmDTO 解除告警MQ信息
     */
    @ApiAction("releaseAlarm")
    void releaseAlarm(IacReleaseAlarmDTO iacReleaseAlarmDTO);

    /**
     * 用户身份验证配置变更
     *
     * @param request 用户身份验证配置变更内容
     */
    @ApiAction("userIdentityConfigChange")
    void userIdentityConfigChange(IacUserIdentityConfigRequest request);

    /**
     * 客户端认证配置通知
     *
     * @param dto 客户端认证配置dto
     */
    @ApiAction("certifiedSecurityConfigUpdatedNotify")
    void certifiedSecurityConfigUpdatedNotify(IacClientAuthSecurityDTO dto);

    /**
     * 短信密码找回通知
     *
     * @param iacSmsPwdRecoverNotifyDTO 短信密码找回通知DTO
     */
    @ApiAction("smsPwdRecoverNotify")
    void smsPwdRecoverNotify(IacSmsPwdRecoverNotifyDTO iacSmsPwdRecoverNotifyDTO);

    /**
     * 登录认证方式变更
     *
     * @param iacAuthTypeDTO 推送的数据对象
     */
    @ApiAction("authTypeChange")
    void notifyClientInfo(IacAuthChangeDTO iacAuthTypeDTO);

    /**
     * 账号安全策略变更
     *
     * @param request 账号安全策略变更变更内容
     */
    @ApiAction("accountStrategyChange")
    void accountStrategyChange(IacAccountStrategyChangeDTO request);

    /**
     * 密码安全策略变更
     *
     * @param request 密码安全策略变更内容
     */
    @ApiAction("passwordStrategyChange")
    void passwordStrategyChange(IacPasswordStrategyChangeDTO request);

    /**
     * 解锁用户信息通知
     *
     * @param request 解锁用户信息通知内容
     */
    @ApiAction("unlockUsers")
    void unlockUsers(IacUnlockUserDTO request);

}

