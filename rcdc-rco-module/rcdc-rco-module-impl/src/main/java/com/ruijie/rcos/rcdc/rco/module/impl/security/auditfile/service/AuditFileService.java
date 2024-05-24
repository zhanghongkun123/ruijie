package com.ruijie.rcos.rcdc.rco.module.impl.security.auditfile.service;

import com.ruijie.rcos.rcdc.clouddesktop.module.def.dto.extstorage.CbbLocalExternalStorageDTO;
import com.ruijie.rcos.rcdc.rco.module.def.constants.ThrowingConsumer;
import com.ruijie.rcos.rcdc.rco.module.def.security.auditapply.dto.AuditApplyDetailDTO;
import com.ruijie.rcos.rcdc.rco.module.def.security.auditfile.dto.AuditFileDTO;
import com.ruijie.rcos.rcdc.rco.module.def.security.auditfile.dto.AuditFileGlobalConfigDTO;
import com.ruijie.rcos.rcdc.rco.module.def.security.auditfile.dto.AuditFileStrategyDTO;
import com.ruijie.rcos.sk.base.exception.BusinessException;
import com.ruijie.rcos.sk.base.lockable.LockableExecutor;

import java.util.Date;
import java.util.UUID;
import java.util.function.BiConsumer;

/**
 * Description: 文件流转审计管理接口
 * Copyright: Copyright (c) 2022
 * Company: Ruijie Co., Ltd.
 * Create Time: 2022年10月20日
 *
 * @author lihengjing
 */
public interface AuditFileService {

    /**
     * 检查文件流转审计申请是否产生告警
     * 
     * @param auditApplyDetailDTO 文件流转审计申请单详情
     * @throws BusinessException 业务异常
     */
    void checkAuditFileApplyAlarm(AuditApplyDetailDTO auditApplyDetailDTO) throws BusinessException;

    /**
     * 保存申请单告警并关闭申请单
     * 
     * @param auditApplyDetailDTO 申请单详情
     * @param alarmTypeKey 告警国际化key
     * @param args 告警国际化参数
     * @throws BusinessException 业务异常
     */
    void alarmAndCloseApply(AuditApplyDetailDTO auditApplyDetailDTO, String alarmTypeKey, String... args) throws BusinessException;

    /**
     * 保存告警信息
     * 
     * @param isFatalAlarm 是否限制
     * @param auditApplyDetailDTO 申请单详情
     * @param alarmContent 告警信息
     */
    void saveAlarm(boolean isFatalAlarm, AuditApplyDetailDTO auditApplyDetailDTO, String alarmContent);

    /**
     * 检查是否开启文件流转审计功能
     *
     * @param deskId 云桌面ID
     * @throws BusinessException 业务异常
     */
    void checkEnableAuditFile(UUID deskId) throws BusinessException;

    /**
     * 根据云桌面ID获得文件流转审计策略
     *
     * @param deskId 云桌面ID
     * @return 文件流转审计策略对象
     * @throws BusinessException 业务异常
     */
    AuditFileStrategyDTO obtainAuditFileStrategy(UUID deskId) throws BusinessException;

    /**
     * 根据申请单号保存空间不足告警
     * 
     * @param applySerialNumber 申请单号
     */
    void saveFileStorageSpaceNotEnoughAlarm(String applySerialNumber);

    /**
     * 编辑文件流转审计全局策略
     * 
     * @param auditFileGlobalConfigDTO 文件流转审计全局策略对象
     */
    void editAuditFileGlobalStrategy(AuditFileGlobalConfigDTO auditFileGlobalConfigDTO);

    /**
     * 将所有状态赋值（申请单状态、文件状态、审批流程状态）
     *
     * @param auditApplyDetailDTO 申请单详情
     * @throws BusinessException 业务异常
     */
    void fillAuditFileApplyDetailDTOAllState(AuditApplyDetailDTO auditApplyDetailDTO) throws BusinessException;

    /**
     * 根据文件ID查询
     *
     * @param fileId 文件ID
     * @return 文件信息
     * @throws BusinessException 业务异常
     */
    AuditFileDTO findAuditFileById(UUID fileId) throws BusinessException;

    /**
     * 获得文件流转审计全局配置
     *
     * @return 文件流转审计全局策略对象
     */
    AuditFileGlobalConfigDTO obtainAuditFileGlobalConfig();

    /**
     * 校验文件流转的外置存储状态是否可用
     * @return 外置存储信息
     * @throws BusinessException 业务异常
     */
    CbbLocalExternalStorageDTO checkAuditFileGlobalExtStorageState() throws BusinessException;

    /**
     * 根据文件ID处理文件上传完成通知
     * @param fileId 文件ID
     * @param applyCreateTime 文件申请单创建时间
     * @throws BusinessException 业务异常
     * @throws InterruptedException 中断异常
     */
    void handleUploadedByFileId(UUID fileId, Date applyCreateTime) throws BusinessException, InterruptedException;

    /**
     * 初始化文件流转审计中文件状态
     */
    void initAuditFileState();

    /**
     * 删除申请单下的文件
     * @param applyId 申请单ID
     * @param runnable 执行者
     * @throws BusinessException 业务异常
     */
    void deleteAuditApplyFiles(UUID applyId, LockableExecutor.LockableRunnable runnable) throws BusinessException;
}
