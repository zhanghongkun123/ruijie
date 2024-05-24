package com.ruijie.rcos.rcdc.rco.module.openapi.rest.migration.user.request;

import com.ruijie.rcos.sk.base.annotation.NotEmpty;

/**
 * Description:
 * Copyright: Copyright (c) 2022
 * Company: Ruijie Co., Ltd.
 * Create Time: 2022.04.02
 *
 * @author chenl
 */
public class ImportRelationRequest {

    @NotEmpty
    private ImportRelationDataRequest[] dataArr;

    public ImportRelationDataRequest[] getDataArr() {
        return dataArr;
    }

    public void setDataArr(ImportRelationDataRequest[] dataArr) {
        this.dataArr = dataArr;
    }
}
