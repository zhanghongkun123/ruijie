package com.ruijie.rcos.rcdc.rco.module.impl.api;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

import org.apache.commons.lang3.BooleanUtils;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.lang.Nullable;
import org.springframework.util.Assert;
import org.springframework.util.CollectionUtils;

import com.alibaba.fastjson.JSONObject;
import com.google.common.collect.Lists;
import com.ruijie.rcos.gss.sdk.iac.module.def.dto.user.IacImportUserDTO;
import com.ruijie.rcos.gss.sdk.iac.module.def.dto.user.IacUpdateUserDTO;
import com.ruijie.rcos.gss.sdk.iac.module.def.dto.user.IacUserDetailDTO;
import com.ruijie.rcos.gss.sdk.iac.module.def.enums.IacUserStateEnum;
import com.ruijie.rcos.gss.sdk.iac.module.def.enums.IacUserTypeEnum;
import com.ruijie.rcos.gss.sdk.iac.module.def.api.IacUserMgmtAPI;
import com.ruijie.rcos.gss.sdk.iac.module.def.api.request.user.IacUpdateBatchUserInvalidTimeRequest;
import com.ruijie.rcos.rcdc.codec.adapter.def.api.CbbTranspondMessageHandlerAPI;
import com.ruijie.rcos.rcdc.codec.adapter.def.dto.CbbShineMessageRequest;
import com.ruijie.rcos.rcdc.rco.module.def.api.RcoGlobalParameterAPI;
import com.ruijie.rcos.rcdc.rco.module.def.api.UserInfoAPI;
import com.ruijie.rcos.rcdc.rco.module.def.api.dto.UserCertificationDTO;
import com.ruijie.rcos.rcdc.rco.module.def.api.dto.UserLogoutDTO;
import com.ruijie.rcos.rcdc.rco.module.def.api.enums.ApiCallerTypeEnum;
import com.ruijie.rcos.rcdc.rco.module.def.api.request.FindParameterRequest;
import com.ruijie.rcos.rcdc.rco.module.def.api.response.FindParameterResponse;
import com.ruijie.rcos.rcdc.rco.module.def.api.response.bigscreen.UserDesktopItem;
import com.ruijie.rcos.rcdc.rco.module.def.api.response.bigscreen.UserDesktopResponse;
import com.ruijie.rcos.rcdc.rco.module.def.cas.qrcode.dto.UserIdentityConfigDTO;
import com.ruijie.rcos.rcdc.rco.module.def.constants.FunctionCallback;
import com.ruijie.rcos.rcdc.rco.module.def.constants.UserGroupValidateConstants;
import com.ruijie.rcos.rcdc.rco.module.def.utils.RedLineUtil;
import com.ruijie.rcos.rcdc.rco.module.impl.BusinessKey;
import com.ruijie.rcos.rcdc.rco.module.impl.Constants;
import com.ruijie.rcos.rcdc.rco.module.impl.certificationstrategy.service.UserInfoService;
import com.ruijie.rcos.rcdc.rco.module.impl.dao.UserTerminalDAO;
import com.ruijie.rcos.rcdc.rco.module.impl.entity.RcoViewUserEntity;
import com.ruijie.rcos.rcdc.rco.module.impl.entity.UserTerminalEntity;
import com.ruijie.rcos.rcdc.rco.module.impl.service.UserService;
import com.ruijie.rcos.rcdc.rco.module.impl.service.impl.connector.mq.api.WebClientProducerAPI;
import com.ruijie.rcos.rcdc.rco.module.impl.session.UserInfo;
import com.ruijie.rcos.rcdc.rco.module.impl.session.UserLoginSession;
import com.ruijie.rcos.rcdc.rco.module.impl.tx.UserServiceTx;
import com.ruijie.rcos.rcdc.terminal.module.def.api.CbbTerminalOperatorAPI;
import com.ruijie.rcos.sk.base.exception.BusinessException;
import com.ruijie.rcos.sk.base.i18n.LocaleI18nResolver;
import com.ruijie.rcos.sk.base.log.Logger;
import com.ruijie.rcos.sk.base.log.LoggerFactory;
import com.ruijie.rcos.sk.base.validator.ValidatorUtil;


