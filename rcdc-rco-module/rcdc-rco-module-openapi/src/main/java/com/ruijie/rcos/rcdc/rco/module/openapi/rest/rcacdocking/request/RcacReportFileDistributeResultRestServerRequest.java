package com.ruijie.rcos.rcdc.rco.module.openapi.rest.rcacdocking.request;

import com.ruijie.rcos.rcdc.rco.module.def.api.dto.filedistribution.DistributeTaskResultDTO;
import com.ruijie.rcos.sk.base.annotation.NotNull;

import java.util.List;

/**
 * Description: RCAC报告应用客户端文件分发任务结果
 * Copyright: Copyright (c) 2022
 * Company: Ruijie Co., Ltd.
 * Create Time: 2022/2/24 11:53
 *
 * @author zhangyichi
 */
public class RcacReportFileDistributeResultRestServerRequest {

    @NotNull
    List<DistributeTaskResultDTO> resultList;

    public List<DistributeTaskResultDTO> getResultList() {
        return resultList;
    }

    public void setResultList(List<DistributeTaskResultDTO> resultList) {
        this.resultList = resultList;
    }
}
