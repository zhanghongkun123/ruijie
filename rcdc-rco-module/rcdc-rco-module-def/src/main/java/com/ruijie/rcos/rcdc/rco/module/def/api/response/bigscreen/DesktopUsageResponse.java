package com.ruijie.rcos.rcdc.rco.module.def.api.response.bigscreen;

import com.ruijie.rcos.rcdc.rco.module.def.api.dto.bigscreen.DesktopUsageDTO;
import com.ruijie.rcos.sk.modulekit.api.comm.DefaultResponse;
import java.util.List;

/**
 * Description: 获取云桌面使用率API响应
 * Copyright: Copyright (c) 2019
 * Company: Ruijie Co., Ltd.
 * Create Time: 2019/7/23 9:41
 *
 * @author zhangyichi
 */
public class DesktopUsageResponse extends DefaultResponse {

    private List<DesktopUsageDTO> resourceUsageList;

    public List<DesktopUsageDTO> getResourceUsageList() {
        return resourceUsageList;
    }

    public void setResourceUsageList(List<DesktopUsageDTO> resourceUsageList) {
        this.resourceUsageList = resourceUsageList;
    }
}
