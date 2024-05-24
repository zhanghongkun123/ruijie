package com.ruijie.rcos.rcdc.rco.module.web.ctrl.rca.apppool.validation;

import com.ruijie.rcos.rcdc.clouddesktop.module.def.api.CbbImageTemplateMgmtAPI;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.api.CbbVDIDeskStrategyMgmtAPI;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.dto.CbbImageTemplateDetailDTO;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.dto.strategy.CbbDeskStrategyVDIDTO;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.enums.CbbCloudDeskPattern;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.enums.CbbImageType;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.enums.ImageRoleType;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.enums.ImageUsageTypeEnum;
import com.ruijie.rcos.rcdc.hciadapter.module.def.VgpuUtil;
import com.ruijie.rcos.rcdc.hciadapter.module.def.dto.gpu.VgpuExtraInfoSupport;
import com.ruijie.rcos.rcdc.hciadapter.module.def.dto.gpu.VgpuInfoDTO;
import com.ruijie.rcos.rcdc.rca.module.def.RcaBusinessKey;
import com.ruijie.rcos.rcdc.rca.module.def.api.RcaMainStrategyAPI;
import com.ruijie.rcos.rcdc.rca.module.def.api.dto.RcaAppPoolBaseDTO;
import com.ruijie.rcos.rcdc.rca.module.def.dto.strategy.RcaMainStrategyDTO;
import com.ruijie.rcos.rcdc.rca.module.def.dto.strategy.RcaStrategyRelationshipDTO;
import com.ruijie.rcos.rcdc.rca.module.def.enums.RcaEnum;
import com.ruijie.rcos.rcdc.rco.module.def.api.ClusterAPI;
import com.ruijie.rcos.rcdc.rco.module.def.api.DeskSpecAPI;
import com.ruijie.rcos.rcdc.rco.module.def.api.dto.VDIDesktopValidateDTO;
import com.ruijie.rcos.rcdc.rco.module.def.deskstrategy.dto.ImageDeskSpecGpuCheckParamDTO;
import com.ruijie.rcos.sk.base.exception.BusinessException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import java.util.UUID;

/**
 * 应用池校验
 *
 * Description: Function Description
 * Copyright: Copyright (c) 2018 Company:
 * Ruijie Co., Ltd. Create Time: 2024年1月5日
 * 
 * @author zhengjingyong
 */
@Service
public class AppPoolImageTemplateValidation {

    @Autowired
    private CbbVDIDeskStrategyMgmtAPI cbbVDIDeskStrategyMgmtAPI;

    @Autowired
    private RcaMainStrategyAPI rcaMainStrategyAPI;

    @Autowired
    private CbbImageTemplateMgmtAPI cbbImageTemplateMgmtAPI;

    @Autowired
    private ClusterAPI clusterAPI;

    @Autowired
    private DeskSpecAPI deskSpecAPI;

