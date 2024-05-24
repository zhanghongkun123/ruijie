package com.ruijie.rcos.rcdc.rco.module.def.api;

import com.ruijie.rcos.rcdc.rco.module.def.api.dto.ClusterServerTrendDTO;

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
public interface ClusterServerStatisticsAPI {

    /**
     * 集群服务器信息趋势
     *
     * @return 返回统计结果
     */

    List<ClusterServerTrendDTO> clusterServerTrend();
}
