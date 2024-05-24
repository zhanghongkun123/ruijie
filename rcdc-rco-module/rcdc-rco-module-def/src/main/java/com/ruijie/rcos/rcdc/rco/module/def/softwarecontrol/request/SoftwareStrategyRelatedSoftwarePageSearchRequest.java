package com.ruijie.rcos.rcdc.rco.module.def.softwarecontrol.request;

import com.ruijie.rcos.rcdc.rco.module.def.api.dto.MatchEqual;
import com.ruijie.rcos.rcdc.rco.module.def.api.request.PageSearchRequest;
import com.ruijie.rcos.sk.webmvc.api.request.PageWebRequest;
import com.ruijie.rcos.sk.webmvc.api.vo.ExactMatch;
import org.springframework.util.Assert;

/**
 * Description:
 * Copyright: Copyright (c) 2019
 * Company: Ruijie Co., Ltd.
 * Create Time: 2022/03/09 10:07
 *
 * @author chenli
 */
public class SoftwareStrategyRelatedSoftwarePageSearchRequest extends PageSearchRequest {

    private final static String NAME_SEARCH_PARAM_NAME = "nameSearch";

    private final static String NAME_PARAM_NAME = "name";

    private final static String STRATEGY_ID_PARAM_NAME = "strategyId";


    public SoftwareStrategyRelatedSoftwarePageSearchRequest(PageWebRequest webRequest) {
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
                case NAME_PARAM_NAME:
                    matchEqual = new MatchEqual(NAME_SEARCH_PARAM_NAME, exactMatch.getValueArr());
                    break;
                case STRATEGY_ID_PARAM_NAME:
                    matchEqual = new MatchEqual(STRATEGY_ID_PARAM_NAME, exactMatch.toUUIDValue());
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
