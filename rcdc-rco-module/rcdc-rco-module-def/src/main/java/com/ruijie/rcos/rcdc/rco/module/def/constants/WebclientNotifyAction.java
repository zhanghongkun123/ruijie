package com.ruijie.rcos.rcdc.rco.module.def.constants;

/**
 * Description: Webclient通知 Action
 * Copyright: Copyright (c) 2022
 * Company: Ruijie Co., Ltd.
 * Create Time: 2022/06/23 11:08
 *
 * @author lihengjing
 */
public interface WebclientNotifyAction {

    /** 远程协助状态变更通知 */
    String NOTIFY_REMOTE_ASSIST = "notify_remote_assist";

    /** 云桌面抢占通知 */
    String NOTIFY_TERMINAL_DESKTOP_IS_ROBBED = "notify_terminal_desktop_is_robbed";

    /** 删除用户变更通知 */
    String NOTIFY_USER_IS_DELETED = "notify_user_is_deleted";
}
