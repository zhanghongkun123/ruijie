package com.ruijie.rcos.rcdc.rco.module.web.ctrl.desksnapshot.response;

import io.swagger.annotations.ApiModelProperty;
import org.springframework.util.Assert;

import java.util.Arrays;
import java.util.Iterator;

/**
 * Description:
 * Copyright: Copyright (c) 2020
 * Company: Ruijie Co., Ltd.
 * Create Time: 2020/09/23
 *
 * @param <T> 响应数据
 * @author luojianmo
 */
public class DeskSnapshotPageQueryResponse<T> implements Iterable<T> {

    @ApiModelProperty(value = "行对象itemArr")
    private T[] itemArr;

    @ApiModelProperty(value = "分页总行数")
    private long total;

    @ApiModelProperty(value = "云桌面状态")
    private String desktopState;

    public DeskSnapshotPageQueryResponse() {

    }

    public DeskSnapshotPageQueryResponse(T[] itemArr, long total) {
        Assert.notNull(itemArr, "the itemArr must not be null");
        Assert.isTrue(total >= 0L, "total must greater than 0");
        this.itemArr = itemArr;
        this.total = total;
    }

    public T[] getItemArr() {
        return this.itemArr;
    }

    public void setItemArr(T[] itemArr) {
        this.itemArr = itemArr;
    }

    public long getTotal() {
        return this.total;
    }

    public void setTotal(long total) {
        this.total = total;
    }

    /**
     * iterator
     * 
     * @return 行对象
     */
    public Iterator<T> iterator() {
        return Arrays.stream(this.itemArr).iterator();
    }

    public String getDesktopState() {
        return desktopState;
    }

    public void setDesktopState(String desktopState) {
        this.desktopState = desktopState;
    }
}
