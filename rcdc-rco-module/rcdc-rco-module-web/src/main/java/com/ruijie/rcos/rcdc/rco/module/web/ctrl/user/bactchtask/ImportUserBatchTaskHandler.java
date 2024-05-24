package com.ruijie.rcos.rcdc.rco.module.web.ctrl.user.bactchtask;

import com.ruijie.rcos.gss.base.iac.module.enums.SubSystem;
import com.ruijie.rcos.gss.sdk.iac.module.def.api.request.user.IacUserIdentityConfigRequest;
import com.ruijie.rcos.gss.sdk.iac.module.def.dto.user.*;
import com.ruijie.rcos.gss.sdk.iac.module.def.enums.IacConfigRelatedType;
import com.ruijie.rcos.gss.sdk.iac.module.def.enums.IacUserImportEnum;
import com.ruijie.rcos.gss.sdk.iac.module.def.enums.IacUserStateEnum;
import com.ruijie.rcos.gss.sdk.iac.module.def.enums.IacUserTypeEnum;
import com.ruijie.rcos.gss.log.module.def.api.BaseAuditLogAPI;
import com.ruijie.rcos.gss.sdk.iac.module.def.api.IacImportUserAPI;
import com.ruijie.rcos.gss.sdk.iac.module.def.api.IacUserGroupMgmtAPI;
import com.ruijie.rcos.gss.sdk.iac.module.def.api.IacUserIdentityConfigMgmtAPI;
import com.ruijie.rcos.gss.sdk.iac.module.def.api.IacUserMgmtAPI;
import com.ruijie.rcos.gss.sdk.iac.module.def.api.request.user.IacImportUserRequest;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.api.CbbDeskSpecAPI;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.api.CbbIDVDeskOperateAPI;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.dto.desk.CbbShutdownDeskIDVDTO;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.enums.CbbCloudDeskState;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.enums.CbbCloudDeskType;
import com.ruijie.rcos.rcdc.rco.module.def.BusinessKey;
import com.ruijie.rcos.rcdc.rco.module.def.api.*;
import com.ruijie.rcos.rcdc.rco.module.def.api.dto.CloudDesktopDTO;
import com.ruijie.rcos.rcdc.rco.module.def.api.dto.UserGroupDesktopConfigDTO;
import com.ruijie.rcos.rcdc.rco.module.def.api.request.CloudDesktopShutdownRequest;
import com.ruijie.rcos.rcdc.rco.module.def.api.request.CloudDesktopStartRequest;
import com.ruijie.rcos.rcdc.rco.module.def.api.request.CreateCloudDesktopRequest;
import com.ruijie.rcos.rcdc.rco.module.def.api.request.CreateUserDesktopConfigRequest;
import com.ruijie.rcos.rcdc.rco.module.def.api.response.CreateDesktopResponse;
import com.ruijie.rcos.rcdc.rco.module.def.enums.UserCloudDeskTypeEnum;
import com.ruijie.rcos.rcdc.rco.module.def.mail.dto.UserSendMailDTO;
import com.ruijie.rcos.rcdc.rco.module.def.utils.RedLineUtil;
import com.ruijie.rcos.rcdc.rco.module.web.Constants;
import com.ruijie.rcos.rcdc.rco.module.web.ctrl.user.UserBusinessKey;
import com.ruijie.rcos.rcdc.rco.module.web.ctrl.user.dto.ImportUserDTO;
import com.ruijie.rcos.rcdc.rco.module.web.ctrl.user.request.ImportUserBatchTaskHandlerRequest;
import com.ruijie.rcos.rcdc.rco.module.web.service.SpringBeanHelper;
import com.ruijie.rcos.rcdc.rco.module.web.util.CharRandomUtils;
import com.ruijie.rcos.sk.base.batch.*;
import com.ruijie.rcos.sk.base.crypto.AesUtil;
import com.ruijie.rcos.sk.base.exception.BusinessException;
import com.ruijie.rcos.sk.base.i18n.LocaleI18nResolver;
import com.ruijie.rcos.sk.base.log.Logger;
import com.ruijie.rcos.sk.base.log.LoggerFactory;
import com.ruijie.rcos.sk.base.usertip.UserTipUtil;
import com.ruijie.rcos.sk.base.util.DateUtils;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;

import java.text.DateFormat;
import java.text.ParseException;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.UUID;
import java.util.concurrent.TimeUnit;
import java.util.regex.Pattern;

