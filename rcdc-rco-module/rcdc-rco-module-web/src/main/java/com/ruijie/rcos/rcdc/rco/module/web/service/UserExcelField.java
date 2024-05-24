package com.ruijie.rcos.rcdc.rco.module.web.service;

/**
 * Description: excel单元格数据列
 * Copyright: Copyright (c) 2018
 * Company: Ruijie Co., Ltd.
 * Create Time: 2018/11/20
 *
 * @author Jarman
 */
public enum UserExcelField {

    GROUP_NAME("用户组名", 0),
    NAME("用户名", 1),
    REAL_NAME("姓名", 2),
    PHONE("手机号", 3),
    EMAIL("邮箱地址", 4),
    USER_STATE("用户状态", 5),
    HARDWARECERTIFICATION("硬件特征码是否开启", 6),
    MAX_HARDWARE_NUM("可绑定终端数", 7),
    OPTCERTIFICATION("动态口令是否开启",8),
    PASSWORD("密码",9),
    SMS_CERTIFICATION("短信认证是否开启",10),
    ACCOUNTEXPIREDATE("过期时间", 11),
    INVALIDETIME("失效天数", 12),
    DESCRIPTION("描述", 13),
    RADIUSCERTIFICATION("Radius认证服务是否开启", 14),
    ACCOUNTPASSWORDCERTIFICATION("本地密码认证是否开启", 15),
    WORKWEIXINCERTIFICATION("企业微信认证是否开启", 16),
    FEISHUCERTIFICATION("飞书认证是否开启", 17),
    DINGDINGCERTIFICATION("钉钉认证是否开启", 18),
    OAUTH2CERTIFICATION("AUTH2.0认证是否开启", 19),
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
    VDI_VGPU_MODEL("显存配置", 12),
    IDV_IMAGE_TEMPLATE_NAME("idv镜像模板", 13),
    IDV_STRATEGY_NAME("idv云桌面策略", 14),
    VOI_IMAGE_TEMPLATE_NAME("voi镜像模板", 15),
    VOI_STRATEGY_NAME("voi云桌面策略", 16),

    ACCOUNT_EXPIRE_DATE("过期时间", 17),

    INVALID_ETIME("失效天数", 18),

    IDV_IMAGE_TEMPLATE_NAME_ONLY_FOR_IDV("idv镜像模板", 1),
    IDV_STRATEGY_NAME_ONLY_FOR_IDV("idv云桌面策略",2),
    VOI_IMAGE_TEMPLATE_NAME_ONLY_FOR_IDV("voi镜像模板", 3),
    VOI_STRATEGY_NAME_ONLY_FOR_IDV("voi云桌面策略", 4),

    ACCOUNT_EXPIRE_DATE_ONLY_FOR_IDV("过期时间", 5),

    INVALID_ETIME_ONLY_FOR_IDV("失效天数", 6);

    private String header;
    private Integer index;

    UserExcelField(String header, Integer index) {
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
