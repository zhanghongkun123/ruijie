package com.ruijie.rcos.rcdc.rco.module.web.ctrl.terminal.batchtask;

import com.alibaba.fastjson.JSON;
import com.ruijie.rcos.base.upgrade.module.def.api.BaseApplicationPacketAPI;
import com.ruijie.rcos.base.upgrade.module.def.api.request.ApplicationInstallerConfigRequest;
import com.ruijie.rcos.base.upgrade.module.def.enums.PacketProductType;
import com.ruijie.rcos.gss.log.module.def.api.BaseAuditLogAPI;
import com.ruijie.rcos.rcdc.rco.module.def.api.RcoGlobalParameterAPI;
import com.ruijie.rcos.rcdc.rco.module.def.api.dto.AppClientCompressionDTO;
import com.ruijie.rcos.rcdc.rco.module.def.api.request.UpdateParameterRequest;
import com.ruijie.rcos.rcdc.rco.module.web.Constants;
import com.ruijie.rcos.rcdc.rco.module.web.ctrl.rco.RcoBusinessKey;
import com.ruijie.rcos.rcdc.rco.module.web.ctrl.user.enums.UserOrGroupBatchStateCache;
import com.ruijie.rcos.sk.base.batch.*;
import com.ruijie.rcos.sk.base.exception.BusinessException;
import com.ruijie.rcos.sk.base.log.Logger;
import com.ruijie.rcos.sk.base.log.LoggerFactory;
import org.apache.commons.lang3.StringUtils;

import java.util.Iterator;

/**
 * Description: 一键安装批处理
 * Copyright: Copyright (c) 2022
 * Company: Ruijie Co., Ltd.
 * Create Time: 2022/3/17
 *
 * @author TD
 */
public class EditOneClickInstallBatchTaskHandler extends AbstractBatchTaskHandler {

    private static final Logger LOGGER = LoggerFactory.getLogger(EditOneClickInstallBatchTaskHandler.class);

    private BaseAuditLogAPI auditLogAPI;

    private RcoGlobalParameterAPI globalParameterAPI;

    private AppClientCompressionDTO newCompressionDTO;

    private BaseApplicationPacketAPI baseApplicationPacketAPI;

    /**
     * 一键安装KEY
     */
    private static final String APP_ONE_CLICK_INSTALL = "app_one_click_install";

    public EditOneClickInstallBatchTaskHandler(Iterator<? extends BatchTaskItem> batchTaskItemIterator, BaseAuditLogAPI auditLogAPI,
            RcoGlobalParameterAPI globalParameterAPI, BaseApplicationPacketAPI baseApplicationPacketAPI, AppClientCompressionDTO compressionDTO) {
        super(batchTaskItemIterator);
        this.auditLogAPI = auditLogAPI;
        this.globalParameterAPI = globalParameterAPI;
        this.newCompressionDTO = compressionDTO;
        this.baseApplicationPacketAPI = baseApplicationPacketAPI;
    }

    @Override
    public BatchTaskItemResult processItem(BatchTaskItem batchTaskItem) throws BusinessException {
        String message = "";
        try {
            ApplicationInstallerConfigRequest request = new ApplicationInstallerConfigRequest();
            request.setProductType(PacketProductType.ONE_CLIENT);
            request.setProxyPort(newCompressionDTO.getProxyPort());
            request.setProxyIp(StringUtils.isBlank(newCompressionDTO.getProxyServerIp()) ? null : newCompressionDTO.getProxyServerIp());
            request.setServerIp(newCompressionDTO.getServerIp());
            request.setOpenOneInstall(true);

            baseApplicationPacketAPI.configInstaller(request);
            // 修改数据库配置
            globalParameterAPI.updateParameter(new UpdateParameterRequest(APP_ONE_CLICK_INSTALL, JSON.toJSONString(newCompressionDTO)));

            auditLogAPI.recordLog(RcoBusinessKey.RCDC_RCO_UPDATE_ONE_CLICK_INSTALL_SUCCESS_LOG, message);
            return DefaultBatchTaskItemResult.builder().batchTaskItemStatus(BatchTaskItemStatus.SUCCESS)
                    .msgKey(RcoBusinessKey.RCDC_RCO_UPDATE_ONE_CLICK_INSTALL_SUCCESS_LOG).msgArgs(message).build();
        } catch (BusinessException e) {
            LOGGER.error("修改一键安装功能配置失败，失败原因：[{}]", e.getI18nMessage());
            auditLogAPI.recordLog(RcoBusinessKey.RCDC_RCO_UPDATE_ONE_CLICK_INSTALL_FAIL_LOG, message, e.getI18nMessage());
            throw new BusinessException(RcoBusinessKey.RCDC_RCO_UPDATE_ONE_CLICK_INSTALL_FAIL_LOG, e, message, e.getI18nMessage());
        }
    }

    @Override
    public BatchTaskFinishResult onFinish(int successCount, int failCount) {
        UserOrGroupBatchStateCache.STATE.removeSyncTask(Constants.ONE_CLICK_INSTALL);
        return buildDefaultFinishResult(successCount, failCount, RcoBusinessKey.RCDC_RCO_UPDATE_ONE_CLICK_INSTALL_RESULT);
    }

}

