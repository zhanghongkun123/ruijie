package com.ruijie.rcos.rcdc.rco.module.def.api.response.bigscreen;

import com.ruijie.rcos.rcdc.rco.module.def.api.dto.bigscreen.CabinetMappingServerDTO;
import com.ruijie.rcos.sk.modulekit.api.comm.DefaultResponse;
import java.util.List;

/**
 *
 * Description: 配置服务器id列表
 * Copyright: Copyright (c) 2019
 * Company: Ruijie Co., Ltd.
 * Create Time: 2019年7月24日
 *
 * @author BaiGuoliang
 */
public class ListConfigedServerResponse extends DefaultResponse {

    private List<CabinetMappingServerDTO> cabinetMappingServerDTOList;

    public List<CabinetMappingServerDTO> getCabinetMappingServerDTOList() {
        return cabinetMappingServerDTOList;
    }

    public void setCabinetMappingServerDTOList(List<CabinetMappingServerDTO> cabinetMappingServerDTOList) {
        this.cabinetMappingServerDTOList = cabinetMappingServerDTOList;
    }
}