package com.ruijie.rcos.rcdc.rco.module.impl.spi.mq.impl;

import com.alibaba.fastjson.JSON;
import com.ruijie.rcos.rcdc.backup.module.def.dto.CbbRestoreResourceMqDTO;
import com.ruijie.rcos.rcdc.backup.module.def.enums.MetaType;
import com.ruijie.rcos.rcdc.rco.module.impl.dao.RcoViewUserDAO;
import com.ruijie.rcos.rcdc.rco.module.impl.spi.mq.CdcBackupRestoreConsumerSPI;
import com.ruijie.rcos.sk.base.log.Logger;
import com.ruijie.rcos.sk.base.log.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.Assert;

import java.util.Objects;

/**
 * Description: Cdc备份恢复消息接收
 * Copyright: Copyright (c) 2023
 * Company: Ruijie Co., Ltd.
 * Create Time: 2024年5月16日
 *
 * @author lanzf
 */
public class CdcBackupRestoreConsumerSPIImpl implements CdcBackupRestoreConsumerSPI {

    private static final Logger LOGGER = LoggerFactory.getLogger(CdcBackupRestoreConsumerSPIImpl.class);

    @Autowired
    private RcoViewUserDAO rcoViewUserDAO;

    @Override
    public void receiveRestoreMsg(CbbRestoreResourceMqDTO restoreResourceMqDTO) {
        Assert.notNull(restoreResourceMqDTO, "restoreResourceMqDTO can not be null");

        LOGGER.info("接收到备份恢复完成通知，内容：[{}]", JSON.toJSONString(restoreResourceMqDTO));
        if (!Objects.equals(restoreResourceMqDTO.getMetaType(), MetaType.DATA_BASE.name())) {
            return;
        }

        try {
            rcoViewUserDAO.findByUserName(MetaType.DATA_BASE.name());
        } catch (Throwable e) {
            LOGGER.error("刷新数据库链接异常，异常原因：", e);
        }
    }

}

