package com.ruijie.rcos.rcdc.rco.module.web.service.userprofile;

/**
 * Description: 路径列定义接口
 * Copyright: Copyright (c) 2022
 * Company: Ruijie Co., Ltd.
 * Create Time: 2022/4/26
 *
 * @author WuShengQiang
 */
public interface TextExcelField {

    /**
     * 获取Excel列名
     *
     * @return 列名
     */
    String getHeader();

    /**
     * 获取Excel列索引
     *
     * @return 列索引
     */
    Integer getIndex();
}