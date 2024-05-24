package com.ruijie.rcos.rcdc.rco.module.impl.desktoppool.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.google.common.collect.ImmutableList;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.dto.desk.CbbAddExtraDiskDTO;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.enums.CbbCloudDeskState;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.enums.desktoppool.CbbDesktopPoolModel;
import com.ruijie.rcos.rcdc.hciadapter.module.def.VgpuUtil;
import com.ruijie.rcos.rcdc.hciadapter.module.def.dto.gpu.VgpuExtraInfo;
import com.ruijie.rcos.rcdc.hciadapter.module.def.dto.gpu.VgpuExtraInfoSupport;
import com.ruijie.rcos.rcdc.hciadapter.module.def.dto.gpu.VgpuInfoDTO;
import com.ruijie.rcos.rcdc.hciadapter.module.def.enums.VgpuType;
import com.ruijie.rcos.rcdc.rco.module.common.query.ConditionQueryRequest;
import com.ruijie.rcos.rcdc.rco.module.def.api.request.PageSearchRequest;
import com.ruijie.rcos.rcdc.rco.module.def.deskspec.dto.ExtraDiskDTO;
import com.ruijie.rcos.rcdc.rco.module.def.desktoppool.dto.DesktopPoolBasicDTO;
import com.ruijie.rcos.rcdc.rco.module.def.desktoppool.dto.PoolDesktopInfoDTO;
import com.ruijie.rcos.rcdc.rco.module.impl.api.dto.DefaultDataSort;
import com.ruijie.rcos.rcdc.rco.module.impl.desktoppool.DesktopPoolBusinessKey;
import com.ruijie.rcos.rcdc.rco.module.impl.desktoppool.dao.DesktopPoolDetailDAO;
import com.ruijie.rcos.rcdc.rco.module.impl.desktoppool.dao.PoolDesktopInfoDAO;
import com.ruijie.rcos.rcdc.rco.module.impl.desktoppool.dto.PoolModelOverviewDTO;
import com.ruijie.rcos.rcdc.rco.module.impl.desktoppool.dto.UserBindDesktopNumDTO;
import com.ruijie.rcos.rcdc.rco.module.impl.desktoppool.entity.DesktopPoolDetailEntity;
import com.ruijie.rcos.rcdc.rco.module.impl.desktoppool.entity.ViewPoolDesktopInfoEntity;
import com.ruijie.rcos.rcdc.rco.module.impl.desktoppool.service.DesktopPoolService;
import com.ruijie.rcos.rcdc.rco.module.impl.service.AbstractPageQueryTemplate;
import com.ruijie.rcos.rcdc.rco.module.impl.service.EntityFieldMapper;
import com.ruijie.rcos.rcdc.rco.module.impl.util.CapacityUnitUtils;
import com.ruijie.rcos.sk.base.exception.BusinessException;
import com.ruijie.rcos.sk.modulekit.api.comm.DefaultPageResponse;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

/**
 * Description: 桌面池service
 * Copyright: Copyright (c) 2021
 * Company: Ruijie Co., Ltd.
 * Create Time:
 *
 * @author linke
 */
@Service("rcoDesktopPoolService")
public class DesktopPoolServiceImpl extends AbstractPageQueryTemplate<DesktopPoolDetailEntity> implements DesktopPoolService {

    private static final String NAME = "name";
    
    private static final String PLATFORM_NAME = "platformName";

    private static final String CREATE_TIME = "createTime";

    @Autowired
    private PoolDesktopInfoDAO poolDesktopInfoDAO;

    @Autowired
    private DesktopPoolDetailDAO desktopPoolDetailDAO;

    @Override
    public List<PoolDesktopInfoDTO> listNormalDeskInfoByDesktopPoolId(UUID desktopPoolId) {
        Assert.notNull(desktopPoolId, "desktopPoolId must not be null");
        List<ViewPoolDesktopInfoEntity> desktopInfoEntityList = poolDesktopInfoDAO.findAllByDesktopPoolId(desktopPoolId);
        desktopInfoEntityList = desktopInfoEntityList.stream().filter(this::checkNotNormalStateDesktop).collect(Collectors.toList());
        return convert2PoolDesktopInfoDTOList(desktopInfoEntityList);
    }

