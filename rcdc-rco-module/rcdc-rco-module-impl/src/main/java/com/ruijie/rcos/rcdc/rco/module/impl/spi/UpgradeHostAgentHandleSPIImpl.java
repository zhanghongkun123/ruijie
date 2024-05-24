package com.ruijie.rcos.rcdc.rco.module.impl.spi;

import com.ruijie.rcos.base.sysmanage.module.def.dto.BaseUpgradeDTO;
import com.ruijie.rcos.base.sysmanage.module.def.spi.BaseMaintenanceModeNotifySPI;
import com.ruijie.rcos.rcdc.rca.module.def.api.RcaClientMgmtAPI;
import com.ruijie.rcos.rcdc.rca.module.def.api.RcaTrusteeshipHostAPI;
import com.ruijie.rcos.rcdc.rca.module.def.dto.RcaClientVersionInfoDTO;
import com.ruijie.rcos.rcdc.rca.module.def.dto.RcaTrusteeshipHostDTO;
import com.ruijie.rcos.rcdc.rca.module.def.enums.HsotAgentUpdateState;
import com.ruijie.rcos.rcdc.rca.module.def.enums.RcaClientOsType;
import com.ruijie.rcos.rcdc.rca.module.def.util.VersionUtils;
import com.ruijie.rcos.sk.base.exception.BusinessException;
import com.ruijie.rcos.sk.base.log.Logger;
import com.ruijie.rcos.sk.base.log.LoggerFactory;
import com.ruijie.rcos.sk.modulekit.api.comm.DispatcherImplemetion;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.CollectionUtils;

import java.util.List;

/**
 * Description: 版本升级过程，处理HostAgent代理状态
 * Copyright: Copyright (c) 2022
 * Company: Ruijie Co., Ltd.
 * Create Time: 21/9/2022 下午 2:12
 *
 * @author gaoxueyuan
 */
@DispatcherImplemetion("UpgradeHostAgentHandleSPIImpl")
public class UpgradeHostAgentHandleSPIImpl implements BaseMaintenanceModeNotifySPI {

    private static final Logger LOGGER = LoggerFactory.getLogger(UpgradeHostAgentHandleSPIImpl.class);

    @Autowired
    private RcaTrusteeshipHostAPI rcaTrusteeshipHost;

    @Autowired
    private RcaClientMgmtAPI rcaClientMgmt;

    @Override
    public Boolean beforeEnteringMaintenance(String s, BaseUpgradeDTO baseUpgradeDTO) throws BusinessException {
        return Boolean.TRUE;
    }

    @Override
    public Boolean afterEnteringMaintenance(String s, BaseUpgradeDTO baseUpgradeDTO) throws BusinessException {
        // 不需处理，直接响应成功
        return Boolean.TRUE;
    }

    @Override
    public Boolean afterUnderMaintenance(String s, BaseUpgradeDTO baseUpgradeDTO) {
        // 不需处理，直接响应成功
        return Boolean.TRUE;
    }

    @Override
    public Boolean afterMaintenanceEnd(String s, BaseUpgradeDTO baseUpgradeDTO) throws BusinessException {
        LOGGER.info("服务器升级完成，处理HostAgent升级状态");

        List<RcaTrusteeshipHostDTO> trusteeshipHostDTOList = rcaTrusteeshipHost.getAllVmInfo();
        if (CollectionUtils.isEmpty(trusteeshipHostDTOList)) {
            // 不需处理，直接响应成功
            return Boolean.TRUE;
        }
        RcaClientVersionInfoDTO versionInfoDTO = rcaClientMgmt.getVersion(RcaClientOsType.WINDOWS_GT);
        for (RcaTrusteeshipHostDTO rcaTrusteeshipHostDTO : trusteeshipHostDTOList) {
            if (isNeedUpdate(rcaTrusteeshipHostDTO, versionInfoDTO)) {
                rcaTrusteeshipHost.updateUpgradeState(rcaTrusteeshipHostDTO.getId(), HsotAgentUpdateState.NEED_UPDATED);
            }
        }
        return Boolean.TRUE;
    }

    private Boolean isNeedUpdate(RcaTrusteeshipHostDTO rcaTrusteeshipHostDTO, RcaClientVersionInfoDTO versionInfoDTO) {

        if (rcaTrusteeshipHostDTO.getHostAgentUpdateState() == null) {
            return Boolean.FALSE;
        }

        if (rcaTrusteeshipHostDTO.getHostAgentUpdateState() != HsotAgentUpdateState.NORMAL) {
            return Boolean.FALSE;
        }

        if (StringUtils.isBlank(rcaTrusteeshipHostDTO.getHostAgentVersion())) {
            return Boolean.FALSE;
        }

        if (StringUtils.isBlank(versionInfoDTO.getVersion())) {
            return Boolean.FALSE;
        }

        if (!VersionUtils.isVersionEquals(rcaTrusteeshipHostDTO.getHostAgentVersion(), versionInfoDTO.getVersion())) {
            return Boolean.TRUE;
        }
        return Boolean.FALSE;
    }

}
