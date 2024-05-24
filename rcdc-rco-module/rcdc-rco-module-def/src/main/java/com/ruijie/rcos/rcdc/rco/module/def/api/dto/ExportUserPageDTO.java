package com.ruijie.rcos.rcdc.rco.module.def.api.dto;

import java.util.List;

/**
 * Description: 用户导出分页数据对象
 * Copyright: Copyright (c) 2023
 * Company: Ruijie Co., Ltd.
 * Create Time: 2023/03/08
 * @param <T> 指定返回列表对象类型
 * @author liusd
 */
public class ExportUserPageDTO<T> {

    private List<T> exportUserViewDTOList;

    private int totalPages;

    private long totalElements;

    public long getTotalElements() {
        return totalElements;
    }

    public void setTotalElements(long totalElements) {
        this.totalElements = totalElements;
    }

    public int getTotalPages() {
        return totalPages;
    }

    public void setTotalPages(int totalPages) {
        this.totalPages = totalPages;
    }

    public List<T> getExportUserViewDTOList() {
        return exportUserViewDTOList;
    }

    public void setExportUserViewDTOList(List<T> exportUserViewDTOList) {
        this.exportUserViewDTOList = exportUserViewDTOList;
    }
}
