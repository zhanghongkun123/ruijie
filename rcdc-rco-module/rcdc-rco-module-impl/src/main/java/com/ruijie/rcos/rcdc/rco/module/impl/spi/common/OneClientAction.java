package com.ruijie.rcos.rcdc.rco.module.impl.spi.common;

/**
 * Description: Function Description
 * Copyright: Copyright (c) 2018
 * Company: Ruijie Co., Ltd.
 * Create Time: 2019/1/16
 *
 * @author chenli
 */
public interface OneClientAction {

    /**
     * est获取快照列表
     */
    String EST_SNAPSHOT_LIST = "est_snapshot_list";

    /**
     * est创建快照
     */
    String EST_SNAPSHOT_CREATE = "est_snapshot_create";

    /**
     * est删除快照
     */
    String EST_SNAPSHOT_DELETE = "est_snapshot_delete";

    /**
     * est恢复快照
     */
    String EST_SNAPSHOT_RECOVER = "est_snapshot_recover";

    /**
     * est任务状态查询
     */
    String EST_MSG_DETAIL = "est_msg_detail";


    /**
     * 确认快照数量
     */
    String EST_SNAPSHOT_CHECK_NUM = "est_snapshot_check_num";

    /**
     * 通知est列表刷新事件
     */
    String EST_SNAPSHOT_REFRESH_LIST = "est_snapshot_refresh_list";

    /**
     * 提交用户自助快照
     */
    String EST_SNAPSHOT_OPERATION_CONFIRM = "est_snapshot_operation_confirm";


    /**
     * 通知est快照信息通知
     */
    String EST_SNAPSHOT_NOTIFY = "est_snapshot_notify";



}
