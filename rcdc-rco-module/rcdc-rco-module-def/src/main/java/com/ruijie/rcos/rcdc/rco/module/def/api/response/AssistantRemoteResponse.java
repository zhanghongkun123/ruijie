package com.ruijie.rcos.rcdc.rco.module.def.api.response;

import com.ruijie.rcos.rcdc.rco.module.def.api.dto.AssistantRemoteResponseDTO;
import com.ruijie.rcos.sk.modulekit.api.comm.DefaultResponse;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * Description:
 * Copyright: Copyright (c) 2019
 * Company: Ruijie Co., Ltd.
 * Create Time: 2020/2/24 0:38
 *
 * @author ketb
 */
public class AssistantRemoteResponse extends DefaultResponse {

    @Autowired
    private AssistantRemoteResponseDTO responseDTO;

    public AssistantRemoteResponseDTO getResponseDTO() {
        return responseDTO;
    }

    public void setResponseDTO(AssistantRemoteResponseDTO responseDTO) {
        this.responseDTO = responseDTO;
    }
}
