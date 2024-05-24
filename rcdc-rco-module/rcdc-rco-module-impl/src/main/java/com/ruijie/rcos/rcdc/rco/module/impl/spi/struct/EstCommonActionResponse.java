package com.ruijie.rcos.rcdc.rco.module.impl.spi.struct;


import org.springframework.lang.Nullable;

import java.util.UUID;

/**
 * Description: EstCommonActionResponse Est透传RCDC消息通道 返回DTO
 * Copyright: Copyright (c) 2021
 * Company: Ruijie Co., Ltd.
 * Create Time: 2021年3月31日
 *
 * @param <T> 响应参数
 * @author lihengjing
 */
public class EstCommonActionResponse<T> {

    /**
     * 子动作Action 小写 例如 snapshot_list
     */
    @Nullable
    private String subAction;

    /**
     * 子动作返回数据对象
     */
    @Nullable
    private EstCommonActionSubResponse<T> data;

    /**
     * 云桌面ID
     */
    @Nullable
    private UUID deskId;

    public String getSubAction() {
        return subAction;
    }

    public void setSubAction(String subAction) {
        this.subAction = subAction;
    }

    public EstCommonActionSubResponse<T> getData() {
        return data;
    }

    public void setData(EstCommonActionSubResponse<T> data) {
        this.data = data;
    }

    public UUID getDeskId() {
        return deskId;
    }

    public void setDeskId(UUID deskId) {
        this.deskId = deskId;
    }
}
