package com.ruijie.rcos.rcdc.rco.module.web.ctrl.clouddesktop;

import com.ruijie.rcos.gss.base.iac.module.annotation.EnableAuthority;
import com.ruijie.rcos.gss.base.iac.module.annotation.NoAuthUrl;
import com.ruijie.rcos.gss.log.module.def.api.BaseAuditLogAPI;
import com.ruijie.rcos.rcdc.appcenter.module.def.api.CbbPushInstallPackageMgmtAPI;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.api.CbbDeskSoftMgmtAPI;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.api.request.PageSearchRequest;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.dto.*;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.enums.DeskSoftType;
import com.ruijie.rcos.rcdc.rco.module.def.api.FileDistributionFileManageAPI;
import com.ruijie.rcos.rcdc.rco.module.def.constants.FilePathContants;
import com.ruijie.rcos.rcdc.rco.module.web.config.annotation.ApiVersion;
import com.ruijie.rcos.rcdc.rco.module.web.config.annotation.ApiVersions;
import com.ruijie.rcos.rcdc.rco.module.web.config.annotation.Version;
import com.ruijie.rcos.rcdc.rco.module.web.ctrl.clouddesktop.batchtask.DeleteDeskSoftBatchHandler;
import com.ruijie.rcos.rcdc.rco.module.web.ctrl.clouddesktop.batchtask.DeleteSingleDeskSoftBatchHandler;
import com.ruijie.rcos.rcdc.rco.module.web.ctrl.clouddesktop.dto.FileInfoDTO;
import com.ruijie.rcos.rcdc.rco.module.web.ctrl.clouddesktop.request.desksoft.*;
import com.ruijie.rcos.rcdc.rco.module.web.ctrl.clouddesktop.request.osfile.BatchCheckFileDuplicationWebRequest;
import com.ruijie.rcos.rcdc.rco.module.web.factory.clouddesktop.UploadDeskSoftHandlerFactory;
import com.ruijie.rcos.sk.base.batch.BatchTaskBuilder;
import com.ruijie.rcos.sk.base.batch.BatchTaskItem;
import com.ruijie.rcos.sk.base.batch.BatchTaskSubmitResult;
import com.ruijie.rcos.sk.base.batch.DefaultBatchTaskItem;
import com.ruijie.rcos.sk.base.exception.BusinessException;
import com.ruijie.rcos.sk.base.i18n.LocaleI18nResolver;
import com.ruijie.rcos.sk.modulekit.api.comm.DefaultPageResponse;
import com.ruijie.rcos.sk.webmvc.api.annotation.FileUpload;
import com.ruijie.rcos.sk.webmvc.api.request.ChunkUploadFile;
import com.ruijie.rcos.sk.webmvc.api.request.PageWebRequest;
import com.ruijie.rcos.sk.webmvc.api.response.DefaultWebResponse;
import com.ruijie.rcos.sk.webmvc.api.response.DownloadWebResponse;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.Assert;
import org.springframework.util.ObjectUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.io.File;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Stream;

import static com.ruijie.rcos.rcdc.rco.module.web.ctrl.filedistribution.FileDistributionBusinessKey.RCDC_RCO_FILE_DISTRIBUTION_FILE_NOT_EXIST;

/**
 * Description: 安装包controller
 * Copyright: Copyright (c) 2018
 * Company: Ruijie Co., Ltd.
 * Create Time: 2018年11月12日
 *
 * @author Ghang
 */
@Controller
@RequestMapping("/rco/deskSoft")
public class DeskSoftCtrl {

    @Autowired
    private CbbDeskSoftMgmtAPI deskSoftMgmtAPI;

    @Autowired
    private UploadDeskSoftHandlerFactory uploadDeskSoftHandlerFactory;

    @Autowired
    private CbbPushInstallPackageMgmtAPI cbbPushInstallPackageMgmtAPI;

    @Autowired
    private FileDistributionFileManageAPI fileDistributionFileManageAPI;

    @Autowired
    private CbbDeskSoftMgmtAPI cbbDeskSoftMgmtAPI;

    @Autowired
    private BaseAuditLogAPI auditLogAPI;

    /**
     * 软件安装包文件名称长度最大值
     */
    private static final int SOFT_NAME_MAX_LENGTH = 128;

