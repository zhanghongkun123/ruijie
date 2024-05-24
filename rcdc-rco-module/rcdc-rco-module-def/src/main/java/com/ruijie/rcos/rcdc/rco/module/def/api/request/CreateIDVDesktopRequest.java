package com.ruijie.rcos.rcdc.rco.module.def.api.request;

import com.ruijie.rcos.rcdc.rco.module.def.api.dto.IDVCloudDesktopDTO;
import com.ruijie.rcos.sk.base.annotation.NotNull;
import com.ruijie.rcos.sk.modulekit.api.comm.Request;

/**
 * 创建IDV云桌面数据请求参数
 * Description: Function Description
 * Copyright: Copyright (c) 2020
 * Company: Ruijie Co., Ltd.
 * Create Time: 2020年3月9日
 *
 * @author  brq
 */
public class CreateIDVDesktopRequest implements Request {

    @NotNull
    private IDVCloudDesktopDTO idvCloudDesktopDTO;

    public IDVCloudDesktopDTO getIdvCloudDesktopDTO() {
        return idvCloudDesktopDTO;
    }

    public void setIdvCloudDesktopDTO(IDVCloudDesktopDTO idvCloudDesktopDTO) {
        this.idvCloudDesktopDTO = idvCloudDesktopDTO;
    }
}
