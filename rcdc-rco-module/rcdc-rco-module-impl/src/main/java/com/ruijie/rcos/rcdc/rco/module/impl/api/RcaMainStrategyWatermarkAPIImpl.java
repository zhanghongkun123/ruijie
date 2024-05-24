package com.ruijie.rcos.rcdc.rco.module.impl.api;

import com.alibaba.fastjson.JSON;
import com.google.common.collect.Lists;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.dto.CbbWatermarkConfigDTO;
import com.ruijie.rcos.rcdc.rca.module.def.api.RcaHostAPI;
import com.ruijie.rcos.rcdc.rca.module.def.api.RcaMainStrategyAPI;
import com.ruijie.rcos.rcdc.rca.module.def.dto.RcaHostDTO;
import com.ruijie.rcos.rcdc.rca.module.def.dto.strategy.RcaMainStrategyDTO;
import com.ruijie.rcos.rcdc.rca.module.def.dto.strategy.WatermarkConfig;
import com.ruijie.rcos.rcdc.rca.module.def.enums.RcaEnum;
import com.ruijie.rcos.rcdc.rco.module.def.api.RcaHostDesktopMgmtAPI;
import com.ruijie.rcos.rcdc.rco.module.def.api.RcaMainStrategyWatermarkAPI;
import com.ruijie.rcos.rcdc.rco.module.def.api.WatermarkMessageAPI;
import com.ruijie.rcos.rcdc.rco.module.def.api.dto.RcaHostDesktopDTO;
import com.ruijie.rcos.sk.base.concurrent.ThreadExecutor;
import com.ruijie.rcos.sk.base.concurrent.ThreadExecutors;
import com.ruijie.rcos.sk.base.exception.BusinessException;
import com.ruijie.rcos.sk.base.log.Logger;
import com.ruijie.rcos.sk.base.log.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.Assert;
import org.springframework.util.CollectionUtils;

import java.util.List;
import java.util.Objects;
import java.util.UUID;
import java.util.stream.Collectors;

/**
 * Description: Function Description
 * Copyright: Copyright (c) 2018
 * Company: Ruijie Co., Ltd.
 * Create Time: 2019年9月2日
 *
 * @author XiaoJiaXin
 */
public class RcaMainStrategyWatermarkAPIImpl implements RcaMainStrategyWatermarkAPI {

    private static final Logger LOGGER = LoggerFactory.getLogger(RcaMainStrategyWatermarkAPIImpl.class);

    private static final ThreadExecutor RCA_MAIN_STRATEGY_CHANGE_NOTIFY_RUNNING_RCA_HOST_DESK_EXECUTOR =
            ThreadExecutors.newBuilder("rca-main-strategy-change-notify-running-rca-host-desk").maxThreadNum(5).queueSize(20).build();

    @Autowired
    private RcaMainStrategyAPI rcaMainStrategyAPI;

    @Autowired
    private RcaHostAPI rcaHostAPI;

    @Autowired
    private RcaHostDesktopMgmtAPI rcaHostDesktopMgmtAPI;

    @Autowired
    private WatermarkMessageAPI watermarkMessageAPI;

    @Override
    public void handleNotifyWatermarkConfig(RcaMainStrategyDTO rcaMainStrategyDTO) throws BusinessException {
        Assert.notNull(rcaMainStrategyDTO, "rcaMainStrategyDTO is not null");

        if (rcaMainStrategyDTO.getDesktopStrategyConfig() != null ) {
            List<UUID> poolIdList = rcaMainStrategyAPI.getPoolIdListByStrategyId(rcaMainStrategyDTO.getStrategyId());
            if (CollectionUtils.isEmpty(poolIdList)) {
                LOGGER.info("云应用策略{}没有关联应用池，不处理");
                return;
            }
            List<RcaHostDTO> rcaHostDTOList = rcaHostAPI.findAllByPoolIdIn(poolIdList);
            if (CollectionUtils.isEmpty(rcaHostDTOList)) {
                LOGGER.info("云应用策略{}关联应用池没有关联应用主机，不处理");
                return;
            }
            CbbWatermarkConfigDTO cbbWatermarkConfigDTO;
            WatermarkConfig watermarkInfo = rcaMainStrategyDTO.getDesktopStrategyConfig().getWatermarkInfo();
            if (Objects.isNull(watermarkInfo) || !Boolean.TRUE.equals(watermarkInfo.getEnable())) {
                cbbWatermarkConfigDTO = null;
            } else {
                cbbWatermarkConfigDTO = new CbbWatermarkConfigDTO();
                cbbWatermarkConfigDTO.setEnable(watermarkInfo.getEnable());
                cbbWatermarkConfigDTO.setDisplayConfig(watermarkInfo.getDisplayConfig());
                cbbWatermarkConfigDTO.setEnableDarkWatermark(watermarkInfo.getEnableDarkWatermark());
                cbbWatermarkConfigDTO.setDisplayContent(JSON.toJSONString(watermarkInfo.getDisplayContent()));
            }

            // VDI应用主机
            if (RcaEnum.HostSourceType.VDI == rcaMainStrategyDTO.getHostSourceType()) {
                List<RcaHostDesktopDTO> rcaHostDesktopDTOList =
                        rcaHostDesktopMgmtAPI.listByHostIdIn(
                                rcaHostDTOList.stream().map(rcaHostDTO -> rcaHostDTO.getId() ).collect(Collectors.toList()));

                RCA_MAIN_STRATEGY_CHANGE_NOTIFY_RUNNING_RCA_HOST_DESK_EXECUTOR.execute(() -> {
                    watermarkMessageAPI.sendToRcaHostDesktopList(rcaHostDesktopDTOList, cbbWatermarkConfigDTO);
                });
            } else {
                RCA_MAIN_STRATEGY_CHANGE_NOTIFY_RUNNING_RCA_HOST_DESK_EXECUTOR.execute(() -> {
                    watermarkMessageAPI.sendToRcaHostList(rcaHostDTOList, cbbWatermarkConfigDTO);
                });
            }
        }
    }

