package com.ruijie.rcos.rcdc.rco.module.impl.datacollect.connector.mq.entity;

import javax.persistence.*;
import java.util.UUID;

/**
 * <br>
 * Description: 数据收集查询SQL <br>
 * Copyright: Copyright (c) 2021 <br>
 * Company: Ruijie Co., Ltd. <br>
 * Create Time: 2021/6/21 <br>
 *
 * @author xwx
 */
@Entity
@Table(name = "t_rcc_increment_mark")
public class IncrementMarkEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;

    private String itemKey;

    @Version
    private Integer version;

    private String mark;

    public IncrementMarkEntity(String itemKey, String mark) {
        this.itemKey = itemKey;
        this.mark = mark;
    }

    public IncrementMarkEntity() {

    }

    public String getItemKey() {
        return itemKey;
    }

    public void setItemKey(String itemKey) {
        this.itemKey = itemKey;
    }

    public Integer getVersion() {
        return version;
    }

    public void setVersion(Integer version) {
        this.version = version;
    }

    public String getMark() {
        return mark;
    }

    public void setMark(String mark) {
        this.mark = mark;
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }
}
