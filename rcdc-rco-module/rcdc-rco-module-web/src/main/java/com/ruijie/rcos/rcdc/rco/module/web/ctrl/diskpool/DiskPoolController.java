package com.ruijie.rcos.rcdc.rco.module.web.ctrl.diskpool;

import java.util.*;

import com.ruijie.rcos.rcdc.rco.module.web.util.WebBatchTaskUtils;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.ArrayUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.Assert;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import com.ruijie.rcos.gss.base.iac.module.annotation.EnableAuthority;
import com.ruijie.rcos.gss.log.module.def.api.BaseAuditLogAPI;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.api.CbbDiskPoolMgmtAPI;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.api.CbbVDIDeskDiskAPI;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.dto.disk.CbbCheckDiskPoolNameDTO;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.dto.diskpool.CbbCreateDiskPoolDTO;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.dto.diskpool.CbbDiskPoolDTO;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.enums.CbbDiskPoolState;
import com.ruijie.rcos.rcdc.hciadapter.module.def.api.CloudPlatformValidateAPI;
import com.ruijie.rcos.rcdc.rco.module.def.api.*;
import com.ruijie.rcos.rcdc.rco.module.def.api.dto.MatchEqual;
import com.ruijie.rcos.rcdc.rco.module.def.api.dto.UserListDTO;
import com.ruijie.rcos.rcdc.rco.module.def.api.enums.AdminDataPermissionType;
import com.ruijie.rcos.rcdc.rco.module.def.api.request.UserPageSearchRequest;
import com.ruijie.rcos.rcdc.rco.module.def.api.request.aaa.ListUserGroupIdRequest;
import com.ruijie.rcos.rcdc.rco.module.def.api.response.aaa.ListUserGroupIdResponse;
import com.ruijie.rcos.rcdc.rco.module.def.desktoppool.dto.UpdatePoolBindObjectDTO;
import com.ruijie.rcos.rcdc.rco.module.def.diskpool.dto.DiskPoolOverviewDTO;
import com.ruijie.rcos.rcdc.rco.module.def.diskpool.dto.DiskPoolStatisticDTO;
import com.ruijie.rcos.rcdc.rco.module.def.diskpool.dto.DiskPoolUserWithAssignmentDTO;
import com.ruijie.rcos.rcdc.rco.module.def.user.common.UserCommonHelper;
import com.ruijie.rcos.rcdc.rco.module.def.utils.PermissionHelper;
import com.ruijie.rcos.rcdc.rco.module.web.ctrl.common.request.CommonPageQueryRequest;
import com.ruijie.rcos.rcdc.rco.module.web.ctrl.diskpool.batchtask.CreateDiskPoolBatchTaskHandler;
import com.ruijie.rcos.rcdc.rco.module.web.ctrl.diskpool.batchtask.DeleteDiskPoolBatchTaskHandler;
import com.ruijie.rcos.rcdc.rco.module.web.ctrl.diskpool.batchtask.EditDiskPoolBatchTaskHandler;
import com.ruijie.rcos.rcdc.rco.module.web.ctrl.diskpool.batchtask.UpdateDiskPoolUserBatchHandler;
import com.ruijie.rcos.rcdc.rco.module.web.ctrl.diskpool.request.*;
import com.ruijie.rcos.rcdc.rco.module.web.response.CommonWebResponse;
import com.ruijie.rcos.rcdc.rco.module.web.service.UserGroupHelper;
import com.ruijie.rcos.rcdc.rco.module.web.validation.DiskPoolValidation;
import com.ruijie.rcos.sk.base.batch.BatchTaskBuilder;
import com.ruijie.rcos.sk.base.batch.BatchTaskSubmitResult;
import com.ruijie.rcos.sk.base.batch.DefaultBatchTaskItem;
import com.ruijie.rcos.sk.base.exception.BusinessException;
import com.ruijie.rcos.sk.base.i18n.LocaleI18nResolver;
import com.ruijie.rcos.sk.base.log.Logger;
import com.ruijie.rcos.sk.base.log.LoggerFactory;
import com.ruijie.rcos.sk.base.sm2.StateMachineFactory;
import com.ruijie.rcos.sk.base.validation.EnableCustomValidate;
import com.ruijie.rcos.sk.modulekit.api.comm.DefaultPageResponse;
import com.ruijie.rcos.sk.pagekit.api.PageQueryBuilderFactory;
import com.ruijie.rcos.sk.pagekit.api.PageQueryRequest;
import com.ruijie.rcos.sk.pagekit.api.PageQueryResponse;
import com.ruijie.rcos.sk.webmvc.api.request.IdWebRequest;
import com.ruijie.rcos.sk.webmvc.api.request.PageWebRequest;
import com.ruijie.rcos.sk.webmvc.api.session.SessionContext;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

