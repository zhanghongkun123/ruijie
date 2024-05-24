package com.ruijie.rcos.rcdc.rco.module.impl.service;

import com.ruijie.rcos.gss.sdk.iac.module.def.dto.user.IacUserDetailDTO;
import com.ruijie.rcos.rcdc.rco.module.def.api.dto.UserMessageDTO;
import com.ruijie.rcos.rcdc.rco.module.impl.entity.UserMessageEntity;
import com.ruijie.rcos.rcdc.rco.module.impl.entity.UserMessageUserEntity;
import com.ruijie.rcos.sk.base.exception.BusinessException;

import java.util.List;
import java.util.UUID;

/**
 * Description: Function Description
 * Copyright: Copyright (c) 2018
 * Company: Ruijie Co., Ltd.
 * Create Time: 2019/1/15
 *
 * @author Jarman
 */
public interface UserMessageService {

    /**
     * 发送消息给guest tool
     *
     * @param messageUser 发送的消息用户对象
     * @param userMessage 发送的消息对象
     * @throws BusinessException 业务异常
     */
    void sendMessageToGuestTool(UserMessageUserEntity messageUser, UserMessageDTO userMessage) throws BusinessException;

    /**
     * 发送消息给guest tool
     *
     * @param deskId 发送的消息用户对象
     * @param userMessage 发送的消息对象
     * @param messageUser 消息
     * @throws BusinessException 业务异常
     */
    void sendDesktopMessageToGuestTool(UUID deskId, UserMessageUserEntity messageUser, UserMessageDTO userMessage) throws BusinessException;

    /**
     * 获取未读消息
     *
     * @param userId 用户id
     * @return 返回用户未读消息列表
     */
    List<UserMessageUserEntity> findNeedSendMessage(UUID userId);

    /**
     * 添加用户消息
     *
     * @param title 标题
     * @param content 内容
     * @return 消息内容实体
     * @throws BusinessException 业务异常
     */
    UserMessageEntity createUserMessage(String title, String content) throws BusinessException;


    /**
     * 创建用户消息管理表数
     * 
     * @param msgEntity UserMessageEntity
     * @param userDetail IacUserDetailDTO
     * @throws BusinessException 业务异常
     */
    void saveUserJoinMessage(UserMessageEntity msgEntity, IacUserDetailDTO userDetail) throws BusinessException;
}
