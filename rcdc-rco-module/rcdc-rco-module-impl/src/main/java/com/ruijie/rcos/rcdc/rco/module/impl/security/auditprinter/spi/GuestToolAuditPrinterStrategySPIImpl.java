package com.ruijie.rcos.rcdc.rco.module.impl.security.auditprinter.spi;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.dto.CbbGuesttoolMessageDTO;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.spi.CbbGuestToolMessageDispatcherSPI;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.spi.request.CbbGuestToolSPIReceiveRequest;
import com.ruijie.rcos.rcdc.hciadapter.module.def.dto.guesttool.GuesttoolMessageContent;
import com.ruijie.rcos.rcdc.rco.module.def.api.AuditPrinterMgmtAPI;
import com.ruijie.rcos.rcdc.rco.module.def.api.UserDesktopMgmtAPI;
import com.ruijie.rcos.rcdc.rco.module.def.api.dto.CloudDesktopDTO;
import com.ruijie.rcos.rcdc.rco.module.def.api.dto.CloudDesktopDetailDTO;
import com.ruijie.rcos.rcdc.rco.module.def.security.auditfile.dto.FtpConfigDTO;
import com.ruijie.rcos.rcdc.rco.module.def.security.auditprinter.dto.AuditPrinterStrategyDTO;
import com.ruijie.rcos.rcdc.rco.module.impl.dto.WatermarkDisplayContentDTO;
import com.ruijie.rcos.rcdc.rco.module.impl.dto.WatermarkFieldMappingValueDTO;
import com.ruijie.rcos.rcdc.rco.module.impl.remoteassist.enums.GuesttoolMessageResultTypeEnum;
import com.ruijie.rcos.rcdc.rco.module.impl.security.auditprinter.spi.response.AuditPrinterStrategyResponse;
import com.ruijie.rcos.rcdc.rco.module.impl.security.auditprinter.utils.AuditPrinterUtils;
import com.ruijie.rcos.rcdc.rco.module.impl.service.WatermarkMessageParser;
import com.ruijie.rcos.rcdc.rco.module.impl.spi.guesttool.GuestToolCmdId;
import com.ruijie.rcos.sk.base.exception.BusinessException;
import com.ruijie.rcos.sk.base.log.Logger;
import com.ruijie.rcos.sk.base.log.LoggerFactory;
import com.ruijie.rcos.sk.base.util.StringUtils;
import com.ruijie.rcos.sk.modulekit.api.comm.DispatcherImplemetion;
import org.apache.commons.lang3.BooleanUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.Assert;

import java.util.UUID;

import static com.alibaba.fastjson.serializer.SerializerFeature.WriteNullListAsEmpty;
import static com.alibaba.fastjson.serializer.SerializerFeature.WriteNullNumberAsZero;
import static com.alibaba.fastjson.serializer.SerializerFeature.WriteNullStringAsEmpty;

/**
 * Description: GT请求RCDC安全打印策略
 * Copyright: Copyright (c) 2022
 * Company: Ruijie Co., Ltd.
 * Create Time: 2022年10月20日
 *
 * @author lihengjing
 */
@DispatcherImplemetion(GuestToolCmdId.RCDC_GT_CMD_ID_AUDIT_PRINTER_GLOBAL_STRATEGY)
public class GuestToolAuditPrinterStrategySPIImpl implements CbbGuestToolMessageDispatcherSPI {

    private static final Logger LOGGER = LoggerFactory.getLogger(GuestToolAuditPrinterStrategySPIImpl.class);

    /**
     * JSON转字符串特征
     */
    private static final SerializerFeature[] JSON_FEATURES =
            new SerializerFeature[] {WriteNullListAsEmpty, WriteNullStringAsEmpty, WriteNullNumberAsZero};


    @Autowired
    private AuditPrinterMgmtAPI auditPrinterMgmtAPI;

    @Autowired
    private WatermarkMessageParser<WatermarkDisplayContentDTO> watermarkMessageParser;

