package com.ruijie.rcos.rcdc.rco.module.impl.service;

import com.ruijie.rcos.rcdc.rco.module.def.desktoppool.request.UserDesktopBindUserRequest;
import com.ruijie.rcos.rcdc.rco.module.impl.entity.UserDesktopEntity;

import java.util.Date;
import java.util.List;
import java.util.UUID;

/**
 * Description:
 * Copyright: Copyright (c) 2019
 * Company: Ruijie Co., Ltd.
 * Create Time: 2021/3/24 22:53
 *
 * @author ketb
 */
public interface UserDesktopService {

    /**
     * 查询当前正在终端上运行的桌面
     * @param terminalId 终端id
     * @return 桌面信息
     */
    UserDesktopEntity findRunningInTerminalDesktop(String terminalId);

    /**
     * 获取用户云桌面
     * @param deskId 桌面id
     * @return 实体
     */
    UserDesktopEntity findByDeskId(UUID deskId);


    /**
     * 解绑用户和池桌面的关系，并重置报障状态
     *
     * @param deskId 桌面id
     */
    void unbindUserAndDesktopRelation(UUID deskId);

    /**
     * 解绑用户和池桌面的关系，并重置报障状态,多会话桌面可以指定用户ID
     *
     * @param deskId 桌面id
     * @param userId 用户id
     */
    void unbindUserAndDesktopRelation(UUID deskId, UUID userId);

    /**
     * 清空connectClosedTime字段
     *
     * @param deskId 桌面id
     */
    void clearConnectClosedTime(UUID deskId);

    /**
     * 标记断连时间
     *
     * @param deskId            桌面id
     * @param connectClosedTime 断连时间
     */
    void setConnectClosedTime(UUID deskId, Date connectClosedTime);

    /**
     * 查询终端上运行的桌面列表
     * @param terminalId 终端id
     * @return 列表
     */
    List<UserDesktopEntity> findTerminalDesktopList(String terminalId);

    /**
     * 根据桌面id列表查询用户id列表
     *
     * @param desktopIdList 桌面id列表
     * @return 用户id列表
     */
    List<UUID> findUserIdByDesktopIdList(List<UUID> desktopIdList);

    /**
     * 通过用户id查询
     * @param userId 用户id
     * @return 实体
     */
    List<UserDesktopEntity> findByUserId(UUID userId);

    /**
     * 为桌面绑定用户
     *
     * @param request 参数
     */
    void desktopBindUser(UserDesktopBindUserRequest request);
}
