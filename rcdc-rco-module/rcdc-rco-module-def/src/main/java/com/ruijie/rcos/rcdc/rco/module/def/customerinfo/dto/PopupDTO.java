package com.ruijie.rcos.rcdc.rco.module.def.customerinfo.dto;

/**
 * <br>
 * Description: 客户信息弹窗 <br>
 * Copyright: Copyright (c) 2021 <br>
 * Company: Ruijie Co., Ltd. <br>
 * Create Time: 2021/6/7 <br>
 *
 * @author xwx
 */
public class PopupDTO {
    private boolean needPopup;

    /**
     * 重命名的get方法
     * @return 是否需要弹窗
     */
    public boolean getNeedPopup() {
        return needPopup;
    }

    public void setNeedPopup(boolean needPopup) {
        this.needPopup = needPopup;
    }
}
