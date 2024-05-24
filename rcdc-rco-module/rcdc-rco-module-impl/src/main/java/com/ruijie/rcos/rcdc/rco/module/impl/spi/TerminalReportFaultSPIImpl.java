package com.ruijie.rcos.rcdc.rco.module.impl.spi;

import com.ruijie.rcos.rcdc.codec.adapter.def.dto.CbbDispatcherRequest;
import com.ruijie.rcos.rcdc.codec.adapter.def.spi.CbbDispatcherHandlerSPI;
import com.ruijie.rcos.rcdc.rco.module.def.api.DeskFaultInfoAPI;
import com.ruijie.rcos.rcdc.rco.module.def.api.dto.CbbDeskFaultInfoDTO;
import com.ruijie.rcos.rcdc.rco.module.def.api.response.CbbDeskFaultInfoResponse;
import com.ruijie.rcos.rcdc.rco.module.impl.BusinessKey;
import com.ruijie.rcos.rcdc.rco.module.impl.Constants;
import com.ruijie.rcos.rcdc.rco.module.impl.dao.UserDesktopDAO;
import com.ruijie.rcos.rcdc.rco.module.impl.entity.UserDesktopEntity;
import com.ruijie.rcos.rcdc.rco.module.impl.spi.common.CommonMessageCode;
import com.ruijie.rcos.rcdc.rco.module.impl.spi.common.ShineAction;
import com.ruijie.rcos.rcdc.rco.module.impl.spi.common.ShineMessageHandler;
import com.ruijie.rcos.rcdc.rco.module.impl.spi.response.TerminalReportFaultResponse;
import com.ruijie.rcos.sk.base.i18n.LocaleI18nResolver;
import com.ruijie.rcos.sk.modulekit.api.comm.DispatcherImplemetion;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.Assert;
import org.springframework.util.CollectionUtils;

import java.util.Date;
import java.util.List;


/**
 * Description: 接收IDV\TCI shine上报故障接口
 * Copyright: Copyright (c) 2023
 * Company: Ruijie Co., Ltd.
 * Create Time: 2023年01月14日
 *
 * @author xwx
 */
@DispatcherImplemetion(ShineAction.TERMINAL_REPORT_FAULT)
public class TerminalReportFaultSPIImpl implements CbbDispatcherHandlerSPI {

    private static final Logger LOGGER = LoggerFactory.getLogger(TerminalReportFaultSPIImpl.class);

    @Autowired
    private UserDesktopDAO userDesktopDAO;

    @Autowired
    private DeskFaultInfoAPI deskFaultInfoAPI;

    @Autowired
    private ShineMessageHandler shineMessageHandler;

    @Override
    public void dispatch(CbbDispatcherRequest request) {
        Assert.notNull(request, "request cant be null");

        LOGGER.info("收到shine的报障请求 request {}", request);
        // 根据终端ID获取云桌面
        List<UserDesktopEntity> desktopByTerminalIdList = userDesktopDAO.findByTerminalId(request.getTerminalId());
        if (CollectionUtils.isEmpty(desktopByTerminalIdList)) {
            LOGGER.warn("终端没有对应的云桌面, terminalId [{}]", request.getTerminalId());
            response(request, CommonMessageCode.DESKTOP_NOT_EXIST,
                    new TerminalReportFaultResponse(LocaleI18nResolver.resolve(BusinessKey.RCDC_RCO_TERMINAL_DESKTOP_NOT_EXIST)));
            return;
        }
        UserDesktopEntity userDesktopEntity = desktopByTerminalIdList.get(0);
        // 增加或者修改报障信息
        CbbDeskFaultInfoResponse response = deskFaultInfoAPI.findFaultInfoByDeskId(userDesktopEntity.getCbbDesktopId());
        CbbDeskFaultInfoDTO cbbDeskFaultInfoDTO;
        if (response == null || response.getCbbDeskFaultInfoDTO() == null) {
            cbbDeskFaultInfoDTO = new CbbDeskFaultInfoDTO();
            cbbDeskFaultInfoDTO.setCreateTime(new Date());
        } else {
            cbbDeskFaultInfoDTO = response.getCbbDeskFaultInfoDTO();
        }
        cbbDeskFaultInfoDTO.setFaultDescription(LocaleI18nResolver.resolve(Constants.TERMINAL_REPORT_FAULT_DES));
        cbbDeskFaultInfoDTO.setDeskId(userDesktopEntity.getCbbDesktopId());
        cbbDeskFaultInfoDTO.setFaultState(true);
        cbbDeskFaultInfoDTO.setFaultTime(new Date());
        cbbDeskFaultInfoDTO.setMac(request.getTerminalId());
        deskFaultInfoAPI.save(cbbDeskFaultInfoDTO);
        response(request, CommonMessageCode.SUCCESS, new TerminalReportFaultResponse());
    }

    private void response(CbbDispatcherRequest request, Integer code, Object content) {
        try {
            LOGGER.info("返回终端[{}]的报障结果code：[{}], 内容[{}]", request.getTerminalId(), code, content);
            shineMessageHandler.responseContent(request, code, content);
        } catch (Exception e) {
            LOGGER.error("返回给终端{}报障消息失败", request.getTerminalId(), e);
        }
    }
}
