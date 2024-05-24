package com.ruijie.rcos.rcdc.rco.module.def.msg;


import com.ruijie.rcos.rcdc.task.ext.module.def.dto.BaseMsgProgressDTO;

/**
 * Description:
 * Copyright: Copyright (c) 2023
 * Company: Ruijie Co., Ltd.
 * Create Time: 2023/6/14 9:17
 *
 * @author yxq
 */
public class RcoMsgDTO extends BaseMsgProgressDTO {

    /**
     * 展示额外提示语
     */
    private String msgCode;

    public String getMsgCode() {
        return msgCode;
    }

    public void setMsgCode(String msgCode) {
        this.msgCode = msgCode;
    }
}
