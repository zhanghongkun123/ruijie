package com.ruijie.rcos.rcdc.rco.module.def.api.response;

import com.ruijie.rcos.rcdc.rco.module.def.api.dto.ExportRcaHostCacheDTO;
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
public class GetExportRcaHostCacheResponse extends DefaultResponse {

    private ExportRcaHostCacheDTO rcaHostCacheDTO;

    public GetExportRcaHostCacheResponse() { }

    public GetExportRcaHostCacheResponse(ExportRcaHostCacheDTO rcaHostCacheDTO) {
        this.rcaHostCacheDTO = rcaHostCacheDTO;
    }

    public ExportRcaHostCacheDTO getRcaHostCacheDTO() {
        return rcaHostCacheDTO;
    }

    public void setRcaHostCacheDTO(ExportRcaHostCacheDTO rcaHostCacheDTO) {
        this.rcaHostCacheDTO = rcaHostCacheDTO;
    }
}
