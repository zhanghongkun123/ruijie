package com.ruijie.rcos.rcdc.rco.module.impl.security.auditfile.spi;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.dto.CbbGuesttoolMessageDTO;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.spi.CbbGuestToolMessageDispatcherSPI;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.spi.request.CbbGuestToolSPIReceiveRequest;
import com.ruijie.rcos.rcdc.hciadapter.module.def.dto.guesttool.GuesttoolMessageContent;
import com.ruijie.rcos.rcdc.rco.module.def.api.AuditFileMgmtAPI;
import com.ruijie.rcos.rcdc.rco.module.def.security.auditfile.dto.AuditFileStrategyDTO;
import com.ruijie.rcos.rcdc.rco.module.def.security.auditfile.dto.FtpConfigDTO;
import com.ruijie.rcos.rcdc.rco.module.impl.remoteassist.enums.GuesttoolMessageResultTypeEnum;
import com.ruijie.rcos.rcdc.rco.module.impl.security.auditfile.spi.request.AuditFileStrategyResponse;
import com.ruijie.rcos.rcdc.rco.module.impl.spi.guesttool.GuestToolCmdId;
import com.ruijie.rcos.sk.base.exception.BusinessException;
import com.ruijie.rcos.sk.base.log.Logger;
import com.ruijie.rcos.sk.base.log.LoggerFactory;
import com.ruijie.rcos.sk.modulekit.api.comm.DispatcherImplemetion;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.Assert;

import static com.alibaba.fastjson.serializer.SerializerFeature.WriteNullListAsEmpty;
import static com.alibaba.fastjson.serializer.SerializerFeature.WriteNullNumberAsZero;
import static com.alibaba.fastjson.serializer.SerializerFeature.WriteNullStringAsEmpty;

/**
 * Description: GT请求RCDC文件导出审批策略
 * Copyright: Copyright (c) 2022
 * Company: Ruijie Co., Ltd.
 * Create Time: 2022年10月20日
 *
 * @author lihengjing
 */
@DispatcherImplemetion(GuestToolCmdId.RCDC_GT_CMD_ID_AUDIT_FILE_GLOBAL_STRATEGY)
public class GuestToolAuditFileStrategySPIImpl implements CbbGuestToolMessageDispatcherSPI {

    private static final Logger LOGGER = LoggerFactory.getLogger(GuestToolAuditFileStrategySPIImpl.class);

    /**
     * JSON转字符串特征
     */
    private static final SerializerFeature[] JSON_FEATURES =
            new SerializerFeature[] {WriteNullListAsEmpty, WriteNullStringAsEmpty, WriteNullNumberAsZero};


    @Autowired
    private AuditFileMgmtAPI auditFileMgmtAPI;

    @Override
    public CbbGuesttoolMessageDTO receive(CbbGuestToolSPIReceiveRequest request) throws BusinessException {

        Assert.notNull(request, "request can not be null");
        CbbGuesttoolMessageDTO requestDto = request.getDto();
        Assert.notNull(requestDto, "requestDto can not be null");
        LOGGER.info("[CMDID=7010]获取文件流转审计全局策略请求消息：{}", JSONObject.toJSONString(request));

        CbbGuesttoolMessageDTO responseBody = new CbbGuesttoolMessageDTO();
        responseBody.setPortId(requestDto.getPortId());
        responseBody.setCmdId(requestDto.getCmdId());
        responseBody.setDeskId(requestDto.getDeskId());

        GuesttoolMessageContent requestBody = JSONObject.parseObject(requestDto.getBody(), GuesttoolMessageContent.class);
        AuditFileStrategyResponse auditFileGlobalStrategyResponse;
        if (requestBody != null && requestBody.getContent() != null) {
            auditFileGlobalStrategyResponse =
                    JSONObject.parseObject(JSON.toJSONString(requestBody.getContent()), AuditFileStrategyResponse.class);
        } else {
            auditFileGlobalStrategyResponse = new AuditFileStrategyResponse();
        }
        AuditFileStrategyDTO auditFileGlobalStrategyDTO = auditFileMgmtAPI.obtainAuditFileStrategy(requestDto.getDeskId());
        FtpConfigDTO ftpConfigDTO = auditFileMgmtAPI.obtainAuditFileEncryptFtpInfo();
        auditFileGlobalStrategyResponse.setAuditFileStrategy(auditFileGlobalStrategyDTO);
        auditFileGlobalStrategyResponse.setFtpInfo(ftpConfigDTO);

        GuesttoolMessageContent guesttoolMessageContent = new GuesttoolMessageContent();
        guesttoolMessageContent.setCode(GuesttoolMessageResultTypeEnum.SUCCESS.getCode());
        guesttoolMessageContent.setMessage(GuesttoolMessageResultTypeEnum.SUCCESS.getMessage());
        guesttoolMessageContent.setContent(auditFileGlobalStrategyResponse);
        responseBody.setBody(JSON.toJSONString(guesttoolMessageContent, JSON_FEATURES));
        LOGGER.info("[CMDID=7010]获取文件流转审计全局策略，请求消息：{}，响应消息：{}", JSONObject.toJSONString(request), JSONObject.toJSONString(responseBody));
        return responseBody;
    }



}
