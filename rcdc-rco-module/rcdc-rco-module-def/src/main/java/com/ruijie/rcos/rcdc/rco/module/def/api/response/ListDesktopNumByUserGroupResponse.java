package com.ruijie.rcos.rcdc.rco.module.def.api.response;

import com.ruijie.rcos.rcdc.rco.module.def.api.dto.DesktopStateNumDTO;
import com.ruijie.rcos.sk.modulekit.api.comm.DefaultResponse;

import java.util.List;

/**
 * Description: ListDesktopNumByUserGroupResponse
 * Copyright: Copyright (c) 2019
 * Company: Ruijie Co., Ltd.
 * Create Time: 2019-08-08
 *
 * @author hli
 */
public class ListDesktopNumByUserGroupResponse extends DefaultResponse {

    private List<DesktopStateNumDTO> desktopStateNumDTOList;

    public List<DesktopStateNumDTO> getDesktopStateNumDTOList() {
        return desktopStateNumDTOList;
    }

    public void setDesktopStateNumDTOList(List<DesktopStateNumDTO> desktopStateNumDTOList) {
        this.desktopStateNumDTOList = desktopStateNumDTOList;
    }

}
