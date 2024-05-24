package com.ruijie.rcos.rcdc.rco.module.web.ctrl.user;

import java.util.Arrays;
import java.util.Iterator;
import java.util.UUID;
import java.util.stream.Stream;

import com.ruijie.rcos.rcdc.rco.module.web.util.WebBatchTaskUtils;
import com.ruijie.rcos.sk.base.util.StringUtils;
import org.apache.commons.lang3.ArrayUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.RequestMapping;

import com.ruijie.rcos.gss.base.iac.module.annotation.EnableAuthority;
import com.ruijie.rcos.gss.sdk.iac.module.def.dto.user.IacUserDetailDTO;
import com.ruijie.rcos.gss.log.module.def.api.BaseAuditLogAPI;
import com.ruijie.rcos.gss.sdk.iac.module.def.api.IacUserMgmtAPI;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.api.CbbDeskRecycleBinMgmtAPI;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.api.CbbDesktopPoolMgmtAPI;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.api.CbbVDIDeskMgmtAPI;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.dto.CbbDeskDTO;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.dto.CbbRecycleBinConfigEditDTO;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.dto.RecycleBinConfigDetailDTO;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.dto.desktoppool.CbbDesktopPoolDTO;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.enums.ImageUsageTypeEnum;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.enums.RecycleBinConfigStateEnum;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.enums.desktoppool.CbbDesktopPoolModel;
import com.ruijie.rcos.rcdc.rco.module.def.api.DesktopPoolMgmtAPI;
import com.ruijie.rcos.rcdc.rco.module.def.api.UserDesktopMgmtAPI;
import com.ruijie.rcos.rcdc.rco.module.def.api.UserRecycleBinMgmtAPI;
import com.ruijie.rcos.rcdc.rco.module.def.api.dto.CloudDesktopDTO;
import com.ruijie.rcos.rcdc.rco.module.def.api.dto.MatchEqual;
import com.ruijie.rcos.rcdc.rco.module.def.api.request.DeskPageSearchRequest;
import com.ruijie.rcos.rcdc.rco.module.def.api.request.PageSearchRequest;
import com.ruijie.rcos.rcdc.rco.module.def.utils.PermissionHelper;
import com.ruijie.rcos.rcdc.rco.module.web.Constants;
import com.ruijie.rcos.rcdc.rco.module.web.config.annotation.ApiVersion;
import com.ruijie.rcos.rcdc.rco.module.web.config.annotation.ApiVersions;
import com.ruijie.rcos.rcdc.rco.module.web.config.annotation.Version;
import com.ruijie.rcos.rcdc.rco.module.web.ctrl.user.bactchtask.*;
import com.ruijie.rcos.rcdc.rco.module.web.ctrl.user.request.*;
import com.ruijie.rcos.rcdc.rco.module.web.response.CommonWebResponse;
import com.ruijie.rcos.rcdc.rco.module.web.validation.RecycleBinValidation;
import com.ruijie.rcos.sk.base.batch.BatchTaskBuilder;
import com.ruijie.rcos.sk.base.batch.BatchTaskSubmitResult;
import com.ruijie.rcos.sk.base.batch.DefaultBatchTaskItem;
import com.ruijie.rcos.sk.base.exception.BusinessException;
import com.ruijie.rcos.sk.base.log.Logger;
import com.ruijie.rcos.sk.base.log.LoggerFactory;
import com.ruijie.rcos.sk.base.validation.EnableCustomValidate;
import com.ruijie.rcos.sk.modulekit.api.comm.DefaultPageResponse;
import com.ruijie.rcos.sk.webmvc.api.request.PageWebRequest;
import com.ruijie.rcos.sk.webmvc.api.request.WebRequest;
import com.ruijie.rcos.sk.webmvc.api.response.DefaultWebResponse;
import com.ruijie.rcos.sk.webmvc.api.session.SessionContext;
import com.ruijie.rcos.sk.webmvc.api.vo.IdLabelEntry;
import com.ruijie.rcos.sk.webmvc.api.vo.IdLabelEntryUtil;

import io.swagger.annotations.ApiOperation;

/**
 * 
 * Description: Function Description
 * Copyright: Copyright (c) 2018
 * Company: Ruijie Co., Ltd.
 * Create Time: 2019年1月7日
 * 
 * @author artom
 */
@Controller
@RequestMapping("/rco/user/recycleBin")
@EnableCustomValidate(validateClass = RecycleBinValidation.class)
public class RecycleBinControl {

