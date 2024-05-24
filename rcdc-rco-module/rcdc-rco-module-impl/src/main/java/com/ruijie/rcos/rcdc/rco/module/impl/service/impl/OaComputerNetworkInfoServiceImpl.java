package com.ruijie.rcos.rcdc.rco.module.impl.service.impl;

import com.ruijie.rcos.rcdc.clouddesktop.module.def.api.CbbDeskMgmtAPI;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.constants.CommonMessageCode;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.dto.CbbDeskDTO;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.enums.CbbCloudDeskType;
import com.ruijie.rcos.rcdc.rco.module.common.annotation.TargetHost;
import com.ruijie.rcos.rcdc.rco.module.common.annotation.TcpAction;
import com.ruijie.rcos.rcdc.rco.module.common.dto.BaseResultDTO;
import com.ruijie.rcos.rcdc.rco.module.common.enums.HostTypeEnums;
import com.ruijie.rcos.rcdc.rco.module.common.message.AbstractRcdcHostMessageHandler;
import com.ruijie.rcos.rcdc.rco.module.def.constants.RcoOneAgentToRcdcAction;
import com.ruijie.rcos.rcdc.rco.module.impl.connector.dto.ComputerNetInfoDTO;
import com.ruijie.rcos.rcdc.rco.module.impl.service.ComputerBusinessService;
import com.ruijie.rcos.sk.base.exception.BusinessException;
import com.ruijie.rcos.sk.base.log.Logger;
import com.ruijie.rcos.sk.base.log.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Description:
 * Copyright: Copyright (c) 2024
 * Company: Ruijie Co., Ltd.
 * Create Time: 2024年02月26日
 *
 * @author wangjie9
 */
@TcpAction(RcoOneAgentToRcdcAction.OA_SEND_CDC_NEW_HOST_IP)
@TargetHost({HostTypeEnums.THIRD_HOST, HostTypeEnums.CLOUD_DESK})
@Service
public class OaComputerNetworkInfoServiceImpl extends AbstractRcdcHostMessageHandler<BaseResultDTO, ComputerNetInfoDTO> {

    private static final Logger LOGGER = LoggerFactory.getLogger(OaComputerNetworkInfoServiceImpl.class);

    @Autowired
    private ComputerBusinessService computerBusinessService;

    @Autowired
    private CbbDeskMgmtAPI cbbDeskMgmtAPI;

    @Override
    protected BaseResultDTO innerProcessMessage(ComputerNetInfoDTO computerNetInfoDTO) {
        LOGGER.info("上报PC终端[{}]网卡信息", computerNetInfoDTO.getHostId());
        // 更新网络信息
        computerBusinessService.updateComputerNetworkInfo(computerNetInfoDTO.getHostId().toString(), computerNetInfoDTO);
        BaseResultDTO defaultResponse = new BaseResultDTO();

        //更新桌面ip和mac
        try {
            CbbDeskDTO deskDTO = cbbDeskMgmtAPI.getDeskById(computerNetInfoDTO.getHostId());
            if (deskDTO.getDeskType() == CbbCloudDeskType.THIRD) {
                cbbDeskMgmtAPI.updateIpAndMac(computerNetInfoDTO.getHostId(), computerNetInfoDTO.getHostIp(), computerNetInfoDTO.getMac());
            }
        } catch (BusinessException e) {
            LOGGER.error("获取桌面[{}]信息报错", computerNetInfoDTO.getHostId());
        }
        defaultResponse.setCode(CommonMessageCode.SUCCESS);
        return defaultResponse;
    }
}
