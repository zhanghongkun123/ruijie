package com.ruijie.rcos.rcdc.rco.module.web.ctrl.user.request.desktop;

import com.ruijie.rcos.sk.base.annotation.NotEmpty;
import com.ruijie.rcos.sk.base.annotation.Size;
import io.swagger.annotations.ApiModelProperty;
import org.springframework.lang.Nullable;

import java.util.UUID;

/**
 * Description: 检测桌面端口请求
 * Copyright: Copyright (c) 2023
 * Company: Ruijie Co., Ltd
 * Create Time: 2023/7/20
 *
 * @author chenjuan
 */
public class CheckDesktopPortWebRequest {

    @ApiModelProperty(value = "云桌面ID数组", required = true)
    @NotEmpty
    @Size(min = 1)
    private UUID[] idArr;

    @ApiModelProperty(value = "port数组")
    @NotEmpty
    @Size(max = 5)
    private Integer[] portArr;

    public UUID[] getIdArr() {
        return idArr;
    }

    public void setIdArr(UUID[] idArr) {
        this.idArr = idArr;
    }

    @Nullable
    public Integer[] getPortArr() {
        return portArr;
    }

    public void setPortArr(@Nullable Integer[] portArr) {
        this.portArr = portArr;
    }
}
