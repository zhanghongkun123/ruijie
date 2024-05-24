package com.ruijie.rcos.rcdc.rco.module.web.ctrl.disk;

import com.ruijie.rcos.gss.log.module.def.api.BaseAuditLogAPI;
import com.ruijie.rcos.gss.sdk.iac.module.def.api.IacUserMgmtAPI;
import com.ruijie.rcos.gss.sdk.iac.module.def.dto.user.IacUserDetailDTO;
import com.ruijie.rcos.gss.sdk.iac.module.def.enums.IacConfigRelatedType;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.api.CbbDiskPoolMgmtAPI;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.api.CbbVDIDeskDiskAPI;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.dto.CbbDeskDiskDTO;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.enums.CbbDiskState;
import com.ruijie.rcos.rcdc.hciadapter.module.def.api.StoragePoolServerMgmtAPI;
import com.ruijie.rcos.rcdc.hciadapter.module.def.dto.PlatformStoragePoolDTO;
import com.ruijie.rcos.rcdc.rco.module.def.api.DiskPoolMgmtAPI;
import com.ruijie.rcos.rcdc.rco.module.def.api.UserDiskMgmtAPI;
import com.ruijie.rcos.rcdc.rco.module.def.api.enums.AdminDataPermissionType;
import com.ruijie.rcos.rcdc.rco.module.def.diskpool.dto.UserDiskDetailDTO;
import com.ruijie.rcos.rcdc.rco.module.def.diskpool.enums.ExpandObjectTypeEnum;
import com.ruijie.rcos.rcdc.rco.module.def.utils.PermissionHelper;
import com.ruijie.rcos.rcdc.rco.module.web.ctrl.disk.batchtask.*;
import com.ruijie.rcos.rcdc.rco.module.web.ctrl.disk.request.BindUserWebRequest;
import com.ruijie.rcos.rcdc.rco.module.web.ctrl.disk.request.DeleteDiskWebRequest;
import com.ruijie.rcos.rcdc.rco.module.web.ctrl.disk.request.DiskArrWebRequest;
import com.ruijie.rcos.rcdc.rco.module.web.ctrl.disk.request.ExpandDiskWebRequest;
import com.ruijie.rcos.rcdc.rco.module.web.ctrl.diskpool.DiskPoolBusinessKey;
import com.ruijie.rcos.rcdc.rco.module.web.response.CommonWebResponse;
import com.ruijie.rcos.rcdc.rco.module.web.service.DiskBaseInfoMgmtWebService;
import com.ruijie.rcos.rcdc.rco.module.web.util.WebBatchTaskUtils;
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
import com.ruijie.rcos.sk.pagekit.api.PageQueryBuilderFactory;
import com.ruijie.rcos.sk.pagekit.api.PageQueryRequest;
import com.ruijie.rcos.sk.pagekit.api.PageQueryResponse;
import com.ruijie.rcos.sk.webmvc.api.request.IdWebRequest;
import com.ruijie.rcos.sk.webmvc.api.session.SessionContext;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.ArrayUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.Assert;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * Description: 磁盘操作相关接口
 * Copyright: Copyright (c) 2022
 * Company: Ruijie Co., Ltd.
 * Create Time: 2022/7/14
 *
 * @author TD
 */
@Api(tags = "磁盘管理")
@Controller
@RequestMapping("/rco/disk")
@EnableCustomValidate(validateClass = DiskPoolValidation.class)
public class DiskController {

    private static final Logger LOGGER = LoggerFactory.getLogger(DiskController.class);

    @Autowired
    private DiskPoolMgmtAPI diskPoolMgmtAPI;

    @Autowired
    private CbbDiskPoolMgmtAPI cbbDiskPoolMgmtAPI;

    @Autowired
    private CbbVDIDeskDiskAPI cbbVDIDeskDiskAPI;

    @Autowired
    private BaseAuditLogAPI auditLogAPI;

    @Autowired
    private PageQueryBuilderFactory pageQueryBuilderFactory;

    @Autowired
    private UserDiskMgmtAPI userDiskMgmtAPI;

    @Autowired
    private IacUserMgmtAPI cbbUserAPI;

    @Autowired
    private StateMachineFactory stateMachineFactory;

    @Autowired
    private PermissionHelper permissionHelper;

    @Autowired
    private StoragePoolServerMgmtAPI storagePoolServerMgmtAPI;

    @Autowired
    private DiskBaseInfoMgmtWebService diskInfoMgmtWebService;

