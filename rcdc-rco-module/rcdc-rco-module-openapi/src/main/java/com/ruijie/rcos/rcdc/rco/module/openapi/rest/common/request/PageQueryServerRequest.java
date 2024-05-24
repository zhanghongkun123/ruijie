package com.ruijie.rcos.rcdc.rco.module.openapi.rest.common.request;

import com.ruijie.rcos.rcdc.rco.module.def.api.dto.MatchEqual;
import com.ruijie.rcos.rcdc.rco.module.def.api.request.PageSearchRequest;
import com.ruijie.rcos.rcdc.rco.module.openapi.rest.RestErrorCode;
import com.ruijie.rcos.rcdc.rco.module.openapi.rest.common.constant.Constant;
import com.ruijie.rcos.sk.base.annotation.NotNull;
import com.ruijie.rcos.sk.base.annotation.Range;
import com.ruijie.rcos.sk.base.exception.BusinessException;
import com.ruijie.rcos.sk.base.support.EqualsHashcodeSupport;
import com.ruijie.rcos.sk.base.util.SqlUtil;
import com.ruijie.rcos.sk.pagekit.api.PageQueryRequest;
import com.ruijie.rcos.sk.pagekit.kernel.request.criteria.DefaultExactMatch;
import com.ruijie.rcos.sk.pagekit.kernel.request.criteria.DefaultSort;
import com.ruijie.rcos.sk.webmvc.api.vo.Sort;
import org.springframework.lang.Nullable;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

/**
 * Description: 分页查询云桌面列表请求体
 * Copyright: Copyright (c) 2019
 * Company: Ruijie Co., Ltd.
 * Create Time: 2021/9/24 14:22
 *
 * @author lyb
 */
public class PageQueryServerRequest extends EqualsHashcodeSupport implements PageQueryRequest {

    /**
     * 页码 page * limit不能超过2147483647
     */
    @Range(min = "0", max = "2147483")
    private int page = 0;

    /**
     * 每页数据条数
     */
    @Range(min = "1", max = "1000")
    private int limit = 1;

    /**
     * 查询条件数组
     */
    @NotNull
    private DefaultExactMatch[] matchArr = new DefaultExactMatch[0];

    /**
     * 排序字段
     */
    @NotNull
    private DefaultSort[] sortArr = new DefaultSort[0];

    @Nullable
    private String searchKeyword;

    public int getPage() {
        return page;
    }

    public void setPage(Integer page) {
        this.page = page;
    }

    public int getLimit() {
        return limit;
    }

    public void setLimit(int limit) {
        this.limit = limit;
    }

    public DefaultExactMatch[] getMatchArr() {
        return matchArr;
    }

    public void setMatchArr(DefaultExactMatch[] matchArr) {
        this.matchArr = matchArr;
    }

    public DefaultSort[] getSortArr() {
        return sortArr;
    }

    public void setSortArr(DefaultSort[] sortArr) {
        this.sortArr = sortArr;
    }

    @Nullable
    public String getSearchKeyword() {
        return SqlUtil.resolveSpecialCharacters(this.searchKeyword);
    }

    public void setSearchKeyword(@Nullable String searchKeyword) {
        this.searchKeyword = searchKeyword;
    }

    /**
     * 将查询请求转换为业务层格式
     *
     * @return 业务层格式请求体
     * @throws BusinessException 异常
     */
    public PageSearchRequest convert() throws BusinessException {
        PageSearchRequest request = new PageSearchRequest();
        request.setPage(this.getPage());
        request.setLimit(this.getLimit());
        validate(this.getMatchArr());
        request.setMatchEqualArr(toMatchEqual(this.getMatchArr()));
        request.setSortArr(toSorts(this.getSortArr()));
        request.setSearchKeyword(this.getSearchKeyword());
        return request;
    }

