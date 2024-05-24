package com.ruijie.rcos.rcdc.rco.module.impl.spi;

import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.lang.Nullable;
import org.springframework.util.Assert;

import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.api.CbbVDIDeskStrategyMgmtAPI;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.dto.ObjectChangeDTO;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.dto.strategy.CbbDeskStrategyVDIDTO;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.dto.strategy.CbbUpdateDeskStrategyVDIDTO;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.enums.CbbCloudDeskState;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.spi.CbbDeskStrategyChangeNotifySPI;
import com.ruijie.rcos.rcdc.rco.module.def.api.UserDesktopMgmtAPI;
import com.ruijie.rcos.rcdc.rco.module.def.api.dto.CloudDesktopDTO;
import com.ruijie.rcos.rcdc.rco.module.impl.quartz.AbstractPageQueryAllHandle;
import com.ruijie.rcos.rcdc.terminal.module.def.api.CbbTerminalOperatorAPI;
import com.ruijie.rcos.rcdc.terminal.module.def.api.dto.DeskSessionConfigDTO;
import com.ruijie.rcos.sk.base.concurrent.ThreadExecutor;
import com.ruijie.rcos.sk.base.concurrent.ThreadExecutors;
import com.ruijie.rcos.sk.base.exception.BusinessException;
import com.ruijie.rcos.sk.base.log.Logger;
import com.ruijie.rcos.sk.base.log.LoggerFactory;
import com.ruijie.rcos.sk.modulekit.api.comm.DefaultPageResponse;
import com.ruijie.rcos.sk.pagekit.api.PageQueryBuilderFactory;
import com.ruijie.rcos.sk.pagekit.api.PageQueryRequest;

/**
 * Description: Function Description
 * Copyright: Copyright (c) 2023
 * Company: Ruijie Co., Ltd.
 * Create Time: 2023年09月11日
 *
 * @author 徐国祥
 */
public class DeskStrategyChangeNotifySPIImpl implements CbbDeskStrategyChangeNotifySPI {
    private static final String STRATEGY_ID = "cbbStrategyId";

    private static final String DESK_STATE = "deskState";

    private static final String HAS_TERMINAL_RUNNING = "hasTerminalRunning";

    private static final String TERMINAL_ID = "terminalId";

    private static final String CREATE_TIME = "createTime";

    private static final ThreadExecutor EST_IDLE_THREAD_POOL = ThreadExecutors.newBuilder("est-idle-over-time-pool") //
            .maxThreadNum(10) //
            .queueSize(1000) //
            .build();

    @Autowired
    private CbbVDIDeskStrategyMgmtAPI cbbVDIDeskStrategyMgmtAPI;

    @Autowired
    private UserDesktopMgmtAPI userDesktopMgmtAPI;

    @Autowired
    private PageQueryBuilderFactory requestBuilderFactory;

    @Autowired
    private CbbTerminalOperatorAPI cbbTerminalOperatorAPI;

    private static final Logger LOGGER = LoggerFactory.getLogger(DeskStrategyChangeNotifySPIImpl.class);

    private final LoadingCache<UUID, Integer> estIdleOverTimeCache =
            CacheBuilder.newBuilder().expireAfterAccess(5, TimeUnit.MINUTES).build(new CacheLoader<UUID, Integer>() {
                @Override
                public Integer load(UUID key) throws Exception {
                    Assert.notNull(key, "key can not be null");
                    CbbDeskStrategyVDIDTO deskStrategyVDI = cbbVDIDeskStrategyMgmtAPI.getDeskStrategyVDI(key);
                    return deskStrategyVDI.getEstIdleOverTime();
                }
            });

    @Override
    public void notify(CbbUpdateDeskStrategyVDIDTO request) throws BusinessException {

    }

