package com.ruijie.rcos.rcdc.rco.module.def.desktoppool.dto;

/**
 *
 * Description: 池桌面列表展示DTO
 * Copyright: Copyright (c) 2021
 * Company: Ruijie Co., Ltd.
 * Create Time: 2021年10月12日
 *
 * @author linke
 */
public class DesktopPoolDTO extends DesktopPoolBasicDTO {

    /**
     * 云桌面已分配
     **/
    private Integer desktopAssignedNum;

    /**
     * 池中配置不一致的桌面数量
     */
    private Integer conflictDeskNum;

    /**
     * 是否可勾选：true可以勾选，false不可勾选（置灰）
     */
    private Boolean canUsed = Boolean.TRUE;

    /**
     * canUsed=false的提示语
     */
    private String canUsedMessage;

    public Integer getDesktopAssignedNum() {
        return desktopAssignedNum;
    }

    public void setDesktopAssignedNum(Integer desktopAssignedNum) {
        this.desktopAssignedNum = desktopAssignedNum;
    }

    public Integer getConflictDeskNum() {
        return conflictDeskNum;
    }

    public void setConflictDeskNum(Integer conflictDeskNum) {
        this.conflictDeskNum = conflictDeskNum;
    }

    public Boolean getCanUsed() {
        return canUsed;
    }

    public void setCanUsed(Boolean canUsed) {
        this.canUsed = canUsed;
    }

    public String getCanUsedMessage() {
        return canUsedMessage;
    }

    public void setCanUsedMessage(String canUsedMessage) {
        this.canUsedMessage = canUsedMessage;
    }
}
