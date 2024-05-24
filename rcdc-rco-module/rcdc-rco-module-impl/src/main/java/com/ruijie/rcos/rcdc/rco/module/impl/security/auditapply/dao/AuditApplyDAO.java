package com.ruijie.rcos.rcdc.rco.module.impl.security.auditapply.dao;

import com.ruijie.rcos.rcdc.rco.module.def.security.auditapply.enums.AuditApplyStateEnum;
import com.ruijie.rcos.rcdc.rco.module.def.security.auditapply.enums.AuditApplyTypeEnum;
import com.ruijie.rcos.rcdc.rco.module.impl.security.auditapply.entity.AuditApplyEntity;
import com.ruijie.rcos.sk.modulekit.api.ds.SkyEngineJpaRepository;
import com.ruijie.rcos.sk.pagekit.api.PageQueryDAO;

import java.util.Date;
import java.util.List;
import java.util.UUID;

/**
 * Description: 文件流转审计申请单DAO
 * Copyright: Copyright (c) 2022
 * Company: Ruijie Co., Ltd.
 * Create Time: 2022年10月20日
 *
 * @author lihengjing
 */
public interface AuditApplyDAO extends SkyEngineJpaRepository<AuditApplyEntity, UUID>, PageQueryDAO<AuditApplyEntity> {

    /**
     * 根据申请单序列号查询申请单
     * 
     * @param applySerialNumber 申请单序列号
     * @return 申请单
     */
    AuditApplyEntity findByApplySerialNumber(String applySerialNumber);

    /**
     * 查询时间范围内产生的状态为入参状态的申请单列表
     *
     * @param applyType 申请类型
     * @param desktopId 云桌面ID
     * @param auditApplyStateEnumArr 申请单状态列表
     * @param dayStartTime 时间范围开始时间
     * @param dayEndTime 时间范围结束时间
     * @return 申请单列表
     */
    List<AuditApplyEntity> findByApplyTypeAndDesktopIdAndStateNotInAndCreateTimeGreaterThanAndCreateTimeLessThan(AuditApplyTypeEnum applyType,
            UUID desktopId, AuditApplyStateEnum[] auditApplyStateEnumArr, Date dayStartTime, Date dayEndTime);

    /**
     * 根据用户-桌面ID查询申请单列表
     *
     * @param userId 用户ID
     * @param desktopId 桌面ID
     * @return 申请单列表
     */
    List<AuditApplyEntity> findByUserIdAndDesktopIdOrderByCreateTimeDesc(UUID userId, UUID desktopId);

    /**
     * 根据申请单状态查询申请单列表
     *
     * @param auditFileApplyState 申请单状态
     * @return 申请单列表
     */
    List<AuditApplyEntity> findByState(AuditApplyStateEnum auditFileApplyState);

    /**
     * 查询更新时间小于清理时间的申请单
     *
     * @param date 清理时间
     * @return 申请单列表
     */
    List<AuditApplyEntity> findByUpdateTimeLessThan(Date date);

    /**
     * 查询时间范围内产生的状态为入参状态的申请单列表
     *
     * @param applyType 申请类型
     * @param desktopId 云桌面ID
     * @param auditApplyState 申请单状态
     * @return 申请单列表
     */
    List<AuditApplyEntity> findByApplyTypeAndDesktopIdAndState(AuditApplyTypeEnum applyType, UUID desktopId, AuditApplyStateEnum auditApplyState);

    /**
     * 根据申请单状态列表查询申请单列表
     *
     * @param auditFileApplyStateList 申请单状态
     * @return 申请单列表
     */
    List<AuditApplyEntity> findByStateIn(List<AuditApplyStateEnum> auditFileApplyStateList);
}
