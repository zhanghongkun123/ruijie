package com.ruijie.rcos.rcdc.rco.module.impl.certificationstrategy.service;

import java.util.List;
import java.util.UUID;

import com.ruijie.rcos.gss.sdk.iac.module.def.dto.user.IacUserDetailDTO;
import com.ruijie.rcos.rcdc.rco.module.def.api.dto.UserCertificationDTO;
import com.ruijie.rcos.sk.base.exception.BusinessException;

/**
 * Description: 用户信息服务类
 * Copyright: Copyright (c) 2021
 * Company: Ruijie Co., Ltd.
 * Create Time: 2021/6/29 23:13
 *
 * @author yxq
 */
public interface UserInfoService {
    /**
     * 根据用户ID获取用户名
     *
     * @param userId 用户ID
     * @return 用户名
     * @throws BusinessException 业务异常
     */
    String getUsernameById(UUID userId) throws BusinessException;

    /**
     * 获取用户列表根据用户状态与云桌面数量
     *
     * @param desktopNum 桌面数量
     * @return 用户名列表
     */
    List<String> findByDesktopNumGe(int desktopNum);


    /**
     * 根据用户名获取用户ID
     *
     * @param userName userName
     * @return 用户ID
     * @throws BusinessException 业务异常
     */
    UUID getUserIdByUserName(String userName) throws BusinessException;

    /**
     * 获取用户总数
     *
     * @return 数量
     */
    long findUserCount();

    /**
     * 根据用户ID，查询用户认证开启情况
     *
     * @param userId 用户ID
     * @return 用户认证开启情况
     */
    UserCertificationDTO getUserCertificationDTO(UUID userId);

    /**
     * 查询有桌面资源的用户列表(普通桌面+池桌面)
     *
     * @param usernameList 用户名列表
     * @return 用户名列表
     */
    List<String> getUserDesktopResource(List<String> usernameList);

    /**
     * 获取用户信息
     * @param userId userId
     * @return 实体
     */
    IacUserDetailDTO getDetail(UUID userId);
}
