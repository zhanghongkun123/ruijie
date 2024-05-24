package com.ruijie.rcos.rcdc.rco.module.web.ctrl.terminal.response;

import com.ruijie.rcos.sk.webmvc.api.response.DefaultWebResponse;

/**
 * Description: Function Description
 * Copyright: Copyright (c) 2020
 * Company: Ruijie Co., Ltd.
 * Create Time: 2020/4/15 18:02
 *
 * @param <T> 内容类型
 * @author zhangyichi
 */
public class TerminalUpgradeItemArrayWebResponse<T> extends DefaultWebResponse {

    private T[] itemArr;

    private Long total;

    public T[] getItemArr() {
        return itemArr;
    }

    public void setItemArr(T[] itemArr) {
        this.itemArr = itemArr;
    }

    public Long getTotal() {
        return total;
    }

    public void setTotal(Long total) {
        this.total = total;
    }
}
