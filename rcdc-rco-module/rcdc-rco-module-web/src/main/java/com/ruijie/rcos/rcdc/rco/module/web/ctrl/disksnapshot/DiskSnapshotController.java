package com.ruijie.rcos.rcdc.rco.module.web.ctrl.disksnapshot;

import com.ruijie.rcos.gss.base.iac.module.annotation.EnableAuthority;
import com.ruijie.rcos.gss.log.module.def.api.BaseAuditLogAPI;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.api.CbbVDIDeskDiskAPI;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.api.CbbVDIDiskSnapshotMgmtAPI;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.dto.CbbDeskDiskDTO;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.dto.CbbDiskSnapshotDetailDTO;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.enums.CbbDiskSnapshotState;
import com.ruijie.rcos.rcdc.rco.module.def.api.DiskSnapshotAPI;
import com.ruijie.rcos.rcdc.rco.module.def.api.UserDiskMgmtAPI;
import com.ruijie.rcos.rcdc.rco.module.def.diskpool.dto.UserDiskDetailDTO;
import com.ruijie.rcos.rcdc.rco.module.web.config.annotation.ApiVersion;
import com.ruijie.rcos.rcdc.rco.module.web.config.annotation.ApiVersions;
import com.ruijie.rcos.rcdc.rco.module.web.config.annotation.Version;
import com.ruijie.rcos.rcdc.rco.module.web.ctrl.disksnapshot.batchtask.CreateDiskSnapshotBatchTaskHandler;
import com.ruijie.rcos.rcdc.rco.module.web.ctrl.disksnapshot.batchtask.DeleteDiskSnapshotBatchTaskHandler;
import com.ruijie.rcos.rcdc.rco.module.web.ctrl.disksnapshot.batchtask.RecoverDiskSnapshotBatchTaskHandler;
import com.ruijie.rcos.rcdc.rco.module.web.ctrl.disksnapshot.request.CheckDiskSnapshotNameDuplicationRequest;
import com.ruijie.rcos.rcdc.rco.module.web.ctrl.disksnapshot.request.CreateDiskSnapshotRequest;
import com.ruijie.rcos.rcdc.rco.module.web.ctrl.disksnapshot.request.EditDiskSnapshotRequest;
import com.ruijie.rcos.rcdc.rco.module.web.ctrl.disksnapshot.request.FindDefaultDiskSnapshotNameRequest;
import com.ruijie.rcos.rcdc.rco.module.web.ctrl.disksnapshot.response.*;
import com.ruijie.rcos.rcdc.rco.module.web.response.CommonWebResponse;
import com.ruijie.rcos.rcdc.rco.module.web.util.PageValidationUtils;
import com.ruijie.rcos.sk.base.batch.BatchTaskBuilder;
import com.ruijie.rcos.sk.base.batch.BatchTaskSubmitResult;
import com.ruijie.rcos.sk.base.batch.DefaultBatchTaskItem;
import com.ruijie.rcos.sk.base.exception.BusinessException;
import com.ruijie.rcos.sk.base.log.Logger;
import com.ruijie.rcos.sk.base.log.LoggerFactory;
import com.ruijie.rcos.sk.base.sm2.StateMachineFactory;
import com.ruijie.rcos.sk.pagekit.api.PageQueryBuilderFactory;
import com.ruijie.rcos.sk.pagekit.api.PageQueryRequest;
import com.ruijie.rcos.sk.pagekit.api.PageQueryResponse;
import com.ruijie.rcos.sk.webmvc.api.request.IdArrWebRequest;
import com.ruijie.rcos.sk.webmvc.api.request.IdWebRequest;
import com.ruijie.rcos.sk.webmvc.api.request.PageWebRequest;
import com.ruijie.rcos.sk.webmvc.api.vo.ExactMatch;
import com.ruijie.rcos.sk.webmvc.api.vo.Sort;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.lang3.ArrayUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.util.Iterator;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Stream;


/**
 *
 * Description: 磁盘快照controller
 * Copyright: Copyright (c) 2022
 * Company: Ruijie Co., Ltd.
 * Create Time: 2022年7月22日
 *
 * @author lyb
 */
@Api(tags = "磁盘快照")
@Controller
@RequestMapping("/rco/disk/diskSnapshot")
public class DiskSnapshotController {

