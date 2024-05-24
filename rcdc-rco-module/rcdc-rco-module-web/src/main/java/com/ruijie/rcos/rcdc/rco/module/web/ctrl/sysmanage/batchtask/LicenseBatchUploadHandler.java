package com.ruijie.rcos.rcdc.rco.module.web.ctrl.sysmanage.batchtask;

import java.io.File;
import java.util.Iterator;
import java.util.Map;
import java.util.UUID;

import org.springframework.util.Assert;

import com.alibaba.fastjson.JSONObject;
import com.ruijie.rcos.gss.log.module.def.api.BaseAuditLogAPI;
import com.ruijie.rcos.base.sysmanage.module.def.api.BaseLicenseMgmtAPI;
import com.ruijie.rcos.base.sysmanage.module.def.dto.UploadLicenseFileDTO;
import com.ruijie.rcos.rcdc.rco.module.web.ctrl.sysmanage.SysmanagerBusinessKey;
import com.ruijie.rcos.rcdc.rco.module.web.ctrl.sysmanage.dto.BaseUploadDTO;
import com.ruijie.rcos.rcdc.rco.module.web.ctrl.sysmanage.utils.DeleteFileUtil;
import com.ruijie.rcos.sk.base.batch.AbstractBatchTaskHandler;
import com.ruijie.rcos.sk.base.batch.BatchTaskFinishResult;
import com.ruijie.rcos.sk.base.batch.BatchTaskItem;
import com.ruijie.rcos.sk.base.batch.BatchTaskItemResult;
import com.ruijie.rcos.sk.base.batch.BatchTaskItemStatus;
import com.ruijie.rcos.sk.base.batch.DefaultBatchTaskItem;
import com.ruijie.rcos.sk.base.batch.DefaultBatchTaskItemResult;
import com.ruijie.rcos.sk.base.exception.BusinessException;
import com.ruijie.rcos.sk.base.log.Logger;
import com.ruijie.rcos.sk.base.log.LoggerFactory;

/**
 * 
 * Description: 上传zip包
 * Copyright: Copyright (c) 2018
 * Company: Ruijie Co., Ltd.
 * Create Time: 2019年4月16日
 * 
 * @author zouqi
 */
public class LicenseBatchUploadHandler extends AbstractBatchTaskHandler {

    private static final Logger LOGGER = LoggerFactory.getLogger(LicenseBatchUploadHandler.class);

    private BaseAuditLogAPI auditLogAPI;

    private Map<UUID, BaseUploadDTO> licFileInfoMap;

    private BaseLicenseMgmtAPI baseLicenseMgmtAPI;

    public LicenseBatchUploadHandler(Iterator<DefaultBatchTaskItem> batchTaskItemIterator,
                                     BaseAuditLogAPI auditLogAPI, Map<UUID, BaseUploadDTO> licFileInfoMap,
                                     BaseLicenseMgmtAPI baseLicenseMgmtAPI) {
        super(batchTaskItemIterator);
        this.auditLogAPI = auditLogAPI;
        this.licFileInfoMap = licFileInfoMap;
        this.baseLicenseMgmtAPI = baseLicenseMgmtAPI;
    }

    @Override
    public BatchTaskFinishResult onFinish(int successCount, int failCount) {

        deleteUnZipFolder(licFileInfoMap);

        return buildDefaultFinishResult(successCount, failCount,
                SysmanagerBusinessKey.BASE_SYS_MANAGE_LICENSE_FILE_BATCH_UPLOAD_TASK_RESULT);
    }

    @Override
    public BatchTaskItemResult processItem(BatchTaskItem item) throws BusinessException {
        Assert.notNull(item, "BatchTaskItem is null");

        BaseUploadDTO dto = licFileInfoMap.get(item.getItemID());
        LOGGER.info("upload, BaseUploadLicFileBeforeDTO:{}", JSONObject.toJSONString(dto));
        UploadLicenseFileDTO uploadLicenseFileDTO = new UploadLicenseFileDTO();

        try {
            baseLicenseMgmtAPI.uploadLicenseFile(uploadLicenseFileDTO);
            auditLogAPI.recordLog(SysmanagerBusinessKey.BASE_SYS_MANAGE_LICENSE_FILE_UPLOAD_SUCCESS, dto.getFileName());
            LOGGER.info("授权证书加载成功，证书名：[{}]", dto.getFileName());
            return DefaultBatchTaskItemResult.builder().batchTaskItemStatus(BatchTaskItemStatus.SUCCESS)
                    .msgKey(SysmanagerBusinessKey.BASE_SYS_MANAGE_LICENSE_FILE_UPLOAD_SUCCESS)
                    .msgArgs(new String[] {dto.getFileName()}).build();

        } catch (BusinessException e) {

            LOGGER.error("授权证书加载失败，证书名：[{}]，异常：{}", dto.getFileName(), e);
            auditLogAPI.recordLog(SysmanagerBusinessKey.BASE_SYS_MANAGE_LICENSE_FILE_UPLOAD_FAIL, dto.getFileName(), e.getI18nMessage());
            throw new BusinessException(SysmanagerBusinessKey.BASE_SYS_MANAGE_LICENSE_FILE_UPLOAD_FAIL, e, dto.getFileName(), e.getI18nMessage());
        }
    }

    /**
     * 删除用于临时解压zip包的文件夹
     * */
    private void deleteUnZipFolder(Map<UUID, BaseUploadDTO> licFileInfoMap) {
        //如果zip上传后，需要删除解压后的文件夹
        licFileInfoMap.keySet().stream().anyMatch(uid -> {
            BaseUploadDTO dto = licFileInfoMap.get(uid);
            String folderPath = dto.getFilePath().substring(0, dto.getFilePath().lastIndexOf(File.separator));
            File folder = new File(folderPath);
            DeleteFileUtil.deleteFile(folder);
            return true;
        });
    }
}
