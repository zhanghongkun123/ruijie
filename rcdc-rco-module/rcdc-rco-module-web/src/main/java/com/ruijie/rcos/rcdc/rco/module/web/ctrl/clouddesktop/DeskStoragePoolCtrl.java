package com.ruijie.rcos.rcdc.rco.module.web.ctrl.clouddesktop;

import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.api.CbbImageTemplateMgmtAPI;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.api.CbbNetworkMgmtAPI;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.dto.*;
import com.ruijie.rcos.rcdc.hciadapter.module.def.api.StoragePoolServerMgmtAPI;
import com.ruijie.rcos.rcdc.hciadapter.module.def.dto.ComputerClusterRelatedDTO;
import com.ruijie.rcos.rcdc.hciadapter.module.def.dto.PlatformStoragePoolDTO;
import com.ruijie.rcos.rcdc.rco.module.def.BusinessKey;
import com.ruijie.rcos.rcdc.rco.module.def.api.ClusterAPI;
import com.ruijie.rcos.rcdc.rco.module.def.api.StoragePoolAPI;
import com.ruijie.rcos.rcdc.rco.module.web.Constants;
import com.ruijie.rcos.rcdc.rco.module.web.ctrl.clouddesktop.response.StoragePoolDetailResponse;
import com.ruijie.rcos.rcdc.rco.module.web.ctrl.common.utils.ImageCheckUtils;
import com.ruijie.rcos.rcdc.rco.module.web.response.CommonWebResponse;
import com.ruijie.rcos.sk.base.exception.BusinessException;
import com.ruijie.rcos.sk.base.i18n.LocaleI18nResolver;
import com.ruijie.rcos.sk.base.log.Logger;
import com.ruijie.rcos.sk.base.log.LoggerFactory;
import com.ruijie.rcos.sk.connectkit.api.data.base.PageResponse;
import com.ruijie.rcos.sk.pagekit.api.Match;
import com.ruijie.rcos.sk.pagekit.api.PageQueryRequest;
import com.ruijie.rcos.sk.pagekit.api.PageQueryResponse;
import com.ruijie.rcos.sk.pagekit.api.match.ExactMatch;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.collections4.SetUtils;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.lang.reflect.Field;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Description: Function Description
 * Copyright: Copyright (c) 2021
 * Company: Ruijie Co., Ltd.
 * Create Time: 2021/9/18 15:52
 *
 * @author yanlin
 */

@Api(tags = "云桌面存储池管理")
@Controller
@RequestMapping("/rco/clouddesktop")
public class DeskStoragePoolCtrl {

    private static final Logger LOGGER = LoggerFactory.getLogger(DeskStoragePoolCtrl.class);

    @Autowired
    private StoragePoolServerMgmtAPI storagePoolServerMgmtAPI;

    @Autowired
    private StoragePoolAPI storagePoolAPI;

    @Autowired
    private ClusterAPI clusterAPI;

    @Autowired
    private CbbImageTemplateMgmtAPI cbbImageTemplateMgmtAPI;

    @Autowired
    private CbbNetworkMgmtAPI cbbNetworkMgmtAPI;

    @Autowired
    private ImageCheckUtils imageCheckUtils;

    /**
     * 分页查询存储池列表
     *
     * @param pageQueryRequest 前端请求信息
     * @return 存储池列表
     * @throws BusinessException      业务异常
     * @throws NoSuchFieldException   NoSuchFieldException
     * @throws IllegalAccessException IllegalAccessException
     */
    @ApiOperation("分页查询镜像存储池列表")
    @RequestMapping(value = "/image/storagePool/list", method = RequestMethod.POST)
    public CommonWebResponse<PageResponse<StoragePoolDetailResponse>> listImageStoragePool(PageQueryRequest pageQueryRequest) throws BusinessException,
            NoSuchFieldException, IllegalAccessException {
        return getStoragePoolListResponse(pageQueryRequest, true);
    }

    /**
     * 分页查询存储池列表
     *
     * @param pageQueryRequest 前端请求信息
     * @return 存储池列表
     * @throws BusinessException      业务异常
     * @throws NoSuchFieldException   NoSuchFieldException
     * @throws IllegalAccessException IllegalAccessException
     */
    @ApiOperation("分页查询存储池列表")
    @RequestMapping(value = "/storagePool/list", method = RequestMethod.POST)
    public CommonWebResponse<PageResponse<StoragePoolDetailResponse>> listStoragePool(PageQueryRequest pageQueryRequest) throws BusinessException,
            NoSuchFieldException, IllegalAccessException {
        Assert.notNull(pageQueryRequest, "pageQueryRequest can not be null");
        return getStoragePoolListResponse(pageQueryRequest , false);
    }

