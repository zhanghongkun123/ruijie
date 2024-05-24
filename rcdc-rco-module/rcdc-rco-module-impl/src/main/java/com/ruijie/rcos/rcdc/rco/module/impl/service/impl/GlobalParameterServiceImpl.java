package com.ruijie.rcos.rcdc.rco.module.impl.service.impl;

import com.ruijie.rcos.rcdc.rco.module.impl.dto.global.GlobalParameterDTO;
import com.ruijie.rcos.sk.modulekit.api.tool.GlobalParameterAPI;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;
import com.ruijie.rcos.rcdc.rco.module.impl.dao.RcoGlobalParameterDAO;
import com.ruijie.rcos.rcdc.rco.module.impl.entity.RcoGlobalParameterEntity;
import com.ruijie.rcos.rcdc.rco.module.impl.service.GlobalParameterService;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * Description: GlobalParameterServiceImpl
 * Copyright: Copyright (c) 2019
 * Company: Ruijie Co., Ltd.
 * Create Time: 2019-08-29
 *
 * @author hli
 */
@Service("rcoGlobalParameterServiceImpl")
public class GlobalParameterServiceImpl implements GlobalParameterService {

    @Autowired
    private RcoGlobalParameterDAO rcoGlobalParameterDAO;

    @Autowired
    private GlobalParameterAPI globalParameterAPI;

    @Override
    public void updateParameter(String paramKey, @Nullable String value) {
        Assert.hasText(paramKey, "paramKey is not empty");
        rcoGlobalParameterDAO.updateValueByParamKey(paramKey, value);
    }

    @Override
    public String findParameter(String key) {
        Assert.hasText(key, "key is not empty");
        return rcoGlobalParameterDAO.findValueByParamKey(key);
    }

    @Override
    public void saveParameter(RcoGlobalParameterEntity entity) {
        Assert.notNull(entity, "entity can not be null");
        rcoGlobalParameterDAO.save(entity);
    }

    @Override
    public String findDefaultValue(String paramKey) {
        Assert.notNull(paramKey, "paramKey can not be null");

        return rcoGlobalParameterDAO.obtainDefaultValueByParamKey(paramKey);
    }

    /**
     * 批量获取全局配置参数
     *
     * @param paramKeys
     * @return
     */
    @Override
    public List<GlobalParameterDTO> findParameters(Set<String> paramKeys) {
        Assert.notNull(paramKeys, "paramKeys can not be null");
        Assert.notEmpty(paramKeys, "paramKeys can not empty");

        List<RcoGlobalParameterEntity> rcoGlobalParameterEntityList = rcoGlobalParameterDAO.findByParamKeyIn(paramKeys);
        return rcoGlobalParameterEntityList.stream().map(entity -> {
            GlobalParameterDTO globalParameterDTO = new GlobalParameterDTO(entity.getParamKey(), entity.getParamValue());
            return globalParameterDTO;
        }).collect(Collectors.toList());
    }

    @Override
    public boolean existParamKey(String paramKey) {
        Assert.hasText(paramKey, "paramKey can not empty");
        return rcoGlobalParameterDAO.existsByParamKey(paramKey);
    }
}