    @Autowired
    private UserDesktopMgmtAPI userDesktopMgmtAPI;

    @Override
    public CbbGuesttoolMessageDTO receive(CbbGuestToolSPIReceiveRequest request) throws BusinessException {

        Assert.notNull(request, "request can not be null");
        CbbGuesttoolMessageDTO requestDto = request.getDto();
        Assert.notNull(requestDto, "requestDto can not be null");
        UUID deskId = requestDto.getDeskId();
        Assert.notNull(deskId, "deskId can not be null");
        LOGGER.info("[CMDID=7015]获取安全打印全局策略请求消息：{}", JSONObject.toJSONString(request));

        CbbGuesttoolMessageDTO responseBody = new CbbGuesttoolMessageDTO();
        responseBody.setPortId(requestDto.getPortId());
        responseBody.setCmdId(requestDto.getCmdId());
        responseBody.setDeskId(requestDto.getDeskId());

        GuesttoolMessageContent requestBody = JSONObject.parseObject(requestDto.getBody(), GuesttoolMessageContent.class);
        AuditPrinterStrategyResponse auditPrinterGlobalStrategyResponse;
        if (requestBody != null && requestBody.getContent() != null) {
            auditPrinterGlobalStrategyResponse =
                    JSONObject.parseObject(JSON.toJSONString(requestBody.getContent()), AuditPrinterStrategyResponse.class);
        } else {
            auditPrinterGlobalStrategyResponse = new AuditPrinterStrategyResponse();
        }
        AuditPrinterStrategyDTO auditPrinterStrategyDTO = auditPrinterMgmtAPI.obtainAuditPrinterStrategy(deskId);
        // 设置水印信息
        if (BooleanUtils.isTrue(auditPrinterStrategyDTO.getEnableWatermark())
                && StringUtils.hasText(auditPrinterStrategyDTO.getDisplayContent())) {
            String displayContent = auditPrinterStrategyDTO.getDisplayContent();
            CloudDesktopDetailDTO desktopDetail = userDesktopMgmtAPI.getDesktopDetailById(deskId);
            CloudDesktopDTO cloudDesktopDTO = new CloudDesktopDTO();
            BeanUtils.copyProperties(desktopDetail, cloudDesktopDTO);
            WatermarkFieldMappingValueDTO mappingValue = AuditPrinterUtils.getMappingValueDTO(cloudDesktopDTO, displayContent);
            WatermarkDisplayContentDTO displayContentDTO = watermarkMessageParser.parse(mappingValue, displayContent);
            auditPrinterStrategyDTO.setDisplayContent(JSON.toJSONString(displayContentDTO));
        } else {
            auditPrinterStrategyDTO.setDisplayContent(StringUtils.EMPTY);
        }
        FtpConfigDTO ftpConfigDTO = auditPrinterMgmtAPI.obtainAuditPrinterEncryptFtpInfo();
        auditPrinterGlobalStrategyResponse.setAuditPrinterStrategy(auditPrinterStrategyDTO);
        auditPrinterGlobalStrategyResponse.setFtpInfo(ftpConfigDTO);

        GuesttoolMessageContent guesttoolMessageContent = new GuesttoolMessageContent();
        guesttoolMessageContent.setCode(GuesttoolMessageResultTypeEnum.SUCCESS.getCode());
        guesttoolMessageContent.setMessage(GuesttoolMessageResultTypeEnum.SUCCESS.getMessage());
        guesttoolMessageContent.setContent(auditPrinterGlobalStrategyResponse);
        responseBody.setBody(JSON.toJSONString(guesttoolMessageContent, JSON_FEATURES));
        // 通道已经有相关日志输出
        LOGGER.info("[CMDID=7015]获取安全打印全局策略，请求消息：{}，响应消息：{}", JSONObject.toJSONString(request), JSONObject.toJSONString(responseBody));
        return responseBody;
    }



}
