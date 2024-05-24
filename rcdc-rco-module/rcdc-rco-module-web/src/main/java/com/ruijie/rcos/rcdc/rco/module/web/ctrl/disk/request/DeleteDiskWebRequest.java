package com.ruijie.rcos.rcdc.rco.module.web.ctrl.disk.request;

import java.util.UUID;

import com.ruijie.rcos.sk.base.annotation.NotEmpty;
import com.ruijie.rcos.sk.webmvc.api.request.WebRequest;

import io.swagger.annotations.ApiModelProperty;
import org.springframework.lang.Nullable;

/**
 * Description:
 * Copyright: Copyright (c) 2022
 * Company: Ruijie Co., Ltd.
 * Create Time: 2022/7/13
 *
 * @author TD
 */
public class DeleteDiskWebRequest implements WebRequest {

    /**
     * 磁盘ID集合
     */
    @NotEmpty
    @ApiModelProperty(value = "磁盘ID集合", required = true)
    private UUID[] idArr;

    @Nullable
    private Boolean shouldOnlyDeleteDataFromDb;

    public UUID[] getIdArr() {
        return idArr;
    }

    public void setIdArr(UUID[] idArr) {
        this.idArr = idArr;
    }

    @Nullable
    public Boolean getShouldOnlyDeleteDataFromDb() {
        return shouldOnlyDeleteDataFromDb;
    }

    public void setShouldOnlyDeleteDataFromDb(@Nullable Boolean shouldOnlyDeleteDataFromDb) {
        this.shouldOnlyDeleteDataFromDb = shouldOnlyDeleteDataFromDb;
    }
}
