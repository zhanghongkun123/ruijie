package com.ruijie.rcos.rcdc.rco.module.impl.tx.impl;

import com.ruijie.rcos.rcdc.rco.module.def.api.dto.bigscreen.CabinetMappingServerDTO;
import com.ruijie.rcos.rcdc.rco.module.impl.BusinessKey;
import com.ruijie.rcos.rcdc.rco.module.impl.dao.CabinetDAO;
import com.ruijie.rcos.rcdc.rco.module.impl.dao.CabinetServerMappingDAO;
import com.ruijie.rcos.rcdc.rco.module.impl.entity.CabinetEntity;
import com.ruijie.rcos.rcdc.rco.module.impl.entity.CabinetServerMappingEntity;
import com.ruijie.rcos.rcdc.rco.module.impl.tx.CabinetServiceTx;
import com.ruijie.rcos.sk.base.exception.BusinessException;
import com.ruijie.rcos.sk.base.log.Logger;
import com.ruijie.rcos.sk.base.log.LoggerFactory;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

/**
 * Description: 机柜配置服务器事务实现
 * Copyright: Copyright (c) 2019
 * Company: Ruijie Co., Ltd.
 * Create Time: 2019年7月24日
 *
 * @author bgl
 */
@Service
public class CabinetServiceTxImpl implements CabinetServiceTx {

    private static final Logger LOGGER = LoggerFactory.getLogger(CabinetServiceTxImpl.class);

    @Autowired
    private CabinetServerMappingDAO cabinetServerMappingDAO;

    @Autowired
    private CabinetDAO cabinetDAO;

    @Override
    public void configServer(CabinetMappingServerDTO dto) throws BusinessException {
        Assert.notNull(dto, "dto不能为空");

        Optional<CabinetEntity> entityOptional = cabinetDAO.findById(dto.getCabinetId());
        if (!entityOptional.isPresent()) {
            LOGGER.error("cabinet info is not exist, id[{}]", dto.getCabinetId());
            throw new BusinessException(BusinessKey.RCDC_RCO_CABINET_NOT_EXIST, dto.getCabinetId().toString());
        }

        CabinetServerMappingEntity mappingEntity = new CabinetServerMappingEntity();
        BeanUtils.copyProperties(dto, mappingEntity);
        cabinetServerMappingDAO.save(mappingEntity);

        CabinetEntity entity = entityOptional.get();
        entity.setServerNum(entity.getServerNum() + 1);
        cabinetDAO.save(entity);
    }

    @Override
    public void deleteCabinet(UUID cabinetId) {
        Assert.notNull(cabinetId, "cabinetId cannot be null!");

        cabinetDAO.deleteById(cabinetId);
        cabinetServerMappingDAO.deleteByCabinetId(cabinetId);
    }

    @Override
    public void deleteServerFromCabinet(UUID cabinetId, List<UUID> serverIdList) throws BusinessException {
        Assert.notNull(cabinetId, "cabinetId不能为空");
        Assert.notNull(serverIdList, "serverIdList不能为空");

        Optional<CabinetEntity> entityOptional = cabinetDAO.findById(cabinetId);
        if (!entityOptional.isPresent()) {
            LOGGER.error("cabinet info is not exist, id[{}]", cabinetId);
            throw new BusinessException(BusinessKey.RCDC_RCO_CABINET_NOT_EXIST, cabinetId.toString());
        }

        cabinetServerMappingDAO.deleteByServerIdIn(serverIdList);

        CabinetEntity entity = entityOptional.get();
        entity.setServerNum(entity.getServerNum() - serverIdList.size());

        cabinetDAO.save(entity);
    }
}