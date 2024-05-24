package com.ruijie.rcos.rcdc.rco.module.impl.datacollect.connector.mq.dto;

import com.alibaba.fastjson.JSON;
import com.ruijie.rcos.sk.base.annotation.NotNull;
import org.springframework.lang.Nullable;

/**
 * Description: 采集参数对象
 * Copyright: Copyright (c) 2020
 * Company: Ruijie Co., Ltd.
 * Create Time: 2021/6/10
 *
 * @author jarman
 */
public class CollectRequestDTO {

    /**
     * 标识业务key，必填
     */
    @NotNull
    private String dataKey;

    /**
     * 增量采集起始标记 ,选填
     */
    @Nullable
    private String startMarker;

    /**
     * 采集的个性参数, json 格式，选填
     */
    @Nullable
    private String collectParam;

    /**
     * 附加参数，选填，如果不为空，响应方必须把该参数透明的填充到响应的attachment 参数中
     */
    @Nullable
    private String attachment;

    public String getDataKey() {
        return dataKey;
    }

    public void setDataKey(String dataKey) {
        this.dataKey = dataKey;
    }

    public String getStartMarker() {
        return startMarker;
    }

    public void setStartMarker(String startMarker) {
        this.startMarker = startMarker;
    }

    public String getCollectParam() {
        return collectParam;
    }

    public void setCollectParam(String collectParam) {
        this.collectParam = collectParam;
    }

    public String getAttachment() {
        return attachment;
    }

    public void setAttachment(String attachment) {
        this.attachment = attachment;
    }

    @Override
    public String toString() {
        return JSON.toJSONString(this);
    }
}
