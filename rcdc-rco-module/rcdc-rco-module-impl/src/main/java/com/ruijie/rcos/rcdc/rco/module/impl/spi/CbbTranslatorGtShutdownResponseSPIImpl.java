package com.ruijie.rcos.rcdc.rco.module.impl.spi;

import com.ruijie.rcos.rcdc.clouddesktop.module.def.dto.desk.GtDesktopShutDownUserResponseDTO;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.spi.CbbTranslatorGtShutdownResponseSPI;

/**
 *
 * Description: 实现CbbTranslatorGtShutdownResponseSPI
 * Copyright: Copyright (c) 2022
 * Company: Ruijie Co., Ltd.
 * Create Time: 2022/09/15
 *
 * @author linke
 */
public class CbbTranslatorGtShutdownResponseSPIImpl implements CbbTranslatorGtShutdownResponseSPI {

    @Override
    public void deskShutDownResponse(GtDesktopShutDownUserResponseDTO gtDesktopShutDownUserResponseDTO) {
        // 办公无需实现
    }
}