    private boolean checkNotNormalStateDesktop(ViewPoolDesktopInfoEntity desktop) {
        CbbCloudDeskState state = desktop.getDeskState();
        return Boolean.FALSE.equals(desktop.getIsDelete()) && state != CbbCloudDeskState.RECYCLE_BIN && state != CbbCloudDeskState.DELETING
                && state != CbbCloudDeskState.CREATING && state != CbbCloudDeskState.COMPLETE_DELETING;
    }

    @Override
    public List<PoolDesktopInfoDTO> listDesktopByDesktopPoolIds(List<UUID> desktopPoolIdList) {
        Assert.notNull(desktopPoolIdList, "desktopPoolIdList must not be null");

        if (CollectionUtils.isEmpty(desktopPoolIdList)) {
            return Collections.emptyList();
        }
        List<ViewPoolDesktopInfoEntity> desktopInfoEntityList = poolDesktopInfoDAO.findAllByDesktopPoolIdIn(desktopPoolIdList);
        return convert2PoolDesktopInfoDTOList(desktopInfoEntityList);
    }

    @Override
    public List<UserBindDesktopNumDTO> listUserBindPoolDesktopNum(List<UUID> desktopIdList, CbbDesktopPoolModel poolModel) {
        Assert.notNull(desktopIdList, "desktopIdList must not be null");
        Assert.notNull(poolModel, "poolModel must not be null");
        if (CollectionUtils.isEmpty(desktopIdList)) {
            return Collections.emptyList();
        }
        return poolDesktopInfoDAO.findUserBindDesktopNumByDeskIdsAndPoolModel(desktopIdList, poolModel);
    }

    @Override
    public List<PoolDesktopInfoDTO> listNormalDesktopByPoolIdAndUserGroupIds(UUID desktopPoolId, List<UUID> userGroupIdList) {
        Assert.notNull(desktopPoolId, "desktopPoolId can not be null");
        Assert.notEmpty(userGroupIdList, "userGroupIdList can not be null");

        List<ViewPoolDesktopInfoEntity> desktopList = poolDesktopInfoDAO.findByDesktopPoolIdAndUserGroupIdIn(desktopPoolId, userGroupIdList);
        desktopList = desktopList.stream().filter(item -> item.getDeskState() != CbbCloudDeskState.RECYCLE_BIN).collect(Collectors.toList());
        return convert2PoolDesktopInfoDTOList(desktopList);
    }

    @Override
    public DefaultPageResponse<DesktopPoolBasicDTO> pageDesktopPool(PageSearchRequest request) {
        Assert.notNull(request, "request must not be null");
        Page<DesktopPoolDetailEntity> page = super.pageQuery(request, DesktopPoolDetailEntity.class);
        DefaultPageResponse<DesktopPoolBasicDTO> resp = new DefaultPageResponse<>();
        resp.setTotal(page.getTotalElements());
        if (CollectionUtils.isEmpty(page.getContent())) {
            resp.setItemArr(new DesktopPoolBasicDTO[0]);
            return resp;
        }
        resp.setItemArr(convert2DesktopPoolBasicDTOList(page.getContent()).toArray(new DesktopPoolBasicDTO[0]));
        return resp;
    }

    @Override
    public List<DesktopPoolBasicDTO> listDesktopPoolByPoolModel(List<CbbDesktopPoolModel> poolModelList) {
        Assert.notEmpty(poolModelList, "poolModelList must not be empty");

        List<DesktopPoolDetailEntity> entityList = desktopPoolDetailDAO.findByPoolModelIn(poolModelList);
        return convert2DesktopPoolBasicDTOList(entityList);
    }

    @Override
    public DesktopPoolBasicDTO getDesktopPoolBasicById(UUID id) throws BusinessException {
        Assert.notNull(id, "id must not be null");
        Optional<DesktopPoolDetailEntity> entityOptional = desktopPoolDetailDAO.findById(id);

        if (!entityOptional.isPresent()) {
            throw new BusinessException(DesktopPoolBusinessKey.RCDC_RCO_DESKTOP_POOL_NOT_EXIST, String.valueOf(id));
        }

        return convert2DesktopPoolBasicDTO(entityOptional.get());
    }

    @Override
    public List<DesktopPoolBasicDTO> listByConditions(ConditionQueryRequest request) throws BusinessException {
        Assert.notNull(request, "request can not be null");
        List<DesktopPoolDetailEntity> desktopPoolDetailEntityList = desktopPoolDetailDAO.listByConditions(request);
        return convert2DesktopPoolBasicDTOList(desktopPoolDetailEntityList);
    }

