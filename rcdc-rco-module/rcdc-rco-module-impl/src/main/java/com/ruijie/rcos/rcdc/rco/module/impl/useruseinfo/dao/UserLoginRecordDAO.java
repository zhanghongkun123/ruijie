package com.ruijie.rcos.rcdc.rco.module.impl.useruseinfo.dao;

import com.ruijie.rcos.rcdc.rco.module.impl.useruseinfo.entity.UserLoginRecordEntity;
import com.ruijie.rcos.sk.modulekit.api.ds.SkyEngineJpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;
import java.util.UUID;

/**
 * @author zjy
 * @version 1.0.0
 * @Description 用户登录记录dao
 * @createTime 2021-10-28 15:00:00
 */
public interface UserLoginRecordDAO extends SkyEngineJpaRepository<UserLoginRecordEntity, UUID> {

    /**
     * @title 根据云桌面id查询最后一条操作记录
     * @description
     * @author zjy
     * @param deskId 桌面id
     * @updateTime 2021/11/1 11:04
     * @return 登录记录
     */
    UserLoginRecordEntity findFirstByDeskIdOrderByCreateTimeDesc(String deskId);

    /**
     * @title 根据终端id查询最后一条操作记录
     * @description
     * @author zjy
     * @param terminalId 桌面id
     * @updateTime 2021/11/1 11:04
     * @return 登录记录
     */
    UserLoginRecordEntity findFirstByTerminalIdOrderByCreateTimeDesc(String terminalId);

    /**
     * @tittle 根据终端id和桌面id查询最后一条记录
     *
     * @param terminalId 终端id
     * @param deskId 桌面id
     * @return 登录记录
     */
    UserLoginRecordEntity findFirstByTerminalIdAndDeskIdOrderByCreateTimeDesc(String terminalId, String deskId);

    /**
     * @tittle 查询指定终端id和会话状态的记录列表
     *
     * @param terminalId 终端id
     * @param sessionState 会话状态
     * @return 登录记录
     */
    List<UserLoginRecordEntity> findAllByTerminalIdAndSessionState(String terminalId, String sessionState);

    /**
     * @tittle 查询指定桌面id、会话id和会话状态的记录列表
     *
     * @param deskId 桌面id
     * @param sessionState 会话状态
     * @return 登录记录
     */
    List<UserLoginRecordEntity> findAllByDeskIdAndSessionState(String deskId, String sessionState);

    /**
     * @tittle 查询指定桌面id、会话id和会话状态的记录列表
     *
     * @param deskId 桌面id
     * @param connectionId 会话id
     * @param sessionState 会话状态
     * @return 登录记录
     */
    UserLoginRecordEntity findFirstByDeskIdAndConnectionIdAndSessionStateOrderByCreateTimeDesc(String deskId, Long connectionId, String sessionState);

    /**
     * @tittle 根据桌面id和会话状态查询最后一条记录
     *
     * @param deskId 桌面id
     * @param sessionState 会话状态
     * @return 登录记录
     */
    UserLoginRecordEntity findFirstByDeskIdAndSessionStateOrderByCreateTimeDesc(String deskId, String sessionState);

    /**
     * @tittle 根据桌面id和会话状态查询
     *
     * @param terminalId 终端id
     * @param deskId 桌面id
     * @param sessionState 会话状态
     * @return 登录记录
     */
    List<UserLoginRecordEntity> findAllByTerminalIdAndDeskIdAndSessionState(String terminalId, String deskId, String sessionState);

    /**
     * @tittle 根据会话状态查询
     *
     * @param sessionState 会话状态
     * @return 登录记录
     */
    List<UserLoginRecordEntity> findAllBySessionState(String sessionState);

    /**
     * 根据创建时间查找最新的记录
     * @param createTime createTime
     * @return 记录
     */
    @Query(value = "select * from t_cbb_user_login_record where create_time <= ?1 order by create_time desc limit 1", nativeQuery = true)
    UserLoginRecordEntity findLastByCreateTime(Date createTime);

    /**
     ** 删除超期的用户报表记录
     * @param overdueTime 日志时间
     */
    @Transactional
    @Modifying
    void deleteByCreateTimeLessThan(Date overdueTime);
}
