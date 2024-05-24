package com.ruijie.rcos.rcdc.rco.module.openapi.rest.desk.impl;

import com.google.common.collect.Lists;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.api.CbbImageTemplateMgmtAPI;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.api.request.PageSearchRequest;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.api.request.image.LocalImageTemplatePageRequest;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.dto.CbbImageTemplateDetailDTO;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.dto.MatchEqual;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.enums.ImageRoleType;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.enums.ImageUsageTypeEnum;
import com.ruijie.rcos.rcdc.rco.module.def.constants.Constants;
import com.ruijie.rcos.rcdc.rco.module.openapi.rest.RestErrorCode;
import com.ruijie.rcos.rcdc.rco.module.openapi.rest.common.request.PageQueryServerRequest;
import com.ruijie.rcos.rcdc.rco.module.openapi.rest.desk.ImageTemplateServer;
import com.ruijie.rcos.rcdc.rco.module.openapi.rest.desk.dto.RestImageTemplateDTO;
import com.ruijie.rcos.sk.base.exception.BusinessException;
import com.ruijie.rcos.sk.base.log.Logger;
import com.ruijie.rcos.sk.base.log.LoggerFactory;
import com.ruijie.rcos.sk.modulekit.api.comm.DefaultPageResponse;
import org.apache.commons.lang3.ArrayUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.Assert;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.UUID;
import java.util.stream.Collectors;

/**
 * Description:
 * Copyright: Copyright (c) 2019
 * Company: Ruijie Co., Ltd.
 * Create Time: 2021/9/27 16:43
 *
 * @author xiejian
 */
public class ImageTemplateServerImpl implements ImageTemplateServer {

    private static final Logger LOGGER = LoggerFactory.getLogger(ImageTemplateServerImpl.class);

    public static final String MATCH_FIELD_ROOT_IMAGE_ID = "rootImageId";

    public static final String MATCH_FIELD_IMAGE_ROLE_TYPE = "imageRoleType";

    public static final String MATCH_FIELD_IMAGE_USAGE = "imageUsage";

    @Autowired
    private CbbImageTemplateMgmtAPI cbbImageTemplateMgmtAPI;

    @Override
    public DefaultPageResponse pageQuery(PageQueryServerRequest pageQueryRequest) throws BusinessException {
        Assert.notNull(pageQueryRequest, "request must not be null");
        try {
            LocalImageTemplatePageRequest request = new LocalImageTemplatePageRequest();
            request.setPage(pageQueryRequest.getPage());
            request.setLimit(pageQueryRequest.getLimit());
            PageSearchRequest pageSearchRequest = pageQueryRequest.convertCbb();
            dealTemplateRoleTypeMatch(true, pageSearchRequest);
            dealTemplateUsageMatch(pageSearchRequest);
            request.setMatchEqualArr(pageSearchRequest.getMatchEqualArr());
            request.setSortList(pageSearchRequest.getSortList());
            DefaultPageResponse<CbbImageTemplateDetailDTO> response = cbbImageTemplateMgmtAPI.pageQueryLocalPageImageTemplate(request);
            return convert(true, response);
        } catch (Exception e) {
            LOGGER.error("OpenAPI查询镜像模板列表失败！", e);
            throw new BusinessException(RestErrorCode.RCDC_OPEN_API_SYSTEM_EXCEPTION, e);
        }
    }

