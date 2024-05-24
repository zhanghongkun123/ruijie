package com.ruijie.rcos.rcdc.rco.module.impl.dao;

import com.ruijie.rcos.rcdc.clouddesktop.module.def.enums.ComputerWorkModelEnum;
import com.ruijie.rcos.rcdc.rco.module.def.api.enums.ComputerStateEnum;
import com.ruijie.rcos.rcdc.rco.module.def.api.enums.ComputerTypeEnum;
import com.ruijie.rcos.rcdc.rco.module.impl.entity.ComputerEntity;
import com.ruijie.rcos.sk.modulekit.api.ds.SkyEngineJpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

/**
 * Description:
 * Copyright: Copyright (c) 2019
 * Company: Ruijie Co., Ltd.
 * Create Time: 2019/12/30 15:57
 *
 * @author ketb
 */
public interface ComputerBusinessDAO extends SkyEngineJpaRepository<ComputerEntity, UUID> {

    /**
     * 根据mac查询pc
     *
     * @param mac pcmac
     * @return 结果
     */
    ComputerEntity findComputerEntityByMac(String mac);

    /**
     * 根据id查询pc
     *
     * @param computerId pcid
     * @return 结果
     */
    ComputerEntity findComputerEntityById(UUID computerId);

    /**
     * 根据terminalGroupId查询pc列表
     *
     * @param terminalGroupId 分组id
     * @return 结果
     */
    List<ComputerEntity> findByTerminalGroupId(UUID terminalGroupId);

    /**
     * 根据terminalGroupId列表查询pc列表
     *
     * @param terminalGroupIdList 分组id列表
     * @return 结果
     */
    @Query(nativeQuery = true, value = "SELECT * FROM t_rco_computer WHERE group_id in ?1")
    List<ComputerEntity> findByTerminalGroupIdList(List<UUID> terminalGroupIdList);

    /**
     * 根据状态查询pc列表
     *
     * @param state 状态
     * @return 结果
     */
    List<ComputerEntity> findByState(ComputerStateEnum state);

    /**
     * 查询终端信息
     * @param ip ip
     * @return 实体
     */
    ComputerEntity findByIp(String ip);

    /**
     * 查询pc列表
     *
     * @param state 状态
     * @param networkNumber 网段
     * @return 结果
     */
    List<ComputerEntity> findByStateAndNetworkNumber(ComputerStateEnum state, String networkNumber);

    /**
     * 查询pc信息
     * @param terminalId 终端id
     * @return ComputerEntity
     */
    ComputerEntity findByTerminalId(UUID terminalId);

    /**
     * 根据pc终端id获取列表
     * @param computerIdList computerIdList
     * @return 列表
     */
    List<ComputerEntity> findByIdIn(List<UUID> computerIdList);


    /**
     * 统计数量
     * @param computerIdList computerIdList
     * @return int
     */
    int countByIdIn(List<UUID> computerIdList);

    /**
     * 查询pc信息
     * @param terminalId 终端id
     * @param ip ip
     * @return ComputerEntity
     */
    ComputerEntity findByTerminalIdOrIp(String terminalId, String ip);

    /**
     * 更新状态
     * @param computerId 终端id
     * @param computerStateEnum 状态
     */
    @Modifying
    @Transactional
    @Query("update ComputerEntity set state=?2,version=version+1 where version=version and id in ?1")
    void updateState(UUID computerId, ComputerStateEnum computerStateEnum);

    /**
     * 更新工作模式
     * @param computerId computerId
     * @param computerWorkModel computerWorkModel
     */
    @Modifying
    @Transactional
    @Query("update ComputerEntity set workModel=?2,version=version+1 where version=version and id in ?1")
    void updateWorkModel(UUID computerId, ComputerWorkModelEnum computerWorkModel);

    /**
     * 获取列表
     * @param computerState computerState
     * @param computerType computerType
     * @return 列表
     */
    List<ComputerEntity> findAllByStateAndType(ComputerStateEnum computerState, ComputerTypeEnum computerType);

    /**
     * 获取列表
     * @param type ComputerTypeEnum
     * @param computerStateEnum computerStateEnum
     * @return 列表
     */
    List<ComputerEntity> findByWorkModelIsNullAndTypeAndState(ComputerTypeEnum type, ComputerStateEnum computerStateEnum);
}
