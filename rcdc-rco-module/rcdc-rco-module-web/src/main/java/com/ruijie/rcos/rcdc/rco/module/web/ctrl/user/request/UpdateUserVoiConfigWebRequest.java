package com.ruijie.rcos.rcdc.rco.module.web.ctrl.user.request;

import com.ruijie.rcos.rcdc.rco.module.web.ctrl.terminal.vo.VoiDesktopConfigVO;
import com.ruijie.rcos.sk.base.annotation.NotNull;
import com.ruijie.rcos.sk.webmvc.api.request.WebRequest;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import org.springframework.lang.Nullable;

import java.util.UUID;

/**
 * Description: 更新用户VOI配置信息提交的数据对象
 * Copyright: Copyright (c) 2018
 * Company: Ruijie Co., Ltd.
 * Create Time: 2018/12/12
 *
 * @author linrenjian
 */
@ApiModel("编辑VOI云桌面请求体")
public class UpdateUserVoiConfigWebRequest implements WebRequest {

    @ApiModelProperty(value = "用户ID",required = true)
    @NotNull
    private UUID id;

    @ApiModelProperty(value = "VOI云桌面配置")
    @Nullable
    private VoiDesktopConfigVO voiDesktopConfig;


    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    @Nullable
    public VoiDesktopConfigVO getVoiDesktopConfig() {
        return voiDesktopConfig;
    }

    public void setVoiDesktopConfig(@Nullable VoiDesktopConfigVO voiDesktopConfig) {
        this.voiDesktopConfig = voiDesktopConfig;
    }
}
