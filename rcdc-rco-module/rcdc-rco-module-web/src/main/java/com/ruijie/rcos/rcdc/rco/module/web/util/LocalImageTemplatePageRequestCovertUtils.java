package com.ruijie.rcos.rcdc.rco.module.web.util;

import com.ruijie.rcos.rcdc.clouddesktop.module.def.api.request.image.LocalImageTemplatePageRequest;
import com.ruijie.rcos.rcdc.rco.module.web.ctrl.clouddesktop.request.image.ListImageWebRequest;
import com.ruijie.rcos.sk.pagekit.api.Match;
import com.ruijie.rcos.sk.pagekit.api.match.CompositeMatch;
import com.ruijie.rcos.sk.webmvc.api.request.PageWebRequest;
import org.springframework.util.Assert;

import java.util.Arrays;
import java.util.Optional;

/**
 * Description: Function Description
 * Copyright: Copyright (c) 2023
 * Company: Ruijie Co., Ltd.
 * Create Time: 2023年07月18日
 *
 * @author xgx
 */
public class LocalImageTemplatePageRequestCovertUtils {
    /**
     * 转换
     *
     * @param request 请求对象
     * @return LocalImageTemplatePageRequest
     */
    public static final LocalImageTemplatePageRequest covert(ListImageWebRequest request) {
        Assert.notNull(request, "request can not be null");
        PageWebRequest pageWebRequest = new PageWebRequest();
        pageWebRequest.setPage(request.getPage());
        pageWebRequest.setLimit(request.getLimit());
        pageWebRequest.setSearchKeyword(request.getSearchKeyword());
        pageWebRequest.setExactMatchArr(request.getExactMatchArr());

        LocalImageTemplatePageRequest pageSearchRequest = new LocalImageTemplatePageRequest(pageWebRequest);
        // fixme 现在不管是不是查询条件都加入到分页查询参数里，由于这个实体涉及多个接口影响面广，项目周期短验证有风险，固采取折中方案修改
        Match[] matchArr = Optional.ofNullable(request.getMatchArr()).orElse(new Match[0]);
        matchArr = Arrays.stream(matchArr).filter(item -> item instanceof CompositeMatch).toArray(Match[]::new);
        pageSearchRequest.setMatchArr(matchArr);
        return pageSearchRequest;
    }
}
