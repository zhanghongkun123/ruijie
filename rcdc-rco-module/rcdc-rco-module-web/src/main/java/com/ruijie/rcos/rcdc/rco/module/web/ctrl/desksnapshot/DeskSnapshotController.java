package com.ruijie.rcos.rcdc.rco.module.web.ctrl.desksnapshot;

import com.ruijie.rcos.gss.base.iac.module.annotation.EnableAuthority;
import com.ruijie.rcos.gss.log.module.def.api.BaseAuditLogAPI;
import com.ruijie.rcos.gss.sdk.iac.module.def.api.IacAdminMgmtAPI;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.api.CbbVDIDeskSnapshotAPI;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.dto.CbbDeskSnapshotDTO;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.dto.desk.CbbDeskSnapshotDistributeDTO;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.enums.CbbDeskSnapshotState;
import com.ruijie.rcos.rcdc.rco.module.def.api.DeskSnapshotAPI;
import com.ruijie.rcos.rcdc.rco.module.def.api.DesktopAPI;
import com.ruijie.rcos.rcdc.rco.module.def.api.SnapshotManageAPI;
import com.ruijie.rcos.rcdc.rco.module.def.api.UserDesktopMgmtAPI;
import com.ruijie.rcos.rcdc.rco.module.def.api.dto.CloudDesktopDetailDTO;
import com.ruijie.rcos.rcdc.rco.module.def.api.response.MaxSnapshotsRangeResponse;
import com.ruijie.rcos.rcdc.rco.module.web.config.annotation.ApiVersion;
import com.ruijie.rcos.rcdc.rco.module.web.config.annotation.ApiVersions;
import com.ruijie.rcos.rcdc.rco.module.web.config.annotation.Version;
import com.ruijie.rcos.rcdc.rco.module.web.ctrl.desksnapshot.batchtask.CreateDeskSnapshotBatchTaskHandler;
import com.ruijie.rcos.rcdc.rco.module.web.ctrl.desksnapshot.batchtask.DeleteDeskSnapshotBatchTaskHandler;
import com.ruijie.rcos.rcdc.rco.module.web.ctrl.desksnapshot.batchtask.RecoverDeskSnapshotBatchTaskHandler;
import com.ruijie.rcos.rcdc.rco.module.web.ctrl.desksnapshot.request.*;
import com.ruijie.rcos.rcdc.rco.module.web.ctrl.desksnapshot.response.*;
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
import com.ruijie.rcos.sk.pagekit.api.PageQueryBuilderFactory;
import com.ruijie.rcos.sk.pagekit.api.PageQueryBuilderFactory.RequestBuilder;
import com.ruijie.rcos.sk.pagekit.api.PageQueryRequest;
import com.ruijie.rcos.sk.pagekit.api.PageQueryResponse;
import com.ruijie.rcos.sk.webmvc.api.request.IdArrWebRequest;
import com.ruijie.rcos.sk.webmvc.api.request.IdWebRequest;
import com.ruijie.rcos.sk.webmvc.api.request.PageWebRequest;
import com.ruijie.rcos.sk.webmvc.api.session.SessionContext;
import com.ruijie.rcos.sk.webmvc.api.vo.ExactMatch;
import com.ruijie.rcos.sk.webmvc.api.vo.Sort;
import com.ruijie.rcos.sk.webmvc.api.vo.Sort.Direction;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.lang3.ArrayUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.util.Iterator;
import java.util.UUID;
import java.util.stream.Stream;

/**
 * Description: Function Description
 * Copyright: Copyright (c) 2018
 * Company: Ruijie Co., Ltd.
 * Create Time: 2020年10月25日
 *
 * @author luojianmo
 */
@Api(tags = "云桌面快照")
@Controller
@RequestMapping("/rco/clouddesktop/deskSnapshot")
public class DeskSnapshotController {

    private static final Logger LOGGER = LoggerFactory.getLogger(DeskSnapshotController.class);

