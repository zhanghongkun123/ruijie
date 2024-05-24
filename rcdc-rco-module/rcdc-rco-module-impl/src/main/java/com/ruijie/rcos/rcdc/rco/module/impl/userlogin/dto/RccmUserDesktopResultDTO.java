package com.ruijie.rcos.rcdc.rco.module.impl.userlogin.dto;

import com.ruijie.rcos.rcdc.rco.module.def.userlogin.dto.UnifiedUserDesktopResultDTO;

import java.util.List;

/**
 *
 * Description: 向rccm查询用户VDI云桌面的返回结果
 * Copyright: Copyright (c) 2021
 * Company: Ruijie Co., Ltd.
 * Create Time: 2021年09月27日
 *
 * @author linke
 */
public class RccmUserDesktopResultDTO {

    /**
     * 结果状态码：0成功，其他失败
     */
    private Integer resultCode;

    /**
     * 云桌面列表
     */
    private List<UnifiedUserDesktopResultDTO> desktopList;

    public Integer getResultCode() {
        return resultCode;
    }

    public void setResultCode(Integer resultCode) {
        this.resultCode = resultCode;
    }

    public List<UnifiedUserDesktopResultDTO> getDesktopList() {
        return desktopList;
    }

    public void setDesktopList(List<UnifiedUserDesktopResultDTO> desktopList) {
        this.desktopList = desktopList;
    }
}
