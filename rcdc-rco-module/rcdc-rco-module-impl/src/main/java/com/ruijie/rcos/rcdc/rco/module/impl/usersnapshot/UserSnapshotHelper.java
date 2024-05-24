package com.ruijie.rcos.rcdc.rco.module.impl.usersnapshot;

import com.ruijie.rcos.gss.log.module.def.api.BaseAuditLogAPI;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.api.CbbVDIDeskSnapshotAPI;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.dto.CbbDeskSnapshotDTO;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.enums.CbbDeskSnapshotState;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.enums.CbbDeskSnapshotUserType;
import com.ruijie.rcos.rcdc.rco.module.def.api.DeskSnapshotAPI;
import com.ruijie.rcos.rcdc.rco.module.def.api.SnapshotManageAPI;
import com.ruijie.rcos.rcdc.rco.module.def.api.UserDesktopMgmtAPI;
import com.ruijie.rcos.rcdc.rco.module.def.api.dto.CloudDesktopDTO;
import com.ruijie.rcos.rcdc.rco.module.def.api.dto.CloudDesktopDetailDTO;
import com.ruijie.rcos.rcdc.rco.module.def.api.dto.usersnapshot.*;
import com.ruijie.rcos.rcdc.rco.module.def.api.response.usersnapshot.UserSnapshotResponse;
import com.ruijie.rcos.rcdc.rco.module.impl.usersnapshot.batchtask.CreateUserSnapshotBatchTaskHandler;
import com.ruijie.rcos.rcdc.rco.module.impl.usersnapshot.batchtask.DeleteUserSnapshotBatchTaskHandler;
import com.ruijie.rcos.rcdc.rco.module.impl.usersnapshot.batchtask.RecoverUserSnapshotBatchTaskHandler;
import com.ruijie.rcos.sk.base.batch.BatchTaskBuilder;
import com.ruijie.rcos.sk.base.batch.BatchTaskSubmitResult;
import com.ruijie.rcos.sk.base.batch.DefaultBatchTaskItem;
import com.ruijie.rcos.sk.base.exception.BusinessException;
import com.ruijie.rcos.sk.base.log.Logger;
import com.ruijie.rcos.sk.base.log.LoggerFactory;
import com.ruijie.rcos.sk.base.sm2.StateMachineFactory;
import com.ruijie.rcos.sk.base.validator.ValidatorUtil;
import com.ruijie.rcos.sk.pagekit.api.PageQueryBuilderFactory;
import com.ruijie.rcos.sk.pagekit.api.PageQueryResponse;
import com.ruijie.rcos.sk.pagekit.api.Sort;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import java.util.*;
import java.util.stream.Stream;

/**
 * Description:  软终端 用户自定义快照功能
 * Copyright: Copyright (c) 2022
 * Company: Ruijie Co., Ltd.
 * Create Time: 2022/12/27
 *
 * @author liusd
 */
@Service
public class UserSnapshotHelper {
    private static final Logger LOGGER = LoggerFactory.getLogger(UserSnapshotHelper.class);

    @Autowired
    private CbbVDIDeskSnapshotAPI cbbVDIDeskSnapshotAPI;

    @Autowired
    private SnapshotManageAPI snapshotManageAPI;

    @Autowired
    private PageQueryBuilderFactory pageQueryBuilderFactory;

    @Autowired
    private DeskSnapshotAPI deskSnapshotAPI;

    @Autowired
    private StateMachineFactory stateMachineFactory;

    @Autowired
    private UserDesktopMgmtAPI userDesktopMgmtAPI;

    @Autowired
    private BaseAuditLogAPI auditLogAPI;

    private static final String SORT_FIELD_CREATE_TIME = "createTime";

    private static final String EQ_FIELD_DESK_ID = "deskId";

    private static final String EQ_FIELD_USER_ID = "userId";

    private static final int SNAPSHOT_NAME_MAX_LENGTH = 64;