    /**
     * 获取磁盘列表
     *
     * @param request        请求参数
     * @param sessionContext session信息
     * @return 任务信息
     * @throws BusinessException 业务异常
     */
    @ApiOperation("获取磁盘列表")
    @RequestMapping(value = "/list", method = RequestMethod.POST)
    public CommonWebResponse<PageQueryResponse<UserDiskDetailDTO>> diskList(PageQueryRequest request, SessionContext sessionContext)
            throws BusinessException {
        Assert.notNull(request, "diskList request must not be null");
        Assert.notNull(sessionContext, "sessionContext request must not be null");
        PageQueryBuilderFactory.RequestBuilder builder = pageQueryBuilderFactory.newRequestBuilder(request);
        if (!permissionHelper.isAllGroupPermission(sessionContext)) {
            UUID[] diskPoolIdArr = permissionHelper.getPermissionIdArr(sessionContext, AdminDataPermissionType.DISK_POOL);
            if (ArrayUtils.isEmpty(diskPoolIdArr)) {
                return CommonWebResponse.success(new PageQueryResponse<>());
            }
            builder.in("diskPoolId", diskPoolIdArr);
        }
        return CommonWebResponse.success(diskPoolMgmtAPI.pagePoolDiskUser(builder.build()));
    }

    /**
     * 获取磁盘详情
     *
     * @param request 请求参数
     * @return 磁盘信息
     * @throws BusinessException 业务异常
     */
    @ApiOperation("获取磁盘详情")
    @RequestMapping(value = "/detail", method = RequestMethod.POST)
    public CommonWebResponse<UserDiskDetailDTO> detail(IdWebRequest request) throws BusinessException {
        Assert.notNull(request, "diskList request must not be null");
        UserDiskDetailDTO userDiskDetailDTO = userDiskMgmtAPI.userDiskDetail(request.getId());
        //设置存储位置信息
        setStoragePoolName(userDiskDetailDTO);

        return CommonWebResponse.success(userDiskDetailDTO);
    }

    private void setStoragePoolName(UserDiskDetailDTO userDiskDetailDTO) throws BusinessException {
        if (StringUtils.hasText(userDiskDetailDTO.getAssignStoragePoolIds())) {
            //获取存储位置信息
            PlatformStoragePoolDTO storagePoolDTO = storagePoolServerMgmtAPI.getStoragePoolDetail(
                    UUID.fromString(userDiskDetailDTO.getAssignStoragePoolIds()));
            if (storagePoolDTO != null) {
                userDiskDetailDTO.setStoragePoolName(storagePoolDTO.getName());
            }
        }
    }


    /**
     * 删除磁盘
     *
     * @param request 请求参数
     * @param builder 批处理构造类
     * @return 任务信息
     * @throws BusinessException 业务异常
     */
    @ApiOperation("删除磁盘")
    @RequestMapping(value = "/delete", method = RequestMethod.POST)
    public CommonWebResponse<BatchTaskSubmitResult> deleteDisk(DeleteDiskWebRequest request, BatchTaskBuilder builder) throws BusinessException {
        Assert.notNull(request, "deleteDisk request must not be null");
        Assert.notNull(builder, "deleteDisk builder request must not be null");
        UUID[] diskIdArr = request.getIdArr();
        Boolean shouldOnlyDeleteDataFromDb = request.getShouldOnlyDeleteDataFromDb();
        String prefix = WebBatchTaskUtils.getDeletePrefix(shouldOnlyDeleteDataFromDb);

        String taskName = LocaleI18nResolver.resolve(DiskPoolBusinessKey.RCDC_RCO_DELETE_DISK, prefix);
        Iterator<DefaultBatchTaskItem> iterator =
                Arrays.stream(diskIdArr).distinct().map(id -> DefaultBatchTaskItem.builder().itemId(id).itemName(taskName).build()).iterator();


        DeleteDiskBatchTaskHandler handler = new DeleteDiskBatchTaskHandler(iterator, cbbVDIDeskDiskAPI, auditLogAPI, shouldOnlyDeleteDataFromDb);
        handler.setDiskPoolMgmtAPI(cbbDiskPoolMgmtAPI);
        handler.setUserDiskMgmtAPI(userDiskMgmtAPI);
        handler.setStateMachineFactory(stateMachineFactory);
        BatchTaskSubmitResult result;
        if (diskIdArr.length == 1) {
            handler.setDiskName(userDiskMgmtAPI.userDiskDetail(diskIdArr[0]).getDiskName());
            result = builder.setTaskName(DiskPoolBusinessKey.RCDC_RCO_DELETE_DISK, prefix).setTaskDesc(DiskPoolBusinessKey.RCDC_RCO_DELETE_DISK_TASK_DESC, prefix)
                    .setUniqueId(diskIdArr[0]).enableParallel().registerHandler(handler).start();
        } else {
            result = builder.setTaskName(DiskPoolBusinessKey.RCDC_RCO_BATCH_DELETE_DISK, prefix)
                    .setTaskDesc(DiskPoolBusinessKey.RCDC_RCO_BATCH_DELETE_DISK_TASK_DESC, prefix).setUniqueId(diskIdArr[0]).enableParallel()
                    .registerHandler(handler).start();
        }

        return CommonWebResponse.success(result);
    }