    @Autowired
    private CbbVDIDeskSnapshotAPI cbbVDIDeskSnapshotAPI;

    @Autowired
    private SnapshotManageAPI snapshotManageAPI;

    @Autowired
    private PageQueryBuilderFactory pageQueryBuilderFactory;

    @Autowired
    private DeskSnapshotAPI deskSnapshotAPI;

    @Autowired
    private UserDesktopMgmtAPI userDesktopMgmtAPI;

    @Autowired
    private StateMachineFactory stateMachineFactory;

    @Autowired
    private BaseAuditLogAPI auditLogAPI;

    @Autowired
    private IacAdminMgmtAPI baseAdminMgmtAPI;

    @Autowired
    private DesktopAPI desktopAPI;

    @Autowired
    private CloudDesktopWebService cloudDesktopWebService;

    private static final String[] SUPPORT_SORT_FIELD_ARR = new String[]{"name"};

    private static final String SORT_FIELD_CREATE_TIME = "createTime";


    /**
     * 获取云桌面快照列表
     *
     * @param webRequest     入参
     * @param sessionContext session上下文
     * @return 响应
     * @throws BusinessException 业务异常
     */
    @ApiOperation("获取云桌面快照列表")
    @RequestMapping(value = "list", method = RequestMethod.POST)
    public CommonWebResponse<DeskSnapshotPageQueryResponse<CbbDeskSnapshotDTO>> list(PageWebRequest webRequest,
                                                                                     SessionContext sessionContext) throws BusinessException {
        Assert.notNull(webRequest, "webRequest must not be null");
        Assert.notNull(sessionContext, "sessionContext must not be null");

        UUID userId = sessionContext.getUserId();

        try {
            baseAdminMgmtAPI.getAdmin(userId);
        } catch (Exception e) {
            ExactMatch a = new ExactMatch();
            a.setName("userId");
            a.setValueArr(new String[]{userId.toString()});
            ExactMatch[] extraMatchArr = new ExactMatch[1];
            extraMatchArr[0] = a;
            webRequest.setExactMatchArr(extraMatchArr);
        }

        PageQueryRequest pageQueryRequest = generatePageQueryBuilder(webRequest).build();
        PageQueryResponse<CbbDeskSnapshotDTO> pageQueryResponse = deskSnapshotAPI.pageQuery(pageQueryRequest);
        DeskSnapshotPageQueryResponse<CbbDeskSnapshotDTO> response = new DeskSnapshotPageQueryResponse();
        CbbDeskSnapshotDTO[] deskSnapshotArr = pageQueryResponse.getItemArr();
        response.setItemArr(deskSnapshotArr);
        response.setTotal(pageQueryResponse.getTotal());
        if (deskSnapshotArr != null && deskSnapshotArr.length > 0) {
            CloudDesktopDetailDTO cloudDesktopDetailDTO = userDesktopMgmtAPI.getDesktopDetailById(deskSnapshotArr[0].getDeskId());
            response.setDesktopState(cloudDesktopDetailDTO.getDesktopState());
        }
        return CommonWebResponse.success(response);
    }


