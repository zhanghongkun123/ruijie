package com.ruijie.rcos.rcdc.rco.module.web.ctrl.deskbackup;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.ruijie.rcos.gss.base.iac.module.annotation.EnableAuthority;
import com.ruijie.rcos.gss.log.module.def.api.BaseAuditLogAPI;
import com.ruijie.rcos.rcdc.backup.module.def.api.CbbBackupDetailAPI;
import com.ruijie.rcos.rcdc.backup.module.def.api.CbbBackupRestoreAPI;
import com.ruijie.rcos.rcdc.backup.module.def.dto.CbbBackupDetailDTO;
import com.ruijie.rcos.rcdc.backup.module.def.dto.CbbResourceDownloadDTO;
import com.ruijie.rcos.rcdc.backup.module.def.enums.BackupStateEnum;
import com.ruijie.rcos.rcdc.backup.module.def.enums.CbbResourceCompressStateEnums;
import com.ruijie.rcos.rcdc.backup.module.def.enums.MetaType;
import com.ruijie.rcos.rcdc.backup.module.def.enums.RecoverType;
import com.ruijie.rcos.rcdc.hciadapter.module.def.api.CloudPlatformManageAPI;
import com.ruijie.rcos.rcdc.hciadapter.module.def.api.ExternalStorageMgmtAPI;
import com.ruijie.rcos.rcdc.hciadapter.module.def.api.StoragePoolServerMgmtAPI;
import com.ruijie.rcos.rcdc.hciadapter.module.def.api.SystemVersionMgmtAPI;
import com.ruijie.rcos.rcdc.hciadapter.module.def.dto.CommonPageQueryRequest;
import com.ruijie.rcos.rcdc.hciadapter.module.def.dto.cloudplatform.CloudPlatformDTO;
import com.ruijie.rcos.rcdc.hciadapter.module.def.dto.externalstorage.ExternalStorageDTO;
import com.ruijie.rcos.rcdc.hciadapter.module.def.dto.request.QueryExternalStorageRequest;
import com.ruijie.rcos.rcdc.hciadapter.module.def.enums.ExternalStorageHealthStateEnum;
import com.ruijie.rcos.rcdc.rco.module.web.config.annotation.ApiVersion;
import com.ruijie.rcos.rcdc.rco.module.web.config.annotation.ApiVersions;
import com.ruijie.rcos.rcdc.rco.module.web.config.annotation.Version;
import com.ruijie.rcos.rcdc.rco.module.web.ctrl.cloudplatform.vo.CloudPlatformVO;
import com.ruijie.rcos.rcdc.rco.module.web.ctrl.deskbackup.batchtask.DeleteServerBackupBatchTaskHandler;
import com.ruijie.rcos.rcdc.rco.module.web.ctrl.deskbackup.batchtask.RecoverCdcResourceSingleTaskHandler;
import com.ruijie.rcos.rcdc.rco.module.web.ctrl.deskbackup.batchtask.SyncExternalStorageSingleTaskHandler;
import com.ruijie.rcos.rcdc.rco.module.web.ctrl.deskbackup.request.DownloadBackupFileRequest;
import com.ruijie.rcos.rcdc.rco.module.web.ctrl.deskbackup.request.RecoverResourceRequest;
import com.ruijie.rcos.rcdc.rco.module.web.ctrl.deskbackup.request.SyncExternalStorageRequest;
import com.ruijie.rcos.rcdc.rco.module.web.ctrl.deskbackup.utils.BackupUtils;
import com.ruijie.rcos.rcdc.rco.module.web.ctrl.deskbackup.vo.ExternalStorageVO;
import com.ruijie.rcos.rcdc.rco.module.web.ctrl.deskbackup.vo.ResourceDownloadVO;
import com.ruijie.rcos.rcdc.rco.module.web.ctrl.deskbackup.vo.ViewBackupDetailVO;
import com.ruijie.rcos.rcdc.rco.module.web.ctrl.user.UserBusinessKey;
import com.ruijie.rcos.rcdc.rco.module.web.response.CommonWebResponse;
import com.ruijie.rcos.rcdc.rco.module.web.util.DateUtil;
import com.ruijie.rcos.sk.base.batch.BatchTaskBuilder;
import com.ruijie.rcos.sk.base.batch.BatchTaskItem;
import com.ruijie.rcos.sk.base.batch.BatchTaskSubmitResult;
import com.ruijie.rcos.sk.base.batch.DefaultBatchTaskItem;
import com.ruijie.rcos.sk.base.config.ConfigFacade;
import com.ruijie.rcos.sk.base.exception.BusinessException;
import com.ruijie.rcos.sk.base.i18n.LocaleI18nResolver;
import com.ruijie.rcos.sk.base.log.Logger;
import com.ruijie.rcos.sk.base.log.LoggerFactory;
import com.ruijie.rcos.sk.connectkit.api.data.base.PageResponse;
import com.ruijie.rcos.sk.modulekit.api.comm.DefaultPageResponse;
import com.ruijie.rcos.sk.modulekit.api.comm.DefaultRequest;
import com.ruijie.rcos.sk.modulekit.api.comm.DtoResponse;
import com.ruijie.rcos.sk.modulekit.api.comm.Response;
import com.ruijie.rcos.sk.pagekit.api.PageQueryBuilderFactory;
import com.ruijie.rcos.sk.pagekit.api.PageQueryRequest;
import com.ruijie.rcos.sk.pagekit.api.PageQueryResponse;
import com.ruijie.rcos.sk.pagekit.api.PageQueryWebConfig;
import com.ruijie.rcos.sk.webmvc.api.request.IdArrWebRequest;
import com.ruijie.rcos.sk.webmvc.api.request.IdWebRequest;
import com.ruijie.rcos.sk.webmvc.api.request.PageWebRequest;
import com.ruijie.rcos.sk.webmvc.api.response.DefaultWebResponse;
import com.ruijie.rcos.sk.webmvc.api.response.DownloadWebResponse;
import com.ruijie.rcos.sk.webmvc.api.vo.ExactMatch;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static com.ruijie.rcos.rcdc.rco.module.web.ctrl.deskbackup.DeskBackupBusinessKey.RCDC_SERVER_RECOVER_RESOURCE_ITEM_NAME;
import static com.ruijie.rcos.rcdc.rco.module.web.ctrl.deskbackup.DeskBackupBusinessKey.RCDC_SERVER_RECOVER_RESOURCE_TASK_DESC;
import static com.ruijie.rcos.rcdc.rco.module.web.ctrl.deskbackup.DeskBackupBusinessKey.RCDC_SERVER_RECOVER_RESOURCE_TASK_NAME;