    private CommonWebResponse<PageResponse<StoragePoolDetailResponse>> getStoragePoolListResponse(PageQueryRequest pageQueryRequest,
                                                                                                  boolean isFromImage) throws NoSuchFieldException, IllegalAccessException, BusinessException {
        Match[] matchArr = pageQueryRequest.getMatchArr();
        UUID[] imageTemplateIdArr = null;
        UUID[] clusterIdArr = null;
        UUID[] networkIdArr = null;
        List<Match> matchList = Lists.newArrayList();
        if (ArrayUtils.isNotEmpty(matchArr)) {
            for (Match match : matchArr) {
                if (match.getType() == Match.Type.EXACT && StringUtils.equals(Constants.IMAGE_TEMPLATE_ID, ((ExactMatch) match).getFieldName())) {
                    imageTemplateIdArr =
                            Arrays.stream(((ExactMatch) match).getValueArr()).map(item -> UUID.fromString(item.toString())).toArray(UUID[]::new);
                    continue;
                }
                if (match.getType() == Match.Type.EXACT && StringUtils.equals(Constants.CLUSTER_ID, ((ExactMatch) match).getFieldName())) {
                    clusterIdArr =
                            Arrays.stream(((ExactMatch) match).getValueArr()).map(item -> UUID.fromString(item.toString())).toArray(UUID[]::new);
                    continue;
                }
                if (match.getType() == Match.Type.EXACT && StringUtils.equals(Constants.NETWORK_ID, ((ExactMatch) match).getFieldName())) {
                    networkIdArr =
                            Arrays.stream(((ExactMatch) match).getValueArr()).map(item -> UUID.fromString(item.toString())).toArray(UUID[]::new);
                    continue;
                }
                matchList.add(match);
            }
        }
        Field matchArrField = pageQueryRequest.getClass().getDeclaredField("matchArr");
        matchArrField.setAccessible(true);
        matchArrField.set(pageQueryRequest, matchList.toArray(new Match[0]));
        PageQueryResponse<PlatformStoragePoolDTO> response = storagePoolServerMgmtAPI.pageQuery(pageQueryRequest);
        PageResponse<StoragePoolDetailResponse> responsePageResponse = new PageResponse<>();
        // 返回数据为空，则直接返回
        if (ArrayUtils.isEmpty(response.getItemArr())) {
            responsePageResponse.setItems(new StoragePoolDetailResponse[0]);
            return CommonWebResponse.success(responsePageResponse);
        }
        // 转换格式
        List<StoragePoolDetailResponse> responseList =
                getStoragePoolByIds(response.getItemArr(), imageTemplateIdArr, clusterIdArr, networkIdArr, isFromImage);
        responsePageResponse.setItems(responseList.toArray(new StoragePoolDetailResponse[0]));
        responsePageResponse.setTotal(response.getTotal());
        return CommonWebResponse.success(responsePageResponse);
    }

    private List<StoragePoolDetailResponse> getStoragePoolByIds(PlatformStoragePoolDTO[] storagePoolArr,
                                                                UUID[] imageTemplateIdArr, UUID[] clusterIdArr,
                                                                UUID[] networkIdArr, boolean isFromImage) throws BusinessException {
        List<StoragePoolDetailResponse> responseList = Lists.newArrayList();
        if (ArrayUtils.isEmpty(storagePoolArr)) {
            return responseList;
        }
        Map<UUID, ClusterInfoDTO> clusterInfoAllMap = clusterAPI.queryAllClusterInfoList().stream()
                .collect(Collectors.toMap(ClusterInfoDTO::getId, clusterInfoDTO -> clusterInfoDTO));
        // 获取过滤的存储池关联计算集群信息
        Map<UUID, Set<UUID>> storageRelatedMap = storagePoolServerMgmtAPI.getComputerClusterByStoragePoolIdList(Arrays.stream(storagePoolArr)
                        .map(PlatformStoragePoolDTO::getId).collect(Collectors.toList())).stream()
                .collect(Collectors.groupingBy(ComputerClusterRelatedDTO::getRelatedId,
                        Collectors.mapping(ComputerClusterRelatedDTO::getClusterId, Collectors.toSet())));
        for (PlatformStoragePoolDTO storagePoolDTO : storagePoolArr) {
            StoragePoolDetailResponse response = new StoragePoolDetailResponse();
            BeanUtils.copyProperties(storagePoolDTO, response);
            responseList.add(response);
            // 取出当前存储池所属计算集群信息
            Set<UUID> clusterIdSet = Optional.ofNullable(storageRelatedMap.get(response.getId())).orElse(Sets.newHashSet());
            // 镜像模板筛选，镜像所属计算集群cpu架构该存储池是否支持
            if (imageTemplateIdArr != null && imageTemplateIdArr.length > 0) {
                getStoragePoolByImageTemplateId(response, clusterIdSet, imageTemplateIdArr, clusterInfoAllMap ,isFromImage);
            }
            // 计算集群筛选，所选计算集群可选的存储池
            if (clusterIdArr != null && clusterIdArr.length > 0) {
                getStoragePoolByClusterId(response, clusterIdSet, clusterIdArr, clusterInfoAllMap);
            }
            // 网络策略筛选，所选网络策略的计算集群是否包含当前存储池
            if (networkIdArr != null && networkIdArr.length > 0) {
                getClusterByNetworkId(response, clusterIdSet, networkIdArr);
            }
        }
        return responseList;
    }

