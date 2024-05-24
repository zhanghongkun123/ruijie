package com.ruijie.rcos.rcdc.rco.module.impl.tx.impl;

import com.ruijie.rcos.base.alarm.module.def.api.BaseAlarmAPI;
import com.ruijie.rcos.base.alarm.module.def.api.request.ReleaseExternalAlarmRequest;
import com.ruijie.rcos.base.alarm.module.def.api.request.SaveAlarmRequest;
import com.ruijie.rcos.base.alarm.module.def.enums.AlarmLevel;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.api.CbbPhysicalServerMgmtAPI;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.api.CbbVDIDeskMgmtAPI;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.dto.CbbAlarmDTO;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.enums.CbbResourceType;
import com.ruijie.rcos.rcdc.rco.module.impl.Constants;
import com.ruijie.rcos.rcdc.rco.module.impl.service.GlobalParameterService;
import com.ruijie.rcos.sk.base.exception.BusinessException;
import com.ruijie.rcos.sk.base.i18n.LocaleI18nResolver;
import com.ruijie.rcos.sk.base.junit.SkyEngineRunner;
import mockit.*;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.UUID;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

/**
 * Description: Function Description
 * Copyright: Copyright (c) 2020
 * Company: Ruijie Co., Ltd.
 * Create Time: 2020/4/23
 *
 * @author XiaoJiaXin
 */
@RunWith(SkyEngineRunner.class)
public class AlarmServiceTxImplTest {

    @Tested
    private AlarmServiceTxImpl alarmServiceTx;

    @Injectable
    private BaseAlarmAPI baseAlarmAPI;

    @Injectable
    private CbbPhysicalServerMgmtAPI cbbPhysicalServerMgmtAPI;

    @Injectable
    private GlobalParameterService globalParameterService;

    @Injectable
    private CbbVDIDeskMgmtAPI cbbDeskMgmtAPI;

    @Mocked
    private LocaleI18nResolver localeI18nResolver;

    /**
     * 测试resume
     * @throws BusinessException 业务异常
     */
    @Test
    public void testSaveAlarmAndRecordResumeTrue() throws BusinessException {
        CbbAlarmDTO alarmDTO = new CbbAlarmDTO();
        alarmDTO.setAlarmContent("");
        alarmDTO.setAlarmLevel(AlarmLevel.TIPS);
        alarmDTO.setAlarmTime(123123L);
        alarmDTO.setBusinessId(UUID.randomUUID().toString());
        alarmDTO.setId(Long.valueOf(12312));
        alarmDTO.setAlarmContent("content");
        alarmDTO.setResourceType(CbbResourceType.DESKTOP);
        alarmDTO.setResume(true);

        new Expectations() {
            {
                baseAlarmAPI.releaseExternalAlarm((ReleaseExternalAlarmRequest) any);
            }
        };
        alarmServiceTx.saveAlarmAndRecord(alarmDTO);
        new Verifications() {
            {
                globalParameterService.updateParameter(Constants.GLOBAL_PARAMETER_KEY_ALARM_RECORD, anyString);
                times = 1;
            }
        };
    }

    /**
     * 测试resume  false
     * @throws BusinessException 业务异常
     */
    @Test
    public void testSaveAlarmAndRecordResumefalse() throws BusinessException {
        CbbAlarmDTO alarmDTO = new CbbAlarmDTO();
        alarmDTO.setAlarmContent("");
        alarmDTO.setAlarmLevel(AlarmLevel.TIPS);
        alarmDTO.setAlarmTime(123123L);
        alarmDTO.setBusinessId(UUID.randomUUID().toString());
        alarmDTO.setId(Long.valueOf(12312));
        alarmDTO.setAlarmContent("content");
        alarmDTO.setResourceType(CbbResourceType.DESKTOP);
        alarmDTO.setResume(false);

        new Expectations() {
            {
                baseAlarmAPI.saveAlarm((SaveAlarmRequest) any);
            }
        };
        alarmServiceTx.saveAlarmAndRecord(alarmDTO);
        new Verifications() {
            {
                globalParameterService.updateParameter(Constants.GLOBAL_PARAMETER_KEY_ALARM_RECORD, anyString);
                times = 1;
            }
        };
    }

