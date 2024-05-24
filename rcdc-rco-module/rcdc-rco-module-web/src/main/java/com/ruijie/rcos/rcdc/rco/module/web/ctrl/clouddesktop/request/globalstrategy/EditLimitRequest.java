package com.ruijie.rcos.rcdc.rco.module.web.ctrl.clouddesktop.request.globalstrategy;

import com.ruijie.rcos.rcdc.rco.module.def.api.dto.IpLimitDTO;
import com.ruijie.rcos.sk.base.annotation.NotNull;
import com.ruijie.rcos.sk.webmvc.api.request.WebRequest;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import org.springframework.lang.Nullable;

import java.util.List;

/**
 * Description: 修改VDI IP限制请求
 * Copyright: Copyright (c) 2021
 * Company: Ruijie Co., Ltd.
 * Create Time: 2021/7/27 21:53
 *
 * @author yxq
 */
@ApiModel("编辑VDI网段限制请求")
public class EditLimitRequest implements WebRequest {

    @ApiModelProperty("是否开启IP网段限制")
    @NotNull
    private Boolean enableIpLimit;

    @ApiModelProperty("IP限制网段")
    @Nullable
    private List<IpLimitDTO> ipLimitDTOList;

    public Boolean getEnableIpLimit() {
        return enableIpLimit;
    }

    public void setEnableIpLimit(Boolean enableIpLimit) {
        this.enableIpLimit = enableIpLimit;
    }


    public List<IpLimitDTO> getIpLimitDTOList() {
        return ipLimitDTOList;
    }

    public void setIpLimitDTOList(List<IpLimitDTO> ipLimitDTOList) {
        this.ipLimitDTOList = ipLimitDTOList;
    }
}
