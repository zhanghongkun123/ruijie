package com.ruijie.rcos.rcdc.rco.module.impl.api;

import com.google.common.collect.Lists;
import com.ruijie.rcos.base.alarm.module.def.api.BaseAlarmAPI;
import com.ruijie.rcos.base.alarm.module.def.api.request.ListAlarmRequest;
import com.ruijie.rcos.base.alarm.module.def.dto.AlarmDTO;
import com.ruijie.rcos.base.alarm.module.def.enums.AlarmType;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.enums.CbbResourceType;
import com.ruijie.rcos.rcdc.rco.module.def.api.enums.AlarmTypeEnum;
import com.ruijie.rcos.rcdc.rco.module.def.api.request.bigscreen.GetAlarmCountRequest;
import com.ruijie.rcos.rcdc.rco.module.def.api.request.bigscreen.GetAlarmCountSumRequest;
import com.ruijie.rcos.rcdc.rco.module.def.api.request.bigscreen.ListAlarmDetailRequest;
import com.ruijie.rcos.rcdc.rco.module.def.api.response.bigscreen.AlarmCountResponse;
import com.ruijie.rcos.rcdc.rco.module.def.api.response.bigscreen.AlarmDetailResponse;
import com.ruijie.rcos.rcdc.rco.module.impl.dao.AlarmCountDayDAO;
import com.ruijie.rcos.rcdc.rco.module.impl.entity.AlarmCountDayEntity;
import com.ruijie.rcos.sk.base.exception.BusinessException;
import com.ruijie.rcos.sk.modulekit.api.comm.DefaultPageResponse;
import mockit.Expectations;
import mockit.Injectable;
import mockit.Tested;
import mockit.Verifications;
import mockit.integration.junit4.JMockit;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.Date;
import java.util.List;
import java.util.UUID;

/**
 * Description: 告警管理API接口单元测试
 * Copyright: Copyright (c) 2019
 * Company: Ruijie Co., Ltd.
 * Create Time: 2019/8/1 17:07
 * @author brq
 */
@RunWith(JMockit.class)
public class AlarmAPIImplTest {

    @Tested
    private BigScreenAlarmAPIImpl bigScreenAlarmAPIImpl;

    @Injectable
    private AlarmCountDayDAO alarmCountDayDAO;

    @Injectable
    private BaseAlarmAPI baseAlarmAPI;

    /**
     * 每日告警数量查询
     */
    @Test
    public void getAlarmCountTest() {
        GetAlarmCountRequest request = new GetAlarmCountRequest();
        List<AlarmCountDayEntity> entityList = Lists.newArrayList();
        AlarmCountDayEntity entity = new AlarmCountDayEntity();
        entity.setId(UUID.randomUUID());
        entity.setAlarmCount(3);
        entity.setCreateTime(new Date());
        entity.setAlarmType(AlarmTypeEnum.DESKTOP);
        entity.setStatisticTime(new Date());
        entity.setVersion(1);
        entityList.add(entity);
        new Expectations() {
            {
                alarmCountDayDAO
                    .findByAlarmTypeAndStatisticTimeBetween((AlarmTypeEnum) any, (Date) any, (Date) any);
                result = entityList;
            }
        };
        AlarmCountResponse response = bigScreenAlarmAPIImpl.getAlarmCount(request);

        new Verifications() {
            {
                alarmCountDayDAO
                    .findByAlarmTypeAndStatisticTimeBetween((AlarmTypeEnum) any, (Date) any, (Date) any);
                times = 1;
            }
        };
    }

    /**
     * 时段内告警总数查询，正常流程
     * @throws BusinessException 业务异常
     */
    @Test
    public void testGetAlarmCountSum() throws BusinessException {
        DefaultPageResponse<AlarmDTO> pageResponse = new DefaultPageResponse<>();
        pageResponse.setTotal(1L);

        new Expectations() {
            {
                baseAlarmAPI.listAlarmList((ListAlarmRequest) any);
                result = pageResponse;
            }
        };

        bigScreenAlarmAPIImpl.getAlarmCountSum(new GetAlarmCountSumRequest());

        new Verifications() {
            {
                baseAlarmAPI.listAlarmList((ListAlarmRequest) any);
                times = 2;
            }
        };
    }

    /**
     * 告警详情列表，正常流程
     * @throws BusinessException 业务异常
     */
    @Test
    public void listAlarmDetailTest() throws BusinessException {
        ListAlarmDetailRequest request = new ListAlarmDetailRequest();
        request.setAlarmType(AlarmTypeEnum.DESKTOP);
        request.setLimit(20);

        AlarmDTO[] dtoArr = new AlarmDTO[20];
        for (int i = 0; i < dtoArr.length; i++) {
            dtoArr[i] = new AlarmDTO();
            dtoArr[i].setAlarmType(AlarmType.CUSTOMER.toString());
            dtoArr[i].setAlarmCode(CbbResourceType.DESKTOP.name() + "xxx");
        }
        DefaultPageResponse<AlarmDTO> pageResponse = new DefaultPageResponse<>();
        pageResponse.setItemArr(dtoArr);

        new Expectations() {
            {
                baseAlarmAPI.listAlarmList((ListAlarmRequest) any);
                result = pageResponse;
            }
        };

        AlarmDetailResponse response = bigScreenAlarmAPIImpl.listAlarmDetail(request);
        Assert.assertEquals(20, response.getAlarmDetailList().size());
    }

}
