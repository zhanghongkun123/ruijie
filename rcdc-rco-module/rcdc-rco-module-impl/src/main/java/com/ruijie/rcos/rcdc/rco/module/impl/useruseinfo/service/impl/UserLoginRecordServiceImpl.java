package com.ruijie.rcos.rcdc.rco.module.impl.useruseinfo.service.impl;

import com.alibaba.fastjson.JSON;
import com.google.common.collect.ImmutableList;
import com.ruijie.rcos.gss.sdk.iac.module.def.api.IacUserGroupMgmtAPI;
import com.ruijie.rcos.gss.sdk.iac.module.def.api.IacUserMgmtAPI;
import com.ruijie.rcos.gss.sdk.iac.module.def.dto.user.IacUserDetailDTO;
import com.ruijie.rcos.gss.sdk.iac.module.def.dto.user.IacUserGroupDetailDTO;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.api.CbbDeskMgmtAPI;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.api.CbbIDVDeskStrategyMgmtAPI;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.api.CbbImageTemplateMgmtAPI;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.api.CbbVDIDeskMgmtAPI;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.dto.CbbDeskDTO;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.dto.image.CbbImageTemplateDTO;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.dto.strategy.CbbDeskStrategyDTO;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.dto.userlicense.UserSessionDTO;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.enums.CbbCloudDeskState;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.enums.CbbCloudDeskType;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.enums.ImageUsageTypeEnum;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.enums.userlicense.ResourceTypeEnum;
import com.ruijie.rcos.rcdc.rco.module.def.api.DesktopAPI;
import com.ruijie.rcos.rcdc.rco.module.def.api.UserDesktopMgmtAPI;
import com.ruijie.rcos.rcdc.rco.module.def.api.dto.RcoUserDesktopDTO;
import com.ruijie.rcos.rcdc.rco.module.def.api.dto.user.UserLoginRecordDTO;
import com.ruijie.rcos.rcdc.rco.module.def.api.enums.RemoteAssistState;
import com.ruijie.rcos.rcdc.rco.module.def.api.request.PageSearchRequest;
import com.ruijie.rcos.rcdc.rco.module.def.api.request.UserLoginRecordPageRequest;
import com.ruijie.rcos.rcdc.rco.module.impl.api.dto.DefaultDataSort;
import com.ruijie.rcos.rcdc.rco.module.impl.service.AbstractPageQueryTemplate;
import com.ruijie.rcos.rcdc.rco.module.impl.service.EntityFieldMapper;
import com.ruijie.rcos.rcdc.rco.module.impl.service.PageQuerySpecification;
import com.ruijie.rcos.rcdc.rco.module.impl.service.RemoteAssistService;
import com.ruijie.rcos.rcdc.rco.module.impl.service.struct.RemoteAssistInfo;
import com.ruijie.rcos.rcdc.rco.module.impl.useruseinfo.dao.UserLoginRecordDAO;
import com.ruijie.rcos.rcdc.rco.module.impl.useruseinfo.dao.ViewUserLoginRecordDAO;
import com.ruijie.rcos.rcdc.rco.module.impl.useruseinfo.entity.UserLoginRecordEntity;
import com.ruijie.rcos.rcdc.rco.module.impl.useruseinfo.entity.ViewUserLoginRecordEntity;
import com.ruijie.rcos.rcdc.rco.module.impl.useruseinfo.service.UserLoginRecordService;
import com.ruijie.rcos.rcdc.terminal.module.def.api.CbbTerminalOperatorAPI;
import com.ruijie.rcos.rcdc.terminal.module.def.api.dto.CbbTerminalBasicInfoDTO;
import com.ruijie.rcos.rcdc.terminal.module.def.enums.CbbTerminalPlatformEnums;
import com.ruijie.rcos.sk.base.concurrent.ThreadExecutor;
import com.ruijie.rcos.sk.base.concurrent.ThreadExecutors;
import com.ruijie.rcos.sk.base.exception.BusinessException;
import com.ruijie.rcos.sk.base.lockable.LockableExecutor;
import com.ruijie.rcos.sk.base.log.Logger;
import com.ruijie.rcos.sk.base.log.LoggerFactory;
import com.ruijie.rcos.sk.base.util.StringUtils;
import com.ruijie.rcos.sk.modulekit.api.comm.DefaultPageResponse;
import org.apache.commons.lang3.ArrayUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

/**
 * @author zjy
 * @version 1.0.0
 * @Description 用户登录实现类
 * @createTime 2021-11-01 10:49:00
 */
