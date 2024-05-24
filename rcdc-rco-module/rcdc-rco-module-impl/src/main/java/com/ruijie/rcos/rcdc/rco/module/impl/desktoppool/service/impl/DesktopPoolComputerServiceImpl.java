package com.ruijie.rcos.rcdc.rco.module.impl.desktoppool.service.impl;

import com.alibaba.fastjson.JSON;
import com.ruijie.rcos.gss.sdk.iac.module.def.api.IacUserMgmtAPI;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.enums.CbbCloudDeskState;
import com.ruijie.rcos.rcdc.rco.module.def.api.enums.ComputerRelatedType;
import com.ruijie.rcos.rcdc.rco.module.def.desktoppool.dto.DesktopPoolComputerDTO;
import com.ruijie.rcos.rcdc.rco.module.def.desktoppool.dto.UserGroupAssignedUserNumDTO;
import com.ruijie.rcos.rcdc.rco.module.impl.adgroup.service.RcoAdGroupService;
import com.ruijie.rcos.rcdc.rco.module.impl.desktoppool.dao.DesktopPoolComputerDAO;
import com.ruijie.rcos.rcdc.rco.module.impl.desktoppool.dao.PoolDesktopInfoDAO;
import com.ruijie.rcos.rcdc.rco.module.impl.desktoppool.entity.DesktopPoolComputerEntity;
import com.ruijie.rcos.rcdc.rco.module.impl.desktoppool.entity.ViewPoolDesktopInfoEntity;
import com.ruijie.rcos.rcdc.rco.module.impl.desktoppool.service.DesktopPoolComputerService;
import com.ruijie.rcos.rcdc.rco.module.impl.entity.RcoViewUserEntity;
import com.ruijie.rcos.rcdc.rco.module.impl.service.UserDesktopService;
import com.ruijie.rcos.rcdc.rco.module.impl.service.UserService;
import com.ruijie.rcos.rcdc.terminal.module.def.api.CbbTerminalGroupMgmtAPI;
import com.ruijie.rcos.rcdc.terminal.module.def.api.dto.CbbTerminalGroupDetailDTO;
import com.ruijie.rcos.sk.base.exception.BusinessException;
import com.ruijie.rcos.sk.base.log.Logger;
import com.ruijie.rcos.sk.base.log.LoggerFactory;
import com.ruijie.rcos.sk.base.util.CollectionUitls;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import java.util.*;
import java.util.stream.Collectors;

/**
 * Description: 桌面池与PC终端/PC终端组关联
 * Copyright: Copyright (c) 2024
 * Company: Ruijie Co., Ltd.
 * Create Time: 2024/1/30
 *
 * @author zqj
 */

@Service
public class DesktopPoolComputerServiceImpl implements DesktopPoolComputerService  {

    private static final Logger LOGGER = LoggerFactory.getLogger(DesktopPoolComputerServiceImpl.class);

    @Autowired
    private DesktopPoolComputerDAO desktopPoolComputerDAO;

    @Autowired
    private UserService userService;

    @Autowired
    private UserDesktopService userDesktopService;

    @Autowired
    private PoolDesktopInfoDAO poolDesktopInfoDAO;

    @Autowired
    private IacUserMgmtAPI cbbUserAPI;

    @Autowired
    private RcoAdGroupService rcoAdGroupService;

    @Autowired
    private CbbTerminalGroupMgmtAPI cbbTerminalGroupMgmtAPI;

