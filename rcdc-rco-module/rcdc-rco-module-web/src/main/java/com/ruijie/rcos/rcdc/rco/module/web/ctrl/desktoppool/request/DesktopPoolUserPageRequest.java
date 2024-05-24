package com.ruijie.rcos.rcdc.rco.module.web.ctrl.desktoppool.request;

import java.util.Arrays;
import java.util.UUID;

import org.springframework.util.Assert;

import com.ruijie.rcos.gss.sdk.iac.module.def.enums.IacConfigRelatedType;
import com.ruijie.rcos.rcdc.rco.module.def.api.dto.MatchEqual;
import com.ruijie.rcos.rcdc.rco.module.def.api.request.PageSearchRequest;
import com.ruijie.rcos.sk.webmvc.api.request.PageWebRequest;
import com.ruijie.rcos.sk.webmvc.api.vo.ExactMatch;

/**
 * Description: 分页查询桌面池关联用户或用户组信息参数
 * Copyright: Copyright (c) 2021
 * Company: Ruijie Co., Ltd.
 * Create Time: 2021/10/22 14:39
 *
 * @author linke
 */
public class DesktopPoolUserPageRequest extends PageSearchRequest {

    public DesktopPoolUserPageRequest(PageWebRequest webRequest) {
        super(webRequest);
    }

    @Override
    protected MatchEqual[] exactMatchConvert(ExactMatch[] exactMatchArr) {
        Assert.notNull(exactMatchArr , "exactMatchArr must not be null");
        MatchEqual[] matchEqualArr = new MatchEqual[exactMatchArr.length];
                // 对条件匹配数组对象进行组装
        assembleMatchEqualArr(exactMatchArr, matchEqualArr);
        return matchEqualArr;
    }

    private void assembleMatchEqualArr(ExactMatch[] exactMatchArr, MatchEqual[] matchEqualArr) {
        for (int i = 0; i < exactMatchArr.length; i++) {
            ExactMatch exactMatch = exactMatchArr[i];
            MatchEqual matchEqual = null;
            switch (exactMatch.getName()) {
                case "relatedType":
                    matchEqual = new MatchEqual(exactMatch.getName(), Arrays.stream(exactMatch.getValueArr())
                            .map(IacConfigRelatedType::valueOf).toArray());
                    break;
                case "relatedId":
                case "desktopPoolId":
                case "id":
                    matchEqual = new MatchEqual(exactMatch.getName(), Arrays.stream(exactMatch.getValueArr()).map(UUID::fromString).toArray());
                    break;
                default:
                    matchEqual = new MatchEqual(exactMatch.getName(), exactMatch.getValueArr());
                    break;
            }
            matchEqualArr[i] = matchEqual;
        }
    }
}
