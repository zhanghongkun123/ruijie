package com.ruijie.rcos.rcdc.rco.module.openapi.rest.unifiedlogin.response;

import java.util.List;

import com.ruijie.rcos.rcdc.rco.module.def.api.dto.QueryDesktopItemDTO;

/**
 *
 * Description: 获取用户桌面列表openApi返回结果
 * Copyright: Copyright (c) 2021
 * Company: Ruijie Co., Ltd.
 * Create Time: 2021年09月27日
 *
 * @author linke
 */
public class UserDesktopQueryResponse {

    private int resultCode;

    private List<QueryDesktopItemDTO> desktopList;

    private String clusterVersion;

    public int getResultCode() {
        return resultCode;
    }

    public void setResultCode(int resultCode) {
        this.resultCode = resultCode;
    }

    public List<QueryDesktopItemDTO> getDesktopList() {
        return desktopList;
    }

    public void setDesktopList(List<QueryDesktopItemDTO> desktopList) {
        this.desktopList = desktopList;
    }

    public String getClusterVersion() {
        return clusterVersion;
    }

    public void setClusterVersion(String clusterVersion) {
        this.clusterVersion = clusterVersion;
    }
}
