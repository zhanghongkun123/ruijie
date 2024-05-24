package com.ruijie.rcos.rcdc.rco.module.impl.desktoppool.api;

import com.alibaba.fastjson.JSON;
import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import com.ruijie.rcos.base.alarm.module.def.api.BaseAlarmAPI;
import com.ruijie.rcos.gss.sdk.iac.module.def.enums.IacUserTypeEnum;
import com.ruijie.rcos.gss.sdk.iac.module.def.api.IacUserMgmtAPI;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.api.CbbDesktopPoolMgmtAPI;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.api.CbbVDIDeskStrategyMgmtAPI;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.dto.desktoppool.CbbDesktopPoolDTO;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.dto.strategy.CbbDeskStrategyDTO;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.enums.CbbCloudDeskState;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.enums.CbbCloudDeskType;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.enums.CbbDesktopSessionType;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.enums.desktoppool.CbbDesktopPoolModel;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.enums.desktoppool.DesktopPoolType;
import com.ruijie.rcos.rcdc.hciadapter.module.def.enums.DiskStatus;
import com.ruijie.rcos.rcdc.rco.module.def.api.DeskStrategyAPI;
import com.ruijie.rcos.rcdc.rco.module.def.api.DesktopPoolThirdPartyMgmtAPI;
import com.ruijie.rcos.rcdc.rco.module.def.api.UserDesktopMgmtAPI;
import com.ruijie.rcos.rcdc.rco.module.def.api.UserDiskMgmtAPI;
import com.ruijie.rcos.rcdc.rco.module.def.api.dto.CloudDesktopDetailDTO;
import com.ruijie.rcos.rcdc.rco.module.def.api.dto.UserListDTO;
import com.ruijie.rcos.rcdc.rco.module.def.api.enums.ComputerRelatedType;
import com.ruijie.rcos.rcdc.rco.module.def.api.request.UserAssignmentPageRequest;
import com.ruijie.rcos.rcdc.rco.module.def.desktoppool.dto.*;
import com.ruijie.rcos.rcdc.rco.module.impl.desktoppool.DesktopPoolAPIHelper;
import com.ruijie.rcos.rcdc.rco.module.impl.desktoppool.DesktopPoolBusinessKey;
import com.ruijie.rcos.rcdc.rco.module.impl.desktoppool.cache.DesktopPoolCache;
import com.ruijie.rcos.rcdc.rco.module.impl.desktoppool.entity.DesktopPoolComputerEntity;
import com.ruijie.rcos.rcdc.rco.module.impl.desktoppool.service.DesktopPoolComputerService;
import com.ruijie.rcos.rcdc.rco.module.impl.desktoppool.service.DesktopPoolService;
import com.ruijie.rcos.rcdc.rco.module.impl.entity.HostUserEntity;
import com.ruijie.rcos.rcdc.rco.module.impl.entity.RcoViewUserEntity;
import com.ruijie.rcos.rcdc.rco.module.impl.service.*;
import com.ruijie.rcos.rcdc.rco.module.impl.tx.DesktopPoolServiceTx;
import com.ruijie.rcos.sk.base.crypto.Md5Builder;
import com.ruijie.rcos.sk.base.exception.BusinessException;
import com.ruijie.rcos.sk.base.lockable.LockableExecutor;
import com.ruijie.rcos.sk.base.log.Logger;
import com.ruijie.rcos.sk.base.log.LoggerFactory;
import com.ruijie.rcos.sk.base.util.StringUtils;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.lang.Nullable;
import org.springframework.util.Assert;

import java.util.*;
import java.util.concurrent.atomic.AtomicReference;
import java.util.stream.Collectors;

/**
 * Description: 桌面池PC终端相关API
 * Copyright: Copyright (c) 2024
 * Company: Ruijie Co., Ltd.
 * Create Time: 2024/1/30
 *
 * @author zqj
 */
public class DesktopPoolThirdPartyMgmtAPIImpl implements DesktopPoolThirdPartyMgmtAPI {

    private static final Logger LOGGER = LoggerFactory.getLogger(DesktopPoolThirdPartyMgmtAPIImpl.class);

