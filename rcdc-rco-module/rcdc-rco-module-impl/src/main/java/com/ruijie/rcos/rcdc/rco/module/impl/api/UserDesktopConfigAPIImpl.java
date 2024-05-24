package com.ruijie.rcos.rcdc.rco.module.impl.api;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.UUID;
import java.util.stream.Collectors;

import com.ruijie.rcos.rcdc.rco.module.common.query.ConditionQueryRequest;
import com.ruijie.rcos.rcdc.rco.module.def.api.*;
import com.ruijie.rcos.sk.pagekit.api.PageQueryRequest;
import com.ruijie.rcos.sk.pagekit.api.PageQueryResponse;
import org.apache.commons.lang3.ArrayUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.util.Assert;

import com.ruijie.rcos.gss.sdk.iac.module.def.dto.user.IacUserDetailDTO;
import com.ruijie.rcos.gss.sdk.iac.module.def.dto.user.IacUserGroupDetailDTO;
import com.ruijie.rcos.gss.sdk.iac.module.def.api.IacUserGroupMgmtAPI;
import com.ruijie.rcos.gss.sdk.iac.module.def.api.IacUserMgmtAPI;
import com.ruijie.rcos.rcdc.rco.module.def.api.DesktopPoolUserMgmtAPI;
import com.ruijie.rcos.rcdc.rco.module.def.api.DiskPoolUserAPI;
import com.ruijie.rcos.rcdc.rco.module.def.api.UserDesktopConfigAPI;
import com.ruijie.rcos.rcdc.rco.module.def.api.UserDesktopMgmtAPI;
import com.ruijie.rcos.rcdc.rco.module.def.api.dto.ExportUserPageDTO;
import com.ruijie.rcos.rcdc.rco.module.def.api.dto.ExportUserViewDTO;
import com.ruijie.rcos.rcdc.rco.module.def.api.dto.UserDesktopConfigDTO;
import com.ruijie.rcos.rcdc.rco.module.def.api.dto.UserGroupDesktopConfigDTO;
import com.ruijie.rcos.rcdc.rco.module.def.api.dto.UserListDTO;
import com.ruijie.rcos.rcdc.rco.module.def.api.request.CreateUserDesktopConfigRequest;
import com.ruijie.rcos.rcdc.rco.module.def.api.request.CreateUserGroupDesktopConfigRequest;
import com.ruijie.rcos.rcdc.rco.module.def.api.request.PageSearchRequest;
import com.ruijie.rcos.rcdc.rco.module.def.api.request.UserAssignmentPageRequest;
import com.ruijie.rcos.rcdc.rco.module.def.api.request.UserPageSearchRequest;
import com.ruijie.rcos.rcdc.rco.module.def.api.response.UserWithAssignmentPageResponse;
import com.ruijie.rcos.rcdc.rco.module.def.certificationstrategy.dto.PwdStrategyDTO;
import com.ruijie.rcos.rcdc.rco.module.def.constants.Constants;
import com.ruijie.rcos.rcdc.rco.module.def.constants.PageQuerySourceConstants;
import com.ruijie.rcos.rcdc.rco.module.def.deskstrategy.enums.DesktopTempPermissionRelatedType;
import com.ruijie.rcos.rcdc.rco.module.def.enums.UserCloudDeskTypeEnum;
import com.ruijie.rcos.rcdc.rco.module.def.user.common.UserCommonHelper;
import com.ruijie.rcos.rcdc.rco.module.impl.cache.ExportUserDataCacheMgt;
import com.ruijie.rcos.rcdc.rco.module.impl.common.RcoInvalidTimeHelper;
import com.ruijie.rcos.rcdc.rco.module.impl.dao.RcoViewUserDAO;
import com.ruijie.rcos.rcdc.rco.module.impl.deskstrategy.dto.DesktopTempPermissionRelationDTO;
import com.ruijie.rcos.rcdc.rco.module.impl.deskstrategy.service.DesktopTempPermissionService;
import com.ruijie.rcos.rcdc.rco.module.impl.entity.RcoViewUserEntity;
import com.ruijie.rcos.rcdc.rco.module.impl.service.QueryUserListService;
import com.ruijie.rcos.rcdc.rco.module.impl.service.UserDesktopConfigService;
import com.ruijie.rcos.rcdc.rco.module.impl.service.UserQueryHelper;
import com.ruijie.rcos.sk.base.exception.BusinessException;
import com.ruijie.rcos.sk.modulekit.api.comm.DefaultPageResponse;