    /**
     * 创建云桌面快照
     *
     * @param createDeskSnapshotRequest 入参
     * @param sessionContext            session上下文
     * @param builder                   批量任务创建对象
     * @return 响应
     * @throws BusinessException 业务异常
     */
    @ApiOperation("创建云桌面快照")
    @ApiVersions({@ApiVersion(value = Version.V1_0_0), @ApiVersion(value = Version.V3_1_1, descriptions = {"分级分权-区分权限，加入权限控制"})})
    @RequestMapping(value = "create", method = RequestMethod.POST)
    @EnableAuthority
    public CommonWebResponse create(CreateDeskSnapshotRequest createDeskSnapshotRequest,
                                    SessionContext sessionContext, BatchTaskBuilder builder) throws BusinessException {
        Assert.notNull(createDeskSnapshotRequest, "createDeskSnapshotRequest must not be null");
        Assert.notNull(sessionContext, "sessionContext is not null");
        Assert.notNull(builder, "builder must not be null");

        UUID[] idArr = new UUID[]{createDeskSnapshotRequest.getDeskId()};
        String desktopType = userDesktopMgmtAPI.getImageUsageByDeskId(createDeskSnapshotRequest.getDeskId());

        final Iterator<DefaultBatchTaskItem> iterator = Stream.of(idArr).distinct().map(id -> DefaultBatchTaskItem.builder()//
                        .itemId(id).itemName(DeskSnapshotBusinessKey.RCDC_DESK_SNAPSHOT_CREATE_ITEM_NAME, new String[]{desktopType}).build())//
                .iterator();

        CreateDeskSnapshotBatchTaskHandler handler = new CreateDeskSnapshotBatchTaskHandler(snapshotManageAPI, cbbVDIDeskSnapshotAPI,
                userDesktopMgmtAPI, iterator, auditLogAPI);
        handler.setCloudDesktopWebService(cloudDesktopWebService);
        handler.setDeskSnapshotAPI(deskSnapshotAPI);
        handler.setStateMachineFactory(stateMachineFactory);
        handler.setSessionContext(sessionContext);
        handler.setBaseAdminMgmtAPI(baseAdminMgmtAPI);
        handler.setSnapshotName(createDeskSnapshotRequest.getName());
        handler.setDesktopType(desktopType);
        BatchTaskSubmitResult result = startCreateBatchTask(idArr, handler, builder, desktopType);
        return CommonWebResponse.success(result);

    }

    private BatchTaskSubmitResult startCreateBatchTask(UUID[] idArr, CreateDeskSnapshotBatchTaskHandler handler, BatchTaskBuilder builder
            , String desktopType) throws BusinessException {
        // 创建单条用户
        BatchTaskSubmitResult result;
        if (idArr.length == 1) {
            handler.setIsBatch(false);
            result = builder.setTaskName(DeskSnapshotBusinessKey.RCDC_DESK_SNAPSHOT_CREATE_SINGLE_TASK_NAME, desktopType)
                    .setTaskDesc(DeskSnapshotBusinessKey.RCDC_DESK_SNAPSHOT_CREATE_SINGLE_TASK_DESC, handler.getSnapshotName(), desktopType)
                    .enableParallel().registerHandler(handler).start();
        } else {
            result = builder.setTaskName(DeskSnapshotBusinessKey.RCDC_DESK_SNAPSHOT_CREATE_TASK_NAME, desktopType)
                    .setTaskDesc(DeskSnapshotBusinessKey.RCDC_DESK_SNAPSHOT_CREATE_TASK_DESC, desktopType).enableParallel()
                    .registerHandler(handler).start();
        }
        return result;
    }


