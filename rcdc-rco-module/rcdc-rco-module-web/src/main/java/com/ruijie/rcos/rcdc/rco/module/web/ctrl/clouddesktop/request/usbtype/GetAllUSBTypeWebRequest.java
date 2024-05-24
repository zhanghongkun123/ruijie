package com.ruijie.rcos.rcdc.rco.module.web.ctrl.clouddesktop.request.usbtype;

import com.ruijie.rcos.sk.webmvc.api.request.WebRequest;
import com.ruijie.rcos.sk.webmvc.api.vo.Sort;
import org.springframework.lang.Nullable;

/**
 * Description: 获取所有USB类型
 * Copyright: Copyright (c) 2018
 * Company: Ruijie Co., Ltd.
 * Create Time: 2018年11月20日
 *
 * @author Ghang
 */
public class GetAllUSBTypeWebRequest implements WebRequest {

    @Nullable
    private Sort sort;

    public Sort getSort() {
        return sort;
    }

    public void setSort(Sort sort) {
        this.sort = sort;
    }
}
