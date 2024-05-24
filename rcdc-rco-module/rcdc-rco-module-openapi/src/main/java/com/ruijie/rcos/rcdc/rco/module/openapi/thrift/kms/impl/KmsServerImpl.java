package com.ruijie.rcos.rcdc.rco.module.openapi.thrift.kms.impl;

import com.ruijie.rcos.rcdc.rco.module.def.api.LicenseAPI;
import com.ruijie.rcos.rcdc.rco.module.openapi.thrift.kms.KmsServer;
import com.ruijie.rcos.rcdc.rco.module.openapi.thrift.kms.request.KmsRequest;
import com.ruijie.rcos.rcdc.rco.module.openapi.thrift.kms.response.KmsClientResponse;
import com.ruijie.rcos.sk.base.log.Logger;
import com.ruijie.rcos.sk.base.log.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

/**
 * Description: Function Description
 * Copyright: Copyright (c) 2018
 * Company: Ruijie Co., Ltd.
 * Create Time: 2019年4月8日
 * 
 * @author zouqi
 */
@Service
public class KmsServerImpl implements KmsServer {

    private static final Logger LOGGER = LoggerFactory.getLogger(KmsServerImpl.class);

    @Autowired
    private LicenseAPI licenseAPI;
    
    /** 开启状态 */
    private static final int ENABLE_OPEN_KMS = 0;

    /** 禁用状态 */
    private static final int DISABLE_OPEN_KMS = 1;

    @Override
    public KmsClientResponse getWindowsAuth(KmsRequest request) {
        Assert.notNull(request, "request can not be null");

        Boolean enableOpenKms = licenseAPI.obtainEnableOpenKms();
        LOGGER.info("enbaleOpenKms : [{}]", enableOpenKms);
        // 是否开始黄金镜像授权
        Integer kmsStatus = Boolean.TRUE.equals(enableOpenKms) ? ENABLE_OPEN_KMS : DISABLE_OPEN_KMS;
        KmsClientResponse kmsResponse = new KmsClientResponse();
        kmsResponse.setKmsStatus(kmsStatus);
        LOGGER.info("KmsClientResponse value:[{}]", kmsResponse.toString());

        return kmsResponse;
    }

}
