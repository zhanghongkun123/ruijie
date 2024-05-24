package com.ruijie.rcos.rcdc.rco.module.web.ctrl.userprofile;

import com.alibaba.fastjson.JSON;
import com.ruijie.rcos.gss.log.module.def.api.BaseAuditLogAPI;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.api.CbbDeskMgmtAPI;
import com.ruijie.rcos.rcdc.rco.module.def.api.*;
import com.ruijie.rcos.rcdc.rco.module.def.api.request.DeskPageSearchRequest;
import com.ruijie.rcos.rcdc.rco.module.def.api.request.PageSearchRequest;
import com.ruijie.rcos.rcdc.rco.module.def.constants.Constants;
import com.ruijie.rcos.rcdc.rco.module.def.userprofile.dto.ExportUserProfilePathCacheDTO;
import com.ruijie.rcos.rcdc.rco.module.def.userprofile.dto.ExportUserProfilePathCacheResponse;
import com.ruijie.rcos.rcdc.rco.module.def.userprofile.dto.ExportUserProfilePathFileResponse;
import com.ruijie.rcos.rcdc.rco.module.def.userprofile.dto.UserProfileChildPathInfoDTO;
import com.ruijie.rcos.rcdc.rco.module.def.userprofile.dto.UserProfilePathDTO;
import com.ruijie.rcos.rcdc.rco.module.def.userprofile.dto.UserProfilePathGroupDTO;
import com.ruijie.rcos.rcdc.rco.module.def.userprofile.enums.UserProfileCleanTypeEnum;
import com.ruijie.rcos.rcdc.rco.module.def.userprofile.request.ExportUserProfilePathRequest;
import com.ruijie.rcos.rcdc.rco.module.def.utils.ListRequestHelper;
import com.ruijie.rcos.rcdc.rco.module.web.ctrl.aaa.AaaBusinessKey;
import com.ruijie.rcos.rcdc.rco.module.web.ctrl.user.request.EmptyDownloadWebRequest;
import com.ruijie.rcos.rcdc.rco.module.web.ctrl.user.response.ImportUserWebResponse;
import com.ruijie.rcos.rcdc.rco.module.web.ctrl.userprofile.batchtask.*;
import com.ruijie.rcos.rcdc.rco.module.web.ctrl.userprofile.dto.ImportUserProfilePathDTO;
import com.ruijie.rcos.rcdc.rco.module.web.ctrl.userprofile.request.CheckUserProfileNameDuplicationRequest;
import com.ruijie.rcos.rcdc.rco.module.web.ctrl.userprofile.request.CleanUserProfilePathInfoRequest;
import com.ruijie.rcos.rcdc.rco.module.web.ctrl.userprofile.request.CreateUserProfilePathRequest;
import com.ruijie.rcos.rcdc.rco.module.web.ctrl.userprofile.request.ExportUserProfilePathWebRequest;
import com.ruijie.rcos.rcdc.rco.module.web.ctrl.userprofile.request.ImportPathBatchTaskHandlerRequest;
import com.ruijie.rcos.rcdc.rco.module.web.ctrl.userprofile.request.MoveUserProfilePathRequest;
import com.ruijie.rcos.rcdc.rco.module.web.ctrl.userprofile.response.CheckNameDuplicationForUserProfileResponse;
import com.ruijie.rcos.rcdc.rco.module.web.ctrl.userprofile.response.UserProfilePathWebResponse;
import com.ruijie.rcos.rcdc.rco.module.web.response.CommonWebResponse;
import com.ruijie.rcos.rcdc.rco.module.web.service.userprofile.ImportUserProfilePathHandler;
import com.ruijie.rcos.rcdc.rco.module.web.service.userprofile.UserProfileHelp;
import com.ruijie.rcos.sk.base.batch.BatchTaskBuilder;
import com.ruijie.rcos.sk.base.batch.BatchTaskSubmitResult;
import com.ruijie.rcos.sk.base.batch.DefaultBatchTaskItem;
import com.ruijie.rcos.sk.base.exception.BusinessException;
import com.ruijie.rcos.sk.base.i18n.LocaleI18nResolver;
import com.ruijie.rcos.sk.base.log.Logger;
import com.ruijie.rcos.sk.base.log.LoggerFactory;
import com.ruijie.rcos.sk.modulekit.api.comm.DefaultPageResponse;
import com.ruijie.rcos.sk.pagekit.api.PageQueryWebConfig;
import com.ruijie.rcos.sk.webmvc.api.request.ChunkUploadFile;
import com.ruijie.rcos.sk.webmvc.api.request.IdArrWebRequest;
import com.ruijie.rcos.sk.webmvc.api.request.IdWebRequest;
import com.ruijie.rcos.sk.webmvc.api.request.PageWebRequest;
import com.ruijie.rcos.sk.webmvc.api.response.DownloadWebResponse;
import com.ruijie.rcos.sk.webmvc.api.response.WebResponse;
import com.ruijie.rcos.sk.webmvc.api.session.SessionContext;
import com.ruijie.rcos.sk.webmvc.api.vo.IdLabelEntry;
import com.ruijie.rcos.sk.webmvc.api.vo.Sort;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.io.IOException;
import java.io.InputStream;
import java.util.*;
import java.util.stream.Stream;

