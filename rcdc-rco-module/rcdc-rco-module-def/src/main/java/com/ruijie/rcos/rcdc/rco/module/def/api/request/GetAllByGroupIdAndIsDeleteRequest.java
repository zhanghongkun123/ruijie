package com.ruijie.rcos.rcdc.rco.module.def.api.request;

import com.ruijie.rcos.sk.modulekit.api.comm.Request;
import org.springframework.lang.Nullable;

import java.util.UUID;

/**
 * Description: Function Description
 * Copyright: Copyright (c) 2020
 * Company: Ruijie Co., Ltd.
 * Create Time: 2020年01月07日
 *
 * @author xiejian
 */
public class GetAllByGroupIdAndIsDeleteRequest implements Request {

    @Nullable
    private UUID[] userGroupIdArr;

    @Nullable
    private UUID[] terminalGroupIdArr;

    @Nullable
    private UUID[] imageIdArr;

    /**
     * 桌面池组
     */
    @Nullable
    private UUID[] desktopPoolArr;

    /**
     * 磁盘池组
     */
    @Nullable
    private UUID[] diskPoolArr;

    @Nullable
    public UUID[] getUserGroupIdArr() {
        return userGroupIdArr;
    }

    public void setUserGroupIdArr(@Nullable UUID[] userGroupIdArr) {
        this.userGroupIdArr = userGroupIdArr;
    }

    @Nullable
    public UUID[] getTerminalGroupIdArr() {
        return terminalGroupIdArr;
    }

    public void setTerminalGroupIdArr(@Nullable UUID[] terminalGroupIdArr) {
        this.terminalGroupIdArr = terminalGroupIdArr;
    }

    @Nullable
    public UUID[] getImageIdArr() {
        return imageIdArr;
    }

    public void setImageIdArr(@Nullable UUID[] imageIdArr) {
        this.imageIdArr = imageIdArr;
    }

    @Nullable
    public UUID[] getDesktopPoolArr() {
        return desktopPoolArr;
    }

    public void setDesktopPoolArr(@Nullable UUID[] desktopPoolArr) {
        this.desktopPoolArr = desktopPoolArr;
    }

    @Nullable
    public UUID[] getDiskPoolArr() {
        return diskPoolArr;
    }

    public void setDiskPoolArr(@Nullable UUID[] diskPoolArr) {
        this.diskPoolArr = diskPoolArr;
    }
}
