package com.ruijie.rcos.rcdc.rco.module.openapi.service.terminal;

import com.ruijie.rcos.rcdc.rco.module.def.api.request.IdvCreateTerminalGroupRequest;
import com.ruijie.rcos.rcdc.rco.module.def.api.wifi.dto.WifiWhitelistDTO;
import com.ruijie.rcos.rcdc.rco.module.openapi.rest.migration.terminal.dto.ImportTerminalGroupInfoDTO;
import com.ruijie.rcos.rcdc.rco.module.openapi.rest.migration.terminal.dto.ImportTerminalGroupRequest;
import com.ruijie.rcos.rcdc.rco.module.openapi.rest.migration.terminal.dto.ImportTerminalInfoDTO;
import com.ruijie.rcos.rcdc.rco.module.openapi.rest.migration.terminal.dto.ImportTerminalRequest;
import com.ruijie.rcos.sk.base.exception.BusinessException;

import java.util.List;
import java.util.UUID;

/**
 * Description:
 * Copyright: Copyright (c) 2022
 * Company: Ruijie Co., Ltd.
 * Create Time: 2022.04.11
 *
 * @author linhj
 */
public interface ImportTerminalService {

    /**
     * 检查终端导入相关业务
     *
     * @param request 入参请求
     * @return ImportTerminalInfoDTO 业务转换信息
     * @throws BusinessException 业务错误
     */
    ImportTerminalInfoDTO validateAndConvertTerminalRequest(ImportTerminalRequest request) throws BusinessException;

    /**
     * 验证终端组导入参数
     *
     * @param request 入参
     * @return ImportTerminalGroupInfoDTO 业务转换信息
     * @throws BusinessException 业务错误
     */
    ImportTerminalGroupInfoDTO validateAndConvertTerminalGroupRequest(ImportTerminalGroupRequest request) throws BusinessException;

    /**
     * 构建终端组导入参数
     *
     * @param request 入参
     * @return 出餐
     * @throws BusinessException 业务错误
     */
    IdvCreateTerminalGroupRequest buildTerminalGroupRequest(ImportTerminalGroupRequest request) throws BusinessException;

    /**
     * 构建终端实体信息
     *
     * @param request 终端请求
     * @throws BusinessException 业务异常
     */
    void saveTerminalInfo(ImportTerminalRequest request) throws BusinessException;

    /**
     * 修改终端分组
     *
     * @param terminalId   终端标识
     * @param terminalName 终端名称
     * @param groupId      分组标识
     * @throws BusinessException 业务错误
     */
    void modifyTerminalGroup(String terminalId, String terminalName, UUID groupId) throws BusinessException;


    /**
     * 处理 WIFI 白名单导入
     *
     * @param terminalGroupId     终端标识
     * @param needApplyToSubgroup 是否应用子组
     * @param wifiWhiteList       白名单列表
     */
    void dealWifiWriteList(UUID terminalGroupId, Boolean needApplyToSubgroup, List<WifiWhitelistDTO> wifiWhiteList);

}
