package com.ruijie.rcos.rcdc.rco.module.def.api.response.usersnapshot;

import org.springframework.util.Assert;

import java.util.Arrays;
import java.util.Iterator;

/**
 * Description: UserSnapshotPageQueryResponse 快照列表分页返回对象
 * Copyright: Copyright (c) 2022
 * Company: Ruijie Co., Ltd.
 * Create Time: 2022/12/28
 *
 * @param <T> 响应参数
 * @author liusd
 */
public class UserSnapshotPageQueryResponse<T> implements Iterable<T> {

    /**
     *行对象itemArr
     */
    private T[] itemArr;

    /**
     * 分页总行数
     */
    private long total;

    /**
     * 云桌面状态
     */
    private String desktopState;

    public UserSnapshotPageQueryResponse() {

    }

    public UserSnapshotPageQueryResponse(T[] itemArr, long total) {
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