    /**
     * 扩容磁盘
     *
     * @param request 请求参数
     * @param builder 批处理构造类
     * @return 任务信息
     * @throws BusinessException 业务异常
     */
    @ApiOperation("扩容磁盘")
    @RequestMapping(value = "/expand", method = RequestMethod.POST)
    public CommonWebResponse<Object> expandDisk(ExpandDiskWebRequest request, BatchTaskBuilder builder) throws BusinessException {
        Assert.notNull(request, "expandDisk request must not be null");
        Assert.notNull(builder, "expandDisk builder request must not be null");
        LOGGER.info("expandDisk 请求参数信息:{}", request.toString());
        List<UUID> diskIdList = obtainExpandDiskList(request);
        if (CollectionUtils.isEmpty(diskIdList)) {
            // 有效的磁盘数量为空，直接结束
            auditLogAPI.recordLog(DiskPoolBusinessKey.RCDC_RCO_NO_DISK_EXPAND_SUCCESS_LOG);
            return CommonWebResponse.success(DiskPoolBusinessKey.RCDC_RCO_NO_DISK_EXPAND_SUCCESS_LOG, new String[0]);
        }
        LOGGER.info("expandDisk 可扩容磁盘信息: {}", ArrayUtils.toString(diskIdList));
        String taskName = LocaleI18nResolver.resolve(DiskPoolBusinessKey.RCDC_RCO_EXPAND_DISK);
        Iterator<DefaultBatchTaskItem> iterator =
                Stream.of(UUID.randomUUID()).map(id -> DefaultBatchTaskItem.builder().itemId(id).itemName(taskName).build()).iterator();

        ExpandDiskBatchTaskHandler handler = new ExpandDiskBatchTaskHandler(iterator, request.getExpandCapacity(), auditLogAPI);
        handler.setStateMachineFactory(stateMachineFactory);
        handler.setCbbVDIDeskDiskAPI(cbbVDIDeskDiskAPI);
        handler.setDiskList(diskIdList);
        BatchTaskSubmitResult result;
        if (diskIdList.size() == 1) {
            handler.setDiskName(cbbVDIDeskDiskAPI.getDiskDetail(diskIdList.get(0)).getName());
            result = builder.setTaskName(DiskPoolBusinessKey.RCDC_RCO_EXPAND_DISK).setTaskDesc(DiskPoolBusinessKey.RCDC_RCO_EXPAND_DISK_TASK_DESC)
                    .setUniqueId(diskIdList.get(0)).enableParallel().registerHandler(handler).start();
        } else {
            result = builder.setTaskName(DiskPoolBusinessKey.RCDC_RCO_BATCH_EXPAND_DISK)
                    .setTaskDesc(DiskPoolBusinessKey.RCDC_RCO_BATCH_EXPAND_DISK_TASK_DESC).setUniqueId(diskIdList.get(0)).enableParallel()
                    .registerHandler(handler).start();
        }
        return CommonWebResponse.success(result);
    }

