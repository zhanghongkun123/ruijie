package com.ruijie.rcos.rcdc.rco.module.def.desktoppool.request;

import com.ruijie.rcos.rcdc.clouddesktop.module.def.dto.deskspec.CbbDeskSpecDTO;
import com.ruijie.rcos.sk.base.annotation.NotNull;

import java.io.Serializable;
import java.util.UUID;

/**
 * Description:
 * Copyright: Copyright (c) 2023
 * Company: Ruijie Co., Ltd.
 * Create Time: 2024/2/4
 *
 * @author linke
 */
public class EditPoolDeskSpecRequest implements Serializable {

    /**
     * 池ID
     */
    @NotNull
    private UUID id;

    /**
     * 池规格
     */
    @NotNull
    private CbbDeskSpecDTO deskSpec;

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public CbbDeskSpecDTO getDeskSpec() {
        return deskSpec;
    }

    public void setDeskSpec(CbbDeskSpecDTO deskSpec) {
        this.deskSpec = deskSpec;
    }
}
