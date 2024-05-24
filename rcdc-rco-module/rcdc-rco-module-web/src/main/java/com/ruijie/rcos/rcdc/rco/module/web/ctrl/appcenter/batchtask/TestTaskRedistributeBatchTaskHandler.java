package com.ruijie.rcos.rcdc.rco.module.web.ctrl.appcenter.batchtask;

import java.util.Collection;

import com.ruijie.rcos.gss.sdk.iac.module.def.enums.IacUserTypeEnum;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;
import org.springframework.util.ObjectUtils;

import com.ruijie.rcos.gss.log.module.def.api.BaseAuditLogAPI;
import com.ruijie.rcos.rcdc.appcenter.module.def.api.CbbUamAppTestAPI;
import com.ruijie.rcos.rcdc.appcenter.module.def.api.CbbUamAppTestTaskAPI;
import com.ruijie.rcos.rcdc.appcenter.module.def.dto.CbbEnterAppTestDTO;
import com.ruijie.rcos.rcdc.appcenter.module.def.dto.CbbUamAppTestDTO;
import com.ruijie.rcos.rcdc.appcenter.module.def.enums.AppResourceTypeEnum;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.api.CbbDeskMgmtAPI;
import com.ruijie.rcos.rcdc.rco.module.def.api.UserDesktopMgmtAPI;
import com.ruijie.rcos.rcdc.rco.module.def.api.dto.CloudDesktopDetailDTO;
import com.ruijie.rcos.rcdc.rco.module.web.ctrl.appcenter.key.UamDeliveryObjectBusinessKey;
import com.ruijie.rcos.sk.base.batch.AbstractBatchTaskHandler;
import com.ruijie.rcos.sk.base.batch.BatchTaskFinishResult;
import com.ruijie.rcos.sk.base.batch.BatchTaskItem;
import com.ruijie.rcos.sk.base.batch.BatchTaskItemResult;
import com.ruijie.rcos.sk.base.batch.BatchTaskItemStatus;
import com.ruijie.rcos.sk.base.batch.DefaultBatchTaskItemResult;
import com.ruijie.rcos.sk.base.exception.BusinessException;

/**
 * Description: Function Description
 * Copyright: Copyright (c) 2023
 * Company: Ruijie Co., Ltd.
 * Create Time: 2023年04月10日
 *
 * @author xgx
 */
@Service
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
public class TestTaskRedistributeBatchTaskHandler extends AbstractBatchTaskHandler {
    @Autowired
    private BaseAuditLogAPI auditLogAPI;

    @Autowired
    private CbbDeskMgmtAPI cbbDeskMgmtAPI;

    private CbbUamAppTestDTO uamAppTestInfo;

    @Autowired
    private CbbUamAppTestTaskAPI cbbUamAppTestTaskAPI;

    @Autowired
    private UserDesktopMgmtAPI userDesktopMgmtAPI;

    @Autowired
    private CbbUamAppTestAPI cbbUamAppTestAPI;

    protected TestTaskRedistributeBatchTaskHandler(Collection<EnterTestDesktopTaskItem> batchTaskItemCollection, CbbUamAppTestDTO uamAppTestInfo) {
        super(batchTaskItemCollection);
        Assert.notNull(uamAppTestInfo, "uamAppTestInfo can not be null");
        this.uamAppTestInfo = uamAppTestInfo;
    }

    @Override
    public BatchTaskItemResult processItem(BatchTaskItem batchTaskItem) throws BusinessException {
        Assert.notNull(batchTaskItem, "batchTaskItem can not be null");
        CloudDesktopDetailDTO desktopDetailDTO = null;
        try {
            EnterTestDesktopTaskItem enterTestDeskTopTaskItem = (EnterTestDesktopTaskItem) batchTaskItem;
            desktopDetailDTO = userDesktopMgmtAPI.getDesktopDetailById(enterTestDeskTopTaskItem.getItemID());

            IacUserTypeEnum userType = desktopDetailDTO.getUserType() == null ? //
                    IacUserTypeEnum.VISITOR : IacUserTypeEnum.valueOf(desktopDetailDTO.getUserType());

            CbbEnterAppTestDTO request = new CbbEnterAppTestDTO();
            request.setResourceType(AppResourceTypeEnum.CLOUD_DESKTOP);
            request.setResourceId(enterTestDeskTopTaskItem.getItemID());
            request.setTestId(enterTestDeskTopTaskItem.getTestId());
            request.setAdminId(enterTestDeskTopTaskItem.getAdminId());
            request.setDeskName(enterTestDeskTopTaskItem.getItemName());
            request.setUserType(userType);

            cbbUamAppTestTaskAPI.reDeliveryApp(request);
            auditLogAPI.recordLog(UamDeliveryObjectBusinessKey.RCDC_UAM_APP_TEST_REDISTRIBUTE_SUCCESS_LOG, uamAppTestInfo.getAppDisk(),
                    desktopDetailDTO.getDesktopName());
            return DefaultBatchTaskItemResult.builder().batchTaskItemStatus(BatchTaskItemStatus.SUCCESS)
                    .msgKey(UamDeliveryObjectBusinessKey.RCDC_UAM_APP_TEST_REDISTRIBUTE_SINGLE_SUCCESS_RESULT)
                    .msgArgs(uamAppTestInfo.getAppDisk(), desktopDetailDTO.getDesktopName()).build();
        } catch (BusinessException ex) {
            String deskName = ObjectUtils.isEmpty(desktopDetailDTO) ? batchTaskItem.getItemID().toString() : desktopDetailDTO.getDesktopName();
            auditLogAPI.recordLog(UamDeliveryObjectBusinessKey.RCDC_UAM_APP_TEST_REDISTRIBUTE_FAIL_LOG, uamAppTestInfo.getAppDisk(), deskName,
                    ex.getI18nMessage());
            throw new BusinessException(UamDeliveryObjectBusinessKey.RCDC_UAM_APP_TEST_REDISTRIBUTE_SINGLE_FAIL_RESULT, ex,
                    uamAppTestInfo.getAppDisk(), deskName, ex.getI18nMessage());
        }
    }

    @Override
    public BatchTaskFinishResult onFinish(int successCount, int failureCount) {
        return buildDefaultFinishResult(successCount, failureCount, UamDeliveryObjectBusinessKey.RCDC_UAM_APP_TEST_REDISTRIBUTE_SUCCESS_RESULT);

    }
}
