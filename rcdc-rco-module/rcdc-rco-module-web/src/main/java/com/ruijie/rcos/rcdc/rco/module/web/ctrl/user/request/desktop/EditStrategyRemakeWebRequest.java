package com.ruijie.rcos.rcdc.rco.module.web.ctrl.user.request.desktop;

import com.ruijie.rcos.sk.base.annotation.NotEmpty;
import com.ruijie.rcos.sk.base.annotation.TextName;
import org.springframework.lang.Nullable;
import com.ruijie.rcos.sk.base.annotation.TextShort;
import io.swagger.annotations.ApiModelProperty;
import java.util.UUID;

/**
 * 编辑云桌面的备注请求
 * Description: Function Description
 * Copyright: Copyright (c) 2018
 * Company: Ruijie Co., Ltd.
 * Create Time: 2021年02月02日
 *
 * @author linrenjian
 */
public class EditStrategyRemakeWebRequest {

    @ApiModelProperty(value = "id数组", required = true)
    @NotEmpty
    private UUID[] idArr;

    @ApiModelProperty(value = "云桌面标签")
    @Nullable
    @TextName
    @TextShort
    private String remark;

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public UUID[] getIdArr() {
        return idArr;
    }

    public void setIdArr(UUID[] idArr) {
        this.idArr = idArr;
    }
}