    @Override
    public DefaultPageResponse versionPageQuery(UUID rootImageId, PageQueryServerRequest pageQueryRequest) throws BusinessException {
        Assert.notNull(rootImageId, "imageTemplateId must not be null");
        Assert.notNull(pageQueryRequest, "request must not be null");
        try {
            LocalImageTemplatePageRequest request = new LocalImageTemplatePageRequest();
            request.setPage(pageQueryRequest.getPage());
            request.setLimit(pageQueryRequest.getLimit());
            PageSearchRequest pageSearchRequest = pageQueryRequest.convertCbb();
            appendRootImageTemplateIdMatch(rootImageId, pageSearchRequest);
            dealTemplateRoleTypeMatch(false, pageSearchRequest);
            request.setMatchEqualArr(pageSearchRequest.getMatchEqualArr());
            request.setSortList(pageSearchRequest.getSortList());
            DefaultPageResponse<CbbImageTemplateDetailDTO> response = cbbImageTemplateMgmtAPI.pageQueryLocalPageImageTemplate(request);
            return convert(false, response);
        } catch (Exception e) {
            LOGGER.error("OpenAPI查询镜像模板列表失败！", e);
            throw new BusinessException(RestErrorCode.RCDC_OPEN_API_SYSTEM_EXCEPTION, e);
        }
    }

    private void appendRootImageTemplateIdMatch(UUID rootImageId, PageSearchRequest request) {
        Assert.notNull(request, "request can not be null");
        UUID[] matchEqualValueArr = new UUID[] {rootImageId};
        if (ArrayUtils.isEmpty(request.getMatchEqualArr())) {
            MatchEqual matchEqual = new MatchEqual(MATCH_FIELD_ROOT_IMAGE_ID, matchEqualValueArr);
            request.setMatchEqualArr(new MatchEqual[] {matchEqual});
            return;
        }
        for (MatchEqual matchEqual : request.getMatchEqualArr()) {
            if (Objects.equals(matchEqual.getName(), MATCH_FIELD_ROOT_IMAGE_ID)) {
                matchEqual.setValueArr(matchEqualValueArr);
                return;
            }
        }
        // 不存在就加默认条件
        MatchEqual matchEqual = new MatchEqual(MATCH_FIELD_ROOT_IMAGE_ID, matchEqualValueArr);
        List<MatchEqual> matchEqualList = Lists.newArrayList(request.getMatchEqualArr());
        matchEqualList.add(matchEqual);
        request.setMatchEqualArr(matchEqualList.toArray(new MatchEqual[0]));
    }


    private void dealTemplateRoleTypeMatch(boolean isTemplate, PageSearchRequest request) {
        Assert.notNull(request, "request can not be null");
        ImageRoleType[] matchEqualValueArr = {isTemplate ? ImageRoleType.TEMPLATE : ImageRoleType.VERSION};
        if (ArrayUtils.isEmpty(request.getMatchEqualArr())) {
            MatchEqual matchEqual = new MatchEqual(MATCH_FIELD_IMAGE_ROLE_TYPE, matchEqualValueArr);
            request.setMatchEqualArr(new MatchEqual[] {matchEqual});
            return;
        }
        for (MatchEqual matchEqual : request.getMatchEqualArr()) {
            if (Objects.equals(matchEqual.getName(), Constants.QUERY_PLATFORM_ID)) {
                matchEqual.setValueArr(Arrays.stream(matchEqual.getValueArr()).map(value -> UUID.fromString(value.toString())).toArray());
            }
            if (Objects.equals(matchEqual.getName(), MATCH_FIELD_IMAGE_ROLE_TYPE)) {
                removeTemplateRoleTypeMatch(isTemplate, matchEqual);
                return;
            }
        }
        // 不存在就加默认条件
        MatchEqual matchEqual = new MatchEqual(MATCH_FIELD_IMAGE_ROLE_TYPE, matchEqualValueArr);
        List<MatchEqual> matchEqualList = Lists.newArrayList(request.getMatchEqualArr());
        matchEqualList.add(matchEqual);
        request.setMatchEqualArr(matchEqualList.toArray(new MatchEqual[0]));
    }

