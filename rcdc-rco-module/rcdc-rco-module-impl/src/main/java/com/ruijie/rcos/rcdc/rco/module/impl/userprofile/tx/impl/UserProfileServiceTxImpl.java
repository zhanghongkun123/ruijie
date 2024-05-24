package com.ruijie.rcos.rcdc.rco.module.impl.userprofile.tx.impl;

import com.ruijie.rcos.rcdc.rco.module.def.userprofile.dto.UserProfilePathGroupDTO;
import com.ruijie.rcos.rcdc.rco.module.def.userprofile.dto.UserProfileStrategyDTO;
import com.ruijie.rcos.rcdc.rco.module.def.userprofile.dto.UserProfileStrategyRelatedDTO;
import com.ruijie.rcos.rcdc.rco.module.impl.userprofile.constant.UserProfileBusinessKey;
import com.ruijie.rcos.rcdc.rco.module.impl.userprofile.dao.*;
import com.ruijie.rcos.rcdc.rco.module.impl.userprofile.dao.UserProfileMainPathDAO;
import com.ruijie.rcos.rcdc.rco.module.impl.userprofile.dao.UserProfilePathGroupDAO;
import com.ruijie.rcos.rcdc.rco.module.impl.userprofile.dao.UserProfileStrategyDAO;
import com.ruijie.rcos.rcdc.rco.module.impl.userprofile.dao.UserProfileStrategyRelatedDAO;
import com.ruijie.rcos.rcdc.rco.module.impl.userprofile.entity.UserProfilePathMainEntity;
import com.ruijie.rcos.rcdc.rco.module.impl.userprofile.entity.UserProfilePathGroupEntity;
import com.ruijie.rcos.rcdc.rco.module.impl.userprofile.entity.UserProfileStrategyEntity;
import com.ruijie.rcos.rcdc.rco.module.impl.userprofile.entity.UserProfileStrategyRelatedEntity;
import com.ruijie.rcos.rcdc.rco.module.def.userprofile.enums.UserProfileRelatedTypeEnum;
import com.ruijie.rcos.rcdc.rco.module.impl.userprofile.tx.UserProfileServiceTx;
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
 * Description: 用户配置管理事务实现类
 * Copyright: Copyright (c) 2022
 * Company: Ruijie Co., Ltd.
 * Create Time: 2022/4/12
 *
 * @author WuShengQiang
 */
@Service
public class UserProfileServiceTxImpl implements UserProfileServiceTx {

    private static final Logger LOGGER = LoggerFactory.getLogger(UserProfileServiceTxImpl.class);

    /**
     * 获取锁的等待时间
     */
    private static final int WAIT_TIME = 1;

    @Autowired
    private UserProfileMainPathDAO rcoUserProfilePathDAO;

    @Autowired
    private UserProfileChildPathDAO userProfileChildPathDAO;

    @Autowired
    private UserProfilePathDAO pathDAO;

    @Autowired
    private UserProfilePathGroupDAO rcoUserProfilePathGroupDAO;

    @Autowired
    private UserProfileStrategyRelatedDAO rcoUserProfileStrategyRelatedDAO;

    @Autowired
    private UserProfileStrategyDAO rcoUserProfileStrategyDAO;

    @Override
    public void deleteUserProfilePathGroup(UserProfilePathGroupEntity entity) throws BusinessException {
        Assert.notNull(entity, "entity 不能为空");
        UUID id = entity.getId();
        List<UserProfilePathMainEntity> userProfilePathEntityList = rcoUserProfilePathDAO.findByGroupId(id);
        if (!userProfilePathEntityList.isEmpty()) {
            LOGGER.error("删除组失败,组下关联路径不为空, id[{}]", id);
            throw new BusinessException(UserProfileBusinessKey.RCDC_RCO_USER_PROFILE_PATH_GROUP_RELATED_PATH_NOT_EMPTY, entity.getName());
        }
        // 组内路径为空,所以删除策略和组的关联记录
        List<UserProfileStrategyRelatedEntity> strategyRelatedEntityList =
                rcoUserProfileStrategyRelatedDAO.findByRelatedIdAndRelatedType(id, UserProfileRelatedTypeEnum.GROUP);
        if (!strategyRelatedEntityList.isEmpty()) {
            rcoUserProfileStrategyRelatedDAO.deleteAll(strategyRelatedEntityList);
        }
        rcoUserProfilePathGroupDAO.delete(entity);
    }

    @Override
    public void moveUserProfilePath(List<UUID> idList, UUID targetGroupId) throws BusinessException {
        Assert.notNull(idList, "idList 不能为空");
        Assert.notNull(targetGroupId, "targetGroupId 不能为空");
        rcoUserProfilePathDAO.updateUserProfilePathGroupId(idList, targetGroupId);
    }

    @Override
    public void deleteUserProfilePath(UUID id) throws BusinessException {
        Assert.notNull(id, "id 不能为空");
        UserProfilePathMainEntity entity = findUserProfilePathEntity(id);
        List<UserProfileStrategyRelatedEntity> userProfileStrategyRelatedEntityList =
                rcoUserProfileStrategyRelatedDAO.findByRelatedIdAndRelatedType(id, UserProfileRelatedTypeEnum.PATH);
        if (!userProfileStrategyRelatedEntityList.isEmpty()) {
            rcoUserProfileStrategyRelatedDAO.deleteAll(userProfileStrategyRelatedEntityList);
        }

        rcoUserProfilePathDAO.delete(entity);
        userProfileChildPathDAO.deleteByUserProfilePathId(id);
        pathDAO.deleteByUserProfilePathId(id);
    }

