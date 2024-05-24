package com.ruijie.rcos.rcdc.rco.module.def.api;

import com.ruijie.rcos.rcdc.rco.module.def.api.dto.CbbDeskFaultInfoDTO;
import com.ruijie.rcos.rcdc.rco.module.def.api.response.CbbDeskFaultInfoResponse;
import com.ruijie.rcos.sk.base.exception.BusinessException;
import com.ruijie.rcos.sk.modulekit.api.comm.DefaultPageResponse;
import com.ruijie.rcos.sk.modulekit.api.comm.DefaultResponse;

import java.util.UUID;

/**
 * Description:
 * Copyright: Copyright (c) 2019
 * Company: Ruijie Co., Ltd.
 * Create Time: 2020/1/4 15:52
 *
 * @author ketb
 */
public interface DeskFaultInfoAPI {


    /**
     * 根据mac查询报障信息
     *
     * @param mac 云桌面mca
     * @return 结果
     */
    CbbDeskFaultInfoResponse findFaultInfoByMac(String mac);

    /**
     * 根据云桌面查询报障信息
     *
     * @param deskId 桌面mca对象
     * @return 结果
     */
    CbbDeskFaultInfoResponse findFaultInfoByDeskId(UUID deskId);

    /**
     * 更新云桌面故障
     *
     * @param cbbDeskFaultInfoDTO 云桌面故障对象
     * @return 结果
     */
    DefaultResponse save(CbbDeskFaultInfoDTO cbbDeskFaultInfoDTO);

    /**
     * 取消报障
     *
     * @param deskId 请求参数
     * @return 结果
     * @throws BusinessException 业务异常
     */
    DefaultResponse relieveFaultForCloudDesk(UUID deskId) throws BusinessException;

    /**
     * 查询指定云桌面的报障信息
     *
     * @param uuidArr 请求参数
     * @return 结果
     */
    DefaultPageResponse<CbbDeskFaultInfoDTO> assemblyInfo(UUID[] uuidArr);
}
