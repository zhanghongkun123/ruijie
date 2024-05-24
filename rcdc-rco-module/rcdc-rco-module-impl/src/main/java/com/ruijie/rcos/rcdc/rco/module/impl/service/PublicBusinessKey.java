package com.ruijie.rcos.rcdc.rco.module.impl.service;

/**
 * Description: 抽取部分提示语，上层需要根据提示语进行加工。例如分级分权等不同提示。
 * Copyright: Copyright (c) 2020
 * Company: Ruijie Co., Ltd.
 * Create Time: 2020年03月25日
 *
 * @author xiejian
 */
public interface PublicBusinessKey {

    String RCDC_USER_USERGROUP_NUM_OVER = "23200537";

    String RCDC_USER_SUB_USERGROUP_NUM_OVER = "23200540";

    String RCDC_USER_USERGROUP_HAS_DUPLICATION_NAME = "23200541";

    String RCDC_USER_DELETE_USER_GROUP_SUB_GROUP_NAME_DUPLICATION_WITH_MOVE_SUB_GROUP =
        "23200538";

    String DEFAULT_EMPTY_USERNAME = "--";

    String DEFAULT_PUBLIC_IDV_DESK_NAME = "公用桌面";
}
