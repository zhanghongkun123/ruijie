package com.ruijie.rcos.rcdc.rco.module.impl.conversion.data;

import java.util.List;
import java.util.UUID;

import org.junit.Test;
import org.junit.runner.RunWith;

import com.ruijie.rcos.rcdc.rco.module.def.api.dto.schedule.ScheduleDataDTO;
import com.ruijie.rcos.sk.base.exception.BusinessException;
import com.ruijie.rcos.sk.base.i18n.LocaleI18nResolver;
import com.ruijie.rcos.sk.base.junit.SkyEngineRunner;
import com.ruijie.rcos.sk.base.log.Logger;
import com.ruijie.rcos.sk.base.test.ThrowExceptionTester;
import com.ruijie.rcos.sk.webmvc.api.vo.GenericIdLabelEntry;
import com.ruijie.rcos.sk.webmvc.api.vo.IdLabelEntry;

import mockit.*;

/**
 * Description: Function Description
 * Copyright: Copyright (c) 2019
 * Company: Ruijie Co., Ltd.
 * Create Time: 2019年10月09日
 *
 * @author xgx
 */
@RunWith(SkyEngineRunner.class)
public class AbstractDataConversionTest {

    @Tested
    private TestAbstractDataConversion abstractDataConversion;

    @Mocked
    private Logger logger;

    /**
     * 测试转换方法
     * 
     * @throws Exception 异常
     */
    @Test
    public void testConversion() throws Exception {
        UUID[] idArr = new UUID[22];
        for (int i = 0; i < 22; i++) {
            idArr[i] = UUID.randomUUID();
        }
        new MockUp<TestAbstractDataConversion>() {
            @Mock
            UUID[] getIdArrayFromScheduleData(ScheduleDataDTO<UUID, String> scheduleDataDTO) {
                return idArr;
            }
        };
        ScheduleDataDTO<UUID, String> from = new ScheduleDataDTO<>();
        ScheduleDataDTO<IdLabelEntry, GenericIdLabelEntry<String>> to = new ScheduleDataDTO<>();
        ThrowExceptionTester.throwIllegalArgumentException(() -> abstractDataConversion.conversion(null, null), "from can not be null");
        ThrowExceptionTester.throwIllegalArgumentException(() -> abstractDataConversion.conversion(from, null), "to can not be null");

        abstractDataConversion.conversion(from, to);
        new Verifications() {
            {
                abstractDataConversion.obtainIdLabelEntryBy(any);
                times = 20;
                abstractDataConversion.setDataToScheduleDataDTO(to, (List<IdLabelEntry>) any);
                times = 1;
            }
        };
    }

    /**
     * 测试转换方法当异常时
     * 
     * @throws Exception 异常
     */
    @Test
    public void testConversionWhileThrowBusinessException() throws Exception {
        UUID id = UUID.randomUUID();

        ScheduleDataDTO<UUID, String> from = new ScheduleDataDTO<>();
        ScheduleDataDTO<IdLabelEntry, GenericIdLabelEntry<String>> to = new ScheduleDataDTO<>();
        new MockUp<LocaleI18nResolver>() {
            @Mock
            public String resolve(String key, String... args) {
                return "xx";
            }
        };
        BusinessException e = new BusinessException("x");
        new Expectations(abstractDataConversion) {
            {
                abstractDataConversion.getIdArrayFromScheduleData(from);
                result = new UUID[] {id};
                abstractDataConversion.obtainIdLabelEntryBy(id);
                result = e;
            }
        };
        abstractDataConversion.conversion(from, to);
        new Verifications() {
            {
                abstractDataConversion.obtainIdLabelEntryBy(id);
                times = 1;
                abstractDataConversion.setDataToScheduleDataDTO(to, (List<IdLabelEntry>) any);
                times = 1;
//                logger.error("查询ID为{}的基本信息发生异常{}", id, e.getI18nMessage());
//                times = 1;
            }
        };
    }

    /**
     * 测试类
     */
    public static class TestAbstractDataConversion extends AbstractDataConversion {

        @Override
        UUID[] getIdArrayFromScheduleData(ScheduleDataDTO<UUID, String> scheduleDataDTO) {
            return new UUID[0];
        }

        @Override
        void setDataToScheduleDataDTO(ScheduleDataDTO<IdLabelEntry, GenericIdLabelEntry<String>> to, List<IdLabelEntry> idLabelEntryList) {

        }

        @Override
        IdLabelEntry obtainIdLabelEntryBy(Object id) throws BusinessException {
            //
            return null;
        }

        @Override
        public boolean isSupport(ScheduleDataDTO<UUID, String> from) {
            return false;
        }
    }
}