/**
 * Description: 备份详情
 * Copyright: Copyright (c) 2024
 * Company: Ruijie Co., Ltd.
 * Create Time: 2024年01月30日
 *
 * @author qiuzy
 */
@Api(tags = "备份明细")
@Controller
@RequestMapping("/rco/serverbackup/resource")
@PageQueryWebConfig(url = "/list", dtoType = ViewBackupDetailVO.class)
public class BackupDetailController {

    private static final Logger LOGGER = LoggerFactory.getLogger(BackupDetailController.class);

    private static final String PLATFORM_PASSWORD = "password";

    @Autowired
    private CbbBackupDetailAPI cbbBackupDetailAPI;

    @Autowired
    private CbbBackupRestoreAPI cbbBackupRestoreAPI;

    @Autowired
    private BaseAuditLogAPI auditLogAPI;

    @Autowired
    private StoragePoolServerMgmtAPI storagePoolServerMgmtAPI;

    @Autowired
    private CloudPlatformManageAPI cloudPlatformManageAPI;

    @Autowired
    private PageQueryBuilderFactory pageQueryBuilderFactory;

    @Autowired
    private ExternalStorageMgmtAPI externalStorageMgmtAPI;

    @Autowired
    private ConfigFacade configFacade;

    @Autowired
    private SystemVersionMgmtAPI systemVersionMgmtAPI;

    private static final String DEFAULT_EXTERNAL_STORAGE_QUERY_ID = "id";

    private static final String DEFAULT_DB_NAME_PATH = "datasource.root.default.dbname";

    private static final UUID RCDC_SERVER_BACKUP_SYNC_EXT_SINGLE_TASK_UNIQUE_ID = UUID.randomUUID();

