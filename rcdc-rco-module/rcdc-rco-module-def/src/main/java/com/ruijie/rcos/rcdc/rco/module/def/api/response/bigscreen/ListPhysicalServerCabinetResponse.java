package com.ruijie.rcos.rcdc.rco.module.def.api.response.bigscreen;

import com.ruijie.rcos.rcdc.rco.module.def.api.dto.bigscreen.PhysicalServerInfoDTO;
import com.ruijie.rcos.sk.modulekit.api.comm.DefaultResponse;

import java.util.List;

/**
 * Description: 获取物理服务器列表信息
 * Copyright: Copyright (c) 2019
 * Company: Ruijie Co., Ltd.
 * Create Time: 2019/9/1 12:57
 *
 * @author BaiGuoliang
 */
public class ListPhysicalServerCabinetResponse extends DefaultResponse {

    private List<PhysicalServerInfoDTO> physicalServerDTOList;

    public List<PhysicalServerInfoDTO> getPhysicalServerDTOList() {
        return physicalServerDTOList;
    }

    public void setPhysicalServerDTOList(List<PhysicalServerInfoDTO> physicalServerDTOList) {
        this.physicalServerDTOList = physicalServerDTOList;
    }
}
