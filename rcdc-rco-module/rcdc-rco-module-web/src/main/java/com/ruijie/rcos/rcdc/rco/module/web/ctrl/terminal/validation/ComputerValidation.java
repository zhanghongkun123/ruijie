package com.ruijie.rcos.rcdc.rco.module.web.ctrl.terminal.validation;

import com.ruijie.rcos.rcdc.rco.module.def.api.ComputerBusinessAPI;
import com.ruijie.rcos.rcdc.rco.module.def.api.response.ComputerInfoResponse;
import com.ruijie.rcos.rcdc.rco.module.web.ctrl.terminal.ComputerBusinessKey;
import com.ruijie.rcos.rcdc.rco.module.web.ctrl.terminal.request.CreateComputerWebRequest;
import com.ruijie.rcos.sk.base.exception.BusinessException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

/**
 *
 * Description: Function Description
 * Copyright: Copyright (c) 2018 Company:
 * Ruijie Co., Ltd. Create Time: 2024/1/16
 *
 * @author artom
 */
@Service
public class ComputerValidation {



    @Autowired
    private ComputerBusinessAPI computerBusinessAPI;

    /**
     * @param request CreateComputerWebRequest request
     * @throws BusinessException 业务异常
     */
    public void computerCreateValidate(CreateComputerWebRequest request) throws BusinessException {
        Assert.notNull(request, "request is null");

        ComputerInfoResponse computerInfo = computerBusinessAPI.getComputerInfoByIp(request.getIp());
        if (computerInfo != null) {
            throw new BusinessException(ComputerBusinessKey.RCDC_RCO_COMPUTER_CREATE_TERMINAL_IP_EXIST);
        }
    }




}
