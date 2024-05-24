package com.ruijie.rcos.rcdc.rco.module.def.api;

import com.ruijie.rcos.rcdc.clouddesktop.module.def.dto.userlicense.UserSessionDTO;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.enums.userlicense.TerminalTypeEnum;
import com.ruijie.rcos.sk.base.exception.BusinessException;

import java.util.List;
import java.util.UUID;

/**
 * Description: 用户并发授权API
 * Copyright: Copyright (c) 2024
 * Company: Ruijie Co., Ltd.
 * Create Time: 2024年02月6日
 *
 * @author lihengjing
 */
public interface UserLicenseAPI {

    /**
     * 创建用户会话记录并占用申请授权
     * 
     * @param userSessionDTO 用户会话记录
     * @return 用户会话记录ID
     * @throws BusinessException 业务异常（授权申请失败）
     */
    UUID createUserSessionAndLicense(UserSessionDTO userSessionDTO) throws BusinessException;

    /**
     * 更新用户会话记录与授权
     * 
     * @param userId 用户ID
     * @param terminalId 终端ID
     * @param terminalType 终端类型
     * @param sessionInfoList 会话记录列表
     * @throws BusinessException 业务异常（授权申请、释放失败）
     */
    void updateUserSessionAndLicense(UUID userId, String terminalId, TerminalTypeEnum terminalType, List<UserSessionDTO> sessionInfoList)
            throws BusinessException;

    /**
     * 根据会话连接关联客户端进行会话与授权清理
     *
     * @param terminalType 终端类型
     * @param terminalId 客户端ID
     * @throws BusinessException 业务异常（释放失败）
     */
    void clearUserSessionByTerminalId(TerminalTypeEnum terminalType, String terminalId) throws BusinessException;

    /**
     * 获取网页版客户端所有会话用户ID列表
     *
     *
     * @param terminalType 终端类型
     * @param terminalId 终端ID
     * @return 网页版客户端所有会话用户ID列表
     */
    List<UUID> findSessionUserIdListByTerminalId(TerminalTypeEnum terminalType, String terminalId);
}
