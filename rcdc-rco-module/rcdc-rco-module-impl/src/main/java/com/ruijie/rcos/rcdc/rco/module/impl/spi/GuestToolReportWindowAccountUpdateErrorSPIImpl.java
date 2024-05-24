package com.ruijie.rcos.rcdc.rco.module.impl.spi;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.ruijie.rcos.base.alarm.module.def.api.BaseAlarmAPI;
import com.ruijie.rcos.base.alarm.module.def.api.request.SaveAlarmRequest;
import com.ruijie.rcos.base.alarm.module.def.enums.AlarmLevel;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.api.CbbDeskMgmtAPI;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.constants.AlarmConstants;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.dto.CbbDeskDTO;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.dto.CbbGuesttoolMessageDTO;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.spi.CbbGuestToolMessageDispatcherSPI;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.spi.request.CbbGuestToolSPIReceiveRequest;
import com.ruijie.rcos.rcdc.hciadapter.module.def.dto.guesttool.GuesttoolMessageContent;
import com.ruijie.rcos.rcdc.rco.module.def.BusinessKey;
import com.ruijie.rcos.rcdc.rco.module.def.constants.Constants;
import com.ruijie.rcos.rcdc.rco.module.impl.spi.guesttool.GuestToolCmdId;
import com.ruijie.rcos.sk.base.exception.BusinessException;
import com.ruijie.rcos.sk.base.i18n.LocaleI18nResolver;
import com.ruijie.rcos.sk.base.log.Logger;
import com.ruijie.rcos.sk.base.log.LoggerFactory;
import com.ruijie.rcos.sk.modulekit.api.comm.DispatcherImplemetion;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.Assert;

import java.util.Date;

/**
 * Description: gt上报window账号密码更新失败
 * Copyright: Copyright (c) 2020
 * Company: Ruijie Co., Ltd.
 * Create Time: 2023/8/24
 *
 * @author zjy
 */
@DispatcherImplemetion(GuestToolCmdId.GT_REPORT_UPDATE_WINDOWS_ACCOUNT_OR_PWD_ERROR)
public class GuestToolReportWindowAccountUpdateErrorSPIImpl implements CbbGuestToolMessageDispatcherSPI {

    private static final Logger LOGGER = LoggerFactory.getLogger(GuestToolReportWindowAccountUpdateErrorSPIImpl.class);

    private static final int ERROR_CODE_HAS_MULTI_USER = 1;

    private static final int ERROR_CODE_INVALID_USERNAME = 2;

    private static final int ERROR_CODE_INVALID_PASSWORD = 3;

    private static final int ERROR_CODE_INVALID_USERNAME_AND_PASSWORD = 4;

    private static final int ERROR_CODE_PASSWORD_TOO_SHORT = 5;

    @Autowired
    private BaseAlarmAPI alarmAPI;

    @Autowired
    private CbbDeskMgmtAPI cbbDeskMgmtAPI;

    @Override
    public CbbGuesttoolMessageDTO receive(CbbGuestToolSPIReceiveRequest request) throws BusinessException {
        Assert.notNull(request, "request is null");

        CbbGuesttoolMessageDTO response = new CbbGuesttoolMessageDTO();
        response.setPortId(request.getDto().getPortId());
        response.setCmdId(request.getDto().getCmdId());
        response.setDeskId(request.getDto().getDeskId());
        if (request.getDto().getDeskId() == null) {
            LOGGER.info("windows用户名或密码设置失败，上报桌面信息为空，暂不处理");
            return response;
        }

        LOGGER.info("windows用户名或密码设置失败，上报信息为：[{}]", JSONObject.toJSONString(request.getDto()));
        try {
            CbbDeskDTO deskDTO = cbbDeskMgmtAPI.getDeskById(request.getDto().getDeskId());
            SaveAlarmRequest saveAlarmRequest = new SaveAlarmRequest();
            saveAlarmRequest.setAlarmCode(Constants.ALARM_PREFIX_GT_UPDATE_WINDOWS_ACCOUNT_OR_PWD_ERROR
                    + "-" + request.getDto().getDeskId());
            saveAlarmRequest.setAlarmType(AlarmConstants.ALARM_TYPE_DESKTOP);
            saveAlarmRequest.setAlarmLevel(AlarmLevel.TIPS);
            saveAlarmRequest.setAlarmTime(new Date());
            saveAlarmRequest.setEnableSendMail(true);
            saveAlarmRequest.setBusinessId(request.getDto().getDeskId().toString());
            saveAlarmRequest.setAlarmName(LocaleI18nResolver.resolve(
                    BusinessKey.RCDC_RCO_GT_UPDATE_WINDOWS_ACCOUNT_OR_PWD_ERROR_NAME));
            String reasonKey = BusinessKey.RCDC_RCO_GT_UPDATE_WINDOWS_ACCOUNT_ERROR_REASON_OTHER;
            GuesttoolMessageContent requestContent = JSON.parseObject(request.getDto().getBody(), GuesttoolMessageContent.class);
            if (ERROR_CODE_HAS_MULTI_USER  == requestContent.getCode()) {
                reasonKey = BusinessKey.RCDC_RCO_GT_UPDATE_WINDOWS_ACCOUNT_ERROR_REASON_HAS_MULTI_USER;
            } else if (ERROR_CODE_INVALID_USERNAME  == requestContent.getCode()) {
                reasonKey = BusinessKey.RCDC_RCO_GT_UPDATE_WINDOWS_ACCOUNT_ERROR_REASON_INVALID_USERNAME;
            } else if (ERROR_CODE_INVALID_PASSWORD  == requestContent.getCode()) {
                reasonKey = BusinessKey.RCDC_RCO_GT_UPDATE_WINDOWS_ACCOUNT_ERROR_REASON_INVALID_PASSWORD;
            } else if (ERROR_CODE_INVALID_USERNAME_AND_PASSWORD  == requestContent.getCode()) {
                reasonKey = BusinessKey.RCDC_RCO_GT_UPDATE_WINDOWS_ACCOUNT_ERROR_REASON_INVALID_USERNAME_AND_PASSWORD;
            } else if (ERROR_CODE_PASSWORD_TOO_SHORT  == requestContent.getCode()) {
                reasonKey = BusinessKey.RCDC_RCO_GT_UPDATE_WINDOWS_ACCOUNT_ERROR_REASON_PASSWORD_TOO_SHORT;
            }
            saveAlarmRequest.setAlarmContent(LocaleI18nResolver.resolve(
                    BusinessKey.RCDC_RCO_GT_UPDATE_WINDOWS_ACCOUNT_OR_PWD_ERROR_CONTENT, deskDTO.getName(),
                    LocaleI18nResolver.resolve(reasonKey)));
            alarmAPI.saveAlarm(saveAlarmRequest);
        } catch (Exception e) {
            LOGGER.error("记录window用户名设置失败告警异常！", e);
        }

        return response;
    }
}
