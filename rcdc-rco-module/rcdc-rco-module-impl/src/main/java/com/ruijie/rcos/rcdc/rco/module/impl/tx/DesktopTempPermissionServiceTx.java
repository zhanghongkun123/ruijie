package com.ruijie.rcos.rcdc.rco.module.impl.tx;

import com.ruijie.rcos.rcdc.rco.module.def.deskstrategy.dto.DesktopTempPermissionCreateDTO;
import com.ruijie.rcos.rcdc.rco.module.def.deskstrategy.dto.DesktopTempPermissionUpdateDTO;
import com.ruijie.rcos.sk.base.exception.BusinessException;

import java.util.UUID;

/**
 * Description: Rco临时权限ServiceTx
 * Copyright: Copyright (c) 2023
 * Company: Ruijie Co., Ltd.
 * Create Time: 2023/5/8
 *
 * @author linke
 */
public interface DesktopTempPermissionServiceTx {

    /**
     * 创建临时权限
     *
     * @param createDTO DesktopTempPermissionCreateDTO
     * @return UUID
     * @throws BusinessException 业务异常
     */
    UUID createDesktopTempPermission(DesktopTempPermissionCreateDTO createDTO) throws BusinessException;

    /**
     * 编辑临时权限
     *
     * @param updateDTO DesktopTempPermissionUpdateDTO
     * @throws BusinessException 业务异常
     */
    void updateDesktopTempPermission(DesktopTempPermissionUpdateDTO updateDTO) throws BusinessException;

    /**
     * 删除临时权限
     *
     * @param id 临时权限ID
     */
    void deleteById(UUID id);
}
