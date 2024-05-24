package com.ruijie.rcos.rcdc.rco.module.def.api.dto.rccm;

import com.ruijie.rcos.rcdc.rco.module.def.api.enums.rccm.UnifiedManageFunctionKeyEnum;

import java.util.List;
import java.util.UUID;

/**
 * Description: 主集群的全量数据DTO
 * Copyright: Copyright (c) 2022
 * Company: Ruijie Co., Ltd.
 * Create Time: 2023/4/21
 *
 * @author WuShengQiang
 */
public class UnifiedManageForMasterClusterAllDataDTO {

    /**
     * 功能Key
     */
    private UnifiedManageFunctionKeyEnum functionKey;

    /**
     * 全量同步数据
     */
    private List<UUID> unifiedManageIdList;

    public UnifiedManageFunctionKeyEnum getFunctionKey() {
        return functionKey;
    }

    public void setFunctionKey(UnifiedManageFunctionKeyEnum functionKey) {
        this.functionKey = functionKey;
    }

    public List<UUID> getUnifiedManageIdList() {
        return unifiedManageIdList;
    }

    public void setUnifiedManageIdList(List<UUID> unifiedManageIdList) {
        this.unifiedManageIdList = unifiedManageIdList;
    }
}