import static com.ruijie.rcos.rcdc.clouddesktop.module.def.enums.CbbCloudDeskType.*;

/**
 * Description:
 * Copyright: Copyright (c) 2021
 * Company: Ruijie Co., Ltd.
 * Create Time: 2021/1/20
 *
 * @author wjp
 */
public class ImportUserBatchTaskHandler extends AbstractBatchTaskHandler {
    private static final Logger LOGGER = LoggerFactory.getLogger(ImportUserBatchTaskHandler.class);

    private static final String PERSIST_NOT_EXPIRE = "永不过期";

    private static final String INVALID_FLAG = "永不失效";

    private static final int INVALID_TIME_MAX_VALUE = 1000;

    private static final int INVALID_TIME_MIN_VALUE = 0;

    private static final String GENERAL_FLAG = "0";

    @Autowired
    private BaseAuditLogAPI auditLogAPI;

    @Autowired
    private IacUserMgmtAPI cbbUserAPI;

    @Autowired
    private IacUserGroupMgmtAPI cbbUserGroupAPI;

    @Autowired
    private UserDesktopMgmtAPI userDesktopMgmtAPI;

    @Autowired
    private UserDesktopConfigAPI userDesktopConfigAPI;

    @Autowired
    private IacImportUserAPI cbbImportUserAPI;

    @Autowired
    private MailMgmtAPI mailMgmtAPI;

    @Autowired
    private UserInfoAPI userInfoAPI;

    @Autowired
    private CbbIDVDeskOperateAPI cbbIDVDeskOperateAPI;

    @Autowired
    private UserDesktopOperateAPI userDesktopOperateAPI;

    @Autowired
    private IacUserIdentityConfigMgmtAPI userIdentityConfigAPI;

    @Autowired
    private CertificationStrategyParameterAPI certificationStrategyParameterAPI;

    @Autowired
    private UserMgmtAPI userMgmtAPI;

    @Autowired
    private DataSyncAPI dataSyncAPI;

    @Autowired
    private CbbDeskSpecAPI cbbDeskSpecAPI;

    public ImportUserBatchTaskHandler(ImportUserBatchTaskHandlerRequest request) {
        super(request.getBatchTaskItemIterator());
        this.auditLogAPI = request.getAuditLogAPI();
        this.cbbUserAPI = request.getCbbUserAPI();
        this.cbbUserGroupAPI = request.getCbbUserGroupAPI();
        this.userDesktopMgmtAPI = request.getUserDesktopMgmtAPI();
        this.userDesktopConfigAPI = request.getUserDesktopConfigAPI();
        this.cbbImportUserAPI = request.getCbbImportUserAPI();
        this.mailMgmtAPI = request.getMailMgmtAPI();
        this.userInfoAPI = request.getUserInfoAPI();
        this.cbbIDVDeskOperateAPI = request.getCbbIDVDeskOperateAPI();
        this.userDesktopOperateAPI = request.getUserDesktopOperateAPI();
        this.userMgmtAPI = request.getUserMgmtAPI();
        this.certificationStrategyParameterAPI = request.getCertificationStrategyParameterAPI();
        this.dataSyncAPI = request.getDataSyncAPI();
        this.userIdentityConfigAPI = request.getUserIdentityConfigAPI();
        this.cbbDeskSpecAPI = SpringBeanHelper.getBean(CbbDeskSpecAPI.class);
    }

