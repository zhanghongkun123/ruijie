package com.ruijie.rcos.rcdc.rco.module.impl.hotupdate.spi;

import com.alibaba.fastjson.JSON;
import com.ruijie.rcos.base.sysmanage.module.def.dto.BaseUpgradeDTO;
import com.ruijie.rcos.base.sysmanage.module.def.spi.BaseUpgradeNotifySPI;
import com.ruijie.rcos.rcdc.rco.module.common.BusinessKey;
import com.ruijie.rcos.rcdc.rco.module.common.utils.ShellUtils;
import com.ruijie.rcos.rcdc.rco.module.def.api.ServerModelAPI;
import com.ruijie.rcos.rcdc.rco.module.impl.hotupdate.constants.HotUpdateConstant;
import com.ruijie.rcos.rcdc.terminal.module.def.api.CbbTerminalComponentUpgradeInitAPI;
import com.ruijie.rcos.sk.base.exception.BusinessException;
import com.ruijie.rcos.sk.base.log.Logger;
import com.ruijie.rcos.sk.base.log.LoggerFactory;
import com.ruijie.rcos.sk.modulekit.api.comm.DispatcherImplemetion;
import com.ruijie.rcos.sk.modulekit.api.tool.GlobalParameterAPI;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.Assert;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.stream.Stream;

/**
 * Description:
 * Copyright: Copyright (c)
 * Company: Ruijie Co., Ltd.
 * Create Time: 2023/11/27 13:59
 *
 * fixme BaseUpgradeNotifySPI需要base组件迁移完T版本后，更新版本号支持该SPI
 *
 * @author coderLee23
 */
@DispatcherImplemetion("terminalComponentUpgradeNotifySPIImpl")
public class TerminalComponentUpgradeNotifySPIImpl implements BaseUpgradeNotifySPI {

    private static final Logger LOGGER = LoggerFactory.getLogger(TerminalComponentUpgradeNotifySPIImpl.class);

    private static final AtomicBoolean IS_RUNNING = new AtomicBoolean(false);

    @Autowired
    private GlobalParameterAPI globalParameterAPI;

    @Autowired
    private CbbTerminalComponentUpgradeInitAPI cbbTerminalComponentUpgradeInitAPI;

    @Autowired
    private ServerModelAPI serverModelAPI;


    /**
     *
     * @param dispatchKey dispatchKey
     * @param upgradeDTO upgradeDTO
     * @return Boolean
     * @throws BusinessException BusinessException
     */
//    @Override
    public Boolean postUpgrade(String dispatchKey, BaseUpgradeDTO upgradeDTO) throws BusinessException {
        Assert.notNull(upgradeDTO, "upgradeDTO must not be null");
        // 判定是否是热升级模式
        if (upgradeDTO.getType() != BaseUpgradeDTO.UpgradeType.ONLINE) {
            LOGGER.info("当前为冷补丁升级，无需执行");
            return true;
        }

        // 判定是否存在终端组件升级
        if (!hasUpdate()) {
            LOGGER.info("当前为热补丁升级，未升级终端组件");
            return true;
        }

        if (IS_RUNNING.compareAndSet(false, true)) {
            try {
                // 设置终端组件包状态为失败，支持重启后重新做种
                updateTerminalComponentPackageFail();
                // 执行终端组件初始移动操作
                doTerminalShell();
                // 执行初始化终端组件操作
                terminalComponentInit();
            } finally {
                IS_RUNNING.set(false);
            }
        }

        return true;
    }

    private void terminalComponentInit() {
        if (serverModelAPI.isVdiModel()) {
            LOGGER.info("开始调用初始化Android组件包");
            cbbTerminalComponentUpgradeInitAPI.initAndroid();
            LOGGER.info("完成调用初始化Android组件包");
        }
        LOGGER.info("开始调用初始化Linux组件包");
        cbbTerminalComponentUpgradeInitAPI.initLinux();
        LOGGER.info("完成调用初始化Linux组件包");
    }

    private void updateTerminalComponentPackageFail() {
        globalParameterAPI.updateParameter(HotUpdateConstant.TERMINAL_COMPONENT_PACKAGE_INIT_STATUS_ANDROID_ARM,
                HotUpdateConstant.TERMINAL_COMPONENT_PACKAGE_RESET_FAIL);

        globalParameterAPI.updateParameter(HotUpdateConstant.TERMINAL_COMPONENT_PACKAGE_INIT_STATUS_LINUX_ARM,
                HotUpdateConstant.TERMINAL_COMPONENT_PACKAGE_RESET_FAIL);

        globalParameterAPI.updateParameter(HotUpdateConstant.TERMINAL_COMPONENT_PACKAGE_INIT_STATUS_LINUX_X86,
                HotUpdateConstant.TERMINAL_COMPONENT_PACKAGE_RESET_FAIL);
    }


    private void doTerminalShell() throws BusinessException {
        for (String terminalComponentSh : HotUpdateConstant.TERMINAL_COMPONENT_SH_LIST) {
            try {
                ShellUtils.CommandResult commandResult = ShellUtils.executeShell(new String[]{"/bin/sh", terminalComponentSh});
                if (commandResult.getResult() != 0) {
                    LOGGER.error("执行{}失败", JSON.toJSONString(commandResult));
                    throw new BusinessException(BusinessKey.RCDC_COMMON_EXECUTE_SHELL_RESULT_ERROR, terminalComponentSh,
                            String.valueOf(commandResult.getResult()));
                }
                LOGGER.error("执行成功：{}", JSON.toJSONString(commandResult));
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }
    }

    private boolean hasUpdate() {
        try (Stream<Path> pathStream = Files.find(Paths.get(HotUpdateConstant.DATA_WEB_TERMINAL_PATH), 1, (path, attr) -> attr.isDirectory())) {
            return pathStream.anyMatch(path -> HotUpdateConstant.TERMINAL_COMPONENT_LIST.contains(path.getFileName().toString()));
        } catch (IOException e) {
            LOGGER.error("未找到终端组件升级路径[{}]", e, HotUpdateConstant.DATA_WEB_TERMINAL_PATH);
            // 设置为true，默认执行也不会出现异常
            return true;
        }
    }

}
