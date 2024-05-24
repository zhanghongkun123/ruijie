package com.ruijie.rcos.rcdc.rco.module.web.ctrl.rcaapphost.request;

import com.google.common.collect.Lists;
import com.ruijie.rcos.rcdc.rca.module.def.dto.MatchEqual;
import com.ruijie.rcos.rcdc.rca.module.def.request.PageSearchRequest;
import com.ruijie.rcos.rcdc.rco.module.web.ctrl.rcaapphost.enums.QueryAppHostTypeEnum;
import com.ruijie.rcos.sk.webmvc.api.request.PageWebRequest;
import com.ruijie.rcos.sk.webmvc.api.vo.ExactMatch;
import org.apache.commons.lang3.StringUtils;
import org.springframework.util.Assert;

import java.util.Arrays;
import java.util.List;
import java.util.UUID;

/**
 * Description:
 * Copyright: Copyright (c) 2021
 * Company: Ruijie Co., Ltd.
 * Create Time: 2024年02月21日
 *
 * @author liuwc
 */
public class RcaHostAppPageRequest extends PageSearchRequest {

    private static final String HOST_ID = "hostId";

    private static final String IS_BLOCKED = "isBlocked";

    private static final String QUERY_APP_HOST_TYPE = "queryAppHostType";

    private static final String IS_MULTIPLE_IMAGE = "isMultipleImage";

    private UUID hostId;

    private Boolean isMultipleImage;

    private QueryAppHostTypeEnum hostQueryType;

    public RcaHostAppPageRequest(PageWebRequest webRequest) {
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
                case HOST_ID:
                    matchEqual = new MatchEqual(exactMatch.getName(), Arrays.stream(exactMatch.getValueArr()).map(UUID::fromString).toArray());
                    matchEqualList.add(matchEqual);
                    setHostId((UUID) matchEqual.getValueArr()[0]);
                    break;
                case IS_BLOCKED:
                    matchEqual = new MatchEqual(exactMatch.getName(), Arrays.stream(exactMatch.getValueArr())
                            .map(Boolean::valueOf).toArray());
                    matchEqualList.add(matchEqual);
                    break;
                case IS_MULTIPLE_IMAGE:
                    String[] isMultipleImageArr = exactMatch.getValueArr();
                    if (!StringUtils.isEmpty(isMultipleImageArr[0])) {
                        this.setIsMultipleImage(Boolean.valueOf(isMultipleImageArr[0]));
                    }
                    break;
                case QUERY_APP_HOST_TYPE:
                    String[] queryAppHostTypeArr = exactMatch.getValueArr();
                    if (!StringUtils.isEmpty(queryAppHostTypeArr[0])) {
                        this.setHostQueryType(QueryAppHostTypeEnum.valueOf(queryAppHostTypeArr[0]));
                    }
                    break;
                default:
                    matchEqual = new MatchEqual(exactMatch.getName(), exactMatch.getValueArr());
                    matchEqualList.add(matchEqual);
                    break;
            }
        }
        return matchEqualList;
    }

    public UUID getHostId() {
        return hostId;
    }

    public void setHostId(UUID hostId) {
        this.hostId = hostId;
    }

    public Boolean getIsMultipleImage() {
        return isMultipleImage;
    }

    public void setIsMultipleImage(Boolean multipleImage) {
        isMultipleImage = multipleImage;
    }

    public QueryAppHostTypeEnum getHostQueryType() {
        return hostQueryType;
    }

    public void setHostQueryType(QueryAppHostTypeEnum hostQueryType) {
        this.hostQueryType = hostQueryType;
    }
}
