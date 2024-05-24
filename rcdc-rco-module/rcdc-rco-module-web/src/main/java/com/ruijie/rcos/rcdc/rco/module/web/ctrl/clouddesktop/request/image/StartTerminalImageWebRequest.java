package com.ruijie.rcos.rcdc.rco.module.web.ctrl.clouddesktop.request.image;

import java.util.UUID;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.enums.CbbDriverInstallMode;
import com.ruijie.rcos.sk.base.annotation.NotBlank;
import com.ruijie.rcos.sk.base.annotation.NotNull;
import com.ruijie.rcos.sk.webmvc.api.request.WebRequest;
import io.swagger.annotations.ApiModelProperty;
import org.springframework.lang.Nullable;

/**
 * Description: 开启终端的web请求
 * Copyright: Copyright (c) 2019
 * Company: Ruijie Co., Ltd.
 * Create Time: 2020/2/20
 *
 * @author songxiang
 */
public class StartTerminalImageWebRequest implements WebRequest {

    @NotNull
    @ApiModelProperty(value = "镜像模板ID", required = true)
    private UUID id;

    @NotBlank
    @ApiModelProperty(value = "终端Id", required = true)
    private String terminalId;

    @Nullable
    @ApiModelProperty(value = "CPU类型", required = true)
    private String cpuType;

    @NotNull
    @ApiModelProperty(value = "安装类型", required = true)
    private CbbDriverInstallMode mode;

    @Nullable
    @ApiModelProperty(value = "终端产品ID", required = true)
    private String productId;

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getTerminalId() {
        return terminalId;
    }

    public void setTerminalId(String terminalId) {
        this.terminalId = terminalId;
    }

    public String getCpuType() {
        return cpuType;
    }

    public void setCpuType(String cpuType) {
        this.cpuType = cpuType;
    }

    public CbbDriverInstallMode getMode() {
        return mode;
    }

    public void setMode(CbbDriverInstallMode mode) {
        this.mode = mode;
    }

    public String getProductId() {
        return productId;
    }

    public void setProductId(String productId) {
        this.productId = productId;
    }
}
