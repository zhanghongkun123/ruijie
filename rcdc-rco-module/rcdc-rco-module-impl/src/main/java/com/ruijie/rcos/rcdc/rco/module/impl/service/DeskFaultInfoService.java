package com.ruijie.rcos.rcdc.rco.module.impl.service;


import com.ruijie.rcos.rcdc.rco.module.def.api.dto.CbbDeskFaultInfoDTO;
import com.ruijie.rcos.rcdc.rco.module.impl.entity.DeskFaultInfoEntity;
import com.ruijie.rcos.sk.base.exception.BusinessException;

import java.util.UUID;

/**
 * Description:
 * Copyright: Copyright (c) 2019
 * Company: Ruijie Co., Ltd.
 * Create Time: 2020/1/8 17:14
 *
 * @author ketb
 */
public interface DeskFaultInfoService {

    /**
     * 查询故障
     * @param deskMac 桌面mac
     * @return 结果
     */
    DeskFaultInfoEntity findByDeskMac(String deskMac);

    /**
     * 报障
     * @param id 桌面id
     * @throws BusinessException 业务异常
     */
    void relieveFault(String id) throws BusinessException;

    /**
     * 添加云桌面报障信息
     * @param uuidArr 桌面id
     * @return 结果
     */
    CbbDeskFaultInfoDTO[] assemblyInfo(UUID[] uuidArr);

    /**
     * 根据云桌面ID修改MAC地址
     *
     * @param deskId 云桌面ID
     * @param newMac 新的MAC地址
     */
    void updateMacByDeskId(UUID deskId, String newMac);
}
