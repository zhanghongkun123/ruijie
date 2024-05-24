package com.ruijie.rcos.rcdc.rco.module.def.softwarecontrol.request;

import com.ruijie.rcos.rcdc.rco.module.def.api.dto.MatchEqual;
import com.ruijie.rcos.rcdc.rco.module.def.api.request.PageSearchRequest;
import com.ruijie.rcos.sk.webmvc.api.request.PageWebRequest;
import com.ruijie.rcos.sk.webmvc.api.vo.ExactMatch;
import org.springframework.util.Assert;

import java.util.Arrays;
import java.util.UUID;

/**
 * Description:
 * Copyright: Copyright (c) 2019
 * Company: Ruijie Co., Ltd.
 * Create Time: 2022/03/09 10:07
 *
 * @author chenli
 */
public class SoftwarePageSearchRequest extends PageSearchRequest {

    private final static String NAME_SEARCH_PARAM_NAME = "nameSearch";

    private final static String NAME_PARAM_NAME = "name";

    private final static String GROUP_ID_PARAM_NAME = "groupId";

    private final static String TOP_LEVEL_FILE_PARAM_NAME = "topLevelFile";

    private final static String PARENT_ID_PARAM_NAME = "parentId";

    public SoftwarePageSearchRequest(PageWebRequest webRequest) {
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
                case GROUP_ID_PARAM_NAME:
                    matchEqual = new MatchEqual(GROUP_ID_PARAM_NAME, exactMatch.toUUIDValue());
                    break;
                case TOP_LEVEL_FILE_PARAM_NAME:
                    matchEqual = new MatchEqual(TOP_LEVEL_FILE_PARAM_NAME, exactMatch.toBooleanValueArr());
                    break;
                case PARENT_ID_PARAM_NAME:
                    matchEqual = new MatchEqual(PARENT_ID_PARAM_NAME, exactMatch.toUUIDValue());
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
