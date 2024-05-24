package com.ruijie.rcos.rcdc.rco.module.impl.ipllimit.api;

import com.alibaba.fastjson.JSON;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.api.CbbThirdPartyDeskStrategyMgmtAPI;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.api.CbbVDIDeskMgmtAPI;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.api.CbbVDIDeskStrategyMgmtAPI;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.dto.CbbDeskDTO;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.dto.strategy.CbbDeskStrategyThirdPartyDTO;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.dto.strategy.CbbDeskStrategyVDIDTO;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.enums.CbbCloudDeskType;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.enums.CbbIpLimitModeEnum;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.enums.desktoppool.CbbDesktopPoolType;
import com.ruijie.rcos.rcdc.rco.module.def.api.IpLimitAPI;
import com.ruijie.rcos.rcdc.rco.module.def.api.dto.IpLimitDTO;
import com.ruijie.rcos.rcdc.rco.module.def.api.dto.IpLimitStrategyDTO;
import com.ruijie.rcos.rcdc.rco.module.impl.BusinessKey;
import com.ruijie.rcos.rcdc.rco.module.impl.Constants;
import com.ruijie.rcos.rcdc.rco.module.impl.ipllimit.service.IpLimitService;
import com.ruijie.rcos.rcdc.rco.module.impl.service.GlobalParameterService;
import com.ruijie.rcos.sk.base.exception.BusinessException;
import com.ruijie.rcos.sk.base.log.Logger;
import com.ruijie.rcos.sk.base.log.LoggerFactory;
import com.ruijie.rcos.sk.base.util.IPv4Util;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.lang.Nullable;
import org.springframework.util.Assert;
import org.springframework.util.CollectionUtils;

import java.util.List;
import java.util.Objects;
import java.util.UUID;


/**
 * Description:
 * Copyright: Copyright (c) 2021
 * Company: Ruijie Co., Ltd.
 * Create Time: 2021/7/27 21:40
 *
 * @author yxq
 */
public class IpLimitAPIImpl implements IpLimitAPI {

    private static final Logger LOGGER = LoggerFactory.getLogger(IpLimitAPIImpl.class);

    @Autowired
    private IpLimitService ipLimitService;

    @Autowired
    private GlobalParameterService globalParameterService;

    @Autowired
    private CbbVDIDeskStrategyMgmtAPI cbbVDIDeskStrategyMgmtAPI;

    @Autowired
    private CbbVDIDeskMgmtAPI cbbVDIDeskMgmtAPI;

    @Autowired
    private CbbThirdPartyDeskStrategyMgmtAPI cbbThirdPartyDeskStrategyMgmtAPI;

    @Override
    public void updateIpLimit(IpLimitStrategyDTO ipLimitStrategyDTO) throws BusinessException {
        Assert.notNull(ipLimitStrategyDTO, "ipLimitStrategyDTO must not null");
        Assert.notNull(ipLimitStrategyDTO.getEnableIpLimit(), "ipLimitStrategyDTO.getEnableIpLimit() must not null");

        LOGGER.info("接收到的IP限制信息为：[{}]", JSON.toJSONString(ipLimitStrategyDTO));
        // 若不开启，则不需要修改数据库IP段的信息
        if (Boolean.FALSE.equals(ipLimitStrategyDTO.getEnableIpLimit())) {
            LOGGER.info("未开启IP限制，无需数据库IP网段信息");
            globalParameterService.updateParameter(Constants.RCDC_RCO_IP_LIMIT_VALUE, ipLimitStrategyDTO.getEnableIpLimit().toString());
            return;
        }

        if (Boolean.TRUE.equals(ipLimitStrategyDTO.getEnableIpLimit()) && CollectionUtils.isEmpty(ipLimitStrategyDTO.getIpLimitDTOList())) {
            LOGGER.error("开启IP网段限制时，IP网段不能为空");
            throw new BusinessException(BusinessKey.RCDC_RCO_IP_POOL_MUST_NOT_EMPTY);
        }

        // 从前端传递过来的
        List<IpLimitDTO> ipLimitDTOList = ipLimitStrategyDTO.getIpLimitDTOList();
        ipLimitService.validateIpDTOList(ipLimitDTOList);
        ipLimitService.modifyIpPoolAndStrategy(ipLimitDTOList, ipLimitStrategyDTO.getEnableIpLimit());
    }

