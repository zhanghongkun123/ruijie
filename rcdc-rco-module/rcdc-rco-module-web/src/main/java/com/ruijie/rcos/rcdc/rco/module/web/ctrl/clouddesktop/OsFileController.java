package com.ruijie.rcos.rcdc.rco.module.web.ctrl.clouddesktop;

import com.ruijie.rcos.gss.base.iac.module.annotation.EnableAuthority;
import com.ruijie.rcos.gss.log.module.def.api.BaseAuditLogAPI;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.api.CbbOsFileMgmtAPI;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.api.request.osfile.PageQueryOsFileRequest;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.dto.*;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.dto.osfile.CbbCheckServerDiskSizeDTO;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.dto.osfile.CbbEditOsFileDTO;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.dto.osfile.CbbImportOsFileDTO;
import com.ruijie.rcos.rcdc.hciadapter.module.def.api.ComputerClusterServerMgmtAPI;
import com.ruijie.rcos.rcdc.hciadapter.module.def.dto.PlatformComputerClusterDTO;
import com.ruijie.rcos.rcdc.hciadapter.module.def.enums.ImageFormat;
import com.ruijie.rcos.rcdc.rco.module.def.BusinessKey;
import com.ruijie.rcos.rcdc.rco.module.def.constants.FilePathContants;
import com.ruijie.rcos.rcdc.rco.module.web.Constants;
import com.ruijie.rcos.rcdc.rco.module.web.config.annotation.ApiVersion;
import com.ruijie.rcos.rcdc.rco.module.web.config.annotation.ApiVersions;
import com.ruijie.rcos.rcdc.rco.module.web.config.annotation.Version;
import com.ruijie.rcos.rcdc.rco.module.web.ctrl.clouddesktop.batchtask.DeleteOsFileBatchHandler;
import com.ruijie.rcos.rcdc.rco.module.web.ctrl.clouddesktop.builder.CloudDesktopAPIRequestBuilder;
import com.ruijie.rcos.rcdc.rco.module.web.ctrl.clouddesktop.request.osfile.*;
import com.ruijie.rcos.rcdc.rco.module.web.factory.clouddesktop.ImportOsFileHandlerFactory;
import com.ruijie.rcos.rcdc.rco.module.web.factory.clouddesktop.ImportQcow2FileHandlerFactory;
import com.ruijie.rcos.rcdc.terminal.module.def.enums.CbbCpuArchType;
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
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.Assert;
import org.springframework.util.ObjectUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.io.File;
import java.util.*;
import java.util.stream.Stream;

/**
 * Description:
 * Copyright: Copyright (c) 2020
 * Company: Ruijie Co., Ltd.
 * Create Time: 2020/2/17 9:31
 *
 * @author conghaifeng
 */
@Controller
@RequestMapping("/rco/clouddesktop/osFile")
public class OsFileController {

    @Autowired
    private CbbOsFileMgmtAPI cbbOsFileMgmtAPI;

    @Autowired
    private ImportOsFileHandlerFactory importOsFileHandlerFactory;

    @Autowired
    private ImportQcow2FileHandlerFactory importQcow2FileHandlerFactory;

    @Autowired
    private BaseAuditLogAPI auditLogAPI;

    @Autowired
    private ComputerClusterServerMgmtAPI clusterMgmtAPI;

    /**
     * 默认镜像名称正则表达式：只包含中英文，数字，“_”，“-”，“@”，“.”且不能以“_”开头
     */
    private static final String DEFAULT_IMAGE_FILE_REGEX_EXPRESSION = "^[0-9a-zA-Z\\u4e00-\\u9fa5\\.\\-@]{1}[0-9a-zA-Z\\u4e00-\\u9fa5\\.\\-_@]*";

    /**
     * * 分页查询镜像文件
     *
     * @param pageWebRequest 分页查询参数
     * @return DefaultWebResponse 返回结果
     */
    @RequestMapping(value = "list")
    public DefaultWebResponse listOsFile(PageWebRequest pageWebRequest) {
        Assert.notNull(pageWebRequest, "pageWebRequest is not null");
        PageQueryOsFileRequest pageSearchRequest = new PageQueryOsFileRequest(pageWebRequest);

        // 过滤掉计算集群id，查询完再处理
        UUID[] clusterIdArr = fillClusterIdArr(pageSearchRequest);
        final DefaultPageResponse<OsFileDTO> response = cbbOsFileMgmtAPI.pageQueryOsFile(pageSearchRequest);


        if (clusterIdArr.length > 0) {
            try {
                filterOsFileByCluster(response.getItemArr(), clusterIdArr);
            } catch (BusinessException e) {
                return DefaultWebResponse.Builder.fail(CloudDesktopBusinessKey.RCDC_CLOUDDESKTOP_OSFILE_GET_CLUSTER_FAIL_LOG,
                        new String[] {e.getI18nMessage()});
            }
        }
        return DefaultWebResponse.Builder.success(response);
    }

