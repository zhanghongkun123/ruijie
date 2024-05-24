package com.ruijie.rcos.rcdc.rco.module.impl.spi;

import com.alibaba.fastjson.JSONObject;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.dto.CbbImageTemplateDetailDTO;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.dto.image.CbbImageTemplateDTO;
import com.ruijie.rcos.rcdc.codec.adapter.def.dto.CbbDispatcherRequest;
import com.ruijie.rcos.rcdc.codec.adapter.def.dto.CbbResponseShineMessage;
import com.ruijie.rcos.rcdc.codec.adapter.def.spi.CbbDispatcherHandlerSPI;
import com.ruijie.rcos.rcdc.rco.module.def.constants.ConfigWizardForIDVCode;
import com.ruijie.rcos.rcdc.rco.module.impl.BusinessKey;
import com.ruijie.rcos.rcdc.rco.module.impl.dto.TerminalDeployValidateImageDTO;
import com.ruijie.rcos.rcdc.rco.module.impl.service.ImageCalcService;
import com.ruijie.rcos.rcdc.rco.module.impl.spi.image.constant.ImageDispatcherConstants;
import com.ruijie.rcos.rcdc.rco.module.impl.spi.image.vo.StandbyValidateImageVO;
import com.ruijie.rcos.rcdc.rco.module.impl.util.ShineMessageUtil;
import com.ruijie.rcos.rcdc.terminal.module.def.api.dto.CbbTerminalBasicInfoDTO;
import com.ruijie.rcos.sk.base.exception.BusinessException;
import com.ruijie.rcos.sk.base.log.Logger;
import com.ruijie.rcos.sk.base.log.LoggerFactory;
import com.ruijie.rcos.sk.modulekit.api.comm.DispatcherImplemetion;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.Assert;

/**
 * Description: 终端部署校验镜像状态
 * Copyright: Copyright (c) 2022
 * Company: Ruijie Co., Ltd.
 * Create Time: 2022/7/12
 *
 * @author WuShengQiang
 */
@DispatcherImplemetion(ImageDispatcherConstants.TERMINAL_DEPLOY_VALIDATE_IMAGE_STATE)
public class TerminalDeployValidateImageStateSPIImpl extends AbstractTerminalDeploySPI implements CbbDispatcherHandlerSPI {

    private static final Logger LOGGER = LoggerFactory.getLogger(TerminalDeployValidateImageStateSPIImpl.class);

    private static final String RCDC_CLOUDDESKTOP_VM_MODE_ENTITY_IS_NULL = "23250354";

    @Autowired
    private ImageCalcService imageCalcService;

