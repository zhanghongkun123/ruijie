package com.ruijie.rcos.rcdc.rco.module.impl.softwarecontrol.tx.impl;

import com.ruijie.rcos.rcdc.rco.module.def.softwarecontrol.dto.SoftwareGroupDTO;
import com.ruijie.rcos.rcdc.rco.module.def.softwarecontrol.dto.SoftwareStrategyDTO;
import com.ruijie.rcos.rcdc.rco.module.def.softwarecontrol.dto.SoftwareStrategyDetailDTO;
import com.ruijie.rcos.rcdc.rco.module.def.softwarecontrol.enums.SoftwareRelationTypeEnum;
import com.ruijie.rcos.rcdc.rco.module.def.softwarecontrol.enums.SoftwareStrategyRelatedTypeEnum;
import com.ruijie.rcos.rcdc.rco.module.def.softwarecontrol.request.SoftwareStrategyBindRelationRequest;
import com.ruijie.rcos.rcdc.rco.module.impl.softwarecontrol.constant.SoftwareControlBusinessKey;
import com.ruijie.rcos.rcdc.rco.module.impl.softwarecontrol.dao.*;
import com.ruijie.rcos.rcdc.rco.module.impl.softwarecontrol.entity.*;
import com.ruijie.rcos.rcdc.rco.module.impl.softwarecontrol.tx.SoftwareControlServiceTx;
import com.ruijie.rcos.sk.base.exception.BusinessException;
import com.ruijie.rcos.sk.base.i18n.LocaleI18nResolver;
import com.ruijie.rcos.sk.base.lockable.LockableExecutor;
import com.ruijie.rcos.sk.base.log.Logger;
import com.ruijie.rcos.sk.base.log.LoggerFactory;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import java.util.*;

/**
 * Description: 软件管控管理事务实现类
 * Copyright: Copyright (c) 2021
 * Company: Ruijie Co., Ltd.
 * Create Time: 2021/7/2
 *
 * @author wuShengQiang
 */
@Service
public class SoftwareControlServiceTxImpl implements SoftwareControlServiceTx {

    private static final Logger LOGGER = LoggerFactory.getLogger(SoftwareControlServiceTxImpl.class);

    @Autowired
    private RcoSoftwareStrategyDAO rcoSoftwareStrategyDAO;

    @Autowired
    private RcoSoftwareStrategyDetailDAO rcoSoftwareStrategyDetailDAO;


    @Autowired
    private RcoSoftwareDAO rcoSoftwareDAO;

    @Autowired
    private RcoSoftwareGroupDAO rcoSoftwareGroupDAO;

    @Autowired
    private RcoSoftwareStrategyRelationDAO rcoSoftwareStrategyRelationDAO;

    @Override
    public void createSoftwareStrategy(SoftwareStrategyDTO softwareStrategyDTO) throws BusinessException {
        Assert.notNull(softwareStrategyDTO, "softwareStrategyDTO 不能为空");
        RcoSoftwareStrategyEntity softwareStrategyEntity = new RcoSoftwareStrategyEntity();
        BeanUtils.copyProperties(softwareStrategyDTO, softwareStrategyEntity);
        RcoSoftwareStrategyEntity entity = rcoSoftwareStrategyDAO.save(softwareStrategyEntity);
        saveStrategyDetail(entity.getId(), softwareStrategyDTO.getSoftwareGroupArr());
    }

    @Override
    public void editSoftwareStrategy(SoftwareStrategyDTO softwareStrategyDTO) throws BusinessException {
        Assert.notNull(softwareStrategyDTO, "softwareStrategyDTO 不能为空");
        UUID strategyId = softwareStrategyDTO.getId();
        Assert.notNull(strategyId, "strategyId 不能为空");
        LockableExecutor.executeWithTryLock(strategyId.toString(), () -> {
            RcoSoftwareStrategyEntity entity = findSoftwareStrategyEntity(softwareStrategyDTO.getId());
            entity.setName(softwareStrategyDTO.getName());
            entity.setDescription(softwareStrategyDTO.getDescription());
            entity.setIsWhitelistMode(softwareStrategyDTO.getIsWhitelistMode());
            entity.setVersion(entity.getVersion() + 1);
            rcoSoftwareStrategyDAO.save(entity);
        }, 1);
        rcoSoftwareStrategyDetailDAO.deleteByStrategyId(strategyId);
        saveStrategyDetail(strategyId, softwareStrategyDTO.getSoftwareGroupArr());
    }

    @Override
    public void deleteSoftwareStrategy(RcoSoftwareStrategyEntity entity) throws BusinessException {
        Assert.notNull(entity, "entity 不能为空");
        rcoSoftwareStrategyDAO.delete(entity);
        List<RcoSoftwareStrategyDetailEntity> detailEntityList = rcoSoftwareStrategyDetailDAO.findByStrategyId(entity.getId());
        if (!detailEntityList.isEmpty()) {
            for (RcoSoftwareStrategyDetailEntity detailEntity : detailEntityList) {
                rcoSoftwareStrategyDetailDAO.delete(detailEntity);
            }
        }
    }

