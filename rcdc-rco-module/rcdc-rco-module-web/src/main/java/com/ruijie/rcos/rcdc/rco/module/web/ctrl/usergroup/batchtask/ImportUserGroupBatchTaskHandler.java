package com.ruijie.rcos.rcdc.rco.module.web.ctrl.usergroup.batchtask;

import java.util.UUID;

import com.ruijie.rcos.sk.base.usertip.UserTipUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.Assert;

import com.ruijie.rcos.gss.sdk.iac.module.def.dto.user.IacImportUserGroupDTO;
import com.ruijie.rcos.gss.log.module.def.api.BaseAuditLogAPI;
import com.ruijie.rcos.gss.sdk.iac.module.def.api.IacImportUserAPI;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.api.CbbDeskSpecAPI;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.dto.deskspec.CbbCreateDeskSpecDTO;
import com.ruijie.rcos.rcdc.rco.module.def.api.UserDesktopConfigAPI;
import com.ruijie.rcos.rcdc.rco.module.def.api.request.CreateUserGroupDesktopConfigRequest;
import com.ruijie.rcos.rcdc.rco.module.def.enums.UserCloudDeskTypeEnum;
import com.ruijie.rcos.rcdc.rco.module.web.ctrl.user.UserBusinessKey;
import com.ruijie.rcos.rcdc.rco.module.web.ctrl.user.bactchtask.CreateUserGroupBatchTaskItem;
import com.ruijie.rcos.rcdc.rco.module.web.ctrl.user.dto.ImportUserGroupDTO;
import com.ruijie.rcos.rcdc.rco.module.web.ctrl.user.vo.IDVDesktopConfig;
import com.ruijie.rcos.rcdc.rco.module.web.ctrl.user.vo.VDIDesktopConfig;
import com.ruijie.rcos.rcdc.rco.module.web.ctrl.user.vo.VOIDesktopConfig;
import com.ruijie.rcos.rcdc.rco.module.web.ctrl.usergroup.request.ImportUserGroupBatchTaskHandlerRequest;
import com.ruijie.rcos.rcdc.rco.module.web.service.ImportUserGroupService;
import com.ruijie.rcos.sk.base.batch.AbstractBatchTaskHandler;
import com.ruijie.rcos.sk.base.batch.BatchTaskFinishResult;
import com.ruijie.rcos.sk.base.batch.BatchTaskItem;
import com.ruijie.rcos.sk.base.batch.BatchTaskItemResult;
import com.ruijie.rcos.sk.base.batch.BatchTaskItemStatus;
import com.ruijie.rcos.sk.base.batch.DefaultBatchTaskItemResult;
import com.ruijie.rcos.sk.base.exception.BusinessException;
import com.ruijie.rcos.sk.base.log.Logger;
import com.ruijie.rcos.sk.base.log.LoggerFactory;

/**
 * Description:
 * Copyright: Copyright (c) 2020
 * Company: Ruijie Co., Ltd.
 * Create Time: 2020/12/12
 *
 * @author wjp
 */
public class ImportUserGroupBatchTaskHandler extends AbstractBatchTaskHandler {
    private static final Logger LOGGER = LoggerFactory.getLogger(ImportUserGroupBatchTaskHandler.class);

    @Autowired
    private BaseAuditLogAPI auditLogAPI;

    @Autowired
    private IacImportUserAPI cbbImportUserAPI;

    @Autowired
    private UserDesktopConfigAPI userDesktopConfigAPI;

    @Autowired
    private ImportUserGroupService importUserGroupService;

    private CbbDeskSpecAPI cbbDeskSpecAPI;

    public ImportUserGroupBatchTaskHandler(ImportUserGroupBatchTaskHandlerRequest request) {
        super(request.getBatchTaskItemIterator());
        this.auditLogAPI = request.getAuditLogAPI();
        this.cbbImportUserAPI = request.getCbbImportUserAPI();
        this.userDesktopConfigAPI = request.getUserDesktopConfigAPI();
        this.importUserGroupService = request.getImportUserGroupService();
        this.cbbDeskSpecAPI = request.getCbbDeskSpecAPI();
    }