@Service
public class UserLoginRecordServiceImpl extends AbstractPageQueryTemplate<ViewUserLoginRecordEntity>
        implements UserLoginRecordService {

    private static final Logger LOGGER = LoggerFactory.getLogger(UserLoginRecordServiceImpl.class);

    private static final Map<String, UserLoginRecordDTO> LOGIN_CACHE = new ConcurrentHashMap<>();

    private static final Map<String, Long> REMOTE_ASSISTANCE_CACHE = new ConcurrentHashMap<>();

    private static final Map<String, String> DESK_BIND_TERMINAL_CACHE = new ConcurrentHashMap<>();

    private static final Set<CbbTerminalPlatformEnums> SUPPORT_TERMINAL_TYPE = new HashSet<>();

    static {
        SUPPORT_TERMINAL_TYPE.add(CbbTerminalPlatformEnums.APP);
        SUPPORT_TERMINAL_TYPE.add(CbbTerminalPlatformEnums.VDI);
    }

    private static final String DISCONNECT = "DISCONNECT";

    private static final String CONNECTING = "CONNECTING";

    private static final String CONNECTED = "CONNECTED";

    private static final String LOGIN_OUT_LOCK = "LOGIN_OUT_LOCK_";

    private static final Long UNLOGIN_AUTH_DURATION = 0L;

    private static final int MAX_LOCK_SECOND = 10;

    private static final String THREAD_POOL_NAME = "handle-desk-session-record-time";

    private static final int MAX_THREAD_NUM = 30;

    private static final int QUEUE_SIZE = 1000;

    private static final ThreadExecutor THREAD_EXECUTOR =
            ThreadExecutors.newBuilder(THREAD_POOL_NAME).maxThreadNum(MAX_THREAD_NUM).queueSize(QUEUE_SIZE).build();

    @Autowired
    private UserLoginRecordDAO userLoginRecordDAO;

    @Autowired
    private ViewUserLoginRecordDAO viewUserLoginRecordDAO;

    @Autowired
    private CbbDeskMgmtAPI cbbDeskMgmtAPI;

    @Autowired
    private CbbIDVDeskStrategyMgmtAPI cbbIDVDeskStrategyMgmtAPI;

    @Autowired
    private CbbImageTemplateMgmtAPI cbbImageTemplateMgmtAPI;

    @Autowired
    private CbbVDIDeskMgmtAPI cbbVDIDeskMgmtAPI;

    @Autowired
    protected IacUserMgmtAPI cbbUserAPI;

    @Autowired
    private CbbTerminalOperatorAPI cbbTerminalOperatorAPI;

    @Autowired
    private IacUserGroupMgmtAPI cbbUserGroupAPI;

    @Autowired
    private RemoteAssistService remoteAssistMgmtService;

    @Autowired
    private DesktopAPI desktopAPI;

    @Autowired
    private UserDesktopMgmtAPI userDesktopMgmtAPI;

    @Override
    protected void mappingField(EntityFieldMapper entityFieldMapper) {

    }

    @Override
    protected List<String> getSearchColumn() {
        return ImmutableList.of("userName", "userGroupName");
    }

    @Override
    protected DefaultDataSort getDefaultDataSort() {
        return new DefaultDataSort("createTime", Sort.Direction.DESC);
    }

    @Override
    protected Page<ViewUserLoginRecordEntity> find(Specification<ViewUserLoginRecordEntity> specification, Pageable pageable) {
        if (specification == null) {
            return viewUserLoginRecordDAO.findAll(pageable);
        }
        return viewUserLoginRecordDAO.findAll(specification, pageable);
    }

    @Override
    protected PageQuerySpecification<ViewUserLoginRecordEntity> buildSpecification(PageSearchRequest request) {
        UserLoginRecordPageRequest pageRequest = (UserLoginRecordPageRequest) request;
        if (pageRequest.getSearchKeyword() == null && pageRequest.getStartTime() == null && pageRequest.getEndTime() == null
                && ArrayUtils.isEmpty(pageRequest.getMatchEqualArr())) {
            // 允许为空
            return null;
        } else {
            UserLoginRecordPageSpec pageSpec = new UserLoginRecordPageSpec(request.getSearchKeyword(), getSearchColumn(), request.getMatchEqualArr());
            pageSpec.setStartTime(pageRequest.getStartTime());
            pageSpec.setEndTime(pageRequest.getEndTime());
            return pageSpec;
        }
    }

    @Override
    public DefaultPageResponse<UserLoginRecordDTO> pageQuery(UserLoginRecordPageRequest request) {
        Assert.notNull(request, "request is null");
        Page<ViewUserLoginRecordEntity> loginRecordEntityPage = super.pageQuery(request, ViewUserLoginRecordEntity.class);
        List<ViewUserLoginRecordEntity> entityList = loginRecordEntityPage.getContent();

        DefaultPageResponse<UserLoginRecordDTO> resp = new DefaultPageResponse<UserLoginRecordDTO>();
        UserLoginRecordDTO[] userLoginRecordDtoArr = new UserLoginRecordDTO[entityList.size()];
        for (int i = 0, j = entityList.size(); i < j; i++) {
            userLoginRecordDtoArr[i] = ViewUserLoginRecordEntity.convertEntityToDTO(entityList.get(i));
        }

        resp.setItemArr(userLoginRecordDtoArr);
        resp.setTotal(loginRecordEntityPage.getTotalElements());
        return resp;
    }

    @Override
    public void saveUserLoginInfo(String terminalId, String userName) {
        Assert.hasText(terminalId, "terminalId is null");
        Assert.hasText(userName, "userName is null");
        UserLoginRecordDTO userLoginRecordDTO = LOGIN_CACHE.get(terminalId);
        if (Objects.isNull(userLoginRecordDTO)) {
            LOGGER.warn("保存用户终端登录记录失败，缓存中缺少终端[{}]登录记录", terminalId);
            return;
        }
        if (!Objects.equals(userName, userLoginRecordDTO.getUserName())) {
            LOGGER.warn("保存用户终端登录记录失败，缓存中终端[{}]登录用户为[{}]，非传入用户[{}]", terminalId, userLoginRecordDTO.getUserName(), userName);
            return;
        }
        LOGGER.info("保存用户终端登录记录，userName: {},终端id: {}", userLoginRecordDTO.getUserName(), userLoginRecordDTO.getTerminalId());
        try {
            saveUserLoginRecord(userLoginRecordDTO);
        } catch (Exception e) {
            LOGGER.error("保存用户[{}]登录终端[{}]信息时失败", userName, terminalId, e);
        }
    }

    @Override
    public void saveUserAuthInfo(String terminalId, String userName) {
        Assert.hasText(terminalId, "terminalId is null");
        Assert.hasText(userName, "userName is null");
        Date currentTime = new Date();
        LOGGER.info("更新认证耗时记录，userName: {},终端id: {},currentTime: {}", userName, terminalId, currentTime);
        try {
            UserLoginRecordDTO dto = LOGIN_CACHE.get(terminalId);
            if (Objects.isNull(dto)) {
                LOGGER.warn("更新认证成功记录失败，原因：未查找到终端[{}]的登录记录", terminalId);
                return;
            }
            if (Objects.nonNull(dto.getUserName()) && !Objects.equals(userName, dto.getUserName())) {
                LOGGER.warn("更新认证成功记录失败，原因：终端[{}]最新登录用户为[{}]，不是用户[{}]。登录记录[{}]", terminalId,
                        dto.getUserName(), userName, JSON.toJSONString(dto));
                return;
            }
            if (Objects.isNull(dto.getLoginTime())) {
                LOGGER.warn("更新认证成功记录失败，原因：终端[{}]最新登录用户[{}]缺少登录时间。登录记录[{}]", terminalId, userName, JSON.toJSONString(dto));
                return;
            }
            // 扫码登录场景，展示二维码时没有用户名，此时记录的LOGIN_CACHE内没有userName，更新认证耗时的时候要添加userName
            dto.setUserName(userName);
            dto.setAuthDuration(currentTime.getTime() - dto.getLoginTime().getTime());
            LOGIN_CACHE.put(terminalId, dto);
        } catch (Exception e) {
            LOGGER.error("更新认证耗时记录失败，userName: {},终端id: {},currentTime: {}", userName, terminalId, currentTime);
        }
    }

    @Override
    public void saveConnectVmSuccessTime(String deskId, Long connectionId) {
        Assert.notNull(deskId, "deskId is null");
        Assert.notNull(connectionId, "connectionId is null");
        String taskId = new StringBuilder().append(LOGIN_OUT_LOCK).append(deskId).toString();
        try {
            LockableExecutor.executeWithTryLock(taskId, () -> {
                saveConnectSuccessInfo(deskId, connectionId);
            }, MAX_LOCK_SECOND);
        } catch (Exception e) {
            LOGGER.error(String.format("锁异常,任务ID:[%s]桌面id:[%s]", taskId, deskId), e);
        }
    }

    @Override
    public void saveLogoutTime(String deskId, @Nullable Long connectionId) {
        Assert.notNull(deskId, "deskId is null");
        LOGGER.info("保存退出EST连接时间，桌面deskId: {}, 会话id：{}", deskId, connectionId);
        String taskId = new StringBuilder().append(LOGIN_OUT_LOCK).append(deskId).toString();
        try {
            LockableExecutor.executeWithTryLock(taskId, () -> {
                if (REMOTE_ASSISTANCE_CACHE.containsKey(deskId) && Objects.equals(REMOTE_ASSISTANCE_CACHE.get(deskId), connectionId)) {
                    LOGGER.info("删除桌面[{}]远程会话[{}]缓存", deskId, connectionId);
                    REMOTE_ASSISTANCE_CACHE.remove(deskId);
                    return;
                }

                Date currentTime = new Date();

                if (Objects.nonNull(connectionId)) {
                    UserLoginRecordEntity entity =
                            userLoginRecordDAO.findFirstByDeskIdAndConnectionIdAndSessionStateOrderByCreateTimeDesc(deskId, connectionId, CONNECTED);
                    if (Objects.nonNull(entity)) {
                        entity.setSessionState(DISCONNECT);
                        entity.setLogoutTime(currentTime);
                        entity.setUseDuration(currentTime.getTime() - entity.getConnectTime().getTime());
                        LOGGER.info("会话断开，改为未连接:[{}]", JSON.toJSONString(entity));
                        userLoginRecordDAO.save(entity);
                    } else {
                        LOGGER.info("未找到桌面[{}]、会话id[{}]且已连接的记录", deskId, connectionId);
                    }
                }

                // X86桌面，连接全部断开，把所有已连接记录改为未连接（补偿）
                if (!isAnyConnectedChannel(deskId)) {
                    LOGGER.info("桌面[{}]无EST连接，将桌面所有已连接的会话状态改为未连接", deskId);
                    List<UserLoginRecordEntity> entityList = userLoginRecordDAO.findAllByDeskIdAndSessionState(deskId, CONNECTED);
                    entityList.forEach(entity -> {
                        entity.setSessionState(DISCONNECT);
                        entity.setLogoutTime(currentTime);
                        entity.setUseDuration(currentTime.getTime() - entity.getConnectTime().getTime());
                        LOGGER.info("将已连接会话状态改为未连接:[{}]", JSON.toJSONString(entity));
                    });
                    userLoginRecordDAO.saveAll(entityList);
                }
            }, MAX_LOCK_SECOND);
        } catch (Exception e) {
            LOGGER.error(String.format("锁异常,任务ID:[%s]桌面id:[%s]", taskId, deskId), e);
        }
    }

    @Override
    public void clear(Date specifiedDate) {
        Assert.notNull(specifiedDate, "specifiedDate can not be null");

        userLoginRecordDAO.deleteByCreateTimeLessThan(specifiedDate);
    }

    @Override
    public void addLoginCache(String terminalId, @Nullable IacUserDetailDTO userDetailDTO) {
        Assert.hasText(terminalId, "terminalId must not be null");
        if (userDetailDTO == null) {
            LOGGER.error("保存终端[{}]用户登录信息，用户不存在", terminalId);
            return;
        }
        Date loginTime = new Date();
        String userName = userDetailDTO.getUserName();
        LOGGER.info("保存终端[{}]用户[{}]登录信息，当前时间：{}", terminalId, userName, loginTime);
        try {
            UserLoginRecordDTO dto = new UserLoginRecordDTO();
            Optional.ofNullable(userName).ifPresent(dto::setUserName);
            dto.setTerminalId(terminalId);
            dto.setLoginTime(loginTime);
            dto.setAuthDuration(0L);
            dto.setUserId(userDetailDTO.getId().toString());
            LOGIN_CACHE.put(terminalId, dto);
        } catch (Exception e) {
            LOGGER.error("保存终端[{}]用户[{}]登录信息时失败", terminalId, userName, e);
        }
    }

    @Override
    public void compensateFishedRecord() {
        compensateConnectingRecord();
        compensateConnectedRecord();
    }

    @Override
    public void convertConnectingToDisconnect(String terminalId, String deskId) {
        Assert.hasText(terminalId, "terminalId must not be null");
        Assert.hasText(deskId, "deskId must not be null");
        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug("将终端[{}]桌面[{}]的会话状态从连接中修改为未连接", terminalId, deskId);
        }
        String taskId = new StringBuilder().append(LOGIN_OUT_LOCK).append(deskId).append(terminalId).toString();
        try {
            LockableExecutor.executeWithTryLock(taskId, () -> {
                List<UserLoginRecordEntity> entityList =
                        userLoginRecordDAO.findAllByTerminalIdAndDeskIdAndSessionState(terminalId, deskId, CONNECTING);
                entityList.forEach(entity -> entity.setSessionState(DISCONNECT));
                userLoginRecordDAO.saveAll(entityList);
            }, MAX_LOCK_SECOND);
        } catch (Exception e) {
            LOGGER.error(String.format("锁异常,任务ID:[%s]桌面id:[%s]终端id:[%s]", taskId, deskId, terminalId), e);
        }
    }

    @Override
    public void compensateTerminalConnectingRecord(String terminalId) {
        Assert.hasText(terminalId, "terminalId must not be null");
        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug("将终端[{}]所有连接中会话状态修改为未连接", terminalId);
        }
        String taskId = new StringBuilder().append(LOGIN_OUT_LOCK).append(terminalId).toString();
        try {
            LockableExecutor.executeWithTryLock(taskId, () -> {
                List<UserLoginRecordEntity> entityList = userLoginRecordDAO.findAllByTerminalIdAndSessionState(terminalId, CONNECTING);
                entityList.forEach(entity -> entity.setSessionState(DISCONNECT));
                userLoginRecordDAO.saveAll(entityList);
            }, MAX_LOCK_SECOND);
        } catch (Exception e) {
            LOGGER.error(String.format("锁异常,任务ID:[%s]终端id:[%s]", taskId, terminalId), e);
        }
    }

    @Override
    public UserLoginRecordDTO findLastByCreateTime(Date createTime) {
        Assert.notNull(createTime, "createTime must not null");
        UserLoginRecordEntity entity = userLoginRecordDAO.findLastByCreateTime(createTime);
        if (Objects.isNull(entity)) {
            //
            return null;
        }
        UserLoginRecordDTO dto = new UserLoginRecordDTO();
        BeanUtils.copyProperties(entity, dto);
        return dto;
    }

    @Override
    public void deleteTerminalCacheByDeskId(String deskId) {
        Assert.hasText(deskId, "deskId must not be null");
        DESK_BIND_TERMINAL_CACHE.remove(deskId);
    }

    @Override
    public void deleteRemoteAssistanceCache(String deskId) {
        Assert.hasText(deskId, "deskId must not be null");
        try {
            REMOTE_ASSISTANCE_CACHE.remove(deskId);
        } catch (Exception e) {
            LOGGER.error("根据桌面删除远程协助缓存失败", e);
        }
    }

    @Override
    public void handleDeskSessionRecordStartTime(UserSessionDTO userSessionDTO) {
        Assert.notNull(userSessionDTO, "userSessionDTO must not null");
        UUID deskId = userSessionDTO.getResourceId();
        String terminalId = userSessionDTO.getTerminalId();
        if (ResourceTypeEnum.DESK != userSessionDTO.getResourceType()) {
            return;
        }
        THREAD_EXECUTOR.execute(() -> {
            try {
                CbbDeskDTO cbbDeskDTO = cbbDeskMgmtAPI.getDeskById(deskId);
                UUID imageTemplateId = cbbDeskDTO.getImageTemplateId();
                if (Objects.nonNull(imageTemplateId)) {
                    CbbImageTemplateDTO cbbImageTemplateDTO = cbbImageTemplateMgmtAPI.getCbbImageTemplateDTO(imageTemplateId);
                    // 应用主机不用记录
                    if (ImageUsageTypeEnum.APP == cbbImageTemplateDTO.getImageUsage()) {
                        return;
                    }
                }
                LOGGER.info("保存开始连接时间，桌面id: {}，桌面名称：{}，终端id ：{}，", deskId, cbbDeskDTO.getName(), terminalId);
                LockableExecutor.executeWithTryLock(LOGIN_OUT_LOCK + deskId, () -> {
                    saveStartTime(userSessionDTO, cbbDeskDTO.getName());
                }, MAX_LOCK_SECOND);
            } catch (Exception e) {
                LOGGER.error("保存开始连接时间，桌面id: {}，终端id ：{}，出现异常:", deskId, terminalId, e);
            }
        });
    }

    private void saveStartTime(UserSessionDTO userSessionDTO, String deskName) {
        String terminalId = userSessionDTO.getTerminalId();
        String deskId = userSessionDTO.getResourceId().toString();
        UserLoginRecordEntity userLoginRecordEntity = userLoginRecordDAO.findFirstByTerminalIdOrderByCreateTimeDesc(terminalId);

        if (Objects.isNull(userLoginRecordEntity)) {
            LOGGER.warn("保存开始连接时间失败，原因：未查找到终端[{}]的登录记录", terminalId);
            return;
        }

        // 终端最新记录必须用户已登录
        if (Objects.isNull(userLoginRecordEntity.getAuthDuration()) ||
                Objects.equals(UNLOGIN_AUTH_DURATION, userLoginRecordEntity.getAuthDuration())) {
            LOGGER.warn("保存开始连接时间失败，原因：终端[{}]最新登录记录缺少认证耗时。登录记录[{}]", terminalId, JSON.toJSONString(userLoginRecordEntity));
            return;
        }

        // 防止收到EST连接成功消息时，桌面未完成终端绑定，先添加到缓存，桌面完成终端绑定后删除
        DESK_BIND_TERMINAL_CACHE.put(deskId, terminalId);

        if (Objects.isNull(userLoginRecordEntity.getConnectTime())) {
            userLoginRecordEntity.setSessionState(CONNECTING);
            userLoginRecordEntity.setDeskId(deskId);
            userLoginRecordEntity.setDeskName(deskName);
            userLoginRecordEntity.setConnectTime(new Date(System.currentTimeMillis()));
            userLoginRecordDAO.save(userLoginRecordEntity);
            return;
        }

        UserLoginRecordEntity loginRecordEntity = userLoginRecordDAO.findFirstByTerminalIdAndDeskIdOrderByCreateTimeDesc(terminalId, deskId);
        if (Objects.nonNull(loginRecordEntity) && loginRecordEntity.getLoginTime().compareTo(userLoginRecordEntity.getLoginTime()) == 0) {
            // 终端用户保持登录，重新登录桌面时，如果桌面在连接中，则刷新建立连接时间
            if (Objects.equals(CONNECTING, loginRecordEntity.getSessionState())) {
                LOGGER.info("桌面[{}]已在终端[{}]建立连接，且状态为连接中，刷新连接时间。终端最新连接记录[{}]，终端最新桌面连接记录[{}]",
                        deskId, terminalId, JSON.toJSONString(userLoginRecordEntity), JSON.toJSONString(loginRecordEntity));
                loginRecordEntity.setConnectTime(new Date(System.currentTimeMillis()));
                loginRecordEntity.setUseDuration(0L);
                loginRecordEntity.setLogoutTime(null);
                userLoginRecordDAO.save(loginRecordEntity);
                return;
            } else if (Objects.equals(CONNECTED, loginRecordEntity.getSessionState())) {
                return;
            }
        }

        // 多开情况下添加其他桌面信息
        loginRecordEntity = new UserLoginRecordEntity();
        loginRecordEntity.setCreateTime(new Date());
        loginRecordEntity.setUserId(userLoginRecordEntity.getUserId());
        loginRecordEntity.setUserName(userLoginRecordEntity.getUserName());
        loginRecordEntity.setUserType(userLoginRecordEntity.getUserType());
        loginRecordEntity.setUserGroupId(userLoginRecordEntity.getUserGroupId());
        loginRecordEntity.setUserGroupName(userLoginRecordEntity.getUserGroupName());
        loginRecordEntity.setTerminalId(terminalId);
        loginRecordEntity.setTerminalIp(userLoginRecordEntity.getTerminalIp());
        loginRecordEntity.setTerminalMac(userLoginRecordEntity.getTerminalMac());
        loginRecordEntity.setTerminalName(userLoginRecordEntity.getTerminalName());
        loginRecordEntity.setDeskType("");
        loginRecordEntity.setComputerName("");
        loginRecordEntity.setDeskIp("");
        loginRecordEntity.setDeskMac("");
        loginRecordEntity.setDeskSystem("");
        loginRecordEntity.setDeskImage("");
        loginRecordEntity.setDeskStrategy("");
        loginRecordEntity.setDeskStrategyPattern("");
        loginRecordEntity.setUseDuration(0L);
        loginRecordEntity.setConnectDuration(0L);
        loginRecordEntity.setLoginTime(userLoginRecordEntity.getLoginTime());
        loginRecordEntity.setAuthDuration(userLoginRecordEntity.getAuthDuration());
        loginRecordEntity.setSessionState(CONNECTING);
        loginRecordEntity.setDeskId(deskId);
        loginRecordEntity.setDeskName(deskName);
        loginRecordEntity.setConnectTime(new Date(System.currentTimeMillis()));
        userLoginRecordDAO.save(loginRecordEntity);
    }

    @Override
    public void handleDeskSessionRecordEndTime(UUID userId, String terminalId, List<UserSessionDTO> sessionInfoList) {
        Assert.notNull(userId, "userId must not null");
        Assert.hasText(terminalId, "terminalId must has text");
        Assert.notNull(sessionInfoList, "sessionInfoList must not null");
        THREAD_EXECUTOR.execute(() -> {
            try {
                LockableExecutor.executeWithTryLock(LOGIN_OUT_LOCK + terminalId, () -> {
                    // 只处理桌面
                    List<UserSessionDTO> deskUserSessionList =
                            sessionInfoList.stream().filter(item -> item.getResourceType() == ResourceTypeEnum.DESK
                                    && Objects.nonNull(item.getResourceId())).collect(Collectors.toList());
                    for (UserSessionDTO userSessionDTO : deskUserSessionList) {
                        String deskId = userSessionDTO.getResourceId().toString();
                        UserLoginRecordEntity userLoginRecordEntity =
                                userLoginRecordDAO.findFirstByTerminalIdAndDeskIdOrderByCreateTimeDesc(terminalId, deskId);
                        if (Objects.isNull(userLoginRecordEntity)) {
                            LOGGER.warn("保存成功连接时间失败，原因：未查找到终端[{}]桌面[{}]的登录记录", terminalId, deskId);
                            continue;
                        }
                        // 连接中变为已连接
                        if (Objects.equals(CONNECTING, userLoginRecordEntity.getSessionState())) {
                            LOGGER.info("客户端上报连接桌面成功，桌面id: {}, 终端id：{}", deskId, terminalId);
                            saveConnectSuccessInfo(deskId, userLoginRecordEntity);
                        }
                    }

                    // 已连接变为未连接
                    List<UserLoginRecordEntity> recordEntityList = userLoginRecordDAO.findAllByTerminalIdAndSessionState(terminalId,
                            CONNECTED);
                    if (CollectionUtils.isEmpty(recordEntityList)) {
                        return;
                    }
                    List<UserLoginRecordEntity> updateList = new ArrayList<>();
                    List<String> deskIdList = deskUserSessionList.stream().map(userSessionDTO -> userSessionDTO.getResourceId().toString())
                            .collect(Collectors.toList());
                    Date currentTime = new Date();
                    for (UserLoginRecordEntity entity : recordEntityList) {
                        if (deskIdList.contains(entity.getDeskId())) {
                            continue;
                        }
                        entity.setSessionState(DISCONNECT);
                        entity.setLogoutTime(currentTime);
                        entity.setUseDuration(currentTime.getTime() - entity.getConnectTime().getTime());
                        updateList.add(entity);
                        LOGGER.info("客户端上报桌面连接关闭，桌面id: {}, 终端id：{}", entity.getDeskId(), terminalId);
                    }
                    if (CollectionUtils.isEmpty(updateList)) {
                        return;
                    }
                    userLoginRecordDAO.saveAll(recordEntityList);
                }, MAX_LOCK_SECOND);
            } catch (Exception e) {
                LOGGER.error("保存变更连接时间，用户id: {}，终端id ：{}，出现异常:", userId, terminalId, e);
            }
        });
    }

    private void compensateConnectingRecord() {
        // 补偿用户信息报表内会话状态为【连接中】的记录
        List<UserLoginRecordEntity> entityList = userLoginRecordDAO.findAllBySessionState(CONNECTING);
        LOGGER.info("共[{}]条会话状态为[CONNECTING]的用户报表信息需要补偿", entityList.size());
        entityList.forEach(entity -> {
            entity.setSessionState(DISCONNECT);
        });
        userLoginRecordDAO.saveAll(entityList);
    }

    private void compensateConnectedRecord() {
        // 补偿用户信息报表内会话状态为【已连接】的记录
        List<UserLoginRecordEntity> entityList = userLoginRecordDAO.findAllBySessionState(CONNECTED);
        LOGGER.info("共[{}]条会话状态为[CONNECTED]的用户报表信息需要确认是否需要补偿", entityList.size());
        for (UserLoginRecordEntity entity : entityList) {
            if (needCompensate(UUID.fromString(entity.getDeskId()), entity.getConnectionId())) {
                saveLogoutTime(entity.getDeskId(), entity.getConnectionId());
            }
        }
    }

    private Boolean needCompensate(UUID desktopId, Long connectionId) {

        try {
            CbbCloudDeskState cbbCloudDeskState = cbbVDIDeskMgmtAPI.queryDeskState(desktopId);
            // 先判断桌面是否正在运行，如果不在运行中，则证明会话已经结束
            if (cbbCloudDeskState != CbbCloudDeskState.RUNNING) {
                LOGGER.info("桌面[{}]状态为[{}]，结束会话[{}]", desktopId, cbbCloudDeskState, connectionId);
                return Boolean.TRUE;
            }

            if (!isAnyConnectedChannel(desktopId.toString())) {
                LOGGER.info("桌面[{}]会话列表中没有会话[{}]，结束会话", desktopId, connectionId);
                return Boolean.TRUE;
            }
        } catch (BusinessException e) {
            LOGGER.error("校验桌面[{}]是否需要补偿失败", desktopId, e);
            return Boolean.TRUE;
        }
        return Boolean.FALSE;
    }

    private void saveUserLoginRecord(UserLoginRecordDTO userLoginRecordDTO) throws BusinessException {
        String userName = userLoginRecordDTO.getUserName();
        String terminalId = userLoginRecordDTO.getTerminalId();
        IacUserDetailDTO userDetailDTO = cbbUserAPI.getUserByName(userName);
        if (Objects.isNull(userDetailDTO)) {
            LOGGER.error("保存用户[{}]登录信息时，用户不存在", userName);
            return;
        }
        CbbTerminalBasicInfoDTO terminalBasicInfoDTO = cbbTerminalOperatorAPI.findBasicInfoByTerminalId(terminalId);
        IacUserGroupDetailDTO userGroupDetail = cbbUserGroupAPI.getUserGroupDetail(userDetailDTO.getGroupId());

        UserLoginRecordEntity userLoginRecordEntity = new UserLoginRecordEntity();
        userLoginRecordEntity.setUserId(userDetailDTO.getId().toString());
        userLoginRecordEntity.setLoginTime(userLoginRecordDTO.getLoginTime());
        userLoginRecordEntity.setTerminalId(terminalId);
        userLoginRecordEntity.setAuthDuration(userLoginRecordDTO.getAuthDuration());

        userLoginRecordEntity.setUserName(userDetailDTO.getUserName());
        userLoginRecordEntity.setUserGroupId(userDetailDTO.getGroupId().toString());
        userLoginRecordEntity.setUserType(userDetailDTO.getUserType().toString());
        userLoginRecordEntity.setUserGroupName(userGroupDetail != null ? userGroupDetail.getName() : "");

        userLoginRecordEntity.setTerminalIp(terminalBasicInfoDTO.getIp());
        userLoginRecordEntity.setTerminalMac(terminalBasicInfoDTO.getMacAddr());
        userLoginRecordEntity.setTerminalName(terminalBasicInfoDTO.getTerminalName());

        userLoginRecordEntity.setDeskType("");
        userLoginRecordEntity.setDeskStrategyPattern("");
        userLoginRecordEntity.setDeskId("");
        userLoginRecordEntity.setDeskName("");
        userLoginRecordEntity.setComputerName("");
        userLoginRecordEntity.setDeskIp("");
        userLoginRecordEntity.setDeskMac("");
        userLoginRecordEntity.setDeskSystem("");
        userLoginRecordEntity.setDeskImage("");
        userLoginRecordEntity.setDeskStrategy("");
        userLoginRecordEntity.setSessionState(DISCONNECT);
        userLoginRecordEntity.setCreateTime(new Date());
        userLoginRecordEntity.setConnectDuration(0L);
        userLoginRecordEntity.setUseDuration(0L);
        userLoginRecordDAO.save(userLoginRecordEntity);
        LOGIN_CACHE.remove(terminalId);
    }

    private void saveConnectSuccessInfo(String deskId, Long connectionId) {

        String terminalId = DESK_BIND_TERMINAL_CACHE.get(deskId);
        if (StringUtils.isBlank(terminalId)) {
            LOGGER.info("桌面{}会话{}绑定终端缓存不存在，从数据库查找绑定终端", deskId, connectionId);
            try {
                RcoUserDesktopDTO rcoUserDesktopDTO = userDesktopMgmtAPI.findByDesktopId(UUID.fromString(deskId));
                terminalId = rcoUserDesktopDTO.getTerminalId();
            } catch (BusinessException businessException) {
                LOGGER.error("查找桌面{}信息失败", deskId, businessException);
            }
        }

        LOGGER.info("保存EST成功连接虚拟机时间，桌面id: {}, 终端id：{}, 会话id：{}", deskId, terminalId, connectionId);

        // 如果存在远程连接，将第一个收到的会话作为远程连接会话
        if (isRemote(UUID.fromString(deskId)) && !REMOTE_ASSISTANCE_CACHE.containsKey(deskId)) {
            LOGGER.info("桌面[{}]存在远程连接，会话id[{}]", deskId, connectionId);
            REMOTE_ASSISTANCE_CACHE.put(deskId, connectionId);
            return;
        }

        if (StringUtils.isBlank(terminalId)) {
            LOGGER.info("桌面[{}]产生会话[{}]时终端id为空，不保存成功连接虚拟机时间", deskId, connectionId);
            return;
        }

        UserLoginRecordEntity userLoginRecordEntity =
                userLoginRecordDAO.findFirstByTerminalIdAndDeskIdOrderByCreateTimeDesc(terminalId, deskId);

        if (Objects.isNull(userLoginRecordEntity)) {
            LOGGER.warn("保存成功连接虚拟机时间失败，原因：未查找到终端[{}]桌面[{}]的登录记录", terminalId, deskId);
            return;
        }
        userLoginRecordEntity.setConnectionId(connectionId);
        saveConnectSuccessInfo(deskId, userLoginRecordEntity);
    }

    private void saveConnectSuccessInfo(String deskId, UserLoginRecordEntity userLoginRecordEntity) {
        CbbDeskDTO cbbDeskDTO;
        CbbDeskStrategyDTO cbbDeskStrategyDTO;
        CbbImageTemplateDTO cbbImageTemplateDTO = null;
        try {
            cbbDeskDTO = cbbDeskMgmtAPI.getDeskById(UUID.fromString(deskId));
            cbbDeskStrategyDTO = cbbIDVDeskStrategyMgmtAPI.findByIds(Collections.singletonList(cbbDeskDTO.getStrategyId())).get(0);
            if (CbbCloudDeskType.THIRD != cbbDeskDTO.getDeskType()) {
                cbbImageTemplateDTO = cbbImageTemplateMgmtAPI.getCbbImageTemplateDTO(cbbDeskDTO.getImageTemplateId());
            }
        } catch (BusinessException e) {
            LOGGER.warn("终端[{}]登录的桌面[{}]保存成功连接虚拟机时间失败，原因：{}", userLoginRecordEntity.getTerminalId(), deskId, e.getI18nMessage(), e);
            return;
        }

        userLoginRecordEntity.setDeskType(cbbDeskDTO.getDeskType().toString());
        userLoginRecordEntity.setComputerName(cbbDeskDTO.getComputerName());
        String deskIp = cbbDeskDTO.getDeskIp();
        userLoginRecordEntity.setDeskIp(Objects.isNull(deskIp) ? "" : deskIp);
        userLoginRecordEntity.setDeskMac(cbbDeskDTO.getDeskMac());
        userLoginRecordEntity.setDeskStrategy(cbbDeskStrategyDTO.getName());
        // 第三方使用桌面表的osType字段,策略Pattern字段为null
        if (CbbCloudDeskType.THIRD == cbbDeskDTO.getDeskType()) {
            if (Objects.nonNull(cbbDeskDTO.getOsType())) {
                userLoginRecordEntity.setDeskSystem(cbbDeskDTO.getOsType().toString());
            }
        } else {
            if (Objects.nonNull(cbbImageTemplateDTO)) {
                userLoginRecordEntity.setDeskImage(cbbImageTemplateDTO.getImageTemplateName());
                userLoginRecordEntity.setDeskSystem(cbbImageTemplateDTO.getOsType().toString());
            }
            userLoginRecordEntity.setDeskStrategyPattern(cbbDeskStrategyDTO.getPattern().toString());
        }
        userLoginRecordEntity.setSessionState(CONNECTED);
        userLoginRecordEntity.setConnectDuration(new Date().getTime() - userLoginRecordEntity.getConnectTime().getTime());
        userLoginRecordEntity.setUseDuration(0L);
        userLoginRecordEntity.setLogoutTime(null);
        userLoginRecordDAO.save(userLoginRecordEntity);
        LOGGER.info("保存连接成功记录[{}]", JSON.toJSONString(userLoginRecordEntity));
    }

    private Boolean isRemote(UUID deskId) {
        RemoteAssistInfo info = remoteAssistMgmtService.queryRemoteAssistInfo(deskId);
        if (Objects.nonNull(info)) {
            return info.getState() == RemoteAssistState.AGREE || info.getState() == RemoteAssistState.IN_REMOTE_ASSIST;
        }
        return Boolean.FALSE;
    }

    private Boolean isAnyConnectedChannel(String deskId) {
        try {
            Boolean isAnyConnectedChannel = desktopAPI.isAnyConnectedChannel(UUID.fromString(deskId));
            return isAnyConnectedChannel;
        } catch (Exception e) {
            LOGGER.error("查询桌面[{}]是否还有EST会话失败", deskId, e);
            return Boolean.FALSE;
        }
    }
}