    /**
     *
     * @param appPoolBaseDTO RcaAppPoolBaseDTO
     * @param imageTemplateId 镜像ID
     * @throws BusinessException 业务异常
     */
    public void validate(RcaAppPoolBaseDTO appPoolBaseDTO, UUID imageTemplateId) throws BusinessException {
        Assert.notNull(appPoolBaseDTO, "appPoolBaseDTO must not be null");
        Assert.notNull(imageTemplateId, "imageTemplateId must not be null");

        if (appPoolBaseDTO.getPlatformId() == null) {
            throw new BusinessException(RcaBusinessKey.RCDC_RCA_APP_POOL_IMAGE_PLATFORM_NOT_BE_NULL);
        }

        // 校验镜像模板是否支持这个池模式
        CbbImageTemplateDetailDTO imageTemplate = cbbImageTemplateMgmtAPI.getImageTemplateDetail(imageTemplateId);
        if (imageTemplate.getCbbImageType() != CbbImageType.VDI) {
            throw new BusinessException(RcaBusinessKey.RCDC_RCA_APP_POOL_IMAGE_MUST_VDI);
        }

        if (ImageUsageTypeEnum.APP != imageTemplate.getImageUsage()) {
            throw new BusinessException(RcaBusinessKey.RCDC_RCA_APP_POOL_IMAGE_TYPE_ONLY_APP);
        }

        // 检查策略匹配
        RcaStrategyRelationshipDTO strategyRequestDTO = new RcaStrategyRelationshipDTO();
        strategyRequestDTO.setStrategyId(appPoolBaseDTO.getMainStrategyId());
        RcaMainStrategyDTO mainStrategyDTO = rcaMainStrategyAPI.getStrategyConfigDetail(strategyRequestDTO);
        checkMainStrategyWithPool(appPoolBaseDTO, mainStrategyDTO);

        UUID desktopStrategyId = rcaMainStrategyAPI.getCorrelativeDesktopStrategyId(appPoolBaseDTO.getMainStrategyId());
        // 动态池的情况下，单版本镜像需要检查是否存在静态桌面绑定
        if (RcaEnum.PoolType.DYNAMIC == appPoolBaseDTO.getPoolType()) {
            boolean isNormalImageTemplate = imageTemplate.getImageRoleType() == ImageRoleType.TEMPLATE && //
                    !Boolean.TRUE.equals(imageTemplate.getEnableMultipleVersion());
            // 动态池镜像模板必须支持还原VDI
            if (isNormalImageTemplate && imageTemplate.getClouldDeskopNumOfPersonal() > 0) {
                throw new BusinessException(RcaBusinessKey.RCDC_RCO_APP_POOL_IMAGE_DYNAMIC_IMAGE_HAS_PERSONAL_VDI);
            }

            CbbDeskStrategyVDIDTO strategyDTO = cbbVDIDeskStrategyMgmtAPI.getDeskStrategyVDI(desktopStrategyId);
            if (CbbCloudDeskPattern.RECOVERABLE != strategyDTO.getPattern()) {
                throw new BusinessException(RcaBusinessKey.RCDC_APP_POOL_EDIT_MAIN_STRATEGY_FAIL_BY_STRATEGY_PATTERN);
            }
        }

        // 校验VDI桌面策略选择
        VDIDesktopValidateDTO validateDTO = new VDIDesktopValidateDTO();
        validateDTO.setImageId(imageTemplateId);
        validateDTO.setNetworkId(appPoolBaseDTO.getNetworkId());
        validateDTO.setStrategyId(desktopStrategyId);
        validateDTO.setClusterId(appPoolBaseDTO.getClusterId());
        validateDTO.setPlatformId(appPoolBaseDTO.getPlatformId());
        clusterAPI.validateVDIDesktopConfig(validateDTO);

        // 校验显卡支持情况
        ImageDeskSpecGpuCheckParamDTO checkParamDTO = new ImageDeskSpecGpuCheckParamDTO();
        checkParamDTO.setImageId(imageTemplateId);
        checkParamDTO.setClusterId(appPoolBaseDTO.getClusterId());
        checkParamDTO.setImageEditionId(imageTemplate.getLastRecoveryPointId());
        VgpuExtraInfoSupport vGpuExtraInfo = VgpuUtil.deserializeVgpuExtraInfoByType(
                appPoolBaseDTO.getVgpuType(), appPoolBaseDTO.getVgpuExtraInfo());
        VgpuInfoDTO vgpuInfoDTO = new VgpuInfoDTO(appPoolBaseDTO.getVgpuType(), vGpuExtraInfo);
        checkParamDTO.setVgpuInfoDTO(vgpuInfoDTO);
        deskSpecAPI.checkGpuSupportByImageAndSpec(checkParamDTO);
    }

    private void checkMainStrategyWithPool(RcaAppPoolBaseDTO appPoolBaseDTO, RcaMainStrategyDTO rcaMainStrategyDTO) throws BusinessException {
        if (appPoolBaseDTO == null || rcaMainStrategyDTO == null) {
            return;
        }

        if (appPoolBaseDTO.getHostSourceType() != null && !appPoolBaseDTO.getHostSourceType().
                equals(rcaMainStrategyDTO.getHostSourceType())) {
            throw new BusinessException(RcaBusinessKey.RCDC_RCA_MAIN_STRATEGY_HOST_SOURCE_TYPE_NOT_MATCH);
        }

        if (appPoolBaseDTO.getSessionType() != null && !rcaMainStrategyDTO.getHostSessionType().equals(appPoolBaseDTO.getSessionType())) {
            throw new BusinessException(RcaBusinessKey.RCDC_RCA_STRATEGY_HOST_SESSION_TYPE_NOT_MATCH);
        }

        if (RcaEnum.PoolType.DYNAMIC.equals(appPoolBaseDTO.getPoolType())) {
            if (rcaMainStrategyDTO.getDesktopStrategyConfig() != null &&
                    rcaMainStrategyDTO.getDesktopStrategyConfig().getPattern() == CbbCloudDeskPattern.PERSONAL) {
                //1.动态池不支持主机模式为个性
                throw new BusinessException(RcaBusinessKey.RCDC_RCA_STRATEGY_DYNAMIC_POOL_NOT_SUPPORT_PERSONAL_PATTERN);
            }
            //2.动态池不支持开启个性数据保存
            if (Boolean.TRUE.equals(rcaMainStrategyDTO.getPersonalDataRetention())) {
                throw new BusinessException(RcaBusinessKey.RCDC_RCA_STRATEGY_DYNAMIC_POOL_NOT_SUPPORT_PERSONAL_DATA_RETENTION);
            }
        }
    }
}