    private static final Logger LOGGER = LoggerFactory.getLogger(RecycleBinControl.class);

    @Autowired
    private UserRecycleBinMgmtAPI recycleBinMgmtAPI;

    @Autowired
    private IacUserMgmtAPI cbbUserAPI;

    @Autowired
    private PermissionHelper permissionHelper;

    @Autowired
    private CbbVDIDeskMgmtAPI cbbVDIDeskMgmtAPI;

    @Autowired
    UserDesktopMgmtAPI userDesktopMgmtAPI;

    @Autowired
    private CbbDeskRecycleBinMgmtAPI cbbDeskRecycleBinMgmtAPI;

    @Autowired
    private CbbDesktopPoolMgmtAPI cbbDesktopPoolMgmtAPI;

    @Autowired
    private DesktopPoolMgmtAPI desktopPoolMgmtAPI;

    @Autowired
    private BaseAuditLogAPI auditLogAPI;


    /**
     * 设置回收站定时清理任务时间
     * 
     * @param request web请求
     * @return 回复结果
     * @throws BusinessException 异常
     */
    @ApiOperation("设置自动清理")
    @ApiVersions({@ApiVersion(value = Version.V1_0_0), @ApiVersion(value = Version.V3_1_1, descriptions = {"分级分权-区分权限，加入权限控制"})})
    @RequestMapping(value = "/config/edit")

    @EnableAuthority
    public DefaultWebResponse config(RecycleBinConfigEditWebRequest request) throws BusinessException {
        Assert.notNull(request, "request must not be null!");

        LOGGER.info("rcv recycleBin/config/edit: {}, {}", request.getEnable(), request.getCycle());
        if (request.getEnable() == RecycleBinConfigStateEnum.OPEN && request.getCycle() == null) {
            LOGGER.error("rcv recycleBin/config/edit: 开启设置回收站定时清理任务但未设置时间");
            throw new BusinessException(UserBusinessKey.RCDC_RCO_RECYCLE_BIN_CONFIG_REQUEST_ERROR);
        }

        CbbRecycleBinConfigEditDTO recycleBinConfigRequest = new CbbRecycleBinConfigEditDTO();
        recycleBinConfigRequest.setEnable(request.getEnable());
        recycleBinConfigRequest.setCycle(request.getCycle());
        cbbDeskRecycleBinMgmtAPI.configCleanTask(recycleBinConfigRequest);

        auditLogAPI.recordLog(UserBusinessKey.RCDC_RCO_RECYCLE_BIN_CONFIG_SUCCESS);
        return DefaultWebResponse.Builder.success(UserBusinessKey.RCDC_RCO_MODULE_OPERATE_SUCCESS, new String[] {});
    }

    /**
     * 获取回收站定时清理任务信息
     * 
     * @param request web请求
     * @return 任务信息
     * @throws BusinessException 异常
     */
    @RequestMapping(value = "/config/detail")
    public DefaultWebResponse detail(RecycleBinConfigDetailWebRequest request) throws BusinessException {
        Assert.notNull(request, "request must not be null!");

        LOGGER.info("rcv recycleBin/config/detail");
        RecycleBinConfigDetailDTO response = cbbDeskRecycleBinMgmtAPI.getCleanTaskInfo();

        return DefaultWebResponse.Builder.success(response);
    }

    /**
     * *查询
     * 
     * @param request 页面请求参数
     * @param sessionContext session信息
     * @throws BusinessException 业务异常
     * @return DataResult
     */
    @RequestMapping(value = "/list")
    public DefaultWebResponse list(PageWebRequest request, SessionContext sessionContext) throws BusinessException {
        Assert.notNull(request, "request is null.");
        Assert.notNull(sessionContext, "sessionContext is not null.");

        PageSearchRequest pageReq = new DeskPageSearchRequest(request);
        pageReq.appendCustomMatchEqual(new MatchEqual("isDelete", new Object[] {Boolean.TRUE}));

        if (!permissionHelper.isAllGroupPermission(sessionContext)) {
            appendDesktopIdMatchEqual(pageReq, sessionContext);
        }

        DefaultPageResponse<CloudDesktopDTO> resp = recycleBinMgmtAPI.pageQuery(pageReq);
        return DefaultWebResponse.Builder.success(resp);
    }