    /**
     * 使用最近的备份点恢复资源,基于备份资源id
     *
     * @param request request
     * @param builder 批任务builder
     * @return 响应信息
     * @throws BusinessException ex
     */
    @ApiOperation(value = "使用资源最近的备份点恢复资源")
    @ApiVersions({@ApiVersion(value = Version.V6_0_50, descriptions = {"使用资源最近的备份点恢复资源"})})
    @RequestMapping(value = "/recover", method = RequestMethod.POST)
    @EnableAuthority
    public DefaultWebResponse recoverNewestResource(RecoverResourceRequest request, BatchTaskBuilder builder) throws BusinessException {
        Assert.notNull(request, "request can not be null");
        Assert.notNull(builder, "builder can not be null");
        final List<CbbBackupDetailDTO> detailList = cbbBackupDetailAPI.getByResourceId(request.getId());
        final CbbBackupDetailDTO newestBackupPoint = getNewestBackupPoint(detailList);

        this.checkRecoverVersion(newestBackupPoint);
        final BatchTaskItem batchTaskItem = new DefaultBatchTaskItem(request.getId(),
                LocaleI18nResolver.resolve(RCDC_SERVER_RECOVER_RESOURCE_ITEM_NAME, newestBackupPoint.getName()));

        final RecoverCdcResourceSingleTaskHandler batchTaskTaskHandler =
                new RecoverCdcResourceSingleTaskHandler(batchTaskItem, auditLogAPI, request, cbbBackupRestoreAPI, newestBackupPoint);
        final BatchTaskSubmitResult result = builder.setTaskName(RCDC_SERVER_RECOVER_RESOURCE_TASK_NAME).setUniqueId(request.getId())
                .setTaskDesc(RCDC_SERVER_RECOVER_RESOURCE_TASK_DESC).registerHandler(batchTaskTaskHandler).start();
        return DefaultWebResponse.Builder.success(result);
    }

    /**
     * 使用选择的历史备份点恢复资源，基于备份详情id
     *
     * @param request request
     * @param builder 批任务builder
     * @return 响应信息
     * @throws BusinessException ex
     */
    @ApiOperation(value = "使用备份点恢复资源")
    @ApiVersions({@ApiVersion(value = Version.V6_0_50, descriptions = {"获取备份对象历史记录列表"})})
    @RequestMapping(value = "/point/recover", method = RequestMethod.POST)
    @EnableAuthority
    public DefaultWebResponse recoverResourceByBackupPoint(RecoverResourceRequest request, BatchTaskBuilder builder) throws BusinessException {
        Assert.notNull(request, "request can not be null");
        Assert.notNull(builder, "builder can not be null");

        CbbBackupDetailDTO backupDTO = cbbBackupDetailAPI.getBackupDetailById(request.getId());
        this.checkRecoverVersion(backupDTO);
        final BatchTaskItem batchTaskItem =
                new DefaultBatchTaskItem(request.getId(), LocaleI18nResolver.resolve(RCDC_SERVER_RECOVER_RESOURCE_ITEM_NAME, backupDTO.getName()));

        final RecoverCdcResourceSingleTaskHandler batchTaskTaskHandler =
                new RecoverCdcResourceSingleTaskHandler(batchTaskItem, auditLogAPI, request, cbbBackupRestoreAPI, backupDTO);
        final BatchTaskSubmitResult result = builder.setTaskName(RCDC_SERVER_RECOVER_RESOURCE_TASK_NAME).setUniqueId(backupDTO.getResourceId())
                .setTaskDesc(RCDC_SERVER_RECOVER_RESOURCE_TASK_DESC).registerHandler(batchTaskTaskHandler).start();
        return DefaultWebResponse.Builder.success(result);
    }

    /**
     * 部分资源相同版本才能恢复
     */
    private void checkRecoverVersion(CbbBackupDetailDTO detailDTO) throws BusinessException {
        DtoResponse<String> versionDto = systemVersionMgmtAPI.obtainSystemReleaseVersion(new DefaultRequest());
        if (versionDto == null || versionDto.getStatus() == null) {
            throw new BusinessException(DeskBackupBusinessKey.RCDC_BACKUP_GET_VERSION_FAIL);
        }

        if (versionDto.getStatus() != Response.Status.SUCCESS) {
            LOGGER.error(String.format("获取系统版本异常,获取状态为：%s", versionDto.getStatus()));
            throw new BusinessException(DeskBackupBusinessKey.RCDC_BACKUP_GET_VERSION_FAIL);
        }

        String backupVersion = detailDTO.getBackupVersion();
        if (MetaType.isVersionChangeRegenerate(detailDTO.getMetaType()) && !backupVersion.equals(versionDto.getDto())) {
            throw new BusinessException(DeskBackupBusinessKey.RCDC_SERVER_RECOVER_VERSION_DIFFERENT, versionDto.getDto(),
                    backupVersion);
        }
    }



