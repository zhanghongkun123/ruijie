package com.ruijie.rcos.rcdc.rco.module.def.api.dto;


import java.util.List;

/**
 *
 * Description: Function Description
 * Copyright: Copyright (c) 2018
 * Company: Ruijie Co., Ltd.
 * Create Time: 2019年9月4日
 *
 * @author XiaoJiaXin
 */

public class CloudDesktopDetailListDTO {
    private List<CloudDesktopDetailDTO> cloudDesktopDetailList;

    public List<CloudDesktopDetailDTO> getCloudDesktopDetailList() {
        return cloudDesktopDetailList;
    }

    public void setCloudDesktopDetailList(List<CloudDesktopDetailDTO> cloudDesktopDetailList) {
        this.cloudDesktopDetailList = cloudDesktopDetailList;
    }
}