    @Override
    public IpLimitStrategyDTO getIpLimitStrategy() {
        String enableIpLimit = globalParameterService.findParameter(Constants.RCDC_RCO_IP_LIMIT_VALUE);
        List<IpLimitDTO> ipLimitDTOList = ipLimitService.getAllIpLimitDTOList();
        IpLimitStrategyDTO ipLimitStrategyDTO = new IpLimitStrategyDTO(Boolean.valueOf(enableIpLimit), ipLimitDTOList);
        LOGGER.info("构造的IP限制信息为：[{}]", JSON.toJSONString(ipLimitStrategyDTO));
        return ipLimitStrategyDTO;
    }

    @Override
    public Boolean isIpLimitEnable(@Nullable UUID deskId) throws BusinessException {
        CbbIpLimitModeEnum ipLimitMode = null;
        if (deskId != null) {
            CbbDeskDTO deskDTO = cbbVDIDeskMgmtAPI.getDeskVDI(deskId);
            if (deskDTO.getDeskType() == CbbCloudDeskType.VDI) {
                CbbDeskStrategyVDIDTO deskStrategyVDI = cbbVDIDeskStrategyMgmtAPI.getDeskStrategyVDI(deskDTO.getStrategyId());
                ipLimitMode = deskStrategyVDI.getIpLimitMode();
            } else {
                CbbDeskStrategyThirdPartyDTO deskStrategyThirdParty = cbbThirdPartyDeskStrategyMgmtAPI.
                        getDeskStrategyThirdParty(deskDTO.getStrategyId());
                ipLimitMode = deskStrategyThirdParty.getIpLimitMode();
            }
        }
        if (ipLimitMode == null || CbbIpLimitModeEnum.NOT_USE == ipLimitMode) {
            String parameter = globalParameterService.findParameter(Constants.RCDC_RCO_IP_LIMIT_VALUE);
            return Boolean.parseBoolean(parameter);
        }
        return true;
    }

    @Override
    public boolean isIpLimited(String terminalIp, @Nullable UUID deskId) throws BusinessException {
        Assert.notNull(terminalIp, "desktopIp must not null");
        CbbIpLimitModeEnum ipLimitMode = null;
        CbbDeskStrategyVDIDTO deskStrategyVDI = new CbbDeskStrategyVDIDTO();
        if (deskId != null) {
            CbbDeskDTO deskDTO = cbbVDIDeskMgmtAPI.getDeskVDI(deskId);
            if (deskDTO.getDeskType() == CbbCloudDeskType.VDI) {
                deskStrategyVDI = cbbVDIDeskStrategyMgmtAPI.getDeskStrategyVDI(deskDTO.getStrategyId());
                ipLimitMode = deskStrategyVDI.getIpLimitMode();
            } else {
                CbbDeskStrategyThirdPartyDTO deskStrategyThirdParty = cbbThirdPartyDeskStrategyMgmtAPI.
                        getDeskStrategyThirdParty(deskDTO.getStrategyId());
                ipLimitMode = deskStrategyThirdParty.getIpLimitMode();
                deskStrategyVDI.setIpLimitMode(ipLimitMode);
                deskStrategyVDI.setIpSegmentDTOList(deskStrategyThirdParty.getIpSegmentDTOList());
                deskStrategyVDI.setId(deskStrategyThirdParty.getId());
            }
        }
        return checkIp(terminalIp, ipLimitMode, deskStrategyVDI);
    }

    @Override
    public boolean isIpLimited(String terminalIp, @Nullable CbbDeskDTO deskDTO) throws BusinessException {
        Assert.notNull(terminalIp, "terminalIp can not be null");
        CbbIpLimitModeEnum ipLimitMode = null;
        CbbDeskStrategyVDIDTO deskStrategyVDI = new CbbDeskStrategyVDIDTO();
        if (deskDTO != null) {
            if (deskDTO.getDeskType() == CbbCloudDeskType.VDI) {
                deskStrategyVDI = cbbVDIDeskStrategyMgmtAPI.getDeskStrategyVDI(deskDTO.getStrategyId());
                ipLimitMode = deskStrategyVDI.getIpLimitMode();
            } else {
                CbbDeskStrategyThirdPartyDTO deskStrategyThirdParty = cbbThirdPartyDeskStrategyMgmtAPI.
                        getDeskStrategyThirdParty(deskDTO.getStrategyId());
                ipLimitMode = deskStrategyThirdParty.getIpLimitMode();
                deskStrategyVDI.setIpLimitMode(ipLimitMode);
                deskStrategyVDI.setIpSegmentDTOList(deskStrategyThirdParty.getIpSegmentDTOList());
                deskStrategyVDI.setId(deskStrategyThirdParty.getId());
            }
        }
        Boolean hasOpenIpLimited = ipLimitMode != null && CbbIpLimitModeEnum.NOT_USE != ipLimitMode;
        if (!hasOpenIpLimited) {
            String parameter = globalParameterService.findParameter(Constants.RCDC_RCO_IP_LIMIT_VALUE);
            hasOpenIpLimited = Boolean.parseBoolean(parameter);
        }
        if (!Boolean.TRUE.equals(hasOpenIpLimited)) {
            return false;
        }
        return checkIp(terminalIp, ipLimitMode, deskStrategyVDI);
    }

