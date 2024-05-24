package com.ruijie.rcos.rcdc.rco.module.def.api.response.aaa;

import java.util.List;

import com.ruijie.rcos.sk.modulekit.api.comm.DefaultResponse;

/**
 * Description: Function Description
 * Copyright: Copyright (c) 2020
 * Company: Ruijie Co., Ltd.
 * Create Time: 2020年01月13日
 *
 * @author linrenjian
 */
public class ListImageIdResponse extends DefaultResponse {

    /**
     * 镜像ID集合
     */
    private List<String> imageIdList;

    public List<String> getImageIdList() {
        return imageIdList;
    }

    public void setImageIdList(List<String> imageIdList) {
        this.imageIdList = imageIdList;
    }
}
