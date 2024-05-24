package com.ruijie.rcos.rcdc.rco.module.openapi.rest.user.thread;

import com.alibaba.fastjson.JSONObject;
import com.ruijie.rcos.gss.sdk.iac.module.def.api.IacImportUserAPI;
import com.ruijie.rcos.gss.sdk.iac.module.def.api.IacUserGroupMgmtAPI;
import com.ruijie.rcos.gss.sdk.iac.module.def.api.IacUserIdentityConfigMgmtAPI;
import com.ruijie.rcos.gss.sdk.iac.module.def.api.IacUserMgmtAPI;
import com.ruijie.rcos.gss.sdk.iac.module.def.api.request.user.IacImportUserRequest;
import com.ruijie.rcos.gss.sdk.iac.module.def.api.request.user.IacUserIdentityConfigRequest;
import com.ruijie.rcos.gss.sdk.iac.module.def.dto.user.IacImportUserResultDTO;
import com.ruijie.rcos.gss.sdk.iac.module.def.dto.user.IacUpdateUserDTO;
import com.ruijie.rcos.gss.sdk.iac.module.def.dto.user.IacUserDetailDTO;
import com.ruijie.rcos.gss.sdk.iac.module.def.enums.IacConfigRelatedType;
import com.ruijie.rcos.gss.sdk.iac.module.def.enums.IacUserImportEnum;
import com.ruijie.rcos.gss.sdk.iac.module.def.enums.IacUserLoginIdentityLevelEnum;
import com.ruijie.rcos.gss.sdk.iac.module.def.enums.IacUserTypeEnum;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.api.CbbDeskSpecAPI;
import com.ruijie.rcos.rcdc.rco.module.def.api.DataSyncAPI;
import com.ruijie.rcos.rcdc.rco.module.def.api.OpenApiTaskInfoAPI;
import com.ruijie.rcos.rcdc.rco.module.def.api.UserDesktopConfigAPI;
import com.ruijie.rcos.rcdc.rco.module.def.api.UserDesktopMgmtAPI;
import com.ruijie.rcos.rcdc.rco.module.def.api.dto.UserGroupDesktopConfigDTO;
import com.ruijie.rcos.rcdc.rco.module.def.api.request.CreateCloudDesktopRequest;
import com.ruijie.rcos.rcdc.rco.module.def.api.request.CreateUserDesktopConfigRequest;
import com.ruijie.rcos.rcdc.rco.module.def.api.response.CreateDesktopResponse;
import com.ruijie.rcos.rcdc.rco.module.def.enums.UserCloudDeskTypeEnum;
import com.ruijie.rcos.rcdc.rco.module.openapi.rest.asynctask.enums.AsyncTaskEnum;
import com.ruijie.rcos.rcdc.rco.module.openapi.rest.common.constant.Constant;
import com.ruijie.rcos.rcdc.rco.module.openapi.rest.user.request.AssistCertificationRequest;
import com.ruijie.rcos.rcdc.rco.module.openapi.rest.user.request.CreateUserRequest;
import com.ruijie.rcos.rcdc.rco.module.openapi.rest.user.request.PrimaryCertificationRequest;
import com.ruijie.rcos.sk.base.exception.BusinessException;
import com.ruijie.rcos.sk.base.log.Logger;
import com.ruijie.rcos.sk.base.log.LoggerFactory;
import org.apache.commons.lang3.ObjectUtils;
import org.springframework.beans.BeanUtils;

import java.util.Objects;
import java.util.UUID;

/**
 * Description: 异步创建用户
 * Copyright: Copyright (c) 2022
 * Company: Ruijie Co., Ltd.
 * Create Time: 2022/3/30
 *
 * @author TD
 */
public class AsyncCreateUserThread extends AbstractAsyncUserMgmtThread {

    private static final Logger LOGGER = LoggerFactory.getLogger(AsyncCreateUserThread.class);

    private JSONObject jsonObject = new JSONObject();

    private CreateUserRequest createUserRequest;

    private IacUserMgmtAPI cbbUserAPI;

    private UserDesktopConfigAPI userDesktopConfigAPI;

    private IacImportUserAPI cbbImportUserAPI;

    private UserDesktopMgmtAPI userDesktopMgmtAPI;

    private IacUserIdentityConfigMgmtAPI userIdentityConfigAPI;
    
    private DataSyncAPI dataSyncAPI;

    private CbbDeskSpecAPI cbbDeskSpecAPI;

    public AsyncCreateUserThread(AsyncTaskEnum action, OpenApiTaskInfoAPI openApiTaskInfoAPI, CreateUserRequest createUserRequest)
            throws BusinessException {
        super(UUID.randomUUID(), action, openApiTaskInfoAPI);
        this.createUserRequest = createUserRequest;
        this.resourceList.add(jsonObject);
    }

