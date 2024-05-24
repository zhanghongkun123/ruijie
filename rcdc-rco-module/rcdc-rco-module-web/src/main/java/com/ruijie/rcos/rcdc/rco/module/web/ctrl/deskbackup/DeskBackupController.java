package com.ruijie.rcos.rcdc.rco.module.web.ctrl.deskbackup;

import java.util.Arrays;
import java.util.Iterator;
import java.util.UUID;
import java.util.stream.Stream;

import com.ruijie.rcos.rcdc.hciadapter.module.def.dto.CommonPageQueryRequest;
import com.ruijie.rcos.rcdc.hciadapter.module.def.dto.request.QueryExternalStorageRequest;
import com.ruijie.rcos.rcdc.rco.module.def.constants.Constants;
import com.ruijie.rcos.rcdc.rco.module.web.ctrl.deskbackup.utils.BackupUtils;
import com.ruijie.rcos.rcdc.rco.module.web.ctrl.deskbackup.vo.ExternalStorageVO;
import com.ruijie.rcos.sk.connectkit.api.data.base.PageResponse;
import com.ruijie.rcos.sk.pagekit.api.Match;
import com.ruijie.rcos.sk.pagekit.api.match.ExactMatch;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.ruijie.rcos.gss.base.iac.module.annotation.EnableAuthority;
import com.ruijie.rcos.gss.log.module.def.api.BaseAuditLogAPI;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.api.CbbVDIDeskBackupAPI;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.deskbackup.dto.CbbDeskBackupDTO;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.deskbackup.dto.CbbDeskBackupDetailDTO;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.deskbackup.enums.CbbDeskBackupStateEnum;
import com.ruijie.rcos.rcdc.hciadapter.module.def.api.ExternalStorageMgmtAPI;
import com.ruijie.rcos.rcdc.hciadapter.module.def.dto.externalstorage.ExternalStorageDTO;
import com.ruijie.rcos.rcdc.hciadapter.module.def.enums.ExternalStorageHealthStateEnum;
import com.ruijie.rcos.rcdc.rco.module.def.api.DeskBackupAPI;
import com.ruijie.rcos.rcdc.rco.module.def.api.UserDesktopMgmtAPI;
import com.ruijie.rcos.rcdc.rco.module.def.api.dto.CloudDesktopDetailDTO;
import com.ruijie.rcos.rcdc.rco.module.web.config.annotation.ApiVersion;
import com.ruijie.rcos.rcdc.rco.module.web.config.annotation.ApiVersions;
import com.ruijie.rcos.rcdc.rco.module.web.config.annotation.Version;
import com.ruijie.rcos.rcdc.rco.module.web.ctrl.deskbackup.batchtask.CancelDeskBackupBatchTaskHandler;
import com.ruijie.rcos.rcdc.rco.module.web.ctrl.deskbackup.batchtask.CreateDeskBackupBatchTaskHandler;
import com.ruijie.rcos.rcdc.rco.module.web.ctrl.deskbackup.batchtask.DeleteDeskBackupBatchTaskHandler;
import com.ruijie.rcos.rcdc.rco.module.web.ctrl.deskbackup.batchtask.RecoverDeskBackupBatchTaskHandler;
import com.ruijie.rcos.rcdc.rco.module.web.ctrl.deskbackup.request.CheckBackupNameDuplicationRequest;
import com.ruijie.rcos.rcdc.rco.module.web.ctrl.deskbackup.request.CreateDeskBackupRequest;
import com.ruijie.rcos.rcdc.rco.module.web.ctrl.deskbackup.request.EditDeskBackupRequest;
import com.ruijie.rcos.rcdc.rco.module.web.ctrl.deskbackup.request.FindDefaultBackupNameRequest;
import com.ruijie.rcos.rcdc.rco.module.web.ctrl.deskbackup.response.CheckBackupFullResponse;
import com.ruijie.rcos.rcdc.rco.module.web.ctrl.deskbackup.response.DeskBackupInfoResponse;
import com.ruijie.rcos.rcdc.rco.module.web.ctrl.deskbackup.response.DeskBackupPageQueryResponse;
import com.ruijie.rcos.rcdc.rco.module.web.ctrl.deskbackup.response.FindDefaultBackupNameResponse;
import com.ruijie.rcos.rcdc.rco.module.web.ctrl.deskbackup.response.GetMaxBackupsResponse;
import com.ruijie.rcos.rcdc.rco.module.web.ctrl.desksnapshot.response.CheckNameDuplicationResponse;
import com.ruijie.rcos.rcdc.rco.module.web.response.CommonWebResponse;
import com.ruijie.rcos.rcdc.rco.module.web.service.CloudDesktopWebService;
import com.ruijie.rcos.rcdc.rco.module.web.util.PageValidationUtils;
import com.ruijie.rcos.sk.base.batch.BatchTaskBuilder;
import com.ruijie.rcos.sk.base.batch.BatchTaskSubmitResult;
import com.ruijie.rcos.sk.base.batch.DefaultBatchTaskItem;
import com.ruijie.rcos.sk.base.exception.BusinessException;
import com.ruijie.rcos.sk.base.log.Logger;
import com.ruijie.rcos.sk.base.log.LoggerFactory;
import com.ruijie.rcos.sk.base.sm2.StateMachineFactory;
import com.ruijie.rcos.sk.modulekit.api.comm.DefaultPageResponse;
import com.ruijie.rcos.sk.pagekit.api.PageQueryBuilderFactory;
import com.ruijie.rcos.sk.pagekit.api.PageQueryRequest;
import com.ruijie.rcos.sk.pagekit.api.PageQueryResponse;
import com.ruijie.rcos.sk.webmvc.api.request.IdArrWebRequest;
import com.ruijie.rcos.sk.webmvc.api.request.IdWebRequest;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.BeanUtils;

