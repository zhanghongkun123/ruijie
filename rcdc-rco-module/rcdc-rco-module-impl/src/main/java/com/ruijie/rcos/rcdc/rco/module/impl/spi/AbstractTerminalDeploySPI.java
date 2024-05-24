package com.ruijie.rcos.rcdc.rco.module.impl.spi;

import static com.ruijie.rcos.rcdc.clouddesktop.module.def.enums.ImageTemplateState.AVAILABLE;
import static com.ruijie.rcos.rcdc.clouddesktop.module.def.enums.ImageTemplateState.AVAILABLE_EXPORTING;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import com.ruijie.rcos.rcdc.rco.module.impl.service.ImageTypeSupportTerminalService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.Assert;

import com.google.common.collect.Lists;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.api.CbbIDVDeskMgmtAPI;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.api.CbbImageTemplateMgmtAPI;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.dto.CbbIDVDeskImageTemplateDTO;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.dto.CbbImageTemplateDriverDTO;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.dto.image.CbbGetImageTemplateInfoDTO;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.dto.image.CbbImageTemplateDTO;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.enums.CbbImageType;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.enums.CbbOsType;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.enums.ImageTemplateState;
import com.ruijie.rcos.rcdc.codec.adapter.def.api.CbbTranspondMessageHandlerAPI;
import com.ruijie.rcos.rcdc.rco.module.def.api.TerminalDriverConfigAPI;
import com.ruijie.rcos.rcdc.rco.module.impl.BusinessKey;
import com.ruijie.rcos.rcdc.terminal.module.def.api.CbbTerminalOperatorAPI;
import com.ruijie.rcos.rcdc.terminal.module.def.api.dto.CbbTerminalBasicInfoDTO;
import com.ruijie.rcos.sk.base.exception.BusinessException;
import com.ruijie.rcos.sk.base.log.Logger;
import com.ruijie.rcos.sk.base.log.LoggerFactory;

/**
 * Description: 终端部署备机抽象类
 * Copyright: Copyright (c) 2022
 * Company: Ruijie Co., Ltd.
 * Create Time: 2022/7/12
 *
 * @author WuShengQiang
 */
public abstract class AbstractTerminalDeploySPI {

    private static final Logger LOGGER = LoggerFactory.getLogger(AbstractTerminalDeploySPI.class);

    /**
     * 镜像模板可用状态
     */
    protected static final List<ImageTemplateState> AVAILABLE_STATE = Lists.newArrayList(AVAILABLE, AVAILABLE_EXPORTING);

    @Autowired
    protected CbbTerminalOperatorAPI cbbTerminalOperatorAPI;

    @Autowired
    protected CbbImageTemplateMgmtAPI cbbImageTemplateMgmtAPI;

    @Autowired
    protected CbbTranspondMessageHandlerAPI messageHandlerAPI;

    @Autowired
    private CbbIDVDeskMgmtAPI cbbIDVDeskMgmtAPI;

    @Autowired
    private TerminalDriverConfigAPI terminalDriverConfigAPI;

    @Autowired
    private ImageTypeSupportTerminalService imageTypeSupportTerminalService;

    /**
     * 1.校验镜像是否是IDV/TCI 2.校验镜像模板系统类型是否支持终端CPU 3.校验镜像系统是否支持TC引导
     *
     * @param imageTemplate 镜像信息
     * @param terminalDTO   终端信息
     */
    protected void validateImage(CbbImageTemplateDTO imageTemplate, CbbTerminalBasicInfoDTO terminalDTO, boolean canSupportTC)
            throws BusinessException {

        if (CbbImageType.IDV != imageTemplate.getCbbImageType() && CbbImageType.VOI != imageTemplate.getCbbImageType()) {
            LOGGER.warn("镜像id:{},类型:[{}],不是IDV或VOI类型,不支持使用", imageTemplate.getId(), imageTemplate.getCbbImageType());
            throw new BusinessException(BusinessKey.RCDC_USER_CLOUDDESKTOP_IMAGE_TEMPLATE_TYPE_UNMATCH, String.valueOf(imageTemplate.getId()),
                    CbbImageType.IDV.name());
        }

        // VOI终端校验TC引导
        validateGuide(canSupportTC, imageTemplate, terminalDTO);

        // VOI 不支持服务端安装驱动，下载无需校验
        if (CbbImageType.VOI != imageTemplate.getCbbImageType()) {
            // 校验终端驱动
            validateDriverInstalled(terminalDTO.getTerminalId(), imageTemplate.getId());
        }

        validateSupportOs(imageTemplate, terminalDTO);
    }

