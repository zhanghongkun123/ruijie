package com.ruijie.rcos.rcdc.rco.module.def.api.response;

import com.ruijie.rcos.rcdc.rco.module.def.api.dto.CbbDeskFaultInfoDTO;
import com.ruijie.rcos.sk.modulekit.api.comm.DefaultResponse;

/**
 * Description:
 * Copyright: Copyright (c) 2019
 * Company: Ruijie Co., Ltd.
 * Create Time: 2020/1/4 18:41
 *
 * @author ketb
 */
public class CbbDeskFaultInfoResponse extends DefaultResponse {

    private CbbDeskFaultInfoDTO cbbDeskFaultInfoDTO;

    public CbbDeskFaultInfoResponse() {

    }

    public CbbDeskFaultInfoResponse(CbbDeskFaultInfoDTO cbbDeskFaultInfoDTO) {
        this.cbbDeskFaultInfoDTO = cbbDeskFaultInfoDTO;
    }

    public CbbDeskFaultInfoDTO getCbbDeskFaultInfoDTO() {
        return cbbDeskFaultInfoDTO;
    }

    public void setCbbDeskFaultInfoDTO(CbbDeskFaultInfoDTO cbbDeskFaultInfoDTO) {
        this.cbbDeskFaultInfoDTO = cbbDeskFaultInfoDTO;
    }
}
