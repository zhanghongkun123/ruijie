package com.ruijie.rcos.rcdc.rco.module.web.ctrl.rca.appgroup.request;

import java.util.Arrays;
import java.util.List;
import java.util.UUID;

import org.springframework.util.Assert;

import com.google.common.collect.Lists;
import com.ruijie.rcos.rcdc.rca.module.def.dto.MatchEqual;
import com.ruijie.rcos.rcdc.rca.module.def.request.PageSearchRequest;
import com.ruijie.rcos.sk.webmvc.api.request.PageWebRequest;
import com.ruijie.rcos.sk.webmvc.api.vo.ExactMatch;

/**
 * Description: 应用分组分页查询
 * Copyright: Copyright (c) 2021
 * Company: Ruijie Co., Ltd.
 * Create Time: 2024/1/3 16:39
 *
 * @author zhengjingyong
 */
public class AppPoolGroupPageRequest extends PageSearchRequest {

    private static final String ID = "id";

    private static final String APP_POOL_ID = "poolId";

    private static final String DEFAULT_GROUP = "defaultGroup";


    public AppPoolGroupPageRequest(PageWebRequest webRequest) {
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
                case ID:
                case APP_POOL_ID:
                    matchEqual = new MatchEqual(exactMatch.getName(), Arrays.stream(exactMatch.getValueArr()).map(UUID::fromString).toArray());
                    matchEqualList.add(matchEqual);
                    break;
                case DEFAULT_GROUP:
                    matchEqual = new MatchEqual(exactMatch.getName(), Arrays.stream(exactMatch.getValueArr())
                            .map(Boolean::valueOf).toArray());
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

}
