package com.ruijie.rcos.rcdc.rco.module.impl.est.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.ruijie.rcos.base.task.module.def.api.BaseMsgMgmtAPI;
import com.ruijie.rcos.base.task.module.def.api.request.msg.BaseGetMsgRequest;
import com.ruijie.rcos.base.task.module.def.dto.msg.BaseMsgDTO;
import com.ruijie.rcos.gss.base.iac.module.enums.SubSystem;
import com.ruijie.rcos.gss.sdk.iac.module.def.dto.user.IacAuthUserDTO;
import com.ruijie.rcos.gss.sdk.iac.module.def.dto.user.IacAuthUserResultDTO;
import com.ruijie.rcos.gss.sdk.iac.module.def.api.IacAdminMgmtAPI;
import com.ruijie.rcos.gss.sdk.iac.module.def.api.IacUserMgmtAPI;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.api.CbbVDIDeskSnapshotAPI;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.dto.CbbDeskSnapshotDTO;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.dto.desk.CbbDeskSnapshotDistributeDTO;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.enums.CbbDeskSnapshotState;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.enums.CbbDeskSnapshotUserType;
import com.ruijie.rcos.rcdc.rco.module.def.api.SnapshotManageAPI;
import com.ruijie.rcos.rcdc.rco.module.def.api.UserDesktopMgmtAPI;
import com.ruijie.rcos.rcdc.rco.module.def.api.dto.CloudDesktopDTO;
import com.ruijie.rcos.rcdc.rco.module.def.api.dto.CloudDesktopDetailDTO;
import com.ruijie.rcos.rcdc.rco.module.def.api.request.usersnapshot.UserSnapshotCreateRequest;
import com.ruijie.rcos.rcdc.rco.module.def.api.request.usersnapshot.UserSnapshotListRequest;
import com.ruijie.rcos.rcdc.rco.module.def.api.request.usersnapshot.UserSnapshotRevertRequest;
import com.ruijie.rcos.rcdc.rco.module.def.api.response.usersnapshot.UserSnapshotPageQueryResponse;
import com.ruijie.rcos.rcdc.rco.module.impl.BusinessKey;
import com.ruijie.rcos.rcdc.rco.module.impl.connector.tcp.api.DeskSnapshotActionTcpApi;
import com.ruijie.rcos.rcdc.rco.module.impl.est.EstCommonActionNotifyService;
import com.ruijie.rcos.rcdc.rco.module.impl.est.EstCommonActionService;
import com.ruijie.rcos.rcdc.rco.module.impl.spi.common.LoginMessageCode;
import com.ruijie.rcos.rcdc.rco.module.impl.spi.struct.EstCommonActionResponse;
import com.ruijie.rcos.rcdc.rco.module.impl.spi.struct.EstCommonActionSubResponse;
import com.ruijie.rcos.rcdc.rco.module.impl.usersnapshot.UserSnapshotBusinessKey;
import com.ruijie.rcos.rcdc.rco.module.impl.usersnapshot.batchtask.CreateUserSnapshotBatchTaskHandler;
import com.ruijie.rcos.rcdc.rco.module.impl.usersnapshot.batchtask.DeleteUserSnapshotBatchTaskHandler;
import com.ruijie.rcos.rcdc.rco.module.impl.usersnapshot.batchtask.RecoverUserSnapshotBatchTaskHandler;
import com.ruijie.rcos.rcdc.rco.module.impl.usersnapshot.batchtask.UserSnapshotOperationListener;
import com.ruijie.rcos.rcdc.terminal.module.def.api.CbbSessionManagerAPI;
import com.ruijie.rcos.sk.base.batch.BatchTaskBuilder;
import com.ruijie.rcos.sk.base.batch.BatchTaskSubmitResult;
import com.ruijie.rcos.sk.base.batch.DefaultBatchTaskItem;
import com.ruijie.rcos.sk.base.exception.BusinessException;
import com.ruijie.rcos.sk.base.log.Logger;
import com.ruijie.rcos.sk.base.log.LoggerFactory;
import com.ruijie.rcos.sk.base.sm2.StateMachineFactory;
import com.ruijie.rcos.sk.base.validator.ValidatorUtil;
import com.ruijie.rcos.sk.connectkit.api.tcp.session.Session;
import com.ruijie.rcos.sk.pagekit.api.PageQueryBuilderFactory;
import com.ruijie.rcos.sk.pagekit.api.PageQueryResponse;
import com.ruijie.rcos.sk.pagekit.api.Sort;
import com.ruijie.rcos.sk.pagekit.kernel.request.criteria.DefaultSort;
import com.ruijie.rcos.sk.webmvc.api.request.IdArrWebRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;
import org.springframework.util.CollectionUtils;

