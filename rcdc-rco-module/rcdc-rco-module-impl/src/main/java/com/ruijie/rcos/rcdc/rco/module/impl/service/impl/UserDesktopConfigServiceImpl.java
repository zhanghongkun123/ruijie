package com.ruijie.rcos.rcdc.rco.module.impl.service.impl;

import com.ruijie.rcos.rcdc.clouddesktop.module.def.api.CbbDeskSpecAPI;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.enums.CbbCloudDeskType;
import com.ruijie.rcos.rcdc.rco.module.common.query.ConditionQueryRequest;
import com.ruijie.rcos.rcdc.rco.module.def.api.SoftwareControlMgmtAPI;
import com.ruijie.rcos.rcdc.rco.module.def.api.dto.UserDesktopConfigDTO;
import com.ruijie.rcos.rcdc.rco.module.def.api.dto.UserGroupDesktopConfigDTO;
import com.ruijie.rcos.rcdc.rco.module.def.api.request.CreateUserDesktopConfigRequest;
import com.ruijie.rcos.rcdc.rco.module.def.api.request.CreateUserGroupDesktopConfigRequest;
import com.ruijie.rcos.rcdc.rco.module.def.enums.UserCloudDeskTypeEnum;
import com.ruijie.rcos.rcdc.rco.module.impl.dao.DesktopVisitorConfigDAO;
import com.ruijie.rcos.rcdc.rco.module.impl.dao.UserDesktopConfigDAO;
import com.ruijie.rcos.rcdc.rco.module.impl.dao.UserGroupDesktopConfigDAO;
import com.ruijie.rcos.rcdc.rco.module.impl.entity.DesktopVisitorConfigEntity;
import com.ruijie.rcos.rcdc.rco.module.impl.entity.UserDesktopConfigEntity;
import com.ruijie.rcos.rcdc.rco.module.impl.entity.UserGroupDesktopConfigEntity;
import com.ruijie.rcos.rcdc.rco.module.impl.service.UserDesktopConfigService;
import com.ruijie.rcos.sk.base.exception.BusinessException;
import com.ruijie.rcos.sk.base.log.Logger;
import com.ruijie.rcos.sk.base.log.LoggerFactory;
import com.ruijie.rcos.sk.pagekit.api.PageQueryRequest;
import com.ruijie.rcos.sk.pagekit.api.PageQueryResponse;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;
import org.springframework.util.ObjectUtils;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * Description: Function Description
 * Copyright: Copyright (c) 2020
 * Company: Ruijie Co., Ltd.
 * Create Time: 2020/7/22
 *
 * @author jarman
 */
@Service
public class UserDesktopConfigServiceImpl implements UserDesktopConfigService {

    private static final Logger LOGGER = LoggerFactory.getLogger(UserDesktopConfigServiceImpl.class);

    @Autowired
    private UserDesktopConfigDAO userDesktopConfigDAO;

    @Autowired
    private UserGroupDesktopConfigDAO userGroupDesktopConfigDAO;

    @Autowired
    private DesktopVisitorConfigDAO desktopVisitorConfigDAO;

    @Autowired
    private SoftwareControlMgmtAPI softwareControlMgmtAPI;

    @Autowired
    private CbbDeskSpecAPI cbbDeskSpecAPI;

    @Override
    public void createOrUpdateUserDesktopConfig(CreateUserDesktopConfigRequest request) {
        Assert.notNull(request, "request cannot be null;");
        Boolean isGlobalSoftwareStrategy = softwareControlMgmtAPI.getGlobalSoftwareStrategy();
        //全局开关关闭的情况下，软控策略以数据库为准
        if (!isGlobalSoftwareStrategy) {
            UserDesktopConfigEntity userDesktopConfigEntity =
                    userDesktopConfigDAO.findByUserIdAndDeskType(request.getUserId(), request.getDeskType());
            request.setSoftwareStrategyId(Objects.isNull(userDesktopConfigEntity) ? null : userDesktopConfigEntity.getSoftwareStrategyId());
        }
        userDesktopConfigDAO.deleteByUserIdAndDeskType(request.getUserId(), request.getDeskType());
        UserDesktopConfigEntity configEntity = new UserDesktopConfigEntity();
        configEntity.setUserId(request.getUserId());
        configEntity.setDeskType(request.getDeskType());
        configEntity.setImageTemplateId(request.getImageTemplateId());
        configEntity.setNetworkId(request.getNetworkId());
        configEntity.setStrategyId(request.getStrategyId());
        configEntity.setUserProfileStrategyId(request.getUserProfileStrategyId());
        configEntity.setCreateTime(new Date());
        configEntity.setSoftwareStrategyId(request.getSoftwareStrategyId());
        userDesktopConfigDAO.save(configEntity);
    }

