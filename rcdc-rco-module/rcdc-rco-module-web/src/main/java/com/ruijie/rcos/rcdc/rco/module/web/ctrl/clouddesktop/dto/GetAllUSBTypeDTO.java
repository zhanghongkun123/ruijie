package com.ruijie.rcos.rcdc.rco.module.web.ctrl.clouddesktop.dto;

import com.ruijie.rcos.rcdc.clouddesktop.module.def.dto.CbbUSBTypeDTO;

/**
 * Description: Function Description
 * Copyright: Copyright (c) 2020
 * Company: Ruijie Co., Ltd.
 * Create Time: 2020/8/4
 *
 * @author hs
 */
public class GetAllUSBTypeDTO {

    private CbbUSBTypeDTO[] itemArr;

    public GetAllUSBTypeDTO(CbbUSBTypeDTO[] itemArr) {
        this.itemArr = itemArr;
    }

    public CbbUSBTypeDTO[] getItemArr() {
        return itemArr;
    }

    public void setItemArr(CbbUSBTypeDTO[] itemArr) {
        this.itemArr = itemArr;
    }
}
