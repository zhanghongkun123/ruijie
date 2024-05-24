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
public class ImportUserGroupRequest {


    @NotEmpty
    private ImportUserGroupDataRequest[] dataArr;

    public ImportUserGroupDataRequest[] getDataArr() {
        return dataArr;
    }

    public void setDataArr(ImportUserGroupDataRequest[] dataArr) {
        this.dataArr = dataArr;
    }
}