/**
 * Description: description
 * Copyright: Copyright (c) 2021
 * Company: Ruijie Co., Ltd.
 * Create Time: 2021/5/7
 *
 * @author wuShengQiang
 */
@Api(tags = "云桌面备份")
@Controller
@RequestMapping("/rco/clouddesktop/deskBackup")
public class DeskBackupController {

    private static final Logger LOGGER = LoggerFactory.getLogger(DeskBackupController.class);

    @Autowired
    private CbbVDIDeskBackupAPI cbbVDIDeskBackupAPI;

    @Autowired
    private UserDesktopMgmtAPI userDesktopMgmtAPI;

    @Autowired
    private BaseAuditLogAPI auditLogAPI;

    @Autowired
    private DeskBackupAPI deskBackupAPI;

    @Autowired
    private PageQueryBuilderFactory pageQueryBuilderFactory;

    @Autowired
    private StateMachineFactory stateMachineFactory;

    @Autowired
    private CloudDesktopWebService cloudDesktopWebService;

    private static final String[] SUPPORT_SORT_FIELD_ARR = new String[] {"name"};

    private static final String SORT_FIELD_CREATE_TIME = "createTime";
    
    @Autowired
    private ExternalStorageMgmtAPI externalStorageMgmtAPI;

    /**
     * 分页获取外置存储列表
     * 
     * @param request 入参
     * @return 响应
     * @throws BusinessException 业务异常
     */
    @ApiOperation(value = "获取外置存储列表")
    @ApiVersions({@ApiVersion(value = Version.V3_1_1, descriptions = {"获取外置存储列表"})})
    @RequestMapping(value = "listExternalStorageInfo", method = RequestMethod.POST)
    public CommonWebResponse<DefaultPageResponse<ExternalStorageVO>> listExternalStorageInfo(PageQueryRequest request)
            throws BusinessException {
        Assert.notNull(request, "request must not be null");

        DefaultPageResponse<ExternalStorageVO> response = new DefaultPageResponse<>();
        try {
            CommonPageQueryRequest queryRequest = buildPageQueryRequest(request);
            PageResponse<ExternalStorageDTO> externalStorageList = externalStorageMgmtAPI.listExternalStorageInfo(queryRequest);
            ExternalStorageVO[] externalStorageArr = Arrays.stream(externalStorageList.getItems())
                    .filter(dto -> dto.getState() == ExternalStorageHealthStateEnum.HEALTHY
                            || dto.getState() == ExternalStorageHealthStateEnum.WARNING)
                    .map(BackupUtils::convertExternalStorageVO)
                    .toArray(ExternalStorageVO[]::new);
            response.setItemArr(externalStorageArr);
            response.setTotal(externalStorageArr.length);
        } catch (BusinessException e) {
            LOGGER.error("获取外置存储列表失败", e);
            return CommonWebResponse.fail(DeskBackupBusinessKey.RCDC_DESK_BACKUP_OPERATE_FAIL, new String[]{e.getI18nMessage()});
        }
        return CommonWebResponse.success(response);
    }

