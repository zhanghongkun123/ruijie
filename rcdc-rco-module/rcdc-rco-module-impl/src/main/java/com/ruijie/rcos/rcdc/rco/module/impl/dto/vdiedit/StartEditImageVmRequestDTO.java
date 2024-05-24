package com.ruijie.rcos.rcdc.rco.module.impl.dto.vdiedit;

import com.ruijie.rcos.rcdc.clouddesktop.module.def.enums.ExternalImageVmState;

/**
 * Description: Function Description
 * Copyright: Copyright (c) 2020
 * Company: Ruijie Co., Ltd.
 * Create Time: 2020/2/28 11:10
 *
 * @author zhangyichi
 */
public class StartEditImageVmRequestDTO extends VDIEditImageIdRequestDTO {

    private ExternalImageVmState vmState;

    public ExternalImageVmState getVmState() {
        return vmState;
    }

    public void setVmState(ExternalImageVmState vmState) {
        this.vmState = vmState;
    }
}
