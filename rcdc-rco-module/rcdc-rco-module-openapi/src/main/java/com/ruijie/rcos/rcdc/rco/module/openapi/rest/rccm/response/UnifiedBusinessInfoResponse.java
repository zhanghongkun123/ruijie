package com.ruijie.rcos.rcdc.rco.module.openapi.rest.rccm.response;

import com.ruijie.rcos.rcdc.rco.module.def.api.enums.rccm.UnifiedManageFunctionKeyEnum;

import java.util.UUID;

/**
 * Description:
 * Copyright: Copyright (c) 2023
 * Company: Ruijie Co., Ltd.
 * Create Time: 2023/7/2 21:59
 *
 * @author yxq
 */
public class UnifiedBusinessInfoResponse {

    /**
     * 业务id
     */
    private UUID businessId;

    /**
     * 功能Key
     */
    private UnifiedManageFunctionKeyEnum functionKey;

    public UUID getBusinessId() {
        return businessId;
    }

    public void setBusinessId(UUID businessId) {
        this.businessId = businessId;
    }

    public UnifiedManageFunctionKeyEnum getFunctionKey() {
        return functionKey;
    }

    public void setFunctionKey(UnifiedManageFunctionKeyEnum functionKey) {
        this.functionKey = functionKey;
    }
}