/**
 * Description: 用户配置路径
 * Copyright: Copyright (c) 2022
 * Company: Ruijie Co., Ltd.
 * Create Time: 2022/4/11
 *
 * @author WuShengQiang
 */
@Api(tags = "用户配置路径管理")
@Controller
@RequestMapping("/rco/userProfilePath")
@PageQueryWebConfig(url = "/list", dtoType = UserProfilePathDTO.class)
public class UserProfilePathController {

    private static final Logger LOGGER = LoggerFactory.getLogger(UserProfilePathController.class);

    /**
     * 模板文件路径
     */
    private static final String TEMPLATE_PATH = "template/";

    /**
     * 路径模板文件名称
     */
    private static final String TEMPLATE_NAME = "user_profile_path_model";

    /**
     * 路径模板文件类型
     */
    private static final String TEMPLATE_TYPE = "xlsx";

    /**
     * 分隔.
     */
    private static final String SYMBOL_SPOT = ".";


    @Autowired
    private UserProfileMgmtAPI userProfileMgmtAPI;

    @Autowired
    private ExportUserProfilePathAPI exportUserProfilePathAPI;

    @Autowired
    private ImportUserProfilePathHandler importUserProfilePathHandler;

    @Autowired
    private UserProfilePathSpecialConfigAPI specialConfigAPI;

    @Autowired
    private BaseAuditLogAPI auditLogAPI;

    @Autowired
    private UserProfileValidateAPI userProfileValidaAPI;

    @Autowired
    private CbbDeskMgmtAPI cbbDeskMgmtAPI;

    @Autowired
    private UserProfileHelp userProfileHelp;

    @Autowired
    private UserProfileStrategyNotifyAPI userProfileStrategyNotifyAPI;

    /**
     * 导出路径信息文件名
     */
    private static final String EXPORT_USER_PROFILE_PATH_FILENAME = "exportUserProfilePath";


    /**
     * 移动路径
     *
     * @param moveUserProfilePathRequest 入参
     * @return 响应
     * @throws BusinessException 业务异常
     */
    @ApiOperation("移动路径")
    @RequestMapping(value = "move", method = RequestMethod.POST)
    public CommonWebResponse move(MoveUserProfilePathRequest moveUserProfilePathRequest) throws BusinessException {
        Assert.notNull(moveUserProfilePathRequest, "moveUserProfilePathRequest must not be null");
        userProfileMgmtAPI.isImportingUserProfilePath();

        //判断源组是否存在
        UUID sourceGroupId = moveUserProfilePathRequest.getSourceGroupId();
        UserProfilePathGroupDTO sourceGroupDTO = userProfileMgmtAPI.findUserProfilePathGroupById(sourceGroupId);
        //判断目标组是否存在
        UUID targetGroupId = moveUserProfilePathRequest.getTargetGroupId();
        UserProfilePathGroupDTO targetGroupDTO = userProfileMgmtAPI.findUserProfilePathGroupById(targetGroupId);

        UUID[] idArr = moveUserProfilePathRequest.getIdArr();
        List<List<UUID>> idArrList = ListRequestHelper.subArray(idArr, Constants.OPERATE_MAX_SIZE);
        for (List<UUID> subIdList : idArrList) {
            userProfileMgmtAPI.moveUserProfilePath(subIdList, targetGroupId);
            List<UserProfilePathDTO> pathDTOList = userProfileMgmtAPI.findUserProfilePathByIdIn(subIdList);
            pathDTOList.forEach((userProfilePathDTO) -> {
                //记录审计日志
                auditLogAPI.recordLog(UserProfileBusinessKey.RCDC_RCO_PATH_GROUP_MOVE_PATH,
                        userProfilePathDTO.getName(), sourceGroupDTO.getName(), targetGroupDTO.getName());
            });
        }
        return CommonWebResponse.success(AaaBusinessKey.RCDC_AAA_OPERATOR_SUCCESS, new String[]{});
    }