    /**
     * * 删除选中数据
     * 
     * @param request id arrar request
     * @param builder 批处理参数
     * @throws BusinessException 业务异常
     * @return web response
     */
    @ApiOperation("删除")
    @ApiVersions({@ApiVersion(value = Version.V1_0_0), @ApiVersion(value = Version.V3_1_1, descriptions = {"分级分权-区分权限，加入权限控制"})})
    @RequestMapping(value = "/delete")
    @EnableAuthority
    public DefaultWebResponse delete(IdArrWebRequest request, BatchTaskBuilder builder) throws BusinessException {
        Assert.notNull(request, "request must not be null!");
        Assert.notNull(builder, "builder must not be null");

        Boolean shouldOnlyDeleteDataFromDb = request.getShouldOnlyDeleteDataFromDb();
        String prefix = WebBatchTaskUtils.getDeletePrefix(shouldOnlyDeleteDataFromDb);

        UUID[] idArr = request.getIdArr();
        String desktopType = userDesktopMgmtAPI.getImageUsageByDeskId(idArr[0]);
        final Iterator<DefaultBatchTaskItem> iterator = Stream.of(idArr).distinct().map(id -> DefaultBatchTaskItem.builder().itemId(id)
                .itemName(UserBusinessKey.RCDC_RCO_RECYCLEBIN_DELETE_ITEM_NAME, new String[] {desktopType, prefix}).build()).iterator();


        BatchTaskSubmitResult result;
        if (idArr.length == 1) {
            result = deleteSingleRecord(builder, idArr, iterator, shouldOnlyDeleteDataFromDb, prefix);
        } else {
            final DeleteRecycleBinBatchTaskHandler handler =
                    new DeleteRecycleBinBatchTaskHandler(recycleBinMgmtAPI, auditLogAPI, iterator, shouldOnlyDeleteDataFromDb);
            handler.setCbbVDIDeskMgmtAPI(cbbVDIDeskMgmtAPI);
            handler.setDesktopType(desktopType);
            result = builder.setTaskName(UserBusinessKey.RCDC_RCO_RECYCLEBIN_DELETE_BATCH_TASK_NAME, desktopType, prefix)
                    .setTaskDesc(UserBusinessKey.RCDC_RCO_RECYCLEBIN_DELETE_BATCH_TASK_DESC, desktopType, prefix).enableParallel().registerHandler(handler)
                    .start();
        }
        return DefaultWebResponse.Builder.success(result);
    }

    private BatchTaskSubmitResult deleteSingleRecord(BatchTaskBuilder builder, UUID[] idArr, final Iterator<DefaultBatchTaskItem> iterator,
            Boolean shouldOnlyDeleteDataFromDb, String prefix) throws BusinessException {
        String logName = obtainDeskName(idArr[0]);
        final DeleteRecycleBinBatchTaskHandler handler =
                new DeleteRecycleBinBatchTaskHandler(recycleBinMgmtAPI, auditLogAPI, iterator, logName, shouldOnlyDeleteDataFromDb);
        handler.setCbbVDIDeskMgmtAPI(cbbVDIDeskMgmtAPI);
        return builder.setTaskName(UserBusinessKey.RCDC_RCO_RECYCLEBIN_DELETE_SINGLE_TASK_NAME, prefix)
                .setTaskDesc(UserBusinessKey.RCDC_RCO_RECYCLEBIN_DELETE_SINGLE_TASK_DESC, logName, prefix).registerHandler(handler).start();
    }

