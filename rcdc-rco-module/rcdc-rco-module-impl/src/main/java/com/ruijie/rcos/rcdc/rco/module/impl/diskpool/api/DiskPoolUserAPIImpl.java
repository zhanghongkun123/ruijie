package com.ruijie.rcos.rcdc.rco.module.impl.diskpool.api;

import com.alibaba.fastjson.JSON;
import com.google.common.collect.Lists;
import com.ruijie.rcos.base.alarm.module.def.api.BaseAlarmAPI;
import com.ruijie.rcos.base.alarm.module.def.api.request.SaveAlarmRequest;
import com.ruijie.rcos.base.alarm.module.def.enums.AlarmLevel;
import com.ruijie.rcos.gss.sdk.iac.module.def.api.response.IacPageResponse;
import com.ruijie.rcos.gss.sdk.iac.module.def.dto.user.IacAdGroupEntityDTO;
import com.ruijie.rcos.gss.sdk.iac.module.def.dto.user.IacUserDetailDTO;
import com.ruijie.rcos.gss.sdk.iac.module.def.dto.user.IacUserGroupDetailDTO;
import com.ruijie.rcos.gss.sdk.iac.module.def.enums.IacConfigRelatedType;
import com.ruijie.rcos.gss.sdk.iac.module.def.enums.IacUserTypeEnum;
import com.ruijie.rcos.gss.log.module.def.api.BaseAuditLogAPI;
import com.ruijie.rcos.gss.sdk.iac.module.def.api.IacUserMgmtAPI;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.api.CbbDiskPoolMgmtAPI;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.constants.AlarmConstants;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.dto.diskpool.CbbDiskPoolDTO;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.enums.DiskPoolType;
import com.ruijie.rcos.rcdc.rco.module.def.api.DiskPoolUserAPI;
import com.ruijie.rcos.rcdc.rco.module.def.api.dto.UserListDTO;
import com.ruijie.rcos.rcdc.rco.module.def.api.request.PageSearchRequest;
import com.ruijie.rcos.rcdc.rco.module.def.api.request.UserAssignmentPageRequest;
import com.ruijie.rcos.rcdc.rco.module.def.constants.Constants;
import com.ruijie.rcos.rcdc.rco.module.def.desktoppool.dto.UpdatePoolBindObjectDTO;
import com.ruijie.rcos.rcdc.rco.module.def.desktoppool.dto.UserGroupAssignedUserNumDTO;
import com.ruijie.rcos.rcdc.rco.module.def.desktoppool.dto.UserGroupDisabledUserNumDTO;
import com.ruijie.rcos.rcdc.rco.module.def.diskpool.dto.DiskPoolUserDTO;
import com.ruijie.rcos.rcdc.rco.module.def.diskpool.dto.DiskPoolUserWithAssignmentDTO;
import com.ruijie.rcos.rcdc.rco.module.impl.adgroup.service.AdGroupPoolService;
import com.ruijie.rcos.rcdc.rco.module.impl.adgroup.service.RcoAdGroupService;
import com.ruijie.rcos.rcdc.rco.module.impl.desktoppool.DesktopPoolAPIHelper;
import com.ruijie.rcos.rcdc.rco.module.impl.diskpool.DiskPoolBusinessKey;
import com.ruijie.rcos.rcdc.rco.module.impl.diskpool.dao.DiskPoolUserDAO;
import com.ruijie.rcos.rcdc.rco.module.impl.diskpool.dao.ViewUserDiskDAO;
import com.ruijie.rcos.rcdc.rco.module.impl.diskpool.entity.DiskPoolUserEntity;
import com.ruijie.rcos.rcdc.rco.module.impl.diskpool.entity.ViewUserDiskEntity;
import com.ruijie.rcos.rcdc.rco.module.impl.dto.PageQueryPoolDTO;
import com.ruijie.rcos.rcdc.rco.module.impl.entity.RcoViewUserEntity;
import com.ruijie.rcos.rcdc.rco.module.impl.service.QueryUserListService;
import com.ruijie.rcos.rcdc.rco.module.impl.service.UserQueryHelper;
import com.ruijie.rcos.rcdc.rco.module.impl.service.UserService;
import com.ruijie.rcos.rcdc.rco.module.impl.tx.UserDiskServiceTx;
import com.ruijie.rcos.sk.base.exception.BusinessException;
import com.ruijie.rcos.sk.base.i18n.LocaleI18nResolver;
import com.ruijie.rcos.sk.base.lockable.LockableExecutor;
import com.ruijie.rcos.sk.base.log.Logger;
import com.ruijie.rcos.sk.base.log.LoggerFactory;
import com.ruijie.rcos.sk.modulekit.api.comm.DefaultPageResponse;
import com.ruijie.rcos.sk.pagekit.api.PageQueryRequest;
import com.ruijie.rcos.sk.pagekit.api.PageQueryResponse;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.collections4.MapUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.lang.Nullable;
import org.springframework.util.Assert;

import java.util.*;
import java.util.stream.Collectors;

;

/**
 * Description: 磁盘池关联用户相关实现类
 * Copyright: Copyright (c) 2022
 * Company: Ruijie Co., Ltd.
 * Create Time: 2022/7/12
 *
 * @author TD
 */
public class DiskPoolUserAPIImpl implements DiskPoolUserAPI {

    private static final Logger LOGGER = LoggerFactory.getLogger(DiskPoolUserAPIImpl.class);

    private static final String DISK_POOL_UPDATE_BIND_LOCK = "DISK_POOL_UPDATE_BIND_LOCK_";

    private static final String RESOURCE_LOCKED_ERROR_KEY = "sk-resource_locked";

    /**
     * 磁盘池分配失败的code
     */
    private static final String DISK_POOL_ASSIGN_FAIL = "DISK_POOL_ASSIGN_FAIL-";

    private static final int SQL_IN_MAX_NUM = 500;

    private static final int SINGLE_LOG_MAX_NUM = 20;

    private static final String NAME_SPLIT = ",";

    @Autowired
    private DiskPoolUserDAO diskPoolUserDAO;

    @Autowired
    private ViewUserDiskDAO viewUserDiskDAO;

    @Autowired
    private IacUserMgmtAPI cbbUserAPI;

    @Autowired
    private QueryUserListService userListService;

    @Autowired
    private UserQueryHelper userQueryHelper;

    @Autowired
    private CbbDiskPoolMgmtAPI cbbDiskPoolMgmtAPI;

    @Autowired
    private UserDiskServiceTx userDiskServiceTx;

    @Autowired
    private UserService userService;

    @Autowired
    private BaseAuditLogAPI auditLogAPI;


    @Autowired
    private BaseAlarmAPI alarmAPI;

    @Autowired
    private AdGroupPoolService adGroupPoolService;

