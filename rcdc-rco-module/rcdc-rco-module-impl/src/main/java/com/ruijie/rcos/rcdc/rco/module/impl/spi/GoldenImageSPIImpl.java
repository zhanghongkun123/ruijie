package com.ruijie.rcos.rcdc.rco.module.impl.spi;

import com.ruijie.rcos.rcdc.clouddesktop.module.def.spi.CbbGoldenImageSPI;

/**
 * 是否需要加载黄金镜像文件
 *
 * Description: Function Description
 * Copyright: Copyright (c) 2021
 * Company: Ruijie Co., Ltd.
 * Create Time: 2021.04.30
 *
 * @author artom
 */
public class GoldenImageSPIImpl implements CbbGoldenImageSPI {

    @Override
    public Boolean isNeedLoadGoldenImageFile() {
        return true;
    }
}
