package com.ruijie.rcos.rcdc.rco.module.web.ctrl.adgroup.request;

import java.util.Arrays;
import java.util.List;
import java.util.UUID;

import com.ruijie.rcos.gss.sdk.iac.module.def.dto.MatchEqual;
import org.apache.commons.lang3.ArrayUtils;
import org.springframework.util.Assert;

import com.google.common.collect.Lists;
import com.ruijie.rcos.gss.sdk.iac.module.def.api.request.IacPageSearchRequest;
import com.ruijie.rcos.sk.base.log.Logger;
import com.ruijie.rcos.sk.base.log.LoggerFactory;
import com.ruijie.rcos.sk.webmvc.api.request.PageWebRequest;
import com.ruijie.rcos.sk.webmvc.api.vo.ExactMatch;

/**
 * Description: 分页查询安全组
 * Copyright: Copyright (c) 2022
 * Company: Ruijie Co., Ltd.
 * Create Time: 2022/9/27
 *
 * @author ZQJ
 */
public class AdGroupIacPageRequest extends IacPageSearchRequest {


    private static final Logger LOGGER = LoggerFactory.getLogger(AdGroupIacPageRequest.class);

    private static final String ID = "id";

    private static final String USER_ID = "userId";

    private UUID userId;

    public AdGroupIacPageRequest(PageWebRequest webRequest) {
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
                case USER_ID:
                    if (ArrayUtils.isNotEmpty(exactMatch.getValueArr())) {
                        userId = UUID.fromString(exactMatch.getValueArr()[0]);
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

    public UUID getUserId() {
        return userId;
    }

    public void setUserId(UUID userId) {
        this.userId = userId;
    }

    /**
     * 增加id的matchEqual
     * @param adGroupIdArr 安全组id列表
     */
    public void addIdMatchEqual(UUID[] adGroupIdArr) {
        Assert.notNull(adGroupIdArr, "adGroupIdArr can not be null");

        MatchEqual[] matchEqualArr = this.getMatchEqualArr();
        if (matchEqualArr == null) {
            matchEqualArr = new MatchEqual[0];
        }

        MatchEqual[] newArr = new MatchEqual[matchEqualArr.length + 1];
        System.arraycopy(matchEqualArr, 0, newArr, 0, matchEqualArr.length);

        MatchEqual addMatchEqual = new MatchEqual();
        addMatchEqual.setName("id");
        addMatchEqual.setValueArr(adGroupIdArr);
        newArr[matchEqualArr.length] = addMatchEqual;

        this.setMatchEqualArr(newArr);
    }
}
