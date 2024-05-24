package com.ruijie.rcos.rcdc.rco.module.impl.spi;

import com.alibaba.fastjson.JSONObject;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.api.CbbImageTemplateMgmtAPI;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.api.CbbVDIDeskMgmtAPI;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.dto.CbbImageTemplateDetailDTO;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.dto.CbbImageTemplateEditingInfoDTO;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.dto.CbbVmGraphicsDTO;
import com.ruijie.rcos.rcdc.codec.adapter.def.dto.CbbDispatcherRequest;
import com.ruijie.rcos.rcdc.codec.adapter.def.dto.CbbResponseShineMessage;
import com.ruijie.rcos.rcdc.hciadapter.module.def.api.request.vm.VmIdRequest;
import com.ruijie.rcos.rcdc.rco.module.impl.Constants;
import com.ruijie.rcos.rcdc.rco.module.impl.dto.vdiedit.GetVmSpiceInfoResponseDTO;
import com.ruijie.rcos.rcdc.rco.module.impl.dto.vdiedit.VDIEditImageIdRequestDTO;
import com.ruijie.rcos.sk.base.exception.BusinessException;
import com.ruijie.rcos.sk.base.log.Logger;
import com.ruijie.rcos.sk.base.log.LoggerFactory;
import com.ruijie.rcos.sk.modulekit.api.comm.DispatcherImplemetion;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.Assert;

import java.util.UUID;

/**
 * Description: 查询虚机SPICE信息
 * Copyright: Copyright (c) 2020
 * Company: Ruijie Co., Ltd.
 * Create Time: 2020/2/21 17:09
 *
 * @author zhangyichi
 */
@DispatcherImplemetion(Constants.VDI_EDIT_IMAGE_GET_VM_SPICE_ACTION)
public class GetVmSpiceInfoHandlerSPIImpl extends AbstractVDIEditImageHandlerSPIImpl<VDIEditImageIdRequestDTO> {

    private static final Logger LOGGER = LoggerFactory.getLogger(GetVmSpiceInfoHandlerSPIImpl.class);

    @Autowired
    private CbbVDIDeskMgmtAPI vdiDeskMgmtAPI;

    @Autowired
    private VDIEditImageHelper helper;

    @Autowired
    private CbbImageTemplateMgmtAPI cbbImageTemplateMgmtAPI;

    @Override
    CbbResponseShineMessage getResponseMessage(CbbDispatcherRequest request, VDIEditImageIdRequestDTO requestDTO, UUID adminId) {
        // 查询传入的镜像是否允许被当前管理员操作，并获取编辑信息
        CbbImageTemplateEditingInfoDTO editingInfoDTO =
                helper.getImageEditingInfoMustExist(requestDTO.getId(), adminId, request.getTerminalId());
        LOGGER.debug("获取镜像编辑信息：[{}]", editingInfoDTO == null ? "null" : editingInfoDTO.toString());
        if (editingInfoDTO == null) {
            return buildErrorResponseMessage(request);
        }

        // 获取虚机spice信息
        GetVmSpiceInfoResponseDTO responseDTO = new GetVmSpiceInfoResponseDTO();
        try {
            CbbImageTemplateDetailDTO imageTemplateDetailDTO = cbbImageTemplateMgmtAPI.getImageTemplateDetail(editingInfoDTO.getImageTemplateId());
            Assert.notNull(imageTemplateDetailDTO, "image template detail is null!");
            Assert.notNull(imageTemplateDetailDTO.getTempVmId(), "tempVmId is null!");
            LOGGER.debug("镜像虚机ID[{}]", imageTemplateDetailDTO.getTempVmId());
            getVmSpiceInfo(imageTemplateDetailDTO, requestDTO.getId(), responseDTO);
            LOGGER.debug("获取到spice信息：[{}]", responseDTO.toString());
        } catch (BusinessException e) {
            LOGGER.error("获取spice信息失败", e);
            responseDTO.setMessage(e.getI18nMessage());
        } catch (Exception e) {
            LOGGER.error("获取spice信息失败", e);
            responseDTO.setMessage(e.getMessage());
        }
        return buildResponseMessage(request, responseDTO);
    }

    private void getVmSpiceInfo(CbbImageTemplateDetailDTO imageTemplateDetailDTO, 
                                UUID imageId, GetVmSpiceInfoResponseDTO responseDTO) throws BusinessException {
        CbbVmGraphicsDTO vmGraphicsDTO = vdiDeskMgmtAPI.querySpiceById(new VmIdRequest(imageTemplateDetailDTO.getPlatformId(),
                imageTemplateDetailDTO.getTempVmId()));

        responseDTO.setIp(vmGraphicsDTO.getAddress());
        responseDTO.setPort(vmGraphicsDTO.getPort());
        // 虚机不开启ssl
        responseDTO.setEnableSsl(false);
        responseDTO.setSslPassword(imageId.toString());
    }

    @Override
    VDIEditImageIdRequestDTO resolveJsonString(String dataJsonString) {
        return JSONObject.parseObject(dataJsonString, VDIEditImageIdRequestDTO.class);
    }
}
