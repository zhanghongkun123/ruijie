package com.ruijie.rcos.rcdc.rco.module.web.ctrl.sysmanage;

import java.nio.charset.StandardCharsets;
import java.util.Iterator;
import java.util.UUID;
import java.util.stream.Stream;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.RequestMapping;

import com.ruijie.rcos.gss.log.module.def.api.BaseAuditLogAPI;
import com.ruijie.rcos.base.sysmanage.module.def.api.DataBackupAPI;
import com.ruijie.rcos.base.sysmanage.module.def.api.request.databackup.BaseListDataBackupRequest;
import com.ruijie.rcos.base.sysmanage.module.def.api.response.databackup.BaseDeleteDataBackupResponse;
import com.ruijie.rcos.base.sysmanage.module.def.dto.BaseDataBackupDTO;
import com.ruijie.rcos.rcdc.rco.module.web.ctrl.sysmanage.batchtask.DataBackupCreateBatchTask;
import com.ruijie.rcos.rcdc.rco.module.web.ctrl.sysmanage.batchtask.DataBackupDeleteBatchTask;
import com.ruijie.rcos.rcdc.rco.module.web.ctrl.sysmanage.request.databackup.BaseCreateDataBackupWebRequest;
import com.ruijie.rcos.rcdc.rco.module.web.ctrl.sysmanage.request.databackup.BaseDeleteDataBackupWebRequest;
import com.ruijie.rcos.rcdc.rco.module.web.ctrl.sysmanage.request.databackup.BaseListDataBackupWebRequest;
import com.ruijie.rcos.sk.base.batch.BatchTaskBuilder;
import com.ruijie.rcos.sk.base.batch.BatchTaskItem;
import com.ruijie.rcos.sk.base.batch.BatchTaskSubmitResult;
import com.ruijie.rcos.sk.base.batch.DefaultBatchTaskItem;
import com.ruijie.rcos.sk.base.exception.BusinessException;
import com.ruijie.rcos.sk.base.i18n.LocaleI18nResolver;
import com.ruijie.rcos.sk.base.log.Logger;
import com.ruijie.rcos.sk.base.log.LoggerFactory;
import com.ruijie.rcos.sk.modulekit.api.comm.DefaultPageResponse;
import com.ruijie.rcos.sk.webmvc.api.response.DefaultWebResponse;

/**
 * Description: 数据库备份CTRL
 * Copyright: Copyright (c) 2018
 * Company: Ruijie Co., Ltd.
 * Create Time: 2018年12月06日
 *
 * @author fyq
 */
@Controller
@RequestMapping("rco/maintenance/dataBackup")
public class DataBackupCtrl {
    private static final Logger LOGGER = LoggerFactory.getLogger(DataBackupCtrl.class);

    private static final UUID CREATE_DATA_BACK_UP_TASK_ID =
            UUID.nameUUIDFromBytes("base_sysmanage_data_back_up".getBytes(StandardCharsets.UTF_8));

    @Autowired
    private DataBackupAPI dataBackupAPI;

    @Autowired
    private BaseAuditLogAPI auditLogAPI;

    /**
     * 创建数据库备份接口
     * 
     * @param webRequest 创建请求
     * @param taskBuilder 批任务接口
     * @throws BusinessException 业务异常
     * @return 创建结果
     */
    @RequestMapping(value = "create")
    public DefaultWebResponse createDataBackup(BaseCreateDataBackupWebRequest webRequest, BatchTaskBuilder taskBuilder) throws BusinessException {

        Assert.notNull(webRequest, "请求参数不能为空");

        final String itemName =
                LocaleI18nResolver.resolve(SysmanagerBusinessKey.BASE_SYS_MANAGE_BACKUP_DATA_TASK_ITEM_NAME);
        final BatchTaskItem batchTaskItem = new DefaultBatchTaskItem(UUID.randomUUID(), itemName);
        final DataBackupCreateBatchTask backupCreateBatchTask =
                new DataBackupCreateBatchTask(batchTaskItem, dataBackupAPI, auditLogAPI);

        BatchTaskSubmitResult batchTaskSubmitResult =
                taskBuilder.setTaskName(SysmanagerBusinessKey.BASE_SYS_MANAGE_BACKUP_DATA_TASK_NAME)//
                        .setTaskDesc(SysmanagerBusinessKey.BASE_SYS_MANAGE_BACKUP_DATA_TASK_DESC)//
                        .registerHandler(backupCreateBatchTask)//
                        .setUniqueId(CREATE_DATA_BACK_UP_TASK_ID) //
                        .start();

        return DefaultWebResponse.Builder.success(batchTaskSubmitResult);
    }


