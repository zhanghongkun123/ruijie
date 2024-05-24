package com.ruijie.rcos.rcdc.rco.module.web.ctrl.user;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.UUID;
import java.util.stream.Stream;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.RequestMapping;

import com.ruijie.rcos.gss.base.iac.module.annotation.EnableAuthority;
import com.ruijie.rcos.gss.log.module.def.api.BaseAuditLogAPI;
import com.ruijie.rcos.gss.sdk.iac.module.def.api.IacUserGroupMgmtAPI;
import com.ruijie.rcos.gss.sdk.iac.module.def.api.IacUserMgmtAPI;
import com.ruijie.rcos.rcdc.rco.module.def.api.UserMessageAPI;
import com.ruijie.rcos.rcdc.rco.module.def.api.dto.UserMessageDTO;
import com.ruijie.rcos.rcdc.rco.module.def.api.request.CreateGroupUserMessageRequest;
import com.ruijie.rcos.rcdc.rco.module.def.api.request.CreateMessageRequest;
import com.ruijie.rcos.rcdc.rco.module.def.api.request.CreateUserMessageRequest;
import com.ruijie.rcos.rcdc.rco.module.def.api.request.PageSearchRequest;
import com.ruijie.rcos.rcdc.rco.module.def.api.response.CreateUserMessageResponse;
import com.ruijie.rcos.rcdc.rco.module.web.config.annotation.ApiVersion;
import com.ruijie.rcos.rcdc.rco.module.web.config.annotation.ApiVersions;
import com.ruijie.rcos.rcdc.rco.module.web.config.annotation.Version;
import com.ruijie.rcos.rcdc.rco.module.web.ctrl.user.bactchtask.DeleteUserMessageBatchTaskHandler;
import com.ruijie.rcos.rcdc.rco.module.web.ctrl.user.bactchtask.UserGroupBatchTaskItem;
import com.ruijie.rcos.rcdc.rco.module.web.ctrl.user.bactchtask.UserGroupSendMsgBatchTaskHandler;
import com.ruijie.rcos.rcdc.rco.module.web.ctrl.user.dto.IdLabelStringEntry;
import com.ruijie.rcos.rcdc.rco.module.web.ctrl.user.enums.UserMessageObjTypeEnum;
import com.ruijie.rcos.rcdc.rco.module.web.ctrl.user.request.CreateUserMessageWebRequest;
import com.ruijie.rcos.rcdc.rco.module.web.ctrl.user.request.IdArrWebRequest;
import com.ruijie.rcos.rcdc.rco.module.web.ctrl.user.vo.CreateUserMsgResultVO;
import com.ruijie.rcos.sk.base.batch.BatchTaskBuilder;
import com.ruijie.rcos.sk.base.batch.BatchTaskSubmitResult;
import com.ruijie.rcos.sk.base.batch.DefaultBatchTaskItem;
import com.ruijie.rcos.sk.base.exception.BusinessException;
import com.ruijie.rcos.sk.base.i18n.LocaleI18nResolver;
import com.ruijie.rcos.sk.base.log.Logger;
import com.ruijie.rcos.sk.base.log.LoggerFactory;
import com.ruijie.rcos.sk.base.validation.EnableCustomValidate;
import com.ruijie.rcos.sk.modulekit.api.comm.DefaultPageResponse;
import com.ruijie.rcos.sk.modulekit.api.comm.IdRequest;
import com.ruijie.rcos.sk.webmvc.api.request.PageWebRequest;
import com.ruijie.rcos.sk.webmvc.api.response.DefaultWebResponse;

import io.swagger.annotations.ApiOperation;

/**
 * Description: 用户消息
 * Copyright: Copyright (c) 2018
 * Company: Ruijie Co., Ltd.
 * Create Time: 2018/11/26
 *
 * @author Jarman
 */
@Controller
@RequestMapping("/rco/user/message")
@EnableCustomValidate(enable = false)
public class UserMessageController {

    @Autowired
    private UserMessageAPI userMessageAPI;

    @Autowired
    private BaseAuditLogAPI auditLogAPI;

    @Autowired
    private IacUserMgmtAPI iacUserMgmtAPI;

    @Autowired
    private IacUserGroupMgmtAPI iacUserGroupMgmtAPI;
    
    private static final Logger LOGGER = LoggerFactory.getLogger(UserMessageController.class);

