package com.ruijie.rcos.rcdc.rco.module.impl.spi;

import java.text.SimpleDateFormat;
import java.util.List;
import java.util.UUID;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.Assert;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import com.alibaba.fastjson.JSONObject;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.api.CbbImageTemplateMgmtAPI;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.dto.CbbImageTemplateDetailDTO;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.dto.CbbPublishImageTemplateTaskDTO;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.enums.ImageTemplateState;
import com.ruijie.rcos.rcdc.codec.adapter.def.dto.CbbDispatcherRequest;
import com.ruijie.rcos.rcdc.codec.adapter.def.dto.CbbResponseShineMessage;
import com.ruijie.rcos.rcdc.rco.module.def.api.AdminDataPermissionAPI;
import com.ruijie.rcos.rcdc.rco.module.def.api.AdminPermissionAPI;
import com.ruijie.rcos.rcdc.rco.module.def.api.ImageTemplateAPI;
import com.ruijie.rcos.rcdc.rco.module.def.api.dto.ImageTemplateDetailDTO;
import com.ruijie.rcos.rcdc.rco.module.def.api.dto.ImageTemplatePublishTaskDTO;
import com.ruijie.rcos.rcdc.rco.module.def.api.request.aaa.ListImageIdRequest;
import com.ruijie.rcos.rcdc.rco.module.def.constants.PermissionConstants;
import com.ruijie.rcos.rcdc.rco.module.impl.Constants;
import com.ruijie.rcos.rcdc.rco.module.impl.cache.AdminLoginOnTerminalCache;
import com.ruijie.rcos.rcdc.rco.module.impl.cache.AdminLoginOnTerminalCacheManager;
import com.ruijie.rcos.rcdc.rco.module.impl.dto.AdminInfoDTO;
import com.ruijie.rcos.rcdc.rco.module.impl.dto.vdiedit.VDIEditImageIdRequestDTO;
import com.ruijie.rcos.rcdc.rco.module.impl.util.CapacityUnitUtils;
import com.ruijie.rcos.rcdc.rco.module.impl.util.ShineMessageUtil;
import com.ruijie.rcos.sk.base.exception.BusinessException;
import com.ruijie.rcos.sk.base.log.Logger;
import com.ruijie.rcos.sk.base.log.LoggerFactory;
import com.ruijie.rcos.sk.modulekit.api.comm.DispatcherImplemetion;

/**
 * Description: 获取VDI镜像详情
 * Copyright: Copyright (c) 2020
 * Company: Ruijie Co., Ltd.
 * Create Time: 2020/4/21 18:52
 *
 * @author zhangyichi
 */
@DispatcherImplemetion(Constants.VDI_EDIT_IMAGE_GET_IMAGES_DETAIL_ACTION)
public class GetVDIImageDetailHandlerSPIImpl extends AbstractVDIEditImageHandlerSPIImpl<VDIEditImageIdRequestDTO> {

    private static final Logger LOGGER = LoggerFactory.getLogger(GetVDIImageDetailHandlerSPIImpl.class);

    @Autowired
    private CbbImageTemplateMgmtAPI cbbImageTemplateMgmtAPI;

    @Autowired
    private ImageTemplateAPI imageTemplateAPI;

    @Autowired
    private AdminLoginOnTerminalCacheManager adminCacheManager;

    @Autowired
    private AdminPermissionAPI adminPermissionAPI;

    @Autowired
    private AdminDataPermissionAPI adminDataPermissionAPI;

