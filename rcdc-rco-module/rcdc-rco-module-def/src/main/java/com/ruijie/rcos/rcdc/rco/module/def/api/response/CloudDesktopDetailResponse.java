package com.ruijie.rcos.rcdc.rco.module.def.api.response;

import com.ruijie.rcos.rcdc.rco.module.def.api.dto.CloudDesktopDetailDTO;
import com.ruijie.rcos.sk.modulekit.api.comm.DefaultResponse;
import org.springframework.util.Assert;

/**
 * 云桌面页面响应类.
 * Description: Function Description
 * Copyright: Copyright (c) 2018
 * Company: Ruijie Co., Ltd.
 * Create Time: 2018年10月30日
 * 
 * @author chenzj
 */
public class CloudDesktopDetailResponse extends DefaultResponse {
    
    private CloudDesktopDetailDTO cloudDesktopDetailDTO;
    
    public CloudDesktopDetailResponse() {
    	setStatus(Status.SUCCESS);
    }
    
    public CloudDesktopDetailResponse(CloudDesktopDetailDTO cloudDesktopDetailDTO) {
        Assert.notNull(cloudDesktopDetailDTO, "cloudDesktopDetailDTO must not be null");
        this.cloudDesktopDetailDTO = cloudDesktopDetailDTO;
    }

    public CloudDesktopDetailDTO getCloudDesktopDetailDTO() {
        return cloudDesktopDetailDTO;
    }

    public void setCloudDesktopDetailDTO(CloudDesktopDetailDTO cloudDesktopDTO) {
        this.cloudDesktopDetailDTO = cloudDesktopDTO;
    }
}
