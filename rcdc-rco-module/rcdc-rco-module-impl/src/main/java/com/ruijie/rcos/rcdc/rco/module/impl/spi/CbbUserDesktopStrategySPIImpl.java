package com.ruijie.rcos.rcdc.rco.module.impl.spi;

import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.Assert;

import com.ruijie.rcos.gss.sdk.iac.module.def.enums.IacUserTypeEnum;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.spi.CbbUserDesktopStrategySPI;
import com.ruijie.rcos.rcdc.rco.module.def.api.UserMgmtAPI;
import com.ruijie.rcos.rcdc.rco.module.def.api.dto.user.UserInfoDTO;
import com.ruijie.rcos.rcdc.rco.module.impl.BusinessKey;
import com.ruijie.rcos.rcdc.rco.module.impl.entity.UserDesktopEntity;
import com.ruijie.rcos.rcdc.rco.module.impl.service.UserDesktopService;
import com.ruijie.rcos.sk.base.exception.BusinessException;
import com.ruijie.rcos.sk.base.log.Logger;
import com.ruijie.rcos.sk.base.log.LoggerFactory;

/**
 * 用户和云桌面关系通知SPI接口实现
 * Description: Function Description
 * Copyright: Copyright (c) 2021
 * Company: Ruijie Co., Ltd.
 * Create Time: 2021年10月24日
 *
 * @author chenl
 */
public class CbbUserDesktopStrategySPIImpl implements CbbUserDesktopStrategySPI {

    private static final Logger LOGGER = LoggerFactory.getLogger(CbbUserDesktopStrategySPIImpl.class);

    @Autowired
    private UserDesktopService userDesktopService;

    @Autowired
    private UserMgmtAPI userMgmtAPI;

    @Override
    public Boolean checkUserDeskIsVisitor(UUID deskId) throws BusinessException {
        Assert.notNull(deskId, "deskId request can not be null");
        UserDesktopEntity userDesktopEntity = userDesktopService.findByDeskId(deskId);
        if (userDesktopEntity == null) {
            throw new BusinessException(BusinessKey.RCDC_RCO_WATERMARK_DESKTOP_NOT_EXIST, String.valueOf(deskId));
        }
        UUID userId = userDesktopEntity.getUserId();
        UserInfoDTO userInfoDTO = userMgmtAPI.getUserInfoById(userId);
        return IacUserTypeEnum.VISITOR == userInfoDTO.getUserType() ;
    }
}