    @Autowired
    private DesktopPoolAPIHelper desktopPoolAPIHelper;

    @Autowired
    private RcoAdGroupService adGroupService;

    @Override
    public PageQueryResponse<DiskPoolUserWithAssignmentDTO> pageUserWithAssignment(PageQueryRequest pageRequest) throws BusinessException {
        Assert.notNull(pageRequest, "request can not be null");
        return DiskPoolUserAPI.super.pageQuery(pageRequest);
    }

    @Override
    public DefaultPageResponse<UserListDTO> pageUserForDistribution(UserAssignmentPageRequest request) throws BusinessException {
        Assert.notNull(request, "request can not be null");
        DefaultPageResponse<UserListDTO> response = new DefaultPageResponse<>();

        UUID diskPoolId = request.getDiskPoolId();
        int endIndex = request.getLimit() * (1 + request.getPage());
        int startIndex = request.getLimit() * request.getPage() + 1;

        Page<RcoViewUserEntity> allPage = userListService.pageQuery(request, RcoViewUserEntity.class);
        int total = (int) allPage.getTotalElements();

        response.setTotal(total);
        // 起始下标超过了总量直接返回空
        if (startIndex > total) {
            // 返回空的
            response.setTotal(total);
            response.setItemArr(new UserListDTO[0]);
            return response;
        }
        // 查出池中的分配关系
        Map<UUID, IacConfigRelatedType> relatedTypeMap = diskPoolUserDAO.findByDiskPoolId(diskPoolId).stream()
                .collect(Collectors.toMap(DiskPoolUserEntity::getRelatedId, DiskPoolUserEntity::getRelatedType, (key1, key2) -> key2));

        Page<RcoViewUserEntity> pageUserIn = userListService
                .pageUserInOrNotInPool(new PageQueryPoolDTO(diskPoolId, UserQueryHelper.DISK_POOL_ID, request, true));
        int pageUserInTotal = (int) pageUserIn.getTotalElements();
        // 已分配用户的满足分页数量
        if (pageUserInTotal >= endIndex) {
            response.setItemArr(convert2UserListDTOArr(request, pageUserIn.getContent(), true, relatedTypeMap));
            return response;
        }

        // 以下为已分配数量不满足此次分页查询
        int notInLastPage = request.getPage() - pageUserInTotal / request.getLimit();
        request.setPage(notInLastPage);
        Page<RcoViewUserEntity> lastPageUserNotIn = userListService
                .pageUserInOrNotInPool(new PageQueryPoolDTO(diskPoolId, UserQueryHelper.DISK_POOL_ID, request, false));
        // 未有用户被分配，或者已分配的刚好是整数页
        if (pageUserInTotal == 0 || pageUserInTotal % request.getLimit() == 0) {
            // pageUserNotIn入参page和limit需要重新根据pageUserIn的数量计算
            response.setItemArr(convert2UserListDTOArr(request, lastPageUserNotIn.getContent(), false, null));
            return response;
        }

        // 已分配的取一部分，未分配的取一部分
        if (startIndex <= pageUserInTotal) {
            response.setItemArr(getFromAssignedAndUnAssigned(request, pageUserIn, lastPageUserNotIn, relatedTypeMap));
            return response;
        }

        // 只取非分配的组成分页数据
        response.setItemArr(getFromUnAssigned(request, lastPageUserNotIn, pageUserInTotal));
        return response;
    }


    private UserListDTO[] getFromAssignedAndUnAssigned(UserAssignmentPageRequest request, Page<RcoViewUserEntity> pageUserIn,
                                                       Page<RcoViewUserEntity> lastPageUserNotIn, Map<UUID, IacConfigRelatedType> relatedTypeMap) {
        UserListDTO[] assignedUserArr = convert2UserListDTOArr(request, pageUserIn.getContent(), true, relatedTypeMap);
        // notIn部分不需要请求第二次
        int diffNum = request.getLimit() - pageUserIn.getContent().size();
        // 还差diffNum个用户凑成limit个
        if (lastPageUserNotIn.getContent().size() <= diffNum) {
            // lastPageUserNotIn + pageUserIn的数据不足一页，后面已经没数据了
            UserListDTO[] unAssignedUserArr = convert2UserListDTOArr(request, lastPageUserNotIn.getContent(), false, null);
            return UserQueryHelper.mergeUserListDTOArr(assignedUserArr, unAssignedUserArr);
        }

        // 把lastPageUserNotIn里的部分记录和pageUserIn里的记录合成一页
        List<RcoViewUserEntity> subNotInUserList = lastPageUserNotIn.getContent().subList(0, diffNum);
        UserListDTO[] unAssignedUserArr = convert2UserListDTOArr(request, subNotInUserList, false, null);
        return UserQueryHelper.mergeUserListDTOArr(assignedUserArr, unAssignedUserArr);
    }

    private UserListDTO[] getFromUnAssigned(UserAssignmentPageRequest request, Page<RcoViewUserEntity> lastPageUserNotIn, int pageUserInTotal) {
        request.setPage(request.getPage() - 1);
        Page<RcoViewUserEntity> prePageUserNotIn = userListService.pageUserInOrNotInPool(
                new PageQueryPoolDTO(request.getDiskPoolId(), UserQueryHelper.DISK_POOL_ID, request, false));

        List<RcoViewUserEntity> preUserList = prePageUserNotIn.getContent();
        int diffNumByLimit = request.getLimit() - pageUserInTotal % request.getLimit();

        // lastPageUserNotIn为空，prePageUserNotIn的数据已经是最后的记录了
        if (CollectionUtils.isEmpty(lastPageUserNotIn.getContent())) {
            // 取prePageUserNotIn中的部分
            List<RcoViewUserEntity> finalUserList = preUserList.subList(diffNumByLimit, preUserList.size());
            return convert2UserListDTOArr(request, finalUserList, false, null);
        }

        // 取prePageUserNotIn中的部分和lastPageUserNotIn中的部分
        List<RcoViewUserEntity> finalUserList = Lists.newArrayList(preUserList.subList(diffNumByLimit, preUserList.size()));
        int lastPageEndIndex = Math.min(diffNumByLimit, lastPageUserNotIn.getContent().size());
        finalUserList.addAll(lastPageUserNotIn.getContent().subList(0, lastPageEndIndex));
        return convert2UserListDTOArr(request, finalUserList, false, null);
    }

