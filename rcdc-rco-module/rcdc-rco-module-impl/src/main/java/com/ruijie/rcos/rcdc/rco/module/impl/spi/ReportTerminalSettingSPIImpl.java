package com.ruijie.rcos.rcdc.rco.module.impl.spi;

import com.alibaba.fastjson.JSON;
import com.ruijie.rcos.rcdc.codec.adapter.def.dto.CbbDispatcherRequest;
import com.ruijie.rcos.rcdc.codec.adapter.def.spi.CbbDispatcherHandlerSPI;
import com.ruijie.rcos.rcdc.rco.module.impl.service.TerminalService;
import com.ruijie.rcos.rcdc.rco.module.impl.spi.common.ShineAction;
import com.ruijie.rcos.rcdc.rco.module.impl.spi.dto.TerminalSettingDTO;
import com.ruijie.rcos.sk.base.exception.BusinessException;
import com.ruijie.rcos.sk.base.log.Logger;
import com.ruijie.rcos.sk.base.log.LoggerFactory;
import com.ruijie.rcos.sk.modulekit.api.comm.DispatcherImplemetion;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.Assert;

/**
 * Description: 上报终端设置消息处理
 * Copyright: Copyright (c) 2018
 * Company: Ruijie Co., Ltd.
 * Create Time: 2019/11/05
 *
 * @author nt
 */
@DispatcherImplemetion(ShineAction.REPORT_TERMINAL_SETTING)
public class ReportTerminalSettingSPIImpl implements CbbDispatcherHandlerSPI {

    private static final Logger LOGGER = LoggerFactory.getLogger(ReportTerminalSettingSPIImpl.class);

    @Autowired
    private TerminalService terminalService;

    @Override
    public void dispatch(CbbDispatcherRequest request) {
        Assert.notNull(request, "CbbDispatcherRequest不能为空");
        Assert.hasText(request.getData(), "data can not be null");
        Assert.hasText(request.getTerminalId(), "terminalId can not be blank");

        TerminalSettingDTO terminalSettingDTO = convertJsonData(request);
        try {
            terminalService.editTerminalSetting(terminalSettingDTO);
        } catch (BusinessException e) {
            LOGGER.error("更新终端" + request.getTerminalId() + "上报终端模式及访客登录设置失败", e);
        }
    }

    private TerminalSettingDTO convertJsonData(CbbDispatcherRequest request) {
        try {
            TerminalSettingDTO terminalSettingDTO = JSON.parseObject(request.getData(), TerminalSettingDTO.class);
            terminalSettingDTO.setTerminalId(request.getTerminalId());

            return terminalSettingDTO;
        } catch (Exception e) {
            LOGGER.error("上报终端设置消息报文数据格式转换错误；data:[" + request.getData() + "]", e);
            throw new IllegalArgumentException("上报终端设置消息报文数据格式转换错误；data:[" + request.getData() + "]", e);
        }
    }
}
