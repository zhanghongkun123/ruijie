package com.ruijie.rcos.rcdc.rco.module.def.api.response.bigscreen;

import com.ruijie.rcos.rcdc.rco.module.def.api.dto.bigscreen.DesktopNormalDistributionDTO;
import com.ruijie.rcos.sk.modulekit.api.comm.DefaultResponse;

/**
 * Description: 云桌面正态分布计算API响应
 * Copyright: Copyright (c) 2019
 * Company: Ruijie Co., Ltd.
 * Create Time: 2019/7/23 9:43
 *
 * @author zhangyichi
 */
public class DesktopNormalDistributionResponse extends DefaultResponse {

    private DesktopNormalDistributionDTO desktopNormalDistribution;

    public DesktopNormalDistributionResponse(DesktopNormalDistributionDTO desktopNormalDistribution) {
        this.desktopNormalDistribution = desktopNormalDistribution;
    }

    public DesktopNormalDistributionResponse() {
    }

    public DesktopNormalDistributionDTO getDesktopNormalDistribution() {
        return desktopNormalDistribution;
    }

    public void setDesktopNormalDistribution(DesktopNormalDistributionDTO desktopNormalDistribution) {
        this.desktopNormalDistribution = desktopNormalDistribution;
    }
}
