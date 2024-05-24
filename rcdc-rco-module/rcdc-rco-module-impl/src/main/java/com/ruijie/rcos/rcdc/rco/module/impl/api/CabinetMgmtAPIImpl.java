package com.ruijie.rcos.rcdc.rco.module.impl.api;

import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.api.CbbPhysicalServerMgmtAPI;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.dto.PhysicalServerDTO;
import com.ruijie.rcos.rcdc.rco.module.def.api.CabinetMgmtAPI;
import com.ruijie.rcos.rcdc.rco.module.def.api.dto.bigscreen.CabinetDTO;
import com.ruijie.rcos.rcdc.rco.module.def.api.dto.bigscreen.CabinetMappingServerDTO;
import com.ruijie.rcos.rcdc.rco.module.def.api.dto.bigscreen.PhysicalServerInfoDTO;
import com.ruijie.rcos.rcdc.rco.module.def.api.request.PageSearchRequest;
import com.ruijie.rcos.rcdc.rco.module.def.api.request.bigscreen.CabinetRequest;
import com.ruijie.rcos.rcdc.rco.module.def.api.request.bigscreen.ConfigServerRequest;
import com.ruijie.rcos.rcdc.rco.module.def.api.request.bigscreen.DeleteServerCabinetRelationRequest;
import com.ruijie.rcos.rcdc.rco.module.def.api.request.bigscreen.NameRequest;
import com.ruijie.rcos.rcdc.rco.module.impl.BusinessKey;
import com.ruijie.rcos.rcdc.rco.module.impl.dao.CabinetDAO;
import com.ruijie.rcos.rcdc.rco.module.impl.dao.CabinetServerMappingDAO;
import com.ruijie.rcos.rcdc.rco.module.impl.entity.CabinetEntity;
import com.ruijie.rcos.rcdc.rco.module.impl.entity.CabinetServerMappingEntity;
import com.ruijie.rcos.rcdc.rco.module.impl.service.ServerService;
import com.ruijie.rcos.rcdc.rco.module.impl.service.impl.QueryCabinetServiceImpl;
import com.ruijie.rcos.rcdc.rco.module.impl.tx.CabinetServiceTx;
import com.ruijie.rcos.sk.base.exception.BusinessException;
import com.ruijie.rcos.sk.base.log.Logger;
import com.ruijie.rcos.sk.base.log.LoggerFactory;
import com.ruijie.rcos.sk.modulekit.api.comm.DefaultPageResponse;
import com.ruijie.rcos.sk.modulekit.api.comm.IdRequest;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.util.Assert;
import org.springframework.util.CollectionUtils;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * Description: 机柜管理API接口实现
 * Copyright: Copyright (c) 2019
 * Company: Ruijie Co., Ltd.
 * Create Time: 2019年7月24日
 *
 * @author bgl
 */
public class CabinetMgmtAPIImpl implements CabinetMgmtAPI {

    private static final Logger LOGGER = LoggerFactory.getLogger(CabinetMgmtAPIImpl.class);

    @Autowired
    private QueryCabinetServiceImpl queryCabinetService;

    @Autowired
    private CabinetDAO cabinetDAO;

    @Autowired
    private CabinetServiceTx cabinetServiceTx;

    @Autowired
    private CabinetServerMappingDAO cabinetServerMappingDAO;

    @Autowired
    private CbbPhysicalServerMgmtAPI cbbPhysicalServerMgmtAPI;

    @Autowired
    private ServerService serverService;

    private static final int MAX_SERVERS = 15;

    private static final int SERVER_SIZE = 3;

    @Override
    public DefaultPageResponse<CabinetDTO> list(PageSearchRequest request) {
        Assert.notNull(request, "PageSearchRequest不能为null");

        Page<CabinetEntity> cabinetEntityPage = queryCabinetService.pageQuery(request, CabinetEntity.class);
        List<CabinetEntity> entityList = cabinetEntityPage.getContent();
        CabinetDTO[] cabinetDTOArr = entityList.stream().map(o -> {
            CabinetDTO dto = new CabinetDTO();
            BeanUtils.copyProperties(o, dto);
            return dto;
        }).collect(Collectors.toList()).toArray(new CabinetDTO[]{});

        DefaultPageResponse<CabinetDTO> response = new DefaultPageResponse<>();
        response.setTotal(cabinetEntityPage.getTotalElements());
        response.setItemArr(cabinetDTOArr);
        return response;
    }

