package com.ruijie.rcos.rcdc.rco.module.web.ctrl.user.bactchtask;

import java.util.Iterator;

import org.springframework.beans.BeanUtils;
import org.springframework.util.Assert;

import com.ruijie.rcos.gss.sdk.iac.module.def.dto.user.IacUpdateUserDTO;
import com.ruijie.rcos.gss.sdk.iac.module.def.dto.user.IacUserDetailDTO;
import com.ruijie.rcos.gss.sdk.iac.module.def.dto.user.IacUserGroupDetailDTO;
import com.ruijie.rcos.gss.log.module.def.api.BaseAuditLogAPI;
import com.ruijie.rcos.gss.sdk.iac.module.def.api.IacUserMgmtAPI;
import com.ruijie.rcos.rcdc.rco.module.def.api.UserDesktopConfigAPI;
import com.ruijie.rcos.rcdc.rco.module.def.api.dto.UserDesktopConfigDTO;
import com.ruijie.rcos.rcdc.rco.module.def.api.dto.UserGroupDesktopConfigDTO;
import com.ruijie.rcos.rcdc.rco.module.def.api.request.CreateUserDesktopConfigRequest;
import com.ruijie.rcos.rcdc.rco.module.def.enums.UserCloudDeskTypeEnum;
import com.ruijie.rcos.rcdc.rco.module.web.ctrl.user.UserBusinessKey;
import com.ruijie.rcos.rcdc.rco.module.web.service.SpringBeanHelper;
import com.ruijie.rcos.sk.base.batch.AbstractBatchTaskHandler;
import com.ruijie.rcos.sk.base.batch.BatchTaskFinishResult;
import com.ruijie.rcos.sk.base.batch.BatchTaskItem;
import com.ruijie.rcos.sk.base.batch.BatchTaskItemResult;
import com.ruijie.rcos.sk.base.batch.BatchTaskItemStatus;
import com.ruijie.rcos.sk.base.batch.DefaultBatchTaskItemResult;
import com.ruijie.rcos.sk.base.exception.BusinessException;
import com.ruijie.rcos.sk.base.log.Logger;
import com.ruijie.rcos.sk.base.log.LoggerFactory;

/**
 * Description:
 * Copyright: Copyright (c) 2023
 * Company: Ruijie Co., Ltd.
 * Create Time: 2024/1/31
 *
 * @author linke
 */
public class ApplyVOIDesktopBatchTaskHandler extends AbstractBatchTaskHandler {

    private static final Logger LOGGER = LoggerFactory.getLogger(ApplyVOIDesktopBatchTaskHandler.class);

    private BaseAuditLogAPI auditLogAPI;

    private IacUserGroupDetailDTO groupInfo;

    private IacUserMgmtAPI cbbUserAPI;

    private UserDesktopConfigAPI userDesktopConfigAPI;

    public ApplyVOIDesktopBatchTaskHandler(IacUserGroupDetailDTO groupInfo, Iterator<? extends BatchTaskItem> iterator) {
        super(iterator);
        this.groupInfo = groupInfo;
        this.auditLogAPI = SpringBeanHelper.getBean(BaseAuditLogAPI.class);
        this.cbbUserAPI = SpringBeanHelper.getBean(IacUserMgmtAPI.class);
        this.userDesktopConfigAPI = SpringBeanHelper.getBean(UserDesktopConfigAPI.class);
    }

    @Override
    public BatchTaskFinishResult onFinish(int successCount, int failCount) {
        return buildDefaultFinishResult(successCount, failCount, UserBusinessKey.RCDC_RCO_APPLY_VOI_DESKTOP_BATCH_RESULT);
    }

    @Override
    public BatchTaskItemResult processItem(BatchTaskItem taskItem) throws BusinessException {
        Assert.notNull(taskItem, "taskItem can not be null");
        // 查询用户是否配置voi云桌面
        IacUserDetailDTO userDetailResponse = cbbUserAPI.getUserDetail(taskItem.getItemID());
        Assert.notNull(userDetailResponse, "userDetailResponse can not be null");
        String userName = userDetailResponse.getUserName();
        UserDesktopConfigDTO userDesktopConfigDTO =
                userDesktopConfigAPI.getUserDesktopConfig(userDetailResponse.getId(), UserCloudDeskTypeEnum.VOI);
        // 用户配置了云桌面策略并却关闭之后，该策略还存在，所以要加上镜像条件判断。
        if (null != userDesktopConfigDTO && null != userDesktopConfigDTO.getImageTemplateId()) {
            LOGGER.info("用户[{}]已配置了voi云桌面，不应用分组的桌面配置", taskItem.getItemID());
            return DefaultBatchTaskItemResult.builder().batchTaskItemStatus(BatchTaskItemStatus.SUCCESS)
                    .msgKey(UserBusinessKey.RCDC_RCO_APPLY_VOI_DESKTOP_IGNORE).msgArgs(new String[]{userName}).build();
        }

        try {
            IacUpdateUserDTO updateUserRequest = buildUpdateUserRequest(userDetailResponse);
            cbbUserAPI.updateUser(updateUserRequest);
            // 更新VOI配置
            updateUserVOIDesktopConfig(userDetailResponse, groupInfo);
            auditLogAPI.recordLog(UserBusinessKey.RCDC_RCO_BATCH_APPLY_GROUP_VOI_CONFIG_SUCCESS_LOG, new String[]{userName});
            return DefaultBatchTaskItemResult.builder().batchTaskItemStatus(BatchTaskItemStatus.SUCCESS)
                    .msgKey(UserBusinessKey.RCDC_RCO_APPLY_VOI_DESKTOP_RESULT_SUCCESS).msgArgs(new String[]{userName}).build();
        } catch (BusinessException e) {
            LOGGER.error("用户[" + userName + "]配置VOI云桌面失败", e);
            auditLogAPI.recordLog(UserBusinessKey.RCDC_RCO_BATCH_APPLY_GROUP_VOI_CONFIG_FAIL_LOG,
                    new String[]{userName, e.getI18nMessage()});
            throw new BusinessException(UserBusinessKey.RCDC_RCO_BATCH_APPLY_GROUP_VOI_CONFIG_FAIL_DESC, e, userName, e.getI18nMessage());
        }
    }

    private void updateUserVOIDesktopConfig(IacUserDetailDTO userDetail, IacUserGroupDetailDTO userGroupInfo) {
        UserGroupDesktopConfigDTO desktopConfigDTO =
                userDesktopConfigAPI.getUserGroupDesktopConfig(userGroupInfo.getId(), UserCloudDeskTypeEnum.VOI);
        CreateUserDesktopConfigRequest configRequest = new CreateUserDesktopConfigRequest(userDetail.getId(), UserCloudDeskTypeEnum.VOI);
        configRequest.setUserId(userDetail.getId());
        configRequest.setStrategyId(desktopConfigDTO.getStrategyId());
        configRequest.setImageTemplateId(desktopConfigDTO.getImageTemplateId());
        configRequest.setNetworkId(desktopConfigDTO.getNetworkId());
        configRequest.setSoftwareStrategyId(desktopConfigDTO.getSoftwareStrategyId());
        configRequest.setUserProfileStrategyId(desktopConfigDTO.getUserProfileStrategyId());
        userDesktopConfigAPI.createOrUpdateUserDesktopConfig(configRequest);
    }

    private IacUpdateUserDTO buildUpdateUserRequest(IacUserDetailDTO userDetailResponse) {
        IacUpdateUserDTO apiRequest = new IacUpdateUserDTO();
        BeanUtils.copyProperties(userDetailResponse, apiRequest, "userName");
        return apiRequest;
    }
}