    @Override
    public void run() {
        try {
            // 创建用户数据
            IacImportUserRequest iacImportUserRequest = new IacImportUserRequest();
            iacImportUserRequest.setDto(createUserRequest.buildCreateUserRequest());
            iacImportUserRequest.setIsEdit(IacUserImportEnum.CREATE);
            IacImportUserResultDTO response = cbbImportUserAPI.importUser(iacImportUserRequest);
            UUID userId = response.getUserId();
            UUID groupId = response.getGroupId();
            // 返回用户ID和用户组ID
            jsonObject.put(Constant.USER_ID, userId);
            jsonObject.put(Constant.GROUP_ID, groupId);
            IacUserTypeEnum userType = createUserRequest.getUserType();
            if (groupId == null || IacUserGroupMgmtAPI.DEFAULT_USER_GROUP_ID.equals(groupId)) {
                LOGGER.info("用户属性为：{}，组ID为空或为默认，不进行创建其它属性配置", userType.name());
                saveTaskSuccess();
                return;
            }
            // 非访客用户，创建对应的认证策略
            saveUserIdentityConfig(createUserRequest.getLoginIdentityLevel(), userId, createUserRequest);
            // 触发同步操作
            dataSyncAPI.activeSyncUserData(userId);

            // 是否需要创建VDI桌面配置
            updateUserDesktopConfig(userId, groupId, UserCloudDeskTypeEnum.VDI, userType);
            // 是否需要修改IDV桌面配置
            updateUserDesktopConfig(userId, groupId, UserCloudDeskTypeEnum.IDV, userType);
            // 是否需要修改VOI桌面配置
            updateUserDesktopConfig(userId, groupId, UserCloudDeskTypeEnum.VOI, userType);
            saveTaskSuccess();
        } catch (BusinessException e) {
            LOGGER.error("AsyncCreateUserThread error:", e);
            saveTaskException(e);
        } catch (Exception e) {
            LOGGER.error("AsyncCreateUserThread Non business error:", e);
            saveTaskUnknownException(e);
        }
    }

    /**
     * 设置cbbUserAPI
     * @param cbbUserAPI 用户操作API
     * @return AsyncCreateUserThread
     */
    public AsyncCreateUserThread setCbbUserAPI(IacUserMgmtAPI cbbUserAPI) {
        this.cbbUserAPI = cbbUserAPI;
        return this;
    }

    /**
     * 设置userDesktopConfigAPI
     * @param userDesktopConfigAPI 用户桌面操作API
     * @return AsyncCreateUserThread
     */
    public AsyncCreateUserThread setUserDesktopConfigAPI(UserDesktopConfigAPI userDesktopConfigAPI) {
        this.userDesktopConfigAPI = userDesktopConfigAPI;
        return this;
    }

    /**
     * 设置导入用户API
     * @param cbbImportUserAPI 导入用户API
     * @return userDesktopConfigAPI
     */
    public AsyncCreateUserThread setCbbImportUserAPI(IacImportUserAPI cbbImportUserAPI) {
        this.cbbImportUserAPI = cbbImportUserAPI;
        return this;
    }

    /**
     * 设置用户桌面操作API
     * @param userDesktopMgmtAPI 用户桌面操作API
     * @return AsyncCreateUserThread
     */
    public AsyncCreateUserThread setUserDesktopMgmtAPI(UserDesktopMgmtAPI userDesktopMgmtAPI) {
        this.userDesktopMgmtAPI = userDesktopMgmtAPI;
        return this;
    }

    /**
     * 设置用户认证属性API
     * @param userIdentityConfigAPI 用户认证属性API
     * @return AsyncCreateUserThread
     */
    public AsyncCreateUserThread setUserIdentityConfigAPI(IacUserIdentityConfigMgmtAPI userIdentityConfigAPI) {
        this.userIdentityConfigAPI = userIdentityConfigAPI;
        return this;
    }

    /**
     * 设置同步数据api
     * 
     * @param dataSyncAPI 同步数据api
     * @return AsyncCreateUserThread
     */
    public AsyncCreateUserThread setDataSyncAPI(DataSyncAPI dataSyncAPI) {
        this.dataSyncAPI = dataSyncAPI;
        return this;
    }


    /**
     * 设置规格api
     *
     * @param cbbDeskSpecAPI 规格api
     * @return AsyncCreateUserThread
     */
    public AsyncCreateUserThread setCbbDeskSpecAPI(CbbDeskSpecAPI cbbDeskSpecAPI) {
        this.cbbDeskSpecAPI = cbbDeskSpecAPI;
        return this;
    }

