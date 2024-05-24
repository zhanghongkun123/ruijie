package com.ruijie.rcos.rcdc.rco.module.def.userprofile.request;

import com.ruijie.rcos.sk.webmvc.api.vo.Sort;
import org.springframework.lang.Nullable;

import java.util.UUID;

/**
 * Description: 导出路径请求对象
 * Copyright: Copyright (c) 2022
 * Company: Ruijie Co., Ltd.
 * Create Time: 2022/4/11
 *
 * @author WuShengQiang
 */
public class ExportUserProfilePathRequest {

    @Nullable
    private String userId;

    @Nullable
    private Sort[] sortArr;

    @Nullable
    private UUID groupId;

    public ExportUserProfilePathRequest(@Nullable String userId, @Nullable UUID groupId) {
        this.userId = userId;
        this.groupId = groupId;
    }

    public ExportUserProfilePathRequest(@Nullable String userId, @Nullable Sort[] sortArr, @Nullable UUID groupId) {
        this.userId = userId;
        this.sortArr = sortArr;
        this.groupId = groupId;
    }

    /**
     * 构造key
     *
     * @return 返回key
     */
    public String genExportPathKey() {
        String userId = this.getUserId();
        StringBuilder sb = new StringBuilder();
        sb.append(userId);
        if (this.getGroupId() != null) {
            sb.append("_").append(this.getGroupId());
        }
        return sb.toString();
    }

    @Nullable
    public String getUserId() {
        return userId;
    }

    public void setUserId(@Nullable String userId) {
        this.userId = userId;
    }

    @Nullable
    public Sort[] getSortArr() {
        return sortArr;
    }

    public void setSortArr(@Nullable Sort[] sortArr) {
        this.sortArr = sortArr;
    }

    @Nullable
    public UUID getGroupId() {
        return groupId;
    }

    public void setGroupId(@Nullable UUID groupId) {
        this.groupId = groupId;
    }
}
