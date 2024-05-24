package com.ruijie.rcos.rcdc.rco.module.impl.api;

import com.alibaba.fastjson.JSON;
import com.google.common.collect.Lists;
import com.ruijie.rcos.gss.log.module.def.api.BaseAuditLogAPI;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.api.CbbDeskMgmtAPI;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.api.CbbOneAgentTcpSendAPI;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.dto.CbbDeskDTO;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.dto.desk.CbbRdsMgrDesktopSessionInfoDTO;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.dto.desk.CbbSessionInfoDTO;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.dto.desk.CbbUpdateReportSessionSuccessDTO;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.dto.desk.DestroyOneAgentSessionDTO;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.enums.CbbCloudDeskState;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.enums.CbbCloudDeskType;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.enums.CbbDesktopSessionType;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.enums.desktoppool.DesktopPoolType;
import com.ruijie.rcos.rcdc.rco.module.common.connect.SessionManager;
import com.ruijie.rcos.rcdc.rco.module.def.annotation.ExcelHead;
import com.ruijie.rcos.rcdc.rco.module.def.api.DesktopSessionServiceAPI;
import com.ruijie.rcos.rcdc.rco.module.def.api.UserDesktopMgmtAPI;
import com.ruijie.rcos.rcdc.rco.module.def.api.UserInfoAPI;
import com.ruijie.rcos.rcdc.rco.module.def.api.dto.RcoUserDesktopDTO;
import com.ruijie.rcos.rcdc.rco.module.def.api.request.PageSearchRequest;
import com.ruijie.rcos.rcdc.rco.module.def.constants.Constants;
import com.ruijie.rcos.rcdc.rco.module.def.desktopsession.DesktopSessionDTO;
import com.ruijie.rcos.rcdc.rco.module.def.security.auditapply.dto.ExportExcelCacheDTO;
import com.ruijie.rcos.rcdc.rco.module.def.security.auditapply.enums.ExportExcelStateEnum;
import com.ruijie.rcos.rcdc.rco.module.def.security.auditapply.response.GetExportExcelResponse;
import com.ruijie.rcos.rcdc.rco.module.impl.DesktopSessionBusinessKey;
import com.ruijie.rcos.rcdc.rco.module.impl.cache.ExportDesktopSessionExcelCacheManger;
import com.ruijie.rcos.rcdc.rco.module.impl.dao.DesktopUserSessionDAO;
import com.ruijie.rcos.rcdc.rco.module.impl.dao.ViewDesktopDetailDAO;
import com.ruijie.rcos.rcdc.rco.module.impl.dao.ViewDesktopSessionDAO;
import com.ruijie.rcos.rcdc.rco.module.impl.dto.ExportDesktopSessionExcelDTO;
import com.ruijie.rcos.rcdc.rco.module.impl.entity.DesktopUserSessionEntity;
import com.ruijie.rcos.rcdc.rco.module.impl.entity.HostUserEntity;
import com.ruijie.rcos.rcdc.rco.module.impl.entity.UserDesktopEntity;
import com.ruijie.rcos.rcdc.rco.module.impl.entity.ViewDesktopSessionEntity;
import com.ruijie.rcos.rcdc.rco.module.impl.enums.SessionStatusEnums;
import com.ruijie.rcos.rcdc.rco.module.impl.security.auditapply.AuditApplyBusinessKey;
import com.ruijie.rcos.rcdc.rco.module.impl.service.HostUserService;
import com.ruijie.rcos.rcdc.rco.module.impl.service.QueryDesktopSessionListService;
import com.ruijie.rcos.rcdc.rco.module.impl.service.UserDesktopService;
import com.ruijie.rcos.rcdc.rco.module.impl.util.ExportUtils;
import com.ruijie.rcos.sk.base.concurrent.ThreadExecutors;
import com.ruijie.rcos.sk.base.exception.BusinessException;
import com.ruijie.rcos.sk.base.lockable.LockableExecutor;
import com.ruijie.rcos.sk.base.log.Logger;
import com.ruijie.rcos.sk.base.log.LoggerFactory;
import com.ruijie.rcos.sk.modulekit.api.comm.DefaultPageResponse;
import com.ruijie.rcos.sk.pagekit.api.PageQueryResponse;
import org.apache.commons.lang3.StringUtils;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;
import org.apache.poi.xssf.usermodel.XSSFRichTextString;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.util.Assert;
import org.springframework.util.CollectionUtils;