    /**
     * 变更安装包描述信息。
     *
     * @param request        请求参数
     * @return DefaultWebResponse 响应参数
     * @throws BusinessException 业务异常
     */
    @ApiOperation("编辑")
    @ApiVersions({@ApiVersion(value = Version.V1_0_0), @ApiVersion(value = Version.V3_1_1, descriptions = {"分级分权-区分权限，加入权限控制"})})
    @RequestMapping("edit")
    @EnableAuthority
    public DefaultWebResponse updateDeskSoftNote(UpdateDeskSoftNoteWebRequest request) throws BusinessException {
        Assert.notNull(request, "UpdateDeskSoftNoteWebRequest is null");
        String logName = request.getId().toString();
        try {
            CbbDeskSoftDTO deskSoftDTO = deskSoftMgmtAPI.getDeskSoft(request.getId());
            logName = deskSoftDTO.getFileName();
            CbbUpdateDeskSoftNoteDTO apiRequest = new CbbUpdateDeskSoftNoteDTO();
            apiRequest.setId(request.getId());
            apiRequest.setNote(request.getNote());
            deskSoftMgmtAPI.updateDeskSoftNote(apiRequest);
            // 为操作日志获取安装包名
            auditLogAPI.recordLog(CloudDesktopBusinessKey.RCDC_CLOUDDESKTOP_DESKSOFT_UPDATE_SUCCESS_LOG, logName);
            return DefaultWebResponse.Builder.success(CloudDesktopBusinessKey.RCDC_CLOUDDESKTOP_MODULE_OPERATE_SUCCESS,
                    new String[]{});
        } catch (BusinessException e) {
            auditLogAPI.recordLog(CloudDesktopBusinessKey.RCDC_CLOUDDESKTOP_DESKSOFT_UPDATE_FAIL_LOG, logName,
                    e.getI18nMessage());
            throw new BusinessException(CloudDesktopBusinessKey.RCDC_CLOUDDESKTOP_MODULE_OPERATE_FAIL, e, e.getI18nMessage());
        }
    }

    /**
     * 分页获取所有安装包描述信息。
     *
     * @param webRequest 请求参数
     * @return GetAllDeskSoftWebResponse 响应参数
     * @throws BusinessException 业务异常
     */
    @RequestMapping("list")
    public DefaultWebResponse getPageDeskSoft(PageWebRequest webRequest) throws BusinessException {
        Assert.notNull(webRequest, "webRequest must not be null");
        PageSearchRequest pageReq = new PageSearchRequest(webRequest);
        DefaultPageResponse<CbbDeskSoftDTO> response = deskSoftMgmtAPI.pageQuery(pageReq);
        return DefaultWebResponse.Builder.success(response);
    }

    /**
     * 删除安装包。
     *
     * @param request        请求参数
     * @param builder        批处理参数
     * @return DefaultWebResponse 响应参数
     * @throws BusinessException 业务异常
     */
    @ApiOperation("删除")
    @ApiVersions({@ApiVersion(value = Version.V1_0_0), @ApiVersion(value = Version.V3_1_1, descriptions = {"分级分权-区分权限，加入权限控制"})})
    @RequestMapping("delete")
    @EnableAuthority
    public DefaultWebResponse deleteDeskSoft(DeleteDeskSoftWebRequest request, BatchTaskBuilder builder) throws BusinessException {
        Assert.notNull(request, "DeleteDeskSoftWebRequest is null");
        Assert.notNull(builder, "taskBuilder is null");
        final UUID[] idArr = request.getIdArr();
        if (idArr.length == 1) {
            return deleteSingleRecord(builder, idArr[0]);
        } else {
            final Iterator<DefaultBatchTaskItem> iterator = Stream.of(idArr).distinct()
                    .map(id -> DefaultBatchTaskItem.builder().itemId(id)
                            .itemName(LocaleI18nResolver.resolve(CloudDesktopBusinessKey.RCDC_CLOUDDESKTOP_DESKSOFT_DELETE_ITEM_NAME)).build())
                    .iterator();
            DeleteDeskSoftBatchHandler handler =
                    new DeleteDeskSoftBatchHandler(iterator, auditLogAPI, deskSoftMgmtAPI, cbbPushInstallPackageMgmtAPI,
                            fileDistributionFileManageAPI);
            BatchTaskSubmitResult result = builder.setTaskName(CloudDesktopBusinessKey.RCDC_CLOUDDESKTOP_DESKSOFT_BATCH_DELETE_TASK_NAME)
                    .setTaskDesc(CloudDesktopBusinessKey.RCDC_CLOUDDESKTOP_DESKSOFT_BATCH_DELETE_TASK_DESC).registerHandler(handler).start();
            return DefaultWebResponse.Builder.success(result);
        }
    }

