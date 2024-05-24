package com.ruijie.rcos.rcdc.rco.module.web.ctrl.user.bactchtask;

import com.google.common.collect.Lists;
import com.ruijie.rcos.gss.log.module.def.api.BaseAuditLogAPI;
import com.ruijie.rcos.gss.sdk.iac.module.def.api.IacSmsCertificationMgmtAPI;
import com.ruijie.rcos.gss.sdk.iac.module.def.api.IacUserMgmtAPI;
import com.ruijie.rcos.gss.sdk.iac.module.def.dto.user.IacUserDetailDTO;
import com.ruijie.rcos.gss.sdk.iac.module.def.enums.IacConfigRelatedType;
import com.ruijie.rcos.gss.sdk.iac.module.def.enums.IacUserStateEnum;
import com.ruijie.rcos.gss.sdk.iac.module.def.enums.IacUserTypeEnum;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.api.CbbDeskMgmtAPI;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.enums.CbbCloudDeskState;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.enums.CbbCloudDeskType;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.enums.desktoppool.DesktopPoolType;
import com.ruijie.rcos.rcdc.rca.module.def.api.RcaGroupMemberAPI;
import com.ruijie.rcos.rcdc.rca.module.def.api.RcaHostSessionAPI;
import com.ruijie.rcos.rcdc.rca.module.def.enums.RcaEnum;
import com.ruijie.rcos.rcdc.rco.module.def.api.*;
import com.ruijie.rcos.rcdc.rco.module.def.api.dto.CloudDesktopDTO;
import com.ruijie.rcos.rcdc.rco.module.def.api.dto.DesktopOpLogDTO;
import com.ruijie.rcos.rcdc.rco.module.def.api.enums.DeleteIDVDesktopOperateLogTypeEnums;
import com.ruijie.rcos.rcdc.rco.module.def.desktopsession.DesktopSessionDTO;
import com.ruijie.rcos.rcdc.rco.module.def.enums.UserCloudDeskTypeEnum;
import com.ruijie.rcos.rcdc.rco.module.def.enums.UserRoleEnum;
import com.ruijie.rcos.rcdc.rco.module.web.ctrl.user.UserBusinessKey;
import com.ruijie.rcos.rcdc.rco.module.web.service.SpringBeanHelper;
import com.ruijie.rcos.sk.base.batch.*;
import com.ruijie.rcos.sk.base.exception.BusinessException;
import com.ruijie.rcos.sk.base.log.Logger;
import com.ruijie.rcos.sk.base.log.LoggerFactory;
import com.ruijie.rcos.sk.modulekit.api.comm.IdRequest;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.util.Assert;

import java.util.*;
import java.util.stream.Collectors;

;

/**
 * Description: 删除用户批量任务处理器
 * Copyright: Copyright (c) 2018
 * Company: Ruijie Co., Ltd.
 * Create Time: 2019年1月11日
 *
 * @author jarman
 */
public class DeleteUserBatchTaskHandler extends AbstractBatchTaskHandler {

    private static final Logger LOGGER = LoggerFactory.getLogger(DeleteUserBatchTaskHandler.class);


    private BaseAuditLogAPI auditLogAPI;

    private static final String NAME_SEPARATOR = "、";


    private IacUserMgmtAPI userAPI;

    private boolean isBatch = true;

    private String userName;

    private UserDesktopMgmtAPI userDesktopMgmtAPI;

    private UserDesktopConfigAPI userDesktopConfigAPI;

    private UserMessageAPI userMessageAPI;

    private DesktopOpLogMgmtAPI desktopOpLogMgmtAPI;

    private UserHardwareCertificationAPI userHardwareCertificationAPI;

    private RccmManageAPI rccmManageAPI;

    private UwsDockingAPI uwsDockingAPI;

    private DesktopPoolUserMgmtAPI desktopPoolUserMgmtAPI;

    private UserDiskMgmtAPI userDiskMgmtAPI;

    private IacSmsCertificationMgmtAPI smsCertificationAPI;

    private CbbDeskMgmtAPI cbbDeskMgmtAPI;

    private RcaGroupMemberAPI rcaGroupMemberAPI;

    /**
     * 临时权限
     */
    private DesktopTempPermissionAPI desktopTempPermissionAPI;


    private DesktopSessionServiceAPI desktopSessionServiceAPI;

    private RcaHostSessionAPI rcaHostSessionAPI;

