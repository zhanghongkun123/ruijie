package com.ruijie.rcos.rcdc.rco.module.web.ctrl.filedistribution;

import java.util.Iterator;
import java.util.UUID;
import java.util.stream.Stream;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.RequestMapping;

import com.ruijie.rcos.gss.log.module.def.api.BaseAuditLogAPI;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.api.CbbDeskSoftMgmtAPI;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.dto.CbbCheckDeskSoftServerDiskSizeDTO;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.dto.CbbCheckServerDiskSizeResultDTO;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.dto.CheckDuplicationResultDTO;
import com.ruijie.rcos.rcdc.rco.module.def.api.FileDistributionFileManageAPI;
import com.ruijie.rcos.rcdc.rco.module.def.api.dto.filedistribution.DistributeFileDTO;
import com.ruijie.rcos.rcdc.rco.module.def.api.request.filedistribution.EditDistributeFileRequest;
import com.ruijie.rcos.rcdc.rco.module.web.Constants;
import com.ruijie.rcos.rcdc.rco.module.web.ctrl.clouddesktop.CloudDesktopBusinessKey;
import com.ruijie.rcos.rcdc.rco.module.web.ctrl.clouddesktop.request.desksoft.CheckSoftNameDuplicationWebRequest;
import com.ruijie.rcos.rcdc.rco.module.web.ctrl.filedistribution.batchtask.DistributeFileCreateTaskHandler;
import com.ruijie.rcos.rcdc.rco.module.web.ctrl.filedistribution.batchtask.DistributeFileDeleteTaskHandler;
import com.ruijie.rcos.rcdc.rco.module.web.ctrl.filedistribution.resquest.DistributeFileEditWebRequest;
import com.ruijie.rcos.sk.base.batch.BatchTaskBuilder;
import com.ruijie.rcos.sk.base.batch.BatchTaskItem;
import com.ruijie.rcos.sk.base.batch.BatchTaskSubmitResult;
import com.ruijie.rcos.sk.base.batch.DefaultBatchTaskItem;
import com.ruijie.rcos.sk.base.exception.BusinessException;
import com.ruijie.rcos.sk.base.i18n.LocaleI18nResolver;
import com.ruijie.rcos.sk.pagekit.api.PageQueryWebConfig;
import com.ruijie.rcos.sk.webmvc.api.request.ChunkUploadFile;
import com.ruijie.rcos.sk.webmvc.api.request.IdArrWebRequest;
import com.ruijie.rcos.sk.webmvc.api.request.IdWebRequest;
import com.ruijie.rcos.sk.webmvc.api.response.DefaultWebResponse;

/**
 * Description: Function Description
 * Copyright: Copyright (c) 2022
 * Company: Ruijie Co., Ltd.
 * Create Time: 2022/2/11 09:43
 *
 * @author zhangyichi
 */
@Deprecated
@Controller
@RequestMapping("/rco/fileDistribute/file")
@PageQueryWebConfig(dtoType = DistributeFileDTO.class)
public class DistributeFileController {
    private static final int FILE_NAME_MAX_LENGTH = 64;

    @Autowired
    private FileDistributionFileManageAPI fileDistributionFileManageAPI;

    @Autowired
    private CbbDeskSoftMgmtAPI deskSoftMgmtAPI;

    @Autowired
    private BaseAuditLogAPI auditLogAPI;

    /**
     * 上传文件处理接口
     *
     * @param file           已上传文件
     * @param taskBuilder    批任务构造器
     * @return 默认响应（批任务）
     * @throws BusinessException 业务异常
     */
    @RequestMapping("create")
    public DefaultWebResponse uploadFile(ChunkUploadFile file, BatchTaskBuilder taskBuilder) throws BusinessException {
        Assert.notNull(file, "file cannot be null!");
        Assert.notNull(taskBuilder, "taskBuilder cannot be null!");

        final BatchTaskItem batchTaskItem = new DefaultBatchTaskItem(UUID.randomUUID(),
                LocaleI18nResolver.resolve(FileDistributionBusinessKey.RCDC_RCO_FILE_DISTRIBUTION_UPLOAD_ITEM_NAME));
        DistributeFileCreateTaskHandler batchTaskHandler = new DistributeFileCreateTaskHandler(batchTaskItem);
        batchTaskHandler.setFile(file);
        batchTaskHandler.setAuditLogAPI(auditLogAPI);
        batchTaskHandler.setFileDistributionFileManageAPI(fileDistributionFileManageAPI);
        BatchTaskSubmitResult result =
                taskBuilder.setTaskName(FileDistributionBusinessKey.RCDC_RCO_FILE_DISTRIBUTION_UPLOAD_TASK_NAME)
                        .setTaskDesc(FileDistributionBusinessKey.RCDC_RCO_FILE_DISTRIBUTION_UPLOAD_DESC, file.getFileName())
                        .registerHandler(batchTaskHandler)
                        .start();

        return DefaultWebResponse.Builder.success(result);
    }

