package com.ruijie.rcos.rcdc.rco.module.impl;

/**
 * Description: rcdc rest url path
 * Copyright: Copyright (c) 2021
 * Company: Ruijie Co., Ltd.
 * Create Time: 2021/9/7
 *
 * @author zqj
 */
public interface RccmRestKey {

    /** 心跳 */
    String HEART_BEAT = "/system/heartBeat";

    /** 推送用户信息到rccm */
    String PUSH_USER = "/rcdc/pushUser";

    /** 通知rccm删除用户集群关系 */
    String NOTICE_RCCM_DEL_USER_CLUSTER = "/rcdc/noticeRccmDelUserCluster";

    /**
     * rccm提供的统一个登录openAPI地址
     */
    String USER_UNIFIED_LOGIN_PATH = "/unifiedLogin/collectLoginResult";

    /**
     * rccm提供的获取用户VDI云桌面openAPI地址
     */
    String USER_QUERY_USER_VDI_PATH = "/unifiedLogin/collectUserImages";

    /**
     * rccm提供的转发rcdc请求接口
     */
    String FORWARD_RCDC_REQUEST_PATH = "/rcdc/forwardRcdcRequest";

    /**
     * rccm提供的转发广播Webclient通知接口
     */
    String BROADCAST_WEBCLIENT_NOTIFY_PATH = "/rcdc/broadcastWebclientNotify";

    /** 推送AD域安全组信息到rccm */
    String PUSH_AD_GROUP = "/rcdc/pushAdGroup";
    
    /** 统一管理操作请求 */
    String PUSH_UNIFIED_MANAGE_DATA = "/unifiedManage/masterRequest";

    /**
     * 检查是否可以退出纳管
     */
    String CHECK_EXIT_MANAGE = "/unifiedManage/checkExitManage";


    /**
     * rccm提供的统一修改密码openAPI地址
     */
    String USER_UNIFIED_CHANGE_PWD_PATH = "/unifiedLogin/collectChangePwd";
    /** 同步用户密码 */
    String SYNC_USER_PASSWORD = "/unifiedLogin/syncUserPassword";

    /** 同步用户辅助认证信息 */
    String SYNC_USER_IDENTITY_CONFIG = "/unifiedLogin/syncUserIdentityConfig";

}