    public DeleteUserBatchTaskHandler(IacUserMgmtAPI userAPI, Iterator<? extends BatchTaskItem> iterator, BaseAuditLogAPI auditLogAPI) {
        super(iterator);
        Assert.notNull(auditLogAPI, "the auditLogAPI is null.");
        this.auditLogAPI = auditLogAPI;
        this.userAPI = userAPI;
        this.desktopPoolUserMgmtAPI = SpringBeanHelper.getBean(DesktopPoolUserMgmtAPI.class);
        this.smsCertificationAPI = SpringBeanHelper.getBean(IacSmsCertificationMgmtAPI.class);
        this.cbbDeskMgmtAPI = SpringBeanHelper.getBean(CbbDeskMgmtAPI.class);
        this.desktopSessionServiceAPI = SpringBeanHelper.getBean(DesktopSessionServiceAPI.class);
    }

    public void setUserDesktopConfigAPI(UserDesktopConfigAPI userDesktopConfigAPI) {
        this.userDesktopConfigAPI = userDesktopConfigAPI;
    }

    public void setUserDesktopMgmtAPI(UserDesktopMgmtAPI userDesktopMgmtAPI) {
        this.userDesktopMgmtAPI = userDesktopMgmtAPI;
    }

    public void setUserMessageAPI(UserMessageAPI userMessageAPI) {
        this.userMessageAPI = userMessageAPI;
    }

    public void setDesktopOpLogMgmtAPI(DesktopOpLogMgmtAPI desktopOpLogMgmtAPI) {
        this.desktopOpLogMgmtAPI = desktopOpLogMgmtAPI;
    }

    public void setUserHardwareCertificationAPI(UserHardwareCertificationAPI userHardwareCertificationAPI) {
        this.userHardwareCertificationAPI = userHardwareCertificationAPI;
    }

    public void setRccmManageAPI(RccmManageAPI rccmManageAPI) {
        this.rccmManageAPI = rccmManageAPI;
    }

    public void setIsBatch(boolean isBatch) {
        this.isBatch = isBatch;
    }

    public void setUwsDockingAPI(UwsDockingAPI uwsDockingAPI) {
        this.uwsDockingAPI = uwsDockingAPI;
    }

    public void setDiskPoolMgmtAPI(UserDiskMgmtAPI userDiskMgmtAPI) {
        this.userDiskMgmtAPI = userDiskMgmtAPI;
    }

    public DesktopTempPermissionAPI getDesktopTempPermissionAPI() {
        return desktopTempPermissionAPI;
    }

    public void setDesktopTempPermissionAPI(DesktopTempPermissionAPI desktopTempPermissionAPI) {
        this.desktopTempPermissionAPI = desktopTempPermissionAPI;
    }

    public RcaGroupMemberAPI getRcaGroupMemberAPI() {
        return rcaGroupMemberAPI;
    }

    public void setRcaGroupMemberAPI(RcaGroupMemberAPI rcaGroupMemberAPI) {
        this.rcaGroupMemberAPI = rcaGroupMemberAPI;
    }

    public void setRcaHostSessionAPI(RcaHostSessionAPI rcaHostSessionAPI) {
        this.rcaHostSessionAPI = rcaHostSessionAPI;
    }

