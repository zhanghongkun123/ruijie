package com.ruijie.rcos.rcdc.rco.module.impl.spi;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.ruijie.rcos.rcdc.codec.adapter.def.api.CbbTranspondMessageHandlerAPI;
import com.ruijie.rcos.rcdc.codec.adapter.def.dto.CbbDispatcherRequest;
import com.ruijie.rcos.rcdc.codec.adapter.def.dto.CbbResponseShineMessage;
import com.ruijie.rcos.rcdc.codec.adapter.def.spi.CbbDispatcherHandlerSPI;
import com.ruijie.rcos.rcdc.rco.module.def.api.ImageTemplateAPI;
import com.ruijie.rcos.rcdc.rco.module.def.api.dto.AllowCreateImageTemplateDTO;
import com.ruijie.rcos.rcdc.rco.module.def.api.enums.ImageNotCreateEnum;
import com.ruijie.rcos.rcdc.rco.module.def.constants.PermissionConstants;
import com.ruijie.rcos.rcdc.rco.module.impl.cache.AdminLoginOnTerminalCache;
import com.ruijie.rcos.rcdc.rco.module.impl.cache.AdminLoginOnTerminalCacheManager;
import com.ruijie.rcos.rcdc.rco.module.impl.dto.AdminInfoDTO;
import com.ruijie.rcos.rcdc.rco.module.impl.spi.common.CommonMessageCode;
import com.ruijie.rcos.rcdc.rco.module.impl.spi.constant.DispatcherConstants;
import com.ruijie.rcos.sk.base.exception.BusinessException;
import com.ruijie.rcos.sk.base.log.Logger;
import com.ruijie.rcos.sk.base.log.LoggerFactory;
import com.ruijie.rcos.sk.modulekit.api.comm.DispatcherImplemetion;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.Assert;

import java.util.UUID;

import static com.ruijie.rcos.rcdc.hciadapter.module.def.constants.Constants.DEFAULT_PLATFORM_ID;

/**
 * Description: LocalEditImageTemplateCheckIsAllowCreateSPIImpl
 * Copyright: Copyright (c)
 * Company: Ruijie Co., Ltd.
 * Create Time: 2021/3/3 15:45
 *
 * @author coderLee23
 */
@DispatcherImplemetion(DispatcherConstants.ACTION_CHECK_LOCAL_EDIT_TEMPLATE_IS_ALLOW_CREATE)
public class LocalEditImageTemplateCheckIsAllowCreateSPIImpl implements CbbDispatcherHandlerSPI {

    private static final Logger LOGGER = LoggerFactory.getLogger(LocalEditImageTemplateCheckIsAllowCreateSPIImpl.class);


    private static final String IS_ALLOW_CREATE = "is_allow_create";

    /**
     * 是否拥有这个的镜像数据权限
     */
    private static final String HAS_IMAGE = "has_image";

    private static final String IMAGE_TEMPLATE_ID = "imageTemplateId";

    @Autowired
    private CbbTranspondMessageHandlerAPI cbbTranspondMessageHandlerAPI;

    @Autowired
    private ImageTemplateAPI imageTemplateAPI;

    @Autowired
    private AdminLoginOnTerminalCacheManager adminCacheManager;

    @Override
    public void dispatch(CbbDispatcherRequest cbbDispatcherRequest) {
        Assert.notNull(cbbDispatcherRequest, "CbbDispatcherRequest不能为null");
        String requestTerminalId = cbbDispatcherRequest.getTerminalId();
        LOGGER.info("IDV终端请求查询是否允许创建新镜像模板，terminalId={},cbbDispatcherRequest:{}", requestTerminalId,
                JSON.toJSONString(cbbDispatcherRequest));
        JSONObject dataJson = JSONObject.parseObject(cbbDispatcherRequest.getData());
        UUID imageTemplateId = dataJson.getObject(IMAGE_TEMPLATE_ID, UUID.class);
        // 1、入参校验
        AdminInfoDTO requestDto = JSONObject.parseObject(cbbDispatcherRequest.getData(), AdminInfoDTO.class);
        // 会话过期校验不在这边做 ，进去这个请求前的 shine 会去请求权限判读会话
        AdminLoginOnTerminalCache adminCache = adminCacheManager.getIfPresent(requestDto.getAdminSessionId());
        // 判断session 是否存在
        if (adminCache == null) {
            // 给shine 信息 -20 代表session过期不存在
            CbbResponseShineMessage<JSONObject> cbbResponseShineMessage =
                    buildShineMessage(cbbDispatcherRequest, PermissionConstants.SESSION_NOT_EXIST);
            cbbTranspondMessageHandlerAPI.response(cbbResponseShineMessage);
            return;
        }
        try {
            AllowCreateImageTemplateDTO allowCreateImageTemplateDTO =
                    imageTemplateAPI.checkIsAllowCreateAndHasImage(adminCache.getAdminId(), imageTemplateId, DEFAULT_PLATFORM_ID );
            LOGGER.info("输出校验结果={}", JSON.toJSONString(allowCreateImageTemplateDTO));
            JSONObject jsonObject = new JSONObject();
            jsonObject.put(IS_ALLOW_CREATE, allowCreateImageTemplateDTO.getEnableCreate());
            jsonObject.put(HAS_IMAGE, allowCreateImageTemplateDTO.getHasImage());
            int code = convrtCode(allowCreateImageTemplateDTO.getImageNotCreateEnum());
            CbbResponseShineMessage<JSONObject> cbbResponseShineMessage = buildShineMessage(cbbDispatcherRequest, code);
            cbbResponseShineMessage.setContent(jsonObject);
            LOGGER.info("IDV终端请求查询是否允许创建新镜像模板，结果返回={}", JSON.toJSONString(cbbResponseShineMessage));
            cbbTranspondMessageHandlerAPI.response(cbbResponseShineMessage);
        } catch (BusinessException e) {
            LOGGER.error("IDV终端请求查询是否允许创建新镜像模板异常！", e);
            CbbResponseShineMessage<JSONObject> cbbResponseShineMessage = buildShineMessage(cbbDispatcherRequest, CommonMessageCode.CODE_ERR_OTHER);
            cbbTranspondMessageHandlerAPI.response(cbbResponseShineMessage);
        }

    }

    private CbbResponseShineMessage<JSONObject> buildShineMessage(CbbDispatcherRequest request, int code) {
        CbbResponseShineMessage<JSONObject> responseMessage = new CbbResponseShineMessage<>();
        responseMessage.setAction(request.getDispatcherKey());
        responseMessage.setRequestId(request.getRequestId());
        responseMessage.setTerminalId(request.getTerminalId());
        responseMessage.setCode(code);
        return responseMessage;
    }

    /**
     * 枚举转为code值
     *
     * @param enums
     * @return code
     */
    private int convrtCode(ImageNotCreateEnum enums) {
        if (enums == null) {
            return CommonMessageCode.SUCCESS;
        }

        int code;
        switch (enums) {
            case CREATING:
                code = 1;
                break;
            case BACKUP:
                code = 2;
                break;
            default:
                code = CommonMessageCode.SUCCESS;
        }
        return code;
    }

}