    /**
     * FengBo用户获取快照列表
     * 
     * @param userSnapshotListDTO 查询参数对象
     * @return 查询对象
     * @throws BusinessException 业务异常
     */
    public PageQueryResponse<CbbDeskSnapshotDTO> list(UserSnapshotListDTO userSnapshotListDTO) throws BusinessException {
        Assert.notNull(userSnapshotListDTO, "userSnapshotListDTO must not be null");
        // 将业务参数解析出来
        Assert.notNull(userSnapshotListDTO.getDeskId(), " Client COMMON_ACTION:SNAPSHOT_LIST deskId is null");
        // 构造分页查询对象
        PageQueryBuilderFactory.RequestBuilder requestBuilder = generatePageQueryBuilder(userSnapshotListDTO);

        // 分页查询快照列表
        PageQueryResponse<CbbDeskSnapshotDTO> pageQueryResponse = cbbVDIDeskSnapshotAPI.pageQuery(requestBuilder.build());

        return pageQueryResponse;
    }

    /**
     * 构造快照分页查询对象
     * 
     * @param userSnapshotListDTO est透过Shine传入的业务参数
     * @return 快照分页查询生成器
     */
    private PageQueryBuilderFactory.RequestBuilder generatePageQueryBuilder(UserSnapshotListDTO userSnapshotListDTO) {
        PageQueryBuilderFactory.RequestBuilder requestBuilder = pageQueryBuilderFactory.newRequestBuilder();
        Integer limit = userSnapshotListDTO.getLimit();
        if (limit == null) {
            limit = 100;
        }
        Integer page = userSnapshotListDTO.getPage();
        if (page == null) {
            page = 0;
        }
        requestBuilder.setPageLimit(page, limit);
        requestBuilder.eq(EQ_FIELD_DESK_ID, userSnapshotListDTO.getDeskId());
        requestBuilder.eq(EQ_FIELD_USER_ID, userSnapshotListDTO.getUserId());
        Sort[] sortArr = userSnapshotListDTO.getSortArr();
        if (sortArr != null && sortArr.length > 0) {
            for (Sort sort : sortArr) {
                switch (sort.getDirection()) {
                    case ASC:
                        requestBuilder.asc(sort.getFieldName());
                        break;
                    case DESC:
                        requestBuilder.desc(sort.getFieldName());
                        break;
                    default:
                        requestBuilder.desc(SORT_FIELD_CREATE_TIME);
                }
            }
        } else {
            requestBuilder.desc(SORT_FIELD_CREATE_TIME);
        }
        return requestBuilder;
    }

