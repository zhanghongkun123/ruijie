package com.ruijie.rcos.rcdc.rco.module.web.ctrl.clouddesktop.response.globalstrategy;

import com.ruijie.rcos.rcdc.rco.module.web.ctrl.clouddesktop.dto.PartDisplayStrategyDTO;

/**
 * 
 * Description: Function Description
 * Copyright: Copyright (c) 2019
 * Company: Ruijie Co., Ltd.
 * Create Time: 2019年1月2日
 * 
 * @author Ghang
 */
public class GetPartDisplayStrategyResponse {

    private PartDisplayStrategyDTO[] itemArr;

    public PartDisplayStrategyDTO[] getItemArr() {
        return itemArr;
    }

    public void setItemArr(PartDisplayStrategyDTO[] itemArr) {
        this.itemArr = itemArr;
    }
    
}