    /**
     * 文件上传前检查
     *
     * @param request 文件检查请求
     * @return 检查结果
     * @throws BusinessException 业务异常
     */
    @RequestMapping("checkDuplication")
    public DefaultWebResponse checkDuplication(CheckSoftNameDuplicationWebRequest request) throws BusinessException {
        Assert.notNull(request, "CheckSoftNameDuplicationWebRequest is null");
        DistributeFileDTO fileDTO = fileDistributionFileManageAPI.findByName(request.getName());

        CheckDuplicationResultDTO dto = checkFileName(request.getName(), fileDTO);
        // 当文件名校验没有问题后，校验文件存放磁盘容量是否足够
        if (!dto.getHasDuplication()) {
            final CbbCheckDeskSoftServerDiskSizeDTO checkDiskSizeRequest = new CbbCheckDeskSoftServerDiskSizeDTO(request.getFileSize());
            CbbCheckServerDiskSizeResultDTO checkDiskSizeResponse = deskSoftMgmtAPI.checkServerDiskSize(checkDiskSizeRequest);

            dto.setHasDuplication(!checkDiskSizeResponse.getServerDiskSizeEnough());
            dto.setErrorMsg(checkDiskSizeResponse.getErrorMsg());
        }

        return DefaultWebResponse.Builder.success(dto);
    }

    private CheckDuplicationResultDTO checkFileName(String deskSoftName, DistributeFileDTO fileDTO) {
        Assert.hasText(deskSoftName, "deskSoftName can not be blank");
        CheckDuplicationResultDTO dto = new CheckDuplicationResultDTO();

        // 检查文件名长度
        if (deskSoftName.length() > FILE_NAME_MAX_LENGTH) {
            dto.setHasDuplication(Boolean.TRUE);
            dto.setErrorMsg(LocaleI18nResolver.resolve(FileDistributionBusinessKey.RCDC_RCO_FILE_DISTRIBUTION_FILE_NAME_LENGTH_ILLEGAL));
            return dto;
        }

        // 检查文件名是否重复
        if (fileDTO != null) {
            dto.setHasDuplication(Boolean.TRUE);
            dto.setErrorMsg(LocaleI18nResolver.resolve(FileDistributionBusinessKey.RCDC_RCO_FILE_DISTRIBUTION_FILE_NAME_REPEATED));
            return dto;
        }

        dto.setHasDuplication(Boolean.FALSE);
        return dto;
    }

    /**
     * 变更文件描述信息。
     *
     * @param request        请求参数
     * @return DefaultWebResponse 响应参数
     * @throws BusinessException 业务异常
     */
    @RequestMapping("edit")
    public DefaultWebResponse updateDeskSoftNote(DistributeFileEditWebRequest request) throws BusinessException {
        Assert.notNull(request, "UpdateDeskSoftNoteWebRequest is null");
        String logName = request.getId().toString();
        try {
            DistributeFileDTO fileDTO = fileDistributionFileManageAPI.findById(request.getId());
            logName = fileDTO.getFileName();
            EditDistributeFileRequest apiRequest = new EditDistributeFileRequest();
            apiRequest.setId(request.getId());
            apiRequest.setDescription(request.getDescription());
            fileDistributionFileManageAPI.editFile(apiRequest);
            // 为操作日志获取安装包名
            auditLogAPI.recordLog(FileDistributionBusinessKey.RCDC_RCO_FILE_DISTRIBUTION_EDIT_FILE_SUCCESS_LOG, logName);
            return DefaultWebResponse.Builder.success(CloudDesktopBusinessKey.RCDC_CLOUDDESKTOP_MODULE_OPERATE_SUCCESS,
                    new String[]{});
        } catch (BusinessException e) {
            auditLogAPI.recordLog(FileDistributionBusinessKey.RCDC_RCO_FILE_DISTRIBUTION_EDIT_FILE_FAIL_LOG, logName,
                    e.getI18nMessage());
            throw new BusinessException(CloudDesktopBusinessKey.RCDC_CLOUDDESKTOP_MODULE_OPERATE_FAIL, e, e.getI18nMessage());
        }
    }

