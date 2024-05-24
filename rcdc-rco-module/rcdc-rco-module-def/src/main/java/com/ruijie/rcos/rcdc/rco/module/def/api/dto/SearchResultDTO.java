package com.ruijie.rcos.rcdc.rco.module.def.api.dto;

import java.util.List;

/**
 * Description: Function Description
 * Copyright: Copyright (c) 2018
 * Company: Ruijie Co., Ltd.
 * Create Time: 2018/12/3
 *
 * @author Jarman
 */
public class SearchResultDTO {

    private Long hitCount;

    private List<SearchHitContent> searchResultList;

    public SearchResultDTO(Long hitCount, List<SearchHitContent> searchResultList) {
        this.hitCount = hitCount;
        this.searchResultList = searchResultList;
    }

    public Long getHitCount() {
        return hitCount;
    }

    public void setHitCount(Long hitCount) {
        this.hitCount = hitCount;
    }

    public List<SearchHitContent> getSearchResultList() {
        return searchResultList;
    }

    public void setSearchResultList(List<SearchHitContent> searchResultList) {
        this.searchResultList = searchResultList;
    }
}
