package com.ruijie.rcos.rcdc.rco.module.impl.quartz;

import com.google.common.collect.Lists;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.api.CbbPhysicalServerMgmtAPI;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.dto.PhysicalServerDTO;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.dto.desk.CbbMonitorDataDTO;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.dto.desk.CbbServerMonitorInfoDTO;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.dto.desk.CbbServerRequestDTO;
import com.ruijie.rcos.rcdc.rco.module.impl.Constants;
import com.ruijie.rcos.rcdc.rco.module.impl.dao.ServerResourceUsageHistoryDAO;
import com.ruijie.rcos.rcdc.rco.module.impl.entity.ServerResourceUsageHistoryEntity;
import com.ruijie.rcos.rcdc.rco.module.impl.service.ServerService;
import com.ruijie.rcos.rcdc.rco.module.impl.util.DateUtil;
import com.ruijie.rcos.rcdc.rco.module.impl.util.MathUtil;
import com.ruijie.rcos.sk.base.exception.BusinessException;
import com.ruijie.rcos.sk.base.log.Logger;
import com.ruijie.rcos.sk.base.log.LoggerFactory;
import com.ruijie.rcos.sk.base.quartz.QuartzTask;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.CollectionUtils;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;

/**
 *
 * Description: 服务器资源使用情况收集抽象类
 * Copyright: Copyright (c) 2018
 * Company: Ruijie Co., Ltd.
 * Create Time: 2019年7月26日
 *
 * @author brq
 */
public abstract class AbstractServerResourceUsageHistoryCollect implements QuartzTask {

    private static final Logger LOGGER = LoggerFactory.getLogger(AbstractServerResourceUsageHistoryCollect.class);

    @Autowired
    private CbbPhysicalServerMgmtAPI cbbPhysicalServerMgmtAPI;

    @Autowired
    protected ServerResourceUsageHistoryDAO serverResourceUsageHistoryDAO;

    @Autowired
    protected ServerService serverService;

    protected void save(List<PhysicalServerDTO> serverList, LocalDateTime startTime, LocalDateTime endTime) {
        // 由于底层一次只能调台服务器的数据,计算需要调用几次hci接口
        int times = serverList.size() / Constants.MONITOR_REQUEST_LIMIT + 1;
        List<ServerResourceUsageHistoryEntity> saveList = Lists.newArrayList();
        for (int i = 0; i < times; i++) {
            // 获取服务器监控数据
            List<CbbServerMonitorInfoDTO> serverMonitorInfoDTOList;
            try {
                serverMonitorInfoDTOList = listServerMonitorInfo(i, serverList, startTime, endTime);
            } catch (BusinessException e) {
                LOGGER.error("获取服务器监控信息数据异常", e);
                continue;
            }
            if (CollectionUtils.isEmpty(serverMonitorInfoDTOList)) {
                continue;
            }

            saveList.addAll(bulidEntity(serverMonitorInfoDTOList, DateUtil.localDateTimeToDate(endTime)));
        }
        serverResourceUsageHistoryDAO.saveAll(saveList);
    }

    /**
     * 获取所有物理服务器
     * @return
     */
    protected List<PhysicalServerDTO> listAllPhysicalServer() {

        return cbbPhysicalServerMgmtAPI.listAllPhysicalServer(true);
    }

    private List<CbbServerMonitorInfoDTO> listServerMonitorInfo(int i, List<PhysicalServerDTO> serverList,
                                                                LocalDateTime startTime, LocalDateTime endTime)
            throws BusinessException {
        // 构造需要请求的5台服务器
        List<CbbServerRequestDTO> serverRequestDTOList = Lists.newArrayList();
        for (int j = 0; j < Constants.MONITOR_REQUEST_LIMIT; j++) {
            int serverListIndex = i * Constants.MONITOR_REQUEST_LIMIT + j;
            if (serverListIndex == serverList.size()) {
                break;
            }
            CbbServerRequestDTO serverRequestDTO = new CbbServerRequestDTO();
            serverRequestDTO.setId(serverList.get(serverListIndex).getId());
            serverRequestDTO.setName(serverList.get(serverListIndex).getHostName());
            serverRequestDTOList.add(serverRequestDTO);
        }

        return serverService.getServerMonitorInfo(serverRequestDTOList, startTime, endTime);
    }

    private List<ServerResourceUsageHistoryEntity> bulidEntity(List<CbbServerMonitorInfoDTO> dtoList, Date date) {
        List<ServerResourceUsageHistoryEntity> saveList = Lists.newArrayList();
        for (CbbServerMonitorInfoDTO serverMonitorInfoDTO : dtoList) {
            double cpuUsage = MathUtil.getUsage(processNullList(serverMonitorInfoDTO.getCpuUseRateDTOList()));
            double memoryUsage = MathUtil.getUsage(processNullList(serverMonitorInfoDTO.getMemoryUseRateDTOList()));
            double diskUsage = 0D;
            // 由于泽塔底层无法获取准确的磁盘使用率，需要根据cpu和内存数据判断磁盘数据是否有效
            if (cpuUsage != 0D || memoryUsage != 0D) {
                diskUsage = MathUtil.getUsage(processNullList(serverMonitorInfoDTO.getStorageUseRateDTOList()));
            }

            ServerResourceUsageHistoryEntity entity = new ServerResourceUsageHistoryEntity();
            entity.setServerId(serverMonitorInfoDTO.getUuid());
            entity.setCpuUsage(cpuUsage);
            entity.setMemoryUsage(memoryUsage);
            entity.setDiskUsage(diskUsage);

            entity.setCollectTime(date);
            entity.setCreateTime(new Date());
            saveList.add(entity);
        }
        return saveList;
    }

    private List<CbbMonitorDataDTO<Double>> processNullList(List<CbbMonitorDataDTO<Double>> monitorDataList) {
        return null == monitorDataList ? Lists.newArrayList() : monitorDataList;
    }

}