    private static final Logger LOGGER = LoggerFactory.getLogger(DiskSnapshotController.class);

    @Autowired
    private CbbVDIDiskSnapshotMgmtAPI cbbVDIDiskSnapshotMgmtAPI;

    @Autowired
    private DiskSnapshotAPI diskSnapshotAPI;

    @Autowired
    private PageQueryBuilderFactory pageQueryBuilderFactory;

    @Autowired
    private StateMachineFactory stateMachineFactory;

    @Autowired
    private CbbVDIDeskDiskAPI cbbVDIDeskDiskAPI;

    @Autowired
    private UserDiskMgmtAPI userDiskMgmtAPI;

    @Autowired
    private BaseAuditLogAPI auditLogAPI;

    private static final String SORT_FIELD_CREATE_TIME = "createTime";

    private static final String[] SUPPORT_SORT_FIELD_ARR = new String[] {"name"};

    /**
     * 获取磁盘快照列表
     *
     * @param webRequest 入参
     * @return 响应
     * @throws BusinessException 业务异常
     */
    @ApiOperation("获取磁盘快照列表")
    @RequestMapping(value = "list", method = RequestMethod.POST)
    public CommonWebResponse<DiskSnapshotPageQueryResponse<DiskSnapshotDetailVO>> list(PageWebRequest webRequest) throws BusinessException {
        Assert.notNull(webRequest, "webRequest must not be null");
        PageQueryRequest pageQueryRequest = generatePageQueryBuilder(webRequest).build();
        PageQueryResponse<CbbDiskSnapshotDetailDTO> detailDTOPageQueryResponse = cbbVDIDiskSnapshotMgmtAPI.pageQueryDiskSnapshot(pageQueryRequest);

        CbbDiskSnapshotDetailDTO[] cbbDiskSnapshotDetailDTOArr = Optional.ofNullable(detailDTOPageQueryResponse.getItemArr()) //
                .orElse(new CbbDiskSnapshotDetailDTO[0]);
        DiskSnapshotDetailVO[] diskSnapshotDetailVOArr = new DiskSnapshotDetailVO[cbbDiskSnapshotDetailDTOArr.length];
        DiskSnapshotPageQueryResponse<DiskSnapshotDetailVO> response =
                new DiskSnapshotPageQueryResponse<>(diskSnapshotDetailVOArr, detailDTOPageQueryResponse.getTotal());
        if (ArrayUtils.isNotEmpty(detailDTOPageQueryResponse.getItemArr())) {
            UUID diskId = detailDTOPageQueryResponse.getItemArr()[0].getDiskId();
            UserDiskDetailDTO userDiskDetailDTO = userDiskMgmtAPI.userDiskDetail(diskId);
            response.setDiskState(userDiskDetailDTO.getDiskState().name());
            for (int i = 0; i < cbbDiskSnapshotDetailDTOArr.length; i++) {
                DiskSnapshotDetailVO diskSnapshotDetailVO = getDiskSnapshotDetailVO(userDiskDetailDTO, cbbDiskSnapshotDetailDTOArr[i]);
                diskSnapshotDetailVOArr[i] = diskSnapshotDetailVO;
            }
        }
        return CommonWebResponse.success(response);
    }

    private static DiskSnapshotDetailVO getDiskSnapshotDetailVO(UserDiskDetailDTO userDiskDetailDTO,
                                                                CbbDiskSnapshotDetailDTO cbbDiskSnapshotDetailDTO) {
        DiskSnapshotDetailVO diskSnapshotDetailVO = new DiskSnapshotDetailVO();
        diskSnapshotDetailVO.setId(cbbDiskSnapshotDetailDTO.getId());
        diskSnapshotDetailVO.setName(cbbDiskSnapshotDetailDTO.getName());
        diskSnapshotDetailVO.setDiskId(cbbDiskSnapshotDetailDTO.getDiskId());
        diskSnapshotDetailVO.setState(cbbDiskSnapshotDetailDTO.getState());
        diskSnapshotDetailVO.setCreateTime(cbbDiskSnapshotDetailDTO.getCreateTime());
        diskSnapshotDetailVO.setPlatformId(userDiskDetailDTO.getPlatformId());
        diskSnapshotDetailVO.setPlatformType(userDiskDetailDTO.getPlatformType());
        diskSnapshotDetailVO.setPlatformName(userDiskDetailDTO.getPlatformName());
        diskSnapshotDetailVO.setPlatformStatus(userDiskDetailDTO.getPlatformStatus());
        return diskSnapshotDetailVO;
    }

