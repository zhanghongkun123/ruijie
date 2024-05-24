package com.ruijie.rcos.rcdc.rco.module.impl.quartz;

import com.google.common.collect.Lists;
import com.ruijie.rcos.base.alarm.module.def.api.BaseAlarmAPI;
import com.ruijie.rcos.base.alarm.module.def.api.request.ListAlarmRequest;
import com.ruijie.rcos.base.alarm.module.def.dto.AlarmDTO;
import com.ruijie.rcos.base.alarm.module.def.enums.AlarmType;
import com.ruijie.rcos.rcdc.rco.module.def.api.PlatformAPI;
import com.ruijie.rcos.rcdc.rco.module.def.api.enums.AlarmTypeEnum;
import com.ruijie.rcos.rcdc.rco.module.def.api.response.bigscreen.SystemTimeResponse;
import com.ruijie.rcos.rcdc.rco.module.impl.Constants;
import com.ruijie.rcos.rcdc.rco.module.impl.dao.AlarmCountDayDAO;
import com.ruijie.rcos.rcdc.rco.module.impl.entity.AlarmCountDayEntity;
import com.ruijie.rcos.sk.base.quartz.QuartzTaskContext;
import com.ruijie.rcos.sk.modulekit.api.comm.DefaultPageResponse;
import mockit.Expectations;
import mockit.Injectable;
import mockit.Tested;
import mockit.Verifications;
import mockit.integration.junit4.JMockit;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.Date;
import java.util.List;

/**
 * Description: 服务器/云桌面告警数定时任务单元测试
 * Copyright: Copyright (c) 2019
 * Company: Ruijie Co., Ltd.
 * Create Time: 2019/8/1 16:43
 *
 * @author zhangyichi
 */
@RunWith(JMockit.class)
public class AlarmCountStatisticQuartzTaskTest {

    @Tested
    private AlarmCountStatisticQuartzTask quartz;

    @Injectable
    private BaseAlarmAPI baseAlarmAPI;

    @Injectable
    private AlarmCountDayDAO alarmCountDayDAO;

    @Injectable
    private PlatformAPI platformAPI;

    @Injectable
    private QuartzTaskContext quartzTaskContext;

    /**
     * 告警数统计，正常流程
     * @throws Exception 异常
     */
    @Test
    public void executeTest() throws Exception {
        SystemTimeResponse timeResponse = new SystemTimeResponse();
        timeResponse.setSystemWorkTime(Constants.DEFAULT_RETRY_DAYS * Constants.ONE_DAY_MILLIS);

        List<AlarmCountDayEntity> deskTopAlarmCountList = Lists.newArrayList();

        AlarmDTO dto = new AlarmDTO();
        dto.setAlarmType(AlarmType.CUSTOMER.toString());
        dto.setAlarmCode("alarmCode");
        DefaultPageResponse<AlarmDTO> pageResponse = new DefaultPageResponse<>();
        pageResponse.setItemArr(new AlarmDTO[]{dto});
        new Expectations() {
            {
                platformAPI.getSystemTime();
                result = timeResponse;
                alarmCountDayDAO.findByAlarmTypeAndStatisticTimeBetween((AlarmTypeEnum)any, (Date)any, (Date)any);
                result = deskTopAlarmCountList;
                baseAlarmAPI.listAlarmList((ListAlarmRequest) any);
                result = pageResponse;
            }
        };

        quartz.execute(quartzTaskContext);

        new Verifications() {
            {
                platformAPI.getSystemTime();
                times = 2;
                alarmCountDayDAO.findByAlarmTypeAndStatisticTimeBetween((AlarmTypeEnum)any, (Date)any, (Date)any);
                times = 2;
                alarmCountDayDAO.save((AlarmCountDayEntity) any);
                times = 14;
            }
        };
    }

}
