package com.ruijie.rcos.rcdc.rco.module.impl.service.impl.connector.mq.api;

import com.ruijie.rcos.rcdc.rco.module.def.api.dto.uws.UwsCloudDeskUpdateDTO;
import com.ruijie.rcos.rcdc.rco.module.def.api.dto.uws.UwsTerminalStateUpdateDTO;
import com.ruijie.rcos.sk.base.annotation.NotNull;
import com.ruijie.rcos.sk.connectkit.api.annotation.ApiAction;
import com.ruijie.rcos.sk.connectkit.api.annotation.ApiGroup;
import com.ruijie.rcos.sk.connectkit.api.annotation.mq.MQ;

import java.util.UUID;

/**
 * Description: UWS 云桌面相关MQ
 * Copyright: Copyright (c) 2018
 * Company: Ruijie Co., Ltd.
 * Create Time: 2021-11-17 20:29:00
 *
 * @author zjy
 */
@MQ
@ApiGroup("uwsCloudDeskMq")
public interface UwsCloudDeskProducerAPI {

    /**
     * 云桌面更新通知
     *
     * @param uwsCloudDeskUpdateDTO 桌面信息
     * @Author: zjy
     * @Date: 2021/11/18 14:27
     **/
    @ApiAction("update")
    void notifyUpdate(@NotNull UwsCloudDeskUpdateDTO uwsCloudDeskUpdateDTO);

    /**
     * 删除云桌面通知
     *
     * @param desktopId 桌面id
     * @Author: zjy
     * @Date: 2021/11/18 14:27
     **/
    @ApiAction("delete")
    void notifyDelete(@NotNull UUID desktopId);

    /**
     * 新增云桌面
     *
     * @param uwsCloudDeskUpdateDTO 桌面信息
     * @Date 2021/11/18 14:31
     * @Author zjy
     **/
    @ApiAction("add")
    void notifyAdd(@NotNull UwsCloudDeskUpdateDTO uwsCloudDeskUpdateDTO);

    /**
     * 恢复云桌面
     *
     * @param uwsCloudDeskUpdateDTO 桌面信息
     * @Date 2021/12/6 10:31
     * @Author zjy
     **/
    @ApiAction("recover")
    void notifyRecover(@NotNull UwsCloudDeskUpdateDTO uwsCloudDeskUpdateDTO);

    /**
     * 终端状态更新通知
     *
     * @param uwsTerminalStateUpdateDTO 终端状态信息
     * @Author: zjy
     * @Date: 2022/04/06 14:27
     **/
    @ApiAction("terminalStateUpdate")
    void terminalStateUpdate(@NotNull UwsTerminalStateUpdateDTO uwsTerminalStateUpdateDTO);
}
