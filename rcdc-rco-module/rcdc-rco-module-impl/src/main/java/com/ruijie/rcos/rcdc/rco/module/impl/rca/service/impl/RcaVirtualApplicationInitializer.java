package com.ruijie.rcos.rcdc.rco.module.impl.rca.service.impl;

import com.ruijie.rcos.rcdc.rca.module.def.api.RcaTrusteeshipHostAPI;
import com.ruijie.rcos.rcdc.rca.module.def.dto.RcaTrusteeshipHostDTO;
import com.ruijie.rcos.rcdc.rco.module.def.api.RcaSupportAPI;
import com.ruijie.rcos.rcdc.rco.module.impl.Constants;
import com.ruijie.rcos.rcdc.rco.module.impl.dao.RcoGlobalParameterDAO;
import com.ruijie.rcos.rcdc.rco.module.impl.entity.RcoGlobalParameterEntity;
import com.ruijie.rcos.sk.base.exception.BusinessException;
import com.ruijie.rcos.sk.base.log.Logger;
import com.ruijie.rcos.sk.base.log.LoggerFactory;
import com.ruijie.rcos.sk.modulekit.api.bootstrap.SafetySingletonInitializer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.List;

/**
 * Description: 虚拟应用初始化
 * Copyright: Copyright (c) 2022
 * Company: Ruijie Co., Ltd.
 * Create Time: 2023/05/08
 *
 * @author zjy
 */
@Service
public class RcaVirtualApplicationInitializer implements SafetySingletonInitializer {

    private static final Logger LOGGER = LoggerFactory.getLogger(RcaVirtualApplicationInitializer.class);

    @Autowired
    private RcoGlobalParameterDAO rcoGlobalParameterDAO;

    @Autowired
    private RcaTrusteeshipHostAPI rcaTrusteeshipHostAPI;

    @Autowired
    private RcaSupportAPI rcaSupportAPI;

    @Override
    public void safeInit() {
        RcoGlobalParameterEntity virtualApplicationStateEntity = rcoGlobalParameterDAO.findByParamKey(Constants.VIRTUAL_APPLICATION_STATE);
        if (virtualApplicationStateEntity == null) {
            LOGGER.info("跳过初始化虚拟应用升级处理，没有查询到虚拟应用全局配置");
            return;
        }

        if (virtualApplicationStateEntity.getVersion() != 0) {
            LOGGER.info("跳过初始化虚拟应用升级处理，非首次升级场景");
            return;
        }

        List<RcaTrusteeshipHostDTO> trusteeshipHostList = rcaTrusteeshipHostAPI.getAllVmInfo();
        Boolean isVirtualApplicationEnable = Boolean.FALSE;
        if (CollectionUtils.isEmpty(trusteeshipHostList)) {
            LOGGER.info("初始化虚拟应用升级处理，首次升级不存在应用主机，判定为未使用该功能");
        } else {
            isVirtualApplicationEnable = Boolean.TRUE;
            LOGGER.info("初始化虚拟应用升级处理，首次升级存在应用主机，判定为已使用该功能，将虚拟应用状态修改为启用");
        }

        try {
            rcaSupportAPI.modifyVirtualApplicationState(isVirtualApplicationEnable);
        } catch (BusinessException e) {
            LOGGER.error("更新虚拟应用启用状态发生异常", e);
        }
    }
}