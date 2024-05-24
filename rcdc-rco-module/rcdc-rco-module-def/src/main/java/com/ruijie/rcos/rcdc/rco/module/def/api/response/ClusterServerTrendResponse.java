package com.ruijie.rcos.rcdc.rco.module.def.api.response;

import java.util.List;
import com.google.common.collect.Lists;
import com.ruijie.rcos.rcdc.rco.module.def.api.dto.ClusterServerTrendDTO;
import com.ruijie.rcos.sk.modulekit.api.comm.DefaultResponse;

/**
 * 集群服务器信息统计应答类
 * Description: Function Description
 * Copyright: Copyright (c) 2018
 * Company: Ruijie Co., Ltd.
 * Create Time: 2019年7月8日
 * 
 * @author zhuangchenwu
 */
public class ClusterServerTrendResponse extends DefaultResponse {

    List<ClusterServerTrendDTO> clusterServerTrendDTOList = Lists.newArrayList();

    public List<ClusterServerTrendDTO> getClusterServerTrendDTOList() {
        return clusterServerTrendDTOList;
    }

    public void setClusterServerTrendDTOList(List<ClusterServerTrendDTO> clusterServerTrendDTOList) {
        this.clusterServerTrendDTOList = clusterServerTrendDTOList;
    }
    
}
