package com.ruijie.rcos.rcdc.rco.module.impl.tx;

import java.util.List;

import com.ruijie.rcos.rcdc.rco.module.impl.entity.AuthenticationEntity;
import com.ruijie.rcos.sk.base.exception.BusinessException;

/**
 * Description: 用户登录认证信息事务接口
 * Copyright: Copyright (c) 2021
 * Company: Ruijie Co., Ltd.
 * Create Time: 2021/8/16 22:48
 *
 * @author yxq
 */
public interface UpgradeServiceTx {
    /**
     * 迁移用户锁定信息和安全红线配置信息，并且修改全局表中S2字段信息
     * 
     * @param authenticationEntityList 用户锁定信息
     * @throws BusinessException 业务异常
     */
    void upgradeFromS2(List<AuthenticationEntity> authenticationEntityList) throws BusinessException;

    /**
     * 迁移用户锁定信息和安全红线配置信息，但是不需要修改全局表中S2字段信息
     *
     * @param authenticationEntityList 用户锁定信息
     *                                 @throws BusinessException 业务异常
     */
    void upgradeFromS2WithoutGlobalValue(List<AuthenticationEntity> authenticationEntityList) throws BusinessException;

    /**
     * 从其他版本升级，增加安全红线配置信息
     * @throws BusinessException 业务异常
     */
    void upgradeFromOtherVersion() throws BusinessException;

    /**
     * 添加EnableForceUpdatePassword字段
     * @throws BusinessException 业务异常
     */
    void addEnableForceUpdatePassword() throws BusinessException;

    /**
     * 更新密码强度等级
     */
    void upgradePwdLevel();

}