    private UserListDTO[] convert2UserListDTOArr(UserAssignmentPageRequest request, List<RcoViewUserEntity> userEntityList, boolean isInPool,
                                                 Map<UUID, IacConfigRelatedType> relatedTypeMap) {
        UserListDTO[] userListDTOArr = userQueryHelper.convertRcoViewUserEntity2UserListDTO(userEntityList);
        // isInPool = true 需要去查是否已经有绑定磁盘了
        Set<UUID> userIdSet = new HashSet<>();
        if (isInPool) {
            List<ViewUserDiskEntity> userDiskEntityList = viewUserDiskDAO.findAllByDiskPoolIdIn(Collections.singletonList(request.getDiskPoolId()));
            userIdSet = userDiskEntityList.stream().map(ViewUserDiskEntity::getUserId).filter(Objects::nonNull).collect(Collectors.toSet());
        }
        List<UUID> relatedIdList = new ArrayList<>();
        for (UserListDTO user : userListDTOArr) {
            user.setIsAssigned(isInPool);
            user.setDisabled(false);
            user.setHasBindDisk(userIdSet.contains(user.getId()));
            // 设置关联查询的ID
            setRelatedIdList(relatedTypeMap, user, relatedIdList);
        }
        if ((isInPool && MapUtils.isEmpty(relatedTypeMap)) || CollectionUtils.isEmpty(relatedIdList)) {
            return userListDTOArr;
        }
        // isAssigned = false 需要去查是否已经有其他池选了
        UUID poolId = request.getDiskPoolId();
        List<DiskPoolUserEntity> poolUserList = diskPoolUserDAO.findByRelatedIdIn(relatedIdList)
                .stream().filter(item -> !Objects.equals(item.getDiskPoolId(), poolId)).collect(Collectors.toList());
        if (CollectionUtils.isEmpty(poolUserList)) {
            return userListDTOArr;
        }
        Map<String, DiskPoolUserEntity> userIdToDiskPoolUserMap = new HashMap<>();
        for (DiskPoolUserEntity entity : poolUserList) {
            userIdToDiskPoolUserMap.put(entity.getRelatedId().toString() + entity.getRelatedType().name(), entity);
        }
        for (UserListDTO user : userListDTOArr) {
            if (userIdToDiskPoolUserMap.containsKey(user.getId().toString() + IacConfigRelatedType.USER.name())) {
                user.setDisabled(true);
                continue;
            }
            if (userIdToDiskPoolUserMap.containsKey(user.getGroupId().toString() + IacConfigRelatedType.USERGROUP.name())) {
                user.setDisabled(true);
            }
        }
        return userListDTOArr;
    }

    private void setRelatedIdList(Map<UUID, IacConfigRelatedType> relatedTypeMap, UserListDTO user, List<UUID> relatedIdList) {
        if (MapUtils.isEmpty(relatedTypeMap)) {
            relatedIdList.add(user.getId());
            relatedIdList.add(user.getGroupId());
            return;
        }
        if (relatedTypeMap.containsKey(user.getGroupId())) {
            relatedIdList.add(user.getId());
        }
    }

    @Override
    public boolean checkUserInDiskPool(UUID diskPoolId, UUID relatedId) {
        Assert.notNull(diskPoolId, "checkUserInDiskPool diskPoolId can not be null");
        Assert.notNull(relatedId, "checkUserInDiskPool relatedId can not be null");

        if (Objects.nonNull(diskPoolUserDAO.findByDiskPoolIdAndRelatedId(diskPoolId, relatedId))) {
            return true;
        }

        // 判断是否是用户，是用户就获取他的组，再判断组是否存在
        try {
            UUID groupId = cbbUserAPI.getUserGroupId(relatedId);
            if (Objects.isNull(groupId)) {
                return false;
            }
            return Objects.nonNull(diskPoolUserDAO.findByDiskPoolIdAndRelatedId(diskPoolId, groupId));
        } catch (BusinessException e) {
            LOGGER.error(String.format("不存在此用户[%s]", relatedId), e);
            return false;
        }
    }

    @Override
    public DefaultPageResponse<UserListDTO> pageQueryDiskPoolBindUser(UUID diskPoolId, PageSearchRequest request) {
        Assert.notNull(diskPoolId, "pageQueryDiskPoolBindUser diskPoolId can not be null");
        Assert.notNull(request, "pageQueryDiskPoolBindUser request can not be null ");
        PageQueryPoolDTO pageQueryPoolDTO = new PageQueryPoolDTO(diskPoolId, UserQueryHelper.DISK_POOL_ID, request, true);
        pageQueryPoolDTO.setAddGroup(true);
        pageQueryPoolDTO.setNotIn(true);
        Page<RcoViewUserEntity> userPage = userListService.pageUserInOrNotInPool(pageQueryPoolDTO);
        DefaultPageResponse<UserListDTO> response = new DefaultPageResponse<>();
        response.setTotal(userPage.getTotalElements());
        if (CollectionUtils.isEmpty(userPage.getContent())) {
            response.setItemArr(new UserListDTO[0]);
        } else {
            UserListDTO[] dtoArr = userQueryHelper.convertRcoViewUserEntity2UserListDTO(userPage.getContent());
            // 查询出已绑定磁盘的用户
            Set<String> userIdSet = viewUserDiskDAO.queryAllByDiskPoolTypeToSet(DiskPoolType.POOL);
            for (UserListDTO dto : dtoArr) {
                if (userIdSet.contains(dto.getId().toString())) {
                    dto.setHasBindDisk(true);
                }
            }
            response.setItemArr(dtoArr);
        }
        return response;
    }

    @Override
    public Optional<DiskPoolUserDTO> listPoolIdByRelatedId(UUID relatedId) throws BusinessException {
        Assert.notNull(relatedId, "listPoolIdByRelatedId relatedId can not be null");
        List<DiskPoolUserEntity> userEntityList = diskPoolUserDAO.findByRelatedId(relatedId);
        if (CollectionUtils.isEmpty(userEntityList)) {
            UUID groupId = cbbUserAPI.getUserGroupId(relatedId);
            userEntityList = diskPoolUserDAO.findByRelatedId(groupId);
        }
        // 用户或所属用户组已经被分配磁盘池，直接返回磁盘池信息
        if (CollectionUtils.isNotEmpty(userEntityList)) {
            DiskPoolUserEntity diskPoolUserEntity = userEntityList.get(0);
            DiskPoolUserDTO diskPoolUserDTO = new DiskPoolUserDTO();
            BeanUtils.copyProperties(diskPoolUserEntity, diskPoolUserDTO);
            return Optional.of(diskPoolUserDTO);
        }
        // 查询用户所属安全组是否有分配磁盘池，有则将用户分配关系加入到磁盘池中
        List<UUID> adGroupIdList = adGroupService.getUserRelatedAdGroupList(relatedId);
        if (CollectionUtils.isEmpty(adGroupIdList)) {
            return Optional.empty();
        }
        userEntityList = diskPoolUserDAO.findByRelatedIdInAndRelatedType(adGroupIdList, IacConfigRelatedType.AD_GROUP);
        // 用户所属的安全组分配关系不为空，则取第一个进行绑定
        if (CollectionUtils.isNotEmpty(userEntityList)) {
            DiskPoolUserEntity diskPoolUserEntity = adGroupAssignDiskPool(adGroupIdList, userEntityList);
            saveDiskPoolRelatedUser(diskPoolUserEntity.getDiskPoolId(), relatedId);
            DiskPoolUserDTO diskPoolUserDTO = new DiskPoolUserDTO();
            BeanUtils.copyProperties(diskPoolUserEntity, diskPoolUserDTO);
            return Optional.of(diskPoolUserDTO);
        }
        return Optional.empty();
    }

