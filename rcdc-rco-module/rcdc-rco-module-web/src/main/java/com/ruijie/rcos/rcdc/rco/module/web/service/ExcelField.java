package com.ruijie.rcos.rcdc.rco.module.web.service;

/**
 * Description: Function Description
 * Copyright: Copyright (c) 2021
 * Company: Ruijie Co., Ltd.
 * Create Time: 2021/7/18
 *
 * @author lihengjing
 */
public interface ExcelField {

    /**
     * 获取Excel列名
     * @return 列名
     */
    String getHeader();

    /**
     * 获取Excel列索引
     * @return 列索引
     */
    Integer getIndex();
}
