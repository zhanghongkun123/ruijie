package com.ruijie.rcos.rcdc.rco.module.web.ctrl.user.bactchtask;

import com.google.common.collect.Sets;
import com.ruijie.rcos.gss.log.module.def.api.BaseAuditLogAPI;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.api.CbbDesktopPoolMgmtAPI;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.dto.desktoppool.CbbDesktopPoolDTO;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.enums.CbbCloudDeskPattern;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.enums.CbbCloudDeskType;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.enums.CbbDesktopSessionType;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.enums.desktoppool.CbbDesktopPoolModel;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.enums.desktoppool.CbbDesktopPoolType;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.enums.desktoppool.DesktopPoolType;
import com.ruijie.rcos.rcdc.rco.module.def.BusinessKey;
import com.ruijie.rcos.rcdc.rco.module.def.api.DesktopPoolMgmtAPI;
import com.ruijie.rcos.rcdc.rco.module.def.api.DesktopPoolUserMgmtAPI;
import com.ruijie.rcos.rcdc.rco.module.def.api.UserDesktopMgmtAPI;
import com.ruijie.rcos.rcdc.rco.module.def.api.dto.CloudDesktopDetailDTO;
import com.ruijie.rcos.rcdc.rco.module.def.desktoppool.dto.DesktopPoolBasicDTO;
import com.ruijie.rcos.rcdc.rco.module.web.ctrl.clouddesktop.batchtask.BatchTaskUtils;
import com.ruijie.rcos.rcdc.rco.module.web.service.SpringBeanHelper;
import com.ruijie.rcos.sk.base.batch.*;
import com.ruijie.rcos.sk.base.exception.BusinessException;
import com.ruijie.rcos.sk.base.i18n.LocaleI18nResolver;
import com.ruijie.rcos.sk.base.log.Logger;
import com.ruijie.rcos.sk.base.log.LoggerFactory;
import org.springframework.util.Assert;

import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.UUID;

/**
 * Description: 移动分组批任务
 * Copyright: Copyright (c) 2023
 * Company: Ruijie Co., Ltd
 * Create Time: 2023/9/6
 *
 * @author linke
 */
public class MoveDesktopBatchTaskHandler extends AbstractBatchTaskHandler {

    private static final Logger LOGGER = LoggerFactory.getLogger(MoveDesktopBatchTaskHandler.class);

    private BaseAuditLogAPI auditLogAPI;

    private DesktopPoolUserMgmtAPI desktopPoolUserMgmtAPI;

    private UserDesktopMgmtAPI userDesktopMgmtAPI;

    private CbbDesktopPoolMgmtAPI cbbDesktopPoolMgmtAPI;

    private UUID desktopPoolId;

    private DesktopPoolMgmtAPI desktopPoolMgmtAPI;

    private static final String WIN_SERVER = "SERVER";

    public MoveDesktopBatchTaskHandler(List<? extends BatchTaskItem> batchTaskItemList, UUID desktopPoolId) {
        super(batchTaskItemList);
        this.desktopPoolId = desktopPoolId;
        auditLogAPI = SpringBeanHelper.getBean(BaseAuditLogAPI.class);
        desktopPoolUserMgmtAPI = SpringBeanHelper.getBean(DesktopPoolUserMgmtAPI.class);
        userDesktopMgmtAPI = SpringBeanHelper.getBean(UserDesktopMgmtAPI.class);
        cbbDesktopPoolMgmtAPI = SpringBeanHelper.getBean(CbbDesktopPoolMgmtAPI.class);
        desktopPoolMgmtAPI = SpringBeanHelper.getBean(DesktopPoolMgmtAPI.class);
    }

