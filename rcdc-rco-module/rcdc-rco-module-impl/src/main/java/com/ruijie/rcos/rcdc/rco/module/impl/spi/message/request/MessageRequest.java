package com.ruijie.rcos.rcdc.rco.module.impl.spi.message.request;

import com.ruijie.rcos.base.task.module.def.enums.MsgType;

import java.util.UUID;

/**
 * Description: Est发起的 任务信息查询请求DTO
 * Copyright: Copyright (c) 2021
 * Company: Ruijie Co., Ltd.
 * Create Time: 2021/3/31 11:42
 *
 * @author lihengjing
 */
public class MessageRequest {

    private UUID msgRelationId;

    private MsgType msgType;

    public UUID getMsgRelationId() {
        return msgRelationId;
    }

    public void setMsgRelationId(UUID msgRelationId) {
        this.msgRelationId = msgRelationId;
    }

    public MsgType getMsgType() {
        return msgType;
    }

    public void setMsgType(MsgType msgType) {
        this.msgType = msgType;
    }
}