    /**
     * 下载备份文件
     *
     * @param request request
     * @return 响应信息
     * @throws BusinessException ex
     */
    @ApiOperation(value = "下载备份文件")
    @ApiVersions({@ApiVersion(value = Version.V6_0_50, descriptions = {"下载备份文件"})})
    @RequestMapping(value = "/point/download", method = RequestMethod.GET)
    @EnableAuthority
    public DefaultWebResponse downloadFile(DownloadBackupFileRequest request) throws BusinessException {
        Assert.notNull(request, "request can not be null");

        final CbbBackupDetailDTO detailDTO = cbbBackupDetailAPI.getBackupDetailById(request.getId());
        return this.downloadInternal(detailDTO);
    }

    /**
     * 下载备份文件
     *
     * @param request request
     * @return 响应信息
     * @throws BusinessException ex
     */
    @ApiOperation(value = "下载备份文件")
    @ApiVersions({@ApiVersion(value = Version.V6_0_50, descriptions = {"下载备份文件"})})
    @RequestMapping(value = "/download", method = RequestMethod.GET)
    @EnableAuthority
    public DefaultWebResponse downloadNewestFile(DownloadBackupFileRequest request) throws BusinessException {
        Assert.notNull(request, "request can not be null");

        final List<CbbBackupDetailDTO> backupDetailDTOList = cbbBackupDetailAPI.getByResourceId(request.getId());
        final CbbBackupDetailDTO detailDTO = getNewestBackupPoint(backupDetailDTOList);
        return this.downloadInternal(detailDTO);
    }

    private DefaultWebResponse downloadInternal(CbbBackupDetailDTO detailDTO) throws BusinessException {
        checkAllowDownloadType(detailDTO);

        final String filePath = cbbBackupDetailAPI.generateBackupFilePath(detailDTO.getId());
        ResourceDownloadVO response = new ResourceDownloadVO();
        response.setId(detailDTO.getId());
        if (StringUtils.isEmpty(filePath)) {
            // 等待压缩中...
            response.setState(CbbResourceCompressStateEnums.DOING);
            return DefaultWebResponse.Builder.success(response);
        }
        response.setState(CbbResourceCompressStateEnums.DONE);
        String downloadFileName = this.getDownloadFileName(filePath, detailDTO.getName());
        response.setFileName(downloadFileName);
        return DefaultWebResponse.Builder.success(response);
    }

    private void checkAllowDownloadType(CbbBackupDetailDTO backupDetailDTO) throws BusinessException {

        final boolean isForbbidDownloadType = MetaType.isForbbidDownloadType(backupDetailDTO.getMetaType());
        if (isForbbidDownloadType) {
            throw new BusinessException(DeskBackupBusinessKey.RCDC_SERVER_BACKUP_RESOURCE_NOT_ALLOW_DOWNLOAD);
        }
    }

    /**
     * 获取下载文件压缩状态
     *
     * @param request 请求参数
     * @return 请求结果
     * @throws BusinessException 业务异常
     */
    @ApiOperation(value = "获取下载文件压缩状态")
    @ApiVersions({@ApiVersion(value = Version.V6_0_50, descriptions = {"获取下载文件压缩状态"})})
    @RequestMapping(value = "/getResourceDownloadState", method = RequestMethod.GET)
    @EnableAuthority
    public DefaultWebResponse getResourceDownloadState(DownloadBackupFileRequest request) throws BusinessException {
        Assert.notNull(request, "request can not be null");

        CbbResourceDownloadDTO dto;
        CbbBackupDetailDTO cbbBackupDetailDTO = cbbBackupDetailAPI.getBackupDetailById(request.getId());
        try {
            dto = cbbBackupDetailAPI.getResourceCompressCacheByDetailId(cbbBackupDetailDTO);
        } catch (BusinessException e) {
            LOGGER.error("获取资源[{}]压缩状态失败", cbbBackupDetailDTO.getName(), e);
            throw new BusinessException(DeskBackupBusinessKey.RCDC_FIND_RESOURCE_COMPRESS_STATE_FAIL, e, cbbBackupDetailDTO.getName(),
                    e.getI18nMessage());
        }

        ResourceDownloadVO response = new ResourceDownloadVO();
        response.setState(dto.getState());
        if (CbbResourceCompressStateEnums.DONE == dto.getState()) {
            response.setId(request.getId());
            response.setFileName(this.getDownloadFileName(dto.getFilePath(), cbbBackupDetailDTO.getName()));
            auditLogAPI.recordLog(DeskBackupBusinessKey.RCDC_RESOURCE_COMPRESS_SUCCESS_LOG, cbbBackupDetailDTO.getName());
        } else if (CbbResourceCompressStateEnums.FAULT == dto.getState()) {
            auditLogAPI.recordLog(DeskBackupBusinessKey.RCDC_RESOURCE_COMPRESS_FAIL_LOG, cbbBackupDetailDTO.getName());
        }

        return DefaultWebResponse.Builder.success(response);
    }

