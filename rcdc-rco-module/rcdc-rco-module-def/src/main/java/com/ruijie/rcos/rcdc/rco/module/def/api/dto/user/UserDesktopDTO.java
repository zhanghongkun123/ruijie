package com.ruijie.rcos.rcdc.rco.module.def.api.dto.user;

import com.ruijie.rcos.sk.base.annotation.NotBlank;
import org.springframework.lang.Nullable;

import java.util.List;

/**
 * Description: 用户桌面查询条件
 * Copyright: Copyright (c) 2022
 * Company: Ruijie Co., Ltd.
 * Create Time: 2022-04-20
 *
 * @author zqj
 */
public class UserDesktopDTO {

    @NotBlank
    private String userName;

    @Nullable
    private String terminalId;

    /**
     * 桌面模式: IDV/VDI 允许查询多种模式，默认空数组查询所有
     */
    @Nullable
    private List<String> desktopCategoryList;

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    @Nullable
    public String getTerminalId() {
        return terminalId;
    }

    public void setTerminalId(@Nullable String terminalId) {
        this.terminalId = terminalId;
    }

    public List<String> getDesktopCategoryList() {
        return desktopCategoryList;
    }

    public void setDesktopCategoryList(List<String> desktopCategoryList) {
        this.desktopCategoryList = desktopCategoryList;
    }
}
