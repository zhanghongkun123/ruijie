package com.ruijie.rcos.rcdc.rco.module.openapi.rest.rccm.response;

import com.ruijie.rcos.rcdc.rco.module.def.api.response.rccm.RccmManageResponse;

/**
 * Description: RccmManageRestServerResponse
 * Copyright: Copyright (c) 2021
 * Company: Ruijie Co., Ltd.
 * Create Time: 2021/10/9
 *
 * @author lihengjing
 */
public class RccmManageRestServerResponse {

    private int code;

    /**
     * 标识此版本统一登录支持辅助认证
     */
    private Boolean hasAssistAuth = Boolean.TRUE;

    private String msg;


    public RccmManageRestServerResponse() {

    }

    public RccmManageRestServerResponse(int code) {
        this.code = code;
    }

    public RccmManageRestServerResponse(RccmManageResponse rccmManageResponse) {
        this.code = rccmManageResponse.getCode();
        this.msg = rccmManageResponse.getMsg();
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public Boolean getHasAssistAuth() {
        return hasAssistAuth;
    }

    public void setHasAssistAuth(Boolean hasAssistAuth) {
        this.hasAssistAuth = hasAssistAuth;
    }
}
