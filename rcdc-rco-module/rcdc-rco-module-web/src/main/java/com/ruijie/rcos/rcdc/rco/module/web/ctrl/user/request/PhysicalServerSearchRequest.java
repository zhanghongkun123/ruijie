package com.ruijie.rcos.rcdc.rco.module.web.ctrl.user.request;

import com.ruijie.rcos.rcdc.clouddesktop.module.def.enums.CbbServerStatusType;
import org.springframework.util.Assert;

import com.ruijie.rcos.rcdc.clouddesktop.module.def.api.request.PageSearchRequest;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.dto.MatchEqual;
import com.ruijie.rcos.sk.webmvc.api.request.PageWebRequest;
import com.ruijie.rcos.sk.webmvc.api.vo.ExactMatch;

/**
 * Description: PhysicalServerSearchRequest
 * Copyright: Copyright (c) 2019
 * Company: Ruijie Co., Ltd.
 * Create Time: 2019-07-29
 *
 * @author hli
 */
public class PhysicalServerSearchRequest extends PageSearchRequest {

    public PhysicalServerSearchRequest(PageWebRequest webRequest) {
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
                case "status":
                    boolean enableStatus = Boolean.parseBoolean(exactMatch.getValueArr()[0]);
                    matchEqual = new MatchEqual("status", new Object[] {enableStatus});
                    break;
                case "serverStatus":
                    CbbServerStatusType[] serverStatusTypeArr = new CbbServerStatusType[exactMatch.getValueArr().length];
                    for (int j = 0; j < exactMatch.getValueArr().length; j++) {
                        serverStatusTypeArr[j] = CbbServerStatusType.valueOf(exactMatch.getValueArr()[j]);
                    }
                    matchEqual = new MatchEqual("serverStatus", serverStatusTypeArr);
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
