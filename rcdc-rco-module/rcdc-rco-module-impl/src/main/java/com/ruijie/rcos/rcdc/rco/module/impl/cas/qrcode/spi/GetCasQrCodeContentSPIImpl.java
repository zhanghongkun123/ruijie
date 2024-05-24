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
import com.ruijie.rcos.rcdc.rco.module.impl.userlogin.service.LoginBusinessService;
import com.ruijie.rcos.rcdc.rco.module.impl.userlogin.service.LoginBusinessServiceFactory;
import com.ruijie.rcos.sk.base.log.Logger;
import com.ruijie.rcos.sk.base.log.LoggerFactory;
import com.ruijie.rcos.sk.modulekit.api.comm.DispatcherImplemetion;
import org.apache.commons.lang3.BooleanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.Assert;
import org.springframework.web.client.RestClientException;

/**
 * Description:
 * Copyright: Copyright (c) 2021
 * Company: Ruijie Co., Ltd.
 * Create Time: 2021/6/28
 *
 * @author TD
 */
@DispatcherImplemetion(ShineAction.CAS_QUERY_QR_CONTENT)
public class GetCasQrCodeContentSPIImpl implements CbbDispatcherHandlerSPI {

    private static final Logger LOGGER = LoggerFactory.getLogger(GetCasQrCodeContentSPIImpl.class);

    private static final String IMG_KEY = "img";

    private static final String REGEX = "\\s";

    private static final String EMPTY = "";

    @Autowired
    private ShineMessageHandler shineMessageHandler;

    @Autowired
    private CasExternalScanCodeService externalScanCodeService;

    @Autowired
    protected LoginBusinessServiceFactory loginBusinessServiceFactory;

    private LoginBusinessService loginBusinessService;

    @Override
    public void dispatch(CbbDispatcherRequest request) {
        Assert.notNull(request, "request is null");
        try {
            CasScanOutDTO casScanOutDTO = externalScanCodeService.getQrCodeService();
            //获取二维码失败
            if (casScanOutDTO == null || !BooleanUtils.toBoolean(casScanOutDTO.getSuccess())) {
                LOGGER.error("获取CAS二维码信息失败,请查看获取二维码接口配置信息是否正确");
                response(request, CasQrCodeEnum.QR_CODE_FAILURE.getCode(), null);
                return;
            }
            JSONObject codeContent = JSON.parseObject(casScanOutDTO.getData());
            if (codeContent.containsKey(IMG_KEY)) {
                codeContent.put(IMG_KEY, codeContent.getString(IMG_KEY).replaceAll(REGEX, EMPTY));
            }
            response(request, CasQrCodeEnum.SUCCESS.getCode(), codeContent);
            saveLoginInfo(request.getTerminalId());
        } catch (RestClientException e) {
            LOGGER.error("终端获取CAS二维码信息失败", e);
            response(request, CasQrCodeEnum.API_FAILURE.getCode(), null);
        } catch (Exception e) {
            LOGGER.error("终端获取CAS二维码信息失败", e);
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

    private void saveLoginInfo(String terminalId) {
        try {
            loginBusinessService = loginBusinessServiceFactory.getLoginBusinessService(ShineAction.CAS_ACTION_LOGIN);
            loginBusinessService.saveUserLoginInfo(terminalId, null);
        } catch (Exception e) {
            LOGGER.error("获取CAS二维码时，保存终端[{}]登录信息失败", terminalId, e);
        }
    }
}