import java.util.*;
import java.util.stream.Stream;

import static com.ruijie.rcos.rcdc.rco.module.common.dto.CommonConstants.PROTOCOL_VERSION;


/**
 * Description: Est common service类
 * Copyright: Copyright (c) 2021
 * Company: Ruijie Co., Ltd.
 * Create Time: 2021/3/31 11:42
 *
 * @author chenl
 */
@Service
public class EstCommonActionServiceImpl implements EstCommonActionService, UserSnapshotOperationListener {

    private static final Logger LOGGER = LoggerFactory.getLogger(EstCommonActionServiceImpl.class);

    @Autowired
    private PageQueryBuilderFactory pageQueryBuilderFactory;

    @Autowired
    private CbbVDIDeskSnapshotAPI cbbVDIDeskSnapshotAPI;

    @Autowired
    private IacAdminMgmtAPI baseAdminMgmtAPI;

    @Autowired
    private UserDesktopMgmtAPI userDesktopMgmtAPI;

    @Autowired
    private SnapshotManageAPI snapshotManageAPI;

    @Autowired
    private StateMachineFactory stateMachineFactory;

    @Autowired
    private BaseMsgMgmtAPI baseMsgMgmtAPI;

    @Autowired
    private IacUserMgmtAPI cbbUserAPI;

    @Autowired
    private EstCommonActionNotifyService estCommonActionNotifyService;

    @Autowired
    private CbbSessionManagerAPI cbbSessionManagerAPI;

    @Autowired
    private DeskSnapshotActionTcpApi deskSnapshotActionTcpApi;

    private static final String EQ_FIELD_DESK_ID = "deskId";

    private static final String SORT_FIELD_CREATE_TIME = "createTime";

    private static final int SNAPSHOT_NAME_MAX_LENGTH = 64;

    @Override
    public EstCommonActionResponse<UserSnapshotPageQueryResponse<CbbDeskSnapshotDTO>> snapshotList(String subAction, JSONObject data)
            throws BusinessException {
        Assert.hasText(subAction, "subAction is need not null");
        Assert.notNull(data, "data is need not null");
        // 将业务参数解析出来
        UserSnapshotListRequest snapshotListRequest = JSON.toJavaObject(data, UserSnapshotListRequest.class);
        Assert.notNull(snapshotListRequest.getDeskId(), "Est Client COMMON_ACTION:SNAPSHOT_LIST deskId is null");
        // 构造分页查询对象
        PageQueryBuilderFactory.RequestBuilder requestBuilder = generatePageQueryBuilder(snapshotListRequest);

        // 分页查询快照列表
        PageQueryResponse<CbbDeskSnapshotDTO> pageQueryResponse = cbbVDIDeskSnapshotAPI.pageQuery(requestBuilder.build());
        return generateListResult(subAction, pageQueryResponse, snapshotListRequest.getDeskId());

    }

