package com.ruijie.rcos.rcdc.rco.module.impl.spi;

import com.alibaba.fastjson.JSONObject;
import com.ruijie.rcos.rcdc.codec.adapter.def.api.CbbTranspondMessageHandlerAPI;
import com.ruijie.rcos.rcdc.codec.adapter.def.dto.CbbDispatcherRequest;
import com.ruijie.rcos.rcdc.codec.adapter.def.dto.CbbResponseShineMessage;
import com.ruijie.rcos.rcdc.codec.adapter.def.spi.CbbDispatcherHandlerSPI;
import com.ruijie.rcos.rcdc.rco.module.def.api.CertificationStrategyParameterAPI;
import com.ruijie.rcos.rcdc.rco.module.def.certificationstrategy.dto.PwdStrategyDTO;
import com.ruijie.rcos.rcdc.rco.module.def.constants.ConfigWizardForIDVCode;
import com.ruijie.rcos.rcdc.rco.module.impl.spi.common.ShineAction;
import com.ruijie.rcos.sk.base.log.Logger;
import com.ruijie.rcos.sk.base.log.LoggerFactory;
import com.ruijie.rcos.sk.modulekit.api.comm.DispatcherImplemetion;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.Assert;

/**
 * Description: 获取认证策略配置
 * Copyright: Copyright (c) 2018
 * Company: Ruijie Co., Ltd.
 * Create Time: 2019/1/16
 *
 * @author zhang.zhiwen
 */
@DispatcherImplemetion(ShineAction.GET_CERTIFICATION_STRATEGY)
public class GetCertificationStrategySPIImpl implements CbbDispatcherHandlerSPI {

    private static final Logger LOGGER = LoggerFactory.getLogger(GetCertificationStrategySPIImpl.class);

    @Autowired
    private CertificationStrategyParameterAPI certificationStrategyParameterAPI;

    @Autowired
    private CbbTranspondMessageHandlerAPI messageHandlerAPI;

    @Override
    public void dispatch(CbbDispatcherRequest request) {
        Assert.notNull(request, "CbbDispatcherRequest can not null");
        try {
            PwdStrategyDTO pwdStrategyDTO = certificationStrategyParameterAPI.getPwdStrategy();
            // 兼容旧的状态码
            if (pwdStrategyDTO.getPwdLevel() != null) {
                int levelNum = Integer.parseInt(pwdStrategyDTO.getPwdLevel());
                levelNum = levelNum > 0 ? levelNum - 1 : levelNum;
                pwdStrategyDTO.setPwdLevel(String.valueOf(levelNum));
            }
            responseSuccessMessage(request, pwdStrategyDTO);
        } catch (Exception e) {
            LOGGER.error("获取镜像模板异常{}", e);
            responseErrorMessage(request, ConfigWizardForIDVCode.CODE_ERR_OTHER);
        }
    }

    protected void responseSuccessMessage(CbbDispatcherRequest request, PwdStrategyDTO pwdStrategyDTO) {
        Assert.notNull(request, "CbbDispatcherRequest can not null");
        LOGGER.info("获取认证策略配置成功报文：terminalId={}, object={}", request.getTerminalId(),
                JSONObject.toJSONString(pwdStrategyDTO));
        CbbResponseShineMessage<PwdStrategyDTO> shineMessage = new CbbResponseShineMessage<>();
        shineMessage.setCode(ConfigWizardForIDVCode.SUCCESS);
        shineMessage.setRequestId(request.getRequestId());
        shineMessage.setTerminalId(request.getTerminalId());
        shineMessage.setAction(request.getDispatcherKey());
        shineMessage.setContent(pwdStrategyDTO);
        messageHandlerAPI.response(shineMessage);
    }

    protected void responseErrorMessage(CbbDispatcherRequest request, int responseCode) {
        Assert.notNull(request, "CbbDispatcherRequest can not null");
        Assert.notNull(responseCode, "resultCode can not null");
        LOGGER.info("获取认证策略配置失败报文：terminalId={}, responseCode={}", request.getTerminalId(), responseCode);
        CbbResponseShineMessage<Object> shineMessage = new CbbResponseShineMessage();
        shineMessage.setCode(responseCode);
        shineMessage.setRequestId(request.getRequestId());
        shineMessage.setTerminalId(request.getTerminalId());
        shineMessage.setAction(request.getDispatcherKey());
        shineMessage.setContent(new Object());
        messageHandlerAPI.response(shineMessage);
    }
}
