package com.ruijie.rcos.rcdc.rco.module.impl.service.impl;

import com.google.common.collect.Interner;
import com.google.common.collect.Interners;
import com.google.common.collect.Maps;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.api.CbbImageTemplateMgmtAPI;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.dto.CbbDeskRemoteAssistResultDTO;
import com.ruijie.rcos.rcdc.rco.module.def.api.ComputerDeskRemoteAssistAPI;
import com.ruijie.rcos.rcdc.rco.module.def.api.RemoteAssistInfoOperateAPI;
import com.ruijie.rcos.rcdc.rco.module.def.api.dto.AssistantRemoteResponseDTO;
import com.ruijie.rcos.rcdc.rco.module.def.api.dto.RemoteAssistInfoDTO;
import com.ruijie.rcos.rcdc.rco.module.def.api.enums.ComputerStateEnum;
import com.ruijie.rcos.rcdc.rco.module.def.api.enums.RemoteAssistState;
import com.ruijie.rcos.rcdc.rco.module.def.api.request.*;
import com.ruijie.rcos.rcdc.rco.module.def.api.response.AssistantRemoteResponse;
import com.ruijie.rcos.rcdc.rco.module.def.api.response.RemoteAssistInfoResponse;
import com.ruijie.rcos.rcdc.rco.module.impl.BusinessKey;
import com.ruijie.rcos.rcdc.rco.module.impl.dto.computer.ComputerAssistDTO;
import com.ruijie.rcos.rcdc.rco.module.impl.entity.ComputerEntity;
import com.ruijie.rcos.rcdc.rco.module.impl.service.ComputerBusinessService;
import com.ruijie.rcos.rcdc.rco.module.impl.service.ComputerRemoteAssistMgmtService;
import com.ruijie.rcos.rcdc.rco.module.impl.service.EstClientService;
import com.ruijie.rcos.rcdc.rco.module.impl.service.struct.RemoteAssistInfo;
import com.ruijie.rcos.rcdc.rco.module.impl.util.BeanUtil;
import com.ruijie.rcos.sk.base.exception.BusinessException;
import com.ruijie.rcos.sk.base.util.StringUtils;
import com.ruijie.rcos.sk.modulekit.api.comm.IdRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import java.util.Map;
import java.util.UUID;

/**
 * Description: pc远程协助管理器实现
 * Copyright: Copyright (c) 2018 Company: Ruijie Co.,
 * Ltd. Create Time: 2020/2/22
 *
 * @author ketb
 */
@Service
public class ComputerRemoteAssistMgmtServiceImpl implements ComputerRemoteAssistMgmtService {

    private static final Logger LOGGER = LoggerFactory.getLogger(ComputerRemoteAssistMgmtServiceImpl.class);

    @Autowired
    private ComputerDeskRemoteAssistAPI remoteAssistService;

    @Autowired
    private ComputerBusinessService computerService;

    @Autowired
    private RemoteAssistInfoOperateAPI remoteAssistInfoOperateAPI;

    @Autowired
    private EstClientService estClientService;

    @Autowired
    private CbbImageTemplateMgmtAPI cbbImageTemplateMgmtAPI;

    /**
     * 用户接受远程协助
     */
    private static final int USER_AGREE = 0;

    /**
     * 用户拒绝远程协助
     */
    private static final int USER_REJECT = 1;

    /**
     * 环境未就绪
     */
    private static final int VNC_UNREADY = 2;

    /**
     * 连接已存在
     */
    private static final int EXIST_CONNECT = 3;

    /**
     * 连接超时
     */
    private static final int TIMEOUT = 4;

    /**
     * 远程协助请求响应成功
     */
    private static final int INQUIRE_RESULT_SUCCESS = 0;

    private final Interner<UUID> desktopIdInterner = Interners.newWeakInterner();

    /**
     * 记录远程 PC 是否完全结束
     * true 已结束，false 未结束
     */
    private Map<UUID, Boolean> hasComputerRemoteAssistEnd = Maps.newConcurrentMap();

