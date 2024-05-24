package com.ruijie.rcos.rcdc.rco.module.web.ctrl.terminal.validation;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;
import com.ruijie.rcos.rcdc.rco.module.def.api.ServerModelAPI;
import com.ruijie.rcos.rcdc.rco.module.web.ctrl.deskstrategy.DeskStrategyBusinessKey;
import com.ruijie.rcos.rcdc.rco.module.web.ctrl.terminal.request.TerminalPlatformWebRequest;
import com.ruijie.rcos.rcdc.terminal.module.def.enums.CbbTerminalPlatformEnums;
import com.ruijie.rcos.sk.base.exception.BusinessException;
import com.ruijie.rcos.sk.base.log.Logger;
import com.ruijie.rcos.sk.base.log.LoggerFactory;

/**
 * Description: Function Description
 * Copyright: Copyright (c) 2021
 * Company: Ruijie Co., Ltd.
 * Create Time: 2021年1月20日
 * 
 * @author wjp
 */
@Service
public class TerminalModelValidation {

    private static final Logger LOGGER = LoggerFactory.getLogger(TerminalModelValidation.class);

    @Autowired
    private ServerModelAPI serverModelAPI;

    /**
     * 获取终端型号列表参数校验
     * 
     * @param request 请求参数对象
     * @throws BusinessException 异常
     */
    public void listTerminalModelValidate(TerminalPlatformWebRequest request) throws BusinessException {
        Assert.notNull(request, "request is not null");

        if (serverModelAPI.isVdiModel()) {
            return;
        }
        platformValueValidate(request.getPlatformArr());
    }

    /**
     * 获取终端运行平台类型参数校验
     * 
     * @param request 请求参数对象
     * @throws BusinessException 异常
     */
    public void listTerminalOsTypeValidate(TerminalPlatformWebRequest request) throws BusinessException {
        Assert.notNull(request, "request is not null");

        if (serverModelAPI.isVdiModel()) {
            return;
        }
        platformValueValidate(request.getPlatformArr());
    }

    private void platformValueValidate(CbbTerminalPlatformEnums[] platformArr) throws BusinessException {
        if (platformArr != null && platformArr.length > 0) {
            /** 校验非法菜单名称 */
            for (CbbTerminalPlatformEnums platform : platformArr) {
                if (platform == CbbTerminalPlatformEnums.VDI || platform == CbbTerminalPlatformEnums.APP) {
                    LOGGER.error("illegal platform [{}]", platform);
                    throw new BusinessException(DeskStrategyBusinessKey.RCO_DESK_STRATEGY_INVALID_VDI_MEM_VALUE);
                }
            }
        }
    }
}
