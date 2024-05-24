package com.ruijie.rcos.rcdc.rco.module.impl.entity;

import com.ruijie.rcos.rcdc.rco.module.def.api.wifi.dto.WifiWhitelistDTO;
import org.springframework.beans.BeanUtils;
import org.springframework.util.Assert;

import javax.persistence.*;
import java.util.Date;
import java.util.UUID;

/**
 * @ClassName: WifiWhitelistEntity
 * @Description: entity for wifi whitelist
 * @author: zhiweiHong
 * @date: 2020/7/29
 **/
@Entity
@Table(name = "t_rco_wifi_whitelist")
public class WifiWhitelistEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;

    @Version
    private Integer version;

    /**
     * wifi名称
     */
    private String ssid;

    /**
     * 绑定terminal group id
     */
    private UUID terminalGroupId;

    /**
     * 当本条目属于列表的一份子时,
     * <code>index<code/> 是 列表中的下标
     */
    private Integer index;

    /**
     * 记录创建时间
     */
    private Date createTime;

    public WifiWhitelistEntity(WifiWhitelistDTO dto, UUID terminalGroupId) {
        Assert.notNull(dto, "wifiWhitelistDTO not be null");
        BeanUtils.copyProperties(dto, this);
        this.terminalGroupId = terminalGroupId;
        this.createTime = new Date();
    }

    public WifiWhitelistEntity() {

    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public Integer getVersion() {
        return version;
    }

    public void setVersion(Integer version) {
        this.version = version;
    }

    public String getSsid() {
        return ssid;
    }

    public void setSsid(String ssid) {
        this.ssid = ssid;
    }

    public UUID getTerminalGroupId() {
        return terminalGroupId;
    }

    public void setTerminalGroupId(UUID terminalGroupId) {
        this.terminalGroupId = terminalGroupId;
    }

    public int getIndex() {
        return index;
    }

    public void setIndex(int index) {
        this.index = index;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }
}
