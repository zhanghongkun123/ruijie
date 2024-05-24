package com.ruijie.rcos.rcdc.rco.module.def.api.response;

import com.ruijie.rcos.rcdc.rco.module.def.api.dto.ComputerDTO;
import com.ruijie.rcos.sk.modulekit.api.comm.DefaultResponse;

/**
 * Description:
 * Copyright: Copyright (c) 2019
 * Company: Ruijie Co., Ltd.
 * Create Time: 2020/1/7 20:00
 *
 * @author ketb
 */
public class GetComputerInfoResponse extends DefaultResponse {

    private ComputerDTO computerDTO;

    public GetComputerInfoResponse() {
    }

    public GetComputerInfoResponse(ComputerDTO computerDTO) {
        this.setStatus(Status.SUCCESS);
        this.computerDTO = computerDTO;
    }

    public ComputerDTO getComputerDTO() {
        return computerDTO;
    }

    public void setComputerDTO(ComputerDTO computerDTO) {
        this.computerDTO = computerDTO;
    }

}
