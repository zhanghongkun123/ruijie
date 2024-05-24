package com.ruijie.rcos.rcdc.rco.module.web.ctrl.user.dto;

import com.ruijie.rcos.rcdc.rco.module.web.ctrl.user.enums.RemoteAssistBussinessStateEnum;
import com.ruijie.rcos.rcdc.rco.module.web.ctrl.user.enums.RemoteAssistWindowActionEnums;

/**
 * Description: 返回给EST的远程协助状态
 * Copyright: Copyright (c) 2020
 * Company: Ruijie Co., Ltd.
 * Create Time: 2020/11/10
 *
 * @author chenjiehui
 */
public class RemoteAssistStateDTO {

    /**
     * 弹窗标题
     */
    private String title;

    /**
     * 弹窗信息内容
     */
    private String message;

    /**
     * 弹出动作
     */
    private RemoteAssistWindowActionEnums action;

    /**
     * 业务类型
     */
    private RemoteAssistBussinessStateEnum state;

    /**
     * 远程连接是否可用
     */
    private int available;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public RemoteAssistWindowActionEnums getAction() {
        return action;
    }

    public void setAction(RemoteAssistWindowActionEnums action) {
        this.action = action;
    }

    public RemoteAssistBussinessStateEnum getState() {
        return state;
    }

    public void setState(RemoteAssistBussinessStateEnum state) {
        this.state = state;
    }

    public int getAvailable() {
        return available;
    }

    public void setAvailable(int available) {
        this.available = available;
    }
}
