package com.ruijie.rcos.rcdc.rco.module.impl.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.api.CbbDeskMgmtAPI;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.dto.CbbDeskDTO;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.enums.CbbCloudDeskState;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.enums.CbbCloudDeskType;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.enums.CbbDesktopSessionType;
import com.ruijie.rcos.rcdc.rca.module.def.response.DefaultResponse;
import com.ruijie.rcos.rcdc.rco.module.common.annotation.TargetHost;
import com.ruijie.rcos.rcdc.rco.module.common.annotation.TcpAction;
import com.ruijie.rcos.rcdc.rco.module.common.enums.HostTypeEnums;
import com.ruijie.rcos.rcdc.rco.module.common.message.AbstractRcdcHostMessageHandler;
import com.ruijie.rcos.rcdc.rco.module.def.constants.RcoOneAgentToRcdcAction;
import com.ruijie.rcos.rcdc.rco.module.impl.cache.DeskAgentLoadInfoCacheManager;
import com.ruijie.rcos.rcdc.rco.module.impl.connector.dto.ComputerReportSystemInfoDTO;
import com.ruijie.rcos.rcdc.rco.module.impl.dto.DeskAgentLoadInfoDTO;
import com.ruijie.rcos.rcdc.rco.module.impl.dto.DeskWinAuthInfoDTO;
import com.ruijie.rcos.rcdc.rco.module.impl.entity.UserDesktopEntity;
import com.ruijie.rcos.rcdc.rco.module.impl.service.ComputerBusinessService;
import com.ruijie.rcos.rcdc.rco.module.impl.service.UserDesktopService;
import com.ruijie.rcos.rcdc.rco.module.impl.spi.dto.DeskHostInfoDTO;
import com.ruijie.rcos.sk.base.exception.BusinessException;
import com.ruijie.rcos.sk.base.lockable.LockableExecutor;
import com.ruijie.rcos.sk.base.log.Logger;
import com.ruijie.rcos.sk.base.log.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

import java.util.Objects;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Description: 桌面资源信息
 * Copyright: Copyright (c) 2024
 * Company: Ruijie Co., Ltd.
 * Create Time: 2024/2/21
 *
 * @author zqj
 */
@TcpAction(RcoOneAgentToRcdcAction.OA_SEND_CDC_HOST_INFO)
@TargetHost({HostTypeEnums.THIRD_HOST, HostTypeEnums.CLOUD_DESK})
@Service
public class OaDesktopSystemInfoImpl extends AbstractRcdcHostMessageHandler<DefaultResponse, DeskHostInfoDTO> {

    private static final Logger LOGGER = LoggerFactory.getLogger(OaDesktopSystemInfoImpl.class);

    private static final AtomicInteger COUNT = new AtomicInteger();

    private static final String LOG_PREFIX = "rco_desk-";

    /**
     * 第三方桌面主机上报日志太多，第隔1000条日志打印一条
     */
    private static final int FREQUENCY = 1000;

    private static final int ZERO = 0;

    private static final int HUNDRED = 100;

    @Autowired
    private UserDesktopService userDesktopService;

    @Autowired
    private ComputerBusinessService computerBusinessService;

    @Autowired
    private CbbDeskMgmtAPI cbbDeskMgmtAPI;

    @Override
    protected DefaultResponse innerProcessMessage(DeskHostInfoDTO deskHostInfoDTO) {
        updateCache(deskHostInfoDTO);
        UUID hostId = deskHostInfoDTO.getHostId();
        if (HostTypeEnums.THIRD_HOST.name().equals(deskHostInfoDTO.getHostBusinessType())) {
            computerBusinessService.online(hostId);
            if (deskHostInfoDTO.getBaseInfo() != null) {
                ComputerReportSystemInfoDTO computerReportSystemInfoDTO = new ComputerReportSystemInfoDTO();
                DeskHostInfoDTO.BaseInfoDTO baseInfo = deskHostInfoDTO.getBaseInfo();
                computerReportSystemInfoDTO.setOs(baseInfo.getOs());
                computerReportSystemInfoDTO.setOsVersion(baseInfo.getOsVersion());
                computerReportSystemInfoDTO.setCpu(String.valueOf(baseInfo.getCpu()));
                computerReportSystemInfoDTO.setMemory(String.valueOf(baseInfo.getMemory()));
                computerReportSystemInfoDTO.setSystemDisk(baseInfo.getSystemDisk());
                computerReportSystemInfoDTO.setPersonDisk(baseInfo.getPersonDisk());
                computerReportSystemInfoDTO.setComputerName(baseInfo.getComputerName());

                LOGGER.info(LOG_PREFIX + "上报的桌面主机[{}]信息={},", deskHostInfoDTO.getHostId(), JSON.toJSONString(deskHostInfoDTO.getBaseInfo()));
                computerBusinessService.updateComputerSystemInfo(deskHostInfoDTO.getHostId().toString(), computerReportSystemInfoDTO);
            }
        }
        DefaultResponse defaultResponse = new DefaultResponse();
        return defaultResponse;
    }

