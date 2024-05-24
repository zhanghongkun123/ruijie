package com.ruijie.rcos.rcdc.rco.module.def.api.dto.filedistribution;

/**
 * Description: 分发任务（父任务）信息，带任务参数
 * Copyright: Copyright (c) 2022
 * Company: Ruijie Co., Ltd.
 * Create Time: 2022/2/18 16:00
 *
 * @author zhangyichi
 */
public class DistributeTaskParameterDTO extends DistributeTaskDTO {

    private DistributeParameterDTO parameter;

    public DistributeParameterDTO getParameter() {
        return parameter;
    }

    public void setParameter(DistributeParameterDTO parameter) {
        this.parameter = parameter;
    }

}
