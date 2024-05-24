package com.ruijie.rcos.rcdc.rco.module.impl.tx.impl;

import com.alibaba.fastjson.JSON;
import com.ruijie.rcos.base.alarm.module.def.api.BaseAlarmAPI;
import com.ruijie.rcos.base.alarm.module.def.api.request.ReleaseExternalAlarmRequest;
import com.ruijie.rcos.base.alarm.module.def.api.request.SaveAlarmRequest;
import com.ruijie.rcos.base.alarm.module.def.enums.AlarmStatus;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.api.CbbPhysicalServerMgmtAPI;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.api.CbbVDIDeskMgmtAPI;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.constants.AlarmConstants;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.dto.CbbAlarmDTO;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.dto.CbbDeskDTO;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.dto.PhysicalServerDTO;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.enums.CbbResourceType;
import com.ruijie.rcos.rcdc.rco.module.impl.BusinessKey;
import com.ruijie.rcos.rcdc.rco.module.impl.Constants;
import com.ruijie.rcos.rcdc.rco.module.impl.dto.AlarmSyncRecordDTO;
import com.ruijie.rcos.rcdc.rco.module.impl.service.GlobalParameterService;
import com.ruijie.rcos.rcdc.rco.module.impl.tx.AlarmServiceTx;
import com.ruijie.rcos.sk.base.crypto.Md5Builder;
import com.ruijie.rcos.sk.base.exception.BusinessException;
import com.ruijie.rcos.sk.base.i18n.LocaleI18nResolver;
import com.ruijie.rcos.sk.base.util.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import java.util.Date;
import java.util.UUID;

/**
 * Description:
 * Copyright: Copyright (c) 2019
 * Company: Ruijie Co., Ltd.
 * Create Time: 2020/4/8
 *
 * @author nt
 */
@Service
public class AlarmServiceTxImpl implements AlarmServiceTx {

    @Autowired
    private GlobalParameterService globalParameterService;

    @Autowired
    private BaseAlarmAPI baseAlarmAPI;

    @Autowired
    private CbbPhysicalServerMgmtAPI cbbPhysicalServerMgmtAPI;

    @Autowired
    private CbbVDIDeskMgmtAPI cbbDeskMgmtAPI;

    @Override
    public void saveAlarmAndRecord(CbbAlarmDTO alarmDTO) throws BusinessException {
        Assert.notNull(alarmDTO, "alarmDTO can not be null");

        Boolean isResume = alarmDTO.getResume();
        if (Boolean.TRUE.equals(isResume)) {
            // 恢复
            baseAlarmAPI.releaseExternalAlarm(buildReleaseAlarmRequest(alarmDTO));
        } else {
            // 保存
            baseAlarmAPI.saveAlarm(buildSaveAlarmRequest(alarmDTO));
        }
        // 更新同步记录
        globalParameterService.updateParameter(Constants.GLOBAL_PARAMETER_KEY_ALARM_RECORD, buildAlarmSyncRecordDTO(alarmDTO));

    }

    private ReleaseExternalAlarmRequest buildReleaseAlarmRequest(CbbAlarmDTO alarmDTO) {
        ReleaseExternalAlarmRequest releaseExternalAlarmRequest = new ReleaseExternalAlarmRequest();
        releaseExternalAlarmRequest.setAlarmStatus(AlarmStatus.AUTO_RELEASED);
        releaseExternalAlarmRequest.setAlarmCode(buildAlarmCode(alarmDTO));
        releaseExternalAlarmRequest.setAlarmType(buildAlarmType(alarmDTO));

        return releaseExternalAlarmRequest;
    }


    private SaveAlarmRequest buildSaveAlarmRequest(CbbAlarmDTO alarmDTO) throws BusinessException {
        SaveAlarmRequest request = new SaveAlarmRequest();
        request.setAlarmNameByI18nKey(getAlarmNameKeyByResourceType(alarmDTO.getResourceType()));
        request.setAlarmCode(buildAlarmCode(alarmDTO));

        request.setAlarmType(buildAlarmType(alarmDTO));
        request.setAlarmContent(buildAlarmContent(alarmDTO));
        request.setAlarmLevel(alarmDTO.getAlarmLevel());
        request.setAlarmTime(new Date(alarmDTO.getAlarmTime()));
        request.setEnableSendMail(true);
        request.setBusinessId(alarmDTO.getBusinessId());

        return request;
    }