    private static final String RESOURCE_LOCKED_ERROR_KEY = "sk-resource_locked";

    private static final String DESKTOP_POOL_UPDATE_BIND_LOCK = "DESKTOP_POOL_UPDATE_BIND_LOCK_";


    private static final String DESKTOP_POOL_MOVE_LOCK = "DESKTOP_POOL_MOVE_LOCK_";

    private static final int LOCK_TIME = 10;

    private static final Integer SUCCEED = 0;

    private static final Set<CbbCloudDeskState> DESKTOP_READY_STATES_SET = Sets.newHashSet(CbbCloudDeskState.RUNNING, CbbCloudDeskState.CLOSE,
            CbbCloudDeskState.SLEEP);

    private static final Set<String> SUPPORT_MOVE_DESKTOP_TYPE_SET = Sets.newHashSet(DesktopPoolType.STATIC.name(), DesktopPoolType.COMMON.name());

    private static final Set<String> DESKTOP_MOVE_STATES_SET = Sets.newHashSet(CbbCloudDeskState.CLOSE.name(), CbbCloudDeskState.OFF_LINE.name());

    @Autowired
    private IacUserMgmtAPI cbbUserAPI;

    @Autowired
    private CbbDesktopPoolMgmtAPI cbbDesktopPoolMgmtAPI;

    @Autowired
    private DesktopPoolComputerService desktopPoolComputerService;

    @Autowired
    private DesktopPoolAPIHelper desktopPoolAPIHelper;

    @Autowired
    private UserDesktopService userDesktopService;

    @Autowired
    private DesktopPoolService desktopPoolService;

    @Autowired
    private DesktopPoolServiceTx desktopPoolServiceTx;

    @Autowired
    private QueryUserListService queryUserListService;

    @Autowired
    private UserQueryHelper userQueryHelper;

    @Autowired
    private BaseAlarmAPI alarmAPI;

    @Autowired
    private UserDiskMgmtAPI userDiskMgmtAPI;

    @Autowired
    private UserService userService;

    @Autowired
    private QueryCloudDesktopService queryCloudDesktopService;

    @Autowired
    private UserDesktopMgmtAPI userDesktopMgmtAPI;

    @Autowired
    private DeskStrategyAPI deskStrategyAPI;

    @Autowired
    private CbbVDIDeskStrategyMgmtAPI cbbVDIDeskStrategyMgmtAPI;

    @Autowired
    private DesktopPoolCache desktopPoolCache;

    @Autowired
    private HostUserService hostUserService;

    @Override
    public List<DesktopPoolComputerDTO> listDesktopPoolUser(UUID desktopPoolId, @Nullable ComputerRelatedType relatedType) {
        Assert.notNull(desktopPoolId, "desktopPoolId can not be null");

        return desktopPoolComputerService.listDesktopPoolUser(desktopPoolId, relatedType);
    }

    /**
     * 获取用户已绑定的桌面，如果没有优先返回用户个人盘绑定的桌面
     *
     * @param userId           userId
     * @param poolDeskInfoList poolDeskInfoList
     * @return PoolDesktopInfoDTO 未分配返回空
     */
    private PoolDesktopInfoDTO getBindingDesktop(UUID userId, List<PoolDesktopInfoDTO> poolDeskInfoList) {
        return poolDeskInfoList.stream().filter(desktopInfoDTO -> Objects.equals(userId, desktopInfoDTO.getUserId())).findFirst()
                .orElse(getUserDiskBindingDesktop(userId, poolDeskInfoList));
    }

    /**
     * 获取用户已绑定个人磁盘的桌面
     * @param userId 用户ID
     * @param poolDeskInfoList 池桌面集合
     * @return 桌面
     */
    private PoolDesktopInfoDTO getUserDiskBindingDesktop(UUID userId, List<PoolDesktopInfoDTO> poolDeskInfoList) {
        AtomicReference<PoolDesktopInfoDTO> atomicReference = new AtomicReference<>();
        userDiskMgmtAPI.diskDetailByUserId(userId)
                .filter(Objects::nonNull)
                .filter(userDiskDetailDTO -> Objects.nonNull(userDiskDetailDTO.getDeskId()))
                .filter(userDiskDetailDTO -> userDiskDetailDTO.getDiskState() != DiskStatus.ERROR)
                .ifPresent(userDiskDetailDTO -> atomicReference.set(
                         poolDeskInfoList.stream()
                        .filter(desktopInfoDTO -> Objects.equals(desktopInfoDTO.getDeskId(), userDiskDetailDTO.getDeskId()))
                        .findFirst()
                        .orElse(null)
                ));
        return atomicReference.get();
    }