/**
 * Description: 磁盘池管理
 * Copyright: Copyright (c) 2022
 * Company: Ruijie Co., Ltd.
 * Create Time: 2022/7/6
 *
 * @author TD
 */
@Api(tags = "磁盘池管理")
@Controller
@RequestMapping("/rco/disk/pool")
@EnableCustomValidate(validateClass = DiskPoolValidation.class)
public class DiskPoolController {

    private static final Logger LOGGER = LoggerFactory.getLogger(DiskPoolController.class);

    @Autowired
    private DiskPoolMgmtAPI diskPoolMgmtAPI;

    @Autowired
    private CbbVDIDeskDiskAPI cbbVDIDeskDiskAPI;

    @Autowired
    private BaseAuditLogAPI auditLogAPI;

    @Autowired
    private UserDiskMgmtAPI userDiskMgmtAPI;

    @Autowired
    private CbbDiskPoolMgmtAPI cbbDiskPoolMgmtAPI;

    @Autowired
    private DiskPoolUserAPI diskPoolUserAPI;

    @Autowired
    private PageQueryBuilderFactory pageQueryBuilderFactory;

    @Autowired
    private StateMachineFactory stateMachineFactory;

    @Autowired
    private PermissionHelper permissionHelper;

    @Autowired
    private UserCommonHelper userCommonHelper;

    @Autowired
    private AdminDataPermissionAPI adminDataPermissionAPI;

    @Autowired
    private UserMgmtAPI userMgmtAPI;

    @Autowired
    private CloudPlatformValidateAPI cloudPlatformValidateAPI;

    /**
     * 基本查询明细
     *
     * @param request 页面请求参数
     * @return DesktopPoolDetailDTO
     * @throws BusinessException 业务异常
     */
    @ApiOperation("获取磁盘池基本信息")
    @RequestMapping(value = "/detail", method = RequestMethod.POST)
    public CommonWebResponse<DiskPoolStatisticDTO> detail(IdWebRequest request) throws BusinessException {
        Assert.notNull(request, "detail request must not be null");
        Assert.notNull(request.getId(), "request id must not be null");
        return CommonWebResponse.success(diskPoolMgmtAPI.diskPoolStatistic(request.getId()));
    }

    /**
     * 获取磁盘池列表
     *
     * @param request 请求参数
     * @param sessionContext session信息
     * @return 磁盘池列表
     * @throws BusinessException 业务异常
     */
    @ApiOperation("获取磁盘池列表")
    @RequestMapping(value = "/list", method = RequestMethod.POST)
    public CommonWebResponse<PageQueryResponse<DiskPoolStatisticDTO>> list(CommonPageQueryRequest request, SessionContext sessionContext)
            throws BusinessException {
        Assert.notNull(request, "list request must not be null");
        Assert.notNull(sessionContext, "sessionContext request must not be null");
        PageQueryBuilderFactory.RequestBuilder requestBuilder = pageQueryBuilderFactory.newRequestBuilder(request);
        if (!Objects.equals(Boolean.TRUE, request.getNoPermission()) && !permissionHelper.isAllGroupPermission(sessionContext)) {
            UUID[] diskPoolIdArr = permissionHelper.getPermissionIdArr(sessionContext, AdminDataPermissionType.DISK_POOL);
            if (ArrayUtils.isEmpty(diskPoolIdArr)) {
                return CommonWebResponse.success(new PageQueryResponse<>());
            }
            requestBuilder.in("id", diskPoolIdArr);
        }
        return CommonWebResponse.success(diskPoolMgmtAPI.pageDiskPool(requestBuilder.build()));
    }

