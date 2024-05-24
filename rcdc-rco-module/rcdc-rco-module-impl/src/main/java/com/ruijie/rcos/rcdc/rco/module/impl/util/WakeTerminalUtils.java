package com.ruijie.rcos.rcdc.rco.module.impl.util;

import com.ruijie.rcos.rcdc.rco.module.impl.BusinessKey;
import com.ruijie.rcos.sk.base.validator.ValidatorUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import com.ruijie.rcos.rcdc.clouddesktop.module.def.dto.CbbNetworkDataPacketDTO;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.dto.CbbNetworkDataPacketOptionDTO;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.enums.CbbEthType;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.enums.CbbIpProtocol;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.enums.CbbVlanEthType;
import com.ruijie.rcos.rcdc.rco.module.def.api.RcoGlobalParameterAPI;
import com.ruijie.rcos.rcdc.rco.module.def.api.request.BuildWakeTerminalNetDataRequest;
import com.ruijie.rcos.rcdc.rco.module.def.api.request.FindParameterRequest;
import com.ruijie.rcos.rcdc.terminal.module.def.api.CbbTerminalOperatorAPI;
import com.ruijie.rcos.rcdc.terminal.module.def.api.dto.CbbTerminalBasicInfoDTO;
import com.ruijie.rcos.sk.base.exception.BusinessException;
import com.ruijie.rcos.sk.base.util.IPv4Util;

import io.netty.util.internal.StringUtil;

/**
 * Description:
 * Copyright: Copyright (c) 2021
 * Company: Ruijie Co., Ltd.
 * Create Time: 2021/8/4
 *
 * @author xwx
 */

@Service
public class WakeTerminalUtils {

    private static final String DEST_MAC = "FF:FF:FF:FF:FF:FF";

    private static final String NET_DATA_HEAD = "FFFFFFFFFFFF";

    private static final String REGEX = ":";
    
    private Integer srcPort;

    private Integer destPort;

    @Autowired
    private RcoGlobalParameterAPI rcoGlobalParameterAPI;

    @Autowired
    private CbbTerminalOperatorAPI cbbTerminalOperatorAPI;

    private static final String WAKE_TERMINAL_SOURCE_PORT = "wake_terminal_source_port";

    private static final String WAKE_TERMINAL_DEST_PORT = "wake_terminal_dest_port";

    private static int TERMINAL_MAC_LENGTH = 16;


    /**
     * 构造唤醒终端请求参数
     * @param terminalId 终端mac
     * @return  BuildWakeTerminalNetDataRequest
     * @throws BusinessException 业务异常
     */
    public CbbNetworkDataPacketDTO buildWakeTerminalNetData(String terminalId) throws BusinessException {
        Assert.hasText(terminalId, "terminalId cant be null");
        BuildWakeTerminalNetDataRequest buildWakeTerminalNetDataRequest = new BuildWakeTerminalNetDataRequest();

        CbbTerminalBasicInfoDTO cbbTerminalBasicInfoDTO = cbbTerminalOperatorAPI.findBasicInfoByTerminalId(terminalId);
        buildWakeTerminalNetDataRequest.setCbbEthType(CbbEthType.IPV4);
        buildWakeTerminalNetDataRequest.setDestMac(cbbTerminalBasicInfoDTO.getTerminalId());
        buildWakeTerminalNetDataRequest.setTerminalId(cbbTerminalBasicInfoDTO.getTerminalId());
        buildWakeTerminalNetDataRequest.setTerminalIp(cbbTerminalBasicInfoDTO.getIp());
        if (srcPort == null || destPort == null) {
            srcPort = Integer.parseInt(rcoGlobalParameterAPI.findParameter(new FindParameterRequest(WAKE_TERMINAL_SOURCE_PORT)).getValue());
            destPort = Integer.parseInt(rcoGlobalParameterAPI.findParameter(new FindParameterRequest(WAKE_TERMINAL_DEST_PORT)).getValue());
        }
        buildWakeTerminalNetDataRequest.setSrcPort(srcPort);
        buildWakeTerminalNetDataRequest.setDestPort(destPort);
        CbbTerminalBasicInfoDTO basicInfoByTerminalId =
                cbbTerminalOperatorAPI.findBasicInfoByTerminalId(buildWakeTerminalNetDataRequest.getTerminalId());
        if (!ValidatorUtil.isIPv4Address(buildWakeTerminalNetDataRequest.getTerminalIp())
                
                || !ValidatorUtil.isIPv4Mask(basicInfoByTerminalId.getSubnetMask())) {
            throw new BusinessException(BusinessKey.TERMINAL_IP_ILLEGAL);
        }

        String networkBroadcastAddress =
                IPv4Util.getNetworkBroadcastAddress(buildWakeTerminalNetDataRequest.getTerminalIp(), basicInfoByTerminalId.getSubnetMask());
        buildWakeTerminalNetDataRequest.setDestIp(networkBroadcastAddress);
        return buildWakeTerminalNetDataDTO(buildWakeTerminalNetDataRequest);
    }

    /**
     * 构建data
     * @param request request
     * @return data
     */
    private CbbNetworkDataPacketDTO buildWakeTerminalNetDataDTO(BuildWakeTerminalNetDataRequest request) {
        Assert.notNull(request, "request cant be null");
        CbbNetworkDataPacketDTO cbbNetworkDataPacketDTO = new CbbNetworkDataPacketDTO();
        cbbNetworkDataPacketDTO.setDstMac(DEST_MAC);
        cbbNetworkDataPacketDTO.setEthType(request.getCbbEthType());
        CbbNetworkDataPacketOptionDTO networkDataPacketOptionDTO = new CbbNetworkDataPacketOptionDTO();
        if (request.getCbbEthType() == CbbEthType.VLAN) {
            networkDataPacketOptionDTO.setVlanEthType(CbbVlanEthType.IPV4);
            networkDataPacketOptionDTO.setVlanId(request.getVlanId());
        }
        networkDataPacketOptionDTO.setDstIp(request.getDestIp());
        networkDataPacketOptionDTO.setIpProtocol(CbbIpProtocol.UDP);
        networkDataPacketOptionDTO.setSrcPort(request.getSrcPort());
        networkDataPacketOptionDTO.setDstPort(request.getDestPort());
        String data = buildWakeTerminalData(request.getDestMac());
        networkDataPacketOptionDTO.setData(data);
        cbbNetworkDataPacketDTO.setNetworkDataPacketOptionDTO(networkDataPacketOptionDTO);
        return cbbNetworkDataPacketDTO;
    }

    private String buildWakeTerminalData(String terminalMac) {
        StringBuilder result = new StringBuilder();
        result.append(NET_DATA_HEAD);
        terminalMac = terminalMac.replaceAll(REGEX, StringUtil.EMPTY_STRING);
        for (int i = 0; i < TERMINAL_MAC_LENGTH; i++) {
            result.append(terminalMac);
        }
        return result.toString();
    }
}
