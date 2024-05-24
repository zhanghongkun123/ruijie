package com.ruijie.rcos.rcdc.rco.module.web.ctrl.physicalserver.dto;

import com.ruijie.rcos.rcdc.clouddesktop.module.def.dto.PhysicalServerDTO;

import java.util.List;

/**
 * Description: Function Description
 * Copyright: Copyright (c) 2020
 * Company: Ruijie Co., Ltd.
 * Create Time: 2020/8/4
 *
 * @author hs
 */
public class ListAllPhysicalServerDTO {

    private List<PhysicalServerDTO> physicalServerDTOList;

    public List<PhysicalServerDTO> getPhysicalServerDTOList() {
        return physicalServerDTOList;
    }

    public void setPhysicalServerDTOList(List<PhysicalServerDTO> physicalServerDTOList) {
        this.physicalServerDTOList = physicalServerDTOList;
    }
}
