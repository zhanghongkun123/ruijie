package com.ruijie.rcos.rcdc.rco.module.impl.spi;

import java.util.List;
import java.util.UUID;

import com.ruijie.rcos.rcdc.rco.module.impl.service.HostUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.Assert;
import org.springframework.util.CollectionUtils;

import com.ruijie.rcos.rcdc.clouddesktop.module.def.spi.CbbDeskTerminalSPI;
import com.ruijie.rcos.rcdc.rco.module.impl.dao.UserDesktopDAO;
import com.ruijie.rcos.rcdc.rco.module.impl.entity.UserDesktopEntity;
import com.ruijie.rcos.rcdc.terminal.module.def.api.CbbTerminalOperatorAPI;
import com.ruijie.rcos.rcdc.terminal.module.def.api.dto.CbbTerminalBasicInfoDTO;
import com.ruijie.rcos.rcdc.terminal.module.def.api.enums.CbbTerminalStateEnums;
import com.ruijie.rcos.sk.base.exception.BusinessException;
import com.ruijie.rcos.sk.base.log.Logger;
import com.ruijie.rcos.sk.base.log.LoggerFactory;

/**
 * cbb获取桌面与终端的联系（即云桌面登陆的终端id）
 * Description:
 * Copyright: Copyright (c) 2019
 * Company: Ruijie Co., Ltd.
 * Create Time: 2020/12/18 15:57
 *
 * @author ketb
 */
public class DeskTerminalMappingSPIImpl implements CbbDeskTerminalSPI {
    private static final Logger LOGGER = LoggerFactory.getLogger(DeskTerminalMappingSPIImpl.class);

    @Autowired
    private UserDesktopDAO userDesktopDAO;

    @Autowired
    private CbbTerminalOperatorAPI cbbTerminalDetectAPI;

    @Autowired
    private HostUserService hostUserService;

    /**
     * 根据桌面id获取桌面登陆的终端id
     * 
     * @param deskId 云桌面ID
     * @return terminalId
     */
    @Override
    public String getDeskBindTerminalId(UUID deskId) {
        Assert.notNull(deskId, "desk id not be null");

        UserDesktopEntity userDesk = userDesktopDAO.findByCbbDesktopId(deskId);
        return userDesk.getTerminalId();
    }

    @Override
    public List<String> listMultiDeskBindTerminalId(UUID deskId) {
        Assert.notNull(deskId, "desk id not be null");

        return hostUserService.listTerminalIdByDeskId(deskId);
    }

    @Override
    public UUID getTerminalBindDesktop(String terminalId) {
        Assert.notNull(terminalId, "terminalId id not be null");

        List<UserDesktopEntity> desktopEntityList = userDesktopDAO.findByTerminalId(terminalId);
        if (CollectionUtils.isEmpty(desktopEntityList)) {
            //
            return null;
        }
        // 当前实现为单桌面，默认返回第一个。，
        return desktopEntityList.get(0).getCbbDesktopId();
    }

    @Override
    public String getDesktopBindOnlineTerminal(UUID deskId) throws BusinessException {
        Assert.notNull(deskId, "deskId can not be null");

        UserDesktopEntity userDesk = userDesktopDAO.findByCbbDesktopId(deskId);
        if (userDesk == null) {
            LOGGER.warn("桌面[{}]绑定终端关联关系不存在", deskId);
            //
            return null;
        }
        CbbTerminalBasicInfoDTO terminalBasicInfoDTO = cbbTerminalDetectAPI.findBasicInfoByTerminalId(userDesk.getTerminalId());
        if (terminalBasicInfoDTO.getState() == CbbTerminalStateEnums.ONLINE) {
            return terminalBasicInfoDTO.getTerminalId();
        }
        LOGGER.debug("桌面[{}]绑定终端[{}]不在线", deskId, userDesk.getTerminalId());
        //
        return null;
    }
}
