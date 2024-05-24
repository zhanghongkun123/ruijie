package com.ruijie.rcos.rcdc.rco.module.impl.dao;

import java.util.List;
import java.util.UUID;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;

import com.ruijie.rcos.gss.sdk.iac.module.def.enums.IacUserStateEnum;
import com.ruijie.rcos.gss.sdk.iac.module.def.enums.IacUserTypeEnum;
import com.ruijie.rcos.rcdc.rco.module.impl.entity.RcoViewUserEntity;
import com.ruijie.rcos.sk.modulekit.api.ds.SkyEngineJpaRepository;

/**
 * Description: 用户持久化接口.
 * Copyright: Copyright (c) 2018
 * Company: Ruijie Co., Ltd.
 * Create Time: 2018年10月30日
 *
 * @author chenzj
 */
public interface RcoViewUserDAO extends SkyEngineJpaRepository<RcoViewUserEntity, UUID> {

    /**
     * 根据组id获取用户列表
     *
     * @param groupId groupId
     * @return 用户列表
     */
    List<RcoViewUserEntity> findByGroupId(UUID groupId);

    /**
     * 根据分组列表查询用户
     *
     * @param groupIdList 分组id集合
     * @return 用户列表
     */
    List<RcoViewUserEntity> findByGroupIdIn(List<UUID> groupIdList);

    /**
     * 根据用户名获取用户信息
     *
     * @param userName 用户名
     * @return RcoViewUserEntity
     */
    RcoViewUserEntity findByUserName(String userName);

    /**
     * 根据用户状态查询用户
     *
     * @param state 用户状态
     * @return 用户列表
     */
    List<RcoViewUserEntity> findByState(IacUserStateEnum state);

    /**
     * 根据用户状态与桌面数量查询用户列表
     *
     * @param desktopNum 桌面数量
     * @return 用户名列表
     */
    @Query("select userName from RcoViewUserEntity where desktopNum >= ?1")
    List<String> findByDesktopNumGe(int desktopNum);

    /**
     * 查询第一个用户ID
     *
     * @param pageable 分页参数
     * @return ID
     */
    @Query(value = "select id from RcoViewUserEntity")
    List<UUID> obtainIdList(Pageable pageable);


    /**
     * 根据用户id列表获取用户信息
     *
     * @param uuidList 用户id列表
     * @return RcoViewUserEntity
     */
    List<RcoViewUserEntity> findAllByIdIn(List<UUID> uuidList);

    /**
     * 根据idList获取记录数量
     *
     * @param idList 用户id列表
     * @return 记录数量
     */
    int countByIdIn(List<UUID> idList);

    /**
     * 根据用户名列表获取用户信息列表
     *
     * @param usernameList 用户名列表
     * @return 用户列表
     */
    List<RcoViewUserEntity> findAllByUserNameIn(List<String> usernameList);

    /**
     * 根据用户id列表和用户类型列表查询是否存在该类型内的用户
     *
     * @param idList 用户id列表
     * @param typeList 用户类型列表
     * @return true 存在该类型内的用户，false不存在
     */
    boolean existsByIdInAndUserTypeIn(List<UUID> idList, List<IacUserTypeEnum> typeList);

    /**
     * 根据ID列表查询用户
     * @param idList 桌面数量
     * @return 返回用户RCO用户
     */
    List<RcoViewUserEntity> findByIdIn(List<UUID>idList);

    /**
     * 根据用户类型查询有使用云桌面的用户
     * @param desktopNum  云桌面数
     * @param userTypeList  用户类型集合
     * @return  用户数
     */
    @Query("select count(id) from RcoViewUserEntity where desktopNum >= ?1 and userType in ?2")
    Long findByDesktopNumAndUserType(int desktopNum, List<IacUserTypeEnum> userTypeList);
}
