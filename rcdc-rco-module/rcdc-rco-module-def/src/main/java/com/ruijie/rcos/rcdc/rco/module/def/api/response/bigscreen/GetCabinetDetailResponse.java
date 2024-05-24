package com.ruijie.rcos.rcdc.rco.module.def.api.response.bigscreen;

import com.ruijie.rcos.rcdc.rco.module.def.api.dto.bigscreen.CabinetDTO;
import com.ruijie.rcos.sk.modulekit.api.comm.DefaultResponse;

/**
 * Description: 获取机柜详细信息
 * Copyright: Copyright (c) 2019
 * Company: Ruijie Co., Ltd.
 * Create Time: 2019年7月24日
 *
 * @author BaiGuoliang
 */
public class GetCabinetDetailResponse extends DefaultResponse {

    private CabinetDTO cabinetDTO;

    public CabinetDTO getCabinetDTO() {
        return cabinetDTO;
    }

    public void setCabinetDTO(CabinetDTO cabinetDTO) {
        this.cabinetDTO = cabinetDTO;
    }
}