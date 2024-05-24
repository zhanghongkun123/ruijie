package com.ruijie.rcos.rcdc.rco.module.impl.cas.qrcode.spi;

import com.alibaba.fastjson.JSON;
import com.ruijie.rcos.rcdc.rco.module.impl.cas.qrcode.enums.CasQrCodeEnum;
import com.ruijie.rcos.rcdc.rco.module.impl.cas.qrcode.service.CasExternalScanCodeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.Assert;

import com.alibaba.fastjson.JSONObject;
import com.ruijie.rcos.rcdc.codec.adapter.def.dto.CbbDispatcherRequest;
import com.ruijie.rcos.rcdc.codec.adapter.def.spi.CbbDispatcherHandlerSPI;
import com.ruijie.rcos.rcdc.rco.module.def.cas.qrcode.constants.CasScanCodeAuthConstants;
import com.ruijie.rcos.rcdc.rco.module.impl.spi.common.CommonMessageCode;
import com.ruijie.rcos.rcdc.rco.module.impl.spi.common.ShineAction;
import com.ruijie.rcos.rcdc.rco.module.impl.spi.common.ShineMessageHandler;
import com.ruijie.rcos.rcdc.rco.module.impl.spi.dto.ShineLoginDTO;
import com.ruijie.rcos.sk.base.log.Logger;
import com.ruijie.rcos.sk.base.log.LoggerFactory;
import com.ruijie.rcos.sk.modulekit.api.comm.DispatcherImplemetion;
import org.springframework.util.StringUtils;
import org.springframework.web.client.RestClientException;

import java.util.Base64;

/**
 * Description:
 * Copyright: Copyright (c) 2021
 * Company: Ruijie Co., Ltd.
 * Create Time: 2021/6/29
 *
 * @author TD
 */
@DispatcherImplemetion(ShineAction.CAS_QUERY_USER_INFO)
public class CasQueryUserInfoSPIImpl implements CbbDispatcherHandlerSPI {

    private static final Logger LOGGER = LoggerFactory.getLogger(CasQueryUserInfoSPIImpl.class);

    @Autowired
    private ShineMessageHandler shineMessageHandler;

    @Autowired
    private CasExternalScanCodeService externalScanCodeService;

    @Override
    public void dispatch(CbbDispatcherRequest request) {
        Assert.notNull(request, "CbbDispatcherRequest不能为null");
        Assert.hasText(request.getData(), "报文data不能为空");
        String data = request.getData();
        String terminalId = request.getTerminalId();
        LOGGER.info("收到获取用户信息报文:terminalId:{};data:{}", terminalId, data);
        JSONObject dataJson = JSON.parseObject(data);
        // 访问第三方验证接口
        String id = dataJson.getString(CasScanCodeAuthConstants.ID);
        String ticket = dataJson.getString(CasScanCodeAuthConstants.TICKET);
        try {
            String content = externalScanCodeService.ticketValidateService(ticket, decoder(id));
            JSONObject resultJson = JSON.parseObject(content);
            String status = resultJson.getString(CasScanCodeAuthConstants.STATUS);
            if (StringUtils.isEmpty(status) || !status.equalsIgnoreCase(CasScanCodeAuthConstants.SUCCESS)) {
                LOGGER.info("CAS扫码票据：{}，验证失败，二维码ID：{}", ticket, id);
                response(request, CasQrCodeEnum.getCasQrCodeEnum(status, CasQrCodeEnum.TICKET_VERIFY_FAIL).getCode(), null);
                return;
            }
            String userName = resultJson.getString(CasScanCodeAuthConstants.USER_NAME);
            ShineLoginDTO resDto = new ShineLoginDTO();
            resDto.setUserName(userName);
            response(request, CommonMessageCode.SUCCESS, resDto);
        } catch (RestClientException e) {
            LOGGER.error("CAS扫码票验证失败，票据：" + ticket + "，验证失败，二维码ID：" + id, e);
            response(request, CasQrCodeEnum.API_FAILURE.getCode(), null);
        } catch (Exception e) {
            LOGGER.error("CAS扫码票验证失败，票据：" + ticket + "，二维码ID：" + id, e);
            response(request, CasQrCodeEnum.SERVER_EXCEPTION.getCode(), null);
        }
    }

    private void response(CbbDispatcherRequest request, Integer code, Object content) {
        try {
            // 返回消息给shine
            shineMessageHandler.responseContent(request, code, content);
        } catch (Exception e) {
            LOGGER.error("终端{}获取用户信息报文异常，e={}", request.getTerminalId(), e);
        }
    }

    /**
     * 对内容进行BASE64两次解码操作
     *
     * @param content
     * @return
     */
    private String decoder(String content) {
        Base64.Decoder decoder = Base64.getDecoder();
        return new String(decoder.decode(decoder.decode(content)));
    }
}