    /**
     * 创建磁盘快照
     *
     * @param createDiskSnapshotRequest 入参
     * @param builder 批量任务创建对象
     * @return 响应
     * @throws BusinessException 业务异常
     */
    @ApiOperation("创建磁盘快照")
    @ApiVersions({@ApiVersion(value = Version.V1_0_0), @ApiVersion(value = Version.V3_1_1, descriptions = {"分级分权-区分权限，加入权限控制"})})
    @RequestMapping(value = "create", method = RequestMethod.POST)
    @EnableAuthority
    public CommonWebResponse<BatchTaskSubmitResult> create(CreateDiskSnapshotRequest createDiskSnapshotRequest, BatchTaskBuilder builder)
            throws BusinessException {
        Assert.notNull(createDiskSnapshotRequest, "createDiskSnapshotRequest must not be null");
        Assert.notNull(builder, "builder must not be null");

        UUID[] idArr = new UUID[] {createDiskSnapshotRequest.getDiskId()};
        final Iterator<DefaultBatchTaskItem> iterator = Stream.of(idArr).distinct().map(id -> DefaultBatchTaskItem.builder()//
                .itemId(id).itemName(DiskSnapshotBusinessKey.RCDC_RCO_DISK_SNAPSHOT_CREATE_ITEM_NAME, new String[] {}).build())//
                .iterator();
        CreateDiskSnapshotBatchTaskHandler handler =
                new CreateDiskSnapshotBatchTaskHandler(cbbVDIDiskSnapshotMgmtAPI, diskSnapshotAPI, iterator, auditLogAPI);
        handler.setStateMachineFactory(stateMachineFactory);
        handler.setSnapshotName(createDiskSnapshotRequest.getName());
        BatchTaskSubmitResult result = startCreateBatchTask(idArr, handler, builder);
        return CommonWebResponse.success(result);

    }

    private BatchTaskSubmitResult startCreateBatchTask(UUID[] idArr, CreateDiskSnapshotBatchTaskHandler handler, BatchTaskBuilder builder)
            throws BusinessException {
        // 创建单条用户
        BatchTaskSubmitResult result;
        if (idArr.length == 1) {
            handler.setIsBatch(false);
            result = builder.setTaskName(DiskSnapshotBusinessKey.RCDC_RCO_DISK_SNAPSHOT_CREATE_SINGLE_TASK_NAME)
                    .setTaskDesc(DiskSnapshotBusinessKey.RCDC_RCO_DISK_SNAPSHOT_CREATE_SINGLE_TASK_DESC, handler.getSnapshotName()).enableParallel()
                    .registerHandler(handler).start();
        } else {
            result = builder.setTaskName(DiskSnapshotBusinessKey.RCDC_RCO_DISK_SNAPSHOT_CREATE_TASK_NAME)
                    .setTaskDesc(DiskSnapshotBusinessKey.RCDC_RCO_DISK_SNAPSHOT_CREATE_TASK_DESC).enableParallel().registerHandler(handler).start();
        }
        return result;
    }

    /**
     * 磁盘快照恢复
     *
     * @param idRequest 入参
     * @param builder 批量任务创建对象
     * @return 响应
     * @throws BusinessException 业务异常
     */
    @ApiOperation("磁盘快照恢复")
    @ApiVersions({@ApiVersion(value = Version.V1_0_0), @ApiVersion(value = Version.V3_1_1, descriptions = {"分级分权-区分权限，加入权限控制"})})
    @RequestMapping(value = "recover", method = RequestMethod.POST)
    @EnableAuthority
    public CommonWebResponse<BatchTaskSubmitResult> recover(IdWebRequest idRequest, BatchTaskBuilder builder) throws BusinessException {
        Assert.notNull(idRequest, "idRequest must not be null");
        Assert.notNull(builder, "builder must not be null");

        UUID[] idArr = new UUID[] {idRequest.getId()};

        final Iterator<DefaultBatchTaskItem> iterator = Stream.of(idArr).distinct().map(id -> DefaultBatchTaskItem.builder()//
                .itemId(id).itemName(DiskSnapshotBusinessKey.RCDC_RCO_DISK_SNAPSHOT_RECOVER_ITEM_NAME, new String[] {}).build())//
                .iterator();

        RecoverDiskSnapshotBatchTaskHandler handler = new RecoverDiskSnapshotBatchTaskHandler(cbbVDIDiskSnapshotMgmtAPI, iterator, auditLogAPI);
        handler.setStateMachineFactory(stateMachineFactory);
        BatchTaskSubmitResult result = startRecoverBatchTask(idArr, handler, builder);
        return CommonWebResponse.success(result);
    }

