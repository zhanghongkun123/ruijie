package com.ruijie.rcos.rcdc.rco.module.impl.connector.tcp.api;


import com.ruijie.rcos.rcdc.rco.module.impl.connector.dto.DesktopBindUserDTO;
import com.ruijie.rcos.rcdc.rco.module.impl.connector.dto.OaUserProfileStrategyDTO;
import com.ruijie.rcos.rcdc.rco.module.impl.spi.common.RcoRcdcToOneAgentAction;
import com.ruijie.rcos.sk.connectkit.api.annotation.ApiAction;
import com.ruijie.rcos.sk.connectkit.api.annotation.tcp.SessionAlias;
import com.ruijie.rcos.sk.connectkit.api.annotation.tcp.Tcp;

/**
 * Description: 桌面用户配置操作TCP消息 API
 * Copyright: Copyright (c) 2024
 * Company: Ruijie Co., Ltd.
 * Create Time: 2024/2/22
 *
 * @author zqj
 */
@Tcp
public interface DesktopUserOpTcpAPI {

    /**
     * cdc推送PC终端用户绑定
     * @param deskId deskId
     * @param desktopBindUserDTO cbbDesktopBindUserDTO
     */
    @ApiAction(RcoRcdcToOneAgentAction.CDC_NOTIFY_OA_DESK_BIND_USER)
    void notifyOaDeskBindUser(@SessionAlias String deskId, DesktopBindUserDTO desktopBindUserDTO);

    /**
     * cdc推送解除PC终端用户绑定
     * @param deskId deskId
     */
    @ApiAction(RcoRcdcToOneAgentAction.CDC_NOTIFY_OA_DESK_UNBIND_USER)
    void notifyOaDeskUnbindUser(@SessionAlias String deskId);

    /**
     * cdc推送桌面个性配置策略信息
     *
     * @param deskId deskId
     * @param oaUserProfileStrategyDTO oaUserProfileStrategyDTO
     */
    @ApiAction(RcoRcdcToOneAgentAction.CDC_NOTIFY_OA_USER_PROFILE_STRATEGY)
    void notifyOaDeskUserProfileStrategy(@SessionAlias String deskId, OaUserProfileStrategyDTO oaUserProfileStrategyDTO);

}