    @Override
    public boolean checkUserInDesktopPool(UUID desktopPoolId, UUID userId) {
        Assert.notNull(desktopPoolId, "desktopPoolId can not be null");
        Assert.notNull(userId, "userId can not be null");

        if (this.checkRelatedIdExist(desktopPoolId, userId)) {
            return true;
        }

        // 判断是否是用户，是用户就获取他的组，再判断组是否存在
        try {
            UUID groupId = cbbUserAPI.getUserGroupId(userId);
            if (Objects.nonNull(groupId) && this.checkRelatedIdExist(desktopPoolId, groupId)) {
                return true;
            }
            // 判断用户所属安全组是否有分配桌面池
            List<UUID> userAdGroupIdList = rcoAdGroupService.getUserRelatedAdGroupList(userId);
            if (CollectionUtils.isEmpty(userAdGroupIdList)) {
                return false;
            }
            userAdGroupIdList = desktopPoolComputerDAO.findRelatedIdByDesktopPoolIdAndRelatedTypeAndRelatedIdIn(desktopPoolId,
                    ComputerRelatedType.COMPUTER_GROUP, userAdGroupIdList);
            return CollectionUtils.isNotEmpty(userAdGroupIdList);
        } catch (BusinessException e) {
            LOGGER.error("校验用户[{}]是否在桌面池[{}]中发生错误", userId, desktopPoolId, e);
            return false;
        }
    }

    @Override
    public boolean checkRelatedIdExist(UUID desktopPoolId, UUID relatedId) {
        Assert.notNull(desktopPoolId, "desktopPoolId can not be null");
        Assert.notNull(relatedId, "relatedId can not be null");
        DesktopPoolComputerEntity entity = desktopPoolComputerDAO.findByDesktopPoolIdAndRelatedId(desktopPoolId, relatedId);
        return Objects.nonNull(entity);
    }

    @Override
    public boolean checkRelatedIdExist(UUID relatedId) {
        Assert.notNull(relatedId, "relatedId can not be null");
        DesktopPoolComputerEntity entity = desktopPoolComputerDAO.findByRelatedId(relatedId);
        return Objects.nonNull(entity);
    }

    @Override
    public List<UUID> listPoolIdByRelatedId(UUID relatedId) {
        Assert.notNull(relatedId, "relatedId can not be null");

        List<DesktopPoolComputerEntity> entityList = desktopPoolComputerDAO.findAllByRelatedId(relatedId);
        if (CollectionUtils.isEmpty(entityList)) {
            return Collections.emptyList();
        }
        return entityList.stream().map(DesktopPoolComputerEntity::getDesktopPoolId).collect(Collectors.toList());
    }

    @Override
    public List<DesktopPoolComputerDTO> listDesktopPoolUser(UUID desktopPoolId, @Nullable ComputerRelatedType relatedType) {
        Assert.notNull(desktopPoolId, "desktopPoolId can not be null");

        List<DesktopPoolComputerEntity> entityList;
        if (Objects.isNull(relatedType)) {
            entityList = desktopPoolComputerDAO.findByDesktopPoolId(desktopPoolId);
        } else {
            entityList = desktopPoolComputerDAO.findAllByDesktopPoolIdAndRelatedType(desktopPoolId, relatedType);
        }
        return convert2PoolComputerList(entityList);
    }

    @Override
    public void deleteByRelatedId(UUID relatedId) {
        Assert.notNull(relatedId, "relatedId can not be null");
        desktopPoolComputerDAO.deleteByRelatedId(relatedId);
    }

    @Override
    public Integer countAssignedUserNumByGroup(UUID groupId, UUID desktopPoolId) {
        Assert.notNull(groupId, "groupId can not be null");
        Assert.notNull(desktopPoolId, "desktopPoolId can not be null");
        return desktopPoolComputerDAO.countAssignedUserNumByGroup(groupId, desktopPoolId);
    }

    @Override
    public List<UserGroupAssignedUserNumDTO> countAssignedUserNumInGroupByDesktopPoolId(UUID desktopPoolId) {
        Assert.notNull(desktopPoolId, "desktopPoolId can not be null");

        List<Map<String, Object>> queryResultList = desktopPoolComputerDAO.countAssignedUserNumInGroupByDesktopPoolId(desktopPoolId);

        List<UserGroupAssignedUserNumDTO> assignedUserNumDTOList = new ArrayList<>();
        queryResultList.forEach(item -> {
            UserGroupAssignedUserNumDTO assignDTO = JSON.parseObject(JSON.toJSONString(item), UserGroupAssignedUserNumDTO.class);
            assignedUserNumDTOList.add(assignDTO);
        });
        return assignedUserNumDTOList;
    }

