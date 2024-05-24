package com.ruijie.rcos.rcdc.rco.module.impl.certificationstrategy.service;

import java.util.UUID;

import com.ruijie.rcos.rcdc.rco.module.def.api.enums.CertificationTypeEnum;
import com.ruijie.rcos.rcdc.rco.module.def.certificationstrategy.dto.AuthenticationDTO;

/**
 * Description: 用户登录认证信息服务类
 * Copyright: Copyright (c) 2018
 * Company: Ruijie Co., Ltd.
 * Create Time: 2021/03/11
 *
 * @author linke
 */
public interface AuthenticationService {


    /**
     * 解锁用户、管理员或者终端
     * @param resourceId 用户id
     * @param type 类型
     */
    void unlock(UUID resourceId, CertificationTypeEnum type);

    /**
     * 修改用户登录认证信息
     *
     * @param authenticationDTO updateUserAuthenticationDTO
     */
    void updateAuthentication(AuthenticationDTO authenticationDTO);

    /**
     * 根据资源ID删除和类型
     * @param resourceId 资源id
     * @param type 类型
     */
    void deleteByResourceId(UUID resourceId, CertificationTypeEnum type);

    /**
     * 获取资源锁定状态
     * @param resourceId  资源ID
     * @param type 类型
     * @return boolean 锁定状态
     */
    boolean getLockedStatusByIdAndType(UUID resourceId, CertificationTypeEnum type);


}