    private DiskPoolUserEntity adGroupAssignDiskPool(List<UUID> adGroupIdList, List<DiskPoolUserEntity> userEntityList) throws BusinessException {
        Map<UUID, DiskPoolUserEntity> poolUserEntityMap = userEntityList.stream()
                .collect(Collectors.toMap(DiskPoolUserEntity::getRelatedId, entity -> entity));
        for (UUID adGroupId : adGroupIdList) {
            DiskPoolUserEntity poolUserEntity = poolUserEntityMap.get(adGroupId);
            if (Objects.nonNull(poolUserEntity)) {
                return poolUserEntity;
            }
        }
        // 若找不到直接抛出异常
        throw new BusinessException(DiskPoolBusinessKey.RCDC_RCO_DISK_POOL_ASSIGN_AD_GROUP_ERROR);
    }

    @Override
    public Integer countAssignedUserNumByGroup(UUID groupId, UUID diskPoolId) {
        Assert.notNull(groupId, "groupId can not be null");
        Assert.notNull(diskPoolId, "diskPoolId can not be null");
        return diskPoolUserDAO.countAssignedUserNumByGroup(groupId, diskPoolId);
    }

    @Override
    public List<UserGroupAssignedUserNumDTO> countAssignedUserNumInGroupByDiskPoolId(UUID diskPoolId) {
        Assert.notNull(diskPoolId, "diskPoolId can not be null");

        List<Map<String, Object>> queryResultList = diskPoolUserDAO.countAssignedUserNumInGroupByDesktopPoolId(diskPoolId);

        List<UserGroupAssignedUserNumDTO> assignedUserNumDTOList = new ArrayList<>();
        queryResultList.forEach(item -> {
            UserGroupAssignedUserNumDTO assignDTO = JSON.parseObject(JSON.toJSONString(item), UserGroupAssignedUserNumDTO.class);
            assignedUserNumDTOList.add(assignDTO);
        });
        return assignedUserNumDTOList;
    }

    @Override
    public List<DiskPoolUserDTO> listDiskPoolUserByRelatedType(IacConfigRelatedType relatedType) {
        Assert.notNull(relatedType, "relatedType can not be null");

        List<DiskPoolUserEntity> userEntityList = diskPoolUserDAO.findByRelatedType(relatedType);
        if (CollectionUtils.isEmpty(userEntityList)) {
            return Collections.emptyList();
        }

        return userEntityList.stream().map(diskPoolUserEntity -> {
            DiskPoolUserDTO diskPoolUserDTO = new DiskPoolUserDTO();
            BeanUtils.copyProperties(diskPoolUserEntity, diskPoolUserDTO);
            return diskPoolUserDTO;
        }).collect(Collectors.toList());
    }

    @Override
    public void updatePoolBindObject(UpdatePoolBindObjectDTO bindObjectDTO) throws BusinessException {
        Assert.notNull(bindObjectDTO, "bindObjectDTO can not be null");
        Assert.notNull(bindObjectDTO.getPoolId(), "diskPoolId can not be null");
        String name = bindObjectDTO.getPoolId().toString();
        try {
            CbbDiskPoolDTO diskPoolDTO = cbbDiskPoolMgmtAPI.getDiskPoolDetail(bindObjectDTO.getPoolId());
            name = diskPoolDTO.getName();
            UUID poolId = bindObjectDTO.getPoolId();
            LockableExecutor.executeWithTryLock(DISK_POOL_UPDATE_BIND_LOCK + diskPoolDTO.getId().toString(), () -> {
                List<DiskPoolUserEntity> oldBindGroupList =
                        diskPoolUserDAO.findAllByDiskPoolIdAndRelatedType(poolId, IacConfigRelatedType.USERGROUP);
                List<DiskPoolUserEntity> oldBindAdGroupList =
                        diskPoolUserDAO.findAllByDiskPoolIdAndRelatedType(poolId, IacConfigRelatedType.AD_GROUP);
                // 1.检查新增的用户组/安全组是否已绑定其他池
                List<IacUserGroupDetailDTO> needAddGroupList = getNeedAddGroupAndCheckIsRepeatBind(bindObjectDTO, oldBindGroupList);
                List<UUID> needAddAdGroupList = getNeedAddAdGroupAndCheckIsRepeatBind(bindObjectDTO, oldBindAdGroupList);
                // 2.整合需要新增的用户，处理exceptList，取出组下所有用户ID，再根据exceptUserIdList删除对应用户，再全部加入到addUserByIdList中
                mergeAddUserAndExceptData(bindObjectDTO);
                // 3.检查新增的用户是否已绑定其他池
                checkIsUserRepeatBind(bindObjectDTO);
                // 4.检查要删除的用户是否绑定了磁盘
                checkDeleteUserIsBindDisk(bindObjectDTO);
                // 5.检查要删除的用户组/安全组是否有绑定了磁盘的用户，绑定了的用户要添加到新增用户中
                List<UUID> deleteGroupIdList = getNeedDeleteGroupAndCheckToAddBindUser(bindObjectDTO, oldBindGroupList);
                List<UUID> deleteAdGroupIdList = getNeedDeleteAdGroupAndCheckToAddBindUser(bindObjectDTO, oldBindAdGroupList);

                // 汇总需要删除的用户ID列表
                mergeDeleteUserIdList(bindObjectDTO);

                checkUserAndGroupExist(bindObjectDTO);
                // 检查安全组是否真是存在
                desktopPoolAPIHelper.checkAdGroupExist(bindObjectDTO);
                // 刷新数据
                bindObjectDTO.refresh();
                userDiskServiceTx.updatePoolBindObject(bindObjectDTO);

                // 保存审计日志
                saveUpdateBindObjLog(needAddGroupList, deleteGroupIdList, needAddAdGroupList, deleteAdGroupIdList, bindObjectDTO);

            }, 1);
        } catch (BusinessException e) {
            LOGGER.error(String.format("修改磁盘池[%s]绑定对像关联关系出现异常，参数[%s]", name, JSON.toJSONString(bindObjectDTO)), e);
            if (Objects.equals(e.getKey(), RESOURCE_LOCKED_ERROR_KEY)) {
                // 资源锁住的错误
                throw new BusinessException(DiskPoolBusinessKey.RCDC_RCO_DISK_POOL_UPDATE_BIND_TASK_REPEAT, e, name);
            }
            throw e;
        }
    }

