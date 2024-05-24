package com.ruijie.rcos.rcdc.rco.module.web.ctrl.user.enums;

import org.springframework.util.Assert;

/**
 * Description: 远程协助状态枚举
 * Copyright: Copyright (c) 2020
 * Company: Ruijie Co., Ltd.
 * Create Time: 2020/11/10
 *
 * @author chenjiehui
 */
public enum  RemoteAssistStateEnum {
    WAITING("WAITING", "正在等待用户回应..."),
    AGREE("AGREE", "用户同意了你的远程协助请求"),
    REJECT("REJECT", "用户拒绝了你的远程协助请求"),
    STOP_USER("STOP_USER", "用户关闭了远程协助"),
    STOP_ADMIN("STOP_ADMIN", "管理员关闭了远程协助"),
    BUSY("BUSY", "有其他管理员抢占了远程协助，一台云桌面只允许一个管理员进行远程协助"),
    STOP_TIMEOUT("STOP_TIMEOUT", "用户未回应，已停止远程协助请求"),
    TOKEN_INVALID("TOKEN_INVALID", "当前通道已失效，请稍后重新发起远程协助"),
    IN_REMOTE_ASSIST("IN_REMOTE_ASSIST", "正在进行远程协助"),
    RESPONSE_TIMEOUT("RESPONSE_TIMEOUT", "服务器响应超时，请稍后重新发起远程协助"),
    FINISH("FINISH", "远程协助已结束"),
    LOCK_SCREEN("LOCK_SCREEN", "无法获取远程用户，请先解除远程用户的账户控制"),
    VNC_TIMEOUT("VNC_TIMEOUT", "远程连接超时，请稍后重新发起远程协助"),
    ERROR("ERROR", "程序出现异常，远程协助已断开");

    private String state;
    private String message;

    RemoteAssistStateEnum(String state, String message) {
        this.state = state;
        this.message = message;
    }

    public String getState() {
        return state;
    }


    public String getMessage() {
        return message;
    }


    /**
     * 根据状态值获取提示语
     * @param state 、
     * @return message
     */
    public static String getMessageByState(String state) {
        Assert.notNull(state, "state is not be null");

        for (RemoteAssistStateEnum stateEnum : RemoteAssistStateEnum.values()) {
            if (state.equals(stateEnum.state)) {
                return stateEnum.message;
            }
        }
        return "";
    }



}
