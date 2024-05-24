package com.ruijie.rcos.rcdc.rco.module.impl.spi;

import com.ruijie.rcos.gss.sdk.iac.module.def.api.IacClientAuthSecurityConfigAPI;
import com.ruijie.rcos.gss.sdk.iac.module.def.dto.IacClientAuthSecurityDTO;
import com.ruijie.rcos.rcdc.terminal.module.def.spi.IacClientAuthSecurityConfigSPI;
import com.ruijie.rcos.rcdc.terminal.module.def.spi.response.RcoAuthSecurityDTO;
import com.ruijie.rcos.sk.base.exception.BusinessException;
import com.ruijie.rcos.sk.base.log.Logger;
import com.ruijie.rcos.sk.base.log.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * Description: 客户端安全策略业务处理类
 * Copyright: Copyright (c) 2024
 * Company: Ruijie Co., Ltd.
 * Create Time: 2024/04/22
 *
 * @author brq
 */
public class IacClientAuthSecurityConfigSPIImpl implements IacClientAuthSecurityConfigSPI {

    private static final Logger LOGGER = LoggerFactory.getLogger(IacClientAuthSecurityConfigSPIImpl.class);

    @Autowired
    private IacClientAuthSecurityConfigAPI iacCertifiedSecurityConfigAPI;

    @Override
    public RcoAuthSecurityDTO getDetail() throws BusinessException {
        IacClientAuthSecurityDTO detail = iacCertifiedSecurityConfigAPI.detail();
        RcoAuthSecurityDTO rcoAuthSecurityDTO = new RcoAuthSecurityDTO();
        BeanUtils.copyProperties(detail, rcoAuthSecurityDTO);
        rcoAuthSecurityDTO.setOfflineAutoLocked(detail.getOfflineAutoLocked().getDays());
        return rcoAuthSecurityDTO;
    }
}