import java.io.File;
import java.io.FileOutputStream;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

/**
 * Description:
 * Copyright: Copyright (c) 2024
 * Company: Ruijie Co., Ltd.
 * Create Time: 2024年02月20日
 *
 * @author wangjie9
 */
public class DesktopSessionServiceAPIImpl implements DesktopSessionServiceAPI {

    private static final Logger LOGGER = LoggerFactory.getLogger(DesktopSessionServiceAPIImpl.class);

    private static final String FILE_PREFIX = "desktop_session_";

    private static final String FILE_POSTFIX = ".xlsx";

    private static final String EXPORT_DESKTOP_SESSION_THREAD_NAME = "exportDesktopSessionInfo";

    private static final int DEFAULT_EXPORT_PAGE_SIZE = 20000;

    private static final String DEFAULT_EXCEL_SHEET_NAME = "Sheet1";

    private static final Integer TEN = 10;

    @Autowired
    private ExportDesktopSessionExcelCacheManger cacheMgt;

    @Autowired
    private DesktopUserSessionDAO desktopUserSessionDAO;

    @Autowired
    private CbbOneAgentTcpSendAPI cbbOneAgentTcpSendAPI;

    @Autowired
    private BaseAuditLogAPI auditLogAPI;

    @Autowired
    private UserDesktopMgmtAPI userDesktopMgmtAPI;

    @Autowired
    private ViewDesktopDetailDAO viewDesktopDetailDAO;

    @Autowired
    private HostUserService hostUserService;

    @Autowired
    private CbbDeskMgmtAPI cbbDeskMgmtAPI;

    @Autowired
    private QueryDesktopSessionListService queryDesktopSessionListService;

    @Autowired
    private UserInfoAPI userInfoAPI;

    @Autowired
    private UserDesktopService userDesktopService;

    @Autowired
    private ViewDesktopSessionDAO viewDesktopSessionDAO;

    @Autowired
    private SessionManager sessionManager;

    @Override
    public DefaultPageResponse<DesktopSessionDTO> pageQuery(PageSearchRequest request) throws BusinessException {
        Assert.notNull(request, "request is not null");

        Page<ViewDesktopSessionEntity> pageQuery = queryDesktopSessionListService.pageQuery(request, ViewDesktopSessionEntity.class);
        DesktopSessionDTO[] desktopSessionDTOArr = pageQuery.getContent().stream()
                .map(this::convertFromEntity).toArray(DesktopSessionDTO[]::new);
        DefaultPageResponse<DesktopSessionDTO> response = new DefaultPageResponse<>();
        response.setTotal(pageQuery.getTotalElements());
        response.setItemArr(desktopSessionDTOArr);
        return response;
    }

    /**
     * entity转dto
     *
     * @param viewDesktopSessionEntity entity
     * @return dto
     */
    public DesktopSessionDTO convertFromEntity(ViewDesktopSessionEntity viewDesktopSessionEntity) {
        Assert.notNull(viewDesktopSessionEntity, "viewDesktopSessionEntity can not be null");
        DesktopSessionDTO desktopSessionDTO = new DesktopSessionDTO();
        BeanUtils.copyProperties(viewDesktopSessionEntity, desktopSessionDTO);
        return desktopSessionDTO;
    }

