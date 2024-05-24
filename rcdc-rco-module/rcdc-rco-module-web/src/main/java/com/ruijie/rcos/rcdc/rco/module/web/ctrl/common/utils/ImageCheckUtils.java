package com.ruijie.rcos.rcdc.rco.module.web.ctrl.common.utils;

import com.ruijie.rcos.rcdc.clouddesktop.module.def.api.CbbImageTemplateMgmtAPI;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.dto.image.CbbGetImageTemplateInfoDTO;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.enums.ImageRoleType;
import com.ruijie.rcos.rcdc.hciadapter.module.def.dto.PlatformStoragePoolDTO;
import com.ruijie.rcos.rcdc.hciadapter.module.def.enums.StoragePoolType;
import com.ruijie.rcos.rcdc.rco.module.def.api.StoragePoolAPI;
import com.ruijie.rcos.sk.base.exception.BusinessException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

import static com.ruijie.rcos.rcdc.clouddesktop.module.def.BusinessKey.RCDC_IMAGE_SINGLE_IMAGE_NOT_SUPPORT;

/**
 * Description: 镜像检查工具累
 * Copyright: Copyright (c) 2018
 * Company: Ruijie Co., Ltd.
 * Create Time: 2024/4/7
 *
 * @author zhiweiHong
 */
@Service
public class ImageCheckUtils {

    @Autowired
    private CbbImageTemplateMgmtAPI imageTemplateMgmtAPI;

    @Autowired
    private StoragePoolAPI storagePoolAPI;


    /**
     * 检查单版本镜像是否跨存储同步
     *
     * @param imageId        imageId
     * @param targeStorageId targeStorageId
     * @throws BusinessException BusinessException
     */
    public void checkForSingleImageSync(UUID imageId,
                                        UUID targeStorageId) throws BusinessException {
        Assert.notNull(imageId, "imageId can not be null");
        Assert.notNull(targeStorageId, "targeStorageId can not be null");
        Map<UUID, PlatformStoragePoolDTO> storagePoolDTOMap = storagePoolAPI.queryAllStoragePool()
                .stream().collect(Collectors.toMap(PlatformStoragePoolDTO::getId, v -> v, (v1, v2) -> v2));
        Boolean isSupportImageSync = isSingleImageNotSupportSync(imageId, targeStorageId, storagePoolDTOMap);

        if (isSupportImageSync) {
            CbbGetImageTemplateInfoDTO imageTemplateDetail = imageTemplateMgmtAPI.getImageTemplateInfo(imageId);
            throw new BusinessException(RCDC_IMAGE_SINGLE_IMAGE_NOT_SUPPORT, imageTemplateDetail.getImageName());
        }
    }

    /**
     * 是否是单版本镜像执行镜像同步
     *
     * @param imageId           imageId
     * @param targeStorageId    targeStorageId
     * @param storagePoolDTOMap storagePoolDTOMap
     * @return true
     * @throws BusinessException BusinessException
     */
    public Boolean isSingleImageNotSupportSync(UUID imageId,
                                               UUID targeStorageId,
                                               Map<UUID, PlatformStoragePoolDTO> storagePoolDTOMap) throws BusinessException {
        Assert.notNull(imageId, "imageId can not be null");
        Assert.notNull(targeStorageId, "targeStorageId can not be null");
        Assert.notNull(storagePoolDTOMap, "storagePoolDTOMap can not be null");

        CbbGetImageTemplateInfoDTO imageTemplateDetail = imageTemplateMgmtAPI.getImageTemplateInfo(imageId);
        // 不支持场景汇总：
        // 第一种场景，单版本镜像和存储所在平台不一致，不允许选择。
        // 第二种场景，单版本镜像的存储位于外置存储，和传入的response不在一个外置存储。
        // 第三种场景，单版本镜像和存储类型不一致，比如镜像位于POS，而目标存储位于外置。
        boolean isSingleVersionImage = Boolean.FALSE.equals(imageTemplateDetail.getEnableMultipleVersion())
                && imageTemplateDetail.getImageRoleType() == ImageRoleType.TEMPLATE;

        PlatformStoragePoolDTO targetStoragePool = storagePoolDTOMap.get(targeStorageId);
        PlatformStoragePoolDTO sourceStoragePool = storagePoolDTOMap.get(imageTemplateDetail.getStoragePoolId());
        boolean isPlatformDiff = !imageTemplateDetail.getPlatformId().equals(targetStoragePool.getPlatformId());
        boolean isAllInExternalStorageWithDiffId = isAllInExternalStorageWithDiffId(targetStoragePool, sourceStoragePool);
        boolean isStorageTypeNotEq = isStorageTypeNotEq(targetStoragePool, sourceStoragePool);
        return isSingleVersionImage && (
                isPlatformDiff || isAllInExternalStorageWithDiffId || isStorageTypeNotEq);
    }

    /**
     * 镜像编辑时是否允许变更存储
     *
     * @param imageId           imageId
     * @param targeStorageId    targeStorageId
     * @param storagePoolDTOMap storagePoolDTOMap
     * @return true
     * @throws BusinessException BusinessException
     */
    public Boolean isImageEditNotSupportChangeStorage(UUID imageId,
                                                      UUID targeStorageId,
                                                      Map<UUID, PlatformStoragePoolDTO> storagePoolDTOMap) throws BusinessException {
        Assert.notNull(imageId, "imageId can not be null");
        Assert.notNull(targeStorageId, "targeStorageId can not be null");
        Assert.notNull(storagePoolDTOMap, "storagePoolDTOMap can not be null");
        CbbGetImageTemplateInfoDTO imageTemplateDetail = imageTemplateMgmtAPI.getImageTemplateInfo(imageId);
        PlatformStoragePoolDTO targetStoragePool = storagePoolDTOMap.get(targeStorageId);
        PlatformStoragePoolDTO sourceStoragePool = storagePoolDTOMap.get(imageTemplateDetail.getStoragePoolId());
        boolean isAllInExternalStorageWithDiffId = isAllInExternalStorageWithDiffId(targetStoragePool, sourceStoragePool);
        boolean isStorageTypeNotEq = isStorageTypeNotEq(targetStoragePool, sourceStoragePool);
        return isAllInExternalStorageWithDiffId || isStorageTypeNotEq;
    }

    private boolean isStorageTypeNotEq(PlatformStoragePoolDTO targetStoragePool, PlatformStoragePoolDTO sourceStoragePool) {
        return sourceStoragePool.getStoragePoolType() != targetStoragePool.getStoragePoolType();
    }

    private boolean isAllInExternalStorageWithDiffId(PlatformStoragePoolDTO targetStoragePool, PlatformStoragePoolDTO sourceStoragePool) {
        return sourceStoragePool.getStoragePoolType() != StoragePoolType.RG_PDS
                && targetStoragePool.getStoragePoolType() != StoragePoolType.RG_PDS
                && !targetStoragePool.getId().equals(sourceStoragePool.getId());
    }
}
