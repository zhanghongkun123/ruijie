package com.ruijie.rcos.rcdc.rco.module.web.ctrl.userprofile.request;

import com.ruijie.rcos.sk.base.annotation.NotNull;
import com.ruijie.rcos.sk.base.annotation.Range;
import com.ruijie.rcos.sk.base.support.EqualsHashcodeSupport;
import com.ruijie.rcos.sk.base.util.StringUtils;
import com.ruijie.rcos.sk.pagekit.api.Match;
import com.ruijie.rcos.sk.pagekit.api.PageQueryBuilderFactory;
import com.ruijie.rcos.sk.pagekit.api.PageQueryRequest;
import com.ruijie.rcos.sk.pagekit.api.Sort;
import com.ruijie.rcos.sk.pagekit.api.match.ExactMatch;
import com.ruijie.rcos.sk.pagekit.kernel.request.PageQueryConstant;
import org.apache.commons.lang3.ArrayUtils;
import org.springframework.lang.Nullable;
import org.springframework.util.Assert;

import java.util.Objects;
import java.util.UUID;

/**
 * Description:
 * Copyright: Copyright (c) 2023
 * Company: Ruijie Co., Ltd.
 * Create Time: 2024/3/20
 *
 * @author linke
 */
public class UserProfileStrategyListRequest extends EqualsHashcodeSupport implements PageQueryRequest {

    private static final String STRATEGY_ID = "strategyId";

    private static final String IMAGE_TEMPLATE_ID = "imageTemplateId";

    private static final String SESSION_TYPE = "sessionType";

    @Range(min = "0")
    private Integer page = PageQueryConstant.DEFAULT_PAGE;

    @Range(min = "1", max = "1000")
    private Integer limit = PageQueryConstant.DEFAULT_LIMIT;

    /**
     * 查询条件
     */
    @NotNull
    private Match[] matchArr = new Match[0];

    /**
     * 排序的字段
     */
    @NotNull
    private Sort[] sortArr = new Sort[0];

    @Nullable
    private UUID strategyId;

    @Nullable
    private UUID imageId;

    @Nullable
    private String sessionType;

    /**
     * 构建参数
     *
     * @param requestBuilder requestBuilder
     * @return PageQueryRequest
     */
    public PageQueryRequest buildPageQueryRequest(PageQueryBuilderFactory.RequestBuilder requestBuilder) {
        Assert.notNull(requestBuilder, "requestBuilder must not null");
        buildExactMatch(this.getMatchArr(), requestBuilder);
        Sort[] sortArr = this.getSortArr();
        if (sortArr != null && sortArr.length > 0) {
            Sort sort = sortArr[0];
            if (sort.getDirection() == Sort.Direction.ASC) {
                requestBuilder.asc(sort.getFieldName());
            } else {
                requestBuilder.desc(sort.getFieldName());
            }
        }

        return requestBuilder.build();
    }

    private void buildExactMatch(Match[] exactMatchArr, PageQueryBuilderFactory.RequestBuilder requestBuilder) {
        if (ArrayUtils.isEmpty(exactMatchArr)) {
            return;
        }
        for (Match exactMatch1 : exactMatchArr) {
            ExactMatch exactMatch = (ExactMatch) exactMatch1;
            if (StringUtils.equals(STRATEGY_ID, exactMatch.getFieldName())) {
                Object[] valueArr = exactMatch.getValueArr();
                if (ArrayUtils.isNotEmpty(valueArr)) {
                    this.strategyId = UUID.fromString(String.valueOf(exactMatch.getValueArr()[0]));
                }
                continue;
            }

            if (StringUtils.equals(IMAGE_TEMPLATE_ID, exactMatch.getFieldName())) {
                Object[] valueArr = exactMatch.getValueArr();
                if (ArrayUtils.isNotEmpty(valueArr)) {
                    this.imageId = UUID.fromString(String.valueOf(exactMatch.getValueArr()[0]));
                }
                continue;
            }

            if (StringUtils.equals(SESSION_TYPE, exactMatch.getFieldName())) {
                Object[] valueArr = exactMatch.getValueArr();
                if (ArrayUtils.isNotEmpty(valueArr) && Objects.nonNull(exactMatch.getValueArr()[0])) {
                    this.sessionType = String.valueOf(exactMatch.getValueArr()[0]);
                }
                continue;
            }
            requestBuilder.in(exactMatch.getFieldName(), exactMatch.getValueArr());
        }
    }

    @Override
    public int getLimit() {
        return limit;
    }

    public void setLimit(Integer limit) {
        this.limit = limit;
    }

    @Override
    public int getPage() {
        return page;
    }

    public void setPage(Integer page) {
        this.page = page;
    }

    @Override
    public Match[] getMatchArr() {
        return matchArr;
    }

    public void setMatchArr(Match[] matchArr) {
        this.matchArr = matchArr;
    }

    @Override
    public Sort[] getSortArr() {
        return sortArr;
    }

    public void setSortArr(Sort[] sortArr) {
        this.sortArr = sortArr;
    }

    @Nullable
    public UUID getStrategyId() {
        return strategyId;
    }

    public void setStrategyId(@Nullable UUID strategyId) {
        this.strategyId = strategyId;
    }

    @Nullable
    public UUID getImageId() {
        return imageId;
    }

    public void setImageId(@Nullable UUID imageId) {
        this.imageId = imageId;
    }

    @Nullable
    public String getSessionType() {
        return sessionType;
    }

    public void setSessionType(@Nullable String sessionType) {
        this.sessionType = sessionType;
    }
}