    /**
     * 云桌面快照恢复
     *
     * @param idRequest 入参
     * @param builder   批量任务创建对象
     * @return 响应
     * @throws BusinessException 业务异常
     */
    @ApiOperation("云桌面快照恢复")
    @ApiVersions({@ApiVersion(value = Version.V1_0_0), @ApiVersion(value = Version.V3_1_1, descriptions = {"分级分权-区分权限，加入权限控制"})})
    @RequestMapping(value = "recover", method = RequestMethod.POST)
    @EnableAuthority
    public CommonWebResponse recover(IdWebRequest idRequest, BatchTaskBuilder builder) throws BusinessException {
        Assert.notNull(idRequest, "idRequest must not be null");
        Assert.notNull(builder, "builder must not be null");

        UUID[] idArr = new UUID[]{idRequest.getId()};
        CbbDeskSnapshotDTO cbbDeskSnapshotDTO = cbbVDIDeskSnapshotAPI.findDeskSnapshotInfoById(idRequest.getId());
        String desktopType = userDesktopMgmtAPI.getImageUsageByDeskId(cbbDeskSnapshotDTO.getDeskId());

        final Iterator<DefaultBatchTaskItem> iterator = Stream.of(idArr).distinct().map(id -> DefaultBatchTaskItem.builder()//
                        .itemId(id).itemName(DeskSnapshotBusinessKey.RCDC_DESK_SNAPSHOT_RECOVER_ITEM_NAME, new String[]{desktopType}).build())//
                .iterator();

        RecoverDeskSnapshotBatchTaskHandler handler = new RecoverDeskSnapshotBatchTaskHandler(snapshotManageAPI, cbbVDIDeskSnapshotAPI,
                userDesktopMgmtAPI, iterator, auditLogAPI);
        handler.setStateMachineFactory(stateMachineFactory);
        handler.setDesktopType(desktopType);
        BatchTaskSubmitResult result = startRecoverBatchTask(idArr, handler, builder, desktopType);
        return CommonWebResponse.success(result);
    }

    private BatchTaskSubmitResult startRecoverBatchTask(UUID[] idArr, RecoverDeskSnapshotBatchTaskHandler handler, BatchTaskBuilder builder
            , String desktopType) throws BusinessException {
        BatchTaskSubmitResult result;
        if (idArr.length == 1) {
            handler.setIsBatch(false);
            CbbDeskSnapshotDTO cbbDeskSnapshotDTO = findDeskSnapshotInfoById(idArr[0]);
            result = builder.setTaskName(DeskSnapshotBusinessKey.RCDC_DESK_SNAPSHOT_RECOVER_SINGLE_TASK_NAME, desktopType)
                    .setTaskDesc(DeskSnapshotBusinessKey.RCDC_DESK_SNAPSHOT_RECOVER_SINGLE_TASK_DESC,
                            cbbDeskSnapshotDTO.getName(), desktopType)
                    .enableParallel().registerHandler(handler).start();
        } else {
            result = builder.setTaskName(DeskSnapshotBusinessKey.RCDC_DESK_SNAPSHOT_RECOVER_TASK_NAME, desktopType)
                    .setTaskDesc(DeskSnapshotBusinessKey.RCDC_DESK_SNAPSHOT_RECOVER_TASK_DESC, desktopType).enableParallel()
                    .registerHandler(handler).start();
        }
        return result;
    }

    /**
     * 删除云桌面快照
     *
     * @param idArrWebRequest 入参
     * @param builder         批量任务创建对象
     * @return 响应
     * @throws BusinessException 业务异常
     */
    @ApiOperation("删除云桌面快照")
    @ApiVersions({@ApiVersion(value = Version.V1_0_0), @ApiVersion(value = Version.V3_1_1, descriptions = {"分级分权-区分权限，加入权限控制"})})
    @RequestMapping(value = "delete", method = RequestMethod.POST)
    @EnableAuthority
    public CommonWebResponse<BatchTaskSubmitResult> delete(IdArrWebRequest idArrWebRequest, BatchTaskBuilder builder) throws BusinessException {
        Assert.notNull(idArrWebRequest, "idArrWebRequest must not be null");
        Assert.notNull(builder, "builder can not be null");

        UUID[] idArr = idArrWebRequest.getIdArr();

        CbbDeskSnapshotDTO cbbDeskSnapshotDTO = cbbVDIDeskSnapshotAPI.findDeskSnapshotInfoById(idArr[0]);
        String desktopType = userDesktopMgmtAPI.getImageUsageByDeskId(cbbDeskSnapshotDTO.getDeskId());
        // 批量删除任务
        final Iterator<DefaultBatchTaskItem> iterator = Stream.of(idArr).distinct().map(id -> DefaultBatchTaskItem.builder().itemId(id)
                .itemName(DeskSnapshotBusinessKey.RCDC_DESK_SNAPSHOT_DELETE_ITEM_NAME, new String[]{desktopType}).build()).iterator();
        DeleteDeskSnapshotBatchTaskHandler handler = new DeleteDeskSnapshotBatchTaskHandler(snapshotManageAPI, iterator);
        handler.setStateMachineFactory(stateMachineFactory);
        handler.setDesktopType(desktopType);
        BatchTaskSubmitResult result = startDeleteBatchTask(idArr, handler, builder, desktopType);
        return CommonWebResponse.success(result);
    }

