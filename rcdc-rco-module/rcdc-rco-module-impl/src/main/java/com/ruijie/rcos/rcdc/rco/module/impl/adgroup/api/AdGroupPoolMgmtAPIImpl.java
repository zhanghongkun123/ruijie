package com.ruijie.rcos.rcdc.rco.module.impl.adgroup.api;

import java.util.*;
import java.util.stream.Collectors;

import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.util.Assert;

import com.google.common.collect.Lists;
import com.ruijie.rcos.rcdc.rco.module.def.adgroup.dto.AdGroupListDTO;
import com.ruijie.rcos.rcdc.rco.module.def.api.AdGroupPoolMgmtAPI;
import com.ruijie.rcos.rcdc.rco.module.def.api.request.PageSearchRequest;
import com.ruijie.rcos.rcdc.rco.module.impl.adgroup.entity.ViewAdGroupEntity;
import com.ruijie.rcos.rcdc.rco.module.impl.adgroup.service.impl.AdGroupPoolServiceImpl;
import com.ruijie.rcos.rcdc.rco.module.impl.diskpool.dao.DiskPoolUserDAO;
import com.ruijie.rcos.rcdc.rco.module.impl.diskpool.entity.DiskPoolUserEntity;
import com.ruijie.rcos.rcdc.rco.module.impl.dto.PageQueryAppGroupDTO;
import com.ruijie.rcos.rcdc.rco.module.impl.dto.PageQueryPoolDTO;
import com.ruijie.rcos.rcdc.rco.module.impl.service.UserQueryHelper;
import com.ruijie.rcos.sk.base.exception.BusinessException;
import com.ruijie.rcos.sk.modulekit.api.comm.DefaultPageResponse;

/**
 * Description: 安全组分配池相关API实现类
 * Copyright: Copyright (c) 2022
 * Company: Ruijie Co., Ltd.
 * Create Time: 2022/9/27
 *
 * @author TD
 */
public class AdGroupPoolMgmtAPIImpl implements AdGroupPoolMgmtAPI {

    private static final String APP_GROUP_ID = "appGroupId";

    @Autowired
    private DiskPoolUserDAO diskPoolUserDAO;

    @Autowired
    private AdGroupPoolServiceImpl adGroupPoolService;

    @Override
    public DefaultPageResponse<AdGroupListDTO> pageAdGroupWithAssignment(UUID poolId, String relatedPoolKey,
                                                                         PageSearchRequest request) throws BusinessException {
        Assert.notNull(request, "request can not be null");
        Assert.hasText(relatedPoolKey, "relatedPoolKey can not be null");
        Assert.notNull(poolId, "poolId can not be null");
        DefaultPageResponse<AdGroupListDTO> response = new DefaultPageResponse<>();

        int endIndex = request.getLimit() * (1 + request.getPage());
        int startIndex = request.getLimit() * request.getPage() + 1;

        Page<ViewAdGroupEntity> allPage = adGroupPoolService.pageQuery(request, ViewAdGroupEntity.class);
        int total = (int) allPage.getTotalElements();

        response.setTotal(total);
        // 起始下标超过了总量直接返回空
        if (startIndex > total) {
            // 返回空的
            response.setTotal(total);
            response.setItemArr(new AdGroupListDTO[0]);
            return response;
        }
        Page<ViewAdGroupEntity> pageUserIn;
        if (APP_GROUP_ID.equals(relatedPoolKey)) {
            pageUserIn = adGroupPoolService.pageAdGroupInOrNotInAppGroup(
                    new PageQueryAppGroupDTO(poolId, request, true));
        } else {
            pageUserIn = adGroupPoolService.pageAdGroupInOrNotInPool(
                    new PageQueryPoolDTO(poolId, relatedPoolKey, request, true));
        }
        int pageUserInTotal = (int) pageUserIn.getTotalElements();
        // 已分配用户的满足分页数量
        if (pageUserInTotal >= endIndex) {
            response.setItemArr(convert2AdGroupListDTOArr(pageUserIn.getContent(), true, poolId, relatedPoolKey));
            return response;
        }

        // 以下为已分配数量不满足此次分页查询
        int notInLastPage = request.getPage() - pageUserInTotal / request.getLimit();
        request.setPage(notInLastPage);

        Page<ViewAdGroupEntity> lastPageUserNotIn;
        if (APP_GROUP_ID.equals(relatedPoolKey)) {
            lastPageUserNotIn = adGroupPoolService.pageAdGroupInOrNotInAppGroup(
                    new PageQueryAppGroupDTO(poolId, request, false));
        } else {
            lastPageUserNotIn = adGroupPoolService.pageAdGroupInOrNotInPool(
                    new PageQueryPoolDTO(poolId, relatedPoolKey, request, false));
        }

        // 未有用户被分配，或者已分配的刚好是整数页
        if (pageUserInTotal == 0 || pageUserInTotal % request.getLimit() == 0) {
            // pageUserNotIn入参page和limit需要重新根据pageUserIn的数量计算
            response.setItemArr(convert2AdGroupListDTOArr(lastPageUserNotIn.getContent(), false, poolId, relatedPoolKey));
            return response;
        }

        // 已分配的取一部分，未分配的取一部分
        if (startIndex <= pageUserInTotal) {
            response.setItemArr(getFromAssignedAndUnAssigned(request, pageUserIn, lastPageUserNotIn, poolId, relatedPoolKey));
            return response;
        }

        // 只取非分配的组成分页数据
        response.setItemArr(getFromUnAssigned(request, lastPageUserNotIn, pageUserInTotal, poolId, relatedPoolKey));
        return response;
    }

