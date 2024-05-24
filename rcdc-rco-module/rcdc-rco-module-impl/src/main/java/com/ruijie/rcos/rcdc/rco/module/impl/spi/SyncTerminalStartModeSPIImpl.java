package com.ruijie.rcos.rcdc.rco.module.impl.spi;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.Assert;

import com.alibaba.fastjson.JSON;
import com.ruijie.rcos.rcdc.codec.adapter.def.dto.CbbDispatcherRequest;
import com.ruijie.rcos.rcdc.codec.adapter.def.spi.CbbDispatcherHandlerSPI;
import com.ruijie.rcos.rcdc.rco.module.impl.dao.UserTerminalDAO;
import com.ruijie.rcos.rcdc.rco.module.impl.entity.UserTerminalEntity;
import com.ruijie.rcos.rcdc.rco.module.impl.spi.common.ShineAction;
import com.ruijie.rcos.rcdc.rco.module.impl.spi.dto.GetBootTypeDTO;
import com.ruijie.rcos.sk.base.log.Logger;
import com.ruijie.rcos.sk.base.log.LoggerFactory;
import com.ruijie.rcos.sk.modulekit.api.comm.DispatcherImplemetion;

/**
 * Description: 获取终端开机模式SPI
 * Copyright: Copyright (c) 2021
 * Company: Ruijie Co., Ltd.
 * Create Time: 2021/10/19 14:36
 *
 * @author yxq
 */
@DispatcherImplemetion(ShineAction.NOTIFY_TERMINAL_START_MODE)
public class SyncTerminalStartModeSPIImpl implements CbbDispatcherHandlerSPI {

    private static final Logger LOGGER = LoggerFactory.getLogger(SyncTerminalStartModeSPIImpl.class);

    @Autowired
    private UserTerminalDAO userTerminalDAO;

    @Override
    public void dispatch(CbbDispatcherRequest request) {
        Assert.notNull(request, " must not be null");
        String data = request.getData();
        Assert.notNull(data, "data must not be null");

        String terminalId = request.getTerminalId();
        GetBootTypeDTO getBootTypeDTO = JSON.parseObject(data, GetBootTypeDTO.class);
        LOGGER.info("同步终端[{}]开机模式请求为：[{}]", terminalId, data);

        UserTerminalEntity entity = userTerminalDAO.findFirstByTerminalId(terminalId);
        if (entity == null) {
            LOGGER.info("数据库不存在终端[{}]对应信息", terminalId);
            return;
        }

        entity.setBootType(getBootTypeDTO.getStartMode().getType());
        userTerminalDAO.save(entity);
        LOGGER.info("更新终端[{}]开机模式成功", terminalId);
    }
}