    @Override
    public EstCommonActionResponse create(String terminalId, String subAction, JSONObject data, BatchTaskBuilder builder)
            throws BusinessException {

        Assert.hasText(terminalId, "terminalId is need not null");
        Assert.notNull(subAction, "subAction is need not null");
        Assert.notNull(data, "data is need not null");
        Assert.notNull(builder, "builder must not be null");

        // 将业务参数解析出来
        UserSnapshotCreateRequest snapshotCreateRequest = JSON.toJavaObject(data, UserSnapshotCreateRequest.class);
        Assert.notNull(snapshotCreateRequest, "snapshotCreateRequest must not be null");
        // 检测是否包含非法字符
        if (!ValidatorUtil.isTextName(snapshotCreateRequest.getName())) {
            return generateFailResult(subAction, UserSnapshotBusinessKey.RCDC_USER_SNAPSHOT_NAME_ILLEGAL_TIP, new String[]{},
                    snapshotCreateRequest.getDeskId());
        }
        // 检测名称是否超过64位
        if (snapshotCreateRequest.getName().length() > SNAPSHOT_NAME_MAX_LENGTH) {
            return generateFailResult(subAction, UserSnapshotBusinessKey.RCDC_USER_SNAPSHOT_NAME_TOO_LONG_TIP, new String[]{},
                    snapshotCreateRequest.getDeskId());
        }

        UUID userId = snapshotCreateRequest.getUserId();
        CbbDeskSnapshotUserType userType = CbbDeskSnapshotUserType.USER;
        try {
            baseAdminMgmtAPI.getAdmin(userId);
            userType = CbbDeskSnapshotUserType.ADMIN;
        } catch (Exception e) {
            LOGGER.info("用户 {} 不是管理员", userId);
        }

        // 不在这里检查超配，在执行创建时滚动创建（根据配置的最大快照数量）
        // 检查云桌面快照名称是否重复
        CloudDesktopDetailDTO cloudDesktopDetail = userDesktopMgmtAPI.getDesktopDetailById(snapshotCreateRequest.getDeskId());
        List<CloudDesktopDTO> cloudDesktopDTOList = userDesktopMgmtAPI.getAllDesktopByUserId(cloudDesktopDetail.getUserId());
        for (CloudDesktopDTO cloudDesktopDTO : cloudDesktopDTOList) {
            List<CbbDeskSnapshotDTO> cbbDeskSnapshotDTOList = cbbVDIDeskSnapshotAPI.listDeskSnapshotInfoByDeskId(cloudDesktopDTO.getId());
            for (CbbDeskSnapshotDTO cbbDeskSnapshotDTO : cbbDeskSnapshotDTOList) {
                if (cbbDeskSnapshotDTO.getName().equals(snapshotCreateRequest.getName())) {
                    return generateFailResult(subAction, UserSnapshotBusinessKey.RCDC_USER_SNAPSHOT_NAME_DUPLICATION_TIP, new String[]{},
                            snapshotCreateRequest.getDeskId());
                }
            }
        }

        UUID[] idArr = new UUID[]{snapshotCreateRequest.getDeskId()};
        Map<UUID, CloudDesktopDetailDTO> cloudDesktopDetailDTOMap = new HashMap<>();
        for (UUID deskId : idArr) {
            CloudDesktopDetailDTO cloudDesktopDetailDTO = userDesktopMgmtAPI.getDesktopDetailById(deskId);
            cloudDesktopDetailDTOMap.put(deskId, cloudDesktopDetailDTO);
        }
        final Iterator<DefaultBatchTaskItem> iterator =
                Stream.of(idArr).distinct().map(id -> DefaultBatchTaskItem.builder()//
                                .itemId(id).itemName(UserSnapshotBusinessKey.RCDC_USER_SNAPSHOT_CREATE_ITEM_NAME, new String[]{}).build())//
                        .iterator();


        CreateUserSnapshotBatchTaskHandler handler = new CreateUserSnapshotBatchTaskHandler(snapshotManageAPI, iterator);
        handler.setStateMachineFactory(stateMachineFactory);
        handler.setDeskSnapshotName(snapshotCreateRequest.getName());
        handler.setUserId(userId);
        handler.setUserType(userType);
        handler.setTerminalId(terminalId);
        handler.setUserSnapshotOperationListener(this);
        BatchTaskSubmitResult result = startCreateBatchTask(idArr, handler, builder);
        return generateBatchTaskSubmitResult(subAction, result, snapshotCreateRequest.getDeskId());
    }

