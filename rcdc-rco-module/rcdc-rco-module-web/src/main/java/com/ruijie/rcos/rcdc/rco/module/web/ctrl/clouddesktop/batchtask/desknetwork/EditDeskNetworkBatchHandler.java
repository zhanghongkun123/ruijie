package com.ruijie.rcos.rcdc.rco.module.web.ctrl.clouddesktop.batchtask.desknetwork;

import com.ruijie.rcos.gss.log.module.def.api.BaseAuditLogAPI;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.api.CbbNetworkMgmtAPI;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.dto.CbbDeskNetworkConfigDTO;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.dto.CbbDeskNetworkDTO;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.dto.CbbDeskNetworkDetailDTO;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.dto.CbbDeskNetworkDnsDTO;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.enums.CbbNetworkStrategyMode;
import com.ruijie.rcos.rcdc.rco.module.web.ctrl.clouddesktop.CloudDesktopBusinessKey;
import com.ruijie.rcos.rcdc.rco.module.web.ctrl.clouddesktop.request.desknetwork.EditDeskNetworkWebRequest;
import com.ruijie.rcos.sk.base.batch.*;
import com.ruijie.rcos.sk.base.exception.BusinessException;
import com.ruijie.rcos.sk.base.log.Logger;
import com.ruijie.rcos.sk.base.log.LoggerFactory;
import org.springframework.util.Assert;

import java.util.UUID;

/**
 * Description: 编辑桌面网络策略handler
 * Copyright: Copyright (c) 2019
 * Company: Ruijie Co., Ltd.
 * Create Time: 2020年4月08日
 *
 * @author nt
 */
public class EditDeskNetworkBatchHandler extends AbstractSingleTaskHandler {


    private static final Logger LOGGER = LoggerFactory.getLogger(EditDeskNetworkBatchHandler.class);

    private BaseAuditLogAPI auditLogAPI;

    private CbbNetworkMgmtAPI cbbNetworkMgmtAPI;

    private EditDeskNetworkWebRequest editWebRequest;

    private String networkName = "";

    public EditDeskNetworkBatchHandler(CbbNetworkMgmtAPI cbbNetworkMgmtAPI, EditDeskNetworkWebRequest editWebRequest, BatchTaskItem batchTaskItem,
                                       BaseAuditLogAPI auditLogAPI) {
        super(batchTaskItem);
        Assert.notNull(cbbNetworkMgmtAPI, "cbbNetworkMgmtAPI is not null");
        Assert.notNull(editWebRequest, "editWebRequest is not null");

        this.cbbNetworkMgmtAPI = cbbNetworkMgmtAPI;
        this.auditLogAPI = auditLogAPI;
        this.editWebRequest = editWebRequest;
    }

    @Override
    public BatchTaskFinishResult onFinish(int successCount, int failCount) {
        if (failCount == 0) {
            return DefaultBatchTaskFinishResult.builder().batchTaskStatus(BatchTaskStatus.SUCCESS)
                    .msgKey(CloudDesktopBusinessKey.RCDC_CLOUDDESKTOP_VIRTUAL_NETWORK_EDIT_TASK_SUCCESS) //
                    .msgArgs(new String[]{networkName}) //
                    .build();
        } else {
            return DefaultBatchTaskFinishResult.builder().batchTaskStatus(BatchTaskStatus.FAILURE)
                    .msgKey(CloudDesktopBusinessKey.RCDC_CLOUDDESKTOP_VIRTUAL_NETWORK_EDIT_TASK_FAIL) //
                    .msgArgs(new String[]{networkName}) //
                    .build();
        }
    }