    /**
     *  用户创建快照
     * 
     * @param userSnapshotCreateDTO 创建快照对象
     * @param builder 批任务处理器
     * @return 操作对象
     * @throws BusinessException 业务异常
     */
    public UserSnapshotResponse create(UserSnapshotCreateDTO userSnapshotCreateDTO, BatchTaskBuilder builder)
            throws BusinessException {
        Assert.notNull(userSnapshotCreateDTO, "userSnapshotCreateDTO must not be null");
        Assert.notNull(builder, "builder must not be null");

        // 检测是否包含非法字符
        if (!ValidatorUtil.isTextName(userSnapshotCreateDTO.getName())) {
            LOGGER.warn("桌面[{}]:检测是否包含非法字符", userSnapshotCreateDTO.getName());
            auditLogAPI.recordLog(UserSnapshotBusinessKey.RCDC_USER_SNAPSHOT_NAME_ILLEGAL_TIP);
            throw new BusinessException(UserSnapshotBusinessKey.RCDC_USER_SNAPSHOT_NAME_ILLEGAL_TIP);
        }
        // 检测名称是否超过64位
        if (userSnapshotCreateDTO.getName().length() > SNAPSHOT_NAME_MAX_LENGTH) {
            LOGGER.warn("桌面[{}]:检测名称超过64位", userSnapshotCreateDTO.getName());
            auditLogAPI.recordLog(UserSnapshotBusinessKey.RCDC_USER_SNAPSHOT_NAME_TOO_LONG_TIP);
            throw new BusinessException(UserSnapshotBusinessKey.RCDC_USER_SNAPSHOT_NAME_TOO_LONG_TIP);
        }

        UUID userId = userSnapshotCreateDTO.getUserId();
        CbbDeskSnapshotUserType userType = CbbDeskSnapshotUserType.USER;

        // 检查云桌面快照名称是否重复
        CloudDesktopDetailDTO cloudDesktopDetail = userDesktopMgmtAPI.getDesktopDetailById(userSnapshotCreateDTO.getDeskId());
        List<CloudDesktopDTO> cloudDesktopDTOList = userDesktopMgmtAPI.getAllDesktopByUserId(cloudDesktopDetail.getUserId());
        for (CloudDesktopDTO cloudDesktopDTO : cloudDesktopDTOList) {
            List<CbbDeskSnapshotDTO> cbbDeskSnapshotDTOList = cbbVDIDeskSnapshotAPI.listDeskSnapshotInfoByDeskId(cloudDesktopDTO.getId());
            for (CbbDeskSnapshotDTO cbbDeskSnapshotDTO : cbbDeskSnapshotDTOList) {
                if (cbbDeskSnapshotDTO.getName().equals(userSnapshotCreateDTO.getName())) {
                    LOGGER.warn("检查云桌面快照名称存在重复：[{}]", userSnapshotCreateDTO.getName());
                    auditLogAPI.recordLog(UserSnapshotBusinessKey.RCDC_USER_SNAPSHOT_NAME_DUPLICATION_TIP);
                    throw new BusinessException(UserSnapshotBusinessKey.RCDC_USER_SNAPSHOT_NAME_DUPLICATION_TIP);
                }
            }
        }

        UUID[] idArr = new UUID[] {userSnapshotCreateDTO.getDeskId()};
        final Iterator<DefaultBatchTaskItem> iterator =
                Stream.of(idArr).distinct().map(id -> DefaultBatchTaskItem.builder()
                .itemId(id).itemName(UserSnapshotBusinessKey.RCDC_USER_SNAPSHOT_CREATE_ITEM_NAME, new String[] {}).build())
                .iterator();

        CreateUserSnapshotBatchTaskHandler handler = new CreateUserSnapshotBatchTaskHandler(snapshotManageAPI, iterator);
        handler.setStateMachineFactory(stateMachineFactory);
        handler.setDeskSnapshotName(userSnapshotCreateDTO.getName());
        handler.setUserId(userId);
        handler.setUserType(userType);

        BatchTaskSubmitResult result = startCreateBatchTask(idArr, handler, builder);
        UserSnapshotResponse response = new UserSnapshotResponse();
        response.setTaskId(result.getTaskId());
        return response;
    }

    private BatchTaskSubmitResult startCreateBatchTask(UUID[] idArr, CreateUserSnapshotBatchTaskHandler handler, BatchTaskBuilder builder)
            throws BusinessException {
        // 创建单条
        BatchTaskSubmitResult result;
        if (idArr.length == 1) {
            handler.setIsBatch(false);
            CloudDesktopDetailDTO cloudDesktopDetailDTO = snapshotManageAPI.getDesktopDetailById(idArr[0]);
            result = builder.setTaskName(UserSnapshotBusinessKey.RCDC_USER_SNAPSHOT_CREATE_SINGLE_TASK_NAME)
                    .setTaskDesc(UserSnapshotBusinessKey.RCDC_USER_SNAPSHOT_CREATE_SINGLE_TASK_DESC, cloudDesktopDetailDTO.getUserName(),
                            cloudDesktopDetailDTO.getDesktopName(), handler.getDeskSnapshotName())
                    .enableParallel().registerHandler(handler).start();
        } else {
            result = builder.setTaskName(UserSnapshotBusinessKey.RCDC_USER_SNAPSHOT_CREATE_TASK_NAME)
                    .setTaskDesc(UserSnapshotBusinessKey.RCDC_USER_SNAPSHOT_CREATE_TASK_DESC).enableParallel()
                    .registerHandler(handler).start();
        }
        return result;
    }