    private void getStoragePoolByImageTemplateId(StoragePoolDetailResponse response,
                                                 Set<UUID> clusterIdSet, UUID[] imageTemplateIdArr,
                                                 Map<UUID, ClusterInfoDTO> clusterInfoAllMap,
                                                 boolean isFromImage) throws BusinessException {

        Map<UUID, PlatformStoragePoolDTO> storagePoolDTOMap = storagePoolAPI.queryAllStoragePool()
                .stream().collect(Collectors.toMap(PlatformStoragePoolDTO::getId, v -> v, (v1, v2) -> v2));

        for (UUID imageTemplateId : imageTemplateIdArr) {
            CbbImageTemplateDetailDTO imageTemplateDetail = cbbImageTemplateMgmtAPI.getImageTemplateDetail(imageTemplateId);

            if (isFromImage) {
                validForImageEdit(imageTemplateId, response, storagePoolDTOMap);
            } else {
                validForDesktop(imageTemplateId, response, storagePoolDTOMap);
            }

            if (Objects.isNull(imageTemplateDetail.getClusterInfo())) {
                continue;
            }

            Set<String> imageArchSet = clusterInfoAllMap.getOrDefault(imageTemplateDetail.getClusterInfo().getId(), new ClusterInfoDTO()).getArchSet();
            boolean allowMatch = clusterIdSet.stream().allMatch(tempClusterId -> {
                Set<String> archSet = clusterInfoAllMap.getOrDefault(tempClusterId, new ClusterInfoDTO()).getArchSet();
                return CollectionUtils.isEmpty(archSet) || CollectionUtils.isEmpty(imageArchSet) || SetUtils.intersection(archSet, imageArchSet).isEmpty();
            });
            if (allowMatch) {
                response.setCanUsed(false);
                response.setCanUsedMessage(
                        LocaleI18nResolver.resolve(BusinessKey.RCDC_RCO_STORAGE_POOL_CLUSTER_CPU_IMAGE_TEMPLATE_NOT_AGREEMENT));
            }
        }
    }

    private void validForImageEdit(UUID imageTemplateId, StoragePoolDetailResponse response,
                                   Map<UUID, PlatformStoragePoolDTO> storagePoolDTOMap) throws BusinessException {
        if (imageCheckUtils.isImageEditNotSupportChangeStorage(imageTemplateId, response.getId(), storagePoolDTOMap)) {
            response.setCanUsed(false);
            response.setCanUsedMessage(
                    LocaleI18nResolver.resolve(BusinessKey.RCDC_RCO_IMAGE_STORAGE_POOL_CLUSTER_NOT_SUPPORT_SELECT));
        }
    }

    private void validForDesktop(UUID imageTemplateId, StoragePoolDetailResponse response,
                                 Map<UUID, PlatformStoragePoolDTO> storagePoolDTOMap) throws BusinessException {
        if (imageCheckUtils.isSingleImageNotSupportSync(imageTemplateId, response.getId(), storagePoolDTOMap)) {
            response.setCanUsed(false);
            response.setCanUsedMessage(
                    LocaleI18nResolver.resolve(BusinessKey.RCDC_RCO_STORAGE_POOL_CLUSTER_NOT_SUPPORT_SELECT));
        }
    }


    private void getStoragePoolByClusterId(StoragePoolDetailResponse response,
                                           Set<UUID> clusterIdSet, UUID[] clusterIdArr, Map<UUID, ClusterInfoDTO> clusterInfoAllMap) {
        for (UUID clusterId : clusterIdArr) {
            if (!clusterIdSet.contains(clusterId)) {
                ClusterInfoDTO clusterInfoDTO = clusterInfoAllMap.get(clusterId);
                response.setCanUsed(Boolean.FALSE);
                response.setCanUsedMessage(LocaleI18nResolver.resolve(BusinessKey.RCDC_RCO_STORAGE_NOT_CLUSTER,
                        response.getName(), Objects.isNull(clusterInfoDTO) ? clusterId.toString() : clusterInfoDTO.getClusterName()));
            }
        }
    }

    private void getClusterByNetworkId(StoragePoolDetailResponse response, Set<UUID> clusterIdSet, UUID[] networkIdArr) throws BusinessException {
        for (UUID networkId : networkIdArr) {
            CbbDeskNetworkDetailDTO networkDetailDTO = cbbNetworkMgmtAPI.getDeskNetwork(networkId);
            if (!clusterIdSet.contains(networkDetailDTO.getClusterId())) {
                response.setCanUsed(false);
                response.setCanUsedMessage(
                        LocaleI18nResolver.resolve(BusinessKey.RCDC_RCO_NETWORK_AND_STORAGE_POOL_CLUSTER_NOT_AGREEMENT));
            }
        }
    }
}
