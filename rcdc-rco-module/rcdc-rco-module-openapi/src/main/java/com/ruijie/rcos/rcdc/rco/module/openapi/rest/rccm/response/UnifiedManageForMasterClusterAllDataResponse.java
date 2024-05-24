package com.ruijie.rcos.rcdc.rco.module.openapi.rest.rccm.response;

import com.ruijie.rcos.rcdc.rco.module.def.api.dto.rccm.UnifiedManageForMasterClusterAllDataDTO;

import java.util.List;

/**
 * Description: 统一管理全量数据收集
 * Copyright: Copyright (c) 2022
 * Company: Ruijie Co., Ltd.
 * Create Time: 2023/4/21
 *
 * @author WuShengQiang
 */
public class UnifiedManageForMasterClusterAllDataResponse {

    private List<UnifiedManageForMasterClusterAllDataDTO> masterClusterAllDataList;

    public List<UnifiedManageForMasterClusterAllDataDTO> getMasterClusterAllDataList() {
        return masterClusterAllDataList;
    }

    public void setMasterClusterAllDataList(List<UnifiedManageForMasterClusterAllDataDTO> masterClusterAllDataList) {
        this.masterClusterAllDataList = masterClusterAllDataList;
    }
}
