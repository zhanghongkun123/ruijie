package com.ruijie.rcos.rcdc.rco.module.web.ctrl.user.request;

import com.ruijie.rcos.sk.base.annotation.NotNull;
import com.ruijie.rcos.sk.webmvc.api.vo.IdLabelEntry;

/**
 * 
 * Description: 回收站恢复桌面
 * Copyright: Copyright (c) 2018
 * Company: Ruijie Co., Ltd.
 * Create Time: 2020年6月12日
 *
 * @author zhuangchenwu
 */
public class RecoverByAssignUserWebRequest extends IdArrWebRequest {

    /**
     * 指定用户id
     */
    @NotNull
    private IdLabelEntry userInfo;

    public IdLabelEntry getUserInfo() {
        return userInfo;
    }

    public void setUserInfo(IdLabelEntry userInfo) {
        this.userInfo = userInfo;
    }
    
}