    /**
     ** 清空回收站
     * 
     * @param request web request
     * @param builder 批处理参数
     * @throws BusinessException 业务异常
     * @return web response
     */
    @RequestMapping(value = "/clear")
    @ApiOperation("清空回收站")
    @ApiVersions({@ApiVersion(value = Version.V1_0_0), @ApiVersion(value = Version.V3_1_1, descriptions = {"分级分权-区分权限，加入权限控制"})})
    @EnableAuthority
    public DefaultWebResponse clear(WebRequest request, BatchTaskBuilder builder) throws BusinessException {
        Assert.notNull(request, "request must not be null!");
        Assert.notNull(builder, "builder must not be null");

        DefaultPageResponse<CloudDesktopDTO> resp = recycleBinMgmtAPI.getAll();
        CloudDesktopDTO[] dtoArr = resp.getItemArr();
        if (dtoArr == null || dtoArr.length == 0) {
            return DefaultWebResponse.Builder.success(UserBusinessKey.RCDC_RCO_MODULE_OPERATE_SUCCESS, new String[] {});
        }
        UUID[] idArr = new UUID[dtoArr.length];
        for (int i = 0; i < dtoArr.length; i++) {
            idArr[i] = dtoArr[i].getId();
        }

        String desktopType = Constants.CLOUD_DESKTOP;
        String finalDesktopType = desktopType;

        final Iterator<DefaultBatchTaskItem> iterator = Stream.of(idArr).distinct().map(id -> DefaultBatchTaskItem.builder().itemId(id)
                .itemName(UserBusinessKey.RCDC_RCO_RECYCLEBIN_DELETE_ITEM_NAME, new String[] {finalDesktopType, StringUtils.EMPTY}).build()).iterator();
        final ClearBatchTaskHandler handler = new ClearBatchTaskHandler(recycleBinMgmtAPI, iterator, auditLogAPI);
        handler.setCbbVDIDeskMgmtAPI(cbbVDIDeskMgmtAPI);
        handler.setImageUsage(ImageUsageTypeEnum.DESK);
        BatchTaskSubmitResult result = builder.setTaskName(UserBusinessKey.RCDC_RCO_RECYCLEBIN_CLEAR_TASK_NAME, finalDesktopType)
                .setTaskDesc(UserBusinessKey.RCDC_RCO_RECYCLEBIN_CLEAR_TASK_DESC, finalDesktopType).enableParallel().registerHandler(handler).start();
        return DefaultWebResponse.Builder.success(result);
    }

    /**
     ** 恢复云桌面
     * 
     * @param request id array web request
     * @param builder 批量任务参数
     * @throws BusinessException 业务异常
     * @return web response
     */
    @ApiOperation("云桌面恢复")
    @ApiVersions({@ApiVersion(value = Version.V1_0_0), @ApiVersion(value = Version.V3_1_1, descriptions = {"分级分权-区分权限，加入权限控制"})})
    @RequestMapping(value = "/recover")

    @EnableAuthority
    public DefaultWebResponse recover(IdArrWebRequest request, BatchTaskBuilder builder) throws BusinessException {
        Assert.notNull(request, "request must not be null!");
        Assert.notNull(builder, "builder must not be null");

        UUID[] idArr = request.getIdArr();
        // 提前校验
        checkBeforeRecoverTask(idArr);
        BatchTaskSubmitResult result = recoverDesk(builder, idArr);
        return DefaultWebResponse.Builder.success(result);
    }

    private void checkBeforeRecoverTask(UUID[] idArr) throws BusinessException {
        // 如果有静态池桌面，检查是否有同一个用户绑定多个该池桌面（这批要恢复的桌面）
        if (ArrayUtils.isEmpty(idArr) || idArr.length == 1) {
            return;
        }
        if (desktopPoolMgmtAPI.existUserBindMoreDesktop(Arrays.asList(idArr), CbbDesktopPoolModel.STATIC)) {
            throw new BusinessException(UserBusinessKey.RCDC_RCO_RECYCLEBIN_RECOVER_EXIST_USER_ONE_MORE_DESK);
        }
    }

    /**
     * 从回收站恢复
     * 
     * @param request 请求参数
     * @param builder 批任务参数
     * @return DefaultWebResponse 响应参数
     * @throws BusinessException 业务异常
     */
    @ApiOperation("从回收站恢复")
    @ApiVersions({@ApiVersion(value = Version.V1_0_0), @ApiVersion(value = Version.V3_1_1, descriptions = {"分级分权-区分权限，加入权限控制"})})
    @RequestMapping(value = "recoverByUser")
    @EnableAuthority
    public DefaultWebResponse recoverByUser(RecoverByUserRequest request, BatchTaskBuilder builder) throws BusinessException {
        Assert.notNull(request, "RecoverByUserRequest must not be null");
        Assert.notNull(builder, "builder must not be null");

        IdLabelEntry[] idLabelArr = request.getRecycleDesktopArr();
        UUID[] idArr = IdLabelEntryUtil.extractIdArr(idLabelArr);
        BatchTaskSubmitResult result = recoverDesk(builder, idArr);
        return DefaultWebResponse.Builder.success(result);
    }

