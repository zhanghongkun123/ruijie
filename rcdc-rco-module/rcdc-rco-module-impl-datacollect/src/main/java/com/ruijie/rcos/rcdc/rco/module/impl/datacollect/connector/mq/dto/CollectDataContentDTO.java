package com.ruijie.rcos.rcdc.rco.module.impl.datacollect.connector.mq.dto;

import com.alibaba.fastjson.JSON;

import java.util.List;

/**
 * <br>
 * Description: 数据收集总结果类 <br>
 * Copyright: Copyright (c) 2021 <br>
 * Company: Ruijie Co., Ltd. <br>
 * Create Time: 2021/6/21 <br>
 *
 * @author xwx
 */
public class CollectDataContentDTO {
    private List<SingleDataDTO> dataResultList;

    private String productLine;

    private String publicVersion;

    public String getPublicVersion() {
        return publicVersion;
    }

    public void setPublicVersion(String publicVersion) {
        this.publicVersion = publicVersion;
    }

    public String getProductLine() {
        return productLine;
    }

    public void setProductLine(String productLine) {
        this.productLine = productLine;
    }

    public List<SingleDataDTO> getDataResultList() {
        return dataResultList;
    }

    public void setDataResultList(List<SingleDataDTO> dataResultList) {
        this.dataResultList = dataResultList;
    }

    @Override
    public String toString() {
        return JSON.toJSONString(this);
    }
}