    /**
     * 创建路径
     *
     * @param createUserProfilePathRequest 路径对象
     * @param sessionContext               session上下文
     * @return 创建结果
     * @throws BusinessException 业务异常
     */
    @ApiOperation("创建路径")
    @RequestMapping(value = "create", method = RequestMethod.POST)
    public CommonWebResponse create(CreateUserProfilePathRequest createUserProfilePathRequest, SessionContext sessionContext)
            throws BusinessException {
        Assert.notNull(createUserProfilePathRequest, "createUserProfil nulePathRequest must not be null");
        Assert.notNull(sessionContext, "sessionContext must not bel");

        userProfileValidaAPI.validateGroupIdExist(createUserProfilePathRequest.getGroupId());
        userProfileValidaAPI.validateUserProfileChildPath(createUserProfilePathRequest.getChildPathArr(), null);

        UserProfilePathDTO userProfilePathDTO = new UserProfilePathDTO();
        BeanUtils.copyProperties(createUserProfilePathRequest, userProfilePathDTO);
        userProfilePathDTO.setCreatorUserName(sessionContext.getUserName());
        userProfileMgmtAPI.createUserProfilePath(userProfilePathDTO);
        //记录审计日志
        auditLogAPI.recordLog(UserProfileBusinessKey.RCDC_RCO_USER_PROFILE_PATH_CREATE, createUserProfilePathRequest.getName());
        return CommonWebResponse.success(AaaBusinessKey.RCDC_AAA_OPERATOR_SUCCESS, new String[]{});
    }

    /**
     * 编辑路径
     *
     * @param createUserProfilePathRequest 入参
     * @param builder                      批处理
     * @return 响应
     * @throws BusinessException 业务异常
     */
    @ApiOperation("编辑路径")
    @RequestMapping(value = "edit", method = RequestMethod.POST)
    public CommonWebResponse edit(CreateUserProfilePathRequest createUserProfilePathRequest,
                                  BatchTaskBuilder builder) throws BusinessException {
        Assert.notNull(createUserProfilePathRequest, "createUserProfilePathRequest must not be null");
        Assert.notNull(builder, "builder must not be null");

        userProfileValidaAPI.validateGroupIdExist(createUserProfilePathRequest.getGroupId());
        userProfileValidaAPI.validateUserProfileChildPath(createUserProfilePathRequest.getChildPathArr(), createUserProfilePathRequest.getId());

        UserProfilePathDTO userProfilePathDTO = new UserProfilePathDTO();
        BeanUtils.copyProperties(createUserProfilePathRequest, userProfilePathDTO);
        List<UUID> desktopIdList = userProfileStrategyNotifyAPI.getRelatedDesktopIdByUserProfilePath(
                Collections.singletonList(createUserProfilePathRequest.getId()));
        userProfileMgmtAPI.editUserProfilePath(userProfilePathDTO);

        // 通知所有云桌面
        if (!desktopIdList.isEmpty()) {
            userProfileHelp.buildPushUserProfilePathUpdate(builder, desktopIdList.toArray(new UUID[desktopIdList.size()]));
        }

        //记录审计日志
        auditLogAPI.recordLog(UserProfileBusinessKey.RCDC_RCO_USER_PROFILE_PATH_EDIT, createUserProfilePathRequest.getName());
        return CommonWebResponse.success(AaaBusinessKey.RCDC_AAA_OPERATOR_SUCCESS, new String[]{});
    }


