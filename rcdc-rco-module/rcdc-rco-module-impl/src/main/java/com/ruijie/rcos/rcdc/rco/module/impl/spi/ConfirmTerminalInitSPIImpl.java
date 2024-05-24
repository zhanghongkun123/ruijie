package com.ruijie.rcos.rcdc.rco.module.impl.spi;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.ruijie.rcos.gss.log.module.def.api.BaseSystemLogMgmtAPI;
import com.ruijie.rcos.gss.log.module.def.api.request.systemlog.BaseCreateSystemLogRequest;
import com.ruijie.rcos.rcdc.codec.adapter.def.dto.CbbDispatcherRequest;
import com.ruijie.rcos.rcdc.codec.adapter.def.spi.CbbDispatcherHandlerSPI;
import com.ruijie.rcos.rcdc.rco.module.def.api.UserTerminalMgmtAPI;
import com.ruijie.rcos.rcdc.rco.module.def.api.UwsDockingAPI;
import com.ruijie.rcos.rcdc.rco.module.def.api.dto.TerminalDTO;
import com.ruijie.rcos.rcdc.rco.module.impl.BusinessKey;
import com.ruijie.rcos.rcdc.rco.module.impl.spi.common.ShineAction;
import com.ruijie.rcos.rcdc.terminal.module.def.api.CbbTerminalOperatorAPI;
import com.ruijie.rcos.rcdc.terminal.module.def.api.dto.CbbTerminalBasicInfoDTO;
import com.ruijie.rcos.sk.base.exception.BusinessException;
import com.ruijie.rcos.sk.base.log.Logger;
import com.ruijie.rcos.sk.base.log.LoggerFactory;
import com.ruijie.rcos.sk.modulekit.api.comm.DispatcherImplemetion;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.Assert;

/**
 * Description:
 * Copyright: Copyright (c) 2019
 * Company: Ruijie Co., Ltd.
 * Create Time: 2020/2/4 11:29
 *
 * @author conghaifeng
 */
@DispatcherImplemetion(ShineAction.RCDC_SHINE_CMM_TERMINAL_INIT_CHOICE)
public class ConfirmTerminalInitSPIImpl implements CbbDispatcherHandlerSPI {

    private static final Logger LOGGER = LoggerFactory.getLogger(ConfirmTerminalInitSPIImpl.class);

    @Autowired
    private BaseSystemLogMgmtAPI baseSystemLogMgmtAPI;

    @Autowired
    private UwsDockingAPI uwsDockingAPI;

    @Autowired
    private UserTerminalMgmtAPI userTerminalMgmtAPI;

    @Autowired
    private CbbTerminalOperatorAPI cbbTerminalOperatorAPI;

    private static final String RESULT_KEY = "enableInit";

    /**
     * 消息分发方法
     *
     * @param request 请求参数对象 请求参数
     */
    @Override
    public void dispatch(CbbDispatcherRequest request) {
        Assert.notNull(request, "CbbDispatcherRequest不能为null");
        Assert.hasText(request.getTerminalId(), "terminalId 不能为空");
        Assert.notNull(request.getData(), "报文消息体不能为空");
        String terminalId = request.getTerminalId();
        String data = request.getData();
        JSONObject jsonObject = JSON.parseObject(data);
        Boolean enableInit = jsonObject.getBoolean(RESULT_KEY);
        if (enableInit == null) {
            LOGGER.error("终端返回是否初始化终端值为null，terminalId = [{}]", request.getTerminalId());
            return;
        }

        String terminalAddr = terminalId;
        try {
            CbbTerminalBasicInfoDTO terminalBasicInfoDTO = cbbTerminalOperatorAPI.findBasicInfoByTerminalId(terminalId);
            terminalAddr = terminalBasicInfoDTO.getUpperMacAddrOrTerminalId();
        } catch (BusinessException e) {
            LOGGER.debug("查询终端信息发生异常，终端id: [{}]", terminalId);
        }

        if (Boolean.TRUE.equals(enableInit)) {
            //记录shine确认进行终端初始化
            LOGGER.info("终端确认进行终端初始化,terminalId = [{}]", terminalId);
            //记录系统日志
            baseSystemLogMgmtAPI.createSystemLog(new BaseCreateSystemLogRequest(BusinessKey.RCDC_USER_TERMINAL_CONFIRM_INIT,
                    terminalAddr));

            try {
                TerminalDTO terminalDTO = userTerminalMgmtAPI.getTerminalById(terminalId);
                if (terminalDTO.getBindDeskId() == null) {
                    LOGGER.info("终端没有绑定桌面,terminalId = [{}]", terminalId);
                } else {
                    // 通知UWS桌面新增
                    uwsDockingAPI.notifyDesktopDelete(terminalDTO.getBindDeskId());
                }

            } catch (Exception ex) {
                LOGGER.error(String.format("查找终端信息或发送删除通知失败,terminalId = %s", terminalId), ex);
            }
        } else {
            //记录shine取消终端初始化
            LOGGER.info("终端取消终端初始化,terminalId = [{}]", terminalId);
            //记录系统日志
            baseSystemLogMgmtAPI.createSystemLog(new BaseCreateSystemLogRequest(BusinessKey.RCDC_USER_TERMINAL_CANCEL_INIT,
                    terminalAddr));
        }
    }


}
