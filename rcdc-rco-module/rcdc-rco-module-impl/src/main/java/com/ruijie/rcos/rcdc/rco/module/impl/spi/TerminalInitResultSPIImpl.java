package com.ruijie.rcos.rcdc.rco.module.impl.spi;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.ruijie.rcos.gss.log.module.def.api.BaseSystemLogMgmtAPI;
import com.ruijie.rcos.gss.log.module.def.api.request.systemlog.BaseCreateSystemLogRequest;
import com.ruijie.rcos.rcdc.codec.adapter.def.dto.CbbDispatcherRequest;
import com.ruijie.rcos.rcdc.codec.adapter.def.spi.CbbDispatcherHandlerSPI;
import com.ruijie.rcos.rcdc.rco.module.impl.BusinessKey;
import com.ruijie.rcos.rcdc.rco.module.impl.dao.UserTerminalDAO;
import com.ruijie.rcos.rcdc.rco.module.impl.entity.UserTerminalEntity;
import com.ruijie.rcos.rcdc.rco.module.impl.service.TerminalService;
import com.ruijie.rcos.rcdc.rco.module.impl.spi.common.ShineAction;
import com.ruijie.rcos.rcdc.terminal.module.def.api.CbbTerminalConfigAPI;
import com.ruijie.rcos.rcdc.terminal.module.def.api.CbbTerminalLicenseMgmtAPI;
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
 * Copyright: Copyright (c) 2020
 * Company: Ruijie Co., Ltd.
 * Create Time: 2020/3/1 16:50
 *
 * @author conghaifeng
 */
@DispatcherImplemetion(ShineAction.RCDC_SHINE_CMM_TERMINAL_INIT_RESULT)
public class TerminalInitResultSPIImpl implements CbbDispatcherHandlerSPI {

    private static final Logger LOGGER = LoggerFactory.getLogger(TerminalInitResultSPIImpl.class);

    @Autowired
    private UserTerminalDAO userTerminalDAO;

    @Autowired
    private BaseSystemLogMgmtAPI baseSystemLogMgmtAPI;

    @Autowired
    private CbbTerminalOperatorAPI cbbTerminalOperatorAPI;

    @Autowired
    private TerminalService terminalService;

    @Autowired
    private CbbTerminalLicenseMgmtAPI cbbTerminalLicenseMgmtAPI;

    @Autowired
    private CbbTerminalConfigAPI cbbTerminalConfigAPI;
    
    private static final String RESULT_KEY = "initResult";

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
        UserTerminalEntity userTerminalEntity = userTerminalDAO.findFirstByTerminalId(terminalId);
        if (userTerminalEntity == null) {
            LOGGER.error("终端不存在,terminalId = [{}]", terminalId);
            return;
        }
        String data = request.getData();
        JSONObject jsonObject = JSON.parseObject(data);
        Boolean isInitSuccess = jsonObject.getBoolean(RESULT_KEY);
        if (isInitSuccess == null) {
            LOGGER.error("终端返回初始化终端是否成功值为null，terminalId = [{}]", request.getTerminalId());
            return;
        }
        if (Boolean.TRUE.equals(isInitSuccess)) {
            initSuccess(userTerminalEntity);
        } else {
            initFail(userTerminalEntity);
        }
    }

    private void initSuccess(UserTerminalEntity userTerminalEntity) {

        recycleTerminalAuth(userTerminalEntity.getTerminalId());

        terminalService.initialTerminalCleanData(userTerminalEntity);
    }

    /**
     * 终端通过初始化切换部署模式(VDI\IDV),初始化后回收授权
     * 
     * @param terminalId 终端id
     */
    private void recycleTerminalAuth(String terminalId) {
        try {

            CbbTerminalBasicInfoDTO terminalBasicInfoDTO = cbbTerminalOperatorAPI.findBasicInfoByTerminalId(terminalId);

            if (cbbTerminalConfigAPI.hasTerminalSupportVdiAndIdv(terminalBasicInfoDTO.getProductType())
                    && Boolean.TRUE.equals(terminalBasicInfoDTO.getAuthed())) {
                LOGGER.info("终端[{}]初始化回收终端授权", terminalId);
                cbbTerminalLicenseMgmtAPI.cancelTerminalAuth(terminalId);
            }

        } catch (BusinessException e) {
            LOGGER.error("终端初始化回收终端授权发生异常，终端id: [{}]", terminalId, e);
        }
    }
    
    private void initFail(UserTerminalEntity userTerminalEntity) {
        // 获取终端mac
        String terminalId = userTerminalEntity.getTerminalId();
        // 记录终端初始化失败
        LOGGER.warn("终端终端初始化失败,terminalId = [{}]", terminalId);
        String terminalAddr = terminalId;
        try {
            CbbTerminalBasicInfoDTO terminalBasicInfoDTO = cbbTerminalOperatorAPI.findBasicInfoByTerminalId(terminalId);
            terminalAddr = terminalBasicInfoDTO.getUpperMacAddrOrTerminalId();
        } catch (BusinessException e) {
            LOGGER.debug("查询终端信息发生异常，终端id: [{}]", terminalId);
        }
        // 记录系统日志
        baseSystemLogMgmtAPI.createSystemLog(new BaseCreateSystemLogRequest(BusinessKey.RCDC_USER_TERMINAL_INIT_FAIL, terminalAddr));
    }

}