    @Override
    public CabinetDTO detail(IdRequest request) throws BusinessException {
        Assert.notNull(request, "IdRequest不能为null");

        CabinetEntity entity = cabinetDAO.getOne(request.getId());
        if (entity == null) {
            LOGGER.error("cabinet info is not exist, id = {}", request.getId());
            throw new BusinessException(BusinessKey.RCDC_RCO_CABINET_NOT_EXIST, request.getId().toString());
        }

        CabinetDTO cabinetDTO = new CabinetDTO();
        BeanUtils.copyProperties(entity, cabinetDTO);
        Set<Integer> usedLocationSet = getUsedLocationSet(request.getId());
        cabinetDTO.setLocationUsed(usedLocationSet.size());

        return cabinetDTO;
    }

    @Override
    public void edit(CabinetRequest request) throws BusinessException {
        Assert.notNull(request, "CabinetRequest不能为空");
        Assert.notNull(request.getId(), "机柜id不能为空");

        CabinetEntity resultEntity = cabinetDAO.getByName(request.getName());
        if (null != resultEntity && !resultEntity.getId().equals(request.getId())) {
            LOGGER.error("cabinet name existed, cabinet name[{}]", resultEntity.getName());
            throw new BusinessException(BusinessKey.RCDC_RCO_CABINET_VALIDATE_EDIT_CABINET_NAME_EXIST,
                resultEntity.getName());
        }

        CabinetEntity entity = cabinetDAO.getOne(request.getId());
        if (null == entity) {
            LOGGER.error("cabinet info is not exist, id = {}", request.getId());
            throw new BusinessException(BusinessKey.RCDC_RCO_CABINET_NOT_EXIST, request.getId().toString());
        }

        entity.setDescription(request.getDescription());
        entity.setName(request.getName());
        cabinetDAO.save(entity);
    }

    @Override
    public void create(CabinetRequest createRequest) throws BusinessException {
        Assert.notNull(createRequest, "CabinetRequest不能为空");

        CabinetEntity resultEntity = cabinetDAO.getByName(createRequest.getName());
        if (null != resultEntity) {
            LOGGER.error("cabinet name existed, cabinet name[{}]", resultEntity.getName());
            throw new BusinessException(BusinessKey.RCDC_RCO_CABINET_VALIDATE_CREATE_CABINET_NAME_EXIST,
                resultEntity.getName());
        }

        CabinetEntity entity = new CabinetEntity();
        entity.setDescription(createRequest.getDescription());
        entity.setName(createRequest.getName());
        entity.setCreateTime(new Date());
        entity.setServerNum(0);
        cabinetDAO.save(entity);
    }

    @Override
    public void delete(IdRequest request) throws BusinessException {
        Assert.notNull(request, "IdRequest不能为空");

        CabinetEntity entity = cabinetDAO.getOne(request.getId());
        if (null == entity) {
            LOGGER.error("cabinet info is not exist, id = {}", request.getId());
            throw new BusinessException(BusinessKey.RCDC_RCO_CABINET_NOT_EXIST, request.getId().toString());
        }
        LOGGER.warn("delete cabinet by id[{}]", request.getId());
        cabinetServiceTx.deleteCabinet(request.getId());
    }

    @Override
    public void configServer(ConfigServerRequest request) throws BusinessException {
        Assert.notNull(request, "ConfigServerRequest不能为空");

        validateConfigServerRequest(request);

        CabinetMappingServerDTO dto = new CabinetMappingServerDTO();
        BeanUtils.copyProperties(request, dto);

        cabinetServiceTx.configServer(dto);
    }

    private void validateConfigServerRequest(ConfigServerRequest request) throws BusinessException {
        CabinetEntity cabinetEntity = cabinetDAO.getOne(request.getCabinetId());
        if (cabinetEntity == null) {
            LOGGER.error("机柜[{}]不存在", request.getCabinetId());
            throw new BusinessException(BusinessKey.RCDC_RCO_CABINET_NOT_EXIST, request.getCabinetId().toString());
        }

        PhysicalServerDTO physicalServerDTO = cbbPhysicalServerMgmtAPI.getPhysicalServer(request.getServerId());
        if (null == physicalServerDTO) {
            LOGGER.error("服务器信息不存在，serverId = {}", request.getServerId());
            throw new BusinessException(BusinessKey.RCDC_RCO_CABINET_SERVER_INFO_NOT_EXIST);
        }

        // 2、判断服务器是否已分配其他机柜
        CabinetServerMappingEntity entity = cabinetServerMappingDAO.getByServerId(request.getServerId());
        if (null != entity) {
            throw new BusinessException(BusinessKey.RCDC_RCO_CABINET_VALIDATE_CONFIG_SERVER_ONE_OVER);
        }

        // 3、判断机柜上服务器数量是否已达上线：15
        List<CabinetServerMappingEntity> entityList =
            cabinetServerMappingDAO.findAllByCabinetId(request.getCabinetId());
        if (entityList.size() >= MAX_SERVERS) {
            throw new BusinessException(BusinessKey.RCDC_RCO_CABINET_VALIDATE_CONFIG_SERVER_NUM_OVER);
        }

        // 4、判断服务器起止位置关系是否正确，以及位置是否被占用
        checkServerLocation(request);
    }