    private void validateDriverInstalled(String terminalId, UUID imageId) throws BusinessException {
        CbbIDVDeskImageTemplateDTO cbbIDVDeskImageTemplateDTO = cbbIDVDeskMgmtAPI.getIDVDeskImageTemplate(imageId);
        if (CbbOsType.WIN_XP_SP3 == cbbIDVDeskImageTemplateDTO.getCbbImageTemplateDetailDTO().getOsType()) {
            LOGGER.info("镜像[{}]操作系统为[XP]不需要判断驱动信息", imageId);
            return;
        }

        CbbTerminalBasicInfoDTO terminalBasicInfoResponse = cbbTerminalOperatorAPI.findBasicInfoByTerminalId(terminalId);

        String driverType = terminalBasicInfoResponse.getCpuType();


        // 查找已安装的镜像驱动中是否有与该终端cpu相匹配的
        Optional<CbbImageTemplateDriverDTO> cbbImageTemplateDriverDTOOptional = cbbIDVDeskImageTemplateDTO.getCbbImageTemplateDriverDTOList().stream()
                .filter(driverDTO -> driverDTO.getDriverType().equals(driverType)).findFirst();

        // 未找到与cpu匹配的驱动信息
        if (!cbbImageTemplateDriverDTOOptional.isPresent()) {
            LOGGER.error("镜像[id:{}]未安装对应的cpu类型为[{}]驱动", imageId, driverType);
            if (isNotSupportInstallDriverOsType(imageId)) {
                throw new BusinessException(BusinessKey.RCDC_IMAGE_NOT_SUPPORT_TERMINAL);
            }
            throw new BusinessException(BusinessKey.RCDC_IMAGE_DRIVER_NOT_INSTALL);
        }

        // 找到与cpu匹配的驱动信息，但是该驱动状态还是处于未发布
        CbbImageTemplateDriverDTO cbbImageTemplateDriverDTO = cbbImageTemplateDriverDTOOptional.get();
        if (Boolean.FALSE.equals(cbbImageTemplateDriverDTO.getPublished())) {
            LOGGER.error("镜像[id:{}]对应的驱动:[{}]已经安装,但是镜像还处于待发布状态,不支持下载", imageId, driverType);
            throw new BusinessException(BusinessKey.RCDC_IMAGE_DRIVER_NOT_INSTALL);
        }
    }

    protected void validateSupportOs(CbbImageTemplateDTO imageTemplate, CbbTerminalBasicInfoDTO terminalDTO)
            throws BusinessException {
        String productType = terminalDTO.getProductType();
        CbbImageType imageType = imageTemplate.getCbbImageType();
        CbbOsType osType = imageTemplate.getOsType();
        if (imageTypeSupportTerminalService.hasImageSupportTerminal(productType, imageType, osType)) {
            return;
        }

        LOGGER.error("终端[{}]镜像支持检查校验不通过，终端型号[{}]，镜像类型[{}]，操作系统[{}]",
                terminalDTO.getTerminalId(), productType, imageType, osType);
        throw new BusinessException(BusinessKey.RCDC_IMAGE_NOT_SUPPORT_TERMINAL);
    }

    private void validateGuide(boolean canSupportTC, CbbImageTemplateDTO imageTemplate, CbbTerminalBasicInfoDTO terminalDTO)
            throws BusinessException {
        // 1.若支持TC引导，则不限制系统类型 2.如果不是VOI，则不限制系统类型
        if (Boolean.TRUE.equals(canSupportTC) || imageTemplate.getCbbImageType() != CbbImageType.VOI) {
            return;
        }

        if (imageTemplate.getOsType() == CbbOsType.WIN_7_32) {
            LOGGER.error("终端[{}]不支持TC引导，并且使用WIN7-32镜像", terminalDTO.getTerminalName());
            throw new BusinessException(BusinessKey.RCDC_IMAGE_NOT_SUPPORT_TC);
        }
    }

    /**
     * 镜像操作系统是否不支持安装驱动
     *
     * @param imageTemplateId 镜像ID
     * @return true：不支持, 否则：支持
     * @throws BusinessException 业务异常
     */
    private boolean isNotSupportInstallDriverOsType(UUID imageTemplateId) throws BusinessException {
        CbbGetImageTemplateInfoDTO imageTemplateInfo = cbbImageTemplateMgmtAPI.getImageTemplateInfo(imageTemplateId);
        Assert.notNull(imageTemplateInfo, "imageTemplateInfo can not be null");
        LOGGER.info("镜像[{}]操作系统类型为:[{}],镜像不支持该终端", imageTemplateInfo.getImageName(), imageTemplateInfo.getCbbOsType());
        return imageTemplateInfo.getCbbOsType() == CbbOsType.UOS_64;
    }

}
