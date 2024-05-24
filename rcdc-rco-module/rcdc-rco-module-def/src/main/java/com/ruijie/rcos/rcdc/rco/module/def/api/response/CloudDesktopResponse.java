package com.ruijie.rcos.rcdc.rco.module.def.api.response;

import com.ruijie.rcos.rcdc.rco.module.def.api.dto.CloudDesktopDTO;
import com.ruijie.rcos.sk.modulekit.api.comm.DefaultResponse;
import org.springframework.util.Assert;

/**
 * 
 * Description: Function Description
 * Copyright: Copyright (c) 2018
 * Company: Ruijie Co., Ltd.
 * Create Time: 2019年5月8日
 * 
 * @author zhuangchenwu
 */
public class CloudDesktopResponse extends DefaultResponse {

    private CloudDesktopDTO cloudDesktopDTO;
    
    public CloudDesktopResponse(CloudDesktopDTO cloudDesktopDTO) {
        Assert.notNull(cloudDesktopDTO, "cloudDesktopDTO must not be null");
        this.cloudDesktopDTO = cloudDesktopDTO;
    }

    public CloudDesktopDTO getCloudDesktopDTO() {
        return cloudDesktopDTO;
    }

    public void setCloudDesktopDTO(CloudDesktopDTO cloudDesktopDTO) {
        this.cloudDesktopDTO = cloudDesktopDTO;
    }
    
    
}