    private UUID[] fillClusterIdArr(PageQueryOsFileRequest pageSearchRequest) {
        MatchEqual[] matchArr = pageSearchRequest.getMatchEqualArr();
        UUID[] clusterIdArr = new UUID[0];
        if (ArrayUtils.isEmpty(matchArr)) {
            return clusterIdArr;
        }
        List<MatchEqual> matchEqualList = new ArrayList<>();
        for (MatchEqual match : matchArr) {
            if (StringUtils.equals(Constants.CLUSTER_ID, match.getName())) {
                clusterIdArr =
                        Arrays.stream(match.getValueArr()).map(item -> UUID.fromString(item.toString())).toArray(UUID[]::new);
            } else {
                matchEqualList.add(match);
            }
        }
        if (matchEqualList.size() > 0) {
            pageSearchRequest.setMatchEqualArr(matchEqualList.toArray(new MatchEqual[matchEqualList.size()]));
        }
        return clusterIdArr;
    }



    private void filterOsFileByCluster(OsFileDTO[] osFileDTOArr, UUID[] clusterIdArr) throws BusinessException {
        if (ArrayUtils.isEmpty(osFileDTOArr) || clusterIdArr.length == 0) {
            return;
        }

        PlatformComputerClusterDTO computerClusterDTO = clusterMgmtAPI.getComputerClusterInfoById(clusterIdArr[0]);
        if (Objects.isNull(computerClusterDTO)) {
            return;
        }
        Arrays.asList(osFileDTOArr).forEach(osFileDTO -> {
            if (CbbCpuArchType.OTHER != osFileDTO.getCpuArch()) {
                if (Objects.isNull(computerClusterDTO.getArchitecture()) || Objects.isNull(osFileDTO.getCpuArch()) ||
                        !computerClusterDTO.getArchitecture().equals(osFileDTO.getCpuArch().getArchName())) {
                    osFileDTO.setCanUsed(false);
                    osFileDTO.setCanUsedMessage(LocaleI18nResolver.resolve(BusinessKey.RCDC_RCO_OS_FILE_CLUSTER_CPU_ARCH_NOT_MATCH,
                            osFileDTO.getImageFileName(), computerClusterDTO.getClusterName()));
                }
            }

        });
    }

    /**
     * * 删除镜像文件
     *
     * @param webRequest     删除参数
     * @param builder        批任务builder
     * @return DefaultWebResponse 返回结果
     * @throws BusinessException 通用异常
     */
    @ApiOperation("驱动删除")
    @ApiVersions({@ApiVersion(value = Version.V1_0_0), @ApiVersion(value = Version.V3_1_1, descriptions = {"分级分权-区分权限，加入权限控制"})})
    @RequestMapping(value = "delete")
    @EnableAuthority
    public DefaultWebResponse deleteOsIsoFile(DeleteOsFileWebRequest webRequest, BatchTaskBuilder builder) throws BusinessException {
        Assert.notNull(webRequest, "webRequest is not null");
        Assert.notNull(builder, "builder is not null");

        final UUID[] idArr = webRequest.getIdArr();

        verifyIsoFileExist(idArr);


        // 创建迭代器
        final Iterator<DefaultBatchTaskItem> iterator = Stream.of(idArr).distinct() //
                .map(id -> DefaultBatchTaskItem.builder() //
                        .itemId(id) //
                        .itemName(LocaleI18nResolver.resolve(CloudDesktopBusinessKey.RCDC_CLOUDDESKTOP_OSFILE_DELETE_ITEM_NAME)) //
                        .build()) //
                .iterator();

        // 创建批处理的handler
        DeleteOsFileBatchHandler handler = new DeleteOsFileBatchHandler(cbbOsFileMgmtAPI, iterator, auditLogAPI);

        BatchTaskSubmitResult result = builder.setTaskName(CloudDesktopBusinessKey.RCDC_CLOUDDESKTOP_OSFILE_BATCH_DELETE_TASK_NAME)//
                .setTaskDesc(CloudDesktopBusinessKey.RCDC_CLOUDDESKTOP_OSFILE_BATCH_DELETE_TASK_DESC)//
                .registerHandler(handler).start();

        return DefaultWebResponse.Builder.success(result);
    }

