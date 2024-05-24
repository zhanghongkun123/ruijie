package com.ruijie.rcos.rcdc.rco.module.impl.spi;

import com.alibaba.fastjson.JSON;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.Assert;

import com.alibaba.fastjson.JSONObject;
import com.ruijie.rcos.rcdc.codec.adapter.def.dto.CbbDispatcherRequest;
import com.ruijie.rcos.rcdc.codec.adapter.def.spi.CbbDispatcherHandlerSPI;
import com.ruijie.rcos.rcdc.rco.module.def.api.AdminDataPermissionAPI;
import com.ruijie.rcos.rcdc.rco.module.def.api.dto.AdminDataPermissionDTO;
import com.ruijie.rcos.rcdc.rco.module.def.api.enums.AdminDataPermissionType;
import com.ruijie.rcos.rcdc.rco.module.def.api.request.CreateAdminDataPermissionRequest;
import com.ruijie.rcos.rcdc.rco.module.def.constants.PermissionConstants;
import com.ruijie.rcos.rcdc.rco.module.impl.Constants;
import com.ruijie.rcos.rcdc.rco.module.impl.cache.AdminLoginOnTerminalCache;
import com.ruijie.rcos.rcdc.rco.module.impl.cache.AdminLoginOnTerminalCacheManager;
import com.ruijie.rcos.rcdc.rco.module.impl.dto.PublishImageDTO;
import com.ruijie.rcos.rcdc.rco.module.impl.spi.common.ShineMessageHandler;
import com.ruijie.rcos.sk.base.log.Logger;
import com.ruijie.rcos.sk.base.log.LoggerFactory;
import com.ruijie.rcos.sk.modulekit.api.comm.DispatcherImplemetion;

/**
 * Description: 终端提取镜像发布完成后续处理
 * Copyright: Copyright (c) 2019
 * Company: Ruijie Co., Ltd.
 * Create Time: 2021/7/24 21:08
 *
 * @author linrenjian
 */
@DispatcherImplemetion(Constants.LOCAL_POST_PROCESSING_AFTER_PUBLISHING)
public class LocalPostProcessingAfterPublishingSPIImpl implements CbbDispatcherHandlerSPI {

    @Autowired
    private AdminDataPermissionAPI adminDataPermissionAPI;

    @Autowired
    private AdminLoginOnTerminalCacheManager adminCacheManager;

    @Autowired
    private ShineMessageHandler shineMessageHandler;

    private static final Logger LOGGER = LoggerFactory.getLogger(LocalPostProcessingAfterPublishingSPIImpl.class);


    @Override
    public void dispatch(CbbDispatcherRequest request) {
        // 请求不为空
        Assert.notNull(request, "request cannot be null!");
        // 数据不为空
        Assert.hasText(request.getData(), "data in request cannot be blank!");

        // 1、入参校验
        PublishImageDTO requestDto = JSONObject.parseObject(request.getData(), PublishImageDTO.class);
        LOGGER.error("终端提取镜像发布完成后续处理，发布信息：{},", JSON.toJSONString(requestDto));
        AdminLoginOnTerminalCache adminCache = adminCacheManager.getIfPresent(requestDto.getAdminSessionId());
        // 判断session 是否存在
        if (adminCache == null) {
            // 给shine 信息 -20 代表session过期不存在
            response(request, PermissionConstants.SESSION_NOT_EXIST);
            return;
        }
        AdminDataPermissionDTO adminInfoMessageDTO = new AdminDataPermissionDTO();
        adminInfoMessageDTO.setAdminId(adminCache.getAdminId());
        adminInfoMessageDTO.setPermissionDataId(requestDto.getImageTemplateId().toString());
        adminInfoMessageDTO.setPermissionDataType(AdminDataPermissionType.IMAGE);
        CreateAdminDataPermissionRequest createAdminDataPermissionRequest = new CreateAdminDataPermissionRequest();
        createAdminDataPermissionRequest.setAdminDataPermissionDTO(adminInfoMessageDTO);
        adminDataPermissionAPI.createAdminGroupPermission(createAdminDataPermissionRequest);
        // 给shine 信息
        response(request, Constants.SUCCESS);
    }

    /**
     * 给shine 信息
     * 
     * @param request
     * @param code
     */
    private void response(CbbDispatcherRequest request, Integer code) {
        try {
            // 返回消息给shine
            shineMessageHandler.responseContent(request, code, null);
        } catch (Exception e) {
            LOGGER.error("终端{}获取管理员信息失败", request.getTerminalId(), e);
        }
    }

}