    private String buildAlarmType(CbbAlarmDTO alarmDTO) {
        CbbResourceType resourceType = alarmDTO.getResourceType();
        switch (resourceType) {
            case PHYSICAL_SERVER:
                return AlarmConstants.ALARM_TYPE_SERVER;
            case SERVICES:
                return AlarmConstants.ALARM_TYPE_RCCP_SERVICE;
            case DESKTOP:
                return AlarmConstants.ALARM_TYPE_DESKTOP;
            case STORAGE_SYSTEM:
                return AlarmConstants.ALARM_TYPE_STORAGE;
            default:
                throw new IllegalStateException("illegal resource type:" + resourceType);
        }

    }

    private String buildAlarmCode(CbbAlarmDTO alarmDTO) {
        // 告警返回信息中没有明确方式区分alarmCode,这里基于资源类型，id和告警内容的md5做alarmCode
        StringBuilder sb = new StringBuilder();
        sb.append(alarmDTO.getResourceType()).append(alarmDTO.getBusinessId()).append(alarmDTO.getAlarmContent());
        byte[] byteArr = sb.toString().getBytes();
        byte[] md5ByteArr = new Md5Builder().update(byteArr, 0, byteArr.length).build();
        // 携带上resourceType，用以区分告警类型
        return alarmDTO.getResourceType() + "|" + StringUtils.bytes2Hex(md5ByteArr);
    }

    private String buildAlarmContent(CbbAlarmDTO alarmDTO) throws BusinessException {
        CbbResourceType resourceType = alarmDTO.getResourceType();
        if (CbbResourceType.PHYSICAL_SERVER == resourceType) {
            return buildServerAlarmContent(alarmDTO);
        } else if (CbbResourceType.DESKTOP == resourceType) {
            return buildDesktopAlarmContent(alarmDTO);
        }
        return alarmDTO.getAlarmContent();
    }

    private String buildServerAlarmContent(CbbAlarmDTO alarmDTO) throws BusinessException {
        UUID uuid = UUID.fromString(alarmDTO.getBusinessId());
        // 服务器
        PhysicalServerDTO physicalServerDTO = cbbPhysicalServerMgmtAPI.getPhysicalServer(uuid);
        if (physicalServerDTO == null) {
            return alarmDTO.getAlarmContent();
        }
        return decorateAlarmContent(LocaleI18nResolver.resolve(BusinessKey.RCDC_RCO_ALARM_RESOURCE_NAME_PHYSICAL_SERVER),
                physicalServerDTO.getHostName(), alarmDTO.getAlarmContent());
    }


    private String buildDesktopAlarmContent(CbbAlarmDTO alarmDTO) throws BusinessException {
        UUID uuid = UUID.fromString(alarmDTO.getBusinessId());
        // 桌面
        CbbDeskDTO cbbDeskDTO = cbbDeskMgmtAPI.getDeskVDI(uuid);
        if (cbbDeskDTO == null) {
            return alarmDTO.getAlarmContent();
        }
        return decorateAlarmContent(LocaleI18nResolver.resolve(BusinessKey.RCDC_RCO_ALARM_RESOURCE_NAME_DESKTOP), cbbDeskDTO.getName(),
                alarmDTO.getAlarmContent());
    }

    private String decorateAlarmContent(String resolve, String hostName, String content) {
        return resolve + "[" + hostName + "]" + content;
    }

    private String getAlarmNameKeyByResourceType(CbbResourceType resourceType) {
        switch (resourceType) {
            case PHYSICAL_SERVER:
                return BusinessKey.RCDC_RCO_ALARM_RESOURCE_NAME_PHYSICAL_SERVER;
            case SERVICES:
                return BusinessKey.RCDC_RCO_ALARM_RESOURCE_NAME_SERVICE;
            case DESKTOP:
                return BusinessKey.RCDC_RCO_ALARM_RESOURCE_NAME_DESKTOP;
            case STORAGE_SYSTEM:
                return BusinessKey.RCDC_RCO_ALARM_RESOURCE_NAME_STORAGE;
            default:
                throw new IllegalStateException("illegal resource type:" + resourceType);
        }

    }

    private String buildAlarmSyncRecordDTO(CbbAlarmDTO alarmDTO) {
        AlarmSyncRecordDTO alarmSyncRecordDTO = new AlarmSyncRecordDTO();
        alarmSyncRecordDTO.setId(alarmDTO.getId());
        alarmSyncRecordDTO.setAlarmTime(alarmDTO.getAlarmTime());

        return JSON.toJSONString(alarmSyncRecordDTO);
    }
}
