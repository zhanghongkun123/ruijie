package com.ruijie.rcos.rcdc.rco.module.impl.api;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.google.common.collect.Lists;
import com.ruijie.rcos.gss.sdk.iac.module.def.api.IacUserMgmtAPI;
import com.ruijie.rcos.gss.sdk.iac.module.def.api.response.IacPageResponse;
import com.ruijie.rcos.gss.sdk.iac.module.def.dto.user.IacUserDetailDTO;
import com.ruijie.rcos.rcdc.rca.module.def.api.RcaAppGroupAPI;
import com.ruijie.rcos.rcdc.rca.module.def.api.RcaAppPoolAPI;
import com.ruijie.rcos.rcdc.rca.module.def.api.RcaGroupMemberAPI;
import com.ruijie.rcos.rcdc.rca.module.def.api.RcaMainStrategyAPI;
import com.ruijie.rcos.rcdc.rca.module.def.api.RcaPeripheralStrategyAPI;
import com.ruijie.rcos.rcdc.rca.module.def.api.dto.RcaAppPoolBaseDTO;
import com.ruijie.rcos.rcdc.rca.module.def.api.dto.RcaAppStrategyBindUserDTO;
import com.ruijie.rcos.rcdc.rca.module.def.api.dto.RcaGroupDTO;
import com.ruijie.rcos.rcdc.rca.module.def.api.dto.RcaGroupMemberDTO;
import com.ruijie.rcos.rcdc.rca.module.def.enums.RcaEnum;
import com.ruijie.rcos.rcdc.rco.module.common.query.AdditionalSpecification;
import com.ruijie.rcos.rcdc.rco.module.def.adgroup.dto.AdGroupListDTO;
import com.ruijie.rcos.rcdc.rco.module.def.api.AdGroupPoolMgmtAPI;
import com.ruijie.rcos.rcdc.rco.module.def.api.dto.UserListDTO;
import com.ruijie.rcos.rcdc.rco.module.def.api.request.PageSearchRequest;
import com.ruijie.rcos.rcdc.rco.module.def.api.RcaStrategyBindRelationAPI;
import com.ruijie.rcos.rcdc.rco.module.def.api.UserRecycleBinMgmtAPI;
import com.ruijie.rcos.rcdc.rco.module.def.api.dto.CloudDesktopDTO;
import com.ruijie.rcos.rcdc.rco.module.def.certificationstrategy.dto.PwdStrategyDTO;
import com.ruijie.rcos.rcdc.rco.module.def.constants.Constants;
import com.ruijie.rcos.rcdc.rco.module.impl.certificationstrategy.service.CertificationStrategyService;
import com.ruijie.rcos.rcdc.rco.module.impl.dto.UserDesktopCountInfo;
import com.ruijie.rcos.rcdc.rco.module.impl.entity.RcoViewUserEntity;
import com.ruijie.rcos.rcdc.rco.module.impl.entity.ViewRcaStrategyUserEntity;
import com.ruijie.rcos.rcdc.rco.module.impl.rca.service.QueryRcaStrategyBindService;
import com.ruijie.rcos.rcdc.rco.module.impl.service.QueryUserListService;
import com.ruijie.rcos.rcdc.rco.module.impl.service.UserQueryHelper;
import com.ruijie.rcos.rcdc.rco.module.impl.service.UserService;
import com.ruijie.rcos.rcdc.rco.module.impl.service.impl.CloudDesktopViewServiceImpl;
import com.ruijie.rcos.sk.base.exception.BusinessException;
import com.ruijie.rcos.sk.base.log.Logger;
import com.ruijie.rcos.sk.base.log.LoggerFactory;
import com.ruijie.rcos.sk.modulekit.api.comm.DefaultPageResponse;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.util.Assert;

import java.util.*;
import java.util.stream.Collectors;

/**
 * Description: 云应用策略绑定用户API实现
 * Copyright: Copyright (c) 2022
 * Company: Ruijie Co., Ltd.
 * Create Time: 2022/09/28 16:05
 *
 * @author fanbaorong
 */
public class RcaStrategyBindRelationAPIImpl implements RcaStrategyBindRelationAPI {

