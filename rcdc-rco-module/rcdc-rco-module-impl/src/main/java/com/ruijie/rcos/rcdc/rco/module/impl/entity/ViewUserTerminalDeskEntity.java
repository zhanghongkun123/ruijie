package com.ruijie.rcos.rcdc.rco.module.impl.entity;

import java.util.UUID;

import javax.persistence.*;

/**
 * Description: 分组终端列表详情视图
 * Copyright: Copyright (c) 2018
 * Company: Ruijie Co., Ltd.
 * Create Time: 2018年12月21日
 *
 * @author nt
 */
@Entity
@Table(name = "v_cbb_user_terminal_desk")
public class ViewUserTerminalDeskEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;


    /**
     * 终端ID
     */
    private String terminalId;

    /**
     * 桌面名称
     */

    private String desktopName;


    /**
     * 桌面ID
     */
    private UUID cbbDesktopId;


    @Version
    private int version;

    private Boolean hasTerminalRunning;


    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getDesktopName() {
        return desktopName;
    }

    public void setDesktopName(String desktopName) {
        this.desktopName = desktopName;
    }

    public UUID getCbbDesktopId() {
        return cbbDesktopId;
    }

    public void setCbbDesktopId(UUID cbbDesktopId) {
        this.cbbDesktopId = cbbDesktopId;
    }

    public int getVersion() {
        return version;
    }

    public void setVersion(int version) {
        this.version = version;
    }

    public String getTerminalId() {
        return terminalId;
    }

    public void setTerminalId(String terminalId) {
        this.terminalId = terminalId;
    }

    public Boolean getHasTerminalRunning() {
        return hasTerminalRunning;
    }

    public void setHasTerminalRunning(Boolean hasTerminalRunning) {
        this.hasTerminalRunning = hasTerminalRunning;
    }
}