/**
 * Description: Function Description
 * Copyright: Copyright (c) 2020
 * Company: Ruijie Co., Ltd.
 * Create Time: 2020/7/22
 *
 * @author jarman
 */
public class UserDesktopConfigAPIImpl implements UserDesktopConfigAPI {

    /**
     * 用户永久锁定时的默认值
     */
    private static final int PERMANENT_LOCK = -1;

    private static final String DEF_USER_GROUP_NAME = "--";

    private static final Long TIME_CONVERSION_UNIT = 24 * 60 * 60 * 1000L;

    private static final Long EXPIRE_TIME_ZERO = 0L;

    private static final String INVALID_TRUE = "是";

    private static final String INVALID_FALSE = "否";

    @Autowired
    private UserDesktopConfigService userDesktopConfigService;

    @Autowired
    private QueryUserListService queryUserListService;

    @Autowired
    private UserQueryHelper userQueryHelper;

    @Autowired
    private RcoViewUserDAO rcoViewUserDAO;

    @Autowired
    private IacUserGroupMgmtAPI cbbUserGroupAPI;

    @Autowired
    private ExportUserDataCacheMgt cacheMgt;

    @Autowired
    private DiskPoolUserAPI diskPoolUserAPI;

    @Autowired
    private DesktopPoolUserMgmtAPI desktopPoolUserMgmtAPI;

    @Autowired
    private DesktopTempPermissionService desktopTempPermissionService;

    @Autowired
    private UserCommonHelper userCommonHelper;

    @Autowired
    private UserDesktopMgmtAPI userDesktopMgmtAPI;

    @Autowired
    RcoInvalidTimeHelper rcoInvalidTimeHelper;

    @Autowired
    IacUserMgmtAPI cbbUserAPI;

    @Autowired
    private RcoGroupMemberAPI rcoGroupMemberAPI;

    @Override
    public void createOrUpdateUserDesktopConfig(CreateUserDesktopConfigRequest request) {
        Assert.notNull(request, "request cannot be null;");

        userDesktopConfigService.createOrUpdateUserDesktopConfig(request);
    }

    @Override
    public void createOrUpdateUserGroupDesktopConfig(CreateUserGroupDesktopConfigRequest request) {
        Assert.notNull(request, "request cannot be null;");

        userDesktopConfigService.createOrUpdateUserGroupDesktopConfig(request);
    }

    @Override
    public UserDesktopConfigDTO getUserDesktopConfig(UUID userId, UserCloudDeskTypeEnum deskType) {
        Assert.notNull(userId, "userId cannot be null;");
        Assert.notNull(deskType, "deskType cannot be null;");

        return userDesktopConfigService.getUserDesktopConfig(userId, deskType);
    }

    @Override
    public void deleteUserDesktopConfig(UUID userId, UserCloudDeskTypeEnum deskType) {
        Assert.notNull(userId, "userId cannot be null;");
        Assert.notNull(deskType, "deskType cannot be null;");

        userDesktopConfigService.deleteUserDesktopConfig(userId, deskType);
    }

    @Override
    public void deleteUserGroupDesktopConfig(UUID groupId, UserCloudDeskTypeEnum deskType) {
        Assert.notNull(groupId, "userId cannot be null;");
        Assert.notNull(deskType, "deskType cannot be null;");

        userDesktopConfigService.deleteUserGroupDesktopConfig(groupId, deskType);
    }

