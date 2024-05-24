package com.ruijie.rcos.rcdc.rco.module.impl.spi;

import com.ruijie.rcos.rcdc.clouddesktop.module.def.dto.strategy.CbbIpLimitDTO;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.enums.CbbIpLimitModeEnum;
import com.ruijie.rcos.rcdc.rca.module.def.spi.GlobalIpLimitSPI;
import com.ruijie.rcos.rcdc.rco.module.def.api.dto.IpLimitDTO;
import com.ruijie.rcos.rcdc.rco.module.impl.Constants;
import com.ruijie.rcos.rcdc.rco.module.impl.ipllimit.service.IpLimitService;
import com.ruijie.rcos.rcdc.rco.module.impl.service.GlobalParameterService;
import org.apache.commons.compress.utils.Lists;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.CollectionUtils;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Description:
 * Copyright: Copyright (c)
 * Company: Ruijie Co., Ltd.
 * Create Time: 2024/5/6 09:51
 *
 * @author zhangsiming
 */
public class GlobalIpLimitSPIImpl implements GlobalIpLimitSPI {

    @Autowired
    private IpLimitService ipLimitService;

    @Autowired
    private GlobalParameterService globalParameterService;

    @Override
    public CbbIpLimitModeEnum getGlobalIpLimitMode() {
        String parameter = globalParameterService.findParameter(Constants.RCDC_RCO_IP_LIMIT_VALUE);
        boolean hasOpenIpLimited = Boolean.parseBoolean(parameter);
        if (!hasOpenIpLimited) {
            return CbbIpLimitModeEnum.NOT_USE;
        }
        return CbbIpLimitModeEnum.REJECT;
    }

    @Override
    public List<CbbIpLimitDTO> getGlobalIpLimitSegmentList() {
        List<IpLimitDTO> ipLimitDTOList = ipLimitService.getAllIpLimitDTOList();
        if (CollectionUtils.isEmpty(ipLimitDTOList)) {
            return Lists.newArrayList();
        }
        return ipLimitDTOList.stream()
                .map(ipLimitDTO -> {
                    CbbIpLimitDTO cbbIpLimitDTO = new CbbIpLimitDTO();
                    cbbIpLimitDTO.setStartIp(ipLimitDTO.getIpStart());
                    cbbIpLimitDTO.setEndIp(ipLimitDTO.getIpEnd());
                    return cbbIpLimitDTO;
                })
                .collect(Collectors.toList());
    }
}