/**
 * Description: 用户信息API
 * Copyright: Copyright (c) 2021
 * Company: Ruijie Co., Ltd.
 * Create Time: 2021/6/29 23:13
 *
 * @author yxq
 */
public class UserInfoAPIImpl implements UserInfoAPI {

    private static final Logger LOGGER = LoggerFactory.getLogger(UserInfoAPIImpl.class);

    /**
     * 分组层级分隔符
     */
    private static final String GROUP_SPILT = "/";

    /**
     * 用户禁用消息提示参数
     */
    private static final String USER_DISABLED_TIP_KEY = "tip";

    @Autowired
    private UserInfoService userInfoService;

    @Autowired
    private IacUserMgmtAPI cbbUserAPI;

    @Autowired
    private UserLoginSession userLoginSession;

    @Autowired
    private UserService userService;

    @Autowired
    private UserTerminalDAO userTerminalDAO;

    @Autowired
    private CbbTranspondMessageHandlerAPI cbbTranspondMessageHandlerAPI;

    @Autowired
    private WebClientProducerAPI webClientProducerAPI;

    @Autowired
    private UserServiceTx userServiceTx;

    @Autowired
    private RcoGlobalParameterAPI rcoGlobalParameterAPI;


    @Autowired
    private CbbTerminalOperatorAPI cbbTerminalOperatorAPI;


    @Override
    public String getUserNameById(UUID userId) throws BusinessException {
        Assert.notNull(userId, "userId must not be null");

        return userInfoService.getUsernameById(userId);
    }

    @Override
    public UUID getUserIdByUserName(String userName) throws BusinessException {
        Assert.notNull(userName, "userName must not be null");

        return userInfoService.getUserIdByUserName(userName);
    }

    @Override
    public long findUserCount() {
        return userInfoService.findUserCount();
    }

    @Override
    public UserCertificationDTO getUserCertificationDTO(UUID userId) {
        Assert.notNull(userId, "userId must not be null");

        return userInfoService.getUserCertificationDTO(userId);
    }


    @Override
    public void validateDeleteUser(UUID userId) throws BusinessException {
        Assert.notNull(userId, "validateDeleteUser userId must not be null");
        IacUserDetailDTO detailResponse = cbbUserAPI.getUserDetail(userId);
        String localUserName = detailResponse.getUserName();
        if ((IacUserTypeEnum.AD == detailResponse.getUserType() || IacUserTypeEnum.LDAP == detailResponse.getUserType())
                && IacUserStateEnum.ENABLE == detailResponse.getUserState()) {
            // 为AD域用户，且是启用状态，不允许删除操作
            throw new BusinessException(BusinessKey.RCDC_RCO_USER_IS_AD_USER_NOT_ALLOW_DELETE, localUserName);
        }

        if (IacUserTypeEnum.THIRD_PARTY == detailResponse.getUserType() && IacUserStateEnum.ENABLE == detailResponse.getUserState()) {
            // 第三方用户，且是启用状态，不允许删除操作
            throw new BusinessException(BusinessKey.RCDC_RCO_USER_IS_THIRD_PARTY_USER_NOT_ALLOW_DELETE, localUserName);
        }

        // 存在创建中的桌面不允许删除
        if (userService.getCreatingDesktopNum(userId) > 0) {
            throw new BusinessException(BusinessKey.RCDC_RCO_USER_CREATING_DESKTOP_NOT_ALLOW_DELETE, localUserName);
        }
    }