    private BatchTaskSubmitResult startRecoverBatchTask(UUID[] idArr, RecoverDiskSnapshotBatchTaskHandler handler, BatchTaskBuilder builder)
            throws BusinessException {
        BatchTaskSubmitResult result;
        if (idArr.length == 1) {
            handler.setIsBatch(false);
            CbbDiskSnapshotDetailDTO cbbDiskSnapshotDetailDTO = findDiskSnapshotInfoById(idArr[0]);
            result = builder.setTaskName(DiskSnapshotBusinessKey.RCDC_RCO_DISK_SNAPSHOT_RECOVER_SINGLE_TASK_NAME)
                    .setTaskDesc(DiskSnapshotBusinessKey.RCDC_RCO_DISK_SNAPSHOT_RECOVER_SINGLE_TASK_DESC, cbbDiskSnapshotDetailDTO.getName())
                    .enableParallel().registerHandler(handler).start();
        } else {
            result = builder.setTaskName(DiskSnapshotBusinessKey.RCDC_RCO_DISK_SNAPSHOT_RECOVER_TASK_NAME)
                    .setTaskDesc(DiskSnapshotBusinessKey.RCDC_RCO_DISK_SNAPSHOT_RECOVER_TASK_DESC).enableParallel().registerHandler(handler).start();
        }
        return result;
    }

    /**
     * 删除磁盘快照
     *
     * @param idArrWebRequest 入参
     * @param builder 批量任务创建对象
     * @return 响应
     * @throws BusinessException 业务异常
     */
    @ApiOperation("删除磁盘快照")
    @ApiVersions({@ApiVersion(value = Version.V1_0_0), @ApiVersion(value = Version.V3_1_1, descriptions = {"分级分权-区分权限，加入权限控制"})})
    @RequestMapping(value = "delete", method = RequestMethod.POST)
    @EnableAuthority
    public CommonWebResponse<BatchTaskSubmitResult> delete(IdArrWebRequest idArrWebRequest, BatchTaskBuilder builder) throws BusinessException {
        Assert.notNull(idArrWebRequest, "idArrWebRequest must not be null");
        Assert.notNull(builder, "builder can not be null");

        UUID[] idArr = idArrWebRequest.getIdArr();
        // 批量删除任务
        final Iterator<DefaultBatchTaskItem> iterator = Stream.of(idArr).distinct().map(id -> DefaultBatchTaskItem.builder().itemId(id)
                .itemName(DiskSnapshotBusinessKey.RCDC_RCO_DISK_SNAPSHOT_DELETE_ITEM_NAME, new String[] {}).build()).iterator();
        DeleteDiskSnapshotBatchTaskHandler handler = new DeleteDiskSnapshotBatchTaskHandler(cbbVDIDiskSnapshotMgmtAPI, iterator, auditLogAPI);
        handler.setStateMachineFactory(stateMachineFactory);
        BatchTaskSubmitResult result = startDeleteBatchTask(idArr, handler, builder);
        return CommonWebResponse.success(result);
    }

