package com.ruijie.rcos.rcdc.rco.module.def.api.request;

import com.ruijie.rcos.sk.webmvc.api.request.PageWebRequest;

/**
 *
 * Description: Function Description
 * Copyright: Copyright (c) 2018
 * Company: Ruijie Co., Ltd.
 * Create Time: 2019年1月8日
 *
 * @author artom
 */
public class RcaHostPageSearchRequest extends DeskPageSearchRequest {

    public final static String RCA_POOL_ID = "rcaPoolId";

    public final static String DEFAULT_SORT_CREATE_TIME = "createTime";

    public RcaHostPageSearchRequest(PageWebRequest webRequest) {
        super(webRequest);
    }

}