    @Override
    public BatchTaskItemResult processItem(BatchTaskItem batchTaskItem) throws BusinessException {
        CreateUserBatchTaskItem item = (CreateUserBatchTaskItem) batchTaskItem;
        ImportUserDTO dto = item.getImportUserDTO();
        IacImportUserResultDTO importUserResponse;
        try {
            // 导入用户数据
            importUserResponse = importUser(dto);
            auditLogAPI.recordLog(UserBusinessKey.RCDC_RCO_IMPORT_USER_SUCCESS_LOG, new String[] {dto.getUserName()});
        } catch (BusinessException e) {
            String exceptionMsg = UserTipUtil.resolveBusizExceptionMsg(e);
            auditLogAPI.recordLog(UserBusinessKey.RCDC_RCO_IMPORT_USER_FAIL_LOG, new String[] {dto.getUserName(), exceptionMsg});
            throw new BusinessException(UserBusinessKey.RCDC_RCO_IMPORT_USER_FAIL, e, dto.getUserName(), exceptionMsg);
        }

        UUID groupId = importUserResponse.getGroupId();
        if (groupId == null || IacUserGroupMgmtAPI.DEFAULT_USER_GROUP_ID.equals(groupId)) {
            return DefaultBatchTaskItemResult.builder().batchTaskItemStatus(BatchTaskItemStatus.SUCCESS)
                    .msgKey(UserBusinessKey.RCDC_RCO_IMPORT_USER_SUCCESS_LOG).msgArgs(new String[] {dto.getUserName()}).build();
        }
        // 分组不为根分组或默认分组，查询分组配置的云桌面信息
        IacUserGroupDetailDTO userGroupInfoResponse = cbbUserGroupAPI.getUserGroupDetail(groupId);
        UserGroupDesktopConfigDTO userGroupDesktopConfig = userDesktopConfigAPI.getUserGroupDesktopConfig(groupId, UserCloudDeskTypeEnum.IDV);
        if (userGroupDesktopConfig != null) {
            LOGGER.info("用户组[{}]绑定了idv云桌面，为用户应用用户分组idv配置", groupId);
            try {
                IacUserDetailDTO userDetailResponse = cbbUserAPI.getUserDetail(importUserResponse.getUserId());
                IacUpdateUserDTO updateUserRequest = buildUpdateUserRequest(userDetailResponse, userGroupInfoResponse);
                cbbUserAPI.updateUser(updateUserRequest);
                updateUserIDVDesktopConfig(updateUserRequest, userGroupDesktopConfig);
                auditLogAPI.recordLog(UserBusinessKey.RCDC_RCO_IMPORT_USER_APPLY_GROUP_IDV_CONFIG_SUCCESS_LOG, new String[] {dto.getUserName()});
            } catch (BusinessException e) {
                auditLogAPI.recordLog(UserBusinessKey.RCDC_RCO_IMPORT_USER_APPLY_GROUP_IDV_CONFIG_FAIL_LOG, new String[] {dto.getUserName()});
                String exceptionMsg = UserTipUtil.resolveBusizExceptionMsg(e);
                throw new BusinessException(UserBusinessKey.RCDC_RCO_IMPORT_USER_APPLY_GROUP_IDV_CONFIG_FAIL_DESC, e, dto.getUserName(),
                        exceptionMsg);
            }
        }
        UserGroupDesktopConfigDTO vdiDesktopConfig = userDesktopConfigAPI.getUserGroupDesktopConfig(groupId, UserCloudDeskTypeEnum.VDI);
        if (vdiDesktopConfig != null) {
            LOGGER.info("用户组[{}]配置了VDI云桌面，为用户绑定分组云桌面", groupId);
            try {
                CreateDesktopResponse createResponse = createDesktop(importUserResponse.getUserId(), vdiDesktopConfig);
                String desktopName = createResponse.getDesktopName();
                auditLogAPI.recordLog(UserBusinessKey.RCDC_RCO_DESKTOP_CREATE_SUC_LOG, dto.getUserName(), desktopName);
            } catch (BusinessException e) {
                auditLogAPI.recordLog(UserBusinessKey.RCDC_RCO_DESKTOP_CREATE_FAIL_LOG, dto.getUserName(), e.getI18nMessage());
                throw new BusinessException(UserBusinessKey.RCDC_RCO_IMPORT_USER_SUCCESS_AND_CREATE_DESKTOP_FAIL, e, dto.getUserName(),
                        e.getI18nMessage());
            }
        }

        UserGroupDesktopConfigDTO voiDesktopConfig = userDesktopConfigAPI.getUserGroupDesktopConfig(groupId, UserCloudDeskTypeEnum.VOI);
        if (voiDesktopConfig != null) {
            LOGGER.info("分组配置了VOI云桌面，为用户绑定分组云桌面");
            try {
                IacUserDetailDTO userDetailResponse = cbbUserAPI.getUserDetail(importUserResponse.getUserId());
                IacUpdateUserDTO updateUserRequest = buildUpdateUserRequest(userDetailResponse, userGroupInfoResponse);
                cbbUserAPI.updateUser(updateUserRequest);
                //更新用户VOI云桌面
                updateUserVOIDesktopConfig(updateUserRequest, voiDesktopConfig);
                auditLogAPI.recordLog(UserBusinessKey.RCDC_RCO_IMPORT_USER_APPLY_GROUP_VOI_CONFIG_SUCCESS_LOG, new String[] {dto.getUserName()});
            } catch (BusinessException e) {
                auditLogAPI.recordLog(UserBusinessKey.RCDC_RCO_IMPORT_USER_APPLY_GROUP_VOI_CONFIG_FAIL_LOG, new String[] {dto.getUserName()});
                String exceptionMsg = UserTipUtil.resolveBusizExceptionMsg(e);
                throw new BusinessException(UserBusinessKey.RCDC_RCO_IMPORT_USER_APPLY_GROUP_VOI_CONFIG_FAIL_DESC, e, dto.getUserName(),
                        exceptionMsg);
            }
        }

        try {
            sendRandomPassword(dto, importUserResponse);
        } catch (BusinessException e) {
            throw new BusinessException(UserBusinessKey.RCDC_RCO_IMPORT_USER_SEND_RANDOM_PASSWORD_FAIL_DESC, e, dto.getUserName(),
                    e.getI18nMessage());
        }

        return DefaultBatchTaskItemResult.builder().batchTaskItemStatus(BatchTaskItemStatus.SUCCESS)
                .msgKey(UserBusinessKey.RCDC_RCO_IMPORT_USER_SUCCESS_LOG).msgArgs(new String[] {dto.getUserName()}).build();
    }

