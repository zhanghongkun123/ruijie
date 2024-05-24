package com.ruijie.rcos.rcdc.rco.module.impl.api;

import com.ruijie.rcos.rcdc.clouddesktop.module.def.enums.CbbCloudDeskState;
import com.ruijie.rcos.rcdc.rco.module.def.api.ComputerBusinessAPI;
import com.ruijie.rcos.rcdc.rco.module.def.api.StatisticsAPI;
import com.ruijie.rcos.rcdc.rco.module.def.api.request.DeskTypeRequest;
import com.ruijie.rcos.rcdc.rco.module.def.api.response.DesktopStatisticsItem;
import com.ruijie.rcos.rcdc.rco.module.def.api.response.DesktopStatisticsResponse;
import com.ruijie.rcos.rcdc.rco.module.impl.dao.ViewDesktopDetailDAO;
import com.ruijie.rcos.rcdc.rco.module.impl.dao.ViewDesktopInfoStatDAO;
import com.ruijie.rcos.rcdc.rco.module.impl.dto.DesktopStatisticsDTO;
import com.ruijie.rcos.rcdc.terminal.module.def.api.CbbTerminalGroupMgmtAPI;
import com.ruijie.rcos.rcdc.terminal.module.def.api.dto.CbbTerminalStatisticsDTO;
import com.ruijie.rcos.rcdc.terminal.module.def.api.dto.TerminalStatisticsItem;
import com.ruijie.rcos.sk.base.exception.BusinessException;
import com.ruijie.rcos.sk.base.log.Logger;
import com.ruijie.rcos.sk.base.log.LoggerFactory;
import com.ruijie.rcos.sk.base.util.HibernateUtil;
import org.apache.commons.lang3.ArrayUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.Assert;
import org.springframework.util.CollectionUtils;

import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Description: Function Description
 * Copyright: Copyright (c) 2018
 * Company: Ruijie Co., Ltd.
 * Create Time: 2018/12/7
 *
 * @author Jarman
 */
public class StatisticsAPIImpl implements StatisticsAPI {

    private static final Logger LOGGER = LoggerFactory.getLogger(StatisticsAPIImpl.class);

    @Autowired
    private ViewDesktopInfoStatDAO viewDesktopInfoStatDAO;

    @Autowired
    private ViewDesktopDetailDAO viewDesktopDetailDAO;

    @Autowired
    private CbbTerminalGroupMgmtAPI cbbTerminalGroupMgmtAPI;

    @Autowired
    private ComputerBusinessAPI computerBusinessAPI;

    private static final Set<String> COUNT_DESKTOP_EXCLUDE_STATE_SET = new HashSet<>();

    static {
        COUNT_DESKTOP_EXCLUDE_STATE_SET.add(CbbCloudDeskState.RECOVING.name());
        COUNT_DESKTOP_EXCLUDE_STATE_SET.add(CbbCloudDeskState.RECYCLE_BIN.name());
        COUNT_DESKTOP_EXCLUDE_STATE_SET.add(CbbCloudDeskState.CREATING.name());
        COUNT_DESKTOP_EXCLUDE_STATE_SET.add(CbbCloudDeskState.COMPLETE_DELETING.name());
    }

    @Override
    public CbbTerminalStatisticsDTO statisticsTerminal(UUID[] groupIdArr) throws BusinessException {
        Assert.notNull(groupIdArr, "groupIdArr不能为null");
        TerminalStatisticsItem itemPC = computerBusinessAPI.statisticsComputer(groupIdArr);
        CbbTerminalStatisticsDTO cbbTerminalStatisticsDTO = cbbTerminalGroupMgmtAPI.statisticsTerminal(groupIdArr);
        cbbTerminalStatisticsDTO.setPc(itemPC);
        return fetchAllTerminalOnlineCount(cbbTerminalStatisticsDTO);
    }

