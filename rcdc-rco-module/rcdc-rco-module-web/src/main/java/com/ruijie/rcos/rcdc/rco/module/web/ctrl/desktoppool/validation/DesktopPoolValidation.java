package com.ruijie.rcos.rcdc.rco.module.web.ctrl.desktoppool.validation;

import com.ruijie.rcos.rcdc.rco.module.web.ctrl.user.request.desktop.EditNeworkWebRequest;
import com.ruijie.rcos.rcdc.rco.module.web.validation.CloudDesktopValidation;
import com.ruijie.rcos.sk.base.batch.BatchTaskBuilder;
import com.ruijie.rcos.sk.base.exception.BusinessException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

/**
 * Description:
 * Copyright: Copyright (c) 2023
 * Company: Ruijie Co., Ltd.
 * Create Time: 2024/1/26
 *
 * @author linke
 */
@Service
public class DesktopPoolValidation {

    @Autowired
    private CloudDesktopValidation cloudDesktopValidation;


    /**
     * @param request edit desktop request
     * @param builder builder
     * @throws BusinessException 业务异常
     */
    public void networkIpValidate(EditNeworkWebRequest request, BatchTaskBuilder builder) throws BusinessException {
        Assert.notNull(request, "request is null");
        Assert.notNull(builder, "builder is null");
        cloudDesktopValidation.networkIpValidate(request, builder);
    }

}