    @Override
    public boolean isIpLimitedByDeskStrategy(CbbDesktopPoolType poolType, String terminalIp, UUID deskStrategyId) throws BusinessException {
        Assert.notNull(terminalIp, "terminalIp must not null");
        Assert.notNull(deskStrategyId, "deskStrategyId must not null");
        CbbIpLimitModeEnum ipLimitMode;
        CbbDeskStrategyVDIDTO deskStrategyVDI = new CbbDeskStrategyVDIDTO();
        if (poolType == CbbDesktopPoolType.VDI) {
            deskStrategyVDI = cbbVDIDeskStrategyMgmtAPI.getDeskStrategyVDI(deskStrategyId);
            ipLimitMode = deskStrategyVDI.getIpLimitMode();
        } else {
            CbbDeskStrategyThirdPartyDTO deskStrategyThirdParty = cbbThirdPartyDeskStrategyMgmtAPI.getDeskStrategyThirdParty(deskStrategyId);
            ipLimitMode = deskStrategyThirdParty.getIpLimitMode();
            deskStrategyVDI.setIpLimitMode(ipLimitMode);
            deskStrategyVDI.setIpSegmentDTOList(deskStrategyThirdParty.getIpSegmentDTOList());
            deskStrategyVDI.setId(deskStrategyThirdParty.getId());
        }
        if (Objects.isNull(ipLimitMode) || CbbIpLimitModeEnum.NOT_USE == ipLimitMode) {
            String parameter = globalParameterService.findParameter(Constants.RCDC_RCO_IP_LIMIT_VALUE);
            if (!Boolean.parseBoolean(parameter)) {
                // 无需IP校验
                return false;
            }
        }

        return checkIp(terminalIp, ipLimitMode, deskStrategyVDI);
    }

    private boolean checkIp(String terminalIp, CbbIpLimitModeEnum ipLimitMode, CbbDeskStrategyVDIDTO deskStrategyVDI) {
        if (Objects.isNull(ipLimitMode) || CbbIpLimitModeEnum.NOT_USE == ipLimitMode) {
            LOGGER.info("使用全局配置校验终端IP[{}]是否被锁定", terminalIp);
            List<IpLimitDTO> ipLimitDTOList = ipLimitService.getAllIpLimitDTOList();
            return ipLimitDTOList.stream().anyMatch(ipLimitDTO -> {
                if (IPv4Util.isCheckIpInIpSection(terminalIp, ipLimitDTO.getIpStart(), ipLimitDTO.getIpEnd())) {
                    LOGGER.info("终端IP[{}]在[{}-{}]范围内，已被锁定", terminalIp, ipLimitDTO.getIpStart(), ipLimitDTO.getIpEnd());
                    return true;
                }
                return false;
            });
        } else {
            LOGGER.info("使用云桌面策略[{}]校验终端IP[{}]是否被锁定,访问规则[{}]", deskStrategyVDI.getId(), terminalIp, ipLimitMode.name());
            boolean exist = deskStrategyVDI.getIpSegmentDTOList().stream().anyMatch(ipSegmentDTO -> {
                if (IPv4Util.isCheckIpInIpSection(terminalIp, ipSegmentDTO.getStartIp(), ipSegmentDTO.getEndIp())) {
                    LOGGER.info("终端IP[{}]在[{}-{}]范围内", terminalIp, ipSegmentDTO.getStartIp(), ipSegmentDTO.getEndIp());
                    return true;
                }
                return false;
            });
            // "允许"/"拒绝" 两种情况
            return (CbbIpLimitModeEnum.ALLOW == ipLimitMode) != exist;
        }
    }

}
