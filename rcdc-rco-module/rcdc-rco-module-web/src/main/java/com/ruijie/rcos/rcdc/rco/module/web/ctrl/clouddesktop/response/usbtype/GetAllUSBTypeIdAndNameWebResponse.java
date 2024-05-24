package com.ruijie.rcos.rcdc.rco.module.web.ctrl.clouddesktop.response.usbtype;

import com.ruijie.rcos.sk.webmvc.api.vo.IdLabelEntry;

/**
 * 
 * Description: Function Description
 * Copyright: Copyright (c) 2019
 * Company: Ruijie Co., Ltd.
 * Create Time: 2019年1月3日
 * 
 * @author Ghang
 */
public class GetAllUSBTypeIdAndNameWebResponse {

    private IdLabelEntry[] itemArr;

    public IdLabelEntry[] getItemArr() {
        return itemArr;
    }

    public void setItemArr(IdLabelEntry[] itemArr) {
        this.itemArr = itemArr;
    }
    
}