    @Override
    public EstCommonActionResponse delete(String terminalId, String subAction, JSONObject data, BatchTaskBuilder builder)
            throws BusinessException {
        Assert.hasText(terminalId, "terminalId is need not null");
        Assert.notNull(subAction, "subAction is need not null");
        Assert.notNull(data, "data is need not null");
        Assert.notNull(builder, "builder must not be null");
        EstCommonActionResponse response;
        // 将业务参数解析出来
        IdArrWebRequest idArrWebRequest = JSON.toJavaObject(data, IdArrWebRequest.class);

        UUID[] idArr = idArrWebRequest.getIdArr();
        UUID deskId = null;
        for (UUID snapshotId : idArr) {
            // 快照不在被创建中、删除中、恢复中
            CbbDeskSnapshotDTO cbbDeskSnapshotDTO = findDeskSnapshotInfoById(snapshotId);
            deskId = cbbDeskSnapshotDTO.getDeskId();
            if (cbbDeskSnapshotDTO.getState() == CbbDeskSnapshotState.DELETING) {
                response = generateFailResult(subAction,
                        UserSnapshotBusinessKey.RCDC_USER_SNAPSHOT_DELETE_BY_DELETING, new String[]{cbbDeskSnapshotDTO.getName()}, deskId);
                return response;
            }
            if (cbbDeskSnapshotDTO.getState() == CbbDeskSnapshotState.CREATING) {
                response = generateFailResult(subAction,
                        UserSnapshotBusinessKey.RCDC_USER_SNAPSHOT_DELETE_BY_CREATING, new String[]{cbbDeskSnapshotDTO.getName()}, deskId);
                return response;
            }
            if (cbbDeskSnapshotDTO.getState() == CbbDeskSnapshotState.RESTORING) {
                response = generateFailResult(subAction,
                        UserSnapshotBusinessKey.RCDC_USER_SNAPSHOT_DELETE_BY_RESTORING, new String[]{cbbDeskSnapshotDTO.getName()}, deskId);
                return response;
            }
        }
        // 批量删除任务
        final Iterator<DefaultBatchTaskItem> iterator = Stream.of(idArr).distinct().map(id -> DefaultBatchTaskItem.builder().itemId(id)
                .itemName(UserSnapshotBusinessKey.RCDC_USER_SNAPSHOT_DELETE_ITEM_NAME, new String[]{}).build()).iterator();
        DeleteUserSnapshotBatchTaskHandler handler = new DeleteUserSnapshotBatchTaskHandler(snapshotManageAPI, iterator);
        handler.setDeskId(deskId);
        handler.setTerminalId(terminalId);
        handler.setUserSnapshotOperationListener(this);
        handler.setStateMachineFactory(stateMachineFactory);
        BatchTaskSubmitResult result = startDeleteBatchTask(idArr, handler, builder);

        response = generateBatchTaskSubmitResult(subAction, result, deskId);
        return response;
    }

