package com.ruijie.rcos.rcdc.rco.module.impl.spi;

import com.alibaba.fastjson.JSONObject;
import com.ruijie.rcos.gss.log.module.def.api.BaseAuditLogAPI;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.BusinessKey;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.api.CbbImageTemplateMgmtAPI;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.convert.LocalImageRespCodeConverter;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.dto.image.CbbGetImageTemplateInfoDTO;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.dto.image.CbbLocalEditImageTemplateDTO;
import com.ruijie.rcos.rcdc.codec.adapter.def.annotation.MaintainFilterAction;
import com.ruijie.rcos.rcdc.codec.adapter.def.api.CbbTranspondMessageHandlerAPI;
import com.ruijie.rcos.rcdc.codec.adapter.def.dto.CbbDispatcherRequest;
import com.ruijie.rcos.rcdc.codec.adapter.def.spi.CbbDispatcherHandlerSPI;
import com.ruijie.rcos.rcdc.rco.module.def.constants.CommonMessageCode;
import com.ruijie.rcos.rcdc.rco.module.impl.spi.image.constant.ImageDispatcherConstants;
import com.ruijie.rcos.rcdc.rco.module.impl.util.ShineMessageUtil;
import com.ruijie.rcos.rcdc.terminal.module.def.api.CbbTerminalOperatorAPI;
import com.ruijie.rcos.rcdc.terminal.module.def.api.dto.CbbTerminalBasicInfoDTO;
import com.ruijie.rcos.sk.base.exception.BusinessException;
import com.ruijie.rcos.sk.base.log.Logger;
import com.ruijie.rcos.sk.base.log.LoggerFactory;
import com.ruijie.rcos.sk.modulekit.api.comm.DispatcherImplemetion;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.Assert;
import org.springframework.util.StringUtils;

import java.util.UUID;

/**
 * Description: 终端镜像加锁
 * Copyright: Copyright (c) 2021
 * Company: Ruijie Co., Ltd.
 * Create Time: 2021.04.26
 *
 * @author linhj
 */
@DispatcherImplemetion(ImageDispatcherConstants.IMAGE_DOWNLOAD_LOCK)
@MaintainFilterAction
public class ImageDownloadLockHandlerSPIImpl implements CbbDispatcherHandlerSPI {

    private static final Logger LOGGER = LoggerFactory.getLogger(ImageDownloadLockHandlerSPIImpl.class);

    private static final String IMAGE_ID = "imageId";

    @Autowired
    private CbbTranspondMessageHandlerAPI messageHandlerAPI;

    @Autowired
    private CbbImageTemplateMgmtAPI cbbImageTemplateMgmtAPI;

    @Autowired
    private BaseAuditLogAPI auditLogAPI;

    @Autowired
    private CbbTerminalOperatorAPI cbbTerminalOperatorAPI;

    @Override
    public void dispatch(CbbDispatcherRequest request) {
        Assert.notNull(request, "request cannot be null!");
        Assert.hasText(request.getData(), "data in request cannot be blank!");

        final JSONObject inputParam = JSONObject.parseObject(request.getData());
        final UUID imageId = UUID.fromString(inputParam.getString(IMAGE_ID));
        final String terminalId = request.getTerminalId();
        LOGGER.info("终端请求锁定镜像模板，terminalId={}, imageId={}", terminalId, imageId);

        CbbLocalEditImageTemplateDTO dto = new CbbLocalEditImageTemplateDTO(imageId, terminalId);

        String terminalAddr = terminalId;
        try {
            CbbTerminalBasicInfoDTO terminalBasicInfoDTO = cbbTerminalOperatorAPI.findBasicInfoByTerminalId(terminalId);
            terminalAddr = terminalBasicInfoDTO.getUpperMacAddrOrTerminalId();
        } catch (BusinessException e) {
            LOGGER.debug("查询终端信息发生异常，终端id: [{}]", terminalId);
        }

        try {
            cbbImageTemplateMgmtAPI.lockLocalEditImageTemplate(dto);
            CbbGetImageTemplateInfoDTO cbbGetImageTemplateInfoDTO = cbbImageTemplateMgmtAPI.getImageTemplateInfo(imageId);
            auditLogAPI.recordLog(BusinessKey.RCDC_CLOUDDESKTOP_LOCAL_EDIT_IMAGE_SUCCESS, terminalAddr,
                    cbbGetImageTemplateInfoDTO.getImageName());
            messageHandlerAPI.response(ShineMessageUtil.buildResponseMessage(request, new Object()));
        } catch (BusinessException ex) {
            int codeKey = CommonMessageCode.CODE_ERR_OTHER;
            if (!StringUtils.isEmpty(ex.getKey())) {
                codeKey = LocalImageRespCodeConverter.getErrorCodeByBusinessKey(ex.getKey());
            }
            LOGGER.error("锁定镜像失败，imageId={}, code={}", imageId, codeKey, ex);
            messageHandlerAPI.response(ShineResponseMessage.failWhitCode(request, codeKey));
        }
    }
}
