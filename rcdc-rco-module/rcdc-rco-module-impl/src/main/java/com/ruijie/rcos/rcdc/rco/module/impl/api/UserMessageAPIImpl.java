package com.ruijie.rcos.rcdc.rco.module.impl.api;

import java.util.Arrays;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.ExecutorService;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.util.Assert;
import org.springframework.util.CollectionUtils;

import com.ruijie.rcos.gss.sdk.iac.module.def.api.IacUserMgmtAPI;
import com.ruijie.rcos.gss.sdk.iac.module.def.api.response.IacPageResponse;
import com.ruijie.rcos.gss.sdk.iac.module.def.dto.user.IacQueryUserListPageDTO;
import com.ruijie.rcos.gss.sdk.iac.module.def.dto.user.IacUserDetailDTO;
import com.ruijie.rcos.gss.sdk.iac.module.def.enums.IacUserTypeEnum;
import com.ruijie.rcos.rcdc.rco.module.def.api.UserMessageAPI;
import com.ruijie.rcos.rcdc.rco.module.def.api.dto.UserMessageDTO;
import com.ruijie.rcos.rcdc.rco.module.def.api.request.CreateGroupUserMessageRequest;
import com.ruijie.rcos.rcdc.rco.module.def.api.request.CreateMessageRequest;
import com.ruijie.rcos.rcdc.rco.module.def.api.request.CreateUserMessageRequest;
import com.ruijie.rcos.rcdc.rco.module.def.api.request.PageSearchRequest;
import com.ruijie.rcos.rcdc.rco.module.def.api.request.SaveUserMessageRequest;
import com.ruijie.rcos.rcdc.rco.module.def.api.response.CreateUserMessageResponse;
import com.ruijie.rcos.rcdc.rco.module.def.constants.Constants;
import com.ruijie.rcos.rcdc.rco.module.impl.BusinessKey;
import com.ruijie.rcos.rcdc.rco.module.impl.dao.RcoViewUserDAO;
import com.ruijie.rcos.rcdc.rco.module.impl.dao.UserMessageDAO;
import com.ruijie.rcos.rcdc.rco.module.impl.dao.UserMessageUserDAO;
import com.ruijie.rcos.rcdc.rco.module.impl.dao.ViewDesktopDetailDAO;
import com.ruijie.rcos.rcdc.rco.module.impl.entity.UserMessageEntity;
import com.ruijie.rcos.rcdc.rco.module.impl.entity.UserMessageUserEntity;
import com.ruijie.rcos.rcdc.rco.module.impl.entity.ViewUserDesktopEntity;
import com.ruijie.rcos.rcdc.rco.module.impl.enums.UserMessageStateEnum;
import com.ruijie.rcos.rcdc.rco.module.impl.service.UserMessageService;
import com.ruijie.rcos.rcdc.rco.module.impl.service.impl.QueryUserMessageService;
import com.ruijie.rcos.rcdc.rco.module.impl.tx.UserMessageServiceTx;
import com.ruijie.rcos.sk.base.concurrent.ThreadExecutors;
import com.ruijie.rcos.sk.base.exception.BusinessException;
import com.ruijie.rcos.sk.base.log.Logger;
import com.ruijie.rcos.sk.base.log.LoggerFactory;
import com.ruijie.rcos.sk.modulekit.api.comm.DefaultPageResponse;
import com.ruijie.rcos.sk.modulekit.api.comm.IdRequest;

/**
 * Description: Function Description
 * Copyright: Copyright (c) 2018
 * Company: Ruijie Co., Ltd.
 * Create Time: 2018/11/25
 *
 * @author Jarman
 */
public class UserMessageAPIImpl implements UserMessageAPI {

    private static final Logger LOGGER = LoggerFactory.getLogger(UserMessageAPIImpl.class);

    private static final ExecutorService SINGLE_THREAD_EXECUTOR =
            ThreadExecutors.newBuilder(UserMessageAPIImpl.class.getName()).maxThreadNum(1).queueSize(10).build();

    private static final ExecutorService SEND_USER_MSG_THREAD_POOL =
            ThreadExecutors.newBuilder("sendUserMsgSingleThreadPool").maxThreadNum(20).queueSize(10).build();

