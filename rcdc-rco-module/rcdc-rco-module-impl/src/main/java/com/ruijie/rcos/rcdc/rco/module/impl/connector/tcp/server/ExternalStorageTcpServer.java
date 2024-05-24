package com.ruijie.rcos.rcdc.rco.module.impl.connector.tcp.server;

import com.ruijie.rcos.rcdc.clouddesktop.module.def.dto.extstorage.CbbCreateExternalStorageDTO;
import com.ruijie.rcos.sk.base.exception.BusinessException;
import com.ruijie.rcos.sk.connectkit.api.annotation.ApiAction;
import com.ruijie.rcos.sk.connectkit.api.annotation.tcp.SessionAlias;
import com.ruijie.rcos.sk.connectkit.api.annotation.tcp.Tcp;

import java.util.UUID;

/**
 *
 * Description: 文件服务器
 * Copyright: Copyright (c) 2024
 * Company: Ruijie Co., Ltd.
 * Create Time: 2024/3/25
 * 
 * @author zqj
 */
@Tcp
public interface ExternalStorageTcpServer {

    /**
     * 获取文件服务器信息
     * 
     * @param terminalId 终端ID
     * @param deskId 桌面ID
     *
     * @return 云桌面连接信息
     * @throws BusinessException 业务异常
     */
    @ApiAction("oa_get_cdc_file_server_config")
    CbbCreateExternalStorageDTO getFileServerConfig(@SessionAlias String terminalId, UUID deskId) throws BusinessException;


}
