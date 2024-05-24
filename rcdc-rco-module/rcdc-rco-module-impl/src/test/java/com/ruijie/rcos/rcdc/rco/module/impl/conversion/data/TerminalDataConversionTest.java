package com.ruijie.rcos.rcdc.rco.module.impl.conversion.data;

import com.ruijie.rcos.rcdc.rco.module.def.api.dto.schedule.ScheduleDataDTO;
import com.ruijie.rcos.rcdc.terminal.module.def.api.CbbTerminalOperatorAPI;
import com.ruijie.rcos.rcdc.terminal.module.def.api.dto.CbbTerminalBasicInfoDTO;
import com.ruijie.rcos.sk.base.exception.BusinessException;
import com.ruijie.rcos.sk.base.i18n.LocaleI18nResolver;
import com.ruijie.rcos.sk.base.junit.SkyEngineRunner;
import com.ruijie.rcos.sk.base.log.Logger;
import com.ruijie.rcos.sk.base.test.ThrowExceptionTester;
import com.ruijie.rcos.sk.webmvc.api.vo.GenericIdLabelEntry;
import com.ruijie.rcos.sk.webmvc.api.vo.IdLabelEntry;
import mockit.*;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.UUID;

/**
 * Description: Function Description
 * Copyright: Copyright (c) 2019
 * Company: Ruijie Co., Ltd.
 * Create Time: 2019年10月09日
 *
 * @author xgx
 */
@RunWith(SkyEngineRunner.class)
public class TerminalDataConversionTest {

    @Tested
    private TerminalDataConversion terminalDataConversion;

    @Injectable
    private CbbTerminalOperatorAPI cbbTerminalOperatorAPI;

    @Mocked
    private Logger logger;

    /**
     * 测试转换方法
     *
     * @throws Exception 异常
     */
    @Test
    public void testConversion() throws Exception {
        CbbTerminalBasicInfoDTO cbbTerminalBasicInfoResponse = new CbbTerminalBasicInfoDTO();
        String[] idArr = new String[22];
        for (int i = 0; i < 22; i++) {
            idArr[i] = String.valueOf(i);
        }

        new Expectations() {
            {
                cbbTerminalOperatorAPI.findBasicInfoByTerminalId(anyString);
                result = cbbTerminalBasicInfoResponse;
            }
        };
        ScheduleDataDTO<UUID, String> from = new ScheduleDataDTO<>();
        from.setTerminalArr(idArr);
        ScheduleDataDTO<IdLabelEntry, GenericIdLabelEntry<String>> to = new ScheduleDataDTO<>();
        ThrowExceptionTester.throwIllegalArgumentException(() -> terminalDataConversion.conversion(null, null), "from can not be null");
        ThrowExceptionTester.throwIllegalArgumentException(() -> terminalDataConversion.conversion(from, null), "to can not be null");

        terminalDataConversion.conversion(from, to);
        new Verifications() {
            {
                cbbTerminalOperatorAPI.findBasicInfoByTerminalId(anyString);
                times = 20;

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

        ScheduleDataDTO<UUID, String> from = new ScheduleDataDTO<>();
        from.setTerminalArr(new String[]{"1"});
        ScheduleDataDTO<IdLabelEntry, GenericIdLabelEntry<String>> to = new ScheduleDataDTO<>();
        new MockUp<LocaleI18nResolver>() {
            @Mock
            public String resolve(String key, String... args) {
                return "xx";
            }
        };
        BusinessException e = new BusinessException("x");
        new Expectations(terminalDataConversion) {
            {
                cbbTerminalOperatorAPI.findBasicInfoByTerminalId(anyString);
                result = e;
            }
        };
        terminalDataConversion.conversion(from, to);
        new Verifications() {
            {
                cbbTerminalOperatorAPI.findBasicInfoByTerminalId(anyString);
                times = 1;
//                logger.error("查询终端ID为{}的基本信息发生异常{}", "1", e.getI18nMessage());
//
//                times = 1;
            }
        };
    }

    /**
     * 测试是否支持方法
     * @throws Exception 异常
     */
    @Test
    public void testIsSupport() throws Exception {
        ScheduleDataDTO<UUID, String> from = new ScheduleDataDTO<>();
        ThrowExceptionTester.throwIllegalArgumentException(() -> terminalDataConversion.isSupport(null), "from can not be null");
        Assert.assertFalse(terminalDataConversion.isSupport(from));
        from.setTerminalArr(new String[] {"1"});
        Assert.assertTrue(terminalDataConversion.isSupport(from));

    }

}