    @Override
    public void exportDataAsync(PageSearchRequest request, String userId) throws BusinessException {
        Assert.notNull(request, "request is not null");
        Assert.hasText(userId, "userId is not null");

        ExportExcelCacheDTO cache = cacheMgt.getCache(userId);
        if (Objects.nonNull(cache) && ExportExcelStateEnum.DOING.equals(cache.getState())) {
            throw new BusinessException(AuditApplyBusinessKey.RCDC_RCO_EXPORT_APPLY_DOING);
        } else {
            String tmpFileName = getTmpFileName(userId);
            // 清空旧缓存
            cacheMgt.deleteCache(userId);
            // 在删除一次，此处是为了防止缓存不存在而目录文件中已有文件，确保只有一个文件
            deleteOldFile(tmpFileName);
            ExportExcelCacheDTO newCache = new ExportExcelCacheDTO();
            cacheMgt.save(userId, newCache);
            // 开启线程导出excel
            ThreadExecutors.submit(EXPORT_DESKTOP_SESSION_THREAD_NAME, () -> {
                try {
                    String exportFilePath = queryAndExport(request, tmpFileName);
                    newCache.setState(ExportExcelStateEnum.DONE);
                    newCache.setCreateTimestamp(System.currentTimeMillis());
                    newCache.setExportFilePath(exportFilePath);
                    newCache.setFileName(tmpFileName);
                    cacheMgt.save(userId, newCache);
                    LOGGER.info("导出申请单报表成功，导出路径是:{}", exportFilePath);
                } catch (Exception e) {
                    LOGGER.error("导出申请单报表出现异常:", e);
                    cacheMgt.updateState(userId, ExportExcelStateEnum.FAULT);
                }
            });
        }
    }

    @Override
    public ExportExcelCacheDTO getExportDataCache(String userId) {
        Assert.hasText(userId, "userId is not null");
        ExportExcelCacheDTO cache = cacheMgt.getCache(userId);
        // 缓存为空时,可能正在进行中
        if (Objects.isNull(cache)) {
            cache = new ExportExcelCacheDTO();
            cache.setState(ExportExcelStateEnum.DOING);
        }
        return cache;
    }

    @Override
    public GetExportExcelResponse getExportFile(String userId) throws BusinessException {
        Assert.hasText(userId, "userId is not null");
        ExportExcelCacheDTO cache = cacheMgt.getCache(userId);
        if (Objects.isNull(cache)) {
            throw new BusinessException(AuditApplyBusinessKey.RCDC_RCO_EXPORT_APPLY_FILE_NOT_EXIST);
        }
        String exportFilePath = cache.getExportFilePath();
        File file = new File(exportFilePath);
        if (!file.exists()) {
            throw new BusinessException(AuditApplyBusinessKey.RCDC_RCO_EXPORT_APPLY_FILE_NOT_EXIST);
        }
        return new GetExportExcelResponse(file);
    }

    @Override
    public CbbUpdateReportSessionSuccessDTO synchronizeSessionInfo(CbbSessionInfoDTO sessionInfoDTO) throws BusinessException {
        Assert.notNull(sessionInfoDTO, "sessionInfoDTO can not be null");
        LOGGER.info("收到会话信息[{}]", JSON.toJSONString(sessionInfoDTO));
        Assert.notNull(sessionInfoDTO.getHostId(), "hostId must not be null");
        List<CbbRdsMgrDesktopSessionInfoDTO> reportOnlineSessionInfoSuccessList = new ArrayList<>();
        List<CbbRdsMgrDesktopSessionInfoDTO> reportDestroySessionInfoSuccessList = new ArrayList<>();
        UUID deskId = sessionInfoDTO.getHostId();

        CbbUpdateReportSessionSuccessDTO updateReportSessionSuccessDTO = new CbbUpdateReportSessionSuccessDTO();
        // OA上报的即将在线的会话信息
        oaReportOnlineSessionInfo(sessionInfoDTO, reportOnlineSessionInfoSuccessList);
        // OA上报的即将离线的会话信息
        onReportDestroySessionInfo(sessionInfoDTO, reportDestroySessionInfoSuccessList);
        // 云桌面上所有用户会话信息列表
        List<CbbRdsMgrDesktopSessionInfoDTO> userSessionInfoList = sessionInfoDTO.getUserSessionInfoList();
        if (CollectionUtils.isEmpty(userSessionInfoList)) {
            deleteSessionByDeskId(deskId);
        } else {
            // 对比正在使用的会话
            List<DesktopUserSessionEntity> desktopUserSessionEntityList = desktopUserSessionDAO.findAllByDeskIdIn(Lists.newArrayList(deskId));
            CbbDeskDTO deskDTO = cbbDeskMgmtAPI.getDeskById(deskId);
            for (DesktopUserSessionEntity sessionEntity : desktopUserSessionEntityList) {
                if (isSessionDisconnect(sessionEntity, sessionInfoDTO.getUserSessionInfoList())) {
                    deleteSession(sessionEntity);
                } else {
                    // 判断用户和桌面的绑定关系,并且通知OA注销会话
                    checkUserNotInDesk(deskDTO, sessionEntity);
                }
            }
        }

        updateReportSessionSuccessDTO.setReportDestroySessionSuccessList(reportDestroySessionInfoSuccessList);
        updateReportSessionSuccessDTO.setReportOnlineSessionSuccessList(reportOnlineSessionInfoSuccessList);
        return updateReportSessionSuccessDTO;
    }

