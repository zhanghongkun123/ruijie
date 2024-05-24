package com.ruijie.rcos.rcdc.rco.module.web.ctrl.user.bactchtask;

import static com.ruijie.rcos.rcdc.clouddesktop.module.def.enums.CbbCloudDeskType.IDV;
import static com.ruijie.rcos.rcdc.clouddesktop.module.def.enums.CbbCloudDeskType.VDI;
import static org.apache.commons.lang3.StringUtils.isBlank;

import java.util.List;
import java.util.Objects;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

import com.google.common.collect.ImmutableList;
import com.ruijie.rcos.rcdc.rco.module.def.api.UwsDockingAPI;
import org.springframework.beans.BeanUtils;
import org.springframework.util.Assert;

import com.ruijie.rcos.gss.sdk.iac.module.def.dto.user.IacUpdateUserDTO;
import com.ruijie.rcos.gss.sdk.iac.module.def.dto.user.IacUserDetailDTO;
import com.ruijie.rcos.gss.sdk.iac.module.def.enums.IacUserStateEnum;
import com.ruijie.rcos.gss.sdk.iac.module.def.enums.IacUserTypeEnum;
import com.ruijie.rcos.gss.log.module.def.api.BaseAuditLogAPI;
import com.ruijie.rcos.gss.sdk.iac.module.def.api.IacUserMgmtAPI;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.api.CbbIDVDeskOperateAPI;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.dto.desk.CbbShutdownDeskIDVDTO;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.enums.CbbCloudDeskState;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.enums.CbbCloudDeskType;
import com.ruijie.rcos.rcdc.rco.module.def.api.UserDesktopMgmtAPI;
import com.ruijie.rcos.rcdc.rco.module.def.api.UserDesktopOperateAPI;
import com.ruijie.rcos.rcdc.rco.module.def.api.UserInfoAPI;
import com.ruijie.rcos.rcdc.rco.module.def.api.UserMgmtAPI;
import com.ruijie.rcos.rcdc.rco.module.def.api.dto.CloudDesktopDTO;
import com.ruijie.rcos.rcdc.rco.module.def.api.request.CloudDesktopShutdownRequest;
import com.ruijie.rcos.rcdc.rco.module.def.api.request.CloudDesktopStartRequest;
import com.ruijie.rcos.rcdc.rco.module.web.ctrl.user.UserBusinessKey;
import com.ruijie.rcos.rcdc.rco.module.web.ctrl.user.request.DisableUserPresetBatchTaskHandlerRequest;
import com.ruijie.rcos.rcdc.task.ext.module.def.batch.AbstractProgressAwareBatchTaskHandler;
import com.ruijie.rcos.sk.base.batch.BatchTaskFinishResult;
import com.ruijie.rcos.sk.base.batch.BatchTaskItem;
import com.ruijie.rcos.sk.base.batch.BatchTaskItemResult;
import com.ruijie.rcos.sk.base.batch.BatchTaskItemStatus;
import com.ruijie.rcos.sk.base.batch.BatchTaskStatus;
import com.ruijie.rcos.sk.base.batch.DefaultBatchTaskFinishResult;
import com.ruijie.rcos.sk.base.batch.DefaultBatchTaskItemResult;
import com.ruijie.rcos.sk.base.exception.BusinessException;
import com.ruijie.rcos.sk.base.log.Logger;
import com.ruijie.rcos.sk.base.log.LoggerFactory;

/**
 * Description: 批量禁用用户任务，包含断开会话和关闭关联桌面
 * Copyright: Copyright (c) 2021
 * Company: Ruijie Co., Ltd.
 * Create Time: 2022/1/17
 *
 * @author yangjinheng
 */
public class DisableUserPresetBatchTaskHandler extends AbstractProgressAwareBatchTaskHandler {
    private static final Logger LOGGER = LoggerFactory.getLogger(DisableUserPresetBatchTaskHandler.class);

    private BaseAuditLogAPI auditLogAPI;

    private IacUserMgmtAPI cbbUserAPI;

    private CbbIDVDeskOperateAPI cbbIDVDeskOperateAPI;

    private UserDesktopOperateAPI cloudDesktopOperateAPI;

    private UserDesktopMgmtAPI userDesktopMgmtAPI;

    private UserInfoAPI userInfoAPI;

    private UserMgmtAPI userMgmtAPI;

    private UwsDockingAPI uwsDockingAPI;

    private boolean isBatch = false;

    public boolean isBatch() {
        return isBatch;
    }