    @Override
    public void assistInquire(UUID deskId, UUID adminId, String adminName) throws BusinessException {
        Assert.notNull(deskId, "deskId must not be null.");
        Assert.notNull(adminId, "adminId must not be null.");
        Assert.hasText(adminName, "adminName can not be blank.");

        RemoteAssistInfo info = getRemoteAssistInfo(deskId);
        if (info == null) {
            long vncCount = remoteAssistInfoOperateAPI.remoteAssistNum() + cbbImageTemplateMgmtAPI.getVncEditingImageNum();
            int vncLimit = estClientService.estClientLimit();
            LOGGER.info("deskId：{} vncCount：{} vncLimit：{}", deskId, vncCount, vncLimit);
            if (vncCount + 1 > vncLimit) {
                throw new BusinessException(BusinessKey.RCDC_USER_REMOTE_ASSIST_VM_LIMIT);
            }
        }

        if (needCreateNewRemoteAssist(info, adminId)) {
            needCreateNewRemoteAssistHandle(deskId, adminId, adminName);
        } else {
            noNeedCreateNewRemoteAssistHandle(deskId, adminId);
        }
        // 发起远程协助的桌面不是当前正在协助的桌面，需要结束当前正在协助的桌面
        remoteAssistInfoOperateAPI.remoteAssistOtherDeskHandle(new RemoteAssistOtherDeskHandleRequest(deskId, adminId));
    }

    private void needCreateNewRemoteAssistHandle(UUID deskId, UUID adminId, String adminName) throws BusinessException {
        LOGGER.info("请求远程协助，创建新的协助请求，当前云桌面为[{}]，管理员为[{}]", deskId, adminId);
        ComputerStateEnum state = computerService.getComputerState(deskId);
        if (state == ComputerStateEnum.OFFLINE) {
            throw new BusinessException(BusinessKey.RCDC_RCO_COMPUTER_OFFLINE_NOT_ALLOW_REMOTE_ASSIST);
        }
        synchronized (desktopIdInterner.intern(deskId)) {
            createDeskRemoteAssist(deskId, adminId, adminName);
        }
    }

    private void createDeskRemoteAssist(UUID deskId, UUID adminId, String adminName) throws BusinessException {
        if (!isNeedCreateDeskRemoteAssist(deskId, adminId, adminName)) {
            LOGGER.info("远程协助已经存在，不需要重新创建。");
            return;
        }
        ComputerEntity entity = computerService.getComputerById(deskId);
        RemoteAssistInfoDTO newAssistInfo = new RemoteAssistInfoDTO(deskId, adminId, adminName);
        RemoteAssistInfo oldAssistInfo = getRemoteAssistInfo(deskId);
        if (oldAssistInfo != null) {
            newAssistInfo.setOldState(oldAssistInfo.getState());
        }

        if (StringUtils.isNotEmpty(entity.getAlias())) {
            newAssistInfo.setDesktopName(entity.getAlias());
        } else {
            newAssistInfo.setDesktopName(entity.getName());
        }
        addInfoMap(deskId, newAssistInfo);
        try {
            ComputerRemoteAssistRequest req = new ComputerRemoteAssistRequest();
            req.setId(deskId);
            req.setAutoAgree(entity.getFaultState());
            callRemoteAssistDesk(deskId, req);
        } catch (BusinessException e) {
            LOGGER.error("远程协助出现异常，发起协助的管理员名称：" + adminName + "，管理员id：" + adminId
                + "，协助的桌面名称：" + entity.getName() + "，桌面id：" + deskId, e);
            removeInfoMap(deskId);
            throw e;
        }
    }

    private boolean isNeedCreateDeskRemoteAssist(UUID deskId, UUID adminId, String adminName) throws BusinessException {
        RemoteAssistInfo assistInfo = getRemoteAssistInfo(deskId);
        if (needCreateNewRemoteAssist(assistInfo, adminId)) {
            return true;
        } else {
            LOGGER.info("PC终端远程协助信息已存在，云桌面id为[{}]，管理员id为[{}]，管理员名称为[{}]，协助状态为[{}]",
                deskId, adminId, adminName, assistInfo.getState().name());
            validateWhetherOtherAdminAssistDesk(adminId, assistInfo);
            return false;
        }
    }

    private boolean needCreateNewRemoteAssist(RemoteAssistInfo info, UUID adminId) {
        if (info == null || info.isFinish() || (info.getState() == RemoteAssistState.WAITING && adminId.equals(info.getAdminId()))) {
            return true;
        } else if (!getRemoteAssistCloseStatus(info.getDeskId())) {
            return false;
        } else {
            return false;
        }
    }

