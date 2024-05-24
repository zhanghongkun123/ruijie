package com.ruijie.rcos.rcdc.rco.module.web.ctrl.computername.request;

import java.util.UUID;
import com.ruijie.rcos.sk.base.annotation.NotBlank;
import com.ruijie.rcos.sk.base.annotation.NotNull;
import com.ruijie.rcos.sk.webmvc.api.request.WebRequest;
import io.swagger.annotations.ApiModelProperty;

/**
 * 
 * Description: Function Description
 * Copyright: Copyright (c) 2020
 * Company: Ruijie Co., Ltd.
 * Create Time: 2020年10月10日
 * 
 * @author wjp
 */
public class CheckComputerNameWebRequest implements WebRequest {

    @ApiModelProperty(value = "云桌面ID", required = true)
    @NotNull
    private UUID id;

    @ApiModelProperty(value = "云桌面计算机名", required = true)
    @NotBlank
    private String computerName;

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getComputerName() {
        return computerName;
    }

    public void setComputerName(String computerName) {
        this.computerName = computerName;
    }
}
