package com.ruijie.rcos.rcdc.rco.module.def.userprofile.request;

import com.ruijie.rcos.rcdc.rco.module.def.api.dto.MatchEqual;
import com.ruijie.rcos.rcdc.rco.module.def.api.request.PageSearchRequest;
import com.ruijie.rcos.sk.webmvc.api.request.PageWebRequest;
import com.ruijie.rcos.sk.webmvc.api.vo.ExactMatch;
import org.springframework.util.Assert;

import java.util.UUID;

/**
 * Description: 用户配置策略关联详情分页查询请求对象
 * Copyright: Copyright (c) 2022
 * Company: Ruijie Co., Ltd.
 * Create Time: 2022/4/18
 *
 * @author WuShengQiang
 */
public class UserProfileStrategyRelatedPageSearchRequest extends PageSearchRequest {

    public UserProfileStrategyRelatedPageSearchRequest(PageWebRequest webRequest) {
        super(webRequest);
    }

    @Override
    protected MatchEqual[] exactMatchConvert(ExactMatch[] exactMatchArr) {
        Assert.notNull(exactMatchArr, "exactMatchArr must not be null");
        MatchEqual[] matchEqualArr = new MatchEqual[exactMatchArr.length];
        for (int i = 0; i < exactMatchArr.length; i++) {
            ExactMatch exactMatch = exactMatchArr[i];
            MatchEqual matchEqual = null;
            switch (exactMatch.getName()) {
                case "strategyId":
                    String[] groupValueArr = exactMatch.getValueArr();
                    UUID[] groupIdArr = new UUID[groupValueArr.length];
                    for (int j = 0; j < groupValueArr.length; j++) {
                        groupIdArr[j] = UUID.fromString(groupValueArr[j]);
                    }
                    matchEqual = new MatchEqual(exactMatch.getName(), groupIdArr);
                    break;
                default:
                    matchEqual = new MatchEqual(exactMatch.getName(), exactMatch.getValueArr());
                    break;
            }
            matchEqualArr[i] = matchEqual;
        }
        return matchEqualArr;
    }

}