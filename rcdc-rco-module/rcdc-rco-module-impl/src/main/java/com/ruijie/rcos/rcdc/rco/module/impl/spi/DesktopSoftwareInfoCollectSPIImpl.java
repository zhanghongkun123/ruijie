package com.ruijie.rcos.rcdc.rco.module.impl.spi;

import com.ruijie.rcos.rcdc.rco.module.def.api.ServerModelAPI;
import com.ruijie.rcos.rcdc.rco.module.impl.cmc.contant.CmcConstants;
import com.ruijie.rcos.rcdc.rco.module.impl.cmc.service.CmcStatusSettingService;
import com.ruijie.rcos.rcdc.rco.module.impl.cmc.dto.CmcMessageQueueUtil;
import com.ruijie.rcos.rcdc.rco.module.impl.cmc.thread.DeskSoftwareInfoConsumeThread;
import com.ruijie.rcos.rcdc.rco.module.impl.cmc.thread.DeskSoftwareLogPrintThread;
import com.ruijie.rcos.rcdc.rco.module.impl.spi.dto.GuestToolNormalMsgDTO;
import com.ruijie.rcos.rcdc.rco.module.impl.spi.guesttool.GuestToolCmdId;
import com.ruijie.rcos.sk.base.config.ConfigFacadeHolder;
import com.ruijie.rcos.sk.base.log.Logger;
import com.ruijie.rcos.sk.base.log.LoggerFactory;
import com.ruijie.rcos.sk.modulekit.api.bootstrap.SafetySingletonInitializer;
import com.ruijie.rcos.sk.modulekit.api.comm.DispatcherImplemetion;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.Assert;

import java.util.Optional;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Description:  GuestTool 上报软件信息
 * Copyright: Copyright (c) 2021
 * Company: Ruijie Co., Ltd.
 * Create Time: 2021.11.05
 *
 * @author LinHJ
 */
@DispatcherImplemetion(GuestToolCmdId.RCDC_GT_DESKSOFT_MSG)
public class DesktopSoftwareInfoCollectSPIImpl extends AbstractGuestToolMessageSPIImpl implements SafetySingletonInitializer {

    private static final Logger LOGGER = LoggerFactory.getLogger(DesktopSoftwareInfoCollectSPIImpl.class);

    @Autowired
    private DeskSoftwareInfoConsumeThread deskSoftwareInfoConsumeThread;

    @Autowired
    private DeskSoftwareLogPrintThread deskSoftwareLogPrintThread;

    @Autowired
    private CmcStatusSettingService cmcStatusSettingService;

    @Autowired
    private CmcMessageQueueUtil cmcMessageQueueUtil;

    @Autowired
    private ServerModelAPI serverModelAPI;

    // CMC 配置业务线程数量
    private static int childThreadNum;

    @Override
    public void safeInit() {

        final boolean isVdiModel = serverModelAPI.isVdiModel();
        String configKey = isVdiModel ? CmcConstants.CONFIG_VDI_CHILD_THREAD_NUM_KEY : CmcConstants.CONFIG_RCM_CHILD_THREAD_NUM_KEY;
        childThreadNum = Optional.ofNullable(ConfigFacadeHolder.getFacade().read(configKey))
                .map(Integer::parseInt).orElse(CmcConstants.DEFAULT_CHILD_THREAD_NUM);

        LOGGER.info("[{}] 部署模式 {}，配置子线程消费数量为：{}", getName(), isVdiModel, childThreadNum);
        if (childThreadNum <= 0) {
            return;
        }

        ExecutorService cmcChildThread = Executors.newFixedThreadPool(childThreadNum);
        for (int index = 0; index < childThreadNum; index++) {
            cmcChildThread.submit(deskSoftwareInfoConsumeThread);
        }

        Executors.newSingleThreadExecutor().submit(deskSoftwareLogPrintThread);
    }

    /**
     * 业务逻辑处理名称
     *
     * @return 名称
     */
    @Override
    public String getName() {
        return "GuestTool 上报软件信息";
    }

    /**
     * 抽象业务处理
     *
     * @param guestToolNormalMsgDTO 处理对象
     */
    @Override
    public void doReceive(GuestToolNormalMsgDTO guestToolNormalMsgDTO) {

        Assert.notNull(guestToolNormalMsgDTO, "guestToolNormalMsgDTO must not be null");

        // 业务配置是否开启 & 是否存在消费线程
        if (cmcStatusSettingService.isOpen() && childThreadNum > 0) {
            cmcMessageQueueUtil.offer(guestToolNormalMsgDTO);
        }
    }
}