    private void callRemoteAssistDesk(UUID deskId, ComputerRemoteAssistRequest req) throws BusinessException {
        try {
            AssistantRemoteResponse response = remoteAssistService.remoteAssistDesk(req);
            validateRemoteAssistInquireResponse(deskId, response.getResponseDTO());
        } catch (Exception e) {
            LOGGER.error("remoteAssist inquire has exception", e);
            throw new BusinessException(BusinessKey.RCDC_RCO_REMOTE_ASSIST_INQUIRE_FAIL, e);
        }
    }

    private void validateRemoteAssistInquireResponse(UUID deskId, AssistantRemoteResponseDTO response) {
        if (response.getCode() != INQUIRE_RESULT_SUCCESS) {
            throw new RuntimeException(
                "remoteAssist inquire response code is not agree, code is : " + response.getCode());
        }
        if (!deskId.equals(response.getDeskId())) {
            throw new RuntimeException(
                "remoteAssist inquire response deskId is illegal, deskId is : " + response.getDeskId());
        }
    }

    private void noNeedCreateNewRemoteAssistHandle(UUID deskId, UUID adminId) throws BusinessException {
        LOGGER.info("请求远程协助，不创建新的协助请求，当前PC终端为[{}]，管理员为[{}]", deskId, adminId);
        RemoteAssistInfo assistInfo = getRemoteAssistInfo(deskId);
        if (assistInfo == null) {
            LOGGER.error("远程协助信息为空，PC终端id为[{}]", deskId);
            throw new BusinessException(BusinessKey.RCDC_RCO_REMOTE_ASSIST_QUERY_NO_INFO);
        }
        validateWhetherOtherAdminAssistDesk(adminId, assistInfo);
    }

    private void validateWhetherOtherAdminAssistDesk(UUID adminId, RemoteAssistInfo assistInfo)
        throws BusinessException {
        // 其他管理员正在进行远程协助操作
        if (!adminId.equals(assistInfo.getAdminId())) {
            LOGGER.error("其他管理员正在进行远程协助，管理员id[{}]，管理员名称[{}]，桌面名称[{}]。当前请求管理员id = {}",
                assistInfo.getAdminId(), assistInfo.getAdminName(), assistInfo.getDesktopName(), adminId);
            throw new BusinessException(BusinessKey.RCDC_RCO_REMOTE_ASSIST_BY_OTHER_ADMIN, assistInfo.getAdminName());
        }
        // 远程协助处于等待状态时又收到协助请求，远程进入等待
        if (assistInfo.getState() == RemoteAssistState.WAITING) {
            LOGGER.info("远程协助处于等待状态时又收到协助请求。adminId = {}, adminName = {}，desktopName = {}, state = {}",
                adminId, assistInfo.getAdminName(), assistInfo.getDesktopName(), assistInfo.getState());
            return;
        }

        // 远程协助处于用户同意协助状态时又收到协助请求，远程进入等待
        if (assistInfo.getState() == RemoteAssistState.AGREE) {
            LOGGER.info("远程协助处于等待状态时又收到协助请求。adminId = {}, adminName = {}，desktopName = {}, state = {}",
                adminId, assistInfo.getAdminName(), assistInfo.getDesktopName(), assistInfo.getState());
            return;
        }
        // 当前PC远程协助正在进行
        throw new BusinessException(BusinessKey.RCDC_RCO_REMOTE_ASSIST_IS_USED);
    }

    @Override
    public RemoteAssistInfo queryAssisInfo(UUID deskId, UUID adminId) throws BusinessException {
        Assert.notNull(deskId, "deskId must not be null.");
        Assert.notNull(adminId, "adminId must not be null.");

        RemoteAssistInfo assist = getRemoteAssistInfo(deskId);
        if (assist == null) {
            throw new BusinessException(BusinessKey.RCDC_RCO_REMOTE_ASSIST_QUERY_NO_INFO);
        }
        if (assist.getAdminId().equals(adminId)) {
            if (assist.getState() == RemoteAssistState.IN_REMOTE_ASSIST
                || assist.getState() == RemoteAssistState.AGREE) {
                // 正在协助中，更新时间戳
                assist.updateStamp();
            }
            LOGGER.info("queryAssistInfo state is [{}], deskId is [{}]", assist.getState().name(), deskId);
            updateInfoMapForAssistInfo(deskId, assist);
            return assist;
        } else {
            RemoteAssistInfo busyInfo = new RemoteAssistInfo(assist.getDeskId(), assist.getAdminId());
            BeanUtils.copyProperties(assist, busyInfo);
            busyInfo.setState(assist.getOldState());
            return busyInfo;
        }
    }

