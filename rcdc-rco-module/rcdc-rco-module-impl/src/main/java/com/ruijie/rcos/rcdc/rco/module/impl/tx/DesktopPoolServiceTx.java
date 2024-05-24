package com.ruijie.rcos.rcdc.rco.module.impl.tx;

import com.ruijie.rcos.rcdc.rco.module.def.desktoppool.dto.MoveDesktopDTO;
import com.ruijie.rcos.rcdc.rco.module.def.desktoppool.dto.UpdatePoolBindObjectDTO;
import com.ruijie.rcos.rcdc.rco.module.def.desktoppool.dto.UpdatePoolThirdPartyBindObjectDTO;
import com.ruijie.rcos.rcdc.rco.module.def.desktoppool.request.UpdateDesktopPoolRequest;
import com.ruijie.rcos.rcdc.rco.module.impl.desktoppool.dto.DesktopPoolBindGroupDTO;
import com.ruijie.rcos.sk.base.exception.BusinessException;

import java.util.UUID;

/**
 * Description: 桌面池事务
 * Copyright: Copyright (c) 2021
 * Company: Ruijie Co., Ltd.
 * Create Time: 2021年12月02日
 *
 * @author linke
 */
public interface DesktopPoolServiceTx {

    /**
     * 编辑桌面池
     *
     * @param request UpdateDesktopPoolRequest
     * @throws BusinessException 业务异常
     */
    void updateDesktopPool(UpdateDesktopPoolRequest request) throws BusinessException;

    /**
     * 修改桌面池绑定对像关联关系
     *
     * @param updatePoolBindObjectDTO UpdatePoolBindObjectDTO
     * @param poolBindGroupDTO poolBindGroupDTO
     */
    void updatePoolBindObject(UpdatePoolBindObjectDTO updatePoolBindObjectDTO, DesktopPoolBindGroupDTO poolBindGroupDTO);

    /**
     * 移动桌面：修改桌面的池信息并添加用户到桌面池中，不传userId就不需要添加用户
     *
     * @param moveDesktopDTO moveDesktopDTO
     * @throws BusinessException 业务异常
     */
    void moveDesktop(MoveDesktopDTO moveDesktopDTO) throws BusinessException;

    /**
     * 取消关联并开启桌面维护模式
     *
     * @param desktopId 桌面ID
     */
    void unbindUserAndDisableDesktop(UUID desktopId);

    /**
     * 修改第三方桌面池绑定对像关联关系
     *
     * @param bindObjectDTO bindObjectDTO
     */
    void updateThirdPartyPoolBindObject(UpdatePoolThirdPartyBindObjectDTO bindObjectDTO);
}
