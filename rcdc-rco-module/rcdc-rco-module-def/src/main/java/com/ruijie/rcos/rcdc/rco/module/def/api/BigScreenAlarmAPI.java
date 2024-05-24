package com.ruijie.rcos.rcdc.rco.module.def.api;

import com.ruijie.rcos.rcdc.rco.module.def.api.request.bigscreen.GetAlarmCountRequest;
import com.ruijie.rcos.rcdc.rco.module.def.api.request.bigscreen.GetAlarmCountSumRequest;
import com.ruijie.rcos.rcdc.rco.module.def.api.request.bigscreen.ListAlarmDetailRequest;
import com.ruijie.rcos.rcdc.rco.module.def.api.response.bigscreen.AlarmCountResponse;
import com.ruijie.rcos.rcdc.rco.module.def.api.response.bigscreen.AlarmCountSumResponse;
import com.ruijie.rcos.rcdc.rco.module.def.api.response.bigscreen.AlarmDetailResponse;
import com.ruijie.rcos.sk.base.exception.BusinessException;


/**
 * 告警管理API接口
 * Copyright: Copyright (c) 2018
 * Company: Ruijie Co., Ltd.
 * Create Time: 2019年8月10日
 * @author brq
 */
public interface BigScreenAlarmAPI {

    /**
     * 每日告警数量查询
     * @param request 请求
     * @return 返回
     */

    AlarmCountResponse getAlarmCount(GetAlarmCountRequest request);

    /**
     * 时段内告警总数查询
     * @param request 请求
     * @return 返回
     * @throws BusinessException 业务异常
     */

    AlarmCountSumResponse getAlarmCountSum(GetAlarmCountSumRequest request) throws BusinessException;

    /**
     * 告警详情列表
     * @param request 请求
     * @return 返回
     * @throws BusinessException 业务异常
     */
    
    AlarmDetailResponse listAlarmDetail(ListAlarmDetailRequest request) throws BusinessException;
}