    @Override
    public void deleteByDesktopPoolId(UUID desktopPoolId) {
        Assert.notNull(desktopPoolId, "desktopPoolId can not be null");
        desktopPoolComputerDAO.deleteByDesktopPoolId(desktopPoolId);
    }

    @Override
    public List<UUID> listBindUserIdByUserGroupIds(UUID desktopPoolId, List<UUID> userGroupIdList) {
        Assert.notNull(desktopPoolId, "desktopPoolId can not be null");
        Assert.notEmpty(userGroupIdList, "userGroupIdList can not be empty");

        return desktopPoolComputerDAO.listUserIdByDesktopPoolIdAndUserGroupIds(desktopPoolId, userGroupIdList);
    }

    @Override
    public List<DesktopPoolComputerDTO> listDesktopPoolAllUser() {
        return convert2PoolComputerList(desktopPoolComputerDAO.findAllDistinctByRelatedIdAndRelatedType());
    }

    @Override
    public List<String> listBindDesktopPoolUserName(List<UUID> userIdList) {
        Assert.notNull(userIdList, "userIdList can not be null");

        if (CollectionUitls.isEmpty(userIdList)) {
            return Collections.emptyList();
        }

        Set<UUID> existUserIdSet = new HashSet<>();

        // 根据用户ID列表查询有绑定了桌面池的用户
        List<DesktopPoolComputerEntity> desktopPoolUserList =
                desktopPoolComputerDAO.findAllDistinctByRelatedIdInAndRelatedType(userIdList, ComputerRelatedType.COMPUTER);
        if (CollectionUtils.isNotEmpty(desktopPoolUserList)) {
            existUserIdSet.addAll(desktopPoolUserList.stream().map(DesktopPoolComputerEntity::getRelatedId).collect(Collectors.toSet()));
        }

        // 过滤出未查询到的用户ID列表，查询其组ID是否关联的桌面池
        List<UUID> tempUserIdList = userIdList.stream().filter(item -> !existUserIdSet.contains(item)).collect(Collectors.toList());
        if (CollectionUtils.isEmpty(tempUserIdList)) {
            return convertToUserName(existUserIdSet);
        }

        List<RcoViewUserEntity> userInfoByIdList = userService.getUserInfoByIdList(tempUserIdList);
        List<UUID> groupIdList = userInfoByIdList.stream().map(RcoViewUserEntity::getGroupId).collect(Collectors.toList());
        List<DesktopPoolComputerEntity> bindPoolGroupList = desktopPoolComputerDAO.findAllDistinctByRelatedIdInAndRelatedType(groupIdList,
                ComputerRelatedType.COMPUTER_GROUP);

        if (CollectionUtils.isNotEmpty(bindPoolGroupList)) {
            // 过滤出组有绑定桌面池的用户
            Set<UUID> groupIdSet = bindPoolGroupList.stream().map(DesktopPoolComputerEntity::getRelatedId).collect(Collectors.toSet());
            List<RcoViewUserEntity> groupBindUserList = userInfoByIdList.stream().filter(item -> groupIdSet.contains(item.getGroupId()))
                    .collect(Collectors.toList());
            existUserIdSet.addAll(groupBindUserList.stream().map(RcoViewUserEntity::getId).collect(Collectors.toList()));
        }
        return convertToUserName(existUserIdSet);
    }

