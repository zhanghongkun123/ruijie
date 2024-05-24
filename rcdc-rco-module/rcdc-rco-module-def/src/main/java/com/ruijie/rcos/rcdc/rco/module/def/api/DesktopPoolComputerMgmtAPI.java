package com.ruijie.rcos.rcdc.rco.module.def.api;

import com.ruijie.rcos.rcdc.rco.module.def.api.dto.ComputerDTO;
import com.ruijie.rcos.rcdc.rco.module.def.api.enums.ComputerRelatedType;
import com.ruijie.rcos.rcdc.rco.module.def.api.request.PageSearchRequest;
import com.ruijie.rcos.rcdc.rco.module.def.desktoppool.dto.DesktopPoolComputerDTO;
import com.ruijie.rcos.sk.modulekit.api.comm.DefaultPageResponse;

import java.util.List;
import java.util.Set;
import java.util.UUID;

/**
 * Description: 桌面池关联PC终端相关API
 * Copyright: Copyright (c) 2024
 * Company: Ruijie Co., Ltd.
 * Create Time: 2024/1/30
 *
 * @author zqj
 */
public interface DesktopPoolComputerMgmtAPI {


    /**
     * 获取关联PC终端列表
     * @param computerRelatedType computerRelatedType
     * @return list
     */
    List<DesktopPoolComputerDTO> listDeskPoolComputerByRelatedType(ComputerRelatedType computerRelatedType);

    /**
     * 获取关联PC终端组id列表
     *
     * @param desktopPoolId desktopPoolId
     * @return 列表
     */
    Set<String> getDesktopPoolRelationTerminalGroup(UUID desktopPoolId);

    /**
     * 获取已分配到桌面池的PC终端列表
     * @param desktopPoolId desktopPoolId
     * @param pageRequest pageRequest
     * @return page
     */
    DefaultPageResponse<ComputerDTO> pageQueryRealBindComputer(UUID desktopPoolId, PageSearchRequest pageRequest);

    /**
     * 移除关系
     * @param poolId poolId
     * @param relatedId relatedId
     */
    void removeByPoolIdAndRelatedId(UUID poolId, UUID relatedId);
}
