package com.ruijie.rcos.rcdc.rco.module.impl.desktoppool.service.impl;

import com.alibaba.fastjson.JSON;
import com.google.common.collect.Lists;
import com.ruijie.rcos.gss.sdk.iac.module.def.api.IacUserMgmtAPI;
import com.ruijie.rcos.gss.sdk.iac.module.def.dto.user.IacUserDetailDTO;
import com.ruijie.rcos.gss.sdk.iac.module.def.enums.IacConfigRelatedType;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.api.CbbDeskMgmtAPI;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.dto.CbbDeskDTO;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.enums.CbbCloudDeskState;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.enums.CbbDesktopSessionType;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.enums.desktoppool.CbbDesktopPoolModel;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.enums.desktoppool.DesktopPoolType;
import com.ruijie.rcos.rcdc.rco.module.def.api.RccmManageAPI;
import com.ruijie.rcos.rcdc.rco.module.def.desktoppool.dto.DesktopPoolUserDTO;
import com.ruijie.rcos.rcdc.rco.module.def.desktoppool.dto.PoolDesktopInfoDTO;
import com.ruijie.rcos.rcdc.rco.module.def.desktoppool.dto.UserGroupAssignedUserNumDTO;
import com.ruijie.rcos.rcdc.rco.module.def.desktoppool.request.UserDesktopBindUserRequest;
import com.ruijie.rcos.rcdc.rco.module.impl.adgroup.service.RcoAdGroupService;
import com.ruijie.rcos.rcdc.rco.module.impl.desktoppool.DesktopPoolBusinessKey;
import com.ruijie.rcos.rcdc.rco.module.impl.desktoppool.dao.DesktopPoolUserDAO;
import com.ruijie.rcos.rcdc.rco.module.impl.desktoppool.dao.PoolDesktopInfoDAO;
import com.ruijie.rcos.rcdc.rco.module.impl.desktoppool.dao.ViewDeskUserRelationDAO;
import com.ruijie.rcos.rcdc.rco.module.impl.desktoppool.entity.DesktopPoolUserEntity;
import com.ruijie.rcos.rcdc.rco.module.impl.desktoppool.entity.ViewDeskUserRelationEntity;
import com.ruijie.rcos.rcdc.rco.module.impl.desktoppool.entity.ViewPoolDesktopInfoEntity;
import com.ruijie.rcos.rcdc.rco.module.impl.desktoppool.service.DesktopPoolUserService;
import com.ruijie.rcos.rcdc.rco.module.impl.entity.HostUserEntity;
import com.ruijie.rcos.rcdc.rco.module.impl.entity.RcoViewUserEntity;
import com.ruijie.rcos.rcdc.rco.module.impl.service.HostUserService;
import com.ruijie.rcos.rcdc.rco.module.impl.service.UserDesktopService;
import com.ruijie.rcos.rcdc.rco.module.impl.service.UserService;
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
 * Description: 桌面池与用户/用户组关联
 * Copyright: Copyright (c) 2021
 * Company: Ruijie Co., Ltd.
 * Create Time: 2021/10/20 14:52
 *
 * @author linke
 */

@Service
public class DesktopPoolUserServiceImpl implements DesktopPoolUserService {

    private static final Logger LOGGER = LoggerFactory.getLogger(DesktopPoolUserServiceImpl.class);

    @Autowired
    private DesktopPoolUserDAO desktopPoolUserDAO;

    @Autowired
    private UserService userService;

    @Autowired
    private UserDesktopService userDesktopService;

    @Autowired
    private PoolDesktopInfoDAO poolDesktopInfoDAO;

    @Autowired
    private ViewDeskUserRelationDAO viewPoolDeskUserRelationDAO;

    @Autowired
    private IacUserMgmtAPI cbbUserAPI;

    @Autowired
    private RcoAdGroupService rcoAdGroupService;

    @Autowired
    private CbbDeskMgmtAPI cbbDeskMgmtAPI;