    private void updateUserIDVDesktopConfig(IacUpdateUserDTO userInfo, UserGroupDesktopConfigDTO userGroupDesktopConfig) {
        CreateUserDesktopConfigRequest configRequest = new CreateUserDesktopConfigRequest(userInfo.getId(), UserCloudDeskTypeEnum.IDV);
        configRequest.setStrategyId(userGroupDesktopConfig.getStrategyId());
        configRequest.setImageTemplateId(userGroupDesktopConfig.getImageTemplateId());
        configRequest.setNetworkId(userGroupDesktopConfig.getNetworkId());
        configRequest.setSoftwareStrategyId(userGroupDesktopConfig.getSoftwareStrategyId());
        configRequest.setUserProfileStrategyId(userGroupDesktopConfig.getUserProfileStrategyId());
        userDesktopConfigAPI.createOrUpdateUserDesktopConfig(configRequest);
    }

    /**
     * 创建VOI 桌面配置
     * @param userInfo
     * @param userGroupDesktopConfig
     */
    private void updateUserVOIDesktopConfig(IacUpdateUserDTO userInfo, UserGroupDesktopConfigDTO userGroupDesktopConfig) {
        CreateUserDesktopConfigRequest configRequest = new CreateUserDesktopConfigRequest(userInfo.getId(), UserCloudDeskTypeEnum.VOI);
        configRequest.setStrategyId(userGroupDesktopConfig.getStrategyId());
        configRequest.setImageTemplateId(userGroupDesktopConfig.getImageTemplateId());
        configRequest.setNetworkId(userGroupDesktopConfig.getNetworkId());
        configRequest.setSoftwareStrategyId(userGroupDesktopConfig.getSoftwareStrategyId());
        configRequest.setUserProfileStrategyId(userGroupDesktopConfig.getUserProfileStrategyId());
        userDesktopConfigAPI.createOrUpdateUserDesktopConfig(configRequest);
    }

    private IacUpdateUserDTO buildUpdateUserRequest(IacUserDetailDTO userDetailResponse, IacUserGroupDetailDTO userGroupInfoResponse) {
        IacUpdateUserDTO apiRequest = new IacUpdateUserDTO();
        BeanUtils.copyProperties(userDetailResponse, apiRequest, "userName");
        return apiRequest;
    }

