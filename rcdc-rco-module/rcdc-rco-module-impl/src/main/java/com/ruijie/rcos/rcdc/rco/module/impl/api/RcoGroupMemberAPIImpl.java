package com.ruijie.rcos.rcdc.rco.module.impl.api;

import com.google.common.collect.Lists;
import com.ruijie.rcos.gss.sdk.iac.module.def.dto.user.IacUserDetailDTO;
import com.ruijie.rcos.gss.sdk.iac.module.def.enums.IacUserTypeEnum;
import com.ruijie.rcos.gss.sdk.iac.module.def.api.IacUserMgmtAPI;
import com.ruijie.rcos.rcdc.rca.module.def.api.RcaAppGroupAPI;
import com.ruijie.rcos.rcdc.rca.module.def.api.RcaAppPoolAPI;
import com.ruijie.rcos.rcdc.rca.module.def.api.RcaGroupMemberAPI;
import com.ruijie.rcos.rcdc.rca.module.def.api.RcaHostSessionAPI;
import com.ruijie.rcos.rcdc.rca.module.def.api.dto.RcaAppPoolBaseDTO;
import com.ruijie.rcos.rcdc.rca.module.def.api.dto.RcaGroupDTO;
import com.ruijie.rcos.rcdc.rca.module.def.api.dto.RcaHostSessionDTO;
import com.ruijie.rcos.rcdc.rca.module.def.enums.RcaEnum;
import com.ruijie.rcos.rcdc.rco.module.def.api.RcoGroupMemberAPI;
import com.ruijie.rcos.rcdc.rco.module.def.api.dto.UserListDTO;
import com.ruijie.rcos.rcdc.rco.module.def.api.dto.UserRcaGroupDTO;
import com.ruijie.rcos.rcdc.rco.module.def.api.request.PageSearchRequest;
import com.ruijie.rcos.rcdc.rco.module.def.api.request.UserAssignmentPageRequest;
import com.ruijie.rcos.rcdc.rco.module.impl.entity.RcoViewUserEntity;
import com.ruijie.rcos.rcdc.rco.module.impl.rca.service.UserRcaGroupService;
import com.ruijie.rcos.rcdc.rco.module.impl.service.QueryUserListService;
import com.ruijie.rcos.rcdc.rco.module.impl.service.UserQueryHelper;
import com.ruijie.rcos.sk.base.exception.BusinessException;
import com.ruijie.rcos.sk.base.log.Logger;
import com.ruijie.rcos.sk.base.log.LoggerFactory;
import com.ruijie.rcos.sk.modulekit.api.comm.DefaultPageResponse;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.util.Assert;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

/**
 * Description: 应用分组分配关系API, 与用户信息强相关，单独放置在RCO层，其余功能在RCA-API中处理
 * Copyright: Copyright (c) 2024
 * Company: Ruijie Co., Ltd.
 * Create Time: 2024年01月12日
 *
 * @author zhengjingyong
 */
public class RcoGroupMemberAPIImpl implements RcoGroupMemberAPI {

    private static final Logger LOGGER = LoggerFactory.getLogger(RcoGroupMemberAPIImpl.class);

    @Autowired
    private QueryUserListService queryUserListService;

    @Autowired
    private UserQueryHelper userQueryHelper;

    @Autowired
    private RcaHostSessionAPI rcaHostSessionAPI;

    @Autowired
    private RcaAppGroupAPI rcaAppGroupAPI;

    @Autowired
    private RcaAppPoolAPI rcaAppPoolAPI;

    @Autowired
    private RcaGroupMemberAPI rcaGroupMemberAPI;

    @Autowired
    private UserRcaGroupService userRcaGroupService;

    @Autowired
    private IacUserMgmtAPI cbbUserAPI;


    @Override
    public DefaultPageResponse<UserListDTO> pageQueryRealBindUser(UUID appGroupId, PageSearchRequest request) throws BusinessException {
        Assert.notNull(appGroupId, "appGroupId can not be null");
        Assert.notNull(request, "request can not be null");

        // 添加默认的用户类型条件
        Page<RcoViewUserEntity> userPage = queryUserListService.pageUserInOrNotInAppGroup(appGroupId, request, true);
        DefaultPageResponse<UserListDTO> response = new DefaultPageResponse<>();
        response.setTotal(userPage.getTotalElements());

        if (CollectionUtils.isEmpty(userPage.getContent())) {
            response.setItemArr(new UserListDTO[0]);
            return response;
        }
        UserListDTO[] dtoArr = userQueryHelper.convertRcoViewUserEntity2UserListDTO(userPage.getContent());

        // 如果为静态池，需要判断用户是否绑定了静态主机
        RcaGroupDTO groupDTO = rcaAppGroupAPI.getGroupDTO(appGroupId);
        RcaAppPoolBaseDTO appPoolBaseDTO = rcaAppPoolAPI.getAppPoolById(groupDTO.getPoolId());
        if (RcaEnum.PoolType.STATIC == appPoolBaseDTO.getPoolType()) {
            List<RcaHostSessionDTO> hostSessionDTOList = rcaHostSessionAPI.listByPoolId(appPoolBaseDTO.getId());
            if (!CollectionUtils.isEmpty(hostSessionDTOList)) {
                Set<UUID> userIdSet = hostSessionDTOList.stream().map(RcaHostSessionDTO::getUserId).collect(Collectors.toSet());
                for (UserListDTO user : dtoArr) {
                    user.setHasBindAppHost(userIdSet.contains(user.getId()));
                }
            }
        }

        response.setItemArr(dtoArr);
        return response;
    }

