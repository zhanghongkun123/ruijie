package com.ruijie.rcos.rcdc.rco.module.def.printer.request;

import com.ruijie.rcos.sk.base.annotation.NotNull;
import com.ruijie.rcos.sk.base.annotation.Size;
import com.ruijie.rcos.sk.webmvc.api.request.WebRequest;
import org.springframework.lang.Nullable;

import java.util.UUID;

/**
 * Description: Function Description
 * Copyright: Copyright (c) 2020
 * Company: Ruijie Co., Ltd.
 * Create Time: 2020/9/15
 *
 * @author chenjiehui
 */
public class EditPrinterConfigRequest implements WebRequest {

    @NotNull
    private UUID id;

    @NotNull
    @Size(max = 259, min = 1)
    private String configName;

    @Nullable
    @Size(max = 500)
    private String configDescription;


    public String getConfigName() {
        return configName;
    }

    public void setConfigName(String configName) {
        this.configName = configName;
    }

    public String getConfigDescription() {
        return configDescription;
    }

    public void setConfigDescription(String description) {
        this.configDescription = description;
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }
}
