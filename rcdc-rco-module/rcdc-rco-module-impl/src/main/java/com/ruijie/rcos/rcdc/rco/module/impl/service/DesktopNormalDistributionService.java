package com.ruijie.rcos.rcdc.rco.module.impl.service;

import com.ruijie.rcos.rcdc.rco.module.def.api.dto.bigscreen.DesktopNormalDistributionDTO;
import com.ruijie.rcos.rcdc.rco.module.def.api.enums.ResourceTypeEnum;
import java.util.Map;

/**
 * Description: 云桌面资源使用率正态分布建模工具
 * Copyright: Copyright (c) 2019
 * Company: Ruijie Co., Ltd.
 * Create Time: 2019/8/10 12:17
 *
 * @author brq
 */
public interface DesktopNormalDistributionService {

    /**
     * 计算云桌面正太分布建模数据
     * 1、获取全部云桌面最近7天资源使用率数据
     * 2、获取全部云桌面最近一个月的平均资源使用率数据
     * 3、计算各个云桌面最近7天和最近1个月，共8组数据的平均值
     * 4、根据每个云桌面的平均值进行正态分布建模数据计算
     * 5、获取全部云桌面最近1天的资源使用率数据
     * 6、并判断每个云桌面数据在建模数据中的分布情况，做数量统计
     *
     * @return Map<ResourceTypeEnum, DesktopNormalDistributionDTO> 返回数据
     */
    Map<ResourceTypeEnum, DesktopNormalDistributionDTO> normalDistribution();

}