    private void updateCache(DeskHostInfoDTO request) {
        UUID deskId = request.getHostId();

        DeskHostInfoDTO.UsageInfoDTO usageInfoDTO = request.getUsageInfo();
        if (usageInfoDTO == null) {
            LOGGER.info(LOG_PREFIX + "上报的桌面主机使用状态为空id={}, 不做处理", deskId);
            return;
        }

        int count = COUNT.incrementAndGet();
        if ((count % FREQUENCY) == ZERO) {
            // 同步651805的修订-上报日志量太大
            LOGGER.info(LOG_PREFIX + "收到更新桌面主机负载请求：{}", JSONObject.toJSONString(usageInfoDTO));
            COUNT.set(ZERO);
        }

        updateDeskActivityState(deskId, usageInfoDTO);


        UserDesktopEntity userDesktopEntity = userDesktopService.findByDeskId(deskId);
        if (ObjectUtils.isEmpty(userDesktopEntity)) {
            // 桌面不存在，不需要更新
            return;
        }

        Integer memoryUsage = usageInfoDTO.getMemoryUsage();
        Integer systemDiskUsage = usageInfoDTO.getSystemDiskUsage();
        // hostAgent上报的是0.51这样的小数，换成整数
        Float result = usageInfoDTO.getCpuUsage() * HUNDRED;
        Integer cpuUsage = result.intValue();

        DeskAgentLoadInfoDTO loadInfoDTO = new DeskAgentLoadInfoDTO();
        loadInfoDTO.setCpuUsage(cpuUsage);
        loadInfoDTO.setMemoryUsage(memoryUsage);
        loadInfoDTO.setSystemDiskUsage(systemDiskUsage);
        DeskAgentLoadInfoCacheManager.updateCache(deskId, loadInfoDTO);
    }

    private void updateDeskActivityState(UUID deskId, DeskHostInfoDTO.UsageInfoDTO usageInfoDTO) {

        try {
            LockableExecutor.executeWithTryLock(deskId.toString(), () -> {
                CbbDeskDTO deskDTO = cbbDeskMgmtAPI.findById(deskId);
                if (Objects.isNull(deskDTO)) {
                    return;
                }
                if (deskDTO.getDeskType() == CbbCloudDeskType.THIRD || deskDTO.getSessionType() == CbbDesktopSessionType.MULTIPLE) {
                    dealWinAction(usageInfoDTO, deskDTO.getOsActiveBySystem(), deskId);
                }
                if (deskDTO.getDeskType() == CbbCloudDeskType.THIRD && deskDTO.getDeskState() == CbbCloudDeskState.OFF_LINE) {
                    syncThirdDeskState(deskId);
                }
            }, 3);
        } catch (BusinessException e) {
            LOGGER.error("桌面[{}]更新激活锁异常", deskId, e);
        }
    }

    private void dealWinAction(DeskHostInfoDTO.UsageInfoDTO usageInfoDTO, Boolean hasActive,
                               UUID deskId) {
        LOGGER.info("桌面[{}]更新激活信息[{}]-[{}]", deskId, hasActive, JSON.toJSONString(usageInfoDTO));
        boolean isActive = usageInfoDTO.getDaysLeft() != null && usageInfoDTO.getDaysLeft() > 0;
        if (!Objects.equals(hasActive, isActive)) {
            try {
                cbbDeskMgmtAPI.updateDeskOsActiveBySystem(deskId, isActive);
            } catch (Exception ex) {
                LOGGER.error("桌面[{}]更新激活失败", deskId, ex);
            }
        }
    }

    private void syncThirdDeskState(UUID deskId) {
        LOGGER.info("桌面[{}]为离线状态，收到oa信息后同步状态", deskId);
        try {
            cbbDeskMgmtAPI.syncThirdDeskStateToLocal(deskId);
        } catch (Exception ex) {
            LOGGER.error("桌面[{}]同步状态", deskId, ex);
        }
    }
}