    private CommonPageQueryRequest buildPageQueryRequest(PageQueryRequest request) {
        CommonPageQueryRequest queryRequest = new CommonPageQueryRequest();
        queryRequest.setPage(request.getPage());
        queryRequest.setLimit(request.getLimit());
        Match[] matchArr = request.getMatchArr();
        if (ArrayUtils.isEmpty(matchArr)) {
            return queryRequest;
        }
        for (Match match : matchArr) {
            if (match.getType() == Match.Type.EXACT && StringUtils.equals(Constants.QUERY_PLATFORM_ID, ((ExactMatch) match).getFieldName())) {
                queryRequest.setPlatformId(Arrays.stream(((ExactMatch) match).getValueArr())
                        // 不传则设置为null
                        .map(item -> UUID.fromString(item.toString())).findFirst().orElse(null));
                break;
            }
        }
        return queryRequest;
    }

    /**
     * 获取云桌面备份详情
     *
     * @param idWebRequest 入参
     * @return 响应
     * @throws BusinessException 业务异常
     */
    @ApiOperation(value = "获取云桌面备份详情")
    @ApiVersions({@ApiVersion(value = Version.V3_1_1, descriptions = {"获取云桌面备份详情"})})
    @RequestMapping(value = "detail", method = RequestMethod.POST)
    public CommonWebResponse<DeskBackupInfoResponse> detail(IdWebRequest idWebRequest) throws BusinessException {
        Assert.notNull(idWebRequest, "idWebRequest must not be null");

        CbbDeskBackupDTO deskBackupInfo = findDeskBackupInfoById(idWebRequest.getId());
        DeskBackupInfoResponse deskBackupInfoResponse = convertFromCbbDeskBackup(deskBackupInfo);
        try {
            QueryExternalStorageRequest storageRequest = new QueryExternalStorageRequest();
            storageRequest.setPlatformId(deskBackupInfo.getPlatformId());
            storageRequest.setExternalStorageId(deskBackupInfo.getExtStorageId());
            ExternalStorageDTO externalStorageDTO = externalStorageMgmtAPI.findExternalStorageInfoById(storageRequest);
            deskBackupInfoResponse.setExternalStorageName(externalStorageDTO.getName());
            deskBackupInfoResponse.setExternalStorageHealthState(externalStorageDTO.getState());
        } catch (BusinessException e) {
            LOGGER.error(String.format("查询云桌面备份详情失败[%s]", idWebRequest.getId()), e);
        }

        return CommonWebResponse.success(deskBackupInfoResponse);
    }

    /**
     * 根据云桌面ID获取云桌面备份默认名称
     *
     * @param findDefaultBackupNameRequest 入参
     * @return 响应
     * @throws BusinessException 业务异常
     */
    @ApiOperation(value = "获取云桌面备份默认名称")
    @ApiVersions({@ApiVersion(value = Version.V3_1_1, descriptions = {"获取云桌面备份默认名称"})})
    @RequestMapping(value = "findDefaultBackupName", method = RequestMethod.POST)
    public CommonWebResponse<FindDefaultBackupNameResponse> findDefaultBackupName(FindDefaultBackupNameRequest findDefaultBackupNameRequest)
            throws BusinessException {
        Assert.notNull(findDefaultBackupNameRequest, "findDefaultBackupNameRequest must not be null");

        CloudDesktopDetailDTO cloudDesktopDetailDTO = userDesktopMgmtAPI.getDesktopDetailById(findDefaultBackupNameRequest.getDeskId());
        String backupName = deskBackupAPI.generateBackupNameByDesktopName(cloudDesktopDetailDTO.getDesktopName());
        FindDefaultBackupNameResponse response = new FindDefaultBackupNameResponse(backupName);
        return CommonWebResponse.success(response);
    }

