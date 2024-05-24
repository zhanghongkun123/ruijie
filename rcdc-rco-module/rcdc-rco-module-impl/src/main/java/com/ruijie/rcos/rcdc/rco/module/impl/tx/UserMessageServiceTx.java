package com.ruijie.rcos.rcdc.rco.module.impl.tx;

import com.ruijie.rcos.gss.sdk.iac.module.def.dto.user.IacUserDetailDTO;
import com.ruijie.rcos.rcdc.rco.module.impl.entity.UserMessageEntity;
import com.ruijie.rcos.rcdc.rco.module.impl.enums.UserMessageStateEnum;
import com.ruijie.rcos.sk.base.exception.BusinessException;

import java.util.List;
import java.util.UUID;

/**
 * 
 * Description: 用户消息存在事物的操作
 * Copyright: Copyright (c) 2018
 * Company: Ruijie Co., Ltd.
 * Create Time: 2019年1月12日
 * 
 * @author nt
 */
public interface UserMessageServiceTx {

    /**
     * 添加用户消息
     * 
     * @param userDetailList 用户id列表
     * @param title 标题
     * @param content 内容
     * @return 消息内容实体
     * @throws BusinessException 业务异常
     */
    UserMessageEntity createUserMessage(List<IacUserDetailDTO> userDetailList, String title, String content) throws BusinessException;

    /**
     * 添加用户桌面消息
     *
     * @param deskIdArr 用户id数组
     * @param title 标题
     * @param content 内容
     * @return 消息内容实体
     * @throws BusinessException 业务异常
     */
    UserMessageEntity createDesktopUserMessage(UUID[] deskIdArr, String title, String content) throws BusinessException;

    /**
     * 修改用户消息状态
     * 
     * @param messageUserId 发送消息用户对象id
     * @param messageState 消息状态
     * @throws BusinessException 业务异常
     */
    void modifyUserMessageState(UUID messageUserId, UserMessageStateEnum messageState) throws BusinessException;

    /**
     * 删除消息
     * 
     * @param id 消息id
     * @throws BusinessException 业务异常
     */
    void deleteMessage(UUID id) throws BusinessException;
}