    @Override
    public Set<String> getDesktopPoolRelationComputerGroup(UUID desktopPoolId) {
        Assert.notNull(desktopPoolId, "desktopPoolId can not be null");
        return desktopPoolComputerDAO.findByDesktopPoolIdAndRelatedType(desktopPoolId, ComputerRelatedType.COMPUTER_GROUP).stream().
                map(s -> s.getRelatedId().toString()).collect(Collectors.toSet());
    }


    private List<String> convertToUserName(Set<UUID> existUserIdSet) {
        if (CollectionUtils.isEmpty(existUserIdSet)) {
            return Collections.emptyList();
        }
        List<RcoViewUserEntity> userInfoByIdList = userService.getUserInfoByIdList(new ArrayList<>(existUserIdSet));
        return userInfoByIdList.stream().map(RcoViewUserEntity::getUserName).collect(Collectors.toList());
    }

    @Override
    public boolean checkUserBindDesktopInPool(UUID desktopPoolId, UUID userId) {
        Assert.notNull(desktopPoolId, "desktopPoolId can not be null");
        Assert.notNull(userId, "userId can not be null");
        List<ViewPoolDesktopInfoEntity> desktopList = poolDesktopInfoDAO.findByDesktopPoolIdAndUserId(desktopPoolId, userId);
        return desktopList.stream().anyMatch(desk -> desk.getDeskState() != CbbCloudDeskState.RECYCLE_BIN);
    }

    @Override
    public boolean checkUserIdListBindDesktopInPool(UUID desktopPoolId, List<UUID> userIdList) {
        Assert.notNull(desktopPoolId, "desktopPoolId can not be null");
        Assert.notEmpty(userIdList, "userIdList can not be empty");
        List<ViewPoolDesktopInfoEntity> desktopList = poolDesktopInfoDAO.findByDesktopPoolIdAndUserIdIn(desktopPoolId, userIdList);
        return desktopList.stream().anyMatch(desk -> desk.getDeskState() != CbbCloudDeskState.RECYCLE_BIN);
    }

    @Override
    public void addUserToDesktopPool(UUID desktopPoolId, UUID userId) {
        Assert.notNull(desktopPoolId, "desktopPoolId can not be null");
        Assert.notNull(userId, "userId can not be null");

        DesktopPoolComputerEntity entity = desktopPoolComputerDAO.findByDesktopPoolIdAndRelatedId(desktopPoolId, userId);
        if (Objects.nonNull(entity)) {
            return;
        }
        entity = new DesktopPoolComputerEntity();
        entity.setDesktopPoolId(desktopPoolId);
        entity.setRelatedId(userId);
        entity.setRelatedType(ComputerRelatedType.COMPUTER);
        entity.setCreateTime(new Date());
        desktopPoolComputerDAO.save(entity);
    }


    @Override
    public UUID findDeskRunningByDesktopPoolIdAndUserId(UUID desktopPoolId, UUID userId) {
        Assert.notNull(desktopPoolId, "desktopPoolId can not be null");
        Assert.notNull(userId, "userId can not be null");
        return poolDesktopInfoDAO.findByDesktopPoolIdAndUserId(desktopPoolId, userId)
                .stream().filter(entity -> entity.getDeskState() == CbbCloudDeskState.RUNNING)
                .map(ViewPoolDesktopInfoEntity::getDeskId).findFirst().orElse(null);
    }


    @Override
    public void checkAndSaveAdGroupUser(UUID desktopPoolId, UUID userId) throws BusinessException {
        Assert.notNull(desktopPoolId, "desktopPoolId can not be null");
        Assert.notNull(userId, "userId can not be null");

        if (this.checkRelatedIdExist(desktopPoolId, userId)) {
            return;
        }
        UUID groupId = cbbUserAPI.getUserGroupId(userId);
        if (this.checkRelatedIdExist(desktopPoolId, groupId)) {
            // 用户组已有就跳过
            return;
        }
        // 判断用户所属安全组是否有分配桌面池
        List<UUID> userAdGroupIdList = rcoAdGroupService.getUserRelatedAdGroupList(userId);
        if (CollectionUtils.isEmpty(userAdGroupIdList)) {
            return;
        }
        userAdGroupIdList = desktopPoolComputerDAO.findRelatedIdByDesktopPoolIdAndRelatedTypeAndRelatedIdIn(desktopPoolId,
                ComputerRelatedType.COMPUTER_GROUP, userAdGroupIdList);
        if (CollectionUtils.isNotEmpty(userAdGroupIdList)) {
            addUserToDesktopPool(desktopPoolId, userId);
        }
    }