    @Override
    public void notifyAssistResult(UUID deskId, int userOperateType) throws BusinessException {
        Assert.notNull(deskId, "deskId must not be null.");
        RemoteAssistInfo info = getRemoteAssistInfo(deskId);
        if (info == null) {
            LOGGER.error("can not find info from map. deskId is [{}]", deskId);
            sendCancelRemoteAssist(deskId);
            return;
        }
        synchronized (info) {
            if (shouldHandleNotify(deskId, info)) {
                userOperateResultHandle(userOperateType, info);
                return;
            }
        }
        changeAssistState(deskId, RemoteAssistState.FINISH);
        // 收到用户同意远程协助消息时，管理员已经不再是等待接受状态了，需要通知guesttool取消协助
        sendCancelRemoteAssist(deskId);
    }

    private boolean shouldHandleNotify(UUID deskId, RemoteAssistInfo info) {
        LOGGER.info("remoteAssistInfo state is [{}]", info.getState().name());
        if (info.getState() != null && info.getState() != RemoteAssistState.WAITING) {
            LOGGER.warn("remote state is not watting, can not remote assist. deskId is [{}], remote state is [{}]",
                deskId, info.getState());
            return false;
        }
        return true;
    }

    private void userOperateResultHandle(int userOperateType, RemoteAssistInfo info) throws BusinessException {
        if (userOperateType == USER_AGREE) {
            obtainAssistInfo(info);
        } else if (userOperateType == USER_REJECT) {
            updateInfoMapForAssistInfo(info.getDeskId(), info);
            changeAssistState(info.getDeskId(), RemoteAssistState.REJECT);
        } else if (userOperateType == VNC_UNREADY) {
            removeInfoMap(info.getDeskId());
            throw new RuntimeException(BusinessKey.RCDC_RCO_COMPUTER_VNC_CONNECT_UNREADY);
        } else if (userOperateType == EXIST_CONNECT) {
            LOGGER.info("连接已存在，不做处理。adminName = {}, deskId = {}", info.getAdminName(), info.getDeskId());
        } else if (userOperateType == TIMEOUT) {
            changeAssistState(info.getDeskId(), RemoteAssistState.STOP_TIMEOUT);
            resetVncHeartBeat(info.getDeskId());
        } else {
            updateInfoMapForAssistInfo(info.getDeskId(), info);
            changeAssistState(info.getDeskId(), RemoteAssistState.DATA_ILLEGAL);
            throw new RuntimeException(
                "remoteAssist notify result data is illegal, userOperateType is : " + userOperateType);
        }
    }

    private void obtainAssistInfo(RemoteAssistInfo info) throws BusinessException {
        UUID deskId = info.getDeskId();
        try {
            ObtainDeskRemoteAssistConnectionInfoRequest cbbReq = new ObtainDeskRemoteAssistConnectionInfoRequest();
            cbbReq.setId(deskId);
            CbbDeskRemoteAssistResultDTO cbbResp = remoteAssistService.obtainDeskRemoteAssistInfo(cbbReq);
            info.setAssistIp(cbbResp.getIp());
            info.setAssistPort(cbbResp.getPort());
            info.setAssistToken(cbbResp.getToken());
            info.setPassword(cbbResp.getPwd());
            updateInfoMapForAssistInfo(deskId, info);
            changeAssistState(deskId, RemoteAssistState.AGREE);
        } catch (BusinessException e) {
            updateInfoMapForAssistInfo(deskId, info);
            changeAssistState(deskId, RemoteAssistState.START_FAIL);
            LOGGER.error("obtainAssistInfo fail. deskId is : " + deskId, e);
            throw new BusinessException(BusinessKey.RCDC_RCO_REMOTE_ASSIST_OBTAIN_CONNECT_INFO_FAIL, e);
        }
    }

    @Override
    public void userCloseAssist(UUID deskId) {
        Assert.notNull(deskId, "deskId must not be null.");
        RemoteAssistInfo info = getRemoteAssistInfo(deskId);
        if (info == null) {
            LOGGER.error("can not find info from map. May be admin");
        } else {
            LOGGER.info("用户发送了关闭远程协助的消息, deskId[{}]", deskId);
            changeAssistState(deskId, RemoteAssistState.STOP_USER);
            resetVncHeartBeat(deskId);
        }
    }

