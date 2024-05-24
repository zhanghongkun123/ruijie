package com.ruijie.rcos.rcdc.rco.module.web.service;

/**
 * Description: excel单元格数据列
 * Copyright: Copyright (c) 2018
 * Company: Ruijie Co., Ltd.
 * Create Time: 2018/11/20
 *
 * @author Jarman
 */
public enum ImportVDIDeskExcelField {

    USERNAME("用户名", 0),
    VDI_CLOUD_PLATFORM_NAME("云平台",1),
    VDI_IMAGE_TEMPLATE_NAME("vdi镜像模板", 2),
    VDI_CLUSTER_NAME("计算资源", 3),
    VDI_STRATEGY_NAME("vdi云桌面策略", 4),
    VDI_NETWORK_NAME("vdi网络策略", 5),
    VDI_CPU("CPU核数", 6),
    VDI_MEMORY("内存", 7),
    VDI_SYSTEM_SIZE("系统盘", 8),
    VDI_STORAGE_POOL_NAME("系统盘存储", 9),
    VDI_PERSON_SIZE("本地盘", 10),
    VDI_PERSON_STORAGE_POOL_NAME("本地盘存储", 11),
    VDI_VGPU_MODEL("显存配置", 12);

    private String header;
    private Integer index;

    ImportVDIDeskExcelField(String header, Integer index) {
        this.header = header;
        this.index = index;
    }

    public String getHeader() {
        return header;
    }

    public Integer getIndex() {
        return index;
    }

}
