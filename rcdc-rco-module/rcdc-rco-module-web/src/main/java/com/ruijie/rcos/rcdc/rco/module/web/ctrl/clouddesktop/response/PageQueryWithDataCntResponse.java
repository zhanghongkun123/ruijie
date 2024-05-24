package com.ruijie.rcos.rcdc.rco.module.web.ctrl.clouddesktop.response;

/**
 * Description: 分页查询
 * Copyright: Copyright (c) 2023
 * Company: Ruijie Co., Ltd.
 * Create Time: 2023/12/21
 *
 * @param <T> 分页数据
 * @author Jarman
 */
public class PageQueryWithDataCntResponse<T> {


    /**
     * 数据统计 -不包含查询条件数据总数
     */
    private Long dataTotal;

    /**
     * 数据记录
     */
    private T[] itemArr;

    /**
     * 总条数
     */
    private long total;
    
    public PageQueryWithDataCntResponse(T[] itemArr, long total, Long dataTotal) {
        this.itemArr = itemArr;
        this.total = total;
        this.dataTotal = dataTotal;
    }



    public Long getDataTotal() {
        return dataTotal;
    }

    public void setDataTotal(Long dataTotal) {
        this.dataTotal = dataTotal;
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
}
