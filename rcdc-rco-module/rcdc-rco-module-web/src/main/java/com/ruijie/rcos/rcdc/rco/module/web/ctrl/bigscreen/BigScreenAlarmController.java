package com.ruijie.rcos.rcdc.rco.module.web.ctrl.bigscreen;

import com.ruijie.rcos.rcdc.rco.module.def.api.BigScreenAlarmAPI;
import com.ruijie.rcos.rcdc.rco.module.def.api.request.bigscreen.GetAlarmCountRequest;
import com.ruijie.rcos.rcdc.rco.module.def.api.request.bigscreen.GetAlarmCountSumRequest;
import com.ruijie.rcos.rcdc.rco.module.def.api.request.bigscreen.ListAlarmDetailRequest;
import com.ruijie.rcos.rcdc.rco.module.def.api.response.bigscreen.AlarmCountResponse;
import com.ruijie.rcos.rcdc.rco.module.def.api.response.bigscreen.AlarmCountSumResponse;
import com.ruijie.rcos.rcdc.rco.module.def.api.response.bigscreen.AlarmDetailResponse;
import com.ruijie.rcos.rcdc.rco.module.web.ctrl.bigscreen.request.bigscreen.GetAlarmCountWebRequest;
import com.ruijie.rcos.rcdc.rco.module.web.ctrl.bigscreen.request.bigscreen.ListAlarmDetailWebRequest;
import com.ruijie.rcos.rcdc.rco.module.web.ctrl.bigscreen.request.bigscreen.TimeWebRequest;
import com.ruijie.rcos.rcdc.rco.module.web.util.DateUtil;
import com.ruijie.rcos.sk.base.exception.BusinessException;
import com.ruijie.rcos.sk.webmvc.api.response.DefaultWebResponse;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.Calendar;
import java.util.Date;

/**
 * 告警管理Controller
 * Copyright: Copyright (c) 2018
 * Company: Ruijie Co., Ltd.
 * Create Time: 2019年8月10日
 * @author brq
 */
@Controller
@RequestMapping("/rco/alarm")
public class BigScreenAlarmController {

    /**
     * 告警详情列表默认长度
     */
    private static final Integer DEFAULT_ALARM_LIST_LENGTH = 20;

    private static final Integer DAFAULT_DAY_AMOUNT = -7;

    @Autowired
    private BigScreenAlarmAPI bigScreenAlarmAPI;

    /**
     * 每日告警数量查询
     *
     * @param request 请求
     * @return 返回
     * @throws BusinessException 业务异常
     */
    @RequestMapping(value = "/getAlarmCount")
    public DefaultWebResponse getAlarmCount(GetAlarmCountWebRequest request) throws BusinessException {
        Assert.notNull(request, "request is null");

        // 处理查询时间段，默认7天内
        processQueryInterval(request);
        GetAlarmCountRequest getAlarmCountRequest = new GetAlarmCountRequest();
        BeanUtils.copyProperties(request, getAlarmCountRequest);
        AlarmCountResponse response = bigScreenAlarmAPI.getAlarmCount(getAlarmCountRequest);
        return DefaultWebResponse.Builder.success(response);
    }

    /**
     * 时段内告警总数查询
     * @param request 请求
     * @return 返回
     * @throws BusinessException 业务异常
     */
    @RequestMapping("/getAlarmCountSum")
    public DefaultWebResponse getAlarmCountSum(TimeWebRequest request) throws BusinessException {
        Assert.notNull(request, "request cannot be null!");

        GetAlarmCountSumRequest getAlarmCountSumRequest = new GetAlarmCountSumRequest();
        BeanUtils.copyProperties(request, getAlarmCountSumRequest);
        AlarmCountSumResponse response = bigScreenAlarmAPI.getAlarmCountSum(getAlarmCountSumRequest);
        return DefaultWebResponse.Builder.success(response);
    }

    /**
     * 处理传入的起止时间
     * @param request 请求
     * @throws BusinessException 业务异常
     */
    private void processQueryInterval(TimeWebRequest request) throws BusinessException {
        if (request.getStartTime() == null || request.getEndTime() == null) {
            // 若未设置查询时段，默认过去一周
            Date endTime = new Date();
            request.setEndTime(endTime);
            request.setStartTime(DateUtil.computeDate(endTime, Calendar.DATE, DAFAULT_DAY_AMOUNT));
            return;
        }
        if (request.getStartTime().compareTo(request.getEndTime()) > 0) {
            // 若开始时间晚于结束时间，则抛出异常
            throw new BusinessException(BigScreenBussinessKey.RCDC_RCO_BIGSCREEN_START_TIME_LATER_THAN_END_TIME);
        }
    }

    /**
     * 告警详情列表
     * @param request 请求
     * @return 返回
     * @throws BusinessException 业务异常
     */
    @RequestMapping("/listAlarmDetail")
    public DefaultWebResponse listAlarmDetail(ListAlarmDetailWebRequest request) throws BusinessException {
        Assert.notNull(request, "request cannot be null!");

        ListAlarmDetailRequest apiRequest = new ListAlarmDetailRequest();
        apiRequest.setAlarmType(request.getAlarmType());
        if (request.getLimit() == null || request.getLimit() == 0) {
            apiRequest.setLimit(DEFAULT_ALARM_LIST_LENGTH);
        } else {
            apiRequest.setLimit(request.getLimit());
        }
        AlarmDetailResponse apiResponse = bigScreenAlarmAPI.listAlarmDetail(apiRequest);
        return DefaultWebResponse.Builder.success(apiResponse);
    }

}
