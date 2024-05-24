package com.ruijie.rcos.rcdc.rco.module.impl.init;

import com.ruijie.rcos.rcdc.rco.module.common.connect.SessionManager;
import com.ruijie.rcos.rcdc.rco.module.def.api.enums.ComputerStateEnum;
import com.ruijie.rcos.rcdc.rco.module.def.api.enums.ComputerTypeEnum;
import com.ruijie.rcos.rcdc.rco.module.impl.entity.ComputerEntity;
import com.ruijie.rcos.rcdc.rco.module.impl.service.ComputerBusinessService;
import com.ruijie.rcos.sk.base.log.Logger;
import com.ruijie.rcos.sk.base.log.LoggerFactory;
import com.ruijie.rcos.sk.connectkit.api.tcp.session.Session;
import com.ruijie.rcos.sk.modulekit.api.bootstrap.SafetySingletonInitializer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.PriorityOrdered;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.List;
import java.util.Objects;
import java.util.UUID;

/**
 * Description: 初始化pc终端主机状态
 * Copyright: Copyright (c) 2024
 * Company: Ruijie Co., Ltd.
 * Create Time: 2024/3/7
 *
 * @author zqj
 */
@Service
public class RcoComputerStatusInit implements SafetySingletonInitializer, PriorityOrdered {

    private static final Logger LOGGER = LoggerFactory.getLogger(RcoComputerStatusInit.class);


    @Autowired
    private ComputerBusinessService computerBusinessService;

    @Autowired
    private SessionManager sessionManager;

    // SPRING 初始化的优先级
    private static final int ORDER_VALUE = 0;

    @Override
    public void safeInit() {
        initHostAgentState();
    }

    /**
     * 把第三方PC终端在线状态初始化为离线状态
     */
    private void initHostAgentState() {
        List<ComputerEntity> hostAgentList
                = computerBusinessService.findAllByStatusAndType(ComputerStateEnum.ONLINE, ComputerTypeEnum.THIRD);
        if (CollectionUtils.isEmpty(hostAgentList)) {
            LOGGER.debug("没有需要初始化的第三方PC终端状态");
            return;
        }
        LOGGER.warn("存在异常关机导致第三方PC终端状态不一致的情况，总共有{}台PC终端状态需要初始化", hostAgentList.size());

        for (ComputerEntity computerEntity : hostAgentList) {
            UUID computerId = computerEntity.getId();
            LOGGER.info("针对第三方PC终端[{}]进行离线处理", computerId);
            try {
                Session session = sessionManager.getSessionByAlias(computerId.toString());
                if (Objects.nonNull(session)) {
                    LOGGER.info("第三方PC终端[{}]存在绑定会话信息，不能改成离线状态！", computerId);
                    return;
                }
                computerBusinessService.offline(computerId);
            } catch (Exception e) {
                LOGGER.error("处理PC终端{" + computerId + "}下线失败", e);
            }
        }
    }

    @Override
    public int getOrder() {
        return ORDER_VALUE;
    }

}
