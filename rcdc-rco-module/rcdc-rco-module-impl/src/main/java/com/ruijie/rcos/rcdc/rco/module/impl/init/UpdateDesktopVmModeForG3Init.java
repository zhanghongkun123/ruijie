package com.ruijie.rcos.rcdc.rco.module.impl.init;

import com.ruijie.rcos.rcdc.clouddesktop.module.def.api.CbbGlobalStrategyMgmtAPI;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.constants.Constants;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.enums.GlobalVmMode;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.enums.TerminalVmModeTypeEnum;
import com.ruijie.rcos.rcdc.rco.module.impl.service.DesktopService;
import com.ruijie.rcos.sk.modulekit.api.bootstrap.SafetySingletonInitializer;
import com.ruijie.rcos.sk.modulekit.api.tool.GlobalParameterAPI;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.ruijie.rcos.sk.base.log.Logger;
import com.ruijie.rcos.sk.base.log.LoggerFactory;

/**
 * Description: G3+WIN10+IDV终端初始化运行策略
 * Copyright: Copyright (c) 2023
 * Company: Ruijie Co., Ltd.
 * Create Time: 2023/08/10
 *
 * @author lifeng
 */
@Service("updateDesktopVmModeForG3Init")
public class UpdateDesktopVmModeForG3Init implements SafetySingletonInitializer {

    private static final Logger LOGGER = LoggerFactory.getLogger(UpdateDesktopVmModeForG3Init.class);

    @Autowired
    private GlobalParameterAPI globalParameterAPI;

    @Autowired
    private DesktopService desktopService;

    @Autowired
    private CbbGlobalStrategyMgmtAPI cbbGlobalStrategyMgmtAPI;

    @Override
    public void safeInit() {
        boolean hasChangeDesktopVmModeForG3 = Boolean.parseBoolean(globalParameterAPI.findParameter(Constants.HAS_UPDATED_VM_MODE_FOR_G3));
        if (hasChangeDesktopVmModeForG3) {
            LOGGER.info("ChangeDesktopVmModeForG3Init--已经执行过，不再执行");
            return;
        }
        boolean existG3IdvWin10 = desktopService.existDesktopByTerminalGlobalVmMode(TerminalVmModeTypeEnum.G3_IDV_WIN10);
        if (existG3IdvWin10) {
            LOGGER.info("ChangeDesktopVmModeForG3Init--存在G3+WIN10+IDV的桌面");
            cbbGlobalStrategyMgmtAPI.updateGlobalVmMode(TerminalVmModeTypeEnum.G3_IDV_WIN10, GlobalVmMode.PASSTHROUGH);
        }

        // 执行过将不再执行
        globalParameterAPI.updateParameter(Constants.HAS_UPDATED_VM_MODE_FOR_G3, Boolean.TRUE.toString());

    }
}