    private PoolDesktopInfoDTO getRunningDeskInfo(List<PoolDesktopInfoDTO> poolDeskInfoList) {
        List<PoolDesktopInfoDTO> deskList =
                poolDeskInfoList.stream().filter(desktopDTO -> CbbCloudDeskState.RUNNING == desktopDTO.getDeskState()).collect(Collectors.toList());
        return sortDesktopList(deskList);
    }

    private PoolDesktopInfoDTO getAvailableDeskInfo(List<PoolDesktopInfoDTO> poolDesktopList) {
        List<PoolDesktopInfoDTO> sleepDesktopList = poolDesktopList.stream()
                .filter(desktopDTO -> CbbCloudDeskState.SLEEP == desktopDTO.getDeskState()).collect(Collectors.toList());
        if (CollectionUtils.isNotEmpty(sleepDesktopList)) {
            return sortDesktopList(sleepDesktopList);
        }
        List<PoolDesktopInfoDTO> deskList = poolDesktopList.stream()
                .filter(desktopDTO -> DESKTOP_READY_STATES_SET.contains(desktopDTO.getDeskState())).collect(Collectors.toList());
        return sortDesktopList(deskList);
    }

    private PoolDesktopInfoDTO sortDesktopList(List<PoolDesktopInfoDTO> deskList) {
        // 按创建顺序排序
        if (CollectionUtils.isEmpty(deskList)) {
            // 返回空
            return null;
        }
        deskList.sort(Comparator.comparing(PoolDesktopInfoDTO::getCreateTime));
        return deskList.get(0);
    }


    @Override
    public boolean checkUserInDesktopPool(UUID desktopPoolId, UUID userId) {
        Assert.notNull(desktopPoolId, "desktopPoolId can not be null");
        Assert.notNull(userId, "userId can not be null");

        return desktopPoolComputerService.checkUserInDesktopPool(desktopPoolId, userId);
    }

    @Override
    public Integer countAssignedUserNumByGroup(UUID groupId, UUID desktopPoolId) {
        Assert.notNull(groupId, "groupId can not be null");
        Assert.notNull(desktopPoolId, "desktopPoolId can not be null");
        return desktopPoolComputerService.countAssignedUserNumByGroup(groupId, desktopPoolId);
    }

    @Override
    public List<UserGroupAssignedUserNumDTO> countAssignedUserNumInGroupByDesktopPoolId(UUID desktopPoolId) {
        Assert.notNull(desktopPoolId, "desktopPoolId can not be null");
        return desktopPoolComputerService.countAssignedUserNumInGroupByDesktopPoolId(desktopPoolId);
    }

