package com.ruijie.rcos.rcdc.rco.module.impl.entity;

import javax.persistence.*;
import java.util.Date;
import java.util.UUID;

/**
 * Description: 搜索桌面信息视图实体类
 * Copyright: Copyright (c) 2018
 * Company: Ruijie Co., Ltd.
 * Create Time: 2018/12/3
 *
 * @author Jarman
 */
@Entity
@Table(name = "v_cbb_desktop_search")
public class ViewDesktopSearchEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID deskId;
    
    private UUID cbbDeskId;

    private String name;

    /**
     ** 桌面信息表的ip
     */
    private String deskIp;

    /**
     **桌面信息配置表的ip
     */
    private String ip;

    private Date timestamp;

    @Version
    private Integer version;

    private Boolean isDelete;

    /**
     ** 终端名称
     */
    private String terminalName;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDeskIp() {
        return deskIp;
    }

    public void setDeskIp(String deskIp) {
        this.deskIp = deskIp;
    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public Date getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Date timestamp) {
        this.timestamp = timestamp;
    }

    public UUID getDeskId() {
        return deskId;
    }

    public void setDeskId(UUID deskId) {
        this.deskId = deskId;
    }
    
    public UUID getCbbDeskId() {
        return cbbDeskId;
    }

    public void setCbbDeskId(UUID cbbDeskId) {
        this.cbbDeskId = cbbDeskId;
    }

    public Integer getVersion() {
        return version;
    }

    public void setVersion(Integer version) {
        this.version = version;
    }

    public Boolean getDelete() {
        return isDelete;
    }

    public void setDelete(Boolean delete) {
        isDelete = delete;
    }

    public String getTerminalName() {
        return terminalName;
    }

    public void setTerminalName(String terminalName) {
        this.terminalName = terminalName;
    }
}
