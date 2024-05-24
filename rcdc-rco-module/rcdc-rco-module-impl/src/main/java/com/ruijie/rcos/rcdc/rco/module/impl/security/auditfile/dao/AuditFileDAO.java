package com.ruijie.rcos.rcdc.rco.module.impl.security.auditfile.dao;

import com.ruijie.rcos.rcdc.rco.module.def.security.auditfile.enums.AuditFileStateEnum;
import com.ruijie.rcos.rcdc.rco.module.impl.security.auditfile.entity.AuditFileEntity;
import com.ruijie.rcos.sk.modulekit.api.ds.SkyEngineJpaRepository;

import java.util.List;
import java.util.UUID;

/**
 * Description: 文件导出审批关联文件DAO
 * Copyright: Copyright (c) 2022
 * Company: Ruijie Co., Ltd.
 * Create Time: 2022年10月20日
 *
 * @author lihengjing
 */
public interface AuditFileDAO extends SkyEngineJpaRepository<AuditFileEntity, UUID> {

    /**
     * 根据申请单UUID查询关联的文件列表
     * @param applyId 申请单UUID
     * @return 申请单关联的文件列表
     */
    List<AuditFileEntity> findByApplyId(UUID applyId);

    /**
     * 查询申请单关联的状态为入参状态的文件列表
     *
     * @param applyIdArr        申请单ID列表
     * @param auditFileStateArr 查询文件状态列表
     * @return 所有申请单关联的文件列表
     */
    List<AuditFileEntity> findByApplyIdInAndFileStateNotIn(UUID[] applyIdArr, AuditFileStateEnum[] auditFileStateArr);

    /**
     * 根据申请ID列表查询
     *
     * @param applyIdList 申请ID列表
     * @return 审批记录列表
     */
    List<AuditFileEntity> findByApplyIdIn(List<UUID> applyIdList);

    /**
     * 根据申请单ID删除文件记录
     *
     * @param applyId 申请单ID
     */
    void deleteAllByApplyId(UUID applyId);

    /**
     * 根据文件单状态获取文件单列表
     * @param fileState 文件状态
     * @return 文件单列表
     */
    List<AuditFileEntity> findByFileState(AuditFileStateEnum fileState);
}
