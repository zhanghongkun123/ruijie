package com.ruijie.rcos.rcdc.rco.module.impl.service.impl;

import com.alibaba.fastjson.JSON;
import com.google.common.collect.Lists;
import com.ruijie.rcos.gss.sdk.iac.module.def.dto.user.IacUserDetailDTO;
import com.ruijie.rcos.gss.sdk.iac.module.def.enums.IacUserTypeEnum;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.api.CbbGuestToolMessageAPI;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.dto.CbbGuesttoolMessageDTO;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.enums.CbbCloudDeskState;
import com.ruijie.rcos.rcdc.rco.module.def.api.dto.UserMessageDTO;
import com.ruijie.rcos.rcdc.rco.module.impl.RcdcGuestToolCmdKey;
import com.ruijie.rcos.rcdc.rco.module.impl.dao.UserMessageDAO;
import com.ruijie.rcos.rcdc.rco.module.impl.dao.UserMessageUserDAO;
import com.ruijie.rcos.rcdc.rco.module.impl.dao.ViewDesktopDetailDAO;
import com.ruijie.rcos.rcdc.rco.module.impl.dto.UserMessageSendDTO;
import com.ruijie.rcos.rcdc.rco.module.impl.entity.HostUserEntity;
import com.ruijie.rcos.rcdc.rco.module.impl.entity.UserMessageEntity;
import com.ruijie.rcos.rcdc.rco.module.impl.entity.UserMessageUserEntity;
import com.ruijie.rcos.rcdc.rco.module.impl.entity.ViewUserDesktopEntity;
import com.ruijie.rcos.rcdc.rco.module.impl.enums.UserMessageStateEnum;
import com.ruijie.rcos.rcdc.rco.module.impl.service.HostUserService;
import com.ruijie.rcos.rcdc.rco.module.impl.service.UserMessageService;
import com.ruijie.rcos.rcdc.rco.module.impl.tx.UserMessageServiceTx;
import com.ruijie.rcos.sk.base.exception.BusinessException;
import com.ruijie.rcos.sk.base.log.Logger;
import com.ruijie.rcos.sk.base.log.LoggerFactory;
import org.apache.commons.lang3.BooleanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;
import org.springframework.util.CollectionUtils;

import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

import static com.ruijie.rcos.rcdc.rco.module.def.BusinessKey.RCDC_USER_CLOUDDESKTOP_NOT_FOUNT_BY_ID;

/**
 * Description: Function Description
 * Copyright: Copyright (c) 2018
 * Company: Ruijie Co., Ltd.
 * Create Time: 2019/1/15
 *
 * @author Jarman
 */
@Service
public class UserMessageServiceImpl implements UserMessageService {

    private static final Logger LOGGER = LoggerFactory.getLogger(UserMessageServiceImpl.class);

    private static final Set<UUID> MSG_USER_SET = new HashSet<>();

    private static final Set<UUID> MSG_DESK_SET = new HashSet<>();

    @Autowired
    private CbbGuestToolMessageAPI guestToolMessageAPI;

    @Autowired
    private ViewDesktopDetailDAO viewDesktopDetailDAO;

    @Autowired
    private UserMessageServiceTx userMessageServiceTx;

    @Autowired
    private UserMessageUserDAO userMessageUserDAO;

    @Autowired
    private HostUserService hostUserService;

    @Autowired
    private UserMessageDAO userMessageDAO;

    @Override
    public void sendMessageToGuestTool(UserMessageUserEntity messageUser, UserMessageDTO userMessage) throws BusinessException {
        Assert.notNull(messageUser, "messageUser can not be null");
        Assert.notNull(userMessage, "UserMessageDTO can not be null");

        UUID msgUserId = messageUser.getId();
        synchronized (this) {
            if (MSG_USER_SET.contains(msgUserId)) {
                LOGGER.debug("用户[{}]正在发送消息[{}]中,跳过该次处理", msgUserId, userMessage.getTitle());
                return;
            }
            MSG_USER_SET.add(msgUserId);
        }

        try {
            doSendMsg(messageUser, userMessage);
        } finally {
            MSG_USER_SET.remove(msgUserId);
        }
    }

    @Override
    public void sendDesktopMessageToGuestTool(UUID deskId, UserMessageUserEntity messageUser, UserMessageDTO userMessage) throws BusinessException {
        Assert.notNull(deskId, "deskId can not be null");
        Assert.notNull(messageUser, "messageUser can not be null");
        Assert.notNull(userMessage, "UserMessageDTO can not be null");

        synchronized (this) {
            if (MSG_DESK_SET.contains(deskId)) {
                LOGGER.debug("桌面[{}]正在发送消息[{}]中,跳过该次处理", deskId, userMessage.getTitle());
                return;
            }
            MSG_DESK_SET.add(deskId);
        }

        try {
            doSendDesktopMsg(deskId, messageUser, userMessage);
        } finally {
            MSG_DESK_SET.remove(deskId);
        }
    }