    @Override
    public long getDisabledUserNum(UUID diskPoolId, @Nullable UUID groupId) {
        Assert.notNull(diskPoolId, "diskPoolId can not be null");
        if (Objects.isNull(groupId)) {
            return diskPoolUserDAO.countByDiskPoolIdNotAndRelatedType(diskPoolId, IacConfigRelatedType.USER);
        }
        return diskPoolUserDAO.countByDiskPoolIdNotAndUserGroupId(diskPoolId, groupId);
    }

    @Override
    public List<UserGroupDisabledUserNumDTO> countDisabledUserNumInGroupByDiskPoolId(UUID diskPoolId) {
        Assert.notNull(diskPoolId, "diskPoolId can not be null");
        List<Map<String, Object>> queryResultList = diskPoolUserDAO.countDisabledUserNumInGroupByDiskPoolId(diskPoolId);

        List<UserGroupDisabledUserNumDTO> disabledUserNumDTOList = new ArrayList<>();
        queryResultList.forEach(item -> {
            UserGroupDisabledUserNumDTO assignDTO = JSON.parseObject(JSON.toJSONString(item), UserGroupDisabledUserNumDTO.class);
            disabledUserNumDTOList.add(assignDTO);
        });
        return disabledUserNumDTOList;
    }

    @Override
    public Set<String> getDiskPoolRelationUserGroup(UUID diskPoolId) {
        Assert.notNull(diskPoolId, "findDiskPoolRelationUserGroup diskPoolId can not be null");
        return diskPoolUserDAO.findByDiskPoolIdToSet(diskPoolId);
    }

    @Override
    public void saveDiskPoolAssignFailWarnLog(String userName, CbbDiskPoolDTO diskPoolDTO) {
        Assert.hasText(userName, "userName can not be null");
        Assert.notNull(diskPoolDTO, "diskPoolDTO can not be null");

        String warnLog = LocaleI18nResolver.resolve(DiskPoolBusinessKey.RCDC_RCO_DISK_POOL_ASSIGN_FAIL, diskPoolDTO.getName(), userName);
        SaveAlarmRequest request = new SaveAlarmRequest();
        request.setAlarmLevel(AlarmLevel.TIPS);
        request.setAlarmType(AlarmConstants.ALARM_TYPE_RCDC_SERVICE);
        request.setAlarmNameByI18nKey(DiskPoolBusinessKey.RCDC_RCO_DISK_POOL_ASSIGN_FAIL_NAME);
        request.setAlarmContent(warnLog);
        request.setAlarmTime(new Date());
        request.setEnableSendMail(true);
        request.setAlarmCode(StringUtils.join(DISK_POOL_ASSIGN_FAIL, diskPoolDTO.getId()));
        alarmAPI.saveAlarm(request);
    }

    @Override
    public void saveDiskPoolRelatedUser(UUID diskPoolId, UUID userId) {
        Assert.notNull(diskPoolId, "addRelatedUserByDiskPoolId diskPoolId can not be null");
        Assert.notNull(userId, "addRelatedUserByDiskPoolId userId can not be null");
        DiskPoolUserEntity poolUserEntity = diskPoolUserDAO.findByDiskPoolIdAndRelatedId(diskPoolId, userId);
        if (Objects.isNull(poolUserEntity)) {
            poolUserEntity = new DiskPoolUserEntity();
            poolUserEntity.setDiskPoolId(diskPoolId);
            poolUserEntity.setRelatedId(userId);
            poolUserEntity.setRelatedType(IacConfigRelatedType.USER);
            poolUserEntity.setCreateTime(new Date());
            diskPoolUserDAO.save(poolUserEntity);
        }
    }

    private List<IacUserGroupDetailDTO> getNeedAddGroupAndCheckIsRepeatBind(UpdatePoolBindObjectDTO bindObjectDTO,
                                                                            List<DiskPoolUserEntity> oldBindGroupList) throws BusinessException {
        // 检查新增的组是否已绑定其他池
        Set<UUID> oldGroupIdSet = oldBindGroupList.stream().map(DiskPoolUserEntity::getRelatedId).collect(Collectors.toSet());
        List<UUID> updateGroupIdList = bindObjectDTO.getSelectedGroupIdList();
        List<UUID> needAddGroupIdList = Lists.newArrayList();
        if (CollectionUtils.isNotEmpty(updateGroupIdList)) {
            needAddGroupIdList = updateGroupIdList.stream().filter(item -> !oldGroupIdSet.contains(item)).collect(Collectors.toList());
        }
        List<DiskPoolUserEntity> otherBindList = diskPoolUserDAO.findByRelatedIdIn(needAddGroupIdList);
        otherBindList = otherBindList.stream().filter(item -> item.getRelatedType() == IacConfigRelatedType.USERGROUP).collect(Collectors.toList());
        if (CollectionUtils.isNotEmpty(otherBindList)) {
            // 根据组ID列表查询组信息列表
            List<UUID> groupIdList = otherBindList.stream().map(DiskPoolUserEntity::getRelatedId).collect(Collectors.toList());
            List<IacUserGroupDetailDTO> groupList = userService.listUserGroupByIds(groupIdList);
            if (CollectionUtils.isEmpty(groupList)) {
                return Collections.emptyList();
            }
            List<String> nameList = groupList.stream().map(IacUserGroupDetailDTO::getName).collect(Collectors.toList());
            throw new BusinessException(DiskPoolBusinessKey.RCDC_RCO_DISK_POOL_GROUP_BIND_REPEAT, StringUtils.join(nameList, NAME_SPLIT));
        }

        // 返回需要新增的组列表
        return userService.listUserGroupByIds(needAddGroupIdList);
    }

    private void mergeAddUserAndExceptData(UpdatePoolBindObjectDTO bindObjectDTO) throws BusinessException {
        List<UUID> groupIdList = bindObjectDTO.getAllExceptGroupIdList();
        // 查询已分配到其它池的用户
        Set<UUID> assignOtherPoolSet = diskPoolUserDAO.findByDiskPoolIdNotAndRelatedType(bindObjectDTO.getPoolId(),
                IacConfigRelatedType.USER).stream().map(DiskPoolUserEntity::getRelatedId).filter(Objects::nonNull).collect(Collectors.toSet());
        List<UUID> userIdOfExceptGroupList = new ArrayList<>();
        if (CollectionUtils.isNotEmpty(groupIdList)) {
            // 组下用户可能会超过1000,需要分页查询
            userIdOfExceptGroupList = getUserIdListByGroupIds(groupIdList, assignOtherPoolSet);
        }

        bindObjectDTO.mergeAllAddUserIdList(userIdOfExceptGroupList);
    }
    
