package com.ruijie.rcos.rcdc.rco.module.impl.cas.qrcode.spi;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.ruijie.rcos.rcdc.codec.adapter.def.dto.CbbDispatcherRequest;
import com.ruijie.rcos.rcdc.codec.adapter.def.spi.CbbDispatcherHandlerSPI;
import com.ruijie.rcos.rcdc.rco.module.def.cas.qrcode.dto.CasScanOutDTO;
import com.ruijie.rcos.rcdc.rco.module.impl.cas.qrcode.enums.CasQrCodeEnum;
import com.ruijie.rcos.rcdc.rco.module.impl.cas.qrcode.service.CasExternalScanCodeService;
import com.ruijie.rcos.rcdc.rco.module.impl.spi.common.ShineAction;
import com.ruijie.rcos.rcdc.rco.module.impl.spi.common.ShineMessageHandler;
import com.ruijie.rcos.sk.base.log.Logger;
import com.ruijie.rcos.sk.base.log.LoggerFactory;
import com.ruijie.rcos.sk.modulekit.api.comm.DispatcherImplemetion;
import org.apache.commons.lang3.BooleanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.Assert;
import org.springframework.web.client.RestClientException;

/**
 * Description: 获取扫码结果
 * Copyright: Copyright (c) 2021
 * Company: Ruijie Co., Ltd.
 * Create Time: 2021/6/28
 *
 * @author TD
 */
@DispatcherImplemetion(ShineAction.CAS_QUERY_QR_RESULT)
public class GetCasQrCodeResultSPIImpl implements CbbDispatcherHandlerSPI {

    /**
     * 二维码ID
     */
    private static final String QR_ID = "id";

    private static final Logger LOGGER = LoggerFactory.getLogger(GetCasQrCodeResultSPIImpl.class);

    @Autowired
    private ShineMessageHandler shineMessageHandler;

    @Autowired
    private CasExternalScanCodeService externalScanCodeService;

    @Override
    public void dispatch(CbbDispatcherRequest request) {
        Assert.notNull(request, "request is null");
        Assert.hasText(request.getData(), "报文data不能为空");

        LOGGER.info("接收到终端获取扫码结果请求，请求参数为:[" + request.getData() + "]");
        JSONObject dataJson = JSON.parseObject(request.getData());
        String qrCodeId = dataJson.getString(QR_ID);

        try {
            CasScanOutDTO scanOutDTO = externalScanCodeService.monitorScanResultService(qrCodeId);
            if (scanOutDTO == null || !BooleanUtils.toBoolean(scanOutDTO.getSuccess())) {
                LOGGER.info("获取二维码结果失败，二维码ID:{}，未被扫码", qrCodeId);
                response(request, CasQrCodeEnum.QR_CODE_NOT_SCAN_CODE.getCode(), null);
                return;
            }
            response(request, CasQrCodeEnum.SUCCESS.getCode(), JSON.parseObject(scanOutDTO.getData()));
        } catch (RestClientException e) {
            LOGGER.error("监听二维码：" + qrCodeId + "，扫码结果出现异常", e);
            response(request, CasQrCodeEnum.API_FAILURE.getCode(), null);
        } catch (Exception e) {
            LOGGER.error("监听二维码：" + qrCodeId + "，扫码结果出现未知异常", e);
            response(request, CasQrCodeEnum.SERVER_EXCEPTION.getCode(), null);
        }
    }

    private void response(CbbDispatcherRequest request, Integer code, Object data) {
        try {
            // 返回消息给shine
            shineMessageHandler.responseContent(request, code, data);
        } catch (Exception e) {
            LOGGER.error("终端{}获取CAS二维码信息失败，e={}", request.getTerminalId(), e);
        }
    }
}
