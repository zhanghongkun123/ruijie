package com.ruijie.rcos.rcdc.rco.module.impl.api;

import com.google.common.collect.Lists;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.api.CbbPhysicalServerMgmtAPI;
import com.ruijie.rcos.rcdc.rco.module.def.api.dto.ClusterServerTrendDTO;
import com.ruijie.rcos.rcdc.rco.module.impl.dao.ClusterServerTrendDAO;
import com.ruijie.rcos.rcdc.rco.module.impl.entity.ClusterServerTrendEntity;
import mockit.Expectations;
import mockit.Injectable;
import mockit.Tested;
import org.junit.Test;
import org.springframework.data.domain.Sort;

import java.util.Date;
import java.util.List;

import static org.junit.Assert.assertEquals;

/**
 * 
 * Description: Function Description
 * Copyright: Copyright (c) 2018
 * Company: Ruijie Co., Ltd.
 * Create Time: 2019年7月29日
 *
 * @author zhuangchenwu
 */
public class ClusterServerStatisticsAPIImplTest {

    @Tested
    private ClusterServerStatisticsAPIImpl apiImpl;
    
    @Injectable
    private ClusterServerTrendDAO clusterServerTrendDAO;
    
    @Injectable
    private CbbPhysicalServerMgmtAPI cbbPhysicalServerMgmtAPI;

    /**
     * 测试clusterServerTrend方法，list集合为空的情况
     */
    @Test
    public void testClusterServerTrendWhileFindListIsEmpty() {
        
        new Expectations() {
            {
                clusterServerTrendDAO.findAll((Sort) any);
                result = Lists.newArrayList();
            }
        };
        List<ClusterServerTrendDTO> clusterServerTrendDTOList = apiImpl.clusterServerTrend();
        assertEquals(0, clusterServerTrendDTOList.size());
    }
    
    /**
     * 测试clusterServerTrend方法，list集合为空的情况
     */
    @Test
    public void testClusterServerTrendWhileFindListNotEmpty() {
        final List<ClusterServerTrendEntity> clusterServerTrendList = Lists.newArrayList();
        final ClusterServerTrendEntity entity = new ClusterServerTrendEntity();
        final String cpuUsedRate = "90";
        entity.setCpuUsedRate(cpuUsedRate);
        entity.setCreateTime(new Date());
        clusterServerTrendList.add(entity);
        new Expectations() {
            {
                clusterServerTrendDAO.findAll((Sort) any);
                result = clusterServerTrendList;
            }
        };
        List<ClusterServerTrendDTO> clusterServerTrendDTOList = apiImpl.clusterServerTrend();
        assertEquals(clusterServerTrendList.size(), clusterServerTrendDTOList.size());
        assertEquals(cpuUsedRate, clusterServerTrendDTOList.get(0).getCpuUsedRate());
    }

}
