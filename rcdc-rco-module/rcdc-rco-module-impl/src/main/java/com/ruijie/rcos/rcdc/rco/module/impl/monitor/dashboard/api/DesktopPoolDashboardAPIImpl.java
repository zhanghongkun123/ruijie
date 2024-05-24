package com.ruijie.rcos.rcdc.rco.module.impl.monitor.dashboard.api;

import com.alibaba.fastjson.JSON;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.enums.desktoppool.DesktopPoolType;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.enums.desktoppool.CbbDesktopPoolType;
import com.ruijie.rcos.rcdc.rco.module.def.BusinessKey;
import com.ruijie.rcos.rcdc.rco.module.def.api.DesktopPoolDashboardAPI;
import com.ruijie.rcos.rcdc.rco.module.def.monitor.dashboard.dto.DesktopSessionLogDTO;
import com.ruijie.rcos.rcdc.rco.module.def.monitor.dashboard.dto.StatisticDesktopPoolDesktopCountDTO;
import com.ruijie.rcos.rcdc.rco.module.def.monitor.dashboard.dto.UserConnectDesktopFaultLogDTO;
import com.ruijie.rcos.rcdc.rco.module.def.monitor.dashboard.request.CreateConnectFaultLogRequest;
import com.ruijie.rcos.rcdc.rco.module.def.monitor.dashboard.request.StatisticDesktopPoolHistoryInfoRequest;
import com.ruijie.rcos.rcdc.rco.module.def.monitor.dashboard.response.StatisticDesktopPoolHistoryResponse;
import com.ruijie.rcos.rcdc.rco.module.impl.dao.DesktopSessionLogDAO;
import com.ruijie.rcos.rcdc.rco.module.impl.dao.UserConnectDesktopFaultLogDAO;
import com.ruijie.rcos.rcdc.rco.module.impl.entity.DesktopSessionLogEntity;
import com.ruijie.rcos.rcdc.rco.module.impl.entity.UserConnectDesktopFaultLogEntity;
import com.ruijie.rcos.rcdc.rco.module.impl.monitor.dashboard.service.DesktopPoolDashboardService;
import com.ruijie.rcos.rcdc.rco.module.impl.monitor.dashboard.service.impl.DesktopPoolDashboardServiceImpl;
import com.ruijie.rcos.rcdc.rco.module.impl.monitor.dashboard.tx.DesktopPoolDashboardServiceTx;
import com.ruijie.rcos.sk.base.concurrent.ThreadExecutor;
import com.ruijie.rcos.sk.base.concurrent.ThreadExecutors;
import com.ruijie.rcos.sk.base.exception.BusinessException;
import com.ruijie.rcos.sk.base.log.Logger;
import com.ruijie.rcos.sk.base.log.LoggerFactory;
import com.ruijie.rcos.sk.pagekit.api.PageQueryRequest;
import com.ruijie.rcos.sk.pagekit.api.PageQueryResponse;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.lang.Nullable;
import org.springframework.util.Assert;

import java.util.Arrays;
import java.util.Date;
import java.util.Objects;
import java.util.UUID;

/**
 * Description: 桌面池报表相关API实现类
 * Copyright: Copyright (c) 2022
 * Company: Ruijie Co., Ltd.
 * Create Time: 2022/7/18 14:13
 *
 * @author yxq
 */
public class DesktopPoolDashboardAPIImpl implements DesktopPoolDashboardAPI {

    private static final ThreadExecutor THREAD_EXECUTOR =
            ThreadExecutors.newBuilder("updateUserGroupOrDesktopPoolName").maxThreadNum(3).queueSize(10).build();

    private static final Logger LOGGER = LoggerFactory.getLogger(DesktopPoolDashboardAPIImpl.class);

    @Autowired
    private DesktopPoolDashboardService desktopPoolDashboardService;

    @Autowired
    private DesktopSessionLogDAO desktopSessionLogDAO;

    @Autowired
    private UserConnectDesktopFaultLogDAO userConnectDesktopFaultLogDAO;

    @Autowired
    private DesktopPoolDashboardServiceTx desktopPoolDashboardServiceTx;