    private BatchTaskSubmitResult startDeleteBatchTask(UUID[] idArr, DeleteDeskSnapshotBatchTaskHandler handler, BatchTaskBuilder builder
            , String desktopType) throws BusinessException {
        // 删除单条用户
        BatchTaskSubmitResult result;
        if (idArr.length == 1) {
            handler.setIsBatch(false);
            CbbDeskSnapshotDTO deskSnapshotDTO = cbbVDIDeskSnapshotAPI.findDeskSnapshotInfoById(idArr[0]);
            result = builder.setTaskName(DeskSnapshotBusinessKey.RCDC_DESK_SNAPSHOT_DELETE_SINGLE_TASK_NAME, desktopType)
                    .setTaskDesc(DeskSnapshotBusinessKey.RCDC_DESK_SNAPSHOT_DELETE_SINGLE_TASK_DESC,
                            deskSnapshotDTO.getName(), desktopType)
                    .enableParallel().registerHandler(handler).start();
        } else {
            result = builder.setTaskName(DeskSnapshotBusinessKey.RCDC_DESK_SNAPSHOT_DELETE_TASK_NAME, desktopType)
                    .setTaskDesc(DeskSnapshotBusinessKey.RCDC_DESK_SNAPSHOT_DELETE_TASK_DESC, desktopType).enableParallel()
                    .registerHandler(handler).start();
        }
        return result;
    }

    /**
     * 获取云桌面快照详情
     *
     * @param idWebRequest 入参
     * @return 响应
     * @throws BusinessException 业务异常
     */
    @ApiOperation("获取云桌面快照详情")
    @RequestMapping(value = "detail", method = RequestMethod.POST)
    public CommonWebResponse<CbbDeskSnapshotDTO> detail(IdWebRequest idWebRequest) throws BusinessException {
        Assert.notNull(idWebRequest, "idWebRequest must not be null");
        CbbDeskSnapshotDTO deskSnapshotDTO = cbbVDIDeskSnapshotAPI.findDeskSnapshotInfoById(idWebRequest.getId());
        return CommonWebResponse.success(deskSnapshotDTO);
    }

    /**
     * 根据云桌面ID获取云桌面快照默认名称
     *
     * @param request 入参
     * @return 响应
     * @throws BusinessException 业务异常
     */
    @ApiOperation("获取云桌面快照默认名称")
    @RequestMapping(value = "findDefaultSnapshotName", method = RequestMethod.POST)
    public CommonWebResponse<FindDefaultSnapshotNameResponse> findDefaultSnapshotName(FindDefaultSnapshotNameRequest request)
            throws BusinessException {
        Assert.notNull(request, "request must not be null");
        CloudDesktopDetailDTO cloudDesktopDetailDTO = userDesktopMgmtAPI.getDesktopDetailById(request.getDeskId());
        String snapshotName = deskSnapshotAPI.generateSnapshotNameByDesktopName(cloudDesktopDetailDTO.getDesktopName());
        FindDefaultSnapshotNameResponse response = new FindDefaultSnapshotNameResponse(snapshotName);
        return CommonWebResponse.success(response);
    }

