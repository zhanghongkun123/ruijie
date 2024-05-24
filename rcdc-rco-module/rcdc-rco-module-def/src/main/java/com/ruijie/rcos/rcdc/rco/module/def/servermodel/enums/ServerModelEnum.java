package com.ruijie.rcos.rcdc.rco.module.def.servermodel.enums;

import java.util.Arrays;

/**
 * Description: 服务器部署类型
 * Copyright: Copyright (c) 2020
 * Company: Ruijie Co., Ltd.
 * Create Time: 2020/09/04
 *
 * @author wjp
 */
public enum ServerModelEnum {

    INIT_SERVER_MODEL("init", "未初始化"),
    VDI_SERVER_MODEL("vdi", "VDI部署模式"),
    IDV_SERVER_MODEL("rcm", "IDV部署模式"),
    MINI_SERVER_MODEL("mini", "MINI部署模式");

    private String name;
    private String desc;

    ServerModelEnum(String name, String desc) {
        this.name = name;
        this.desc = desc;
    }

    public String getName() {
        return name;
    }

    public String getDesc() {
        return desc;
    }

    /**
     * 根据name获取
     *
     * @param name 参数
     * @return ServerModelEnum
     */
    public static ServerModelEnum getByName(String name) {
        return Arrays.stream(ServerModelEnum.values()).filter(item -> name.equals(item.getName())).findFirst().orElse(null);
    }
}
