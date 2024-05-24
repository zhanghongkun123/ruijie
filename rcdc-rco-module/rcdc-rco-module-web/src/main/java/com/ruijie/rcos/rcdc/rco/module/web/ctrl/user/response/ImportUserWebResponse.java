package com.ruijie.rcos.rcdc.rco.module.web.ctrl.user.response;

import com.ruijie.rcos.sk.webmvc.api.response.WebResponse;

/**
 * Description: 用户导入业务异常响应消息对象
 * Copyright: Copyright (c) 2018
 * Company: Ruijie Co., Ltd.
 * Create Time: 2018/11/21
 *
 * @author Jarman
 */
public class ImportUserWebResponse implements WebResponse {

    private Status status;

    private String message;

    private Object content;


    @Override
    public Status getStatus() {
        return this.status;
    }

    @Override
    public String getMessage() {
        return this.message;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Object getContent() {
        return content;
    }

    public void setContent(Object content) {
        this.content = content;
    }

    @Override
    public String getMsgKey() {
        //
        return null;
    }

    @Override
    public String[] getMsgArgArr() {
        return new String[0];
    }
}