    private IacImportUserResultDTO importUser(ImportUserDTO dto) throws BusinessException {
        IacImportUserDTO request = new IacImportUserDTO();
        request.setGroupNames(dto.getGroupNames());
        request.setUserName(dto.getUserName());
        request.setRealName(dto.getRealName());
        request.setPhoneNum(dto.getPhoneNum());
        dto.setOpenHardwareCertificationBoolean(parseEnableFromCertification(dto.getOpenHardwareCertification()));
        dto.setOpenOtpCertificationBoolean(parseEnableFromCertification(dto.getOpenOtpCertification()));
        dto.setOpenCasCertificationBoolean(parseEnableFromCertification(dto.getOpenCasCertification()));
        dto.setOpenRadiusCertificationBoolean(parseEnableFromCertification(dto.getOpenRadiusCertification()));
        if (dto.getState() != null && StringUtils.equals(dto.getState().trim(),
                LocaleI18nResolver.resolve(BusinessKey.RCDC_RCO_USER_STATE_DISABLE))) {
            request.setState(IacUserStateEnum.DISABLE);
        } else {
            request.setState(IacUserStateEnum.ENABLE);
        }
        //用户是否修改状态
        boolean isChangeState = false;
        IacUserDetailDTO userDetailDTO = null;
        if (dto.getIsEdit() == IacUserImportEnum.EDIT) {
            userDetailDTO = cbbUserAPI.getUserByName(dto.getUserName());
            isChangeState = userDetailDTO == null || !userDetailDTO.getUserState().equals(request.getState());
        }

        request.setEmail(dto.getEmail());
        Long accountExpire = validateExpireDateFormat(dto.getAccountExpireDate());
        request.setAccountExpireDate(accountExpire);
        Integer invalidTime = validateInvalidTime(dto.getInvalidTime());
        request.setInvalidTime(invalidTime);
        if (StringUtils.isNotBlank(dto.getDescription())) {
            request.setDescription(dto.getDescription());
        }
        IacImportUserRequest iacImportUserRequest = new IacImportUserRequest();
        iacImportUserRequest.setDto(request);
        iacImportUserRequest.setIsEdit(dto.getIsEdit());
        IacImportUserResultDTO importUserResponse = cbbImportUserAPI.importUser(iacImportUserRequest);

        // 导入编辑用户信息,通知终端处理
        notifyTerminal(dto, request, isChangeState, userDetailDTO);

        // 设置用户密码
        initUserPassword(dto,importUserResponse);

        initUserCetification(dto);

        IacUserDetailDTO cbbUserDetailDTO = cbbUserAPI.getUserByName(dto.getUserName());
        if (Objects.nonNull(cbbUserDetailDTO)) {
            dataSyncAPI.activeSyncUserData(cbbUserDetailDTO.getId());
        }

        return importUserResponse;
    }

    private void notifyTerminal(ImportUserDTO dto, IacImportUserDTO request, boolean isChangeState,
                                IacUserDetailDTO userDetailDTO) throws BusinessException {
        if (dto.getIsEdit() == IacUserImportEnum.EDIT && isChangeState) {
            //当用户为禁用状态，同步终端用户信息，踢出用户并关闭云桌面
            if (request.getState() == IacUserStateEnum.DISABLE) {
                notifyTerminalOp(request);
            } else if (userDetailDTO != null) {
                //用户启用时同步信息给终端
                userDetailDTO.setUserState(IacUserStateEnum.ENABLE);
                userMgmtAPI.syncUserInfoToTerminal(userDetailDTO);
            }
        }
    }

    /**
     * 1、随机密码：导入与编辑导入不支持
     * 2、导出保留密码导出字段，且为******
     * 3、导入创建用户：密码字段空与带*时统一初始化为123456，其它情况按实际输入修改密码
     * 4、导入编辑用户：空与带*不处理，保留原有密码，其它情况按实际输入修改密码
     */
    private void initUserPassword(ImportUserDTO dto, IacImportUserResultDTO importUserResponse) throws BusinessException {
        // 导入用户数据
        if (dto.getIsEdit() == IacUserImportEnum.EDIT) {
            initUserPasswordForEditMode(dto);
        } else {
            initUserPasswordForOtherMode(dto, importUserResponse);
        }
    }

    private void initUserPasswordForEditMode(ImportUserDTO dto) throws BusinessException {
        String newPassword = dto.getPassword();
        if (StringUtils.isNotBlank(newPassword) && !StringUtils.equals(Constants.SECRT_USER_PASSWORD, newPassword)) {
            IacUserDetailDTO userDetail = cbbUserAPI.getUserByName(dto.getUserName());
            if (userDetail != null) {
                IacChangeUserPasswordDTO changeUserPasswordRequest = new IacChangeUserPasswordDTO();
                setShouldChangePassword(changeUserPasswordRequest, newPassword);
                changeUserPasswordRequest.setUserId(userDetail.getId());
                changeUserPasswordRequest.setNewPassword(AesUtil.encrypt(dto.getPassword(), RedLineUtil.getRealUserRedLine()));
                changeUserPasswordRequest.setSubSystem(SubSystem.CDC);
                cbbUserAPI.changeUserPassword(changeUserPasswordRequest);
            }
        }
    }

