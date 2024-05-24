package com.ruijie.rcos.rcdc.rco.module.impl.filedistribution.connector.rest.request;

import com.alibaba.fastjson.annotation.JSONField;
import com.ruijie.rcos.sk.base.annotation.NotBlank;
import com.ruijie.rcos.sk.base.annotation.NotEmpty;

/**
 * Description: 云应用列表筛选DTO
 * Copyright: Copyright (c) 2021
 * Company: Ruijie Co., Ltd.
 * Create Time: 2022/4/7 11:15
 *
 * @author chenjuan
 */
public class AppClientExactMatchDTO {

    @NotBlank
    @JSONField(name = "name")
    private String name;

    @NotEmpty
    @JSONField(name = "valueArr")
    private String[] valueArr;

    public AppClientExactMatchDTO() {
    }

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String[] getValueArr() {
        return this.valueArr;
    }


    public void setValueArr(String[] valueArr) {
        this.valueArr = valueArr;
    }


}
