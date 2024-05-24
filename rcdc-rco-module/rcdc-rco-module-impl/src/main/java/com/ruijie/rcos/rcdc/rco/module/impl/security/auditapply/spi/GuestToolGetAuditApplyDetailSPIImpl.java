package com.ruijie.rcos.rcdc.rco.module.impl.security.auditapply.spi;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.dto.CbbGuesttoolMessageDTO;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.spi.CbbGuestToolMessageDispatcherSPI;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.spi.request.CbbGuestToolSPIReceiveRequest;
import com.ruijie.rcos.rcdc.hciadapter.module.def.dto.guesttool.GuesttoolMessageContent;
import com.ruijie.rcos.rcdc.rco.module.def.api.AuditApplyMgmtAPI;
import com.ruijie.rcos.rcdc.rco.module.def.constants.CommonMessageCode;
import com.ruijie.rcos.rcdc.rco.module.def.security.auditapply.dto.AuditApplyDetailDTO;
import com.ruijie.rcos.rcdc.rco.module.impl.security.auditapply.spi.request.AuditApplyClientNotifyRequest;
import com.ruijie.rcos.rcdc.rco.module.impl.spi.guesttool.GuestToolCmdId;
import com.ruijie.rcos.sk.base.exception.BusinessException;
import com.ruijie.rcos.sk.base.log.Logger;
import com.ruijie.rcos.sk.base.log.LoggerFactory;
import com.ruijie.rcos.sk.modulekit.api.comm.DispatcherImplemetion;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.Assert;

import java.util.UUID;

/**
 * Description: GT查询申请单详情
 * Copyright: Copyright (c) 2022
 * Company: Ruijie Co., Ltd.
 * Create Time: 2022/10/31
 *
 * @author WuShengQiang
 */
@DispatcherImplemetion(GuestToolCmdId.RCDC_GT_CMD_ID_AUDIT_APPLY_DETAIL)
public class GuestToolGetAuditApplyDetailSPIImpl implements CbbGuestToolMessageDispatcherSPI {

    private static final Logger LOGGER = LoggerFactory.getLogger(GuestToolGetAuditApplyDetailSPIImpl.class);

    @Autowired
    private AuditApplyMgmtAPI auditApplyMgmtAPI;

    @Override
    public CbbGuesttoolMessageDTO receive(CbbGuestToolSPIReceiveRequest request) throws BusinessException {
        Assert.notNull(request, "request can not be null");
        CbbGuesttoolMessageDTO requestDto = request.getDto();
        Assert.notNull(requestDto, "requestDto can not be null");
        UUID deskId = requestDto.getDeskId();
        Assert.notNull(deskId, "deskId can not be null");
        LOGGER.info("[CMDID=7012]获取安全审计申请单详情，请求消息：{}", JSONObject.toJSONString(request));
        GuesttoolMessageContent requestBody = JSONObject.parseObject(requestDto.getBody(), GuesttoolMessageContent.class);
        CbbGuesttoolMessageDTO responseBody = new CbbGuesttoolMessageDTO();
        responseBody.setPortId(requestDto.getPortId());
        responseBody.setCmdId(requestDto.getCmdId());
        responseBody.setDeskId(requestDto.getDeskId());
        GuesttoolMessageContent body = new GuesttoolMessageContent();
        if (requestBody == null || requestBody.getContent() == null) {
            body.setCode(CommonMessageCode.FAIL);
            responseBody.setBody(JSON.toJSONString(body));
            LOGGER.error("[CMDID=7012]获取安全审计申请单详情，请求消息内容不正确，请求消息：{}，响应消息：{}", JSONObject.toJSONString(request),
                    JSONObject.toJSONString(responseBody));
            return responseBody;
        }

        AuditApplyClientNotifyRequest requestContent =
                JSONObject.parseObject(JSON.toJSONString(requestBody.getContent()), AuditApplyClientNotifyRequest.class);
        UUID applyId = requestContent.getApplyId();
        if (applyId == null) {
            body.setCode(CommonMessageCode.FAIL);
            responseBody.setBody(JSON.toJSONString(body));
            LOGGER.error("[CMDID=7012]获取安全审计申请单详情，请求消息必须携带applyId，请求消息：{}，响应消息：{}", JSONObject.toJSONString(request),
                    JSONObject.toJSONString(responseBody));
            return responseBody;
        }
        AuditApplyDetailDTO auditApplyDetailDTO = auditApplyMgmtAPI.findAuditApplyDetailById(applyId);
        body.setCode(CommonMessageCode.SUCCESS);
        body.setContent(auditApplyDetailDTO);
        responseBody.setBody(JSON.toJSONString(body));
        LOGGER.info("[CMDID=7012]获取安全审计申请单详情【成功】，请求消息：{}，响应消息：{}", JSONObject.toJSONString(request),
                JSONObject.toJSONString(responseBody));
        return responseBody;
    }
}