    @Override
    public void updateRemoteAssistState(UUID deskId, RemoteAssistState state) throws BusinessException {
        Assert.notNull(deskId, "deskId must not be null.");
        Assert.notNull(state, "state must not be null.");
        RemoteAssistInfo info = getRemoteAssistInfo(deskId);
        if (info == null) {
            LOGGER.error("can not find info from map. May be admin");
            throw new BusinessException(BusinessKey.RCDC_RCO_COMPUTER_GET_REMOTE_ASSIST_IS_NULL);
        } else {
            LOGGER.info("小助手更新远程协助的状态, deskId = {}， state = {}", deskId, state.toString());
            changeAssistState(deskId, state);
            resetVncHeartBeat(deskId);
        }
    }

    @Override
    public void userAssistStartFail(UUID deskId) {
        Assert.notNull(deskId, "deskId must not be null.");
        RemoteAssistInfo info = getRemoteAssistInfo(deskId);
        if (info == null) {
            LOGGER.error("can not find info from map. May be admin");
        } else {
            changeAssistState(deskId, RemoteAssistState.START_FAIL);
        }
    }

    @Override
    public void adminCancelAssist(UUID deskId, UUID adminId) throws BusinessException {
        Assert.notNull(deskId, "deskId must not be null.");
        Assert.notNull(adminId, "adminId must not be null.");
        RemoteAssistInfo info = getRemoteAssistInfo(deskId);
        if (info == null) {
            LOGGER.error("can not find info from map. deskId is [{}]", deskId);
            return;
        }
        if (!adminId.equals(info.getAdminId())) {
            LOGGER.warn("other admin is assist. cancel is forbid. deskId is [{}]", deskId);
            return;
        }
        changeAssistState(deskId, RemoteAssistState.STOP_ADMIN);
        LOGGER.info("send cancel remote assist<{}>", deskId);
        sendCancelRemoteAssist(deskId);
    }

    @Override
    public void computerUserResponseExpiredTime(UUID deskId) throws BusinessException {
        Assert.notNull(deskId, "deskId must not be null.");
        RemoteAssistInfo info = getRemoteAssistInfo(deskId);
        if (info == null) {
            LOGGER.error("can not find info from map. deskId is [{}]", deskId);
            throw new BusinessException(BusinessKey.RCDC_RCO_COMPUTER_GET_REMOTE_ASSIST_IS_NULL);
        }
        changeAssistState(deskId, RemoteAssistState.STOP_TIMEOUT);
        LOGGER.info("send cancel remote assist<{}>", deskId);
        sendCancelRemoteAssist(deskId);
    }

    @Override
    public void createVncChannelResult(UUID deskId, UUID adminId) throws BusinessException {
        Assert.notNull(deskId, "deskId must not be null");
        Assert.notNull(adminId, "adminId must not be null");
        RemoteAssistInfo info = getRemoteAssistInfo(deskId);
        if (info == null) {
            LOGGER.error("can not find info from map. deskId is [{}]", deskId);
            return;
        }
        try {
            remoteAssistService.createVncChannelResult(deskId);
            afterCreateVncChannelHandle(deskId);
        } catch (BusinessException e) {
            changeAssistState(deskId, RemoteAssistState.RESPONSE_TIMEOUT);
            LOGGER.error("createVncChannelResult notify guesttool has exception, deskId is : " + deskId, e);
        }
    }

    /**
     * vnc通道创建成功后的处理 1.将远程协助状态更新为“在远程协助中” 2.取消该管理员正在远程协助的其他桌面
     *
     * @param newDeskId 新的vnc对应的桌面id
     */
    private void afterCreateVncChannelHandle(UUID newDeskId) {
        RemoteAssistInfo info = getRemoteAssistInfo(newDeskId);
        if (info == null) {
            LOGGER.error("can not find info from map. deskId is [{}]", newDeskId);
        } else {
            changeAssistState(newDeskId, RemoteAssistState.IN_REMOTE_ASSIST);
        }
    }

    @Override
    public void vncHeartbeatHandle(UUID deskId) {
        Assert.notNull(deskId, "deskId must not be null");
        RemoteAssistInfo info = getRemoteAssistInfo(deskId);
        if (info == null) {
            LOGGER.error("can not find info from map. deskId is [{}]", deskId);
            return;
        }
        if (info.getState() != RemoteAssistState.IN_REMOTE_ASSIST) {
            LOGGER.error("vnc heartbeat must send [{}] state. desk id is [{}]",
                RemoteAssistState.IN_REMOTE_ASSIST.name(), deskId);
            return;
        }
        resetVncHeartBeat(deskId);
    }

