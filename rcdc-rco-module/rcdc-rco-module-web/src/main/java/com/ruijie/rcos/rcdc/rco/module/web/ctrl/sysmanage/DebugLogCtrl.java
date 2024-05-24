package com.ruijie.rcos.rcdc.rco.module.web.ctrl.sysmanage;

import java.io.File;
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
import com.ruijie.rcos.base.sysmanage.module.def.api.DebugLogAPI;
import com.ruijie.rcos.base.sysmanage.module.def.api.request.debuglog.BaseListDebugLogRequest;
import com.ruijie.rcos.base.sysmanage.module.def.api.response.debuglog.BaseDeleteDebugLogResponse;
import com.ruijie.rcos.base.sysmanage.module.def.api.response.debuglog.BaseDetailDebugLogResponse;
import com.ruijie.rcos.base.sysmanage.module.def.dto.BaseDebugLogDTO;
import com.ruijie.rcos.rcdc.rco.module.web.ctrl.sysmanage.batchtask.DebugLogCreateBatchTask;
import com.ruijie.rcos.rcdc.rco.module.web.ctrl.sysmanage.batchtask.DebugLogDeleteBatchTask;
import com.ruijie.rcos.rcdc.rco.module.web.ctrl.sysmanage.request.debuglog.BaseCreateDebugLogWebRequest;
import com.ruijie.rcos.rcdc.rco.module.web.ctrl.sysmanage.request.debuglog.BaseDeleteDebugLogWebRequest;
import com.ruijie.rcos.rcdc.rco.module.web.ctrl.sysmanage.request.debuglog.BaseDownloadDebugLogWebRequest;
import com.ruijie.rcos.rcdc.rco.module.web.ctrl.sysmanage.request.debuglog.BaseListDebugLogWebRequest;
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
import com.ruijie.rcos.sk.webmvc.api.response.DownloadWebResponse;

/**
 * Description: 调试日志CTRL
 * Copyright: Copyright (c) 2018
 * Company: Ruijie Co., Ltd.
 * Create Time: 2018年12月19日
 *
 * @author fyq
 */
@Controller
@RequestMapping("rco/maintenance/debugLog")
public class DebugLogCtrl {

    private static final Logger LOGGER = LoggerFactory.getLogger(DebugLogCtrl.class);

    private static final UUID CREATE_DEBUG_LOG_TASK_ID = UUID.nameUUIDFromBytes("base_sysmanage_debug_log".getBytes(StandardCharsets.UTF_8));
    
    private static final String TAR_GZ_SUFFIX = "tar.gz";

    @Autowired
    private DebugLogAPI debugLogAPI;

    @Autowired
    private BaseAuditLogAPI auditLogAPI;

    /**
     * 创建调试日志压缩文件接口
     * 
     * @param webRequest web请求
     * @param taskBuilder 比任务接口
     * @return 请求结果
     * @throws BusinessException 业务异常
     */
    @RequestMapping(value = "create")
    public DefaultWebResponse createDebugLog(BaseCreateDebugLogWebRequest webRequest, BatchTaskBuilder taskBuilder) throws BusinessException {

        Assert.notNull(webRequest, "请求参数不能为空");

        final String itemName = LocaleI18nResolver.resolve(SysmanagerBusinessKey.BASE_SYS_MANAGE_COLLECT_LOG_ITEM_NAME);
        final BatchTaskItem batchTaskItem = new DefaultBatchTaskItem(UUID.randomUUID(), itemName);
        final DebugLogCreateBatchTask debugLogCreateBatchTask = new DebugLogCreateBatchTask(batchTaskItem, debugLogAPI, auditLogAPI);

        BatchTaskSubmitResult batchTaskSubmitResult = taskBuilder.setTaskName(SysmanagerBusinessKey.BASE_SYS_MANAGE_COLLECT_LOG_TASK_NAME)//
                .setTaskDesc(SysmanagerBusinessKey.BASE_SYS_MANAGE_COLLECT_LOG_TASK_DESC)//
                .registerHandler(debugLogCreateBatchTask)//
                .setUniqueId(CREATE_DEBUG_LOG_TASK_ID) //
                .start();

        return DefaultWebResponse.Builder.success(batchTaskSubmitResult);
    }

