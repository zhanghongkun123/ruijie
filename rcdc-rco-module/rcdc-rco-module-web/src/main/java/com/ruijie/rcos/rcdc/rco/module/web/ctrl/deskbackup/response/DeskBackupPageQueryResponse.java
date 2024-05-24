package com.ruijie.rcos.rcdc.rco.module.web.ctrl.deskbackup.response;

import io.swagger.annotations.ApiModelProperty;

import java.util.Arrays;
import java.util.Iterator;

/**
 * Description: description
 * Copyright: Copyright (c) 2021
 * Company: Ruijie Co., Ltd.
 * Create Time: 2021/5/10
 * 
 * @param <T> 响应数据
 * @author wuShengQiang
 */
public class DeskBackupPageQueryResponse<T> implements Iterable<T> {

    @ApiModelProperty(value = "行对象itemArr")
    private T[] itemArr;

    @ApiModelProperty(value = "分页总行数")
    private long total;

    @ApiModelProperty(value = "云桌面状态")
    private String desktopState;

    @Override
    public Iterator<T> iterator() {
        return Arrays.stream(this.itemArr).iterator();
    }

    public T[] getItemArr() {
        return itemArr;
    }

    public void setItemArr(T[] itemArr) {
        this.itemArr = itemArr;
    }

    public long getTotal() {
        return total;
    }

    public void setTotal(long total) {
        this.total = total;
    }

    public String getDesktopState() {
        return desktopState;
    }

    public void setDesktopState(String desktopState) {
        this.desktopState = desktopState;
    }
}