    /**
     * 检测云桌面备份数量是否超配
     *
     * @param idWebRequest 入参
     * @return 响应
     * @throws BusinessException 业务异常
     */
    @ApiOperation(value = "检测云桌面备份数量是否超配")
    @ApiVersions({@ApiVersion(value = Version.V3_1_1, descriptions = {"检测云桌面备份数量是否超配"})})
    @RequestMapping(value = "checkBackupFull", method = RequestMethod.POST)
    public CommonWebResponse<CheckBackupFullResponse> checkBackupFull(IdWebRequest idWebRequest) throws BusinessException {
        Assert.notNull(idWebRequest, "idWebRequest must not be null");

        Boolean hasFull = deskBackupAPI.checkBackupNumberOverByDeskId(idWebRequest.getId());
        CheckBackupFullResponse response = new CheckBackupFullResponse(hasFull);
        return CommonWebResponse.success(response);
    }

    /**
     * 获取云桌面备份数量最大限制数
     *
     * @return 响应
     */
    @ApiOperation(value = "获取云桌面备份数量最大限制数")
    @ApiVersions({@ApiVersion(value = Version.V3_1_1, descriptions = {"获取云桌面备份数量最大限制数"})})
    @RequestMapping(value = "getMaxBackups", method = RequestMethod.POST)
    public CommonWebResponse<GetMaxBackupsResponse> getMaxBackups() {
        Integer maxBackups = deskBackupAPI.getMaxBackups();
        GetMaxBackupsResponse response = new GetMaxBackupsResponse(maxBackups);
        return CommonWebResponse.success(response);
    }

    /**
     * 检查云桌面备份名称是否重复
     *
     * @param checkBackupNameDuplicationRequest 入参
     * @return 响应
     * @throws BusinessException 业务异常
     */
    @ApiOperation(value = "检查云桌面备份名称是否重复")
    @ApiVersions({@ApiVersion(value = Version.V3_1_1, descriptions = {"检查云桌面备份名称是否重复"})})
    @RequestMapping(value = "checkNameDuplication", method = RequestMethod.POST)
    public CommonWebResponse<CheckNameDuplicationResponse> checkNameDuplication(CheckBackupNameDuplicationRequest checkBackupNameDuplicationRequest)
            throws BusinessException {
        Assert.notNull(checkBackupNameDuplicationRequest, "checkBackupNameDuplicationRequest must not be null");

        CheckNameDuplicationResponse checkNameDuplicationResponse = new CheckNameDuplicationResponse(false);
        String name = checkBackupNameDuplicationRequest.getName();
        try {
            UUID deskBackupId = checkBackupNameDuplicationRequest.getDeskBackupId();
            if (deskBackupId != null) {
                CbbDeskBackupDTO cbbDeskBackupDTO = findDeskBackupInfoById(deskBackupId);
                if (name.equals(cbbDeskBackupDTO.getName())) {
                    return CommonWebResponse.success(checkNameDuplicationResponse);
                }
            }
            Boolean hasDuplication = deskBackupAPI.checkNameDuplication(name);
            checkNameDuplicationResponse.setHasDuplication(hasDuplication);
        } catch (BusinessException e) {
            LOGGER.error(String.format("检查云桌面[%s]是否重复,异常信息", name), e.getI18nMessage());
            Boolean hasDuplication = deskBackupAPI.checkNameDuplication(name);
            checkNameDuplicationResponse.setHasDuplication(hasDuplication);
        }
        return CommonWebResponse.success(checkNameDuplicationResponse);
    }