    /**
     * 获取用户消息记录
     *
     * @param request 请求参数对象
     * @return 返回消息记录列表
     * @throws BusinessException 业务异常
     */
    @ApiOperation("消息记录")
    @RequestMapping(value = "/list")
    public DefaultWebResponse getMessageList(PageWebRequest request) {
        Assert.notNull(request, "PageWebRequest不能为null");
        PageSearchRequest apiRequest = new PageSearchRequest(request);
        DefaultPageResponse<UserMessageDTO> pageResponse = userMessageAPI.queryUserMessage(apiRequest);
        return DefaultWebResponse.Builder.success(pageResponse);
    }

    /**
     * 创建并发送用户消息
     *
     * @param request        请求参数对象
     * @return 批量任务执行结果
     * @throws BusinessException 业务异常
     */
    @ApiOperation("发送消息")
    @ApiVersions({@ApiVersion(value = Version.V1_0_0), @ApiVersion(value = Version.V3_1_1, descriptions = {"分级分权-区分权限，加入权限控制"})})
    @RequestMapping(value = "create")
    @EnableAuthority
    public DefaultWebResponse createMessage(CreateUserMessageWebRequest request) throws BusinessException {
        Assert.notNull(request, "CreateUserMessageRequest 不能为null");
        LOGGER.debug("接收到的参数：{}", request.toString());

        return createUserMessageAddOptLog(request);
    }


    /**
     * 创建并发送用户组消息
     *
     * @param request        请求参数对象
     * @param builder 批处理任务
     * @return 批量任务执行结果
     * @throws BusinessException 业务异常
     */
    @ApiOperation("发送消息")
    @ApiVersions({@ApiVersion(value = Version.V1_0_0), @ApiVersion(value = Version.V3_1_1, descriptions = {"分级分权-区分权限，加入权限控制"})})
    @RequestMapping(value = "userGroupMessage/create")
    @EnableAuthority
    public DefaultWebResponse sendUserGroupMessage(CreateUserMessageWebRequest request, BatchTaskBuilder builder) throws BusinessException {
        Assert.notNull(request, "CreateUserMessageRequest 不能为null");
        Assert.notNull(builder, "builder 不能为null");

        LOGGER.debug("接收到的参数：{}", request.toString());
        return getBatchCreateMsgResponse(request, builder);

    }

    private DefaultWebResponse getBatchCreateMsgResponse(CreateUserMessageWebRequest request,
                                                         BatchTaskBuilder builder) throws BusinessException {
        // 创建信息
        CreateMessageRequest createMessageRequest = new CreateMessageRequest(request.getTitle(), request.getContent());
        UserMessageDTO messageDTO = userMessageAPI.createMessage(createMessageRequest);
        List<UserGroupBatchTaskItem> taskItemList = buildTaskItems(request);
        UserGroupSendMsgBatchTaskHandler handler =
                new UserGroupSendMsgBatchTaskHandler(taskItemList.iterator(), auditLogAPI, userMessageAPI, iacUserMgmtAPI);
        handler.setUserMessageDTO(messageDTO);
        handler.setIacUserGroupMgmtAPI(iacUserGroupMgmtAPI);
        BatchTaskSubmitResult result = builder.setTaskName(UserBusinessKey.RCDC_RCO_USER_GROUP_SEND_MESSAGE_TASK_NAME)//
            .setTaskDesc(UserBusinessKey.RCDC_RCO_USER_GROUP_SEND_MESSAGE_ITEM_DESC).enableParallel()//
            .registerHandler(handler)//
            .start();

        return DefaultWebResponse.Builder.success(result);
    }

    private List<UserGroupBatchTaskItem> buildTaskItems(CreateUserMessageWebRequest request) {
        String taskName = LocaleI18nResolver.resolve(UserBusinessKey.RCDC_RCO_USER_GROUP_SEND_MESSAGE_ITEM_NAME, new String[]{});
        UUID[] groupIdArr = extractId(request.getSendingObjArr());
        List<UserGroupBatchTaskItem> taskItemList = new ArrayList<>(groupIdArr.length);
        for (UUID groupId : groupIdArr) {
            UserGroupBatchTaskItem taskItem = new UserGroupBatchTaskItem(groupId, taskName);
            taskItem.setGroupId(groupId);
            taskItemList.add(taskItem);
        }
        return taskItemList;
    }

