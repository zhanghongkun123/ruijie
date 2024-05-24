package com.ruijie.rcos.rcdc.rco.module.web.ctrl.user.vo;

import com.ruijie.rcos.rcdc.clouddesktop.module.def.enums.DesktopRole;
import com.ruijie.rcos.sk.base.annotation.NotNull;
import com.ruijie.rcos.sk.webmvc.api.vo.IdLabelEntry;

/**
 * 云桌面配置信息公共基础类
 * Description: Function Description
 * Copyright: Copyright (c) 2018
 * Company: Ruijie Co., Ltd.
 * Create Time: 2019/12/25
 *
 * @author chen zj
 */
public class DesktopConfigBaseVO {

    @NotNull
    private IdLabelEntry strategy;

    @NotNull
    private IdLabelEntry image;

    @NotNull
    private DesktopRole desktopRole = DesktopRole.NORMAL;

    public IdLabelEntry getStrategy() {
        return strategy;
    }

    public void setStrategy(IdLabelEntry strategy) {
        this.strategy = strategy;
    }

    public IdLabelEntry getImage() {
        return image;
    }

    public void setImage(IdLabelEntry image) {
        this.image = image;
    }

    public DesktopRole getDesktopRole() {
        return desktopRole;
    }

    public void setDesktopRole(DesktopRole desktopRole) {
        this.desktopRole = desktopRole;
    }
}
