package com.ruijie.rcos.rcdc.rco.module.def.api.dto.desk;

import java.util.List;

/**
 * Description: 检测端口结果DTO
 * Copyright: Copyright (c) 2023
 * Company: Ruijie Co., Ltd
 * Create Time: 2023/8/8
 *
 * @author chenjuan
 */
public class CheckDesktopPortResultDTO {

    List<Integer> successPortList;

    List<Integer> errorPortList;

    public List<Integer> getSuccessPortList() {
        return successPortList;
    }

    public void setSuccessPortList(List<Integer> successPortList) {
        this.successPortList = successPortList;
    }

    public List<Integer> getErrorPortList() {
        return errorPortList;
    }

    public void setErrorPortList(List<Integer> errorPortList) {
        this.errorPortList = errorPortList;
    }
}
