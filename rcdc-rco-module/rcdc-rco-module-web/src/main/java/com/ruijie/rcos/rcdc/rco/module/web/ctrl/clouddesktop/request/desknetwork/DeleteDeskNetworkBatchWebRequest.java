package com.ruijie.rcos.rcdc.rco.module.web.ctrl.clouddesktop.request.desknetwork;

import java.util.UUID;

import com.ruijie.rcos.sk.base.annotation.NotEmpty;
import com.ruijie.rcos.sk.base.annotation.Size;
import com.ruijie.rcos.sk.webmvc.api.request.WebRequest;
import io.swagger.annotations.ApiModelProperty;
import org.springframework.lang.Nullable;

/**
 * Description: 删除批量网络策略的Web请求
 * Copyright: Copyright (c) 2018
 * Company: Ruijie Co., Ltd.
 * Create Time: 2019年3月18日
 * 
 * @author huangxiaodan
 */
public class DeleteDeskNetworkBatchWebRequest implements WebRequest {

    @ApiModelProperty(value = "网络策略ip数组", required = true)
    @NotEmpty
    @Size(min = 1)
    private UUID[] idArr;

    @ApiModelProperty(value = "是否仅仅将数据从数据库删除")
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
