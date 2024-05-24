package com.ruijie.rcos.rcdc.rco.module.impl.tx;

import com.ruijie.rcos.rcdc.rco.module.def.api.dto.bigscreen.CabinetMappingServerDTO;
import com.ruijie.rcos.sk.base.exception.BusinessException;
import java.util.List;
import java.util.UUID;

/**
 * Description: 机柜配置服务器事务
 * Copyright: Copyright (c) 2019
 * Company: Ruijie Co., Ltd.
 * Create Time: 2019年7月24日
 *
 * @author bgl
 */
public interface CabinetServiceTx {

    /**
     * 服务器配置事务操作
     * @param dto 机柜dto
     * @throws BusinessException 业务异常
     */
    void configServer(CabinetMappingServerDTO dto) throws BusinessException;

    /**
     * 删除机柜
     * @param cabinetId 机柜id
     */
    void deleteCabinet(UUID cabinetId);

    /**
     * 删除机柜上的服务器，并同步机柜关联的服务器数量
     * @param cabinetId 机柜id
     * @param serverIdList 服务器id列表
     * @throws BusinessException 业务异常
     */
    void deleteServerFromCabinet(UUID cabinetId, List<UUID> serverIdList) throws BusinessException;
}