    /**
     * 获取云桌面备份列表
     *
     * @param webRequest 入参
     * @return 响应
     * @throws BusinessException 业务异常
     */
    @ApiOperation(value = "获取云桌面备份列表")
    @ApiVersions({@ApiVersion(value = Version.V3_1_1, descriptions = {"获取云桌面备份列表"})})
    @RequestMapping(value = "list", method = RequestMethod.POST)
    public CommonWebResponse<DeskBackupPageQueryResponse<DeskBackupInfoResponse>> list(PageQueryRequest webRequest) throws BusinessException {
        Assert.notNull(webRequest, "webRequest must not be null");

        PageQueryResponse<CbbDeskBackupDetailDTO> pageQueryResponse =
                cbbVDIDeskBackupAPI.pageQuery(generatePageQueryBuilder(webRequest).build());
        DeskBackupPageQueryResponse<DeskBackupInfoResponse> response = new DeskBackupPageQueryResponse<>();
        CbbDeskBackupDetailDTO[] deskBackupArr = pageQueryResponse.getItemArr();
        response.setTotal(pageQueryResponse.getTotal());
        if (ArrayUtils.isEmpty(deskBackupArr)) {
            response.setItemArr(new DeskBackupInfoResponse[0]);
        } else {
            response.setItemArr(Arrays.stream(deskBackupArr).map(this::convertFromCbbDeskBackup).toArray(DeskBackupInfoResponse[]::new));
            if (deskBackupArr.length > 0) {
                CloudDesktopDetailDTO cloudDesktopDetailDTO = userDesktopMgmtAPI.getDesktopDetailById(deskBackupArr[0].getDeskId());
                response.setDesktopState(cloudDesktopDetailDTO.getDesktopState());
            }
        }
        return CommonWebResponse.success(response);
    }

    /**
     * 编辑云桌面备份
     *
     * @param editDeskBackupRequest 入参
     * @return 响应
     * @throws BusinessException 业务异常
     */
    @ApiOperation(value = "编辑云桌面备份")
    @ApiVersions({@ApiVersion(value = Version.V3_1_1, descriptions = {"编辑云桌面备份"})})
    @RequestMapping(value = "edit", method = RequestMethod.POST)
    @EnableAuthority
    public CommonWebResponse edit(EditDeskBackupRequest editDeskBackupRequest) throws BusinessException {
        Assert.notNull(editDeskBackupRequest, "editDeskBackupRequest must not be null");

        UUID deskBackupId = editDeskBackupRequest.getId();
        try {
            CbbDeskBackupDTO cbbDeskBackupDTO = findDeskBackupInfoById(deskBackupId);

            if (cbbDeskBackupDTO.getState() == CbbDeskBackupStateEnum.DELETING) {
                LOGGER.error("云桌面备份[{}]修改异常。失败原因：云桌面处于删除中，不支持修改。", cbbDeskBackupDTO.getName());
                throw new BusinessException(DeskBackupBusinessKey.RCDC_DESK_BACKUP_EDIT_BY_DELETING, new String[] {cbbDeskBackupDTO.getName()});
            }

            if (cbbDeskBackupDTO.getName().equals(editDeskBackupRequest.getName())) {
                LOGGER.info("云桌面备份[{}]名称未发生改变。", editDeskBackupRequest.getName());
            } else {
                cbbVDIDeskBackupAPI.updateDeskBackupName(editDeskBackupRequest.getId(), editDeskBackupRequest.getName());
            }
            auditLogAPI.recordLog(DeskBackupBusinessKey.RCDC_DESK_BACKUP_OPERATE_EDIT_SUCCESS, new String[] {editDeskBackupRequest.getName()});
            return CommonWebResponse.success(DeskBackupBusinessKey.RCDC_DESK_BACKUP_OPERATE_SUCCESS, new String[] {});
        } catch (BusinessException e) {
            auditLogAPI.recordLog(DeskBackupBusinessKey.RCDC_DESK_BACKUP_OPERATE_EDIT_FAIL,
                    new String[] {editDeskBackupRequest.getName(), e.getI18nMessage()});
            return CommonWebResponse.fail(DeskBackupBusinessKey.RCDC_DESK_BACKUP_OPERATE_FAIL, new String[] {e.getI18nMessage()});
        }
    }

