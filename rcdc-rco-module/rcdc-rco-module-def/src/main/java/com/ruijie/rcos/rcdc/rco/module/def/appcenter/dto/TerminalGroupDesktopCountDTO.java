package com.ruijie.rcos.rcdc.rco.module.def.appcenter.dto;

import java.util.UUID;

/**
 * Description:
 * Copyright: Copyright (c)
 * Company: Ruijie Co., Ltd.
 * Create Time: 2023/03/26 16:34
 *
 * @author coderLee23
 */
public class TerminalGroupDesktopCountDTO {

    private UUID terminalGroupId;

    private Long notUsedDesktopCount;

    private Long usedDesktopCount;

    public TerminalGroupDesktopCountDTO(UUID terminalGroupId, Long notUsedDesktopCount, Long usedDesktopCount) {
        this.terminalGroupId = terminalGroupId;
        this.notUsedDesktopCount = notUsedDesktopCount;
        this.usedDesktopCount = usedDesktopCount;
    }

    public UUID getTerminalGroupId() {
        return terminalGroupId;
    }

    public void setTerminalGroupId(UUID terminalGroupId) {
        this.terminalGroupId = terminalGroupId;
    }

    public Long getNotUsedDesktopCount() {
        return notUsedDesktopCount;
    }

    public void setNotUsedDesktopCount(Long notUsedDesktopCount) {
        this.notUsedDesktopCount = notUsedDesktopCount;
    }

    public Long getUsedDesktopCount() {
        return usedDesktopCount;
    }

    public void setUsedDesktopCount(Long usedDesktopCount) {
        this.usedDesktopCount = usedDesktopCount;
    }
}