    private void checkUserNotInDesk(CbbDeskDTO deskDTO, DesktopUserSessionEntity sessionEntity) {
        UUID deskId = deskDTO.getDeskId();
        UUID userId = sessionEntity.getUserId();
        try {
            if (CbbDesktopSessionType.MULTIPLE == deskDTO.getSessionType()) {
                HostUserEntity hostUserEntity = hostUserService.findByDeskIdAndUserId(deskId, userId);
                if (Objects.isNull(hostUserEntity)) {
                    LOGGER.warn("OA上报会话检查用户:{} 与桌面:{} 的关系已经解绑,通知OA注销", userId, deskId);
                    destroySession(sessionEntity);
                }
            } else {
                UserDesktopEntity userDesktopEntity = userDesktopService.findByDeskId(deskId);
                if (!Objects.equals(userDesktopEntity.getUserId(), userId)) {
                    LOGGER.warn("OA上报会话检查用户:{} 与桌面:{} 的关系已经解绑,通知OA注销", userId, deskId);
                    destroySession(sessionEntity);
                }
            }
        } catch (Exception e) {
            LOGGER.error("OA上报的会话:{} 用户与桌面关系已经解绑,通知OA注销时异常:", JSON.toJSONString(sessionEntity), e);
        }
    }

    private void onReportDestroySessionInfo(CbbSessionInfoDTO sessionInfoDTO, List<CbbRdsMgrDesktopSessionInfoDTO> destroySessionSuccessList) {
        List<CbbRdsMgrDesktopSessionInfoDTO> needReportDestroySessionInfoList = sessionInfoDTO.getNeedReportDestroySessionInfoList();
        if (CollectionUtils.isEmpty(needReportDestroySessionInfoList)) {
            return;
        }
        for (CbbRdsMgrDesktopSessionInfoDTO desktopSessionInfoDTO : needReportDestroySessionInfoList) {
            List<DesktopUserSessionEntity> sessionEntityList = desktopUserSessionDAO.findByUserIdAndSessionIdAndDeskId(
                    desktopSessionInfoDTO.getUserId(), desktopSessionInfoDTO.getSessionId(), sessionInfoDTO.getHostId());
            if (CollectionUtils.isEmpty(sessionEntityList)) {
                continue;
            }
            DesktopUserSessionEntity sessionEntity = sessionEntityList.get(0);
            long destroyTimeMillis = desktopSessionInfoDTO.getDestroyTime() == null ? 0 : desktopSessionInfoDTO.getDestroyTime() * 1000;
            if (destroyTimeMillis > sessionEntity.getLastCreateTime().getTime()) {
                deleteSession(sessionEntity);
            }
            destroySessionSuccessList.add(desktopSessionInfoDTO);
        }
    }

