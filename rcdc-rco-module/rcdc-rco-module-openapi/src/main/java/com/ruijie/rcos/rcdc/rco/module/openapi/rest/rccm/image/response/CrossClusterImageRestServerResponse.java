package com.ruijie.rcos.rcdc.rco.module.openapi.rest.rccm.image.response;

/**
 * Description: 镜像同步返回通用类
 * Copyright: Copyright (c) 2018
 * Company: Ruijie Co., Ltd.
 * Create Time: 2023/6/29
 *
 * @author zhiweiHong
 */
public class CrossClusterImageRestServerResponse {

    private int code;

    private String userTip;

    private Object data;


    public CrossClusterImageRestServerResponse() {

    }

    public CrossClusterImageRestServerResponse(int code) {
        this.code = code;
    }


    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getUserTip() {
        return userTip;
    }

    public void setUserTip(String userTip) {
        this.userTip = userTip;
    }

    public Object getData() {
        return data;
    }

    public void setData(Object data) {
        this.data = data;
    }
}
