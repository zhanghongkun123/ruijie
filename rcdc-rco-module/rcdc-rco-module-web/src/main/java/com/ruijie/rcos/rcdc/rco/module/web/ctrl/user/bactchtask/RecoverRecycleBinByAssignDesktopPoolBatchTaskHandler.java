package com.ruijie.rcos.rcdc.rco.module.web.ctrl.user.bactchtask;

import com.google.common.collect.Sets;
import com.ruijie.rcos.gss.sdk.iac.module.def.enums.IacUserTypeEnum;
import com.ruijie.rcos.gss.log.module.def.api.BaseAuditLogAPI;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.api.CbbVDIDeskMgmtAPI;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.api.CbbVDIDeskStrategyMgmtAPI;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.dto.CbbDeskDTO;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.dto.desktoppool.CbbDesktopPoolDTO;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.dto.strategy.CbbDeskStrategyDTO;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.enums.CbbDesktopSessionType;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.enums.desktoppool.CbbDesktopPoolModel;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.enums.desktoppool.CbbDesktopPoolState;
import com.ruijie.rcos.rcdc.rco.module.def.BusinessKey;
import com.ruijie.rcos.rcdc.rco.module.def.api.DesktopAPI;
import com.ruijie.rcos.rcdc.rco.module.def.api.DesktopPoolUserMgmtAPI;
import com.ruijie.rcos.rcdc.rco.module.def.api.UserDesktopMgmtAPI;
import com.ruijie.rcos.rcdc.rco.module.def.api.UserRecycleBinMgmtAPI;
import com.ruijie.rcos.rcdc.rco.module.def.api.dto.CloudDesktopDetailDTO;
import com.ruijie.rcos.rcdc.rco.module.web.ctrl.clouddesktop.batchtask.BatchTaskUtils;
import com.ruijie.rcos.rcdc.rco.module.web.ctrl.desktoppool.DesktopPoolBusinessKey;
import com.ruijie.rcos.rcdc.rco.module.web.service.SpringBeanHelper;
import com.ruijie.rcos.sk.base.batch.*;
import com.ruijie.rcos.sk.base.exception.BusinessException;
import com.ruijie.rcos.sk.base.i18n.LocaleI18nResolver;
import com.ruijie.rcos.sk.base.log.Logger;
import com.ruijie.rcos.sk.base.log.LoggerFactory;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.lang.Nullable;
import org.springframework.util.Assert;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.UUID;

/**
 * Description: 指定桌面池回收站恢复批量任务
 * Copyright: Copyright (c) 2023
 * Company: Ruijie Co., Ltd.
 * Create Time: 2023/9/12
 *
 * @author linke
 */
public class RecoverRecycleBinByAssignDesktopPoolBatchTaskHandler extends AbstractBatchTaskHandler {

    private static final Logger LOGGER = LoggerFactory.getLogger(RecoverRecycleBinByAssignDesktopPoolBatchTaskHandler.class);

    private static final Set<CbbDesktopPoolState> ALLOW_RECOVER_POOL_STATE_SET = Sets.newHashSet(CbbDesktopPoolState.UPDATING,
            CbbDesktopPoolState.AVAILABLE);

    private CbbDesktopPoolDTO targetDesktopPoolDTO;

    private UserRecycleBinMgmtAPI userRecycleBinMgmtAPI;

    private BaseAuditLogAPI auditLogAPI;

    private UserDesktopMgmtAPI userDesktopMgmtAPI;

    private DesktopPoolUserMgmtAPI desktopPoolUserMgmtAPI;

    private CbbVDIDeskStrategyMgmtAPI cbbVDIDeskStrategyMgmtAPI;

    private CbbVDIDeskMgmtAPI cbbVDIDeskMgmtAPI;

    private DesktopAPI desktopAPI;

