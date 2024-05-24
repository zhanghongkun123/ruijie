package com.ruijie.rcos.rcdc.rco.module.impl.datasync.strategy.user.impl;

import java.util.Objects;
import java.util.UUID;

import com.ruijie.rcos.gss.sdk.iac.module.def.api.request.user.IacCreateDomainUserRequest;
import com.ruijie.rcos.gss.sdk.iac.module.def.api.request.user.IacExceptionToBusinessKeyRequest;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.IncorrectResultSizeDataAccessException;
import org.springframework.util.Assert;

import com.alibaba.fastjson.JSON;
import com.ruijie.rcos.gss.sdk.iac.module.def.api.request.user.IacUserIdentityConfigRequest;
import com.ruijie.rcos.gss.sdk.iac.module.def.dto.user.IacUpdateUserOnSyncDataDTO;
import com.ruijie.rcos.gss.sdk.iac.module.def.dto.user.IacUserDetailDTO;
import com.ruijie.rcos.gss.sdk.iac.module.def.enums.IacUserTypeEnum;
import com.ruijie.rcos.gss.sdk.iac.module.def.enums.DomainServerType;
import com.ruijie.rcos.gss.sdk.iac.module.def.api.IacUserGroupMgmtAPI;
import com.ruijie.rcos.gss.sdk.iac.module.def.api.IacUserIdentityConfigMgmtAPI;
import com.ruijie.rcos.gss.sdk.iac.module.def.api.IacUserMgmtAPI;
import com.ruijie.rcos.rcdc.rco.module.def.api.dto.UserSyncDataDTO;
import com.ruijie.rcos.rcdc.rco.module.impl.datasync.DataSyncBusinessKey;
import com.ruijie.rcos.rcdc.rco.module.impl.datasync.strategy.user.UserSyncDataStrategy;
import com.ruijie.rcos.rcdc.rco.module.impl.entity.ViewUserGroupSyncDataEntity;
import com.ruijie.rcos.rcdc.rco.module.impl.service.ViewUserGroupSyncDataService;
import com.ruijie.rcos.rcdc.rco.module.impl.service.ViewUserSyncDataService;
import com.ruijie.rcos.sk.base.exception.BusinessException;
import com.ruijie.rcos.sk.base.log.Logger;
import com.ruijie.rcos.sk.base.log.LoggerFactory;

/**
 * Description:
 * Copyright: Copyright (c)
 * Company: Ruijie Co., Ltd.
 * Create Time: 2023/09/21 10:58
 *
 * @author coderLee23
 */
public abstract class AbstractAdAndLdapUserDataStrategy implements UserSyncDataStrategy {

    private static final Logger LOGGER = LoggerFactory.getLogger(AbstractAdAndLdapUserDataStrategy.class);

    @Autowired
    private ViewUserSyncDataService viewUserSyncDataService;

    @Autowired
    private ViewUserGroupSyncDataService viewUserGroupSyncDataService;

    @Autowired
    private IacUserMgmtAPI userAPI;

    @Autowired
    private IacUserIdentityConfigMgmtAPI userIdentityConfigAPI;

    @Autowired
    private IacUserGroupMgmtAPI userGroupAPI;

    @Override
    public void syncData(UserSyncDataDTO userSyncData) throws BusinessException {
        Assert.notNull(userSyncData, "userSyncData must not be null");
        String fullGroupName = userSyncData.getFullGroupName();
        ViewUserGroupSyncDataEntity viewUserGroupSyncData = viewUserGroupSyncDataService.findViewUserGroupSyncData(fullGroupName);
        UUID groupId = IacUserGroupMgmtAPI.DEFAULT_USER_GROUP_ID;
        // 用户组不存在，则考虑移动到未分组
        if (Objects.nonNull(viewUserGroupSyncData)) {
            groupId = viewUserGroupSyncData.getId();
            LOGGER.warn(" 用户组存在，则设置为 {}", viewUserGroupSyncData.getName());
        }

        // 判定用户是存在
        String userName = userSyncData.getName();
        IacUserDetailDTO cbbUserDetailDTO = userAPI.getUserByName(userName);
        if (Objects.isNull(cbbUserDetailDTO)) {
            LOGGER.info("采用新增策略 用户数据 {}", JSON.toJSONString(userSyncData));
            try {
                IacCreateDomainUserRequest iacCreateDomainUserRequest = new IacCreateDomainUserRequest();
                iacCreateDomainUserRequest.setUserType(userSyncData.getUserType());
                iacCreateDomainUserRequest.setUserName(userSyncData.getName());
                userAPI.createDomainUser(iacCreateDomainUserRequest);
            } catch (IncorrectResultSizeDataAccessException e) {
                throw new BusinessException(DataSyncBusinessKey.RCDC_RCO_SYNC_DATA_AD_OR_LDAP_USER_EXISTS, e, userSyncData.getUserType().name(),
                        userSyncData.getName());
            } catch (Exception e) {
                DomainServerType domainServerType = getCbbUserType() == IacUserTypeEnum.AD ? DomainServerType.AD : DomainServerType.LDAP;
                IacExceptionToBusinessKeyRequest iacExceptionToBusinessKeyRequest = new IacExceptionToBusinessKeyRequest();
                iacExceptionToBusinessKeyRequest.setException(e);
                iacExceptionToBusinessKeyRequest.setServerType(domainServerType);
                throw new BusinessException(userAPI.exceptionToBusinessKey(iacExceptionToBusinessKeyRequest), e);
            }

            return;
        }

        LOGGER.info("采用更新策略 用户数据 {}", JSON.toJSONString(userSyncData));
        // 更新用户
        IacUpdateUserOnSyncDataDTO cbbUpdateUserOnSyncDataDTO = new IacUpdateUserOnSyncDataDTO();
        BeanUtils.copyProperties(userSyncData, cbbUpdateUserOnSyncDataDTO);
        cbbUpdateUserOnSyncDataDTO.setGroupId(groupId);
        cbbUpdateUserOnSyncDataDTO.setId(cbbUserDetailDTO.getId());
        // 采用更新方案
        userAPI.updateUserOnSyncData(cbbUpdateUserOnSyncDataDTO);

        IacUserIdentityConfigRequest userIdentityConfigRequest = new IacUserIdentityConfigRequest();
        userIdentityConfigRequest.setRelatedId(cbbUserDetailDTO.getId());
        BeanUtils.copyProperties(userSyncData, userIdentityConfigRequest);
        userIdentityConfigAPI.updateUserIdentityConfig(userIdentityConfigRequest);
    }

}