    @Override
    public UserGroupDesktopConfigDTO getUserGroupDesktopConfig(UUID groupId, UserCloudDeskTypeEnum deskType) {
        Assert.notNull(groupId, "userId cannot be null;");
        Assert.notNull(deskType, "deskType cannot be null;");

        return userDesktopConfigService.getUserGroupDesktopConfig(groupId, deskType);
    }

    @Override
    public void deleteVisitorUserDesktopConfig(UUID userId) {
        Assert.notNull(userId, "userId cannot be null;");

        userDesktopConfigService.deleteVisitorUserDesktopConfig(userId);
    }


    @Override
    public List<UserGroupDesktopConfigDTO> getUserGroupDesktopConfigList(UUID networkId) {
        Assert.notNull(networkId, "networkId cannot be null;");
        return userDesktopConfigService.getUserGroupDesktopConfigList(networkId);
    }

    @Override
    public List<ExportUserViewDTO> getAllUserList() throws BusinessException {

        List<RcoViewUserEntity> entityList = rcoViewUserDAO.findAll();

        List<ExportUserViewDTO> excelDTOArrayList = new ArrayList<>();
        for (RcoViewUserEntity rcoViewUserEntity : entityList) {
            ExportUserViewDTO dto = new ExportUserViewDTO();
            BeanUtils.copyProperties(rcoViewUserEntity, dto);

            IacUserGroupDetailDTO cbbUserGroupDetailDTO = cbbUserGroupAPI.getUserGroupDetail(rcoViewUserEntity.getGroupId());
            dto.setGroupName(cbbUserGroupDetailDTO.getName());
            while (cbbUserGroupDetailDTO.getParentId() != null) {
                cbbUserGroupDetailDTO = cbbUserGroupAPI.getUserGroupDetail(cbbUserGroupDetailDTO.getParentId());
                dto.setGroupName(cbbUserGroupDetailDTO.getName() + "/" + dto.getGroupName());
            }

            excelDTOArrayList.add(dto);
        }

        return excelDTOArrayList;
    }

    @Override
    public List<ExportUserViewDTO> getUserList(List<UUID> idArr) throws BusinessException {
        Assert.notNull(idArr, "idArr不能为null");

        List<RcoViewUserEntity> entityList =  rcoViewUserDAO.findByIdIn(idArr);

        List<ExportUserViewDTO> excelDTOArrayList = new ArrayList<>();
        for (int i = 0; i < entityList.size(); i++) {
            ExportUserViewDTO dto = new ExportUserViewDTO();
            BeanUtils.copyProperties(entityList.get(i), dto);

            IacUserGroupDetailDTO cbbUserGroupDetailDTO = cbbUserGroupAPI.getUserGroupDetail(entityList.get(i).getGroupId());
            dto.setGroupName(cbbUserGroupDetailDTO.getName());
            while (cbbUserGroupDetailDTO.getParentId() != null) {
                cbbUserGroupDetailDTO = cbbUserGroupAPI.getUserGroupDetail(cbbUserGroupDetailDTO.getParentId());
                dto.setGroupName(cbbUserGroupDetailDTO.getName() + "/" + dto.getGroupName());
            }

            excelDTOArrayList.add(dto);
        }

        return excelDTOArrayList;
    }

    @Override
    public DefaultPageResponse<UserListDTO> pageQuery(PageSearchRequest request) throws BusinessException {
        Assert.notNull(request, "PageSearchRequest不能为null");

        Page<RcoViewUserEntity> page = queryUserListService.pageQuery(request, RcoViewUserEntity.class);
        List<RcoViewUserEntity> entityList = page.getContent();
        UserListDTO[] dtoArr = userQueryHelper.convertRcoViewUserEntity2UserListDTO(entityList);
        DefaultPageResponse<UserListDTO> response = new DefaultPageResponse<>();
        response.setItemArr(dtoArr);
        response.setTotal(page.getTotalElements());
        return response;
    }