    private void oaReportOnlineSessionInfo(CbbSessionInfoDTO sessionInfoDTO, List<CbbRdsMgrDesktopSessionInfoDTO> reportOnlineSessionInfoSuccessList)
            throws BusinessException {
        List<CbbRdsMgrDesktopSessionInfoDTO> needReportOnlineSessionInfoList = sessionInfoDTO.getNeedReportOnlineSessionInfoList();
        if (!CollectionUtils.isEmpty(needReportOnlineSessionInfoList)) {
            for (CbbRdsMgrDesktopSessionInfoDTO desktopSessionInfoDTO : needReportOnlineSessionInfoList) {
                Date lastCreateTime =
                        desktopSessionInfoDTO.getLastCreateTime() == null ? null : new Date(desktopSessionInfoDTO.getLastCreateTime());
                Date lastIdleTime =
                        desktopSessionInfoDTO.getLastIdleTime() == null ? null : new Date(desktopSessionInfoDTO.getLastIdleTime());
                String userName = userInfoAPI.getUserNameById(desktopSessionInfoDTO.getUserId());
                // 创建会话记录
                createSessionInfo(sessionInfoDTO, desktopSessionInfoDTO, lastCreateTime, lastIdleTime, userName);
                reportOnlineSessionInfoSuccessList.add(desktopSessionInfoDTO);
            }
        }
    }

    private void createSessionInfo(CbbSessionInfoDTO sessionInfoDTO, CbbRdsMgrDesktopSessionInfoDTO desktopSessionInfoDTO,
                                        Date lastCreateTime, Date lastIdleTime, String userName) throws BusinessException {

        String lockKey = (desktopSessionInfoDTO.getUserId() != null ? desktopSessionInfoDTO.getUserId().toString() : "") + "-"
                + (desktopSessionInfoDTO.getSessionId() != null ? desktopSessionInfoDTO.getSessionId().toString() : "") + "-"
                + (sessionInfoDTO.getHostId() != null ? sessionInfoDTO.getHostId().toString() : "") ;

        // 防止oa重复上报出现两条相同会话记录
        LockableExecutor.executeWithTryLock(lockKey, () -> {
            List<DesktopUserSessionEntity> sessionEntityList = desktopUserSessionDAO.findByUserIdAndSessionIdAndDeskId(
                    desktopSessionInfoDTO.getUserId(), desktopSessionInfoDTO.getSessionId(), sessionInfoDTO.getHostId());
            if (CollectionUtils.isEmpty(sessionEntityList)) {
                //新增一个session
                DesktopUserSessionEntity newEntity = new DesktopUserSessionEntity();
                newEntity.setUserId(desktopSessionInfoDTO.getUserId());
                newEntity.setDeskId(sessionInfoDTO.getHostId());
                newEntity.setSessionId(desktopSessionInfoDTO.getSessionId());
                newEntity.setSessionStatus(SessionStatusEnums.ONLINE);
                newEntity.setCreateTime(new Date());
                newEntity.setUpdateTime(new Date());
                newEntity.setLastCreateTime(lastCreateTime);
                newEntity.setLastIdleTime(lastIdleTime);
                desktopUserSessionDAO.save(newEntity);

                LOGGER.info("新增桌面会话信息:{}", JSON.toJSONString(newEntity));
                RcoUserDesktopDTO desktopDTO = userDesktopMgmtAPI.findByDesktopId(newEntity.getDeskId());
                auditLogAPI.recordLog(DesktopSessionBusinessKey.RCDC_RCO_DESKTOP_SESSION_SAVE_SUCCESS, userName, desktopDTO.getDesktopName());
            } else {
                // 更新session
                DesktopUserSessionEntity sessionEntity = sessionEntityList.get(0);
                if (sessionEntity.getSessionStatus() != SessionStatusEnums.DESTROYING) {
                    sessionEntity.setLastCreateTime(lastCreateTime);
                    sessionEntity.setLastIdleTime(lastIdleTime);
                    sessionEntity.setUpdateTime(new Date());
                    desktopUserSessionDAO.save(sessionEntity);
                }
            }
        }, TEN);
    }

    @Override
    public void destroySessionInDestroying(UUID id) throws BusinessException {
        Assert.notNull(id, "id can not be null");
        List<DesktopUserSessionEntity> sessionList = desktopUserSessionDAO.findAllByIdAndSessionStatus(id, SessionStatusEnums.DESTROYING);
        if (CollectionUtils.isEmpty(sessionList)) {
            return;
        }
        for (DesktopUserSessionEntity session : sessionList) {
            destroySession(session);
        }
    }

