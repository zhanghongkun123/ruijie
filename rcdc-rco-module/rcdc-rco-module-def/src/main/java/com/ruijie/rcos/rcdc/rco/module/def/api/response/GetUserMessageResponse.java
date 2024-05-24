package com.ruijie.rcos.rcdc.rco.module.def.api.response;

import com.ruijie.rcos.rcdc.rco.module.def.api.dto.UserMessageDTO;
import com.ruijie.rcos.sk.modulekit.api.comm.DefaultResponse;

/**
 * 
 * Description: 获取用户消息响应
 * Copyright: Copyright (c) 2018
 * Company: Ruijie Co., Ltd.
 * Create Time: 2019年3月26日
 * 
 * @author nt
 */
public class GetUserMessageResponse extends DefaultResponse {
    
    private UserMessageDTO userMessage;

    public UserMessageDTO getUserMessage() {
        return userMessage;
    }

    public void setUserMessage(UserMessageDTO userMessage) {
        this.userMessage = userMessage;
    }
    
}