    @Override
    public void updatePoolBindObject(UpdatePoolThirdPartyBindObjectDTO bindObjectDTO) throws BusinessException {
        Assert.notNull(bindObjectDTO, "bindObjectDTO can not be null");
        Assert.notNull(bindObjectDTO.getPoolId(), "desktopPoolId can not be null");
        String name = bindObjectDTO.getPoolId().toString();
        try {
            CbbDesktopPoolDTO desktopPoolDTO = cbbDesktopPoolMgmtAPI.getDesktopPoolDetail(bindObjectDTO.getPoolId());
            name = desktopPoolDTO.getName();
            LockableExecutor.executeWithTryLock(DESKTOP_POOL_UPDATE_BIND_LOCK + desktopPoolDTO.getId().toString(), () -> {
                // 去重
                List<UUID> computerIdList = null;
                if (bindObjectDTO.getAddComputerByIdList() != null) {
                    computerIdList = bindObjectDTO.getAddComputerByIdList().stream().distinct().collect(Collectors.toList());
                }
                bindObjectDTO.setAddComputerByIdList(computerIdList);

                // 检查PC终端ID和PC终端组ID是否存在
                desktopPoolAPIHelper.checkComputerAndComputerGroupExist(bindObjectDTO);

                // 更新绑定关系
                desktopPoolServiceTx.updateThirdPartyPoolBindObject(bindObjectDTO);

                // 保存审计日志
                desktopPoolAPIHelper.saveUpdateThirdPartyBindObjLog(desktopPoolDTO, bindObjectDTO);
            }, 1);
        } catch (BusinessException e) {
            LOGGER.error("修改第三方桌面池[{}]绑定对像关联关系出现异常，参数[{}]", name, JSON.toJSONString(bindObjectDTO), e);
            if (Objects.equals(e.getKey(), RESOURCE_LOCKED_ERROR_KEY)) {
                // 资源锁住的错误
                throw new BusinessException(DesktopPoolBusinessKey.RCDC_RCO_THIRD_PARTY_DESKTOP_POOL_UPDATE_BIND_TASK_REPEAT, e, name);
            }
            throw e;
        } catch (Exception e) {
            LOGGER.error("修改第三方桌面池[{}]绑定对像关联关系出现异常，参数[{}]", name, JSON.toJSONString(bindObjectDTO), e);
            throw new BusinessException(DesktopPoolBusinessKey.RCDC_RCO_THIRD_PARTY_DESKTOP_POOL_UPDATE_BIND_ERROR, e, name);
        }
    }

    private List<UUID> getNeedAddGroupIdList(List<UUID> updateGroupIdList, List<DesktopPoolComputerDTO> oldBindGroupList) {
        Set<UUID> oldGroupIdSet = oldBindGroupList.stream().map(DesktopPoolComputerDTO::getRelatedId).collect(Collectors.toSet());
        if (CollectionUtils.isEmpty(updateGroupIdList)) {
            return Collections.emptyList();
        }
        return updateGroupIdList.stream().filter(item -> !oldGroupIdSet.contains(item)).distinct().collect(Collectors.toList());
    }

    private List<UUID> getNeedDeleteGroupIdList(List<UUID> updateGroupIdList, List<DesktopPoolComputerDTO> oldBindGroupList) {
        if (CollectionUtils.isEmpty(updateGroupIdList)) {
            return oldBindGroupList.stream().map(DesktopPoolComputerDTO::getRelatedId).collect(Collectors.toList());
        }
        Set<UUID> updateGroupIdSet = new HashSet<>(updateGroupIdList);
        return oldBindGroupList.stream().map(DesktopPoolComputerDTO::getRelatedId)
                .filter(relatedId -> !updateGroupIdSet.contains(relatedId)).collect(Collectors.toList());
    }



    private void mergeStaticPoolBindDesktopUser(UpdatePoolBindObjectDTO bindObjectDTO, CbbDesktopPoolDTO desktopPoolDTO,
                                                List<UUID> deleteGroupIdList) {
        if (desktopPoolDTO.getPoolModel() != CbbDesktopPoolModel.STATIC || CollectionUtils.isEmpty(deleteGroupIdList)) {
            return;
        }
        // 静态池如果删除用户组，组下已绑定桌面的用户要加回来
        List<PoolDesktopInfoDTO> desktopList = desktopPoolService.listNormalDesktopByPoolIdAndUserGroupIds(desktopPoolDTO.getId(),
                deleteGroupIdList);
        if (CollectionUtils.isEmpty(desktopList)) {
            return;
        }
        List<UUID> userIdList = desktopList.stream().map(PoolDesktopInfoDTO::getUserId).filter(Objects::nonNull).collect(Collectors.toList());
        List<UUID> addUserByIdList = Optional.ofNullable(bindObjectDTO.getAddUserByIdList()).orElse(new ArrayList<>());
        addUserByIdList.addAll(userIdList);
        bindObjectDTO.setAddUserByIdList(addUserByIdList.stream().distinct().collect(Collectors.toList()));
    }

