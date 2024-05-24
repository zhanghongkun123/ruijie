package com.ruijie.rcos.rcdc.rco.module.def.api;

import com.ruijie.rcos.rcdc.clouddesktop.module.def.dto.desk.CbbSessionInfoDTO;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.dto.desk.CbbUpdateReportSessionSuccessDTO;
import com.ruijie.rcos.rcdc.rco.module.def.api.request.PageSearchRequest;
import com.ruijie.rcos.rcdc.rco.module.def.desktopsession.DesktopSessionDTO;
import com.ruijie.rcos.rcdc.rco.module.def.security.auditapply.dto.ExportExcelCacheDTO;
import com.ruijie.rcos.rcdc.rco.module.def.security.auditapply.response.GetExportExcelResponse;
import com.ruijie.rcos.sk.base.exception.BusinessException;
import com.ruijie.rcos.sk.modulekit.api.comm.DefaultPageResponse;

import java.util.List;
import java.util.UUID;

/**
 * Description: 桌面会话信息api
 * Copyright: Copyright (c) 2024
 * Company: Ruijie Co., Ltd.
 * Create Time: 2024年02月20日
 *
 * @author wangjie9
 */
public interface DesktopSessionServiceAPI {

    /**
     * 获取会话列表
     *
     * @param request 请求参数对象
     * @return 返回查询
     * @throws BusinessException 业务异常
     */
    DefaultPageResponse<DesktopSessionDTO> pageQuery(PageSearchRequest request) throws BusinessException;

    /**
     * 异步导出数据到excel
     *
     * @param request 分页查询参数
     * @param userId  用户ID
     * @throws BusinessException 业务异常
     */
    void exportDataAsync(PageSearchRequest request, String userId) throws BusinessException;

    /**
     * 获取导出报表结果
     *
     * @param userId 用户ID
     * @return 结果DTO
     */
    ExportExcelCacheDTO getExportDataCache(String userId);

    /**
     * 下载报表excel
     *
     * @param userId 用户ID
     * @return 下载信息
     * @throws BusinessException 业务异常
     */
    GetExportExcelResponse getExportFile(String userId) throws BusinessException;

    /**
     * 更新主机会话信息
     *
     * @param sessionInfoDTO 会话信息
     * @return 记录成功的会话列表
     * @throws BusinessException 业务异常
     */
    CbbUpdateReportSessionSuccessDTO synchronizeSessionInfo(CbbSessionInfoDTO sessionInfoDTO) throws BusinessException;

    /**
     * 通知OA对注销中的会话进行注销
     *
     * @param id 会话信息主键id
     * @throws BusinessException 业务异常
     */
    void destroySessionInDestroying(UUID id) throws BusinessException;

    /**
     * 修改会话状态
     *
     * @param id 主键id
     * @return 桌面id
     * @throws BusinessException 业务异常
     */
    UUID updateSessionStatus(UUID id) throws BusinessException;

    /**
     * 统计数量
     * @param deskId deskId
     * @return 列表
     */
    int countByDeskId(UUID deskId);

    /**
     * 获取会话信息
     * @param userId userId
     * @return List<DesktopSessionDTO>
     */
    List<DesktopSessionDTO> findByUserId(UUID userId);

    /**
     * 获取会话信息
     * @param deskId deskId
     * @return List<DesktopSessionDTO>
     */
    List<DesktopSessionDTO> findByDeskId(UUID deskId);

    /**
     * 获取会话信息
     * @param userId userId
     * @param desktopId desktopId
     * @return DesktopSessionDTO
     */
    DesktopSessionDTO findByUserIdAndDeskId(UUID userId, UUID desktopId);

    /**
     * 获取会话信息
     *
     * @param id id
     * @return DesktopSessionDTO
     * @throws BusinessException 业务异常
     */
    DesktopSessionDTO findById(UUID id) throws BusinessException;

    /**
     * 根据桌面id判断桌面是否在线
     *
     * @param deskId 桌面id
     * @return boolean
     * @throws BusinessException 业务异常
     */
    Boolean isOffLine(UUID deskId) throws BusinessException;

    /**
     * 删除会话信息
     *
     * @param id 会话id
     */
    void deleteSessionById(UUID id);

    /**
     * 根据桌面ID删除会话
     *
     * @param deskId 桌面ID
     */
    void deleteSessionByDeskId(UUID deskId);
}