    @Autowired
    private HostUserService hostUserService;

    @Autowired
    private RccmManageAPI rccmManageAPI;

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
            userAdGroupIdList = desktopPoolUserDAO.findRelatedIdByDesktopPoolIdAndRelatedTypeAndRelatedIdIn(desktopPoolId,
                    IacConfigRelatedType.AD_GROUP, userAdGroupIdList);
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
        DesktopPoolUserEntity entity = desktopPoolUserDAO.findByDesktopPoolIdAndRelatedId(desktopPoolId, relatedId);
        return Objects.nonNull(entity);
    }

    @Override
    public List<UUID> listPoolIdByRelatedId(UUID relatedId) {
        Assert.notNull(relatedId, "relatedId can not be null");

        List<DesktopPoolUserEntity> entityList = desktopPoolUserDAO.findAllByRelatedId(relatedId);
        if (CollectionUtils.isEmpty(entityList)) {
            return Collections.emptyList();
        }
        return entityList.stream().map(DesktopPoolUserEntity::getDesktopPoolId).collect(Collectors.toList());
    }

    @Override
    public List<UUID> listPoolIdByRelatedIdIn(List<UUID> relatedIdList) {
        Assert.notEmpty(relatedIdList, "relatedIdList can not be empty");
        List<DesktopPoolUserEntity> entityList = desktopPoolUserDAO.findAllByRelatedIdIn(relatedIdList);
        if (CollectionUtils.isEmpty(entityList)) {
            return Collections.emptyList();
        }
        return entityList.stream().map(DesktopPoolUserEntity::getDesktopPoolId).collect(Collectors.toList());
    }

    @Override
    public List<DesktopPoolUserDTO> listDesktopPoolUser(UUID desktopPoolId, @Nullable IacConfigRelatedType relatedType) {
        Assert.notNull(desktopPoolId, "desktopPoolId can not be null");

        List<DesktopPoolUserEntity> entityList;
        if (Objects.isNull(relatedType)) {
            entityList = desktopPoolUserDAO.findByDesktopPoolId(desktopPoolId);
        } else {
            entityList = desktopPoolUserDAO.findAllByDesktopPoolIdAndRelatedType(desktopPoolId, relatedType);
        }
        return convert2PoolUserList(entityList);
    }

    @Override
    public void deleteByRelatedId(UUID relatedId) {
        Assert.notNull(relatedId, "relatedId can not be null");
        desktopPoolUserDAO.deleteByRelatedId(relatedId);
    }

    @Override
    public Integer countAssignedUserNumByGroup(UUID groupId, UUID desktopPoolId) {
        Assert.notNull(groupId, "groupId can not be null");
        Assert.notNull(desktopPoolId, "desktopPoolId can not be null");
        return desktopPoolUserDAO.countAssignedUserNumByGroup(groupId, desktopPoolId);
    }

    @Override
    public List<UserGroupAssignedUserNumDTO> countAssignedUserNumInGroupByDesktopPoolId(UUID desktopPoolId) {
        Assert.notNull(desktopPoolId, "desktopPoolId can not be null");

        List<Map<String, Object>> queryResultList = desktopPoolUserDAO.countAssignedUserNumInGroupByDesktopPoolId(desktopPoolId);

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
        desktopPoolUserDAO.deleteByDesktopPoolId(desktopPoolId);
    }

    @Override
    public List<UUID> listBindUserIdByUserGroupIds(UUID desktopPoolId, List<UUID> userGroupIdList) {
        Assert.notNull(desktopPoolId, "desktopPoolId can not be null");
        Assert.notEmpty(userGroupIdList, "userGroupIdList can not be empty");

        return desktopPoolUserDAO.listUserIdByDesktopPoolIdAndUserGroupIds(desktopPoolId, userGroupIdList);
    }

    @Override
    public List<DesktopPoolUserDTO> listDesktopPoolAllUser() {
        return convert2PoolUserList(desktopPoolUserDAO.findAllDistinctByRelatedIdAndRelatedType());
    }

    @Override
    public List<String> listBindDesktopPoolUserName(List<UUID> userIdList) {
        Assert.notNull(userIdList, "userIdList can not be null");

        if (CollectionUitls.isEmpty(userIdList)) {
            return Collections.emptyList();
        }

        Set<UUID> existUserIdSet = new HashSet<>();

        // 根据用户ID列表查询有绑定了桌面池的用户
        List<DesktopPoolUserEntity> desktopPoolUserList =
                desktopPoolUserDAO.findAllDistinctByRelatedIdInAndRelatedType(userIdList, IacConfigRelatedType.USER);
        if (CollectionUtils.isNotEmpty(desktopPoolUserList)) {
            existUserIdSet.addAll(desktopPoolUserList.stream().map(DesktopPoolUserEntity::getRelatedId).collect(Collectors.toSet()));
        }

        // 过滤出未查询到的用户ID列表，查询其组ID是否关联的桌面池
        List<UUID> tempUserIdList = userIdList.stream().filter(item -> !existUserIdSet.contains(item)).collect(Collectors.toList());
        if (CollectionUtils.isEmpty(tempUserIdList)) {
            return convertToUserName(existUserIdSet);
        }

        List<RcoViewUserEntity> userInfoByIdList = userService.getUserInfoByIdList(tempUserIdList);
        List<UUID> groupIdList = userInfoByIdList.stream().map(RcoViewUserEntity::getGroupId).collect(Collectors.toList());
        List<DesktopPoolUserEntity> bindPoolGroupList = desktopPoolUserDAO.findAllDistinctByRelatedIdInAndRelatedType(groupIdList,
                IacConfigRelatedType.USERGROUP);

        if (CollectionUtils.isNotEmpty(bindPoolGroupList)) {
            // 过滤出组有绑定桌面池的用户
            Set<UUID> groupIdSet = bindPoolGroupList.stream().map(DesktopPoolUserEntity::getRelatedId).collect(Collectors.toSet());
            List<RcoViewUserEntity> groupBindUserList = userInfoByIdList.stream().filter(item -> groupIdSet.contains(item.getGroupId()))
                    .collect(Collectors.toList());
            existUserIdSet.addAll(groupBindUserList.stream().map(RcoViewUserEntity::getId).collect(Collectors.toList()));
        }
        return convertToUserName(existUserIdSet);
    }

    @Override
    public Set<String> getDesktopPoolRelationUserGroup(UUID desktopPoolId) {
        Assert.notNull(desktopPoolId, "desktopPoolId can not be null");
        return desktopPoolUserDAO.findByDesktopPoolIdToSet(desktopPoolId);
    }

    @Override
    public int countByRelatedType(IacConfigRelatedType relatedType) {
        Assert.notNull(relatedType, "relatedType can not be null");
        return desktopPoolUserDAO.countByRelatedType(relatedType);
    }

    @Override
    public List<UUID> listPoolIdByRelatedIdList(List<UUID> relatedIdList, IacConfigRelatedType relatedType) {
        Assert.notNull(relatedIdList, "relatedIdList can not be null");
        Assert.notNull(relatedType, "relatedType can not be null");
        if (CollectionUtils.isEmpty(relatedIdList)) {
            return Collections.emptyList();
        }
        return desktopPoolUserDAO.findByRelatedIdInAndRelatedType(relatedIdList, relatedType)
                .stream().map(DesktopPoolUserEntity::getDesktopPoolId)
                .filter(Objects::nonNull).collect(Collectors.toList());
    }

    private List<String> convertToUserName(Set<UUID> existUserIdSet) {
        if (CollectionUtils.isEmpty(existUserIdSet)) {
            return Collections.emptyList();
        }
        List<RcoViewUserEntity> userInfoByIdList = userService.getUserInfoByIdList(new ArrayList<>(existUserIdSet));
        return userInfoByIdList.stream().map(RcoViewUserEntity::getUserName).collect(Collectors.toList());
    }

    private List<DesktopPoolUserDTO> convert2PoolUserList(List<DesktopPoolUserEntity> entityList) {
        if (CollectionUtils.isEmpty(entityList)) {
            return Collections.emptyList();
        }
        List<DesktopPoolUserDTO> poolUserList = new ArrayList<>();
        for (DesktopPoolUserEntity entity : entityList) {
            DesktopPoolUserDTO poolUser = new DesktopPoolUserDTO();
            BeanUtils.copyProperties(entity, poolUser);
            poolUserList.add(poolUser);
        }
        return poolUserList;
    }

    @Override
    public void poolDesktopBindUser(UserDesktopBindUserRequest request) throws BusinessException {
        Assert.notNull(request, "request can not be null");

        ViewPoolDesktopInfoEntity desktopInfoEntity = checkDeskExist(request);

        // 用户是否已被分配到桌面池
        if (!checkUserInDesktopPool(desktopInfoEntity.getDesktopPoolId(), request.getUserId())) {
            IacUserDetailDTO userDetailDTO = cbbUserAPI.getUserDetail(request.getUserId());
            throw new BusinessException(DesktopPoolBusinessKey.RCDC_RCO_DESKTOP_BIND_USER_FAIL_USER_NOT_IN_POOL, userDetailDTO.getUserName(),
                    desktopInfoEntity.getPoolName());
        }

        desktopBindUser(request, desktopInfoEntity);
    }

    @Override
    public void poolDesktopBindUser(PoolDesktopInfoDTO desktopInfoDTO, UUID userId) {
        Assert.notNull(desktopInfoDTO, "cbbDesktopPoolDTO can not be null");
        Assert.notNull(userId, "userId can not be null");

        UserDesktopBindUserRequest bindUserRequest = new UserDesktopBindUserRequest();
        bindUserRequest.setUserId(userId);
        bindUserRequest.setDesktopId(desktopInfoDTO.getDeskId());
        userDesktopService.desktopBindUser(bindUserRequest);
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

        List<ViewDeskUserRelationEntity> desktopList = viewPoolDeskUserRelationDAO.findByDesktopPoolIdAndUserIdIn(desktopPoolId,
                userIdList);
        return desktopList.stream().anyMatch(desk -> desk.getDeskState() != CbbCloudDeskState.RECYCLE_BIN);
    }

    @Override
    public void addUserToDesktopPool(UUID desktopPoolId, UUID userId) {
        Assert.notNull(desktopPoolId, "desktopPoolId can not be null");
        Assert.notNull(userId, "userId can not be null");

        DesktopPoolUserEntity entity = desktopPoolUserDAO.findByDesktopPoolIdAndRelatedId(desktopPoolId, userId);
        if (Objects.nonNull(entity)) {
            return;
        }
        entity = new DesktopPoolUserEntity();
        entity.setDesktopPoolId(desktopPoolId);
        entity.setRelatedId(userId);
        entity.setRelatedType(IacConfigRelatedType.USER);
        entity.setCreateTime(new Date());
        desktopPoolUserDAO.save(entity);

        try {
            LOGGER.info("用户分配桌面池[{}]成功,通知rccm添加用户集群关系缓存", desktopPoolId);
            //推送用户数据
            rccmManageAPI.pushUserByUserIdList(Collections.singletonList(userId));
        } catch (Exception e) {
            LOGGER.error("用户分配桌面池[{}]异常", desktopPoolId, e);
        }
    }

    @Override
    public void dealRelationAfterUpdateUser(UUID userId) {
        Assert.notNull(userId, "userId can not be null");

        RcoViewUserEntity rcoViewUserEntity = userService.getUserInfoById(userId);
        if (Objects.isNull(rcoViewUserEntity)) {
            LOGGER.warn("用户[{}]修改事件处理，查询用户信息为空", userId);
            return;
        }

        List<ViewPoolDesktopInfoEntity> desktopList = poolDesktopInfoDAO.findByDesktopPoolTypeInAndUserIdIn(
                Lists.newArrayList(DesktopPoolType.STATIC), Lists.newArrayList(userId));
        // 多会话池的桌面和用户绑定关系为一对多，在hostUser表中，需要处理
        List<HostUserEntity> hostUserEntityList = hostUserService.findByUserId(userId);
        List<UUID> deskIdList = hostUserEntityList.stream().map(HostUserEntity::getDesktopId).filter(Objects::nonNull).collect(Collectors.toList());
        if (CollectionUtils.isNotEmpty(deskIdList)) {
            List<ViewPoolDesktopInfoEntity> hostDesktopList = poolDesktopInfoDAO.findByDeskIdIn(deskIdList);
            if (CollectionUtils.isNotEmpty(hostDesktopList)) {
                desktopList.addAll(hostDesktopList);
            }
        }
        List<UUID> poolIdList = desktopList.stream().filter(item -> Objects.nonNull(item.getDesktopPoolId())
                        && Boolean.FALSE.equals(item.getIsDelete()) && item.getDesktopPoolType() == DesktopPoolType.STATIC)
                .map(ViewPoolDesktopInfoEntity::getDesktopPoolId).distinct().collect(Collectors.toList());
        if (CollectionUtils.isEmpty(poolIdList)) {
            return;
        }
        // 用户已绑定池中的桌面，需要处理用户与池的分配关系
        for (UUID poolId : poolIdList) {
            // 判断用户组是否已分配到池中，已分配就删除用户单独关联的数据库记录

            if (Objects.nonNull(desktopPoolUserDAO.findByDesktopPoolIdAndRelatedId(poolId, rcoViewUserEntity.getGroupId()))) {
                LOGGER.info("用户[{}]修改事件处理，用户组已分配到桌面池[{}]", rcoViewUserEntity.getUserName(), poolId);
                desktopPoolUserDAO.deleteByDesktopPoolIdAndRelatedTypeAndRelatedIdIn(poolId, IacConfigRelatedType.USER,
                        Lists.newArrayList(userId));
                continue;
            }
            if (Objects.isNull(desktopPoolUserDAO.findByDesktopPoolIdAndRelatedId(poolId, userId))) {
                // 将用户单独分配到池中
                LOGGER.info("用户[{}]修改事件处理，添加用户到桌面池[{}]", rcoViewUserEntity.getUserName(), poolId);
                addUserToDesktopPool(poolId, userId);
            }
        }
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
    public List<UUID> listRelatedIdByPoolIdAndRelatedObj(UUID desktopPoolId, IacConfigRelatedType relatedType, List<UUID> inRelatedIdList) {
        Assert.notNull(desktopPoolId, "desktopPoolId can not be null");
        Assert.notNull(relatedType, "relatedType can not be null");
        Assert.notEmpty(inRelatedIdList, "inRelatedIdList can not be empty");
        return desktopPoolUserDAO.findRelatedIdByDesktopPoolIdAndRelatedTypeAndRelatedIdIn(desktopPoolId, relatedType, inRelatedIdList);
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
        userAdGroupIdList = desktopPoolUserDAO.findRelatedIdByDesktopPoolIdAndRelatedTypeAndRelatedIdIn(desktopPoolId,
                IacConfigRelatedType.AD_GROUP, userAdGroupIdList);
        if (CollectionUtils.isNotEmpty(userAdGroupIdList)) {
            addUserToDesktopPool(desktopPoolId, userId);
        }
    }

    @Override
    public void thirdPartyPoolDesktopBindUser(UserDesktopBindUserRequest request) throws BusinessException {
        Assert.notNull(request, "request can not be null");

        ViewPoolDesktopInfoEntity desktopInfoEntity = checkDeskExist(request);

        desktopBindUser(request, desktopInfoEntity);
    }

    private ViewPoolDesktopInfoEntity checkDeskExist(UserDesktopBindUserRequest request) throws BusinessException {
        ViewPoolDesktopInfoEntity desktopInfoEntity = poolDesktopInfoDAO.findByDeskId(request.getDesktopId());
        if (Objects.isNull(desktopInfoEntity) || Objects.isNull(desktopInfoEntity.getDesktopPoolId())
                || !DesktopPoolType.isPoolDesktop(desktopInfoEntity.getDesktopPoolType())) {
            String name = Objects.isNull(desktopInfoEntity) ? request.getDesktopId().toString() : desktopInfoEntity.getDesktopName();
            throw new BusinessException(DesktopPoolBusinessKey.RCDC_RCO_DESKTOP_BIND_USER_DESKTOP_INFO_ERROR, name);
        }
        return desktopInfoEntity;
    }

    private void desktopBindUser(UserDesktopBindUserRequest request, ViewPoolDesktopInfoEntity desktopInfoEntity) throws BusinessException {
        try {
            CbbDeskDTO deskDTO = cbbDeskMgmtAPI.getDeskById(request.getDesktopId());
            request.setDesktopPoolId(deskDTO.getDesktopPoolId());
            request.setSessionType(deskDTO.getSessionType());
        } catch (BusinessException e) {
            LOGGER.error("获取桌面[{}]异常", request.getDesktopId());
        }
        if (desktopInfoEntity.getPoolModel() == CbbDesktopPoolModel.STATIC) {
            checkPoolBindUser(request, desktopInfoEntity);

        } else {
            // 非静态池桌面不支持关联用户
            throw new BusinessException(DesktopPoolBusinessKey.RCDC_RCO_DESKTOP_BIND_USER_MUST_STATIC_POOL, desktopInfoEntity.getDesktopName());
        }

        userDesktopService.desktopBindUser(request);
    }

    private void checkPoolBindUser(UserDesktopBindUserRequest request, ViewPoolDesktopInfoEntity desktopInfoEntity) throws BusinessException {
        if (request.getSessionType() == CbbDesktopSessionType.MULTIPLE) {
            List<HostUserEntity> hostUserEntityList = hostUserService.findByDesktopPoolIdAndUserId(desktopInfoEntity.getDesktopPoolId(), request.getUserId());
            List<UUID> deskIdList = hostUserEntityList.stream().map(HostUserEntity::getDesktopId).filter(Objects::nonNull).collect(Collectors.toList());
            if (CollectionUtils.isEmpty(deskIdList)) {
                return;
            }
            List<ViewPoolDesktopInfoEntity> desktopInfoEntityList = poolDesktopInfoDAO.findByDeskIdIn(deskIdList);
            if (desktopInfoEntityList.stream().anyMatch(item -> item.getDeskState() != CbbCloudDeskState.RECYCLE_BIN)) {
                throw new BusinessException(DesktopPoolBusinessKey.RCDC_RCO_DESKTOP_BIND_USER_ERROR_HAD_BIND_OTHER);
            }
        } else {
            // 用户是否已有绑定桌面
            List<ViewPoolDesktopInfoEntity> desktopList = poolDesktopInfoDAO.findByDesktopPoolIdAndUserId(desktopInfoEntity.getDesktopPoolId(),
                    request.getUserId());
            desktopList = desktopList.stream().filter(desk -> desk.getDeskState() != CbbCloudDeskState.RECYCLE_BIN)
                    .filter(desk -> !Objects.equals(desk.getDeskId(), desktopInfoEntity.getDeskId())).collect(Collectors.toList());
            if (CollectionUtils.isNotEmpty(desktopList)) {
                // 用户绑定了其他桌面，不能再绑定新的桌面
                throw new BusinessException(DesktopPoolBusinessKey.RCDC_RCO_DESKTOP_BIND_USER_ERROR_HAD_BIND_OTHER);
            }
        }
    }
}
