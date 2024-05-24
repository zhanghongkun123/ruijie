package com.ruijie.rcos.rcdc.rco.module.impl.spi;

import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.Assert;

import com.ruijie.rcos.rcdc.clouddesktop.module.def.api.CbbVDIDeskMgmtAPI;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.dto.CbbDeskDTO;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.spi.CbbGetAcpiSPI;
import com.ruijie.rcos.rcdc.rco.module.impl.entity.RcoViewUserEntity;
import com.ruijie.rcos.rcdc.rco.module.impl.entity.UserDesktopEntity;
import com.ruijie.rcos.rcdc.rco.module.impl.service.AcpiService;
import com.ruijie.rcos.rcdc.rco.module.impl.service.QueryCloudDesktopService;
import com.ruijie.rcos.rcdc.rco.module.impl.service.UserService;
import com.ruijie.rcos.sk.base.exception.BusinessException;

/**
 * 
 * Description: ACPI spi 实现
 * Copyright: Copyright (c) 2018
 * Company: Ruijie Co., Ltd.
 * Create Time: 2022/7/6
 *
 * @author zhiweiHong
 */
public class CbbGetAcpiSPIImpl implements CbbGetAcpiSPI {

    @Autowired
    private AcpiService acpiUtils;

    @Autowired
    private QueryCloudDesktopService queryCloudDesktopService;

    @Autowired
    private UserService userService;

    @Autowired
    private CbbVDIDeskMgmtAPI cbbVDIDeskMgmtAPI;

    @Override
    public String getAcpi(UUID deskId) throws BusinessException {
        Assert.notNull(deskId, "deskId can not be null");

        UserDesktopEntity userDesktopEntity = queryCloudDesktopService.checkAndFindById(deskId);
        RcoViewUserEntity userEntity = null;
        if (userDesktopEntity.getUserId() != null) {
            userEntity = userService.getUserInfoById(userDesktopEntity.getUserId());
        }
        //获取VDI桌面信息
        CbbDeskDTO deskVDI = cbbVDIDeskMgmtAPI.getDeskVDI(deskId);

        return acpiUtils.genAcpiPara(userEntity, userDesktopEntity, deskVDI);
    }
}
