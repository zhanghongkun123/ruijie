package com.ruijie.rcos.rcdc.rco.module.impl.spi;

import com.ruijie.rcos.rcdc.clouddesktop.module.def.api.CbbDeskMgmtAPI;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.dto.CbbDeskDTO;
import com.ruijie.rcos.rcdc.rca.module.def.api.RcaAppPoolAPI;
import com.ruijie.rcos.rcdc.rca.module.def.api.RcaHostSessionAPI;
import com.ruijie.rcos.rcdc.rca.module.def.api.dto.RcaAppPoolBaseDTO;
import com.ruijie.rcos.rcdc.rca.module.def.spi.RcaPoolHostBindSPI;
import com.ruijie.rcos.rcdc.rco.module.def.api.RcoAppPoolAPI;
import com.ruijie.rcos.rcdc.rco.module.impl.desktoppool.DesktopPoolAPIHelper;
import com.ruijie.rcos.rcdc.rco.module.impl.entity.ViewUserDesktopEntity;
import com.ruijie.rcos.rcdc.rco.module.impl.service.QueryCloudDesktopService;
import com.ruijie.rcos.sk.base.exception.BusinessException;
import com.ruijie.rcos.sk.base.log.Logger;
import com.ruijie.rcos.sk.base.log.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.util.Assert;

import java.util.UUID;

/**
 * Description: 云应用主机分配SPI
 * Copyright: Copyright (c) 2024
 * Company: Ruijie Co., Ltd.
 * Create Time: 2024年04月03日
 *
 * @author zhengjingyong
 */
@Lazy
public class RcaPoolHostBindSPIImpl implements RcaPoolHostBindSPI {

    private static final Logger LOGGER = LoggerFactory.getLogger(RcaPoolHostBindSPIImpl.class);

    @Autowired
    private DesktopPoolAPIHelper desktopPoolAPIHelper;

    @Autowired
    private RcaHostSessionAPI rcaHostSessionAPI;

    @Autowired
    private QueryCloudDesktopService queryCloudDesktopService;

    @Autowired
    private RcoAppPoolAPI rcoAppPoolAPI;

    @Autowired
    private RcaAppPoolAPI rcaAppPoolAPI;

    @Autowired
    private CbbDeskMgmtAPI cbbDeskMgmtAPI;

    @Override
    public void notifyGtHostUserInfo(UUID hostId, UUID userId) throws BusinessException {
        Assert.notNull(hostId, "hostId can not be null ");
        Assert.notNull(userId, "userId can not be null ");

        UUID hostSingleStaticBindUser = rcaHostSessionAPI.getHostSingleBindUser(hostId, null);
        if (hostSingleStaticBindUser == null) {
            LOGGER.info("不存在绑定单会话的用户信息，跳过处理，hostId : [{}]", hostId);
            return;
        }

        ViewUserDesktopEntity viewUserDesktopEntity = queryCloudDesktopService.checkDesktopExistInDeskViewById(hostId);
        LOGGER.info("向应用主机发送用户信息，主机id: [{}], 用户id:[{}]", hostId, userId);
        desktopPoolAPIHelper.asyncNotifyGuestToolUserInfo(viewUserDesktopEntity, userId);
    }

    @Override
    public void unbindStaticHostUpdateSpec(UUID hostId, UUID poolId) throws BusinessException {
        Assert.notNull(hostId, "hostId can not be null ");
        Assert.notNull(poolId, "poolId can not be null ");

        CbbDeskDTO cbbDeskDTO = cbbDeskMgmtAPI.getDeskById(hostId);
        RcaAppPoolBaseDTO appPoolBaseDTO = rcaAppPoolAPI.getAppPoolById(poolId);
        rcoAppPoolAPI.syncSpec(appPoolBaseDTO, cbbDeskDTO);
    }
}
