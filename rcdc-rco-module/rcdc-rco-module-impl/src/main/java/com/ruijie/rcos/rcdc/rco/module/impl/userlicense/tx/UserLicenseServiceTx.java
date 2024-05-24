package com.ruijie.rcos.rcdc.rco.module.impl.userlicense.tx;

import com.ruijie.rcos.rcdc.license.module.def.enums.CbbLicenseDurationEnum;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.dto.userlicense.UserSessionDTO;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.enums.userlicense.ResourceTypeEnum;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.enums.userlicense.TerminalTypeEnum;
import com.ruijie.rcos.rcdc.rco.module.impl.userlicense.entity.UserSessionEntity;
import com.ruijie.rcos.rcdc.rco.module.impl.userlicense.enums.ClearSessionReasonTypeEnum;
import com.ruijie.rcos.sk.base.exception.BusinessException;

import java.util.List;
import java.util.UUID;

/**
 * Description: 用户授权事务处理类
 * Copyright: Copyright (c) 2024
 * Company: Ruijie Co., Ltd.
 * Create Time: 2024年02月6日
 *
 * @author lihengjing
 */
public interface UserLicenseServiceTx {

    /**
     * 清空用户授权信息
     * 
     * @param userIdList 用户ID列表
     * @param licenseType 授权类型
     * @param duration 授权持续类型
     * @return 清理用户授权条数
     */
    int clearUserAuthByLicenseTypeAndDuration(List<UUID> userIdList, String licenseType, CbbLicenseDurationEnum duration);

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
     * 根据会话记录进行清理
     * @param entity 会话记录实体
     * @param clearType 清理原因
     */
    void clearUserSession(UserSessionEntity entity, ClearSessionReasonTypeEnum clearType);
}
