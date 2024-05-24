package com.ruijie.rcos.rcdc.rco.module.web.ctrl.rca.poolapp.request;

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
 * Description: 池应用分页查询
 * Copyright: Copyright (c) 2021
 * Company: Ruijie Co., Ltd.
 * Create Time: 2024/1/15 16:39
 *
 * @author zhengjingyong
 */
public class PoolAppPageRequest extends PageSearchRequest {

    private static final String ID = "id";

    private static final String POOL_ID = "poolId";

    private static final String GROUP_ID = "appGroupId";

    private static final String PUBLISHED = "isPublished";

    private static final String APP_PUBLISHER = "appPublisher";

    private static final String ENABLED = "enabled";

    private static final String APP_START_MODE = "appStartMode";

    public PoolAppPageRequest(PageWebRequest webRequest) {
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
                case APP_PUBLISHER:
                    matchEqual = new MatchEqual(exactMatch.getName(), Arrays.stream(exactMatch.getValueArr())
                            .map(RcaEnum.AppPublisher::valueOf).toArray());
                    matchEqualList.add(matchEqual);
                    break;
                case APP_START_MODE:
                    matchEqual = new MatchEqual(exactMatch.getName(), Arrays.stream(exactMatch.getValueArr())
                            .map(RcaEnum.AppStartMode::valueOf).toArray());
                    matchEqualList.add(matchEqual);
                    break;
                case ID:
                case POOL_ID:
                    String[] valueArr = exactMatch.getValueArr();
                    UUID[] idArr = new UUID[valueArr.length];
                    for (int j = 0; j < valueArr.length; j++) {
                        idArr[j] = UUID.fromString(valueArr[j]);
                    }
                    matchEqual = new MatchEqual(exactMatch.getName(), idArr);
                    matchEqualList.add(matchEqual);
                    break;
                case PUBLISHED:
                case ENABLED:
                    matchEqual = new MatchEqual(exactMatch.getName(), Arrays.stream(exactMatch.getValueArr())
                            .map(Boolean::valueOf).toArray());
                    matchEqualList.add(matchEqual);
                    break;
                case GROUP_ID:
                    continue;
                default:
                    matchEqual = new MatchEqual(exactMatch.getName(), exactMatch.getValueArr());
                    matchEqualList.add(matchEqual);
                    break;
            }
        }
        return matchEqualList;
    }

}
