package com.ruijie.rcos.rcdc.rco.module.web.ctrl.desktoppool.request;

import com.ruijie.rcos.sk.base.annotation.NotNull;
import com.ruijie.rcos.sk.base.annotation.Range;
import io.swagger.annotations.ApiModelProperty;

import java.util.UUID;

/**
 * Description: 添加云桌面WebRequest
 * Copyright: Copyright (c) 2021
 * Company: Ruijie Co., Ltd.
 * Create Time: 2021/10/25 15:55
 *
 * @author linke
 */
public class DesktopPoolAddDesktopWebRequest {

    /**
     * 桌面池id
     */
    @ApiModelProperty(value = "桌面池ID")
    @NotNull
    private UUID id;

    /**
     * 添加云桌面个数
     */
    @ApiModelProperty(value = "添加云桌面数量")
    @NotNull
    @Range(min = "1", max = "1000")
    private Integer addNum;

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public Integer getAddNum() {
        return addNum;
    }

    public void setAddNum(Integer addNum) {
        this.addNum = addNum;
    }
}
