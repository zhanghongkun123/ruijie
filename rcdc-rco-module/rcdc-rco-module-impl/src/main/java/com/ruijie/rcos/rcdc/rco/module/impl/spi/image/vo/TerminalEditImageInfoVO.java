package com.ruijie.rcos.rcdc.rco.module.impl.spi.image.vo;

/**
 * Description: 终端编辑镜像
 * Copyright: Copyright (c) 2023
 * Company: Ruijie Co., Ltd.
 * Create Time: 2023/8/15
 *
 * @author ypp
 */
public class TerminalEditImageInfoVO extends ImageInfoVO {


    /**
     * false:不能选择
     */
    private boolean canUse;

    /**
     * 不能选择的提示消息
     */
    private String canNotUseMessage;

    public boolean isCanUse() {
        return canUse;
    }

    public void setCanUse(boolean canUse) {
        this.canUse = canUse;
    }

    public String getCanNotUseMessage() {
        return canNotUseMessage;
    }

    public void setCanNotUseMessage(String canNotUseMessage) {
        this.canNotUseMessage = canNotUseMessage;
    }
}
