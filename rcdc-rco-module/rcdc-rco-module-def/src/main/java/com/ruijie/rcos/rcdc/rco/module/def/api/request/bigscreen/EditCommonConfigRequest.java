package com.ruijie.rcos.rcdc.rco.module.def.api.request.bigscreen;

import com.ruijie.rcos.rcdc.rco.module.def.api.dto.bigscreen.EditCommonConfigDTO;
import com.ruijie.rcos.sk.base.annotation.NotEmpty;
import com.ruijie.rcos.sk.modulekit.api.comm.Request;

/**
 *
 * Description: 编辑通用配置请求
 * Copyright: Copyright (c) 2019
 * Company: Ruijie Co., Ltd.
 * Create Time: 2019年7月24日
 *
 * @author BaiGuoliang
 */
public class EditCommonConfigRequest implements Request {

    @NotEmpty
    private EditCommonConfigDTO[] configArr;

    public EditCommonConfigDTO[] getConfigArr() {
        return configArr;
    }

    public void setConfigArr(EditCommonConfigDTO[] configArr) {
        this.configArr = configArr;
    }
}