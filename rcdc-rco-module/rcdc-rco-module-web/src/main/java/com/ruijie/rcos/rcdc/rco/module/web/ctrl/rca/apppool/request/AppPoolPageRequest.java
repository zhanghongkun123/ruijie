package com.ruijie.rcos.rcdc.rco.module.web.ctrl.rca.apppool.request;

import java.util.Arrays;
import java.util.List;
import java.util.UUID;

import org.springframework.util.Assert;

import com.google.common.collect.Lists;
import com.ruijie.rcos.rcdc.rca.module.def.dto.MatchEqual;
import com.ruijie.rcos.rcdc.rca.module.def.enums.RcaEnum;
import com.ruijie.rcos.rcdc.rca.module.def.request.PageSearchRequest;
import com.ruijie.rcos.sk.webmvc.api.request.PageWebRequest;
import com.ruijie.rcos.sk.webmvc.api.vo.ExactMatch;

/**
 * Description:
 * Copyright: Copyright (c) 2021
 * Company: Ruijie Co., Ltd.
 * Create Time: 2024/1/3 16:39
 *
 * @author zhengjingyong
 */
public class AppPoolPageRequest extends PageSearchRequest {

    private static final String ID = "id";

    private static final String POOL_TYPE = "poolType";

    private static final String POOL_STATE = "poolState";

    private static final String SESSION_TYPE = "sessionType";

    private static final String HOST_RESOURCE = "hostSourceType";

    private static final String IS_OPEN_MAINTENANCE = "isOpenMaintenance";

    private static final String DEFAULT_GROUP = "isContainDefaultGroup";

    private static final String ROOT_IMAGE_ID = "rootImageId";

    public AppPoolPageRequest(PageWebRequest webRequest) {
        super(webRequest);
        getDefaultGroupInfo(webRequest);
    }

    private void getDefaultGroupInfo(PageWebRequest webRequest) {
        ExactMatch[] exactMatchArr = webRequest.getExactMatchArr();
        if (exactMatchArr == null || exactMatchArr.length == 0) {
            return;
        }
        for (ExactMatch exactMatch : exactMatchArr) {
            if (DEFAULT_GROUP.equalsIgnoreCase(exactMatch.getName())) {
                isContainDefaultGroup = Boolean.valueOf(exactMatch.getValueArr()[0]);
                break;
            }
        }
    }


    private Boolean isContainDefaultGroup = Boolean.FALSE;

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
                case SESSION_TYPE:
                    matchEqual = new MatchEqual(exactMatch.getName(), Arrays.stream(exactMatch.getValueArr())
                            .map(RcaEnum.HostSessionType::valueOf).toArray());
                    matchEqualList.add(matchEqual);
                    break;
                case HOST_RESOURCE:
                    matchEqual = new MatchEqual(exactMatch.getName(), Arrays.stream(exactMatch.getValueArr())
                            .map(RcaEnum.HostSourceType::valueOf).toArray());
                    matchEqualList.add(matchEqual);
                    break;
                case POOL_TYPE:
                    matchEqual = new MatchEqual(exactMatch.getName(), Arrays.stream(exactMatch.getValueArr())
                            .map(RcaEnum.PoolType::valueOf).toArray());
                    matchEqualList.add(matchEqual);
                    break;
                case POOL_STATE:
                    matchEqual = new MatchEqual(exactMatch.getName(), Arrays.stream(exactMatch.getValueArr())
                            .map(RcaEnum.PoolState::valueOf).toArray());
                    matchEqualList.add(matchEqual);
                    break;
                case ID:
                case ROOT_IMAGE_ID:
                    matchEqual = new MatchEqual(exactMatch.getName(), Arrays.stream(exactMatch.getValueArr()).map(UUID::fromString).toArray());
                    matchEqualList.add(matchEqual);
                    break;
                case IS_OPEN_MAINTENANCE:
                    matchEqual = new MatchEqual(exactMatch.getName(), Arrays.stream(exactMatch.getValueArr())
                            .map(Boolean::valueOf).toArray());
                    matchEqualList.add(matchEqual);
                    break;
                case DEFAULT_GROUP:
                    break;
                default:
                    matchEqual = new MatchEqual(exactMatch.getName(), exactMatch.getValueArr());
                    matchEqualList.add(matchEqual);
                    break;
            }
        }
        return matchEqualList;
    }

    public Boolean getIsContainDefaultGroup() {
        return isContainDefaultGroup;
    }
}