    /**
     * 下载备份资源
     *
     * @param request 请求参数
     * @return 请求结果
     * @throws BusinessException 业务异常
     * @throws IOException       io异常
     */
    @ApiOperation(value = "下载备份资源")
    @ApiVersions({@ApiVersion(value = Version.V6_0_50, descriptions = {"下载备份资源"})})
    @RequestMapping(value = "/downloadResource", method = RequestMethod.GET)
    @EnableAuthority
    public DownloadWebResponse downloadResource(DownloadBackupFileRequest request) throws BusinessException, IOException {
        Assert.notNull(request, "request can not be null");

        CbbBackupDetailDTO cbbBackupDetailDTO = cbbBackupDetailAPI.getBackupDetailById(request.getId());
        String filePath = cbbBackupDetailAPI.getResourceCompressPathByDetailId(cbbBackupDetailDTO);

        return getDownloadWebResponse(filePath, cbbBackupDetailDTO.getName());
    }

    private String getDownloadFileName(String filePath, String backName) {
        final File file = new File(filePath);
        String fileExtension = extractFileExtension(file.getName());
        String fileNamePre = extractFileNameWithoutExtension(backName, fileExtension);

        return fileNamePre + "." + fileExtension;
    }

    private DownloadWebResponse getDownloadWebResponse(String filePath, String backName) {
        final File file = new File(filePath);
        String fileExtension = extractFileExtension(file.getName());
        String fileNamePre = extractFileNameWithoutExtension(backName, fileExtension);

        return new DownloadWebResponse.Builder()
                .setContentType("application/octet-stream")
                .setName(fileNamePre, fileExtension)
                .setFile(file)
                .build();
    }

    private static String extractFileExtension(String fileName) {
        int lastIndex = fileName.lastIndexOf('.');
        if (lastIndex != -1) {
            return fileName.substring(lastIndex + 1);
        } else {
            return "bak";
        }
    }

    private static String extractFileNameWithoutExtension(String backName, String fileExtension) {
        int extensionIndex = backName.lastIndexOf("." + fileExtension);
        if (extensionIndex != -1) {
            return backName.substring(0, extensionIndex);
        } else {
            return backName;
        }
    }

    /**
     * 获取备份对象历史记录列表
     *
     * @param request request
     * @return 响应信息
     * @throws BusinessException ex
     */
    @ApiOperation(value = "获取备份对象历史记录列表")
    @ApiVersions({@ApiVersion(value = Version.V6_0_50, descriptions = {"获取备份对象历史记录列表"})})
    @RequestMapping(value = "/point/list", method = RequestMethod.POST)
    public DefaultWebResponse pointList(PageQueryRequest request) throws BusinessException {
        Assert.notNull(request, "request can not be null");
        PageQueryResponse<CbbBackupDetailDTO> dtoResponse = cbbBackupDetailAPI.pageQuery(request);
        CbbBackupDetailDTO[] itemArr = dtoResponse.getItemArr();
        if (ArrayUtils.isEmpty(itemArr)) {
            return DefaultWebResponse.Builder.success(dtoResponse);
        }

        for (CbbBackupDetailDTO cbbBackupDetailDTO : itemArr) {
            CbbBackupDetailDTO dto = cbbBackupDetailAPI.getBackupDetailById(cbbBackupDetailDTO.getId());
            cbbBackupDetailDTO.setSize(dto.getSize());
        }
        PageQueryResponse<CbbBackupDetailDTO> voResponse = new PageQueryResponse<>(itemArr, dtoResponse.getTotal());
        return DefaultWebResponse.Builder.success(voResponse);
    }

    private CbbBackupDetailDTO getNewestBackupPoint(List<CbbBackupDetailDTO> detailList) throws BusinessException {
        return detailList.stream().filter(item -> item.getBackupState() == BackupStateEnum.DONE)
                .max(Comparator.comparing(CbbBackupDetailDTO::getCreateTime))
                .orElseThrow(() -> new BusinessException(DeskBackupBusinessKey.RCDC_SERVER_BACKUP_NEWEST_BACKUP_POINT_NOT_EXIST));
    }

