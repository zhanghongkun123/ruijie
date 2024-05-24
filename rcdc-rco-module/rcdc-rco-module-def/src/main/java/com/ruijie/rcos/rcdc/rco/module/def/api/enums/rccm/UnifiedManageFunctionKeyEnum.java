package com.ruijie.rcos.rcdc.rco.module.def.api.enums.rccm;

import com.google.common.collect.Lists;

import java.util.List;

/**
 * Description: 统一管理-同步策略支持的功能key
 * Copyright: Copyright (c) 2022
 * Company: Ruijie Co., Ltd.
 * Create Time: 2023/4/12
 *
 * @author WuShengQiang
 */
public enum UnifiedManageFunctionKeyEnum {

    /**
     * 云桌面策略
     */
    DESK_STRATEGY,

    /**
     * 镜像模板
     */
    IMAGE_TEMPLATE,

    /**
     * 镜像快照
     */
    IMAGE_SNAPSHOT;

    /**
     * 接收同步策略时,需要删除多余数据的类型列表
     *
     * @return 功能类型列表
     */
    public static List<UnifiedManageFunctionKeyEnum> getNeedDeleteUnwantedDataKey() {
        return Lists.newArrayList(DESK_STRATEGY, IMAGE_TEMPLATE);
    }
}
