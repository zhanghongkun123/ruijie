package com.ruijie.rcos.rcdc.rco.module.impl.service;

import com.google.common.collect.Lists;
import com.ruijie.rcos.rcdc.rco.module.def.api.dto.SearchHitContent;
import com.ruijie.rcos.rcdc.rco.module.def.api.dto.SearchResultDTO;
import com.ruijie.rcos.rcdc.rco.module.def.api.request.PageSearchRequest;
import com.ruijie.rcos.rcdc.rco.module.def.api.request.SearchRequest;
import com.ruijie.rcos.sk.base.log.Logger;
import com.ruijie.rcos.sk.base.log.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.util.Assert;

import java.util.Collections;
import java.util.List;

/**
 * Description: 搜索抽象模板
 * Copyright: Copyright (c) 2018
 * Company: Ruijie Co., Ltd.
 * Create Time: 2018/12/3
 * 
 * @param <T> 表实体类
 * @author Jarman
 */
public abstract class AbstractSearchTemplate<T> extends AbstractPageQueryTemplate<T> {

    private static final Logger LOGGER = LoggerFactory.getLogger(AbstractSearchTemplate.class);

    /**
     * 搜索
     * 
     * @param request 搜索请求参数
     * @param clz 搜索对应的Entity实体类
     * @return 返回搜索结果
     */
    public final SearchResultDTO search(SearchRequest request, Class<T> clz) {
        Assert.notNull(request, "SearchRequest不能为null");
        Assert.notNull(clz, "SearchRequest不能为null");
        // 转义后的字符串，用户数据库查询
        String escapeKeyword = request.getEscapeKeyword();
        int page = request.getPage();
        int limit = request.getLimit();
        PageSearchRequest pageSearchRequest = new PageSearchRequest();
        pageSearchRequest.setPage(page);
        pageSearchRequest.setLimit(limit);
        pageSearchRequest.setSearchKeyword(escapeKeyword);
        pageSearchRequest.setMatchEqualArr(request.getMatchEqualArr());
        pageSearchRequest.setIsAnd(request.getIsAnd());
        Page<T> pageResult = pageQuery(pageSearchRequest, clz);
        if (pageResult.getTotalElements() == 0) {
            LOGGER.debug("没有搜索到关键字为{}的用户信息", request.getKeyword());
            return new SearchResultDTO(0L, Collections.emptyList());
        }
        List<T> resultList = pageResult.getContent();
        List<SearchHitContent> hitContentList = Lists.newArrayList();
        for (int i = 0; i < resultList.size(); i++) {
            T entity = resultList.get(i);
            // 需要传转移前的字符串才能正确匹配实际的匹配字段
            SearchHitContent hitContent = matchHitContent(entity, request.getKeyword());
            if (hitContent != null) {
                hitContentList.add(hitContent);
            }
        }
        return new SearchResultDTO(pageResult.getTotalElements(), hitContentList);
    }

    /**
     * 找到匹配的列和对应的id
     */
    protected abstract SearchHitContent matchHitContent(T t, String keyword);
}
