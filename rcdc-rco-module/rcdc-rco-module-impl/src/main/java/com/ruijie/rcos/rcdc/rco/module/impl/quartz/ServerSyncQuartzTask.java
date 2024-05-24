package com.ruijie.rcos.rcdc.rco.module.impl.quartz;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.api.CbbPhysicalServerMgmtAPI;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.dto.PhysicalServerDTO;
import com.ruijie.rcos.rcdc.rco.module.impl.dao.CabinetServerMappingDAO;
import com.ruijie.rcos.rcdc.rco.module.impl.entity.CabinetServerMappingEntity;
import com.ruijie.rcos.rcdc.rco.module.impl.tx.CabinetServiceTx;
import com.ruijie.rcos.sk.base.exception.BusinessException;
import com.ruijie.rcos.sk.base.log.Logger;
import com.ruijie.rcos.sk.base.log.LoggerFactory;
import com.ruijie.rcos.sk.base.quartz.QuartzTask;
import com.ruijie.rcos.sk.base.quartz.QuartzTaskContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.Assert;
import org.springframework.util.CollectionUtils;

import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.UUID;
import java.util.stream.Collectors;

/**
 * 
 * <br>
 * Description: Function Description <br>
 * Copyright: Copyright (c) 2017 <br>
 * Company: Ruijie Co., Ltd. <br>
 * Create Time: 2019年12月16日 <br>
 * 
 * * @Service
 * 
 * @Quartz(scheduleTypeCode = "rco_server_sync", scheduleName =
 *                          BusinessKey.RCDC_RCO_BIGSCREEN_QUARTZ_SERVER_SYNC, cron = "5 * * * * ? *")
 * 
 * @author brq
 */
public class ServerSyncQuartzTask implements QuartzTask {

    private static final Logger LOGGER = LoggerFactory.getLogger(ServerSyncQuartzTask.class);

    @Autowired
    private CbbPhysicalServerMgmtAPI cbbPhysicalServerMgmtAPI;

    @Autowired
    private CabinetServerMappingDAO cabinetServerMappingDAO;

    @Autowired
    CabinetServiceTx cabinetServiceTx;

    @Override
    public void execute(QuartzTaskContext quartzTaskContext) throws BusinessException {
        Assert.notNull(quartzTaskContext, "quartzTaskContext can not be null");
        LOGGER.info("同步服务器数据定时任务开始===");

        List<PhysicalServerDTO> physicalServerDTOList = cbbPhysicalServerMgmtAPI.listAllPhysicalServer(false);
        if (CollectionUtils.isEmpty(physicalServerDTOList)) {
            return;
        }
        List<UUID> idList = physicalServerDTOList.stream().map(PhysicalServerDTO::getId).collect(Collectors.toList());

        Map<UUID, List<UUID>> mappingMap = getMappingMap(idList);

        for (Entry<UUID, List<UUID>> entry : mappingMap.entrySet()) {
            cabinetServiceTx.deleteServerFromCabinet(entry.getKey(), entry.getValue());
        }

        LOGGER.info("同步服务器数据定时任务结束===");
    }

    private Map<UUID, List<UUID>> getMappingMap(List<UUID> idList) {
        List<CabinetServerMappingEntity> mappingEntityList = cabinetServerMappingDAO.getByServerIdIn(idList);
        Map<UUID, List<UUID>> mappingMap = Maps.newHashMap();
        for (CabinetServerMappingEntity entity : mappingEntityList) {
            if (mappingMap.containsKey(entity.getCabinetId())) {
                mappingMap.get(entity.getCabinetId()).add(entity.getServerId());
            } else {
                List<UUID> tempIdList = Lists.newArrayList();
                tempIdList.add(entity.getServerId());
                mappingMap.put(entity.getCabinetId(), tempIdList);
            }
        }
        return mappingMap;
    }
}
