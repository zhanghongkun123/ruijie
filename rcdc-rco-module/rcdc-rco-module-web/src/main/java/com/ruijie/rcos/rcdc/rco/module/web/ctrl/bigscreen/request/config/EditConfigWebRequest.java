package com.ruijie.rcos.rcdc.rco.module.web.ctrl.bigscreen.request.config;

import com.ruijie.rcos.rcdc.rco.module.def.api.dto.bigscreen.EditCommonConfigDTO;
import com.ruijie.rcos.sk.base.annotation.NotEmpty;
import com.ruijie.rcos.sk.webmvc.api.request.WebRequest;

/**
 * Description: 编辑配置项提交的数据
 * Copyright: Copyright (c) 2019
 * Company: Ruijie Co., Ltd.
 * Create Time: 2019/7/24 15:16
 *
 * @author BaiGuoliang
 */
public class EditConfigWebRequest implements WebRequest {

    @NotEmpty
    private EditCommonConfigDTO[] configArr;

    public EditCommonConfigDTO[] getConfigArr() {
        return configArr;
    }

    public void setConfigArr(EditCommonConfigDTO[] configArr) {
        this.configArr = configArr;
    }
}