    @Override
    public void validateImportUser(IacImportUserDTO importUser, @Nullable FunctionCallback callback) throws BusinessException {
        Assert.notNull(importUser, "CbbImportUserDTO is not null");
        IacUserTypeEnum userType = importUser.getUserType();
        if (userType == IacUserTypeEnum.AD || userType == IacUserTypeEnum.LDAP) {
            throw new BusinessException(BusinessKey.RCDC_RCO_CREATE_USER_TYPE_NOT_AD_LDAP_FAIL);
        }
        String userName = importUser.getUserName();
        // 用户名不能是保留字
        if (getReserveUserName().contains(userName)) {
            throw new BusinessException(BusinessKey.RCDC_RCO_CREATE_USER_NAME_CONTAIN_DEFAULT, userName);
        }
        IacUserDetailDTO userDetailDTO = cbbUserAPI.getUserByName(userName);
        // 用户名称已被使用
        if (!ObjectUtils.isEmpty(userDetailDTO)) {
            throw new BusinessException(BusinessKey.RCDC_RCO_CREATE_USER_SAME_NAME_EXIST_ERROR, userName);
        }
        // 普通用户用户姓名不能为空
        if (IacUserTypeEnum.NORMAL == userType && StringUtils.isBlank(importUser.getRealName())) {
            throw new BusinessException(BusinessKey.RCDC_RCO_USER_NORMAL_USER_REAL_NAME_MUST_NOT_BE_NULL);
        }
        // 校验用户组名称
        String userGroupName = importUser.getGroupNames();
        if (StringUtils.isNotBlank(userGroupName)) {
            this.validateUserGroupName(userGroupName);
        }
        // 回调不为空，执行回调方法
        if (ObjectUtils.isNotEmpty(callback)) {
            callback.callback();
        }
    }

    @Override
    public void validateUserGroupName(String userGroupName) throws BusinessException {
        Assert.hasText(userGroupName, "userGroupName is not null");
        List<String> userGroupNameList = Arrays.asList(userGroupName.split(GROUP_SPILT));
        if (CollectionUtils.isEmpty(userGroupNameList)) {
            LOGGER.error("用户组/父分组不能为空");
            throw new BusinessException(BusinessKey.RCDC_RCO_USER_GROUP_PARENT_GROUP_NOT_ALLOWED_EMPTY, userGroupName);
        }
        if (userGroupNameList.size() > UserGroupValidateConstants.MAX_HIERARCHY_NUM) {
            LOGGER.error("用户组层级不能超过系统最多支持的{}级", UserGroupValidateConstants.MAX_HIERARCHY_NUM);
            throw new BusinessException(BusinessKey.RCDC_RCO_USERGROUP_HIERARCHY_OVER, String.valueOf(UserGroupValidateConstants.MAX_HIERARCHY_NUM));
        }
        for (String name : userGroupNameList) {
            // 分组中间不能有空字符串，即父分组不能为空字符串
            if (StringUtils.isBlank(name)) {
                LOGGER.error("用户组/父分组不能为空");
                throw new BusinessException(BusinessKey.RCDC_RCO_USER_GROUP_PARENT_GROUP_NOT_ALLOWED_EMPTY, userGroupName);
            }
            if (!ValidatorUtil.isTextName(name)) {
                // 用户组名格式错误
                LOGGER.error("用户组名[{}]格式错误", name);
                throw new BusinessException(BusinessKey.RCDC_RCO_USER_GROUP_NAME_INCORRECT, name);
            }
            if (!ValidatorUtil.isNumberInRangeForInteger(0, UserGroupValidateConstants.MAX_REAL_NAME_LENGTH, name.length())) {
                // 用户组名不能超过32个字符
                LOGGER.error("用户组名长度不能超过{}个字符", UserGroupValidateConstants.MAX_REAL_NAME_LENGTH);
                throw new BusinessException(BusinessKey.RCDC_RCO_USER_GROUP_NAME_LENGTH_OVER, name,
                        String.valueOf(UserGroupValidateConstants.MAX_REAL_NAME_LENGTH));
            }
            if (this.getReserveGroupName(true).contains(name)) {
                throw new BusinessException(BusinessKey.RCDC_RCO_USER_GROUP_NOT_ALLOW_RESERVE_NAME, userGroupName);
            }
        }
    }