    public void setBatch(boolean batch) {
        isBatch = batch;
    }

    public DisableUserPresetBatchTaskHandler(DisableUserPresetBatchTaskHandlerRequest request) {
        super(request.getBatchTaskItemIterator());
        this.auditLogAPI = request.getAuditLogAPI();
        this.cbbUserAPI = request.getCbbUserAPI();
        this.cbbIDVDeskOperateAPI = request.getCbbIDVDeskOperateAPI();
        this.cloudDesktopOperateAPI = request.getCloudDesktopOperateAPI();
        this.userDesktopMgmtAPI = request.getUserDesktopMgmtAPI();
        this.userInfoAPI = request.getUserInfoAPI();
        this.userMgmtAPI = request.getUserMgmtAPI();
        this.uwsDockingAPI = request.getUwsDockingAPI();
    }

    private String threadUserName;

    @Override
    public BatchTaskItemResult processItem(BatchTaskItem batchTaskItem) throws BusinessException {
        Assert.notNull(batchTaskItem, "taskItem can not be null");

        UUID userId = batchTaskItem.getItemID();
        IacUserDetailDTO cbbUserDetailDTO = null;
        String userName = null;
        // 1.保存用户状态，2.推送用户状态，3.关闭虚机，4.关闭会话
        try {
            cbbUserDetailDTO = cbbUserAPI.getUserDetail(userId);
            userName = cbbUserDetailDTO.getUserName();
            userName = isBlank(userName) ? userId.toString() : userName;
            threadUserName = cbbUserDetailDTO.getUserName();

            if (cbbUserDetailDTO.getUserType() == IacUserTypeEnum.AD ||
                    cbbUserDetailDTO.getUserType() == IacUserTypeEnum.LDAP ||
                    cbbUserDetailDTO.getUserType() == IacUserTypeEnum.THIRD_PARTY) {
                throw new BusinessException(UserBusinessKey.RCDC_RCO_USER_DISABLE_FAIL_BY_USER_TYPE);
            }

            if (cbbUserDetailDTO.getUserState() == IacUserStateEnum.DISABLE) {
                return DefaultBatchTaskItemResult.builder().batchTaskItemStatus(BatchTaskItemStatus.SUCCESS)
                        .msgKey(UserBusinessKey.RCDC_RCO_USER_DISABLE_PRESET_SUC_LOG).msgArgs(userName).build();
            }

            cbbUserDetailDTO.setUserState(IacUserStateEnum.DISABLE);
            final IacUpdateUserDTO cbbUpdateUserDTO = new IacUpdateUserDTO();
            BeanUtils.copyProperties(cbbUserDetailDTO, cbbUpdateUserDTO);
            cbbUserAPI.updateUser(cbbUpdateUserDTO);
            auditLogAPI.recordLog(UserBusinessKey.RCDC_RCO_USER_DISABLE_UPDATE_SUCCESS_LOG, userName);
        } catch (BusinessException e) {
            LOGGER.error(String.format("禁用用户[%s]失败", userName), e);
            auditLogAPI.recordLog(UserBusinessKey.RCDC_RCO_USER_DISABLE_PRESET_FAIL_LOG, userName, e.getI18nMessage());
            throw new BusinessException(UserBusinessKey.RCDC_RCO_USER_DISABLE_PRESET_FAIL_RESULT, e, userName);
        }

        notifyTerminal(userId, cbbUserDetailDTO, userName);


        LOGGER.info("禁用用户关闭桌面，用户为[{}]", userName);
        boolean isSuccess = this.closeVm(cbbUserDetailDTO);
        if (!isSuccess) {
            throw new BusinessException(UserBusinessKey.RCDC_RCO_USER_DISABLE_CLOSE_VM_FAIL_RESULT, userName);
        }


        auditLogAPI.recordLog(UserBusinessKey.RCDC_RCO_USER_DISABLE_PRESET_SUC_LOG, userName);
        // 返回成功
        return DefaultBatchTaskItemResult.builder().batchTaskItemStatus(BatchTaskItemStatus.SUCCESS)
                .msgKey(UserBusinessKey.RCDC_RCO_USER_DISABLE_PRESET_SUC_LOG).msgArgs(userName).build();
    }

