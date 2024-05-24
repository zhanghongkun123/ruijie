package com.ruijie.rcos.rcdc.rco.module.impl.security.auditapply.dao;

import com.ruijie.rcos.rcdc.rco.module.def.security.auditapply.enums.AuditApplyAuditLogStateEnum;
import com.ruijie.rcos.rcdc.rco.module.impl.security.auditapply.entity.AuditApplyAuditLogEntity;
import com.ruijie.rcos.sk.modulekit.api.ds.SkyEngineJpaRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

/**
 * Description: 文件流转审计审批流转日志DAO
 * Copyright: Copyright (c) 2022
 * Company: Ruijie Co., Ltd.
 * Create Time: 2022年10月20日
 *
 * @author lihengjing
 */
public interface AuditApplyAuditLogDAO extends SkyEngineJpaRepository<AuditApplyAuditLogEntity, UUID> {

    /**
     * 根据申请单UUID查询关联的审批流程日志
     * @param applyId 申请单UUID
     * @return 申请单关联的审批流程日志列表
     */
    List<AuditApplyAuditLogEntity> findByApplyId(UUID applyId);

    /**
     * 根据申请单UUID与审批员ID查询关联的审批流程日志
     *
     * @param applyId   申请单UUID
     * @param auditorId 审批员ID
     * @return 申请单关联的审批流程日志实体
     */
    Optional<AuditApplyAuditLogEntity> findByApplyIdAndAuditorId(UUID applyId, UUID auditorId);

    /**
     * 根据申请ID列表查询
     *
     * @param applyIdList 申请ID列表
     * @return 申请单关联的审批流程日志列表
     */
    List<AuditApplyAuditLogEntity> findByApplyIdIn(List<UUID> applyIdList);

    /**
     * 根据申请ID与审批人角色查询
     * @param applyId 申请UUID
     * @param roleId 角色ID
     * @return 申请单关联的审批流程日志实体
     */
    Optional<AuditApplyAuditLogEntity> findByApplyIdAndRoleId(UUID applyId, UUID roleId);

    /**
     * 根据申请ID与审批流程状态查询
     *
     * @param applyId      申请UUID
     * @param auditorState 审批流程状态
     * @return 申请单关联的审批流程日志列表
     */
    List<AuditApplyAuditLogEntity> findByApplyIdAndAuditorState(UUID applyId, AuditApplyAuditLogStateEnum auditorState);

    /**
     * 根据申请单ID删除审批记录
     *
     * @param applyId 申请单ID
     */
    void deleteAllByApplyId(UUID applyId);
}