    private List<UUID> getUserIdListByGroupIds(List<UUID> groupIdList, Set<UUID> assignOtherPoolSet) throws BusinessException {
        List<UUID> userIdList = new ArrayList<>();
        for (UUID groupId : groupIdList) {
            IacPageResponse<IacUserDetailDTO> pageResult = userService.pageQueryByGroupId(groupId, 0);
            // 总页数
            int totalPage = (int) Math.ceil((double) pageResult.getTotal() / Constants.MAX_QUERY_LIST_SIZE);
            for (int page = 0; page < totalPage; page++) {
                // 前面已查过，不重复查
                if (page == 0) {
                    userIdList.addAll(filterUserIdList(pageResult.getItemArr(), assignOtherPoolSet));
                    continue;
                }
                pageResult = userService.pageQueryByGroupId(groupId, page);
                if (pageResult.getTotal() == 0) {
                    break;
                }
                userIdList.addAll(filterUserIdList(pageResult.getItemArr(), assignOtherPoolSet));
            }
        }

        return userIdList;
    }
    
    private List<UUID> filterUserIdList(IacUserDetailDTO[] userDetailDTOArr, Set<UUID> assignOtherPoolSet) {
        return Arrays.stream(userDetailDTOArr)//
               .filter(item -> item.getUserType() != IacUserTypeEnum.VISITOR)//
               .map(IacUserDetailDTO::getId)//
               .filter(id -> !assignOtherPoolSet.contains(id))//
               .collect(Collectors.toList());
    }

    private void checkIsUserRepeatBind(UpdatePoolBindObjectDTO bindObjectDTO) throws BusinessException {
        // 检查新增的用户是否已绑定其他池
        List<UUID> addUserIdList = bindObjectDTO.getAddUserByIdList();
        if (CollectionUtils.isEmpty(addUserIdList)) {
            return;
        }
        if (addUserIdList.size() <= SQL_IN_MAX_NUM) {
            List<DiskPoolUserEntity> otherBindList = diskPoolUserDAO.findByDiskPoolIdNotAndRelatedIdInAndRelatedType(bindObjectDTO.getPoolId(),
                    addUserIdList, IacConfigRelatedType.USER);
            if (CollectionUtils.isNotEmpty(otherBindList)) {
                throw new BusinessException(DiskPoolBusinessKey.RCDC_RCO_DISK_POOL_USER_BIND_REPEAT, buildUserRepeatBindNames(otherBindList));
            }
            return;
        }
        // 分批去检查是否绑定其他池
        List<List<UUID>> tempIdList = Lists.partition(addUserIdList, SQL_IN_MAX_NUM);
        List<DiskPoolUserEntity> otherList;
        for (List<UUID> idList : tempIdList) {
            otherList =
                    diskPoolUserDAO.findByDiskPoolIdNotAndRelatedIdInAndRelatedType(bindObjectDTO.getPoolId(), idList, IacConfigRelatedType.USER);
            if (CollectionUtils.isNotEmpty(otherList)) {
                throw new BusinessException(DiskPoolBusinessKey.RCDC_RCO_DISK_POOL_USER_BIND_REPEAT, buildUserRepeatBindNames(otherList));
            }
        }
    }

    private String buildUserRepeatBindNames(List<DiskPoolUserEntity> repeatBindList) throws BusinessException {
        if (CollectionUtils.isEmpty(repeatBindList)) {
            return StringUtils.EMPTY;
        }
        List<UUID> idList = repeatBindList.stream().map(DiskPoolUserEntity::getRelatedId).collect(Collectors.toList());
        List<IacUserDetailDTO> userList = userService.listUserByIds(idList);
        if (CollectionUtils.isEmpty(userList)) {
            return StringUtils.EMPTY;
        }
        List<String> nameList = userList.stream().map(IacUserDetailDTO::getUserName).collect(Collectors.toList());
        return StringUtils.join(nameList, NAME_SPLIT);
    }

    private void checkDeleteUserIsBindDisk(UpdatePoolBindObjectDTO bindObjectDTO) throws BusinessException {
        // 检查要删除的用户是否绑定了磁盘
        List<UUID> deleteUserIdList = bindObjectDTO.getDeleteUserByIdList();
        if (CollectionUtils.isEmpty(deleteUserIdList)) {
            return;
        }
        // 分批检查是否有用户已经绑定了磁盘
        CbbDiskPoolDTO diskPoolDTO = cbbDiskPoolMgmtAPI.getDiskPoolDetail(bindObjectDTO.getPoolId());
        if (deleteUserIdList.size() <= SQL_IN_MAX_NUM) {
            List<ViewUserDiskEntity> diskBindList = viewUserDiskDAO.findByDiskPoolIdAndUserIdIn(bindObjectDTO.getPoolId(), deleteUserIdList);
            if (CollectionUtils.isNotEmpty(diskBindList)) {
                throw new BusinessException(DiskPoolBusinessKey.RCDC_RCO_DISK_POOL_USER_BIND_DISK_DELETE_ERROR,
                        buildUserIsBindDiskNames(diskBindList), diskPoolDTO.getName());
            }
            return;
        }

        // 分批检查是否有用户已经绑定了磁盘
        List<List<UUID>> tempIdList = Lists.partition(deleteUserIdList, SQL_IN_MAX_NUM);
        List<ViewUserDiskEntity> otherList;
        for (List<UUID> idList : tempIdList) {
            otherList = viewUserDiskDAO.findByDiskPoolIdAndUserIdIn(bindObjectDTO.getPoolId(), idList);
            if (CollectionUtils.isNotEmpty(otherList)) {
                throw new BusinessException(DiskPoolBusinessKey.RCDC_RCO_DISK_POOL_USER_BIND_DISK_DELETE_ERROR,
                        buildUserIsBindDiskNames(otherList), diskPoolDTO.getName());
            }
        }
    }

    private String buildUserIsBindDiskNames(List<ViewUserDiskEntity> userList) {
        if (CollectionUtils.isEmpty(userList)) {
            return StringUtils.EMPTY;
        }
        List<String> nameList = userList.stream().map(ViewUserDiskEntity::getUserName).collect(Collectors.toList());
        return StringUtils.join(nameList, NAME_SPLIT);
    }

