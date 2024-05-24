package com.ruijie.rcos.rcdc.rco.module.impl.conversion.data;

import java.util.Arrays;
import java.util.List;
import java.util.UUID;

import org.apache.commons.lang3.reflect.FieldUtils;
import org.junit.Test;
import org.junit.runner.RunWith;

import com.ruijie.rcos.rcdc.rco.module.def.api.dto.schedule.ScheduleDataDTO;
import com.ruijie.rcos.sk.base.junit.SkyEngineRunner;
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
public class DataConversionChainTest {
    @Tested
    private DataConversionChain dataConversionChain;

    @Injectable
    private List<DataConversion> dataConversionList;

    /**
     * 测试转换方法当转换器不存在时
     * 
     * @param dataConversion 转换器
     */
    @Test
    public void testConversionWhileHasNoConversion(@Capturing DataConversion dataConversion) {
        ScheduleDataDTO<UUID, String> scheduleDataDTO = new ScheduleDataDTO<>();
        dataConversionChain.conversion(scheduleDataDTO);
        new Verifications() {
            {
                dataConversion.isSupport(scheduleDataDTO);
                times = 0;
            }
        };
    }

    /**
     * 测试转换方法
     * 
     * @param dataConversion1 转换器1
     * @param dataConversion2 转换器2
     * @throws IllegalAccessException 异常
     */
    @Test
    public void testConversion(@Injectable DataConversion dataConversion1, @Injectable DataConversion dataConversion2) throws IllegalAccessException {
        ScheduleDataDTO<UUID, String> scheduleDataDTO = new ScheduleDataDTO<>();

        List<DataConversion> dataConversionList = Arrays.asList(dataConversion1, dataConversion2);
        FieldUtils.writeField(dataConversionChain, "dataConversionList", dataConversionList, true);
        new Expectations() {
            {
                dataConversion1.isSupport(scheduleDataDTO);
                result = false;
                dataConversion2.isSupport(scheduleDataDTO);
                result = true;
            }
        };

        dataConversionChain.conversion(scheduleDataDTO);
        new Verifications() {
            {
                dataConversion2.conversion(scheduleDataDTO, (ScheduleDataDTO<IdLabelEntry, GenericIdLabelEntry<String>>) any);
                times = 1;
                dataConversion1.isSupport(scheduleDataDTO);
                times = 1;
                dataConversion2.isSupport(scheduleDataDTO);
                times = 1;
                dataConversion1.conversion(scheduleDataDTO, (ScheduleDataDTO<IdLabelEntry, GenericIdLabelEntry<String>>) any);
                times = 0;
            }
        };
    }

}