    /**
     * 编辑磁盘；绑定/解绑用户
     *
     * @param request 请求参数
     * @return 编辑磁盘结果
     * @throws BusinessException 业务异常
     */
    @ApiOperation("编辑磁盘")
    @RequestMapping(value = "/edit", method = RequestMethod.POST)
    @EnableCustomValidate(validateMethod = "bindUserValidation")
    public CommonWebResponse<Object> bindUser(BindUserWebRequest request) {
        Assert.notNull(request, "bindUser request can not be null");
        UUID diskId = request.getDiskId();
        UUID userId = request.getUserId();
        UserDiskDetailDTO userDiskDetailDTO = null;
        try {
            userDiskDetailDTO = userDiskMgmtAPI.userDiskDetail(diskId);
            // 相等说明磁盘绑定用户信息未进行变更
            if (Objects.equals(userId, userDiskDetailDTO.getUserId())) {
                diskInfoMgmtWebService.remoteUpdateDiskBindUserInfo(userDiskDetailDTO.getPlatformId(), diskId, userDiskDetailDTO.getUserName());
                return CommonWebResponse.success();
            }
            userDiskMgmtAPI.bindUserOrOn(diskId, userId);
            String diskName = userDiskDetailDTO.getDiskName();
            // 用户为null，则解绑磁盘
            if (Objects.isNull(userId)) {
                auditLogAPI.recordLog(DiskPoolBusinessKey.RCDC_RCO_DISK_COVER_USER_UNBIND_SUCCESS_LOG, diskName, userDiskDetailDTO.getUserName());
                return CommonWebResponse.success(DiskPoolBusinessKey.RCDC_RCO_DISK_RELATION_USER_EDIT_SUCCESS_LOG, new String[] {diskName});
            }
            IacUserDetailDTO userDetail = cbbUserAPI.getUserDetail(userId);
            if (Objects.isNull(userDiskDetailDTO.getUserId())) {
                // 磁盘未进行磁盘绑定则直接绑定用户
                auditLogAPI.recordLog(DiskPoolBusinessKey.RCDC_RCO_DISK_COVER_USER_BIND_LOG, diskName, userDetail.getUserName());
            } else {
                // 磁盘原来已绑定用户先解绑后绑定其它用户
                auditLogAPI.recordLog(DiskPoolBusinessKey.RCDC_RCO_DISK_COVER_USER_UNBIND_SUCCESS_LOG, diskName, userDiskDetailDTO.getUserName());
                auditLogAPI.recordLog(DiskPoolBusinessKey.RCDC_RCO_DISK_COVER_USER_BIND_LOG, diskName, userDetail.getUserName());
            }
            return CommonWebResponse.success(DiskPoolBusinessKey.RCDC_RCO_DISK_RELATION_USER_EDIT_SUCCESS_LOG, new String[] {diskName});
        } catch (BusinessException e) {
            String signName = Objects.nonNull(userDiskDetailDTO) ? userDiskDetailDTO.getDiskName() : diskId.toString();
            LOGGER.error("编辑磁盘[{}]异常", signName, e);
            auditLogAPI.recordLog(DiskPoolBusinessKey.RCDC_RCO_DISK_RELATION_USER_EDIT_FAIL_LOG, e, signName, e.getI18nMessage());
            return CommonWebResponse.fail(DiskPoolBusinessKey.RCDC_RCO_DISK_RELATION_USER_EDIT_FAIL_LOG, new String[] {signName, e.getI18nMessage()});
        }
    }

    /**
     * 解绑用户
     *
     * @param request 请求
     * @param builder 构建器
     * @return 任务信息
     * @throws BusinessException 业务信息
     */
    @ApiOperation("解绑用户")
    @RequestMapping(value = "/unbind/user", method = RequestMethod.POST)
    public CommonWebResponse<Object> batchUnbindUser(DiskArrWebRequest request, BatchTaskBuilder builder) throws BusinessException {
        Assert.notNull(request, "batchUnbindUser request can not be null");
        Assert.notNull(builder, "batchUnbindUser builder request must not be null");

        LOGGER.info("batchUnbindUser 请求参数信息:{}", request.toString());
        UUID[] diskIdArr = request.getDiskIdArr();
        String taskName = LocaleI18nResolver.resolve(DiskPoolBusinessKey.RCDC_RCO_USER_UNBIND_DISK_LOG);
        Iterator<DefaultBatchTaskItem> iterator =
                Arrays.stream(diskIdArr).distinct().map(id -> DefaultBatchTaskItem.builder().itemId(id).itemName(taskName).build()).iterator();

        UnBindDiskBatchTaskHandler handler = new UnBindDiskBatchTaskHandler(iterator, auditLogAPI, userDiskMgmtAPI);
        BatchTaskSubmitResult result;
        if (diskIdArr.length == 1) {
            handler.setDiskName(cbbVDIDeskDiskAPI.getDiskDetail(diskIdArr[0]).getName());
            result = builder.setTaskName(DiskPoolBusinessKey.RCDC_RCO_UNBIND_DISK_NAME)
                    .setTaskDesc(DiskPoolBusinessKey.RCDC_RCO_UNBIND_DISK_TASK_DESC).setUniqueId(diskIdArr[0]).enableParallel()
                    .registerHandler(handler).start();
        } else {
            result = builder.setTaskName(DiskPoolBusinessKey.RCDC_RCO_BATCH_UNBIND_DISK_NAME)
                    .setTaskDesc(DiskPoolBusinessKey.RCDC_RCO_BATCH_UNBIND_DISK_TASK_DESC).setUniqueId(diskIdArr[0]).enableParallel()
                    .registerHandler(handler).start();
        }

        return CommonWebResponse.success(result);
    }

