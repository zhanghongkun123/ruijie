package com.ruijie.rcos.rcdc.rco.module.def.api;

import com.ruijie.rcos.base.upgrade.module.def.enums.PacketProductType;
import com.ruijie.rcos.rcdc.rco.module.def.api.dto.AppClientCompressionDTO;
import com.ruijie.rcos.rcdc.terminal.module.def.enums.CbbTerminalTypeEnums;
import com.ruijie.rcos.sk.base.exception.BusinessException;

/**
 * Description: 客户端压缩API
 * Copyright: Copyright (c) 2022
 * Company: Ruijie Co., Ltd.
 * Create Time: 2022/3/16
 *
 * @author TD
 */
public interface ClientCompressionAPI {


    /**
     * 初始化oc安装包免配置信息
     * @param productType 升级包类型
     * @throws BusinessException 业务异常
     */
    void createConfiguredInstaller(PacketProductType productType) throws BusinessException;


    /**
     * 获取下载文件名称
     * @param terminalType 终端类型
     * @return 下载文件名称
     * @throws BusinessException 业务异常
     */
    @Deprecated
    String getCompletePackageName(CbbTerminalTypeEnums terminalType) throws BusinessException;

    /**
     * 获取APP软客户端一键安装配置
     * @return 一键安装配置信息
     * @throws BusinessException 业务异常
     */
    @Deprecated
    AppClientCompressionDTO getAppClientCompressionConfig() throws BusinessException;

}