    private BatchTaskSubmitResult startDeleteBatchTask(UUID[] idArr, DeleteDiskSnapshotBatchTaskHandler handler, BatchTaskBuilder builder)
            throws BusinessException {
        // 删除单条用户
        BatchTaskSubmitResult result;
        if (idArr.length == 1) {
            handler.setIsBatch(false);
            CbbDiskSnapshotDetailDTO cbbDiskSnapshotDetailDTO = cbbVDIDiskSnapshotMgmtAPI.findDiskSnapshotById(idArr[0]);
            result = builder.setTaskName(DiskSnapshotBusinessKey.RCDC_RCO_DISK_SNAPSHOT_DELETE_SINGLE_TASK_NAME)
                    .setTaskDesc(DiskSnapshotBusinessKey.RCDC_RCO_DISK_SNAPSHOT_DELETE_SINGLE_TASK_DESC, cbbDiskSnapshotDetailDTO.getName())
                    .enableParallel().registerHandler(handler).start();
        } else {
            result = builder.setTaskName(DiskSnapshotBusinessKey.RCDC_RCO_DISK_SNAPSHOT_DELETE_TASK_NAME)
                    .setTaskDesc(DiskSnapshotBusinessKey.RCDC_RCO_DISK_SNAPSHOT_DELETE_TASK_DESC).enableParallel().registerHandler(handler).start();
        }
        return result;
    }

    /**
     * 编辑磁盘快照
     *
     * @param editDiskSnapshotRequest 入参
     * @return 响应
     * @throws BusinessException 业务异常
     */
    @ApiOperation("编辑磁盘快照")
    @ApiVersions({@ApiVersion(value = Version.V1_0_0), @ApiVersion(value = Version.V3_1_1, descriptions = {"分级分权-区分权限，加入权限控制"})})
    @RequestMapping(value = "edit", method = RequestMethod.POST)
    @EnableAuthority
    public CommonWebResponse edit(EditDiskSnapshotRequest editDiskSnapshotRequest) throws BusinessException {
        Assert.notNull(editDiskSnapshotRequest, "editDiskSnapshotRequest must not be null");
        UUID deskSnapshotId = editDiskSnapshotRequest.getId();

        try {
            CbbDiskSnapshotDetailDTO cbbDiskSnapshotDetailDTO = findDiskSnapshotInfoById(deskSnapshotId);

            if (cbbDiskSnapshotDetailDTO.getState() == CbbDiskSnapshotState.DELETING) {
                LOGGER.error("磁盘快照[{}]修改异常。失败原因：磁盘处于删除中，不支持修改。", editDiskSnapshotRequest.getName());
                throw new BusinessException(DiskSnapshotBusinessKey.RCDC_RCO_DISK_SNAPSHOT_EDIT_BY_DELETING, cbbDiskSnapshotDetailDTO.getName());
            }

            if (cbbDiskSnapshotDetailDTO.getName().equals(editDiskSnapshotRequest.getName())) {
                LOGGER.info("磁盘快照[{}]名称未发生改变。", editDiskSnapshotRequest.getName());
            } else {
                cbbVDIDiskSnapshotMgmtAPI.updateDiskSnapshotName(editDiskSnapshotRequest.getId(), editDiskSnapshotRequest.getName());
            }
            auditLogAPI.recordLog(DiskSnapshotBusinessKey.RCDC_RCO_DISK_SNAPSHOT_OPERATE_EDIT_SUCCESS, editDiskSnapshotRequest.getName());
            return CommonWebResponse.success(DiskSnapshotBusinessKey.RCDC_RCO_DISK_SNAPSHOT_OPERATE_SUCCESS, new String[] {});
        } catch (BusinessException e) {
            auditLogAPI.recordLog(DiskSnapshotBusinessKey.RCDC_RCO_DISK_SNAPSHOT_OPERATE_EDIT_FAIL, editDiskSnapshotRequest.getName(),
                    e.getI18nMessage());
            return CommonWebResponse.fail(DiskSnapshotBusinessKey.RCDC_RCO_DISK_SNAPSHOT_OPERATE_FAIL, new String[] {e.getI18nMessage()});
        }
    }

    /**
     * 根据磁盘ID获取磁盘快照默认名称
     *
     * @param request 入参
     * @return 响应
     * @throws BusinessException 业务异常
     */
    @ApiOperation("获取磁盘快照默认名称")
    @RequestMapping(value = "findDefaultSnapshotName", method = RequestMethod.POST)
    public CommonWebResponse<FindDefaultDiskSnapshotNameResponse> findDefaultSnapshotName(FindDefaultDiskSnapshotNameRequest request)
            throws BusinessException {
        Assert.notNull(request, "request must not be null");
        CbbDeskDiskDTO cbbDeskDiskDTO = cbbVDIDeskDiskAPI.getDiskDetail(request.getDiskId());
        String snapshotName = diskSnapshotAPI.generateSnapshotNameByDiskName(cbbDeskDiskDTO.getName());
        FindDefaultDiskSnapshotNameResponse response = new FindDefaultDiskSnapshotNameResponse(snapshotName);
        return CommonWebResponse.success(response);
    }

