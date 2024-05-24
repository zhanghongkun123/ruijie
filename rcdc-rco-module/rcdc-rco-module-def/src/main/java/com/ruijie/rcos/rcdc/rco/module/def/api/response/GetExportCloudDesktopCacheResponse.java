package com.ruijie.rcos.rcdc.rco.module.def.api.response;

import com.ruijie.rcos.rcdc.rco.module.def.api.dto.ExportCloudDesktopCacheDTO;

import com.ruijie.rcos.sk.modulekit.api.comm.DefaultResponse;

/**
 * 
 * Description: 获取缓存信息
 * Copyright: Copyright (c) 2018
 * Company: Ruijie Co., Ltd.
 * Create Time: 2020/9/16
 *
 * @author zhiweiHong
 */
public class GetExportCloudDesktopCacheResponse extends DefaultResponse {
    
    private ExportCloudDesktopCacheDTO cloudDesktopCacheDTO;

    public GetExportCloudDesktopCacheResponse()  { }

    public GetExportCloudDesktopCacheResponse(ExportCloudDesktopCacheDTO cloudDesktopCacheDTO) {
        this.cloudDesktopCacheDTO = cloudDesktopCacheDTO;
    }

    public ExportCloudDesktopCacheDTO getCloudDesktopCacheDTO() {
        return cloudDesktopCacheDTO;
    }

    public void setCloudDesktopCacheDTO(ExportCloudDesktopCacheDTO cloudDesktopCacheDTO) {
        this.cloudDesktopCacheDTO = cloudDesktopCacheDTO;
    }
}
