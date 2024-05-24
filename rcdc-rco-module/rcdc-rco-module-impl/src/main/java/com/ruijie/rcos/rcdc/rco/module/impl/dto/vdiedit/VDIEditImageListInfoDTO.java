package com.ruijie.rcos.rcdc.rco.module.impl.dto.vdiedit;

import java.util.List;

/**
 * Description: Function Description
 * Copyright: Copyright (c) 2020
 * Company: Ruijie Co., Ltd.
 * Create Time: 2020/4/1 16:27
 *
 * @param <T> 列表信息类型
 *
 * @author zhangyichi
 */
public class VDIEditImageListInfoDTO<T> {

    List<T> infoList;

    public VDIEditImageListInfoDTO(List<T> infoList) {
        this.infoList = infoList;
    }

    public List<T> getInfoList() {
        return infoList;
    }

    public void setInfoList(List<T> infoList) {
        this.infoList = infoList;
    }
}
