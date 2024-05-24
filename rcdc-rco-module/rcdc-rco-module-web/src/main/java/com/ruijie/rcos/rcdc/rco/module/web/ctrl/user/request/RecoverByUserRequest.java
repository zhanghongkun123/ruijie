package com.ruijie.rcos.rcdc.rco.module.web.ctrl.user.request;

import org.springframework.lang.Nullable;
import com.ruijie.rcos.sk.base.annotation.NotNull;
import com.ruijie.rcos.sk.webmvc.api.request.WebRequest;
import com.ruijie.rcos.sk.webmvc.api.vo.IdLabelEntry;

/**
 * 
 * Description: 恢复回收站请求
 * Copyright: Copyright (c) 2019
 * Company: Ruijie Co., Ltd.
 * Create Time: 2019年1月17日
 * 
 * @author Ghang
 */
public class RecoverByUserRequest implements WebRequest {

    @Nullable
    private IdLabelEntry[] recycleDesktopArr;
    
    @NotNull
    private String userId;

    public IdLabelEntry[] getRecycleDesktopArr() {
        return recycleDesktopArr;
    }

    public void setRecycleDesktopArr(IdLabelEntry[] recycleDesktopArr) {
        this.recycleDesktopArr = recycleDesktopArr;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

}
