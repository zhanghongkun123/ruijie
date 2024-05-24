package com.ruijie.rcos.rcdc.rco.module.impl.service.struct;

import com.ruijie.rcos.rcdc.rco.module.def.api.dto.AssistInfoDTO;
import com.ruijie.rcos.rcdc.rco.module.def.api.dto.CloudDesktopRemoteAssistDTO;
import com.ruijie.rcos.rcdc.rco.module.def.api.enums.RemoteAssistState;
import com.ruijie.rcos.rcdc.rco.module.impl.enums.RemoteAssistTypeEnum;
import org.springframework.util.Assert;

import java.util.UUID;

/**
 * Description: 远程协助信息单元
 * Copyright: Copyright (c) 2018
 * Company: Ruijie Co., Ltd.
 * Create Time: 2019/1/23
 *
 * @author artom
 */
public class RemoteAssistInfo {

    /** 其他状态超时 */
    public static final long EXPIRED_TIME = 10 * 1000L;

    /** 远程协助用户响应超时时间，单位/秒 */
    public static final long USER_RESPONSE_EXPIRED_TIME = 250 * 1000L;

    private UUID deskId;

    private UUID adminId;

    private String adminName;

    private RemoteAssistState state;

    private long timeStamp = System.currentTimeMillis();// 时间戳，状态变化时进行更新

    private String assistIp;

    private int assistPort;

    private String assistToken;

    private String password;

    private int overtimeCount = 0;

    private String desktopName;

    private RemoteAssistTypeEnum remoteAssistTypeEnum;

    private int ssl;
    
    /**
     * 旧的协助状态，用于map缓存中协助信息未被销毁时，又重新创建了新的同桌面不同管理员协助信息的情况下，可能存在接收到旧协助信息查询时使用
     */
    private RemoteAssistState oldState;

    private String serverAddr;

    private Integer serverPort;

    private Integer remoteConnectChain;

    public RemoteAssistInfo() {
    }

    public RemoteAssistInfo(UUID deskId, UUID adminId, String adminName) {
        Assert.notNull(deskId, "deskId must not be null.");
        Assert.notNull(adminId, "adminId must not be null.");
        Assert.hasText(adminName, "adminName can not be blank.");
        this.deskId = deskId;
        this.adminId = adminId;
        this.adminName = adminName;
        this.state = RemoteAssistState.WAITING;
    }

    public RemoteAssistInfo(UUID deskId, UUID adminId) {
        Assert.notNull(deskId, "deskId must not be null.");
        Assert.notNull(adminId, "adminId must not be null.");
        this.deskId = deskId;
        this.adminId = adminId;
    }

    public UUID getAdminId() {
        return adminId;
    }

    public void setAdminId(UUID adminId) {
        this.adminId = adminId;
    }

    public UUID getDeskId() {
        return deskId;
    }

    public void setDeskId(UUID deskId) {
        this.deskId = deskId;
    }

    public RemoteAssistState getState() {
        return state;
    }

    public void setState(RemoteAssistState state) {
        this.state = state;
    }

    public long getTimeStamp() {
        return timeStamp;
    }

    public void setTimeStamp(long timeStamp) {
        this.timeStamp = timeStamp;
    }

    /**
     * 更新时间戳
     */
    public void updateStamp() {
        timeStamp = System.currentTimeMillis();
    }

    public String getAssistIp() {
        return assistIp;
    }

    public void setAssistIp(String assistIp) {
        this.assistIp = assistIp;
    }

    public int getAssistPort() {
        return assistPort;
    }

    public void setAssistPort(int assistPort) {
        this.assistPort = assistPort;
    }

    public String getAssistToken() {
        return assistToken;
    }