    /**
     * 磁盘池总览
     *
     * @return CommonWebResponse<DiskPoolOverviewDTO>
     */
    @ApiOperation("磁盘池总览")
    @RequestMapping(value = "/overview", method = RequestMethod.POST)
    public CommonWebResponse<DiskPoolOverviewDTO> overview() {
        return CommonWebResponse.success(diskPoolMgmtAPI.getDiskPoolOverview());
    }

    /**
     * 校验磁盘池名称重复
     *
     * @param request 请求参数
     * @return 校验结果返回
     * @throws BusinessException 异常信息
     */
    @ApiOperation("校验磁盘池重名")
    @RequestMapping(value = "/checkNameDuplication", method = RequestMethod.POST)
    public CommonWebResponse<CheckDiskPoolNameWebResponse> checkDiskPoolNameDuplication(DiskPoolNameWebRequest request) throws BusinessException {
        Assert.notNull(request, "checkDiskPoolNameDuplication request must not be null");

        CbbCheckDiskPoolNameDTO poolNameDTO = new CbbCheckDiskPoolNameDTO();
        BeanUtils.copyProperties(request, poolNameDTO);
        Boolean hasDuplicate = cbbDiskPoolMgmtAPI.checkDiskPoolNameDuplicate(poolNameDTO);

        return CommonWebResponse.success(new CheckDiskPoolNameWebResponse(hasDuplicate));
    }


    /**
     * 校验磁盘池名称前缀重名
     *
     * @param request 请求参数
     * @return 校验结果返回
     * @throws BusinessException 异常信息
     */
    @ApiOperation("校验磁盘池名称前缀重名")
    @RequestMapping(value = "/checkNamePrefixDuplication", method = RequestMethod.POST)
    public CommonWebResponse<CheckDiskPoolNameWebResponse> checkDiskNamePrefixDuplication(DiskNamePrefixWebRequest request) throws BusinessException {
        Assert.notNull(request, "checkDiskNamePrefixDuplication request must not be null");

        CbbCheckDiskPoolNameDTO poolNameDTO = new CbbCheckDiskPoolNameDTO();
        BeanUtils.copyProperties(request, poolNameDTO);
        poolNameDTO.setName(request.getDiskNamePrefix());
        Boolean hasDuplicate = cbbDiskPoolMgmtAPI.checkDiskNamePrefixDuplicate(poolNameDTO);

        return CommonWebResponse.success(new CheckDiskPoolNameWebResponse(hasDuplicate));
    }

    /**
     * 创建磁盘池
     *
     * @param request 请求参数
     * @param builder 批处理构造类
     * @param sessionContext session信息
     * @return 任务信息
     */
    @ApiOperation("创建磁盘池")
    @RequestMapping(value = "/create", method = RequestMethod.POST)
    @EnableCustomValidate(validateMethod = "diskPoolCreateValidation")
    public CommonWebResponse<Object> create(CreateDiskPoolWebRequest request, BatchTaskBuilder builder, SessionContext sessionContext) {
        Assert.notNull(request, "createDiskPool request must not be null");
        Assert.notNull(builder, "builder request must not be null");
        Assert.notNull(sessionContext, "sessionContext cannot be null");
        LOGGER.info("createDiskPool CreateDiskPoolWebRequest:{}", request.toString());
        String poolName = request.getName();
        request.setDiskNamePrefix(StringUtils.isEmpty(request.getDiskNamePrefix()) ? poolName : request.getDiskNamePrefix());
        CbbDiskPoolDTO diskPoolDTO = null;
        try {
            // 创建磁盘池
            UUID diskPoolId = UUID.randomUUID();
            // 添加数据权限
            permissionHelper.saveAdminGroupPermission(sessionContext, diskPoolId, AdminDataPermissionType.DISK_POOL);

            CbbCreateDiskPoolDTO createDiskPoolDTO = new CbbCreateDiskPoolDTO();
            BeanUtils.copyProperties(request, createDiskPoolDTO);
            createDiskPoolDTO.setId(diskPoolId);
            createDiskPoolDTO.setPoolState(request.getDiskNum() > 0 ? CbbDiskPoolState.CREATING : CbbDiskPoolState.AVAILABLE);
            cbbDiskPoolMgmtAPI.createDiskPool(createDiskPoolDTO);

            diskPoolDTO = cbbDiskPoolMgmtAPI.getDiskPoolDetail(diskPoolId);

            // 创建异步执行任务--创建桌面
            if (request.getDiskNum() <= 0) {
                auditLogAPI.recordLog(DiskPoolBusinessKey.RCDC_RCO_DISK_POOL_CREATE_SUCCESS_LOG, poolName);
                return CommonWebResponse.success();
            }
            BatchTaskSubmitResult result = batchCreatePoolDisk(createDiskPoolDTO.getDiskNum(), diskPoolDTO, builder);
            auditLogAPI.recordLog(DiskPoolBusinessKey.RCDC_RCO_DISK_POOL_CREATE_SUCCESS_LOG, poolName);
            return CommonWebResponse.success(result);
        } catch (Exception e) {
            LOGGER.error("创建磁盘池发生异常，异常原因：", e);
            if (Objects.nonNull(diskPoolDTO)) {
                cbbDiskPoolMgmtAPI.updateState(diskPoolDTO.getId(), CbbDiskPoolState.AVAILABLE);
            }
            String message = e instanceof BusinessException ? ((BusinessException) e).getI18nMessage() : e.getMessage();
            auditLogAPI.recordLog(DiskPoolBusinessKey.RCDC_RCO_DISK_POOL_CREATE_FAIL_LOG, poolName, message);
            return CommonWebResponse.fail(DiskPoolBusinessKey.RCDC_RCO_DISK_POOL_CREATE_FAIL_LOG, new String[] {poolName, message});
        }
    }