    /**
     * 检查磁盘快照名称是否重复
     *
     * @param checkNameDuplicationRequest 入参
     * @return 响应
     * @throws BusinessException 业务异常
     */
    @ApiOperation("检查磁盘快照名称是否重复")
    @RequestMapping(value = "checkNameDuplication", method = RequestMethod.POST)
    public CommonWebResponse<CheckDiskSnapshotNameDuplicationResponse> checkNameDuplication(
            CheckDiskSnapshotNameDuplicationRequest checkNameDuplicationRequest)
            throws BusinessException {
        Assert.notNull(checkNameDuplicationRequest, "checkNameDuplicationRequest must not be null");
        CheckDiskSnapshotNameDuplicationResponse checkNameDuplicationResponse = new CheckDiskSnapshotNameDuplicationResponse(false);
        String name = checkNameDuplicationRequest.getName();
        try {
            UUID diskSnapshotId = checkNameDuplicationRequest.getDiskSnapshotId();
            if (diskSnapshotId != null) {
                CbbDiskSnapshotDetailDTO cbbDiskSnapshotDetailDTO = cbbVDIDiskSnapshotMgmtAPI.findDiskSnapshotById(diskSnapshotId);
                if (name.equals(cbbDiskSnapshotDetailDTO.getName())) {
                    return CommonWebResponse.success(checkNameDuplicationResponse);
                }
            }
            Boolean hasDuplication = diskSnapshotAPI.checkNameDuplication(name);
            checkNameDuplicationResponse.setHasDuplication(hasDuplication);
        } catch (BusinessException e) {
            LOGGER.error("检查磁盘快照名称是否重复出现异常: ", e.getI18nMessage());
            Boolean hasDuplication = diskSnapshotAPI.checkNameDuplication(name);
            checkNameDuplicationResponse.setHasDuplication(hasDuplication);
        }
        return CommonWebResponse.success(checkNameDuplicationResponse);
    }

    /**
     * 检测磁盘快照数量是否满配
     *
     * @param idWebRequest 入参
     * @return 响应
     * @throws BusinessException 业务异常
     */
    @ApiOperation("检测磁盘快照数量是否满配")
    @RequestMapping(value = "checkSnapshotFull", method = RequestMethod.POST)
    public CommonWebResponse<CheckDiskSnapshotFullResponse> checkSnapshotFull(IdWebRequest idWebRequest) throws BusinessException {
        Assert.notNull(idWebRequest, "idWebRequest must not be null");
        Boolean hasFull = diskSnapshotAPI.checkSnapshotNumberOverByDiskId(idWebRequest.getId());
        CheckDiskSnapshotFullResponse response = new CheckDiskSnapshotFullResponse(hasFull);
        return CommonWebResponse.success(response);
    }

    private CbbDiskSnapshotDetailDTO findDiskSnapshotInfoById(UUID deskSnapshotId) throws BusinessException {
        CbbDiskSnapshotDetailDTO cbbDiskSnapshotDetailDTO = cbbVDIDiskSnapshotMgmtAPI.findDiskSnapshotById(deskSnapshotId);
        if (cbbDiskSnapshotDetailDTO == null) {
            throw new BusinessException(DiskSnapshotBusinessKey.RCDC_RCO_DISK_SNAPSHOT_IS_NULL, deskSnapshotId.toString());
        }
        return cbbDiskSnapshotDetailDTO;
    }

    private PageQueryBuilderFactory.RequestBuilder generatePageQueryBuilder(PageWebRequest request) throws BusinessException {
        PageQueryBuilderFactory.RequestBuilder builder =
                pageQueryBuilderFactory.newRequestBuilder().setPageLimit(request.getPage(), request.getLimit());

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

        if (sort.getDirection() == Sort.Direction.ASC) {
            builder.asc(sort.getSortField());
        } else {
            builder.desc(sort.getSortField());
        }
        return builder;
    }
}
