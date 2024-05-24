package com.ruijie.rcos.rcdc.rco.module.def.softwarecontrol.dto;

import com.ruijie.rcos.sk.modulekit.api.comm.DefaultResponse;

/**
 * 
 * Description: 获取缓存信息
 * Copyright: Copyright (c) 2021
 * Company: Ruijie Co., Ltd.
 * Create Time: 2021/7/16
 *
 * @author lihengjing
 */
public class GetExportSoftwareCacheResponse extends DefaultResponse {

    private ExportSoftwareCacheDTO softwareCacheDTO;

    public GetExportSoftwareCacheResponse() {  }

    public GetExportSoftwareCacheResponse(ExportSoftwareCacheDTO softwareCacheDTO) {
        this.softwareCacheDTO = softwareCacheDTO;
    }

    public ExportSoftwareCacheDTO getSoftwareCacheDTO() {
        return softwareCacheDTO;
    }

    public void setSoftwareCacheDTO(ExportSoftwareCacheDTO softwareCacheDTO) {
        this.softwareCacheDTO = softwareCacheDTO;
    }
}
