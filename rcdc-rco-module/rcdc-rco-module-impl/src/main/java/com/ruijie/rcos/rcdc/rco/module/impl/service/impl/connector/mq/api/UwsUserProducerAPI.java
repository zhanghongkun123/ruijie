package com.ruijie.rcos.rcdc.rco.module.impl.service.impl.connector.mq.api;

import com.ruijie.rcos.sk.base.annotation.NotEmpty;
import com.ruijie.rcos.sk.base.annotation.NotNull;
import com.ruijie.rcos.sk.connectkit.api.annotation.ApiAction;
import com.ruijie.rcos.sk.connectkit.api.annotation.ApiGroup;
import com.ruijie.rcos.sk.connectkit.api.annotation.mq.MQ;

import java.util.List;
import java.util.UUID;

/**
 * Description: UWS 用户相关MQ
 * Copyright: Copyright (c) 2018
 * Company: Ruijie Co., Ltd.
 * Create Time: 2021-11-17 20:20:00
 *
 * @author zjy
 */
@MQ
@ApiGroup("uwsUserMq")
public interface UwsUserProducerAPI {

    /**
     * 删除用户
     *
     * @param userIds 用户id列表
     * @Author: zjy
     * @Date: 2021/11/17 20:23
     **/
    @ApiAction("deleted")
    void notifyUserDeleted(@NotEmpty List<UUID> userIds);


    /**
     * 修改密码
     *
     * @param userIds 用户id列表
     * @Author: zjy
     * @Date: 2021/11/17 20:23
     **/
    @ApiAction("updatePwd")
    void notifyUserUpdatePwd(@NotEmpty List<UUID> userIds);


    /**
     * 禁用用户
     *
     * @param userIds 用户id列表
     * @Author: zjy
     * @Date: 2021/11/17 20:23
     **/
    @ApiAction("disabled")
    void notifyUserDisabled(@NotEmpty List<UUID> userIds);

    /**
     * 修改密码开关变更
     *
     * @param allowChangePwd 是否允许更改密码
     * @Date 2021/12/2 19:11
     * @Author zjy
     **/
    @ApiAction("modifyPwdConfigChanged")
    void notifyModifyPwdConfigChanged(@NotNull Boolean allowChangePwd);
}