    @Override
    public EstCommonActionResponse revert(String terminalId, String subAction, JSONObject data, BatchTaskBuilder builder)
            throws BusinessException {
        Assert.hasText(terminalId, "terminalId is need not null");
        Assert.notNull(subAction, "subAction is need not null");
        Assert.notNull(data, "data is need not null");
        Assert.notNull(builder, "builder must not be null");

        EstCommonActionResponse response;

        UserSnapshotRevertRequest userSnapshotRecoverRequest = JSON.toJavaObject(data, UserSnapshotRevertRequest.class);

        // 快照不在被创建中、删除中、恢复中
        CbbDeskSnapshotDTO cbbDeskSnapshotDTO = findDeskSnapshotInfoById(userSnapshotRecoverRequest.getId());
        if (cbbDeskSnapshotDTO.getState() == CbbDeskSnapshotState.DELETING) {
            response = generateFailResult(subAction,
                    UserSnapshotBusinessKey.RCDC_USER_SNAPSHOT_RECOVER_BY_DELETING, new String[]{cbbDeskSnapshotDTO.getName()},
                    cbbDeskSnapshotDTO.getDeskId());
            return response;
        }
        if (cbbDeskSnapshotDTO.getState() == CbbDeskSnapshotState.CREATING) {
            response = generateFailResult(subAction,
                    UserSnapshotBusinessKey.RCDC_USER_SNAPSHOT_RECOVER_BY_CREATING, new String[]{cbbDeskSnapshotDTO.getName()},
                    cbbDeskSnapshotDTO.getDeskId());
            return response;
        }
        if (cbbDeskSnapshotDTO.getState() == CbbDeskSnapshotState.RESTORING) {
            response = generateFailResult(subAction,
                    UserSnapshotBusinessKey.RCDC_USER_SNAPSHOT_RECOVER_BY_RESTORING, new String[]{cbbDeskSnapshotDTO.getName()},
                    cbbDeskSnapshotDTO.getDeskId());
            return response;
        }
        UUID[] idArr = new UUID[]{userSnapshotRecoverRequest.getId()};

        Map<UUID, Boolean> snapshotShutdownTypeMap = new HashMap<>();
        for (UUID deskId : idArr) {
            snapshotShutdownTypeMap.put(deskId, userSnapshotRecoverRequest.getForceShutdown());
        }
        final Iterator<DefaultBatchTaskItem> iterator = Stream.of(idArr).distinct().map(id -> DefaultBatchTaskItem.builder()//
                        .itemId(id).itemName(UserSnapshotBusinessKey.RCDC_USER_SNAPSHOT_RECOVER_ITEM_NAME, new String[]{}).build())//
                .iterator();

        RecoverUserSnapshotBatchTaskHandler handler = new RecoverUserSnapshotBatchTaskHandler(snapshotManageAPI, iterator);
        handler.setStateMachineFactory(stateMachineFactory);
        handler.setSnapshotShutdownTypeMap(snapshotShutdownTypeMap);
        handler.setDeskId(cbbDeskSnapshotDTO.getDeskId());
        handler.setTerminalId(terminalId);
        handler.setUserSnapshotOperationListener(this);
        BatchTaskSubmitResult result = startRecoverBatchTask(idArr, handler, builder);
        response = generateBatchTaskSubmitResult(subAction, result, cbbDeskSnapshotDTO.getDeskId());
        return response;
    }

    @Override
    public EstCommonActionResponse<BaseMsgDTO> detail(String subAction, JSONObject data) throws BusinessException {
        Assert.hasText(subAction, "subAction is need not null");
        Assert.notNull(data, "data is need not null");
        BaseGetMsgRequest baseGetMsgRequest = JSONObject.toJavaObject(data, BaseGetMsgRequest.class);
        Assert.notNull(baseGetMsgRequest.getMsgRelationId(), "msgRelationId is need not null");
        Assert.notNull(baseGetMsgRequest.getMsgType(), "msgType is need not null");
        BaseMsgDTO msg = baseMsgMgmtAPI.getMsg(baseGetMsgRequest);

        EstCommonActionResponse<BaseMsgDTO> estCommonActionResponse = new EstCommonActionResponse<>();
        estCommonActionResponse.setSubAction(subAction);
        estCommonActionResponse.setData(EstCommonActionSubResponse.success(msg));
        return estCommonActionResponse;
    }

