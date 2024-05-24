package com.ruijie.rcos.rcdc.rco.module.impl.filedistribution.service.impl;

import com.alibaba.fastjson.JSON;
import com.google.common.collect.Lists;
import com.ruijie.rcos.rcdc.rco.module.def.api.dto.filedistribution.DistributeParameterDTO;
import com.ruijie.rcos.rcdc.rco.module.def.api.enums.FileDistributionExecType;
import com.ruijie.rcos.rcdc.rco.module.impl.BusinessKey;
import com.ruijie.rcos.rcdc.rco.module.impl.dao.DistributeParameterDAO;
import com.ruijie.rcos.rcdc.rco.module.impl.entity.DistributeParameterEntity;
import com.ruijie.rcos.rcdc.rco.module.impl.filedistribution.service.DistributeParameterService;
import com.ruijie.rcos.sk.base.exception.BusinessException;
import com.ruijie.rcos.sk.base.log.Logger;
import com.ruijie.rcos.sk.base.log.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;
import org.springframework.util.CollectionUtils;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

/**
 * Description: Function Description
 * Copyright: Copyright (c) 2022
 * Company: Ruijie Co., Ltd.
 * Create Time: 2022/2/17 10:07
 *
 * @author zhangyichi
 */
@Service
public class DistributeParameterServiceImpl implements DistributeParameterService {
    private static final Logger LOGGER = LoggerFactory.getLogger(DistributeParameterServiceImpl.class);

    /**
     * SQL通配符
     */
    private static final String ANY_STRING_SYMBOL = "%";

    @Autowired
    private DistributeParameterDAO distributeParameterDAO;

    @Override
    public UUID createNewParameter(DistributeParameterDTO parameterDTO) {
        Assert.notNull(parameterDTO, "parameterDTO cannot be null!");
        UUID id = UUID.randomUUID();
        String parameter = JSON.toJSONString(parameterDTO);
        LOGGER.debug("新建分发任务参数记录，id[{}]，参数[{}]", id, parameter);

        DistributeParameterEntity entity = new DistributeParameterEntity();
        entity.setId(id);
        entity.setParameter(parameter);
        distributeParameterDAO.save(entity);
        return id;
    }

    @Override
    public void deleteParameter(UUID parameterId) {
        Assert.notNull(parameterId, "parameterId cannot be null!");

        Optional<DistributeParameterEntity> optional = distributeParameterDAO.findById(parameterId);
        if (!optional.isPresent()) {
            LOGGER.warn("参数[{}]不存在或已删除", parameterId);
            return;
        }
        distributeParameterDAO.deleteById(parameterId);
    }

    @Override
    public DistributeParameterDTO findById(UUID parameterId) throws BusinessException {
        Assert.notNull(parameterId, "parameterId cannot be null!");

        Optional<DistributeParameterEntity> optional = distributeParameterDAO.findById(parameterId);
        DistributeParameterEntity parameterEntity = optional.orElseThrow(() -> {
            LOGGER.error("参数[{}]不存在或已删除", parameterId);
            return new BusinessException(BusinessKey.RCDC_RCO_FILE_DISTRIBUTION_TASK_PARAMETER_NOT_EXIST);
        });
        DistributeParameterDTO parameterDTO = JSON.parseObject(parameterEntity.getParameter(), DistributeParameterDTO.class);
        parameterDTO.setId(parameterEntity.getId());
        return parameterDTO;
    }

    @Override
    public List<UUID> findByParameterContent(String content) {
        Assert.hasText(content, "content cannot be blank!");

        content = ANY_STRING_SYMBOL + content + ANY_STRING_SYMBOL;
        List<DistributeParameterEntity> parameterEntityList = distributeParameterDAO.findByParameterIsLike(content);
        if (CollectionUtils.isEmpty(parameterEntityList)) {
            return Lists.newArrayList();
        }
        return parameterEntityList.stream().map(DistributeParameterEntity::getId).collect(Collectors.toList());
    }
}