    private void mergeDeleteUserIdList(UpdatePoolBindObjectDTO updateDTO, CbbDesktopPoolDTO desktopPoolDTO) {
        // 这是前端上传过来需要去根据用户组删除用户绑定记录的用户组列表
        // 如果用户组被选了，其底下的用户记录需要清掉，不需要再记录了，因为他的组已经绑定这个池了
        List<UUID> finalDelUserByGroupIdList = updateDTO.getSelectedGroupIdAndDeleteUserByGroupId();

        // 根据用户组去查询其下的用户ID列表
        List<UUID> needDeleteByGroupUserIdList = new ArrayList<>();
        if (CollectionUtils.isNotEmpty(finalDelUserByGroupIdList)) {
            needDeleteByGroupUserIdList = desktopPoolComputerService.listBindUserIdByUserGroupIds(updateDTO.getPoolId(), finalDelUserByGroupIdList);
            needDeleteByGroupUserIdList = needDeleteByGroupUserIdList.stream().distinct().collect(Collectors.toList());
        }

        updateDTO.mergeDeleteUserIdList(needDeleteByGroupUserIdList);

        if (desktopPoolDTO.getPoolModel() == CbbDesktopPoolModel.STATIC && CollectionUtils.isNotEmpty(updateDTO.getDeleteUserByIdList())) {
            // 防止已绑定静态池桌面的用户被删除，如果该用户所属的组未分配不需要删除该用户
            List<PoolDesktopInfoDTO> desktopList = desktopPoolService.listBindUserDesktopByPoolId(desktopPoolDTO.getId());
            if (CollectionUtils.isEmpty(desktopList)) {
                return;
            }
            Set<UUID> selectedGroupIdSet = new HashSet<>();
            if (Objects.nonNull(updateDTO.getSelectedGroupIdList())) {
                selectedGroupIdSet.addAll(updateDTO.getSelectedGroupIdList());
            }
            // 用户组不在桌面池里的用户ID的Set
            Set<UUID> bindDeskUserIdSet = desktopList.stream().filter(desk -> !selectedGroupIdSet.contains(desk.getUserGroupId()))
                    .map(PoolDesktopInfoDTO::getUserId).collect(Collectors.toSet());
            // 有绑定桌面且用户组不在池内的用户不能删除
            updateDTO.setDeleteUserByIdList(updateDTO.getDeleteUserByIdList().stream().filter(id -> !bindDeskUserIdSet.contains(id))
                    .collect(Collectors.toList()));
        }
    }

    private UserListDTO[] getFromAssignedAndUnAssigned(UserAssignmentPageRequest request, Page<RcoViewUserEntity> pageUserIn,
                                                       Page<RcoViewUserEntity> lastPageUserNotIn, Set<UUID> bindDesktopUserIdSet) {
        UserListDTO[] assignedUserArr = convert2UserListDTOArr(pageUserIn.getContent(), bindDesktopUserIdSet, true);
        // notIn部分不需要请求第二次
        int diffNum = request.getLimit() - pageUserIn.getContent().size();
        // 还差diffNum个用户凑成limit个
        if (lastPageUserNotIn.getContent().size() <= diffNum) {
            // lastPageUserNotIn + pageUserIn的数据不足一页，后面已经没数据了
            UserListDTO[] unAssignedUserArr = convert2UserListDTOArr(lastPageUserNotIn.getContent(), bindDesktopUserIdSet, false);
            return UserQueryHelper.mergeUserListDTOArr(assignedUserArr, unAssignedUserArr);
        }

        // 把lastPageUserNotIn里的部分记录和pageUserIn里的记录合成一页
        List<RcoViewUserEntity> subNotInUserList = lastPageUserNotIn.getContent().subList(0, diffNum);
        UserListDTO[] unAssignedUserArr = convert2UserListDTOArr(subNotInUserList, bindDesktopUserIdSet, false);
        return UserQueryHelper.mergeUserListDTOArr(assignedUserArr, unAssignedUserArr);
    }

