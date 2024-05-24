package com.ruijie.rcos.rcdc.rco.module.openapi.rest.user.thread;

import com.alibaba.fastjson.JSONObject;
import com.ruijie.rcos.gss.sdk.iac.module.def.api.IacUserIdentityConfigMgmtAPI;
import com.ruijie.rcos.gss.sdk.iac.module.def.api.IacUserMgmtAPI;
import com.ruijie.rcos.gss.sdk.iac.module.def.api.request.user.IacUserIdentityConfigRequest;
import com.ruijie.rcos.gss.sdk.iac.module.def.dto.user.IacUpdateUserDTO;
import com.ruijie.rcos.gss.sdk.iac.module.def.dto.user.IacUserDetailDTO;
import com.ruijie.rcos.gss.sdk.iac.module.def.enums.IacConfigRelatedType;
import com.ruijie.rcos.gss.sdk.iac.module.def.enums.IacUserLoginIdentityLevelEnum;
import com.ruijie.rcos.gss.sdk.iac.module.def.enums.IacUserTypeEnum;
import com.ruijie.rcos.rcdc.rco.module.def.api.DataSyncAPI;
import com.ruijie.rcos.rcdc.rco.module.def.api.OpenApiTaskInfoAPI;
import com.ruijie.rcos.rcdc.rco.module.def.api.RcoUserIdentityConfigAPI;
import com.ruijie.rcos.rcdc.rco.module.def.api.dto.UpdateUserConfigNotifyContentDTO;
import com.ruijie.rcos.rcdc.rco.module.def.api.dto.user.AssistCertification;
import com.ruijie.rcos.rcdc.rco.module.def.api.dto.user.PrimaryCertification;
import com.ruijie.rcos.rcdc.rco.module.openapi.rest.asynctask.enums.AsyncTaskEnum;
import com.ruijie.rcos.rcdc.rco.module.openapi.rest.common.constant.Constant;
import com.ruijie.rcos.rcdc.rco.module.openapi.rest.user.dto.EditUserDTO;
import com.ruijie.rcos.sk.base.exception.BusinessException;
import com.ruijie.rcos.sk.base.log.Logger;
import com.ruijie.rcos.sk.base.log.LoggerFactory;
import org.apache.commons.lang3.BooleanUtils;
import org.springframework.util.Assert;

import java.util.UUID;

import static com.ruijie.rcos.rcdc.rco.module.openapi.rest.common.constant.Constant.USER_NAME;

/**
 * Description: 编辑用户线程
 * Copyright: Copyright (c)
 * Company: Ruijie Co., Ltd.
 * Create Time: 2023/8/29 16:41
 *
 * @author zdc
 */
public class AsyncEditUserThread extends AbstractAsyncUserMgmtThread {

    private static final Logger LOGGER = LoggerFactory.getLogger(AsyncEditUserThread.class);

    private JSONObject jsonObject = new JSONObject();

    private EditUserDTO editUserRequest;

    private IacUpdateUserDTO cbbUpdateUserDTO;

    private IacUserDetailDTO cbbUserDetailDTO;

    private IacUserMgmtAPI cbbUserAPI;

    private RcoUserIdentityConfigAPI rcoUserIdentityConfigAPI;

    private IacUserIdentityConfigMgmtAPI iacUserIdentityConfigMgmtAPI;

    private DataSyncAPI dataSyncAPI;

    public AsyncEditUserThread(AsyncTaskEnum action, OpenApiTaskInfoAPI openApiTaskInfoAPI, EditUserDTO editUserRequest) throws BusinessException {
        super(UUID.randomUUID(), action, openApiTaskInfoAPI);
        this.editUserRequest = editUserRequest;
        this.resourceList.add(jsonObject);
    }

