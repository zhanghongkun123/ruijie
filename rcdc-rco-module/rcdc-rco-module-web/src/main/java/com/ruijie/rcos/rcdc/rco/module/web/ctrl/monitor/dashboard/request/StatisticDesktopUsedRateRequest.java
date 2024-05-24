package com.ruijie.rcos.rcdc.rco.module.web.ctrl.monitor.dashboard.request;

import com.ruijie.rcos.rcdc.clouddesktop.module.def.enums.desktoppool.DesktopPoolType;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.enums.desktoppool.CbbDesktopPoolType;
import io.swagger.annotations.ApiModelProperty;
import org.springframework.lang.Nullable;

import java.util.UUID;

/**
 * Description:
 * Copyright: Copyright (c) 2022
 * Company: Ruijie Co., Ltd.
 * Create Time: 2022/7/14 11:05
 *
 * @author yxq
 */
public class StatisticDesktopUsedRateRequest {

    /**
     * 桌面池类型：VDI/第三方
     */
    @Nullable
    @ApiModelProperty("桌面池类型，null时表示统计全部桌面池(VDI和第三方)")
    private CbbDesktopPoolType cbbDesktopPoolType;

    /**
     * 需要查询的桌面池
     */
    @Nullable
    @ApiModelProperty("桌面池ID，null时表示统计全部桌面池")
    private UUID desktopPoolId;

    /**
     * 需要查询的桌面池类型范围
     */
    @Nullable
    @ApiModelProperty("desktopPoolId=null时，传desktopPoolType表示获取该类型全部桌面池；不传不限制类型范围")
    private DesktopPoolType desktopPoolType;

    @Nullable
    public UUID getDesktopPoolId() {
        return desktopPoolId;
    }

    public void setDesktopPoolId(@Nullable UUID desktopPoolId) {
        this.desktopPoolId = desktopPoolId;
    }

    @Nullable
    public DesktopPoolType getDesktopPoolType() {
        return desktopPoolType;
    }

    public void setDesktopPoolType(@Nullable DesktopPoolType desktopPoolType) {
        this.desktopPoolType = desktopPoolType;
    }

    @Nullable
    public CbbDesktopPoolType getCbbDesktopPoolType() {
        return cbbDesktopPoolType;
    }

    public void setCbbDesktopPoolType(@Nullable CbbDesktopPoolType cbbDesktopPoolType) {
        this.cbbDesktopPoolType = cbbDesktopPoolType;
    }
}
