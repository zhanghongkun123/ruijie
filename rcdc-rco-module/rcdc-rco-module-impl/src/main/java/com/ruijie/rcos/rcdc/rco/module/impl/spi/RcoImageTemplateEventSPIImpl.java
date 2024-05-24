package com.ruijie.rcos.rcdc.rco.module.impl.spi;

import com.ruijie.rcos.rcdc.clouddesktop.module.def.api.CbbDeskMgmtAPI;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.spi.CbbImageTemplateEventSPI;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.spi.request.CbbImageTemplateDeleteEventRequest;
import com.ruijie.rcos.rcdc.rca.module.def.api.RcaHostAPI;
import com.ruijie.rcos.rcdc.rco.module.def.api.AdminDataPermissionAPI;
import com.ruijie.rcos.rcdc.rco.module.def.api.CmsUpgradeAPI;
import com.ruijie.rcos.rcdc.rco.module.def.api.ImageExportAPI;
import com.ruijie.rcos.rcdc.rco.module.def.api.enums.rccm.UnifiedManageFunctionKeyEnum;
import com.ruijie.rcos.rcdc.rco.module.def.api.request.rccm.UnifiedManageDataRequest;
import com.ruijie.rcos.rcdc.rco.module.def.imageexport.dto.ViewImageExportDTO;
import com.ruijie.rcos.rcdc.rco.module.impl.rccm.service.UnifiedManageDataService;
import com.ruijie.rcos.sk.base.exception.BusinessException;
import com.ruijie.rcos.sk.base.log.Logger;
import com.ruijie.rcos.sk.base.log.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.Assert;
import org.springframework.util.CollectionUtils;

import java.util.List;
import java.util.UUID;

/**
 * Description:
 * Copyright: Copyright (c)
 * Company: Ruijie Co., Ltd.
 * Create Time: 2023/6/28 17:20
 *
 * @author zhangsiming
 */
public class RcoImageTemplateEventSPIImpl implements CbbImageTemplateEventSPI {

    private static final Logger LOGGER = LoggerFactory.getLogger(RcoImageTemplateEventSPIImpl.class);

    @Autowired
    private ImageExportAPI imageExportAPI;

    @Autowired
    private AdminDataPermissionAPI adminDataPermissionAPI;

    @Autowired
    private CmsUpgradeAPI cmsUpgradeAPI;

    @Autowired
    private CbbDeskMgmtAPI cbbDeskMgmtAPI;

    @Autowired
    private UnifiedManageDataService unifiedManageDataService;

    @Autowired
    private RcaHostAPI rcaHostAPI;

    @Override
    public void onDeleteEvent(CbbImageTemplateDeleteEventRequest deleteEventRequest) throws BusinessException {
        Assert.notNull(deleteEventRequest, "deleteEventRequest can not be null");

        UUID imageId = deleteEventRequest.getImageTemplateId();
        LOGGER.info("收到CBB镜像[{}]删除消息", imageId);
        // 导出文件与镜像独立，不影响导出文件删除，避免出现镜像不可删除但导出文件被删除
        List<ViewImageExportDTO> exportImageList = imageExportAPI.getExportImageByTemplateId(imageId);
        if (!CollectionUtils.isEmpty(exportImageList)) {
            for (ViewImageExportDTO exportDTO : exportImageList) {
                imageExportAPI.deleteExportImage(exportDTO.getId());
            }
        }

        // 根据镜像ID 删除权限
        adminDataPermissionAPI.deleteByPermissionDataId(imageId.toString());
        cmsUpgradeAPI.deleteCmIsoRecord(imageId);

        //清空待变更的镜像模板的云桌面
        cbbDeskMgmtAPI.clearAllWillApplyImageId(imageId);

        // 删除统一管理数据
        unifiedManageDataService.deleteUnifiedManage(new UnifiedManageDataRequest(imageId, UnifiedManageFunctionKeyEnum.IMAGE_TEMPLATE));

        // 清除应用镜像对应的应用信息
        rcaHostAPI.removeImageHost(imageId);

    }

}
