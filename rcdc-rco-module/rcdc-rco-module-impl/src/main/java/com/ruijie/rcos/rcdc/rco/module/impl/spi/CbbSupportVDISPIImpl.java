package com.ruijie.rcos.rcdc.rco.module.impl.spi;

import org.springframework.beans.factory.annotation.Autowired;

import com.ruijie.rcos.rcdc.clouddesktop.module.def.enums.CbbSupportVDIEnum;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.spi.CbbSupportVDISPI;
import com.ruijie.rcos.rcdc.rco.module.def.api.ServerModelAPI;

/**
 * 
 * Description: 实现CbbSupportVDISPI
 * Copyright: Copyright (c) 2018
 * Company: Ruijie Co., Ltd.
 * Create Time: 2021/1/14
 *
 * @author zhiweiHong
 */
public class CbbSupportVDISPIImpl implements CbbSupportVDISPI {

    @Autowired
    private ServerModelAPI serverModelAPI;


    @Override
    public CbbSupportVDIEnum getSupportVDIEnum() {
        if (serverModelAPI.isInitModel()) {
            return CbbSupportVDIEnum.UNKNOW;
        }
        if (serverModelAPI.isVdiModel()) {
            return CbbSupportVDIEnum.SUPPORT;
        }
        return CbbSupportVDIEnum.NOT_SUPPORT;
    }
}
