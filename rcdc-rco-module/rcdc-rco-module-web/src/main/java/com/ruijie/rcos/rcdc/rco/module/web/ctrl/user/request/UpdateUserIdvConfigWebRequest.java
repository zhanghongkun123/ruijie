package com.ruijie.rcos.rcdc.rco.module.web.ctrl.user.request;

import com.ruijie.rcos.rcdc.rco.module.web.ctrl.terminal.vo.IdvDesktopConfigVO;
import com.ruijie.rcos.sk.base.annotation.NotNull;
import com.ruijie.rcos.sk.webmvc.api.request.WebRequest;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import org.springframework.lang.Nullable;

import java.util.UUID;

/**
 * Description: 更新用户IDV配置信息提交的数据对象
 * Copyright: Copyright (c) 2018
 * Company: Ruijie Co., Ltd.
 * Create Time: 2018/12/12
 *
 * @author Jarman
 */
@ApiModel("编辑IDV云桌面请求体")
public class UpdateUserIdvConfigWebRequest implements WebRequest {

    @ApiModelProperty(value = "用户ID",required = true)
    @NotNull
    private UUID id;

    @ApiModelProperty(value = "IDV云桌面配置")
    @Nullable
    private IdvDesktopConfigVO idvDesktopConfig;


    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }


    @Nullable
    public IdvDesktopConfigVO getIdvDesktopConfig() {
        return idvDesktopConfig;
    }

    public void setIdvDesktopConfig(@Nullable IdvDesktopConfigVO idvDesktopConfig) {
        this.idvDesktopConfig = idvDesktopConfig;
    }

}
