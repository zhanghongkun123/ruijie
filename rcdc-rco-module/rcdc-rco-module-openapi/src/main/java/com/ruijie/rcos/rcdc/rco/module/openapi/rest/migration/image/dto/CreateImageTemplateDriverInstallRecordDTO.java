package com.ruijie.rcos.rcdc.rco.module.openapi.rest.migration.image.dto;

import com.ruijie.rcos.sk.base.annotation.NotBlank;
import com.ruijie.rcos.sk.base.annotation.NotNull;

/**
 * Description: Function Description
 * Copyright: Copyright (c) 2022
 * Company: Ruijie Co., Ltd.
 * Create Time: 2022年04月08日
 *
 * @author xgx
 */
public class CreateImageTemplateDriverInstallRecordDTO {
    @NotBlank
    private String id;

    @NotNull
    private CreateImageTemplateCpuDriverDTO supportCpu;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public CreateImageTemplateCpuDriverDTO getSupportCpu() {
        return supportCpu;
    }

    public void setSupportCpu(CreateImageTemplateCpuDriverDTO supportCpu) {
        this.supportCpu = supportCpu;
    }
}
