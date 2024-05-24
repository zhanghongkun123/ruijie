package com.ruijie.rcos.rcdc.rco.module.impl.api;

import com.google.common.collect.Lists;
import com.ruijie.rcos.base.alarm.module.def.api.BaseAlarmAPI;
import com.ruijie.rcos.base.alarm.module.def.api.request.ListAlarmRequest;
import com.ruijie.rcos.base.alarm.module.def.dto.AlarmDTO;
import com.ruijie.rcos.rcdc.rco.module.def.api.BigScreenAlarmAPI;
import com.ruijie.rcos.rcdc.rco.module.def.api.dto.bigscreen.AlarmCountDTO;
import com.ruijie.rcos.rcdc.rco.module.def.api.dto.bigscreen.AlarmCountSumDTO;
import com.ruijie.rcos.rcdc.rco.module.def.api.dto.bigscreen.AlarmDetailDTO;
import com.ruijie.rcos.rcdc.rco.module.def.api.enums.AlarmTypeEnum;
import com.ruijie.rcos.rcdc.rco.module.def.api.request.bigscreen.GetAlarmCountRequest;
import com.ruijie.rcos.rcdc.rco.module.def.api.request.bigscreen.GetAlarmCountSumRequest;
import com.ruijie.rcos.rcdc.rco.module.def.api.request.bigscreen.ListAlarmDetailRequest;
import com.ruijie.rcos.rcdc.rco.module.def.api.response.bigscreen.AlarmCountResponse;
import com.ruijie.rcos.rcdc.rco.module.def.api.response.bigscreen.AlarmCountSumResponse;
import com.ruijie.rcos.rcdc.rco.module.def.api.response.bigscreen.AlarmDetailResponse;
import com.ruijie.rcos.rcdc.rco.module.impl.dao.AlarmCountDayDAO;
import com.ruijie.rcos.rcdc.rco.module.impl.entity.AlarmCountDayEntity;
import com.ruijie.rcos.sk.modulekit.api.comm.DefaultPageResponse;
import com.ruijie.rcos.sk.webmvc.api.vo.Sort;
import com.ruijie.rcos.sk.webmvc.api.vo.Sort.Direction;
import org.apache.commons.lang3.ArrayUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.Assert;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * Description: 告警管理API接口实现
 * Copyright: Copyright (c) 2018
 * Company: Ruijie Co., Ltd.
 * Create Time: 2019年4月10日
 *
 * @author brq
 */
public class BigScreenAlarmAPIImpl implements BigScreenAlarmAPI {

    private static final Logger LOGGER = LoggerFactory.getLogger(BigScreenAlarmAPIImpl.class);

    private static final Integer PAGE_INIT_VAL = 0;

    @Autowired
    private AlarmCountDayDAO alarmCountDayDAO;

    @Autowired
    private BaseAlarmAPI baseAlarmAPI;

    @Override
    public AlarmCountResponse getAlarmCount(GetAlarmCountRequest request) {
        Assert.notNull(request, "request is null");

        List<AlarmCountDayEntity> entityList = alarmCountDayDAO.findByAlarmTypeAndStatisticTimeBetween(
            request.getAlarmType(), request.getStartTime(), request.getEndTime());

        List<AlarmCountDTO> dtoList = entityList.stream().map(entity -> {
            AlarmCountDTO dto = new AlarmCountDTO();
            dto.setDate(entity.getStatisticTime());
            dto.setAlarmCount(entity.getAlarmCount());
            return dto;
        }).collect(Collectors.toList());

        AlarmCountResponse response = new AlarmCountResponse();
        response.setAlarmCountList(dtoList);
        return response;
    }

    @Override
    public AlarmCountSumResponse getAlarmCountSum(GetAlarmCountSumRequest request) {
        Assert.notNull(request, "request cannot be null!");

        List<AlarmCountSumDTO> alarmCountSumList = Lists.newArrayList();

        ListAlarmRequest apiRequest = bulidListAlarmRequest(AlarmTypeEnum.SERVER.getName());
        apiRequest.setStartTime(request.getStartTime());
        apiRequest.setEndTime(request.getEndTime());

        alarmCountSumList.add(getCountSumDto(apiRequest, AlarmTypeEnum.SERVER));

        apiRequest.setAlarmTypeArr(new String[]{AlarmTypeEnum.DESKTOP.getName()});
        alarmCountSumList.add(getCountSumDto(apiRequest, AlarmTypeEnum.DESKTOP));

        AlarmCountSumResponse response = new AlarmCountSumResponse();
        response.setAlarmCountSumList(alarmCountSumList);
        return response;
    }

    private AlarmCountSumDTO getCountSumDto(ListAlarmRequest request, AlarmTypeEnum alarmType) {
        AlarmCountSumDTO countSumDTO = new AlarmCountSumDTO(alarmType);
        DefaultPageResponse<AlarmDTO> apiResponse = baseAlarmAPI.listAlarmList(request);
        if (null == apiResponse) {
            LOGGER.error("未获取到[{}]告警信息", alarmType.name());
            return countSumDTO;
        }
        countSumDTO.setAlarmCountSum((int) apiResponse.getTotal());
        return countSumDTO;
    }

    @Override
    public AlarmDetailResponse listAlarmDetail(ListAlarmDetailRequest request) {
        Assert.notNull(request, "request cannot be null!");

        AlarmDetailResponse response = new AlarmDetailResponse();

        ListAlarmRequest apiRequest = bulidListAlarmRequest(request.getAlarmType().getName());
        apiRequest.setLimit(request.getLimit());

        DefaultPageResponse<AlarmDTO> apiResponse = baseAlarmAPI.listAlarmList(apiRequest);
        if (apiResponse == null || ArrayUtils.isEmpty(apiResponse.getItemArr())) {
            LOGGER.error("未获取到告警列表");
            return response;
        }

        List<AlarmDetailDTO> alarmDetailDtoList = Stream.of(apiResponse.getItemArr()).map(alarmDTO -> {
            AlarmDetailDTO alarmDetailDTO = new AlarmDetailDTO();
            alarmDetailDTO.setDate(alarmDTO.getLastAlarmTime());
            alarmDetailDTO.setAlarmType(request.getAlarmType());
            alarmDetailDTO.setAlarmContent(alarmDTO.getAlarmContent());
            return alarmDetailDTO;
        }).collect(Collectors.toList());
        response.getAlarmDetailList().addAll(alarmDetailDtoList);
        response.setTotal(apiResponse.getTotal());
        return response;
    }

    private ListAlarmRequest bulidListAlarmRequest(String alarmType) {
        Sort sort = new Sort();
        sort.setSortField("lastAlarmTime");
        sort.setDirection(Direction.DESC);

        ListAlarmRequest apiRequest = new ListAlarmRequest();
        apiRequest.setPage(PAGE_INIT_VAL);
        apiRequest.setSort(sort);
        apiRequest.setEnableQueryHistory(false);
        apiRequest.setAlarmTypeArr(new String[]{alarmType});

        return apiRequest;
    }
}
