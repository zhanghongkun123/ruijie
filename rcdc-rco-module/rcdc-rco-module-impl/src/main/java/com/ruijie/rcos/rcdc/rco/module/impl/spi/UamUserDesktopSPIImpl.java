package com.ruijie.rcos.rcdc.rco.module.impl.spi;

import java.util.Objects;
import java.util.UUID;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.Assert;

import com.ruijie.rcos.rcdc.appcenter.module.def.dto.UamUserDesktopDTO;
import com.ruijie.rcos.rcdc.appcenter.module.def.spi.UamUserDesktopSPI;
import com.ruijie.rcos.rcdc.rco.module.impl.entity.ViewUamUserDesktopEntity;
import com.ruijie.rcos.rcdc.rco.module.impl.service.ViewUamUserDesktopService;
import com.ruijie.rcos.sk.base.log.Logger;
import com.ruijie.rcos.sk.base.log.LoggerFactory;

/**
 * Description:
 * Copyright: Copyright (c)
 * Company: Ruijie Co., Ltd.
 * Create Time: 2023/05/01 18:23
 *
 * @author coderLee23
 */
public class UamUserDesktopSPIImpl implements UamUserDesktopSPI {

    private static final Logger LOGGER = LoggerFactory.getLogger(UamUserDesktopSPIImpl.class);

    @Autowired
    private ViewUamUserDesktopService viewUamUserDesktopService;


    @Override
    public UamUserDesktopDTO getUamUserDesktopByDesktopId(UUID cloudDesktopId) {
        Assert.notNull(cloudDesktopId, "cloudDesktopId must not be null");
        ViewUamUserDesktopEntity viewUamUserDesktop = viewUamUserDesktopService.findByCloudDesktopId(cloudDesktopId);
        if (Objects.isNull(viewUamUserDesktop)) {
            // 不存在则返回空值
            return null;
        }
        UamUserDesktopDTO uamUserDesktopDTO = new UamUserDesktopDTO();
        BeanUtils.copyProperties(viewUamUserDesktop, uamUserDesktopDTO);
        return uamUserDesktopDTO;
    }

}
