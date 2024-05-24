package com.ruijie.rcos.rcdc.rco.module.web.ctrl.deskimport.batchtask;

import com.alibaba.fastjson.JSON;
import com.ruijie.rcos.gss.log.module.def.api.BaseAuditLogAPI;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.api.CbbIDVDeskMgmtAPI;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.api.CbbIDVDeskOperateAPI;
import com.ruijie.rcos.rcdc.rco.module.def.api.UserDesktopMgmtAPI;
import com.ruijie.rcos.rcdc.rco.module.def.api.UserDesktopOperateAPI;
import com.ruijie.rcos.rcdc.rco.module.def.api.request.CreateCloudDesktopRequest;
import com.ruijie.rcos.rcdc.rco.module.web.ctrl.user.UserBusinessKey;
import com.ruijie.rcos.rcdc.rco.module.web.ctrl.user.bactchtask.AbstractDesktopBatchTaskHandler;
import com.ruijie.rcos.rcdc.rco.module.web.ctrl.user.dto.ImportVDIDeskDTO;
import com.ruijie.rcos.rcdc.rco.module.web.ctrl.user.response.desktop.CreateDesktopWebResponse;
import com.ruijie.rcos.rcdc.rco.module.web.ctrl.user.vo.ImportVDIDesktopConfig;
import com.ruijie.rcos.rcdc.rco.module.web.service.CloudDesktopWebService;
import com.ruijie.rcos.rcdc.rco.module.web.service.ImportVDIDeskService;
import com.ruijie.rcos.sk.base.batch.*;
import com.ruijie.rcos.sk.base.exception.BusinessException;
import com.ruijie.rcos.sk.base.log.Logger;
import com.ruijie.rcos.sk.base.log.LoggerFactory;
import org.springframework.util.Assert;

/**
 * 
 * Description: 创建云桌面批量任务处理器
 * Copyright: Copyright (c) 2018
 * Company: Ruijie Co., Ltd.
 * Create Time: 2019年1月16日
 * 
 * @author artom
 */
public class ImportVDIDeskBatchTaskHandler extends AbstractDesktopBatchTaskHandler {

    /**
     * 日志
     */
    private static final Logger LOGGER = LoggerFactory.getLogger(ImportVDIDeskBatchTaskHandler.class);

    /**
     * 操作记录
     */
    private BaseAuditLogAPI auditLogAPI;



    /**
     * 导入VDI云桌面配置服务
     */
    private ImportVDIDeskService importVDIDeskService;

    public ImportVDIDeskBatchTaskHandler(ImportVDIDeskBatchTaskHandlerRequest request) {
        super(request.getBatchTaskItemIterator());
        this.auditLogAPI = request.getAuditLogAPI();
        this.importVDIDeskService = request.getImportVDIDeskService();
        this.cloudDesktopWebService = request.getCloudDesktopWebService();
    }

    @Override
    public ImportVDIDeskBatchTaskHandler setCloudDesktopWebService(CloudDesktopWebService cloudDesktopWebService) {
        Assert.notNull(cloudDesktopWebService, "cloudDesktopWebService must not be null");
        this.cloudDesktopWebService = cloudDesktopWebService;
        return this;
    }

    @Override
    public ImportVDIDeskBatchTaskHandler setCbbUserDesktopMgmtAPI(UserDesktopMgmtAPI cloudDesktopMgmtAPI) {
        Assert.notNull(cloudDesktopMgmtAPI, "cloudDesktopMgmtAPI must not be null");
        this.cloudDesktopMgmtAPI = cloudDesktopMgmtAPI;
        return this;
    }

    @Override
    public ImportVDIDeskBatchTaskHandler setCbbUserDesktopOperateAPI(UserDesktopOperateAPI cloudDesktopOperateAPI) {
        // 不需要设置
        return null;
    }

    @Override
    public ImportVDIDeskBatchTaskHandler setCbbIDVDeskOperateAPI(CbbIDVDeskOperateAPI cbbIDVDeskOperateAPI) {
        // 不需要设置
        return null;
    }

    @Override
    public ImportVDIDeskBatchTaskHandler setcbbIDVDeskMgmtAPI(CbbIDVDeskMgmtAPI cbbIDVDeskMgmtAPI) {
        // 不需要设置
        return null;
    }