    @Override
    public void deleteSoftwareGroup(RcoSoftwareGroupEntity entity) throws BusinessException {
        Assert.notNull(entity, "id 不能为空");
        UUID id = entity.getId();
        List<RcoSoftwareEntity> softwareEntityList = rcoSoftwareDAO.findByGroupId(id);
        if (!softwareEntityList.isEmpty()) {
            LOGGER.error("软件分组下关联软件不为空, id[{}]", id);
            throw new BusinessException(SoftwareControlBusinessKey.RCDC_RCO_SOFTWARE_GROUP_RELATED_SOFTWARE_NOT_EMPTY, id.toString());
        }
        List<RcoSoftwareStrategyDetailEntity> strategyDetailEntityList =
                rcoSoftwareStrategyDetailDAO.findByRelatedIdAndRelatedType(id, SoftwareStrategyRelatedTypeEnum.GROUP);
        if (!strategyDetailEntityList.isEmpty()) {
            for (RcoSoftwareStrategyDetailEntity strategyDetailEntity : strategyDetailEntityList) {
                rcoSoftwareStrategyDetailDAO.delete(strategyDetailEntity);
            }
        }
        rcoSoftwareGroupDAO.delete(entity);
    }

    @Override
    public void deleteSoftware(UUID id) throws BusinessException {
        Assert.notNull(id, "id 不能为空");
        RcoSoftwareEntity entity = findSoftwareEntity(id);
        List<RcoSoftwareStrategyDetailEntity> strategyDetailEntityList =
                rcoSoftwareStrategyDetailDAO.findByRelatedIdAndRelatedType(id, SoftwareStrategyRelatedTypeEnum.SOFTWARE);
        if (!strategyDetailEntityList.isEmpty()) {
            for (RcoSoftwareStrategyDetailEntity strategyDetailEntity : strategyDetailEntityList) {
                rcoSoftwareStrategyDetailDAO.delete(strategyDetailEntity);
            }
        }
        rcoSoftwareDAO.delete(entity);
        if (entity != null && entity.getDirectoryFlag()) {
            rcoSoftwareDAO.deleteByParentId(entity.getId());
        }
    }

    @Override
    public void deleteSoftwareFromSoftwareStrategyDetail(UUID strategyId, UUID softwareId) throws BusinessException {
        Assert.notNull(strategyId, "strategyId 不能为空");
        Assert.notNull(softwareId, "softwareId 不能为空");
        RcoSoftwareStrategyDetailEntity detailEntity = rcoSoftwareStrategyDetailDAO.findByStrategyIdAndRelatedIdAndRelatedType(strategyId, softwareId,
                SoftwareStrategyRelatedTypeEnum.SOFTWARE);
        if (detailEntity != null) {
            rcoSoftwareStrategyDetailDAO.delete(detailEntity);
        }
    }

    private void saveStrategyDetail(UUID strategyId, SoftwareStrategyDetailDTO[] softwareGroupArr) {
        List<RcoSoftwareStrategyDetailEntity> strategyDetailEntityList = new ArrayList<>();
        for (SoftwareStrategyDetailDTO softwareDTO : softwareGroupArr) {
            RcoSoftwareStrategyDetailEntity strategyDetailEntity = new RcoSoftwareStrategyDetailEntity();
            strategyDetailEntity.setStrategyId(strategyId);
            strategyDetailEntity.setRelatedId(softwareDTO.getId());
            strategyDetailEntity.setRelatedType(
                    softwareDTO.getGroupId() == null ? SoftwareStrategyRelatedTypeEnum.GROUP : SoftwareStrategyRelatedTypeEnum.SOFTWARE);
            strategyDetailEntityList.add(strategyDetailEntity);
        }
        rcoSoftwareStrategyDetailDAO.saveAll(strategyDetailEntityList);
    }

    private RcoSoftwareStrategyEntity findSoftwareStrategyEntity(UUID id) throws BusinessException {
        Optional<RcoSoftwareStrategyEntity> entityOptional = rcoSoftwareStrategyDAO.findById(id);
        if (!entityOptional.isPresent()) {
            LOGGER.error(SoftwareControlBusinessKey.RCDC_RCO_SOFTWARE_STRATEGY_NOT_EXIST, id);
            throw new BusinessException(SoftwareControlBusinessKey.RCDC_RCO_SOFTWARE_STRATEGY_NOT_EXIST, id.toString());
        }

        return entityOptional.get();
    }

    private RcoSoftwareGroupEntity findSoftwareGroupEntity(UUID id) throws BusinessException {
        Optional<RcoSoftwareGroupEntity> entityOptional = rcoSoftwareGroupDAO.findById(id);
        if (!entityOptional.isPresent()) {
            LOGGER.error(SoftwareControlBusinessKey.RCDC_RCO_SOFTWARE_GROUP_NOT_EXIST, id);
            throw new BusinessException(SoftwareControlBusinessKey.RCDC_RCO_SOFTWARE_GROUP_NOT_EXIST, id.toString());
        }

        return entityOptional.get();
    }