    private void verifyIsoFileExist(UUID[] idArr) throws BusinessException {
        for (UUID id : idArr) {
            cbbOsFileMgmtAPI.getOsFile(id);
        }
    }

    /**
     * * 显示镜像文件详情
     *
     * @param webRequest 镜像文件详情查询参数
     * @return DefaultWebResponse 返回结果
     * @throws BusinessException 业务异常
     */
    @RequestMapping(value = "detail")
    public DefaultWebResponse detailOsFile(DetailOsFileWebRequest webRequest) throws BusinessException {
        Assert.notNull(webRequest, "webRequest is null");
        try {
            CbbGetOsFileResultDTO response = cbbOsFileMgmtAPI.getOsFile(webRequest.getId());
            return DefaultWebResponse.Builder.success(response);
        } catch (BusinessException e) {
            throw new BusinessException(CloudDesktopBusinessKey.RCDC_CLOUDDESKTOP_OSFILE_DETAIL_FAIL_LOG, e, e.getI18nMessage());
        }

    }

    /**
     * * 编辑镜像文件详情
     *
     * @param webRequest 镜像文件详情查询参数
     * @return DefaultWebResponse 返回结果
     * @throws BusinessException 业务异常
     */

    @ApiOperation("编辑镜像文件详情")
    @ApiVersions({//
            @ApiVersion(value = Version.V1_0_0), //
            @ApiVersion(value = Version.V3_1_1, descriptions = {"分级分权-区分权限，加入权限控制"}),//
            @ApiVersion(value = Version.V3_2_0, descriptions = {"数据D盘"})})//
    @RequestMapping(value = "edit")
    @EnableAuthority
    public DefaultWebResponse editOsFile(EditOsFileWebRequest webRequest) throws BusinessException {
        Assert.notNull(webRequest, "webRequest is null");
        CbbGetOsFileResultDTO cbbGetOsFileResultDTO = new CbbGetOsFileResultDTO();
        String fileName;
        try {
            cbbGetOsFileResultDTO = cbbOsFileMgmtAPI.getOsFile(webRequest.getId());
            fileName = cbbGetOsFileResultDTO.getImageFileName();
        } catch (BusinessException e) {
            fileName = webRequest.getId().toString();
        }
        try {
            // 验证磁盘类型是否允许编辑：
            if (cbbGetOsFileResultDTO.getImageDiskType() != webRequest.getImageDiskType()) {
                validCbbImageDiskType(cbbGetOsFileResultDTO);
            }

            final CbbEditOsFileDTO request = new CbbEditOsFileDTO(webRequest.getId(), webRequest.getNote());
            request.setCbbImageDiskType(webRequest.getImageDiskType());
            if (Objects.nonNull(webRequest.getCpuArch())) {
                request.setCpuArch(webRequest.getCpuArch());
            }
            cbbOsFileMgmtAPI.editOsFile(request);
            auditLogAPI.recordLog(CloudDesktopBusinessKey.RCDC_CLOUDDESKTOP_OS_EDIT_INFO_SUCCESS_LOG, fileName);
            return DefaultWebResponse.Builder.success(CloudDesktopBusinessKey.RCDC_CLOUDDESKTOP_OS_EDIT_INFO_SUCCESS_LOG, new String[]{fileName});
        } catch (BusinessException e) {
            auditLogAPI.recordLog(CloudDesktopBusinessKey.RCDC_CLOUDDESKTOP_OS_EDIT_INFO_FAIL_LOG, e, fileName, e.getI18nMessage());
            throw new BusinessException(CloudDesktopBusinessKey.RCDC_CLOUDDESKTOP_OS_EDIT_INFO_FAIL_LOG, e, fileName, e.getI18nMessage());
        }

    }

    private void validCbbImageDiskType(CbbGetOsFileResultDTO cbbGetOsFileResultDTO) throws BusinessException {
        if (cbbGetOsFileResultDTO.getAllowUpdateDiskType() == false) {
            throw new BusinessException(BusinessKey.RCDC_RCO_NOT_ALLOW_UPDATE_IMAGE_STATE);
        }

    }

