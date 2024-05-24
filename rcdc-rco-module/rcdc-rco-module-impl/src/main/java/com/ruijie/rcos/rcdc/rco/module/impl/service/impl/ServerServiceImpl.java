package com.ruijie.rcos.rcdc.rco.module.impl.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.api.CbbPhysicalServerMgmtAPI;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.dto.PhysicalServerDTO;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.dto.desk.CbbListServerMonitorInfoDTO;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.dto.desk.CbbServerMonitorInfoDTO;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.dto.desk.CbbServerRequestDTO;
import com.ruijie.rcos.rcdc.rco.module.def.api.dto.bigscreen.PhysicalServerInfoDTO;
import com.ruijie.rcos.rcdc.rco.module.impl.dao.CabinetDAO;
import com.ruijie.rcos.rcdc.rco.module.impl.dao.CabinetServerMappingDAO;
import com.ruijie.rcos.rcdc.rco.module.impl.entity.CabinetEntity;
import com.ruijie.rcos.rcdc.rco.module.impl.entity.CabinetServerMappingEntity;
import com.ruijie.rcos.rcdc.rco.module.impl.service.ServerService;
import com.ruijie.rcos.rcdc.rco.module.impl.util.DateUtil;
import com.ruijie.rcos.sk.base.exception.BusinessException;
import com.ruijie.rcos.sk.base.log.Logger;
import com.ruijie.rcos.sk.base.log.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;
import org.springframework.util.CollectionUtils;

import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

/**
 * 服务器管理Service接口实现
 * Copyright: Copyright (c) 2018
 * Company: Ruijie Co., Ltd.
 * Create Time: 2019年8月10日
 *
 * @author brq
 */
@Service
public class ServerServiceImpl implements ServerService {

    private static final Logger LOGGER = LoggerFactory.getLogger(ServerServiceImpl.class);

    @Autowired
    private CbbPhysicalServerMgmtAPI cbbPhysicalServerMgmtAPI;

    @Autowired
    private CabinetServerMappingDAO cabinetServerMappingDAO;

    @Autowired
    private CabinetDAO cabinetDAO;

    @Override
    public List<CbbServerMonitorInfoDTO> getServerMonitorInfo(
            List<CbbServerRequestDTO> serverRequestDTOList, LocalDateTime startTime, LocalDateTime endTime) throws BusinessException {
        Assert.notNull(serverRequestDTOList, "serverRequestDTOList is null!");
        Assert.notNull(startTime, "startTime is null!");
        Assert.notNull(endTime, "endTime is null!");

        CbbListServerMonitorInfoDTO monitorInfoRequest = new CbbListServerMonitorInfoDTO();
        monitorInfoRequest.setServerRequestDTOArr(serverRequestDTOList.toArray(new CbbServerRequestDTO[0]));
        monitorInfoRequest.setStartTime(DateUtil.localDateTimeToSecondLong(startTime));
        monitorInfoRequest.setEndTime(DateUtil.localDateTimeToSecondLong(endTime));

        List<CbbServerMonitorInfoDTO> serverMonitorInfoDTOList = cbbPhysicalServerMgmtAPI.listServerMonitorInfo(monitorInfoRequest);
        if (CollectionUtils.isEmpty(serverMonitorInfoDTOList)) {
            LOGGER.error("未获取到服务器监控信息，monitorInfoRequest = {}，monitorInfoResponse = {}",
                JSONObject.toJSONString(monitorInfoRequest), JSONObject.toJSONString(serverMonitorInfoDTOList));
            return Lists.newArrayList();
        }
        return serverMonitorInfoDTOList;
    }

    @Override
    public List<PhysicalServerInfoDTO> listAllPhysicalServer() {

        List<PhysicalServerDTO> physicalServerDTOList =
            cbbPhysicalServerMgmtAPI.listAllPhysicalServer(true);

        if (CollectionUtils.isEmpty(physicalServerDTOList)) {
            return Lists.newArrayList();
        }
        List<PhysicalServerInfoDTO> physicalServerInfoDTOList =
                physicalServerDTOList.stream().map(this::generatePhysicalServerInfoDTO).sorted(
                Comparator.comparing(PhysicalServerInfoDTO::getIp).reversed())
                .collect(Collectors.toList());

        // 标记已经被分配了的服务器
        buildConfiguredServer(physicalServerInfoDTOList);

        return physicalServerInfoDTOList;
    }

    private PhysicalServerInfoDTO generatePhysicalServerInfoDTO(PhysicalServerDTO physicalServerDTO) {
        PhysicalServerInfoDTO physicalServerInfoDTO = new PhysicalServerInfoDTO();
        BeanUtils.copyProperties(physicalServerDTO, physicalServerInfoDTO);
        physicalServerInfoDTO.setTotalCpuNum(physicalServerDTO.getCpuInfo().getTotalCpuNum());
        physicalServerInfoDTO.setTotalMemory(physicalServerDTO.getMemoryInfo().getTotalMemory());
        physicalServerInfoDTO.setTotalStorage(physicalServerDTO.getStorageInfo().getTotalStorage());
        return physicalServerInfoDTO;
    }

    private void buildConfiguredServer(List<PhysicalServerInfoDTO> physicalServerInfoDTOList) {
        List<CabinetServerMappingEntity> entityList = cabinetServerMappingDAO.findAll();

        Map<UUID, CabinetServerMappingEntity> mappingServerDTOMap = Maps.newHashMap();
        for (CabinetServerMappingEntity entity : entityList) {
            mappingServerDTOMap.put(entity.getServerId(), entity);
        }

        for (PhysicalServerInfoDTO physicalServerInfoDTO : physicalServerInfoDTOList) {
            CabinetServerMappingEntity entity = mappingServerDTOMap.get(physicalServerInfoDTO.getId());
            if (null != entity) {
                CabinetEntity cabinetEntity = cabinetDAO.getOne(entity.getCabinetId());
                physicalServerInfoDTO.setConfiged(true);
                physicalServerInfoDTO.setCabinetName(cabinetEntity == null ? null : cabinetEntity.getName());
                BeanUtils.copyProperties(entity, physicalServerInfoDTO);
                physicalServerInfoDTO.setId(entity.getServerId());
            }
        }
    }

}
