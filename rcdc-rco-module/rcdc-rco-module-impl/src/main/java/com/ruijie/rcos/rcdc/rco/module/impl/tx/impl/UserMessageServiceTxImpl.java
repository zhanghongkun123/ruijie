package com.ruijie.rcos.rcdc.rco.module.impl.tx.impl;

import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;
import org.springframework.util.CollectionUtils;

import com.ruijie.rcos.gss.sdk.iac.module.def.dto.user.IacUserDetailDTO;
import com.ruijie.rcos.gss.sdk.iac.module.def.enums.IacUserTypeEnum;
import com.ruijie.rcos.rcdc.rco.module.impl.BusinessKey;
import com.ruijie.rcos.rcdc.rco.module.impl.dao.UserMessageDAO;
import com.ruijie.rcos.rcdc.rco.module.impl.dao.UserMessageUserDAO;
import com.ruijie.rcos.rcdc.rco.module.impl.dao.ViewDesktopDetailDAO;
import com.ruijie.rcos.rcdc.rco.module.impl.entity.UserMessageEntity;
import com.ruijie.rcos.rcdc.rco.module.impl.entity.UserMessageUserEntity;
import com.ruijie.rcos.rcdc.rco.module.impl.entity.ViewUserDesktopEntity;
import com.ruijie.rcos.rcdc.rco.module.impl.enums.UserMessageStateEnum;
import com.ruijie.rcos.rcdc.rco.module.impl.tx.UserMessageServiceTx;
import com.ruijie.rcos.sk.base.exception.BusinessException;
import com.ruijie.rcos.sk.base.log.Logger;
import com.ruijie.rcos.sk.base.log.LoggerFactory;

/**
 * Description: 用户消息操作实现类
 * Copyright: Copyright (c) 2018
 * Company: Ruijie Co., Ltd.
 * Create Time: 2019年1月12日
 *
 * @author nt
 */
@Service
public class UserMessageServiceTxImpl implements UserMessageServiceTx {

    private static final Logger LOGGER = LoggerFactory.getLogger(UserMessageServiceTxImpl.class);

    @Autowired
    private UserMessageDAO userMessageDAO;

    @Autowired
    private UserMessageUserDAO userMessageUserDAO;


    @Autowired
    private ViewDesktopDetailDAO viewDesktopDetailDAO;

    @Override
    public UserMessageEntity createUserMessage(List<IacUserDetailDTO> userDetailList, String title, String content) throws BusinessException {
        Assert.notEmpty(userDetailList, "userDetailList can not be null");
        Assert.hasText(title, "title can not be blank");
        Assert.hasText(content, "content can not be blank");

        UserMessageEntity msgEntity = buildUserMessageEntity(title, content);
        userMessageDAO.save(msgEntity);
        for (IacUserDetailDTO iacUserDetailDTO : userDetailList) {
            saveMessageUser(msgEntity, iacUserDetailDTO);
        }

        return msgEntity;
    }

    @Override
    public UserMessageEntity createDesktopUserMessage(UUID[] deskIdArr, String title, String content) throws BusinessException {
        Assert.notEmpty(deskIdArr, "userId can not be null");
        Assert.hasText(title, "title can not be blank");
        Assert.hasText(content, "content can not be blank");

        UserMessageEntity msgEntity = buildUserMessageEntity(title, content);
        userMessageDAO.save(msgEntity);

        return msgEntity;
    }

    private void saveMessageUser(UserMessageEntity msgEntity, IacUserDetailDTO userDetail) throws BusinessException {
        if (userDetail.getUserType() == IacUserTypeEnum.VISITOR) {
            // 访客用户需记录云桌面id，针对每个云桌面发送消息和确认消息状态
            saveVisitorMessageUser(msgEntity, userDetail);
            return;
        }
        UserMessageUserEntity msgUserEntity = buildMessageUserEntity(userDetail.getId(), msgEntity.getId());
        msgUserEntity.setUserType(userDetail.getUserType());
        userMessageUserDAO.save(msgUserEntity);
    }

    private void saveVisitorMessageUser(UserMessageEntity msgEntity, IacUserDetailDTO userDetail) throws BusinessException {
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

    private UserMessageEntity buildUserMessageEntity(String title, String content) {
        UserMessageEntity entity = new UserMessageEntity();
        entity.setTitle(title);
        entity.setContent(content);
        entity.setCreateTime(new Date());
        return entity;
    }

    @Override
    public void modifyUserMessageState(UUID messageUserId, UserMessageStateEnum messageState) throws BusinessException {
        Assert.notNull(messageUserId, "messageUserId can not be null");
        Assert.notNull(messageState, "messageState can not be null");

        UserMessageUserEntity entity = userMessageUserDAO.getOne(messageUserId);
        if (entity == null) {
            LOGGER.error("user message user record not exist, messageUserId[{}]", messageUserId);
            throw new BusinessException(BusinessKey.RCDC_USER_USER_MESSAGE_USER_NOT_EXIST);
        }
        // 如果是最终态不需要更新，有潜在的有时序问题
        if (UserMessageStateEnum.READ == messageState) {
            LOGGER.warn("用户消息为已读状态，属于最终状态，不需要更新");
            return;
        }
        entity.setState(messageState);
        userMessageUserDAO.save(entity);
    }

    @Override
    public void deleteMessage(UUID id) throws BusinessException {
        Assert.notNull(id, "id can not be null");

        Optional<UserMessageEntity> messageOpt = userMessageDAO.findById(id);
        if (!messageOpt.isPresent()) {
            LOGGER.error("user message record not exist, messageId[{}]", id);
            throw new BusinessException(com.ruijie.rcos.rcdc.rco.module.def.BusinessKey.RCDC_USER_USER_MESSAGE_NOT_EXIST);
        }

        userMessageDAO.deleteById(id);

        List<UserMessageUserEntity> entityList = userMessageUserDAO.findByMessageId(id);
        if (CollectionUtils.isEmpty(entityList)) {
            LOGGER.debug("no message user record, messageId[{}]", id);
            return;
        }

        for (UserMessageUserEntity entity : entityList) {
            userMessageUserDAO.delete(entity);
        }
    }

}
