package com.ruijie.rcos.rcdc.rco.module.web.ctrl.desktoppool.request;

import java.util.UUID;

import com.ruijie.rcos.sk.base.annotation.NotEmpty;
import io.swagger.annotations.ApiModelProperty;
import org.springframework.lang.Nullable;

/**
 * Description: 删除桌面池request
 * Copyright: Copyright (c) 2021
 * Company: Ruijie Co., Ltd.
 * Create Time: 2021/10/18 6:03 下午
 *
 * @author zhouhuan
 */
public class DeleteDesktopPoolRequest {

    @ApiModelProperty(value = "桌面池ID")
    @NotEmpty
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
