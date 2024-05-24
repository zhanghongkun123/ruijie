package com.ruijie.rcos.rcdc.rco.module.def.openapi.request;

import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.annotation.JSONField;
import com.ruijie.rcos.sk.base.annotation.NotBlank;
import com.ruijie.rcos.sk.base.annotation.NotNull;
import com.ruijie.rcos.sk.base.batch.BatchTaskItemStatus;
import com.ruijie.rcos.sk.webmvc.api.request.WebRequest;
import org.springframework.lang.Nullable;

import java.util.UUID;


/**
 * Description: Function Description
 * Copyright: Copyright (c) 2020
 * Company: Ruijie Co., Ltd.
 * Create Time: 2021/10/12
 *
 * @author zqj
 */
public class TaskItemRequest implements WebRequest {


    @NotBlank
    @JSONField(name = "item_result")
    private String itemResult;

    @Nullable
    private String id;

    @Nullable
    @JSONField(serialize = false)
    private UUID mainTaskId;

    @Nullable
    @JSONField(serialize = false)
    private JSONObject requestBody;

    @NotNull
    @JSONField(name = "item_biz_status")
    private BatchTaskItemStatus itemBizStatus;

    @Nullable
    @JSONField(name = "item_biz_data")
    private JSONObject itemBizData;

    /**
     * 创建失败是否回滚已创建的云桌面n
     */
    @NotNull
    private Boolean failRollback;

    @Nullable
    public UUID getMainTaskId() {
        return mainTaskId;
    }

    public void setMainTaskId(@Nullable UUID mainTaskId) {
        this.mainTaskId = mainTaskId;
    }

    public String getItemResult() {
        return itemResult;
    }

    public void setItemResult(String itemResult) {
        this.itemResult = itemResult;
    }

    public BatchTaskItemStatus getItemBizStatus() {
        return itemBizStatus;
    }

    public void setItemBizStatus(BatchTaskItemStatus itemBizStatus) {
        this.itemBizStatus = itemBizStatus;
    }

    @Nullable
    public JSONObject getRequestBody() {
        return requestBody;
    }

    public void setRequestBody(@Nullable JSONObject requestBody) {
        this.requestBody = requestBody;
    }

    @Nullable
    public JSONObject getItemBizData() {
        return itemBizData;
    }

    public void setItemBizData(@Nullable JSONObject itemBizData) {
        this.itemBizData = itemBizData;
    }

    @Nullable
    public String getId() {
        return id;
    }

    public void setId(@Nullable String id) {
        this.id = id;
    }

    public Boolean getFailRollback() {
        return failRollback;
    }

    public void setFailRollback(Boolean failRollback) {
        this.failRollback = failRollback;
    }
}
