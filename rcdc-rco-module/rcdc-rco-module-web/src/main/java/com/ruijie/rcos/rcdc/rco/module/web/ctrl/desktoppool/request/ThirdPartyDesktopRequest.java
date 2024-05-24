package com.ruijie.rcos.rcdc.rco.module.web.ctrl.desktoppool.request;

import com.ruijie.rcos.rcdc.rco.module.def.desktoppool.dto.DesktopPoolBasicDTO;
import com.ruijie.rcos.sk.base.batch.BatchTaskBuilder;

import java.util.List;
import java.util.Set;
import java.util.UUID;

/**
 * Description: 创建第三方桌面基础信息DTO
 * Copyright: Copyright (c) 2024
 * Company: Ruijie Co., Ltd.
 * Create Time: 2024/2/21
 *
 * @author zqj
 */
public class ThirdPartyDesktopRequest {

    /**
     * 权限终端组数组
     */
    private UUID[] groupIdArr;

    /**
     * 桌面池已关联的终端组
     */
    private Set<String> computerGroupArr;

    /**
     * 新增终端组
     */
    private List<UUID> addGroupIdList;

    /**
     * 新增终端id数组列表
     */
    private List<UUID> computerIdList;

    private DesktopPoolBasicDTO poolBasicDTO;

    private BatchTaskBuilder builder;

    private DesktopPoolAddComputerWebRequest desktopPoolAddComputerWebRequest;

    public UUID[] getGroupIdArr() {
        return groupIdArr;
    }

    public void setGroupIdArr(UUID[] groupIdArr) {
        this.groupIdArr = groupIdArr;
    }

    public Set<String> getComputerGroupArr() {
        return computerGroupArr;
    }

    public void setComputerGroupArr(Set<String> computerGroupArr) {
        this.computerGroupArr = computerGroupArr;
    }

    public List<UUID> getAddGroupIdList() {
        return addGroupIdList;
    }

    public void setAddGroupIdList(List<UUID> addGroupIdList) {
        this.addGroupIdList = addGroupIdList;
    }

    public DesktopPoolBasicDTO getPoolBasicDTO() {
        return poolBasicDTO;
    }

    public void setPoolBasicDTO(DesktopPoolBasicDTO poolBasicDTO) {
        this.poolBasicDTO = poolBasicDTO;
    }

    public BatchTaskBuilder getBuilder() {
        return builder;
    }

    public void setBuilder(BatchTaskBuilder builder) {
        this.builder = builder;
    }

    public DesktopPoolAddComputerWebRequest getDesktopPoolAddComputerWebRequest() {
        return desktopPoolAddComputerWebRequest;
    }

    public void setDesktopPoolAddComputerWebRequest(DesktopPoolAddComputerWebRequest desktopPoolAddComputerWebRequest) {
        this.desktopPoolAddComputerWebRequest = desktopPoolAddComputerWebRequest;
    }

    public List<UUID> getComputerIdList() {
        return computerIdList;
    }

    public void setComputerIdList(List<UUID> computerIdList) {
        this.computerIdList = computerIdList;
    }
}
