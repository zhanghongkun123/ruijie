package com.ruijie.rcos.rcdc.rco.module.web.ctrl.clouddesktop.batchtask;

import java.util.Iterator;
import java.util.UUID;

import com.ruijie.rcos.rcdc.rco.module.common.batch.I18nBatchTaskItem;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import com.ruijie.rcos.rcdc.appcenter.module.def.api.CbbAppDeliveryMgmtAPI;
import com.ruijie.rcos.rcdc.appcenter.module.def.api.CbbUamAppTestAPI;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.api.CbbDeskMgmtAPI;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.api.CbbImageDiskSnapshotMgmtAPI;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.api.CbbImageTemplateMgmtAPI;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.dto.CbbImageTemplateDetailDTO;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.dto.image.CbbDeleteImageTemplateTaskFlowDTO;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.dto.image.CbbRestoreImageTemplateDTO;
import com.ruijie.rcos.rcdc.rco.module.common.batch.AbstractI18nAwareBatchTaskHandler;
import com.ruijie.rcos.rcdc.rco.module.def.api.ImageExportAPI;
import com.ruijie.rcos.rcdc.rco.module.def.api.UamAppDiskAPI;
import com.ruijie.rcos.rcdc.rco.module.web.util.LockUtils;
import com.ruijie.rcos.sk.base.batch.BatchTaskItem;
import com.ruijie.rcos.sk.base.exception.BusinessException;

/**
 * Description: Function Description
 * Copyright: Copyright (c) 2023
 * Company: Ruijie Co., Ltd.
 * Create Time: 2023年07月27日
 *
 * @author xgx
 */
@Service
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
public class RollbackImageTemplateVersionHandler extends AbstractI18nAwareBatchTaskHandler<Void> {

    @Autowired
    private CbbImageTemplateMgmtAPI cbbImageTemplateMgmtAPI;

    @Autowired
    private CbbImageDiskSnapshotMgmtAPI cbbImageDiskSnapshotMgmtAPI;

    @Autowired
    private CbbDeskMgmtAPI cbbDeskMgmtAPI;

    @Autowired
    private ImageExportAPI imageExportAPI;

    @Autowired
    private CbbUamAppTestAPI cbbUamAppTestAPI;

    @Autowired
    private CbbAppDeliveryMgmtAPI cbbAppDeliveryMgmtAPI;

    @Autowired
    private UamAppDiskAPI uamAppDiskAPI;

    private UUID currentVersionId;

    private UUID preVersionId;

    protected RollbackImageTemplateVersionHandler(Iterator<? extends BatchTaskItem> batchTaskItemIterator, UUID currentVersionId, UUID preVersionId) {
        super(batchTaskItemIterator);
        Assert.notNull(currentVersionId, "currentVersionId can not be null");
        Assert.notNull(preVersionId, "preVersionId can not be null");

        this.preVersionId = preVersionId;
        this.currentVersionId = currentVersionId;
    }

    @Override
    protected void innerProcessItem(I18nBatchTaskItem<Void> item) throws BusinessException {
        UUID imageTemplateId = item.getItemID();
        LockUtils.tryExecute(preVersionId.toString(), () -> {
            // 校验是否允许删除版本
            cbbImageTemplateMgmtAPI.prepareDeleteImageTemplate(currentVersionId);
            // 是否与uam相关绑定
            checkUamBind();

            CbbImageTemplateDetailDTO preImageTemplateDetail = cbbImageTemplateMgmtAPI.getImageTemplateDetail(preVersionId);
            CbbRestoreImageTemplateDTO request = new CbbRestoreImageTemplateDTO();
            request.setImageId(imageTemplateId);
            request.setRestorePointId(preImageTemplateDetail.getSourceSnapshotId());

            cbbImageTemplateMgmtAPI.restoreImageTemplate(request);
            try {
                checkUamBind();

                CbbDeleteImageTemplateTaskFlowDTO deleteImageTemplateTaskFlowDTO = new CbbDeleteImageTemplateTaskFlowDTO();
                deleteImageTemplateTaskFlowDTO.setImageTemplateId(currentVersionId);
                cbbImageTemplateMgmtAPI.deleteImageTemplate(deleteImageTemplateTaskFlowDTO);
            } catch (BusinessException e) {
                CbbImageTemplateDetailDTO currentImageTemplateDetail = cbbImageTemplateMgmtAPI.getImageTemplateDetail(currentVersionId);
                CbbRestoreImageTemplateDTO rollbackRequest = new CbbRestoreImageTemplateDTO();
                rollbackRequest.setImageId(imageTemplateId);
                rollbackRequest.setRestorePointId(currentImageTemplateDetail.getSourceSnapshotId());
                cbbImageTemplateMgmtAPI.restoreImageTemplate(rollbackRequest);
                throw e;
            }

            //
            return null;
        });

    }

    private void checkUamBind() throws BusinessException {
        cbbUamAppTestAPI.isExistRelateAppTestByImageTemplateIdThrowEx(currentVersionId);
        cbbAppDeliveryMgmtAPI.isExistRelateDeliveryGroupByImageTemplateIdThrowEx(currentVersionId);
        uamAppDiskAPI.isExistRelateAppByImageIdThrowEx(currentVersionId);
    }
}
