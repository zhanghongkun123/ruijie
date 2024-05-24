package com.ruijie.rcos.rcdc.rco.module.impl.spi;

import com.ruijie.rcos.rcdc.clouddesktop.module.def.spi.request.CbbDeskOperateNotifyRequest;
import com.ruijie.rcos.rcdc.rco.module.impl.spi.desktopoperate.*;
import com.ruijie.rcos.rcdc.rco.module.impl.spi.spimsghandler.AbstractMessageHandler;
import com.ruijie.rcos.sk.base.log.Logger;
import com.ruijie.rcos.sk.base.log.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;

/**
 * Description: 接收zeta桌面消息事件处理器
 * Copyright: Copyright (c) 2018
 * Company: Ruijie Co., Ltd.
 * Create Time: 2019/7/12
 *
 * @author Jarman
 */
@Service
public class DesktopOperateHandler extends AbstractMessageHandler<CbbDeskOperateNotifyRequest> {

    private static final Logger LOGGER = LoggerFactory.getLogger(DesktopOperateHandler.class);

    /**
     * est关闭桌面事件处理
     */
    @Autowired
    private CloseDeskByEstHandler closeDeskByEstEventHandler;

    /**
     * 启动桌面事件处理
     */
    @Autowired
    private StartDeskHandler startDeskEventHandler;

    /**
     * 登录桌面事件处理
     */
    @Autowired
    private LoginDeskHandler loginDeskHandler;

    /**
     * 系统关闭桌面事件处理
     */
    @Autowired
    private CloseDeskBySystemHandler closeDeskBySystemEventHandler;

    /**
     * 发布镜像模板关闭桌面事件处理
     */
    @Autowired
    private CloseDeskByPublishImageHandler closeDeskByPublishImageEventHandler;

    /**
     * 自动休眠云桌面时间处理
     */
    @Autowired
    private SleepDeskBySystemHandler sleepDeskBySystemHandler;

    @Autowired
    private CreateDeskHandler createDeskHandler;

    @Autowired
    private RecycleBinDeleteDeskHandler recycleBinDeleteDeskHandler;

    @Autowired
    private ShutdownIDVDesktopHandler shutdownIDVDesktopHandler;

    @Autowired
    private StartIDVDesktopHandler startIDVDesktopHandler;

    @Autowired
    private CreateIDVDesktopHandler createIDVDesktopHandler;

    @Autowired
    private RestoreDeskHandler restoreDeskHandler;

    @Autowired
    private SoftDeleteDeskHandler softDeleteDeskHandler;

    @Autowired
    private RecoverDeskHandler recoverDeskHandler;

    @Autowired
    private StartUpDesktopHandler startUpDesktopHandler;

    @Autowired
    private WakingDesktopHandler wakingDesktopHandler;

    @Autowired
    private ShuttingDesktopHandler shuttingDesktopHandler;

    @Autowired
    private SleepingDesktopHandler sleepingDesktopHandler;

    @Autowired
    private DeskStateUpdateHandler deskStateUpdateHandler;

    @PostConstruct
    @Override
    protected void register() {
        LOGGER.info("DesktopOperateHandler注册桌面事件处理");
        handlers.add(closeDeskByEstEventHandler);
        handlers.add(startDeskEventHandler);
        handlers.add(closeDeskBySystemEventHandler);
        handlers.add(closeDeskByPublishImageEventHandler);
        handlers.add(loginDeskHandler);
        handlers.add(sleepDeskBySystemHandler);
        handlers.add(createDeskHandler);
        handlers.add(recycleBinDeleteDeskHandler);
        handlers.add(shutdownIDVDesktopHandler);
        handlers.add(startIDVDesktopHandler);
        handlers.add(createIDVDesktopHandler);
        handlers.add(restoreDeskHandler);
        handlers.add(softDeleteDeskHandler);
        handlers.add(recoverDeskHandler);
        handlers.add(startUpDesktopHandler);
        handlers.add(wakingDesktopHandler);
        handlers.add(shuttingDesktopHandler);
        handlers.add(sleepingDesktopHandler);
        handlers.add(deskStateUpdateHandler);
    }
}