    /**
     * 删除路径
     *
     * @param idArrWebRequest 入参
     * @param builder         批处理
     * @return 响应
     * @throws BusinessException 业务异常
     */
    @ApiOperation("删除路径")
    @RequestMapping(value = "delete", method = RequestMethod.POST)
    public CommonWebResponse delete(IdArrWebRequest idArrWebRequest, BatchTaskBuilder builder) throws BusinessException {
        Assert.notNull(idArrWebRequest, "idArrWebRequest must not be null");
        Assert.notNull(builder, "builder must not be null");
        UUID[] idArr = idArrWebRequest.getIdArr();
        Assert.notEmpty(idArr, "idArr can not be null");

        List<UUID> desktopIdList = userProfileStrategyNotifyAPI.getRelatedDesktopIdByUserProfilePath(Arrays.asList(idArr));

        // 批量删除任务
        final Iterator<DefaultBatchTaskItem> iterator = Stream.of(idArr).distinct().map(id -> DefaultBatchTaskItem.builder().itemId(id)
                .itemName(UserProfileBusinessKey.RCDC_RCO_USER_PROFILE_PATH_DELETE, new String[]{}).build()).iterator();
        DeleteUserProfilePathBatchTaskHandler handler = new DeleteUserProfilePathBatchTaskHandler(iterator, auditLogAPI, userProfileMgmtAPI);
        BatchTaskSubmitResult result;
        if (idArr.length == 1) {
            handler.setBatch(false);
            UserProfilePathDTO userProfilePathDTO = userProfileMgmtAPI.findUserProfilePathById(idArr[0]);
            result = builder.setTaskName(UserProfileBusinessKey.RCDC_RCO_USER_PROFILE_PATH_DELETE)
                    .setTaskDesc(UserProfileBusinessKey.RCDC_RCO_USER_PROFILE_PATH_DELETE_SINGLE_TASK_DESC, userProfilePathDTO.getName())
                    .enableParallel().registerHandler(handler).start();
        } else {
            result = builder.setTaskName(UserProfileBusinessKey.RCDC_RCO_USER_PROFILE_PATH_BATCH_DELETE)
                    .setTaskDesc(UserProfileBusinessKey.RCDC_RCO_USER_PROFILE_PATH_BATCH_DELETE_TASK_DESC).enableParallel()
                    .registerHandler(handler).start();
        }

        // 通知所有云桌面
        if (!desktopIdList.isEmpty()) {
            userProfileHelp.buildPushUserProfilePathUpdate(builder, desktopIdList.toArray(new UUID[desktopIdList.size()]));
        }

        return CommonWebResponse.success(result);
    }

    /**
     * 删除子路径
     *
     * @param webRequest 入参
     * @param builder    批处理
     * @return 响应
     * @throws BusinessException 业务异常
     */
    @ApiOperation("删除子路径")
    @RequestMapping(value = "child/delete", method = RequestMethod.POST)
    public CommonWebResponse deleteChildPath(IdWebRequest webRequest,
                                             BatchTaskBuilder builder) throws BusinessException {
        Assert.notNull(webRequest, "webRequest must not be null");
        Assert.notNull(builder, "builder must not be null");

        UUID userProfilePathId = userProfileMgmtAPI.findUserProfilePathIdByChildPath(webRequest.getId());
        List<UUID> desktopIdList = userProfileStrategyNotifyAPI.getRelatedDesktopIdByUserProfilePath(Collections.singletonList(userProfilePathId));
        userProfileMgmtAPI.deleteChildPath(webRequest.getId());
        // 通知所有云桌面
        if (!desktopIdList.isEmpty()) {
            userProfileHelp.buildPushUserProfilePathUpdate(builder, desktopIdList.toArray(new UUID[desktopIdList.size()]));
        }

        return CommonWebResponse.success();
    }

    /**
     * 获取路径详情
     *
     * @param idWebRequest 入参
     * @return 响应
     * @throws BusinessException 业务异常
     */
    @ApiOperation("获取路径详情")
    @RequestMapping(value = {"detail", "getInfo"}, method = RequestMethod.POST)
    public CommonWebResponse<UserProfilePathWebResponse> detail(IdWebRequest idWebRequest) throws BusinessException {
        Assert.notNull(idWebRequest, "idWebRequest must not be null");
        UserProfilePathDTO userProfilePathDTO = userProfileMgmtAPI.findUserProfilePathById(idWebRequest.getId());
        UserProfilePathWebResponse pathWebResponse = new UserProfilePathWebResponse();
        BeanUtils.copyProperties(userProfilePathDTO, pathWebResponse);
        pathWebResponse.setPathGroup(IdLabelEntry.build(pathWebResponse.getGroupId(), pathWebResponse.getGroupName()));
        return CommonWebResponse.success(pathWebResponse);
    }

