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
public class ComputerRemoteAssistDTO {
    
    private String assistState;
    
    private ComputerAssistInfoDTO assistInfo;

    public String getAssistState() {
        return assistState;
    }

    public void setAssistState(String assistState) {
        this.assistState = assistState;
    }

    public ComputerAssistInfoDTO getAssistInfo() {
        return assistInfo;
    }

    public void setAssistInfo(ComputerAssistInfoDTO assistInfo) {
        this.assistInfo = assistInfo;
    }
}