    private void doSendMsg(UserMessageUserEntity messageUser, UserMessageDTO userMessage) throws BusinessException {
        IacUserTypeEnum userType = messageUser.getUserType();
        if (IacUserTypeEnum.VISITOR == userType) {
            LOGGER.debug("发送访客[{}]消息", messageUser.getUserId());
            // 访客用户消息根据指定的云桌面进行发送
            sendVisitorMessage(messageUser, userMessage);
            return;
        }
        LOGGER.debug("发送非访客用户[{}]消息", messageUser.getUserId());
        sendNormalUserMessage(messageUser, userMessage);
    }

    private void doSendDesktopMsg(UUID deskId, UserMessageUserEntity messageUser, UserMessageDTO userMessage) throws BusinessException {
        ViewUserDesktopEntity viewDesktop = viewDesktopDetailDAO.findByCbbDesktopId(deskId);

        UserMessageSendDTO messageDTO = buildMessageMessageDTO(userMessage);
        CbbGuesttoolMessageDTO guesttoolMessageDTO = buildSendMessageRequest(messageDTO);
        sendMsgToDesktop(messageUser, guesttoolMessageDTO, viewDesktop.getCbbDesktopId());
    }

    private void sendNormalUserMessage(UserMessageUserEntity messageUser, UserMessageDTO userMessage) {

        doSendSingleSessionDeskNormalUserMessage(messageUser, userMessage);
        doSendMultiSessionDeskNormalUserMessage(messageUser, userMessage);
    }

    private void doSendSingleSessionDeskNormalUserMessage(UserMessageUserEntity messageUser, UserMessageDTO userMessage) {
        UUID userId = messageUser.getUserId();
        // 根据userId获取用户绑定在线状态云桌面
        List<ViewUserDesktopEntity> runningDesktopList =
                viewDesktopDetailDAO.findViewDesktopEntitiesByUserIdAndDeskStateAndIsDelete(userId, CbbCloudDeskState.RUNNING.name(), false);
        if (CollectionUtils.isEmpty(runningDesktopList)) {
            LOGGER.debug("user[{}] can not found running desktop, send the message[{}] later", userId, userMessage.toString());
            return;
        }
        // 逐个向运行中的桌面发送消息
        UserMessageSendDTO messageDTO = buildMessageMessageDTO(userMessage);
        CbbGuesttoolMessageDTO guesttoolMessageDTO = buildSendMessageRequest(messageDTO);

        for (ViewUserDesktopEntity viewDesktop : runningDesktopList) {
            sendMsgToDesktop(messageUser, guesttoolMessageDTO, viewDesktop.getCbbDesktopId());
        }
    }

    private void doSendMultiSessionDeskNormalUserMessage(UserMessageUserEntity messageUser, UserMessageDTO userMessage) {
        UUID userId = messageUser.getUserId();
        List<HostUserEntity> hostUserEntityList = hostUserService.findByUserId(userId);
        List<UUID> hostIdList = hostUserEntityList.stream().map(HostUserEntity::getDesktopId).filter(Objects::nonNull).collect(Collectors.toList());
        if (CollectionUtils.isEmpty(hostIdList)) {
            LOGGER.debug("user[{}] can not found related multi desktop, send the message[{}] later", userId, JSON.toJSONString(userMessage));
            return;
        }
        List<ViewUserDesktopEntity> hostDeskList = viewDesktopDetailDAO.findAllByCbbDesktopIdIn(hostIdList);
        hostDeskList = hostDeskList.stream().filter(item -> Objects.equals(CbbCloudDeskState.RUNNING.name(), item.getDeskState())
                && BooleanUtils.isFalse(item.getIsDelete())).collect(Collectors.toList());
        if (CollectionUtils.isEmpty(hostDeskList)) {
            LOGGER.debug("user[{}] can not found running multi desktop, send the message[{}] later", userId, JSON.toJSONString(userMessage));
            return;
        }
        userMessage.setUserId(userId);
        UserMessageSendDTO messageDTO = buildMessageMessageDTO(userMessage);
        CbbGuesttoolMessageDTO guesttoolMessageDTO = buildSendMessageRequest(messageDTO);
        for (ViewUserDesktopEntity desktopEntity : hostDeskList) {
            sendMsgToDesktop(messageUser, guesttoolMessageDTO, desktopEntity.getCbbDesktopId());
        }
    }

