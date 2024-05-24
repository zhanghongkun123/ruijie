package com.ruijie.rcos.rcdc.rco.module.impl.tx.impl;

import com.google.common.collect.Lists;
import com.ruijie.rcos.rcdc.rco.module.def.api.dto.bigscreen.EditCommonConfigDTO;
import com.ruijie.rcos.rcdc.rco.module.def.api.enums.ConfigKeyEnum;
import com.ruijie.rcos.rcdc.rco.module.impl.BusinessKey;
import com.ruijie.rcos.rcdc.rco.module.impl.cache.ServerForecastCache;
import com.ruijie.rcos.rcdc.rco.module.impl.dao.CommonConfigDAO;
import com.ruijie.rcos.rcdc.rco.module.impl.entity.CommonConfigEntity;
import com.ruijie.rcos.rcdc.rco.module.impl.tx.CommonConfigTx;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import com.ruijie.rcos.sk.base.exception.BusinessException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

/**
 * Description: Function Description
 * Copyright: Copyright (c) 2019
 * Company: Ruijie Co., Ltd.
 * Create Time: 2017/7/24
 *
 * @author zhangyichi
 */
@Service
public class CommonConfigTxImpl implements CommonConfigTx {

    @Autowired
    CommonConfigDAO commonConfigDAO;

    @Autowired
    private ServerForecastCache serverForecastCache;

    @Override
    public void edit(EditCommonConfigDTO[] dtoArr) throws BusinessException {
        Assert.notEmpty(dtoArr, "parameter array cannot be null!");

        List<ConfigKeyEnum> configKeyEnumList = bulidConfigKeyEnumList();
        for (EditCommonConfigDTO dto : dtoArr) {
            CommonConfigEntity entity = this.getCommonConfigEntityById(dto.getId());

            // 判断：如果修改了
            if (!dto.getConfigValue().equals(entity.getConfigValue()) && configKeyEnumList.contains(entity.getConfigKey())) {
                serverForecastCache.clear();
            }
            entity.setConfigValue(dto.getConfigValue());
            commonConfigDAO.save(entity);
        }
    }

    private List<ConfigKeyEnum> bulidConfigKeyEnumList() {
        List<ConfigKeyEnum> configKeyEnumList = Lists.newArrayList();
        configKeyEnumList.add(ConfigKeyEnum.BIGSCREEN_SERVER_FORECAST_LIMIT_CPU);
        configKeyEnumList.add(ConfigKeyEnum.BIGSCREEN_SERVER_FORECAST_LIMIT_MEMORY);
        configKeyEnumList.add(ConfigKeyEnum.BIGSCREEN_SERVER_FORECAST_LIMIT_DISK);
        configKeyEnumList.add(ConfigKeyEnum.BIGSCREEN_SERVER_FORECAST_TIME_CPU);
        configKeyEnumList.add(ConfigKeyEnum.BIGSCREEN_SERVER_FORECAST_TIME_MEMORY);
        configKeyEnumList.add(ConfigKeyEnum.BIGSCREEN_SERVER_FORECAST_TIME_DISK);

        return configKeyEnumList;
    }

    /**
     * @param dtoId
     * @return
     * @throws BusinessException 业务异常
     */
    private CommonConfigEntity getCommonConfigEntityById(UUID dtoId) throws BusinessException {
        Optional<CommonConfigEntity> optional = commonConfigDAO.findById(dtoId);
        if (!optional.isPresent()) {
            throw new BusinessException(BusinessKey.RCDC_ENTITY_NOT_FIND);
        }
        return optional.get();
    }

}