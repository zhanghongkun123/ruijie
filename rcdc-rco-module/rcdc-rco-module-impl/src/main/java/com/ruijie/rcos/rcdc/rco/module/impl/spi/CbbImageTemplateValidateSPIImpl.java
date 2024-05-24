package com.ruijie.rcos.rcdc.rco.module.impl.spi;

import com.ruijie.rcos.rcdc.clouddesktop.module.def.api.CbbImageTemplateMgmtAPI;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.dto.CbbImageTemplateDetailDTO;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.enums.CbbImageType;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.enums.CbbOsType;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.dto.ImageDeskPatternRefDTO;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.enums.CbbDiskType;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.spi.CbbImageTemplateValidateSPI;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.spi.response.CbbImageSystemDiskValidateResponse;
import com.ruijie.rcos.rcdc.rco.module.def.api.ImageTemplateAPI;
import com.ruijie.rcos.rcdc.rco.module.def.api.ServerModelAPI;
import com.ruijie.rcos.rcdc.rco.module.def.api.dto.ImageTypeSupportOsVersionDTO;
import com.ruijie.rcos.rcdc.rco.module.def.api.enums.rccm.UnifiedManageFunctionKeyEnum;
import com.ruijie.rcos.rcdc.rco.module.def.api.request.rccm.UnifiedManageDataRequest;
import com.ruijie.rcos.rcdc.rco.module.def.constants.Constants;
import com.ruijie.rcos.rcdc.rco.module.impl.service.ImageTypeSupportOsVersionService;
import com.ruijie.rcos.rcdc.rco.module.impl.rccm.entity.UnifiedManageDataEntity;
import com.ruijie.rcos.rcdc.rco.module.impl.rccm.service.RccmManageService;
import com.ruijie.rcos.rcdc.rco.module.impl.rccm.service.UnifiedManageDataService;
import com.ruijie.rcos.sk.base.exception.BusinessException;
import com.ruijie.rcos.sk.base.log.Logger;
import com.ruijie.rcos.sk.base.log.LoggerFactory;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.lang.Nullable;
import org.springframework.util.Assert;
import org.springframework.util.CollectionUtils;

import java.util.List;
import java.util.Objects;
import java.util.UUID;

/**
 * Description: 镜像模板检验
 * Copyright: Copyright (c) 2021
 * Company: Ruijie Co., Ltd.
 * Create Time: 2021.08.31
 *
 * @author linhj
 */
public class CbbImageTemplateValidateSPIImpl implements CbbImageTemplateValidateSPI {

    private static final Logger LOGGER = LoggerFactory.getLogger(CbbImageTemplateValidateSPIImpl.class);

    @Autowired
    private CbbImageTemplateMgmtAPI cbbImageTemplateMgmtAPI;


    @Autowired
    private ImageTemplateAPI imageTemplateAPI;

    @Autowired
    private ServerModelAPI serverModelAPI;

    @Autowired
    private UnifiedManageDataService unifiedManageDataService;

    @Autowired
    private RccmManageService rccmManageService;

    @Autowired
    private ImageTypeSupportOsVersionService imageTypeSupportOsVersionService;

    private static final Integer SUCCESS = 0;

    @Override
    public int validate(UUID imageId) throws BusinessException {

        Assert.notNull(imageId, "imageId must be not null");

        CbbImageTemplateDetailDTO imageTemplateDetail = cbbImageTemplateMgmtAPI.getImageTemplateDetail(imageId);
        // 存在链接克隆桌面，进行校验
        if (imageTemplateDetail.getClouldDeskopNum() > 0) {
            LOGGER.info("VDI镜像[{}]，关联了链接克隆的云桌面，需要校验该镜像是否绑定个性桌面", imageId);
            cbbImageTemplateMgmtAPI.checkVDINotBindPersonalDesk(imageId);
        }
        return SUCCESS;
    }

    @Override
    public boolean isRecoveryImage(UUID imageId) throws BusinessException {
        Assert.notNull(imageId, "ImageId can not be null");

        ImageDeskPatternRefDTO imageDeskPatternRefDTO = cbbImageTemplateMgmtAPI.getImageDeskPatternRefDTOByImageIdAndDiskType(
                imageId, CbbDiskType.SYSTEM, false);
        return imageDeskPatternRefDTO.getPersonalDeskRef() == 0;
    }

    @Override
    public int validateForRestore(UUID imageId) throws BusinessException {
        Assert.notNull(imageId, "imageId can not be null");

        imageTemplateAPI.vaildImageRestoreForSnapshot(imageId);
        return SUCCESS;
    }

    /**
     * 校验终端终端
     *
     * @param systemSize 镜像大小
     * @return
     * @throws BusinessException 业务异常
     */
    @Override
    public CbbImageSystemDiskValidateResponse validateImageSystemDisk(Long systemSize) throws BusinessException {
        Assert.notNull(systemSize, "systemSize can not be null");
        CbbImageSystemDiskValidateResponse response = new CbbImageSystemDiskValidateResponse();
        response.setServerModel(serverModelAPI.getServerModel());

        if (serverModelAPI.isMiniModel()) {
            //设置IDV|TCI镜像系统盘限制最大值 200G
            response.setSystemDiskLimit(Constants.MAX_SYSTEM_SIZE_FOR_MINI_SERVER);
            //设置为校验结果 终端系统盘小于最大值 true 校验通过
            response.setEnableSystemSizeValidate(systemSize <= Constants.MAX_SYSTEM_SIZE_FOR_MINI_SERVER);
        } else {
            //获取VDI|RCM服务器模式下 IDV|TCI镜像系统盘最大值 1024G
            //设置系统盘限制大小
            response.setSystemDiskLimit(Constants.SYSTEM_DISK_MAX_SIZE);
            //设置为校验结果 终端系统盘小于最大值 true 校验通过
            response.setEnableSystemSizeValidate(systemSize <= Constants.SYSTEM_DISK_MAX_SIZE);
        }
        return response;
    }

    @Override
    public boolean isLockImage(UUID imageId) throws BusinessException {
        Assert.notNull(imageId, "imageId can not be null");

        UnifiedManageDataRequest request = new UnifiedManageDataRequest();
        request.setRelatedId(imageId);
        request.setRelatedType(UnifiedManageFunctionKeyEnum.IMAGE_TEMPLATE);
        UnifiedManageDataEntity unifiedManageDataEntity = unifiedManageDataService.findByRelatedIdAndRelatedType(request);

        return Objects.nonNull(unifiedManageDataEntity) && rccmManageService.isSlave();
    }

    @Override
    public boolean validateSupportOsVersion(CbbImageType cbbImageType, CbbOsType cbbOsType, @Nullable String osVersion) {

        Assert.notNull(cbbImageType,"cbbImageType can not be null");
        Assert.notNull(cbbOsType,"cbbOsType can not be null");

        List<ImageTypeSupportOsVersionDTO> imageTypeSupportOsVersionDTOList =
                imageTypeSupportOsVersionService.getByImageTypeAndOsType(cbbImageType, cbbOsType);

        ImageTypeSupportOsVersionDTO imageTypeSupportOsVersionDTO = new ImageTypeSupportOsVersionDTO();
        imageTypeSupportOsVersionDTO.setCbbImageType(cbbImageType);
        imageTypeSupportOsVersionDTO.setOsType(cbbOsType);
        imageTypeSupportOsVersionDTO.setOsVersion(osVersion);

        return imageTypeSupportOsVersionService.hasImageSupportOsVersion(imageTypeSupportOsVersionDTOList, imageTypeSupportOsVersionDTO);

    }
}
