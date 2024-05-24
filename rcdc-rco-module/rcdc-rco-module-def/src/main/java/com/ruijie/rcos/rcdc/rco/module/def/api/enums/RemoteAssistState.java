package com.ruijie.rcos.rcdc.rco.module.def.api.enums;

/**
 * Description: 远程协助状态
 * Copyright: Copyright (c) 2018
 * Company: Ruijie Co., Ltd.
 * Create Time: 2019/1/23
 * 
 * @author artom
 */
public enum RemoteAssistState {
    /** 管理员等待用户选择状态 */
    WAITING,
    /** 用户同意协助 */
    AGREE,
    /** 用户拒绝协助 */
    REJECT,
    /** 启动远程协助失败 */
    START_FAIL,
    /** 用户主动取消协助 */
    STOP_USER,
    /** 管理员主动取消协助 */
    STOP_ADMIN,
    /** 超时后停止协助状态 */
    STOP_TIMEOUT,
    /** token失效 */
    TOKEN_INVALID,
    /** 远程协助数据非法 */
    DATA_ILLEGAL,
    /** 远程协助中 */
    IN_REMOTE_ASSIST,
    /** 消息响应超时 */
    RESPONSE_TIMEOUT,
    /** 远程协助已结束 */
    FINISH,
    /** 锁屏导致远程协助失败 */
    LOCK_SCREEN,
    /** vnc连接超时 */
    VNC_TIMEOUT,
    /** 程序出现异常 */
    ERROR
}
