package com.ruijie.rcos.rcdc.rco.module.web.ctrl.terminal.vo;

import com.ruijie.rcos.rcdc.terminal.module.def.api.dto.CbbTerminalDetectDTO;
import com.ruijie.rcos.rcdc.terminal.module.def.api.dto.CbbTerminalDetectStatisticsDTO;
import com.ruijie.rcos.rcdc.terminal.module.def.api.dto.CbbTerminalDetectThresholdDTO;

/**
 * 
 * Description: 终端检测列表响应内容
 * Copyright: Copyright (c) 2018
 * Company: Ruijie Co., Ltd.
 * Create Time: 2018年12月28日
 * 
 * @author nt
 */
public class TerminalDetectListContentVO {

    private CbbTerminalDetectDTO[] itemArr;

    private long total;

    private CbbTerminalDetectStatisticsDTO result;

    private CbbTerminalDetectThresholdDTO threshold;

    public CbbTerminalDetectDTO[] getItemArr() {
        return itemArr;
    }

    public void setItemArr(CbbTerminalDetectDTO[] itemArr) {
        this.itemArr = itemArr;
    }

    public long getTotal() {
        return total;
    }

    public void setTotal(long total) {
        this.total = total;
    }

    public CbbTerminalDetectStatisticsDTO getResult() {
        return result;
    }

    public void setResult(CbbTerminalDetectStatisticsDTO result) {
        this.result = result;
    }

    public CbbTerminalDetectThresholdDTO getThreshold() {
        return threshold;
    }

    public void setThreshold(CbbTerminalDetectThresholdDTO threshold) {
        this.threshold = threshold;
    }
}
