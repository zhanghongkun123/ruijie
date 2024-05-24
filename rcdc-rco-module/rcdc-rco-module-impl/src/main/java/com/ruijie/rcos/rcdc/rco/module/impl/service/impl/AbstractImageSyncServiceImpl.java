package com.ruijie.rcos.rcdc.rco.module.impl.service.impl;


import com.ruijie.rcos.rcdc.clouddesktop.module.def.api.CbbImageTemplateMgmtAPI;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.dto.CbbImageTemplateDetailDTO;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.enums.ImageTemplateState;
import com.ruijie.rcos.rcdc.rco.module.def.BusinessKey;
import com.ruijie.rcos.sk.base.exception.BusinessException;
import com.ruijie.rcos.sk.base.log.Logger;
import com.ruijie.rcos.sk.base.log.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.UUID;

import static com.ruijie.rcos.rcdc.rco.module.def.BusinessKey.RCDC_CLOUDDESKTOP_IMAGE_NOT_EXIST;

/**
 * Description: 抽象处理
 * Copyright: Copyright (c) 2018
 * Company: Ruijie Co., Ltd.
 * Create Time: 2023/6/29
 *
 * @author zhiweiHong
 */
public abstract class AbstractImageSyncServiceImpl {

    private static final Logger LOGGER = LoggerFactory.getLogger(AbstractImageSyncServiceImpl.class);

    @Autowired
    private CbbImageTemplateMgmtAPI cbbImageTemplateMgmtAPI;

    protected void notAllowRoleExecuteCheck(Boolean isRoleEq) throws BusinessException {
        if (isRoleEq) {
            throw new BusinessException(BusinessKey.RCDC_RCO_CLUSTER_ROLE_NOT_ALLOW);
        }
    }


    protected void isImageExist(UUID imageId) throws BusinessException {
        boolean isExistImage = cbbImageTemplateMgmtAPI.existsImageTemplate(imageId);
        if (!isExistImage) {
            throw new BusinessException(RCDC_CLOUDDESKTOP_IMAGE_NOT_EXIST, imageId.toString());
        }
    }

    protected void isImageStateInSync(CbbImageTemplateDetailDTO imageTemplateDetail) throws BusinessException {
        if (imageTemplateDetail.getImageState() == ImageTemplateState.SYNCING) {
            LOGGER.error("镜像[{}]状态已经处于同步中", imageTemplateDetail.getId());
            throw new BusinessException(BusinessKey.RCDC_RCO_IMAGE_STATE_IS_READY_SYNCING, imageTemplateDetail.getImageName());
        }
    }


    protected void isImageStateNotInSync(CbbImageTemplateDetailDTO imageTemplateDetail, String businessKey) throws BusinessException {
        if (imageTemplateDetail.getImageState() != ImageTemplateState.SYNCING) {
            LOGGER.error("镜像[{}]状态未处于同步中", imageTemplateDetail.getId());
            throw new BusinessException(businessKey, imageTemplateDetail.getImageName());
        }
    }


    protected void isImageStateNotInAvailable(CbbImageTemplateDetailDTO imageTemplateDetail) throws BusinessException {

        if (imageTemplateDetail.getImageState() != ImageTemplateState.AVAILABLE) {
            LOGGER.error("镜像[{}]状态不可用", imageTemplateDetail.getId());
            throw new BusinessException(BusinessKey.RCDC_RCO_IMAGE_STATE_NOT_AVAILABLE, imageTemplateDetail.getImageName());
        }
    }
}