    /**
     * 编辑磁盘池
     *
     * @param request 编辑磁盘池请求类
     * @param builder 批处理构造类
     * @return 编辑磁盘结果
     */
    @ApiOperation("编辑磁盘池")
    @RequestMapping(value = "/edit", method = RequestMethod.POST)
    @EnableCustomValidate(validateMethod = "diskPoolEditValidation")
    public CommonWebResponse<Object> edit(UpdateDiskPoolWebRequest request, BatchTaskBuilder builder) {
        Assert.notNull(request, "UpdateDiskPoolWebRequest can not be null");
        Assert.notNull(builder, "builder request must not be null");
        Assert.notNull(request.getId(), "UpdateDiskPoolWebRequest--->id can not be null");

        String taskName = LocaleI18nResolver.resolve(DiskPoolBusinessKey.RCDC_RCO_EDIT_DISK_POOL);
        List<DefaultBatchTaskItem> taskItemList =
                Lists.newArrayList(DefaultBatchTaskItem.builder().itemId(request.getId()).itemName(taskName).build());

        try {
            CbbDiskPoolDTO diskPoolDTO = cbbDiskPoolMgmtAPI.getDiskPoolDetail(request.getId());
            BeanUtils.copyProperties(request, diskPoolDTO);
            EditDiskPoolBatchTaskHandler handler = new EditDiskPoolBatchTaskHandler(taskItemList, diskPoolDTO);

            BatchTaskSubmitResult result = builder.setTaskName(DiskPoolBusinessKey.RCDC_RCO_EDIT_DISK_POOL)
                    .setTaskDesc(DiskPoolBusinessKey.RCDC_RCO_EDIT_DISK_POOL_TASK_DESC).setUniqueId(request.getId()).enableParallel()
                    .registerHandler(handler).start();

            return CommonWebResponse.success(result);
        } catch (BusinessException e) {
            LOGGER.error("编辑磁盘池出现异常", e);
            auditLogAPI.recordLog(DiskPoolBusinessKey.RCDC_RCO_DISK_POOL_EDIT_FAIL_LOG, e.getI18nMessage());
            return CommonWebResponse.fail(DiskPoolBusinessKey.RCDC_RCO_DISK_POOL_EDIT_FAIL_LOG, new String[] {request.getName(), e.getI18nMessage()});
        }
    }

