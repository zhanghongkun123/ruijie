package com.ruijie.rcos.rcdc.rco.module.impl.api.searchhandler;


import com.ruijie.rcos.rcdc.rco.module.def.api.dto.SearchResultDTO;
import com.ruijie.rcos.rcdc.rco.module.def.api.request.SearchRequest;

/**
 * Description: Function Description
 * Copyright: Copyright (c) 2018
 * Company: Ruijie Co., Ltd.
 * Create Time: 2019/9/11
 *
 * @author nt
 */
public interface SearchHandler {

    /**
     *  首页顶部查询
     *
     * @param request 查询请求参数
     * @return 查询结果
     */
    SearchResultDTO search(SearchRequest request);
}