    /**
     * 检测云桌面快照数量是否满配
     *
     * @param idWebRequest   入参
     * @param sessionContext session上下文
     * @return 响应
     * @throws BusinessException 业务异常
     */
    @ApiOperation("检测云桌面快照数量是否满配")
    @RequestMapping(value = "checkSnapshotFull", method = RequestMethod.POST)
    public CommonWebResponse<CheckSnapshotFullResponse> checkSnapshotFull(IdWebRequest idWebRequest,
                                                                          SessionContext sessionContext) throws BusinessException {
        Assert.notNull(idWebRequest, "idWebRequest must not be null");
        Assert.notNull(sessionContext, "sessionContext must not be null");

        UUID deskId = idWebRequest.getId();

        desktopAPI.throwExceptionWhenNotExist(deskId);

        CbbDeskSnapshotDistributeDTO cbbDeskSnapshotDistributeDTO = cbbVDIDeskSnapshotAPI.getCurrentSnapshotSituation(deskId);
        boolean hasFull = cbbDeskSnapshotDistributeDTO.getCapacity() <= cbbDeskSnapshotDistributeDTO.getTotal();
        boolean hasUserSnapshotFull = cbbDeskSnapshotDistributeDTO.getCapacity() <= cbbDeskSnapshotDistributeDTO.getUserSnapshotCount();
        CheckSnapshotFullResponse response = new CheckSnapshotFullResponse(hasFull, hasUserSnapshotFull);
        return CommonWebResponse.success(response);
    }

    /**
     * 获取云桌面快照数量最大限制数
     *
     * @return 响应
     * @throws BusinessException 业务异常
     */
    @ApiOperation("获取云桌面快照数量最大限制数")
    @RequestMapping(value = "getMaxSnapshots", method = RequestMethod.POST)
    public CommonWebResponse<GetMaxSnapshotsResponse> getMaxSnapshots() {
        Integer maxSnapshots = deskSnapshotAPI.getMaxSnapshots();
        MaxSnapshotsRangeResponse allowMaxSnapshotsRange = deskSnapshotAPI.getMaxSnapshotsRange();
        GetMaxSnapshotsResponse response =
                new GetMaxSnapshotsResponse(maxSnapshots, allowMaxSnapshotsRange.getMinSnapshot(), allowMaxSnapshotsRange.getMaxSnapshot());
        return CommonWebResponse.success(response);
    }

    /**
     * 编辑云桌面快照数量最大限制数
     *
     * @param updateMaxSnapshotsRequest 入参
     * @return 响应
     * @throws BusinessException 业务异常
     */
    @ApiOperation("编辑云桌面快照数量最大限制数")
    @RequestMapping(value = "editMaxSnapshots", method = RequestMethod.POST)
    public CommonWebResponse editMaxSnapshots(UpdateMaxSnapshotsRequest updateMaxSnapshotsRequest) {
        Assert.notNull(updateMaxSnapshotsRequest, "updateMaxSnapshotsRequest must not be null");
        MaxSnapshotsRangeResponse maxSnapshotsRange = deskSnapshotAPI.getMaxSnapshotsRange();

        //扩大 ，不能超过最大
        if (updateMaxSnapshotsRequest.getMaxSnapshots() > maxSnapshotsRange.getMaxSnapshot()) {
            auditLogAPI.recordLog(DeskSnapshotBusinessKey.RCDC_DESK_EDIT_MAX_SNAPSHOT_REQUEST_TIP,
                    String.valueOf(maxSnapshotsRange.getMinSnapshot()), String.valueOf(maxSnapshotsRange.getMaxSnapshot()));
            return CommonWebResponse.fail(DeskSnapshotBusinessKey.RCDC_DESK_EDIT_MAX_SNAPSHOT_REQUEST_TIP,
                    new String[]{String.valueOf(maxSnapshotsRange.getMinSnapshot()), String.valueOf(maxSnapshotsRange.getMaxSnapshot())});
        }
        //缩小，不能小于所有云桌面策略中用户快照数量最大值，防止缩容导致 用户与管理员快照数量无法重新计算
        //解决此问题：全局快照总数量原来20 个，用户配置10个， 使用一段时间后 管理员把总数量改成9个，那用户与管理员数量怎么处理？
        if (updateMaxSnapshotsRequest.getMaxSnapshots() < maxSnapshotsRange.getMinSnapshot()) {
            auditLogAPI.recordLog(DeskSnapshotBusinessKey.RCDC_DESK_EDIT_MAX_SNAPSHOT_REQUEST_TIP,
                    String.valueOf(maxSnapshotsRange.getMinSnapshot()), String.valueOf(maxSnapshotsRange.getMaxSnapshot()));
            return CommonWebResponse.fail(DeskSnapshotBusinessKey.RCDC_DESK_EDIT_MAX_SNAPSHOT_REQUEST_TIP,
                    new String[]{String.valueOf(maxSnapshotsRange.getMinSnapshot()), String.valueOf(maxSnapshotsRange.getMaxSnapshot())});
        }

        Integer maxSnapshots = updateMaxSnapshotsRequest.getMaxSnapshots();
        deskSnapshotAPI.editMaxSnapshots(maxSnapshots);
        auditLogAPI.recordLog(DeskSnapshotBusinessKey.RCDC_DESK_EDIT_MAX_SNAPSHOT_SUCCESS, String.valueOf(maxSnapshots));
        return CommonWebResponse.success(DeskSnapshotBusinessKey.RCDC_DESK_EDIT_MAX_SNAPSHOT_SUCCESS, new String[]{String.valueOf(maxSnapshots)});
    }