    /**
     * 用户删除快照
     * 
     * @param userSnapshotDeleteDTO 原始的分发器传输对象
     * @param builder 批处理
     * @return 操作对象
     * @throws BusinessException 业务异常
     */
    public UserSnapshotResponse delete(UserSnapshotDeleteDTO userSnapshotDeleteDTO, BatchTaskBuilder builder)
            throws BusinessException {
        Assert.notNull(userSnapshotDeleteDTO, "userSnapshotDeleteDTO is need not null");
        Assert.notNull(builder, "builder must not be null");

        UUID[] idArr = userSnapshotDeleteDTO.getIdArr();
        for (UUID snapshotId : idArr) {
            // 快照不在被创建中、删除中、恢复中
            CbbDeskSnapshotDTO cbbDeskSnapshotDTO = findDeskSnapshotInfoById(snapshotId);
            if (cbbDeskSnapshotDTO.getState() == CbbDeskSnapshotState.DELETING) {
                LOGGER.warn("桌面[{}]正在删除中，无法二次删除操作", userSnapshotDeleteDTO.getDeskId());
                String[] msgArr = new String[]{cbbDeskSnapshotDTO.getName()};
                auditLogAPI.recordLog(UserSnapshotBusinessKey.RCDC_USER_SNAPSHOT_DELETE_BY_DELETING,
                        msgArr);
                throw new BusinessException(UserSnapshotBusinessKey.RCDC_USER_SNAPSHOT_DELETE_BY_DELETING,
                        msgArr);
            }
            if (cbbDeskSnapshotDTO.getState() == CbbDeskSnapshotState.CREATING) {
                LOGGER.warn("桌面[{}]正在创建中，无法删除操作", userSnapshotDeleteDTO.getDeskId());
                String[] msgArr = new String[]{cbbDeskSnapshotDTO.getName()};
                auditLogAPI.recordLog(UserSnapshotBusinessKey.RCDC_USER_SNAPSHOT_DELETE_BY_CREATING,
                        msgArr);
                throw new BusinessException(UserSnapshotBusinessKey.RCDC_USER_SNAPSHOT_DELETE_BY_CREATING,
                        msgArr);
            }
            if (cbbDeskSnapshotDTO.getState() == CbbDeskSnapshotState.RESTORING) {
                LOGGER.warn("桌面[{}]正在恢复中，无法删除操作", userSnapshotDeleteDTO.getDeskId());
                String[] msgArr = new String[]{cbbDeskSnapshotDTO.getName()};
                auditLogAPI.recordLog(UserSnapshotBusinessKey.RCDC_USER_SNAPSHOT_DELETE_BY_RESTORING,
                        msgArr);
                throw new BusinessException(UserSnapshotBusinessKey.RCDC_USER_SNAPSHOT_DELETE_BY_RESTORING,
                        msgArr);
            }
        }
        // 批量删除任务
        final Iterator<DefaultBatchTaskItem> iterator = Stream.of(idArr).distinct().map(id -> DefaultBatchTaskItem.builder().itemId(id)
                .itemName(UserSnapshotBusinessKey.RCDC_USER_SNAPSHOT_DELETE_ITEM_NAME, new String[] {}).build()).iterator();
        DeleteUserSnapshotBatchTaskHandler handler = new DeleteUserSnapshotBatchTaskHandler(snapshotManageAPI, iterator);
        handler.setStateMachineFactory(stateMachineFactory);
        BatchTaskSubmitResult result = startDeleteBatchTask(idArr, handler, builder);
        UserSnapshotResponse response = new UserSnapshotResponse();
        response.setTaskId(result.getTaskId());
        return response;
    }

