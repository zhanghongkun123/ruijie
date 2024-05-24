package com.ruijie.rcos.rcdc.rco.module.impl.spi;

import com.ruijie.rcos.rcdc.rca.module.def.dto.RcoUserTerminalDTO;
import com.ruijie.rcos.rcdc.rca.module.def.spi.GetRcoUserTerminalSPI;
import com.ruijie.rcos.rcdc.rco.module.impl.dao.UserTerminalDAO;
import com.ruijie.rcos.rcdc.rco.module.impl.dao.ViewTerminalDAO;
import com.ruijie.rcos.rcdc.rco.module.impl.entity.UserTerminalEntity;
import com.ruijie.rcos.rcdc.rco.module.impl.entity.ViewTerminalEntity;
import com.ruijie.rcos.sk.base.util.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.Assert;

import java.util.Objects;
import java.util.UUID;

/**
 * Description: 获取终端持久化信息实现
 * Copyright: Copyright (c) 2022
 * Company: RuiJie Co., Ltd.
 * Create Time: 2022/6/29 14:21
 *
 * @author fanbaorong
 */
public class GetRcoUserTerminalSPIImpl implements GetRcoUserTerminalSPI {

    @Autowired
    private UserTerminalDAO userTerminalDAO;

    @Autowired
    private ViewTerminalDAO viewTerminalDAO;

    @Override
    public RcoUserTerminalDTO getCloudDockTerminal(UUID userId) {
        Assert.notNull(userId, "userId不能为空");

        return new RcoUserTerminalDTO();
    }


    @Override
    public String getCloudDockTerminalByHostId(UUID hostId) {
        Assert.notNull(hostId, "hostId不能为空");

        ViewTerminalEntity terminalEntity = viewTerminalDAO.findByBindDeskId(hostId);

        if (Objects.isNull(terminalEntity)) {
            return StringUtils.EMPTY;
        }
        return terminalEntity.getTerminalId();
    }

    @Override
    public String getTerminalIdByHostId(UUID hostId) {
        Assert.notNull(hostId, "hostId不能为空");

        UserTerminalEntity terminalEntity = userTerminalDAO.findByBindDeskId(hostId);
        if (Objects.isNull(terminalEntity)) {
            return StringUtils.EMPTY;
        }
        return terminalEntity.getTerminalId();
    }
}
