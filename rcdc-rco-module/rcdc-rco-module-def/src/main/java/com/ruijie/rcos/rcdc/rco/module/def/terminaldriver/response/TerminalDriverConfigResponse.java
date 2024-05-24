package com.ruijie.rcos.rcdc.rco.module.def.terminaldriver.response;


import java.util.Date;
import java.util.UUID;

/**
 * Description: 终端驱动安装配置实体
 * Copyright: Copyright (c) 2020
 * Company: Ruijie Co., Ltd.
 * Create Time: 2021/02/02
 *
 * @author luojianmo
 */
public class TerminalDriverConfigResponse {

    private UUID id;

    private String productModel;

    private String productId;

    private String driverType;

    private Date createTime;

    private Date updateTime;


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

    public String getProductId() {
        return productId;
    }

    public void setProductId(String productId) {
        this.productId = productId;
    }

    public String getDriverType() {
        return driverType;
    }

    public void setDriverType(String driverType) {
        this.driverType = driverType;
    }

}
