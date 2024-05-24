package com.ruijie.rcos.rcdc.rco.module.web.ctrl.sysmanage.response;

import com.ruijie.rcos.sk.webmvc.api.response.WebResponse;

/**
 * Description: Function Description
 * Copyright: Copyright (c) 2019
 * Company: Ruijie Co., Ltd.
 * Create Time: 2019年03月16日
 *
 * @author GuoZhouYue
 */
public class BaseSaveUpgradePackageWebResponse implements WebResponse {

    private Status status;

    private String message;

    private String msgKey;

    private String[] msgArgArr;

    private Object content;

    @Override
    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    @Override
    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    @Override
    public String getMsgKey() {
        return msgKey;
    }

    public void setMsgKey(String msgKey) {
        this.msgKey = msgKey;
    }

    @Override
    public String[] getMsgArgArr() {
        return msgArgArr;
    }

    public void setMsgArgArr(String[] msgArgArr) {
        this.msgArgArr = msgArgArr;
    }

    public Object getContent() {
        return content;
    }

    public void setContent(Object content) {
        this.content = content;
    }
}