    private void initUserPasswordForOtherMode(ImportUserDTO dto, IacImportUserResultDTO importUserResponse) throws BusinessException {
        if (importUserResponse.getUserId() == null) {
            return;
        }
        IacChangeUserPasswordDTO changeUserPasswordRequest = new IacChangeUserPasswordDTO();
        String newPassword = dto.getPassword();
        if (StringUtils.isBlank(newPassword) || StringUtils.equals(Constants.SECRT_USER_PASSWORD, newPassword)) {
            if (!com.ruijie.rcos.sk.base.util.StringUtils.isEmpty(dto.getEmail())) {
                // 邮箱不为空，设置随机密码，后续发送至用户邮箱
                String password = CharRandomUtils.passwordCreate(6);
                changeUserPasswordRequest.setNewPassword(password);
                importUserResponse.setPassword(password);
                importUserResponse.setPasswordRandom(Boolean.TRUE);
            } else {
                //设置为初始密码
                changeUserPasswordRequest.setNewPassword(Constants.INIT_USER_PASSWORD);
            }
        } else {
            changeUserPasswordRequest.setNewPassword(dto.getPassword());
        }

        Boolean shouldUpdatePwd = certificationStrategyParameterAPI.isNeedUpdatePassword(changeUserPasswordRequest.getNewPassword());
        changeUserPasswordRequest.setShouldChangePassword(shouldUpdatePwd);
        LOGGER.info("导入普通用户[{}]是否需要需要修改密码结果为：[{}]", importUserResponse.getUserName(), shouldUpdatePwd);

        if (dto.getIsEdit() == IacUserImportEnum.CREATE) {
            changeUserPasswordRequest.setUnChangePwd(Boolean.TRUE);
        }

        changeUserPasswordRequest.setUserId(importUserResponse.getUserId());
        changeUserPasswordRequest.setNewPassword(AesUtil.encrypt(changeUserPasswordRequest.getNewPassword(), RedLineUtil.getRealUserRedLine()));
        changeUserPasswordRequest.setSubSystem(SubSystem.CDC);
        cbbUserAPI.changeUserPassword(changeUserPasswordRequest);
    }

    /**
     * 设置是否提示用户需要时行初始密码修改
     * @param changeUserPasswordRequest
     * @param newPassword
     */
    private void setShouldChangePassword(IacChangeUserPasswordDTO changeUserPasswordRequest, String newPassword) {
        boolean isMatches = Pattern.matches(Constants.USER_PASSWORD_REGEX, newPassword);
        if (isMatches) {
            changeUserPasswordRequest.setShouldChangePassword(Boolean.FALSE);
        }
    }

    private void notifyTerminalOp(IacImportUserDTO request) throws BusinessException {
        IacUserDetailDTO cbbUserDetailDTO = cbbUserAPI.getUserByName(request.getUserName());
        if (cbbUserDetailDTO == null) {
            LOGGER.info("未找到{}用户信息，关闭云桌面失败", request.getUserName());
            throw new BusinessException(UserBusinessKey.RCDC_RCO_IMPORT_USER_NOT_FOUND_USER_INFO, request.getUserName());
        }
        UUID userId = cbbUserDetailDTO.getId();
        String userName = cbbUserDetailDTO.getUserName();
        LOGGER.info("禁用用户主动推送用户状态，用户id为[{}]", userId);
        cbbUserDetailDTO.setUserState(IacUserStateEnum.DISABLE);
        userMgmtAPI.syncUserInfoToTerminal(cbbUserDetailDTO);

        LOGGER.info("禁用用户并退出终端会话，用户id为[{}]", userId);
        userInfoAPI.userLogout(userId);
        //通知web端用户退出
        userInfoAPI.notifyWebUserLogout(userId);
        // 关闭用户关联的桌面
        final List<CloudDesktopDTO> desktopList = userDesktopMgmtAPI.getAllDesktopByUserId(userId);
        for (CloudDesktopDTO desktop : desktopList) {
            try {
                if (!Objects.equals(desktop.getDesktopState(), CbbCloudDeskState.RUNNING.toString())
                        && !Objects.equals(desktop.getDesktopState(), CbbCloudDeskState.SLEEP.toString())) {
                    continue;
                }
                userDesktopOperate(desktop);
            } catch (BusinessException e) {
                LOGGER.error("禁用用户时发送云桌面关闭命令失败，云桌面[id={}]", desktop.getId().toString(), e);
                auditLogAPI.recordLog(UserBusinessKey.RCDC_RCO_DESKTOP_SHUTDOWN_FAIL_LOG, userName, desktop.getDesktopName(),
                        e.getI18nMessage());
            }
        }
    }

