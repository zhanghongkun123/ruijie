package com.ruijie.rcos.rcdc.rco.module.def.api.response;

import com.ruijie.rcos.rcdc.rco.module.def.user.dto.ExportUserInfoDTO;
import com.ruijie.rcos.sk.modulekit.api.comm.DefaultResponse;

/**
 * 
 * Description: 获取缓存信息
 * Copyright: Copyright (c) 2023
 * Company: Ruijie Co., Ltd.
 * Create Time: 2023/2/24
 *
 * @author tangxu
 */
public class GetExportUserCacheResponse extends DefaultResponse {

    private ExportUserInfoDTO exportUserInfoDTO;

    public GetExportUserCacheResponse(ExportUserInfoDTO exportUserInfoDTO) {
        this.exportUserInfoDTO = exportUserInfoDTO;
    }

    public ExportUserInfoDTO getExportUserInfoDTO() {
        return exportUserInfoDTO;
    }

    public void setExportUserInfoDTO(ExportUserInfoDTO exportUserInfoDTO) {
        this.exportUserInfoDTO = exportUserInfoDTO;
    }

}