    private void updateUserDesktopConfig(UUID userId, UUID groupId, UserCloudDeskTypeEnum deskTypeEnum, IacUserTypeEnum userType)
            throws BusinessException {
        // 分组不为根分组或默认分组，查询分组配置的云桌面信息
        UserGroupDesktopConfigDTO userGroupDesktopConfig = userDesktopConfigAPI.getUserGroupDesktopConfig(groupId, deskTypeEnum);
        if (userGroupDesktopConfig == null || (deskTypeEnum != UserCloudDeskTypeEnum.VDI && userType == IacUserTypeEnum.VISITOR)) {
            LOGGER.debug("用户组[{}]对应的[{}]配置为空，不为用户[{}]分配对应的配置", groupId, deskTypeEnum.name(), userId);
            return;
        }
        if (deskTypeEnum == UserCloudDeskTypeEnum.VDI) {
            LOGGER.info("分组配置了VDI云桌面，为用户绑定分组云桌面");
            CreateCloudDesktopRequest request = new CreateCloudDesktopRequest();
            request.setUserId(userId);
            request.setStrategyId(userGroupDesktopConfig.getStrategyId());
            request.setNetworkId(userGroupDesktopConfig.getNetworkId());
            request.setDesktopImageId(userGroupDesktopConfig.getImageTemplateId());
            request.setUserProfileStrategyId(userGroupDesktopConfig.getUserProfileStrategyId());
            if (userGroupDesktopConfig.getClusterId() != null) {
                request.setClusterId(userGroupDesktopConfig.getClusterId());
            }
            if (userGroupDesktopConfig.getPlatformId() != null) {
                request.setPlatformId(userGroupDesktopConfig.getPlatformId());
            }
            if (Objects.nonNull(userGroupDesktopConfig.getDeskSpecId())) {
                request.setDeskSpec(cbbDeskSpecAPI.getById(userGroupDesktopConfig.getDeskSpecId()));
            }
            CreateDesktopResponse response = userDesktopMgmtAPI.create(request);
            // VDI返回对应的桌面信息
            jsonObject.put(Constant.DESK_ID, response.getId());
            jsonObject.put(Constant.DESK_NAME, response.getDesktopName());
        } else {
            LOGGER.info("分组绑定了{}云桌面，为用户应用用户分组配置", deskTypeEnum.name());
            IacUserDetailDTO userDetailResponse = cbbUserAPI.getUserDetail(userId);
            IacUpdateUserDTO updateUserRequest = new IacUpdateUserDTO();
            BeanUtils.copyProperties(userDetailResponse, updateUserRequest, "userName");
            cbbUserAPI.updateUser(updateUserRequest);
            updateUserDesktopConfig(updateUserRequest, userGroupDesktopConfig, deskTypeEnum);
        }
    }

    private void updateUserDesktopConfig(IacUpdateUserDTO userInfo, UserGroupDesktopConfigDTO userGroupDesktopConfig,
                                         UserCloudDeskTypeEnum deskTypeEnum) {
        CreateUserDesktopConfigRequest configRequest = new CreateUserDesktopConfigRequest(userInfo.getId(), deskTypeEnum);
        configRequest.setStrategyId(userGroupDesktopConfig.getStrategyId());
        configRequest.setImageTemplateId(userGroupDesktopConfig.getImageTemplateId());
        configRequest.setNetworkId(userGroupDesktopConfig.getNetworkId());
        configRequest.setUserProfileStrategyId(userGroupDesktopConfig.getUserProfileStrategyId());
        userDesktopConfigAPI.createOrUpdateUserDesktopConfig(configRequest);
    }

    private void saveUserIdentityConfig(IacUserLoginIdentityLevelEnum loginIdentityLevel, UUID userId, CreateUserRequest userRequest)
            throws BusinessException {

        // 访客则不进行设置
        if (userRequest.getUserType() == IacUserTypeEnum.VISITOR) {
            return;
        }

        IacUserIdentityConfigRequest userIdentityConfigRequest = new IacUserIdentityConfigRequest(IacConfigRelatedType.USER, userId);

        userIdentityConfigRequest.setLoginIdentityLevel(loginIdentityLevel);
        PrimaryCertificationRequest primaryCertification = userRequest.getPrimaryCertification();
        if (ObjectUtils.isEmpty(primaryCertification)) {
            primaryCertification = new PrimaryCertificationRequest();
        }
        userIdentityConfigRequest.setOpenAccountPasswordCertification(primaryCertification.getOpenAccountPasswordCertification());
        userIdentityConfigRequest.setOpenCasCertification(primaryCertification.getOpenCasCertification());
        userIdentityConfigRequest.setOpenWorkWeixinCertification(primaryCertification.getOpenWorkWeixinCertification());
        userIdentityConfigRequest.setOpenFeishuCertification(primaryCertification.getOpenFeishuCertification());
        userIdentityConfigRequest.setOpenDingdingCertification(primaryCertification.getOpenDingdingCertification());
        userIdentityConfigRequest.setOpenOauth2Certification(primaryCertification.getOpenOauth2Certification());
        userIdentityConfigRequest.setOpenRjclientCertification(primaryCertification.getOpenRjclientCertification());

        AssistCertificationRequest assistCertification = userRequest.getAssistCertification();
        if (ObjectUtils.isNotEmpty(assistCertification)) {
            userIdentityConfigRequest.setOpenHardwareCertification(assistCertification.getOpenHardwareCertification());
            userIdentityConfigRequest.setMaxHardwareNum(assistCertification.getMaxHardwareNum());
            userIdentityConfigRequest.setOpenOtpCertification(assistCertification.getOpenOtpCertification());
            userIdentityConfigRequest.setOpenSmsCertification(assistCertification.getOpenSmsCertification());
        }
        userIdentityConfigAPI.updateUserIdentityConfig(userIdentityConfigRequest);
    }
}
