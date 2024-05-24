package com.ruijie.rcos.rcdc.rco.module.impl.entity;

import com.ruijie.rcos.rcdc.clouddesktop.module.def.enums.CbbCloudDeskType;

import javax.persistence.*;
import java.util.UUID;

/**
 * Description: Function Description
 * Copyright: Copyright (c) 2018
 * Company: Ruijie Co., Ltd.
 * Create Time: 2018/12/6
 *
 * @author Jarman
 */
@Entity
@Table(name = "v_cbb_desktop_info_stat")
public class ViewDesktopInfoStatEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID deskId;

    private String deskState;

    @Enumerated(EnumType.STRING)
    private CbbCloudDeskType deskType;

    @Version
    private Integer version;

    private UUID userGroupId;

    private UUID terminalGroupId;

    public UUID getDeskId() {
        return deskId;
    }

    public void setDeskId(UUID deskId) {
        this.deskId = deskId;
    }

    public String getDeskState() {
        return deskState;
    }

    public void setDeskState(String deskState) {
        this.deskState = deskState;
    }

    public CbbCloudDeskType getDeskType() {
        return deskType;
    }

    public void setDeskType(CbbCloudDeskType deskType) {
        this.deskType = deskType;
    }

    public Integer getVersion() {
        return version;
    }

    public void setVersion(Integer version) {
        this.version = version;
    }

    public UUID getUserGroupId() {
        return userGroupId;
    }

    public void setUserGroupId(UUID userGroupId) {
        this.userGroupId = userGroupId;
    }

    public UUID getTerminalGroupId() {
        return terminalGroupId;
    }

    public void setTerminalGroupId(UUID terminalGroupId) {
        this.terminalGroupId = terminalGroupId;
    }
}