    @Override
    public DefaultPageResponse<AdGroupListDTO> pageQueryPoolBindAdGroup(UUID poolId, String relatedPoolKey,
                                                                        PageSearchRequest request) throws BusinessException {
        Assert.notNull(request, "request can not be null");
        Assert.hasText(relatedPoolKey, "relatedPoolKey can not be null");
        Assert.notNull(poolId, "poolId can not be null");
        DefaultPageResponse<AdGroupListDTO> response = new DefaultPageResponse<>();
        Page<ViewAdGroupEntity> pageUserIn;
        if (APP_GROUP_ID.equals(relatedPoolKey)) {
            pageUserIn = adGroupPoolService.pageAdGroupInOrNotInAppGroup(new PageQueryAppGroupDTO(poolId, request, true));
        } else {
            pageUserIn = adGroupPoolService.pageAdGroupInOrNotInPool(new PageQueryPoolDTO(poolId, relatedPoolKey, request, true));
        }

        response.setTotal(pageUserIn.getTotalElements());
        if (CollectionUtils.isEmpty(pageUserIn.getContent())) {
            response.setItemArr(new AdGroupListDTO[0]);
        } else {
            response.setItemArr(convert2AdGroupListDTOArr(pageUserIn.getContent(), true, poolId, relatedPoolKey));
        }
        return response;
    }

    @Override
    public DefaultPageResponse<AdGroupListDTO> pageQueryAdGroupInObjectGuidList(PageSearchRequest request, List<String> objectGuidList) {
        Assert.notNull(request, "request can not be null");
        Assert.notNull(objectGuidList, "objectGuidList can not be null");
        Page<ViewAdGroupEntity> adGroupEntityPage = adGroupPoolService.pageQueryAdGroupInObjectGuidList(request, objectGuidList);
        DefaultPageResponse<AdGroupListDTO> adGroupListDTODefaultPageResponse = new DefaultPageResponse<>();
        adGroupListDTODefaultPageResponse.setTotal(adGroupEntityPage.getTotalElements());
        if (CollectionUtils.isEmpty(adGroupEntityPage.getContent())) {
            adGroupListDTODefaultPageResponse.setItemArr(new AdGroupListDTO[0]);
        } else {
            adGroupListDTODefaultPageResponse.setItemArr(adGroupEntityPage.getContent().stream().map(viewAdGroupEntity -> {
                AdGroupListDTO groupListDTO = new AdGroupListDTO();
                BeanUtils.copyProperties(viewAdGroupEntity, groupListDTO);
                return groupListDTO;
            }).toArray(AdGroupListDTO[]::new));
        }
        return adGroupListDTODefaultPageResponse;
    }

    @Override
    public DefaultPageResponse<AdGroupListDTO> pageQueryAdGroupInAdGroupIdList(PageSearchRequest request, List<UUID> adGroupIdList) {
        Assert.notNull(request, "request can not be null");
        Assert.notNull(adGroupIdList, "adGroupIdList can not be null");
        Page<ViewAdGroupEntity> adGroupEntityPage = adGroupPoolService.pageQueryAdGroupInAdGroupIdList(request, adGroupIdList);
        DefaultPageResponse<AdGroupListDTO> adGroupListDTODefaultPageResponse = new DefaultPageResponse<>();
        adGroupListDTODefaultPageResponse.setTotal(adGroupEntityPage.getTotalElements());
        if (CollectionUtils.isEmpty(adGroupEntityPage.getContent())) {
            adGroupListDTODefaultPageResponse.setItemArr(new AdGroupListDTO[0]);
        } else {
            adGroupListDTODefaultPageResponse.setItemArr(adGroupEntityPage.getContent().stream().map(viewAdGroupEntity -> {
                AdGroupListDTO groupListDTO = new AdGroupListDTO();
                BeanUtils.copyProperties(viewAdGroupEntity, groupListDTO);
                return groupListDTO;
            }).toArray(AdGroupListDTO[]::new));
        }
        return adGroupListDTODefaultPageResponse;
    }