    /**
     * 获取子路径列表
     *
     * @param request 请求
     * @return 响应
     * @throws BusinessException 异常处理
     */
    @ApiOperation("获取子路径列表")
    @RequestMapping(value = "child/list", method = RequestMethod.POST)
    public CommonWebResponse<DefaultPageResponse<UserProfileChildPathInfoDTO>> getChildList(PageWebRequest request) throws BusinessException {
        Assert.notNull(request, "request must not be null");

        PageSearchRequest pageReq = new DeskPageSearchRequest(request);
        pageReq.coverMatchEqualForUUID("userProfilePathId");

        DefaultPageResponse<UserProfileChildPathInfoDTO> resp = userProfileMgmtAPI.pageQuery(pageReq);
        return CommonWebResponse.success(resp);
    }

    /**
     * 检查路径名称是否重复
     *
     * @param checkNameDuplicationRequest 入参
     * @return 响应
     */
    @ApiOperation("检查路径名称是否重复")
    @RequestMapping(value = "checkNameDuplication", method = RequestMethod.POST)
    public CommonWebResponse<CheckNameDuplicationForUserProfileResponse> checkNameDuplication(
            CheckUserProfileNameDuplicationRequest checkNameDuplicationRequest) {
        Assert.notNull(checkNameDuplicationRequest, "checkNameDuplicationRequest must not be null");
        String name = checkNameDuplicationRequest.getName();
        UUID id = checkNameDuplicationRequest.getId();
        Boolean hasDuplication = userProfileMgmtAPI.checkUserProfilePathNameDuplication(id, name);
        return CommonWebResponse.success(new CheckNameDuplicationForUserProfileResponse(hasDuplication));
    }

    /**
     * 异步导出路径数据到excel
     *
     * @param sessionContext                  Session上下文
     * @param exportUserProfilePathWebRequest 请求参数
     * @return result
     * @throws BusinessException 业务异常
     */
    @RequestMapping(value = "/export", method = RequestMethod.POST)
    @ApiOperation("导出路径")
    public CommonWebResponse export(ExportUserProfilePathWebRequest exportUserProfilePathWebRequest, SessionContext sessionContext)
            throws BusinessException {
        Assert.notNull(exportUserProfilePathWebRequest, "exportUserProfilePathWebRequest is null");
        Assert.notNull(sessionContext, "sessionContext is null");

        // 增加在某个路径组导出的场景
        Sort[] sortArr = generateSortArr(null);
        String userId = sessionContext.getUserId().toString();
        ExportUserProfilePathRequest request = new ExportUserProfilePathRequest(userId, sortArr, exportUserProfilePathWebRequest.getGroupId());
        exportUserProfilePathAPI.exportDataAsync(request);
        return CommonWebResponse.success(AaaBusinessKey.RCDC_AAA_OPERATOR_SUCCESS, new String[]{});
    }

    /**
     * 获取路径数据导出任务情况
     *
     * @param exportUserProfilePathWebRequest 请求参数
     * @param sessionContext                  sessionContext
     * @return DataResult
     */
    @ApiOperation("获取导出结果")
    @RequestMapping(value = "/getExportResult", method = RequestMethod.POST)
    public CommonWebResponse<ExportUserProfilePathCacheDTO> getExportResult(ExportUserProfilePathWebRequest exportUserProfilePathWebRequest,
                                                                            SessionContext sessionContext) {
        Assert.notNull(sessionContext, "sessionContext is null");
        Assert.notNull(exportUserProfilePathWebRequest, "exportUserProfilePathWebRequest is null");

        String userId = sessionContext.getUserId().toString();
        ExportUserProfilePathRequest request = new ExportUserProfilePathRequest(userId, exportUserProfilePathWebRequest.getGroupId());
        ExportUserProfilePathCacheResponse response = exportUserProfilePathAPI.getExportDataCache(request);
        return CommonWebResponse.success(response.getExportUserProfilePathCacheDTO());
    }