    /**
     * 删除磁盘池
     *
     * @param request 请求参数
     * @param builder 批处理构造类
     * @return 任务信息
     * @throws BusinessException 业务异常
     */
    @ApiOperation("删除磁盘池")
    @RequestMapping(value = "/delete", method = RequestMethod.POST)
    public CommonWebResponse<Object> delete(DeleteDiskPoolWebRequest request, BatchTaskBuilder builder) throws BusinessException {
        Assert.notNull(request, "deleteDiskPool request must not be null");
        Assert.notNull(builder, "builder request must not be null");

        String prefix = WebBatchTaskUtils.getDeletePrefix(request.getShouldOnlyDeleteDataFromDb());

        UUID[] diskPoolIdArr = request.getIdArr();
        String taskName = LocaleI18nResolver.resolve(DiskPoolBusinessKey.RCDC_RCO_DELETE_DISK_POOL, prefix);
        Iterator<DefaultBatchTaskItem> iterator =
                Arrays.stream(diskPoolIdArr).distinct().map(id -> DefaultBatchTaskItem.builder().itemId(id).itemName(taskName).build()).iterator();

        Boolean shouldOnlyDeleteDataFromDb = request.getShouldOnlyDeleteDataFromDb();
        DeleteDiskPoolBatchTaskHandler handler =
                new DeleteDiskPoolBatchTaskHandler(iterator, cbbDiskPoolMgmtAPI, cbbVDIDeskDiskAPI, shouldOnlyDeleteDataFromDb);
        handler.setAuditLogAPI(auditLogAPI);
        handler.setUserDiskMgmtAPI(userDiskMgmtAPI);
        handler.setDiskPoolMgmtAPI(diskPoolMgmtAPI);
        handler.setStateMachineFactory(stateMachineFactory);
        handler.setCloudPlatformValidateAPI(cloudPlatformValidateAPI);
        BatchTaskSubmitResult result;
        if (diskPoolIdArr.length == 1) {
            handler.setDiskPoolName(cbbDiskPoolMgmtAPI.getDiskPoolDetail(diskPoolIdArr[0]).getName());
            result = builder.setTaskName(DiskPoolBusinessKey.RCDC_RCO_DELETE_DISK_POOL, prefix)
                    .setTaskDesc(DiskPoolBusinessKey.RCDC_RCO_DELETE_DISK_POOL_TASK_DESC, prefix).setUniqueId(diskPoolIdArr[0]).enableParallel()
                    .registerHandler(handler).start();
        } else {
            result = builder.setTaskName(DiskPoolBusinessKey.RCDC_RCO_BATCH_DELETE_DISK_POOL, prefix)
                    .setTaskDesc(DiskPoolBusinessKey.RCDC_RCO_BATCH_DELETE_DISK_POOL_TASK_DESC, prefix).setUniqueId(diskPoolIdArr[0]).enableParallel()
                    .registerHandler(handler).start();
        }
        return CommonWebResponse.success(result);
    }

    /**
     * 给磁盘池添加磁盘
     *
     * @param request 请求参数
     * @param builder 批处理构造类
     * @return 任务信息
     * @throws BusinessException 业务异常
     */
    @ApiOperation("给磁盘池添加磁盘")
    @RequestMapping(value = "/addDisk", method = RequestMethod.POST)
    public CommonWebResponse<Object> addDisk(DiskPoolAddDiskWebRequest request, BatchTaskBuilder builder) throws BusinessException {
        Assert.notNull(request, "addDisk request must not be null");
        Assert.notNull(builder, "addDisk builder request must not be null");
        UUID diskPoolId = request.getDiskPoolId();
        CbbDiskPoolDTO diskPoolDTO = null;
        try {
            diskPoolDTO = cbbDiskPoolMgmtAPI.getDiskPoolDetail(diskPoolId);
            int diskNum = request.getAddNum();
            if (diskNum <= 0) {
                LOGGER.info("磁盘池[{}]添加磁盘成功，数量:{}", diskPoolDTO.getName(), diskNum);
                return CommonWebResponse.success();
            }
            // 校验磁盘池状态是否可用
            checkDiskPoolState(diskPoolDTO);
            cbbDiskPoolMgmtAPI.updateState(diskPoolId, CbbDiskPoolState.UPDATING);
            BatchTaskSubmitResult batchTaskSubmitResult = batchCreatePoolDisk(diskNum, diskPoolDTO, builder);
            return CommonWebResponse.success(batchTaskSubmitResult);
        } catch (Exception e) {
            LOGGER.error("磁盘池添加磁盘发生异常，异常原因：", e);
            if (Objects.nonNull(diskPoolDTO)) {
                cbbDiskPoolMgmtAPI.updateState(diskPoolId, CbbDiskPoolState.AVAILABLE);
            }
            String message = e instanceof BusinessException ? ((BusinessException) e).getI18nMessage() : e.getMessage();
            String signName = Objects.nonNull(diskPoolDTO) ? diskPoolDTO.getName() : diskPoolId.toString();
            auditLogAPI.recordLog(DiskPoolBusinessKey.RCDC_RCO_DISK_POOL_ADD_DISK_FAIL_LOG, signName, message);
            return CommonWebResponse.fail(DiskPoolBusinessKey.RCDC_RCO_DISK_POOL_ADD_DISK_FAIL_LOG, new String[] {signName, message});
        }
    }

