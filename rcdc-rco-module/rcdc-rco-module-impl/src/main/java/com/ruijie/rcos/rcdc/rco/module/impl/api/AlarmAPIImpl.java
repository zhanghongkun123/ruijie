package com.ruijie.rcos.rcdc.rco.module.impl.api;

import java.util.Calendar;
import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.Assert;

import com.ruijie.rcos.base.alarm.module.def.api.BaseAlarmAPI;
import com.ruijie.rcos.base.alarm.module.def.api.request.ListAlarmRequest;
import com.ruijie.rcos.base.alarm.module.def.dto.AlarmDTO;
import com.ruijie.rcos.rcdc.rco.module.def.api.AlarmAPI;
import com.ruijie.rcos.rcdc.rco.module.def.api.response.AlarmWebResponse;
import com.ruijie.rcos.sk.base.log.Logger;
import com.ruijie.rcos.sk.base.log.LoggerFactory;
import com.ruijie.rcos.sk.modulekit.api.comm.DefaultPageResponse;
import com.ruijie.rcos.sk.webmvc.api.request.PageWebRequest;

/**
 * Description: Function Description
 * Copyright: Copyright (c) 2019
 * Company: Ruijie Co., Ltd.
 * Create Time: 2019/8/10 12:07
 *
 * @author zqj
 */
public class AlarmAPIImpl implements AlarmAPI {

    private static final Logger LOGGER = LoggerFactory.getLogger(AlarmAPIImpl.class);

    @Autowired
    private BaseAlarmAPI baseAlarmAPI;

    @Override
    public DefaultPageResponse<AlarmWebResponse> pageQuery(PageWebRequest request) {
        Assert.notNull(request, "PageWebRequest不能为null");
        ListAlarmRequest alarmRequest = new ListAlarmRequest();
        alarmRequest.setPage(request.getPage());
        alarmRequest.setLimit(request.getLimit());
        alarmRequest.setEnableQueryHistory(false);
        // 查最近一个月的告警
        alarmRequest.setStartTime(getMonthAgoDate());

        DefaultPageResponse<AlarmDTO> response = baseAlarmAPI.listAlarmList(alarmRequest);
        return buildAlarmResponse(response);
    }

    private Date getMonthAgoDate() {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(new Date());
        calendar.add(Calendar.MONTH, -1);
        return calendar.getTime();
    }

    private DefaultPageResponse<AlarmWebResponse> buildAlarmResponse(DefaultPageResponse<AlarmDTO> response) {
        AlarmDTO[] alarmArr = response.getItemArr();
        long total = response.getTotal();
        DefaultPageResponse<AlarmWebResponse> webResponse = new DefaultPageResponse<>();
        if (total == 0) {
            return webResponse;
        }
        AlarmWebResponse[] alarmWebResponseArr = new AlarmWebResponse[alarmArr.length];
        for (int i = 0; i < alarmArr.length; i++) {
            AlarmDTO alarm = alarmArr[i];
            AlarmWebResponse target = new AlarmWebResponse();
            target.setId(alarm.getId());
            target.setContent(alarm.getAlarmContent());
            target.setAlarmLevel(alarm.getAlarmLevel());
            target.setLastAlarmTime(alarm.getLastAlarmTime());
            alarmWebResponseArr[i] = target;
        }
        webResponse.setTotal(total);
        webResponse.setItemArr(alarmWebResponseArr);
        return webResponse;
    }
}