    @Override
    public DefaultPageResponse<UserListDTO> pageUserWithAssignment(UserAssignmentPageRequest request) throws BusinessException {
        Assert.notNull(request, "request can not be null");
        Assert.notNull(request.getAppGroupId(), "appGroupId is null");
        DefaultPageResponse<UserListDTO> response = new DefaultPageResponse<>();

        // 因为交互要求已分配的优先排序在前面，所以需要后端分别查询已分配和未分配的分页数据进行合并再返回给前端
        UUID appGroupId = request.getAppGroupId();
        int startIndex = request.getLimit() * request.getPage() + 1;
        int endIndex = request.getLimit() * (1 + request.getPage());

        Page<RcoViewUserEntity> allPage = queryUserListService.pageQuery(request, RcoViewUserEntity.class);
        int total = (int) allPage.getTotalElements();

        response.setTotal(total);
        // 起始下标超过了总量直接返回空
        if (startIndex > total) {
            // 返回空的
            response.setTotal(total);
            response.setItemArr(new UserListDTO[0]);
            return response;
        }
        // 先查询已分配的用户列表
        Page<RcoViewUserEntity> pageUserIn = queryUserListService.pageUserInOrNotInAppGroup(appGroupId, request, true);

        // 获取应用分组中已绑定桌面的用户列表
        Set<UUID> userIdSet = new HashSet<>();
        RcaGroupDTO groupDTO = rcaAppGroupAPI.getGroupDTO(appGroupId);
        RcaAppPoolBaseDTO appPoolBaseDTO = rcaAppPoolAPI.getAppPoolById(groupDTO.getPoolId());
        if (RcaEnum.PoolType.STATIC == appPoolBaseDTO.getPoolType()) {
            List<RcaHostSessionDTO> hostSessionDTOList = rcaHostSessionAPI.listByPoolId(appGroupId);
            if (!CollectionUtils.isEmpty(hostSessionDTOList)) {
                userIdSet = hostSessionDTOList.stream().map(RcaHostSessionDTO::getUserId).collect(Collectors.toSet());
            }
        }

        int pageUserInTotal = (int) pageUserIn.getTotalElements();
        // 已分配用户的满足分页数量，不需要再查后续的数据了，直接返回结果
        if (pageUserInTotal >= endIndex) {
            response.setItemArr(convert2UserListDTOArr(pageUserIn.getContent(), userIdSet, true));
            return response;
        }

        // 以下为已分配数量不满足此次分页查询，需要再查询未分配的用户进行组装
        int notInLastPage = request.getPage() - pageUserInTotal / request.getLimit();
        request.setPage(notInLastPage);

        // 查询未分配的用户列表
        Page<RcoViewUserEntity> lastPageUserNotIn = queryUserListService.pageUserInOrNotInAppGroup(appGroupId, request, false);
        // 未有用户被分配，或者已分配的刚好是整数页
        if (pageUserInTotal == 0 || pageUserInTotal % request.getLimit() == 0) {
            // pageUserNotIn入参page和limit需要重新根据pageUserIn的数量计算
            response.setItemArr(convert2UserListDTOArr(lastPageUserNotIn.getContent(), userIdSet, false));
            return response;
        }

        // 已分配的取一部分，未分配的取一部分
        if (startIndex <= pageUserInTotal) {
            response.setItemArr(getFromAssignedAndUnAssigned(request, pageUserIn, lastPageUserNotIn, userIdSet));
            return response;
        }

        // 只取非分配的组成分页数据
        response.setItemArr(getFromUnAssigned(request, lastPageUserNotIn, pageUserInTotal, userIdSet));
        return response;
    }

    @Override
    public Integer countAssignedUserNumByGroup(UUID groupId, UUID appGroupId) {
        Assert.notNull(groupId, "groupId can not be null");
        Assert.notNull(appGroupId, "appGroupId can not be null");
        return rcaGroupMemberAPI.countAssignedUserNumByUserGroupIdAndAppGroupId(groupId, appGroupId);
    }