    @Override
    public UUID updateSessionStatus(UUID id) throws BusinessException {
        Assert.notNull(id, "id can not be null");
        DesktopUserSessionEntity sessionEntity = desktopUserSessionDAO.getOne(id);
        if (sessionEntity == null) {
            throw new BusinessException(DesktopSessionBusinessKey.RCDC_RCO_DESKTOP_SESSION_NOT_EXIT);
        }
        sessionEntity.setSessionStatus(SessionStatusEnums.DESTROYING);
        desktopUserSessionDAO.save(sessionEntity);

        ViewDesktopSessionEntity viewDesktopSessionDTO = new ViewDesktopSessionEntity();
        BeanUtils.copyProperties(sessionEntity, viewDesktopSessionDTO);
        return sessionEntity.getDeskId();
    }

    @Override
    public int countByDeskId(UUID deskId) {
        Assert.notNull(deskId, "deskId can not be null");
        return desktopUserSessionDAO.countByDeskId(deskId);
    }

    @Override
    public List<DesktopSessionDTO> findByUserId(UUID userId) {
        Assert.notNull(userId, "userId can not be null");
        List<DesktopUserSessionEntity> sessionEntityList = desktopUserSessionDAO.findByUserId(userId);
        return getDesktopSessionDTOS(sessionEntityList);
    }

    @Override
    public List<DesktopSessionDTO> findByDeskId(UUID deskId) {
        Assert.notNull(deskId, "deskId can not be null");
        List<DesktopUserSessionEntity> sessionEntityList = desktopUserSessionDAO.findByDeskId(deskId);
        return getDesktopSessionDTOS(sessionEntityList);
    }

    @Override
    public DesktopSessionDTO findByUserIdAndDeskId(UUID userId, UUID desktopId) {
        Assert.notNull(userId, "userId can not be null");
        Assert.notNull(desktopId, "desktopId can not be null");
        DesktopUserSessionEntity userSessionEntity = desktopUserSessionDAO.findByUserIdAndDeskId(userId, desktopId);
        return getDesktopSessionDTO(userSessionEntity);
    }

    @Override
    public DesktopSessionDTO findById(UUID id) throws BusinessException {
        Assert.notNull(id, "id can not be null");

        ViewDesktopSessionEntity entity = viewDesktopSessionDAO.getOne(id);
        if (entity == null) {
            throw new BusinessException(DesktopSessionBusinessKey.RCDC_RCO_DESKTOP_SESSION_NOT_EXIT);
        }
        DesktopSessionDTO desktopSessionDTO = new DesktopSessionDTO();
        BeanUtils.copyProperties(entity, desktopSessionDTO);
        return desktopSessionDTO;
    }

    @Override
    public Boolean isOffLine(UUID deskId) throws BusinessException {
        Assert.notNull(deskId, "deskId can not be null");

        CbbDeskDTO deskDTO = cbbDeskMgmtAPI.getDeskById(deskId);
        if (CbbCloudDeskState.OFF_LINE == deskDTO.getDeskState()) {
            return true;
        }
        return false;
    }

    @Override
    public void deleteSessionById(UUID id) {
        Assert.notNull(id, "id can not be null");
        Optional<DesktopUserSessionEntity> optional = desktopUserSessionDAO.findById(id);
        if (optional.isPresent()) {
            desktopUserSessionDAO.deleteById(id);
            DesktopUserSessionEntity sessionEntity = optional.get();
            deleteSessionPosterior(sessionEntity.getUserId(), sessionEntity.getDeskId(), false);
        }
    }

    @Override
    public void deleteSessionByDeskId(UUID deskId) {
        Assert.notNull(deskId, "deskId can not be null");
        List<DesktopUserSessionEntity> entityList = desktopUserSessionDAO.findByDeskId(deskId);
        for (DesktopUserSessionEntity sessionEntity : entityList) {
            desktopUserSessionDAO.deleteById(sessionEntity.getId());
            deleteSessionPosterior(sessionEntity.getUserId(), deskId, false);
        }

    }

    private static List<DesktopSessionDTO> getDesktopSessionDTOS(List<DesktopUserSessionEntity> sessionEntityList) {
        List<DesktopSessionDTO> sessionDTOList = sessionEntityList.stream().map(s -> {
            return getDesktopSessionDTO(s);
        }).collect(Collectors.toList());
        return sessionDTOList;
    }