    private static void removeTemplateRoleTypeMatch(boolean isTemplate, MatchEqual matchEqual) {
        ImageRoleType[] matchEqualValueArr = {isTemplate ? ImageRoleType.TEMPLATE : ImageRoleType.VERSION};
        if (ArrayUtils.isEmpty(matchEqual.getValueArr())) {
            matchEqual.setValueArr(matchEqualValueArr);
            return;
        }
        // String 先转为ImageRoleType枚举类
        for (int i = 0; i < matchEqual.getValueArr().length; i++) {
            ImageRoleType imageRoleTypeEnums = ImageRoleType.valueOf(String.valueOf(matchEqual.getValueArr()[i]));
            matchEqual.getValueArr()[i] = imageRoleTypeEnums;
        }
        List<Object> valueList = Arrays.stream(matchEqual.getValueArr()).filter(item -> {
            ImageRoleType type = (ImageRoleType) item;
            return type == (isTemplate ? ImageRoleType.TEMPLATE : ImageRoleType.VERSION);
        }).collect(Collectors.toList());

        if (CollectionUtils.isEmpty(valueList)) {
            matchEqual.setValueArr(matchEqualValueArr);
            return;
        }
        matchEqual.setValueArr(valueList.toArray(new Object[0]));
    }

    private DefaultPageResponse<RestImageTemplateDTO> convert(boolean isTemplate, DefaultPageResponse<CbbImageTemplateDetailDTO> pageQueryResponse) {
        List<RestImageTemplateDTO> dtoList = new ArrayList<>();
        DefaultPageResponse<RestImageTemplateDTO> result = new DefaultPageResponse<>();
        if (pageQueryResponse == null || pageQueryResponse.getItemArr() == null) {
            result.setTotal(0);
            result.setItemArr(dtoList.toArray(new RestImageTemplateDTO[0]));
            return result;
        }
        Arrays.stream(pageQueryResponse.getItemArr()).forEach(cbbImageTemplateDTO -> {
            RestImageTemplateDTO dto = new RestImageTemplateDTO();
            BeanUtils.copyProperties(cbbImageTemplateDTO, dto);
            dto.setImageType(cbbImageTemplateDTO.getCbbImageType());
            if (Objects.nonNull(cbbImageTemplateDTO.getNetwork())) {
                dto.setNetworkId(cbbImageTemplateDTO.getNetwork().getId());
            }
            if (Objects.nonNull(cbbImageTemplateDTO.getClusterInfo())) {
                dto.setClusterId(cbbImageTemplateDTO.getClusterInfo().getId());
            }
            if (Objects.nonNull(cbbImageTemplateDTO.getStoragePool())) {
                dto.setStoragePoolId(cbbImageTemplateDTO.getStoragePool().getId());
            }
            if (isTemplate) {
                dto.setNewestVersion(null);
                dto.setRootImageId(null);
                dto.setRootImageName(null);
            } else {
                dto.setEnableMultipleVersion(null);
            }

            dto.setCloudDesktopNumOfPersonal(cbbImageTemplateDTO.getClouldDeskopNumOfPersonal());
            dto.setCloudDesktopNumOfRecoverable(cbbImageTemplateDTO.getClouldDeskopNumOfRecoverable());
            dto.setCloudDesktopNumOfAppLayer(cbbImageTemplateDTO.getClouldDeskopNumOfAppLayer());

            dtoList.add(dto);
        });

        result.setTotal(pageQueryResponse.getTotal());
        result.setItemArr(dtoList.toArray(new RestImageTemplateDTO[dtoList.size()]));
        return result;
    }

    /**
     * 默认过滤应用镜像
     * @param request 请求参数
     */
    private void dealTemplateUsageMatch(PageSearchRequest request) {
        Assert.notNull(request, "request can not be null");
        ImageUsageTypeEnum[] usageArr =
                Arrays.stream(ImageUsageTypeEnum.values()).filter(usage -> usage != ImageUsageTypeEnum.APP).toArray(ImageUsageTypeEnum[]::new);

        List<MatchEqual> matchEqualList = Lists.newArrayList(request.getMatchEqualArr());
        // 过滤应用类型镜像
        MatchEqual matchEqual = new MatchEqual(MATCH_FIELD_IMAGE_USAGE, usageArr);
        matchEqualList.add(matchEqual);
        request.setMatchEqualArr(matchEqualList.toArray(new MatchEqual[0]));
    }
}
