package com.ruijie.rcos.rcdc.rco.module.web.ctrl.adgroup.request;

import com.ruijie.rcos.rcdc.rco.module.web.ctrl.adgroup.dto.CreateAdGroupWebDTO;
import com.ruijie.rcos.sk.base.annotation.NotEmpty;
import com.ruijie.rcos.sk.webmvc.api.request.WebRequest;

/**
 * Description: 创建Ad域组请求体
 * Copyright: Copyright (c) 2022
 * Company: Ruijie Co., Ltd.
 * Create Time: 2022-09-26
 *
 * @author zqj
 */
public class BatchCreateAdGroupWebRequest implements WebRequest {

    @NotEmpty
    private CreateAdGroupWebDTO[] adGroupArr;

    public CreateAdGroupWebDTO[] getAdGroupArr() {
        return adGroupArr;
    }

    public void setAdGroupArr(CreateAdGroupWebDTO[] adGroupArr) {
        this.adGroupArr = adGroupArr;
    }
}
