package com.ruijie.rcos.rcdc.rco.module.web.ctrl.clouddesktop.request.image;

import com.ruijie.rcos.sk.base.annotation.NotEmpty;
import com.ruijie.rcos.sk.webmvc.api.request.WebRequest;
import org.springframework.lang.Nullable;

import java.util.UUID;

/**
 * <br>
 * Description:  Function Description <br>
 * Copyright: Copyright (c) 2019 <br>
 * Company: Ruijie Co., Ltd.  <br>
 * Create Time: 2019/3/29  <br>
 *
 * @author yyz
 */
public class DeleteImageTemplateWebRequest implements WebRequest {

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