    @Override
    public void handleNotifyWatermarkConfig(RcaHostDTO rcaHostDTO, UUID multiSessionId, RcaMainStrategyDTO rcaMainStrategyDTO)
            throws BusinessException {
        Assert.notNull(rcaHostDTO, "rcaHostDTO is not null");
        Assert.notNull(rcaMainStrategyDTO, "rcaMainStrategyDTO is not null");

        if (rcaMainStrategyDTO.getDesktopStrategyConfig() != null ) {
            List<UUID> poolIdList = rcaMainStrategyAPI.getPoolIdListByStrategyId(rcaMainStrategyDTO.getStrategyId());
            if (CollectionUtils.isEmpty(poolIdList)) {
                LOGGER.info("云应用策略{}没有关联应用池，不处理", rcaMainStrategyDTO.getStrategyId());
                return;
            }
            List<RcaHostDTO> rcaHostDTOList = rcaHostAPI.findAllByPoolIdIn(poolIdList);
            if (CollectionUtils.isEmpty(rcaHostDTOList)) {
                LOGGER.info("云应用策略{}关联应用池没有关联应用主机，不处理", rcaMainStrategyDTO.getStrategyId());
                return;

            }
            if (!rcaHostDTOList.stream().anyMatch(rcaHostItem -> rcaHostItem.getId().equals(rcaHostDTO.getId()))) {
                LOGGER.info("云应用策略{}关联应用池没有关联传入的应用主机{}，不处理", rcaMainStrategyDTO.getStrategyId(), rcaHostDTO.getId());
                return;
            }

            CbbWatermarkConfigDTO cbbWatermarkConfigDTO;
            WatermarkConfig watermarkInfo = rcaMainStrategyDTO.getDesktopStrategyConfig().getWatermarkInfo();
            if (Objects.isNull(watermarkInfo) || !Boolean.TRUE.equals(watermarkInfo.getEnable())) {
                cbbWatermarkConfigDTO = null;
            } else {
                cbbWatermarkConfigDTO = new CbbWatermarkConfigDTO();
                cbbWatermarkConfigDTO.setEnable(watermarkInfo.getEnable());
                cbbWatermarkConfigDTO.setDisplayConfig(watermarkInfo.getDisplayConfig());
                cbbWatermarkConfigDTO.setEnableDarkWatermark(watermarkInfo.getEnableDarkWatermark());
                cbbWatermarkConfigDTO.setDisplayContent(JSON.toJSONString(watermarkInfo.getDisplayContent()));
            }

            if (RcaEnum.HostSourceType.VDI == rcaMainStrategyDTO.getHostSourceType()) { // VDI应用主机
                List<RcaHostDesktopDTO> rcaHostDesktopDTOList =
                        rcaHostDesktopMgmtAPI.listByHostIdIn(Lists.newArrayList(rcaHostDTO.getId()));
                RcaHostDesktopDTO rcaHostDesktopDTO = rcaHostDesktopDTOList.get(0);

                RCA_MAIN_STRATEGY_CHANGE_NOTIFY_RUNNING_RCA_HOST_DESK_EXECUTOR.execute(() -> {
                    watermarkMessageAPI.sendToRcaHostDesktopMultiSessionId(rcaHostDesktopDTO, multiSessionId, cbbWatermarkConfigDTO);
                });
            } else { // 第三方应用主机
                RCA_MAIN_STRATEGY_CHANGE_NOTIFY_RUNNING_RCA_HOST_DESK_EXECUTOR.execute(() -> {
                    watermarkMessageAPI.sendToRcaHostListMultiSessionId(rcaHostDTO, multiSessionId, cbbWatermarkConfigDTO);
                });
            }
        }
    }

}