    /**
     * 统计所有终端在线数量
     */
    private CbbTerminalStatisticsDTO fetchAllTerminalOnlineCount(CbbTerminalStatisticsDTO response) {
        final Integer vdiTotal = response.getVdi().getTotal();
        final Integer voiTotal = response.getVoi().getTotal();
        final Integer idvTotal = response.getIdv().getTotal();
        final Integer appTotal = response.getApp().getTotal();
        final Integer pcTotal = response.getPc().getTotal();

        final Integer vdiOnline = response.getVdi().getOnline();
        final Integer voiOnline = response.getVoi().getOnline();
        final Integer idvOnline = response.getIdv().getOnline();
        final Integer appOnline = response.getApp().getOnline();
        final Integer pcTOnline = response.getPc().getOnline();

        response.setTotal(vdiTotal + idvTotal + appTotal + pcTotal + voiTotal);
        response.setTotalOnline(vdiOnline + idvOnline + appOnline + pcTOnline + voiOnline);
        return response;
    }

    @Override
    public DesktopStatisticsResponse statisticsDesktop(DeskTypeRequest request) {
        Assert.notNull(request, "DeskTypeRequest不能为null");

        List<DesktopStatisticsDTO> resultList;
        Long neverLoginCount;
        if (ArrayUtils.isEmpty(request.getUserGroupIdArr()) && ArrayUtils.isEmpty(request.getTerminalGroupIdArr())) {
            resultList = viewDesktopInfoStatDAO.statisticsByDesktopState(request.getDesktopType());
            neverLoginCount = viewDesktopDetailDAO.countByHasLoginAndDesktopTypeAndIsDelete(false, request.getDesktopType().name(), false);
        } else {
            List<UUID> userGroupIdList = HibernateUtil.handleQueryIncludeList(Arrays.asList(request.getUserGroupIdArr()));
            List<UUID> terminalGroupIdList = HibernateUtil.handleQueryIncludeList(Arrays.asList(request.getTerminalGroupIdArr()));
            resultList = viewDesktopInfoStatDAO.statisticsByDesktopStateAndGroupId(request.getDesktopType(), userGroupIdList, terminalGroupIdList);
            neverLoginCount = viewDesktopDetailDAO.countVDINeverLoginByUserGroupIdInAndTerminalGroupIdIn(userGroupIdList, terminalGroupIdList);
        }
        if (CollectionUtils.isEmpty(resultList)) {
            LOGGER.debug("没有桌面类型为[{}]的数据", request.getDesktopType());
            return new DesktopStatisticsResponse();
        }
        DesktopStatisticsItem item = buildDesktopStatisticsItem(resultList, neverLoginCount);
        DesktopStatisticsResponse response = new DesktopStatisticsResponse();
        response.setVdi(item);
        return response;
    }

    private DesktopStatisticsItem buildDesktopStatisticsItem(List<DesktopStatisticsDTO> list, Long neverLoginCount) {
        AtomicInteger online = new AtomicInteger();
        AtomicInteger sleep = new AtomicInteger();
        AtomicInteger offline = new AtomicInteger();
        AtomicInteger other = new AtomicInteger();
        list.forEach((item -> {
            // 其中"休眠"、"休眠中"、"唤醒中"归类为休眠状态；运行中归类为在线状态，关机状态只统计关机（不包括回收站，故障状态）
            // 不统计从回收站恢复中、回收站、创建中及回收站删除中状态
            if (COUNT_DESKTOP_EXCLUDE_STATE_SET.contains(item.getDeskState())) {
                LOGGER.debug("不统计状态为:[{}]的桌面", item.getDeskState());
                return;
            }

            int count = item.getCount().intValue();
            if (CbbCloudDeskState.RUNNING.name().equals(item.getDeskState())) {
                online.addAndGet(count);
                return;
            }
            if (isSleep(item.getDeskState())) {
                sleep.addAndGet(count);
                return;
            }
            if (CbbCloudDeskState.CLOSE.name().equals(item.getDeskState())) {
                offline.addAndGet(count);
                return;
            }
            other.addAndGet(count);
        }));

        DesktopStatisticsItem item = new DesktopStatisticsItem();
        item.setOnline(online.get());
        item.setSleep(sleep.get());
        item.setNeverLogin(neverLoginCount.intValue());
        item.setOffline(offline.get());
        int total = online.get() + offline.get() + sleep.get() + other.get();
        item.setTotal(total);
        return item;
    }

    private boolean isSleep(String deskState) {
        // 休眠、休眠中、唤醒中归类为"休眠"
        CbbCloudDeskState deskStateEnum = CbbCloudDeskState.valueOf(deskState);

        switch (deskStateEnum) {
            case SLEEP:
            case SLEEPING:
            case WAKING:
                return true;
            default:
                return false;
        }
    }
}
