package com.ruijie.rcos.rcdc.rco.module.web.ctrl.bigscreen.vo;

import java.util.List;

/**
 * Description: 异常虚机列表VO
 * Copyright: Copyright (c) 2019
 * Company: Ruijie Co., Ltd.
 * Create Time: 2019/7/24 15:16
 *
 * @author BaiGuoliang
 */
public class AbnormalDesktopVO {

    private List<String> nameList;

    private Integer total;

    public List<String> getNameList() {
        return nameList;
    }

    public void setNameList(List<String> nameList) {
        this.nameList = nameList;
    }

    public Integer getTotal() {
        return total;
    }

    public void setTotal(Integer total) {
        this.total = total;
    }
}