    private List<UUID> getNeedDeleteGroupAndCheckToAddBindUser(UpdatePoolBindObjectDTO bindObjectDTO, List<DiskPoolUserEntity> oldBindGroupList) {
        // 检查要删除的组是否有绑定了磁盘的用户，绑定了的用户要添加到新增用户中
        List<UUID> updateGroupIdList = bindObjectDTO.getSelectedGroupIdList();
        List<UUID> deleteGroupIdList;
        if (CollectionUtils.isEmpty(updateGroupIdList)) {
            deleteGroupIdList = oldBindGroupList.stream().map(DiskPoolUserEntity::getRelatedId).collect(Collectors.toList());
        } else {
            Set<UUID> updateGroupIdSet = new HashSet<>(updateGroupIdList);
            deleteGroupIdList = oldBindGroupList.stream().map(DiskPoolUserEntity::getRelatedId)
                    .filter(relatedId -> !updateGroupIdSet.contains(relatedId)).collect(Collectors.toList());
        }
        deleteGroupIdList.addAll(Optional.ofNullable(bindObjectDTO.getDeleteUserByGroupIdList()).orElse(Lists.newArrayList()));
        if (CollectionUtils.isEmpty(deleteGroupIdList)) {
            return Collections.emptyList();
        }
        deleteGroupIdList = deleteGroupIdList.stream().distinct().collect(Collectors.toList());
        // 检查deleteGroupIdList下的用户是否绑定了磁盘，绑定了的用户要添加到新增用户中
        List<ViewUserDiskEntity> viewUserDiskList = viewUserDiskDAO.findByDiskPoolIdAndGroupIdIn(bindObjectDTO.getPoolId(), deleteGroupIdList);
        if (CollectionUtils.isEmpty(viewUserDiskList)) {
            return deleteGroupIdList;
        }
        List<UUID> userIdList = viewUserDiskList.stream().map(ViewUserDiskEntity::getUserId).collect(Collectors.toList());
        List<UUID> addUserByIdList = Optional.ofNullable(bindObjectDTO.getAddUserByIdList()).orElse(new ArrayList<>());
        addUserByIdList.addAll(userIdList);
        bindObjectDTO.setAddUserByIdList(addUserByIdList);
        return deleteGroupIdList;
    }

    private void mergeDeleteUserIdList(UpdatePoolBindObjectDTO bindObjectDTO) {
        // 这是前端上传过来需要去根据用户组删除用户绑定记录的用户组列表
        // 如果用户组被选了，其底下的用户记录需要清掉，不需要再记录了，因为他的组已经绑定这个池了
        List<UUID> finalDelUserByGroupIdList = bindObjectDTO.getSelectedGroupIdAndDeleteUserByGroupId();

        // 根据用户组去查询其下的用户ID列表
        List<UUID> needDeleteByGroupUserIdList = new ArrayList<>();
        if (CollectionUtils.isNotEmpty(finalDelUserByGroupIdList)) {
            needDeleteByGroupUserIdList = diskPoolUserDAO.listUserIdByDiskPoolIdAndUserGroupIds(bindObjectDTO.getPoolId(), finalDelUserByGroupIdList);
            needDeleteByGroupUserIdList = needDeleteByGroupUserIdList.stream().distinct().collect(Collectors.toList());
        }

        bindObjectDTO.mergeDeleteUserIdList(needDeleteByGroupUserIdList);
    }

    private void checkUserAndGroupExist(UpdatePoolBindObjectDTO bindObjectDTO) throws BusinessException {
        List<UUID> addUserIdList = bindObjectDTO.getAddUserByIdList();

        if (CollectionUtils.isNotEmpty(addUserIdList)) {
            List<List<UUID>> tempIdList = Lists.partition(addUserIdList, SQL_IN_MAX_NUM);
            for (List<UUID> idList : tempIdList) {
                if (!userService.checkAllUserExistByIds(idList)) {
                    throw new BusinessException(DiskPoolBusinessKey.RCDC_RCO_DISK_POOL_UPDATE_BIND_USER_NULL);
                }
                if (userService.checkAnyVisitor(idList)) {
                    throw new BusinessException(DiskPoolBusinessKey.RCDC_RCO_DISK_POOL_UPDATE_BIND_VISITOR_FAIL);
                }
            }
        }

        List<UUID> groupIdList = bindObjectDTO.getSelectedGroupIdList();
        if (CollectionUtils.isNotEmpty(groupIdList)) {
            List<List<UUID>> tempIdList = Lists.partition(groupIdList, SQL_IN_MAX_NUM);
            for (List<UUID> idList : tempIdList) {
                if (!userService.checkAllUserGroupExistByIds(idList)) {
                    throw new BusinessException(DiskPoolBusinessKey.RCDC_RCO_DISK_POOL_UPDATE_BIND_GROUP_NULL);
                }
            }
        }
    }

    private void saveUpdateBindObjLog(List<IacUserGroupDetailDTO> addGroupList, List<UUID> deleteGroupIdList,
                                      List<UUID> needAddAdGroupList, List<UUID> deleteAdGroupIdList,
                                      UpdatePoolBindObjectDTO bindObjectDTO) throws BusinessException {
        CbbDiskPoolDTO diskPoolDTO = cbbDiskPoolMgmtAPI.getDiskPoolDetail(bindObjectDTO.getPoolId());

        if (CollectionUtils.isNotEmpty(addGroupList)) {
            List<String> nameList = addGroupList.stream().map(IacUserGroupDetailDTO::getName).collect(Collectors.toList());
            auditLogAPI.recordLog(DiskPoolBusinessKey.RCDC_RCO_DISK_POOL_ADD_BIND_GROUP_LOG, diskPoolDTO.getName(),
                    StringUtils.join(nameList, NAME_SPLIT));
        }

        List<IacUserGroupDetailDTO> deleteGroupList = userService.listUserGroupByIds(deleteGroupIdList);
        if (CollectionUtils.isNotEmpty(deleteGroupList)) {
            List<String> nameList = deleteGroupList.stream().map(IacUserGroupDetailDTO::getName).collect(Collectors.toList());
            auditLogAPI.recordLog(DiskPoolBusinessKey.RCDC_RCO_DISK_POOL_DELETE_BIND_GROUP_LOG, diskPoolDTO.getName(),
                    StringUtils.join(nameList, NAME_SPLIT));
        }

        // 添加安全组的分配记录日志
        desktopPoolAPIHelper.savePoolUpdateAdGroupLog(diskPoolDTO.getName(),
                needAddAdGroupList, DiskPoolBusinessKey.RCDC_RCO_DISK_POOL_ADD_BIND_AD_GROUP_LOG);

        // 删除安全组的分配记录日志
        desktopPoolAPIHelper.savePoolUpdateAdGroupLog(diskPoolDTO.getName(),
                deleteAdGroupIdList, DiskPoolBusinessKey.RCDC_RCO_DISK_POOL_DELETE_BIND_AD_GROUP_LOG);

        List<UUID> addUserIdList = bindObjectDTO.getAddUserByIdList();
        saveDiskPoolAddUserLog(diskPoolDTO, addUserIdList);

        List<UUID> deleteUserIdList = bindObjectDTO.getDeleteUserByIdList();
        // 判断用户所在的组是否被加进来了，如果加进来了，这里就不用记录删除这个用户的记录
        saveDiskPoolDeleteUserLog(diskPoolDTO, deleteUserIdList, bindObjectDTO);
    }