    @Override
    public void createOrUpdateUserGroupDesktopConfig(CreateUserGroupDesktopConfigRequest request) {
        Assert.notNull(request, "request cannot be null;");
        Boolean isGlobalSoftwareStrategy = softwareControlMgmtAPI.getGlobalSoftwareStrategy();
        //全局开关关闭的情况下，软控策略以数据库为准
        if (!isGlobalSoftwareStrategy) {
            UserGroupDesktopConfigEntity userGroupDesktopConfigEntity =
                    userGroupDesktopConfigDAO.findByGroupIdAndDeskType(request.getGroupId(), request.getDeskType());
            request.setSoftwareStrategyId(Objects.isNull(userGroupDesktopConfigEntity) ? null : userGroupDesktopConfigEntity.getSoftwareStrategyId());
        }
        UserGroupDesktopConfigEntity oldEntity = userGroupDesktopConfigDAO.findByGroupIdAndDeskType(request.getGroupId(), request.getDeskType());
        userGroupDesktopConfigDAO.deleteByGroupIdAndDeskType(request.getGroupId(), request.getDeskType());
        if (Objects.nonNull(oldEntity) && Objects.nonNull(oldEntity.getDeskSpecId())) {
            // 删除旧的，每次修改都新增一个
            cbbDeskSpecAPI.deleteById(oldEntity.getDeskSpecId());
        }
        UserGroupDesktopConfigEntity configEntity = new UserGroupDesktopConfigEntity();
        configEntity.setGroupId(request.getGroupId());
        configEntity.setDeskType(request.getDeskType());
        configEntity.setImageTemplateId(request.getImageTemplateId());
        configEntity.setNetworkId(request.getNetworkId());
        configEntity.setStrategyId(request.getStrategyId());
        configEntity.setUserProfileStrategyId(request.getUserProfileStrategyId());
        configEntity.setSoftwareStrategyId(request.getSoftwareStrategyId());
        configEntity.setClusterId(request.getClusterId());
        configEntity.setPlatformId(request.getPlatformId());
        configEntity.setDeskSpecId(request.getDeskSpecId());
        configEntity.setCreateTime(new Date());
        userGroupDesktopConfigDAO.save(configEntity);
    }

    @Override
    public UserDesktopConfigDTO getUserDesktopConfig(UUID userId, UserCloudDeskTypeEnum deskType) {
        Assert.notNull(userId, "userId cannot be null");
        Assert.notNull(deskType, "deskType cannot be null");
        UserDesktopConfigEntity configEntity = userDesktopConfigDAO.findByUserIdAndDeskType(userId, deskType);
        if (configEntity == null) {
            // 没有用户桌面配置信息，返回null
            return null;
        }
        UserDesktopConfigDTO configDTO = new UserDesktopConfigDTO();
        BeanUtils.copyProperties(configEntity, configDTO);
        return configDTO;
    }

    @Override
    public void deleteUserDesktopConfig(UUID userId, UserCloudDeskTypeEnum deskType) {
        Assert.notNull(userId, "userId cannot be null");
        Assert.notNull(deskType, "deskType cannot be null");

        userDesktopConfigDAO.deleteByUserIdAndDeskType(userId, deskType);
    }

    @Override
    public void deleteUserGroupDesktopConfig(UUID groupId, UserCloudDeskTypeEnum deskType) {
        Assert.notNull(groupId, "userId cannot be null");
        Assert.notNull(deskType, "deskType cannot be null");

        UserGroupDesktopConfigEntity entity = userGroupDesktopConfigDAO.findByGroupIdAndDeskType(groupId, deskType);
        if (Objects.isNull(entity)) {
            return;
        }
        if (Objects.nonNull(entity.getDeskSpecId())) {
            cbbDeskSpecAPI.deleteById(entity.getDeskSpecId());
        }
        userGroupDesktopConfigDAO.deleteByGroupIdAndDeskType(groupId, deskType);
    }

    @Override
    public UserGroupDesktopConfigDTO getUserGroupDesktopConfig(UUID groupId, UserCloudDeskTypeEnum deskType) {
        Assert.notNull(groupId, "groupId cannot be null");
        Assert.notNull(deskType, "deskType cannot be null");

        UserGroupDesktopConfigEntity configEntity = userGroupDesktopConfigDAO.findByGroupIdAndDeskType(groupId, deskType);
        if (configEntity == null) {
            LOGGER.info("查找不到相关用户组ID:{}，桌面类型为{}桌面配置", groupId, deskType);
            // 未进行过桌面配置返回null
            return null;
        }
        UserGroupDesktopConfigDTO configDTO = new UserGroupDesktopConfigDTO();
        BeanUtils.copyProperties(configEntity, configDTO);

        return configDTO;
    }

    @Override
    public void deleteVisitorUserDesktopConfig(UUID userId) {
        Assert.notNull(userId, "userId cannot be null");
        DesktopVisitorConfigEntity entity = desktopVisitorConfigDAO.getOne(userId);
        // 未添加过云桌面配置信息
        if (entity == null) {
            return;
        }
        desktopVisitorConfigDAO.delete(entity);

    }

