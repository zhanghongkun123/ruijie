package com.ruijie.rcos.rcdc.rco.module.def.api;

/**
 * Description: 自动扩容API
 * Copyright: Copyright (c) 2022
 * Company: Ruijie Co., Ltd.
 * Create Time: 2022/6/27 9:55
 *
 * @author yxq
 */
public interface EnableFullSystemDiskAPI {

    /**
     * 获取自动扩容全局配置
     *
     * @return 是否开启
     */
    Boolean getEnableFullSystemDisk();

    /**
     * 编辑自动扩容全局配置
     *
     * @param enableFullSystemDisk 是否开启
     */
    void editEnableFullSystemDisk(Boolean enableFullSystemDisk);
}