    public RecoverRecycleBinByAssignDesktopPoolBatchTaskHandler(Iterator<? extends BatchTaskItem> batchTaskItemIterator,
                                                                CbbDesktopPoolDTO targetDesktopPoolDTO) {
        super(batchTaskItemIterator);
        this.targetDesktopPoolDTO = targetDesktopPoolDTO;
        this.userRecycleBinMgmtAPI = SpringBeanHelper.getBean(UserRecycleBinMgmtAPI.class);
        this.auditLogAPI = SpringBeanHelper.getBean(BaseAuditLogAPI.class);
        this.userDesktopMgmtAPI = SpringBeanHelper.getBean(UserDesktopMgmtAPI.class);
        this.desktopPoolUserMgmtAPI = SpringBeanHelper.getBean(DesktopPoolUserMgmtAPI.class);
        this.cbbVDIDeskStrategyMgmtAPI = SpringBeanHelper.getBean(CbbVDIDeskStrategyMgmtAPI.class);
        this.cbbVDIDeskMgmtAPI = SpringBeanHelper.getBean(CbbVDIDeskMgmtAPI.class);
        this.desktopAPI = SpringBeanHelper.getBean(DesktopAPI.class);
    }

    @Override
    public BatchTaskItemResult processItem(BatchTaskItem taskItem) throws BusinessException {
        Assert.notNull(taskItem, "taskItem can not be null");
        UUID desktopId = taskItem.getItemID();
        CloudDesktopDetailDTO desktopDetailDTO = null;
        String desktopPoolName = targetDesktopPoolDTO.getName();

        try {
            // 获取云桌面信息
            desktopDetailDTO = userDesktopMgmtAPI.getDesktopDetailById(desktopId);
        } catch (BusinessException e) {
            LOGGER.error("获取云桌面[{}]信息异常", desktopId, e);
            failResult(e, desktopId.toString(), desktopPoolName, e.getI18nMessage());
        }

        // 访客桌面不能指定静态池恢复
        if (Objects.equals(desktopDetailDTO.getUserType(), IacUserTypeEnum.VISITOR.name())) {
            String message = LocaleI18nResolver.resolve(BusinessKey.RCDC_RCO_RECOVER_ASSIGN_POOL_NOT_SUPPORT_VISITOR);
            failResult(desktopDetailDTO.getDesktopName(), desktopPoolName, message);
        }

        // 校验目标桌面池是否时静态池
        if (targetDesktopPoolDTO.getPoolModel() != CbbDesktopPoolModel.STATIC) {
            String message = LocaleI18nResolver.resolve(BusinessKey.RCDC_RCO_RECOVER_ASSIGN_POOL_TARGET_MUST_POOL);
            failResult(desktopDetailDTO.getDesktopName(), desktopPoolName, message);
        }

        if (!ALLOW_RECOVER_POOL_STATE_SET.contains(targetDesktopPoolDTO.getPoolState())) {
            String message = LocaleI18nResolver.resolve(BusinessKey.RCDC_RCO_RECOVER_ASSIGN_POOL_POOL_STATE_ERROR);
            failResult(desktopDetailDTO.getDesktopName(), desktopPoolName, message);
        }

        CbbDeskStrategyDTO poolDeskStrategyDTO = cbbVDIDeskStrategyMgmtAPI.getDeskStrategy(targetDesktopPoolDTO.getStrategyId());
        // 云桌面策略中云桌面类型必须是相同的，同为个性或者同为还原
        if (!Objects.equals(poolDeskStrategyDTO.getPattern().name(), desktopDetailDTO.getDesktopType())) {
            // 云桌面配置的云桌面策略的云桌面类型与桌面池中的不一致
            String message = LocaleI18nResolver.resolve(BusinessKey.RCDC_RCO_RECOVER_ASSIGN_POOL_STRATEGY_NOT_SAME_TYPE);
            failResult(desktopDetailDTO.getDesktopName(), desktopPoolName, message);
        }
        // 判断会话类型是否相符
        checkDesktopPool(desktopId, targetDesktopPoolDTO);

        List<UUID> userIdList = new ArrayList<>();
        UUID userId = desktopDetailDTO.getUserId();
        // 判断用户在桌面池中是否已有绑定的桌面
        if (Objects.nonNull(userId) && desktopPoolUserMgmtAPI.checkUserBindDesktopInPool(targetDesktopPoolDTO.getId(), userId)) {
            String message = LocaleI18nResolver.resolve(BusinessKey.RCDC_RCO_RECOVER_ASSIGN_POOL_USER_HAD_BIND_DESKTOP,
                    desktopDetailDTO.getUserName(), desktopPoolName);
            failResult(desktopDetailDTO.getDesktopName(), desktopPoolName, message);
        }
        // 多会话桌面，需根据t_rco_host_user中桌面关联的用户列表判断是否有已绑定桌面
        if (CbbDesktopSessionType.MULTIPLE == desktopDetailDTO.getSessionType()) {
            userIdList = desktopAPI.findUserIdByDeskId(desktopDetailDTO.getId());
            if (!CollectionUtils.isEmpty(userIdList)
                    && desktopPoolUserMgmtAPI.checkUserIdListBindDesktopInPool(targetDesktopPoolDTO.getId(), userIdList)) {
                String message = LocaleI18nResolver.resolve(DesktopPoolBusinessKey.RCDC_RCO_MOVE_DESKTOP_USER_ALREADY_BIND_IN_POOL);
                failResult(desktopDetailDTO.getDesktopName(), desktopPoolName, message);
            }
        }
        userIdList.add(desktopDetailDTO.getUserId());

        try {
            userRecycleBinMgmtAPI.recoverAssignDesktopPool(desktopId, userIdList, targetDesktopPoolDTO.getId());
        } catch (Exception e) {
            LOGGER.error("指定静态池[{}]恢复云桌面[{}]信息异常", desktopPoolName, desktopDetailDTO.getDesktopName(), e);
            String msg = e instanceof BusinessException ? ((BusinessException) e).getI18nMessage() : e.getMessage();
            failResult(e, desktopDetailDTO.getDesktopName(), desktopPoolName, msg);
        }

        auditLogAPI.recordLog(BusinessKey.RCDC_RCO_RECOVER_ASSIGN_POOL_SUCCESS_LOG, desktopDetailDTO.getDesktopName(), desktopPoolName);
        return DefaultBatchTaskItemResult.builder().batchTaskItemStatus(BatchTaskItemStatus.SUCCESS)
                .msgKey(BusinessKey.RCDC_RCO_RECOVER_ASSIGN_POOL_SUCCESS_LOG)
                .msgArgs(desktopDetailDTO.getDesktopName(), desktopPoolName).build();
    }

