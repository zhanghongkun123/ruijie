package com.ruijie.rcos.rcdc.rco.module.def.api.dto;

/**
 * Guesttool消息参数类
 * Description: Function Description
 * Copyright: Copyright (c) 2018
 * Company: Ruijie Co., Ltd.
 * Create Time: 2019年1月2日
 * 
 * @author ketb
 */
public class AssistantMessageBody {

    private String business;
    
    private Integer portId;
    
    private AssistantMessageContent content;

    public String getBusiness() {
        return business;
    }

    public void setBusiness(String business) {
        this.business = business;
    }

    public Integer getPortId() {
        return portId;
    }

    public void setPortId(Integer portId) {
        this.portId = portId;
    }

    public AssistantMessageContent getContent() {
        return content;
    }

    public void setContent(AssistantMessageContent content) {
        this.content = content;
    }

}