    @Override
    public void dispatch(CbbDispatcherRequest request) {
        Assert.notNull(request, "request cannot be null!");
        Assert.notNull(request.getTerminalId(), "terminalId cannot be null!");
        String data = request.getData();
        Assert.notNull(data, "data cannot be null!");
        JSONObject dataJson = JSONObject.parseObject(data);
        TerminalDeployValidateImageDTO requestDto = dataJson.toJavaObject(TerminalDeployValidateImageDTO.class);
        CbbResponseShineMessage<?> cbbResponseShineMessage;

        // 查询终端信息
        CbbTerminalBasicInfoDTO terminalDTO = null;
        try {
            terminalDTO = cbbTerminalOperatorAPI.findBasicInfoByTerminalId(request.getTerminalId());
        } catch (BusinessException e) {
            cbbResponseShineMessage = ShineMessageUtil.buildErrorResponseMessage(request, ConfigWizardForIDVCode.TERMINAL_NOT_EXIST);
            messageHandlerAPI.response(cbbResponseShineMessage);
            return;
        }

        CbbImageTemplateDetailDTO imageTemplate = cbbImageTemplateMgmtAPI.getImageTemplateDetail(requestDto.getImageId());
        if (!AVAILABLE_STATE.contains(imageTemplate.getImageState())) {
            LOGGER.warn("终端部署校验镜像状态:{},结果不可用", imageTemplate.getImageState());
            cbbResponseShineMessage = ShineMessageUtil.buildErrorResponseMessage(request, ConfigWizardForIDVCode.IMAGE_TEMPLATE_STATUS_NOT_AVAILABLE);
            messageHandlerAPI.response(cbbResponseShineMessage);
        }

        // 校验镜像
        CbbImageTemplateDTO cbbImageTemplateDTO = new CbbImageTemplateDTO();
        cbbImageTemplateDTO.setId(imageTemplate.getId());
        cbbImageTemplateDTO.setCbbImageType(imageTemplate.getCbbImageType());
        cbbImageTemplateDTO.setOsType(imageTemplate.getOsType());
        try {
            validateImage(cbbImageTemplateDTO, terminalDTO, requestDto.getCanSupportTC());
        } catch (BusinessException e) {
            LOGGER.error("校验镜像出现异常, imageId: {},异常信息:{}", imageTemplate.getId(), e);
            switch (e.getKey()) {
                case BusinessKey.RCDC_IMAGE_NOT_SUPPORT_TC:
                    cbbResponseShineMessage = ShineMessageUtil.buildErrorResponseMessage(request,
                            ConfigWizardForIDVCode.NOT_SUPPORT_TC_CAN_NOT_USER_WIN7_32);
                    break;
                case RCDC_CLOUDDESKTOP_VM_MODE_ENTITY_IS_NULL:
                    cbbResponseShineMessage = ShineMessageUtil.buildErrorResponseMessage(request,
                            ConfigWizardForIDVCode.DESK_RELEASE_IMAGE_UN_SUPPORT_WITH_TERMINAL_CPU);
                    break;
                case BusinessKey.RCDC_IMAGE_DRIVER_NOT_INSTALL:
                    cbbResponseShineMessage = ShineMessageUtil.buildErrorResponseMessage(request,
                            ConfigWizardForIDVCode.IMAGE_DRIVER_NOT_INSTALL);
                    break;
                case BusinessKey.RCDC_IMAGE_NOT_SUPPORT_TERMINAL:
                    cbbResponseShineMessage = ShineMessageUtil.buildErrorResponseMessage(request,
                            ConfigWizardForIDVCode.IMAGE_NOT_SUPPORT_TERMINAL);
                    break;
                default:
                    cbbResponseShineMessage = ShineMessageUtil.buildErrorResponseMessage(request,
                            ConfigWizardForIDVCode.IMAGE_TEMPLATE_STATUS_NOT_AVAILABLE);
                    break;
            }
            messageHandlerAPI.response(cbbResponseShineMessage);
            return;
        } catch (Exception e) {
            LOGGER.error("校验镜像出现未知异常, imageId: {},异常信息:{}", imageTemplate.getId(), e);
            cbbResponseShineMessage = ShineMessageUtil.buildErrorResponseMessage(request, ConfigWizardForIDVCode.CODE_ERR_OTHER);
            messageHandlerAPI.response(cbbResponseShineMessage);
            return;
        }

        // 返回给shine的信息
        StandbyValidateImageVO standbyValidateImageVO = new StandbyValidateImageVO();
        try {
            int imageFileSize = imageCalcService.getImageFileSize(imageTemplate.getId());
            standbyValidateImageVO.setImageFileSize(imageFileSize);
        } catch (BusinessException e) {
            LOGGER.error("获取镜像大小失败, imageId: {},异常信息:{}", imageTemplate.getId(), e);
            standbyValidateImageVO.setImageFileSize(0);
        }

        standbyValidateImageVO.setImageId(imageTemplate.getId());
        standbyValidateImageVO.setImageName(imageTemplate.getImageName());
        standbyValidateImageVO.setCbbImageType(imageTemplate.getCbbImageType());
        standbyValidateImageVO.setCbbOsType(imageTemplate.getOsType());
        cbbResponseShineMessage = ShineMessageUtil.buildResponseMessage(request, standbyValidateImageVO);
        messageHandlerAPI.response(cbbResponseShineMessage);
    }
}
