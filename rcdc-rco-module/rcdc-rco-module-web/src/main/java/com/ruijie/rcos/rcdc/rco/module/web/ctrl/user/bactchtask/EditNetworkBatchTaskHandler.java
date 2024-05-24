package com.ruijie.rcos.rcdc.rco.module.web.ctrl.user.bactchtask;

import com.ruijie.rcos.gss.sdk.iac.module.def.enums.IacUserTypeEnum;
import com.ruijie.rcos.gss.log.module.def.api.BaseAuditLogAPI;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.api.CbbDesktopPoolMgmtAPI;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.api.CbbIDVDeskMgmtAPI;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.api.CbbIDVDeskOperateAPI;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.enums.desktoppool.CbbDesktopPoolState;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.enums.desktoppool.DesktopPoolType;
import com.ruijie.rcos.rcdc.rco.module.def.api.ClusterAPI;
import com.ruijie.rcos.rcdc.rco.module.def.api.UserDesktopMgmtAPI;
import com.ruijie.rcos.rcdc.rco.module.def.api.UserDesktopOperateAPI;
import com.ruijie.rcos.rcdc.rco.module.def.api.dto.CloudDesktopDetailDTO;
import com.ruijie.rcos.rcdc.rco.module.def.api.request.EditDesktopNetworkRequest;
import com.ruijie.rcos.rcdc.rco.module.web.ctrl.user.UserBusinessKey;
import com.ruijie.rcos.rcdc.rco.module.web.ctrl.user.bactchtask.param.EditNetworkBatchParam;
import com.ruijie.rcos.rcdc.rco.module.web.service.CloudDesktopWebService;
import com.ruijie.rcos.rcdc.rco.module.web.service.SpringBeanHelper;
import com.ruijie.rcos.sk.base.batch.*;
import com.ruijie.rcos.sk.base.exception.BusinessException;
import com.ruijie.rcos.sk.base.log.Logger;
import com.ruijie.rcos.sk.base.log.LoggerFactory;
import com.ruijie.rcos.sk.base.util.StringUtils;
import org.springframework.util.Assert;

import java.util.Iterator;
import java.util.Objects;
import java.util.UUID;

/**
 * 
 * Description: 批量修改网络
 * Copyright: Copyright (c) 2018
 * Company: Ruijie Co., Ltd.
 * Create Time: 2019年1月16日
 * 
 * @author artom
 */
public class EditNetworkBatchTaskHandler extends AbstractDesktopBatchTaskHandler {

    private static final Logger LOGGER = LoggerFactory.getLogger(EditNetworkBatchTaskHandler.class);

    private BaseAuditLogAPI auditLogAPI;

    private UUID networkId;

    private String ip;

    private UUID desktopPoolId = null;

    private CbbDesktopPoolMgmtAPI cbbDesktopPoolMgmtAPI;

    private ClusterAPI clusterAPI;

    public EditNetworkBatchTaskHandler(Iterator<? extends BatchTaskItem> iterator, UUID networkId, BaseAuditLogAPI auditLogAPI) {
        super(iterator);
        Assert.notNull(iterator, "iterator is not null");
        Assert.notNull(networkId, "networkId is not null");
        Assert.notNull(auditLogAPI, "auditLogAPI is not null");
        this.networkId = networkId;
        this.auditLogAPI = auditLogAPI;
        this.cbbDesktopPoolMgmtAPI = SpringBeanHelper.getBean(CbbDesktopPoolMgmtAPI.class);
        this.clusterAPI = SpringBeanHelper.getBean(ClusterAPI.class);
    }

    public EditNetworkBatchTaskHandler(Iterator<? extends BatchTaskItem> iterator, EditNetworkBatchParam param,
                                       BaseAuditLogAPI auditLogAPI) {
        super(iterator);
        Assert.notNull(iterator, "iterator is not null");
        Assert.notNull(param, "param is not null");
        Assert.notNull(auditLogAPI, "auditLogAPI is not null");
        this.networkId = param.getNetwork().getAddress().getId();
        this.auditLogAPI = auditLogAPI;
        this.ip = param.getNetwork().getAddress().getIp();
        this.clusterAPI = SpringBeanHelper.getBean(ClusterAPI.class);
    }