    @Override
    public List<String> getReserveGroupName(Boolean isNewUserGroup) {
        Assert.notNull(isNewUserGroup, "isNewUserGroup is not null");
        List<String> reserveGroupNameList = Lists.newArrayList();
        // 不允许使用系统保留名，包括 总览、未分组、AD域用户组、LDAP组
        if (BooleanUtils.toBoolean(isNewUserGroup)) {
            reserveGroupNameList.add(LocaleI18nResolver.resolve(BusinessKey.RCDCC_RCO_USER_ROOT_LDAP_USER_GROUP_NAME));
            reserveGroupNameList.add(LocaleI18nResolver.resolve(BusinessKey.RCDC_RCO_RESERVED_AD_GROUP_NAME));
        }
        reserveGroupNameList.add(LocaleI18nResolver.resolve(BusinessKey.RCDC_RCO_RESERVED_GROUP_NAME_UNGROUPED));
        reserveGroupNameList.add(LocaleI18nResolver.resolve(BusinessKey.RCDC_RCO_RESERVED_GROUP_NAME_OVERVIEW));
        return reserveGroupNameList;
    }

    public List<String> getReserveUserName() {
        return Arrays.asList("admin", "public", "guest", "local");
    }

    @Override
    public void userLogout(UUID userId) {
        Assert.notNull(userId, "userId must not be null");

        LOGGER.info("通知用户[{}]退出终端登录", userId);
        List<UserTerminalEntity> terminalEntityList = userTerminalDAO.findByUserId(userId);
        for (UserTerminalEntity terminalEntity : terminalEntityList) {

            UserInfo loginUserInfo = userLoginSession.getLoginUserInfo(terminalEntity.getTerminalId());
            if (loginUserInfo != null) {
                CbbShineMessageRequest<JSONObject> request = CbbShineMessageRequest.create(
                        Constants.NOTIFY_USER_LOGOUT, terminalEntity.getTerminalId());
                JSONObject json = new JSONObject();
                json.put(USER_DISABLED_TIP_KEY, LocaleI18nResolver.resolve(BusinessKey.RCDC_RCO_USER_DISABLED));
                request.setContent(json);
                try {
                    cbbTranspondMessageHandlerAPI.request(request);
                } catch (Exception e) {
                    LOGGER.error("通知终端[" + terminalEntity.getTerminalId() + "]用户退出登录失败.", e);
                }
            }

        }
    }

    @Override
    public UserDesktopResponse statisticsUserDeskTop() throws BusinessException {
        UserDesktopResponse response = new UserDesktopResponse();
        response.setNormalUser(makeUserDeskTop(Arrays.asList(IacUserTypeEnum.NORMAL, IacUserTypeEnum.VISITOR)));
        response.setAdUser(makeUserDeskTop(Collections.singletonList(IacUserTypeEnum.AD)));
        response.setLdapUser(makeUserDeskTop(Collections.singletonList(IacUserTypeEnum.LDAP)));
        response.setThirdPartyUser(makeUserDeskTop(Collections.singletonList(IacUserTypeEnum.THIRD_PARTY)));
        return response;
    }

    @Override
    public void notifyWebUserLogout(UUID userId) {
        Assert.notNull(userId, "userId cannot be null!");
        try {
            RcoViewUserEntity userEntity = userService.getUserInfoById(userId);
            webClientProducerAPI.notifyUserLogout(new UserLogoutDTO(
                    LocaleI18nResolver.resolve(BusinessKey.RCDC_RCO_USER_DISABLED), userEntity.getUserName()));
        } catch (BusinessException e) {
            LOGGER.error("通知网页客户端[{}]用户退出登录失败.", userId, e);
        }
    }