    private RcoSoftwareEntity findSoftwareEntity(UUID id) throws BusinessException {
        Optional<RcoSoftwareEntity> entityOptional = rcoSoftwareDAO.findById(id);
        if (!entityOptional.isPresent()) {
            LOGGER.error(SoftwareControlBusinessKey.RCDC_RCO_SOFTWARE_NOT_EXIST, id);
            throw new BusinessException(SoftwareControlBusinessKey.RCDC_RCO_SOFTWARE_NOT_EXIST, id.toString());
        }

        return entityOptional.get();
    }

    @Override
    public UUID getSoftwareGroupIdIfNotExistCreate(SoftwareGroupDTO softwareGroupDTO) {
        Assert.notNull(softwareGroupDTO, "softwareGroupDTO 不能为空");
        if (StringUtils.isBlank(softwareGroupDTO.getName())) {
            softwareGroupDTO.setName(LocaleI18nResolver.resolve(SoftwareControlBusinessKey.RCDC_RCO_SOFTWARE_GROUP_DEFAULT_NAME));
        }
        RcoSoftwareGroupEntity softwareGroupEntity =
                rcoSoftwareGroupDAO.findByNameAndGroupType(softwareGroupDTO.getName(), softwareGroupDTO.getGroupType());
        if (softwareGroupEntity != null) {
            return softwareGroupEntity.getId();
        } else {
            RcoSoftwareGroupEntity entity = new RcoSoftwareGroupEntity();
            BeanUtils.copyProperties(softwareGroupDTO, entity);
            try {
                rcoSoftwareGroupDAO.save(entity);
            } catch (Exception e) {
                LOGGER.error("软件分组名已存在: ", softwareGroupDTO.getName());
            }
            return entity.getId();
        }
    }

    @Override
    public void bindRelation(SoftwareStrategyBindRelationRequest request) {
        Assert.notNull(request, "request 不能为空");

        RcoSoftwareStrategyRelationEntity relationEntity = new RcoSoftwareStrategyRelationEntity();
        BeanUtils.copyProperties(request, relationEntity);
        relationEntity.setCreateTime(new Date());
        relationEntity.setUpdateTime(new Date());
        rcoSoftwareStrategyRelationDAO.save(relationEntity);
    }

    @Override
    public void updateRelationBindStrategy(SoftwareRelationTypeEnum relationType, UUID relationId, UUID newStrategyId) {
        Assert.notNull(relationType, "relationType 不能为空");
        Assert.notNull(relationId, "relationId 不能为空");
        Assert.notNull(newStrategyId, "newStrategyId 不能为空");
        RcoSoftwareStrategyRelationEntity relationEntity = rcoSoftwareStrategyRelationDAO.findByRelationTypeAndRelationId(relationType, relationId);
        if (Objects.isNull(relationEntity)) {
            relationEntity = new RcoSoftwareStrategyRelationEntity();
            relationEntity.setRelationId(relationId);
            relationEntity.setRelationType(relationType);
            relationEntity.setSoftwareStrategyId(newStrategyId);
            relationEntity.setCreateTime(new Date());
            relationEntity.setUpdateTime(new Date());
            rcoSoftwareStrategyRelationDAO.save(relationEntity);
            return;
        }

        relationEntity.setSoftwareStrategyId(newStrategyId);
        rcoSoftwareStrategyRelationDAO.save(relationEntity);
    }

    @Override
    public void deleteBindRelation(SoftwareRelationTypeEnum relationType, UUID relationId) {
        Assert.notNull(relationType, "relationType 不能为空");
        Assert.notNull(relationId, "relationId 不能为空");

        rcoSoftwareStrategyRelationDAO.deleteByRelationTypeAndRelationId(relationType, relationId);
    }

    /**
     * @param idList
     * @param targetGroupId
     */
    @Override
    public void moveSoftware(List<UUID> idList, UUID targetGroupId) {
        Assert.notNull(idList, "idArr 不能为空");
        Assert.notNull(targetGroupId, "targetGroupId 不能为空");
        rcoSoftwareDAO.updateSoftwareGroupId(idList, targetGroupId);
    }

    /**
     * 添加软件到软件策略
     *
     * @param strategyId
     * @param softwareIds
     */
    @Override
    public void batchAddSoftwareStrategyDetail(UUID strategyId, Set<UUID> softwareIds) {
        Assert.notNull(strategyId, "strategyId 不能为空");
        Assert.notNull(softwareIds, "softwareIds 不能为空");

        rcoSoftwareStrategyDetailDAO.deleteByStrategyIdAndRelatedIdIn(strategyId, softwareIds);
        List<RcoSoftwareStrategyDetailEntity> strategyDetailEntityList = new ArrayList<>();
        for (UUID softwareId : softwareIds) {
            RcoSoftwareStrategyDetailEntity strategyDetailEntity = new RcoSoftwareStrategyDetailEntity();
            strategyDetailEntity.setStrategyId(strategyId);
            strategyDetailEntity.setRelatedId(softwareId);
            strategyDetailEntity.setRelatedType(SoftwareStrategyRelatedTypeEnum.SOFTWARE);
            strategyDetailEntityList.add(strategyDetailEntity);
        }
        rcoSoftwareStrategyDetailDAO.saveAll(strategyDetailEntityList);

    }
}
