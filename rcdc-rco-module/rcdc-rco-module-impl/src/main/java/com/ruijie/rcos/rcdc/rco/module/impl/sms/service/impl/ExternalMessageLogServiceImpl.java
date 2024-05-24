package com.ruijie.rcos.rcdc.rco.module.impl.sms.service.impl;

import com.ruijie.rcos.rcdc.rco.module.def.sms.enums.MessageBusinessType;
import com.ruijie.rcos.rcdc.rco.module.def.sms.enums.MessagePlatformType;
import com.ruijie.rcos.rcdc.rco.module.impl.sms.dao.ExternalMessageLogDAO;
import com.ruijie.rcos.rcdc.rco.module.impl.sms.service.ExternalMessageLogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import java.util.Date;

/**
 * Description: ExternalMessageLogServiceImpl
 * Copyright: Copyright (c) 2023
 * Company: Ruijie Co., Ltd.
 * Create Time: 2023/6/29
 *
 * @author TD
 */
@Service
public class ExternalMessageLogServiceImpl implements ExternalMessageLogService {
    
    @Autowired
    private ExternalMessageLogDAO externalMessageLogDAO;
    
    @Override
    public Integer countBySendTimeLessThan(Date overdueTime) {
        Assert.notNull(overdueTime, "overdueTime must not be null");
        return externalMessageLogDAO.countBySendTimeLessThan(overdueTime);
    }

    @Override
    public void deleteBySendTimeLessThan(Date overdueTime) {
        Assert.notNull(overdueTime, "overdueTime must not be null");
        externalMessageLogDAO.deleteBySendTimeLessThan(overdueTime);
    }

    @Override
    public Long countCurrentDayMessageSuccessTotalNumber(MessageBusinessType relatedType, String relatedTarget) {
        Assert.notNull(relatedType, "relatedType must not be null");
        Assert.hasText(relatedTarget, "relatedTarget must not be empty");
        return externalMessageLogDAO.countCurrentDayMessageSuccessTotalNumber(relatedType.name(), relatedTarget, MessagePlatformType.SMS.name());
    }
}
