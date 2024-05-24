package com.ruijie.rcos.rcdc.rco.module.web.ctrl.networkstrategy.vo;

import com.ruijie.rcos.rcdc.clouddesktop.module.def.dto.CbbDeskNetworkBasicInfoDTO;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.dto.ClusterInfoDTO;
import com.ruijie.rcos.rcdc.rco.module.web.ctrl.networkstrategy.dto.RcoNetworkStrategyDetailDTO;

/**
 * 
 * Description: Function Description
 * Copyright: Copyright (c) 2021
 * Company: Ruijie Co., Ltd.
 * Create Time: 2021年4月1日
 * 
 * @author wjp
 */
public class NetworkStrategyDetailVO {

    private CbbDeskNetworkBasicInfoDTO cbbDeskNetworkBasicInfoDTO;

    private RcoNetworkStrategyDetailDTO rcoNetworkStrategyDetailDTO;

    private Boolean canUsed = Boolean.TRUE;

    private String canUsedMessage;

    private ClusterInfoDTO clusterInfoDTO;

    public CbbDeskNetworkBasicInfoDTO getCbbDeskNetworkBasicInfoDTO() {
        return cbbDeskNetworkBasicInfoDTO;
    }

    public void setCbbDeskNetworkBasicInfoDTO(CbbDeskNetworkBasicInfoDTO cbbDeskNetworkBasicInfoDTO) {
        this.cbbDeskNetworkBasicInfoDTO = cbbDeskNetworkBasicInfoDTO;
    }

    public RcoNetworkStrategyDetailDTO getRcoNetworkStrategyDetailDTO() {
        return rcoNetworkStrategyDetailDTO;
    }

    public void setRcoNetworkStrategyDetailDTO(RcoNetworkStrategyDetailDTO rcoNetworkStrategyDetailDTO) {
        this.rcoNetworkStrategyDetailDTO = rcoNetworkStrategyDetailDTO;
    }

    public Boolean getCanUsed() {
        return canUsed;
    }

    public void setCanUsed(Boolean canUsed) {
        this.canUsed = canUsed;
    }

    public String getCanUsedMessage() {
        return canUsedMessage;
    }

    public void setCanUsedMessage(String canUsedMessage) {
        this.canUsedMessage = canUsedMessage;
    }

    public ClusterInfoDTO getClusterInfoDTO() {
        return clusterInfoDTO;
    }

    public void setClusterInfoDTO(ClusterInfoDTO clusterInfoDTO) {
        this.clusterInfoDTO = clusterInfoDTO;
    }
}
