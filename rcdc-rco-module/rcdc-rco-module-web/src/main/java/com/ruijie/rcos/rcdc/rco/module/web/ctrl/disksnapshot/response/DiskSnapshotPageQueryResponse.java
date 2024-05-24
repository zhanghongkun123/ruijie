package com.ruijie.rcos.rcdc.rco.module.web.ctrl.disksnapshot.response;

import java.util.Arrays;
import java.util.Iterator;

import org.springframework.util.Assert;

import io.swagger.annotations.ApiModelProperty;

/**
 * Description: 获取磁盘快照列表响应
 * Copyright: Copyright (c) 2022
 * Company: Ruijie Co., Ltd.
 * Create Time: 2022年08月16日
 * 
 * @param <T> 响应数据
 * @author lyb
 */
public class DiskSnapshotPageQueryResponse<T> implements Iterable<T> {

    @ApiModelProperty(value = "行对象itemArr")
    private T[] itemArr;

    @ApiModelProperty(value = "分页总行数")
    private long total;

    @ApiModelProperty(value = "磁盘状态")
    private String diskState;

    public DiskSnapshotPageQueryResponse(T[] itemArr, long total) {
        Assert.notNull(itemArr, "itemArr must not be null");
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

    public String getDiskState() {
        return diskState;
    }

    public void setDiskState(String diskState) {
        this.diskState = diskState;
    }
}