    @Override
    public BatchTaskItemResult processItem(BatchTaskItem taskItem) throws BusinessException {
        Assert.notNull(taskItem, "taskItem can not be null");
        UUID userId = taskItem.getItemID();
        IacUserDetailDTO detailResponse = userAPI.getUserDetail(userId);
        String localUserName = detailResponse.getUserName();
        // 删除单条用户任务结果需要用到用户名
        userName = localUserName;
        if ((IacUserTypeEnum.AD == detailResponse.getUserType() || IacUserTypeEnum.THIRD_PARTY == detailResponse.getUserType()
            || IacUserTypeEnum.LDAP == detailResponse.getUserType()) && IacUserStateEnum.ENABLE == detailResponse.getUserState()) {
            // 为AD域用户，且是启用状态，不允许删除操作
            auditLogAPI.recordLog(UserBusinessKey.RCDC_RCO_USER_IS_AD_USER_NOT_ALLOW_DELETE, localUserName);
            throw new BusinessException(UserBusinessKey.RCDC_RCO_USER_IS_AD_USER_NOT_ALLOW_DELETE, new String[] {localUserName});
        }

        if (IacUserTypeEnum.AD == detailResponse.getUserType() && Boolean.TRUE.equals(detailResponse.getEnableDomainSync())) {
            // 为AD域用户，且存在映射关系，不允许删除操作
            auditLogAPI.recordLog(UserBusinessKey.RCDC_RCO_USER_IS_MAPPING_AD_USER_NOT_ALLOW_DELETE, localUserName);
            throw new BusinessException(UserBusinessKey.RCDC_RCO_USER_IS_MAPPING_AD_USER_NOT_ALLOW_DELETE, new String[] {localUserName});
        }

        if (UserRoleEnum.ADMIN.name().equals(detailResponse.getUserRole())) {
            // 如果是已经升级为管理员，不允许删除
            auditLogAPI.recordLog(UserBusinessKey.RCDC_RCO_USER_IS_ADMIN_NOT_ALLOW_DELETE, localUserName);
            throw new BusinessException(UserBusinessKey.RCDC_RCO_USER_IS_ADMIN_NOT_ALLOW_DELETE, new String[] {localUserName});
        }
        // 用户登录状态不允许删除
        boolean isUserHasLogin = userDesktopMgmtAPI.isUserHasLogin(userId);

        if (isUserHasLogin) {
            auditLogAPI.recordLog(UserBusinessKey.RCDC_RCO_USER_HAS_LOGIN_NOT_ALLOW_DELETE, localUserName);
            throw new BusinessException(UserBusinessKey.RCDC_RCO_USER_HAS_LOGIN_NOT_ALLOW_DELETE, new String[] {localUserName});
        }
        try {
            // 存在创建中的桌面不允许删除
            int desktopNum = userDesktopMgmtAPI.getCreatingDesktopNum(userId);

            if (desktopNum > 0) {
                auditLogAPI.recordLog(UserBusinessKey.RCDC_RCO_USER_CREATING_DESKTOP_NOT_ALLOW_DELETE, localUserName);
                throw new BusinessException(UserBusinessKey.RCDC_RCO_USER_CREATING_DESKTOP_NOT_ALLOW_DELETE, new String[] {localUserName});
            }

            // 清除用户绑定胖终端的镜像下载状态
            LOGGER.info("==== 清除用户绑定IDV/TCI终端的镜像下载状态 ====");
            userDesktopMgmtAPI.clearDownloadStateInfo(userId);

            // 删除关联的用户数据
            LOGGER.info("==== 删除用户关联的信息 ====");
            deleteRelativeData(userId);

            // 解绑与该用户绑定的云桌面
            LOGGER.info("==== 解除用户与云桌面的绑定信息 ====");
            userDesktopMgmtAPI.unbindCloudDeskFromUser(userId);

            // 解除与该用户绑定的池
            desktopPoolUserMgmtAPI.unbindUserAllDesktopPool(userId);

            // 解除与该用户绑定的磁盘池
            userDiskMgmtAPI.unbindUserAllDiskPool(userId, IacConfigRelatedType.USER);

            // 解除与该用户绑定的应用会话
            LOGGER.info("==== 解除用户与应用主机的绑定信息 ====");
            rcaHostSessionAPI.unbindUserAllHostSession(userId);

            // 删除用户数据
            LOGGER.info("==== 删除用户信息 ====");
            userAPI.deleteUser(taskItem.getItemID());

            deleteVisitorUserDesktopConfig(detailResponse.getUserType(), userId);
            userDesktopConfigAPI.deleteUserDesktopConfig(userId, UserCloudDeskTypeEnum.IDV);
            //删除VOI桌面配置
            userDesktopConfigAPI.deleteUserDesktopConfig(userId, UserCloudDeskTypeEnum.VOI);
            //删除用户临时权限配置
            desktopTempPermissionAPI.deleteDesktopTempPermissionByUserId(userId);

            rcaGroupMemberAPI.unbindAllAppGroup(userId, RcaEnum.GroupMemberType.USER, userName);

            auditLogAPI.recordLog(UserBusinessKey.RCDC_RCO_USER_DELETE_SUCCESS_LOG, localUserName);

            // 删除rccm用户集群缓存
            rccmManageAPI.delRccmUserCLuster(Collections.singletonList(userName), true);

            // 通知UWS人员被删除
            uwsDockingAPI.notifyUserDeleted(Collections.singletonList(detailResponse.getId()));
            return DefaultBatchTaskItemResult.builder().batchTaskItemStatus(BatchTaskItemStatus.SUCCESS)
                    .msgKey(UserBusinessKey.RCDC_RCO_USER_DELETE_RESULT_SUCCESS).msgArgs(new String[] {localUserName}).build();
        } catch (BusinessException e) {
            auditLogAPI.recordLog(UserBusinessKey.RCDC_RCO_USER_DELETE_FAIL_LOG, e, localUserName, e.getI18nMessage());
            throw new BusinessException(UserBusinessKey.RCDC_RCO_USER_DELETE_RESULT_FAIL, e, localUserName, e.getI18nMessage());
        }

    }

    private void deleteVisitorUserDesktopConfig(IacUserTypeEnum userType, UUID userId) {
        if (userType == IacUserTypeEnum.VISITOR) {
            userDesktopConfigAPI.deleteVisitorUserDesktopConfig(userId);
        }
    }