    @Override
    public StatisticDesktopPoolHistoryResponse statisticDesktopPoolUseHistory(StatisticDesktopPoolHistoryInfoRequest request)
            throws BusinessException {
        Assert.notNull(request, "request must not be null");

        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug("查询桌面池报表历史信息请求为：[{}]", JSON.toJSONString(request));
        }
        switch (request.getTimeQueryType()) {
            case DAY:
                return desktopPoolDashboardService.statisticByHourStep(request);
            case WEEK:
                return desktopPoolDashboardService.statisticByDayStep(request, DesktopPoolDashboardServiceImpl.ONE_WEEK_DAYS);
            case MONTHLY:
                return desktopPoolDashboardService.statisticByDayStep(request, DesktopPoolDashboardServiceImpl.ONE_MONTH_DAYS);
            default:
                throw new BusinessException(BusinessKey.RCDC_RCO_TIME_QUERY_TYPE_ONLY_SUPPORT_DAY_WEEK_MONTH);
        }

    }

    @Override
    public PageQueryResponse<DesktopSessionLogDTO> pageDesktopPoolConnectHistory(PageQueryRequest pageQueryRequest) throws BusinessException {
        Assert.notNull(pageQueryRequest, "pageQueryRequest must not be null");

        PageQueryResponse<DesktopSessionLogEntity> pageQueryResponse = desktopSessionLogDAO.pageQuery(pageQueryRequest);
        DesktopSessionLogDTO[] desktopSessionLogArr = Arrays.stream(pageQueryResponse.getItemArr()).map(entity -> {
            DesktopSessionLogDTO dto = new DesktopSessionLogDTO();
            BeanUtils.copyProperties(entity, dto);
            return dto;
        }).toArray(DesktopSessionLogDTO[]::new);

        PageQueryResponse<DesktopSessionLogDTO> response = new PageQueryResponse<>();
        response.setItemArr(desktopSessionLogArr);
        response.setTotal(pageQueryResponse.getTotal());

        return response;
    }

    @Override
    public PageQueryResponse<UserConnectDesktopFaultLogDTO> pageDesktopPoolConnectFailHistory(PageQueryRequest pageQueryRequest)
            throws BusinessException {
        Assert.notNull(pageQueryRequest, "pageQueryRequest must not be null");

        PageQueryResponse<UserConnectDesktopFaultLogEntity> pageQueryResponse = userConnectDesktopFaultLogDAO.pageQuery(pageQueryRequest);
        UserConnectDesktopFaultLogDTO[] desktopSessionLogArr = Arrays.stream(pageQueryResponse.getItemArr()).map(entity -> {
            UserConnectDesktopFaultLogDTO dto = new UserConnectDesktopFaultLogDTO();
            BeanUtils.copyProperties(entity, dto);
            return dto;
        }).toArray(UserConnectDesktopFaultLogDTO[]::new);

        PageQueryResponse<UserConnectDesktopFaultLogDTO> response = new PageQueryResponse<>();
        response.setItemArr(desktopSessionLogArr);
        response.setTotal(pageQueryResponse.getTotal());

        return response;
    }

    @Override
    public StatisticDesktopPoolDesktopCountDTO getCurrentDesktopPoolInfo(@Nullable CbbDesktopPoolType cbbDesktopPoolType,
                                                                         @Nullable DesktopPoolType desktopPoolType,
                                                                         @Nullable UUID deskPoolId) {
        if (Objects.nonNull(deskPoolId)) {
            // 查询指定某个桌面池
            return desktopPoolDashboardService.getCurrentDesktopPoolInfo(deskPoolId);
        }
        if (Objects.isNull(desktopPoolType) && Objects.isNull(cbbDesktopPoolType)) {
            // 查询全部桌面池的累加信息
            return desktopPoolDashboardService.getCurrentAllDesktopPoolInfo();
        }
        // 查询指定类型桌面池的累加信息
        return desktopPoolDashboardService.getCurrentAllDesktopPoolInfo(desktopPoolType, cbbDesktopPoolType);
    }

    @Override
    public void recordConnectFaultLog(CreateConnectFaultLogRequest request) {
        Assert.notNull(request, "request must not be null");

        UserConnectDesktopFaultLogEntity entity = new UserConnectDesktopFaultLogEntity();
        BeanUtils.copyProperties(request, entity);
        entity.setFaultTime(new Date());

        userConnectDesktopFaultLogDAO.save(entity);
    }

    @Override
    public void updateUserGroupName(UUID userGroupId, @Nullable String userGroupName) {
        Assert.notNull(userGroupId, "userGroupId must not be null");
        THREAD_EXECUTOR.execute(() -> desktopPoolDashboardServiceTx.updateUserGroupName(userGroupId, userGroupName));
    }

    @Override
    public void updateDesktopPoolName(UUID desktopPoolId, @Nullable String desktopPoolName) {
        Assert.notNull(desktopPoolId, "desktopPoolId must not be null");

        THREAD_EXECUTOR.execute(() -> desktopPoolDashboardServiceTx.updateDesktopPoolName(desktopPoolId, desktopPoolName));
    }
}
