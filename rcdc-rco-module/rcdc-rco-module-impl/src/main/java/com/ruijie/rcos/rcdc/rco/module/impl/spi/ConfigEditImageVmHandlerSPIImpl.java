package com.ruijie.rcos.rcdc.rco.module.impl.spi;

import com.alibaba.fastjson.JSONObject;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.api.CbbImageTemplateMgmtAPI;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.dto.image.CbbConfigVmForEditImageTemplateDTO;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.dto.image.CbbGetImageTemplateInfoDTO;
import com.ruijie.rcos.rcdc.codec.adapter.def.annotation.MaintainFilterAction;
import com.ruijie.rcos.rcdc.codec.adapter.def.dto.CbbDispatcherRequest;
import com.ruijie.rcos.rcdc.codec.adapter.def.dto.CbbResponseShineMessage;
import com.ruijie.rcos.rcdc.rco.module.impl.BusinessKey;
import com.ruijie.rcos.rcdc.rco.module.impl.Constants;
import com.ruijie.rcos.rcdc.rco.module.impl.dto.vdiedit.ConfigEditImageVmRequestDTO;
import com.ruijie.rcos.rcdc.rco.module.impl.dto.vdiedit.ConfigImageVmResponseDTO;
import com.ruijie.rcos.rcdc.rco.module.impl.util.CapacityUnitUtils;
import com.ruijie.rcos.rcdc.terminal.module.def.api.CbbTerminalOperatorAPI;
import com.ruijie.rcos.rcdc.terminal.module.def.api.dto.CbbTerminalBasicInfoDTO;
import com.ruijie.rcos.sk.base.exception.BusinessException;
import com.ruijie.rcos.sk.base.log.Logger;
import com.ruijie.rcos.sk.base.log.LoggerFactory;
import com.ruijie.rcos.sk.modulekit.api.comm.DispatcherImplemetion;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.UUID;

/**
 * Description: 配置镜像编辑虚机
 * Copyright: Copyright (c) 2020
 * Company: Ruijie Co., Ltd.
 * Create Time: 2020/2/21 01:26
 *
 * @author zhangyichi
 */
@DispatcherImplemetion(Constants.VDI_EDIT_IMAGE_CONFIG_VM_ACTION)
@MaintainFilterAction
public class ConfigEditImageVmHandlerSPIImpl extends AbstractVDIEditImageHandlerSPIImpl<ConfigEditImageVmRequestDTO> {

    private static final Logger LOGGER = LoggerFactory.getLogger(ConfigEditImageVmHandlerSPIImpl.class);

    @Autowired
    private CbbImageTemplateMgmtAPI cbbImageTemplateMgmtAPI;

    @Autowired
    private VDIEditImageHelper helper;

    @Autowired
    private CbbTerminalOperatorAPI cbbTerminalOperatorAPI;

    @Override
    CbbResponseShineMessage getResponseMessage(CbbDispatcherRequest request, ConfigEditImageVmRequestDTO requestDTO, UUID adminId) {
        // 查询传入的镜像是否允许被当前管理员操作
        try {
            helper.getImageEditingInfoIfPresent(requestDTO.getId(), adminId, request.getTerminalId());
        } catch (BusinessException e) {
            LOGGER.error("当前管理员不可操作目标镜像", e);
            return buildErrorResponseMessage(request);
        }

        String terminalAddr = request.getTerminalId();
        try {
            CbbTerminalBasicInfoDTO terminalBasicInfoDTO = cbbTerminalOperatorAPI.findBasicInfoByTerminalId(request.getTerminalId());
            terminalAddr = terminalBasicInfoDTO.getUpperMacAddrOrTerminalId();
        } catch (BusinessException e) {
            LOGGER.debug("查询终端信息发生异常，终端id: [{}]", request.getTerminalId());
        }

        CbbConfigVmForEditImageTemplateDTO configRequest = getConfigRequest(requestDTO);

        try {
            // Shine端目前没修改vgpu和计算机名的地方，直接取原来的值
            CbbGetImageTemplateInfoDTO imageTemplateInfoDTO = cbbImageTemplateMgmtAPI.getImageTemplateInfo(requestDTO.getId());
            configRequest.setVgpuInfoDTO(imageTemplateInfoDTO.getVgpuInfoDTO());
            configRequest.setComputerName(imageTemplateInfoDTO.getComputerName());

            cbbImageTemplateMgmtAPI.prepareEditImageTemplate(requestDTO.getId());
            cbbImageTemplateMgmtAPI.configVmForEditImageTemplate(configRequest);

            LOGGER.info("配置镜像编辑虚机成功");
            helper.recordLog(BusinessKey.RCDC_RCO_VDI_EDIT_IMAGE_CONFIG_VM, adminId, requestDTO.getId(), terminalAddr);

            return buildResponseMessage(request, ConfigImageVmResponseDTO.buildSuccessDTO());
        } catch (BusinessException be) {
            LOGGER.error("配置镜像编辑虚机出现业务异常", be);
            return buildResponseMessage(request, ConfigImageVmResponseDTO.buildFailDTO(be.getI18nMessage()));
        } catch (Exception e) {
            LOGGER.error("配置镜像编辑虚机出现非业务异常", e);
            return buildResponseMessage(request, ConfigImageVmResponseDTO.buildFailDTO(e.getMessage()));
        }
    }

    private CbbConfigVmForEditImageTemplateDTO getConfigRequest(ConfigEditImageVmRequestDTO requestDTO) {
        CbbConfigVmForEditImageTemplateDTO configRequest = new CbbConfigVmForEditImageTemplateDTO();
        configRequest.setCpuCoreCount(requestDTO.getCpu());
        configRequest.setMemorySize(CapacityUnitUtils.gb2Mb(requestDTO.getMemory()));
        configRequest.setDiskSize(requestDTO.getSystemDisk());
        configRequest.setDeskNetworkId(requestDTO.getNetworkId());
        configRequest.setImageTemplateId(requestDTO.getId());
        configRequest.setEnableNested(requestDTO.getEnableNested());
        return configRequest;
    }

    @Override
    ConfigEditImageVmRequestDTO resolveJsonString(String dataJsonString) {
        return JSONObject.parseObject(dataJsonString, ConfigEditImageVmRequestDTO.class);
    }
}
