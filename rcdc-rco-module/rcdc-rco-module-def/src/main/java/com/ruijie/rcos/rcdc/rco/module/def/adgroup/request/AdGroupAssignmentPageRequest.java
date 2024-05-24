package com.ruijie.rcos.rcdc.rco.module.def.adgroup.request;

import com.google.common.collect.Lists;
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
 * Description: 分页查询池关联的所有安全组
 * Copyright: Copyright (c) 2022
 * Company: Ruijie Co., Ltd.
 * Create Time: 2022/9/27
 *
 * @author TD
 */
public class AdGroupAssignmentPageRequest extends PageSearchRequest {

    private static final String ID = "id";

    private static final String DESKTOP_POOL_ID = "desktopPoolId";

    private static final String DISK_POOL_ID = "diskPoolId";

    private static final String APP_GROUP_ID = "appGroupId";

    private String relatedPoolKey;

    private UUID relatedPoolId;

    public AdGroupAssignmentPageRequest(PageWebRequest webRequest) {
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
                case DISK_POOL_ID:
                    if (ArrayUtils.isNotEmpty(exactMatch.getValueArr())) {
                        relatedPoolId = UUID.fromString(exactMatch.getValueArr()[0]);
                        relatedPoolKey = DISK_POOL_ID;
                    }
                    break;
                case DESKTOP_POOL_ID:
                    if (ArrayUtils.isNotEmpty(exactMatch.getValueArr())) {
                        relatedPoolId = UUID.fromString(exactMatch.getValueArr()[0]);
                        relatedPoolKey = DESKTOP_POOL_ID;
                    }
                    break;
                case APP_GROUP_ID:
                    if (ArrayUtils.isNotEmpty(exactMatch.getValueArr())) {
                        relatedPoolId = UUID.fromString(exactMatch.getValueArr()[0]);
                        relatedPoolKey = APP_GROUP_ID;
                    }
                    break;
                case ID:
                    matchEqual = new MatchEqual(exactMatch.getName(), Arrays.stream(exactMatch.getValueArr()).map(UUID::fromString).toArray());
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

    public UUID getRelatedPoolId() {
        return relatedPoolId;
    }

    public void setRelatedPoolId(UUID relatedPoolId) {
        this.relatedPoolId = relatedPoolId;
    }

    public String getRelatedPoolKey() {
        return relatedPoolKey;
    }

    public void setRelatedPoolKey(String relatedPoolKey) {
        this.relatedPoolKey = relatedPoolKey;
    }
}