    @Override
    CbbResponseShineMessage<?> getResponseMessage(CbbDispatcherRequest request, VDIEditImageIdRequestDTO requestDTO, UUID adminId) {
        try {
            ImageTemplateDetailDTO imageTemplateDetailDTO = new ImageTemplateDetailDTO();

            JSONObject dataJson = JSONObject.parseObject(request.getData());
            AdminInfoDTO requestDto = dataJson.toJavaObject(AdminInfoDTO.class);
            // 上层做了会话过期的校验
            AdminLoginOnTerminalCache adminCache = adminCacheManager.getIfPresent(requestDto.getAdminSessionId());
            // 如果是超级管理员 或者 管理员名称是sysadmin 查询全部 不是超级管理员 进行条件查询
            if (!adminPermissionAPI.roleIsAdminOrAdminNameIsSysadmin(adminCache.getAdminId())) {
                ListImageIdRequest listImageIdRequest = new ListImageIdRequest();
                listImageIdRequest.setAdminId(adminCache.getAdminId());
                // 获取镜像权限列表
                List<String> imageIdStrList = adminDataPermissionAPI.listImageIdByAdminId(listImageIdRequest).getImageIdList();
                if (CollectionUtils.isEmpty(imageIdStrList)) {
                    // 没有镜像关联权限 直接空
                    LOGGER.error("获取镜像数据权限列表为空，获取VDI镜像详情失败，用户ID：{},镜像ID:{}", adminCache.getAdminId(), requestDTO.getId());
                    // 权限查询异常
                    return ShineMessageUtil.buildErrorResponseMessage(request, PermissionConstants.PERMISSSION_QUERY_EXCEPTION);
                }
                // 是否有镜像模板权限
                Boolean hasImageTemplate = imageIdStrList.stream().anyMatch(dto -> {
                    return dto.equals(requestDTO.getId().toString());
                });

                if (!hasImageTemplate) {
                    // 没有镜像关联权限 直接空
                    LOGGER.error("镜像不在所属数据权限中，获取VDI镜像详情失败，用户ID：{},镜像ID:{}", adminCache.getAdminId(), requestDTO.getId());
                    // 权限查询异常
                    return ShineMessageUtil.buildErrorResponseMessage(request, PermissionConstants.PERMISSSION_QUERY_EXCEPTION);
                }
            }
            // 镜像详情
            buildVDIImageDetai(requestDTO, imageTemplateDetailDTO);
            return buildResponseMessage(request, imageTemplateDetailDTO);
        } catch (Exception ex) {
            LOGGER.error("获取VDI镜像详情失败", ex);
            return buildErrorResponseMessage(request);
        }
    }

    @Override
    VDIEditImageIdRequestDTO resolveJsonString(String dataJsonString) {
        return JSONObject.parseObject(dataJsonString, VDIEditImageIdRequestDTO.class);
    }


    /**
     * VDI 镜像详情
     * 
     * @param requestDTO
     * @param imageTemplateDetailDTO
     * @throws BusinessException
     */
    private void buildVDIImageDetai(VDIEditImageIdRequestDTO requestDTO, ImageTemplateDetailDTO imageTemplateDetailDTO) throws BusinessException {

        CbbImageTemplateDetailDTO cbbImageTemplateDetailDTO = cbbImageTemplateMgmtAPI.getImageTemplateDetail(requestDTO.getId());
        Assert.notNull(cbbImageTemplateDetailDTO, "查询镜像数据不存在，镜像ID：[{" + requestDTO.getId() + "}]");

        BeanUtils.copyProperties(cbbImageTemplateDetailDTO, imageTemplateDetailDTO);
        imageTemplateDetailDTO.setMemory(CapacityUnitUtils.mb2Gb(cbbImageTemplateDetailDTO.getMemory()));
        boolean canEditSystemDiskSize = imageTemplateAPI.isImageCanEditSystemDiskSize(cbbImageTemplateDetailDTO.getId());
        boolean canEditDataDiskSize = imageTemplateAPI.isImageCanEditDataDiskSize(cbbImageTemplateDetailDTO.getId());
        imageTemplateDetailDTO.setCanEditSystemDiskSize(canEditSystemDiskSize);
        imageTemplateDetailDTO.setCanEditDataDiskSize(canEditDataDiskSize);

        if (cbbImageTemplateDetailDTO.getImageState() == ImageTemplateState.PREPUBLISH) {
            CbbPublishImageTemplateTaskDTO cbbTaskDto = cbbImageTemplateMgmtAPI.getPublishImageTemplateTask(cbbImageTemplateDetailDTO.getId());
            if (cbbTaskDto != null) {
                ImageTemplatePublishTaskDTO imageTemplatePublishTaskDTO = new ImageTemplatePublishTaskDTO();
                imageTemplatePublishTaskDTO.setId(cbbTaskDto.getImageTemplateId());

                if (!StringUtils.isEmpty(cbbTaskDto.getCronExpression())) {
                    imageTemplatePublishTaskDTO.setScheduleTime(imageTemplateAPI.parseCronExpression(cbbTaskDto.getCronExpression()));
                }
                if (!StringUtils.isEmpty(cbbTaskDto.getTipMsgSendTime())) {
                    imageTemplatePublishTaskDTO.setNoticeTime(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(cbbTaskDto.getTipMsgSendTime()));
                }
                imageTemplatePublishTaskDTO.setTipMsg(cbbTaskDto.getTipMsg());
                imageTemplatePublishTaskDTO.setErrorMsg(cbbTaskDto.getErrorMsg());
                imageTemplatePublishTaskDTO.setEnableForcePublish(cbbTaskDto.getEnableForcePublish());
                imageTemplatePublishTaskDTO.setStatus(cbbTaskDto.getStatus());
                imageTemplatePublishTaskDTO.setPublishType(cbbTaskDto.getType());
                imageTemplateDetailDTO.setPublishTaskDTO(imageTemplatePublishTaskDTO);
            }
        }
    }
}
