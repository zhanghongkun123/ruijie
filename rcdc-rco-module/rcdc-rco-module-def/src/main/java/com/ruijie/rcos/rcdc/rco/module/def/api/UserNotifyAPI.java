package com.ruijie.rcos.rcdc.rco.module.def.api;

import java.util.UUID;

/**
 * Description: 用户通知接口
 * Copyright: Copyright (c) 2018
 * Company: Ruijie Co., Ltd.
 * Create Time: 2022-03-18 16:57:00
 *
 * @author zjy
 */
public interface UserNotifyAPI {


    /**
     * 发送用户密码更新通知到GT
     *
     * @param userId 用户id
     * @Date 2022/3/18 15:50
     * @Author zhengjingyong
     **/
    void notifyUserPwdToGt(UUID userId);

    /**
     * 发送用户密码更新通知到指定桌面gt
     *
     * @param desktopId 桌面id
     * @param userName  用户名
     * @param password  密码
     * @Date 2023/8/24
     * @Author zhengjingyong
     **/
    void notifyUserPwdToGtByDesktopId(UUID desktopId, String userName, String password);
}
