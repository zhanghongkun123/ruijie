package com.ruijie.rcos.rcdc.rco.module.impl.migration.spi;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.api.CbbDeskMgmtAPI;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.api.CbbImageTemplateMgmtAPI;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.dto.CbbDeskDTO;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.dto.CbbGuesttoolMessageDTO;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.dto.CbbImageTemplateDetailDTO;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.spi.CbbGuestToolMessageDispatcherSPI;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.spi.request.CbbGuestToolSPIReceiveRequest;
import com.ruijie.rcos.rcdc.hciadapter.module.def.dto.guesttool.HciGuesttoolMessageDTO;
import com.ruijie.rcos.rcdc.rco.module.def.api.SystemBusinessMappingAPI;
import com.ruijie.rcos.rcdc.rco.module.def.api.dto.SystemBusinessMappingDTO;
import com.ruijie.rcos.rcdc.rco.module.def.migration.dto.SunnyStatusMessageDTO;
import com.ruijie.rcos.rcdc.rco.module.def.upgrade.dto.SyncUpgradeConsts;
import com.ruijie.rcos.rcdc.rco.module.impl.BusinessKey;
import com.ruijie.rcos.rcdc.rco.module.impl.spi.guesttool.GuestToolCmdId;
import com.ruijie.rcos.sk.base.exception.BusinessException;
import com.ruijie.rcos.sk.base.log.Logger;
import com.ruijie.rcos.sk.base.log.LoggerFactory;
import com.ruijie.rcos.sk.modulekit.api.comm.DispatcherImplemetion;
import org.apache.commons.lang3.math.NumberUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.Assert;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;


import static com.alibaba.fastjson.serializer.SerializerFeature.WriteNullListAsEmpty;
import static com.alibaba.fastjson.serializer.SerializerFeature.WriteNullStringAsEmpty;
import static com.alibaba.fastjson.serializer.SerializerFeature.WriteNullNumberAsZero;



/**
 * Description: sunny状态上报
 * Copyright: Copyright (c) 2021
 * Company: Ruijie Co., Ltd.
 * Create Time: 2021/6/28
 *
 * @author chenl
 */
@DispatcherImplemetion(GuestToolCmdId.RCDC_SUNNY_CMD_ID_STATUS_REPORT_INFO)
public class SunnyStatusReportControlSPIImpl implements CbbGuestToolMessageDispatcherSPI {

    private static final Logger LOGGER = LoggerFactory.getLogger(SunnyStatusReportControlSPIImpl.class);

    @Autowired
    private SystemBusinessMappingAPI systemBusinessMappingAPI;

    @Autowired
    private CbbDeskMgmtAPI cbbDeskMgmtAPI;

    @Autowired
    private CbbImageTemplateMgmtAPI cbbImageTemplateMgmtAPI;

    private static final String CODE_KEY = "code";
    
    /**
     * JSON转字符串特征
     */
    private static final SerializerFeature[] JSON_FEATURES =
            new SerializerFeature[]{WriteNullListAsEmpty, WriteNullStringAsEmpty, WriteNullNumberAsZero};


    @Override
    public CbbGuesttoolMessageDTO receive(CbbGuestToolSPIReceiveRequest request) throws BusinessException {
        Assert.notNull(request, "request can not be null");
        CbbGuesttoolMessageDTO requestDto = request.getDto();
        Assert.notNull(requestDto, "requestDto can not be null");
        UUID deskId = requestDto.getDeskId();
        Assert.notNull(deskId, "deskId can not be null");
        Integer cmdId = requestDto.getCmdId();
        LOGGER.info("sunny安装结果上报:  {}", JSONObject.toJSON(request));

        SunnyStatusMessageDTO sunnyStatusMessageDTO = parseGuestToolMsg(requestDto.getBody(), SunnyStatusMessageDTO.class);
        final UUID vmId = requestDto.getDeskId();

        String imageTemplateId = null;
        CbbImageTemplateDetailDTO cbbImageTemplateDetailDTO = cbbImageTemplateMgmtAPI.findByTempVmId(vmId);
        if (cbbImageTemplateDetailDTO != null) {
            imageTemplateId = cbbImageTemplateDetailDTO.getId().toString();
        } else {
            try {
                CbbDeskDTO cbbDeskDTO = cbbDeskMgmtAPI.getDeskById(vmId);
                imageTemplateId = cbbDeskDTO.getImageTemplateId().toString();
            } catch (BusinessException e) {
                LOGGER.error("sunny状态上报，镜像/桌面不存在[{}]", vmId);
            }
        }
        if (imageTemplateId == null) {
            throw new BusinessException(BusinessKey.SUNNY_REPORT_IMAGE_NOT_FOUND_ERROR, String.valueOf(vmId));
        }

        SystemBusinessMappingDTO systemBusinessMappingDTO = systemBusinessMappingAPI.findSystemBusinessMapping(SyncUpgradeConsts.SYSTEM_TYPE_MTOOL,
                SyncUpgradeConsts.BUSINESS_TYPE_IMAGE_SUNNY_STATUS, imageTemplateId);

        String finalImageTemplateId = imageTemplateId;
        String sunnyContext = JSONObject.toJSONString(sunnyStatusMessageDTO.getContent());
        systemBusinessMappingDTO = Optional.ofNullable(systemBusinessMappingDTO).orElseGet(() -> new SystemBusinessMappingDTO(
                SyncUpgradeConsts.BUSINESS_TYPE_IMAGE_SUNNY_STATUS, finalImageTemplateId,
                finalImageTemplateId, sunnyContext
        ));

        systemBusinessMappingDTO.setContext(sunnyContext);
        systemBusinessMappingAPI.saveSystemBusinessMapping(systemBusinessMappingDTO);

        CbbGuesttoolMessageDTO messageDTO = new CbbGuesttoolMessageDTO();
        messageDTO.setDeskId(deskId);
        messageDTO.setCmdId(cmdId);
        messageDTO.setPortId(GuestToolCmdId.RCDC_SUNNY_PORT_ID_STATUS_REPORT_INFO);
        Map<String, Integer> content = new HashMap<>();
        content.put(CODE_KEY, NumberUtils.INTEGER_ZERO);
        messageDTO.setBody(JSON.toJSONString(content, JSON_FEATURES));
        return messageDTO;
    }

    private <T> T parseGuestToolMsg(String msgBody, Class<T> clz) {
        T bodyMsg;
        try {
            bodyMsg = JSON.parseObject(msgBody, clz);
        } catch (Exception e) {
            throw new IllegalArgumentException("guest tool报文格式错误.data:" + msgBody, e);
        }
        return bodyMsg;
    }


    private HciGuesttoolMessageDTO covertToHciMessageDTO(CbbGuesttoolMessageDTO cbbMessageDTO) {
        HciGuesttoolMessageDTO hciMessageDTO = new HciGuesttoolMessageDTO();
        BeanUtils.copyProperties(cbbMessageDTO, hciMessageDTO);
        return hciMessageDTO;
    }
}
