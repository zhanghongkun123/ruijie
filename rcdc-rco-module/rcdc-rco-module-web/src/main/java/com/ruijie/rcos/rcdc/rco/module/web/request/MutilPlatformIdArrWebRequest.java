package com.ruijie.rcos.rcdc.rco.module.web.request;

import com.ruijie.rcos.sk.webmvc.api.request.IdArrWebRequest;
import org.springframework.lang.Nullable;

/**
 * Description: Function Description
 * Copyright: Copyright (c) 2024
 * Company: Ruijie Co., Ltd.
 * Create Time: 2024年03月08日
 *
 * @author 徐国祥
 */
public class MutilPlatformIdArrWebRequest extends IdArrWebRequest {
    @Nullable
    private Boolean shouldOnlyDeleteDataFromDb;

    @Nullable
    public Boolean getShouldOnlyDeleteDataFromDb() {
        return shouldOnlyDeleteDataFromDb;
    }

    public void setShouldOnlyDeleteDataFromDb(@Nullable Boolean shouldOnlyDeleteDataFromDb) {
        this.shouldOnlyDeleteDataFromDb = shouldOnlyDeleteDataFromDb;
    }
}
