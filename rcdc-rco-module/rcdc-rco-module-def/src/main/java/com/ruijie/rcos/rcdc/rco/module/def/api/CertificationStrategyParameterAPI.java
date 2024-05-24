package com.ruijie.rcos.rcdc.rco.module.def.api;

import java.util.List;
import java.util.UUID;

import com.ruijie.rcos.rcdc.rco.module.def.certificationstrategy.dto.PwdStrategyDTO;
import com.ruijie.rcos.sk.base.exception.BusinessException;

/**
 * <p>Title: CertificationStrategyParameterAPI</p>
 * <p>Description: 认证策略 </p>
 * <p>Copyright: Ruijie Co., Ltd. (c) 2021</p>
 * <p>@Author: zhang.zhiwen</p>
 * <p>@Date: 2021/3/9 14:51</p>
 */
public interface CertificationStrategyParameterAPI {

    /**
     * 获取密码认证策略信息
     *
     * @return PwdStrategyDTO 密码认证策略信息
     */
    PwdStrategyDTO getPwdStrategy();

    /**
     * 查询终端锁定状态
     * @param realTerminalId 终端真实的ID
     * @return 锁定状态
     */
    boolean getTerminalLockedStatusById(UUID realTerminalId);

    /**
     * 解锁终端管理密码
     *
     * @param terminalId 终端ID（mac地址）
     * @throws BusinessException 业务异常
     */
    void unlockTerminalManagePwd(String terminalId) throws BusinessException;

    /**
     * 通知所有在线终端IDV、TCI终端策略信息变更
     *
     */
    void notifyPwdStrategyModified();

    /**
     * 通知用户所关联在线IDV、TCI用户解锁
     * @param userIdList 用户id列表
     */
    void notifyTerminalUserUnlocked(List<UUID> userIdList);

    /**
     * 通知用户所关联在线IDV、TCI用户解锁
     * @param userId 用户id
     */
    void notifyTerminalUserUnlocked(UUID userId);


    /**
     * 基于策略等级校验是否需要修改密码
     *
     * @param password 密码
     * @return Boolean 是否通过校验
     */
    Boolean isNeedUpdatePassword(String password);

    /**
     * 获取首次登录是否需要强制修改密码（无论是否符合密码规则）
     * @return Boolean 是否强制修改密码
     */
    Boolean isNeedForceUpdatePwd();
}
