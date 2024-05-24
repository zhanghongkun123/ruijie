package com.ruijie.rcos.rcdc.rco.module.impl.spi;

import com.ruijie.rcos.rcdc.rco.module.impl.spi.dto.StartVmDispatcherDTO;
import com.ruijie.rcos.rcdc.rco.module.impl.spi.spimsghandler.AbstractMessageHandler;
import com.ruijie.rcos.rcdc.rco.module.impl.spi.startvmhandler.*;
import com.ruijie.rcos.sk.base.log.Logger;
import com.ruijie.rcos.sk.base.log.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;

/**
 * Description: 根据虚机所处的状态执行启动虚机处理过程
 * Copyright: Copyright (c) 2018
 * Company: Ruijie Co., Ltd.
 * Create Time: 2019/7/12
 *
 * @author Jarman
 */
@Service
public class StartVmByStateHandler extends AbstractMessageHandler<StartVmDispatcherDTO> {

    private static final Logger LOGGER = LoggerFactory.getLogger(StartVmByStateHandler.class);

    @Autowired
    private StartVmFromCloseStateHandler closeStateHandler;

    @Autowired
    private StartVmFromDeletingStateHandler deletingStateHandler;

    @Autowired
    private StartVmFromErrorStateHandler errorStateHandler;

    @Autowired
    private StartVmFromMovingStateHandler movingStateHandler;

    @Autowired
    private StartVmFromRunningStateHandler runningStateHandler;

    @Autowired
    private StartVmFromSleepStateHandler sleepStateHandler;

    @Autowired
    private StartVmFromStartUpStateHandler startUpStateHandler;

    @Autowired
    private StartVmFromUnavailableStateHandler unavailableStateHandler;

    @Autowired
    private StartVmFromBackupCreatingStateHandler startVmFromBackupCreatingStateHandler;

    @PostConstruct
    @Override
    protected void register() {
        LOGGER.info("StartVmByStateHandler注册虚机启动处理");
        handlers.add(startVmFromBackupCreatingStateHandler);
        handlers.add(closeStateHandler);
        handlers.add(runningStateHandler);
        handlers.add(startUpStateHandler);
        handlers.add(unavailableStateHandler);
        handlers.add(sleepStateHandler);
        handlers.add(deletingStateHandler);
        handlers.add(movingStateHandler);
        handlers.add(errorStateHandler);
    }
}