    @Override
    public void run() {
        try {
            cbbUserAPI.updateUser(cbbUpdateUserDTO);
            if (hasUserIdentityConfig(cbbUserDetailDTO.getUserType())) {
                IacUserLoginIdentityLevelEnum loginIdentityLevel = editUserRequest.getLoginIdentityLevel();
                Assert.notNull(loginIdentityLevel, "loginIdentityLevel不能为null");
                saveUserIdentityConfig(loginIdentityLevel, editUserRequest.getUserId(), editUserRequest.getAssistCertification(),
                        editUserRequest.getPrimaryCertification());
                // 用户绑定IDV/TCI并修改了动态口令，通知shine
                if (editUserRequest.getAssistCertification() != null) {
                    LOGGER.info("用户[{}]辅助认证策略产生变更，变更信息为[{}]", cbbUserDetailDTO.getUserName(), editUserRequest.getAssistCertification());
                    UpdateUserConfigNotifyContentDTO dto = new UpdateUserConfigNotifyContentDTO();
                    dto.setUserId(editUserRequest.getUserId());
                    boolean enableOpenOtp = BooleanUtils.isTrue(editUserRequest.getAssistCertification().getOpenOtpCertification());
                    dto.setOpenOtp(enableOpenOtp);
                    rcoUserIdentityConfigAPI.editUserIdentityConfigNotifyShine(dto);
                }
            }
            // 此处触发同步集群操作
            dataSyncAPI.activeSyncUserData(cbbUpdateUserDTO.getId());
            // 返回用户ID和用户名称
            jsonObject.put(Constant.USER_ID, cbbUpdateUserDTO.getId());
            jsonObject.put(USER_NAME, cbbUpdateUserDTO.getUserName());
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
     *
     * @param cbbUserAPI cbbUserAPI
     * @return this
     */
    public AsyncEditUserThread setCbbUserAPI(IacUserMgmtAPI cbbUserAPI) {
        this.cbbUserAPI = cbbUserAPI;
        return this;
    }

    /**
     *
     * @param userIdentityConfigAPI userIdentityConfigAPI
     * @return this
     */
    public AsyncEditUserThread setUserIdentityConfigAPI(RcoUserIdentityConfigAPI userIdentityConfigAPI) {
        this.rcoUserIdentityConfigAPI = userIdentityConfigAPI;
        return this;
    }

    /**
     *
     * @param iacUserIdentityConfigMgmtAPI iacUserIdentityConfigMgmtAPI
     * @return this
     */
    public AsyncEditUserThread setIacUserIdentityConfigAPI(IacUserIdentityConfigMgmtAPI iacUserIdentityConfigMgmtAPI) {
        this.iacUserIdentityConfigMgmtAPI = iacUserIdentityConfigMgmtAPI;
        return this;
    }

    /**
     * 
     * @return BaseUpdateUserDTO
     */
    public IacUpdateUserDTO getCbbUpdateUserDTO() {
        return cbbUpdateUserDTO;
    }

    /**
     *
     * @param cbbUpdateUserDTO cbbUpdateUserDTO
     * @return this
     */
    public AsyncEditUserThread setCbbUpdateUserDTO(IacUpdateUserDTO cbbUpdateUserDTO) {
        this.cbbUpdateUserDTO = cbbUpdateUserDTO;
        return this;
    }

    /**
     *
     * @return BaseUserDetailDTO
     */
    public IacUserDetailDTO getCbbUserDetailDTO() {
        return cbbUserDetailDTO;
    }

    /**
     *
     * @param cbbUserDetailDTO cbbUserDetailDTO
     * @return this
     */
    public AsyncEditUserThread setCbbUserDetailDTO(IacUserDetailDTO cbbUserDetailDTO) {
        this.cbbUserDetailDTO = cbbUserDetailDTO;
        return this;
    }

    /**
     *
     * @param dataSyncAPI dataSyncAPI
     * @return this
     */
    public AsyncEditUserThread setDataSyncAPI(DataSyncAPI dataSyncAPI) {
        this.dataSyncAPI = dataSyncAPI;
        return this;
    }

    private boolean hasUserIdentityConfig(IacUserTypeEnum userType) throws BusinessException {
        return iacUserIdentityConfigMgmtAPI.hasUserIdentityConfig(userType);
    }

    private void saveUserIdentityConfig(IacUserLoginIdentityLevelEnum loginIdentityLevel, UUID userId, AssistCertification assistCertification,
                                        PrimaryCertification primaryCertification) throws BusinessException {
        IacUserIdentityConfigRequest userIdentityConfigRequest = new IacUserIdentityConfigRequest(IacConfigRelatedType.USER, userId);

        userIdentityConfigRequest.setLoginIdentityLevel(loginIdentityLevel);
        userIdentityConfigRequest.setOpenAccountPasswordCertification(primaryCertification.getOpenAccountPasswordCertification());
        userIdentityConfigRequest.setOpenCasCertification(primaryCertification.getOpenCasCertification());
        userIdentityConfigRequest.setOpenDingdingCertification(primaryCertification.getOpenDingdingCertification());
        userIdentityConfigRequest.setOpenFeishuCertification(primaryCertification.getOpenFeishuCertification());
        userIdentityConfigRequest.setOpenWorkWeixinCertification(primaryCertification.getOpenWorkWeixinCertification());
        userIdentityConfigRequest.setOpenOauth2Certification(primaryCertification.getOpenOauth2Certification());
        userIdentityConfigRequest.setOpenRjclientCertification(primaryCertification.getOpenRjclientCertification());
        if (assistCertification != null) {
            userIdentityConfigRequest.setOpenHardwareCertification(assistCertification.getOpenHardwareCertification());
            userIdentityConfigRequest.setMaxHardwareNum(assistCertification.getMaxHardwareNum());
            userIdentityConfigRequest.setOpenOtpCertification(assistCertification.getOpenOtpCertification());
            userIdentityConfigRequest.setOpenSmsCertification(assistCertification.getOpenSmsCertification());
            userIdentityConfigRequest.setOpenRadiusCertification(assistCertification.getOpenRadiusCertification());
        }
        iacUserIdentityConfigMgmtAPI.updateUserIdentityConfig(userIdentityConfigRequest);
    }
}