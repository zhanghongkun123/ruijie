package com.ruijie.rcos.rcdc.rco.module.web.ctrl.hardwarecertification.bactchtask;

import com.ruijie.rcos.gss.log.module.def.api.BaseAuditLogAPI;
import com.ruijie.rcos.rcdc.rco.module.def.api.UserHardwareCertificationAPI;
import com.ruijie.rcos.rcdc.rco.module.def.hardwarecertification.dto.RcoViewUserHardwareCertificationDTO;
import com.ruijie.rcos.rcdc.rco.module.web.service.SpringBeanHelper;
import com.ruijie.rcos.sk.base.batch.*;
import com.ruijie.rcos.sk.base.exception.BusinessException;
import com.ruijie.rcos.sk.base.log.Logger;
import com.ruijie.rcos.sk.base.log.LoggerFactory;
import org.springframework.util.Assert;

import java.util.Iterator;
import java.util.UUID;

import static com.ruijie.rcos.rcdc.rco.module.web.ctrl.hardwarecertification.HardwareCertificationBusinessKey.*;

/**
 * Description: 删除用户硬件特征码审批记录批量任务处理器
 * Copyright: Copyright (c) 2021
 * Company: Ruijie Co., Ltd.
 * Create Time: 2021年04月09日
 *
 * @author linke
 */
public class DeleteUserCertificationTaskHandler extends AbstractBatchTaskHandler {

    private static final Logger LOGGER = LoggerFactory.getLogger(DeleteUserCertificationTaskHandler.class);

    private BaseAuditLogAPI auditLogAPI;

    private UserHardwareCertificationAPI userHardwareCertificationAPI;

    public DeleteUserCertificationTaskHandler(Iterator<? extends BatchTaskItem> iterator, BaseAuditLogAPI auditLogAPI) {
        super(iterator);
        Assert.notNull(auditLogAPI, "the auditLogAPI is null.");
        this.auditLogAPI = auditLogAPI;
        this.userHardwareCertificationAPI = SpringBeanHelper.getBean(UserHardwareCertificationAPI.class);
    }

    @Override
    public BatchTaskItemResult processItem(BatchTaskItem batchTaskItem) throws BusinessException {
        Assert.notNull(batchTaskItem, "batchTaskItem can not be null");
        UUID id = batchTaskItem.getItemID();
        RcoViewUserHardwareCertificationDTO certification = userHardwareCertificationAPI.getById(id);
        String userName = certification.getUserName();
        String i18nFeatureCode = certification.getI18nFeatureCode();
        String upperCaseMac = certification.getUpperCaseMac();

        try {
            userHardwareCertificationAPI.deleteById(id);
            auditLogAPI.recordLog(RCDC_HARDWARE_CERTIFICATION_DELETE_SUCCESS_LOG, userName, upperCaseMac, i18nFeatureCode);
            return DefaultBatchTaskItemResult.builder().batchTaskItemStatus(BatchTaskItemStatus.SUCCESS)
                    .msgKey(RCDC_HARDWARE_CERTIFICATION_DELETE_SUCCESS_LOG)
                    .msgArgs(userName, upperCaseMac, i18nFeatureCode).build();
        } catch (Exception e) {
            LOGGER.error("删除用户硬件特征码审批记录异常，用户[{}]，MAC[{}]，特征码[{}]，异常：{}", userName, upperCaseMac, i18nFeatureCode, e);

            auditLogAPI.recordLog(RCDC_HARDWARE_CERTIFICATION_DELETE_FAIL, userName, upperCaseMac,  i18nFeatureCode);
            return DefaultBatchTaskItemResult.builder().batchTaskItemStatus(BatchTaskItemStatus.FAILURE)
                    .msgKey(RCDC_HARDWARE_CERTIFICATION_DELETE_FAIL)
                    .msgArgs(userName, upperCaseMac, i18nFeatureCode).build();
        }
    }

    @Override
    public BatchTaskFinishResult onFinish(int successCount, int failCount) {
        return buildDefaultFinishResult(successCount, failCount, RCDC_HARDWARE_CERTIFICATION_BATCH_DELETE_RESULT);
    }
}