    /**
     * 将查询请求转换为cbb业务层格式
     * 
     * @return 业务层格式请求体
     * @throws BusinessException 异常
     */
    public com.ruijie.rcos.rcdc.clouddesktop.module.def.api.request.PageSearchRequest convertCbb() throws BusinessException {
        com.ruijie.rcos.rcdc.clouddesktop.module.def.api.request.PageSearchRequest request =
                new com.ruijie.rcos.rcdc.clouddesktop.module.def.api.request.PageSearchRequest();
        request.setPage(this.getPage());
        request.setLimit(this.getLimit());
        validate(this.getMatchArr());
        request.setMatchEqualArr(toMatchEqualCbb(this.getMatchArr()));
        List<Sort> sortList = new ArrayList<>();
        if (this.getSortArr().length > 0) {
            sortList.add(toSort(this.getSortArr()[0]));
        }
        request.setSortList(sortList);
        return request;
    }

    /**
     * 校验匹配条件
     * 
     * @param matches 匹配条件数组
     * @throws BusinessException 异常
     */
    private void validate(DefaultExactMatch[] matches) throws BusinessException {
        if (matches != null) {
            for (DefaultExactMatch match : matches) {
                if (match.getMatchRule() != DefaultExactMatch.Rule.EQ || match.getType() != com.ruijie.rcos.sk.pagekit.api.Match.Type.EXACT) {
                    throw new BusinessException(RestErrorCode.OPEN_API_DESK_PAGE_QUERY_MATCH_RULE_OR_TYPE_INVALID);
                }
            }
        }
    }

    /**
     * 转换
     * 
     * @param matches 匹配条件
     * @return 转换后的匹配条件
     */
    protected MatchEqual[] toMatchEqual(DefaultExactMatch[] matches) {
        MatchEqual[] matchEqualArr = null;
        if (matches != null && matches.length > 0) {
            matchEqualArr = Arrays.stream(matches).map(match -> {
                Object[] valueArr = Arrays.stream(match.getValueArr()).map(value -> {
                    if (value instanceof Boolean) {
                        return ((Boolean) value).booleanValue();
                    }
                    if (Constant.DESK_ID.equals(match.getFieldName())) {
                        return UUID.fromString((String) value);
                    }
                    return value;
                }).toArray();
                if (Constant.DESK_ID.equals(match.getFieldName())) {
                    match.setFieldName(Constant.CBB_DESK_ID);
                }
                if (Constant.DESK_TYPE.equals(match.getFieldName())) {
                    match.setFieldName(Constant.PATTERN);
                }
                return new MatchEqual(match.getFieldName(), valueArr);
            }).toArray(MatchEqual[]::new);
        }
        return matchEqualArr;
    }

    /**
     * 转换
     * 
     * @param matches 匹配条件
     * @return 转换后的匹配条件
     */
    private com.ruijie.rcos.rcdc.clouddesktop.module.def.dto.MatchEqual[] toMatchEqualCbb(DefaultExactMatch[] matches) {
        com.ruijie.rcos.rcdc.clouddesktop.module.def.dto.MatchEqual[] matchEqualArr = null;
        if (matches != null && matches.length > 0) {
            matchEqualArr = Arrays.stream(matches)
                    .map(match -> new com.ruijie.rcos.rcdc.clouddesktop.module.def.dto.MatchEqual(match.getFieldName(), match.getValueArr()))
                    .toArray(com.ruijie.rcos.rcdc.clouddesktop.module.def.dto.MatchEqual[]::new);
        }
        return matchEqualArr;
    }

    /**
     * 转换排序条件
     * 
     * @param sortItemArr 排序字段数组
     * @return 转换后的
     */
    private Sort[] toSorts(DefaultSort[] sortItemArr) {
        Sort[] sortArr;
        if (sortItemArr != null && sortItemArr.length > 0) {
            sortArr = Arrays.stream(sortItemArr).map(s -> {
                Sort sort = new Sort();
                sort.setSortField(s.getFieldName());
                sort.setDirection(Sort.Direction.valueOf(s.getDirection().name()));
                return sort;
            }).toArray(Sort[]::new);
        } else {
            sortArr = new Sort[0];
        }
        return sortArr;
    }

    /**
     * 转换排序条件
     * 
     * @param defaultSort 排序字段
     * @return 转换后的
     */
    private Sort toSort(DefaultSort defaultSort) {
        Sort sort = new Sort();
        sort.setSortField(defaultSort.getFieldName());
        sort.setDirection(Sort.Direction.valueOf(defaultSort.getDirection().name()));
        return sort;
    }
}
