package com.ruijie.rcos.rcdc.rco.module.web.ctrl.desktoppool.request;

import com.ruijie.rcos.sk.base.annotation.NotNull;
import io.swagger.annotations.ApiModelProperty;
import org.springframework.lang.Nullable;

import java.util.UUID;

/**
 * Description: 添加PC云桌面WebRequest
 * Copyright: Copyright (c) 2024
 * Company: Ruijie Co., Ltd.
 * Create Time: 2024/1/30
 *
 * @author zqj
 */
public class DesktopPoolAddComputerWebRequest {

    /**
     * 桌面池id
     */
    @ApiModelProperty(value = "桌面池ID")
    @NotNull
    private UUID id;

    /**
     * PC终端数组Id
     */
    @ApiModelProperty(value = "PC终端数组Id")
    @Nullable
    private UUID[] computerIdArr;

    /**
     * PC终端组
     */
    @ApiModelProperty(value = "PC终端组")
    @Nullable
    private UUID[] terminalGroupIdArr;

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    @Nullable
    public UUID[] getComputerIdArr() {
        return computerIdArr;
    }

    public void setComputerIdArr(@Nullable UUID[] computerIdArr) {
        this.computerIdArr = computerIdArr;
    }

    @Nullable
    public UUID[] getTerminalGroupIdArr() {
        return terminalGroupIdArr;
    }

    public void setTerminalGroupIdArr(@Nullable UUID[] terminalGroupIdArr) {
        this.terminalGroupIdArr = terminalGroupIdArr;
    }
}