    @Override
    public BatchTaskItemResult processItem(BatchTaskItem item) throws BusinessException {
        Assert.notNull(item, "item can not be null");
        String name = item.getItemID().toString();

        try {

            CbbDeskNetworkDTO cbbEditRequest = generateDeskNetworkRequest();

            name = getNetworkName(editWebRequest.getId());
            cbbNetworkMgmtAPI.editDeskNetwork(cbbEditRequest);
            auditLogAPI.recordLog(CloudDesktopBusinessKey.RCDC_CLOUDDESKTOP_VIRTUAL_NETWORK_EDIT_SUCCESS, cbbEditRequest.getDeskNetworkName());

            return DefaultBatchTaskItemResult.builder().batchTaskItemStatus(BatchTaskItemStatus.SUCCESS)
                    .msgKey(CloudDesktopBusinessKey.RCDC_CLOUDDESKTOP_VIRTUAL_NETWORK_EDIT_RESULT_SUCCESS).msgArgs(new String[]{name})
                    .build();
        } catch (BusinessException e) {
            LOGGER.error("编辑镜像异常[{}]", e);
            auditLogAPI.recordLog(CloudDesktopBusinessKey.RCDC_CLOUDDESKTOP_VIRTUAL_NETWORK_EDIT_FAIL, e, name,
                    e.getI18nMessage());
            throw new BusinessException(CloudDesktopBusinessKey.RCDC_CLOUDDESKTOP_VIRTUAL_NETWORK_EDIT_RESULT_FAIL, e, name, e.getI18nMessage());
        } finally {
            networkName = name;
        }

    }

    private CbbDeskNetworkDTO generateDeskNetworkRequest() throws BusinessException {
        CbbDeskNetworkDetailDTO deskNetworkDTO = cbbNetworkMgmtAPI.getDeskNetwork(editWebRequest.getId());
        Assert.notNull(deskNetworkDTO, "deskNetwork can not be null");
        Assert.notNull(deskNetworkDTO.getCbbVswitchDTO(), "vswitch info can not be null");

        CbbDeskNetworkDTO cbbEditRequest = new CbbDeskNetworkDTO();
        cbbEditRequest.setId(editWebRequest.getId());
        cbbEditRequest.setDeskNetworkName(editWebRequest.getDeskNetworkName());
        cbbEditRequest.setNetworkType(editWebRequest.getNetworkType());
        cbbEditRequest.setVlan(editWebRequest.getVlan());
        cbbEditRequest.setNetworkMode(editWebRequest.getNetworkMode());

        CbbDeskNetworkDnsDTO cbbNetworkDnsDTO = new CbbDeskNetworkDnsDTO();
        cbbNetworkDnsDTO.setDnsPrimary(editWebRequest.getDns().getDnsPrimary());
        cbbNetworkDnsDTO.setDnsSecondary(editWebRequest.getDns().getDnsSecondary());
        cbbEditRequest.setDns(cbbNetworkDnsDTO);
        if (CbbNetworkStrategyMode.DHCP != editWebRequest.getNetworkMode()) {
            CbbDeskNetworkConfigDTO cbbNetworkConfigDTO = new CbbDeskNetworkConfigDTO();
            cbbNetworkConfigDTO.setGateway(editWebRequest.getNetworkConfig().getGateway());
            cbbNetworkConfigDTO.setIpCidr(editWebRequest.getNetworkConfig().getIpCidr());
            cbbNetworkConfigDTO.setIpPoolArr(editWebRequest.getNetworkConfig().getIpPoolArr());
            cbbEditRequest.setNetworkConfig(cbbNetworkConfigDTO);
        }
        cbbEditRequest.setVswitchId(deskNetworkDTO.getCbbVswitchDTO().getId());
        cbbEditRequest.setNetworkType(deskNetworkDTO.getNetworkType());
        cbbEditRequest.setPlatformId(editWebRequest.getPlatformId());

        return cbbEditRequest;
    }

    private String getNetworkName(UUID id) throws BusinessException {
        CbbDeskNetworkDetailDTO dto = cbbNetworkMgmtAPI.getDeskNetwork(id);
        String result = id.toString();
        if (dto != null) {
            result = dto.getDeskNetworkName();
        }
        return result;
    }

}
