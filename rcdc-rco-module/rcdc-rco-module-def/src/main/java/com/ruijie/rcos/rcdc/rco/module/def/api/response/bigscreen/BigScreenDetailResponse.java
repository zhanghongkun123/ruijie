package com.ruijie.rcos.rcdc.rco.module.def.api.response.bigscreen;

import com.ruijie.rcos.rcdc.rco.module.def.api.dto.bigscreen.CommonConfigDTO;
import com.ruijie.rcos.sk.modulekit.api.comm.DefaultResponse;

/**
 *
 * Description: 大屏详细信息
 * Copyright: Copyright (c) 2019
 * Company: Ruijie Co., Ltd.
 * Create Time: 2019年7月24日
 *
 * @author BaiGuoliang
 */
public class BigScreenDetailResponse extends DefaultResponse {

    private CommonConfigDTO[] configArr;

    public CommonConfigDTO[] getConfigArr() {
        return configArr;
    }

    public void setConfigArr(CommonConfigDTO[] configArr) {
        this.configArr = configArr;
    }
}