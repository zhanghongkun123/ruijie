package com.ruijie.rcos.rcdc.rco.module.def.api.dto;

/**
 * *远程协助信息DTO
 * Description: Function Description
 * Copyright: Copyright (c) 2018
 * Company: Ruijie Co., Ltd.
 * Create Time: 2019年1月7日
 *
 * @author artom
 */
public class CloudDesktopRemoteAssistDTO {
    
    private String assistState;
    
    private AssistInfoDTO assistInfo;

    private String title;

    public String getAssistState() {
        return assistState;
    }

    public void setAssistState(String assistState) {
        this.assistState = assistState;
    }

    public AssistInfoDTO getAssistInfo() {
        return assistInfo;
    }

    public void setAssistInfo(AssistInfoDTO assistInfo) {
        this.assistInfo = assistInfo;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }
}
