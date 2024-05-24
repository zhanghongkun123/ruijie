package com.ruijie.rcos.rcdc.rco.module.def.api;

import java.util.List;
import java.util.UUID;

import com.ruijie.rcos.rcdc.rco.module.def.security.auditapply.dto.AuditApplyDetailDTO;
import com.ruijie.rcos.rcdc.rco.module.def.security.auditapply.dto.ViewAuditApplyDTO;
import com.ruijie.rcos.rcdc.rco.module.def.security.auditapply.dto.AuditApplyAuditLogDTO;
import com.ruijie.rcos.sk.base.exception.BusinessException;
import com.ruijie.rcos.sk.pagekit.api.PageQueryRequest;
import com.ruijie.rcos.sk.pagekit.api.PageQueryResponse;

/**
 * Description: 文件导出审批功能管理API
 * Copyright: Copyright (c) 2022
 * Company: Ruijie Co., Ltd.
 * Create Time: 2022年10月20日
 *
 * @author lihengjing
 */
public interface AuditApplyMgmtAPI {

    /**
     * 创建文件导出审批申请单
     *
     * @param deskId 云桌面Id
     * @param auditApplyDetailDTO 文件导出审批申请请求对象
     * @return 文件导出审批申请单详情
     * @throws BusinessException 业务异常
     */
    AuditApplyDetailDTO createAuditApply(UUID deskId, AuditApplyDetailDTO auditApplyDetailDTO) throws BusinessException;

    /**
     * 审批文件导出审批申请单
     * @param auditApplyAuditLogDTO 文件导出审批审批记录对象
     * @throws BusinessException 业务异常
     * @return 申请单序列号
     */
    String handleAuditApplyByAuditor(AuditApplyAuditLogDTO auditApplyAuditLogDTO) throws BusinessException;

    /**
     * 根据申请UUID获取申请单详情
     * @param applyId 申请单UUID
     * @return 申请单详情
     * @throws BusinessException 业务异常
     */
    AuditApplyDetailDTO findAuditApplyDetailById(UUID applyId) throws BusinessException;

    /**
     * 根据申请UUID撤回申请
     * @param deskId 桌面ID
     * @param applyId 申请单UUID
     * @throws BusinessException 业务异常
     */
    void cancelAuditFileApply(UUID deskId, UUID applyId) throws BusinessException;

    /**
     * 废弃申请单
     * @param applyId 申请单UUID
     * @param reason 终止原因
     * @throws BusinessException 业务异常
     */
    void discardAuditApply(UUID applyId, String reason) throws BusinessException;

    /**
     * 根据用户-桌面ID查询申请单列表
     *
     * @param userId 用户ID
     * @param desktopId 桌面ID
     * @return 申请单列表
     */
    List<AuditApplyDetailDTO> findAuditFileApplyByUserIdDesktopId(UUID userId, UUID desktopId);

    /**
     * 获取文件导出审批申请列表
     *
     * @param request 分页复杂查询
     * @return 分页列表
     * @throws BusinessException 业务异常
     */
    PageQueryResponse<ViewAuditApplyDTO> auditApplyPageQuery(PageQueryRequest request) throws BusinessException;

    /**
     * 处理上传文件完成状态（单个文件上传完成通知）
     *
     * @param deskId 桌面ID
     * @param applyId 申请单ID
     * @param fileId 文件ID
     * @throws BusinessException 业务异常
     */
    void handleAuditFileApplyUploaded(UUID deskId, UUID applyId, UUID fileId) throws BusinessException;

    /**
     * 申请单解除告警
     * @param alarmId 告警ID
     * @param businessId 业务ID
     * @throws BusinessException 业务异常
     */
    void applyRelease(UUID alarmId, String businessId) throws BusinessException;
}
