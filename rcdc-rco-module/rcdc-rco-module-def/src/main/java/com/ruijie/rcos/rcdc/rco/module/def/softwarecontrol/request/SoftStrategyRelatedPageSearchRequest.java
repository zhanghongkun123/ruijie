package com.ruijie.rcos.rcdc.rco.module.def.softwarecontrol.request;

import com.ruijie.rcos.rcdc.rco.module.def.api.dto.MatchEqual;
import com.ruijie.rcos.rcdc.rco.module.def.api.request.PageSearchRequest;
import com.ruijie.rcos.rcdc.rco.module.def.constants.Constants;
import com.ruijie.rcos.sk.webmvc.api.request.PageWebRequest;
import com.ruijie.rcos.sk.webmvc.api.vo.ExactMatch;
import org.springframework.util.Assert;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

/**
 * Description:
 * Copyright: Copyright (c) 2019
 * Company: Ruijie Co., Ltd.
 * Create Time: 2022/1/25 10:07
 *
 * @author linrenjian
 */
public class SoftStrategyRelatedPageSearchRequest extends PageSearchRequest {

    public SoftStrategyRelatedPageSearchRequest(PageWebRequest webRequest) {
        super(webRequest);
    }

    @Override
    protected MatchEqual[] exactMatchConvert(ExactMatch[] exactMatchArr) {
        Assert.notNull(exactMatchArr, "exactMatchArr must not be null");
        List<ExactMatch> exactMatchList = new ArrayList<>(exactMatchArr.length);
        for (int i = 0; i < exactMatchArr.length; i++) {
            ExactMatch exactMatch = exactMatchArr[i];
            if (exactMatch.getName().equals(Constants.WEB_REQUEST_SOFTWARE_FIELD_NAME)) {
                continue;
            }
            exactMatchList.add(exactMatch);
        }

        MatchEqual[] matchEqualArr = new MatchEqual[exactMatchList.size()];
        for (int i = 0; i < exactMatchList.size(); i++) {
            ExactMatch exactMatch = exactMatchArr[i];
            MatchEqual matchEqual;
            switch (exactMatch.getName()) {
                case "id":
                    matchEqual = new MatchEqual(exactMatch.getName(), Arrays.stream(exactMatch.getValueArr()).map(UUID::fromString).toArray());
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
