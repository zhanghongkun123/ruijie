package com.ruijie.rcos.rcdc.rco.module.impl.security.auditprinter.dao;


import com.ruijie.rcos.rcdc.rco.module.impl.security.auditprinter.entity.AuditFilePrintInfoEntity;
import com.ruijie.rcos.sk.modulekit.api.ds.SkyEngineJpaRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

/**
 * Description: 安全打印机申请单DAO
 * Copyright: Copyright (c) 2022
 * Company: Ruijie Co., Ltd.
 * Create Time: 2022年10月20日
 *
 * @author lihengjing
 */
public interface AuditFilePrintInfoDAO extends SkyEngineJpaRepository<AuditFilePrintInfoEntity, UUID> {

    /**
     * 根据文件ID获取打印记录ID
     * @param fileId 文件ID
     * @return 打印记录ID
     */
    Optional<AuditFilePrintInfoEntity> findByFileId(UUID fileId);

    /**
     * 根据申请单ID删除文件打印记录
     *
     * @param applyId 申请单ID
     */
    void deleteByFileId(UUID applyId);

    /**
     * 根据文件列表ID获取打印记录
     * @param fileIdList 文件ID列表
     * @return 打印记录列表
     */
    List<AuditFilePrintInfoEntity> findByFileIdIn(List<UUID> fileIdList);
}
