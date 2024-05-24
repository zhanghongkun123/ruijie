package com.ruijie.rcos.rcdc.rco.module.web.validation;

import com.ruijie.rcos.rcdc.rco.module.def.api.CasScanCodeAuthParameterAPI;
import com.ruijie.rcos.rcdc.rco.module.def.cas.qrcode.dto.AuthTokenDTO;
import com.ruijie.rcos.rcdc.rco.module.def.cas.qrcode.dto.CasScanCodeAuthDTO;
import com.ruijie.rcos.rcdc.rco.module.web.ctrl.cas.qrcode.CasScanCodeAuthBusinessKey;
import com.ruijie.rcos.rcdc.rco.module.web.ctrl.cas.qrcode.request.CasScanCodeAuthWebRequest;
import com.ruijie.rcos.sk.base.exception.BusinessException;
import com.ruijie.rcos.sk.base.log.Logger;
import com.ruijie.rcos.sk.base.log.LoggerFactory;
import org.apache.commons.lang3.BooleanUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;
import org.springframework.util.ObjectUtils;

/**
 * Description: CAS相关校验
 * Copyright: Copyright (c) 2021
 * Company: Ruijie Co., Ltd.
 * Create Time: 2021/9/26
 *
 * @author TD
 */
@Service
public class CasScanCodeValidation {

    private static final Logger LOGGER = LoggerFactory.getLogger(CasScanCodeValidation.class);

    @Autowired
    private CasScanCodeAuthParameterAPI scanCodeAuthParameterAPI;

    /**
     * 校验CAS服务是否可用
     * 
     * @param request CAS认证配置参数
     * @throws BusinessException CAS服务异常
     */
    public void updateCasServiceValidate(CasScanCodeAuthWebRequest request) throws BusinessException {
        Assert.notNull(request, "CasScanCodeAuthWebRequest is not null");
        try {
            if (BooleanUtils.toBoolean(request.getCasScanCodeAuth().getApplyOpen())) {
                CasScanCodeAuthDTO scanCodeAuthDTO = new CasScanCodeAuthDTO();
                BeanUtils.copyProperties(request.getCasScanCodeAuth(), scanCodeAuthDTO);
                AuthTokenDTO authTokenDTO = scanCodeAuthParameterAPI.testCasServiceConnectivity(scanCodeAuthDTO);
                if (ObjectUtils.isEmpty(authTokenDTO)) {
                    LOGGER.error("返回鉴权AuthTokenDTO 内容为null，CAS服务器存在有误");
                    throw new BusinessException(CasScanCodeAuthBusinessKey.RCDC_CAS_SERVER_CONNECT_FAIL);
                }
            }
        } catch (Exception e) {
            LOGGER.error("测试CAS服务器连通性出现异常", e);
            throw new BusinessException(CasScanCodeAuthBusinessKey.RCDC_CAS_SERVER_CONNECT_FAIL, e);
        }
    }
}