    @Override
    public BatchTaskItemResult processItem(BatchTaskItem batchTaskItem) throws BusinessException {
        Assert.notNull(batchTaskItem, "batchTaskItem can not be null");
        CreateUserGroupBatchTaskItem item = (CreateUserGroupBatchTaskItem) batchTaskItem;
        ImportUserGroupDTO dto = item.getImportUserGroupDTO();
        IacImportUserGroupDTO request = ImportUserGroupDTO.convertFor(dto);
        try {
            // 先校验导入的桌面配置是否合法
            VDIDesktopConfig vdiDesktopConfig = importUserGroupService.checkAndGetVdiDesktopConfig(dto);
            IDVDesktopConfig idvDesktopConfig = importUserGroupService.checkAndGetIdvDesktopConfig(dto);
            VOIDesktopConfig voiDesktopConfig = importUserGroupService.checkAndGetVoiDesktopConfig(dto);
            // 导入用户组数据
            UUID groupId = cbbImportUserAPI.importUserGroup(request);
            LOGGER.info("导入用户组数据<{}>", groupId);
            // 生成桌面配置
            configDesktop(groupId, vdiDesktopConfig, idvDesktopConfig,voiDesktopConfig);
            auditLogAPI.recordLog(UserBusinessKey.RCDC_RCO_IMPORT_USER_GROUP_SUCCESS_LOG, new String[] {dto.getGroupNames()});
        } catch (BusinessException e) {
            String exceptionMsg = UserTipUtil.resolveBusizExceptionMsg(e);
            auditLogAPI.recordLog(UserBusinessKey.RCDC_RCO_IMPORT_USER_GROUP_FAIL_LOG, new String[] {dto.getGroupNames(), exceptionMsg});
            throw new BusinessException(UserBusinessKey.RCDC_RCO_IMPORT_USER_GROUP_FAIL, e, dto.getGroupNames(), exceptionMsg);
        } catch (Exception e) {
            String exceptionMsg = e.getMessage();
            auditLogAPI.recordLog(UserBusinessKey.RCDC_RCO_IMPORT_USER_GROUP_FAIL_LOG, new String[] {dto.getGroupNames(), exceptionMsg});
            throw new BusinessException(UserBusinessKey.RCDC_RCO_IMPORT_USER_GROUP_FAIL, e, dto.getGroupNames(), exceptionMsg);
        }
        return DefaultBatchTaskItemResult.builder().batchTaskItemStatus(BatchTaskItemStatus.SUCCESS)
                .msgKey(UserBusinessKey.RCDC_RCO_IMPORT_USER_GROUP_SUCCESS_LOG).msgArgs(new String[] {dto.getGroupNames()}).build();
    }


    private void configDesktop(UUID groupId, VDIDesktopConfig vdiDesktopConfig, IDVDesktopConfig idvDesktopConfig, VOIDesktopConfig voiDesktopConfig)
            throws BusinessException {
        //保存VDI云桌面配置
        if (vdiDesktopConfig != null && vdiDesktopConfig.getVdiImageId() != null && vdiDesktopConfig.getVdiNetworkId() != null
                && vdiDesktopConfig.getVdiStrategyId() != null  && vdiDesktopConfig.getDeskSpecDTO() != null
                && vdiDesktopConfig.getVdiPlatformId() != null) {
            CreateUserGroupDesktopConfigRequest configVdiRequest = new CreateUserGroupDesktopConfigRequest(groupId, UserCloudDeskTypeEnum.VDI);
            configVdiRequest.setDeskSpecId(cbbDeskSpecAPI.create(new CbbCreateDeskSpecDTO(vdiDesktopConfig.getDeskSpecDTO())));
            configVdiRequest.setImageTemplateId(vdiDesktopConfig.getVdiImageId());
            configVdiRequest.setStrategyId(vdiDesktopConfig.getVdiStrategyId());
            configVdiRequest.setNetworkId(vdiDesktopConfig.getVdiNetworkId());
            configVdiRequest.setClusterId(vdiDesktopConfig.getVdiClusterId());
            configVdiRequest.setPlatformId(vdiDesktopConfig.getVdiPlatformId());
            userDesktopConfigAPI.createOrUpdateUserGroupDesktopConfig(configVdiRequest);
        }
        //保存IDV云桌面配置
        if (idvDesktopConfig != null && idvDesktopConfig.getIdvImageId() != null && idvDesktopConfig.getIdvStrategyId() != null) {
            CreateUserGroupDesktopConfigRequest configIdvRequest = new CreateUserGroupDesktopConfigRequest(groupId, UserCloudDeskTypeEnum.IDV);
            configIdvRequest.setImageTemplateId(idvDesktopConfig.getIdvImageId());
            configIdvRequest.setStrategyId(idvDesktopConfig.getIdvStrategyId());
            userDesktopConfigAPI.createOrUpdateUserGroupDesktopConfig(configIdvRequest);
        }
         //保存VOI云桌面配置
        if (voiDesktopConfig != null && voiDesktopConfig.getVoiImageId() != null && voiDesktopConfig.getVoiStrategyId() != null) {
            CreateUserGroupDesktopConfigRequest configIdvRequest = new CreateUserGroupDesktopConfigRequest(groupId, UserCloudDeskTypeEnum.VOI);
            configIdvRequest.setImageTemplateId(voiDesktopConfig.getVoiImageId());
            configIdvRequest.setStrategyId(voiDesktopConfig.getVoiStrategyId());
            userDesktopConfigAPI.createOrUpdateUserGroupDesktopConfig(configIdvRequest);
        }

    }

    @Override
    public BatchTaskFinishResult onFinish(int successCount, int failCount) {
        return buildDefaultFinishResult(successCount, failCount, UserBusinessKey.RCDC_RCO_IMPORT_USER_GROUP_BATCH_TASK_SUCCESS);
    }
}