    private AdGroupListDTO[] getFromAssignedAndUnAssigned(PageSearchRequest request, Page<ViewAdGroupEntity> pageUserIn,
                                                          Page<ViewAdGroupEntity> lastPageUserNotIn, UUID poolId, String relatedPoolKey) {
        AdGroupListDTO[] assignedUserArr = convert2AdGroupListDTOArr(pageUserIn.getContent(), true, poolId, relatedPoolKey);
        // notIn部分不需要请求第二次
        int diffNum = request.getLimit() - pageUserIn.getContent().size();
        // 还差diffNum个用户凑成limit个
        if (lastPageUserNotIn.getContent().size() <= diffNum) {
            // lastPageUserNotIn + pageUserIn的数据不足一页，后面已经没数据了
            AdGroupListDTO[] unAssignedUserArr = convert2AdGroupListDTOArr(lastPageUserNotIn.getContent(), false, poolId, relatedPoolKey);
            return mergeAdGroupListDTOArr(assignedUserArr, unAssignedUserArr);
        }

        // 把lastPageUserNotIn里的部分记录和pageUserIn里的记录合成一页
        List<ViewAdGroupEntity> subNotInUserList = lastPageUserNotIn.getContent().subList(0, diffNum);
        AdGroupListDTO[] unAssignedUserArr = convert2AdGroupListDTOArr(subNotInUserList, false, poolId, relatedPoolKey);
        return mergeAdGroupListDTOArr(assignedUserArr, unAssignedUserArr);
    }

    private AdGroupListDTO[] getFromUnAssigned(PageSearchRequest request, Page<ViewAdGroupEntity> lastPageUserNotIn,
                                               int pageUserInTotal, UUID poolId, String relatedPoolKey) {
        request.setPage(request.getPage() - 1);
        int diffNumByLimit = request.getLimit() - pageUserInTotal % request.getLimit();

        Page<ViewAdGroupEntity> prePageUserNotIn = adGroupPoolService.pageAdGroupInOrNotInPool(new PageQueryPoolDTO(poolId,
                relatedPoolKey, request, false));
        List<ViewAdGroupEntity> preUserList = prePageUserNotIn.getContent();

        // lastPageUserNotIn为空，prePageUserNotIn的数据已经是最后的记录了
        if (CollectionUtils.isEmpty(lastPageUserNotIn.getContent())) {
            // 取prePageUserNotIn中的部分
            List<ViewAdGroupEntity> finalUserList = preUserList.subList(diffNumByLimit, preUserList.size());
            return convert2AdGroupListDTOArr(finalUserList, false, poolId, relatedPoolKey);
        }

        // 取prePageUserNotIn中的部分和lastPageUserNotIn中的部分
        List<ViewAdGroupEntity> finalUserList = Lists.newArrayList(preUserList.subList(diffNumByLimit, preUserList.size()));
        int lastPageEndIndex = Math.min(diffNumByLimit, lastPageUserNotIn.getContent().size());
        finalUserList.addAll(lastPageUserNotIn.getContent().subList(0, lastPageEndIndex));
        return convert2AdGroupListDTOArr(finalUserList, false, poolId, relatedPoolKey);
    }

    private AdGroupListDTO[] convert2AdGroupListDTOArr(List<ViewAdGroupEntity> adGroupPoolEntityList,
                                                       boolean isInPool, UUID poolId, String relatedPoolKey) {
        AdGroupListDTO[] dtoArr = new AdGroupListDTO[adGroupPoolEntityList.size()];
        if (CollectionUtils.isEmpty(adGroupPoolEntityList)) {
            return dtoArr;
        }
        List<UUID> relatedIdList = new ArrayList<>();
        for (int i = 0; i < adGroupPoolEntityList.size(); i++) {
            ViewAdGroupEntity adGroupPoolEntity = adGroupPoolEntityList.get(i);
            AdGroupListDTO groupListDTO = new AdGroupListDTO();
            BeanUtils.copyProperties(adGroupPoolEntity, groupListDTO);
            groupListDTO.setAssigned(isInPool);
            dtoArr[i] = groupListDTO;
            relatedIdList.add(groupListDTO.getId());
        }
        // 已分配或者桌面池直接返回数据
        if (isInPool || relatedPoolKey.equals(UserQueryHelper.DESKTOP_POOL_ID)) {
            return dtoArr;
        }
        // 查询未分配的安全组是否有分配到其它磁盘池
        Set<UUID> poolAdGroupSet = diskPoolUserDAO.findByRelatedIdIn(relatedIdList).stream().filter(item ->
                !Objects.equals(item.getDiskPoolId(), poolId)).map(DiskPoolUserEntity::getRelatedId).collect(Collectors.toSet());
        if (poolAdGroupSet.isEmpty()) {
            return dtoArr;
        }
        for (AdGroupListDTO adGroup : dtoArr) {
            if (poolAdGroupSet.contains(adGroup.getId())) {
                adGroup.setDisabled(true);
            }
        }
        return dtoArr;
    }

    /**
     * 合并数组
     *
     * @param leftArr  leftArr
     * @param rightArr rightArr
     * @return AdGroupListDTO[]
     */
    private AdGroupListDTO[] mergeAdGroupListDTOArr(AdGroupListDTO[] leftArr, AdGroupListDTO[] rightArr) {
        Assert.notNull(leftArr, "leftArr can not be null");
        Assert.notNull(rightArr, "rightArr can not be null");

        AdGroupListDTO[] finalUserArr = new AdGroupListDTO[leftArr.length + rightArr.length];
        System.arraycopy(leftArr, 0, finalUserArr, 0, leftArr.length);
        System.arraycopy(rightArr, 0, finalUserArr, leftArr.length, rightArr.length);
        return finalUserArr;
    }
}
