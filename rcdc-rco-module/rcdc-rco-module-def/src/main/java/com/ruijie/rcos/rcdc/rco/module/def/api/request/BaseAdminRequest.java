package com.ruijie.rcos.rcdc.rco.module.def.api.request;


import com.ruijie.rcos.sk.modulekit.api.comm.Request;
import java.util.UUID;
import org.springframework.lang.Nullable;

/**
 * Description: Function Description
 * Copyright: Copyright (c) 2019
 * Company: Ruijie Co., Ltd.
 * Create Time: 2020年01月15日
 *
 * @author ljm
 */
public class BaseAdminRequest implements Request {

    @Nullable
    private UUID id;

    @Nullable
    private String adminName;




    public BaseAdminRequest() {
    }

    public BaseAdminRequest(String adminName) {
        this.adminName = adminName;
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    @Nullable
    public String getAdminName() {
        return adminName;
    }

    public void setAdminName(@Nullable String adminName) {
        this.adminName = adminName;
    }

}