    /**
     * 删除调试日志压缩文件接口
     * 
     * @param webRequest web请求
     * @param batchTaskBuilder 批任务接口
     * @return 请求结果
     * @throws BusinessException 业务异常
     */
    @RequestMapping(value = "delete")
    public DefaultWebResponse deleteDebugLog(BaseDeleteDebugLogWebRequest webRequest, BatchTaskBuilder batchTaskBuilder) throws BusinessException {

        Assert.notNull(webRequest, "请求参数不能为空");

        final UUID[] idArr = webRequest.getIdArr();
        boolean isBatch = idArr.length > 1;

        if (isBatch) {
            return batchDeleteDebugLog(batchTaskBuilder, idArr);
        } else {
            return deleteOneDebugLog(idArr[0]);
        }
    }



    /**
     * 获取调试日志列表接口
     * 
     * @param webRequest web请求
     * @return 请求结果
     */
    @RequestMapping(value = "list")
    public DefaultWebResponse listDebugLog(BaseListDebugLogWebRequest webRequest) {

        Assert.notNull(webRequest, "请求参数不能为空");

        BaseListDebugLogRequest apiRequest = new BaseListDebugLogRequest();
        BeanUtils.copyProperties(webRequest, apiRequest);

        DefaultPageResponse<BaseDebugLogDTO> response = debugLogAPI.listDebugLog(apiRequest);

        return DefaultWebResponse.Builder.success(response);
    }


    /**
     * 下载调试日志压缩文件接口
     * 
     * @param webRequest web请求
     * @throws BusinessException 业务异常
     * @return 请求结果
     */
    @RequestMapping(value = "download")
    public DownloadWebResponse downloadDebugLog(BaseDownloadDebugLogWebRequest webRequest) throws BusinessException {

        Assert.notNull(webRequest, "请求参数不能为空");

        BaseDetailDebugLogResponse apiResponse = getDebugLogDetail(webRequest.getId());

        final String filePath = apiResponse.getFilePath();
        final String fileName = apiResponse.getFileName();
        final String fileNameWithoutSuffix = fileName.substring(0, fileName.indexOf(TAR_GZ_SUFFIX) - 1);

        final File file = new File(filePath, fileName);

        return new DownloadWebResponse.Builder()//
                .setContentType("application/octet-stream")//
                .setName(fileNameWithoutSuffix, TAR_GZ_SUFFIX)//
                .setFile(file)//
                .build();
    }

    private DefaultWebResponse batchDeleteDebugLog(BatchTaskBuilder batchTaskBuilder, UUID[] idArr)
            throws BusinessException {
        String itemName = LocaleI18nResolver.resolve(SysmanagerBusinessKey.BASE_SYS_MANAGE_DELETE_LOG_ITEM_NAME);
        final Iterator<BatchTaskItem> iterator = Stream.of(idArr) //
                .map(id -> (BatchTaskItem) DefaultBatchTaskItem.builder()//
                        .itemId(id)//
                        .itemName(itemName)//
                        .build())
                .iterator();

        final DebugLogDeleteBatchTask debugLogDeleteBatchTask = new DebugLogDeleteBatchTask(iterator, debugLogAPI, auditLogAPI);

        BatchTaskSubmitResult batchTaskSubmitResult = batchTaskBuilder.setTaskName(SysmanagerBusinessKey.BASE_SYS_MANAGE_BATCH_DELETE_LOG_TASK_NAME)//
                .setTaskDesc(SysmanagerBusinessKey.BASE_SYS_MANAGE_BATCH_DELETE_LOG_TASK_DESC)//
                .registerHandler(debugLogDeleteBatchTask)//
                .start();

        return DefaultWebResponse.Builder.success(batchTaskSubmitResult);
    }

    private DefaultWebResponse deleteOneDebugLog(UUID id) throws BusinessException {
        BaseDeleteDebugLogResponse apiResponse = null;
        try {
            apiResponse = debugLogAPI.deleteDebugLog(id);
            auditLogAPI.recordLog(SysmanagerBusinessKey.BASE_SYS_MANAGE_DELETE_LOG_DO_SUCCESS, apiResponse.getFileName());
            return DefaultWebResponse.Builder.success(SysmanagerBusinessKey.BASE_SYS_MANAGE_OPERATOR_SUCCESS, StringUtils.EMPTY);
        } catch (BusinessException e) {
            LOGGER.error("删除日志失败", e);
            String name = apiResponse == null ? id.toString() : apiResponse.getFileName();
            auditLogAPI.recordLog(SysmanagerBusinessKey.BASE_SYS_MANAGE_DELETE_LOG_DO_FAIL, name, e.getI18nMessage());
            throw e;
        }
    }

    private BaseDetailDebugLogResponse getDebugLogDetail(UUID id) throws BusinessException {

        return debugLogAPI.detailDebugLog(id);
    }

}