    @Override
    public BatchTaskFinishResult onFinish(int successCount, int failCount) {
        return BatchTaskUtils.buildBatchTaskFinishResult(successCount, failCount, BusinessKey.RCDC_RCO_RECOVER_ASSIGN_POOL_BATCH_TASK_RESULT);
    }

    private void failResult(String desktopName, String desktopPoolName, String message) throws BusinessException {
        failResult(null, desktopName, desktopPoolName, message);
    }

    private void failResult(@Nullable Exception e, String desktopName, String desktopPoolName, String message) throws BusinessException {
        auditLogAPI.recordLog(BusinessKey.RCDC_RCO_RECOVER_ASSIGN_POOL_FAIL, desktopName, desktopPoolName, message);
        throw new BusinessException(BusinessKey.RCDC_RCO_RECOVER_ASSIGN_POOL_FAIL, e, desktopName, desktopPoolName, message);
    }

    private void checkDesktopPool(UUID desktopId, CbbDesktopPoolDTO cbbDesktopPoolDTO) throws BusinessException {
        CbbDeskDTO deskVDI = cbbVDIDeskMgmtAPI.getDeskVDI(desktopId);
        boolean isEqualDesktopPoolType = deskVDI.getDeskType().name().equals(cbbDesktopPoolDTO.getPoolType().name());
        boolean isEqualSessionType = deskVDI.getSessionType() == cbbDesktopPoolDTO.getSessionType();
        if (!isEqualSessionType || !isEqualDesktopPoolType) {
            String message = LocaleI18nResolver.resolve(BusinessKey.RCDC_RCO_DESKTOP_RECOVER_VDI_SESSIONTYPE_STATIC, deskVDI.getName());
            auditLogAPI.recordLog(BusinessKey.RCDC_RCO_RECOVER_ASSIGN_POOL_FAIL, deskVDI.getName(), cbbDesktopPoolDTO.getName(), message);
            throw new BusinessException(BusinessKey.RCDC_RCO_DESKTOP_RECOVER_VDI_SESSIONTYPE_STATIC, deskVDI.getName());
        }
    }
}