    /**
     * 编辑云桌面快照
     *
     * @param editDeskSnapshotRequest 入参
     * @return 响应
     * @throws BusinessException 业务异常
     */
    @ApiOperation("编辑云桌面快照")
    @ApiVersions({@ApiVersion(value = Version.V1_0_0), @ApiVersion(value = Version.V3_1_1, descriptions = {"分级分权-区分权限，加入权限控制"})})
    @RequestMapping(value = "edit", method = RequestMethod.POST)
    @EnableAuthority
    public CommonWebResponse edit(EditDeskSnapshotRequest editDeskSnapshotRequest) throws BusinessException {
        Assert.notNull(editDeskSnapshotRequest, "editDeskSnapshotRequest must not be null");
        UUID deskSnapshotId = editDeskSnapshotRequest.getId();

        try {
            CbbDeskSnapshotDTO cbbDeskSnapshotDTO = findDeskSnapshotInfoById(deskSnapshotId);
            String desktopType = userDesktopMgmtAPI.getImageUsageByDeskId(cbbDeskSnapshotDTO.getDeskId());

            if (cbbDeskSnapshotDTO.getState() == CbbDeskSnapshotState.DELETING) {
                LOGGER.error("云桌面快照[{}]修改异常。失败原因：云桌面处于删除中，不支持修改。", editDeskSnapshotRequest.getName());
                throw new BusinessException(DeskSnapshotBusinessKey.RCDC_DESK_SNAPSHOT_EDIT_BY_DELETING,
                        cbbDeskSnapshotDTO.getName(), desktopType);
            }

            if (cbbDeskSnapshotDTO.getName().equals(editDeskSnapshotRequest.getName())) {
                LOGGER.info("云桌面快照[{}]名称未发送改变。", editDeskSnapshotRequest.getName());
            } else {
                cbbVDIDeskSnapshotAPI.updateDeskSnapshotName(editDeskSnapshotRequest.getId(), editDeskSnapshotRequest.getName());
            }
            auditLogAPI.recordLog(DeskSnapshotBusinessKey.RCDC_DESK_SNAPSHOT_OPERATE_EDIT_SUCCESS, desktopType, editDeskSnapshotRequest.getName());
            return CommonWebResponse.success(DeskSnapshotBusinessKey.RCDC_DESK_SNAPSHOT_OPERATE_SUCCESS, new String[]{});
        } catch (BusinessException e) {
            auditLogAPI.recordLog(DeskSnapshotBusinessKey.RCDC_DESK_SNAPSHOT_OPERATE_EDIT_FAIL,
                    editDeskSnapshotRequest.getName(), e.getI18nMessage());
            return CommonWebResponse.fail(DeskSnapshotBusinessKey.RCDC_DESK_SNAPSHOT_OPERATE_FAIL, new String[]{e.getI18nMessage()});
        }
    }