    /**
     * 启用磁盘
     *
     * @param request 请求
     * @param builder 构建器
     * @return 任务信息
     * @throws BusinessException 业务信息
     */
    @ApiOperation("启用磁盘")
    @RequestMapping(value = "/enable", method = RequestMethod.POST)
    public CommonWebResponse<Object> batchEnableDisk(DiskArrWebRequest request, BatchTaskBuilder builder) throws BusinessException {
        Assert.notNull(request, "batchEnableDisk request can not be null");
        Assert.notNull(builder, "batchEnableDisk builder request must not be null");
        LOGGER.info("batchEnableDisk 请求参数信息:{}", request.toString());
        UUID[] diskIdArr = request.getDiskIdArr();
        String taskName = LocaleI18nResolver.resolve(DiskPoolBusinessKey.RCDC_RCO_ENABLE_DISK);
        Iterator<DefaultBatchTaskItem> iterator =
                Arrays.stream(diskIdArr).distinct().map(id -> DefaultBatchTaskItem.builder().itemId(id).itemName(taskName).build()).iterator();

        DiskEnableOrDisableBatchTaskHandler handler =
                new DiskEnableOrDisableBatchTaskHandler(iterator, auditLogAPI, CbbDiskState.ACTIVE).setCbbVDIDeskDiskAPI(cbbVDIDeskDiskAPI);
        BatchTaskSubmitResult result;
        if (diskIdArr.length == 1) {
            handler.setDiskName(cbbVDIDeskDiskAPI.getDiskDetail(diskIdArr[0]).getName());
            result = builder.setTaskName(DiskPoolBusinessKey.RCDC_RCO_ENABLE_DISK).setTaskDesc(DiskPoolBusinessKey.RCDC_RCO_ENABLE_DISK_DESC)
                    .setUniqueId(diskIdArr[0]).enableParallel().registerHandler(handler).start();
        } else {
            result = builder.setTaskName(DiskPoolBusinessKey.RCDC_RCO_BATCH_ENABLE_DISK)
                    .setTaskDesc(DiskPoolBusinessKey.RCDC_RCO_BATCH_ENABLE_DISK_DESC).setUniqueId(diskIdArr[0]).enableParallel()
                    .registerHandler(handler).start();
        }
        return CommonWebResponse.success(result);
    }

    /**
     * 禁用磁盘
     *
     * @param request 请求
     * @param builder 构建器
     * @return 任务信息
     * @throws BusinessException 业务信息
     */
    @ApiOperation("禁用磁盘")
    @RequestMapping(value = "/disable", method = RequestMethod.POST)
    public CommonWebResponse<Object> batchDisableDisk(DiskArrWebRequest request, BatchTaskBuilder builder) throws BusinessException {
        Assert.notNull(request, "batchDisableDisk request can not be null");
        Assert.notNull(builder, "batchDisableDisk builder request must not be null");
        LOGGER.info("batchDisableDisk 请求参数信息:{}", request.toString());
        UUID[] diskIdArr = request.getDiskIdArr();
        String taskName = LocaleI18nResolver.resolve(DiskPoolBusinessKey.RCDC_RCO_DISABLE_DISK);
        Iterator<DefaultBatchTaskItem> iterator =
                Arrays.stream(diskIdArr).distinct().map(id -> DefaultBatchTaskItem.builder().itemId(id).itemName(taskName).build()).iterator();

        DiskEnableOrDisableBatchTaskHandler handler =
                new DiskEnableOrDisableBatchTaskHandler(iterator, auditLogAPI, CbbDiskState.DISABLE).setCbbVDIDeskDiskAPI(cbbVDIDeskDiskAPI);
        BatchTaskSubmitResult result;
        if (diskIdArr.length == 1) {
            handler.setDiskName(cbbVDIDeskDiskAPI.getDiskDetail(diskIdArr[0]).getName());
            result = builder.setTaskName(DiskPoolBusinessKey.RCDC_RCO_DISABLE_DISK).setTaskDesc(DiskPoolBusinessKey.RCDC_RCO_DISABLE_DISK_DESC)
                    .setUniqueId(diskIdArr[0]).enableParallel().registerHandler(handler).start();
        } else {
            result = builder.setTaskName(DiskPoolBusinessKey.RCDC_RCO_BATCH_DISABLE_DISK)
                    .setTaskDesc(DiskPoolBusinessKey.RCDC_RCO_BATCH_DISABLE_DISK_DESC).setUniqueId(diskIdArr[0]).enableParallel()
                    .registerHandler(handler).start();
        }

        return CommonWebResponse.success(result);
    }