    private UserListDTO[] getFromUnAssigned(UserAssignmentPageRequest request, Page<RcoViewUserEntity> lastPageUserNotIn, int pageUserInTotal,
                                            Set<UUID> bindDesktopUserIdSet) {
        request.setPage(request.getPage() - 1);
        int diffNumByLimit = request.getLimit() - pageUserInTotal % request.getLimit();

        Page<RcoViewUserEntity> prePageUserNotIn = queryUserListService.pageUserInOrNotInDesktopPool(request.getDesktopPoolId(), request, false);
        List<RcoViewUserEntity> preUserList = prePageUserNotIn.getContent();

        // lastPageUserNotIn为空，prePageUserNotIn的数据已经是最后的记录了
        if (CollectionUtils.isEmpty(lastPageUserNotIn.getContent())) {
            // 取prePageUserNotIn中的部分
            List<RcoViewUserEntity> finalUserList = preUserList.subList(diffNumByLimit, preUserList.size());
            return convert2UserListDTOArr(finalUserList, bindDesktopUserIdSet, false);
        }

        // 取prePageUserNotIn中的部分和lastPageUserNotIn中的部分
        List<RcoViewUserEntity> finalUserList = Lists.newArrayList(preUserList.subList(diffNumByLimit, preUserList.size()));
        int lastPageEndIndex = Math.min(diffNumByLimit, lastPageUserNotIn.getContent().size());
        finalUserList.addAll(lastPageUserNotIn.getContent().subList(0, lastPageEndIndex));
        return convert2UserListDTOArr(finalUserList, bindDesktopUserIdSet, false);
    }

    private UserListDTO[] convert2UserListDTOArr(List<RcoViewUserEntity> userEntityList, Set<UUID> userIdSet, boolean isInPool) {
        UserListDTO[] userListDTOArr = userQueryHelper.convertRcoViewUserEntity2UserListDTO(userEntityList);
        for (UserListDTO user : userListDTOArr) {
            user.setIsAssigned(isInPool);
            user.setHasBindPoolDesktop(userIdSet.contains(user.getId()));
        }
        return userListDTOArr;
    }


    private String buildAlarmCode(String type, String id, String content) {
        // 告警返回信息中没有明确方式区分alarmCode,这里基于资源类型，id和告警内容的md5做alarmCode
        StringBuilder sb = new StringBuilder();
        sb.append(type).append(id).append(content);
        byte[] byteArr = sb.toString().getBytes();
        byte[] md5ByteArr = new Md5Builder().update(byteArr, 0, byteArr.length).build();
        // 携带上resourceType，用以区分告警类型
        return type + "|" + StringUtils.bytes2Hex(md5ByteArr);
    }

    @Override
    public Set<String> getDesktopPoolRelationComputerGroup(UUID desktopPoolId) {
        Assert.notNull(desktopPoolId, "getDesktopPoolRelationUserGroup desktopPoolId can not be null");
        return desktopPoolComputerService.getDesktopPoolRelationComputerGroup(desktopPoolId);
    }


