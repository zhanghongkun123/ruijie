package com.ruijie.rcos.rcdc.rco.module.impl.datasync.strategy.user.impl;

import java.util.Objects;
import java.util.UUID;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import com.alibaba.fastjson.JSON;
import com.ruijie.rcos.gss.sdk.iac.module.def.api.request.user.IacUserIdentityConfigRequest;
import com.ruijie.rcos.gss.sdk.iac.module.def.dto.user.IacCreateUserOnSyncDataDTO;
import com.ruijie.rcos.gss.sdk.iac.module.def.dto.user.IacUpdateUserOnSyncDataDTO;
import com.ruijie.rcos.gss.sdk.iac.module.def.dto.user.IacUserDetailDTO;
import com.ruijie.rcos.gss.sdk.iac.module.def.api.IacUserGroupMgmtAPI;
import com.ruijie.rcos.gss.sdk.iac.module.def.api.IacUserIdentityConfigMgmtAPI;
import com.ruijie.rcos.gss.sdk.iac.module.def.api.IacUserMgmtAPI;
import com.ruijie.rcos.rcdc.rco.module.def.api.dto.UserSyncDataDTO;
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
 * Create Time: 2023/09/20 20:08
 *
 * @author coderLee23
 */
@Service
public abstract class AbstractNormalAndThirdPartyUserSyncDataStrategy implements UserSyncDataStrategy {

    private static final Logger LOGGER = LoggerFactory.getLogger(AbstractNormalAndThirdPartyUserSyncDataStrategy.class);

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
        // 用户组默认使用未分组
        UUID groupId = IacUserGroupMgmtAPI.DEFAULT_USER_GROUP_ID;
        if (Objects.nonNull(viewUserGroupSyncData)) {
            groupId = viewUserGroupSyncData.getId();
            LOGGER.warn(" 用户组存在，则设置为 {}", viewUserGroupSyncData.getName());
        }

        // 判定用户是存在
        String userName = userSyncData.getName();
        IacUserDetailDTO cbbUserDetailDTO = userAPI.getUserByName(userName);
        if (Objects.isNull(cbbUserDetailDTO)) {
            LOGGER.info("采用新增策略 用户数据 {}", JSON.toJSONString(userSyncData));
            // 不存在新增用户
            IacCreateUserOnSyncDataDTO cbbCreateUserOnSyncDataDTO = new IacCreateUserOnSyncDataDTO();
            BeanUtils.copyProperties(userSyncData, cbbCreateUserOnSyncDataDTO);
            cbbCreateUserOnSyncDataDTO.setGroupId(groupId);
            cbbCreateUserOnSyncDataDTO.setIsUserModifyPassword(userSyncData.getUserModifyPassword());
            IacUserDetailDTO newUserDetailDTO = userAPI.createUserOnSyncData(cbbCreateUserOnSyncDataDTO);

            IacUserIdentityConfigRequest userIdentityConfigRequest = new IacUserIdentityConfigRequest();
            userIdentityConfigRequest.setRelatedId(newUserDetailDTO.getId());
            BeanUtils.copyProperties(userSyncData, userIdentityConfigRequest);
            userIdentityConfigAPI.createUserIdentityConfig(userIdentityConfigRequest);

        } else {
            LOGGER.info("采用更新策略 用户数据 {}", JSON.toJSONString(userSyncData));
            // 更新用户
            IacUpdateUserOnSyncDataDTO cbbUpdateUserOnSyncDataDTO = new IacUpdateUserOnSyncDataDTO();
            BeanUtils.copyProperties(userSyncData, cbbUpdateUserOnSyncDataDTO);
            cbbUpdateUserOnSyncDataDTO.setGroupId(groupId);
            cbbUpdateUserOnSyncDataDTO.setId(cbbUserDetailDTO.getId());
            cbbUpdateUserOnSyncDataDTO.setIsUserModifyPassword(userSyncData.getUserModifyPassword());
            // 采用更新方案
            userAPI.updateUserOnSyncData(cbbUpdateUserOnSyncDataDTO);

            IacUserIdentityConfigRequest userIdentityConfigRequest = new IacUserIdentityConfigRequest();
            userIdentityConfigRequest.setRelatedId(cbbUserDetailDTO.getId());
            BeanUtils.copyProperties(userSyncData, userIdentityConfigRequest);
            userIdentityConfigAPI.updateUserIdentityConfig(userIdentityConfigRequest);

        }

    }
}
