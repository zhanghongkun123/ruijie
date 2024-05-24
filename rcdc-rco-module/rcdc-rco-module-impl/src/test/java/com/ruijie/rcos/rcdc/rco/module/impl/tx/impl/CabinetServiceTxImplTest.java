package com.ruijie.rcos.rcdc.rco.module.impl.tx.impl;

import com.ruijie.rcos.rcdc.rco.module.def.api.dto.bigscreen.CabinetMappingServerDTO;
import com.ruijie.rcos.rcdc.rco.module.impl.BusinessKey;
import com.ruijie.rcos.rcdc.rco.module.impl.dao.CabinetDAO;
import com.ruijie.rcos.rcdc.rco.module.impl.dao.CabinetServerMappingDAO;
import com.ruijie.rcos.rcdc.rco.module.impl.entity.CabinetEntity;
import com.ruijie.rcos.sk.base.exception.BusinessException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import mockit.Expectations;
import mockit.Injectable;
import mockit.Tested;
import mockit.integration.junit4.JMockit;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;

/**
 * Description: 机柜配置服务器本地事务事务实现测试
 * Copyright: Copyright (c) 2019
 * Company: Ruijie Co., Ltd.
 * Create Time: 2019/8/6 10:59
 *
 * @author BaiGuoliang
 */
@RunWith(JMockit.class)
public class CabinetServiceTxImplTest {

    @Tested
    private CabinetServiceTxImpl cabinetServiceTx;

    @Injectable
    private CabinetServerMappingDAO cabinetServerMappingDAO;

    @Injectable
    private CabinetDAO cabinetDAO;

    /**
     * 测试删除方法
     */
    @Test
    public void testDelete() {
        cabinetServiceTx.deleteCabinet(UUID.randomUUID());
        Assert.assertTrue(true);
    }

    /**
     * 测试配置服务器机柜
     *
     * @throws BusinessException 异常
     */
    @Test
    public void testConfigServer() throws BusinessException {
        CabinetMappingServerDTO dto = new CabinetMappingServerDTO();
        dto.setCabinetId(UUID.randomUUID());
        CabinetEntity cabinetEntity = new CabinetEntity();
        cabinetEntity.setServerNum(1);
        Optional<CabinetEntity> entityOptional = Optional.of(cabinetEntity);

        new Expectations() {
            {
                cabinetDAO.findById((UUID) any);
                result = entityOptional;
            }
        };

        cabinetServiceTx.configServer(dto);
        Assert.assertTrue(true);
    }

    /**
     * 测试配置服务器机柜不存在的情况
     *
     * @throws BusinessException 异常
     */
    @Test
    public void testConfigServerWithBusinessException() throws BusinessException {
        CabinetMappingServerDTO dto = new CabinetMappingServerDTO();
        dto.setCabinetId(UUID.randomUUID());
        try {
            cabinetServiceTx.configServer(dto);
        } catch (BusinessException ex) {
            String str = ex.getKey();
            Assert.assertEquals(BusinessKey.RCDC_RCO_CABINET_NOT_EXIST, str);
        }
    }

    /**
     * 测试配置服务器机柜不存在的情况
     *
     * @throws BusinessException 异常
     */
    @Test
    public void testDeleteServerFromCabinet() throws BusinessException {
        UUID cabinetId = UUID.randomUUID();
        List<UUID> serverIdList = new ArrayList<>();
        CabinetEntity cabinetEntity = new CabinetEntity();
        cabinetEntity.setServerNum(1);
        Optional<CabinetEntity> entityOptional = Optional.of(cabinetEntity);

        new Expectations() {
            {
                cabinetDAO.findById((UUID) any);
                result = entityOptional;
            }
        };

        cabinetServiceTx.deleteServerFromCabinet(cabinetId, serverIdList);
        Assert.assertTrue(true);
    }
}








