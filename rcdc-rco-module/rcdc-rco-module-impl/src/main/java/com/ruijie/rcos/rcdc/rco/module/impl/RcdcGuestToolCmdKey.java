package com.ruijie.rcos.rcdc.rco.module.impl;

/**
 * 
 * Description: rcdc与guesttool接口功能定义
 * Copyright: Copyright (c) 2018
 * Company: Ruijie Co., Ltd.
 * Create Time: 2019年1月5日
 * 
 * @author nt
 */
public interface RcdcGuestToolCmdKey {

    /** 消息推送  */
    int RCDC_GT_CMD_ID_SMS = 106;
    int RCDC_GT_PORT_ID_SMS_SEND = 136;

    /**GT请求加域信息*/
    int RCDC_GT_CMD_ID_AD = 140;
    int RCDC_GT_PORT_ID_AD = 135;
    
    /**通知guesttool ad域用户权限变更*/
    int RCDC_GT_CMD_ID_AD_USER = 143;
    int RCDC_GT_PORT_ID_AD_USER = 135;

    /* 通知RATool */
    int RCDC_RA_TOOL_CMD_ID_REMOTE_ASSIST_STATUS = 5004;
    int RCDC_RA_TOOL_PORT_ID_REMOTE_ASSIST_STATUS = 501;

    /* 文件分发任务 */
    int RCDC_GT_CMD_ID_FILE_DISTRIBUTE_TASK = 7000;
    int RCDC_GT_CMD_ID_FILE_DISTRIBUTE_TASK_CANCEL = 7002;
    int RCDC_GT_PORT_ID_FILE_DISTRIBUTE_TASK = 700;
    String RCDC_GT_FILE_DISTRIBUTE_TASK_REPORT = "7001";



}
