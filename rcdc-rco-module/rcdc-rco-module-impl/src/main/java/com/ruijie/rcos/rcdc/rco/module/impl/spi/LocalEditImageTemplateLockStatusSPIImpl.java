package com.ruijie.rcos.rcdc.rco.module.impl.spi;

import com.alibaba.fastjson.JSONObject;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.api.CbbImageTemplateMgmtAPI;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.dto.image.CbbGetImageTemplateInfoDTO;
import com.ruijie.rcos.rcdc.codec.adapter.def.api.CbbTranspondMessageHandlerAPI;
import com.ruijie.rcos.rcdc.codec.adapter.def.dto.CbbDispatcherRequest;
import com.ruijie.rcos.rcdc.codec.adapter.def.dto.CbbResponseShineMessage;
import com.ruijie.rcos.rcdc.codec.adapter.def.spi.CbbDispatcherHandlerSPI;
import com.ruijie.rcos.rcdc.rco.module.impl.spi.common.CommonMessageCode;
import com.ruijie.rcos.rcdc.rco.module.impl.spi.constant.DispatcherConstants;
import com.ruijie.rcos.sk.base.exception.BusinessException;
import com.ruijie.rcos.sk.base.log.Logger;
import com.ruijie.rcos.sk.base.log.LoggerFactory;
import com.ruijie.rcos.sk.modulekit.api.comm.DispatcherImplemetion;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.Assert;

import java.util.UUID;

/**
 * Description: LocalEditImageTemplateLockStatusSPIImpl
 * Copyright: Copyright (c)
 * Company: Ruijie Co., Ltd.
 * Create Time: 2020/10/15 15:45
 *
 * @author coderLee23
 */
@DispatcherImplemetion(DispatcherConstants.ACTION_CHECK_LOCAL_EDIT_TEMPLATE_LOCK_STATUS)
public class LocalEditImageTemplateLockStatusSPIImpl implements CbbDispatcherHandlerSPI {

    private static final Logger LOGGER = LoggerFactory.getLogger(LocalEditImageTemplateLockStatusSPIImpl.class);

    private static final String IMAGE_ID = "imageId";

    private static final String IS_LOCK_STATUS = "is_Lock_status";

    @Autowired
    private CbbImageTemplateMgmtAPI cbbImageTemplateMgmtAPI;

    @Autowired
    private CbbTranspondMessageHandlerAPI cbbTranspondMessageHandlerAPI;



    @Override
    public void dispatch(CbbDispatcherRequest cbbDispatcherRequest) {
        Assert.notNull(cbbDispatcherRequest, "CbbDispatcherRequest不能为null");
        String requestTerminalId = cbbDispatcherRequest.getTerminalId();
        LOGGER.info("IDV终端请求查询是否锁定镜像模板，terminalId={}", requestTerminalId);
        JSONObject dataJson = JSONObject.parseObject(cbbDispatcherRequest.getData());
        UUID imageId = dataJson.getObject(IMAGE_ID, UUID.class);

        try {
            CbbGetImageTemplateInfoDTO cbbGetImageTemplateInfoDTO = cbbImageTemplateMgmtAPI.getImageTemplateInfo(imageId);
            String lockTerminalId = cbbGetImageTemplateInfoDTO.getTerminalId();

            JSONObject jsonObject = new JSONObject();
            jsonObject.put(IS_LOCK_STATUS, requestTerminalId.equals(lockTerminalId));

            CbbResponseShineMessage<JSONObject> cbbResponseShineMessage = buildShineMessage(cbbDispatcherRequest, CommonMessageCode.SUCCESS);
            cbbResponseShineMessage.setContent(jsonObject);
            cbbTranspondMessageHandlerAPI.response(cbbResponseShineMessage);
        } catch (BusinessException e) {
            LOGGER.error("IDV终端请求查询是否锁定镜像模板异常！", e);
            CbbResponseShineMessage<JSONObject> cbbResponseShineMessage = buildShineMessage(cbbDispatcherRequest, CommonMessageCode.CODE_ERR_OTHER);
            cbbTranspondMessageHandlerAPI.response(cbbResponseShineMessage);
        }

    }

    private CbbResponseShineMessage<JSONObject> buildShineMessage(CbbDispatcherRequest request, int code) {
        CbbResponseShineMessage<JSONObject> responseMessage = new CbbResponseShineMessage<>();
        responseMessage.setAction(request.getDispatcherKey());
        responseMessage.setRequestId(request.getRequestId());
        responseMessage.setTerminalId(request.getTerminalId());
        responseMessage.setCode(code);
        return responseMessage;
    }
}
