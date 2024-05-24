package com.ruijie.rcos.rcdc.rco.module.def.api.response;

import com.ruijie.rcos.rcdc.rco.module.def.api.dto.ComputerRemoteAssistDTO;
import com.ruijie.rcos.sk.modulekit.api.comm.DefaultResponse;

/**
 * Description: 远程协助 response
 * Copyright: Copyright (c) 2018
 * Company: Ruijie Co., Ltd.
 * Create Time: 2019/1/7
 * 
 * @author artom
 */
public class ComputerRemoteAssistResponse extends DefaultResponse {

    private ComputerRemoteAssistDTO dto;

    public ComputerRemoteAssistResponse() {
    	setStatus(Status.SUCCESS);
    }

    public ComputerRemoteAssistResponse(ComputerRemoteAssistDTO dto) {
        setStatus(Status.SUCCESS);
        this.dto = dto;
    }
    
    public ComputerRemoteAssistDTO getDto() {
        return dto;
    }
    
    public void setDto(ComputerRemoteAssistDTO dto) {
        this.dto = dto;
    }
}