    private DefaultWebResponse deleteSingleRecord(BatchTaskBuilder builder, final UUID id)
            throws BusinessException {
        DefaultBatchTaskItem batchTaskItem = DefaultBatchTaskItem.builder().itemId(id)
                .itemName(LocaleI18nResolver.resolve(CloudDesktopBusinessKey.RCDC_CLOUDDESKTOP_DESKSOFT_DELETE_SINGLE_ITEM_NAME)).build();
        DeleteSingleDeskSoftBatchHandler handler =
                new DeleteSingleDeskSoftBatchHandler(batchTaskItem, auditLogAPI, deskSoftMgmtAPI, cbbPushInstallPackageMgmtAPI,
                        fileDistributionFileManageAPI);
        BatchTaskSubmitResult result = builder.setTaskName(CloudDesktopBusinessKey.RCDC_CLOUDDESKTOP_DESKSOFT_DELETE_SINGLE_TASK_NAME)
                .setTaskDesc(CloudDesktopBusinessKey.RCDC_CLOUDDESKTOP_DESKSOFT_DELETE_SINGLE_TASK_DESC).registerHandler(handler).start();
        return DefaultWebResponse.Builder.success(result);
    }

    /**
     * 上传安装包。
     *
     * @param file           请求参数
     * @param taskBuilder    批处理任务
     * @return DefaultWebResponse 响应参数
     * @throws BusinessException 业务异常
     */
    @ApiOperation("创建")
    @ApiVersions({@ApiVersion(value = Version.V1_0_0), @ApiVersion(value = Version.V3_1_1, descriptions = {"分级分权-区分权限，加入权限控制"})})
    @RequestMapping("create")
    @EnableAuthority
    @FileUpload(uploadTarget = FilePathContants.SOFT_PATH)
    public DefaultWebResponse uploadDeskSoft(ChunkUploadFile file, BatchTaskBuilder taskBuilder) throws BusinessException {
        Assert.notNull(file, "file is null");
        Assert.notNull(taskBuilder, "taskBuilder is null");

        final BatchTaskItem batchTaskItem = new DefaultBatchTaskItem(UUID.randomUUID(),
                LocaleI18nResolver.resolve(CloudDesktopBusinessKey.RCDC_CLOUDDESKTOP_DESKSOFT_UPLOAD_ITEM_NAME));
        BatchTaskSubmitResult result =
                taskBuilder.setTaskName(CloudDesktopBusinessKey.RCDC_CLOUDDESKTOP_DESKSOFT_UPLOAD_TASK_NAME)
                        .setTaskDesc(CloudDesktopBusinessKey.RCDC_CLOUDDESKTOP_DESKSOFT_UPLOAD_DESC, file.getFileName())
                        .registerHandler(
                                uploadDeskSoftHandlerFactory.createHandler(batchTaskItem, file))
                        .start();

        return DefaultWebResponse.Builder.success(result);
    }

    /**
     * 获取安装包详情
     *
     * @param request 请求参数
     * @return DefaultWebResponse 响应参数
     * @throws BusinessException 业务异常
     */
    @RequestMapping("detail")
    public DefaultWebResponse getDeskSoft(GetDeskSoftWebRequest request) throws BusinessException {
        Assert.notNull(request, "request is null");
        CbbDeskSoftDTO deskSoftDTO = deskSoftMgmtAPI.getDeskSoft(request.getId());
        return DefaultWebResponse.Builder.success(deskSoftDTO);
    }

