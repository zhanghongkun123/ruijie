package com.ruijie.rcos.rcdc.rco.module.def.userprofile.dto;

import com.ruijie.rcos.sk.modulekit.api.comm.DefaultResponse;

/**
 * Description: 获取导出路径信息响应对象
 * Copyright: Copyright (c) 2022
 * Company: Ruijie Co., Ltd.
 * Create Time: 2022/4/11
 *
 * @author WuShengQiang
 */
public class ExportUserProfilePathCacheResponse extends DefaultResponse {

    private ExportUserProfilePathCacheDTO exportUserProfilePathCacheDTO;

    public ExportUserProfilePathCacheResponse() {
    }

    public ExportUserProfilePathCacheResponse(ExportUserProfilePathCacheDTO exportUserProfilePathCacheDTO) {
        this.exportUserProfilePathCacheDTO = exportUserProfilePathCacheDTO;
    }

    public ExportUserProfilePathCacheDTO getExportUserProfilePathCacheDTO() {
        return exportUserProfilePathCacheDTO;
    }

    public void setExportUserProfilePathCacheDTO(ExportUserProfilePathCacheDTO exportUserProfilePathCacheDTO) {
        this.exportUserProfilePathCacheDTO = exportUserProfilePathCacheDTO;
    }
}