    @Override
    public BatchTaskItemResult processItem(BatchTaskItem taskItem) throws BusinessException {
        Assert.notNull(taskItem, "taskItem is not null");
        CreateVDIDeskBatchTaskItem createVDIDeskBatchTaskItem = (CreateVDIDeskBatchTaskItem) taskItem;
        LOGGER.info("批任务导入桌面对象createVDIDeskBatchTaskItem:{}", JSON.toJSONString(createVDIDeskBatchTaskItem));
        //导入VDI桌面信息
        ImportVDIDeskDTO importVDIDeskDTO = createVDIDeskBatchTaskItem.getImportVDIDeskDTO();
        try {
            //检查获取VDI云桌面配置
            ImportVDIDesktopConfig importVDIDesktopConfig = importVDIDeskService.checkAndGetVdiDesktopConfig(importVDIDeskDTO);
            LOGGER.info("导入桌面数据校验成功 importVDIDesktopConfig:{}", JSON.toJSONString(importVDIDesktopConfig));
            //创建VDI云桌面
            CreateCloudDesktopRequest createRequest = new CreateCloudDesktopRequest();
            //设置用户ID
            createRequest.setUserId(importVDIDesktopConfig.getUserId());
            //设置镜像
            createRequest.setDesktopImageId(importVDIDesktopConfig.getVdiImageId());
            //设置云桌面策略
            createRequest.setStrategyId(importVDIDesktopConfig.getVdiStrategyId());
            //设置网络策略
            createRequest.setNetworkId(importVDIDesktopConfig.getVdiNetworkId());
            //设置运行位置
            createRequest.setClusterId(importVDIDesktopConfig.getClusterId());
            //设置规格
            createRequest.setDeskSpec(importVDIDesktopConfig.getDeskSpecDTO());
            //设置云平台
            createRequest.setPlatformId(importVDIDesktopConfig.getPlatformId());
            //导入VDI云桌面
            CreateDesktopWebResponse createResponse =
                    cloudDesktopWebService.importDesktop(createRequest, importVDIDeskDTO.getUserName());
            auditLogAPI.recordLog(UserBusinessKey.RCDC_RCO_IMPORT_VDI_DESK_SUC_LOG, importVDIDeskDTO.getUserName(),
                    createResponse.getDesktopName());
            return DefaultBatchTaskItemResult.builder().batchTaskItemStatus(BatchTaskItemStatus.SUCCESS)
                    .msgKey(UserBusinessKey.RCDC_RCO_IMPORT_VDI_DESK_ITEM_SUC_DESC)
                    .msgArgs(new String[] {importVDIDeskDTO.getUserName(), createResponse.getDesktopName()})
                    .build();
        } catch (BusinessException e) {
            auditLogAPI.recordLog(UserBusinessKey.RCDC_RCO_IMPORT_VDI_DESK_FAIL_LOG, importVDIDeskDTO.getUserName(), e.getI18nMessage());
            LOGGER.error("导入云桌面失败，用户id=" + taskItem.getItemID().toString(), e);
            throw new BusinessException(UserBusinessKey.RCDC_RCO_IMPORT_VDI_DESK_ITEM_FAIL_DESC, e, importVDIDeskDTO.getUserName(),
                    e.getI18nMessage());
        } catch (Exception e) {
            auditLogAPI.recordLog(UserBusinessKey.RCDC_RCO_IMPORT_VDI_DESK_FAIL_LOG, importVDIDeskDTO.getUserName(), e.getMessage());
            LOGGER.error("导入云桌面失败，用户id=" + taskItem.getItemID().toString(), e);
            throw new BusinessException(UserBusinessKey.RCDC_RCO_IMPORT_VDI_DESK_ITEM_FAIL_DESC, e, importVDIDeskDTO.getUserName(), e.getMessage());
        } finally {
            processItemCount.incrementAndGet();
        }
    }

    @Override
    public BatchTaskFinishResult onFinish(int sucCount, int failCount) {
        return buildDefaultFinishResult(sucCount, failCount, UserBusinessKey.RCDC_RCO_IMPORT_VDI_DESK_BATCH_RESULT);
    }

}