    private void userDesktopOperate(CloudDesktopDTO desktop) throws BusinessException {
        CbbCloudDeskType deskType = CbbCloudDeskType.valueOf(desktop.getDesktopCategory());
        LOGGER.info("准备下发关闭{}类型云桌面[id={}]命令", deskType.name(), desktop.getId());
        if (deskType == IDV) {
            CbbShutdownDeskIDVDTO shutdownDeskIDVDTO = new CbbShutdownDeskIDVDTO();
            shutdownDeskIDVDTO.setId(desktop.getId());
            shutdownDeskIDVDTO.setIsForce(Boolean.FALSE);
            shutdownDeskIDVDTO.setTimeout(TimeUnit.MINUTES.toMillis(5));
            cbbIDVDeskOperateAPI.shutdownDeskIDV(shutdownDeskIDVDTO);
        } else if (deskType == VDI || deskType == THIRD) {
            if (Objects.equals(desktop.getDesktopState(), CbbCloudDeskState.SLEEP.toString())) {
                userDesktopOperateAPI.start(new CloudDesktopStartRequest(desktop.getCbbId()));
            }
            userDesktopOperateAPI.shutdown(new CloudDesktopShutdownRequest(desktop.getId(), Boolean.FALSE));
        } else {
            throw new BusinessException(UserBusinessKey.RCDC_RCO_IMPORT_USER_NOT_SUPPORT_DESKTOP_TYPE, deskType.name());
        }
    }

    private void initUserCetification(ImportUserDTO dto) throws BusinessException {
        IacUserDetailDTO cbbUserDetailDTO = cbbUserAPI.getUserByName(dto.getUserName());
        if (cbbUserDetailDTO == null) {
            LOGGER.info("未找到[{}]用户信息", dto.getUserName());
            return;
        }
        if (cbbUserDetailDTO.getUserType() == IacUserTypeEnum.AD || cbbUserDetailDTO.getUserType() == IacUserTypeEnum.LDAP) {
            LOGGER.info("[{}]用户属于[{}]不支持修改", dto.getUserName(), cbbUserDetailDTO.getUserType());
            return;
        }
        IacUserIdentityConfigRequest userIdentityConfigRequest =
                new IacUserIdentityConfigRequest(IacConfigRelatedType.USER, cbbUserDetailDTO.getId());
        userIdentityConfigRequest.setOpenCasCertification(dto.getOpenCasCertificationBoolean());
        userIdentityConfigRequest.setOpenOtpCertification(dto.getOpenOtpCertificationBoolean());
        if (!StringUtils.isBlank(dto.getMaxHardwareNum())) {
            userIdentityConfigRequest.setMaxHardwareNum(Integer.parseInt(dto.getMaxHardwareNum()));
        }
        userIdentityConfigRequest.setOpenHardwareCertification(dto.getOpenHardwareCertificationBoolean());
        userIdentityConfigRequest.setOpenSmsCertification(dto.getOpenSmsCertificationToBoolean());
        userIdentityConfigRequest.setOpenRadiusCertification(dto.getOpenRadiusCertificationBoolean());
        userIdentityConfigRequest.setOpenAccountPasswordCertification(parseEnableFromCertification(dto.getOpenAccountPasswordCertification()));
        userIdentityConfigRequest.setOpenWorkWeixinCertification(parseEnableFromCertification(dto.getOpenWorkWeixinCertification()));
        userIdentityConfigRequest.setOpenFeishuCertification(parseEnableFromCertification(dto.getOpenFeishuCertification()));
        userIdentityConfigRequest.setOpenDingdingCertification(parseEnableFromCertification(dto.getOpenDingdingCertification()));
        userIdentityConfigRequest.setOpenOauth2Certification(parseEnableFromCertification(dto.getOpenOauth2Certification()));
        userIdentityConfigRequest.setOpenRjclientCertification(parseEnableFromCertification(dto.getOpenRjclientCertification()));
        userIdentityConfigAPI.updateUserIdentityConfig(userIdentityConfigRequest);
    }

