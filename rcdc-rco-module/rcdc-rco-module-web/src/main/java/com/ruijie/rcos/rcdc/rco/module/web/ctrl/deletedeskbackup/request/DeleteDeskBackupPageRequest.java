package com.ruijie.rcos.rcdc.rco.module.web.ctrl.deletedeskbackup.request;

import com.ruijie.rcos.sk.base.annotation.NotNull;
import com.ruijie.rcos.sk.pagekit.kernel.request.criteria.DefaultSort;
import com.ruijie.rcos.sk.webmvc.api.request.PageWebRequest;
import org.springframework.lang.Nullable;

/**
 * Description:
 * Copyright: Copyright (c)
 * Company: Ruijie Co., Ltd.
 * Create Time: 2023/10/13 16:30
 *
 * @author zhangsiming
 */
public class DeleteDeskBackupPageRequest extends PageWebRequest {

    @Nullable
    private String searchKeyword;

    @NotNull
    private DefaultSort[] sortArr = new DefaultSort[0];

    @Override
    public String getSearchKeyword() {
        return searchKeyword;
    }

    @Override
    public void setSearchKeyword(String searchKeyword) {
        this.searchKeyword = searchKeyword;
    }

    public DefaultSort[] getSortArr() {
        return sortArr;
    }

    public void setSortArr(DefaultSort[] sortArr) {
        this.sortArr = sortArr;
    }
}