    @Override
    public boolean isStrategyBind(UUID strategyId) {
        Assert.notNull(strategyId, "strategyId can not be nul");
        return userDesktopConfigService.isStrategyBind(strategyId);
    }

    @Override
    public boolean hasImageBindUserGroup(UUID imageId) {
        Assert.notNull(imageId, "imageId can not be null");
        return userDesktopConfigService.hasImageBindUserGroup(imageId);
    }

    /**
     * 参考optional的ofNullable方法重写校验方法
     * @param value
     * @param defaultValue
     * @param <T>
     * @return
     */
    private <T> T checkToGetDefaultValue(T value, T defaultValue) {
        return value == null ? defaultValue : value;
    }

    /**
     * 判断用户是否上锁
     * @param pwdStrategyDTO
     * @param dto
     * @param realUnLockTime
     * @return boolean
     */
    private boolean checkUserIsLock(PwdStrategyDTO pwdStrategyDTO,UserListDTO dto,Date realUnLockTime) {
        // 如果没有开启防爆功能，则用户都处于未锁定状态
        if (!pwdStrategyDTO.getPreventsBruteForce()) {
            return false;
        }

        boolean isLock = checkToGetDefaultValue(dto.getLock(),false);
        // 1.基于防暴设置位true,锁定时长不等于null判断是否上锁
        // 2.基于lockTime重新判断isLock字段
        if (pwdStrategyDTO.getPreventsBruteForce() && dto.getLockTime() != null) {
            if (pwdStrategyDTO.getUserLockTime() != PERMANENT_LOCK) {
                // 指定时长锁定
                isLock = dto.getLockTime().after(realUnLockTime);
            } else {
                // 永久锁定
                isLock = true;
            }
        }
        return isLock;
    }

    @Override
    public ExportUserPageDTO getExportUserList(UserPageSearchRequest request, String cacheKey) throws BusinessException {
        Assert.notNull(request, "request不能为null");
        Assert.notNull(cacheKey, "cacheKey不能为null");

        List<RcoViewUserEntity> entityList;

        Page<RcoViewUserEntity> entityPage = queryUserListService.pageQuery(request, RcoViewUserEntity.class);
        entityList = entityPage.getContent();
        int totalPage = entityPage.getTotalPages();
        long totalElements = entityPage.getTotalElements();

        //提升大数据量导出效率，减少提取用户组名频繁查数据库问题
        if (ArrayUtils.isEmpty(this.cacheMgt.getUserGroupCache(cacheKey))) {
            IacUserGroupDetailDTO[] allArr = cbbUserGroupAPI.getAllUserGroup();
            this.cacheMgt.saveUserGroupCache(cacheKey, allArr);
        }

        List<ExportUserViewDTO> excelDTOArrayList = new ArrayList<>();
        for (int i = 0; i < entityList.size(); i++) {
            ExportUserViewDTO dto = new ExportUserViewDTO();
            BeanUtils.copyProperties(entityList.get(i), dto);
            dto.setPassword(Constants.SECRET_USER_PASSWORD);
            IacUserGroupDetailDTO cbbUserGroupDetailDTO = this.filterUserGroup(entityList.get(i).getGroupId(), cacheKey);
            dto.setGroupName(cbbUserGroupDetailDTO.getName());
            while (cbbUserGroupDetailDTO.getParentId() != null) {
                cbbUserGroupDetailDTO = this.filterUserGroup(cbbUserGroupDetailDTO.getParentId(), cacheKey);
                dto.setGroupName(cbbUserGroupDetailDTO.getName() + "/" + dto.getGroupName());
            }
            dto.setDescription(entityList.get(i).getUserDescription());
            dto.setInvalidDescription(dealInvalidDescription(entityList.get(i)));
            excelDTOArrayList.add(dto);
        }

        ExportUserPageDTO pageDTO = new ExportUserPageDTO();
        pageDTO.setTotalPages(totalPage);
        pageDTO.setExportUserViewDTOList(excelDTOArrayList);
        pageDTO.setTotalElements(totalElements);
        return pageDTO;
    }