    private BatchTaskSubmitResult recoverDesk(BatchTaskBuilder builder, final UUID[] idArr) throws BusinessException {
        String desktopType = userDesktopMgmtAPI.getImageUsageByDeskId(idArr[0]);
        final Iterator<DefaultBatchTaskItem> iterator = Stream.of(idArr).distinct().map(id -> DefaultBatchTaskItem.builder().itemId(id)
                .itemName(UserBusinessKey.RCDC_RCO_RECYCLEBIN_RECOVER_ITEM_NAME, new String[] {desktopType}).build()).iterator();
        BatchTaskSubmitResult result;
        if (idArr.length == 1) {
            result = recoverSingleRecord(builder, idArr, iterator, desktopType);
        } else {
            result = batchRecoverTaskSubmit(builder, iterator, desktopType);
        }
        return result;
    }

    private BatchTaskSubmitResult recoverSingleRecord(BatchTaskBuilder builder, UUID[] idArr, final Iterator<DefaultBatchTaskItem> iterator,
            String desktopType) throws BusinessException {
        String logName = obtainDeskName(idArr[0]);
        final RecoverRecycleBinBatchTaskHandler handler = new RecoverRecycleBinBatchTaskHandler(recycleBinMgmtAPI, iterator, auditLogAPI, logName);
        handler.setCbbVDIDeskMgmtAPI(cbbVDIDeskMgmtAPI);
        handler.setDesktopType(desktopType);
        return builder.setTaskName(UserBusinessKey.RCDC_RCO_RECYCLEBIN_RECOVER_SINGLE_TASK_NAME, desktopType)
                .setTaskDesc(UserBusinessKey.RCDC_RCO_RECYCLEBIN_RECOVER_SINGLE_TASK_DESC, logName, desktopType).registerHandler(handler).start();
    }

    private BatchTaskSubmitResult batchRecoverTaskSubmit(BatchTaskBuilder builder, Iterator<DefaultBatchTaskItem> iterator, String desktopType)
            throws BusinessException {
        final RecoverRecycleBinBatchTaskHandler handler = new RecoverRecycleBinBatchTaskHandler(recycleBinMgmtAPI, iterator, auditLogAPI);
        handler.setCbbVDIDeskMgmtAPI(cbbVDIDeskMgmtAPI);
        handler.setDesktopType(desktopType);
        return builder.setTaskName(UserBusinessKey.RCDC_RCO_RECYCLEBIN_RECOVER_BATCH_TASK_NAME, desktopType)
                .setTaskDesc(UserBusinessKey.RCDC_RCO_RECYCLEBIN_RECOVER_BATCH_TASK_DESC, desktopType).enableParallel().registerHandler(handler)
                .start();
    }

    /**
     * 指定用户恢复回收站云桌面
     * 
     * @param request 请求参数
     * @param builder 批任务参数
     * @return DefaultWebResponse 响应参数
     * @throws BusinessException 业务异常
     */
    @ApiOperation("指定用户恢复云桌面")
    @ApiVersions({@ApiVersion(value = Version.V1_0_0), @ApiVersion(value = Version.V3_1_1, descriptions = {"分级分权-区分权限，加入权限控制"})})
    @RequestMapping(value = "recoverByAssignUser")

    @EnableAuthority
    public DefaultWebResponse recoverByAssignUser(RecoverByAssignUserWebRequest request, BatchTaskBuilder builder) throws BusinessException {
        Assert.notNull(request, "RecoverByUserRequest must not be null");
        Assert.notNull(builder, "builder must not be null");

        UUID[] idArr = request.getIdArr();
        IdLabelEntry userInfo = request.getUserInfo();
        // 仅支持单个桌面指定用户恢复操作
        Assert.state(idArr.length == 1, "RecoverDeskByAssignUser request param has error, idArr length is :" + idArr.length);

        IacUserDetailDTO userDetail = cbbUserAPI.getUserDetail(userInfo.getId());
        BatchTaskSubmitResult result = recoverSingleRecordByAssignUser(builder, request.getIdArr(), userDetail);
        return DefaultWebResponse.Builder.success(result);
    }