    /**
     * 删除服务器备份,指定资源id删除
     *
     * @param idArrWebRequest idArrWebRequest 备份id
     * @param builder 批量任务创建对象
     * @return 响应
     * @throws BusinessException 业务异常
     */
    @ApiOperation(value = "删除服务器备份")
    @ApiVersions({@ApiVersion(value = Version.V6_0_50, descriptions = {"删除服务器备份"})})
    @RequestMapping(value = "delete", method = RequestMethod.POST)
    @EnableAuthority
    public DefaultWebResponse delete(IdArrWebRequest idArrWebRequest, BatchTaskBuilder builder) throws BusinessException {
        Assert.notNull(idArrWebRequest, "idArrWebRequest must not be null");
        Assert.notNull(builder, "builder can not be null");

        // 资源ID集，需要删除资源id对应的全部资源历史
        UUID[] idArr = idArrWebRequest.getIdArr();
        final Iterator<DefaultBatchTaskItem> iterator = Arrays.stream(idArr).distinct().map(id -> DefaultBatchTaskItem.builder().itemId(id)
                .itemName(DeskBackupBusinessKey.RCDC_DESK_BACKUP_DELETE_TASK_NAME, new String[] {}).build()).iterator();
        DeleteServerBackupBatchTaskHandler batchTaskHandler = new DeleteServerBackupBatchTaskHandler(cbbBackupDetailAPI, iterator, auditLogAPI);
        BatchTaskSubmitResult result = builder.setTaskName(DeskBackupBusinessKey.RCDC_SERVER_BACKUP_DELETE_TASK_NAME)
                .setTaskDesc(DeskBackupBusinessKey.RCDC_SERVER_BACKUP_DELETE_TASK_DESC).enableParallel().registerHandler(batchTaskHandler).start();
        return DefaultWebResponse.Builder.success(result);
    }

    /**
     * 删除服务器备份，指定详情id删除
     *
     * @param idArrWebRequest idArrWebRequest 备份id
     * @return 响应
     * @throws BusinessException 业务异常
     */
    @ApiOperation(value = "删除备份资源历史")
    @ApiVersions({@ApiVersion(value = Version.V6_0_50, descriptions = {"删除备份资源历史"})})
    @RequestMapping(value = "/point/delete", method = RequestMethod.POST)
    @EnableAuthority
    public DefaultWebResponse pointDelete(IdArrWebRequest idArrWebRequest) throws BusinessException {
        Assert.notNull(idArrWebRequest, "idArrWebRequest must not be null");

        UUID[] idArr = idArrWebRequest.getIdArr();
        for (UUID detailId : idArr) {
            deleteOne(detailId);
        }

        return DefaultWebResponse.Builder.success(UserBusinessKey.RCDC_RCO_MODULE_OPERATE_SUCCESS, new String[] {});
    }

    private void deleteOne(UUID detailId) throws BusinessException {
        CbbBackupDetailDTO backupDetailDTO = cbbBackupDetailAPI.getBackupDetailById(detailId);
        String name = backupDetailDTO.getName();
        Date backupEndTime = backupDetailDTO.getBackupEndTime();
        final String backupFormatTime = DateUtil.formatDate(backupEndTime, DateUtil.YYYY_MM_DD_HH24MISS);
        try {
            cbbBackupDetailAPI.deleteBackupDetail(detailId);
            auditLogAPI.recordLog(DeskBackupBusinessKey.RCDC_SERVER_BACKUP_RESOURCE_DELETE_SUCCESS_LOG, name, backupFormatTime);
        } catch (BusinessException e) {
            auditLogAPI.recordLog(DeskBackupBusinessKey.RCDC_SERVER_BACKUP_RESOURCE_DELETE_FAIL_LOG, name, e.getI18nMessage());
            LOGGER.error("删除服务器定时备份失败", e);
            throw new BusinessException(DeskBackupBusinessKey.RCDC_SERVER_BACKUP_DETAIL_DELETE_FAIL, e, name, e.getI18nMessage());
        }
    }

