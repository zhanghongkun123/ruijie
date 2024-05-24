package com.ruijie.rcos.rcdc.rco.module.web.ctrl.appcenter.batchtask;

import com.ruijie.rcos.gss.log.module.def.api.BaseAuditLogAPI;
import com.ruijie.rcos.rcdc.appcenter.module.def.CbbAppCenterBusinessKey;
import com.ruijie.rcos.rcdc.appcenter.module.def.api.CbbAppDeliveryMgmtAPI;
import com.ruijie.rcos.rcdc.appcenter.module.def.dto.CbbUamDeliveryGroupDetailDTO;
import com.ruijie.rcos.rcdc.appcenter.module.def.dto.request.CbbDeleteDeliveryObjectDTO;
import com.ruijie.rcos.rcdc.rco.module.def.api.AppDeliveryMgmtAPI;
import com.ruijie.rcos.rcdc.rco.module.def.api.enums.AdminDataPermissionType;
import com.ruijie.rcos.rcdc.rco.module.def.appcenter.dto.UamDeliveryObjectDTO;
import com.ruijie.rcos.rcdc.rco.module.web.ctrl.appcenter.key.UamDeliveryObjectBusinessKey;
import com.ruijie.rcos.rcdc.rco.module.web.service.GeneralPermissionHelper;
import com.ruijie.rcos.sk.base.batch.*;
import com.ruijie.rcos.sk.base.exception.BusinessException;
import com.ruijie.rcos.sk.base.log.Logger;
import com.ruijie.rcos.sk.base.log.LoggerFactory;
import com.ruijie.rcos.sk.webmvc.api.session.SessionContext;
import org.springframework.beans.BeanUtils;
import org.springframework.util.Assert;

import java.util.Iterator;
import java.util.UUID;

/**
 * Description:
 * Copyright: Copyright (c)
 * Company: Ruijie Co., Ltd.
 * Create Time: 2022/12/29 11:53
 *
 * @author coderLee23
 */
public class DeleteUamDeliveryObjectBatchTaskHandler extends AbstractBatchTaskHandler {

    private static final Logger LOGGER = LoggerFactory.getLogger(DeleteUamDeliveryObjectBatchTaskHandler.class);

    private BaseAuditLogAPI auditLogAPI;

    private CbbAppDeliveryMgmtAPI cbbAppDeliveryMgmtAPI;

    private AppDeliveryMgmtAPI appDeliveryMgmtAPI;

    private GeneralPermissionHelper generalPermissionHelper;

    private SessionContext sessionContext;

    private String cloudDesktopName;

    private boolean batchFlag = true;

    public DeleteUamDeliveryObjectBatchTaskHandler(Iterator<? extends BatchTaskItem> batchTaskItemIterator) {
        super(batchTaskItemIterator);
    }

    public void setAuditLogAPI(BaseAuditLogAPI auditLogAPI) {
        this.auditLogAPI = auditLogAPI;
    }

    public void setCbbAppDeliveryMgmtAPI(CbbAppDeliveryMgmtAPI cbbAppDeliveryMgmtAPI) {
        this.cbbAppDeliveryMgmtAPI = cbbAppDeliveryMgmtAPI;
    }

    public void setAppDeliveryMgmtAPI(AppDeliveryMgmtAPI appDeliveryMgmtAPI) {
        this.appDeliveryMgmtAPI = appDeliveryMgmtAPI;
    }

    public void setAppCenterPermissionHelper(GeneralPermissionHelper generalPermissionHelper) {
        this.generalPermissionHelper = generalPermissionHelper;
    }

    public void setSessionContext(SessionContext sessionContext) {
        this.sessionContext = sessionContext;
    }

    public void setCloudDesktopName(String cloudDesktopName) {
        this.cloudDesktopName = cloudDesktopName;
    }

    public void setBatchFlag(boolean batchFlag) {
        this.batchFlag = batchFlag;
    }


