package com.ruijie.rcos.rcdc.rco.module.impl.datasync.strategy.group.impl;

import java.util.Objects;
import java.util.UUID;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.Assert;

import com.alibaba.fastjson.JSON;
import com.ruijie.rcos.gss.sdk.iac.module.def.api.request.user.IacUserIdentityConfigRequest;
import com.ruijie.rcos.gss.sdk.iac.module.def.dto.user.IacUpdateUserGroupOnSyncDataDTO;
import com.ruijie.rcos.gss.sdk.iac.module.def.api.IacUserGroupMgmtAPI;
import com.ruijie.rcos.gss.sdk.iac.module.def.api.IacUserIdentityConfigMgmtAPI;
import com.ruijie.rcos.rcdc.rco.module.def.api.dto.UserGroupSyncDataDTO;
import com.ruijie.rcos.rcdc.rco.module.impl.datasync.DataSyncBusinessKey;
import com.ruijie.rcos.rcdc.rco.module.impl.datasync.strategy.group.UserGroupSyncDataStrategy;
import com.ruijie.rcos.rcdc.rco.module.impl.entity.ViewUserGroupSyncDataEntity;
import com.ruijie.rcos.rcdc.rco.module.impl.service.ViewUserGroupSyncDataService;
import com.ruijie.rcos.sk.base.exception.BusinessException;
import com.ruijie.rcos.sk.base.i18n.LocaleI18nResolver;
import com.ruijie.rcos.sk.base.log.Logger;
import com.ruijie.rcos.sk.base.log.LoggerFactory;

/**
 * Description:
 * Copyright: Copyright (c)
 * Company: Ruijie Co., Ltd.
 * Create Time: 2023/09/21 9:50
 *
 * @author coderLee23
 */
public abstract class AbstractCommonUserGroupSyncDataStrategy implements UserGroupSyncDataStrategy {

    private static final Logger LOGGER = LoggerFactory.getLogger(AbstractCommonUserGroupSyncDataStrategy.class);

    @Autowired
    private ViewUserGroupSyncDataService viewUserGroupSyncDataService;

    @Autowired
    private IacUserGroupMgmtAPI userGroupAPI;

    @Autowired
    private IacUserIdentityConfigMgmtAPI userIdentityConfigAPI;

    @Override
    public void syncData(UserGroupSyncDataDTO userGroupSyncData) throws BusinessException {
        Assert.notNull(userGroupSyncData, "userGroupSyncData must not be null");
        String fullGroupName = userGroupSyncData.getFullGroupName();
        // 只做更新操作
        ViewUserGroupSyncDataEntity viewUserGroupSyncData = viewUserGroupSyncDataService.findViewUserGroupSyncData(fullGroupName);
        if (Objects.isNull(viewUserGroupSyncData)) {
            LOGGER.error("{}域用户组不存在 {}，则不更新！", getUserGroupType(), JSON.toJSONString(userGroupSyncData));
            throw new BusinessException(DataSyncBusinessKey.RCDC_RCO_DATA_SYNC_AD_OR_LDAP_USER_GROUP_NOT_EXISTS,
                    getUserGroupType().name(), userGroupSyncData.getName());
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