    /**
     * 创建云桌面备份
     *
     * @param createDeskBackupRequest 入参
     * @param builder 批量任务创建对象
     * @return 响应
     * @throws BusinessException 业务异常
     */
    @ApiOperation(value = "创建云桌面备份")
    @ApiVersions({@ApiVersion(value = Version.V3_1_1, descriptions = {"创建云桌面备份"})})
    @RequestMapping(value = "create", method = RequestMethod.POST)
    @EnableAuthority
    public CommonWebResponse create(CreateDeskBackupRequest createDeskBackupRequest, BatchTaskBuilder builder) throws BusinessException {
        Assert.notNull(createDeskBackupRequest, "createDeskBackupRequest must not be null");
        Assert.notNull(builder, "builder must not be null");

        UUID[] idArr = new UUID[] {createDeskBackupRequest.getDeskId()};

        final Iterator<DefaultBatchTaskItem> iterator = Stream.of(idArr).distinct().map(id -> DefaultBatchTaskItem.builder()//
                .itemId(id).itemName(DeskBackupBusinessKey.RCDC_DESK_BACKUP_CREATE_ITEM_NAME, new String[] {}).build())//
                .iterator();

        CreateDeskBackupBatchTaskHandler handler = new CreateDeskBackupBatchTaskHandler(deskBackupAPI, userDesktopMgmtAPI, iterator
                , auditLogAPI, cloudDesktopWebService);
        handler.setStateMachineFactory(stateMachineFactory);
        handler.setBackupName(createDeskBackupRequest.getName());
        handler.setExtStorageId(createDeskBackupRequest.getExtStorageId());
        BatchTaskSubmitResult result = startCreateBatchTask(idArr, handler, builder);
        return CommonWebResponse.success(result);
    }

    private BatchTaskSubmitResult startCreateBatchTask(UUID[] idArr, CreateDeskBackupBatchTaskHandler handler, BatchTaskBuilder builder)
            throws BusinessException {
        // 创建单条用户
        BatchTaskSubmitResult result;
        if (idArr.length == 1) {
            handler.setIsBatch(false);
            result = builder.setTaskName(DeskBackupBusinessKey.RCDC_DESK_BACKUP_CREATE_SINGLE_TASK_NAME, new String[] {})
                    .setTaskDesc(DeskBackupBusinessKey.RCDC_DESK_BACKUP_CREATE_SINGLE_TASK_DESC, new String[] {handler.getBackupName()})
                    .enableParallel().registerHandler(handler).start();
        } else {
            result = builder.setTaskName(DeskBackupBusinessKey.RCDC_DESK_BACKUP_CREATE_TASK_NAME, new String[] {})
                    .setTaskDesc(DeskBackupBusinessKey.RCDC_DESK_BACKUP_CREATE_TASK_DESC, new String[] {}).enableParallel().registerHandler(handler)
                    .start();
        }
        return result;
    }

    /**
     * 删除云桌面备份
     *
     * @param idArrWebRequest 入参
     * @param builder 批量任务创建对象
     * @return 响应
     * @throws BusinessException 业务异常
     */
    @ApiOperation(value = "删除云桌面备份")
    @ApiVersions({@ApiVersion(value = Version.V3_1_1, descriptions = {"删除云桌面备份"})})
    @RequestMapping(value = "delete", method = RequestMethod.POST)
    @EnableAuthority
    public CommonWebResponse<BatchTaskSubmitResult> delete(IdArrWebRequest idArrWebRequest, BatchTaskBuilder builder) throws BusinessException {
        Assert.notNull(idArrWebRequest, "idArrWebRequest must not be null");
        Assert.notNull(builder, "builder can not be null");

        UUID[] idArr = idArrWebRequest.getIdArr();
        // 批量删除任务
        final Iterator<DefaultBatchTaskItem> iterator = Stream.of(idArr).distinct().map(id -> DefaultBatchTaskItem.builder().itemId(id)
                .itemName(DeskBackupBusinessKey.RCDC_DESK_BACKUP_DELETE_ITEM_NAME, new String[] {}).build()).iterator();
        DeleteDeskBackupBatchTaskHandler handler = new DeleteDeskBackupBatchTaskHandler(this.cbbVDIDeskBackupAPI, iterator, auditLogAPI);
        handler.setStateMachineFactory(stateMachineFactory);
        BatchTaskSubmitResult result = startDeleteBatchTask(idArr, handler, builder);
        return CommonWebResponse.success(result);
    }

