package com.ruijie.rcos.rcdc.rco.module.web.ctrl.imagetemplate.batchtask;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import com.ruijie.rcos.rcdc.clouddesktop.module.def.api.CbbImageDriverMgmtAPI;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.api.request.image.CbbImageCdromDTO;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.dto.CbbImageTemplateDetailDTO;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.enums.OsPlatform;
import com.ruijie.rcos.rcdc.rco.module.def.api.CmsUpgradeAPI;
import com.ruijie.rcos.sk.base.batch.AbstractBatchTaskHandler;
import com.ruijie.rcos.sk.base.batch.BatchTaskItem;
import com.ruijie.rcos.sk.base.exception.BusinessException;
import com.ruijie.rcos.sk.base.log.Logger;
import com.ruijie.rcos.sk.base.log.LoggerFactory;

/**
 * Description: Function Description
 * Copyright: Copyright (c) 2023
 * Company: Ruijie Co., Ltd.
 * Create Time: 2023.03.27
 *
 * @author linhj
 */
public abstract class AbstractRunVmForEditImageHandler extends AbstractBatchTaskHandler {

    private static final Logger LOGGER = LoggerFactory.getLogger(AbstractRunVmForEditImageHandler.class);

    private final CbbImageDriverMgmtAPI cbbImageDriverMgmtAPI;

    private final CmsUpgradeAPI cmsUpgradeAPI;

    protected AbstractRunVmForEditImageHandler(Iterator<? extends BatchTaskItem> batchTaskItemCollection, CmsUpgradeAPI cmsUpgradeAPI,
            CbbImageDriverMgmtAPI cbbImageDriverMgmtAPI) {
        super(batchTaskItemCollection);
        this.cmsUpgradeAPI = cmsUpgradeAPI;
        this.cbbImageDriverMgmtAPI = cbbImageDriverMgmtAPI;
    }

    /**
     * 封装 CDROM
     *
     * @return 列表
     */
    protected List<CbbImageCdromDTO> wrapperCdromDTO(CbbImageTemplateDetailDTO cbbImageTemplateDetailDTO) throws BusinessException {

        List<CbbImageCdromDTO> cdromList = new ArrayList<>();
        if (cbbImageTemplateDetailDTO.getOsType() != null && cbbImageTemplateDetailDTO.getOsType().getOsPlatform() != OsPlatform.LINUX) {
            cdromList.addAll(cmsUpgradeAPI.getCmIso(cbbImageTemplateDetailDTO.getId(), cbbImageTemplateDetailDTO.getOsType()));
        }

        return cdromList;
    }
}