    private void saveDiskPoolAddUserLog(CbbDiskPoolDTO diskPoolDTO, List<UUID> addUserIdList) throws BusinessException {
        if (CollectionUtils.isEmpty(addUserIdList)) {
            return;
        }
        List<List<UUID>> tempIdList = Lists.partition(addUserIdList, SQL_IN_MAX_NUM);
        for (List<UUID> idList : tempIdList) {
            List<IacUserDetailDTO> userList = userService.listUserByIds(idList);
            saveUpdateUserBindSysLog(diskPoolDTO, userList, DiskPoolBusinessKey.RCDC_RCO_DISK_POOL_ADD_BIND_USER_LOG);
        }
    }

    private void saveDiskPoolDeleteUserLog(CbbDiskPoolDTO diskPoolDTO, List<UUID> deleteUserIdList, UpdatePoolBindObjectDTO bindObjectDTO)
            throws BusinessException {
        if (CollectionUtils.isEmpty(deleteUserIdList)) {
            return;
        }
        Set<UUID> groupIdSet = new HashSet<>(Optional.ofNullable(bindObjectDTO.getSelectedGroupIdList()).orElse(Collections.emptyList()));
        // 判断用户所在的组是否被加进来了，如果加进来了，这里就不用记录删除这个用户的记录
        List<IacUserDetailDTO> userList = Lists.newArrayList();
        List<List<UUID>> tempIdList = Lists.partition(deleteUserIdList, SQL_IN_MAX_NUM);
        for (List<UUID> idList : tempIdList) {
            List<IacUserDetailDTO> tempUserList = userService.listUserByIds(idList);
            tempUserList = tempUserList.stream().filter(item -> !groupIdSet.contains(item.getGroupId())).collect(Collectors.toList());
            if (CollectionUtils.isEmpty(tempUserList)) {
                continue;
            }
            userList.addAll(tempUserList);
            if (userList.size() > SQL_IN_MAX_NUM) {
                saveUpdateUserBindSysLog(diskPoolDTO, userList, DiskPoolBusinessKey.RCDC_RCO_DISK_POOL_DELETE_BIND_USER_LOG);
                userList.clear();
            }
        }
        if (CollectionUtils.isNotEmpty(userList)) {
            saveUpdateUserBindSysLog(diskPoolDTO, userList, DiskPoolBusinessKey.RCDC_RCO_DISK_POOL_DELETE_BIND_USER_LOG);
        }
    }

    private void saveUpdateUserBindSysLog(CbbDiskPoolDTO diskPoolDTO, List<IacUserDetailDTO> userList, String key) {
        if (CollectionUtils.isEmpty(userList)) {
            return;
        }
        List<List<IacUserDetailDTO>> tempUserList = Lists.partition(userList, SINGLE_LOG_MAX_NUM);
        for (List<IacUserDetailDTO> subUserList : tempUserList) {
            List<String> nameList = subUserList.stream().map(IacUserDetailDTO::getUserName).collect(Collectors.toList());
            auditLogAPI.recordLog(key, diskPoolDTO.getName(), StringUtils.join(nameList, NAME_SPLIT));
        }
    }

    private List<UUID> getNeedAddAdGroupAndCheckIsRepeatBind(UpdatePoolBindObjectDTO bindObjectDTO,
                                                             List<DiskPoolUserEntity> oldBindAdGroupList) throws BusinessException {
        // 检查新增的安全组是否已绑定其他池
        Set<UUID> oldAdGroupIdSet = oldBindAdGroupList.stream().map(DiskPoolUserEntity::getRelatedId).collect(Collectors.toSet());
        List<UUID> updateAdGroupIdList = bindObjectDTO.getSelectedAdGroupIdList();
        List<UUID> needAddAdGroupIdList = Lists.newArrayList();
        if (CollectionUtils.isNotEmpty(updateAdGroupIdList)) {
            needAddAdGroupIdList = updateAdGroupIdList.stream().filter(item -> !oldAdGroupIdSet.contains(item)).collect(Collectors.toList());
        }
        List<DiskPoolUserEntity> otherBindList = diskPoolUserDAO.findByRelatedIdIn(needAddAdGroupIdList);
        otherBindList = otherBindList.stream().filter(item -> item.getRelatedType() == IacConfigRelatedType.AD_GROUP).collect(Collectors.toList());
        if (CollectionUtils.isNotEmpty(otherBindList)) {
            // 根据组ID列表查询组信息列表
            List<UUID> adGroupIdList = otherBindList.stream().map(DiskPoolUserEntity::getRelatedId).collect(Collectors.toList());
            List<IacAdGroupEntityDTO> adGroupList = adGroupPoolService.listAdGroupByIds(adGroupIdList);
            if (CollectionUtils.isEmpty(adGroupList)) {
                return Collections.emptyList();
            }
            List<String> nameList = adGroupList.stream().map(IacAdGroupEntityDTO::getName).collect(Collectors.toList());
            throw new BusinessException(DiskPoolBusinessKey.RCDC_RCO_DISK_POOL_AD_GROUP_BIND_REPEAT, StringUtils.join(nameList, NAME_SPLIT));
        }

        // 返回需要新增的安全组ID集合
        return adGroupPoolService.listAdGroupByIds(needAddAdGroupIdList).stream().map(IacAdGroupEntityDTO::getId).collect(Collectors.toList());
    }

    private List<UUID> getNeedDeleteAdGroupAndCheckToAddBindUser(UpdatePoolBindObjectDTO bindObjectDTO, List<DiskPoolUserEntity> oldBindGroupList) {
        // 检查要删除的安全组是否有绑定了磁盘的用户，绑定了的用户要添加到新增用户中
        List<UUID> updateAdGroupIdList = bindObjectDTO.getSelectedAdGroupIdList();
        List<UUID> deleteAdGroupIdList;
        if (CollectionUtils.isEmpty(updateAdGroupIdList)) {
            deleteAdGroupIdList = oldBindGroupList.stream().map(DiskPoolUserEntity::getRelatedId).collect(Collectors.toList());
        } else {
            Set<UUID> updateAdGroupIdSet = new HashSet<>(updateAdGroupIdList);
            deleteAdGroupIdList = oldBindGroupList.stream().map(DiskPoolUserEntity::getRelatedId)
                    .filter(relatedId -> !updateAdGroupIdSet.contains(relatedId)).collect(Collectors.toList());
        }
        if (CollectionUtils.isEmpty(deleteAdGroupIdList)) {
            return Collections.emptyList();
        }
        // 安全组删除无需进行加回用户，用户分配已做该操作
        return deleteAdGroupIdList.stream().distinct().collect(Collectors.toList());
    }
}