    @Override
    public void moveDesktop(CloudDesktopDetailDTO desktopDetailDTO, CbbDesktopPoolDTO targetDesktopPoolDTO) throws BusinessException {
        Assert.notNull(desktopDetailDTO, "desktopDetailDTO can not be null");
        Assert.notNull(targetDesktopPoolDTO, "targetDesktopPoolDTO can not be null");

        if (Objects.equals(desktopDetailDTO.getDesktopCategory(), CbbCloudDeskType.IDV.name()) ||
                Objects.equals(desktopDetailDTO.getDesktopCategory(), CbbCloudDeskType.VOI.name())) {
            throw new BusinessException(DesktopPoolBusinessKey.RCDC_RCO_MOVE_DESKTOP_ONLY_SUPPORT_VDI);
        }

        if (Objects.equals(desktopDetailDTO.getDesktopPoolId(), targetDesktopPoolDTO.getId())) {
            LOGGER.info("云桌面[{}]已在桌面池[{}]中无需移动", desktopDetailDTO.getDesktopName(), targetDesktopPoolDTO.getName());
            return;
        }

        // 访客桌面不能移动
        if (Objects.equals(desktopDetailDTO.getUserType(), IacUserTypeEnum.VISITOR.name())) {
            throw new BusinessException(DesktopPoolBusinessKey.RCDC_RCO_MOVE_DESKTOP_NOT_SUPPORT_VISITOR);
        }

        // 只支持移动关机和离线状态的云桌面
        if (!DESKTOP_MOVE_STATES_SET.contains(desktopDetailDTO.getDesktopState())) {
            throw new BusinessException(DesktopPoolBusinessKey.RCDC_RCO_MOVE_DESKTOP_STATE_NOT_SUPPORT);
        }

        // 只有普通桌面和静态桌面能移动
        if (!SUPPORT_MOVE_DESKTOP_TYPE_SET.contains(desktopDetailDTO.getDesktopPoolType())) {
            throw new BusinessException(DesktopPoolBusinessKey.RCDC_RCO_MOVE_DESKTOP_TYPE_NOT_SUPPORT, desktopDetailDTO.getDesktopName());
        }

        // 只能移动到静态池
        if (targetDesktopPoolDTO.getPoolModel() != CbbDesktopPoolModel.STATIC) {
            throw new BusinessException(DesktopPoolBusinessKey.RCDC_RCO_MOVE_DESKTOP_TARGET_MUST_POOL);
        }

        // 桌面会话类型与桌面池会话类型不同，不能移动
        if (targetDesktopPoolDTO.getSessionType() != desktopDetailDTO.getSessionType()) {
            throw new BusinessException(DesktopPoolBusinessKey.RCDC_RCO_MOVE_DESKTOP_SESSION_TYPE_ERROR);
        }

        // 桌面类型与桌面池不同，VDI不能移动到第三方，第三方不能移动到VDI
        if (!desktopDetailDTO.getDesktopCategory().equals(targetDesktopPoolDTO.getPoolType().name())) {
            throw new BusinessException(DesktopPoolBusinessKey.RCDC_RCO_MOVE_DESKTOP_STRATEGY_NOT_SAME_TYPE);
        }

        CbbDeskStrategyDTO poolDeskStrategyDTO = cbbVDIDeskStrategyMgmtAPI.getDeskStrategy(targetDesktopPoolDTO.getStrategyId());
        // 云桌面策略中云桌面类型必须是相同的，同为个性或者同为还原
        if (Objects.nonNull(poolDeskStrategyDTO.getPattern()) && Objects.nonNull(desktopDetailDTO.getDesktopType())) {
            if (!Objects.equals(poolDeskStrategyDTO.getPattern().name(), desktopDetailDTO.getDesktopType())) {
                throw new BusinessException(DesktopPoolBusinessKey.RCDC_RCO_MOVE_DESKTOP_STRATEGY_NOT_SAME_TYPE);
            }
        }

        MoveDesktopDTO moveDesktopDTO = new MoveDesktopDTO();
        moveDesktopDTO.setDesktopId(desktopDetailDTO.getId());
        moveDesktopDTO.setTargetDesktopPoolId(targetDesktopPoolDTO.getId());
        moveDesktopDTO.setDesktopPoolType(DesktopPoolType.convertToPoolDeskType(targetDesktopPoolDTO.getPoolModel()));

        List<UUID> userIdList = new ArrayList<>();
        // 获取与多会话桌面关联的用户
        List<HostUserEntity> entityList = hostUserService.findByDeskId(desktopDetailDTO.getId());
        if (CbbDesktopSessionType.MULTIPLE == desktopDetailDTO.getSessionType() && entityList != null) {
            userIdList = entityList.stream().map(HostUserEntity::getUserId).collect(Collectors.toList());
            userIdList.removeIf(userId -> !checkUserInDesktopPool(targetDesktopPoolDTO.getId(), userId));
            moveDesktopDTO.setUserIdList(userIdList);
        } else {
            if (Objects.nonNull(desktopDetailDTO.getUserId()) && !checkUserInDesktopPool(targetDesktopPoolDTO.getId(),
                    desktopDetailDTO.getUserId())) {
                userIdList.add(desktopDetailDTO.getUserId());
                moveDesktopDTO.setUserIdList(userIdList);
            }
        }

        LockableExecutor.executeWithTryLock(DESKTOP_POOL_MOVE_LOCK + moveDesktopDTO.getTargetDesktopPoolId().toString(), () -> {
            // 检查用户在静态池中是否已有绑定的桌面
            if (!CollectionUtils.isEmpty(moveDesktopDTO.getUserIdList())
                    && desktopPoolComputerService.checkUserIdListBindDesktopInPool(targetDesktopPoolDTO.getId(), moveDesktopDTO.getUserIdList())) {
                throw new BusinessException(DesktopPoolBusinessKey.RCDC_RCO_MOVE_DESKTOP_USER_ALREADY_BIND_IN_POOL);
            }
            try {
                desktopPoolCache.checkUserAndIncreaseDesktopNum(moveDesktopDTO.getTargetDesktopPoolId(), moveDesktopDTO.getDesktopId(),
                        moveDesktopDTO.getUserIdList());
                // 修改桌面并添加用户到桌面池
                desktopPoolServiceTx.moveDesktop(moveDesktopDTO);
            } finally {
                desktopPoolCache.reduceDesktopNum(moveDesktopDTO.getTargetDesktopPoolId(), moveDesktopDTO.getDesktopId(),
                        moveDesktopDTO.getUserIdList());
            }
        }, 1);

        if (moveDesktopDTO.getUserIdList() != null && !moveDesktopDTO.getUserIdList().isEmpty()) {
            CbbDesktopPoolDTO desktopPoolDTO = cbbDesktopPoolMgmtAPI.getDesktopPoolDetail(moveDesktopDTO.getTargetDesktopPoolId());
            // 添加用户被加入桌面池的审计日志
            desktopPoolAPIHelper.saveDesktopPoolAddUserLog(desktopPoolDTO, moveDesktopDTO.getUserIdList());
        }
    }