    @Override
    public EditNetworkBatchTaskHandler setCloudDesktopWebService(CloudDesktopWebService cloudDesktopWebService) {
        Assert.notNull(cloudDesktopWebService, "cloudDesktopWebService must not be null");
        this.cloudDesktopWebService = cloudDesktopWebService;
        return this;
    }

    @Override
    public EditNetworkBatchTaskHandler setCbbUserDesktopMgmtAPI(UserDesktopMgmtAPI cloudDesktopMgmtAPI) {
        Assert.notNull(cloudDesktopMgmtAPI, "cloudDesktopMgmtAPI must not be null");
        this.cloudDesktopMgmtAPI = cloudDesktopMgmtAPI;
        return this;
    }

    @Override
    public EditNetworkBatchTaskHandler setCbbUserDesktopOperateAPI(UserDesktopOperateAPI cloudDesktopOperateAPI) {
        Assert.notNull(cloudDesktopOperateAPI, "cloudDesktopOperateAPI must not be null");
        this.cloudDesktopOperateAPI = cloudDesktopOperateAPI;
        return this;
    }

    @Override
    public EditNetworkBatchTaskHandler setCbbIDVDeskOperateAPI(CbbIDVDeskOperateAPI cbbIDVDeskOperateAPI) {
        // 不需要设置
        return null;
    }

    @Override
    public EditNetworkBatchTaskHandler setcbbIDVDeskMgmtAPI(CbbIDVDeskMgmtAPI cbbIDVDeskMgmtAPI) {
        // 不需要设置
        return null;
    }

    @Override
    public BatchTaskItemResult processItem(BatchTaskItem taskItem) throws BusinessException {
        Assert.notNull(taskItem, "taskItem is not null");

        CloudDesktopDetailDTO desktopDTO = cloudDesktopWebService.obtainCloudDesktopResponse(taskItem.getItemID());
        String tmpDesktopName = desktopDTO.getDesktopName();
        EditDesktopNetworkRequest apiRequest = buildEditDesktopNetworkRequest(taskItem);
        try {
            cloudDesktopWebService.checkThirdPartyDesktop(taskItem.getItemID(), UserBusinessKey.RCDC_RCO_DESKTOP_EDIT_NETWORK_THIRD_PARTY);
            checkBeforeUpdate(desktopDTO, apiRequest.getNetworkId());

            cloudDesktopMgmtAPI.configNetwork(apiRequest);
            auditLogAPI.recordLog(UserBusinessKey.RCDC_RCO_DESKTOP_EDIT_NETWORK_SUC_LOG, tmpDesktopName);
            return DefaultBatchTaskItemResult.builder().batchTaskItemStatus(BatchTaskItemStatus.SUCCESS)
                    .msgKey(UserBusinessKey.RCDC_RCO_DESKTOP_EDIT_NETWORK_ITEM_SUC_DESC).msgArgs(new String[] {tmpDesktopName}).build();
        } catch (BusinessException e) {
            LOGGER.error("编辑云桌面的网络策略失败，云桌面id=" + taskItem.getItemID().toString(), e);
            auditLogAPI.recordLog(UserBusinessKey.RCDC_RCO_DESKTOP_EDIT_NETWORK_FAIL_LOG, tmpDesktopName, e.getI18nMessage());
            throw new BusinessException(UserBusinessKey.RCDC_RCO_DESKTOP_EDIT_NETWORK_ITEM_FAIL_DESC, e, tmpDesktopName, e.getI18nMessage());
        } finally {
            desktopName = tmpDesktopName;
            processItemCount.incrementAndGet();
        }
    }

