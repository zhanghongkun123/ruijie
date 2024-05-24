package com.ruijie.rcos.rcdc.rco.module.impl.service.impl;

import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;
import org.springframework.util.CollectionUtils;

import com.ruijie.rcos.base.alarm.module.def.api.BaseAlarmAPI;
import com.ruijie.rcos.base.alarm.module.def.api.request.SaveAlarmRequest;
import com.ruijie.rcos.base.alarm.module.def.enums.AlarmLevel;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.constants.AlarmConstants;
import com.ruijie.rcos.rcdc.rco.module.def.BusinessKey;
import com.ruijie.rcos.rcdc.rco.module.def.api.UserTerminalMgmtAPI;
import com.ruijie.rcos.rcdc.rco.module.def.api.dto.TerminalDTO;
import com.ruijie.rcos.rcdc.rco.module.impl.dto.CheckTerminalWhiteDTO;
import com.ruijie.rcos.rcdc.rco.module.impl.service.AuthorizationTerminalWhiteSnService;
import com.ruijie.rcos.rcdc.rco.module.impl.service.TerminalService;
import com.ruijie.rcos.rcdc.terminal.module.def.api.dto.CbbTerminalWhiteSnDTO;
import com.ruijie.rcos.sk.base.exception.BusinessException;
import com.ruijie.rcos.sk.base.i18n.LocaleI18nResolver;
import com.ruijie.rcos.sk.base.log.Logger;
import com.ruijie.rcos.sk.base.log.LoggerFactory;
import com.ruijie.rcos.sk.base.util.StringUtils;

/**
 * Description: Function Description
 * Copyright: Copyright (c) 2018
 * Company: Ruijie Co., Ltd.
 * Create Time: 2022/8/31
 *
 * @author zjy
 */
@Service
public class AuthorizationTerminalWhiteSnServiceImpl implements AuthorizationTerminalWhiteSnService {

    private static final Logger LOGGER = LoggerFactory.getLogger(AuthorizationTerminalWhiteSnServiceImpl.class);


    private static final Lock WHITELIST_TERMINAL_CACHE_LOCK = new ReentrantLock();

    @Autowired
    private BaseAlarmAPI alarmAPI;

    @Autowired
    private UserTerminalMgmtAPI userTerminalMgmtAPI;

    @Autowired
    private TerminalService terminalService;

    public static final String SPLIT = "/";



    @Override
    public void saveIllegalTerminalWarnLog(String terminalId, String terminalMac, String productType, @Nullable String sn) {
        Assert.hasText(terminalId, "terminalId不能为空");
        Assert.hasText(terminalMac, "terminalMac不能为空");
        Assert.hasText(productType, "productType不能为空");

        String warnLog =
                LocaleI18nResolver.resolve(BusinessKey.RCDC_RCO_AUTHORIZATION_WHITELIST_TERMINAL_ILLEGAL, terminalId, terminalMac, productType);
        SaveAlarmRequest request = new SaveAlarmRequest();
        request.setAlarmLevel(AlarmLevel.WARN);
        request.setAlarmType(AlarmConstants.ALARM_TYPE_RCDC_SERVICE);
        request.setAlarmNameByI18nKey(BusinessKey.RCDC_RCO_RCO_AUTHORIZATION_WHITELIST_CHECK);
        request.setAlarmContent(warnLog);
        request.setAlarmTime(new Date());
        request.setEnableSendMail(true);
        request.setAlarmCode(AlarmConstants.ALARM_TYPE_RCDC_SERVICE + terminalId);
        alarmAPI.saveAlarm(request);
    }
    
}
