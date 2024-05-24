package com.ruijie.rcos.rcdc.rco.module.def.api;

import com.ruijie.rcos.rcdc.clouddesktop.module.def.dto.CbbNetworkDataPacketDTO;
import com.ruijie.rcos.sk.base.exception.BusinessException;


/**
 * Description: 对终端进行操作的API
 * Copyright: Copyright (c) 2022
 * Company: Ruijie Co., Ltd.
 * Create Time: 2022年03月24日
 *
 * @author xgx
 */
public interface TerminalOperatorAPI {

    /**
     * 构造唤醒终端参数
     *
     * @param terminalId 终端id
     * @return NetworkDataPacketDTO
     * @throws BusinessException 业务异常
     */
    CbbNetworkDataPacketDTO buildNetworkDataPacketDTO(String terminalId) throws BusinessException;

}
