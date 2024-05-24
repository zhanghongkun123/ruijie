package com.ruijie.rcos.rcdc.rco.module.def.api.request;

import com.ruijie.rcos.rcdc.rco.module.def.api.dto.MatchEqual;
import com.ruijie.rcos.sk.base.annotation.NotBlank;
import com.ruijie.rcos.sk.modulekit.api.comm.DefaultPageRequest;
import org.springframework.lang.Nullable;

/**
 * Description: Function Description
 * Copyright: Copyright (c) 2018
 * Company: Ruijie Co., Ltd.
 * Create Time: 2018/12/3
 *
 * @author Jarman
 */
public class SearchRequest extends DefaultPageRequest {

    @NotBlank
    private String keyword;
    
    /** 转义后的字符串 */
    @NotBlank
    private String escapeKeyword;

    @Nullable
    private MatchEqual[] matchEqualArr;
    
    /**精确查找 多条件查询采用and还是or(and:true, or:false) */
    @Nullable
    private Boolean isAnd;

    public String getKeyword() {
        return keyword;
    }

    public void setKeyword(String keyword) {
        this.keyword = keyword;
    }

    @Nullable
    public MatchEqual[] getMatchEqualArr() {
        return matchEqualArr;
    }

    public void setMatchEqualArr(@Nullable MatchEqual[] matchEqualArr) {
        this.matchEqualArr = matchEqualArr;
    }

    public String getEscapeKeyword() {
        return escapeKeyword;
    }

    public void setEscapeKeyword(String escapeKeyword) {
        this.escapeKeyword = escapeKeyword;
    }

    public Boolean getIsAnd() {
        return isAnd;
    }

    public void setIsAnd(Boolean isAnd) {
        this.isAnd = isAnd;
    }
    
    
}
