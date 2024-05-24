package com.ruijie.rcos.rcdc.rco.module.impl.dao;

import java.util.List;
import java.util.UUID;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import com.ruijie.rcos.rcdc.rco.module.def.api.enums.FileDistributionStashTaskStatus;
import com.ruijie.rcos.rcdc.rco.module.impl.entity.DistributeSubTaskEntity;
import com.ruijie.rcos.sk.modulekit.api.ds.SkyEngineJpaRepository;

/**
 * Description: 文件分发任务（子任务）DAO
 * Copyright: Copyright (c) 2022
 * Company: Ruijie Co., Ltd.
 * Create Time: 2022/2/17 15:47
 *
 * @author zhangyichi
 */
public interface DistributeSubTaskDAO extends SkyEngineJpaRepository<DistributeSubTaskEntity, UUID> {

    /**
     * 根据父任务ID查询子任务
     * 
     * @param parentTaskId 父任务ID
     * @return 子任务列表
     */
    List<DistributeSubTaskEntity> findByParentTaskId(UUID parentTaskId);

    /**
     * 根据分发的对象ID查询子任务
     * 
     * @param targetId 分发对象ID
     * @return 子任务列表
     */
    List<DistributeSubTaskEntity> findByTargetId(String targetId);

    /**
     * 根据暂存状态查询子任务
     * 
     * @param stashStatus 暂存状态
     * @return 子任务列表
     */
    List<DistributeSubTaskEntity> findByStashStatus(FileDistributionStashTaskStatus stashStatus);


    /**
     * 根据获取可执行的任务
     * 识别方式 targetId【云桌面】，云桌面需要运行中。相同云桌面同一时间只能执行一个任务。 可能存在多个父任务，必须优先执行最早的未完成的任务，如果该任务已经在队列或者线程中，
     * 则不在将该任务放入到队列中。
     * 
     * @return 子任务列表
     */
    @Query(value = "select t.* from t_rco_distribute_sub_task t inner join t_cbb_desk_info i on CAST(t.target_id  AS UUID) = i.desk_id "
            + "and i.desk_state = 'RUNNING' where t.stash_status ='STASHED' and not exists (select 1 from t_rco_distribute_sub_task t1 "
            + "where t1.stash_status in ('QUEUED','RUNNING') and t1.target_id=t.target_id)", nativeQuery = true)
    List<DistributeSubTaskEntity> findExecutableTaskList();


    /**
     * 根据targetID删除子任务
     *
     * @param targetId 父任务ID
     */
    @Transactional
    @Modifying
    void deleteByTargetId(String targetId);


    /**
     * 根据id更新
     * 
     * @param id 唯一标识
     * @param stashStatus 状态
     */
    @Transactional
    @Modifying
    @Query("update DistributeSubTaskEntity set stashStatus=?2,version=version+1 where id=?1 and version=version")
    void updateStashStatusById(UUID id, FileDistributionStashTaskStatus stashStatus);

}