    /**
     * 云桌面备份恢复
     * 
     * @param idRequest 入参
     * @param builder 批量任务创建对象
     * @return 响应
     * @throws BusinessException 业务异常
     */
    @ApiOperation(value = "云桌面备份恢复")
    @ApiVersions({@ApiVersion(value = Version.V3_1_1, descriptions = {"云桌面备份恢复"})})
    @RequestMapping(value = "recover", method = RequestMethod.POST)
    @EnableAuthority
    public CommonWebResponse recover(IdWebRequest idRequest, BatchTaskBuilder builder) throws BusinessException {
        Assert.notNull(idRequest, "idRequest must not be null");
        Assert.notNull(builder, "builder must not be null");

        UUID[] idArr = new UUID[] {idRequest.getId()};

        final Iterator<DefaultBatchTaskItem> iterator = Stream.of(idArr).distinct().map(id -> DefaultBatchTaskItem.builder()//
                .itemId(id).itemName(DeskBackupBusinessKey.RCDC_DESK_BACKUP_RECOVER_ITEM_NAME, new String[] {}).build())//
                .iterator();

        RecoverDeskBackupBatchTaskHandler handler = new RecoverDeskBackupBatchTaskHandler(cbbVDIDeskBackupAPI,
                userDesktopMgmtAPI, iterator, auditLogAPI);
        handler.setStateMachineFactory(stateMachineFactory);
        BatchTaskSubmitResult result = startRecoverBatchTask(idArr, handler, builder);
        return CommonWebResponse.success(result);
    }

    private BatchTaskSubmitResult startRecoverBatchTask(UUID[] idArr, RecoverDeskBackupBatchTaskHandler handler, BatchTaskBuilder builder)
            throws BusinessException {
        BatchTaskSubmitResult result;
        if (idArr.length == 1) {
            handler.setIsBatch(false);
            CbbDeskBackupDTO deskBackupDTO = findDeskBackupInfoById(idArr[0]);
            result = builder.setTaskName(DeskBackupBusinessKey.RCDC_DESK_BACKUP_RECOVER_SINGLE_TASK_NAME, new String[] {})
                    .setTaskDesc(DeskBackupBusinessKey.RCDC_DESK_BACKUP_RECOVER_SINGLE_TASK_DESC, new String[] {deskBackupDTO.getName()})
                    .enableParallel().registerHandler(handler).start();
        } else {
            result = builder.setTaskName(DeskBackupBusinessKey.RCDC_DESK_BACKUP_RECOVER_TASK_NAME, new String[] {})
                    .setTaskDesc(DeskBackupBusinessKey.RCDC_DESK_BACKUP_RECOVER_TASK_DESC, new String[] {}).enableParallel().registerHandler(handler)
                    .start();
        }
        return result;
    }

    private BatchTaskSubmitResult startDeleteBatchTask(UUID[] idArr, DeleteDeskBackupBatchTaskHandler handler, BatchTaskBuilder builder)
            throws BusinessException {
        // 删除单条用户
        BatchTaskSubmitResult result;
        if (idArr.length == 1) {
            handler.setIsBatch(false);
            CbbDeskBackupDTO deskBackupDTO = findDeskBackupInfoById(idArr[0]);
            result = builder.setTaskName(DeskBackupBusinessKey.RCDC_DESK_BACKUP_DELETE_SINGLE_TASK_NAME, new String[] {})
                    .setTaskDesc(DeskBackupBusinessKey.RCDC_DESK_BACKUP_DELETE_SINGLE_TASK_DESC, new String[] {deskBackupDTO.getName()})
                    .enableParallel().registerHandler(handler).start();
        } else {
            result = builder.setTaskName(DeskBackupBusinessKey.RCDC_DESK_BACKUP_DELETE_TASK_NAME, new String[] {})
                    .setTaskDesc(DeskBackupBusinessKey.RCDC_DESK_BACKUP_DELETE_TASK_DESC, new String[] {}).enableParallel().registerHandler(handler)
                    .start();
        }
        return result;
    }