    private BatchTaskSubmitResult startDeleteBatchTask(UUID[] idArr, DeleteUserSnapshotBatchTaskHandler handler, BatchTaskBuilder builder)
            throws BusinessException {
        // 删除单条用户
        BatchTaskSubmitResult result;
        if (idArr.length == 1) {
            handler.setIsBatch(false);
            CbbDeskSnapshotDTO deskSnapshotDTO = cbbVDIDeskSnapshotAPI.findDeskSnapshotInfoById(idArr[0]);
            CloudDesktopDetailDTO cloudDesktopDetailDTO = userDesktopMgmtAPI.getDesktopDetailById(deskSnapshotDTO.getDeskId());
            result = builder.setTaskName(UserSnapshotBusinessKey.RCDC_USER_SNAPSHOT_DELETE_SINGLE_TASK_NAME)
                    .setTaskDesc(UserSnapshotBusinessKey.RCDC_USER_SNAPSHOT_DELETE_SINGLE_TASK_DESC, cloudDesktopDetailDTO.getUserName(),
                            cloudDesktopDetailDTO.getDesktopName(), deskSnapshotDTO.getName())
                    .enableParallel().registerHandler(handler).start();
        } else {
            result = builder.setTaskName(UserSnapshotBusinessKey.RCDC_USER_SNAPSHOT_DELETE_TASK_NAME)
                    .setTaskDesc(UserSnapshotBusinessKey.RCDC_USER_SNAPSHOT_DELETE_TASK_DESC).enableParallel()
                    .registerHandler(handler).start();
        }
        return result;
    }

    /**
     * 用户恢复快照
     * 
     * @param userSnapshotRecoverDTO 原始的分发器传输对象
     * @param builder 批处理
     * @return 操作对象
     * @throws BusinessException 业务异常
     */
    public UserSnapshotResponse revert(UserSnapshotRevertDTO userSnapshotRecoverDTO, BatchTaskBuilder builder) throws BusinessException {
        Assert.notNull(userSnapshotRecoverDTO, "userSnapshotRecoverDTO is need not null");
        Assert.notNull(builder, "builder must not be null");

        // 快照不在被创建中、删除中、恢复中
        CbbDeskSnapshotDTO cbbDeskSnapshotDTO = findDeskSnapshotInfoById(userSnapshotRecoverDTO.getId());
        if (cbbDeskSnapshotDTO.getState() == CbbDeskSnapshotState.DELETING) {
            LOGGER.warn("桌面[{}]正在删除中，无法二次删除操作", userSnapshotRecoverDTO.getDeskId());
            String[] msgArr = new String[]{cbbDeskSnapshotDTO.getName()};
            auditLogAPI.recordLog(UserSnapshotBusinessKey.RCDC_USER_SNAPSHOT_RECOVER_BY_DELETING,
                    msgArr);
            throw new BusinessException(UserSnapshotBusinessKey.RCDC_USER_SNAPSHOT_RECOVER_BY_DELETING,
                    msgArr);
        }
        if (cbbDeskSnapshotDTO.getState() == CbbDeskSnapshotState.CREATING) {
            LOGGER.warn("桌面[{}]正在创建中，无法删除操作", userSnapshotRecoverDTO.getDeskId());
            String[] msgArr = new String[]{cbbDeskSnapshotDTO.getName()};
            auditLogAPI.recordLog(UserSnapshotBusinessKey.RCDC_USER_SNAPSHOT_RECOVER_BY_CREATING,
                    msgArr);
            throw new BusinessException(UserSnapshotBusinessKey.RCDC_USER_SNAPSHOT_RECOVER_BY_CREATING,
                    msgArr);
        }
        if (cbbDeskSnapshotDTO.getState() == CbbDeskSnapshotState.RESTORING) {
            LOGGER.warn("桌面[{}]正在恢复中，无法删除操作", userSnapshotRecoverDTO.getDeskId());
            String[] msgArr = new String[]{cbbDeskSnapshotDTO.getName()};
            auditLogAPI.recordLog(UserSnapshotBusinessKey.RCDC_USER_SNAPSHOT_RECOVER_BY_RESTORING,
                    msgArr);
            throw new BusinessException(UserSnapshotBusinessKey.RCDC_USER_SNAPSHOT_RECOVER_BY_RESTORING,
                    msgArr);
        }

        UUID[] idArr = new UUID[] {userSnapshotRecoverDTO.getId()};

        Map<UUID, Boolean> snapshotShutdownTypeMap = new HashMap<>();
        for (UUID deskId : idArr) {
            snapshotShutdownTypeMap.put(deskId, userSnapshotRecoverDTO.getForceShutdown());
        }
        final Iterator<DefaultBatchTaskItem> iterator = Stream.of(idArr).distinct().map(id -> DefaultBatchTaskItem.builder()
                .itemId(id).itemName(UserSnapshotBusinessKey.RCDC_USER_SNAPSHOT_RECOVER_ITEM_NAME, new String[] {}).build())
                .iterator();

        RecoverUserSnapshotBatchTaskHandler handler = new RecoverUserSnapshotBatchTaskHandler(snapshotManageAPI, iterator);
        handler.setStateMachineFactory(stateMachineFactory);
        handler.setSnapshotShutdownTypeMap(snapshotShutdownTypeMap);
        BatchTaskSubmitResult result = startRecoverBatchTask(idArr, handler, builder);
        UserSnapshotResponse response = new UserSnapshotResponse();
        response.setTaskId(result.getTaskId());
        return response;
    }

