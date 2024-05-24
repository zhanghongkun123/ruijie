package com.ruijie.rcos.rcdc.rco.module.impl.security.auditapply.service;

import com.ruijie.rcos.rcdc.rco.module.def.security.auditapply.dto.AuditApplyDTO;
import com.ruijie.rcos.rcdc.rco.module.def.security.auditapply.dto.AuditApplyDetailDTO;
import com.ruijie.rcos.rcdc.rco.module.def.security.auditapply.dto.ViewAuditApplyDTO;
import com.ruijie.rcos.rcdc.rco.module.def.security.auditapply.dto.AuditApplyAuditLogDTO;
import com.ruijie.rcos.rcdc.rco.module.def.security.auditapply.enums.AuditApplyStateEnum;
import com.ruijie.rcos.sk.base.exception.BusinessException;
import com.ruijie.rcos.sk.pagekit.api.PageQueryAPI;
import com.ruijie.rcos.sk.pagekit.api.PageQueryRequest;
import com.ruijie.rcos.sk.pagekit.api.PageQueryResponse;

import java.util.Date;
import java.util.List;
import java.util.UUID;

/**
 * Description: 文件流转审计管理接口
 * Copyright: Copyright (c) 2022
 * Company: Ruijie Co., Ltd.
 * Create Time: 2022年10月20日
 *
 * @author lihengjing
 */
public interface AuditApplyService extends PageQueryAPI<ViewAuditApplyDTO> {

    /**
     * 生成文件流转审计申请单号（非UUID）
     *
     * @return 申请单号（申请单序列号）
     */
    String generateApplySerialNumber();

    /**
     * 检查申请单是否允许处理
     *
     * @param auditApplyAuditLogDTO 申请流程处理意见信息
     * @throws BusinessException 业务异常
     */
    void checkAuditApplyCanHandle(AuditApplyAuditLogDTO auditApplyAuditLogDTO) throws BusinessException;

    /**
     * 自动审批申请单
     * @param applyId 申请单ID
     * @throws BusinessException 业务异常
     */
    void autoApproveAuditFileApply(UUID applyId) throws BusinessException;

    /**
     * 检查申请单文件是否都上传完成
     * 上传完成更新申请单状态 并且 通知客户端状态发生变化
     * @param deskId 桌面ID
     * @param applyId 申请单ID
     * @throws BusinessException 业务异常
     */
    void checkAllAuditFileUploaded(UUID deskId, UUID applyId) throws BusinessException;

    /**
     * 根据申请UUID获取申请单详情
     * @param applyId 申请单UUID
     * @return 申请单详情
     * @throws BusinessException 业务异常
     */
    AuditApplyDetailDTO findAuditApplyDetailById(UUID applyId) throws BusinessException;

    /**
     * 根据申请UUID获取申请单信息
     * @param applyId 申请单UUID
     * @return 申请单详情
     * @throws BusinessException 业务异常
     */
    AuditApplyDTO findAuditApplyById(UUID applyId) throws BusinessException;

    /**
     * 更新申请单状态
     *
     * @param id        申请单ID
     * @param stateEnum 申请单状态
     * @throws BusinessException 业务异常
     */
    void updateAuditApplyState(UUID id, AuditApplyStateEnum stateEnum) throws BusinessException;

    /**
     * 根据申请UUID撤回申请
     * @param applyId 申请单UUID
     * @throws BusinessException 业务异常
     */
    void cancelAuditApply(UUID applyId) throws BusinessException;

    /**
     * 废弃申请单
     * @param applyId 申请单UUID
     * @param reason 终止原因
     * @throws BusinessException 业务异常
     */
    void discardAuditApply(UUID applyId, String reason) throws BusinessException;

    /**
     * 废弃申请单成功
     * @param applyId 申请单UUID
     * @throws BusinessException 业务异常
     */
    void discardAuditApplyHandler(UUID applyId) throws BusinessException;

    /**
     * 废弃申请单校验
     * @param applyId 申请单UUID
     * @throws BusinessException 业务异常
     */
    void validateDiscardAuditApply(UUID applyId) throws BusinessException;

    /**
     * 根据用户-桌面ID查询申请单列表
     *
     * @param userId 用户ID
     * @param desktopId 桌面ID
     * @return 申请单列表
     */
    List<AuditApplyDetailDTO> findAuditApplyListByUserIdDesktopId(UUID userId, UUID desktopId);

    /**
     * 查询更新时间小于清理时间的申请单
     *
     * @param updateTime 清理时间
     * @return 申请单列表
     * @throws BusinessException 业务异常
     */
    List<AuditApplyDetailDTO> findAuditApplyDetailListByUpdateTimeLessThan(Date updateTime) throws BusinessException;

    /**
     * 根据申请单状态查询申请单列表
     *
     * @param auditFileApplyState 申请单状态
     * @return 申请单列表
     * @throws BusinessException 业务异常
     */
    List<AuditApplyDetailDTO> findAuditApplyDetailListByState(AuditApplyStateEnum auditFileApplyState) throws BusinessException;

    /**
     * 根据申请单状态列表查询申请单列表
     *
     * @param  auditFileApplyStateList 申请单状态列表
     * @return 申请单列表
     * @throws BusinessException 业务异常
     */
    List<AuditApplyDetailDTO> findAuditApplyListByStateIn(List<AuditApplyStateEnum> auditFileApplyStateList) throws BusinessException;

    /**
     * 导出报表查询
     *
     * @param pageQueryRequest pageQueryRequest
     * @return PageQueryResponse<AuditApplyDTO>
     * @throws BusinessException 业务异常
     */
    PageQueryResponse<AuditApplyDTO> pageQueryForExport(PageQueryRequest pageQueryRequest) throws BusinessException;

    /**
     * 更新申请单关联告警信息
     * @param applyId 申请单ID
     * @param alarmIds 告警ID集合
     */
    void updateAuditApplyAlarmIds(UUID applyId, String alarmIds);
}