    /**
     * 判断是否存在重复机柜位置的服务器以及始末位置占用不大于3U
     *
     * @param request 结束位置
     * @throws BusinessException 业务异常
     */
    private void checkServerLocation(ConfigServerRequest request) throws BusinessException {
        int locationBegin = request.getCabinetLocationBegin();
        int locationEnd = request.getCabinetLocationEnd();

        // 3.1、判断起止位置关系是否正确
        if (locationBegin > locationEnd) {
            throw new BusinessException(BusinessKey.RCDC_RCO_CABINET_CONFIG_SERVER_LOCATION_SET_ERROR);
        }
        if ((locationEnd - locationBegin) >= SERVER_SIZE) {
            throw new BusinessException(BusinessKey.RCDC_RCO_CABINET_CONFIG_SERVER_LOCATION_SET_OVER_LIMIT);
        }

        // 3.2、判断位置是否被占用
        Set<Integer> locationSet = getUsedLocationSet(request.getCabinetId());
        for (int flag = locationBegin; flag <= locationEnd; flag++) {
            if (locationSet.contains(flag)) {
                throw new BusinessException(BusinessKey.RCDC_RCO_CABINET_CONFIG_SERVER_LOCATION_USED);
            }
        }
    }

    /**
     * 获得这个机柜已使用的位置信息
     *
     * @param cabinetId 机柜Id
     */
    private Set<Integer> getUsedLocationSet(UUID cabinetId) {
        Set<Integer> usedLocationSet = Sets.newHashSet();
        List<CabinetServerMappingEntity> mappingEntityList = cabinetServerMappingDAO.findAllByCabinetId(cabinetId);
        for (CabinetServerMappingEntity entity : mappingEntityList) {
            for (int i = entity.getCabinetLocationBegin(); i <= entity.getCabinetLocationEnd(); i ++) {
                usedLocationSet.add(i);
            }
        }
        return usedLocationSet;
    }

    @Override
    public void deleteServerFromCabinet(DeleteServerCabinetRelationRequest request) throws BusinessException {
        Assert.notNull(request, "request不能为空");

        CabinetServerMappingEntity entity = cabinetServerMappingDAO.getByServerId(request.getServerId());
        if (null == entity) {
            // 机柜和服务器映射关系已被删除
            throw new BusinessException(BusinessKey.RCDC_RCO_CABINET_SERVER_HAS_BEEN_DELETED);
        }

        if (!request.getCabinetId().equals(entity.getCabinetId())) {
            // 该服务器不属于当前机柜
            LOGGER.error("该服务器不属于当前机柜，request = {}，entity = {}", request.getCabinetId(), entity.getCabinetId());
            throw new BusinessException(BusinessKey.RCDC_RCO_CABINET_SERVER_NOT_BELONG_THIS_CABINET);
        }

        List<UUID> serverIdList = Lists.newArrayList();
        serverIdList.add(request.getServerId());
        cabinetServiceTx.deleteServerFromCabinet(request.getCabinetId(), serverIdList);
    }

    @Override
    public CabinetDTO getCabinetByName(NameRequest request) {
        Assert.notNull(request, "request不能为空");

        CabinetDTO cabinetDTO = new CabinetDTO();
        CabinetEntity cabinetEntity = cabinetDAO.getByName(request.getName());
        if (null == cabinetEntity) {
            return cabinetDTO;
        }

        BeanUtils.copyProperties(cabinetEntity, cabinetDTO);
        return cabinetDTO;
    }

    @Override
    public List<PhysicalServerInfoDTO> listCabinetConfigedServer(IdRequest request) {
        Assert.notNull(request, "PageSearchRequest不能为null");

        List<PhysicalServerInfoDTO> physicalServerInfoList = serverService.listAllPhysicalServer();
        if (CollectionUtils.isEmpty(physicalServerInfoList)) {
            return Lists.newArrayList();
        }
        Map<UUID, PhysicalServerInfoDTO> serverInfoDTOMap = physicalServerInfoList.stream()
            .collect(Collectors.toMap(PhysicalServerInfoDTO::getId, Function.identity()));

        List<CabinetServerMappingEntity> entityList = cabinetServerMappingDAO.findAllByCabinetId(request.getId());

        return entityList.stream().filter(entity -> null != serverInfoDTOMap.get(entity.getServerId())
        ).map(entity -> serverInfoDTOMap.get(entity.getServerId())).collect(Collectors.toList());
    }
}