    /**
     * 删除数据库备份
     *
     * @param webRequest 删除请求
     * @param taskBuilder 批任务接口
     * @return 删除结果
     * @throws BusinessException 业务异常
     */
    @RequestMapping(value = "delete")
    public DefaultWebResponse deleteDataBackup(BaseDeleteDataBackupWebRequest webRequest,
                                               BatchTaskBuilder taskBuilder) throws BusinessException {

        Assert.notNull(webRequest, "请求参数不能为空");

        final UUID[] idArr = webRequest.getIdArr();

        boolean isBatch = idArr.length > 1;

        if (isBatch) {
            return batchDeleteDataBackup(taskBuilder, idArr);
        } else {
            return deleteOneDataBackup(idArr);
        }
    }

    /**
     * 获取数据库备份列表
     * 
     * @param webRequest 获取请求
     * @return 备份列表
     */
    @RequestMapping(value = "list")
    public DefaultWebResponse listDataBackup(BaseListDataBackupWebRequest webRequest) {

        Assert.notNull(webRequest, "请求参数不能为空");

        BaseListDataBackupRequest apiRequest = new BaseListDataBackupRequest();
        BeanUtils.copyProperties(webRequest, apiRequest);

        DefaultPageResponse<BaseDataBackupDTO> apiResponse = dataBackupAPI.listDataBackup(apiRequest);

        return DefaultWebResponse.Builder.success(apiResponse);
    }

    private DefaultWebResponse batchDeleteDataBackup(BatchTaskBuilder taskBuilder, UUID[] idArr) throws BusinessException {
        String itemName =
                LocaleI18nResolver.resolve(SysmanagerBusinessKey.BASE_SYS_MANAGE_DELETE_BACKUP_DATA_TASK_ITEM_NAME);
        final Iterator<BatchTaskItem> iterator = Stream.of(idArr) //
                .map(id -> (BatchTaskItem) DefaultBatchTaskItem.builder()//
                        .itemId(id)//
                        .itemName(itemName)//
                        .build())
                .iterator();

        final DataBackupDeleteBatchTask dataBackupDeleteBatchTask =
                new DataBackupDeleteBatchTask(iterator, dataBackupAPI, auditLogAPI);

        BatchTaskSubmitResult batchTaskSubmitResult =
                taskBuilder.setTaskName(SysmanagerBusinessKey.BASE_SYS_MANAGE_BATCH_DELETE_BACKUP_DATA_TASK_NAME)//
                        .setTaskDesc(SysmanagerBusinessKey.BASE_SYS_MANAGE_BATCH_DELETE_BACKUP_DATA_TASK_DESC)//
                        .registerHandler(dataBackupDeleteBatchTask)//
                        .start();

        return DefaultWebResponse.Builder.success(batchTaskSubmitResult);
    }

    private DefaultWebResponse deleteOneDataBackup(UUID[] idArr)
            throws BusinessException {
        BaseDeleteDataBackupResponse apiResponse = null;
        try {
            apiResponse = dataBackupAPI.deleteDataBackup(idArr[0]);
            auditLogAPI.recordLog(SysmanagerBusinessKey.BASE_SYS_MANAGE_DELETE_BACKUP_DATA_DO_SUCCESS,
                    apiResponse.getFileName());
            return DefaultWebResponse.Builder.success(SysmanagerBusinessKey.BASE_SYS_MANAGE_OPERATOR_SUCCESS,
                    StringUtils.EMPTY);
        } catch (BusinessException e) {
            LOGGER.error("删除数据库备份失败", e);
            String name = apiResponse == null ? idArr[0].toString() : apiResponse.getFileName();
            auditLogAPI.recordLog(SysmanagerBusinessKey.BASE_SYS_MANAGE_DELETE_BACKUP_DATA_DO_FAIL, name,
                    e.getI18nMessage());
            throw e;
        }
    }



}
