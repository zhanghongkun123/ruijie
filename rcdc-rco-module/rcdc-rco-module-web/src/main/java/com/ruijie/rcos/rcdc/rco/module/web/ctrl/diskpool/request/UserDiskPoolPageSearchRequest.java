package com.ruijie.rcos.rcdc.rco.module.web.ctrl.diskpool.request;

import com.google.common.collect.Lists;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.enums.CbbDiskPoolState;
import com.ruijie.rcos.rcdc.hciadapter.module.def.enums.CloudPlatformStatus;
import com.ruijie.rcos.rcdc.hciadapter.module.def.enums.CloudPlatformType;
import com.ruijie.rcos.rcdc.rco.module.def.api.dto.MatchEqual;
import com.ruijie.rcos.rcdc.rco.module.def.api.request.PageSearchRequest;
import com.ruijie.rcos.sk.webmvc.api.request.PageWebRequest;
import com.ruijie.rcos.sk.webmvc.api.vo.ExactMatch;
import org.apache.commons.lang3.ArrayUtils;
import org.springframework.util.Assert;

import java.util.Arrays;
import java.util.List;
import java.util.UUID;

/**
 * Description: 用户查询磁盘池
 * Copyright: Copyright (c) 2022
 * Company: Ruijie Co., Ltd.
 * Create Time: 2022/8/15
 *
 * @author TD
 */
public class UserDiskPoolPageSearchRequest extends PageSearchRequest {

    private static final String ID = "id";

    private static final String USER_ID = "userId";

    private static final String AD_GROUP_ID = "adGroupId";

    private static final String POOL_STATE = "poolState";

    private static final String ENABLE_CREATE_DISK = "enableCreateDisk";
    
    private static final String PLATFORM_ID = "platformId";

    private static final String PLATFORM_TYPE = "platformType";

    private static final String PLATFORM_STATUS = "platformStatus";

    private UUID userId;

    private UUID adGroupId;

    public UserDiskPoolPageSearchRequest(PageWebRequest webRequest) {
        super(webRequest);
    }

    @Override
    protected MatchEqual[] exactMatchConvert(ExactMatch[] exactMatchArr) {
        Assert.notNull(exactMatchArr , "exactMatchArr must not be null");
        // 对条件匹配数组对象进行组装
        List<MatchEqual> matchEqualList = assembleMatchEqualArr(exactMatchArr);
        return matchEqualList.toArray(new MatchEqual[0]);
    }

    private List<MatchEqual> assembleMatchEqualArr(ExactMatch[] exactMatchArr) {
        List<MatchEqual> matchEqualList = Lists.newArrayList();
        for (ExactMatch exactMatch : exactMatchArr) {
            MatchEqual matchEqual;
            switch (exactMatch.getName()) {
                case USER_ID:
                    if (ArrayUtils.isNotEmpty(exactMatch.getValueArr())) {
                        userId = UUID.fromString(exactMatch.getValueArr()[0]);
                    }
                    break;
                case AD_GROUP_ID:
                    if (ArrayUtils.isNotEmpty(exactMatch.getValueArr())) {
                        adGroupId = UUID.fromString(exactMatch.getValueArr()[0]);
                    }
                    break;
                case POOL_STATE:
                    matchEqual = new MatchEqual(exactMatch.getName(), Arrays.stream(exactMatch.getValueArr())
                            .map(CbbDiskPoolState::valueOf).toArray());
                    matchEqualList.add(matchEqual);
                    break;
                case ID:
                case PLATFORM_ID:
                    matchEqual = new MatchEqual(exactMatch.getName(), Arrays.stream(exactMatch.getValueArr()).map(UUID::fromString).toArray());
                    matchEqualList.add(matchEqual);
                    break;
                case ENABLE_CREATE_DISK:
                    matchEqual = new MatchEqual(exactMatch.getName(), Arrays.stream(exactMatch.getValueArr())
                            .map(Boolean::valueOf).toArray());
                    matchEqualList.add(matchEqual);
                    break;
                case PLATFORM_TYPE:
                    matchEqual = new MatchEqual(exactMatch.getName(),
                            Arrays.stream(exactMatch.getValueArr()).map(CloudPlatformType::valueOf).toArray(CloudPlatformType[]::new));
                    matchEqualList.add(matchEqual);
                    break;
                case PLATFORM_STATUS:
                    matchEqual = new MatchEqual(exactMatch.getName(),
                            Arrays.stream(exactMatch.getValueArr()).map(CloudPlatformStatus::valueOf).toArray(CloudPlatformStatus[]::new));
                    matchEqualList.add(matchEqual);
                    break;
                default:
                    matchEqual = new MatchEqual(exactMatch.getName(), exactMatch.getValueArr());
                    matchEqualList.add(matchEqual);
                    break;
            }
        }
        return matchEqualList;
    }

    public UUID getUserId() {
        return userId;
    }

    public void setUserId(UUID userId) {
        this.userId = userId;
    }

    public UUID getAdGroupId() {
        return adGroupId;
    }

    public void setAdGroupId(UUID adGroupId) {
        this.adGroupId = adGroupId;
    }
}
