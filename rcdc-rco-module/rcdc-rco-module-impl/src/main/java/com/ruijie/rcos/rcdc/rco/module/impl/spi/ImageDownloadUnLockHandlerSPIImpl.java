package com.ruijie.rcos.rcdc.rco.module.impl.spi;

import com.alibaba.fastjson.JSONObject;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.api.CbbImageTemplateMgmtAPI;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.constants.ImageMessageCode;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.convert.LocalImageRespCodeConverter;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.dto.image.CbbAbortEditImageTemplateDTO;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.dto.image.CbbGetImageTemplateInfoDTO;
import com.ruijie.rcos.rcdc.codec.adapter.def.api.CbbTranspondMessageHandlerAPI;
import com.ruijie.rcos.rcdc.codec.adapter.def.dto.CbbDispatcherRequest;
import com.ruijie.rcos.rcdc.codec.adapter.def.spi.CbbDispatcherHandlerSPI;
import com.ruijie.rcos.rcdc.rco.module.def.constants.CommonMessageCode;
import com.ruijie.rcos.rcdc.rco.module.impl.spi.image.constant.ImageDispatcherConstants;
import com.ruijie.rcos.rcdc.rco.module.impl.util.ShineMessageUtil;
import com.ruijie.rcos.sk.base.exception.BusinessException;
import com.ruijie.rcos.sk.base.log.Logger;
import com.ruijie.rcos.sk.base.log.LoggerFactory;
import com.ruijie.rcos.sk.modulekit.api.comm.DispatcherImplemetion;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.Assert;
import org.springframework.util.StringUtils;

import java.util.UUID;

/**
 * Description: 终端镜像解锁
 * Copyright: Copyright (c) 2021
 * Company: Ruijie Co., Ltd.
 * Create Time: 2021.04.26
 *
 * @author linhj
 */
@DispatcherImplemetion(ImageDispatcherConstants.IMAGE_DOWNLOAD_UNLOCK)
public class ImageDownloadUnLockHandlerSPIImpl implements CbbDispatcherHandlerSPI {

    private static final Logger LOGGER = LoggerFactory.getLogger(ImageDownloadUnLockHandlerSPIImpl.class);

    private static final String IMAGE_ID = "imageId";

    @Autowired
    private CbbTranspondMessageHandlerAPI messageHandlerAPI;

    @Autowired
    private CbbImageTemplateMgmtAPI cbbImageTemplateMgmtAPI;

    @Override
    public void dispatch(CbbDispatcherRequest request) {
        Assert.notNull(request, "request cannot be null!");
        Assert.hasText(request.getData(), "data in request cannot be blank!");

        final JSONObject inputParam = JSONObject.parseObject(request.getData());
        final UUID imageId = UUID.fromString(inputParam.getString(IMAGE_ID));
        final String terminalId = request.getTerminalId();
        LOGGER.info("终端请求锁定镜像模板，terminalId={}, imageId={}", terminalId, imageId);

        CbbAbortEditImageTemplateDTO dto = new CbbAbortEditImageTemplateDTO(imageId);

        try {
            CbbGetImageTemplateInfoDTO cbbGetImageTemplateInfoDTO = cbbImageTemplateMgmtAPI.getImageTemplateInfo(imageId);
            if (cbbGetImageTemplateInfoDTO == null) {
                LOGGER.error("通过镜像标识[{}]镜像未找到镜像模板", imageId);
                messageHandlerAPI.response(ShineResponseMessage.failWhitCode(request, ImageMessageCode.CODE_ERR_IMAGE_TEMPLATE_NOT_EXIST));
                return;
            }

            // 当前镜像模板对应锁定的终端标识
            final String obtainedTerminalId = cbbGetImageTemplateInfoDTO.getTerminalId();
            if (obtainedTerminalId == null) {
                LOGGER.error("镜像ID为[{}]的镜像模板对应终端标识为空，无法取消镜像绑定", imageId);
                messageHandlerAPI.response(ShineResponseMessage.failWhitCode(request, ImageMessageCode.CODE_ERR_IMAGE_TEMPLATE_STATUS_ERROR));
                return;
            }
            if (!terminalId.equals(obtainedTerminalId)) {
                LOGGER.error("镜像ID为[{}]的镜像模板被其他终端[{}]本地编辑中", imageId, obtainedTerminalId);
                messageHandlerAPI.response(ShineResponseMessage.failWhitCode(request, ImageMessageCode.CODE_ERR_IMAGE_TEMPLATE_EDIT_OTHER));
                return;
            }

            cbbImageTemplateMgmtAPI.abortLocalEditImageTemplate(dto);
            messageHandlerAPI.response(ShineMessageUtil.buildResponseMessage(request, new Object()));
        } catch (BusinessException ex) {
            int codeKey = CommonMessageCode.CODE_ERR_OTHER;
            if (!StringUtils.isEmpty(ex.getKey())) {
                codeKey = LocalImageRespCodeConverter.getErrorCodeByBusinessKey(ex.getKey());
            }
            LOGGER.error("解除锁定镜像失败，imageId={}, code={}", imageId, codeKey, ex);
            messageHandlerAPI.response(ShineResponseMessage.failWhitCode(request, codeKey));
        }
    }
}