    @Override
    public BatchTaskFinishResult onFinish(int successCount, int failCount) {

        // 批量删除用户
        if (isBatch) {
            return buildDefaultFinishResult(successCount, failCount, UserBusinessKey.RCDC_RCO_USER_DELETE_RESULT);
        }

        // 删除单条用户
        if (successCount == 1) {
            return DefaultBatchTaskFinishResult.builder().msgKey(UserBusinessKey.RCDC_RCO_USER_DELETE_SINGLE_SUCCESS_RESULT)
                    .msgArgs(new String[] {userName}).batchTaskStatus(BatchTaskStatus.SUCCESS).build();
        } else {
            return DefaultBatchTaskFinishResult.builder().msgKey(UserBusinessKey.RCDC_RCO_USER_DELETE_SINGLE_FAIL_RESULT)
                    .msgArgs(new String[] {userName}).batchTaskStatus(BatchTaskStatus.FAILURE).build();
        }
    }

    private void deleteRelativeData(UUID userId) throws BusinessException {
        List<CloudDesktopDTO> desktopDTOList = userDesktopMgmtAPI.getAllDesktopByUserId(userId);
        // 检查关联桌面是否都已关机
        checkBeforeDeleteDeskData(desktopDTOList);
        // 检查是否有多会话桌面在线会话记录存在
        checkDesktopSessionBeforeDelete(userId);
        for (CloudDesktopDTO desktopDTO : desktopDTOList) {
            if (Objects.nonNull(desktopDTO.getDesktopPoolId())) {
                // 池桌面不删，静态池要进入维护模式
                cbbDeskMgmtAPI.changeDeskMaintenanceModel(Lists.newArrayList(desktopDTO.getCbbId()), true);
                continue;
            }

            // 删除云桌面
            DesktopOpLogDTO desktopOpLogRequest =
                    desktopOpLogMgmtAPI.buildDeleteDesktopOpLogRequest(desktopDTO.getCbbId(), DeleteIDVDesktopOperateLogTypeEnums.DELETE_USER);
            userDesktopMgmtAPI.deleteDesktop(desktopDTO.getId(), CbbCloudDeskType.valueOf(desktopDTO.getDesktopCategory()));

            // 删除桌面关联用户消息
            userMessageAPI.deleteByDesktopId(new IdRequest(desktopDTO.getCbbId()));
            // 删除桌面关联的应用ISO挂载信息
            userDesktopMgmtAPI.deleteDesktopAppConfig(desktopDTO.getCbbId());

            desktopOpLogMgmtAPI.saveOperateLog(desktopOpLogRequest);
        }
    }

    private void checkBeforeDeleteDeskData(List<CloudDesktopDTO> desktopDTOList) throws BusinessException {
        // 非池的VDI桌面运行中不允许删除用户，池桌面运行中需要判断是否有在线会话记录
        List<String> nameList = desktopDTOList.stream().filter(item -> CbbCloudDeskType.valueOf(item.getDesktopCategory()) == CbbCloudDeskType.VDI
                        && !Objects.equals(item.getDesktopState(), CbbCloudDeskState.CLOSE.name())
                        && Objects.equals(item.getDesktopPoolType(), DesktopPoolType.COMMON.name())).map(CloudDesktopDTO::getDesktopName)
                .collect(Collectors.toList());
        if (CollectionUtils.isNotEmpty(nameList)) {
            throw new BusinessException(UserBusinessKey.RCDC_RCO_USER_DELETE_ERROR_DESK_NOT_CLOSE, StringUtils.join(nameList, NAME_SEPARATOR));
        }
    }

    private void checkDesktopSessionBeforeDelete(UUID userId) throws BusinessException {
        List<DesktopSessionDTO> desktopSessionDTOList = desktopSessionServiceAPI.findByUserId(userId);
        List<UUID> deskIdList = desktopSessionDTOList.stream().map(DesktopSessionDTO::getDeskId).filter(Objects::nonNull).distinct()
                .collect(Collectors.toList());
        if (CollectionUtils.isEmpty(deskIdList)) {
            return;
        }
        List<CloudDesktopDTO> cloudDesktopDTOList = userDesktopMgmtAPI.listDesktopByDesktopIds(deskIdList);
        List<String> nameList = cloudDesktopDTOList.stream().map(CloudDesktopDTO::getDesktopName).collect(Collectors.toList());
        throw new BusinessException(UserBusinessKey.RCDC_RCO_USER_DELETE_ERROR_EXIST_DESK_SESSION, StringUtils.join(nameList, NAME_SEPARATOR));
    }
}