    /**
     * 下载路径数据excel
     *
     * @param emptyDownloadWebRequest 页面请求参数
     * @param sessionContext          SessionContext
     * @return DataResult
     * @throws BusinessException 业务异常
     */
    @RequestMapping(value = "/downloadExportData", method = RequestMethod.GET)
    public DownloadWebResponse downloadExportFile(EmptyDownloadWebRequest emptyDownloadWebRequest
            , SessionContext sessionContext) throws BusinessException {
        Assert.notNull(emptyDownloadWebRequest, "request is null");
        Assert.notNull(sessionContext, "sessionContext is null");
        String userId = sessionContext.getUserId().toString();
        ExportUserProfilePathRequest request = new ExportUserProfilePathRequest(userId, emptyDownloadWebRequest.getGroupId());
        ExportUserProfilePathFileResponse exportFile = exportUserProfilePathAPI.getExportFile(request);
        DownloadWebResponse.Builder builder = new DownloadWebResponse.Builder();
        return builder.setFile(exportFile.getExportFile(), false).setName(EXPORT_USER_PROFILE_PATH_FILENAME, "xlsx").build();
    }


    private Sort[] generateSortArr(Sort[] sortArr) {
        if (sortArr == null) {
            Sort sortFirst = new Sort();
            sortFirst.setSortField("groupId");
            sortFirst.setDirection(Sort.Direction.DESC);

            return new Sort[]{sortFirst};
        }
        List<Sort> sortList = new ArrayList<>();
        for (Sort sort : sortArr) {
            sortList.add(sort);
        }
        return sortList.toArray(new Sort[sortList.size()]);
    }


    /**
     * 导入路径数据
     *
     * @param file           导入的文件对象
     * @param builder        批处理对象
     * @param sessionContext 用户上下文
     * @return 返回WebResponse 响应对象
     * @throws IOException       IO异常
     * @throws BusinessException 业务异常
     */
    @ApiOperation("导入路径数据")
    @RequestMapping(value = "importPath", method = RequestMethod.POST)
    public ImportUserWebResponse importPath(ChunkUploadFile file, BatchTaskBuilder builder, SessionContext sessionContext) throws BusinessException
            , IOException {
        Assert.notNull(file, "file is not null");
        Assert.notNull(builder, "builder is not null");
        Assert.notNull(sessionContext, "sessionContext is null");
        String userName = sessionContext.getUserName();
        List<ImportUserProfilePathDTO> importPathList = importUserProfilePathHandler.getImportDataList(file);
        importUserProfilePathHandler.validate(importPathList);
        List<UserProfilePathDTO> userProfilePathDTOList =
                importUserProfilePathHandler.changeImportUserProfileToRealUserProfile(importPathList);
        try {
            // 开始导入软件，如果已存在导入任务不允许导入
            userProfileMgmtAPI.isImportingPath();
            userProfileMgmtAPI.startAddPathData();

            // 校验通过，开始执行批量任务
            UserProfilePathDTO[] userProfilePathArr = userProfilePathDTOList.toArray(new UserProfilePathDTO[userProfilePathDTOList.size()]);
            final Iterator<CreatePathBatchTaskItem> iterator = Stream.of(userProfilePathArr)
                    .map(pathDTO -> CreatePathBatchTaskItem.builder().itemId(UUID.randomUUID()) //
                            .itemName(UserProfileBusinessKey.RCDC_RCO_IMPORT_PATH_ITEM_NAME, new String[]{}).itemPath(pathDTO).build())
                    .iterator();
            ImportPathBatchTaskHandlerRequest request = new ImportPathBatchTaskHandlerRequest();
            request.setBatchTaskItemIterator(iterator);
            request.setAuditLogAPI(auditLogAPI);
            request.setUserProfileMgmtAPI(userProfileMgmtAPI);
            request.setUserName(userName);
            ImportPathBatchTaskHandler handler = new ImportPathBatchTaskHandler(request);
            handler.setUserProfileValidateAPI(userProfileValidaAPI);
            BatchTaskSubmitResult batchTaskSubmitResult = builder.setTaskName(UserProfileBusinessKey.RCDC_RCO_IMPORT_PATH_ITEM_NAME)
                    .setTaskDesc(UserProfileBusinessKey.RCDC_RCO_IMPORT_PATH_TASK_DESC).registerHandler(handler).start();
            ImportUserWebResponse result = new ImportUserWebResponse();
            result.setStatus(WebResponse.Status.SUCCESS);
            result.setContent(batchTaskSubmitResult);
            return result;
        } catch (BusinessException e) {
            LOGGER.error("批量导入任务出现异常,信息:", e);
            throw e;
        } finally {
            // 结束任务删除缓存标记
            userProfileMgmtAPI.finishAddPathData();
        }
    }