    @Autowired
    private QueryUserMessageService queryUserMessageService;

    @Autowired
    private RcoViewUserDAO rcoViewUserDAO;

    @Autowired
    private UserMessageDAO userMessageDAO;

    @Autowired
    private UserMessageUserDAO userMessageUserDAO;

    @Autowired
    private UserMessageServiceTx userMessageServiceTx;

    @Autowired
    private UserMessageService userMessageService;

    @Autowired
    private ViewDesktopDetailDAO viewDesktopDetailDAO;

    @Autowired
    private IacUserMgmtAPI iacUserMgmtAPI;

    @Override
    public DefaultPageResponse<UserMessageDTO> queryUserMessage(PageSearchRequest request) {
        Assert.notNull(request, "PageSearchRequest 不能为null");
        Page<UserMessageEntity> page = queryUserMessageService.pageQuery(request, UserMessageEntity.class);
        List<UserMessageEntity> entityList = page.getContent();
        UserMessageDTO[] dtoArr = new UserMessageDTO[entityList.size()];
        for (int i = 0; i < entityList.size(); i++) {
            UserMessageEntity entity = entityList.get(i);
            UserMessageDTO dto = UserMessageEntity.convertFor(entity);
            completeInfo(dto);
            dtoArr[i] = dto;
        }
        DefaultPageResponse<UserMessageDTO> response = new DefaultPageResponse<>();
        response.setItemArr(dtoArr);
        response.setTotal(page.getTotalElements());
        return response;
    }

    @Override
    public UserMessageDTO getById(IdRequest request) throws BusinessException {
        Assert.notNull(request, "request 不能为null");
        UserMessageEntity userMessageEntity = userMessageDAO.getOne(request.getId());
        if (userMessageEntity == null) {
            throw new BusinessException(com.ruijie.rcos.rcdc.rco.module.def.BusinessKey.RCDC_USER_USER_MESSAGE_NOT_EXIST);
        }

        UserMessageDTO msgDTO = UserMessageEntity.convertFor(userMessageEntity);
        completeInfo(msgDTO);

        return msgDTO;
    }

    private void completeInfo(UserMessageDTO dto) {
        int unsendNum = userMessageUserDAO.countByMessageIdAndState(dto.getId(), UserMessageStateEnum.UNSEND);
        int unreadNum = userMessageUserDAO.countByMessageIdAndState(dto.getId(), UserMessageStateEnum.UNREAD);
        int readNum = userMessageUserDAO.countByMessageIdAndState(dto.getId(), UserMessageStateEnum.READ);
        dto.setUnsendNum(unsendNum);
        dto.setUnreadNum(unreadNum);
        dto.setReadNum(readNum);
    }

    @Override
    public CreateUserMessageResponse createGroupUserMessage(CreateGroupUserMessageRequest request) throws BusinessException {
        Assert.notNull(request, "request 不能为null");

        // 按单个组分页查，用户数太多引发OOM
        UUID[] groupIdArr = request.getGroupIdArr();
        // 已创建则不重复创建
        UserMessageEntity msgEntity = null;
        for (UUID groupId : groupIdArr) {
            IacPageResponse<IacUserDetailDTO> pageResult = pageQueryByGroupId(groupId, 0);
            // 总页数
            int totalPage = (int) Math.ceil((double) pageResult.getTotal() / Constants.MAX_QUERY_LIST_SIZE);
            for (int page = 0; page < totalPage; page++) {
                if (msgEntity == null) {
                    // 创建消息
                    msgEntity = userMessageService.createUserMessage(request.getMessageTitle(), request.getMessageContent());
                }
                // 前面已查过，不重复查
                if (page == 0) {
                    // 创建用户消息并发送
                    saveAndSendMessage(msgEntity, pageResult.getItemArr());
                    continue;
                }
                pageResult = pageQueryByGroupId(groupId, page);
                if (pageResult.getTotal() == 0) {
                    break;
                }
                saveAndSendMessage(msgEntity, pageResult.getItemArr());
            }
        }
        if (msgEntity == null) {
            throw new BusinessException(BusinessKey.RCDC_USER_MESSAGE_USER_GROUP_NO_USER);
        }

        return new CreateUserMessageResponse(msgEntity.getId());
    }
    