    public void setAssistToken(String assistToken) {
        this.assistToken = assistToken;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public int getOvertimeCount() {
        return overtimeCount;
    }

    public String getAdminName() {
        return adminName;
    }
    
    public RemoteAssistState getOldState() {
        return oldState;
    }

    public void setOldState(RemoteAssistState oldState) {
        this.oldState = oldState;
    }

    /**
     * 更新状态
     * 
     * @param state 新状态
     */
    public void changeState(RemoteAssistState state) {
        Assert.notNull(state, "state is not null");
        this.state = state;
        updateStamp();
    }

    /**
     * 远程协助是否已结束
     * 
     * @return true is finish
     */
    public boolean isFinish() {
        switch (state) {
            case START_FAIL:
            case STOP_USER:
            case STOP_ADMIN:
            case STOP_TIMEOUT:
            case REJECT:
            case TOKEN_INVALID:
            case DATA_ILLEGAL:
            case RESPONSE_TIMEOUT:
            case FINISH:
            case LOCK_SCREEN:
            case VNC_TIMEOUT:
                return true;
            default:
                return false;
        }
    }

    /**
     * 状态是否过期
     * 
     * @return true 过期
     */
    public boolean isStateExpired() {
        return (System.currentTimeMillis() - timeStamp) > EXPIRED_TIME;
    }

    /**
     * 用户响应超时
     * 
     * @return true 用户响应过期
     */
    public boolean isUserResponseExpired() {
        return (System.currentTimeMillis() - timeStamp) > USER_RESPONSE_EXPIRED_TIME;
    }

    /**
     ** 转换
     * 
     * @param info assist info
     * @return CloudDesktopRemoteAssitDTO
     */
    public static CloudDesktopRemoteAssistDTO convertToResponse(RemoteAssistInfo info) {
        Assert.notNull(info, "info is not null");

        CloudDesktopRemoteAssistDTO remoteDTO = new CloudDesktopRemoteAssistDTO();
        remoteDTO.setAssistState(info.getState().name());
        if (info.getState() == RemoteAssistState.AGREE || info.getState() == RemoteAssistState.IN_REMOTE_ASSIST) {
            AssistInfoDTO infoDTO = new AssistInfoDTO();
            infoDTO.setIp(info.getAssistIp());
            infoDTO.setPort(info.getAssistPort());
            infoDTO.setToken(info.getAssistToken());
            infoDTO.setPassword(info.getPassword());
            infoDTO.setServerAddr(info.getServerAddr());
            infoDTO.setServerPort(info.getServerPort());
            infoDTO.setSsl(info.getSsl());
            remoteDTO.setAssistInfo(infoDTO);
        }

        return remoteDTO;
    }

    /**
     * 重置心跳消息次数
     */
    public void resetVncHeartbeatOvertimeCount() {
        overtimeCount = 0;
    }

    /**
     * 心跳消息超时次数自增
     */
    public void increaseOvertimeCount() {
        overtimeCount = overtimeCount + 1;
    }

    public String getDesktopName() {
        return desktopName;
    }

    public void setDesktopName(String desktopName) {
        this.desktopName = desktopName;
    }

    public RemoteAssistTypeEnum getRemoteAssistTypeEnum() {
        return remoteAssistTypeEnum;
    }

    public void setRemoteAssistTypeEnum(RemoteAssistTypeEnum remoteAssistTypeEnum) {
        this.remoteAssistTypeEnum = remoteAssistTypeEnum;
    }

    public int getSsl() {
        return ssl;
    }

    public void setSsl(int ssl) {
        this.ssl = ssl;
    }

    public String getServerAddr() {
        return serverAddr;
    }

    public void setServerAddr(String serverAddr) {
        this.serverAddr = serverAddr;
    }

    public Integer getServerPort() {
        return serverPort;
    }

    public void setServerPort(Integer serverPort) {
        this.serverPort = serverPort;
    }

    public Integer getRemoteConnectChain() {
        return remoteConnectChain;
    }

    public void setRemoteConnectChain(Integer remoteConnectChain) {
        this.remoteConnectChain = remoteConnectChain;
    }
}