    private CreateDesktopResponse createDesktop(UUID userId, UserGroupDesktopConfigDTO vdiDesktopConfig) throws BusinessException {
        CreateCloudDesktopRequest request = new CreateCloudDesktopRequest();
        request.setUserId(userId);
        request.setStrategyId(vdiDesktopConfig.getStrategyId());
        request.setNetworkId(vdiDesktopConfig.getNetworkId());
        request.setDesktopImageId(vdiDesktopConfig.getImageTemplateId());
        request.setSoftwareStrategyId(vdiDesktopConfig.getSoftwareStrategyId());
        request.setUserProfileStrategyId(vdiDesktopConfig.getUserProfileStrategyId());
        if (vdiDesktopConfig.getClusterId() != null) {
            request.setClusterId(vdiDesktopConfig.getClusterId());
        }
        if (vdiDesktopConfig.getPlatformId() != null) {
            request.setPlatformId(vdiDesktopConfig.getPlatformId());
        }
        if (Objects.nonNull(vdiDesktopConfig.getDeskSpecId())) {
            request.setDeskSpec(cbbDeskSpecAPI.getById(vdiDesktopConfig.getDeskSpecId()));
        }
        return userDesktopMgmtAPI.create(request);
    }

    private Long validateExpireDateFormat(String expireDateFormat) throws BusinessException {
        if (ObjectUtils.isEmpty(expireDateFormat) || expireDateFormat.equals(PERSIST_NOT_EXPIRE) || expireDateFormat.equals(GENERAL_FLAG)) {
            return 0L;
        }
        DateFormat dateFormat = DateUtils.createDateFormat("yyyy-MM-dd HH:mm:ss");
        try {
            Date parseFormat = dateFormat.parse(expireDateFormat);
            return parseFormat.getTime();
        } catch (ParseException e) {
            LOGGER.error("导入的日期[{}]格式不合法", expireDateFormat);
            throw new BusinessException(UserBusinessKey.RCDC_RCO_USER_IMPORT_EXPIRE_RESOLVE_FAIL, e, expireDateFormat);
        }
    }

    private Integer validateInvalidTime(String s) throws BusinessException {
        if (ObjectUtils.isEmpty(s) || s.equals(INVALID_FLAG) || s.equals(GENERAL_FLAG)) {
            return 0;
        }
        Integer invalidTime;
        try {
            invalidTime = Integer.parseInt(s);
            if (invalidTime < INVALID_TIME_MIN_VALUE || invalidTime > INVALID_TIME_MAX_VALUE) {
                throw new BusinessException(UserBusinessKey.RCDC_RCO_USER_INVALID_RESOLVE_ERROR, s);
            }
        } catch (NumberFormatException e) {
            throw new BusinessException(UserBusinessKey.RCDC_RCO_USER_INVALID_RESOLVE_ERROR, e, s);
        }
        return invalidTime;
    }

    private void sendRandomPassword(ImportUserDTO dto, IacImportUserResultDTO importUserResponse) throws BusinessException {
        //随机密码，发送邮件
        if (Boolean.TRUE.equals(importUserResponse.getPasswordRandom()) &&
                !com.ruijie.rcos.sk.base.util.StringUtils.isEmpty(importUserResponse.getUserName())) {

            UserSendMailDTO userSendMailDTO = new UserSendMailDTO();
            userSendMailDTO.setContent(importUserResponse.getPassword());
            userSendMailDTO.setUserName(importUserResponse.getUserName());
            userSendMailDTO.setRealName(dto.getRealName());
            userSendMailDTO.setEmail(dto.getEmail());
            mailMgmtAPI.sendMail(userSendMailDTO);
        }

    }

    @Override
    public BatchTaskFinishResult onFinish(int successCount, int failCount) {
        // 结束导入任务
        try {
            cbbImportUserAPI.finishImportUser();
        } catch (BusinessException e) {
            // 不会执行到这里
            LOGGER.error("finishImportUser error", e);
        }
        return buildDefaultFinishResult(successCount, failCount, UserBusinessKey.RCDC_RCO_IMPORT_USER_BATCH_TASK_SUCCESS);
    }

    private Boolean parseEnableFromCertification(String value) {
        if (StringUtils.isEmpty(value)) {
            return Boolean.FALSE;
        }
        if (StringUtils.equals(value.trim(), LocaleI18nResolver.resolve(BusinessKey.RCDC_RCO_CERTIFICATION_ENABLE))) {
            return Boolean.TRUE;
        } else {
            return Boolean.FALSE;
        }
    }
}
