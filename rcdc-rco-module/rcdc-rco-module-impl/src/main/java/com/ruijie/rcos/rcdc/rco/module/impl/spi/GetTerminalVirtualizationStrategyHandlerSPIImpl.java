package com.ruijie.rcos.rcdc.rco.module.impl.spi;


import com.ruijie.rcos.rcdc.clouddesktop.module.def.api.CbbImageTemplateVmMgmtAPI;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.dto.CbbVirtualizationStrategyDetailDTO;
import com.ruijie.rcos.rcdc.rco.module.impl.spi.common.CommonMessageCode;
import com.ruijie.rcos.rcdc.rco.module.impl.spi.dto.ShineRequestTerminalImageVmModeDTO;
import com.ruijie.rcos.rcdc.rco.module.impl.spi.dto.VirtualizationStrategyDetailView;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.Assert;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.api.CbbImageTemplateMgmtAPI;
import com.ruijie.rcos.rcdc.codec.adapter.def.api.CbbTranspondMessageHandlerAPI;
import com.ruijie.rcos.rcdc.codec.adapter.def.dto.CbbDispatcherRequest;
import com.ruijie.rcos.rcdc.codec.adapter.def.dto.CbbResponseShineMessage;
import com.ruijie.rcos.rcdc.codec.adapter.def.spi.CbbDispatcherHandlerSPI;
import com.ruijie.rcos.rcdc.rco.module.impl.spi.image.constant.ImageDispatcherConstants;
import com.ruijie.rcos.rcdc.rco.module.impl.util.ShineMessageUtil;
import com.ruijie.rcos.sk.base.exception.BusinessException;
import com.ruijie.rcos.sk.base.log.Logger;
import com.ruijie.rcos.sk.base.log.LoggerFactory;
import com.ruijie.rcos.sk.modulekit.api.comm.DispatcherImplemetion;

/**
 * Description: 获取终端镜像虚机运行策略
 * Copyright: Copyright (c) 2023
 * Company: Ruijie Co., Ltd.
 * Create Time: 2023.09.07
 *
 * @author lifeng
 */
@DispatcherImplemetion(ImageDispatcherConstants.GET_TERMINAL_VIRTUALIZATION_STRATEGY)
public class GetTerminalVirtualizationStrategyHandlerSPIImpl implements CbbDispatcherHandlerSPI {

    private static final Logger LOGGER = LoggerFactory.getLogger(GetTerminalVirtualizationStrategyHandlerSPIImpl.class);

    @Autowired
    private CbbTranspondMessageHandlerAPI messageHandlerAPI;

    @Autowired
    private CbbImageTemplateMgmtAPI cbbImageTemplateMgmtAPI;

    @Autowired
    private CbbImageTemplateVmMgmtAPI cbbImageTemplateVmMgmtAPI;

    private final static String VIRTUALIZATION_STRATEGY_DETAIL_KEY = "virtualizationStrategyDetail";

    @Override
    public void dispatch(CbbDispatcherRequest request) {
        Assert.notNull(request, "request cannot be null!");
        Assert.hasText(request.getData(), "data in request cannot be blank!");

        LOGGER.info("获取终端镜像虚机运行策略，terminalId[{}],请求信息为{}", request.getTerminalId(), JSON.toJSONString(request));
        Integer errorCode = CommonMessageCode.FAIL_CODE;
        CbbResponseShineMessage<?> cbbResponseShineMessage;

        try {
            JSONObject dataJson = JSONObject.parseObject(request.getData());
            ShineRequestTerminalImageVmModeDTO terminalImageVmModeDTO = dataJson.toJavaObject(ShineRequestTerminalImageVmModeDTO.class);

            // 根据终端ID跟镜像ID获取虚机运行策略
            CbbVirtualizationStrategyDetailDTO cbbVirtualizationStrategyDetailDTO =
                    cbbImageTemplateVmMgmtAPI.getTerminalImageVirtualStrategy(terminalImageVmModeDTO.getImageId(), request.getTerminalId());

            VirtualizationStrategyDetailView virtualizationStrategyDetailView = new VirtualizationStrategyDetailView();
            virtualizationStrategyDetailView.convertByCbbVirtualizationStrategyDetail(cbbVirtualizationStrategyDetailDTO);
            cbbResponseShineMessage = ShineMessageUtil.buildResponseMessage(request, virtualizationStrategyDetailView);
        } catch (BusinessException ex) {
            LOGGER.error("获取终端[{}]虚拟策略失败", request.getTerminalId(), ex);
            cbbResponseShineMessage = ShineMessageUtil.buildErrorResponseMessage(request, errorCode);
        } catch (Exception ex) {
            LOGGER.error("获取终端[{}]虚拟策略失败未知异常", request.getTerminalId(), ex);
            cbbResponseShineMessage = ShineMessageUtil.buildErrorResponseMessage(request, errorCode);
        }
        messageHandlerAPI.response(cbbResponseShineMessage);
    }

}
