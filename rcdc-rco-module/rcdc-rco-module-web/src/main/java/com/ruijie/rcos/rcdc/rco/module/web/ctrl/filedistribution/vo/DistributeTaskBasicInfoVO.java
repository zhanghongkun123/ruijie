package com.ruijie.rcos.rcdc.rco.module.web.ctrl.filedistribution.vo;

import com.ruijie.rcos.rcdc.rco.module.def.api.dto.filedistribution.DistributeTaskDTO;

/**
 * Description: 文件分发任务（父任务）基本信息VO
 * Copyright: Copyright (c) 2022
 * Company: Ruijie Co., Ltd.
 * Create Time: 2022/2/25 15:49
 *
 * @author zhangyichi
 */
public class DistributeTaskBasicInfoVO extends DistributeTaskDTO {
    private DistributeParameterVO parameter;

    public DistributeParameterVO getParameter() {
        return parameter;
    }

    public void setParameter(DistributeParameterVO parameter) {
        this.parameter = parameter;
    }

}
