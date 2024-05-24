package com.ruijie.rcos.rcdc.rco.module.impl.sms.service.impl;

import com.alibaba.fastjson.JSON;
import com.ruijie.rcos.base.notify.module.def.api.BaseSmsNotifyAPI;
import com.ruijie.rcos.rcdc.rco.module.def.sms.constnts.SmsAndScanCodeCheckConstants;
import com.ruijie.rcos.rcdc.rco.module.def.sms.dto.HttpConfigDTO;
import com.ruijie.rcos.rcdc.rco.module.def.sms.dto.SmsGatewayConfigDTO;
import com.ruijie.rcos.rcdc.rco.module.def.sms.dto.SmsPwdRecoverDTO;
import com.ruijie.rcos.rcdc.rco.module.def.sms.dto.SmsPwdRecoverNotifyDTO;
import com.ruijie.rcos.rcdc.rco.module.def.sms.enums.MessagePlatformType;
import com.ruijie.rcos.rcdc.rco.module.def.sms.enums.MessageStatus;
import com.ruijie.rcos.rcdc.rco.module.def.sms.enums.SmsPlatformType;
import com.ruijie.rcos.rcdc.rco.module.def.sms.request.SmsSendRequest;
import com.ruijie.rcos.rcdc.rco.module.def.utils.SmsConverterUtils;
import com.ruijie.rcos.rcdc.rco.module.impl.diskpool.DiskPoolBusinessKey;
import com.ruijie.rcos.rcdc.rco.module.impl.sms.SmsBusinessKey;
import com.ruijie.rcos.rcdc.rco.module.impl.sms.dao.ExternalMessageLogDAO;
import com.ruijie.rcos.rcdc.rco.module.impl.sms.entity.ExternalMessageLogEntity;
import com.ruijie.rcos.rcdc.rco.module.impl.sms.service.SmsManageService;
import com.ruijie.rcos.rcdc.rco.module.impl.spi.common.ShineMessageHandler;
import com.ruijie.rcos.rcdc.terminal.module.def.api.CbbTerminalOperatorAPI;
import com.ruijie.rcos.sk.base.exception.BusinessException;
import com.ruijie.rcos.sk.base.i18n.LocaleI18nResolver;
import com.ruijie.rcos.sk.base.log.Logger;
import com.ruijie.rcos.sk.base.log.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;
import org.springframework.util.CollectionUtils;

import java.util.Date;
import java.util.List;

import static com.ruijie.rcos.rcdc.rco.module.impl.sms.SmsBusinessKey.RCDC_RCO_SMS_SEND_FAIL_ERROR;

/**
 * Description: SmsHttpManageServiceImpl
 * Copyright: Copyright (c) 2023
 * Company: Ruijie Co., Ltd.
 * Create Time: 2023/6/8
 *
 * @author TD
 */
@Service
public class SmsHttpManageServiceImpl implements SmsManageService {
    
    private static final Logger LOGGER = LoggerFactory.getLogger(SmsHttpManageServiceImpl.class);
    
    @Autowired
    private ExternalMessageLogDAO externalMessageLogDAO;

    @Autowired
    private CbbTerminalOperatorAPI cbbTerminalOperatorAPI;

    @Autowired
    private ShineMessageHandler shineMessageHandler;

    @Autowired
    private BaseSmsNotifyAPI baseSmsNotifyAPI;

    @Override
    public void sendSmsMessage(SmsSendRequest request) throws BusinessException {
        Assert.notNull(request, "sendSmsMessage request can be not null");
        ExternalMessageLogEntity entity = buildMessageNoticeEntity(request);
        try {
            baseSmsNotifyAPI.sendSms(request.getPhone(), request.getSmsContent());
        } catch (Exception e) {
            LOGGER.error("发送短信出现异常：", e);
            entity.setStatus(MessageStatus.FAIL);
            StringBuilder sb = new StringBuilder();
            if (e instanceof BusinessException) {
                sb.append(((BusinessException) e).getI18nMessage());
                entity.setFailMsg(sb.toString());
                throw new BusinessException(RCDC_RCO_SMS_SEND_FAIL_ERROR, e, sb.toString());
            }
            sb.append(LocaleI18nResolver.resolve(DiskPoolBusinessKey.RCDC_RCO_SYSTEM_ERROR));
            entity.setFailMsg(sb.toString());
            throw e;
        } finally {
            externalMessageLogDAO.save(entity);
        }
    }

    @Override
    public void notifyOnlineClient(SmsPwdRecoverDTO pwdRecoverDTO) {
        Assert.notNull(pwdRecoverDTO, "pwdRecoverDTO can be not null");
        SmsPwdRecoverNotifyDTO notifyDTO = new SmsPwdRecoverNotifyDTO(pwdRecoverDTO.getEnable()
                , pwdRecoverDTO.getInterval(), pwdRecoverDTO.getPeriod());
        // 通知在线终端
        List<String> terminalList = cbbTerminalOperatorAPI.getOnlineTerminalIdList();
        if (CollectionUtils.isEmpty(terminalList)) {
            return;
        }
        LOGGER.info("准备通知在线终端消息，[{}]终端在线数：{}", JSON.toJSONString(notifyDTO), terminalList.size());
        terminalList.parallelStream().forEach(terminalId -> {
            try {
                shineMessageHandler.requestContent(terminalId, SmsAndScanCodeCheckConstants.PUSH_SMS_RECOVER_PWD, notifyDTO);
            } catch (Exception e) {
                LOGGER.error("RCDC推送密码找回配置信息异常，终端ID：{}，e={}", terminalId, e);
            }
        });
    }

    private ExternalMessageLogEntity buildMessageNoticeEntity(SmsSendRequest request) {
        ExternalMessageLogEntity entity = new ExternalMessageLogEntity();
        entity.setRelatedType(request.getBusinessType());
        entity.setPlatformType(MessagePlatformType.SMS);
        entity.setRelatedTarget(request.getPhone());
        entity.setSendContent(request.getSmsContent());
        entity.setSendTime(new Date());
        entity.setStatus(MessageStatus.SUCCESS);
        return entity;
    }
}