    private void sendVisitorMessage(UserMessageUserEntity messageUser, UserMessageDTO userMessage) throws BusinessException {
        UUID desktopId = messageUser.getDesktopId();
        ViewUserDesktopEntity viewUserDesktopEntity = viewDesktopDetailDAO.findByCbbDesktopId(desktopId);
        if (viewUserDesktopEntity == null) {
            throw new BusinessException(RCDC_USER_CLOUDDESKTOP_NOT_FOUNT_BY_ID, desktopId.toString());
        }

        // 非运行状态云桌面，不发送消息
        CbbCloudDeskState deskState = CbbCloudDeskState.valueOf(viewUserDesktopEntity.getDeskState());
        if (deskState != CbbCloudDeskState.RUNNING) {
            LOGGER.debug("云桌面[{}]未处于运行状态，不进行消息发送", viewUserDesktopEntity.getDesktopName());
            return;
        }

        // 构造发送消息请求
        UserMessageSendDTO messageDTO = buildMessageMessageDTO(userMessage);
        CbbGuesttoolMessageDTO guesttoolMessageDTO = buildSendMessageRequest(messageDTO);
        sendMsgToDesktop(messageUser, guesttoolMessageDTO, viewUserDesktopEntity.getCbbDesktopId());
    }

    private void sendMsgToDesktop(UserMessageUserEntity messageUser, CbbGuesttoolMessageDTO guesttoolMessageDTO, UUID cbbDesktopId) {
        guesttoolMessageDTO.setDeskId(cbbDesktopId);
        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug("发送用户消息, 请求内容{}", JSON.toJSONString(guesttoolMessageDTO));
        }

        try {
            guestToolMessageAPI.syncRequest(guesttoolMessageDTO);
            userMessageServiceTx.modifyUserMessageState(messageUser.getId(), UserMessageStateEnum.UNREAD);
        } catch (Exception e) {
            LOGGER.error("用户[" + messageUser.getUserId() + "]消息发送失败", e);
        }
    }

    private CbbGuesttoolMessageDTO buildSendMessageRequest(UserMessageSendDTO messageDTO) {
        final CbbGuesttoolMessageDTO dto = new CbbGuesttoolMessageDTO();
        dto.setBody(JSON.toJSONString(messageDTO));
        dto.setCmdId(RcdcGuestToolCmdKey.RCDC_GT_CMD_ID_SMS);
        dto.setPortId(RcdcGuestToolCmdKey.RCDC_GT_PORT_ID_SMS_SEND);

        return dto;
    }

    private UserMessageSendDTO buildMessageMessageDTO(UserMessageDTO message) {
        UserMessageSendDTO messageSendDTO = new UserMessageSendDTO();
        messageSendDTO.setCode(0);
        UserMessageSendDTO.Content content = new UserMessageSendDTO.Content();
        content.setId(message.getId());
        content.setContent(message.getContent());
        content.setTitle(message.getTitle());
        content.setTime(message.getCreateTime());
        if (message.getUserId() != null) {
            content.setMultiSessionId(message.getUserId());
        }
        messageSendDTO.setContent(content);
        return messageSendDTO;
    }

    @Override
    public List<UserMessageUserEntity> findNeedSendMessage(UUID userId) {
        Assert.notNull(userId, "用户id不能为null");
        return userMessageUserDAO.findByUserIdAndStateIn(userId, Lists.newArrayList(UserMessageStateEnum.UNSEND, UserMessageStateEnum.UNREAD));
    }

    @Override
    public UserMessageEntity createUserMessage(String title, String content) throws BusinessException {
        Assert.hasText(title, "title can not be blank");
        Assert.hasText(content, "content can not be blank");

        UserMessageEntity msgEntity = buildUserMessageEntity(title, content);
        userMessageDAO.save(msgEntity);

        return msgEntity;
    }

    private UserMessageEntity buildUserMessageEntity(String title, String content) {
        UserMessageEntity entity = new UserMessageEntity();
        entity.setTitle(title);
        entity.setContent(content);
        entity.setCreateTime(new Date());
        return entity;
    }

    @Override
    public void saveUserJoinMessage(UserMessageEntity msgEntity, IacUserDetailDTO userDetail) throws BusinessException {
        Assert.notNull(msgEntity, "msgEntity cannot be null");
        Assert.notNull(userDetail, "userDetail cannot be null");
        if (userDetail.getUserType() == IacUserTypeEnum.VISITOR) {
            // 访客用户需记录云桌面id，针对每个云桌面发送消息和确认消息状态
            saveVisitorMessageUser(msgEntity, userDetail);
            return;
        }
        UserMessageUserEntity msgUserEntity = buildMessageUserEntity(userDetail.getId(), msgEntity.getId());
        msgUserEntity.setUserType(userDetail.getUserType());
        userMessageUserDAO.save(msgUserEntity);
    }

    private void saveVisitorMessageUser(UserMessageEntity msgEntity, IacUserDetailDTO userDetail) {
        UUID userId = userDetail.getId();
        IacUserTypeEnum userType = userDetail.getUserType();
        List<ViewUserDesktopEntity> userDesktopList = viewDesktopDetailDAO.findByUserIdAndIsDelete(userId, false);
        if (CollectionUtils.isEmpty(userDesktopList)) {
            LOGGER.info("访客用户[{}]无可用云桌面", userDetail.getUserName());
            return;
        }
        for (ViewUserDesktopEntity viewUserDesktopEntity : userDesktopList) {
            UserMessageUserEntity messageUserEntity = buildMessageUserEntity(userId, msgEntity.getId());
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
