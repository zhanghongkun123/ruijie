package com.ruijie.rcos.rcdc.rco.module.impl.util;

import com.ruijie.rcos.rcdc.clouddesktop.module.def.dto.desk.CbbMonitorDataDTO;
import com.ruijie.rcos.rcdc.rco.module.def.api.dto.bigscreen.NormalDistributionDTO;
import java.util.List;
import org.springframework.util.Assert;
import org.springframework.util.CollectionUtils;

/**
 * 数学计算相关工具类
 * Description: Function Description
 * Copyright: Copyright (c) 2018
 * Company: Ruijie Co., Ltd.
 * Create Time: 2019年7月23日
 *
 * @author brq
 */
public class MathUtil {

    private MathUtil() {
        throw new IllegalStateException("MathUtil Utility class");
    }

    private static final Double TWO_SIGMA_INTERVAL = 1.96;

    private static final Double THREE_SIGMA_INTERVAL = 2.58;

    /**
     * 计算正太分布区间值
     *
     * @param averageValue 中值
     * @param standardDeviation 标差
     * @return NormalDistributionDTO 正太分布区间值
     */
    public static NormalDistributionDTO normalDistribution(Double averageValue, Double standardDeviation) {
        Assert.notNull(averageValue, "average value cannot be null!");
        Assert.notNull(standardDeviation, "standard deviation cannot be null!");

        NormalDistributionDTO normalDistributionDTO = new NormalDistributionDTO();
        normalDistributionDTO.setAverageValue(averageValue);
        normalDistributionDTO.setNormalNumLeftLine(averageValue - standardDeviation);
        normalDistributionDTO.setNormalNumRightLine(averageValue + standardDeviation);
        normalDistributionDTO.setLowLoadLine(averageValue - TWO_SIGMA_INTERVAL * standardDeviation);
        normalDistributionDTO.setHighLoadLine(averageValue + TWO_SIGMA_INTERVAL * standardDeviation);
        normalDistributionDTO.setUltraLowLoadLine(averageValue - THREE_SIGMA_INTERVAL * standardDeviation);
        normalDistributionDTO.setUltraHighLoadLine(averageValue + THREE_SIGMA_INTERVAL * standardDeviation);
        return normalDistributionDTO;
    }

    /**
     * 获取资源使用率
     * @param list 参数列表
     * @return Double 资源使用率
     */
    public static Double getUsage(List<CbbMonitorDataDTO<Double>> list) {
        Assert.notNull(list, "list cannot be null!");

        if (!CollectionUtils.isEmpty(list)) {
            return list.get(list.size() - 1).getValue();
        }
        return 0D;
    }

}