    private IacUserGroupDetailDTO filterUserGroup(UUID groupId, String key) {
        IacUserGroupDetailDTO[] userGroupDetailDTOArr = this.cacheMgt.getUserGroupCache(key);
        for (IacUserGroupDetailDTO userGroupDetailDTO : userGroupDetailDTOArr) {
            if (groupId.equals(userGroupDetailDTO.getId())) {
                return userGroupDetailDTO;
            }
        }
        IacUserGroupDetailDTO userGroupDetailDTO = new IacUserGroupDetailDTO();
        userGroupDetailDTO.setName(DEF_USER_GROUP_NAME);
        userGroupDetailDTO.setId(groupId);
        return userGroupDetailDTO;
    }

    private String dealInvalidDescription(RcoViewUserEntity entity) throws BusinessException {

        IacUserDetailDTO userDetail = cbbUserAPI.getUserDetail(entity.getId());
        if (rcoInvalidTimeHelper.isAccountInvalid(userDetail)) {
            return INVALID_TRUE;
        } else {
            return INVALID_FALSE;
        }
    }

    @Override
    public UserWithAssignmentPageResponse pageQueryWithAssignment(UserAssignmentPageRequest pageRequest) throws BusinessException {
        Assert.notNull(pageRequest, "UserAssignmentPageRequest不能为null");

        if (Objects.equals(pageRequest.getQuerySource(), PageQuerySourceConstants.DESKTOP_TEMP_PERMISSION) ||
                Objects.nonNull(pageRequest.getDesktopTempPermissionId())) {
            // 云桌面临时权限查询用户及分配信息(临时权限需要查访客信息)
            return pageQueryDesktopTempPermissionAssignment(pageRequest);
        }

        // 池桌面需要排除 添加排除访客类型用户条件
        userCommonHelper.dealNonVisitorUserTypeMatch(pageRequest);

        if (Objects.nonNull(pageRequest.getDiskPoolId())) {
            // 磁盘池查询用户及分配信息
            return pageQueryDiskAssignment(pageRequest);
        }

        if (Objects.nonNull(pageRequest.getDesktopPoolId())) {
            // 桌面池查询用户及分配信息
            return pageQueryDesktopPoolAssignment(pageRequest);
        }

        if (Objects.nonNull(pageRequest.getAppGroupId())) {
            // 应用分组查询用户及分配信息
            return pageQueryAppGroupAssignment(pageRequest);
        }

        return new UserWithAssignmentPageResponse(new DefaultPageResponse<>());
    }

    @Override
    public List<UserGroupDesktopConfigDTO> getUserGroupDesktopConfigListByDeskType(UserCloudDeskTypeEnum deskType) {
        Assert.notNull(deskType, "deskType cannot be null;");
        return userDesktopConfigService.getUserGroupDesktopConfigListByDeskType(deskType);
    }

    @Override
    public List<UserGroupDesktopConfigDTO> getUserGroupDesktopConfigListByStrategyId(UUID strategyId) {
        Assert.notNull(strategyId, "strategyId cannot be null;");
        return userDesktopConfigService.getUserGroupDesktopConfigListByStrategyId(strategyId);
    }

    @Override
    public PageQueryResponse<UserGroupDesktopConfigDTO> pageQueryUserGroupDesktopConfigDTO(PageQueryRequest request) throws BusinessException {
        Assert.notNull(request, "request can not be null");
        return userDesktopConfigService.pageQueryUserGroupDesktopConfigDTO(request);
    }