    /**
     * 删除分发文件
     *
     * @param request        请求参数
     * @param builder        批处理参数
     * @return DefaultWebResponse 响应参数
     * @throws BusinessException 业务异常
     */
    @RequestMapping("delete")
    public DefaultWebResponse deleteFile(IdArrWebRequest request, BatchTaskBuilder builder) throws BusinessException {
        Assert.notNull(request, "DeleteDeskSoftWebRequest is null");
        Assert.notNull(builder, "taskBuilder is null");

        final UUID[] idArr = request.getIdArr();
        if (idArr.length == 1) {
            return singleDeleteProcess(idArr[0]);
        } else {
            return batchDeleteProcess(idArr, builder);
        }
    }

    private DefaultWebResponse batchDeleteProcess(UUID[] idArr, BatchTaskBuilder builder)
            throws BusinessException {
        final Iterator<DefaultBatchTaskItem> iterator = Stream.of(idArr)
                .distinct()
                .map(id -> DefaultBatchTaskItem.builder().itemId(id)
                        .itemName(LocaleI18nResolver
                                .resolve(FileDistributionBusinessKey.RCDC_RCO_FILE_DISTRIBUTION_DELETE_ITEM_NAME))
                        .build())
                .iterator();
        DistributeFileDeleteTaskHandler handler = new DistributeFileDeleteTaskHandler(iterator);
        handler.setAuditLogAPI(auditLogAPI);
        handler.setFileDistributionFileManageAPI(fileDistributionFileManageAPI);
        BatchTaskSubmitResult result =
                builder.setTaskName(FileDistributionBusinessKey.RCDC_RCO_FILE_DISTRIBUTION_BATCH_DELETE_TASK_NAME)
                        .setTaskDesc(FileDistributionBusinessKey.RCDC_RCO_FILE_DISTRIBUTION_BATCH_DELETE_TASK_DESC)
                        .registerHandler(handler).start();
        return DefaultWebResponse.Builder.success(result);
    }

    private DefaultWebResponse singleDeleteProcess(UUID fileId) throws BusinessException {
        DistributeFileDTO fileDTO = fileDistributionFileManageAPI.findById(fileId);
        if (fileDTO != null) {
            fileDistributionFileManageAPI.deleteFile(fileId, Constants.SAMBA_APP_PATH);
            auditLogAPI.recordLog(FileDistributionBusinessKey.RCDC_RCO_FILE_DISTRIBUTION_DELETE_ITEM_SUCCESS_DESC, fileDTO.getFileName());
        }
        return DefaultWebResponse.Builder.success(CloudDesktopBusinessKey.RCDC_CLOUDDESKTOP_MODULE_OPERATE_SUCCESS,
                new String[]{});
    }

    /**
     * 分发文件详情
     *
     * @param request 文件ID请求
     * @return 文件详情
     * @throws BusinessException 业务异常
     */
    @RequestMapping("detail")
    public DefaultWebResponse getFileDetail(IdWebRequest request) throws BusinessException {
        Assert.notNull(request, "request cannot be null!");

        DistributeFileDTO fileDTO = fileDistributionFileManageAPI.findById(request.getId());
        if (fileDTO == null) {
            throw new BusinessException(FileDistributionBusinessKey.RCDC_RCO_FILE_DISTRIBUTION_FILE_NOT_EXIST);
        }

        return DefaultWebResponse.Builder.success(fileDTO);
    }
}
