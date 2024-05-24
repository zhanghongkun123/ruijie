package com.ruijie.rcos.rcdc.rco.module.openapi.thrift.kms.response;

import com.ruijie.rcos.sk.connectkit.api.annotation.thrift.ThriftField;
import com.ruijie.rcos.sk.connectkit.api.annotation.thrift.ThriftStruct;
import com.ruijie.rcos.sk.modulekit.api.comm.DefaultResponse;

/**
 * Description: ksm service response
 * Copyright: Copyright (c) 2019
 * Company: Ruijie Co., Ltd.
 * Create Time: 2019/4/8
 *
 * @author zouqi
 */
@ThriftStruct("RetMsg")
public class KmsClientResponse extends DefaultResponse {

    private Integer kmsStatus;

    private String msg;

    private Long timeStamp;

    @ThriftField(name = "status", value = 1)
    public Integer getKmsStatus() {
        return kmsStatus;
    }

    public void setKmsStatus(Integer kmsStatus) {
        this.kmsStatus = kmsStatus;
    }

    @ThriftField(name = "msg", value = 2)
    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    @ThriftField(name = "timestamp", value = 3)
    public Long getTimeStamp() {
        return timeStamp;
    }

    public void setTimeStamp(Long timeStamp) {
        this.timeStamp = timeStamp;
    }

    @Override
    public String toString() {
        return "KmsResponseDTO{" +
                "status=" + kmsStatus +
                ", msg='" + msg + '\'' +
                ", timeStamp=" + timeStamp +
                '}';
    }
}