    @Override
    public void createUserProfileStrategy(UserProfileStrategyDTO userProfileStrategyDTO) throws BusinessException {
        Assert.notNull(userProfileStrategyDTO, "userProfileStrategyDTO 不能为空");
        UserProfileStrategyEntity strategyEntity = new UserProfileStrategyEntity();
        BeanUtils.copyProperties(userProfileStrategyDTO, strategyEntity);
        strategyEntity.setCreateTime(new Date());
        strategyEntity.setUpdateTime(new Date());
        UserProfileStrategyEntity entity = rcoUserProfileStrategyDAO.save(strategyEntity);
        saveStrategyRelated(entity.getId(), userProfileStrategyDTO.getPathArr());
    }

    @Override
    public void editUserProfileStrategy(UserProfileStrategyDTO userProfileStrategyDTO) throws BusinessException {
        Assert.notNull(userProfileStrategyDTO, "userProfileStrategyDTO 不能为空");
        UUID strategyId = userProfileStrategyDTO.getId();
        Assert.notNull(strategyId, "strategyId 不能为空");
        LockableExecutor.executeWithTryLock(strategyId.toString(), () -> {
            UserProfileStrategyEntity entity = findUserProfileStrategyEntity(strategyId);
            entity.setName(userProfileStrategyDTO.getName());
            entity.setStorageType(userProfileStrategyDTO.getStorageType());
            entity.setDiskPath(userProfileStrategyDTO.getDiskPath());
            entity.setDiskSize(userProfileStrategyDTO.getDiskSize());
            entity.setDescription(userProfileStrategyDTO.getDescription());
            entity.setUpdateTime(new Date());
            entity.setExternalStorageId(userProfileStrategyDTO.getExternalStorageId());
            rcoUserProfileStrategyDAO.save(entity);
        }, WAIT_TIME);
        rcoUserProfileStrategyRelatedDAO.deleteByStrategyId(strategyId);
        saveStrategyRelated(strategyId, userProfileStrategyDTO.getPathArr());
    }

    @Override
    public void deleteUserProfileStrategy(UserProfileStrategyEntity entity) throws BusinessException {
        Assert.notNull(entity, "entity 不能为空");
        rcoUserProfileStrategyDAO.delete(entity);
        rcoUserProfileStrategyRelatedDAO.deleteByStrategyId(entity.getId());
    }

    @Override
    public UUID getPathGroupIdIfNotExistCreate(UserProfilePathGroupDTO groupDTO) {
        Assert.notNull(groupDTO, "groupDTO 不能为空");
        if (StringUtils.isBlank(groupDTO.getName())) {
            groupDTO.setName(LocaleI18nResolver.resolve(UserProfileBusinessKey.RCDC_RCO_PATH_GROUP_DEFAULT_NAME));
        }
        UserProfilePathGroupEntity userProfilePathGroupEntity = rcoUserProfilePathGroupDAO.findByName(groupDTO.getName());
        if (userProfilePathGroupEntity != null) {
            return userProfilePathGroupEntity.getId();
        } else {
            UserProfilePathGroupEntity entity = new UserProfilePathGroupEntity();
            BeanUtils.copyProperties(groupDTO, entity);
            entity.setCreateTime(new Date());
            entity.setUpdateTime(new Date());
            try {
                rcoUserProfilePathGroupDAO.save(entity);
            } catch (Exception e) {
                LOGGER.error("新增组出现异常,路径分组名{}已存在,失败原因： ", groupDTO.getName(), e);
            }
            return entity.getId();
        }
    }

    private UserProfilePathMainEntity findUserProfilePathEntity(UUID id) throws BusinessException {
        Optional<UserProfilePathMainEntity> entityOptional = rcoUserProfilePathDAO.findById(id);
        if (!entityOptional.isPresent()) {
            LOGGER.error("路径ID[{}]对应的路径不存在", id);
            throw new BusinessException(UserProfileBusinessKey.RCDC_RCO_USER_PROFILE_PATH_NOT_EXIST);
        }
        return entityOptional.get();
    }

    private void saveStrategyRelated(UUID strategyId, UserProfileStrategyRelatedDTO[] pathArr) {
        List<UserProfileStrategyRelatedEntity> strategyRelatedEntityList = new ArrayList<>();
        for (UserProfileStrategyRelatedDTO strategyDetailDTO : pathArr) {
            UserProfileStrategyRelatedEntity strategyRelatedEntity = new UserProfileStrategyRelatedEntity();
            strategyRelatedEntity.setStrategyId(strategyId);
            strategyRelatedEntity.setRelatedId(strategyDetailDTO.getId());
            strategyRelatedEntity.setRelatedType(strategyDetailDTO.getGroupId() == null ? UserProfileRelatedTypeEnum.GROUP :
                    UserProfileRelatedTypeEnum.PATH);
            strategyRelatedEntity.setUpdateTime(new Date());
            strategyRelatedEntity.setCreateTime(new Date());
            strategyRelatedEntityList.add(strategyRelatedEntity);
        }
        rcoUserProfileStrategyRelatedDAO.saveAll(strategyRelatedEntityList);
    }

    private UserProfileStrategyEntity findUserProfileStrategyEntity(UUID id) throws BusinessException {
        Optional<UserProfileStrategyEntity> entityOptional = rcoUserProfileStrategyDAO.findById(id);
        if (!entityOptional.isPresent()) {
            LOGGER.error("ID[{}]对应的用户配置策略不存在", id);
            throw new BusinessException(UserProfileBusinessKey.RCDC_RCO_USER_PROFILE_STRATEGY_NOT_EXIST);
        }
        return entityOptional.get();
    }

}