    private void checkBeforeUpdate(CloudDesktopDetailDTO desktopDTO, UUID networkId) throws BusinessException {
        if (Objects.isNull(desktopPoolId) && StringUtils.equals(desktopDTO.getDesktopPoolType(), DesktopPoolType.DYNAMIC.name())) {
            throw new BusinessException(UserBusinessKey.RCDC_RCO_DESKTOP_EDIT_NETWORK_DYNAMIC_POOL_NOT_SUPPORT, desktopDTO.getDesktopName());
        }
        // 访客用户不允许网络云桌面策略
        if (StringUtils.equals(IacUserTypeEnum.VISITOR.name(), desktopDTO.getUserType())) {
            throw new BusinessException(UserBusinessKey.RCDC_RCO_VISITOR_NOT_ALLOW_EDIT_NETWORK, new String[] {desktopDTO.getUserName()});
        }
        //检查多计算集群网络配置
        clusterAPI.validateVDIDesktopNetwork(desktopDTO.getClusterInfo().getId(),networkId);
    }

    private EditDesktopNetworkRequest buildEditDesktopNetworkRequest(BatchTaskItem taskItem) {
        if (StringUtils.isBlank(ip)) {
            return new EditDesktopNetworkRequest(taskItem.getItemID(), networkId);
        }
        return new EditDesktopNetworkRequest(taskItem.getItemID(), networkId, ip);
    }

    @Override
    public BatchTaskFinishResult onFinish(int sucCount, int failCount) {
        if (Objects.isNull(desktopPoolId)) {
            return normalDesktopResult(sucCount, failCount);
        } else {
            // 桌面池编辑网络策略结果
            return poolDesktopResult(sucCount, failCount);
        }
    }

    private BatchTaskFinishResult normalDesktopResult(int sucCount, int failCount) {
        if (processItemCount.get() == 1) {
            if (sucCount == 1) {
                return DefaultBatchTaskFinishResult.builder().msgKey(UserBusinessKey.RCDC_RCO_DESKTOP_EDIT_NETWORK_SINGLE_RESULT_SUC)
                        .msgArgs(new String[] {desktopName}).batchTaskStatus(BatchTaskStatus.SUCCESS).build();
            }
            return DefaultBatchTaskFinishResult.builder().msgKey(UserBusinessKey.RCDC_RCO_DESKTOP_EDIT_NETWORK_SINGLE_RESULT_FAIL)
                    .msgArgs(new String[] {desktopName}).batchTaskStatus(BatchTaskStatus.FAILURE).build();
        }
        return buildDefaultFinishResult(sucCount, failCount, UserBusinessKey.RCDC_RCO_DESKTOP_EDIT_NETWORK_BATCH_RESULT);
    }

    private BatchTaskFinishResult poolDesktopResult(int sucCount, int failCount) {
        updateDesktopPoolState();
        // 桌面池批量修改云桌面策略，都是绿字
        if (failCount > 0) {
            return DefaultBatchTaskFinishResult.builder().msgKey(UserBusinessKey.RCDC_RCO_DESKTOP_EDIT_POOL_NETWORK_SINGLE_RESULT_FAIL)
                    .msgArgs(new String[]{String.valueOf(sucCount), String.valueOf(failCount)}).batchTaskStatus(BatchTaskStatus.SUCCESS).build();
        }
        return DefaultBatchTaskFinishResult.builder().msgKey(UserBusinessKey.RCDC_RCO_DESKTOP_EDIT_POOL_NETWORK_SINGLE_RESULT_SUC)
                .msgArgs(new String[]{String.valueOf(sucCount)}).batchTaskStatus(BatchTaskStatus.SUCCESS).build();
    }

    private void updateDesktopPoolState() {
        if (Objects.isNull(desktopPoolId)) {
            return;
        }

        try {
            cbbDesktopPoolMgmtAPI.updateState(desktopPoolId, CbbDesktopPoolState.AVAILABLE);
        } catch (Exception e) {
            LOGGER.error(String.format("更新桌面池[%s]信息失败", desktopPoolId), e);
        }
    }

    public void setDesktopPoolId(UUID desktopPoolId) {
        this.desktopPoolId = desktopPoolId;
    }
}
