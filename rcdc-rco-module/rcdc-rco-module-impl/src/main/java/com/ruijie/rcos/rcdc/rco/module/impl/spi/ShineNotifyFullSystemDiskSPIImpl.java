package com.ruijie.rcos.rcdc.rco.module.impl.spi;

import com.alibaba.fastjson.JSON;
import com.ruijie.rcos.rcdc.codec.adapter.def.dto.CbbDispatcherRequest;
import com.ruijie.rcos.rcdc.codec.adapter.def.spi.CbbDispatcherHandlerSPI;
import com.ruijie.rcos.rcdc.rco.module.impl.dao.UserDesktopDAO;
import com.ruijie.rcos.rcdc.rco.module.impl.spi.common.ShineAction;
import com.ruijie.rcos.rcdc.rco.module.impl.spi.dto.ShineNotifyFullSystemDiskDTO;
import com.ruijie.rcos.sk.base.log.Logger;
import com.ruijie.rcos.sk.base.log.LoggerFactory;
import com.ruijie.rcos.sk.modulekit.api.comm.DispatcherImplemetion;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.Assert;

/**
 * Description: SHINE上报桌面是否开启系统盘自动扩容配置
 * Copyright: Copyright (c) 2022
 * Company: Ruijie Co., Ltd.
 * Create Time: 2022/6/28 11:02
 *
 * @author yxq
 */
@DispatcherImplemetion(ShineAction.GET_FULL_SYSTEM_DISK_CONFIG)
public class ShineNotifyFullSystemDiskSPIImpl implements CbbDispatcherHandlerSPI {

    private static final Logger LOGGER = LoggerFactory.getLogger(ShineNotifyFullSystemDiskSPIImpl.class);

    @Autowired
    private UserDesktopDAO userDesktopDAO;

    @Override
    public void dispatch(CbbDispatcherRequest request) {
        Assert.notNull(request, "request must not be null");

        LOGGER.info("终端[{}]上报桌面系统盘配置为[{}]", request.getTerminalId(), request.getData());
        ShineNotifyFullSystemDiskDTO shineNotifyDTO = JSON.parseObject(request.getData(), ShineNotifyFullSystemDiskDTO.class);
        // 修改rco表是否开启系统盘自动扩容
        userDesktopDAO.updateEnableFullSystemDiskByDeskId(shineNotifyDTO.getDeskId(), shineNotifyDTO.getEnableFullSystemDisk());
    }
}