    @Override
    public long countByConditions(ConditionQueryRequest request) throws BusinessException {
        Assert.notNull(request, "request can not be null");
        return desktopPoolDetailDAO.countByConditions(request);
    }

    @Override
    public List<DesktopPoolBasicDTO> listByUserProfileStrategyId(UUID userProfileStrategyId) {
        Assert.notNull(userProfileStrategyId, "userProfileStrategyId must not be null");

        List<DesktopPoolDetailEntity> desktopPoolDetailEntityList = desktopPoolDetailDAO.findAllByUserProfileStrategyId(userProfileStrategyId);
        return convert2DesktopPoolBasicDTOList(desktopPoolDetailEntityList);
    }

    @Override
    public List<DesktopPoolBasicDTO> listByImageId(UUID imageTemplateId) {
        Assert.notNull(imageTemplateId, "imageTemplateId must not be null");

        List<DesktopPoolDetailEntity> desktopPoolDetailEntityList = desktopPoolDetailDAO.findAllByImageTemplateId(imageTemplateId);
        return convert2DesktopPoolBasicDTOList(desktopPoolDetailEntityList);
    }

    @Override
    public List<PoolDesktopInfoDTO> listBindUserDesktopByPoolId(UUID desktopPoolId) {
        Assert.notNull(desktopPoolId, "desktopPoolId must not be null");
        List<ViewPoolDesktopInfoEntity> desktopList = poolDesktopInfoDAO.findByDesktopPoolIdAndUserIdNotNull(desktopPoolId);
        desktopList = desktopList.stream().filter(item -> item.getDeskState() != CbbCloudDeskState.RECYCLE_BIN).collect(Collectors.toList());
        return convert2PoolDesktopInfoDTOList(desktopList);
    }

    @Override
    public PoolDesktopInfoDTO getUserBindPoolDesktop(UUID userId, UUID desktopPoolId) {
        Assert.notNull(userId, "userId must not be null");
        Assert.notNull(desktopPoolId, "desktopPoolId must not be null");
        List<ViewPoolDesktopInfoEntity> desktopList = poolDesktopInfoDAO.findByDesktopPoolIdAndUserId(desktopPoolId, userId);
        Optional<ViewPoolDesktopInfoEntity> optional = desktopList.stream().filter(item -> item.getDeskState() != CbbCloudDeskState.RECYCLE_BIN)
                .findFirst();
        return optional.map(this::convert2PoolDesktopInfoDTO).orElse(null);
    }

    @Override
    public List<PoolModelOverviewDTO> countPoolOverviewByModel(List<CbbDesktopPoolModel> poolModelList) {
        Assert.notEmpty(poolModelList, "poolModelList must not be null");
        List<String> modelStrList = poolModelList.stream().map(Enum::name).collect(Collectors.toList());
        List<Map<String, Object>> queryResultList = poolDesktopInfoDAO.countPoolOverviewByPoolModel(modelStrList);
        List<PoolModelOverviewDTO> overviewDTOList = new ArrayList<>();
        queryResultList.forEach(item -> {
            PoolModelOverviewDTO overviewDTO = JSON.parseObject(JSON.toJSONString(item), PoolModelOverviewDTO.class);
            overviewDTOList.add(overviewDTO);
        });
        return overviewDTOList;
    }

    @Override
    public List<PoolModelOverviewDTO> countPoolOverviewByIds(List<UUID> poolIdList) {
        Assert.notEmpty(poolIdList, "poolIdList must not be null");
        List<Map<String, Object>> queryResultList = poolDesktopInfoDAO.countPoolOverviewByPoolId(poolIdList);
        List<PoolModelOverviewDTO> overviewDTOList = new ArrayList<>();
        queryResultList.forEach(item -> {
            PoolModelOverviewDTO overviewDTO = JSON.parseObject(JSON.toJSONString(item), PoolModelOverviewDTO.class);
            overviewDTOList.add(overviewDTO);
        });
        return overviewDTOList;
    }

    @Override
    public PoolDesktopInfoDTO getPoolDeskInfoByDeskId(UUID deskId) {
        Assert.notNull(deskId, "userId must not be null");
        ViewPoolDesktopInfoEntity desktopInfoEntity = poolDesktopInfoDAO.findByDeskId(deskId);
        if (Objects.isNull(desktopInfoEntity)) {
            // null
            return null;
        }
        return convert2PoolDesktopInfoDTO(desktopInfoEntity);
    }

