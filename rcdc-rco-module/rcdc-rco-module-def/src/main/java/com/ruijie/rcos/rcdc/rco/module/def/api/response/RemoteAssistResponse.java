package com.ruijie.rcos.rcdc.rco.module.def.api.response;

import com.ruijie.rcos.rcdc.rco.module.def.api.dto.CloudDesktopRemoteAssistDTO;
import com.ruijie.rcos.sk.modulekit.api.comm.DefaultResponse;

/**
 * Description: 远程协助 response
 * Copyright: Copyright (c) 2018
 * Company: Ruijie Co., Ltd.
 * Create Time: 2019/1/7
 * 
 * @author artom
 */
public class RemoteAssistResponse extends DefaultResponse {
    
    private CloudDesktopRemoteAssistDTO dto;
    
    public RemoteAssistResponse() {
    	setStatus(Status.SUCCESS);
    }
    
    public RemoteAssistResponse(CloudDesktopRemoteAssistDTO dto) {
        setStatus(Status.SUCCESS);
        this.dto = dto;
    }
    
    public CloudDesktopRemoteAssistDTO getDto() {
        return dto;
    }
    
    public void setDto(CloudDesktopRemoteAssistDTO dto) {
        this.dto = dto;
    }
}