    @Override
    public EstCommonActionResponse checkUserPwd(String subAction, JSONObject data) throws BusinessException {

        Assert.hasText(subAction, "subAction is need not null");
        Assert.notNull(data, "data can not be null");
        String userName = data.getString("userName");
        String password = data.getString("password");
        UUID deskId = (UUID) data.get("deskId");
        IacAuthUserDTO cbbAuthUserDTO = new IacAuthUserDTO(userName, password);
        // 非登录认证
        cbbAuthUserDTO.setIsLogin(false);
        // 忽略图形校验码和设备id传参
        cbbAuthUserDTO.setDeviceId(UUID.randomUUID().toString());
        cbbAuthUserDTO.setShouldCheckCaptchaCode(false);
        cbbAuthUserDTO.setSubSystem(SubSystem.CDC);
        IacAuthUserResultDTO resultDTO = cbbUserAPI.authUser(cbbAuthUserDTO);
        EstCommonActionResponse response;
        if (resultDTO.getAuthCode() == LoginMessageCode.SUCCESS) {
            // 用户密码验证成功
            response = generateSuccessResult(subAction, deskId);
        } else if (resultDTO.getAuthCode() == LoginMessageCode.USERNAME_OR_PASSWORD_ERROR) {
            // 密码验证失败
            response = generateFailResult(subAction, BusinessKey.RCDC_USER_SELF_OPERATION_PWD_CONFIRM_PWD_ERROR, new String[]{}, deskId);
        } else if (resultDTO.getAuthCode() == LoginMessageCode.AD_SERVER_ERROR) {
            response = generateFailResult(subAction, BusinessKey.RCDC_USER_SELF_OPERATION_PWD_CONFIRM_AD_ERROR, new String[]{}, deskId);
        } else if (resultDTO.getAuthCode() == LoginMessageCode.AD_ACCOUNT_DISABLE) {
            response = generateFailResult(subAction, BusinessKey.RCDC_USER_SELF_OPERATION_PWD_CONFIRM_USER_FORBIDDEN, new String[]{}, deskId);
        } else if (resultDTO.getAuthCode() == LoginMessageCode.AD_LOGIN_LIMIT) {
            response = generateFailResult(subAction, BusinessKey.RCDC_USER_SELF_OPERATION_PWD_CONFIRM_LOGIN_LIMIT, new String[]{}, deskId);
        } else if (resultDTO.getAuthCode() == LoginMessageCode.AD_ACCOUNT_EXPIRE) {
            response = generateFailResult(subAction, BusinessKey.RCDC_USER_SELF_OPERATION_PWD_CONFIRM_AD_ACCOUNT_EXPIRED, new String[]{}, deskId);
        } else if (resultDTO.getAuthCode() == LoginMessageCode.LDAP_SERVER_ERROR) {
            response = generateFailResult(subAction, BusinessKey.RCDC_USER_SELF_OPERATION_PWD_CONFIRM_LDAP_ERROR, new String[]{}, deskId);
        } else if (resultDTO.getAuthCode() == LoginMessageCode.USER_LOCKED) {
            response = generateFailResult(subAction, BusinessKey.RCDC_USER_SELF_OPERATION_PWD_CONFIRM_USER_LOCKED, new String[]{}, deskId);
        } else if (resultDTO.getAuthCode() == LoginMessageCode.REMIND_ERROR_TIMES) {
            response = generateFailResult(subAction, BusinessKey.RCDC_USER_SELF_OPERATION_PWD_CONFIRM_PWD_ERROR_WITH_REMIND_TIMES,
                    new String[]{resultDTO.getErrorMsg()}, deskId);
        } else {
            response = generateFailResult(subAction, BusinessKey.RCDC_USER_SELF_OPERATION_PWD_CONFIRM_OTHER_ERROR, new String[]{}, deskId);
        }
        return response;
    }

