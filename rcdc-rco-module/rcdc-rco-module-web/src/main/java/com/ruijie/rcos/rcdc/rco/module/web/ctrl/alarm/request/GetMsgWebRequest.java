package com.ruijie.rcos.rcdc.rco.module.web.ctrl.alarm.request;

import com.ruijie.rcos.base.task.module.def.enums.MsgType;
import com.ruijie.rcos.sk.base.annotation.NotNull;
import com.ruijie.rcos.sk.webmvc.api.request.WebRequest;

import java.util.UUID;

/**
 * Description: Function Description
 * Copyright: Copyright (c) 2019
 * Company: Ruijie Co., Ltd.
 * Create Time: 2019年03月07日
 *
 * @author xgx
 */
public class GetMsgWebRequest implements WebRequest {
    @NotNull
    private UUID msgRelationId;

    @NotNull
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
