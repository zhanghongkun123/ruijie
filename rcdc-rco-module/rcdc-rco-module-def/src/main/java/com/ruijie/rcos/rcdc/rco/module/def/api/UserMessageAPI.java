package com.ruijie.rcos.rcdc.rco.module.def.api;

import com.ruijie.rcos.gss.sdk.iac.module.def.dto.user.IacUserDetailDTO;
import com.ruijie.rcos.rcdc.rco.module.def.api.dto.UserMessageDTO;
import com.ruijie.rcos.rcdc.rco.module.def.api.request.*;
import com.ruijie.rcos.rcdc.rco.module.def.api.response.CreateUserMessageResponse;
import com.ruijie.rcos.sk.base.exception.BusinessException;
import com.ruijie.rcos.sk.modulekit.api.comm.DefaultPageResponse;
import com.ruijie.rcos.sk.modulekit.api.comm.IdRequest;

import java.util.UUID;


/**
 * Description: 用户消息接口
 * Copyright: Copyright (c) 2018
 * Company: Ruijie Co., Ltd.
 * Create Time: 2018/11/25
 *
 * @author Jarman
 */
public interface UserMessageAPI {

    /**
     * 搜索用户消息
     * 
     * @param request 请求参数对象
     * @return 返回搜索结果
     */
    
    DefaultPageResponse<UserMessageDTO> queryUserMessage(PageSearchRequest request);

    /**
     * 创建用户消息并发送给云桌面
     * 
     * @param request 创建消息请求参数
     * @throws BusinessException 业务异常
     * @return response
     */
    
    CreateUserMessageResponse createUserMessage(CreateUserMessageRequest request) throws BusinessException;
    
    /**
     * 创建用户消息并发送给云桌面
     * 
     * @param request 创建消息请求参数
     * @throws BusinessException 业务异常
     * @return response
     */
    
    CreateUserMessageResponse createGroupUserMessage(CreateGroupUserMessageRequest request) throws BusinessException;

    /**
     * 删除用户消息
     * @param idRequest 删除消息请求参数
     * @throws BusinessException 业务异常
     */
    
    void deleteUserMessage(IdRequest idRequest) throws BusinessException;
    
    /**
     * 获取用户消息
     * @param idRequest 请求参数
     * @return 请求结果
     * @throws BusinessException 业务异常
     */

    UserMessageDTO getById(IdRequest idRequest)throws BusinessException;

    /**
     *  根据用户id删除用户消息
     *
     * @param idRequest 请求参数
     */
    
    void deleteByUser(IdRequest idRequest);

    /**
     * 根据云桌面Id删除用户消息
     * @param idRequest 请求参数
     */
    void deleteByDesktopId(IdRequest idRequest);

    /**
     * 创建信息
     * @param request 请求参数
     * @return 请求结果
     * @throws BusinessException 业务异常
     */
    UserMessageDTO createMessage(CreateMessageRequest request) throws BusinessException;

    /**
     * 保存和发送用户信息
     * @param request 请求
     * @throws BusinessException 业务异常
     */
    void saveAndSendUserMessage(SaveUserMessageRequest request) throws BusinessException;
}