    @Override
    public BatchTaskItemResult processItem(BatchTaskItem batchTaskItem) throws BusinessException {
        Assert.notNull(batchTaskItem, "batchTaskItem cannot be null");
        UUID id = batchTaskItem.getItemID();
        String desktopName = null;
        try {
            UamDeliveryObjectDTO uamDeliveryObject = appDeliveryMgmtAPI.findDeliveryObjectById(id);
            desktopName = uamDeliveryObject.getCloudDesktopName();
            // 判定交付组是否存在,需要每次获取，添加时候有可能更新
            CbbUamDeliveryGroupDetailDTO cbbUamDeliveryGroupDetail =
                    cbbAppDeliveryMgmtAPI.getCbbUamDeliveryGroupDetail(uamDeliveryObject.getDeliveryGroupId());

            // 校验是否有操作该记录权限
            generalPermissionHelper.checkPermission(sessionContext, uamDeliveryObject.getDeliveryGroupId(), AdminDataPermissionType.DELIVERY_GROUP);

            CbbDeleteDeliveryObjectDTO cbbDeleteDeliveryObjectDTO = new CbbDeleteDeliveryObjectDTO();
            BeanUtils.copyProperties(uamDeliveryObject, cbbDeleteDeliveryObjectDTO);
            cbbDeleteDeliveryObjectDTO.setAppDeliveryType(cbbUamDeliveryGroupDetail.getAppDeliveryType());

            cbbAppDeliveryMgmtAPI.deleteUamDeliveryObject(cbbDeleteDeliveryObjectDTO);
            LOGGER.info("删除交付对象[{}]成功", desktopName);
            auditLogAPI.recordLog(UamDeliveryObjectBusinessKey.RCDC_UAM_DELIVERY_OBJECT_DELETE_SUCCESS_LOG, desktopName);
            return DefaultBatchTaskItemResult.builder().batchTaskItemStatus(BatchTaskItemStatus.SUCCESS)
                    .msgKey(UamDeliveryObjectBusinessKey.RCDC_UAM_DELIVERY_OBJECT_DELETE_SUCCESS_LOG).msgArgs(desktopName).build();
        } catch (BusinessException e) {
            LOGGER.error("删除交付对象[{}]失败，失败原因：", desktopName, e);

            if (CbbAppCenterBusinessKey.RCDC_UAM_DELIVERY_OBJECT_NOT_EXIST.equals(e.getKey())) {
                auditLogAPI.recordLog(UamDeliveryObjectBusinessKey.RCDC_UAM_DELIVERY_OBJECT_NOT_EXISTS_DELETE_FAIL_LOG, e.getI18nMessage());
                throw new BusinessException(UamDeliveryObjectBusinessKey.RCDC_UAM_DELIVERY_OBJECT_NOT_EXISTS_DELETE_FAIL_LOG, e, e.getI18nMessage());
            }


            auditLogAPI.recordLog(UamDeliveryObjectBusinessKey.RCDC_UAM_DELIVERY_OBJECT_DELETE_FAIL_LOG, desktopName, e.getI18nMessage());
            throw new BusinessException(UamDeliveryObjectBusinessKey.RCDC_UAM_DELIVERY_OBJECT_DELETE_FAIL_LOG, e, desktopName, e.getI18nMessage());
        }

    }

    @Override
    public BatchTaskFinishResult onFinish(int successCount, int failCount) {
        // 批量删除返回结果
        if (batchFlag) {
            return buildDefaultFinishResult(successCount, failCount, UamDeliveryObjectBusinessKey.RCDC_UAM_DELIVERY_OBJECT_DELETE_SUCCESS_RESULT);
        }

        // 删除单个应用
        if (successCount == 1) {
            return DefaultBatchTaskFinishResult.builder().msgKey(UamDeliveryObjectBusinessKey.RCDC_UAM_DELIVERY_OBJECT_DELETE_SINGLE_SUCCESS_RESULT)
                    .msgArgs(new String[] {cloudDesktopName}).batchTaskStatus(BatchTaskStatus.SUCCESS).build();
        } else {
            return DefaultBatchTaskFinishResult.builder().msgKey(UamDeliveryObjectBusinessKey.RCDC_UAM_DELIVERY_OBJECT_DELETE_SINGLE_FAIL_RESULT)
                    .msgArgs(new String[] {cloudDesktopName}).batchTaskStatus(BatchTaskStatus.FAILURE).build();
        }
    }


}
