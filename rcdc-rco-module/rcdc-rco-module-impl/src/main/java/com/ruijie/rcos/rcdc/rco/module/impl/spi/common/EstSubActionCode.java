package com.ruijie.rcos.rcdc.rco.module.impl.spi.common;

/**
 * EstCommonAction Est透传RCDC消息子请求动作常量
 * Description: Function Description
 * Copyright: Copyright (c) 2021
 * Company: Ruijie Co., Ltd.
 * Create Time: 2021年3月31日
 *
 * @author lihengjing
 */
public interface EstSubActionCode {

    /**
     * est获取快照列表
     */
    String EST_SNAPSHOT_LIST = "SNAPSHOT_LIST";

    /**
     * est创建快照
     */
    String EST_SNAPSHOT_CREATE = "SNAPSHOT_CREATE";

    /**
     * est删除快照
     */
    String EST_SNAPSHOT_DELETE = "SNAPSHOT_DELETE";

    /**
     * est恢复快照
     */
    String EST_SNAPSHOT_RECOVER = "SNAPSHOT_RECOVER";

    /**
     * est任务状态查询
     */
    String EST_MSG_DETAIL = "MSG_DETAIL";

    String EST_SNAPSHOT_OPERATION_CONFIRM = "SNAPSHOT_OPERATION_CONFIRM";

    String EST_SNAPSHOT_CHECK_NUM = "SNAPSHOT_CHECK_NUM";

    String EST_SNAPSHOT_REFRESH_LIST = "SNAPSHOT_REFRESH_LIST";
}
