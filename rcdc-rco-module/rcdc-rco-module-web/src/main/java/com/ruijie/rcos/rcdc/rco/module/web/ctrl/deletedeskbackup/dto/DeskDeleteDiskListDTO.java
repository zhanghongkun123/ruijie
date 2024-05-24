package com.ruijie.rcos.rcdc.rco.module.web.ctrl.deletedeskbackup.dto;

/**
 * Description: description
 * Copyright: Copyright (c) 2022
 * Company: Ruijie Co., Ltd.
 * Create Time: 2021/02/28
 *
 * @author guoyongxin
 */
public class DeskDeleteDiskListDTO {

    private DeskDeleteDiskDTO[] itemArr;

    private Integer total;

    public DeskDeleteDiskDTO[] getItemArr() {
        return itemArr;
    }

    public void setItemArr(DeskDeleteDiskDTO[] itemArr) {
        this.itemArr = itemArr;
    }

    public Integer getTotal() {
        return total;
    }

    public void setTotal(Integer total) {
        this.total = total;
    }
}
