package com.ruijie.rcos.rcdc.rco.module.impl.tx;

import java.util.UUID;

import com.ruijie.rcos.gss.sdk.iac.module.def.enums.IacConfigRelatedType;
import com.ruijie.rcos.rcdc.rco.module.def.desktoppool.dto.UpdatePoolBindObjectDTO;
import com.ruijie.rcos.sk.base.exception.BusinessException;

/**
 * Description:
 * Copyright: Copyright (c) 2022
 * Company: Ruijie Co., Ltd.
 * Create Time: 2022/7/15
 *
 * @author TD
 */
public interface UserDiskServiceTx {

    /**
     * 绑定磁盘给用户功能
     * @param diskId 磁盘ID
     * @param userId 用户ID
     * @throws BusinessException 业务异常
     */
    void bindUserDisk(UUID diskId, UUID userId) throws BusinessException;

    /**
     * 解除绑定磁盘用户关系
     * @param diskId 磁盘ID
     * @throws BusinessException 业务异常
     */
    void unBindUserDisk(UUID diskId) throws BusinessException;

    /**
     * 解绑用户与磁盘池关系
     * @param relatedId 关联ID
     * @param relatedType 关联类型
     * @throws BusinessException 业务异常
     */
    void unbindUserAllDiskPool(UUID relatedId, IacConfigRelatedType relatedType) throws BusinessException;

    /**
     * 修改池绑定对像关联关系
     *
     * @param bindObjectDTO bindObjectDTO
     */
    void updatePoolBindObject(UpdatePoolBindObjectDTO bindObjectDTO);
}
