package com.ruijie.rcos.rcdc.rco.module.impl.certificationstrategy.service;

import com.ruijie.rcos.rcdc.rco.module.def.certificationstrategy.dto.PwdStrategyDTO;

/**
 * Description: 认证策略
 * Copyright: Copyright (c) 2021
 * Company: Ruijie Co., Ltd.
 * Create Time: 2021-03-09
 *
 * @author zhang.zhiwen
 */
public interface CertificationStrategyService {

    /**
     * 获取全局参数表配置的密码认证信息
     *
     * @return PwdStrategyDTO
     */
    PwdStrategyDTO getPwdStrategyParameter();

    /**
     * 基于策略等级校验密码是否合理
     *
     * @param password 密码
     * @return Boolean 是否通过校验
     */
    Boolean validatePwdByStrategyLevel(String password);

    /**
     * 修改用户密码同步给管理员后，管理员首次登录需要修改密码
     * 若用户密码不符合管理员密码规则，则需要修改
     *
     * @param newPassword 用户密码
     * @return 是否需要修改密码
     */
    Boolean shouldUpdateAdminPwdFirstLogin(String newPassword);
}
