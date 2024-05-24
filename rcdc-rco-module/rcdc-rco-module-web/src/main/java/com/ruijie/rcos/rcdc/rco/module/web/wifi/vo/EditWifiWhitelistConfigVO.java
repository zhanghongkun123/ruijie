package com.ruijie.rcos.rcdc.rco.module.web.wifi.vo;

import com.ruijie.rcos.rcdc.rco.module.web.ctrl.terminal.vo.WifiWhitelistConfigVO;
import io.swagger.annotations.ApiModelProperty;
import org.springframework.lang.Nullable;

import java.util.ArrayList;

/**
 *
 * Description: 创建白名单配置实体
 * Copyright: Copyright (c) 2018
 * Company: Ruijie Co., Ltd.
 * Create Time: 2020/10/18
 *
 * @author zhiweiHong
 */
public class EditWifiWhitelistConfigVO extends WifiWhitelistConfigVO {

    @Nullable
    @ApiModelProperty(value = "是否需要应用分组")
    private Boolean needApplyToSubgroup;

    public EditWifiWhitelistConfigVO() {
        this.needApplyToSubgroup = false;
        this.setWifiWhiteList(new ArrayList<>());
    }

    public Boolean getNeedApplyToSubgroup() {
        return needApplyToSubgroup;
    }

    public void setNeedApplyToSubgroup(Boolean needApplyToSubgroup) {
        this.needApplyToSubgroup = needApplyToSubgroup;
    }
}
