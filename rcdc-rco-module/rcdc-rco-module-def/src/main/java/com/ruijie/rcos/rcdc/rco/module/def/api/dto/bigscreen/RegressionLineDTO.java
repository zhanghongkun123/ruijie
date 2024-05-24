package com.ruijie.rcos.rcdc.rco.module.def.api.dto.bigscreen;

import java.util.Date;

/**
 * Description: 服务器线性回归起始点数据
 * Copyright: Copyright (c) 2019
 * Company: Ruijie Co., Ltd.
 * Create Time: 2019/8/5
 *
 * @author brq
 */
public class RegressionLineDTO {

    private Date date;

    private Double resourceUsage;

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public Double getResourceUsage() {
        return resourceUsage;
    }

    public void setResourceUsage(Double resourceUsage) {
        this.resourceUsage = resourceUsage;
    }
}