    @Override
    public int countAssignedComputerNumInGroupByDesktopPoolId(UUID[] computerIdArr, UUID desktopPoolId) {
        Assert.notNull(desktopPoolId, "desktopPoolId can not be null");
        Assert.notEmpty(computerIdArr, "computerIdArr can not be empty");
        return desktopPoolComputerDAO.countAssignedNumInComputerByDesktopPoolId(computerIdArr, desktopPoolId);
    }

    @Override
    public List<DesktopPoolComputerEntity> findDesktopPoolRelationComputerGroupList(List<UUID> groupIdArr) {
        Assert.notEmpty(groupIdArr, "groupIdArr can not be empty");
        return desktopPoolComputerDAO.findByRelatedIdIn(groupIdArr);
    }

    @Override
    public DesktopPoolComputerEntity findByRelatedId(UUID relatedId) {
        Assert.notNull(relatedId, "relatedId can not be null");
        return desktopPoolComputerDAO.findByRelatedId(relatedId);
    }

    @Override
    public List<DesktopPoolComputerDTO> listDeskPoolComputerByRelatedType(ComputerRelatedType computerRelatedType) {
        Assert.notNull(computerRelatedType, "computerRelatedType can not be null");
        List<DesktopPoolComputerEntity> computerEntityList = desktopPoolComputerDAO.findByRelatedType(computerRelatedType);
        List<DesktopPoolComputerDTO> poolComputerDTOList = convert2PoolComputerList(computerEntityList);
        return poolComputerDTOList;
    }

    @Override
    public List<DesktopPoolComputerEntity> findDesktopPoolRelationComputerList(UUID[] computerIdArr) {
        Assert.notEmpty(computerIdArr, "computerIdArr is not be empty");
        return desktopPoolComputerDAO.findByRelatedIdIn(Arrays.asList(computerIdArr));
    }

    @Override
    public void removeByPoolIdAndRelatedId(UUID poolId, UUID relatedId) {
        Assert.notNull(relatedId, "relatedId can not be null");
        Assert.notNull(poolId, "poolId can not be null");
        desktopPoolComputerDAO.deleteByDesktopPoolIdAndRelatedId(poolId, relatedId);
    }

    private List<DesktopPoolComputerDTO> convert2PoolComputerList(List<DesktopPoolComputerEntity> entityList) {
        if (CollectionUtils.isEmpty(entityList)) {
            return Collections.emptyList();
        }
        List<DesktopPoolComputerDTO> poolComputerDTOList = new ArrayList<>();
        for (DesktopPoolComputerEntity entity : entityList) {
            DesktopPoolComputerDTO computerDTO = new DesktopPoolComputerDTO();
            BeanUtils.copyProperties(entity, computerDTO);
            if (computerDTO.getRelatedType() == ComputerRelatedType.COMPUTER_GROUP) {
                try {
                    CbbTerminalGroupDetailDTO groupDetailDTO = cbbTerminalGroupMgmtAPI.findById(computerDTO.getRelatedId());
                    computerDTO.setName(groupDetailDTO.getGroupName());
                } catch (BusinessException e) {
                    LOGGER.error("查询PC终端组[{}]信息失败", computerDTO.getRelatedId(), e);
                }
            }

            poolComputerDTOList.add(computerDTO);
        }
        return poolComputerDTOList;
    }
}