    private void checkDiskPoolState(CbbDiskPoolDTO diskPoolDTO) throws BusinessException {
        if (diskPoolDTO.getPoolState() != CbbDiskPoolState.AVAILABLE) {
            throw new BusinessException(DiskPoolBusinessKey.RCDC_RCO_DISK_POOL_STATE_NOT_AVAILABLE, diskPoolDTO.getName());
        }
    }

    private BatchTaskSubmitResult batchCreatePoolDisk(int diskNum, CbbDiskPoolDTO diskPoolDTO, BatchTaskBuilder builder) throws BusinessException {

        int maxIndex = diskPoolMgmtAPI.getMaxIndexNumWhenAddDisk(diskPoolDTO.getId());
        String taskName = LocaleI18nResolver.resolve(DiskPoolBusinessKey.RCDC_RCO_DISK_POOL_CREATE_DISK_TASK_NAME);

        List<DefaultBatchTaskItem> taskItemList = new ArrayList<>();
        for (int i = 0; i < diskNum; i++) {
            taskItemList.add(DefaultBatchTaskItem.builder().itemId(UUID.randomUUID()).itemName(taskName).build());
        }
        CreateDiskPoolBatchTaskHandler handler = new CreateDiskPoolBatchTaskHandler(diskPoolDTO, maxIndex, taskItemList.iterator());
        handler.setAuditLogAPI(auditLogAPI).setCbbDiskPoolMgmtAPI(cbbDiskPoolMgmtAPI).setDeskDiskAPI(cbbVDIDeskDiskAPI);
        return builder.setTaskName(DiskPoolBusinessKey.RCDC_RCO_DISK_POOL_CREATE_DISK_TASK_NAME)
                .setTaskDesc(DiskPoolBusinessKey.RCDC_RCO_DISK_POOL_CREATE_DISK_TASK_DESC, diskPoolDTO.getName()).enableParallel()
                .registerHandler(handler).start();
    }

    /**
     * 获取已分配到磁盘池的用户列表
     *
     * @param request 请求参数对象
     * @param sessionContext session信息
     * @return 返回消息记录列表
     * @throws BusinessException 业务异常
     */
    @ApiOperation(value = "获取已分配到磁盘池的用户列表")
    @RequestMapping(value = "/user/list", method = RequestMethod.POST)
    public CommonWebResponse<DefaultPageResponse<UserListDTO>> getUserList(PageWebRequest request, SessionContext sessionContext)
            throws BusinessException {
        Assert.notNull(request, "PageWebRequest request  can not be null");
        Assert.notNull(sessionContext, "sessionContext request  can not be null");
        DiskPoolRealBindUserPageRequest pageRequest = new DiskPoolRealBindUserPageRequest(request);
        Assert.notNull(pageRequest.getDiskPoolId(), "pageRequest.diskPoolId must not be null");
        userCommonHelper.dealNonVisitorUserTypeMatch(pageRequest);
        if (!permissionHelper.isAllGroupPermission(sessionContext)) {
            List<String> userGroupIdList = getPermissionUserGroupIdList(sessionContext.getUserId());
            if (CollectionUtils.isNotEmpty(userGroupIdList)) {
                UUID[] uuidArr = userGroupIdList.stream().filter(idStr -> !idStr.equals(UserGroupHelper.USER_GROUP_ROOT_ID)).map(UUID::fromString)
                        .toArray(UUID[]::new);
                pageRequest.appendCustomMatchEqual(new MatchEqual(UserPageSearchRequest.GROUP_ID, uuidArr));
            }
        }
        DefaultPageResponse<UserListDTO> pageResponse = diskPoolUserAPI.pageQueryDiskPoolBindUser(pageRequest.getDiskPoolId(), pageRequest);

        return CommonWebResponse.success(pageResponse);
    }

