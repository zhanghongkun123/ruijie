package com.ruijie.rcos.rcdc.rco.module.impl.api;

import com.google.common.collect.Lists;
import com.ruijie.rcos.rcdc.rco.module.def.api.ClusterServerStatisticsAPI;
import com.ruijie.rcos.rcdc.rco.module.def.api.dto.ClusterServerTrendDTO;
import com.ruijie.rcos.rcdc.rco.module.impl.Constants;
import com.ruijie.rcos.rcdc.rco.module.impl.dao.ClusterServerTrendDAO;
import com.ruijie.rcos.rcdc.rco.module.impl.entity.ClusterServerTrendEntity;
import com.ruijie.rcos.sk.base.log.Logger;
import com.ruijie.rcos.sk.base.log.LoggerFactory;
import com.ruijie.rcos.sk.modulekit.api.bootstrap.SafetySingletonInitializer;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Order;
import org.springframework.util.CollectionUtils;

import java.util.Date;
import java.util.List;

/**
 * 
 * Description: Function Description
 * Copyright: Copyright (c) 2018
 * Company: Ruijie Co., Ltd.
 * Create Time: 2019年7月29日
 *
 * @author zhuangchenwu
 */
public class ClusterServerStatisticsAPIImpl implements ClusterServerStatisticsAPI, SafetySingletonInitializer {

    private static final Logger LOGGER = LoggerFactory.getLogger(ClusterServerStatisticsAPIImpl.class);

    @Autowired
    private ClusterServerTrendDAO clusterServerTrendDAO;

    /** 集群服务器趋势信息最大记录数 */
    private static final long CLUSTER_SERVER_TREND_NUMBER_MAX = 7 * 24L;

    @Override
    public List<ClusterServerTrendDTO> clusterServerTrend() {

        List<ClusterServerTrendEntity> clusterServerTrendList = clusterServerTrendDAO.findAll(Sort.by(Order.asc(Constants.ORDER_BY_CREATE_TIME)));
        if (CollectionUtils.isEmpty(clusterServerTrendList)) {
            LOGGER.info("集群服务器资源趋势信息统计数据查询结果为空");
            return Lists.newArrayList();
        }
        return obtainClusterTrendData(clusterServerTrendList);
    }

    private List<ClusterServerTrendDTO> obtainClusterTrendData(List<ClusterServerTrendEntity> clusterServerTrendList) {
        List<ClusterServerTrendDTO> clusterServerTrendDTOList = Lists.newArrayList();
        for (ClusterServerTrendEntity entity : clusterServerTrendList) {
            ClusterServerTrendDTO trendDTO = new ClusterServerTrendDTO();
            BeanUtils.copyProperties(entity, trendDTO);
            clusterServerTrendDTOList.add(trendDTO);
        }
        return clusterServerTrendDTOList;
    }

    @Override
    public void safeInit() {
        long currentTime = System.currentTimeMillis() / Constants.ONE_HOUR_MILLIS;
        // 可保存的最小时间应为当前时间的7天前小时值，因此需要加1
        long appointTime = currentTime - CLUSTER_SERVER_TREND_NUMBER_MAX + 1;
        // 删除趋势表中过期的记录
        clusterServerTrendDAO.deleteByCreateTimeLessThan(new Date(appointTime * Constants.ONE_HOUR_MILLIS));
        List<ClusterServerTrendEntity> clusterServerTrendList = clusterServerTrendDAO.findAll(Sort.by(Order.asc(Constants.ORDER_BY_CREATE_TIME)));
        fillClusterTrendDataWithBlankDate(clusterServerTrendList, currentTime);
    }

    private void fillClusterTrendDataWithBlankDate(List<ClusterServerTrendEntity> clusterServerTrendList, long currentTime) {
        long lastestCreateTime;
        long intervalHoursWithCurrent;
        if (CollectionUtils.isEmpty(clusterServerTrendList)) {
            lastestCreateTime = (currentTime - CLUSTER_SERVER_TREND_NUMBER_MAX) * Constants.ONE_HOUR_MILLIS;
            intervalHoursWithCurrent = CLUSTER_SERVER_TREND_NUMBER_MAX;
        } else {
            lastestCreateTime = clusterServerTrendList.get(clusterServerTrendList.size() - 1).getCreateTime().getTime();
            intervalHoursWithCurrent = currentTime - lastestCreateTime / Constants.ONE_HOUR_MILLIS;
        }
        // 补充缺失时间段的记录
        for (long i = 1; i <= intervalHoursWithCurrent; i++) {
            ClusterServerTrendDTO trendDTO = new ClusterServerTrendDTO();
            Date createTime = new Date(lastestCreateTime + Constants.ONE_HOUR_MILLIS * i);
            trendDTO.setCreateTime(createTime);
            // 保存空数值的记录入库
            saveToClusterServerTrend(createTime);
        }
    }

    private void saveToClusterServerTrend(Date createTime) {
        ClusterServerTrendEntity entity = new ClusterServerTrendEntity();
        entity.setCreateTime(createTime);
        clusterServerTrendDAO.save(entity);
    }

}
