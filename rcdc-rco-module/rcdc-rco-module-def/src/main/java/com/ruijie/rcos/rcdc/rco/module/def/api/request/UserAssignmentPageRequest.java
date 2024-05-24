package com.ruijie.rcos.rcdc.rco.module.def.api.request;

import com.google.common.collect.ImmutableList;
import com.ruijie.rcos.rcdc.rco.module.def.api.dto.MatchEqual;
import com.ruijie.rcos.sk.webmvc.api.request.PageWebRequest;
import com.ruijie.rcos.sk.webmvc.api.vo.ExactMatch;
import org.apache.commons.lang3.ArrayUtils;
import org.springframework.util.Assert;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * Description: 分页查询桌面池关联的所有用户（用户组下的用户+分配的用户）
 * Copyright: Copyright (c) 2021
 * Company: Ruijie Co., Ltd.
 * Create Time: 2021/10/22 14:39
 *
 * @author linke
 */
public class UserAssignmentPageRequest extends UserPageSearchRequest {

    public static final String DESKTOP_POOL_ID = "desktopPoolId";

    public static final String DISK_POOL_ID = "diskPoolId";

    public static final String QUERY_SOURCE = "querySource";

    public static final String DESKTOP_TEMP_PERMISSION_ID = "desktopTempPermissionId";

    private UUID desktopPoolId;

    private UUID diskPoolId;

    private UUID groupId;

    private UUID desktopTempPermissionId;

    private String querySource;

    private UUID appGroupId;

    public UserAssignmentPageRequest(PageWebRequest webRequest) {
        super(webRequest);
    }

    @Override
    protected MatchEqual[] exactMatchConvert(ExactMatch[] exactMatchArr) {
        Assert.notNull(exactMatchArr , "exactMatchArr must not be null");
        // 对条件匹配数组对象进行组装
        MatchEqual[] matchEqualArr = super.exactMatchConvert(exactMatchArr);

        List<MatchEqual> matchEqualList = new ArrayList<>();
        for (MatchEqual matchEqual : matchEqualArr) {
            switch (matchEqual.getName()) {
                case DESKTOP_POOL_ID:
                    desktopPoolId = covertUUID(matchEqual.getValueArr());
                    continue;
                case DISK_POOL_ID:
                    diskPoolId = covertUUID(matchEqual.getValueArr());
                    continue;
                case APP_GROUP_ID:
                    appGroupId = covertUUID(matchEqual.getValueArr());
                    continue;
                case DESKTOP_TEMP_PERMISSION_ID:
                    desktopTempPermissionId = covertUUID(matchEqual.getValueArr());
                    continue;
                case QUERY_SOURCE:
                    querySource = covertString(matchEqual.getValueArr());
                    continue;
                case GROUP_ID:
                    if (ArrayUtils.isNotEmpty(matchEqual.getValueArr())) {
                        groupId = (UUID) matchEqual.getValueArr()[0];
                    }
                    matchEqualList.add(matchEqual);
                    break;
                default:
                    matchEqualList.add(matchEqual);
            }
        }
        return matchEqualList.toArray(new MatchEqual[0]);
    }

    private UUID covertUUID(Object[] valueArr) {
        return ArrayUtils.isNotEmpty(valueArr) ? UUID.fromString(String.valueOf(valueArr[0])) : null;
    }

    private String covertString(Object[] valueArr) {
        return ArrayUtils.isNotEmpty(valueArr) ? String.valueOf(valueArr[0]) : null;
    }

    /**
     * 获取关键字查询列名
     *
     * @return List<String>
     */
    public List<String> getSearchColumn() {
        return ImmutableList.of("userName", "realName");
    }

    public UUID getDesktopPoolId() {
        return desktopPoolId;
    }

    public void setDesktopPoolId(UUID desktopPoolId) {
        this.desktopPoolId = desktopPoolId;
    }

    public UUID getDiskPoolId() {
        return diskPoolId;
    }

    public void setDiskPoolId(UUID diskPoolId) {
        this.diskPoolId = diskPoolId;
    }

    public UUID getGroupId() {
        return groupId;
    }

    public void setGroupId(UUID groupId) {
        this.groupId = groupId;
    }

    public UUID getDesktopTempPermissionId() {
        return desktopTempPermissionId;
    }

    public void setDesktopTempPermissionId(UUID desktopTempPermissionId) {
        this.desktopTempPermissionId = desktopTempPermissionId;
    }

    public String getQuerySource() {
        return querySource;
    }

    public void setQuerySource(String querySource) {
        this.querySource = querySource;
    }

    public UUID getAppGroupId() {
        return appGroupId;
    }

    public void setAppGroupId(UUID appGroupId) {
        this.appGroupId = appGroupId;
    }
}
