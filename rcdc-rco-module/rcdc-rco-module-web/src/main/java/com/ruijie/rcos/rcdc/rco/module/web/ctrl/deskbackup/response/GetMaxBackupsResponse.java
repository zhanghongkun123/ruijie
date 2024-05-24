package com.ruijie.rcos.rcdc.rco.module.web.ctrl.deskbackup.response;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 * Description: description
 * Copyright: Copyright (c) 2021
 * Company: Ruijie Co., Ltd.
 * Create Time: 2021/5/10
 *
 * @author wuShengQiang
 */
@ApiModel("最大备份数返回体")
public class GetMaxBackupsResponse {

    @ApiModelProperty(value = "云桌面备份数量最大限制数")
    private Integer maxBackups;

    public GetMaxBackupsResponse(Integer maxBackups) {
        this.maxBackups = maxBackups;
    }

    public Integer getMaxBackups() {
        return maxBackups;
    }

    public void setMaxBackups(Integer maxBackups) {
        this.maxBackups = maxBackups;
    }
}