    /**
     * 云桌面备份取消
     *
     * @param idArrWebRequest 入参
     * @param builder 取消云桌面備份
     * @return 响应
     * @throws BusinessException 业务异常
     */
    @ApiOperation(value = "云桌面备份取消")
    @ApiVersions({@ApiVersion(value = Version.V3_1_1, descriptions = {"云桌面备份取消"})})
    @RequestMapping(value = "cancel", method = RequestMethod.POST)
    @EnableAuthority
    public CommonWebResponse cancel(IdArrWebRequest idArrWebRequest, BatchTaskBuilder builder) throws BusinessException {
        Assert.notNull(idArrWebRequest, "idArrWebRequest must not be null");
        Assert.notNull(builder, "builder must not be null");

        UUID[] idArr = idArrWebRequest.getIdArr();
        final Iterator<DefaultBatchTaskItem> iterator = Stream.of(idArr).distinct().map(id -> DefaultBatchTaskItem.builder()//
                        .itemId(id).itemName(DeskBackupBusinessKey.RCDC_DESK_BACKUP_CANCEL_ITEM_NAME, new String[] {}).build())//
                .iterator();

        CancelDeskBackupBatchTaskHandler handler = new CancelDeskBackupBatchTaskHandler(iterator);
        handler.setAuditLogAPI(auditLogAPI);
        handler.setCbbVDIDeskBackupAPI(cbbVDIDeskBackupAPI);
        BatchTaskSubmitResult result = startCancelBatchTask(idArr, handler, builder);
        return CommonWebResponse.success(result);
    }

    private BatchTaskSubmitResult startCancelBatchTask(UUID[] idArr, CancelDeskBackupBatchTaskHandler handler, BatchTaskBuilder builder)
            throws BusinessException {
        BatchTaskSubmitResult result;
        if (idArr.length == 1) {
            handler.setIsBatch(false);
            CbbDeskBackupDTO deskBackupDTO = findDeskBackupInfoById(idArr[0]);
            result = builder.setTaskName(DeskBackupBusinessKey.RCDC_DESK_BACKUP_CANCEL_SINGLE_TASK_NAME, new String[] {})
                    .setTaskDesc(DeskBackupBusinessKey.RCDC_DESK_BACKUP_CANCEL_SINGLE_TASK_DESC, new String[] {deskBackupDTO.getName()})
                    .enableParallel().registerHandler(handler).start();
        } else {
            result = builder.setTaskName(DeskBackupBusinessKey.RCDC_DESK_BACKUP_CANCEL_TASK_NAME, new String[] {})
                    .setTaskDesc(DeskBackupBusinessKey.RCDC_DESK_BACKUP_CANCEL_TASK_DESC, new String[] {}).enableParallel().registerHandler(handler)
                    .start();
        }
        return result;
    }

    private CbbDeskBackupDetailDTO findDeskBackupInfoById(UUID deskBackupId) throws BusinessException {
        CbbDeskBackupDetailDTO cbbDeskBackupDTO = cbbVDIDeskBackupAPI.findDeskBackupInfoById(deskBackupId);
        if (cbbDeskBackupDTO == null) {
            throw new BusinessException(DeskBackupBusinessKey.RCDC_DESK_BACKUP_IS_NULL, deskBackupId.toString());
        }
        return cbbDeskBackupDTO;
    }

    private PageQueryBuilderFactory.RequestBuilder generatePageQueryBuilder(PageQueryRequest request) {
        PageQueryBuilderFactory.RequestBuilder builder = pageQueryBuilderFactory.newRequestBuilder(request);
        return ArrayUtils.isEmpty(request.getSortArr()) ? builder.desc(SORT_FIELD_CREATE_TIME) : builder;
    }

    private DeskBackupInfoResponse convertFromCbbDeskBackup(CbbDeskBackupDTO cbbDeskBackupDTO) {
        Assert.notNull(cbbDeskBackupDTO, "cbbDeskBackupDTO can not be null");

        DeskBackupInfoResponse deskBackupInfo = new DeskBackupInfoResponse();
        BeanUtils.copyProperties(cbbDeskBackupDTO, deskBackupInfo);
        return deskBackupInfo;
    }

}
