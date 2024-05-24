package com.ruijie.rcos.rcdc.rco.module.impl.user.strategy.impl;

import java.util.Collections;
import java.util.UUID;

import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import com.ruijie.rcos.gss.sdk.iac.module.def.dto.user.IacUserDetailDTO;
import com.ruijie.rcos.rcdc.rco.module.def.enums.UserOperNotifyEnum;
import com.ruijie.rcos.gss.sdk.iac.module.def.enums.IacUserTypeEnum;
import com.ruijie.rcos.rcdc.rco.module.def.user.dto.UserOperSyncNotifyDTO;
import com.ruijie.rcos.rcdc.rco.module.def.api.dto.UserGroupDesktopConfigDTO;
import com.ruijie.rcos.rcdc.rco.module.def.api.request.CreateUserDesktopConfigRequest;
import com.ruijie.rcos.rcdc.rco.module.def.enums.UserCloudDeskTypeEnum;
import com.ruijie.rcos.sk.base.exception.BusinessException;
import com.ruijie.rcos.sk.base.log.Logger;
import com.ruijie.rcos.sk.base.log.LoggerFactory;

/**
 * Description:
 * Copyright: Copyright (c)
 * Company: Ruijie Co., Ltd.
 * Create Time: 2023/09/26 15:07
 *
 * @author coderLee23
 */
@Service
public class AddUserOperSyncNotifyStrategy extends AbstractOperSyncNotifyStrategy {

    private static final Logger LOGGER = LoggerFactory.getLogger(AddUserOperSyncNotifyStrategy.class);

    @Override
    public String getOper() {
        return UserOperNotifyEnum.ADD_USER.getOper();
    }

    @Override
    public void syncNotifyUserChange(UserOperSyncNotifyDTO cbbUserOperSyncNotifyRequest) throws BusinessException {
        Assert.notNull(cbbUserOperSyncNotifyRequest, "cbbUserOperSyncNotifyRequest must not be null");
        IacUserDetailDTO userDetailDTO = createIdvDesktopConfig(cbbUserOperSyncNotifyRequest.getRelatedId());
        if (!hasUserIdentityConfig(userDetailDTO)) {
            LOGGER.warn("访客收到用户操作请求，不处理身份验证记录");
            return;
        }

        // 推送用户数据到rccm
        rccmManageService.pushUser(Collections.singletonList(userDetailDTO.getUserName()), false);

        if (Boolean.TRUE.equals(cbbUserOperSyncNotifyRequest.getEnableSyncData())) {
            dataSyncService.activeSyncUserData(userDetailDTO.getId());
        }
    }


    private boolean hasUserIdentityConfig(IacUserDetailDTO userDetailDTO) throws BusinessException {
        return userIdentityConfigAPI.hasUserIdentityConfig(userDetailDTO.getUserType());
    }

    private IacUserDetailDTO createIdvDesktopConfig(UUID userId) throws BusinessException {
        IacUserDetailDTO userDetail = userAPI.getUserDetail(userId);
        if (userDetail.getUserType() == IacUserTypeEnum.AD || userDetail.getUserType() == IacUserTypeEnum.LDAP
                || userDetail.getUserType() == IacUserTypeEnum.THIRD_PARTY) {
            LOGGER.info("当前新增的是同步的域用户[{}],需要检查组idv配置", userDetail.getUserName());
            UserGroupDesktopConfigDTO userGroupDesktopConfigDTO =
                    userDesktopConfigAPI.getUserGroupDesktopConfig(userDetail.getGroupId(), UserCloudDeskTypeEnum.IDV);
            // IDV 配置桌面存在 才进行创建
            if (userGroupDesktopConfigDTO == null) {
                LOGGER.info("域用户[{}]的组没有配置IDV桌面，不需要生成桌面配置", userDetail.getUserName());
            } else {
                CreateUserDesktopConfigRequest idvConfigRequest = new CreateUserDesktopConfigRequest(userId, UserCloudDeskTypeEnum.IDV);
                idvConfigRequest.setNetworkId(userGroupDesktopConfigDTO.getNetworkId());
                idvConfigRequest.setStrategyId(userGroupDesktopConfigDTO.getStrategyId());
                idvConfigRequest.setImageTemplateId(userGroupDesktopConfigDTO.getImageTemplateId());
                idvConfigRequest.setSoftwareStrategyId(userGroupDesktopConfigDTO.getSoftwareStrategyId());
                idvConfigRequest.setUserProfileStrategyId(userGroupDesktopConfigDTO.getUserProfileStrategyId());
                userDesktopConfigAPI.createOrUpdateUserDesktopConfig(idvConfigRequest);
            }
            UserGroupDesktopConfigDTO voiUserGroupDesktopConfigDTO =
                    userDesktopConfigAPI.getUserGroupDesktopConfig(userDetail.getGroupId(), UserCloudDeskTypeEnum.VOI);
            // VOI 配置桌面存在 才进行创建
            if (voiUserGroupDesktopConfigDTO == null) {
                LOGGER.info("域用户[{}]的组没有配置VOI桌面，不需要生成桌面配置", userDetail.getUserName());
            } else {
                CreateUserDesktopConfigRequest voiConfigRequest = new CreateUserDesktopConfigRequest(userId, UserCloudDeskTypeEnum.VOI);
                voiConfigRequest.setNetworkId(voiUserGroupDesktopConfigDTO.getNetworkId());
                voiConfigRequest.setStrategyId(voiUserGroupDesktopConfigDTO.getStrategyId());
                voiConfigRequest.setImageTemplateId(voiUserGroupDesktopConfigDTO.getImageTemplateId());
                voiConfigRequest.setSoftwareStrategyId(voiUserGroupDesktopConfigDTO.getSoftwareStrategyId());
                voiConfigRequest.setUserProfileStrategyId(voiUserGroupDesktopConfigDTO.getUserProfileStrategyId());
                userDesktopConfigAPI.createOrUpdateUserDesktopConfig(voiConfigRequest);
            }
        }
        return userDetail;
    }

}
