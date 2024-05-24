package com.ruijie.rcos.rcdc.rco.module.impl.spi;

import com.ruijie.rcos.rcdc.clouddesktop.module.def.api.CbbImageDriverMgmtAPI;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.api.CbbImageTemplateMgmtAPI;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.api.request.image.CbbImageCdromDTO;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.dto.CbbImageDriverDTO;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.dto.CbbImageTemplateDetailDTO;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.enums.CbbImageType;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.enums.CbbOsType;
import com.ruijie.rcos.rcdc.hciadapter.module.def.enums.ProtocolType;
import com.ruijie.rcos.rcdc.maintenance.module.def.spi.CbbBusinessMaintenanceImageCdromSPI;
import com.ruijie.rcos.rcdc.rco.module.def.api.CmsUpgradeAPI;
import com.ruijie.rcos.sk.base.log.Logger;
import com.ruijie.rcos.sk.base.log.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.Assert;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * Description:
 * Copyright: Copyright (c) 2022
 * Company: Ruijie Co., Ltd.
 * Create Time: 2022/7/4
 *
 * @author zhangsiming
 */
public class ImageCdromSPIImpl implements CbbBusinessMaintenanceImageCdromSPI {

    private static final Logger LOGGER = LoggerFactory.getLogger(ImageCdromSPIImpl.class);

    @Autowired
    private CmsUpgradeAPI cmsUpgradeAPI;

    @Autowired
    private CbbImageTemplateMgmtAPI cbbImageTemplateMgmtAPI;

    @Autowired
    private CbbImageDriverMgmtAPI cbbImageDriverMgmtAPI;

    @Override
    public List<CbbImageCdromDTO> listImageCdrom(UUID imageTemplateId, CbbOsType osType) {
        Assert.notNull(imageTemplateId, "imageTemplatId cannot be null");
        Assert.notNull(osType, "osType cannot be null");

        return cmsUpgradeAPI.getCmIso(imageTemplateId, osType);
    }

    @Override
    public boolean setCmIsoRecord(UUID imageTemplateId) {
        Assert.notNull(imageTemplateId, "imageTemplatId cannot be null");
        cmsUpgradeAPI.setCmIsoRecord(imageTemplateId);
        return true;
    }
}
