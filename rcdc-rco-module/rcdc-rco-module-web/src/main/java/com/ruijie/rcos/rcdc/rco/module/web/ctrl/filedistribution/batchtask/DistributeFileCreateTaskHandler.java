package com.ruijie.rcos.rcdc.rco.module.web.ctrl.filedistribution.batchtask;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.ruijie.rcos.gss.log.module.def.api.BaseAuditLogAPI;
import com.ruijie.rcos.rcdc.rco.module.def.api.FileDistributionFileManageAPI;
import com.ruijie.rcos.rcdc.rco.module.def.api.dto.filedistribution.DistributeFileDTO;
import com.ruijie.rcos.rcdc.rco.module.def.api.request.filedistribution.CreateDistributeFileRequest;
import com.ruijie.rcos.rcdc.rco.module.web.Constants;
import com.ruijie.rcos.rcdc.rco.module.web.ctrl.filedistribution.FileDistributionBusinessKey;
import com.ruijie.rcos.sk.base.batch.*;
import com.ruijie.rcos.sk.base.exception.BusinessException;
import com.ruijie.rcos.sk.base.log.Logger;
import com.ruijie.rcos.sk.base.log.LoggerFactory;
import com.ruijie.rcos.sk.webmvc.api.request.ChunkUploadFile;
import org.springframework.util.Assert;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Date;
import java.util.UUID;

/**
 * Description: 分发文件创建批任务处理器
 * Copyright: Copyright (c) 2022
 * Company: Ruijie Co., Ltd.
 * Create Time: 2022/2/11 14:46
 *
 * @author zhangyichi
 */
public class DistributeFileCreateTaskHandler extends AbstractSingleTaskHandler {

    private static final Logger LOGGER = LoggerFactory.getLogger(DistributeFileCreateTaskHandler.class);

    private static final String FILE_DESCRIPTION_KEY = "description";

    private ChunkUploadFile file;

    private BaseAuditLogAPI auditLogAPI;

    private FileDistributionFileManageAPI fileDistributionFileManageAPI;

    public DistributeFileCreateTaskHandler(BatchTaskItem batchTaskItem) {
        super(batchTaskItem);
    }

    public void setFile(ChunkUploadFile file) {
        this.file = file;
    }

    public void setAuditLogAPI(BaseAuditLogAPI auditLogAPI) {
        this.auditLogAPI = auditLogAPI;
    }

    public void setFileDistributionFileManageAPI(FileDistributionFileManageAPI fileDistributionFileManageAPI) {
        this.fileDistributionFileManageAPI = fileDistributionFileManageAPI;
    }

    @Override
    public BatchTaskItemResult processItem(BatchTaskItem batchTaskItem) throws BusinessException {
        Assert.notNull(batchTaskItem, "batchTaskItem cannot be null!");

        try {
            checkAndCreateTargetPath();
            fileDistributionFileManageAPI.createFile(buildCreateFileRequest());
            auditLogAPI.recordLog(FileDistributionBusinessKey.RCDC_RCO_FILE_DISTRIBUTION_FILE_UPLOAD_SUCCESS_LOG,
                    file.getFileName());
            return DefaultBatchTaskItemResult.builder().batchTaskItemStatus(BatchTaskItemStatus.SUCCESS)
                    .msgKey(FileDistributionBusinessKey.RCDC_RCO_FILE_DISTRIBUTION_FILE_UPLOAD_ITEM_SUCCESS_DESC)
                    .msgArgs(new String[] {file.getFileName()}).build();
        } catch (BusinessException e) {
            auditLogAPI.recordLog(FileDistributionBusinessKey.RCDC_RCO_FILE_DISTRIBUTION_FILE_UPLOAD_FAIL_LOG,
                    file.getFileName(), e.getI18nMessage());
            throw new BusinessException(FileDistributionBusinessKey.RCDC_RCO_FILE_DISTRIBUTION_FILE_UPLOAD_ITEM_FAIL_DESC, e, file.getFileName(),
                    e.getI18nMessage());
        }
    }

    private CreateDistributeFileRequest buildCreateFileRequest() {
        DistributeFileDTO fileDTO = new DistributeFileDTO();
        fileDTO.setId(UUID.randomUUID());
        fileDTO.setFileName(file.getFileName());
        fileDTO.setFileSize(file.getFileSize());
        JSONObject json = file.getCustomData();
        String fileDescription = json.getString(FILE_DESCRIPTION_KEY);
        fileDTO.setDescription(fileDescription);
        fileDTO.setCreateTime(new Date());
        CreateDistributeFileRequest request = new CreateDistributeFileRequest();
        request.setSourcePath(file.getFilePath());
        request.setTargetPath(Constants.SAMBA_APP_PATH + File.separator + file.getFileName());
        request.setFileDTO(fileDTO);
        LOGGER.info("构建创建分发文件请求[{}]", JSON.toJSONString(request));
        return request;
    }

    private void checkAndCreateTargetPath() throws BusinessException {
        try {
            Path sambaAppPath = Paths.get(Constants.SAMBA_APP_PATH);
            if (Files.notExists(sambaAppPath)) {
                Files.createDirectories(sambaAppPath);
            }
        } catch (Exception e) {
            LOGGER.error("创建分发文件存放目录失败", e);
            throw new BusinessException(FileDistributionBusinessKey.RCDC_RCO_FILE_DISTRIBUTION_CREATE_FILE_DIR_FAIL, e);
        }
    }

    @Override
    public BatchTaskFinishResult onFinish(int successCount, int failCount) {
        if (failCount == 0) {
            return DefaultBatchTaskFinishResult.builder().batchTaskStatus(BatchTaskStatus.SUCCESS)
                    .msgKey(FileDistributionBusinessKey.RCDC_RCO_FILE_DISTRIBUTION_UPLOAD_TASK_SUCCESS)
                    .msgArgs(new String[] {file.getFileName()}).build();
        } else {
            return DefaultBatchTaskFinishResult.builder().batchTaskStatus(BatchTaskStatus.FAILURE)
                    .msgKey(FileDistributionBusinessKey.RCDC_RCO_FILE_DISTRIBUTION_UPLOAD_TASK_FAIL)
                    .msgArgs(new String[] {file.getFileName()}).build();
        }
    }
}