    @Override
    public DefaultPageResponse<UserRcaGroupDTO> pageQueryByAdGroup(UUID adGroupId, PageSearchRequest request) throws BusinessException {
        Assert.notNull(request, "request must not be null");
        Assert.notNull(adGroupId, "adGroupId must not be null");

        return userRcaGroupService.pageQueryAdGroupRcaGroup(adGroupId, request);
    }

    @Override
    public DefaultPageResponse<UserRcaGroupDTO> pageQueryByUser(UUID userId, PageSearchRequest request) throws BusinessException {
        Assert.notNull(request, "request must not be null");
        Assert.notNull(userId, "userId must not be null");

        IacUserDetailDTO cbbUserDetailDTO = cbbUserAPI.getUserDetail(userId);
        // 访客返回空
        if (cbbUserDetailDTO.getUserType() == IacUserTypeEnum.VISITOR) {
            DefaultPageResponse<UserRcaGroupDTO> resp = new DefaultPageResponse<>();
            resp.setTotal(0);
            resp.setItemArr(new UserRcaGroupDTO[0]);
            return resp;
        }

        return userRcaGroupService.pageQueryUserRcaGroup(cbbUserDetailDTO, request);
    }

    private UserListDTO[] getFromAssignedAndUnAssigned(UserAssignmentPageRequest request, Page<RcoViewUserEntity> pageUserIn,
                                                       Page<RcoViewUserEntity> lastPageUserNotIn, Set<UUID> bindHostUserIdSet) {
        UserListDTO[] assignedUserArr = convert2UserListDTOArr(pageUserIn.getContent(), bindHostUserIdSet, true);
        // notIn部分不需要请求第二次
        int diffNum = request.getLimit() - pageUserIn.getContent().size();
        // 还差diffNum个用户凑成limit个
        if (lastPageUserNotIn.getContent().size() <= diffNum) {
            // lastPageUserNotIn + pageUserIn的数据不足一页，后面已经没数据了
            UserListDTO[] unAssignedUserArr = convert2UserListDTOArr(lastPageUserNotIn.getContent(), bindHostUserIdSet, false);
            return UserQueryHelper.mergeUserListDTOArr(assignedUserArr, unAssignedUserArr);
        }

        // 把lastPageUserNotIn里的部分记录和pageUserIn里的记录合成一页
        List<RcoViewUserEntity> subNotInUserList = lastPageUserNotIn.getContent().subList(0, diffNum);
        UserListDTO[] unAssignedUserArr = convert2UserListDTOArr(subNotInUserList, bindHostUserIdSet, false);
        return UserQueryHelper.mergeUserListDTOArr(assignedUserArr, unAssignedUserArr);
    }

    private UserListDTO[] getFromUnAssigned(UserAssignmentPageRequest request, Page<RcoViewUserEntity> lastPageUserNotIn, int pageUserInTotal,
                                            Set<UUID> bindHostUserIdSet) {
        request.setPage(request.getPage() - 1);
        int diffNumByLimit = request.getLimit() - pageUserInTotal % request.getLimit();

        Page<RcoViewUserEntity> prePageUserNotIn = queryUserListService.pageUserInOrNotInAppGroup(request.getAppGroupId(), request, false);
        List<RcoViewUserEntity> preUserList = prePageUserNotIn.getContent();

        // lastPageUserNotIn为空，prePageUserNotIn的数据已经是最后的记录了
        if (CollectionUtils.isEmpty(lastPageUserNotIn.getContent())) {
            // 取prePageUserNotIn中的部分
            List<RcoViewUserEntity> finalUserList = preUserList.subList(diffNumByLimit, preUserList.size());
            return convert2UserListDTOArr(finalUserList, bindHostUserIdSet, false);
        }

        // 取prePageUserNotIn中的部分和lastPageUserNotIn中的部分
        List<RcoViewUserEntity> finalUserList = Lists.newArrayList(preUserList.subList(diffNumByLimit, preUserList.size()));
        int lastPageEndIndex = Math.min(diffNumByLimit, lastPageUserNotIn.getContent().size());
        finalUserList.addAll(lastPageUserNotIn.getContent().subList(0, lastPageEndIndex));
        return convert2UserListDTOArr(finalUserList, bindHostUserIdSet, false);
    }

    private UserListDTO[] convert2UserListDTOArr(List<RcoViewUserEntity> userEntityList, Set<UUID> userIdSet, boolean isInGroup) {
        UserListDTO[] userListDTOArr = userQueryHelper.convertRcoViewUserEntity2UserListDTO(userEntityList);
        for (UserListDTO user : userListDTOArr) {
            user.setIsAssigned(isInGroup);
            user.setHasBindAppHost(userIdSet.contains(user.getId()));
        }
        return userListDTOArr;
    }

}