    /**
     * * 校验文件唯一性
     *
     * @param webRequest request
     * @return DefaultWebResponse 返回结果
     */
    @RequestMapping(value = "checkDuplication")
    @ApiOperation("校验文件唯一性")
    @ApiVersions({@ApiVersion(value = Version.V1_0_0, descriptions = {"校验文件唯一性"}),
            @ApiVersion(value = Version.V3_1_1, descriptions = {"非法文件提示文件名错误"})})
    public DefaultWebResponse checkDuplication(CheckFileDuplicationWebRequest webRequest) {
        Assert.notNull(webRequest, "webRequest is null");

        CbbHasDuplicationResultDTO dto = checkImageFile(webRequest.getImageFileName());

        // 当文件名校验没有问题后，校验文件存放磁盘容量是否足够
        if (!dto.isHasDuplication()) {
            final CbbCheckServerDiskSizeDTO checkDiskSizeRequest =
                    new CbbCheckServerDiskSizeDTO(webRequest.getImageFileName(), webRequest.getFileSize());
            CbbCheckServerDiskSizeResultDTO checkDiskSizeResponse = cbbOsFileMgmtAPI.checkServerDiskSize(checkDiskSizeRequest);

            dto.setHasDuplication(!checkDiskSizeResponse.getServerDiskSizeEnough());
            dto.setErrorMsg(checkDiskSizeResponse.getErrorMsg());
        }

        return DefaultWebResponse.Builder.success(dto);
    }

    private CbbHasDuplicationResultDTO checkImageFile(String fileName) {
        CbbHasDuplicationResultDTO cbbHasDuplicationResultDTO = new CbbHasDuplicationResultDTO();
        if (!checkFileNameWithoutSuffix(fileName)) {
            cbbHasDuplicationResultDTO.setHasDuplication(true);
            cbbHasDuplicationResultDTO.setErrorMsg(LocaleI18nResolver.resolve(CloudDesktopBusinessKey.RCDC_RCO_IMAGE_FILE_NAME_CHECK_FAIL));
            return cbbHasDuplicationResultDTO;
        }
        Boolean hasDuplication = cbbOsFileMgmtAPI.findByName(fileName) != null;
        cbbHasDuplicationResultDTO.setHasDuplication(hasDuplication);
        return cbbHasDuplicationResultDTO;
    }

    private boolean checkFileNameWithoutSuffix(String fileName) {
        int index = fileName.lastIndexOf('.');
        if (index < 0) {
            return false;
        }
        fileName = fileName.substring(0, index);

        // 镜像文件文件名校验，校验规则：只包含中英文，数字，“_”，“-”，“@”，“.”且不能以“_”开头
        return fileName.matches(DEFAULT_IMAGE_FILE_REGEX_EXPRESSION);
    }


    /**
     * * 导入镜像文件
     *
     * @param chunkUploadFile 上传文件参数
     * @param builder         builder
     * @return DefaultWebResponse 返回结果
     * @throws BusinessException 异常
     */
    @ApiOperation("导入QCOW2镜像文件")
    @ApiVersions({@ApiVersion(value = Version.V1_0_0), @ApiVersion(value = Version.V3_1_1, descriptions = {"分级分权-区分权限，加入权限控制"})})
    @RequestMapping(value = "create", method = RequestMethod.POST)
    @EnableAuthority
    @FileUpload(uploadTarget = FilePathContants.QCOW2_PATH)
    public DefaultWebResponse importOsFile(ChunkUploadFile chunkUploadFile, BatchTaskBuilder builder) throws BusinessException {
        Assert.notNull(chunkUploadFile, "chunkUploadFile is not null");
        Assert.notNull(builder, "builder is not null");

        final CbbImportOsFileDTO request = CloudDesktopAPIRequestBuilder.buildImportOsFileRequest(chunkUploadFile);

        final BatchTaskItem batchTaskItem = new DefaultBatchTaskItem(UUID.randomUUID(),
                LocaleI18nResolver.resolve(CloudDesktopBusinessKey.RCDC_CLOUDDESKTOP_OS_UPLOAD_TASK_NAME));
        BatchTaskSubmitResult result = builder.setTaskName(CloudDesktopBusinessKey.RCDC_CLOUDDESKTOP_OS_UPLOAD_TASK_NAME)
                .setTaskDesc(CloudDesktopBusinessKey.RCDC_CLOUDDESKTOP_OS_UPLOAD_DESC, "")
                .registerHandler(importQcow2FileHandlerFactory.createHandler(batchTaskItem, request)).start();

        return DefaultWebResponse.Builder.success(result);
    }


