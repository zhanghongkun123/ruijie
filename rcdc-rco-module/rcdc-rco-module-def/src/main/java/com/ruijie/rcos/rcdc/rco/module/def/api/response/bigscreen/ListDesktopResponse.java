package com.ruijie.rcos.rcdc.rco.module.def.api.response.bigscreen;

import com.ruijie.rcos.rcdc.rco.module.def.api.dto.bigscreen.BigScreenCloudDesktopDTO;
import com.ruijie.rcos.sk.modulekit.api.comm.DefaultPageResponse;
import java.util.List;

/**
 * Description: 按用户组获取云桌面列表API响应
 * Copyright: Copyright (c) 2019
 * Company: Ruijie Co., Ltd.
 * Create Time: 2019/7/23 9:39
 *
 * @author zhangyichi
 */
public class ListDesktopResponse extends DefaultPageResponse {

    private List<BigScreenCloudDesktopDTO> desktopList;

    public List<BigScreenCloudDesktopDTO> getDesktopList() {
        return desktopList;
    }

    public void setDesktopList(List<BigScreenCloudDesktopDTO> desktopList) {
        this.desktopList = desktopList;
    }
}