    private BatchTaskSubmitResult startRecoverBatchTask(UUID[] idArr, RecoverUserSnapshotBatchTaskHandler handler, BatchTaskBuilder builder)
            throws BusinessException {
        BatchTaskSubmitResult result;
        if (idArr.length == 1) {
            handler.setIsBatch(false);
            CbbDeskSnapshotDTO cbbDeskSnapshotDTO = findDeskSnapshotInfoById(idArr[0]);
            CloudDesktopDetailDTO cloudDesktopDetailDTO = userDesktopMgmtAPI.getDesktopDetailById(cbbDeskSnapshotDTO.getDeskId());
            result = builder.setTaskName(UserSnapshotBusinessKey.RCDC_USER_SNAPSHOT_RECOVER_SINGLE_TASK_NAME)
                    .setTaskDesc(UserSnapshotBusinessKey.RCDC_USER_SNAPSHOT_RECOVER_SINGLE_TASK_DESC,
                            cloudDesktopDetailDTO.getUserName(), cloudDesktopDetailDTO.getDesktopName(),
                            cbbDeskSnapshotDTO.getName())
                    .enableParallel().registerHandler(handler).start();
        } else {
            result = builder.setTaskName(UserSnapshotBusinessKey.RCDC_USER_SNAPSHOT_RECOVER_TASK_NAME)
                    .setTaskDesc(UserSnapshotBusinessKey.RCDC_USER_SNAPSHOT_RECOVER_TASK_DESC).enableParallel()
                    .registerHandler(handler).start();
        }
        return result;
    }

    private CbbDeskSnapshotDTO findDeskSnapshotInfoById(UUID deskSnapshotId) throws BusinessException {
        CbbDeskSnapshotDTO cbbDeskSnapshotDTO = cbbVDIDeskSnapshotAPI.findDeskSnapshotInfoById(deskSnapshotId);
        if (cbbDeskSnapshotDTO == null) {
            throw new BusinessException(UserSnapshotBusinessKey.RCDC_USER_SNAPSHOT_IS_NULL, deskSnapshotId.toString());
        }
        return cbbDeskSnapshotDTO;
    }

}
