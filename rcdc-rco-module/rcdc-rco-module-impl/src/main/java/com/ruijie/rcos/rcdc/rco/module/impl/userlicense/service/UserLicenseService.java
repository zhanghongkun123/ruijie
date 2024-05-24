package com.ruijie.rcos.rcdc.rco.module.impl.userlicense.service;

import com.ruijie.rcos.rcdc.clouddesktop.module.def.dto.userlicense.UserSessionDTO;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.enums.userlicense.ResourceTypeEnum;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.enums.userlicense.TerminalTypeEnum;
import com.ruijie.rcos.rcdc.license.module.def.enums.CbbLicenseDurationEnum;
import com.ruijie.rcos.rcdc.rco.module.impl.userlicense.enums.ClearSessionReasonTypeEnum;
import com.ruijie.rcos.sk.base.exception.BusinessException;

import java.util.List;
import java.util.UUID;

/**
 * Description: 用户登录业务处理
 * Copyright: Copyright (c) 2018
 * Company: Ruijie Co., Ltd.
 * Create Time: 2019/1/16
 *
 * @author linke
 */
public interface UserLicenseService {

    /**
     * 清空用户授权信息
     *
     * @param licenseType 授权类型
     * @param duration 授权持续类型
     * @return 清理用户授权条数
     */
    int clearUserAuthByLicenseTypeAndDuration(String licenseType, CbbLicenseDurationEnum duration);

    /**
     * 创建用户会话记录并占用申请授权
     * @param userSessionDTO 用户会话记录
     * @return 用户会话记录ID
     * @throws BusinessException 业务异常（授权申请失败）
     */
    UUID createUserSessionAndLicense(UserSessionDTO userSessionDTO) throws BusinessException;

    /**
     * 更新用户会话记录与授权
     * @param userId 用户ID
     * @param terminalId 终端ID
     * @param terminalType 终端类型
     * @param sessionInfoList 会话记录列表
     * @return 删除用户会话记录条数
     * @throws BusinessException 业务异常（授权申请失败）
     */
    int updateUserSessionAndLicense(UUID userId, String terminalId, TerminalTypeEnum terminalType, List<UserSessionDTO> sessionInfoList)
            throws BusinessException;

    /**
     * 根据会话连接关联资源进行会话与授权清理
     *
     * @param clearType 清理类型
     * @param resourceType 资源类型
     * @param resourceId 资源ID
     * @return 删除用户会话记录条数
     */
    int clearUserSessionAndLicenseByResource(ClearSessionReasonTypeEnum clearType, ResourceTypeEnum resourceType, UUID resourceId);

    /**
     * 根据会话连接关联客户端进行会话与授权清理
     *
     * @param clearType 清理类型
     * @param terminalType 客户端类型
     * @param terminalId 客户端ID
     * @return 删除用户会话记录条数
     */
    int clearUserSessionAndLicenseByTerminal(ClearSessionReasonTypeEnum clearType, TerminalTypeEnum terminalType, String terminalId);

    /**
     * 根据会话ID进行会话清理以及授权释放
     * @param sessionId 会话ID
     * @return 清理数量
     */
    int clearTimeoutReportUserSession(UUID sessionId);

    /**
     * 获取客户端所有会话用户ID列表
     *
     *
     * @param terminalType 终端类型
     * @param terminalId 终端ID
     * @return 客户端所有会话用户ID列表
     */
    List<UUID> findSessionUserIdListByTerminalId(TerminalTypeEnum terminalType, String terminalId);

    /**
     * 更新本集群网页版客户端用户会话信息
     * @param oldClusterId 旧集群ID
     * @param newClusterId 新集群ID
     */
    void updateWebClientUserSessionInfo(UUID oldClusterId, UUID newClusterId);
}
