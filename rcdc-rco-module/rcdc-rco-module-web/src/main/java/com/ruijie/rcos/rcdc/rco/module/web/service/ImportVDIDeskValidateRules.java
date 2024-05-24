package com.ruijie.rcos.rcdc.rco.module.web.service;

import com.ruijie.rcos.sk.base.annotation.TextShort;

/**
 * Description: 导入VDI云桌面校验规则
 * Copyright: Copyright (c) 2019
 * Company: Ruijie Co., Ltd.
 * Create Time: 2018/4/24
 *
 * @author linrenjian
 */
public class ImportVDIDeskValidateRules {

    private ImportVDIDeskValidateRules() {
        throw new IllegalStateException("ImportVDIDeskValidateRules Utility class");
    }

    /**
     * 镜像多版本后，长度要求增加到64位
     */
    public static final int IMAGE_TEMPLATE_NAME_SIZE = 64;

    public static final int STRATEGY_NAME_SIZE = TextShort.TEXT_SIZE;

    public static final int NETWORK_NAME_SIZE = TextShort.TEXT_SIZE;

    /**
     * 最多允许数据行
     */
    public static final int ALLOW_MAX_ROW = 1000;


}