    /**
     * 获取全部存在备份数据的外置存储列表
     *
     * @return 响应
     * @throws BusinessException 业务异常
     */
    @ApiOperation(value = "获取外置存储列表")
    @ApiVersions({@ApiVersion(value = Version.V6_0_50, descriptions = {"获取外置存储列表"})})
    @RequestMapping(value = "listExternalStorageInfo", method = RequestMethod.POST)
    public CommonWebResponse<DefaultPageResponse<ExternalStorageVO>> listExternalStorageInfo() throws BusinessException {

        DefaultPageResponse<ExternalStorageVO> response = new DefaultPageResponse<>();
        try {
            CommonPageQueryRequest queryRequest = new CommonPageQueryRequest();
            queryRequest.setPage(0);
            queryRequest.setLimit(1000);
            PageResponse<ExternalStorageDTO> externalStorageList = externalStorageMgmtAPI.listExternalStorageInfo(queryRequest);
            ExternalStorageVO[] externalStorageArr = Arrays.stream(externalStorageList.getItems()).filter(
                dto -> dto.getState() == ExternalStorageHealthStateEnum.HEALTHY || dto.getState() == ExternalStorageHealthStateEnum.WARNING)
                    .map(BackupUtils::convertExternalStorageVO).toArray(ExternalStorageVO[]::new);
            response.setItemArr(externalStorageArr);
            response.setTotal(externalStorageArr.length);
        } catch (BusinessException e) {
            LOGGER.error("获取外置存储列表失败", e);
            return CommonWebResponse.fail(DeskBackupBusinessKey.RCDC_DESK_BACKUP_OPERATE_FAIL, new String[] {e.getI18nMessage()});
        }
        return CommonWebResponse.success(response);
    }

    /**
     * 根据存储池ID查询存在备份的平台列表
     *
     * @param request request
     * @return 响应
     * @throws BusinessException 业务异常
     */
    @ApiOperation(value = "根据存储池ID查询已备份的平台列表")
    @ApiVersions({@ApiVersion(value = Version.V6_0_50, descriptions = {"根据存储池ID查询已备份的平台列表"})})
    @RequestMapping(value = "listPlatformByExternalStorageId", method = RequestMethod.POST)
    public DefaultWebResponse listPlatformByExternalStorageId(PageWebRequest request) throws BusinessException {
        Assert.notNull(request, "request must not be null");

        String id = this.getId(request);
        List<CloudPlatformDTO> dtoList = cbbBackupDetailAPI.findBackupPlatformByExternalStorageId(UUID.fromString(id));
        final CloudPlatformVO[] cloudPlatformArr = dtoList.stream().map(item -> {
            final CloudPlatformVO cloudPlatformVO = new CloudPlatformVO();
            BeanUtils.copyProperties(item, cloudPlatformVO, "extendConfig");
            if (StringUtils.isNotBlank(item.getExtendConfig())) {
                JSONObject extendConfig = JSON.parseObject(item.getExtendConfig());
                extendConfig.remove(PLATFORM_PASSWORD);
                cloudPlatformVO.setExtendConfig(extendConfig);
            }
            return cloudPlatformVO;
        }).toArray(CloudPlatformVO[]::new);
        return DefaultWebResponse.Builder.success(new PageQueryResponse(cloudPlatformArr, cloudPlatformArr.length));
    }

    private String getId(PageWebRequest request) throws BusinessException {
        if (ArrayUtils.isNotEmpty(request.getExactMatchArr())) {
            for (ExactMatch exactMatch : request.getExactMatchArr()) {
                if (DEFAULT_EXTERNAL_STORAGE_QUERY_ID.equals(exactMatch.getName()) && exactMatch.getValueArr().length > 0) {
                    return exactMatch.getValueArr()[0];
                }
            }
        }
        throw new BusinessException(DeskBackupBusinessKey.RCDC_EXTERNAL_STORAGE_ID_CANNOT_BE_EMPTY);
    }