    private void sendCancelRemoteAssist(UUID deskId) {
        try {
            LOGGER.info("发送取消远程协助消息，cbbDesktopId = [{}]", deskId);
            remoteAssistService.cancelRemoteAssistDesk(deskId);
        } catch (BusinessException e) {
            LOGGER.error("cancelRemoteAssistDeskVDI fail. deskId is : " + deskId, e);
        }
    }


    private void addInfoMap(UUID deskId, RemoteAssistInfoDTO infoDTO) {
        CreateRemoteAssistInfoRequest request = new CreateRemoteAssistInfoRequest();
        request.setDeskId(deskId);
        request.setInfoDTO(infoDTO);
        remoteAssistInfoOperateAPI.createRemoteAssistInfo(request);
    }


    private void updateInfoMapForAssistInfo(UUID deskId, RemoteAssistInfo info) {
        RemoteAssistInfoDTO infoDTO = new RemoteAssistInfoDTO();
        BeanUtil.copyPropertiesIgnoreNull(info, infoDTO);
        infoDTO.setDeskId(deskId);
        remoteAssistInfoOperateAPI.updateRemoteAssistInfo(infoDTO);
    }

    /**
     * 用户同意、拒绝、超时后更新远程信息
     *
     * @param assistDTO 当前桌面远程信息
     */
    @Override
    public void updateAssistInfoAfterUserConfirm(ComputerAssistDTO assistDTO) throws BusinessException {
        Assert.notNull(assistDTO, "deskId is not be null");
        UUID deskId = assistDTO.getDeskId();
        RemoteAssistInfoDTO infoDTO = new RemoteAssistInfoDTO();
        infoDTO.setDeskId(deskId);
        infoDTO.setAssistPort(assistDTO.getPort());
        infoDTO.setPassword(assistDTO.getPwd());
        infoDTO.setServerPort(assistDTO.getPort());
        infoDTO.setServerAddr(assistDTO.getIp());
        RemoteAssistInfo info = getRemoteAssistInfo(deskId);
        if (info == null) {
            LOGGER.error("can not find info from map. deskId is [{}]", deskId);
            throw new BusinessException(BusinessKey.RCDC_USER_REMOTE_ASSIST_QUERY_NO_INFO);
        }
        infoDTO.setAdminId(info.getAdminId());
        remoteAssistInfoOperateAPI.updateRemoteAssistInfo(infoDTO);
    }

    @Override
    public void setRemoteAssistCloseStatus(UUID deskId, Boolean status) {
        Assert.notNull(deskId, "deskId is not be null");
        Assert.notNull(status, "status is not be null");
        hasComputerRemoteAssistEnd.put(deskId, status);
    }

    @Override
    public Boolean getRemoteAssistCloseStatus(UUID deskId) {
        Assert.notNull(deskId, "deskId is not be null");
        if (hasComputerRemoteAssistEnd.get(deskId) == null) {
            // 如果没有该值，默认为true
            return true;
        }
        return hasComputerRemoteAssistEnd.get(deskId);
    }

    @Override
    public void removeRemoteAssistCloseStatus(UUID deskId) {
        Assert.notNull(deskId, "deskId is not be null");
        hasComputerRemoteAssistEnd.remove(deskId);
    }

    private RemoteAssistInfo getRemoteAssistInfo(UUID deskId) {
        RemoteAssistInfoResponse response = remoteAssistInfoOperateAPI.queryRemoteAssistInfo(new IdRequest(deskId));
        if (response.getDeskId() == null) {
            // 不存在远程信息则返回空
            return null;
        }
        if (response.getState() == null) {
            removeInfoMap(deskId);
        }
        RemoteAssistInfo info = new RemoteAssistInfo();
        BeanUtils.copyProperties(response, info);
        return info;
    }

    private void removeInfoMap(UUID deskId) {
        remoteAssistInfoOperateAPI.removeRemoteAssistInfo(new IdRequest(deskId));
    }

    private void resetVncHeartBeat(UUID deskId) {
        remoteAssistInfoOperateAPI.resetVncHeartBeat(new IdRequest(deskId));
    }

    /**
     * 变更状态，要更新时间戳
     */
    private void changeAssistState(UUID deskId, RemoteAssistState state) {
        remoteAssistInfoOperateAPI.changeAssistState(new ChangeAssistStateRequest(deskId, state));
    }


}