    @Override
    public List<UserGroupDesktopConfigDTO> getUserGroupDesktopConfigList(UUID networkId) {
        Assert.notNull(networkId, "networkId cannot be null");

        List<UserGroupDesktopConfigDTO> desktopConfigDTOList = new ArrayList<>();
        List<UserGroupDesktopConfigEntity> desktopConfigEntityList = userGroupDesktopConfigDAO.findByNetworkId(networkId);
        if (desktopConfigEntityList == null) {
            // 没有用户组配置过该网络策略，返回null
            return Collections.emptyList();
        }
        for (UserGroupDesktopConfigEntity entity : desktopConfigEntityList) {
            UserGroupDesktopConfigDTO desktopConfigDTO = new UserGroupDesktopConfigDTO();
            BeanUtils.copyProperties(entity, desktopConfigDTO);
            desktopConfigDTOList.add(desktopConfigDTO);
        }

        return desktopConfigDTOList;
    }

    @Override
    public Boolean isStrategyBind(UUID strategyId) {
        Assert.notNull(strategyId, "strategyId can not be nul");

        int bindUserCount = userDesktopConfigDAO.countByStrategyId(strategyId);
        int bindUserGroupCount = userGroupDesktopConfigDAO.countByStrategyId(strategyId);

        return bindUserCount > 0 || bindUserGroupCount > 0;
    }

    @Override
    public Boolean hasImageBindUserGroup(UUID imageId) {
        Assert.notNull(imageId, "imageId can not be null");

        int bindUserCount = userDesktopConfigDAO.countByImageTemplateId(imageId);
        int bindUserGroupCount = userGroupDesktopConfigDAO.countByImageTemplateId(imageId);

        return bindUserCount > 0 || bindUserGroupCount > 0;
    }

    @Override
    public List<UserGroupDesktopConfigDTO> getUserGroupDesktopConfigListByDeskType(UserCloudDeskTypeEnum deskType) {
        Assert.notNull(deskType, "deskType can not be nul");

        return convert2DTOList(userGroupDesktopConfigDAO.findByDeskType(deskType));
    }

    @Override
    public List<UserGroupDesktopConfigDTO> getUserGroupDesktopConfigListByStrategyId(UUID strategyId) {
        Assert.notNull(strategyId, "strategyId can not be nul");

        return convert2DTOList(userGroupDesktopConfigDAO.findByStrategyId(strategyId));
    }

    @Override
    public PageQueryResponse<UserGroupDesktopConfigDTO> pageQueryUserGroupDesktopConfigDTO(PageQueryRequest request) throws BusinessException {
        Assert.notNull(request, "request can not be null");
        PageQueryResponse<UserGroupDesktopConfigEntity> groupDesktopConfigEntityPageQueryResponse = userGroupDesktopConfigDAO.pageQuery(request);
        if (ObjectUtils.isEmpty(groupDesktopConfigEntityPageQueryResponse.getItemArr())) {
            return new PageQueryResponse<>(new UserGroupDesktopConfigDTO[0], 0);
        }
        UserGroupDesktopConfigDTO[] userGroupDesktopConfigDTOArr = Stream.of(groupDesktopConfigEntityPageQueryResponse.getItemArr()).map(item -> {
            UserGroupDesktopConfigDTO desktopConfigDTO = new UserGroupDesktopConfigDTO();
            desktopConfigDTO.setStoragePoolId(item.getStoragePoolId());
            desktopConfigDTO.setClusterId(item.getClusterId());
            desktopConfigDTO.setGroupId(item.getGroupId());

            CbbCloudDeskType deskType = item.getDeskType() == UserCloudDeskTypeEnum.THIRD ? null : CbbCloudDeskType.valueOf(item.getDeskType().name());
            desktopConfigDTO.setDeskType(deskType);
            desktopConfigDTO.setImageTemplateId(item.getImageTemplateId());
            desktopConfigDTO.setStrategyId(item.getStrategyId());
            desktopConfigDTO.setNetworkId(item.getNetworkId());
            desktopConfigDTO.setSoftwareStrategyId(item.getSoftwareStrategyId());
            desktopConfigDTO.setUserProfileStrategyId(item.getUserProfileStrategyId());
            desktopConfigDTO.setPlatformId(item.getPlatformId());
            desktopConfigDTO.setDeskSpecId(item.getDeskSpecId());

            return desktopConfigDTO;
        }).toArray(UserGroupDesktopConfigDTO[]::new);

        return new PageQueryResponse<>(userGroupDesktopConfigDTOArr, groupDesktopConfigEntityPageQueryResponse.getTotal());
    }

    private List<UserGroupDesktopConfigDTO> convert2DTOList(List<UserGroupDesktopConfigEntity> entityList) {
        if (CollectionUtils.isEmpty(entityList)) {
            return new ArrayList<>();
        }
        return entityList.stream().map(item -> {
            UserGroupDesktopConfigDTO configDTO = new UserGroupDesktopConfigDTO();
            BeanUtils.copyProperties(item, configDTO);
            return configDTO;
        }).collect(Collectors.toList());
    }
}
