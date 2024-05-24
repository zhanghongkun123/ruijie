package com.ruijie.rcos.rcdc.rco.module.impl.dao;

import java.util.List;
import java.util.UUID;

import com.ruijie.rcos.rcdc.rco.module.impl.entity.ViewUserTerminalDeskEntity;
import com.ruijie.rcos.sk.modulekit.api.ds.SkyEngineJpaRepository;

/**
 * 
 * Description: 终端视图DAO
 * Copyright: Copyright (c) 2018
 * Company: Ruijie Co., Ltd.
 * Create Time: 2018年12月21日
 * 
 * @author nt
 */
public interface ViewUsetTerminalDeskDAO extends SkyEngineJpaRepository<ViewUserTerminalDeskEntity, UUID> {


    /**
     * 查询桌面信息 通过终端ID
     * 
     * @param idList 终端ID 集合
     * @return 用户终端桌面 基本信息
     */
    List<ViewUserTerminalDeskEntity> findByterminalIdIn(List<String> idList);


}
