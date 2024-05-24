package com.ruijie.rcos.rcdc.rco.module.impl.spi;

import com.ruijie.rcos.rcdc.clouddesktop.module.def.api.CbbDeskMgmtAPI;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.enums.CbbCloudDeskType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.Assert;

import com.alibaba.fastjson.JSON;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.api.CbbVDIDeskGtMgmtAPI;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.dto.CbbGuesttoolMessageDTO;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.dto.deskext.CbbGtHeartBeatConfig;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.spi.CbbGuestToolMessageDispatcherSPI;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.spi.request.CbbGuestToolSPIReceiveRequest;
import com.ruijie.rcos.rcdc.hciadapter.module.def.dto.guesttool.GuesttoolMessageContent;
import com.ruijie.rcos.rcdc.rco.module.impl.remoteassist.enums.GuesttoolMessageResultTypeEnum;
import com.ruijie.rcos.rcdc.rco.module.impl.spi.guesttool.GuestToolCmdId;
import com.ruijie.rcos.sk.base.exception.BusinessException;
import com.ruijie.rcos.sk.base.log.Logger;
import com.ruijie.rcos.sk.base.log.LoggerFactory;
import com.ruijie.rcos.sk.modulekit.api.comm.DispatcherImplemetion;

/**
 * Description: GT上报启动完成消息
 * Copyright: Copyright (c) 2021
 * Company: Ruijie Co., Ltd.
 * Create Time: 2021/7/26 11:00
 *
 * @author zhangyichi
 */
@DispatcherImplemetion(GuestToolCmdId.RCDC_GT_CMD_ID_GT_START_END)
public class GuestToolReportStartSPIImpl implements CbbGuestToolMessageDispatcherSPI {

    private static final Logger LOGGER = LoggerFactory.getLogger(GuestToolReportStartSPIImpl.class);

    @Autowired
    private CbbVDIDeskGtMgmtAPI cbbVDIDeskGtMgmtAPI;

    @Autowired
    private CbbDeskMgmtAPI cbbDeskMgmtAPI;
    
    @Override
    public CbbGuesttoolMessageDTO receive(CbbGuestToolSPIReceiveRequest request) throws BusinessException {
        Assert.notNull(request, "request cannot be null!");

        CbbGuesttoolMessageDTO requestDto = request.getDto();
        LOGGER.info("收到GT启动完成消息，deskId[{}]", requestDto.getDeskId());
        // 暂时用不到，接口保留
        CbbGtHeartBeatConfig cbbGtHeartBeatConfig = new CbbGtHeartBeatConfig();
        CbbCloudDeskType desktopType = cbbDeskMgmtAPI.getDesktopType(requestDto.getDeskId());
        if (CbbCloudDeskType.VDI != desktopType) {
            // 非VDI 桌面 则不开启心跳
            cbbGtHeartBeatConfig.setEnableGtHeartBeat(false);
        } else {
            // VDI 桌面 查询心跳信息
            cbbGtHeartBeatConfig = cbbVDIDeskGtMgmtAPI.getGtHeartBeatConfig();
        }
      
        //返回GT 心跳总开关
        final CbbGuesttoolMessageDTO responseDto = new CbbGuesttoolMessageDTO();
        responseDto.setCmdId(requestDto.getCmdId());
        responseDto.setPortId(requestDto.getPortId());
        responseDto.setDeskId(requestDto.getDeskId());
        responseDto.setTerminalId(requestDto.getTerminalId());
        // 配置默认code、message、content值
        GuesttoolMessageContent messageContent = new GuesttoolMessageContent();
        messageContent.setCode(GuesttoolMessageResultTypeEnum.SUCCESS.getCode());
        messageContent.setMessage(GuesttoolMessageResultTypeEnum.SUCCESS.getMessage());
        messageContent.setContent(cbbGtHeartBeatConfig);

        responseDto.setBody(JSON.toJSONString(messageContent));
      
        return responseDto;
    }



}
