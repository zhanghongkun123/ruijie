package com.ruijie.rcos.rcdc.rco.module.def.desktoppool.dto;

import java.util.List;

/**
 *
 * Description: 桌面池与终端关系表示DTO
 * Copyright: Copyright (c) 2024
 * Company: Ruijie Co., Ltd.
 * Create Time: 2024/1/30
 *
 * @author zqj
 */
public class DesktopPoolComputerDetailDTO {

    private List<DesktopPoolComputerDTO> computerList;

    private List<DesktopPoolComputerDTO> computerGroupList;

    public List<DesktopPoolComputerDTO> getComputerList() {
        return computerList;
    }

    public void setComputerList(List<DesktopPoolComputerDTO> computerList) {
        this.computerList = computerList;
    }

    public List<DesktopPoolComputerDTO> getComputerGroupList() {
        return computerGroupList;
    }

    public void setComputerGroupList(List<DesktopPoolComputerDTO> computerGroupList) {
        this.computerGroupList = computerGroupList;
    }
}