    /**
     * * 导入镜像文件
     *
     * @param chunkUploadFile 上传文件参数
     * @param builder         builder
     * @return DefaultWebResponse 返回结果
     * @throws BusinessException 异常
     */
    @ApiOperation("导入ISO镜像文件")
    @ApiVersions({@ApiVersion(value = Version.V1_0_0), @ApiVersion(value = Version.V3_1_1, descriptions = {"分级分权-区分权限，加入权限控制"})})
    @RequestMapping(value = "iso/create", method = RequestMethod.POST)
    @FileUpload(uploadTarget = FilePathContants.ISO_PATH)
    @EnableAuthority
    public DefaultWebResponse importIOsFile(ChunkUploadFile chunkUploadFile, BatchTaskBuilder builder) throws BusinessException {
        Assert.notNull(chunkUploadFile, "chunkUploadFile is not null");
        Assert.notNull(builder, "builder is not null");
        final CbbImportOsFileDTO request = CloudDesktopAPIRequestBuilder.buildImportOsFileRequest(chunkUploadFile);

        final BatchTaskItem batchTaskItem = new DefaultBatchTaskItem(UUID.randomUUID(),
                LocaleI18nResolver.resolve(CloudDesktopBusinessKey.RCDC_CLOUDDESKTOP_OS_UPLOAD_TASK_NAME));
        BatchTaskSubmitResult result = builder.setTaskName(CloudDesktopBusinessKey.RCDC_CLOUDDESKTOP_OS_UPLOAD_TASK_NAME)
                .setTaskDesc(CloudDesktopBusinessKey.RCDC_CLOUDDESKTOP_OS_UPLOAD_DESC, "")
                .registerHandler(importOsFileHandlerFactory.createHandler(batchTaskItem, request)).start();

        return DefaultWebResponse.Builder.success(result);
    }


    /**
     * 下载镜像文件
     *
     * @param request 请求参数
     * @return 返回结果
     * @throws BusinessException 业务异常
     */
    @ApiOperation("下载镜像文件")
    @RequestMapping(value = "/download", method = RequestMethod.GET)
    public DownloadWebResponse downloadImageFile(DownloadOsFileWebRequest request) throws BusinessException {
        Assert.notNull(request, "request is not null");

        CbbGetOsFileResultDTO osFile = cbbOsFileMgmtAPI.getOsFile(request.getId());
        if (ObjectUtils.isEmpty(osFile)) {
            throw new BusinessException(CloudDesktopBusinessKey.RCDC_CLOUDDESKTOP_FILE_NOT_EXIST);
        }

        String fileName = osFile.getImageFileName();
        validateFileName(fileName);
        ImageFormat imageFileType = osFile.getImageFileType();

        String filePath = null;
        filePath = getFilePath(imageFileType, filePath);

        File file = getFile(filePath, fileName);

        int lastIndex = fileName.lastIndexOf('.');
        String fileNameWithoutSuffix = fileName.substring(0, lastIndex);
        String suffix = fileName.substring(lastIndex + 1);

        return new DownloadWebResponse.Builder()
                .setContentType("application/octet-stream")
                .setName(fileNameWithoutSuffix, suffix)
                .setFile(file)
                .build();
    }

    private void validateFileName(String fileName) throws BusinessException {
        Assert.hasText(fileName, "fileName is not blank");

        if (!fileName.contains(".")) {
            throw new BusinessException(CloudDesktopBusinessKey.RCDC_CLOUDDESKTOP_DOWNLOAD_FILE_NAME_ERROR, fileName);
        }
    }

    private String getFilePath(ImageFormat imageFileType, String filePath) throws BusinessException {

        if (imageFileType == ImageFormat.ISO) {
            filePath = FilePathContants.ISO_PATH + "/";
        } else if (imageFileType == ImageFormat.QCOW2) {
            filePath = FilePathContants.QCOW2_PATH + "/";
        } else {
            throw new BusinessException(CloudDesktopBusinessKey.RCDC_CLOUDDESKTOP_DOWNLOAD_FILE_TYPE_ERROR);
        }
        return filePath;
    }

    private File getFile(String filePath, String fileName) throws BusinessException {

        String generalFilePath = filePath + fileName;
        File file = new File(generalFilePath);
        if (!file.exists()) {
            throw new BusinessException(CloudDesktopBusinessKey.RCDC_CLOUDDESKTOP_FILE_NOT_EXIST);
        }

        if (!file.isFile()) {
            throw new BusinessException(CloudDesktopBusinessKey.RCDC_CLOUDDESKTOP_FILE_DOWNLOAD_NOT_EXIST, fileName);
        }
        return file;
    }


}