    @Override
    public void notify(ObjectChangeDTO<CbbDeskStrategyVDIDTO> request) throws BusinessException {
        Assert.notNull(request, "request can not be null");
        final CbbDeskStrategyVDIDTO oldObject = request.getOldObject();
        final CbbDeskStrategyVDIDTO newObject = request.getNewObject();
        if (Objects.equals(oldObject.getEstIdleOverTime(), newObject.getEstIdleOverTime())) {
            LOGGER.info("云桌面策略[{}]est空闲断开时间[{}]未发生变化，无需通知", newObject.getId(), newObject.getEstIdleOverTime());
            return;
        }
        // 将缓存失效，让程序加载最新的配置，若不实时获取可能出现并发问题，两个线程同时发消息，客户端接收消息的顺序可能不同
        estIdleOverTimeCache.invalidate(newObject.getId());

        EST_IDLE_THREAD_POOL.execute(() -> {
            try {
                AbstractPageQueryAllHandle pageQueryAllHandle = getAbstractPageQueryAllHandle(newObject);
                Set<DeskEstIdleOverTimeDTO> deskEstIdleOverTimeDTOSet = pageQueryAllHandle.queryAll();

                for (DeskEstIdleOverTimeDTO deskStrategy : deskEstIdleOverTimeDTOSet) {

                    Integer estIdleOverTime = estIdleOverTimeCache.getUnchecked(deskStrategy.getStrategyId());
                    DeskSessionConfigDTO deskSessionConfigDTO = new DeskSessionConfigDTO();
                    deskSessionConfigDTO.setIdleOvertime(estIdleOverTime);
                    deskSessionConfigDTO.setTerminalId(deskStrategy.getTerminalId());
                    deskSessionConfigDTO.setDeskId(deskStrategy.getDeskId());
                    // 发送桌面会话配置 及时生效
                    cbbTerminalOperatorAPI.sendDeskSessionConfig(deskSessionConfigDTO);
                }
            } catch (BusinessException e) {
                LOGGER.error("云桌面策略[{}]estIdleOverTime({}->{})发生变更，查询关联云桌面列表失败", newObject.getId(), oldObject.getEstIdleOverTime(),
                        newObject.getEstIdleOverTime(), e);
            }

        });

    }

    private AbstractPageQueryAllHandle getAbstractPageQueryAllHandle(CbbDeskStrategyVDIDTO newObject) {
        AbstractPageQueryAllHandle pageQueryAllHandle = new AbstractPageQueryAllHandle<DeskEstIdleOverTimeDTO>() {
            @Override
            protected List<DeskEstIdleOverTimeDTO> queryByPage(Pageable pageable) throws BusinessException {
                PageQueryRequest pageQueryRequest = requestBuilderFactory.newRequestBuilder() //
                        .eq(STRATEGY_ID, newObject.getId()) //
                        .eq(DESK_STATE, CbbCloudDeskState.RUNNING.name()) //
                        .eq(HAS_TERMINAL_RUNNING, true).notNull(TERMINAL_ID).asc(CREATE_TIME)
                        .setPageLimit(pageable.getPageNumber(), pageable.getPageSize()).build();
                DefaultPageResponse<CloudDesktopDTO> cloudDesktopDTODefaultPageResponse = //
                        userDesktopMgmtAPI.pageQuery(pageQueryRequest);
                CloudDesktopDTO[] cloudDesktopDTOArr = //
                        Optional.ofNullable(cloudDesktopDTODefaultPageResponse.getItemArr()).orElse(new CloudDesktopDTO[0]);
                return Stream.of(cloudDesktopDTOArr) //
                        .map(item -> new DeskEstIdleOverTimeDTO(item.getCbbId(), item.getDesktopStrategyId(), item.getTerminalId())) //
                        .collect(Collectors.toList());

            }
        };
        return pageQueryAllHandle;
    }

    @Override
    public void createNotify(UUID deskStrategyId) throws BusinessException {

    }

    @Override
    public void updateNotify(UUID deskStrategyId) throws BusinessException {

    }

    @Override
    public void deleteNotify(UUID deskStrategyId, String deskStrategyName) throws BusinessException {

    }

    /**
     * 桌面est连接空闲超时DTO
     */
    class DeskEstIdleOverTimeDTO {
        private UUID deskId;

        private UUID strategyId;

        private String terminalId;

        DeskEstIdleOverTimeDTO(UUID deskId, UUID strategyId, String terminalId) {
            this.deskId = deskId;
            this.strategyId = strategyId;
            this.terminalId = terminalId;
        }

        public UUID getDeskId() {
            return deskId;
        }

        public UUID getStrategyId() {
            return strategyId;
        }

        public String getTerminalId() {
            return terminalId;
        }

        /**
         *
         * @param o 对象
         * @return true/false
         */
        @Override
        public boolean equals(@Nullable Object o) {
            if (this == o) {
                return true;
            }

            if (o == null || getClass() != o.getClass()) {
                return false;
            }

            DeskEstIdleOverTimeDTO that = (DeskEstIdleOverTimeDTO) o;

            return new EqualsBuilder().append(deskId, that.deskId) //
                    .append(strategyId, that.strategyId) //
                    .append(terminalId, that.terminalId) //
                    .isEquals();
        }

        /**
         *
         * @return Integer
         */
        @Override
        public int hashCode() {
            return new HashCodeBuilder(17, 37).append(deskId) //
                    .append(strategyId) //
                    .append(terminalId) //
                    .toHashCode();
        }
    }
}
