package com.ruijie.rcos.rcdc.rco.module.web.ctrl.softclient.handler;

import com.ruijie.rcos.base.upgrade.module.def.api.BaseApplicationPacketAPI;
import com.ruijie.rcos.base.upgrade.module.def.api.request.ApplicationInstallerConfigRequest;
import com.ruijie.rcos.base.upgrade.module.def.enums.PacketProductType;
import com.ruijie.rcos.gss.log.module.def.api.BaseAuditLogAPI;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.BusinessKey;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.api.CbbSoftClientGlobalStrategyAPI;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.api.request.globalstrategy.softclient.CbbSoftClientGlobalStrategyDTO;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.api.request.globalstrategy.softclient.CbbSoftClientGlobalStrategyWithSlideDTO;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.api.request.globalstrategy.softclient.PictureDataInfoDTO;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.api.request.globalstrategy.softclient.SlideShowInfoDTO;
import com.ruijie.rcos.rcdc.rca.module.def.api.RcaNotifyAPI;
import com.ruijie.rcos.rcdc.rca.module.def.api.SlideShowInfoAPI;
import com.ruijie.rcos.rcdc.rca.module.def.api.dto.NotifyConfigChangeDTO;
import com.ruijie.rcos.rcdc.rca.module.def.api.dto.NotifyTargetDTO;
import com.ruijie.rcos.rcdc.rca.module.def.constants.RcaApiActionConstant;
import com.ruijie.rcos.rcdc.rca.module.def.enums.RcaEnum;
import com.ruijie.rcos.rcdc.rco.module.web.ctrl.commonupgrade.CommonUpgradeBusinessKey;
import com.ruijie.rcos.sk.base.batch.*;
import com.ruijie.rcos.sk.base.exception.BusinessException;
import com.ruijie.rcos.sk.base.log.Logger;
import com.ruijie.rcos.sk.base.log.LoggerFactory;
import org.apache.commons.compress.utils.Lists;
import org.apache.commons.lang3.StringUtils;
import org.springframework.util.Assert;
import org.springframework.util.CollectionUtils;

import java.util.List;
import java.util.Objects;

/**
 * Description:
 * Copyright: Copyright (c) 2020
 * Company: Ruijie Co., Ltd.
 * Create Time: 2020/5/19 10:45
 *
 * @author chenl
 */
public class SoftClientGlobalStrategyTaskHandler extends AbstractSingleTaskHandler {

    private static final Logger LOGGER = LoggerFactory.getLogger(SoftClientGlobalStrategyTaskHandler.class);

    private CbbSoftClientGlobalStrategyAPI cbbSoftClientGlobalStrategyAPI;

    private SlideShowInfoAPI slideShowInfoAPI;

    private BaseAuditLogAPI auditLogAPI;

    private RcaNotifyAPI rcaNotifyAPI;

    private BaseApplicationPacketAPI baseApplicationPacketAPI;

    private CbbSoftClientGlobalStrategyWithSlideDTO softClientGlobalStrategyWithSlideDTO;

    public SoftClientGlobalStrategyTaskHandler(CbbSoftClientGlobalStrategyWithSlideDTO softClientGlobalStrategyWithSlideDTO,
                                               BatchTaskItem batchTaskItem,
                                               CbbSoftClientGlobalStrategyAPI cbbSoftClientGlobalStrategyAPI,
                                               BaseAuditLogAPI auditLogAPI,
                                               SlideShowInfoAPI slideShowInfoAPI) {
        super(batchTaskItem);
        Assert.notNull(softClientGlobalStrategyWithSlideDTO, "softClientGlobalStrategyWithSlideDTO is not null");
        Assert.notNull(batchTaskItem, "batchTaskItem is not null");
        Assert.notNull(cbbSoftClientGlobalStrategyAPI, "cbbSoftClientGlobalStrategyAPI is not null");
        Assert.notNull(auditLogAPI, "auditLogAPI is not null");
        Assert.notNull(slideShowInfoAPI, "slideShowInfoAPI is not null");

        this.auditLogAPI = auditLogAPI;
        this.cbbSoftClientGlobalStrategyAPI = cbbSoftClientGlobalStrategyAPI;
        this.slideShowInfoAPI = slideShowInfoAPI;
        this.softClientGlobalStrategyWithSlideDTO = softClientGlobalStrategyWithSlideDTO;
    }