    private UserWithAssignmentPageResponse pageQueryDiskAssignment(UserAssignmentPageRequest pageRequest) throws BusinessException {
        DefaultPageResponse<UserListDTO> pageResponse = diskPoolUserAPI.pageUserForDistribution(pageRequest);
        UserWithAssignmentPageResponse resultResponse = new UserWithAssignmentPageResponse(pageResponse);
        // 用户总数要减去被disabled的数量
        if (resultResponse.getTotalUserNum() > 0) {
            long disabledUserNum = diskPoolUserAPI.getDisabledUserNum(pageRequest.getDiskPoolId(), pageRequest.getGroupId());
            long finalTotalUser = resultResponse.getTotalUserNum() - disabledUserNum;
            resultResponse.setTotalUserNum(Math.max(finalTotalUser, 0));
        }
        if (Objects.nonNull(pageRequest.getGroupId())) {
            resultResponse.setAssignedUserNum(diskPoolUserAPI.countAssignedUserNumByGroup(pageRequest.getGroupId(),
                    pageRequest.getDiskPoolId()));
        }
        return resultResponse;
    }

    private UserWithAssignmentPageResponse pageQueryDesktopPoolAssignment(UserAssignmentPageRequest pageRequest) throws BusinessException {
        DefaultPageResponse<UserListDTO> pageResponse = desktopPoolUserMgmtAPI.pageUserWithAssignment(pageRequest);
        UserWithAssignmentPageResponse resultResponse = new UserWithAssignmentPageResponse(pageResponse);
        if (Objects.nonNull(pageRequest.getGroupId())) {
            resultResponse.setAssignedUserNum(desktopPoolUserMgmtAPI.countAssignedUserNumByGroup(pageRequest.getGroupId(),
                    pageRequest.getDesktopPoolId()));
        }

        return resultResponse;
    }

    private UserWithAssignmentPageResponse pageQueryDesktopTempPermissionAssignment(UserAssignmentPageRequest request) throws BusinessException {
        DefaultPageResponse<UserListDTO> pageResponse = this.pageQuery(request);
        if (ArrayUtils.isEmpty(pageResponse.getItemArr())) {
            return new UserWithAssignmentPageResponse(pageResponse);
        }

        // 添加disable标识
        List<UUID> userIdList = Arrays.stream(pageResponse.getItemArr()).map(UserListDTO::getId).distinct().collect(Collectors.toList());
        List<DesktopTempPermissionRelationDTO> relationList = desktopTempPermissionService.listRelationByRelatedIdsAndRelatedType(
                userIdList, DesktopTempPermissionRelatedType.USER);
        Map<UUID, UUID> userPermissionIdMap = new HashMap<>();
        for (DesktopTempPermissionRelationDTO dto : relationList) {
            userPermissionIdMap.put(dto.getRelatedId(), dto.getDesktopTempPermissionId());
        }
        UUID permissionId;
        for (UserListDTO userListDTO : pageResponse.getItemArr()) {
            permissionId = userPermissionIdMap.get(userListDTO.getId());
            userListDTO.setDisabled(Objects.nonNull(permissionId) && !Objects.equals(permissionId, request.getDesktopTempPermissionId()));
        }
        return new UserWithAssignmentPageResponse(pageResponse);
    }

    private UserWithAssignmentPageResponse pageQueryAppGroupAssignment(UserAssignmentPageRequest pageRequest) throws BusinessException {
        DefaultPageResponse<UserListDTO> pageResponse = rcoGroupMemberAPI.pageUserWithAssignment(pageRequest);
        UserWithAssignmentPageResponse resultResponse = new UserWithAssignmentPageResponse(pageResponse);
        if (Objects.nonNull(pageRequest.getGroupId())) {
            resultResponse.setAssignedUserNum(rcoGroupMemberAPI.countAssignedUserNumByGroup(pageRequest.getGroupId(),
                    pageRequest.getAppGroupId()));
        }

        return resultResponse;
    }
}