    @Override
    public boolean checkUserBindDesktopInPool(UUID desktopPoolId, UUID userId) {
        Assert.notNull(desktopPoolId, "desktopPoolId can not be null");
        Assert.notNull(userId, "userId can not be null");
        return desktopPoolComputerService.checkUserBindDesktopInPool(desktopPoolId, userId);
    }



    @Override
    public int countAssignedComputerNumInGroupByDesktopPoolId(UUID[] computerIdArr, UUID desktopPoolId) {
        Assert.notNull(desktopPoolId, "desktopPoolId can not be null");
        Assert.notEmpty(computerIdArr, "computerIdArr can not be empty");
        return desktopPoolComputerService.countAssignedComputerNumInGroupByDesktopPoolId(computerIdArr, desktopPoolId);
    }

    @Override
    public List<DesktopPoolComputerDTO> getDesktopPoolRelationComputerGroupList(List<UUID> groupIdArr) {
        Assert.notEmpty(groupIdArr, "groupIdArr can not be empty");
        List<DesktopPoolComputerEntity> poolComputerEntityList = desktopPoolComputerService.
                findDesktopPoolRelationComputerGroupList(groupIdArr);
        return buildDesktopPoolComputerDTO(poolComputerEntityList);
    }

    @Override
    public List<DesktopPoolComputerDTO> getDesktopPoolRelationComputerList(UUID[] computerIdArr) {
        Assert.notEmpty(computerIdArr, "computerIdArr can not be empty");
        List<DesktopPoolComputerEntity> poolComputerEntityList =  desktopPoolComputerService.findDesktopPoolRelationComputerList(computerIdArr);
        return buildDesktopPoolComputerDTO(poolComputerEntityList);
    }

    private static List<DesktopPoolComputerDTO> buildDesktopPoolComputerDTO(List<DesktopPoolComputerEntity> poolComputerEntityList) {
        List<DesktopPoolComputerDTO> computerDTOList = poolComputerEntityList.stream().map(desktopPoolComputerDTO -> {
            DesktopPoolComputerDTO dto = new DesktopPoolComputerDTO();
            BeanUtils.copyProperties(desktopPoolComputerDTO, dto);
            return dto;
        }).collect(Collectors.toList());
        return computerDTOList;
    }
}
