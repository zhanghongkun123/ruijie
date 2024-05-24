package com.ruijie.rcos.rcdc.rco.module.openapi.thrift.kms;

import com.ruijie.rcos.rcdc.rco.module.openapi.thrift.kms.request.KmsRequest;
import com.ruijie.rcos.rcdc.rco.module.openapi.thrift.kms.response.KmsClientResponse;
import com.ruijie.rcos.sk.connectkit.api.annotation.thrift.ThriftField;
import com.ruijie.rcos.sk.connectkit.api.annotation.thrift.ThriftMethod;
import com.ruijie.rcos.sk.connectkit.api.annotation.thrift.ThriftService;


/**
 * Description: 
 * Copyright: Copyright (c) 2018
 * Company: Ruijie Co., Ltd.
 * Create Time: 2019年4月8日
 * 
 * @author zouqi
 */
@ThriftService("kms")
public interface KmsServer {

    /**
     * kms server
     *
     * @param request request param
     * @return kms response
     */
    @ThriftMethod("get_windows_auth")
    KmsClientResponse getWindowsAuth(@ThriftField(name = "req_msg") KmsRequest request);
}
