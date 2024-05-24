package com.ruijie.rcos.rcdc.rco.module.web.ctrl.clouddesktop.batchtask;

import java.util.Iterator;

import org.springframework.util.Assert;

import com.ruijie.rcos.rcdc.clouddesktop.module.def.api.CbbImageTemplateMgmtAPI;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.dto.image.CbbDeleteImageTemplateTaskFlowDTO;
import com.ruijie.rcos.rcdc.rco.module.common.batch.AbstractI18nAwareBatchTaskHandler;
import com.ruijie.rcos.rcdc.rco.module.common.batch.I18nBatchTaskItem;
import com.ruijie.rcos.rcdc.rco.module.web.util.LockUtils;
import com.ruijie.rcos.sk.base.exception.BusinessException;
import com.ruijie.rcos.sk.base.log.Logger;
import com.ruijie.rcos.sk.base.log.LoggerFactory;

/**
 * Description: 删除安装包批量handler
 * Copyright: Copyright (c) 2019
 * Company: Ruijie Co., Ltd.
 * Create Time: 2019年1月17日
 *
 * @author Ghang
 */
public class DeleteImageTemplateBatchHandler extends AbstractI18nAwareBatchTaskHandler<Void> {

    protected static final Logger LOGGER = LoggerFactory.getLogger(DeleteImageTemplateBatchHandler.class);

    private CbbImageTemplateMgmtAPI cbbImageTemplateMgmtAPI;

    private Boolean shouldOnlyDeleteDataFromDb;

    public DeleteImageTemplateBatchHandler(CbbImageTemplateMgmtAPI cbbImageTemplateMgmtAPI, Iterator<? extends I18nBatchTaskItem<?>> iterator,
            Boolean shouldForceDelete) {
        super(iterator);
        Assert.notNull(cbbImageTemplateMgmtAPI, "cbbImageTemplateMgmtAPI is not null");
        this.cbbImageTemplateMgmtAPI = cbbImageTemplateMgmtAPI;
        this.shouldOnlyDeleteDataFromDb = shouldForceDelete;
    }

    @Override
    protected void innerProcessItem(I18nBatchTaskItem<Void> item) throws BusinessException {
        if (Boolean.TRUE.equals(shouldOnlyDeleteDataFromDb)) {
            LOGGER.info("管理发起强制删除镜像模板[{}]数据库数据", item.getItemID());
            cbbImageTemplateMgmtAPI.deleteImageTemplateFormDb(item.getItemID());
            return;
        }
        LockUtils.tryExecute(item.getItemID().toString(), () -> {
            CbbDeleteImageTemplateTaskFlowDTO deleteImageTemplateTaskFlowDTO = new CbbDeleteImageTemplateTaskFlowDTO();
            deleteImageTemplateTaskFlowDTO.setImageTemplateId(item.getItemID());
            deleteImageTemplateTaskFlowDTO.setRunAsync(false);
            cbbImageTemplateMgmtAPI.deleteImageTemplate(deleteImageTemplateTaskFlowDTO);
            // 删除后逻辑迁移到RcoImageTemplateEventSPIImpl实现
            return null;
        });
    }

}
