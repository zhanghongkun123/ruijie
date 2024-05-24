package com.ruijie.rcos.rcdc.rco.module.openapi.thrift.kms.request;

import org.springframework.lang.Nullable;

import com.ruijie.rcos.sk.connectkit.api.annotation.thrift.ThriftField;
import com.ruijie.rcos.sk.connectkit.api.annotation.thrift.ThriftStruct;


/**
 * Description: Function Description
 * Copyright: Copyright (c) 2018
 * Company: Ruijie Co., Ltd.
 * Create Time: 2019年4月10日
 * 
 * @author zouqi
 */
@ThriftStruct("ReqMsg")
public class KmsRequest {

    @Nullable
    private String hostName;

    @Nullable
    private String uuid;

    @ThriftField(name = "hostname", value = 1, required = ThriftField.Requiredness.OPTIONAL)
    public String getHostName() {
        return hostName;
    }

    public void setHostName(String hostName) {
        this.hostName = hostName;
    }

    @ThriftField(name = "uuid", value = 2, required = ThriftField.Requiredness.OPTIONAL)
    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }
}
