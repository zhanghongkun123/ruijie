package com.ruijie.rcos.rcdc.rco.module.web.ctrl.rca.apppool.request;

import com.ruijie.rcos.sk.base.annotation.NotNull;
import com.ruijie.rcos.sk.base.annotation.Range;
import io.swagger.annotations.ApiModelProperty;

import java.util.UUID;

/**
 * Description: 添加应用池云主机数
 * Copyright: Copyright (c) 2024
 * Company: Ruijie Co., Ltd.
 * Create Time: 2024年01月05日
 *
 * @author zhengjingyong
 */
public class AddAppPoolHostWebRequest {

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
