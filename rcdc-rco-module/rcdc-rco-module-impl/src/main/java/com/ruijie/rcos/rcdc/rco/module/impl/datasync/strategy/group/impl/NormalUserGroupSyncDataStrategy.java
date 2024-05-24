package com.ruijie.rcos.rcdc.rco.module.impl.datasync.strategy.group.impl;

import java.util.Objects;
import java.util.UUID;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;
import org.springframework.util.StringUtils;

import com.alibaba.fastjson.JSON;
import com.ruijie.rcos.gss.sdk.iac.module.def.api.request.user.IacUserIdentityConfigRequest;
import com.ruijie.rcos.gss.sdk.iac.module.def.dto.user.IacCreateUserGroupOnSyncDataDTO;
import com.ruijie.rcos.gss.sdk.iac.module.def.dto.user.IacUpdateUserGroupOnSyncDataDTO;
import com.ruijie.rcos.gss.sdk.iac.module.def.dto.user.IacUserGroupDetailDTO;
import com.ruijie.rcos.gss.sdk.iac.module.def.api.IacUserGroupMgmtAPI;
import com.ruijie.rcos.gss.sdk.iac.module.def.api.IacUserIdentityConfigMgmtAPI;
import com.ruijie.rcos.rcdc.rco.module.def.api.dto.UserGroupSyncDataDTO;
import com.ruijie.rcos.rcdc.rco.module.impl.datasync.enums.UserGroupTypeEnum;
import com.ruijie.rcos.rcdc.rco.module.impl.datasync.strategy.group.UserGroupSyncDataStrategy;
import com.ruijie.rcos.rcdc.rco.module.impl.entity.ViewUserGroupSyncDataEntity;
import com.ruijie.rcos.rcdc.rco.module.impl.service.ViewUserGroupSyncDataService;
import com.ruijie.rcos.sk.base.exception.BusinessException;
import com.ruijie.rcos.sk.base.log.Logger;
import com.ruijie.rcos.sk.base.log.LoggerFactory;

;

/**
 * Description:
 * Copyright: Copyright (c)
 * Company: Ruijie Co., Ltd.
 * Create Time: 2023/09/20 19:08
 *
 * @author coderLee23
 */
@Service
public class NormalUserGroupSyncDataStrategy implements UserGroupSyncDataStrategy {

    private static final Logger LOGGER = LoggerFactory.getLogger(NormalUserGroupSyncDataStrategy.class);

    private static final String SEPARATOR = ">";

    @Autowired
    private ViewUserGroupSyncDataService viewUserGroupSyncDataService;

    @Autowired
    private IacUserGroupMgmtAPI userGroupAPI;

    @Autowired
    private IacUserIdentityConfigMgmtAPI userIdentityConfigAPI;



    @Override
    public UserGroupTypeEnum getUserGroupType() {
        return UserGroupTypeEnum.NORMAL;
    }


    @Override
    public void syncData(UserGroupSyncDataDTO userGroupSyncData) throws BusinessException {
        Assert.notNull(userGroupSyncData, "userGroupSyncData must not be null");
        String fullGroupName = userGroupSyncData.getFullGroupName();
        int lastIndex = fullGroupName.lastIndexOf(SEPARATOR);
        String parentFullGroupName = lastIndex < 0 ? "" : fullGroupName.substring(0, lastIndex);

        // 只做更新操作
        ViewUserGroupSyncDataEntity viewUserGroupSyncData = viewUserGroupSyncDataService.findViewUserGroupSyncData(fullGroupName);
        if (Objects.isNull(viewUserGroupSyncData)) {
            LOGGER.info("采用新增策略 用户组数据 {}", JSON.toJSONString(userGroupSyncData));
            // root 的id是为空
            UUID parentId = null;
            if (StringUtils.hasText(parentFullGroupName)) {
                ViewUserGroupSyncDataEntity parentData = viewUserGroupSyncDataService.findViewUserGroupSyncData(parentFullGroupName);
                Assert.notNull(parentData, "parentData must not be null");
                parentId = parentData.getId();
            }

            IacCreateUserGroupOnSyncDataDTO cbbCreateUserGroupOnSyncDataDTO = new IacCreateUserGroupOnSyncDataDTO();
            BeanUtils.copyProperties(userGroupSyncData, cbbCreateUserGroupOnSyncDataDTO);
            cbbCreateUserGroupOnSyncDataDTO.setParentId(parentId);
            cbbCreateUserGroupOnSyncDataDTO.setIsAdGroup(userGroupSyncData.getAdGroup());
            cbbCreateUserGroupOnSyncDataDTO.setIsLdapGroup(userGroupSyncData.getLdapGroup());
            cbbCreateUserGroupOnSyncDataDTO.setIsThirdPartyGroup(userGroupSyncData.getThirdPartyGroup());
            IacUserGroupDetailDTO cbbUserGroupDetailDTO = userGroupAPI.createUserGroupOnSyncData(cbbCreateUserGroupOnSyncDataDTO);

            IacUserIdentityConfigRequest userIdentityConfigRequest = new IacUserIdentityConfigRequest();
            userIdentityConfigRequest.setRelatedId(cbbUserGroupDetailDTO.getId());
            BeanUtils.copyProperties(userGroupSyncData, userIdentityConfigRequest);
            userIdentityConfigAPI.updateUserIdentityConfig(userIdentityConfigRequest);

        } else {
            LOGGER.info("采用更新策略 用户组数据 {}", JSON.toJSONString(userGroupSyncData));

            UUID userGroupId = viewUserGroupSyncData.getId();
            IacUpdateUserGroupOnSyncDataDTO cbbUpdateUserGroupOnSyncDataDTO = new IacUpdateUserGroupOnSyncDataDTO();
            BeanUtils.copyProperties(userGroupSyncData, cbbUpdateUserGroupOnSyncDataDTO);
            cbbUpdateUserGroupOnSyncDataDTO.setId(userGroupId);
            cbbUpdateUserGroupOnSyncDataDTO.setIsAdGroup(userGroupSyncData.getAdGroup());
            cbbUpdateUserGroupOnSyncDataDTO.setIsLdapGroup(userGroupSyncData.getLdapGroup());
            cbbUpdateUserGroupOnSyncDataDTO.setIsThirdPartyGroup(userGroupSyncData.getThirdPartyGroup());
            userGroupAPI.updateUserGroupOnSyncData(cbbUpdateUserGroupOnSyncDataDTO);

            IacUserIdentityConfigRequest userIdentityConfigRequest = new IacUserIdentityConfigRequest();
            userIdentityConfigRequest.setRelatedId(userGroupId);
            BeanUtils.copyProperties(userGroupSyncData, userIdentityConfigRequest);
            userIdentityConfigAPI.updateUserIdentityConfig(userIdentityConfigRequest);
        }

    }

}