    private static DesktopSessionDTO getDesktopSessionDTO(DesktopUserSessionEntity userSessionEntity) {
        if (Objects.isNull(userSessionEntity)) {
            // null
            return null;
        }
        DesktopSessionDTO desktopSessionDTO = new DesktopSessionDTO();
        BeanUtils.copyProperties(userSessionEntity, desktopSessionDTO);
        return desktopSessionDTO;
    }

    private void destroySession(DesktopUserSessionEntity session) throws BusinessException {
        if (session.getSessionStatus() != SessionStatusEnums.DESTROYING) {
            session.setSessionStatus(SessionStatusEnums.DESTROYING);
            desktopUserSessionDAO.save(session);
        }
        DestroyOneAgentSessionDTO destroyOASessionDTO = new DestroyOneAgentSessionDTO();
        destroyOASessionDTO.setSessionId(session.getSessionId());
        LOGGER.info("桌面：{}，注销会话请求：{}", session.getDeskId(), JSON.toJSONString(destroyOASessionDTO));
        cbbOneAgentTcpSendAPI.destroySession(session.getDeskId().toString(), session.getUserId().toString(), destroyOASessionDTO);

    }

    private boolean isSessionDisconnect(DesktopUserSessionEntity sessionEntity, List<CbbRdsMgrDesktopSessionInfoDTO> userSessionInfoList) {
        return userSessionInfoList.stream().noneMatch(session -> Objects.equals(session.getSessionId(), sessionEntity.getSessionId())
                && Objects.equals(session.getUserId(), sessionEntity.getUserId()));
    }

    private void deleteSession(DesktopUserSessionEntity sessionEntity) {
        desktopUserSessionDAO.delete(sessionEntity);
        deleteSessionPosterior(sessionEntity.getUserId(), sessionEntity.getDeskId(), true);
    }

    private void deleteSessionPosterior(UUID userId, UUID deskId, boolean needShutdown) {
        try {
            CbbDeskDTO deskDTO = cbbDeskMgmtAPI.getDeskById(deskId);
            // CDC主动注销会话:动态池删除用户与桌面的关系 静态池不处理
            if (DesktopPoolType.DYNAMIC != deskDTO.getDesktopPoolType()) {
                sessionDeleteLog(deskId, userId, deskDTO.getName());
                return;
            }
            if (CbbDesktopSessionType.MULTIPLE == deskDTO.getSessionType()) {
                // 多会话移除host桌面与用户关系
                hostUserService.removeByUserIdAndDeskId(userId, deskId);
            } else {
                if (needShutdown && CbbCloudDeskType.VDI == deskDTO.getDeskType()) {
                    // VDI动态单会话需要关机,不在这里删除关系
                    return;
                }
                // 第三方动态单会话移除桌面与用户关系
                userDesktopService.unbindUserAndDesktopRelation(deskDTO.getDeskId());
            }
            sessionDeleteLog(deskId, userId, deskDTO.getName());
        } catch (BusinessException e) {
            LOGGER.error("用户[{}]桌面[{}]会话删除后置解绑失败:", userId, deskId, e);
        }
    }

    private void sessionDeleteLog(UUID deskId, UUID userId, String desktopName) {
        try {
            String userName = userInfoAPI.getUserNameById(userId);
            auditLogAPI.recordLog(DesktopSessionBusinessKey.RCDC_RCO_DESKTOP_SESSION_DELETE_SUCCESS, userName, desktopName);
        } catch (Exception e) {
            LOGGER.error("桌面:{} 回收时查询用户:{} 信息异常:", deskId, userId, e);
        }
    }