    private BatchTaskSubmitResult recoverSingleRecordByAssignUser(BatchTaskBuilder builder, UUID[] idArr, IacUserDetailDTO userDetail)
            throws BusinessException {
        String desktopType = userDesktopMgmtAPI.getImageUsageByDeskId(idArr[0]);
        final Iterator<DefaultBatchTaskItem> iterator =
                Stream.of(idArr).distinct()
                        .map(id -> DefaultBatchTaskItem.builder().itemId(id)
                                .itemName(UserBusinessKey.RCDC_RCO_RECYCLEBIN_RECOVER_ASSIGN_USER_ITEM_NAME, new String[] {desktopType}).build())
                        .iterator();

        String deskName = obtainDeskName(idArr[0]);
        final RecoverRecycleBinByAssignUserBatchTaskHandler handler =
                new RecoverRecycleBinByAssignUserBatchTaskHandler(recycleBinMgmtAPI, iterator, auditLogAPI, deskName)
                        .setAssignUserId(userDetail.getId()).setAssignUserName(userDetail.getUserName());
        handler.setCbbVDIDeskMgmtAPI(cbbVDIDeskMgmtAPI);
        handler.setDesktopType(desktopType);
        return builder.setTaskName(UserBusinessKey.RCDC_RCO_RECYCLEBIN_RECOVER_ASSIGN_USER_SINGLE_TASK_NAME, desktopType)
                .setTaskDesc(UserBusinessKey.RCDC_RCO_RECYCLEBIN_RECOVER_ASSIGN_USER_SINGLE_TASK_DESC, userDetail.getUserName(), deskName,
                        desktopType)
                .registerHandler(handler).start();
    }

    private void appendDesktopIdMatchEqual(PageSearchRequest request, SessionContext sessionContext) throws BusinessException {
        UUID[] uuidArr = permissionHelper.getDesktopIdArr(sessionContext);
        request.appendCustomMatchEqual(new MatchEqual("cbbDesktopId", uuidArr));
    }

    /**
     * 指定桌面池恢复回收站云桌面
     *
     * @param request 请求参数
     * @param builder 批任务参数
     * @return DefaultWebResponse 响应参数
     * @throws BusinessException 业务异常
     */
    @ApiOperation("指定桌面池恢复云桌面")
    @ApiVersions({@ApiVersion(value = Version.V3_2_221), @ApiVersion(value = Version.V3_2_221, descriptions = {"指定桌面池恢复云桌面"})})
    @RequestMapping(value = "recoverByAssignDesktopPool")
    @EnableAuthority
    public CommonWebResponse<BatchTaskSubmitResult> recoverByAssignDesktopPool(RecoverByAssignDesktopPoolWebRequest request, BatchTaskBuilder builder)
            throws BusinessException {
        Assert.notNull(request, "request must not be null");
        Assert.notNull(builder, "builder must not be null");

        UUID[] idArr = request.getIdArr();
        // 仅支持单个桌面指定桌面池恢复操作
        Assert.state(idArr.length == 1, "指定桌面池恢复云桌面只支持单个桌面操作");

        BatchTaskSubmitResult result = recoverSingleRecordByAssignDesktopPool(builder, request);
        return CommonWebResponse.success(result);
    }

    private BatchTaskSubmitResult recoverSingleRecordByAssignDesktopPool(BatchTaskBuilder builder, RecoverByAssignDesktopPoolWebRequest request)
            throws BusinessException {

        CbbDesktopPoolDTO cbbDesktopPoolDTO = cbbDesktopPoolMgmtAPI.getDesktopPoolDetail(request.getDesktopPoolId());

        final Iterator<DefaultBatchTaskItem> iterator =
                Stream.of(request.getIdArr()).distinct()
                        .map(id -> DefaultBatchTaskItem.builder().itemId(id)
                                .itemName(UserBusinessKey.RCDC_RCO_RECYCLEBIN_RECOVER_ASSIGN_DESKTOP_POOL_ITEM_NAME, new String[] {}).build())
                        .iterator();

        String deskName = obtainDeskName(request.getIdArr()[0]);
        final RecoverRecycleBinByAssignDesktopPoolBatchTaskHandler handler =
                new RecoverRecycleBinByAssignDesktopPoolBatchTaskHandler(iterator, cbbDesktopPoolDTO);
        return builder.setTaskName(UserBusinessKey.RCDC_RCO_RECYCLEBIN_RECOVER_ASSIGN_DESKTOP_POOL_ITEM_NAME)
                .setTaskDesc(UserBusinessKey.RCDC_RCO_RECYCLEBIN_RECOVER_ASSIGN_DESKTOP_POOL_TASK_DESC, cbbDesktopPoolDTO.getName(), deskName)
                .registerHandler(handler).start();
    }

    private String obtainDeskName(UUID deskId) {
        String resultName = deskId.toString();
        try {
            CbbDeskDTO deskDTO = cbbVDIDeskMgmtAPI.getDeskVDI(deskId);
            resultName = deskDTO.getName();
        } catch (BusinessException e) {
            LOGGER.error("获取云桌面数据失败，桌面id[{}]", deskId, e);
        }
        return resultName;
    }
}