    @Override
    public EstCommonActionResponse<CbbDeskSnapshotDistributeDTO> checkNum(String subAction, JSONObject data)
            throws BusinessException {
        Assert.hasText(subAction, "subAction can not be empty");
        Assert.notNull(data, "data can not be null");
        UUID deskId = (UUID) data.get("deskId");
        CbbDeskSnapshotDistributeDTO cbbDeskSnapshotDistributeDTO = cbbVDIDeskSnapshotAPI.getCurrentSnapshotSituation(deskId);
        return generateCheckNumResult(subAction, deskId, cbbDeskSnapshotDistributeDTO);
    }

    @Override
    public void onFinish(String actionCode, String terminalId, UUID deskId) {
        Assert.hasText(actionCode, "actionCode can not be empty");
        Assert.hasText(terminalId, "terminal can not be empty");
        Assert.notNull(deskId, "deskId can not be null");
        try {
            EstCommonActionResponse<String> response = generateSuccessResult(actionCode, deskId);
            estCommonActionNotifyService.responseToEst(terminalId, actionCode,
                    JSON.toJSONString(response, SerializerFeature.WriteDateUseDateFormat));
        } catch (BusinessException e) {
            LOGGER.error("用户自助快照操作完成[{}-{}-{}-{}]回调异常：", actionCode, terminalId, deskId, e);
        }
    }

    @Override
    public void onException(String actionCode, String terminalId, UUID deskId, @Nullable String msg) {
        Assert.hasText(actionCode, "actionCode can not be empty");
        Assert.hasText(terminalId, "terminal can not be empty");
        Assert.notNull(deskId, "deskId can not be null");
        try {
            Session session = cbbSessionManagerAPI.getSessionByAlias(terminalId);
            if (session != null && session.getAttribute(PROTOCOL_VERSION) != null) {
                deskSnapshotActionTcpApi.snapshotNotify(terminalId,
                        generateFailResult(actionCode, deskId, msg));
            } else {
                estCommonActionNotifyService.responseToEst(terminalId, actionCode, failWithMsg(actionCode, deskId, msg));
            }
        } catch (BusinessException e) {
            LOGGER.error("用户自助快照操作异常[{}-{}-{}-{}]回调异常：", actionCode, terminalId, deskId, msg, e);
        }
    }


