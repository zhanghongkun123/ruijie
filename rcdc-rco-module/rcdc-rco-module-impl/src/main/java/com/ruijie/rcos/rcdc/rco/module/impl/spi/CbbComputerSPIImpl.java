package com.ruijie.rcos.rcdc.rco.module.impl.spi;

import com.ruijie.rcos.rcdc.clouddesktop.module.def.api.CbbDeskMgmtAPI;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.api.CbbIDVDeskMgmtAPI;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.dto.CbbComputerConnectDTO;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.dto.CbbDeskDTO;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.enums.CbbCloudDeskState;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.spi.CbbComputerSPI;
import com.ruijie.rcos.rcdc.rco.module.def.api.ServerModelAPI;
import com.ruijie.rcos.rcdc.rco.module.def.api.enums.ComputerStateEnum;
import com.ruijie.rcos.rcdc.rco.module.def.api.enums.ComputerTypeEnum;
import com.ruijie.rcos.rcdc.rco.module.impl.BusinessKey;
import com.ruijie.rcos.rcdc.rco.module.impl.entity.ComputerEntity;
import com.ruijie.rcos.rcdc.rco.module.impl.service.ComputerBusinessService;
import com.ruijie.rcos.rcdc.terminal.module.def.api.CbbTerminalGroupMgmtAPI;
import com.ruijie.rcos.sk.base.exception.BusinessException;
import com.ruijie.rcos.sk.base.log.Logger;
import com.ruijie.rcos.sk.base.log.LoggerFactory;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.Assert;

import java.util.Date;
import java.util.UUID;

/**
 * Description: PC终端业务相关
 * Copyright: Copyright (c) 2024
 * Company: Ruijie Co., Ltd.
 * Create Time: 2024/2/21
 *
 * @author zqj
 */

public class CbbComputerSPIImpl implements CbbComputerSPI {

    private static final Logger LOGGER = LoggerFactory.getLogger(CbbComputerSPIImpl.class);


    @Autowired
    private ComputerBusinessService computerBusinessService;


    @Autowired
    private ServerModelAPI serverModelAPI;


    @Autowired
    private CbbIDVDeskMgmtAPI cbbIDVDeskMgmtAPI;

    @Autowired
    private CbbDeskMgmtAPI cbbDeskMgmtAPI;


    @Override
    public UUID computerConnect(CbbComputerConnectDTO computerConnectDTO) throws BusinessException {
        Assert.notNull(computerConnectDTO, "computerConnectDTO is not be null");
        LOGGER.info("pc终端[{}]首次连接", computerConnectDTO.getTerminalId());
        if (!serverModelAPI.isVdiModel()) {
            LOGGER.info("非VDI服务器不支持PC终端连接");
            throw new BusinessException(BusinessKey.RCDC_RCO_COMPUTER_CONNECT_MOT_VDI_SERVER);
        }
        ComputerEntity computerEntity = computerBusinessService.getComputerByTerminalId(computerConnectDTO.getTerminalId().toString());
        boolean isCreate = false;
        if (computerEntity == null) {
            computerEntity = new ComputerEntity();
            computerEntity.setId(computerConnectDTO.getTerminalId());
            computerEntity.setTerminalId(computerConnectDTO.getTerminalId());
            computerEntity.setType(ComputerTypeEnum.THIRD);
            computerEntity.setTerminalGroupId(CbbTerminalGroupMgmtAPI.DEFAULT_TERMINAL_GROUP_ID);
            computerEntity.setCreateTime(new Date());
            isCreate = true;
        }
        computerEntity.setIp(computerConnectDTO.getIp());
        computerEntity.setMac(computerConnectDTO.getMac());
        computerEntity.setState(ComputerStateEnum.ONLINE);
        if (isCreate) {
            computerBusinessService.create(computerEntity);
        } else {
            computerBusinessService.saveComputer(computerEntity);
        }
        try {
            // 判断云桌面是否存在 更新桌面状态为运行中
            CbbDeskDTO deskDTO = cbbDeskMgmtAPI.findById(computerEntity.getId());
            if (deskDTO != null) {
                cbbIDVDeskMgmtAPI.updateIDVDeskStateByDeskId(computerEntity.getId(), CbbCloudDeskState.RUNNING);
            }
        } catch (Exception ex) {
            LOGGER.error("更新桌面[{}]为运行中异常", computerEntity.getId(), ex);
        }
        return computerEntity.getId();
    }

    @Override
    public Boolean updateComputerAgentVersion(UUID terminalId, String agentVersion) {
        Assert.notNull(terminalId, "terminalId cannot be null");
        Assert.hasText(agentVersion, "agentVersion cannot be empty");

        ComputerEntity computerEntity = computerBusinessService.getComputerByTerminalId(terminalId.toString());
        if (computerEntity != null && !agentVersion.equals(computerEntity.getAgentVersion())) {
            computerEntity.setAgentVersion(agentVersion);
            computerBusinessService.saveComputer(computerEntity);
        }
        return Boolean.TRUE;
    }

    @Override
    public String getComputerAgentVersion(UUID terminalId) {
        Assert.notNull(terminalId, "terminalId cannot be null");

        ComputerEntity computerEntity = computerBusinessService.getComputerByTerminalId(terminalId.toString());
        if (computerEntity != null) {
            return computerEntity.getAgentVersion();
        } else {
            return StringUtils.EMPTY;
        }
    }

    @Override
    public CbbCloudDeskState getThirdDeskStateByDeskId(UUID deskId) {
        Assert.notNull(deskId, "deskId cannot be null");
        ComputerEntity computerEntity = computerBusinessService.getComputerById(deskId);
        if (computerEntity != null) {
            return computerEntity.getState() == ComputerStateEnum.ONLINE ?
                    CbbCloudDeskState.RUNNING : CbbCloudDeskState.OFF_LINE;
        }
        return CbbCloudDeskState.OFF_LINE;
    }

    @Override
    public boolean existsById(UUID terminalId) {
        Assert.notNull(terminalId, "terminalId cannot be null");
        ComputerEntity computerEntity = computerBusinessService.getComputerById(terminalId);
        if (computerEntity != null) {
            return Boolean.TRUE;
        }
        return Boolean.FALSE;
    }
}