    private void saveAndSendMessage(UserMessageEntity msgEntity, IacUserDetailDTO[] userDetailArr) throws BusinessException {
        for (IacUserDetailDTO iacUserDetailDTO : userDetailArr) {
            userMessageService.saveUserJoinMessage(msgEntity, iacUserDetailDTO);
            // 发送消息给桌面
            sendUserMessageToVm(msgEntity);
        }
    }
    
    private void sendUserMessageToVm(final UserMessageEntity msgEntity) {
        // 调用vmmessage接口，发送消息给桌面
        SINGLE_THREAD_EXECUTOR.execute(() -> sendMsgToVm(msgEntity));
    }

    private IacPageResponse<IacUserDetailDTO> pageQueryByGroupId(UUID groupId, int page) throws BusinessException {
        IacQueryUserListPageDTO pageDTO = new IacQueryUserListPageDTO();
        pageDTO.setGroupId(groupId);
        pageDTO.setPage(page);
        pageDTO.setLimit(Constants.MAX_QUERY_LIST_SIZE);
        return iacUserMgmtAPI.pageQueryUserListByGroupId(pageDTO);
    }

    @Override
    public CreateUserMessageResponse createUserMessage(CreateUserMessageRequest request) throws BusinessException {
        Assert.notNull(request, "CreateUserMessageRequest 不能为null");

        // 创建用户消息
        UUID[] userIdArr = request.getUserIdArr();
        String messageTitle = request.getMessageTitle();
        String messageContent = request.getMessageContent();
        List<UUID> userIdList = Arrays.stream(userIdArr).filter(userId -> userId != null).collect(Collectors.toList());
        // 保存消息
        UserMessageEntity msgEntity = userMessageService.createUserMessage(messageTitle, messageContent);
        // 接口要求一次限制最多查1000条，分批查
        List<List<UUID>> userIdListGroupList = userIdList.stream()//
            .collect(Collectors.groupingBy(e -> (userIdList.indexOf(e) / Constants.MAX_QUERY_LIST_SIZE)))//
            .values().stream()//
            .collect(Collectors.toList());
        for (List<UUID> idList : userIdListGroupList) {
            List<IacUserDetailDTO> userDetailList = iacUserMgmtAPI.listUserByUserIds(idList);
            for (IacUserDetailDTO iacUserDetailDTO : userDetailList) {
                // 创建关联用户表数据
                userMessageService.saveUserJoinMessage(msgEntity,iacUserDetailDTO);
            }
        }
        // 调用vmmessage接口，发送消息给桌面
        SINGLE_THREAD_EXECUTOR.execute(() -> sendMsgToVm(msgEntity));

        return new CreateUserMessageResponse(msgEntity.getId());
    }



    private void sendMsgToVm(UserMessageEntity msgEntity) {
        final UserMessageDTO userMessageDTO = new UserMessageDTO();
        userMessageDTO.setTitle(msgEntity.getTitle());
        userMessageDTO.setContent(msgEntity.getContent());
        userMessageDTO.setCreateTime(msgEntity.getCreateTime());
        userMessageDTO.setId(msgEntity.getId());

        List<UserMessageUserEntity> messageUserList = userMessageUserDAO.findByMessageId(msgEntity.getId());
        for (UserMessageUserEntity messageUser : messageUserList) {
            SEND_USER_MSG_THREAD_POOL.execute(() -> doSendMsg(userMessageDTO, messageUser));
        }
    }

    @Override
    public void deleteUserMessage(IdRequest request) throws BusinessException {
        Assert.notNull(request, "request 不能为null");

        UUID id = request.getId();
        userMessageServiceTx.deleteMessage(id);
    }

    @Override
    public void deleteByUser(IdRequest userIdRequest) {
        Assert.notNull(userIdRequest, "userIdRequest can not be null");

        List<UserMessageUserEntity> messageUserList = userMessageUserDAO.findByUserId(userIdRequest.getId());
        if (CollectionUtils.isEmpty(messageUserList)) {
            LOGGER.debug("无用户消息需要删除，用户id:{}", userIdRequest.getId());
            return;
        }

        messageUserList.forEach(messageUserEntity -> userMessageUserDAO.deleteById(messageUserEntity.getId()));
    }

