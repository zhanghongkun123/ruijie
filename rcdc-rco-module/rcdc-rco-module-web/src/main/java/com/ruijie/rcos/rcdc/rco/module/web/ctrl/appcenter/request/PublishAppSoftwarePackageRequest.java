package com.ruijie.rcos.rcdc.rco.module.web.ctrl.appcenter.request;


import com.ruijie.rcos.sk.base.annotation.NotNull;
import org.springframework.lang.Nullable;

import java.io.Serializable;
import java.util.UUID;

/**
 * Description: Function Description
 * Copyright: Copyright (c) 2021
 * Company: Ruijie Co., Ltd.
 * Create Time: 2022年6月21日
 *
 * @author chenl
 */
public class PublishAppSoftwarePackageRequest implements Serializable {

    @NotNull
    private UUID id;


    public PublishAppSoftwarePackageRequest() {

    }

    public PublishAppSoftwarePackageRequest(UUID id) {
        this.id = id;
    }


    @Nullable
    public UUID getId() {
        return id;
    }

    public void setId(@Nullable UUID id) {
        this.id = id;
    }

}
