package com.ruijie.rcos.rcdc.rco.module.impl.service;


import com.ruijie.rcos.rcdc.appcenter.module.def.api.CbbAppSoftwarePackageMgmtAPI;
import com.ruijie.rcos.rcdc.appcenter.module.def.dto.AppSoftwarePackageDTO;
import com.ruijie.rcos.rcdc.appcenter.module.def.dto.request.CbbSearchObtainVmAppSoftwarePackageDTO;
import com.ruijie.rcos.rcdc.rco.module.def.BusinessKey;
import com.ruijie.rcos.sk.base.exception.BusinessException;
import com.ruijie.rcos.sk.base.log.Logger;
import com.ruijie.rcos.sk.base.log.LoggerFactory;
import com.ruijie.rcos.sk.base.util.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

/**
 * Description: UamApp 服务类
 * Copyright: Copyright (c) 2018
 * Company: Ruijie Co., Ltd.
 * Create Time: 2023/5/17
 *
 * @author zhiweiHong
 */
@Service
public class UamAppService {

    @Autowired
    private CbbAppSoftwarePackageMgmtAPI appSoftPackageMgmtAPI;

    private static final String DELIMITER = ",";

    private static final Logger LOGGER = LoggerFactory.getLogger(UamAppService.class);

    /**
     * 查询是否有临时虚机正在运行,如果有抛出异常
     *
     * @param imageId 镜像Id
     * @throws BusinessException 业务异常
     */
    public void isExistTempVmRunningThrowEx(UUID imageId) throws BusinessException {
        Assert.notNull(imageId, "imageId can not be null");

        CbbSearchObtainVmAppSoftwarePackageDTO vmAppSoftwarePackageDTO = new CbbSearchObtainVmAppSoftwarePackageDTO();
        vmAppSoftwarePackageDTO.setImageTemplateId(imageId);
        List<AppSoftwarePackageDTO> appSoftwarePackageDTOList = appSoftPackageMgmtAPI.searchObtainVmAppSoftwarePackage(vmAppSoftwarePackageDTO);

        String editingAppListStr = getRelateAppStr(appSoftwarePackageDTOList);
        if (StringUtils.hasText(editingAppListStr)) {
            LOGGER.error("镜像[{}]存在临时虚机处于运行状态,应用为:{}", imageId, editingAppListStr);
            throw new BusinessException(BusinessKey.RCDC_RCO_IMAGE_HAS_UAM_TEMP_VM, editingAppListStr);
        }
    }

    /**
     * 查询是否有关联应用,如果有抛出异常
     *
     * @param imageId 镜像Id
     * @throws BusinessException 业务异常
     */
    public void isExistRelateAppByImageIdThrowEx(UUID imageId) throws BusinessException {
        Assert.notNull(imageId, "imageId can not be null");

        List<AppSoftwarePackageDTO> appSoftwareList = appSoftPackageMgmtAPI.listAppByImageId(imageId);
        String editingAppListStr = getRelateAppStr(appSoftwareList);
        if (StringUtils.hasText(editingAppListStr)) {
            LOGGER.error("镜像[{}]关联应用,应用为:{}", imageId, editingAppListStr);
            throw new BusinessException(BusinessKey.RCDC_RCO_IMAGE_HAS_UAM_APP, editingAppListStr);
        }
    }

    /**
     * 查询是否有关联网络策略,如果有抛出异常
     *
     * @param networkId 网络ID
     * @throws BusinessException 业务异常
     */
    public void isExistRelateAppByNetworkIdThrowEx(UUID networkId) throws BusinessException {
        Assert.notNull(networkId, "networkId can not be null");
        List<AppSoftwarePackageDTO> appSoftwareList = appSoftPackageMgmtAPI.listAppByNetworkId(networkId);
        String editingAppListStr = getRelateAppStr(appSoftwareList);
        if (StringUtils.hasText(editingAppListStr)) {
            LOGGER.error("网络策略[{}]关联应用,应用为:{}", networkId, editingAppListStr);
            throw new BusinessException(BusinessKey.RCDC_RCO_NETWORK_HAS_UAM_APP, editingAppListStr);
        }
    }

    private String getRelateAppStr(List<AppSoftwarePackageDTO> appSoftwarePackageDTOList) {
        return appSoftwarePackageDTOList.stream()
                .map(AppSoftwarePackageDTO::getName)
                .collect(Collectors.joining(DELIMITER));

    }
}