    @Override
    public void deleteByDesktopId(IdRequest idRequest) {
        Assert.notNull(idRequest, "idRequest can not be null");

        List<UserMessageUserEntity> messageUserList = userMessageUserDAO.findByDesktopId(idRequest.getId());
        messageUserList.forEach(messageUser -> userMessageUserDAO.deleteById(messageUser.getId()));
    }

    @Override
    public UserMessageDTO createMessage(CreateMessageRequest request) throws BusinessException {
        Assert.notNull(request, "request can not be null");

        UserMessageEntity msgEntity = userMessageService.createUserMessage(request.getMessageTitle(), request.getMessageContent());
        return UserMessageEntity.convertFor(msgEntity);
    }

    @Override
    public void saveAndSendUserMessage(SaveUserMessageRequest request) throws BusinessException {
        Assert.notNull(request, "request can not be null");

        // 保存用户信息
        saveUserInfo(request);
        // 发送桌面信息
        sendUserMessageToGT(request);
    }

    private void sendUserMessageToGT(SaveUserMessageRequest request) {
        final UserMessageDTO userMessageDTO = new UserMessageDTO();
        userMessageDTO.setTitle(request.getTitle());
        userMessageDTO.setContent(request.getContent());
        userMessageDTO.setCreateTime(request.getCreateTime());
        userMessageDTO.setId(request.getMessageId());

        List<UserMessageUserEntity> messageUserList = userMessageUserDAO.findByUserIdAndMessageId(request.getUserId(),
                request.getMessageId());
        for (UserMessageUserEntity messageUser : messageUserList) {
            LOGGER.info("用户{}开始发送消息给桌面", request.getUserId());
            SEND_USER_MSG_THREAD_POOL.execute(() -> doSendMsg(userMessageDTO, messageUser));
        }
    }

    private void doSendMsg(UserMessageDTO userMessageDTO, UserMessageUserEntity messageUser) {
        try {
            userMessageService.sendMessageToGuestTool(messageUser, userMessageDTO);
        } catch (Exception e) {
            LOGGER.error("用户[" + messageUser.getUserId() + "]消息发送失败", e);
        }
    }

    private void saveUserInfo(SaveUserMessageRequest request) {
        if (request.getIacUserTypeEnum() == IacUserTypeEnum.VISITOR) {
            // 访客用户需记录云桌面id，针对每个云桌面发送消息和确认消息状态
            saveVisitorMessageUser(request);
            return;
        }
        UserMessageUserEntity msgUserEntity = buildMessageUserEntity(request.getUserId(), request.getMessageId());
        msgUserEntity.setUserType(request.getIacUserTypeEnum());
        userMessageUserDAO.save(msgUserEntity);
    }

    private void saveVisitorMessageUser(SaveUserMessageRequest request) {
        UUID userId = request.getUserId();
        IacUserTypeEnum userType = request.getIacUserTypeEnum();
        List<ViewUserDesktopEntity> userDesktopList = viewDesktopDetailDAO.findByUserIdAndIsDelete(userId, false);
        if (CollectionUtils.isEmpty(userDesktopList)) {
            LOGGER.info("访客[{}]无可用云桌面", userId);
            return;
        }
        for (ViewUserDesktopEntity viewUserDesktopEntity : userDesktopList) {
            UserMessageUserEntity messageUserEntity = buildMessageUserEntity(userId, request.getMessageId());
            messageUserEntity.setDesktopId(viewUserDesktopEntity.getCbbDesktopId());
            messageUserEntity.setUserType(userType);
            userMessageUserDAO.save(messageUserEntity);
        }
    }

    private UserMessageUserEntity buildMessageUserEntity(UUID userId, UUID msgId) {
        UserMessageUserEntity msgUserEntity = new UserMessageUserEntity();
        msgUserEntity.setMessageId(msgId);
        msgUserEntity.setUserId(userId);
        msgUserEntity.setState(UserMessageStateEnum.UNSEND);

        return msgUserEntity;
    }
}