    /**
     * 命名唯一性校验
     *
     * @param request 请求参数
     * @return DefaultWebResponse 响应参数
     * @throws BusinessException 业务异常
     */
    @RequestMapping("batchCheckDuplication")
    public DefaultWebResponse batchCheckDuplication(BatchCheckFileDuplicationWebRequest request) throws BusinessException {
        Assert.notNull(request, "BatchCheckFileDuplicationWebRequest is null");

        FileInfoDTO[] fileInfoArr = request.getFileInfoArr();
        Set<String> invalidFileNameLengthSet = new HashSet<>();
        Set<String> invalidFileNameSet = new HashSet<>();
        Long totalFileSize = 0L;
        CheckDuplicationResultDTO dto = new CheckDuplicationResultDTO();

        for (FileInfoDTO fileInfoDTO : fileInfoArr) {
            if (fileInfoDTO.getFileName().length() > SOFT_NAME_MAX_LENGTH) {
                invalidFileNameLengthSet.add(fileInfoDTO.getFileName());
            }

            CbbDeskSoftDTO deskSoftDTO = deskSoftMgmtAPI.findByName(fileInfoDTO.getFileName());
            if (deskSoftDTO != null) {
                invalidFileNameSet.add(fileInfoDTO.getFileName());
            }

            totalFileSize += fileInfoDTO.getFileSize();
        }

        if (invalidFileNameLengthSet.size() > 0) {
            dto.setHasDuplication(Boolean.TRUE);
            String msg = getInvalidFileList(invalidFileNameLengthSet);
            dto.setErrorMsg(LocaleI18nResolver.resolve(CloudDesktopBusinessKey.RCDC_CLOUDDESKTOP_BATCH_DESKSOFT_SOFT_NAME_LENGTH_ILLEGAL, msg));
            return DefaultWebResponse.Builder.success(dto);
        }

        if (invalidFileNameSet.size() > 0) {
            dto.setHasDuplication(Boolean.TRUE);
            String msg = getInvalidFileList(invalidFileNameSet);
            dto.setErrorMsg(LocaleI18nResolver.resolve(CloudDesktopBusinessKey.RCDC_CLOUDDESKTOP_BATCH_DESKSOFT_SOFT_NAME_REPETION, msg));
            return DefaultWebResponse.Builder.success(dto);
        }

        // 当文件名校验没有问题后，校验文件存放磁盘容量是否足够
        final CbbCheckDeskSoftServerDiskSizeDTO checkDiskSizeRequest = new CbbCheckDeskSoftServerDiskSizeDTO(totalFileSize);
        CbbCheckServerDiskSizeResultDTO checkDiskSizeResponse = deskSoftMgmtAPI.checkServerDiskSize(checkDiskSizeRequest);
        dto.setHasDuplication(!checkDiskSizeResponse.getServerDiskSizeEnough());
        dto.setErrorMsg(checkDiskSizeResponse.getErrorMsg());
        return DefaultWebResponse.Builder.success(dto);
    }

    private String getInvalidFileList(Set<String> invalidFileNameSet) {
        if (invalidFileNameSet.isEmpty()) {
            // 无异常文件
            return null;
        }

        StringBuffer invalidMsg = new StringBuffer();

        for (String fileName : invalidFileNameSet) {
            invalidMsg.append(fileName).append("、");
        }

        invalidMsg.deleteCharAt(invalidMsg.length() - 1);
        return invalidMsg.toString();
    }

    /**
     * 命名唯一性校验
     *
     * @param request 请求参数
     * @return DefaultWebResponse 响应参数
     * @throws BusinessException 业务异常
     */
    @RequestMapping("checkDuplication")
    public DefaultWebResponse checkDuplication(CheckSoftNameDuplicationWebRequest request) throws BusinessException {
        Assert.notNull(request, "CheckSoftNameDuplicationWebRequest is null");
        CbbDeskSoftDTO deskSoftDTO = deskSoftMgmtAPI.findByName(request.getName());

        CheckDuplicationResultDTO dto = checkDuplication(request.getName(), deskSoftDTO);
        // 当文件名校验没有问题后，校验文件存放磁盘容量是否足够
        if (!dto.getHasDuplication()) {
            final CbbCheckDeskSoftServerDiskSizeDTO checkDiskSizeRequest = new CbbCheckDeskSoftServerDiskSizeDTO(request.getFileSize());
            CbbCheckServerDiskSizeResultDTO checkDiskSizeResponse = deskSoftMgmtAPI.checkServerDiskSize(checkDiskSizeRequest);

            dto.setHasDuplication(!checkDiskSizeResponse.getServerDiskSizeEnough());
            dto.setErrorMsg(checkDiskSizeResponse.getErrorMsg());
        }

        return DefaultWebResponse.Builder.success(dto);
    }

