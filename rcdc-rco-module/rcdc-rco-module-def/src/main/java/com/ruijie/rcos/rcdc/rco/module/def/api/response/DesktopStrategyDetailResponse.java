package com.ruijie.rcos.rcdc.rco.module.def.api.response;

import com.ruijie.rcos.rcdc.rco.module.def.api.dto.CloudDesktopDetailDTO;
import com.ruijie.rcos.sk.modulekit.api.comm.DefaultResponse;

/**
 * 
 * Description: 获取云桌面策略详情响应
 * Copyright: Copyright (c) 2018
 * Company: Ruijie Co., Ltd.
 * Create Time: 2019年3月27日
 * 
 * @author nt
 */
public class DesktopStrategyDetailResponse extends DefaultResponse {

    private CloudDesktopDetailDTO cloudDesktopDTO;

    public DesktopStrategyDetailResponse(CloudDesktopDetailDTO cloudDesktopDTO) {
        this.cloudDesktopDTO = cloudDesktopDTO;
    }

    public CloudDesktopDetailDTO getCloudDesktopDTO() {
        return cloudDesktopDTO;
    }

    public void setCloudDesktopDTO(CloudDesktopDetailDTO cloudDesktopDTO) {
        this.cloudDesktopDTO = cloudDesktopDTO;
    }
}