    private void notifyTerminal(UUID userId, IacUserDetailDTO cbbUserDetailDTO, String userName) {
        // UWS 客户端退出用户登录
        uwsDockingAPI.notifyUserDisabled(ImmutableList.of(userId));
        LOGGER.info("禁用用户主动推送用户状态，用户为[{}]", userName);
        userMgmtAPI.syncUserInfoToTerminal(cbbUserDetailDTO);
        LOGGER.info("禁用用户并退出终端会话，用户为[{}]", userName);
        userInfoAPI.userLogout(userId);
        //通知web端用户退出
        userInfoAPI.notifyWebUserLogout(userId);
    }

    private boolean closeVm(IacUserDetailDTO cbbUserDetailDTO) {
        Assert.notNull(cbbUserDetailDTO, "cbbUserDetailDTO is null");
        UUID userId = cbbUserDetailDTO.getId();
        String userName = cbbUserDetailDTO.getUserName();
        // 关闭用户关联的桌面
        String logFlag = null;
        boolean isSuccess = true;
        final List<CloudDesktopDTO> desktopList = userDesktopMgmtAPI.getAllDesktopByUserId(userId);
        for (CloudDesktopDTO desktop : desktopList) {
            try {
                logFlag = isBlank(desktop.getDesktopName()) ? desktop.getId().toString() : desktop.getDesktopName();
                if (!Objects.equals(desktop.getDesktopState(), CbbCloudDeskState.RUNNING.toString())
                        && !Objects.equals(desktop.getDesktopState(), CbbCloudDeskState.SLEEP.toString())) {
                    continue;
                }
                CbbCloudDeskType deskType = CbbCloudDeskType.valueOf(desktop.getDesktopCategory());
                LOGGER.info("准备下发关闭{}类型云桌面[id={}]命令", deskType.name(), desktop.getId());
                if (deskType == IDV) {
                    CbbShutdownDeskIDVDTO shutdownDeskIDVDTO = new CbbShutdownDeskIDVDTO();
                    shutdownDeskIDVDTO.setId(desktop.getId());
                    shutdownDeskIDVDTO.setIsForce(Boolean.FALSE);
                    shutdownDeskIDVDTO.setTimeout(TimeUnit.MINUTES.toMillis(5));
                    cbbIDVDeskOperateAPI.shutdownDeskIDV(shutdownDeskIDVDTO);
                    auditLogAPI.recordLog(UserBusinessKey.RCDC_RCO_USER_DISABLE_CLOSE_VM_SUCCESS_LOG, userName, logFlag);
                } else if (deskType == VDI) {
                    if (Objects.equals(desktop.getDesktopState(), CbbCloudDeskState.SLEEP.toString())) {
                        cloudDesktopOperateAPI.start(new CloudDesktopStartRequest(desktop.getCbbId()));
                    }
                    cloudDesktopOperateAPI.shutdown(new CloudDesktopShutdownRequest(desktop.getId(), Boolean.FALSE));
                    auditLogAPI.recordLog(UserBusinessKey.RCDC_RCO_USER_DISABLE_CLOSE_VM_SUCCESS_LOG, userName, logFlag);
                } else {
                    LOGGER.error("用户[{}]不支持对云桌面类型：[{}]，进行关闭", logFlag, deskType.name());
                }
            } catch (Exception e) {
                LOGGER.error(String.format("用户[%s]禁用成功，关闭桌面[%s]失败", userName, logFlag), e);
                String msg = e instanceof BusinessException ? ((BusinessException) e).getI18nMessage() : e.getMessage();
                auditLogAPI.recordLog(UserBusinessKey.RCDC_RCO_USER_DISABLE_CLOSE_VM_FAIL_LOG, userName, logFlag, msg);
                isSuccess = false;
            }
        }

        return isSuccess;
    }

    @Override
    public BatchTaskFinishResult onFinish(int successCount, int failCount) {
        if (isBatch) {
            return buildDefaultFinishResult(successCount, failCount, UserBusinessKey.RCDC_RCO_USER_DISABLE_PRESET_RESULT);
        }

        if (successCount == 1) {
            return DefaultBatchTaskFinishResult.builder().batchTaskStatus(BatchTaskStatus.SUCCESS)
                    .msgKey(UserBusinessKey.RCDC_RCO_USER_DISABLE_PRESET_SUCCESS_RESULT).msgArgs(new String[]{threadUserName}).build();
        } else {
            return DefaultBatchTaskFinishResult.builder().batchTaskStatus(BatchTaskStatus.FAILURE)
                    .msgKey(UserBusinessKey.RCDC_RCO_USER_DISABLE_PRESET_FAIL_RESULT).msgArgs(new String[]{threadUserName}).build();
        }
    }
}