    /**
     * 检查云桌面快照名称是否重复
     *
     * @param checkNameDuplicationRequest 入参
     * @return 响应
     * @throws BusinessException 业务异常
     */
    @ApiOperation("检查云桌面快照名称是否重复")
    @RequestMapping(value = "checkNameDuplication", method = RequestMethod.POST)
    public CommonWebResponse<CheckNameDuplicationResponse> checkNameDuplication(CheckNameDuplicationRequest checkNameDuplicationRequest)
            throws BusinessException {
        Assert.notNull(checkNameDuplicationRequest, "checkNameDuplicationRequest must not be null");
        CheckNameDuplicationResponse checkNameDuplicationResponse = new CheckNameDuplicationResponse(false);
        String name = checkNameDuplicationRequest.getName();
        try {
            UUID deskSnapshotId = checkNameDuplicationRequest.getDeskSnapshotId();
            if (deskSnapshotId != null) {
                CbbDeskSnapshotDTO cbbDeskSnapshotDTO = cbbVDIDeskSnapshotAPI.findDeskSnapshotInfoById(deskSnapshotId);
                if (name.equals(cbbDeskSnapshotDTO.getName())) {
                    return CommonWebResponse.success(checkNameDuplicationResponse);
                }
            }
            Boolean hasDuplication = deskSnapshotAPI.checkNameDuplication(name);
            checkNameDuplicationResponse.setHasDuplication(hasDuplication);
        } catch (BusinessException e) {
            LOGGER.info("检查云桌面快照名称是否重复: ", e.getI18nMessage());
            Boolean hasDuplication = deskSnapshotAPI.checkNameDuplication(name);
            checkNameDuplicationResponse.setHasDuplication(hasDuplication);
        }
        return CommonWebResponse.success(checkNameDuplicationResponse);
    }

    private CbbDeskSnapshotDTO findDeskSnapshotInfoById(UUID deskSnapshotId) throws BusinessException {
        CbbDeskSnapshotDTO cbbDeskSnapshotDTO = cbbVDIDeskSnapshotAPI.findDeskSnapshotInfoById(deskSnapshotId);
        if (cbbDeskSnapshotDTO == null) {
            throw new BusinessException(DeskSnapshotBusinessKey.RCDC_DESK_SNAPSHOT_IS_NULL, deskSnapshotId.toString());
        }
        return cbbDeskSnapshotDTO;
    }

    private RequestBuilder generatePageQueryBuilder(PageWebRequest request) throws BusinessException {
        RequestBuilder builder = pageQueryBuilderFactory.newRequestBuilder().setPageLimit(request.getPage(), request.getLimit());

        // 精确匹配
        ExactMatch[] exactMatchArr = request.getExactMatchArr();
        if (ArrayUtils.isNotEmpty(exactMatchArr)) {
            for (ExactMatch exactMatch : exactMatchArr) {
                builder.in(exactMatch.getName(), exactMatch.getValueArr());
            }
        }

        // 排序处理
        Sort sort = request.getSort();
        if (request.getSort() == null) {
            return builder.desc(SORT_FIELD_CREATE_TIME);
        }

        PageValidationUtils.sortFieldValidation(SUPPORT_SORT_FIELD_ARR, sort.getSortField());

        if (sort.getDirection() == Direction.ASC) {
            builder.asc(sort.getSortField());
        } else {
            builder.desc(sort.getSortField());
        }
        return builder;
    }
}