    private static final Logger LOGGER = LoggerFactory.getLogger(RcaStrategyBindRelationAPIImpl.class);

    @Autowired
    private CertificationStrategyService certificationStrategyService;

    @Autowired
    private UserRecycleBinMgmtAPI userRecycleBinMgmtAPI;

    @Autowired
    private QueryRcaStrategyBindService queryRcaStrategyBindService;

    @Autowired
    private CloudDesktopViewServiceImpl cloudDesktopViewService;

    @Autowired
    private RcaMainStrategyAPI rcaMainStrategyAPI;

    @Autowired
    private RcaPeripheralStrategyAPI rcaPeripheralStrategyAPI;

    @Autowired
    private QueryUserListService queryUserListService;

    @Autowired
    private AdGroupPoolMgmtAPI adGroupPoolMgmtAPI;

    @Autowired
    private RcaAppPoolAPI rcaAppPoolAPI;

    @Autowired
    private UserQueryHelper userQueryHelper;

    @Autowired
    private RcaGroupMemberAPI rcaGroupMemberAPI;

    @Autowired
    private RcaAppGroupAPI rcaAppGroupAPI;

    @Autowired
    private IacUserMgmtAPI userMgmtAPI;

    @Autowired
    private UserService userService;

    /**
     * 用户永久锁定时的默认值
     */
    private static final int PERMANENT_LOCK = -1;

    private static final int SIZE_ZERO = 0;

    /**
     * 分钟转换成毫秒
     */
    private static final int ONE_MINUTE_MILLIS = 60 * 1000;

    @Override
    public DefaultPageResponse<RcaAppStrategyBindUserDTO> pageQueryRcaStrategyBindUser(UUID rcaStrategyId, PageSearchRequest request) {
        Assert.notNull(request, "pageQuery方法的request can not be null");
        Assert.notNull(rcaStrategyId, "rcaStrategyId can not be null");

        Page<ViewRcaStrategyUserEntity> page = queryRcaStrategyBindService.pageQuery(rcaStrategyId, request);
        DefaultPageResponse<RcaAppStrategyBindUserDTO> response = new DefaultPageResponse<>();
        if (page.getTotalElements() == SIZE_ZERO) {
            response.setTotal(SIZE_ZERO);
            response.setItemArr(new RcaAppStrategyBindUserDTO[0]);
            return response;
        }
        List<RcaAppStrategyBindUserDTO> dtoList = generateAppStrategyBindUserDTO(page);
        response.setItemArr(dtoList.toArray(new RcaAppStrategyBindUserDTO[0]));
        response.setTotal(page.getTotalElements());
        return response;
    }

    private List<RcaAppStrategyBindUserDTO> generateAppStrategyBindUserDTO(Page<ViewRcaStrategyUserEntity> page) {
        List<ViewRcaStrategyUserEntity> entityList = page.getContent();
        List<RcaAppStrategyBindUserDTO> bindUserDTOList = Lists.newArrayList();
        for (ViewRcaStrategyUserEntity entity : entityList) {
            RcaAppStrategyBindUserDTO bindUserDTO = buildBindUserDTO(entity);
            bindUserDTO.setStrategyId(entity.getRcaStrategyId());
            bindUserDTOList.add(bindUserDTO);
        }
        return bindUserDTOList;
    }

