package com.ruijie.rcos.rcdc.rco.module.def.api.request;

import com.ruijie.rcos.sk.base.annotation.NotNull;
import com.ruijie.rcos.sk.modulekit.api.comm.Request;
import org.springframework.lang.Nullable;

import java.util.UUID;

/**
 * Description: Function Description
 * Copyright: Copyright (c) 2018
 * Company: Ruijie Co., Ltd.
 * Create Time: 2019年1月2日
 * 
 * @author zhuangchenwu
 */
public class RemoteAssistUserConfirmNotifyReqeust implements Request {

    @Nullable
    private UUID deskId;

    @NotNull
    private String business;

    @Nullable
    private String passwd;

    @NotNull
    private Integer port;

    @Nullable
    private Integer status;
    
    @NotNull
    private Integer code;
    
    public Integer getCode() {
        return code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }

    @Nullable
    public UUID getDeskId() {
        return deskId;
    }

    public void setDeskId(@Nullable UUID deskId) {
        this.deskId = deskId;
    }

    public String getBusiness() {
        return business;
    }

    public void setBusiness(String business) {
        this.business = business;
    }

    @Nullable
    public String getPasswd() {
        return passwd;
    }

    public void setPasswd(@Nullable String passwd) {
        this.passwd = passwd;
    }

    public Integer getPort() {
        return port;
    }

    public void setPort(Integer port) {
        this.port = port;
    }

    /**
     * 远程协助请求结果类型，0：接受，1：拒绝
     * @return 请求结果
     */
    @Nullable
    public Integer getStatus() {
        return status;
    }

    public void setStatus(@Nullable Integer status) {
        this.status = status;
    }

    @Override
    public String toString() {
        return "AssistantMessageResponseDTO{" +
                "business='" + business + '\'' +
                ", passwd='" + passwd + '\'' +
                ", port=" + port +
                ", status=" + status +
                '}';
    }
    
}