    @Override
    public void updateBatchUserInfo(UserIdentityConfigDTO dto, List<UUID> userIdList) throws BusinessException {
        Assert.notNull(dto, "dto is not null");
        Assert.notNull(userIdList, "userIdList is not null");
        IacUserDetailDTO userDetail = cbbUserAPI.getUserDetail(userIdList.get(0));
        IacUserTypeEnum userType = userDetail.getUserType();

        // 普通用户更新失效天数和过期时间
        if (IacUserTypeEnum.NORMAL == userType) {
            for (UUID userId : userIdList) {
                IacUserDetailDTO userDetailDTO = cbbUserAPI.getUserDetail(userId);
                IacUpdateUserDTO cbbUpdateUserDTO = new IacUpdateUserDTO();
                BeanUtils.copyProperties(userDetailDTO, cbbUpdateUserDTO);
                cbbUpdateUserDTO.setInvalidTime(dto.getInvalidTime());
                cbbUpdateUserDTO.setAccountExpires(dto.getAccountExpireDate());
                LOGGER.info("普通用户：开始批量更新");
                cbbUserAPI.updateUser(cbbUpdateUserDTO);
            }
            // ad/ldap用户只更新失效天数
        } else {
            for (UUID userId : userIdList) {
                IacUserDetailDTO userDetailDTO = cbbUserAPI.getUserDetail(userId);
                IacUpdateUserDTO cbbUpdateUserDTO = new IacUpdateUserDTO();
                BeanUtils.copyProperties(userDetailDTO, cbbUpdateUserDTO);
                cbbUpdateUserDTO.setInvalidTime(dto.getInvalidTime());
                LOGGER.info("AD/LDAP域用户：开始批量更新");
                cbbUserAPI.updateUser(cbbUpdateUserDTO);
            }
        }
    }

    @Override
    public void updateBatchUserInvalidTime(Boolean isNormalUser, List<UUID> userIdList, Integer invalidTime, @Nullable Long accountExpireDate)
            throws BusinessException {
        Assert.notNull(isNormalUser, "isNormalUser is not null");
        Assert.notNull(userIdList, "userIdList is not null");
        Assert.state(!CollectionUtils.isEmpty(userIdList), "userIdList length > 0");
        Assert.notNull(invalidTime, "invalidTime is not null");
        IacUpdateBatchUserInvalidTimeRequest iacUpdateBatchUserInvalidTimeRequest = new IacUpdateBatchUserInvalidTimeRequest();
        iacUpdateBatchUserInvalidTimeRequest.setInvalidTime(invalidTime);
        iacUpdateBatchUserInvalidTimeRequest.setIsNormalUser(isNormalUser);
        iacUpdateBatchUserInvalidTimeRequest.setUserIdList(userIdList);
        iacUpdateBatchUserInvalidTimeRequest.setAccountExpireDate(accountExpireDate);
        cbbUserAPI.updateBatchUserInvalidTime(iacUpdateBatchUserInvalidTimeRequest);
    }

    @Override
    public String getUserPasswordKey(ApiCallerTypeEnum apiCallerTypeEnum) {
        Assert.notNull(apiCallerTypeEnum, "apiCallerTypeEnum must not be null");
        if (apiCallerTypeEnum == ApiCallerTypeEnum.EXTERNAL) {
            FindParameterRequest findParameterRequest =
                    new FindParameterRequest(com.ruijie.rcos.rcdc.rco.module.def.constants.Constants.USER_REQ_PARAMETER);
            FindParameterResponse findParameterResponse = rcoGlobalParameterAPI.findParameter(findParameterRequest);
            if (findParameterResponse != null && StringUtils.isNotBlank(findParameterResponse.getValue())) {
                return findParameterResponse.getValue();
            }
        }
        return RedLineUtil.getRealUserRedLine();
    }

    @Override
    public void sendUserPasswordChange(UUID userId, @Nullable String sourceTerminalId) {
        Assert.notNull(userId, "userId must not be null");
        List<UserTerminalEntity> terminalEntityList = userTerminalDAO.findByUserId(userId);
        for (UserTerminalEntity terminalEntity : terminalEntityList) {
            UserInfo loginUserInfo = userLoginSession.getLoginUserInfo(terminalEntity.getTerminalId());
            if (loginUserInfo != null) {
                LOGGER.info("通知终端[{}]用户[{}]修改密码成功", terminalEntity.getTerminalId(), userId);
                cbbTerminalOperatorAPI.sendUserPasswordChange(terminalEntity.getTerminalId(), sourceTerminalId);
            }

        }
    }

    private UserDesktopItem makeUserDeskTop(List<IacUserTypeEnum> normalUserType) throws BusinessException {
        UserDesktopItem userDesktopItem = new UserDesktopItem();
        userDesktopItem.setUserCount(cbbUserAPI.countByUserType(normalUserType));
        userDesktopItem.setDeskTopCount(userService.findByDesktopNumAndUserType(Constants.INT_1, normalUserType));
        return userDesktopItem;
    }

}