    private DefaultWebResponse createUserMessageAddOptLog(CreateUserMessageWebRequest request) throws BusinessException {
        final String messageTitle = request.getTitle();
        try {
            final UUID messageId = createUserMsg(request);
            auditLogAPI.recordLog(UserBusinessKey.RCDC_RCO_USER_CREATE_USER_MESSAGE_SUCCESS_LOG, messageTitle);
            return DefaultWebResponse.Builder.success(UserBusinessKey.RCDC_RCO_USER_CREATE_USER_MESSAGE_SUCCESS,
                    new String[]{messageTitle}, new CreateUserMsgResultVO(messageId));
        } catch (BusinessException ex) {
            auditLogAPI.recordLog(UserBusinessKey.RCDC_RCO_USER_CREATE_USER_MESSAGE_FAIL_LOG, ex, messageTitle,
                    ex.getI18nMessage());
            throw new BusinessException(UserBusinessKey.RCDC_RCO_USER_CREATE_USER_MESSAGE_FAIL, ex, messageTitle, ex.getI18nMessage());
        }

    }

    private UUID createUserMsg(CreateUserMessageWebRequest request) throws BusinessException {
        UserMessageObjTypeEnum type = request.getType();
        UUID[] objIdArr = extractId(request.getSendingObjArr());
        String messageTitle = request.getTitle();
        String messageContent = request.getContent();

        UUID messageId = null;
        switch (type) {
            case USER:
                CreateUserMessageRequest userRequest = new CreateUserMessageRequest(objIdArr, messageTitle, messageContent);
                final CreateUserMessageResponse resp = userMessageAPI.createUserMessage(userRequest);
                messageId = resp.getMessageId();
                break;
            case GROUP:
                CreateGroupUserMessageRequest groupRequest = new CreateGroupUserMessageRequest(objIdArr, messageTitle, messageContent);
                final CreateUserMessageResponse groupResp = userMessageAPI.createGroupUserMessage(groupRequest);
                messageId = groupResp.getMessageId();
                break;
            default:
                break;
        }
        return messageId;
    }

    private UUID[] extractId(IdLabelStringEntry[] objArr) {
        UUID[] idArr = new UUID[objArr.length];
        for (int i = 0; i < objArr.length; i++) {
            idArr[i] = UUID.fromString(objArr[i].getId());
        }
        return idArr;
    }

    /**
     * 批量删除用户消息记录
     *
     * @param request        请求参数对象
     * @param builder        批量任务对象
     * @return 批量任务执行结果
     * @throws BusinessException 业务异常
     */
    @ApiOperation("删除消息记录")
    @RequestMapping(value = "delete")
    public DefaultWebResponse delete(IdArrWebRequest request, BatchTaskBuilder builder) throws BusinessException {
        Assert.notNull(request, "request can not be null");
        Assert.notNull(builder, "builder can not be null");

        UUID[] idArr = request.getIdArr();
        if (idArr.length == 1) {
            return deleteSingleUserMsg(idArr[0]);
        }
        final Iterator<DefaultBatchTaskItem> iterator = Stream.of(idArr).distinct().map(id -> DefaultBatchTaskItem.builder().itemId(id)
                .itemName(UserBusinessKey.RCDC_RCO_USER_DELETE_USER_MESSAGE_ITEM_NAME, new String[]{}).build()).iterator();

        final DeleteUserMessageBatchTaskHandler handler = new DeleteUserMessageBatchTaskHandler(userMessageAPI, iterator, auditLogAPI);
        BatchTaskSubmitResult result = builder.setTaskName(UserBusinessKey.RCDC_RCO_USER_DELETE_USER_MESSAGE_TASK_NAME)
                .setTaskDesc(UserBusinessKey.RCDC_RCO_USER_DELETE_USER_MESSAGE_ITEM_DESC).enableParallel().registerHandler(handler).start();
        return DefaultWebResponse.Builder.success(result);
    }

    private DefaultWebResponse deleteSingleUserMsg(UUID msgId) throws BusinessException {

        String msgTitle = msgId.toString();
        try {
            IdRequest idRequest = new IdRequest(msgId);
            UserMessageDTO userMessageDTO = userMessageAPI.getById(idRequest);
            msgTitle = userMessageDTO.getTitle();
            userMessageAPI.deleteUserMessage(idRequest);
            auditLogAPI.recordLog(UserBusinessKey.RCDC_RCO_USER_DELETE_USER_MESSAGE_SUCCESS_LOG,
                    msgTitle);
            return DefaultWebResponse.Builder.success(UserBusinessKey.RCDC_RCO_USER_DELETE_USER_MESSAGE_SUCCESS, new String[]{});
        } catch (BusinessException e) {
            LOGGER.error("delete user message fail", e);
            auditLogAPI.recordLog(UserBusinessKey.RCDC_RCO_USER_DELETE_USER_MESSAGE_FAIL_LOG, e,
                    msgTitle, e.getI18nMessage());
            throw new BusinessException(UserBusinessKey.RCDC_RCO_USER_DELETE_USER_MESSAGE_FAIL, e, e.getI18nMessage());
        }
    }


}
