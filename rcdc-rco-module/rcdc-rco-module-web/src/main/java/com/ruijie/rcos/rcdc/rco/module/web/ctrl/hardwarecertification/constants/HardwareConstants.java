package com.ruijie.rcos.rcdc.rco.module.web.ctrl.hardwarecertification.constants;

/**
 *
 * Description:硬件特征码常量
 * Copyright: Copyright (c) 2021
 * Company: Ruijie Co., Ltd.
 * Create Time: 2020年03月09日
 *
 * @author linke
 */
public interface HardwareConstants {

    /** 开启硬件特征码 */
    Boolean RCDC_HARDWARE_CERTIFICATION_DEFAULT = false;

    /**
     * 最多允许数据行
     */
    int ALLOW_MAX_ROW = 1000;

    /**
     * 可绑定终端数最小值
     */
    int MIN_HARDWARE_NUM = 1;

    /**
     * 可绑定终端数最大值
     */
    int MAX_HARDWARE_NUM = 999;
}