    @Override
    public BatchTaskItemResult processItem(BatchTaskItem batchTaskItem) throws BusinessException {
        Assert.notNull(batchTaskItem, "batchTaskItem can not be null");

        UUID desktopId = batchTaskItem.getItemID();
        CloudDesktopDetailDTO desktopDetailDTO;
        try {
            // 获取云桌面信息
            desktopDetailDTO = userDesktopMgmtAPI.getDesktopDetailById(desktopId);
        } catch (BusinessException e) {
            LOGGER.error("获取云桌面[{}]信息异常", desktopId, e);
            auditLogAPI.recordLog(BusinessKey.RCDC_RCO_MOVE_DESKTOP_FAIL, desktopId.toString(), e.getI18nMessage());
            throw new BusinessException(BusinessKey.RCDC_RCO_MOVE_DESKTOP_FAIL, e, desktopId.toString(), e.getI18nMessage());
        }
        // 获取池信息
        CbbDesktopPoolDTO desktopPoolDTO = null;
        DesktopPoolBasicDTO desktopPoolBasicDTO = null;
        try {
            desktopPoolDTO = cbbDesktopPoolMgmtAPI.getDesktopPoolDetail(desktopPoolId);
            desktopPoolBasicDTO = desktopPoolMgmtAPI.getDesktopPoolBasicById(desktopPoolId);
            // 设置云桌面类型
            desktopPoolDTO.setDesktopType(desktopPoolBasicDTO.getDesktopType());
        } catch (BusinessException e) {
            LOGGER.error("获取桌面池[{}]信息异常", desktopPoolId, e);
            failResult(e, desktopDetailDTO.getDesktopName(), desktopPoolId.toString(), e.getI18nMessage());
        }

        // 同一个池直接成功
        if (Objects.equals(desktopDetailDTO.getDesktopPoolId(), desktopPoolId)) {
            auditLogAPI.recordLog(BusinessKey.RCDC_RCO_MOVE_DESKTOP_SUCCESS_ALREADY_IN_POOL, desktopDetailDTO.getDesktopName(),
                    desktopPoolDTO.getName());
            return DefaultBatchTaskItemResult.builder().batchTaskItemStatus(BatchTaskItemStatus.SUCCESS)
                    .msgKey(BusinessKey.RCDC_RCO_MOVE_DESKTOP_SUCCESS_ALREADY_IN_POOL)
                    .msgArgs(desktopDetailDTO.getDesktopName(), desktopPoolDTO.getName()).build();
        }

        if (desktopPoolBasicDTO != null) {
            // - 非Window server的操作系統桌面，不能移动到多会话桌面池
            checkSystem(desktopDetailDTO, desktopPoolBasicDTO);
        }

        try {
            desktopPoolUserMgmtAPI.moveDesktop(desktopDetailDTO, desktopPoolDTO);
        } catch (Exception e) {
            LOGGER.error("移动桌面[{}]到桌面池[{}]异常", desktopDetailDTO.getDesktopName(), desktopPoolDTO.getName(), e);
            String msg = e instanceof BusinessException ? ((BusinessException) e).getI18nMessage() : e.getMessage();
            failResult(e,desktopDetailDTO.getDesktopName(), desktopPoolDTO.getName(), msg);
        }

        auditLogAPI.recordLog(BusinessKey.RCDC_RCO_MOVE_DESKTOP_SUCCESS_LOG, desktopDetailDTO.getDesktopName(), desktopPoolDTO.getName());
        return DefaultBatchTaskItemResult.builder().batchTaskItemStatus(BatchTaskItemStatus.SUCCESS)
                .msgKey(BusinessKey.RCDC_RCO_MOVE_DESKTOP_SUCCESS_LOG)
                .msgArgs(desktopDetailDTO.getDesktopName(), desktopPoolDTO.getName()).build();
    }

    private void checkSystem(CloudDesktopDetailDTO desktopDetailDTO, DesktopPoolBasicDTO desktopPoolDTO) throws BusinessException {
        boolean isWindowsServer = !Objects.isNull(desktopDetailDTO.getOsVersion()) &&
                desktopDetailDTO.getOsVersion().toLowerCase().contains(WIN_SERVER.toLowerCase());
        boolean isMultiple = CbbDesktopSessionType.MULTIPLE == desktopPoolDTO.getSessionType();
        // - 非Windows server的操作系统桌面，禁止移动到多会话桌面池
        if (!isWindowsServer && isMultiple) {
            String message = LocaleI18nResolver.resolve(BusinessKey.RCDC_RCO_MOVE_DESKTOP_THIRD_PARTY_DESKTOP_POOL_SINGLE_ERROR);
            auditLogAPI.recordLog(BusinessKey.RCDC_RCO_MOVE_DESKTOP_FAIL_LOG, desktopDetailDTO.getDesktopName(),
                    desktopPoolDTO.getName(), message);
            throw new BusinessException(BusinessKey.RCDC_RCO_MOVE_DESKTOP_FAIL_LOG, desktopDetailDTO.getDesktopName(),
                    desktopPoolDTO.getName(), message);
        }
    }

    @Override
    public BatchTaskFinishResult onFinish(int successCount, int failCount) {
        return BatchTaskUtils.buildBatchTaskFinishResult(successCount, failCount, BusinessKey.RCDC_RCO_MOVE_DESKTOP_BATCH_TASK_RESULT);
    }

    private void failResult(Exception e, String desktopName, String desktopPoolName, String message) throws BusinessException {
        auditLogAPI.recordLog(BusinessKey.RCDC_RCO_MOVE_DESKTOP_FAIL_LOG, desktopName, desktopPoolName, message);
        throw new BusinessException(BusinessKey.RCDC_RCO_MOVE_DESKTOP_FAIL_LOG, e, desktopName, desktopPoolName, message);
    }
}
