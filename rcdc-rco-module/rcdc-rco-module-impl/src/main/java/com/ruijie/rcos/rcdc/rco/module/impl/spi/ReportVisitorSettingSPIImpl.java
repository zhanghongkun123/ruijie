package com.ruijie.rcos.rcdc.rco.module.impl.spi;

import com.ruijie.rcos.rcdc.codec.adapter.def.dto.CbbDispatcherRequest;
import com.ruijie.rcos.rcdc.codec.adapter.def.spi.CbbDispatcherHandlerSPI;
import com.ruijie.rcos.rcdc.rco.module.impl.dao.UserTerminalDAO;
import com.ruijie.rcos.rcdc.rco.module.impl.entity.UserTerminalEntity;
import com.ruijie.rcos.rcdc.rco.module.impl.spi.common.CommonMessageCode;
import com.ruijie.rcos.rcdc.rco.module.impl.spi.common.ShineAction;
import com.ruijie.rcos.rcdc.rco.module.impl.spi.common.ShineMessageHandler;
import com.ruijie.rcos.rcdc.rco.module.impl.spi.dto.TerminalSettingDTO;
import com.ruijie.rcos.sk.base.log.Logger;
import com.ruijie.rcos.sk.base.log.LoggerFactory;
import com.ruijie.rcos.sk.modulekit.api.comm.DispatcherImplemetion;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.Assert;

/**
 * Description:
 * Copyright: Copyright (c) 2019
 * Company: Ruijie Co., Ltd.
 * Create Time: 2019/12/1 12:17
 *
 * @author conghaifeng
 */

@DispatcherImplemetion(ShineAction.RCDC_SHINE_CMM_VISITOR_SETTING)
public class ReportVisitorSettingSPIImpl implements CbbDispatcherHandlerSPI {

    private static final Logger LOGGER = LoggerFactory.getLogger(ReportVisitorSettingSPIImpl.class);

    @Autowired
    private ShineMessageHandler shineMessageHandler;

    @Autowired
    private UserTerminalDAO userTerminalDAO;

    /**
     * 消息分发方法
     *
     * @param request 请求参数对象 请求参数
     */
    @Override
    public void dispatch(CbbDispatcherRequest request) {
        Assert.notNull(request, "CbbDispatcherRequest不能为null");
        String data = request.getData();
        TerminalSettingDTO visitorSettingDTO;
        try {
            visitorSettingDTO = shineMessageHandler.parseObject(data, TerminalSettingDTO.class);
        } catch (Exception e) {
            LOGGER.error("解析终端修改访客登录设置报文失败，请检查报文格式是否正确", e);
            return;
        }
        // 判断终端对应的访客登录设置是否存在
        UserTerminalEntity entity = userTerminalDAO.findFirstByTerminalId(request.getTerminalId());
        if (entity == null) {
            LOGGER.info("数据库找不到对应终端[{}]的访客登录设置信息", request.getTerminalId());
            responseMessage(request, CommonMessageCode.CODE_ERR_OTHER);
            return;
        }
        // 保存访客登录设置，并返回信息
        updateVisitorSetting(visitorSettingDTO, entity);
        responseMessage(request, CommonMessageCode.SUCCESS);
    }

    private void responseMessage(CbbDispatcherRequest request, int responseCode) {
        String terminalId = request.getTerminalId();
        try {
            shineMessageHandler.response(request, responseCode);
            LOGGER.info("应答终端[{}]修改访客登录设置报文,应答状态码：", terminalId, responseCode);
        } catch (Exception e) {
            LOGGER.error("应答终端[{}]修改访客登录设置失败，应答状态码：", terminalId, CommonMessageCode.CODE_ERR_OTHER, e);
        }
    }

    private void updateVisitorSetting(TerminalSettingDTO dto, UserTerminalEntity entity) {
        entity.setEnableVisitorLogin(dto.getEnableVisitorLogin());
        entity.setEnableAutoLogin(dto.getEnableAutoLogin());
        userTerminalDAO.save(entity);
    }
}
