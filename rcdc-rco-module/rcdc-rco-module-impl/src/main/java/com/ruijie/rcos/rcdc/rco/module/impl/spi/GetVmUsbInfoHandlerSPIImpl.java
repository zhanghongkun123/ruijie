package com.ruijie.rcos.rcdc.rco.module.impl.spi;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.api.CbbUSBAdvancedSettingMgmtAPI;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.api.CbbUSBDeviceMgmtAPI;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.api.CbbUSBTypeMgmtAPI;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.api.request.usbdevice.CbbUSBDevicePageRequest;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.dto.CbbGetAllUSBTypeDTO;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.dto.CbbImageTemplateEditingInfoDTO;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.dto.CbbUSBDeviceDTO;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.dto.CbbUSBTypeDTO;
import com.ruijie.rcos.rcdc.codec.adapter.def.dto.CbbDispatcherRequest;
import com.ruijie.rcos.rcdc.codec.adapter.def.dto.CbbResponseShineMessage;
import com.ruijie.rcos.rcdc.rco.module.impl.Constants;
import com.ruijie.rcos.rcdc.rco.module.impl.dto.vdiedit.GetVmUsbInfoResponseDTO;
import com.ruijie.rcos.rcdc.rco.module.impl.dto.vdiedit.VDIEditImageIdRequestDTO;
import com.ruijie.rcos.sk.base.exception.BusinessException;
import com.ruijie.rcos.sk.base.log.Logger;
import com.ruijie.rcos.sk.base.log.LoggerFactory;
import com.ruijie.rcos.sk.modulekit.api.comm.DefaultPageResponse;
import com.ruijie.rcos.sk.modulekit.api.comm.DispatcherImplemetion;
import com.ruijie.rcos.sk.modulekit.api.tool.GlobalParameterAPI;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.Assert;

import java.util.List;
import java.util.Optional;
import java.util.Objects;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;
import java.util.stream.Stream;



/**
 * Description: 获取虚机USB信息
 * Copyright: Copyright (c) 2020
 * Company: Ruijie Co., Ltd.
 * Create Time: 2020/2/24 16:59
 *
 * @author zhangyichi
 */
@DispatcherImplemetion(Constants.VDI_EDIT_IMAGE_GET_VM_USB_INFO_ACTION)
public class GetVmUsbInfoHandlerSPIImpl extends AbstractVDIEditImageHandlerSPIImpl<VDIEditImageIdRequestDTO> {

    private static final Logger LOGGER = LoggerFactory.getLogger(GetVmUsbInfoHandlerSPIImpl.class);

    private static final String SIGN_COMMA = ",";

    private static final Integer INT_1 = 1;

    private static final String SIGN_VERTICAL = "|";

    private static final int DEFAULT_DEVICE_LIMIT = 1000;

    @Autowired
    private CbbUSBTypeMgmtAPI cbbUSBTypeMgmtAPI;

    @Autowired
    private CbbUSBDeviceMgmtAPI cbbUSBDeviceMgmtAPI;

    @Autowired
    private CbbUSBAdvancedSettingMgmtAPI cbbUSBAdvancedSettingMgmtAPI;

    @Autowired
    private VDIEditImageHelper helper;

    @Autowired
    private GlobalParameterAPI globalParameterAPI;

    @Override
    CbbResponseShineMessage getResponseMessage(CbbDispatcherRequest request, VDIEditImageIdRequestDTO requestDTO, UUID adminId) {
        // 查询传入的镜像是否允许被当前管理员操作，并获取编辑信息
        CbbImageTemplateEditingInfoDTO editingInfoDTO =
                helper.getImageEditingInfoMustExist(requestDTO.getId(), adminId, request.getTerminalId());
        LOGGER.debug("获取镜像编辑信息：[{}]", editingInfoDTO == null ? "null" : editingInfoDTO.toString());
        if (editingInfoDTO == null) {
            return buildErrorResponseMessage(request);
        }

        try {
            GetVmUsbInfoResponseDTO responseDTO = new GetVmUsbInfoResponseDTO();
            GetVmUsbInfoResponseDTO.USBFilterInfo usbFilterDTO = responseDTO.new USBFilterInfo();
            // 允许使用其他USB设备，开放所有USB设备类型
            usbFilterDTO.setAllowInfo(getUsbFilterAllowString());
            responseDTO.setUsbFilter(usbFilterDTO);
            responseDTO.setUsbConf(getUsbConfString());
            LOGGER.debug("获取到USB信息：[{}]", responseDTO.toString());
            return buildResponseMessage(request, responseDTO);
        } catch (Exception e) {
            LOGGER.error("获取USB配置信息失败", e);
            return buildErrorResponseMessage(request);
        }

    }

    @Override
    VDIEditImageIdRequestDTO resolveJsonString(String dataJsonString) {
        return JSONObject.parseObject(dataJsonString, VDIEditImageIdRequestDTO.class);
    }

    private String getUsbConfString() throws BusinessException {
        // 临时虚机没有策略ID，传null返回默认配置
        return cbbUSBAdvancedSettingMgmtAPI.getUsbConfString(null);
    }

    private String getUsbFilterAllowString() throws BusinessException {
        CbbUSBTypeDTO[] usbTypeDTOArr = cbbUSBTypeMgmtAPI.getAllUSBType(new CbbGetAllUSBTypeDTO());
        Assert.notNull(usbTypeDTOArr, "usb type list is null!");
        // 过滤"其它设备"
        String otherUsbTypeIdStr = globalParameterAPI.findParameter(
                com.ruijie.rcos.rcdc.clouddesktop.module.def.constants.Constants.OTHER_USB_TYPE_ID);
        UUID otherUsbTypeId = Optional.ofNullable(otherUsbTypeIdStr).isPresent()
                ? UUID.fromString(otherUsbTypeIdStr) : UUID.randomUUID();

        List<UUID> allUSBTypeIdList = Stream.of(usbTypeDTOArr).map(CbbUSBTypeDTO::getId)
                .filter(id -> !otherUsbTypeId.equals(id)).collect(Collectors.toList());

        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug("vdi获取的usb设备类型id列表:[{}]", JSON.toJSONString(allUSBTypeIdList));
        }

        StringBuilder strBuilder = new StringBuilder();
        for (UUID usbTypeId : allUSBTypeIdList) {
            CbbUSBDevicePageRequest deviceRequest = new CbbUSBDevicePageRequest();
            deviceRequest.setUsbTypeId(usbTypeId);
            deviceRequest.setPage(0);
            deviceRequest.setLimit(DEFAULT_DEVICE_LIMIT);
            DefaultPageResponse<CbbUSBDeviceDTO> usbDeviceResponse = cbbUSBDeviceMgmtAPI.pageQueryUSBDevice(deviceRequest);
            CbbUSBDeviceDTO[] usbDeviceArr = usbDeviceResponse.getItemArr();
            for (CbbUSBDeviceDTO usbDeviceDTO : usbDeviceArr) {
                strBuilder.append(usbDeviceDTO.getDeviceClass()).append(SIGN_COMMA);
                strBuilder.append(usbDeviceDTO.getFirmFlag()).append(SIGN_COMMA);
                strBuilder.append(usbDeviceDTO.getProductFlag()).append(SIGN_COMMA);
                strBuilder.append(usbDeviceDTO.getReleaseVersion()).append(SIGN_COMMA);
                strBuilder.append(INT_1).append(SIGN_VERTICAL);
            }
        }
        LOGGER.debug("VDI终端镜像编辑允许的设备[{}]", strBuilder.toString());
        return strBuilder.toString();
    }
}