    /**
     * 获取磁盘池分配信息列表
     *
     * @param request 请求类
     * @param sessionContext 上下文
     * @return 用户及分配信息列表
     * @throws BusinessException 业务异常
     */
    @ApiOperation(value = "获取磁盘池分配信息列表")
    @RequestMapping(value = "/listWithAssignment", method = RequestMethod.POST)
    public CommonWebResponse<PageQueryResponse<DiskPoolUserWithAssignmentDTO>> getUserWithAssignmentList(PageQueryRequest request,
            SessionContext sessionContext) throws BusinessException {
        Assert.notNull(request, "PageQueryRequest can not be null");
        Assert.notNull(sessionContext, "sessionContext can not be null");
        PageQueryBuilderFactory.RequestBuilder builder = pageQueryBuilderFactory.newRequestBuilder(request);
        PageQueryResponse<DiskPoolUserWithAssignmentDTO> pageResponse = diskPoolUserAPI.pageUserWithAssignment(builder.build());
        return CommonWebResponse.success(pageResponse);
    }

    /**
     * 磁盘池分配用户
     *
     * @param request 编辑磁盘池request
     * @param builder builder
     * @param sessionContext sessionContext
     * @return 响应消息
     * @throws BusinessException BusinessException
     */
    @ApiOperation("磁盘池分配用户")
    @RequestMapping(value = "/user/update", method = RequestMethod.POST)
    @EnableAuthority
    public CommonWebResponse<?> updatePoolBindObject(DiskPoolUpdateBindObjWebRequest request, BatchTaskBuilder builder, SessionContext sessionContext)
            throws BusinessException {
        Assert.notNull(request, "request cannot be null");
        Assert.notNull(builder, "builder cannot be null");
        Assert.notNull(sessionContext, "sessionContext cannot be null");

        String poolName = request.getDiskPoolId().toString();
        CbbDiskPoolDTO diskPoolDTO;
        try {
            diskPoolDTO = cbbDiskPoolMgmtAPI.getDiskPoolDetail(request.getDiskPoolId());
            poolName = diskPoolDTO.getName();
        } catch (BusinessException e) {
            LOGGER.error("编辑磁盘池用户或用户组发生异常", e);
            auditLogAPI.recordLog(DiskPoolBusinessKey.RCDC_RCO_DISK_POOL_UPDATE_BIND_OBJ_FAIL_LOG, poolName, e.getI18nMessage());
            return CommonWebResponse.fail(DiskPoolBusinessKey.RCDC_RCO_DISK_POOL_UPDATE_BIND_OBJ_FAIL, new String[] {poolName, e.getI18nMessage()});
        }

        UpdatePoolBindObjectDTO bindObjectDTO = new UpdatePoolBindObjectDTO();
        BeanUtils.copyProperties(request, bindObjectDTO);
        bindObjectDTO.setPoolId(request.getDiskPoolId());

        // 获取管理员的数据权限，超管就是null
        UUID[] groupIdArr = null;
        if (!permissionHelper.isAllGroupPermission(sessionContext)) {
            groupIdArr = permissionHelper.getPermissionIdArr(sessionContext, AdminDataPermissionType.USER_GROUP);
            this.checkGroupPermission(bindObjectDTO, groupIdArr);
        }

        String taskName = LocaleI18nResolver.resolve(DiskPoolBusinessKey.RCDC_DISK_POOL_UPDATE_BIND_OBJ);
        DefaultBatchTaskItem taskItem = DefaultBatchTaskItem.builder().itemId(request.getDiskPoolId()).itemName(taskName).build();
        Iterator<DefaultBatchTaskItem> iterator = Lists.newArrayList(taskItem).iterator();

        UpdateDiskPoolUserBatchHandler handler = new UpdateDiskPoolUserBatchHandler(bindObjectDTO, groupIdArr, iterator);
        BatchTaskSubmitResult result = builder.setTaskName(DiskPoolBusinessKey.RCDC_DISK_POOL_UPDATE_BIND_OBJ_TASK_NAME)
                .setTaskDesc(DiskPoolBusinessKey.RCDC_DISK_POOL_UPDATE_BIND_OBJ_TASK_DESC, poolName).setUniqueId(request.getDiskPoolId())
                .registerHandler(handler).start();
        return CommonWebResponse.success(result);
    }