    private RcaAppStrategyBindUserDTO buildBindUserDTO(ViewRcaStrategyUserEntity entity) {
        RcaAppStrategyBindUserDTO bindUserDTO = new RcaAppStrategyBindUserDTO();

        // 计算获取解锁时间，公式当前时间减去配置锁定时间
        PwdStrategyDTO pwdStrategyDTO = certificationStrategyService.getPwdStrategyParameter();
        Date realUnLockTime = new Date(System.currentTimeMillis() - (long) ONE_MINUTE_MILLIS * pwdStrategyDTO.getUserLockTime());

        List<UUID> userIdList = Lists.newArrayList(entity.getId());
        UserDesktopCountInfo userDesktopCountInfo = getUserDesktopCountInfo(userIdList);
        List<CloudDesktopDTO> recycleUserList = userRecycleBinMgmtAPI.getAllDesktopByUserIdList(userIdList);
        Map<UUID, List<CloudDesktopDTO>> userIdToDesktopMap = recycleUserList.stream().collect(Collectors.groupingBy(CloudDesktopDTO::getUserId));

        List<CloudDesktopDTO> emptyList = Lists.newLinkedList();
        BeanUtils.copyProperties(entity, bindUserDTO);
        bindUserDTO.setUserState(entity.getState());
        boolean canDelete = userDesktopCountInfo.isUserCanDelete(entity.getId());
        bindUserDTO.setCanDelete(canDelete);
        boolean hasRecycleBin = userIdToDesktopMap.getOrDefault(bindUserDTO.getId(), emptyList).size() > SIZE_ZERO;
        bindUserDTO.setHasRecycleBin(hasRecycleBin);
        // 暂时未移植独享应用主机相关桌面信息，后续需要再移植
        boolean isLock = checkUserIsLock(pwdStrategyDTO, bindUserDTO, realUnLockTime);
        bindUserDTO.setLock(isLock);
        return bindUserDTO;
    }

    private UserDesktopCountInfo getUserDesktopCountInfo(List<UUID> userIdList) {
        return cloudDesktopViewService.countUserDesktopInfo(userIdList);
    }

