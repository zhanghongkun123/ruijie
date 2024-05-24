package com.ruijie.rcos.rcdc.rco.module.def.api;


import com.ruijie.rcos.rcdc.rco.module.def.api.request.SearchRequest;
import com.ruijie.rcos.rcdc.rco.module.def.api.response.SearchResultResponse;

/**
 * Description: 搜索接口
 * Copyright: Copyright (c) 2018
 * Company: Ruijie Co., Ltd.
 * Create Time: 2018/12/3
 *
 * @author Jarman
 */
public interface SearchAPI {

    /**
     * 搜索请求
     * 
     * @param request 搜索请求参数对象
     * @return 返回搜索结果
     */
    
    SearchResultResponse search(SearchRequest request);
}
