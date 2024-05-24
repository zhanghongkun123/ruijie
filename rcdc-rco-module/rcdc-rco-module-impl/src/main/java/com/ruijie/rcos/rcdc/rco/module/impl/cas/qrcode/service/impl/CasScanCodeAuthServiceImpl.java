package com.ruijie.rcos.rcdc.rco.module.impl.cas.qrcode.service.impl;

import com.alibaba.fastjson.JSON;
import com.ruijie.rcos.rcdc.rco.module.def.cas.qrcode.constants.CasScanCodeAuthConstants;
import com.ruijie.rcos.rcdc.rco.module.def.cas.qrcode.dto.CasScanCodeAuthDTO;
import com.ruijie.rcos.rcdc.rco.module.impl.cas.qrcode.service.CasScanCodeAuthService;
import com.ruijie.rcos.rcdc.rco.module.impl.dao.RcoGlobalParameterDAO;
import com.ruijie.rcos.rcdc.rco.module.impl.entity.RcoGlobalParameterEntity;
import com.ruijie.rcos.sk.base.log.Logger;
import com.ruijie.rcos.sk.base.log.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

/**
 * Description:
 * Copyright: Copyright (c) 2021
 * Company: Ruijie Co., Ltd.
 * Create Time: 2021/6/23
 *
 * @author TD
 */
@Service
public class CasScanCodeAuthServiceImpl implements CasScanCodeAuthService {

    private static final Logger LOGGER = LoggerFactory.getLogger(CasScanCodeAuthServiceImpl.class);

    @Autowired
    private RcoGlobalParameterDAO rcoGlobalParameterDAO;

    @Override
    public CasScanCodeAuthDTO getCasScanCodeAuthInfo() {
        RcoGlobalParameterEntity globalParameterEntity =
                rcoGlobalParameterDAO.findByParamKey(CasScanCodeAuthConstants.CAS_SCAN_CODE_AUTH);
        // json转DTO
        return JSON.parseObject(globalParameterEntity.getParamValue(),CasScanCodeAuthDTO.class);
    }

    @Override
    public void updateCasScanCodeAuthInfo(CasScanCodeAuthDTO casScanCodeAuthDTO) {
        Assert.notNull(casScanCodeAuthDTO, "CasScanCodeAuthDTO can not be null");
        RcoGlobalParameterEntity globalParameterEntity =
                rcoGlobalParameterDAO.findByParamKey(CasScanCodeAuthConstants.CAS_SCAN_CODE_AUTH);
        // DTO转换存储的json数据
        String jsonStr = JSON.toJSONString(casScanCodeAuthDTO);
        if (LOGGER.isInfoEnabled()) {
            LOGGER.info("认证策略配置更新，当前配置内容为[{}]", jsonStr);
        }
        globalParameterEntity.setParamValue(jsonStr);
        rcoGlobalParameterDAO.save(globalParameterEntity);
    }
}
