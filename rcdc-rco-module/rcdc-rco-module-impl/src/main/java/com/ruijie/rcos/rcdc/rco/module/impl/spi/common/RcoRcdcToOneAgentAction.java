package com.ruijie.rcos.rcdc.rco.module.impl.spi.common;

/**
 * Description: Function Description
 * Copyright: Copyright (c) 2024
 * Company: Ruijie Co., Ltd.
 * Create Time: 2024/1/21
 *
 * @author zqj
 */
public interface RcoRcdcToOneAgentAction {

    /**
     * 通知唤醒pc终端
     */
    String WAKE_UP_COMPUTER = "wake_up_computer";



    /**
     * cdc推送PC终端用户绑定
     */
    String CDC_NOTIFY_OA_DESK_BIND_USER = "cdc_notify_oa_desk_bind_user";

    /**
     * cdc推送解除PC终端用户绑定
     */
    String CDC_NOTIFY_OA_DESK_UNBIND_USER = "cdc_notify_oa_desk_unbind_user";

    /**
     * cdc推送桌面池配置
     */
    String CDC_NOTIFY_OA_COMPUTER_INFO = "cdc_notify_oa_computer_info";

    /**
     * 推送用户个性化配置
     */
    String CDC_NOTIFY_OA_USER_PROFILE_STRATEGY = "cdc_notify_oa_user_profile_strategy";

}