    private boolean checkUserIsLock(PwdStrategyDTO pwdStrategyDTO, RcaAppStrategyBindUserDTO dto, Date realUnLockTime) {
        // 如果没有开启防爆功能，则用户都处于未锁定状态
        if (!pwdStrategyDTO.getPreventsBruteForce()) {
            return false;
        }

        boolean isLock = checkToGetDefaultValue(dto.getLock(), false);
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


    /**
     * 参考optional的ofNullable方法重写校验方法
     *
     * @param value
     * @param defaultValue
     * @param <T>
     * @return
     */
    private <T> T checkToGetDefaultValue(T value, T defaultValue) {
        return value == null ? defaultValue : value;
    }


    @Override
    public DefaultPageResponse<UserListDTO> pageQueryRcaMainStrategyBindUser(UUID rcaStrategyId, PageSearchRequest request) {
        Assert.notNull(rcaStrategyId, "rcaStrategyId can not be null");
        Assert.notNull(request, "request can not be null");

        List<UUID> userList = getBindUserListByStrategyId(rcaStrategyId, RcaEnum.StrategyType.MAIN_STRATEGY);
        //通过策略表查询用户
        AdditionalSpecification<RcoViewUserEntity> additionalSpecification = new AdditionalSpecification(
                ((Specification<RcoViewUserEntity>) (root, query, criteriaBuilder) ->
                        query.where(criteriaBuilder.in(root.get("id")).value(userList)).getRestriction()
                ), AdditionalSpecification.AdditionalPredicate.AND);
        Page<RcoViewUserEntity> userEntityPage = queryUserListService.pageQuery(request, RcoViewUserEntity.class, additionalSpecification);
        DefaultPageResponse<UserListDTO> userPage = new DefaultPageResponse<>();
        UserListDTO[] userListDTOArr = userQueryHelper.convertRcoViewUserEntity2UserListDTO(userEntityPage.getContent());
        userPage.setItemArr(userListDTOArr);
        userPage.setTotal(userEntityPage.getTotalElements());
        return userPage;
    }

    @Override
    public DefaultPageResponse<RcaAppPoolBaseDTO> pageQueryRcaMainStrategyBindPool(UUID rcaStrategyId, PageSearchRequest request) {
        Assert.notNull(rcaStrategyId, "rcaStrategyId can not be null");
        Assert.notNull(request, "request can not be null");
        List<UUID> poolIdList = rcaMainStrategyAPI.getPoolIdListByStrategyId(rcaStrategyId);
        com.ruijie.rcos.rcdc.rca.module.def.request.PageSearchRequest pageSearchRequest = getFromPageSearchRequest(request);
        BeanUtils.copyProperties(request, pageSearchRequest);
        return rcaAppPoolAPI.pageQuery(pageSearchRequest, poolIdList);
    }

    @Override
    public DefaultPageResponse<AdGroupListDTO> pageQueryRcaMainStrategyBindSafetyGroup(UUID rcaStrategyId, PageSearchRequest request) {
        Assert.notNull(rcaStrategyId, "rcaStrategyId can not be null");
        Assert.notNull(request, "request can not be null");
        List<UUID> adSafetyGroupIdList = getSaftyGroupListByStrategy(rcaStrategyId, RcaEnum.StrategyType.MAIN_STRATEGY);
        return adGroupPoolMgmtAPI.pageQueryAdGroupInAdGroupIdList(request, adSafetyGroupIdList);
    }

    @Override
    public DefaultPageResponse<UserListDTO> pageQueryRcaPeripheralStrategyBindUser(UUID rcaStrategyId, PageSearchRequest request) {
        Assert.notNull(rcaStrategyId, "rcaStrategyId can not be null");
        Assert.notNull(request, "request can not be null");
        List<UUID> userList =  getBindUserListByStrategyId(rcaStrategyId, RcaEnum.StrategyType.PERIPHERAL_STRATEGY);
        //通过策略表查询用户
        AdditionalSpecification<RcoViewUserEntity> additionalSpecification = new AdditionalSpecification(
                ((Specification<RcoViewUserEntity>) (root, query, criteriaBuilder) ->
                        query.where(criteriaBuilder.in(root.get("id")).value(userList)).getRestriction()
                ), AdditionalSpecification.AdditionalPredicate.AND);
        Page<RcoViewUserEntity> userEntityPage = queryUserListService.pageQuery(request, RcoViewUserEntity.class, additionalSpecification);
        DefaultPageResponse<UserListDTO> userPage = new DefaultPageResponse<>();
        UserListDTO[] userListDTOArr = userQueryHelper.convertRcoViewUserEntity2UserListDTO(userEntityPage.getContent());
        userPage.setItemArr(userListDTOArr);
        userPage.setTotal(userEntityPage.getTotalElements());
        return userPage;
    }

    @Override
    public DefaultPageResponse<RcaAppPoolBaseDTO> pageQueryRcaPeripheralStrategyBindPool(UUID rcaStrategyId, PageSearchRequest request) {
        Assert.notNull(rcaStrategyId, "rcaStrategyId can not be null");
        Assert.notNull(request, "request can not be null");
        List<UUID> poolIdList = rcaPeripheralStrategyAPI.getPoolIdListByStrategyId(rcaStrategyId);
        com.ruijie.rcos.rcdc.rca.module.def.request.PageSearchRequest pageSearchRequest = getFromPageSearchRequest(request);
        return rcaAppPoolAPI.pageQuery(pageSearchRequest, poolIdList);
    }

    @Override
    public DefaultPageResponse<AdGroupListDTO> pageQueryRcaPeripheralStrategyBindSafetyGroup(UUID rcaStrategyId, PageSearchRequest request) {
        Assert.notNull(rcaStrategyId, "rcaStrategyId can not be null");
        Assert.notNull(request, "request can not be null");
        List<UUID> adSafetyGroupIdList = getSaftyGroupListByStrategy(rcaStrategyId, RcaEnum.StrategyType.PERIPHERAL_STRATEGY);
        return adGroupPoolMgmtAPI.pageQueryAdGroupInAdGroupIdList(request, adSafetyGroupIdList);
    }

    private com.ruijie.rcos.rcdc.rca.module.def.request.PageSearchRequest getFromPageSearchRequest(PageSearchRequest request) {
        String jsonString = JSON.toJSONString(request);
        return JSONObject.parseObject(jsonString, com.ruijie.rcos.rcdc.rca.module.def.request.PageSearchRequest.class);
    }

    private List<UUID> getBindUserListByStrategyId(UUID rcaStrategyId, RcaEnum.StrategyType strategyType) {
        List<UUID> poolIdList;
        // 获取应用池id列表
        if (strategyType == RcaEnum.StrategyType.MAIN_STRATEGY) {
            poolIdList = rcaMainStrategyAPI.getPoolIdListByStrategyId(rcaStrategyId);
        } else {
            poolIdList = rcaPeripheralStrategyAPI.getPoolIdListByStrategyId(rcaStrategyId);
        }

        List<UUID> groupIdList =
                rcaAppGroupAPI.findAllGroupByPoolIdIn(poolIdList).stream().map(RcaGroupDTO::getId).collect(Collectors.toList());

        List<UUID> memberIdList = new ArrayList<>();
        List<UUID> userList = new ArrayList<>();
        try {
            for (UUID groupId : groupIdList) {
                List<RcaGroupMemberDTO> groupList = rcaGroupMemberAPI.listGroupMember(groupId, RcaEnum.GroupMemberType.USER_GROUP);
                memberIdList.addAll(groupList.stream().map(RcaGroupMemberDTO::getMemberId).collect(Collectors.toList()));

                List<RcaGroupMemberDTO> groupMemgerList = rcaGroupMemberAPI.listGroupMember(groupId, RcaEnum.GroupMemberType.USER);
                userList.addAll(groupMemgerList.stream().map(RcaGroupMemberDTO::getMemberId).collect(Collectors.toList()));
            }

            // 获取成员
            if (!memberIdList.isEmpty()) {
                List<UUID> groupUserList = getUserIdByGroupIds(memberIdList);
                userList.addAll(groupUserList);
            }
        } catch (BusinessException e) {
            LOGGER.error("获取策略[{}]绑定的用户失败", rcaStrategyId, e);
        }

        return userList;
    }

    private List<UUID> getUserIdByGroupIds(List<UUID> memberIdList) throws BusinessException {
        // 组下用户可能会超过1000，按组分页查询
        List<UUID> userIdList = new ArrayList<>();
        for (UUID groupId : memberIdList) {
            IacPageResponse<IacUserDetailDTO> pageResult = userService.pageQueryByGroupId(groupId, 0);
            // 总页数
            int totalPage = (int) Math.ceil((double) pageResult.getTotal() / Constants.MAX_QUERY_LIST_SIZE);
            for (int page = 0; page < totalPage; page++) {
                // 前面已查过，不重复查
                if (page == 0) {
                    userIdList.addAll(Arrays.stream(pageResult.getItemArr()).map(IacUserDetailDTO::getId).collect(Collectors.toList()));
                    continue;
                }
                pageResult = userService.pageQueryByGroupId(groupId, page);
                if (pageResult.getTotal() == 0) {
                    break;
                }
                userIdList.addAll(Arrays.stream(pageResult.getItemArr()).map(IacUserDetailDTO::getId).collect(Collectors.toList()));
            }
        }
        return userIdList;
    }

    private List<UUID> getSaftyGroupListByStrategy(UUID rcaStrategyId, RcaEnum.StrategyType strategyType) {
        List<UUID> poolIdList;
        // 获取应用池id列表
        if (strategyType == RcaEnum.StrategyType.MAIN_STRATEGY) {
            poolIdList = rcaMainStrategyAPI.getPoolIdListByStrategyId(rcaStrategyId);
        } else {
            poolIdList = rcaPeripheralStrategyAPI.getPoolIdListByStrategyId(rcaStrategyId);
        }
        List<UUID> groupIdList =
                rcaAppGroupAPI.findAllGroupByPoolIdIn(poolIdList).stream().map(RcaGroupDTO::getId).collect(Collectors.toList());
        List<UUID> adSafetyGroupIdList = new ArrayList<>();
        try {
            for (UUID groupId : groupIdList) {
                List<RcaGroupMemberDTO> groupList = rcaGroupMemberAPI.listGroupMember(groupId, RcaEnum.GroupMemberType.AD_SAFETY_GROUP);
                List<UUID> safeGroupIdList = groupList.stream().map(RcaGroupMemberDTO::getMemberId).collect(Collectors.toList());
                adSafetyGroupIdList.addAll(safeGroupIdList);
            }
        } catch (BusinessException e) {
            LOGGER.error("获取策略[{}]绑定的安全组失败",rcaStrategyId, e);
        }
        return adSafetyGroupIdList;
    }
}
