package com.ruijie.rcos.rcdc.rco.module.impl.spi;

import com.alibaba.fastjson.JSONObject;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.api.CbbImageTemplateMgmtAPI;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.dto.CbbImageTemplateDetailDTO;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.enums.CbbImageType;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.enums.ImageRoleType;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.enums.ImageUsageTypeEnum;
import com.ruijie.rcos.rcdc.codec.adapter.def.dto.CbbDispatcherRequest;
import com.ruijie.rcos.rcdc.codec.adapter.def.dto.CbbResponseShineMessage;
import com.ruijie.rcos.rcdc.rco.module.common.query.ConditionQueryRequest;
import com.ruijie.rcos.rcdc.rco.module.common.query.ConditionQueryRequestBuilder;
import com.ruijie.rcos.rcdc.rco.module.common.query.DefaultConditionQueryRequestBuilder;
import com.ruijie.rcos.rcdc.rco.module.def.api.AdminDataPermissionAPI;
import com.ruijie.rcos.rcdc.rco.module.def.api.AdminPermissionAPI;
import com.ruijie.rcos.rcdc.rco.module.def.api.request.aaa.ListImageIdRequest;
import com.ruijie.rcos.rcdc.rco.module.impl.Constants;
import com.ruijie.rcos.rcdc.rco.module.impl.cache.AdminLoginOnTerminalCache;
import com.ruijie.rcos.rcdc.rco.module.impl.cache.AdminLoginOnTerminalCacheManager;
import com.ruijie.rcos.rcdc.rco.module.impl.dto.AdminInfoDTO;
import com.ruijie.rcos.rcdc.rco.module.impl.dto.vdiedit.VDIEditImageListInfoDTO;
import com.ruijie.rcos.rcdc.rco.module.impl.dto.vdiedit.VDIEditImageSessionRequestDTO;
import com.ruijie.rcos.rcdc.rco.module.impl.rccm.service.RccmManageService;
import com.ruijie.rcos.rcdc.rco.module.impl.rccm.service.UnifiedManageDataService;
import com.ruijie.rcos.rcdc.rco.module.impl.service.ImageService;
import com.ruijie.rcos.sk.base.log.Logger;
import com.ruijie.rcos.sk.base.log.LoggerFactory;
import com.ruijie.rcos.sk.modulekit.api.comm.DispatcherImplemetion;
import com.ruijie.rcos.sk.webmvc.api.request.PageWebRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.CollectionUtils;

import java.util.Collections;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

/**
 * Description: 分页获取VDI镜像列表
 * Copyright: Copyright (c) 2020
 * Company: Ruijie Co., Ltd.
 * Create Time: 2020/2/20 08:48
 *
 * @author zhangyichi
 */
@DispatcherImplemetion(Constants.VDI_EDIT_IMAGE_REFRESH_IMAGES_LIST_ACTION)
public class GetVDIImageListHandlerSPIImpl extends AbstractVDIEditImageHandlerSPIImpl<VDIEditImageSessionRequestDTO> {

    private static final Logger LOGGER = LoggerFactory.getLogger(GetVDIImageListHandlerSPIImpl.class);

    private static final Integer DEFAULT_LIMIT = 50;

    @Autowired
    private CbbImageTemplateMgmtAPI cbbImageTemplateMgmtAPI;

    @Autowired
    private ImageService imageService;

    @Autowired
    private AdminDataPermissionAPI adminDataPermissionAPI;

    @Autowired
    private AdminPermissionAPI adminPermissionAPI;

    @Autowired
    private AdminLoginOnTerminalCacheManager adminCacheManager;

    @Autowired
    private RccmManageService rccmManageService;

    @Autowired
    private UnifiedManageDataService unifiedManageDataService;

    @Override
    CbbResponseShineMessage<?> getResponseMessage(CbbDispatcherRequest request, VDIEditImageSessionRequestDTO requestDTO, UUID adminId) {

        PageWebRequest pageWebRequest = new PageWebRequest();
        pageWebRequest.setLimit(DEFAULT_LIMIT);

        ConditionQueryRequestBuilder listQueryRequestBuilder = new DefaultConditionQueryRequestBuilder();
        listQueryRequestBuilder.eq("imageRoleType", ImageRoleType.TEMPLATE)
                .eq("cbbImageType", CbbImageType.VDI)
                .desc("createTime");
        try {
            // 1、入参校验
            JSONObject dataJson = JSONObject.parseObject(request.getData());
            AdminInfoDTO requestDto = dataJson.toJavaObject(AdminInfoDTO.class);
            // 上层做了会话过期的校验
            AdminLoginOnTerminalCache adminCache = adminCacheManager.getIfPresent(requestDto.getAdminSessionId());


            if (!adminPermissionAPI.roleIsAdminOrAdminNameIsSysadmin(adminCache.getAdminId())) {
                ListImageIdRequest listImageIdRequest = new ListImageIdRequest();
                listImageIdRequest.setAdminId(adminCache.getAdminId());
                // 获取镜像权限列表
                List<String> imageIdStrList = adminDataPermissionAPI.listImageIdByAdminId(listImageIdRequest).getImageIdList();
                if (CollectionUtils.isEmpty(imageIdStrList)) {
                    // 没有镜像关联权限 直接空
                    return buildResponseMessage(request, new VDIEditImageListInfoDTO<>(Collections.emptyList()));
                } else {
                    listQueryRequestBuilder.in("id", imageIdStrList.toArray());
                }
            }
            ConditionQueryRequest listQueryRequest = listQueryRequestBuilder.build();
            List<CbbImageTemplateDetailDTO> imageTemplateDetailDTOList = cbbImageTemplateMgmtAPI.listAll(listQueryRequest)
                    .stream().filter(this::isNotLockImage)
                    .collect(Collectors.toList());

            return buildResponseMessage(request, new VDIEditImageListInfoDTO<>(imageTemplateDetailDTOList));
        } catch (Exception ex) {
            LOGGER.error("获取VDI镜像列表失败", ex);
            return buildErrorResponseMessage(request);
        }
    }

    private boolean isNotLockImage(CbbImageTemplateDetailDTO imageTemplateDetailDTO) {
        if (imageTemplateDetailDTO.getImageUsage() == ImageUsageTypeEnum.APP) {
            return false;
        }
        return !imageService.isLockImage(imageTemplateDetailDTO.getId());
    }

    @Override
    VDIEditImageSessionRequestDTO resolveJsonString(String dataJsonString) {
        return JSONObject.parseObject(dataJsonString, VDIEditImageSessionRequestDTO.class);
    }
}