    /**
     * 磁盘故障恢复
     *
     * @param request 请求
     * @param builder 构建器
     * @return 任务信息
     * @throws BusinessException 业务异常
     */
    @ApiOperation("磁盘故障恢复")
    @RequestMapping(value = "/fault/recovery", method = RequestMethod.POST)
    public CommonWebResponse<Object> diskFaultRecovery(DiskArrWebRequest request, BatchTaskBuilder builder) throws BusinessException {
        Assert.notNull(request, "diskFaultRecovery request can not be null");
        Assert.notNull(builder, "diskFaultRecovery builder must not be null");
        UUID[] diskIdArr = request.getDiskIdArr();
        String taskName = LocaleI18nResolver.resolve(DiskPoolBusinessKey.RCDC_RCO_DISK_FAULT_RECOVERY);
        Iterator<DefaultBatchTaskItem> iterator =
                Arrays.stream(diskIdArr).distinct().map(id -> DefaultBatchTaskItem.builder().itemId(id).itemName(taskName).build()).iterator();

        DiskFaultRecoveryBatchTaskHandler handler = new DiskFaultRecoveryBatchTaskHandler(iterator, auditLogAPI, cbbVDIDeskDiskAPI);
        BatchTaskSubmitResult result;
        if (diskIdArr.length == 1) {
            handler.setDiskName(cbbVDIDeskDiskAPI.getDiskDetail(diskIdArr[0]).getName());
            result = builder.setTaskName(DiskPoolBusinessKey.RCDC_RCO_DISK_FAULT_RECOVERY)
                    .setTaskDesc(DiskPoolBusinessKey.RCDC_RCO_DISK_FAULT_RECOVERY_DESC).setUniqueId(diskIdArr[0]).enableParallel()
                    .registerHandler(handler).start();
        } else {
            result = builder.setTaskName(DiskPoolBusinessKey.RCDC_RCO_BATCH_DISK_FAULT_RECOVERY)
                    .setTaskDesc(DiskPoolBusinessKey.RCDC_RCO_BATCH_DISK_FAULT_RECOVERY_DESC).setUniqueId(diskIdArr[0]).enableParallel()
                    .registerHandler(handler).start();
        }
        return CommonWebResponse.success(result);
    }

    private List<UUID> obtainExpandDiskList(ExpandDiskWebRequest request) throws BusinessException {
        ExpandObjectTypeEnum type = request.getType();
        UUID diskPoolId = request.getDiskPoolId();
        switch (type) {
            case ALL:
                if (Objects.nonNull(diskPoolId)) {
                    return cbbVDIDeskDiskAPI.listDiskInfoByDiskPoolId(diskPoolId).stream().map(CbbDeskDiskDTO::getId).collect(Collectors.toList());
                }
                break;
            case USERGROUP:
                UUID[] groupIdArr = request.getGroupIdArr();
                if (Objects.nonNull(diskPoolId) && ArrayUtils.isNotEmpty(groupIdArr)) {
                    return userDiskMgmtAPI.diskDetailByDiskPoolIdAndUserIdList(diskPoolId, IacConfigRelatedType.USERGROUP,
                            Arrays.asList(groupIdArr));
                }
                break;
            case USER:
                UUID[] userIdArr = request.getUserIdArr();
                if (Objects.nonNull(diskPoolId) && ArrayUtils.isNotEmpty(userIdArr)) {
                    return userDiskMgmtAPI.diskDetailByDiskPoolIdAndUserIdList(diskPoolId, IacConfigRelatedType.USER, Arrays.asList(userIdArr));
                }
                break;
            case DISK:
                if (ArrayUtils.isNotEmpty(request.getDiskIdArr())) {
                    return Arrays.asList(request.getDiskIdArr());
                }
                break;
            default:
                return Arrays.asList();
        }
        return Arrays.asList();
    }
}