    private CheckDuplicationResultDTO checkDuplication(String deskSoftName, CbbDeskSoftDTO deskSoftDTO) {
        Assert.hasText(deskSoftName, "deskSoftName can not be blank");
        CheckDuplicationResultDTO dto = new CheckDuplicationResultDTO();

        if (deskSoftName.length() > SOFT_NAME_MAX_LENGTH) {

            dto.setHasDuplication(Boolean.TRUE);
            dto.setErrorMsg(LocaleI18nResolver.resolve(CloudDesktopBusinessKey.RCDC_CLOUDDESKTOP_DESKSOFT_SOFT_NAME_LENGTH_ILLEGAL));
            return dto;
        }

        if (deskSoftDTO != null) {
            dto.setHasDuplication(Boolean.TRUE);
            dto.setErrorMsg(LocaleI18nResolver.resolve(CloudDesktopBusinessKey.RCDC_CLOUDDESKTOP_DESKSOFT_SOFT_NAME_REPETION));
            return dto;
        }

        dto.setHasDuplication(Boolean.FALSE);
        return dto;
    }


    /**
     * 文件下载
     *
     * @param request 请求参数
     * @return 返回结果
     * @throws BusinessException 业务异常
     */
    @ApiOperation("下载文件")
    @RequestMapping(value = "/download", method = RequestMethod.GET)
    @NoAuthUrl
    public DownloadWebResponse downloadFile(DownloadDeskSoftRequest request) throws BusinessException {
        Assert.notNull(request, "request is not null");

        CbbDeskSoftDTO deskSoftDTO = cbbDeskSoftMgmtAPI.getDeskSoft(request.getId());
        if (ObjectUtils.isEmpty(deskSoftDTO)) {
            throw new BusinessException(CloudDesktopBusinessKey.RCDC_CLOUDDESKTOP_FILE_NOT_EXIST);
        }

        String fileName = deskSoftDTO.getFileName();
        validateFileTypeAndName(deskSoftDTO);
        File file = getFile(FilePathContants.SOFT_PATH, fileName);

        int lastIndex = fileName.lastIndexOf('.');
        String fileNameWithoutSuffix = fileName.substring(0, lastIndex);
        String suffix = fileName.substring(lastIndex + 1);

        return new DownloadWebResponse.Builder()
                .setContentType("application/octet-stream")
                .setName(fileNameWithoutSuffix, suffix)
                .setFile(file)
                .build();

    }

    private void validateFileTypeAndName(CbbDeskSoftDTO deskSoftDTO) throws BusinessException {
        Assert.notNull(deskSoftDTO, "deskSoftDTO is not null");

        if (DeskSoftType.FILE_DIR.equals(deskSoftDTO.getDeskSoftType())) {
            throw new BusinessException(CloudDesktopBusinessKey.RCDC_CLOUDDESKTOP_DOWNLOAD_FILE_NAME_ERROR, deskSoftDTO.getFileName());
        }

        if (!deskSoftDTO.getFileName().contains(".")) {
            throw new BusinessException(CloudDesktopBusinessKey.RCDC_CLOUDDESKTOP_DOWNLOAD_FILE_FORMAT_ERROR, deskSoftDTO.getFileName());
        }

    }

    private File getFile(String filePath, String fileName) throws BusinessException {

        String generalFilePath = filePath + "/" + fileName;
        File file = new File(generalFilePath);
        if (!file.exists()) {
            throw new BusinessException(RCDC_RCO_FILE_DISTRIBUTION_FILE_NOT_EXIST);
        }

        if (!file.isFile()) {
            throw new BusinessException(CloudDesktopBusinessKey.RCDC_CLOUDDESKTOP_FILE_DOWNLOAD_NOT_EXIST, fileName);
        }
        return file;
    }


}