    private String queryAndExport(PageSearchRequest request, String fileName) throws Exception {
        int page = 0;
        int rowIndex = 0;
        request.setPage(page);
        request.setLimit(DEFAULT_EXPORT_PAGE_SIZE);
        PageQueryResponse<ExportDesktopSessionExcelDTO> pageResult = getExportDesktopSessionList(request);
        LOGGER.info("准备导出申请单报表，导出条目为[{}]", pageResult.getTotal());
        List<List> exList = new ArrayList<>();
        exList.add(ExportUtils.generateHeader(ExportDesktopSessionExcelDTO.class));

        String exportFilePath = Constants.EXPORT_TMP_DIRECTORY + File.separator + fileName;
        try (SXSSFWorkbook workbook = new SXSSFWorkbook(DEFAULT_EXPORT_PAGE_SIZE)) {
            Sheet sheet = workbook.createSheet(DEFAULT_EXCEL_SHEET_NAME);
            checkDirectory();

            while (pageResult.getItemArr().length > 0) {
                Arrays.stream(pageResult.getItemArr()).forEach(item -> {
                    List dataList = new ArrayList();
                    Class clz = item.getClass();
                    Field[] fieldArr = clz.getDeclaredFields();
                    for (Field field : fieldArr) {
                        field.setAccessible(true);
                        ExcelHead annotation = field.getAnnotation(ExcelHead.class);
                        if (Objects.isNull(annotation)) {
                            continue;
                        }
                        String header = annotation.value();
                        if (StringUtils.isNotBlank(header)) {
                            try {
                                dataList.add(field.get(item));
                            } catch (IllegalAccessException e) {
                                LOGGER.info("反射excel字段失败，field为{}", field.getName());
                            }
                        }
                    }
                    exList.add(dataList);
                });
                writerData(exList, sheet, rowIndex);
                rowIndex += exList.size();
                request.setPage(++page);
                request.setLimit(DEFAULT_EXPORT_PAGE_SIZE);
                pageResult = getExportDesktopSessionList(request);
                exList.clear();
            }

            FileOutputStream os = new FileOutputStream(exportFilePath);
            workbook.write(os);
            os.flush();
            os.close();
        } catch (Exception e) {
            LOGGER.error("导出申请单异常", e);
            throw e;
        }
        return exportFilePath;
    }

    /**
     * 获取导出报表数据
     * @param request request
     * @return 响应
     */
    private PageQueryResponse<ExportDesktopSessionExcelDTO> getExportDesktopSessionList(PageSearchRequest request) {
        Page<ViewDesktopSessionEntity> pageQuery = queryDesktopSessionListService.pageQuery(request, ViewDesktopSessionEntity.class);
        PageQueryResponse<ExportDesktopSessionExcelDTO> response = new PageQueryResponse<>();
        response.setTotal(pageQuery.getTotalElements());
        List<ViewDesktopSessionEntity> entityList = pageQuery.getContent();
        if (CollectionUtils.isEmpty(entityList)) {
            response.setItemArr(new ExportDesktopSessionExcelDTO[0]);
            return response;
        }
        response.setItemArr(entityList.stream().map(ExportDesktopSessionExcelDTO::new).toArray(ExportDesktopSessionExcelDTO[]::new));
        return response;
    }

    /**
     * 检查目录是否存在
     */
    private void checkDirectory() {
        File file = new File(Constants.EXPORT_TMP_DIRECTORY);
        if (!file.exists() || !file.isDirectory()) {
            file.mkdirs();
        }
    }

    /**
     * 生成文件名
     *
     * @param userId 用户ID
     * @return 文件名
     */
    private String getTmpFileName(String userId) {
        return StringUtils.join(FILE_PREFIX, userId, FILE_POSTFIX);
    }

    /**
     * 删除旧的缓存
     *
     * @param fileName 文件名
     */
    private void deleteOldFile(String fileName) {
        File file = new File(fileName);
        if (file.exists()) {
            file.delete();
        }
    }

    private static void writerData(List<List> list, Sheet sheet, int rowIndex) {
        // 循环遍历数据，逐行写入Sheet中
        for (List childList : list) {
            // 创建行
            Row row = sheet.createRow(rowIndex++);
            Cell cell;
            for (int j = 0; j < childList.size(); j++) {
                String value = String.valueOf(childList.get(j));
                // 设置第i行第j列为Cells[j]单元格
                cell = row.createCell(j);
                // 设置单元格的值
                cell.setCellValue(new XSSFRichTextString(value));
            }
        }
    }
}