    @Override
    public BatchTaskItemResult processItem(BatchTaskItem batchTaskItem) throws BusinessException {
        Assert.notNull(batchTaskItem, "batchTaskItem can not be null");

        try {
            cbbSoftClientGlobalStrategyAPI.updateGlobalStrategy(softClientGlobalStrategyWithSlideDTO.getSoftClientGlobalConfig());
            if (softClientGlobalStrategyWithSlideDTO.getSlideShowInfo() != null) {
                SlideShowInfoDTO slideShowInfoDTO = softClientGlobalStrategyWithSlideDTO.getSlideShowInfo();
                try {
                    if (CollectionUtils.isEmpty(slideShowInfoDTO.getSlideshowImgList())) {
                        // 关闭轮播图
                        slideShowInfoAPI.deleteAllSlideInfo();
                    } else {
                        List<PictureDataInfoDTO> dataInfoDTOList = slideShowInfoDTO.getSlideshowImgList();
                        slideShowInfoAPI.editSlideInfo(dataInfoDTOList);
                    }
                } catch (BusinessException e) {
                    // 记录操作日志
                    LOGGER.error("编辑云应用全局配置的轮播图发生异常：", e);
                    auditLogAPI.recordLog(BusinessKey.RCDC_SOFT_CLIENT_GLOBAL_STRATEGY_SLIDE_SHOW_EDIT_FAIL_LOG, e.getI18nMessage());
                    throw e;
                }
            }
            SlideShowInfoDTO slideShowInfoDTO = slideShowInfoAPI.getSlideConfigDTO();
            softClientGlobalStrategyWithSlideDTO.setSlideShowInfo(slideShowInfoDTO);

            NotifyConfigChangeDTO notifyConfigChangeDTO = new NotifyConfigChangeDTO();
            notifyConfigChangeDTO.setNotifyComponentScope(RcaEnum.NotifyComponentScope.ONE_CLIENT);
            notifyConfigChangeDTO.setNotifyTargetList(NotifyTargetDTO.addGlobalTarget(Lists.newArrayList()));
            notifyConfigChangeDTO.setApiAction(RcaApiActionConstant.CDC_NOTIFY_OC_SOFT_CLIENT_GLOBAL_CONFIG);
            notifyConfigChangeDTO.setConfig(softClientGlobalStrategyWithSlideDTO);
            try {
                rcaNotifyAPI.notifyConfigChange(notifyConfigChangeDTO);
            } catch (Exception e) {
                LOGGER.error("通知OC软终端配置变化失败：{}", e.getMessage());
            }
            // 调用通用组件升级接口
            configInstaller();
            return DefaultBatchTaskItemResult.builder().batchTaskItemStatus(BatchTaskItemStatus.SUCCESS)
                    .msgKey(CommonUpgradeBusinessKey.RCDC_SOFT_CLIENT_GLOBAL_STRATEGY_SUCCESS_LOG).msgArgs().build();
        } catch (BusinessException ex) {
            auditLogAPI.recordLog(CommonUpgradeBusinessKey.RCDC_SOFT_CLIENT_GLOBAL_STRATEGY_FAIL_LOG, ex.getI18nMessage());
            throw new BusinessException(CommonUpgradeBusinessKey.RCDC_SOFT_CLIENT_GLOBAL_STRATEGY_FAIL_LOG, ex,
                    ex.getI18nMessage());
        }
    }


    @Override
    public BatchTaskFinishResult onFinish(int successCount, int failCount) {
        if (failCount == 0) {
            return DefaultBatchTaskFinishResult.builder().batchTaskStatus(BatchTaskStatus.SUCCESS)
                    .msgKey(CommonUpgradeBusinessKey.RCDC_SOFT_CLIENT_GLOBAL_STRATEGY_TASK_SUCCESS).msgArgs(new String[]{}).build();
        } else {
            return DefaultBatchTaskFinishResult.builder().batchTaskStatus(BatchTaskStatus.FAILURE)
                    .msgKey(CommonUpgradeBusinessKey.RCDC_SOFT_CLIENT_GLOBAL_STRATEGY_TASK_FAIL).msgArgs(new String[]{}).build();
        }
    }

    private void configInstaller() throws BusinessException {
        // 调用通用组件升级接口
        ApplicationInstallerConfigRequest request = new ApplicationInstallerConfigRequest();
        CbbSoftClientGlobalStrategyDTO softClientGlobalConfig = softClientGlobalStrategyWithSlideDTO.getSoftClientGlobalConfig();

        request.setProductType(PacketProductType.ONE_CLIENT);
        request.setProxyPort(softClientGlobalConfig.getProxyPort());
        request.setProxyIp(StringUtils.isBlank(softClientGlobalConfig.getProxyServerIp()) ? null : softClientGlobalConfig.getProxyServerIp());
        request.setServerIp(softClientGlobalConfig.getServerIp());
        request.setOpenOneInstall(true);
        baseApplicationPacketAPI.configInstaller(request);
    }


    public void setRcaNotifyAPI(RcaNotifyAPI rcaNotifyAPI) {
        this.rcaNotifyAPI = rcaNotifyAPI;
    }

    public void setBaseApplicationPacketAPI(BaseApplicationPacketAPI baseApplicationPacketAPI) {
        this.baseApplicationPacketAPI = baseApplicationPacketAPI;
    }
}
