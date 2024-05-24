package com.ruijie.rcos.rcdc.rco.module.def.api;

import com.ruijie.rcos.rcdc.rco.module.def.api.dto.ComputerDTO;
import com.ruijie.rcos.rcdc.rco.module.def.api.dto.ExportComputerFileInfoDTO;
import com.ruijie.rcos.rcdc.rco.module.def.api.request.*;
import com.ruijie.rcos.rcdc.rco.module.def.api.response.ComputerInfoResponse;
import com.ruijie.rcos.rcdc.rco.module.def.api.response.GetAssistantAppPathResponse;
import com.ruijie.rcos.rcdc.rco.module.def.api.response.GetExportCloudDesktopFileResponse;
import com.ruijie.rcos.rcdc.terminal.module.def.api.dto.TerminalStatisticsItem;
import com.ruijie.rcos.sk.base.exception.BusinessException;
import com.ruijie.rcos.sk.modulekit.api.comm.DefaultPageResponse;
import com.ruijie.rcos.sk.modulekit.api.comm.DefaultRequest;

import java.util.List;
import java.util.UUID;

/**
 * Description:
 * Copyright: Copyright (c) 2019
 * Company: Ruijie Co., Ltd.
 * Create Time: 2020/1/4 15:52
 *
 * @author ketb
 */
public interface ComputerBusinessAPI {

    /**
     * 分页查询
     *
     * @param request 请求参数
     * @return 结果
     */
    DefaultPageResponse<ComputerDTO> pageQuery(PageSearchRequest request);

    /**
     * 根据桌面id查询信息
     *
     * @param request 请求参数
     * @return 结果
     * @throws BusinessException 业务异常
     */
    ComputerInfoResponse getComputerInfoByComputerId(ComputerIdRequest request) throws BusinessException;

    /**
     * 取消报障
     *
     * @param request 请求参数
     * @throws BusinessException 业务异常
     */
    void relieveFault(ComputerIdRequest request) throws BusinessException;

    /**
     * 修改备注名
     *
     * @param request 请求参数
     * @throws BusinessException 业务异常
     */
    void editComputer(EditComputerRequest request) throws BusinessException;

    /**
     * 删除pc
     *
     * @param request 请求参数
     * @throws BusinessException 业务异常
     */
    void deleteComputer(ComputerIdRequest request) throws BusinessException;

    /**
     * 获取小助手安装包下载路径
     *
     * @param request 请求消息体
     * @return 结果
     * @throws BusinessException 业务异常
     */
    GetAssistantAppPathResponse getAssistantAppDownloadUrl(DefaultRequest request) throws BusinessException;

    /**
     * 获取HALO安装包下载路径
     *
     * @param request 请求消息体
     * @return 结果
     * @throws BusinessException 业务异常
     */
    GetAssistantAppPathResponse getHaloAppDownloadUrl(DefaultRequest request) throws BusinessException;

    /**
     * 编辑终端信息
     *
     * @param request 请求参数
     * @throws BusinessException 业务异常
     */

    void moveComputerGroup(MoveComputerGroupRequest request) throws BusinessException;

    /**
     * 编辑终端信息
     *
     * @param groupIdArr 请求消息体
     * @return 请求结果
     * @throws BusinessException 业务异常
     */
    TerminalStatisticsItem statisticsComputer(UUID[] groupIdArr) throws BusinessException;

    /**
     * 分页查询
     * @param pageRequest 请求消息体
     * @return 请求结果
     */
    DefaultPageResponse<ComputerDTO> computerFaultPageQuery(PageSearchRequest pageRequest);

    /**
     * 获取终端信息
     * @return 终端实体
     * @param ip ip
     */
    ComputerInfoResponse getComputerInfoByIp(String ip);

    /** 添加终端
     * @param request 请求消息体
     * @throws BusinessException 业务异常
     */
    void saveComputer(CreateComputerRequest request) throws BusinessException;

    /**
     * 删除第三方PC
     * @param request 请求实体
     * @throws BusinessException 业务异常
     */
    void deleteThirdPartyComputer(ComputerIdRequest request) throws BusinessException;

    /**
     * 导出信息
     * @param pageReq 请求实体
     * @param userId userId
     * @throws BusinessException 业务异常
     */
    void exportDataAsync(PageSearchRequest pageReq,String  userId) throws BusinessException;

    /**
     * 获取导出结果
     * @param userId userId
     * @return ExportComputerFileInfoDTO
     */
    ExportComputerFileInfoDTO getExportDataCache(String userId);

    /**
     * 获取下载文件
     * @param userId userId
     * @return GetExportCloudDesktopFileResponse
     * @throws BusinessException 业务异常
     */
    GetExportCloudDesktopFileResponse getExportFile(String userId) throws BusinessException;

    /**
     * 唤醒PC终端
     *
     * @param computerId computerId
     * @param enableParallelWake 是否支持同时唤醒
     * @throws BusinessException 业务异常
     */
    void wakeUpComputer(UUID computerId, Boolean enableParallelWake) throws BusinessException;

    /**
     * 根据终端组id数组获取列表
     * @param groupIdList groupIdList
     * @return 列表
     */
    List<ComputerDTO> getComputerInfoByGroupIdList(List<UUID> groupIdList);

    /**
     * 根据pc终端id获取列表
     * @param computerIdList computerIdList
     * @return 列表
     */
    List<ComputerDTO> getComputerInfoByIdList(List<UUID> computerIdList);

    /**
     * 检查Id是否都存在
     * @param idList idList
     * @return boolean
     */
    boolean checkAllComputerExistByIds(List<UUID> idList);

    /**
     * 获取PC终端所属终端组列表
     * @param computerIdList computerIdList
     * @return 列表
     */
    List<UUID> listGroupIdByComputerIdList(List<UUID> computerIdList);

    /**
     * 唤醒桌面
     * @param deskId deskId
     * @throws BusinessException 业务异常
     */
    void wakeUpDesk(UUID deskId) throws BusinessException;

    /**
     * 收集日志
     * @param id pcId
     * @throws BusinessException 业务异常
     */
    void collectLog(UUID id) throws BusinessException;
}
