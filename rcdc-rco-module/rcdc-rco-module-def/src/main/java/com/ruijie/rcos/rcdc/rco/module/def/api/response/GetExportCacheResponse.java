package com.ruijie.rcos.rcdc.rco.module.def.api.response;

import com.ruijie.rcos.rcdc.rco.module.def.api.dto.ExportCacheDTO;
import com.ruijie.rcos.sk.modulekit.api.comm.DefaultResponse;

/**
 *
 * Description: 获取缓存信息
 * Copyright: Copyright (c) 2022
 * Company: Ruijie Co., Ltd.
 * Create Time: 2022/04/13
 *
 * @author guoyongxin
 */
public class GetExportCacheResponse extends DefaultResponse {

    private ExportCacheDTO exportCacheDTO;

    public GetExportCacheResponse() {
    }

    public GetExportCacheResponse(ExportCacheDTO exportCacheDTO) {
        this.exportCacheDTO = exportCacheDTO;
    }

    public ExportCacheDTO getExportCacheDTO() {
        return exportCacheDTO;
    }

    public void setExportCacheDTO(ExportCacheDTO exportCacheDTO) {
        this.exportCacheDTO = exportCacheDTO;
    }
}
