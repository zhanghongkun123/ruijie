package com.ruijie.rcos.rcdc.rco.module.impl.quartz;

import com.google.common.collect.Lists;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.api.CbbPhysicalServerMgmtAPI;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.dto.PhysicalServerDTO;
import com.ruijie.rcos.rcdc.rco.module.impl.dao.CabinetServerMappingDAO;
import com.ruijie.rcos.rcdc.rco.module.impl.entity.CabinetServerMappingEntity;
import com.ruijie.rcos.rcdc.rco.module.impl.tx.CabinetServiceTx;
import com.ruijie.rcos.sk.base.quartz.QuartzTaskContext;
import mockit.Expectations;
import mockit.Injectable;
import mockit.Tested;
import mockit.integration.junit4.JMockit;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.List;
import java.util.UUID;

/**
 * Description: 定时清理不可用的服务器测试
 * Copyright: Copyright (c) 2019
 * Company: Ruijie Co., Ltd.
 * Create Time: 2019/8/30 15:28
 *
 * @author BaiGuoliang
 */
@RunWith(JMockit.class)
public class ServerSyncQuartzTaskTest {

    @Tested
    private ServerSyncQuartzTask serverSyncQuartzTask;

    @Injectable
    private CbbPhysicalServerMgmtAPI cbbPhysicalServerMgmtAPI;

    @Injectable
    private CabinetServerMappingDAO cabinetServerMappingDAO;

    @Injectable
    private QuartzTaskContext quartzTaskContext;

    @Injectable
    private CabinetServiceTx cabinetServiceTx;

    /**
     * 测试定时任务流程
     *
     * @throws Exception 异常
     */
    @Test
    public void testExecuteWithNull() throws Exception {
        new Expectations() {
            {
                cbbPhysicalServerMgmtAPI.listAllPhysicalServer((Boolean) any);
                result = null;
            }
        };
        serverSyncQuartzTask.execute(quartzTaskContext);
        Assert.assertTrue(true);
    }

    /**
     * 测试定时任务流程
     *
     * @throws Exception 异常
     */
    @Test
    public void testExecute() throws Exception {
        List<PhysicalServerDTO> physicalServerDTOList = Lists.newArrayList();
        PhysicalServerDTO dto = new PhysicalServerDTO();
        dto.setId(UUID.randomUUID());
        physicalServerDTOList.add(dto);

        UUID cabinetId = UUID.randomUUID();
        CabinetServerMappingEntity mappingEntity1 = new CabinetServerMappingEntity();
        mappingEntity1.setCabinetId(cabinetId);
        mappingEntity1.setServerId(UUID.randomUUID());
        CabinetServerMappingEntity mappingEntity2 = new CabinetServerMappingEntity();
        mappingEntity2.setCabinetId(cabinetId);
        mappingEntity2.setServerId(UUID.randomUUID());
        List<CabinetServerMappingEntity> mappingEntityList = Lists.newArrayList();
        mappingEntityList.add(mappingEntity1);
        mappingEntityList.add(mappingEntity2);

        new Expectations() {
            {
                cbbPhysicalServerMgmtAPI.listAllPhysicalServer((Boolean) any);
                result = physicalServerDTOList;
                cabinetServerMappingDAO.getByServerIdIn((List<UUID>) any);
                result = mappingEntityList;
            }
        };
        serverSyncQuartzTask.execute(quartzTaskContext);
        Assert.assertTrue(true);
    }

}