    private void checkGroupPermission(UpdatePoolBindObjectDTO bindObjectDTO, UUID[] groupIdArr) throws BusinessException {
        if (ArrayUtils.isEmpty(groupIdArr)) {
            throw new BusinessException(DiskPoolBusinessKey.RCDC_RCO_DISK_POOL_NO_USER_GROUP_AUTH);
        }
        // 校验上传的这些涉及用户组的数据是否有权限
        Set<UUID> authGroupIdSet = Sets.newHashSet(groupIdArr);
        List<UUID> groupIdList = bindObjectDTO.getAllInvolvedGroupIdList();
        isGroupsHasAuth(groupIdList, authGroupIdSet, DiskPoolBusinessKey.RCDC_RCO_DISK_POOL_NO_USER_GROUP_AUTH);

        List<UUID> userIdList = bindObjectDTO.getAllInvolvedUserIdList();
        if (CollectionUtils.isEmpty(userIdList)) {
            return;
        }
        // 获取用户的用户组集合，校验是否有权限
        groupIdList = userMgmtAPI.listGroupIdByUserIdList(userIdList);
        isGroupsHasAuth(groupIdList, authGroupIdSet, DiskPoolBusinessKey.RCDC_RCO_DISK_POOL_NO_USER_AUTH);
    }

    private void isGroupsHasAuth(List<UUID> groupIdList, Set<UUID> authGroupIdSet, String errorKey) throws BusinessException {
        if (CollectionUtils.isEmpty(groupIdList)) {
            return;
        }
        // 检查是否存在不在这个管理员权限内的组ID
        if (groupIdList.stream().anyMatch(item -> !authGroupIdSet.contains(item))) {
            throw new BusinessException(errorKey);
        }
    }

    /**
     * 根据用户获取磁盘池列表
     *
     * @param request 请求参数
     * @return 磁盘池列表
     * @throws BusinessException 业务异常
     */
    @ApiOperation("查询用户所在的磁盘池")
    @RequestMapping(value = "/listByUser", method = RequestMethod.POST)
    public CommonWebResponse<DefaultPageResponse<DiskPoolStatisticDTO>> listByUser(PageWebRequest request) throws BusinessException {
        Assert.notNull(request, "list request must not be null");
        UserDiskPoolPageSearchRequest pageRequest = new UserDiskPoolPageSearchRequest(request);
        Assert.notNull(pageRequest.getUserId(), "pageRequest.userId must not be null");
        return CommonWebResponse.success(diskPoolMgmtAPI.pageDiskPoolByUserId(pageRequest.getUserId(), pageRequest));
    }

    /**
     * 根据AD域组获取磁盘池列表
     *
     * @param request 请求参数
     * @return 磁盘池列表
     * @throws BusinessException 业务异常
     */
    @ApiOperation("查询AD域组所在的磁盘池")
    @RequestMapping(value = "/listByAdGroup", method = RequestMethod.POST)
    public CommonWebResponse<DefaultPageResponse<DiskPoolStatisticDTO>> listByAdGroup(PageWebRequest request) throws BusinessException {
        Assert.notNull(request, "list request must not be null");
        UserDiskPoolPageSearchRequest pageRequest = new UserDiskPoolPageSearchRequest(request);
        Assert.notNull(pageRequest.getAdGroupId(), "pageRequest.adGroupId must not be null");
        return CommonWebResponse.success(diskPoolMgmtAPI.pageDiskPoolByAdGroupId(pageRequest.getAdGroupId(), pageRequest));
    }

    private List<String> getPermissionUserGroupIdList(UUID adminId) throws BusinessException {
        ListUserGroupIdRequest listUserGroupIdRequest = new ListUserGroupIdRequest();
        listUserGroupIdRequest.setAdminId(adminId);
        ListUserGroupIdResponse listUserGroupIdResponse = adminDataPermissionAPI.listUserGroupIdByAdminId(listUserGroupIdRequest);
        return listUserGroupIdResponse.getUserGroupIdList();
    }
}
