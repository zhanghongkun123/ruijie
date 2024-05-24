package com.ruijie.rcos.rcdc.rco.module.impl.remoteassist.dto;

/**
 * Description: Function Description
 * Copyright: Copyright (c) 2020
 * Company: Ruijie Co., Ltd.
 * Create Time: 2020/12/24
 *
 * @author chenjiehui
 */
public class RemoteAssistMessageDTO {
    /**
     * 是否显示请求远程协助的状态（通知浮动条）
     */
    private Boolean hasRequest;

    public Boolean getHasRequest() {
        return hasRequest;
    }

    public void setHasRequest(Boolean hasRequest) {
        this.hasRequest = hasRequest;
    }
}
