package com.ruijie.rcos.rcdc.rco.module.impl.cas.qrcode.api;

import com.ruijie.rcos.rcdc.rco.module.def.api.CasScanCodeAuthParameterAPI;
import com.ruijie.rcos.rcdc.rco.module.def.cas.qrcode.constants.CasScanCodeAuthConstants;
import com.ruijie.rcos.rcdc.rco.module.def.cas.qrcode.dto.AuthTokenDTO;
import com.ruijie.rcos.rcdc.rco.module.def.cas.qrcode.dto.CasScanCodeAuthDTO;
import com.ruijie.rcos.rcdc.rco.module.impl.cas.qrcode.service.CasExternalScanCodeService;
import com.ruijie.rcos.rcdc.rco.module.impl.cas.qrcode.service.CasScanCodeAuthService;
import com.ruijie.rcos.rcdc.rco.module.impl.spi.common.ShineMessageHandler;
import com.ruijie.rcos.rcdc.terminal.module.def.api.CbbTerminalOperatorAPI;
import com.ruijie.rcos.sk.base.concurrent.ThreadExecutors;
import com.ruijie.rcos.sk.base.exception.BusinessException;
import com.ruijie.rcos.sk.base.log.Logger;
import com.ruijie.rcos.sk.base.log.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.Assert;
import org.springframework.util.CollectionUtils;

import java.util.List;

/**
 * Description:
 * Copyright: Copyright (c) 2021
 * Company: Ruijie Co., Ltd.
 * Create Time: 2021/6/23
 *
 * @author TD
 */
public class CasScanCodeAuthParameterAPIImpl implements CasScanCodeAuthParameterAPI {

    private static final Logger LOGGER = LoggerFactory.getLogger(CasScanCodeAuthParameterAPIImpl.class);

    @Autowired
    private CasScanCodeAuthService codeAuthService;

    @Autowired
    private CasExternalScanCodeService codeService;

    @Autowired
    private ShineMessageHandler shineMessageHandler;

    @Autowired
    private CbbTerminalOperatorAPI cbbTerminalOperatorAPI;

    @Override
    public CasScanCodeAuthDTO getCasScanCodeAuthInfo() {
        return codeAuthService.getCasScanCodeAuthInfo();
    }

    @Override
    public void updateCasScanCodeAuthInfo(CasScanCodeAuthDTO casScanCodeAuthDTO) {
        Assert.notNull(casScanCodeAuthDTO, "casScanCodeAuthDTO must not be null");
        codeAuthService.updateCasScanCodeAuthInfo(casScanCodeAuthDTO);

        ThreadExecutors.execute("RCDC-CAS扫码配置变更推送给终端", () -> {
            List<String> terminalList = cbbTerminalOperatorAPI.getOnlineTerminalIdList();
            LOGGER.info("终端在线数：{}", terminalList.size());
            if (!CollectionUtils.isEmpty(terminalList)) {
                for (String terminalId : terminalList) {
                    try {
                        shineMessageHandler.requestContent(terminalId, CasScanCodeAuthConstants.PUSH_CAS_QR_CONFIG, casScanCodeAuthDTO);
                    } catch (Exception e) {
                        LOGGER.error("RCDC推送CAS扫码配置信息异常，终端ID：{}，e={}", terminalId, e);
                    }
                }
            }

        });
    }

    @Override
    public AuthTokenDTO testCasServiceConnectivity(CasScanCodeAuthDTO casScanCodeAuthDTO) throws BusinessException {
        Assert.notNull(casScanCodeAuthDTO, "CasScanCodeAuthDTO is not null");
        return codeService.getAuthTokenService(casScanCodeAuthDTO);
    }
}
