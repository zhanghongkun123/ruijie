package com.ruijie.rcos.rcdc.rco.module.web.ctrl.clouddesktop.request.driver;

import com.ruijie.rcos.sk.base.annotation.NotNull;
import com.ruijie.rcos.sk.webmvc.api.request.WebRequest;
import java.util.UUID;

/**
 * Description: 获取硬件版本请求
 * Copyright: Copyright (c) 2024
 * Company: Ruijie Co., Ltd.
 * Create Time: 2024/01/29
 *
 * @author lifeng
 */
public class ListHardwareVersionRequest implements WebRequest {

    @NotNull
    private UUID id;

    @NotNull
    private String productModel;


    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getProductModel() {
        return productModel;
    }

    public void setProductModel(String productModel) {
        this.productModel = productModel;
    }
}