    /**
     * 同步外置存储备份信息
     *
     * @param request 外置存储id/云平台id
     * @param builder 批量任务创建对象
     * @return 响应报文
     * @throws BusinessException 业务异常
     */
    @ApiOperation(value = "同步外置存储信息")
    @ApiVersions({@ApiVersion(value = Version.V6_0_50, descriptions = {"同步外置存储信息"})})
    @RequestMapping(value = "syncExternalStorage", method = RequestMethod.POST)
    @EnableAuthority
    public DefaultWebResponse syncExternalStorage(SyncExternalStorageRequest request, BatchTaskBuilder builder) throws BusinessException {
        Assert.notNull(request, "request must not be null");
        Assert.notNull(builder, "BatchTaskBuilder must not be null");

        final BatchTaskItem batchTaskItem = DefaultBatchTaskItem.builder().itemId(request.getExternalStorageId())
                .itemName(LocaleI18nResolver.resolve(DeskBackupBusinessKey.RCDC_SERVER_BACKUP_SYNC_EXT_ITEM_NAME)).build();
        SyncExternalStorageSingleTaskHandler handler = new SyncExternalStorageSingleTaskHandler(cbbBackupDetailAPI, batchTaskItem, auditLogAPI);
        String externalStorageName = resolveExternalStorageName(request.getExternalStorageId());
        String platformName = resolvePlatformName(request.getPlatformId());
        handler.setPlatformId(request.getPlatformId());
        handler.setPlatformName(platformName);
        handler.setExternalStorageName(externalStorageName);
        BatchTaskSubmitResult result = builder.setTaskName(DeskBackupBusinessKey.RCDC_SERVER_BACKUP_SYNC_EXT_SINGLE_TASK_NAME)
                .setTaskDesc(DeskBackupBusinessKey.RCDC_SERVER_BACKUP_SYNC_EXT_SINGLE_TASK_DESC, externalStorageName)
                .setUniqueId(RCDC_SERVER_BACKUP_SYNC_EXT_SINGLE_TASK_UNIQUE_ID).registerHandler(handler).start();
        return DefaultWebResponse.Builder.success(result);
    }

    /**
     * 根据外置存储id，查询外置存储名称
     *
     * @param externalStorageId 外置存储id
     * @return 外置存储名称
     */
    private String resolveExternalStorageName(UUID externalStorageId) {

        String externalStorageName = "";
        try {
            QueryExternalStorageRequest request = new QueryExternalStorageRequest();
            request.setExternalStorageId(externalStorageId);
            ExternalStorageDTO externalStorageDTO = externalStorageMgmtAPI.findExternalStorageInfoById(request);
            externalStorageName = Optional.ofNullable(externalStorageDTO).map(ExternalStorageDTO::getName).orElse(externalStorageId.toString());
            LOGGER.info("根据外置ID[{}]查询外置存储名称为[{}]", externalStorageId, externalStorageName);
            return externalStorageName;
        } catch (BusinessException e) {
            LOGGER.error(String.format("查询外置存储[%s]信息失败", externalStorageId), e);
            return externalStorageId.toString();
        }
    }

    /**
     * 根据平台id，查询云平台名称
     *
     * @param platformId 云平台ID
     * @return 云平台名称
     */
    private String resolvePlatformName(UUID platformId) {
        String platformName = "";
        try {
            CloudPlatformDTO cloudPlatformDTO = cloudPlatformManageAPI.getInfoById(platformId);
            platformName = Optional.ofNullable(cloudPlatformDTO).map(CloudPlatformDTO::getName).orElse(platformId.toString());
            LOGGER.info("根据云平台ID[{}]查询云平台名称为[{}]", platformId, platformName);
            return platformName;
        } catch (Exception e) {
            LOGGER.error(String.format("查询云平台[%s]信息失败", platformId), e);
            return platformId.toString();
        }
    }

    @ApiOperation(value = "查询资源可恢复类型")
    @ApiVersions({@ApiVersion(value = Version.V6_0_50, descriptions = {"查询资源可恢复类型"})})
    @RequestMapping(value = "getRecoverType", method = RequestMethod.POST)
    private DefaultWebResponse getRecoverTypeList(IdWebRequest request) throws BusinessException {
        Assert.notNull(request, "request cannot be null");

        CbbBackupDetailDTO dto = cbbBackupDetailAPI.getBackupDetailById(request.getId());

        List<RecoverType> recoverTypeList = determineRecoverTypeList(dto);
        return DefaultWebResponse.Builder.success(recoverTypeList);
    }

    private List<RecoverType> determineRecoverTypeList(CbbBackupDetailDTO dto) {
        String defaultDbName = this.configFacade.read(DEFAULT_DB_NAME_PATH);
        List<RecoverType> recoverTypeList = new ArrayList<>();

        if (dto.getMetaType() == MetaType.DATA_BASE && dto.getName().equals(defaultDbName)) {
            recoverTypeList.addAll(RecoverType.getRcdcDefaultRecoverTypeList());
        } else if (dto.getMetaType() == MetaType.IMAGE_TEMPLATE || dto.getMetaType() == MetaType.APP_SOFTWARE) {
            recoverTypeList.addAll(RecoverType.getImageTemplateAndAppSoftRecoverTypeList());
        } else {
            recoverTypeList.add(RecoverType.SINGLE_COVER);
        }

        return recoverTypeList;
    }

}