    private List<PoolDesktopInfoDTO> convert2PoolDesktopInfoDTOList(List<ViewPoolDesktopInfoEntity> desktopInfoEntityList) {
        if (CollectionUtils.isEmpty(desktopInfoEntityList)) {
            return Collections.emptyList();
        }

        return desktopInfoEntityList.stream().map(this::convert2PoolDesktopInfoDTO).collect(Collectors.toList());
    }

    private PoolDesktopInfoDTO convert2PoolDesktopInfoDTO(ViewPoolDesktopInfoEntity entity) {
        PoolDesktopInfoDTO desktopInfoDTO = new PoolDesktopInfoDTO();
        BeanUtils.copyProperties(entity, desktopInfoDTO);

        VgpuType vgpuType = entity.getVgpuType();
        VgpuExtraInfoSupport vgpuExtraInfoSupport = VgpuUtil.deserializeVgpuExtraInfoByType(vgpuType,
                entity.getVgpuExtraInfo());
        desktopInfoDTO.setVgpuInfoDTO(new VgpuInfoDTO(vgpuType, vgpuExtraInfoSupport));
        return desktopInfoDTO;
    }

    private List<DesktopPoolBasicDTO> convert2DesktopPoolBasicDTOList(List<DesktopPoolDetailEntity> desktopPoolEntityList) {
        if (CollectionUtils.isEmpty(desktopPoolEntityList)) {
            return Collections.emptyList();
        }

        return desktopPoolEntityList.stream().map(this::convert2DesktopPoolBasicDTO).collect(Collectors.toList());
    }

    private DesktopPoolBasicDTO convert2DesktopPoolBasicDTO(DesktopPoolDetailEntity desktopPoolDetailEntity) {
        DesktopPoolBasicDTO desktopPoolDTO = new DesktopPoolBasicDTO();
        BeanUtils.copyProperties(desktopPoolDetailEntity, desktopPoolDTO);
        if (Objects.nonNull(desktopPoolDetailEntity.getMemory())) {
            desktopPoolDTO.setMemory(CapacityUnitUtils.mb2Gb(desktopPoolDetailEntity.getMemory()));
        }
        desktopPoolDTO.setCanUpgradeAgent(desktopPoolDetailEntity.getCanUpgradeAgent());
        desktopPoolDTO.setPersonDisk(desktopPoolDetailEntity.getPersonSize());
        desktopPoolDTO.setSystemDisk(desktopPoolDetailEntity.getSystemSize());
        desktopPoolDTO.setConnectedNum(Optional.ofNullable(desktopPoolDetailEntity.getConnectedNum()).orElse(0));
        if (StringUtils.isNotEmpty(desktopPoolDetailEntity.getExtraDiskInfo())) {
            List<CbbAddExtraDiskDTO> extraDiskList = JSONObject.parseArray(desktopPoolDetailEntity.getExtraDiskInfo(), CbbAddExtraDiskDTO.class);
            desktopPoolDTO.setExtraDiskList(extraDiskList.stream().map(item -> {
                ExtraDiskDTO extraDiskDTO = new ExtraDiskDTO();
                extraDiskDTO.setIndex(item.getIndex());
                extraDiskDTO.setDiskId(item.getDiskId());
                extraDiskDTO.setExtraSize(item.getExtraSize());
                extraDiskDTO.setAssignedStoragePoolId(item.getAssignedStoragePoolId());
                return extraDiskDTO;
            }).collect(Collectors.toList()));
        }
        if (StringUtils.isNotBlank(desktopPoolDetailEntity.getVgpuExtraInfo())) {
            desktopPoolDTO.setVgpuExtraInfo(JSON.parseObject(desktopPoolDetailEntity.getVgpuExtraInfo(), VgpuExtraInfo.class));
        }
        return desktopPoolDTO;
    }

    @Override
    protected List<String> getSearchColumn() {
        return ImmutableList.of(NAME, PLATFORM_NAME);
    }

    @Override
    protected DefaultDataSort getDefaultDataSort() {
        return new DefaultDataSort(CREATE_TIME, Sort.Direction.DESC);
    }

    @Override
    protected void mappingField(EntityFieldMapper entityFieldMapper) {

    }

    @Override
    protected Page<DesktopPoolDetailEntity> find(Specification<DesktopPoolDetailEntity> specification, Pageable pageable) {
        if (specification == null) {
            return desktopPoolDetailDAO.findAll(pageable);
        }
        return desktopPoolDetailDAO.findAll(specification, pageable);
    }
}
