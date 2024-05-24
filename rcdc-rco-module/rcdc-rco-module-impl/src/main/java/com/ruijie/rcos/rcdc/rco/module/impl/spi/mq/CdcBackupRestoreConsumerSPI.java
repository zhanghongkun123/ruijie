package com.ruijie.rcos.rcdc.rco.module.impl.spi.mq;

import com.ruijie.rcos.rcdc.backup.module.def.dto.CbbRestoreResourceMqDTO;
import com.ruijie.rcos.sk.connectkit.api.annotation.ApiAction;
import com.ruijie.rcos.sk.connectkit.api.annotation.ApiGroup;
import com.ruijie.rcos.sk.connectkit.api.annotation.mq.MQ;
import com.ruijie.rcos.sk.connectkit.api.annotation.mq.MqConsumer;

/**
 * Description: Cdc备份恢复消息接收
 * Copyright: Copyright (c) 2023
 * Company: Ruijie Co., Ltd.
 * Create Time: 2024年5月16日
 *
 * @author lanzf
 */
@MQ
@ApiGroup("BACKUP-RESTORE")
public interface CdcBackupRestoreConsumerSPI {

    /**
     * 接收到恢复完成消息
     *
     * @param restoreResourceMqDTO 恢复完成DTO
     */
    @ApiAction("resource.restore")
    @MqConsumer(name = "cdcRestore")
    void receiveRestoreMsg(CbbRestoreResourceMqDTO restoreResourceMqDTO);

}