    /**
     * 测试resume  false PHYSICAL_SERVER
     * @throws BusinessException 业务异常
     */
    @Test
    public void testSaveAlarmAndRecordResumefalsePhysicalServer() throws BusinessException {
        CbbAlarmDTO alarmDTO = new CbbAlarmDTO();
        alarmDTO.setAlarmContent("");
        alarmDTO.setAlarmLevel(AlarmLevel.TIPS);
        alarmDTO.setAlarmTime(123123L);
        alarmDTO.setBusinessId(UUID.randomUUID().toString());
        alarmDTO.setId(Long.valueOf(12312));
        alarmDTO.setAlarmContent("content");
        alarmDTO.setResourceType(CbbResourceType.PHYSICAL_SERVER);
        alarmDTO.setResume(false);

        new Expectations() {
            {
                baseAlarmAPI.saveAlarm((SaveAlarmRequest) any);
            }
        };
        alarmServiceTx.saveAlarmAndRecord(alarmDTO);
        new Verifications() {
            {
                globalParameterService.updateParameter(Constants.GLOBAL_PARAMETER_KEY_ALARM_RECORD, anyString);
                times = 1;
            }
        };
    }

    /**
     * 测试resume  false SERVICES
     * @throws BusinessException 业务异常
     */
    @Test
    public void testSaveAlarmAndRecordResumefalseServices() throws BusinessException {
        CbbAlarmDTO alarmDTO = new CbbAlarmDTO();
        alarmDTO.setAlarmContent("");
        alarmDTO.setAlarmLevel(AlarmLevel.TIPS);
        alarmDTO.setAlarmTime(123123L);
        alarmDTO.setBusinessId(UUID.randomUUID().toString());
        alarmDTO.setId(Long.valueOf(12312));
        alarmDTO.setAlarmContent("content");
        alarmDTO.setResourceType(CbbResourceType.SERVICES);
        alarmDTO.setResume(false);

        new Expectations() {
            {
                baseAlarmAPI.saveAlarm((SaveAlarmRequest) any);
            }
        };
        alarmServiceTx.saveAlarmAndRecord(alarmDTO);
        new Verifications() {
            {
                globalParameterService.updateParameter(Constants.GLOBAL_PARAMETER_KEY_ALARM_RECORD, anyString);
                times = 1;
            }
        };
    }

    /**
     * 测试resume  false STORAGE_SYSTEM
     * @throws BusinessException 业务异常
     */
    @Test
    public void testSaveAlarmAndRecordResumefalseStorageSystem() throws BusinessException {
        CbbAlarmDTO alarmDTO = new CbbAlarmDTO();
        alarmDTO.setAlarmContent("");
        alarmDTO.setAlarmLevel(AlarmLevel.TIPS);
        alarmDTO.setAlarmTime(123123L);
        alarmDTO.setBusinessId(UUID.randomUUID().toString());
        alarmDTO.setId(Long.valueOf(12312));
        alarmDTO.setAlarmContent("content");
        alarmDTO.setResourceType(CbbResourceType.STORAGE_SYSTEM);
        alarmDTO.setResume(false);

        new Expectations() {
            {
                baseAlarmAPI.saveAlarm((SaveAlarmRequest) any);
            }
        };
        alarmServiceTx.saveAlarmAndRecord(alarmDTO);
        new Verifications() {
            {
                globalParameterService.updateParameter(Constants.GLOBAL_PARAMETER_KEY_ALARM_RECORD, anyString);
                times = 1;
            }
        };
    }

    /**
     * 测试resume  error
     * @throws BusinessException 业务异常
     */
    @Test
    public void testSaveAlarmAndRecordResumefalseError() throws BusinessException {
        CbbAlarmDTO alarmDTO = new CbbAlarmDTO();
        alarmDTO.setAlarmContent("");
        alarmDTO.setAlarmLevel(AlarmLevel.TIPS);
        alarmDTO.setAlarmTime(123123L);
        alarmDTO.setBusinessId(UUID.randomUUID().toString());
        alarmDTO.setId(Long.valueOf(12312));
        alarmDTO.setAlarmContent("content");
        alarmDTO.setResourceType(CbbResourceType.IMAGE_TEMPLATE);
        alarmDTO.setResume(false);

        try {
            alarmServiceTx.saveAlarmAndRecord(alarmDTO);
            fail();
        } catch (Exception e) {
            assertEquals("illegal resource type:IMAGE_TEMPLATE", e.getMessage());
        }

    }
}