    /**
     * 构造快照分页查询对象
     *
     * @param snapshotListRequest est透过Shine传入的业务参数
     * @return 快照分页查询生成器
     */
    private PageQueryBuilderFactory.RequestBuilder generatePageQueryBuilder(UserSnapshotListRequest snapshotListRequest) {
        PageQueryBuilderFactory.RequestBuilder requestBuilder = pageQueryBuilderFactory.newRequestBuilder();
        Integer limit = snapshotListRequest.getLimit();
        if (limit == null) {
            limit = 100;
        }
        Integer page = snapshotListRequest.getPage();
        if (page == null) {
            page = 0;
        }
        requestBuilder.setPageLimit(page, limit);
        requestBuilder.eq(EQ_FIELD_DESK_ID, snapshotListRequest.getDeskId());
        Sort[] sortArr = snapshotListRequest.getSortArr();
        if (sortArr != null && sortArr.length > 0) {
            for (Sort sort : sortArr) {
                switch (sort.getDirection()) {
                    case ASC:
                        requestBuilder.asc(sort.getFieldName());
                        break;
                    case DESC:
                        requestBuilder.desc(sort.getFieldName());
                        break;
                }
            }
        } else {
            requestBuilder.desc(SORT_FIELD_CREATE_TIME);
        }
        return requestBuilder;
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

    private EstCommonActionResponse<String> generateFailResult(String subAction, String key, String[] keyArgs, UUID deskId) {
        EstCommonActionResponse<String> response = new EstCommonActionResponse<>();
        response.setSubAction(subAction);
        response.setData(EstCommonActionSubResponse.fail(key, keyArgs));
        response.setDeskId(deskId);
        return response;
    }

    private CbbDeskSnapshotDTO findDeskSnapshotInfoById(UUID deskSnapshotId) throws BusinessException {
        CbbDeskSnapshotDTO cbbDeskSnapshotDTO = cbbVDIDeskSnapshotAPI.findDeskSnapshotInfoById(deskSnapshotId);
        if (cbbDeskSnapshotDTO == null) {
            throw new BusinessException(UserSnapshotBusinessKey.RCDC_USER_SNAPSHOT_IS_NULL, deskSnapshotId.toString());
        }
        return cbbDeskSnapshotDTO;
    }

    private BatchTaskSubmitResult startCreateBatchTask(UUID[] idArr, CreateUserSnapshotBatchTaskHandler handler, BatchTaskBuilder builder)
            throws BusinessException {
        // 创建单条用户
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

    private EstCommonActionResponse<BatchTaskSubmitResult> generateBatchTaskSubmitResult(String subAction,
                                                                                         BatchTaskSubmitResult result, UUID deskId) {
        EstCommonActionResponse<BatchTaskSubmitResult> estCommonActionResponse = new EstCommonActionResponse<>();
        estCommonActionResponse.setSubAction(subAction);
        estCommonActionResponse.setData(EstCommonActionSubResponse.success(result));
        estCommonActionResponse.setDeskId(deskId);
        return estCommonActionResponse;
    }

    private EstCommonActionResponse<String> generateSuccessResult(String subAction, UUID deskId) {
        EstCommonActionResponse<String> response = new EstCommonActionResponse<>();
        response.setSubAction(subAction);
        response.setData(EstCommonActionSubResponse.success());
        response.setDeskId(deskId);
        return response;
    }

    private String failWithMsg(String subAction, UUID deskId, String msg) {
        EstCommonActionResponse<String> response = generateFailResult(subAction, deskId, msg);
        return JSON.toJSONString(response, SerializerFeature.WriteDateUseDateFormat);
    }


    private EstCommonActionResponse<String> generateFailResult(String subAction, UUID deskId, String msg) {
        EstCommonActionResponse<String> response = new EstCommonActionResponse<>();
        response.setSubAction(subAction);
        response.setData(EstCommonActionSubResponse.failWithMsg(msg));
        response.setDeskId(deskId);
        return response;
    }

    private EstCommonActionResponse<CbbDeskSnapshotDistributeDTO> generateCheckNumResult(String subAction, UUID deskId,
                                                                                         CbbDeskSnapshotDistributeDTO content) {
        EstCommonActionResponse<CbbDeskSnapshotDistributeDTO> response = new EstCommonActionResponse<>();
        response.setSubAction(subAction);
        response.setData(EstCommonActionSubResponse.success(content));
        response.setDeskId(deskId);
        return response;
    }

    private EstCommonActionResponse<UserSnapshotPageQueryResponse<CbbDeskSnapshotDTO>> generateListResult(String subAction,
                PageQueryResponse<CbbDeskSnapshotDTO> pageQueryResponse, UUID deskId)
            throws BusinessException {
        // 构造快照列表返回对象
        UserSnapshotPageQueryResponse<CbbDeskSnapshotDTO> response = new UserSnapshotPageQueryResponse();
        CbbDeskSnapshotDTO[] deskSnapshotArr = pageQueryResponse.getItemArr();
        response.setItemArr(deskSnapshotArr);
        response.setTotal(pageQueryResponse.getTotal());
        if (deskSnapshotArr != null && deskSnapshotArr.length > 0) {
            CloudDesktopDetailDTO cloudDesktopDetailDTO = userDesktopMgmtAPI.getDesktopDetailById(deskSnapshotArr[0].getDeskId());
            response.setDesktopState(cloudDesktopDetailDTO.getDesktopState());
        }
        EstCommonActionResponse<UserSnapshotPageQueryResponse<CbbDeskSnapshotDTO>> estCommonActionResponse = new EstCommonActionResponse<>();
        estCommonActionResponse.setSubAction(subAction);
        estCommonActionResponse.setData(EstCommonActionSubResponse.success(response));
        estCommonActionResponse.setDeskId(deskId);

        return estCommonActionResponse;
    }
}
