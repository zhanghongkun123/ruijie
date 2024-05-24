package com.ruijie.rcos.rcdc.rco.module.impl.spi;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.Assert;
import org.springframework.util.CollectionUtils;

import com.ruijie.rcos.rcdc.codec.adapter.def.dto.CbbDispatcherRequest;
import com.ruijie.rcos.rcdc.codec.adapter.def.spi.CbbDispatcherHandlerSPI;
import com.ruijie.rcos.rcdc.rco.module.def.api.DeskFaultInfoAPI;
import com.ruijie.rcos.rcdc.rco.module.def.api.dto.CbbDeskFaultInfoDTO;
import com.ruijie.rcos.rcdc.rco.module.def.api.response.CbbDeskFaultInfoResponse;
import com.ruijie.rcos.rcdc.rco.module.impl.dao.UserDesktopDAO;
import com.ruijie.rcos.rcdc.rco.module.impl.entity.UserDesktopEntity;
import com.ruijie.rcos.rcdc.rco.module.impl.enums.ProcessStageEnum;
import com.ruijie.rcos.rcdc.rco.module.impl.spi.common.CommonMessageCode;
import com.ruijie.rcos.rcdc.rco.module.impl.spi.common.ShineAction;
import com.ruijie.rcos.rcdc.rco.module.impl.spi.common.ShineMessageHandler;
import com.ruijie.rcos.rcdc.rco.module.impl.spi.response.RequestReportFaultResultResponse;
import com.ruijie.rcos.rcdc.rco.module.impl.util.DateUtil;
import com.ruijie.rcos.sk.base.log.Logger;
import com.ruijie.rcos.sk.base.log.LoggerFactory;
import com.ruijie.rcos.sk.modulekit.api.comm.DispatcherImplemetion;

/**
 * Description: 终端请求报障结果
 * Copyright: Copyright (c) 2023
 * Company: Ruijie Co., Ltd.
 * Create Time: 2023年02月02日
 *
 * @author xwx
 */
@DispatcherImplemetion(ShineAction.REQUEST_REPORT_FAULT_RESULT)
public class TerminalRequestReportFaultResultSPIImpl implements CbbDispatcherHandlerSPI {

    private static final Logger LOGGER = LoggerFactory.getLogger(TerminalRequestReportFaultResultSPIImpl.class);

    @Autowired
    private DeskFaultInfoAPI deskFaultInfoAPI;

    @Autowired
    private ShineMessageHandler shineMessageHandler;

    @Autowired
    private UserDesktopDAO userDesktopDAO;

    @Override
    public void dispatch(CbbDispatcherRequest request) {
        Assert.notNull(request, "request cant be null");
        LOGGER.info("收到shine的获取报障结果请求 request {}", request);
        // 根据终端ID获取云桌面
        List<UserDesktopEntity> desktopByTerminalIdList = userDesktopDAO.findByTerminalId(request.getTerminalId());
        if (CollectionUtils.isEmpty(desktopByTerminalIdList)) {
            LOGGER.warn("未找到终端桌面, terminalId {}", request.getTerminalId());
            response(request, CommonMessageCode.DESKTOP_NOT_EXIST, new RequestReportFaultResultResponse(ProcessStageEnum.COMPLETED));
            return;
        }
        UserDesktopEntity userDesktopEntity = desktopByTerminalIdList.get(0);
        CbbDeskFaultInfoResponse response = deskFaultInfoAPI.findFaultInfoByDeskId(userDesktopEntity.getCbbDesktopId());
        if (response == null || response.getCbbDeskFaultInfoDTO() == null) {
            LOGGER.warn("未找到终端报障信息, terminalId {}", request.getTerminalId());
            response(request, CommonMessageCode.FAIL_CODE, new RequestReportFaultResultResponse(ProcessStageEnum.COMPLETED));
            return;
        }
        CbbDeskFaultInfoDTO cbbDeskFaultInfoDTO = response.getCbbDeskFaultInfoDTO();
        String dateMinuteFormat = DateUtil.getDateMinuteFormat(cbbDeskFaultInfoDTO.getFaultTime());
        if (cbbDeskFaultInfoDTO.getFaultState() == Boolean.TRUE) {
            response(request, CommonMessageCode.SUCCESS,
                    new RequestReportFaultResultResponse(ProcessStageEnum.PROCESSING, dateMinuteFormat));
        } else {
            response(request, CommonMessageCode.SUCCESS,
                    new RequestReportFaultResultResponse(ProcessStageEnum.COMPLETED, dateMinuteFormat));
        }
    }

    private void response(CbbDispatcherRequest request, Integer code, Object content) {
        try {
            LOGGER.info("返回终端[{}]的获取报障结果code：[{}], 内容[{}]", request.getTerminalId(), code, content);
            shineMessageHandler.responseContent(request, code, content);
        } catch (Exception e) {
            LOGGER.error("返回给终端{}报障结果失败", request.getTerminalId(), e);
        }
    }
}
