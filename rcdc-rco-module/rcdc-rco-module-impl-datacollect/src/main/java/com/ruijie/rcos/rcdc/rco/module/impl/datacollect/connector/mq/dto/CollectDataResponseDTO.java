package com.ruijie.rcos.rcdc.rco.module.impl.datacollect.connector.mq.dto;

import com.alibaba.fastjson.annotation.JSONField;
import com.ruijie.rcos.rcdc.rco.module.impl.datacollect.connector.mq.DataCollectConstant;
import com.ruijie.rcos.sk.base.annotation.NotNull;
import com.ruijie.rcos.sk.base.util.StringUtils;
import org.springframework.lang.Nullable;

import java.util.Date;

/**
 * Description: Function Description
 * Copyright: Copyright (c) 2020
 * Company: Ruijie Co., Ltd.
 * Create Time: 2021/6/10
 *
 * @author jarman
 */
public class CollectDataResponseDTO {

    /**
     * 标识业务key，必填
     */
    @NotNull
    private String dataKey = DataCollectConstant.CMC_DATA_COLLECT_KEY;

    /**
     * 采集的个性参数, json 格式，选填
     */
    @Nullable
    private String collectParam;

    /**
     * 数据类型，如： json, csv 等，小写，必填
     */
    @NotNull
    private String dataType = DataCollectConstant.JSON_STRING;

    /**
     * 数据格式，小写，如： gzip ，选填
     */
    @Nullable
    private String compressFormat = "gzip";

    /**
     * 文本数据内容。必填
     */
    @NotNull
    private String content = StringUtils.EMPTY;

    /**
     * 字节数据内容，选填
     */
    @JSONField(name = "contentBytes")
    @Nullable
    private byte[] contentByteArr;

    /**
     * 增量采集时的起始标识，选填
     */
    @Nullable
    private String nextMarker;

    /**
     * 增量采集时是否立即继续标识，选填
     */
    @Nullable
    private Boolean continued = Boolean.FALSE;

    /**
     * 采集时间，必填
     */
    @NotNull
    private Date collectTime;


    /**
     * 数据来源，选填
     */
    @Nullable
    private String source;

    /**
     * 为兼容旧的sdp版本，该字段必填，这里固定设置为空字符串
     */
    @NotNull
    private String dataLocation = StringUtils.EMPTY;

    /**
     * 后续扩展，选填
     */
    @Nullable
    private String extend;

    /**
     * 附加参数，如果请求的attachment参数不为空，必须把请求的attachment填充到响应的attachment 参数中
     */
    @Nullable
    private String attachment;

    public String getDataKey() {
        return dataKey;
    }

    public void setDataKey(String dataKey) {
        this.dataKey = dataKey;
    }

    public String getCollectParam() {
        return collectParam;
    }

    public void setCollectParam(String collectParam) {
        this.collectParam = collectParam;
    }

    public String getDataType() {
        return dataType;
    }

    public void setDataType(String dataType) {
        this.dataType = dataType;
    }

    public String getCompressFormat() {
        return compressFormat;
    }

    public void setCompressFormat(String compressFormat) {
        this.compressFormat = compressFormat;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public byte[] getContentByteArr() {
        return contentByteArr;
    }

    public void setContentByteArr(byte[] contentByteArr) {
        this.contentByteArr = contentByteArr;
    }

    public String getNextMarker() {
        return nextMarker;
    }

    public void setNextMarker(String nextMarker) {
        this.nextMarker = nextMarker;
    }

    public Boolean getContinued() {
        return continued;
    }

    public void setContinued(Boolean continued) {
        this.continued = continued;
    }

    public Date getCollectTime() {
        return collectTime;
    }

    public void setCollectTime(Date collectTime) {
        this.collectTime = collectTime;
    }

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }

    public String getDataLocation() {
        return dataLocation;
    }

    public void setDataLocation(String dataLocation) {
        this.dataLocation = dataLocation;
    }

    public String getExtend() {
        return extend;
    }

    public void setExtend(String extend) {
        this.extend = extend;
    }

    public String getAttachment() {
        return attachment;
    }

    public void setAttachment(String attachment) {
        this.attachment = attachment;
    }
}

