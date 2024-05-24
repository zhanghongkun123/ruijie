package com.ruijie.rcos.rcdc.rco.module.web.ctrl.user.enums;

import com.ruijie.rcos.rcdc.rco.module.def.api.enums.RemoteAssistState;
import org.springframework.util.Assert;


/**
 * Description: 远程协助窗口动作枚举
 * Copyright: Copyright (c) 2020
 * Company: Ruijie Co., Ltd.
 * Create Time: 2020/11/10
 *
 * @author chenjiehui
 */
public enum  RemoteAssistWindowActionEnums {
    /**
     * 仅弹出窗口进行信息提示
     */
    INFO,

    /**
     * 弹出窗口提示已断开，并关闭链接
     */
    CLOSE,

    /**
     * 不弹出窗口，只返回信息
     */
    MESSAGE;



    /**
     * 根据状态值获取窗口动作
     * @param state 、
     * @return action
     */
    public static RemoteAssistWindowActionEnums getActionByState(String state) {
        Assert.notNull(state, "state is not be null");

        if (state.equals(RemoteAssistState.AGREE.toString()) || state.equals(RemoteAssistState.IN_REMOTE_ASSIST.toString())) {
            return RemoteAssistWindowActionEnums.MESSAGE;
        }

        if (state.equals(RemoteAssistState.WAITING.toString())) {
            return RemoteAssistWindowActionEnums.INFO;
        }

        return RemoteAssistWindowActionEnums.CLOSE;
    }

}