    /**
     * 路径模板下载
     *
     * @return 路径模板
     * @throws IOException IO异常
     */
    @ApiOperation("路径模板下载")
    @RequestMapping(value = "downloadTemplate", method = RequestMethod.GET)
    public DownloadWebResponse downloadTemplate() throws IOException {
        InputStream inputStream = this.getClass().getClassLoader().getResourceAsStream(TEMPLATE_PATH + TEMPLATE_NAME + SYMBOL_SPOT + TEMPLATE_TYPE);
        DownloadWebResponse.Builder builder = new DownloadWebResponse.Builder();
        return builder.setInputStream(inputStream, (long) inputStream.available()).setName(TEMPLATE_NAME, TEMPLATE_TYPE).build();
    }

    /**
     * 基于路径清理配置数据
     *
     * @param webRequest     请求
     * @param sessionContext session信息
     * @param builder        批处理对象
     * @return 响应
     * @throws BusinessException 异常处理
     */
    @ApiOperation("基于个性化配置策略的清理")
    @RequestMapping(value = "strategy/clean", method = RequestMethod.POST)
    public CommonWebResponse cleanUserProfileStrategy(CleanUserProfilePathInfoRequest webRequest, SessionContext sessionContext,
                                                      BatchTaskBuilder builder) throws BusinessException {
        Assert.notNull(webRequest, "webRequest must not be null");
        Assert.notNull(sessionContext, "sessionContext must not be null");
        Assert.notNull(builder, "builder is not null");

        userProfileHelp.setCloudDesktopIdArrWhenAllClean(webRequest, sessionContext);
        return batchCleanPath(webRequest, builder, UserProfileCleanTypeEnum.STRATEGY);
    }


    /**
     * 基于路径清理配置数据
     *
     * @param webRequest     请求
     * @param sessionContext session信息
     * @param builder        批处理对象
     * @return 响应
     * @throws BusinessException 异常处理
     */
    @ApiOperation("配置清理")
    @RequestMapping(value = "path/clean", method = RequestMethod.POST)
    public CommonWebResponse cleanUserProfilePathInformation(CleanUserProfilePathInfoRequest webRequest, SessionContext sessionContext,
                                                             BatchTaskBuilder builder) throws BusinessException {
        Assert.notNull(webRequest, "webRequest must not be null");
        Assert.notNull(sessionContext, "sessionContext must not be null");
        Assert.notNull(builder, "builder is not null");

        userProfileHelp.setCloudDesktopIdArrWhenAllClean(webRequest, sessionContext);
        return batchCleanPath(webRequest, builder, UserProfileCleanTypeEnum.CONFIGURATION);
    }

    /**
     * 基于子路径清理配置数据
     *
     * @param webRequest     请求
     * @param builder        批处理对象
     * @param sessionContext session信息
     * @return 响应
     * @throws BusinessException 异常处理
     */
    @ApiOperation("子路径清理")
    @RequestMapping(value = "path/child/clean", method = RequestMethod.POST)
    public CommonWebResponse cleanUserProfileChildPathInformation(CleanUserProfilePathInfoRequest webRequest, SessionContext sessionContext,
                                                                  BatchTaskBuilder builder) throws BusinessException {
        Assert.notNull(webRequest, "webRequest must not be null");
        Assert.notNull(sessionContext, "sessionContext must not be null");
        Assert.notNull(builder, "builder is not null");

        userProfileHelp.setCloudDesktopIdArrWhenAllClean(webRequest, sessionContext);
        return batchCleanPath(webRequest, builder, UserProfileCleanTypeEnum.PATH);
    }

