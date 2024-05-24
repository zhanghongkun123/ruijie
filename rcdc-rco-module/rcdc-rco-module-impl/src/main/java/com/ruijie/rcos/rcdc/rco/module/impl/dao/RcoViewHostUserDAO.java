package com.ruijie.rcos.rcdc.rco.module.impl.dao;

import com.ruijie.rcos.rcdc.rco.module.def.api.dto.HostUserGroupBindDeskNumDTO;
import com.ruijie.rcos.rcdc.rco.module.impl.entity.RcoViewHostUserEntity;
import com.ruijie.rcos.sk.modulekit.api.ds.SkyEngineJpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.UUID;

/**
 * Description: 用户持久化接口.
 * Copyright: Copyright (c) 2018
 * Company: Ruijie Co., Ltd.
 * Create Time: 2018年10月30日
 *
 * @author chenzj
 */
public interface RcoViewHostUserDAO extends SkyEngineJpaRepository<RcoViewHostUserEntity, UUID> {


    /**
     * 获取列表
     * @param deskId deskId
     * @return 列表
     */
    List<RcoViewHostUserEntity> findByDesktopId(UUID deskId);

    /**
     * 根据桌面池ID获取用户组绑定的桌面数量
     * @param desktopPoolId desktopPoolId
     * @return 列表
     */
    @Query("select new com.ruijie.rcos.rcdc.rco.module.def.api.dto.HostUserGroupBindDeskNumDTO(groupId, count(distinct desktopId)) "
            + "FROM RcoViewHostUserEntity  where desktopPoolId=?1 GROUP BY groupId")
    List<HostUserGroupBindDeskNumDTO> countGroupDeskNumByDesktopPoolId(UUID desktopPoolId);
}
