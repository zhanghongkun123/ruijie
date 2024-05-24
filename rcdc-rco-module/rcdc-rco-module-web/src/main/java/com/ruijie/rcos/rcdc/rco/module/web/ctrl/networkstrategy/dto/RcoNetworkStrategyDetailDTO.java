package com.ruijie.rcos.rcdc.rco.module.web.ctrl.networkstrategy.dto;

import com.ruijie.rcos.rcdc.clouddesktop.module.def.dto.CbbDeskNetworkIpPoolDTO;

/**
 * Description: 
 * Copyright: Copyright (c) 2021
 * Company: Ruijie Co., Ltd.
 * Create Time: 2021/4/1
 *
 * @author wjp
 */
public class RcoNetworkStrategyDetailDTO {

    private Boolean bindUserGroup;

    private CbbDeskNetworkIpPoolDTO[] ipPoolArr;

    public Boolean getBindUserGroup() {
        return bindUserGroup;
    }

    public void setBindUserGroup(Boolean bindUserGroup) {
        this.bindUserGroup = bindUserGroup;
    }

    public CbbDeskNetworkIpPoolDTO[] getIpPoolArr() {
        return ipPoolArr;
    }

    public void setIpPoolArr(CbbDeskNetworkIpPoolDTO[] ipPoolArr) {
        this.ipPoolArr = ipPoolArr;
    }

}