    private CommonWebResponse<BatchTaskSubmitResult> batchCleanPath(CleanUserProfilePathInfoRequest webRequest, BatchTaskBuilder builder,
                                                                    UserProfileCleanTypeEnum cleanType)
            throws BusinessException {
        String itemName = LocaleI18nResolver.resolve(UserProfileBusinessKey.RCDC_RCO_USER_PROFILE_PATH_CLEAN_PATH_TASK_NAME);
        UUID[] idArr = webRequest.getCloudDesktopIdArr();

        final Iterator<DefaultBatchTaskItem> iterator =
                Stream.of(idArr).distinct().map(id -> DefaultBatchTaskItem.builder().itemId(id).itemName(itemName).build()).iterator();
        CleanUserProfilePathInfoBatchTaskHandler handler = new CleanUserProfilePathInfoBatchTaskHandler(iterator, auditLogAPI, userProfileMgmtAPI,
                cbbDeskMgmtAPI);
        handler.setUserProfileTargetId(webRequest.getId());
        handler.setCleanType(cleanType);
        handler.setUserProfileValidateAPI(userProfileValidaAPI);
        BatchTaskSubmitResult batchTaskSubmitResult = builder.setTaskName(UserProfileBusinessKey.RCDC_RCO_USER_PROFILE_PATH_CLEAN_PATH_TASK_NAME)
                .setTaskDesc(UserProfileBusinessKey.RCDC_RCO_USER_PROFILE_PATH_BATCH_CLEAN_TASK_DESC).registerHandler(handler).start();

        return CommonWebResponse.success(batchTaskSubmitResult);
    }

    /**
     * 导入路径特殊配置
     *
     * @param file           特殊配置文件
     * @param builder        批处理对象
     * @param sessionContext 用户上下文
     * @return 相应对象
     * @throws BusinessException 异常处理
     * @throws IOException       IO异常
     */
    @ApiOperation("导入路径特殊配置")
    @RequestMapping(value = "specialConfig/importPath", method = RequestMethod.POST)
    public ImportUserWebResponse importSpecialConfig(ChunkUploadFile file, BatchTaskBuilder builder, SessionContext sessionContext)
            throws BusinessException, IOException {
        Assert.notNull(file, "file is not null");
        Assert.notNull(builder, "builder is not null");
        Assert.notNull(sessionContext, "sessionContext is null");

        String userName = sessionContext.getUserName();
        String content = specialConfigAPI.importUserProfilePathSpecialConfig(file);
        List<UserProfilePathDTO> userProfilePathDTOList = JSON.parseArray(content, UserProfilePathDTO.class);

        try {
            // 开始导入软件，如果已存在导入任务不允许导入
            userProfileMgmtAPI.isImportingPath();
            userProfileMgmtAPI.startAddPathData();

            // 校验通过，开始执行批量任务
            UserProfilePathDTO[] userProfilePathArr = userProfilePathDTOList.toArray(new UserProfilePathDTO[userProfilePathDTOList.size()]);
            final Iterator<CreatePathBatchTaskItem> iterator = Stream.of(userProfilePathArr)
                    .map(pathDTO -> CreatePathBatchTaskItem.builder().itemId(UUID.randomUUID()) //
                            .itemName(UserProfileBusinessKey.RCDC_RCO_IMPORT_PATH_ITEM_NAME, new String[]{}).itemPath(pathDTO).build())
                    .iterator();
            ImportPathBatchTaskHandlerRequest request = new ImportPathBatchTaskHandlerRequest();
            request.setBatchTaskItemIterator(iterator);
            request.setAuditLogAPI(auditLogAPI);
            request.setUserProfileMgmtAPI(userProfileMgmtAPI);
            request.setUserName(userName);
            ImportSpecialPathBatchTaskHandler handler = new ImportSpecialPathBatchTaskHandler(request);
            handler.setUserProfileValidateAPI(userProfileValidaAPI);
            BatchTaskSubmitResult batchTaskSubmitResult = builder.setTaskName(UserProfileBusinessKey.RCDC_RCO_IMPORT_PATH_ITEM_NAME)
                    .setTaskDesc(UserProfileBusinessKey.RCDC_RCO_IMPORT_PATH_TASK_DESC).registerHandler(handler).start();
            ImportUserWebResponse result = new ImportUserWebResponse();
            result.setStatus(WebResponse.Status.SUCCESS);
            result.setContent(batchTaskSubmitResult);
            return result;
        } catch (BusinessException e) {
            LOGGER.error("批量导入任务出现异常,信息:", e);
            throw e;
        } finally {
            // 结束任务删除缓存标记
            userProfileMgmtAPI.finishAddPathData();
        }
    }


    /**
     * 获取路径特殊配置信息
     *
     * @return CommonWebResponse
     */
    @ApiOperation("获取特殊路径配置信息")
    @RequestMapping(value = "specialConfig/getInfo", method = RequestMethod.POST)
    public CommonWebResponse getSpecialConfigInfo() {
        return CommonWebResponse.success(specialConfigAPI.getUserProfilePathSpecialConfig());
    }

}