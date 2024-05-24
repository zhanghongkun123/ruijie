package com.ruijie.rcos.rcdc.rco.module.impl.softwarecontrol.entity;

import com.ruijie.rcos.rcdc.rco.module.def.softwarecontrol.enums.SoftwareGroupTypeEnum;

import javax.persistence.*;
import java.util.UUID;

/**
 * Description:
 * Copyright: Copyright (c) 2019
 * Company: Ruijie Co., Ltd.
 * Create Time: 2020/12/16 19:57
 *
 * @author ketb
 */
@Entity
@Table(name = "v_rco_software_group_count")
public class ViewRcoSoftwareGroupCountEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;

    private String name;

    @Enumerated(EnumType.STRING)
    private SoftwareGroupTypeEnum groupType;

    private Long count;

    private String description;

    @Version
    private int version;

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public SoftwareGroupTypeEnum getGroupType() {
        return groupType;
    }

    public void setGroupType(SoftwareGroupTypeEnum groupType) {
        this.groupType = groupType;
    }

    public Long getCount() {
        return count;
    }

    public void setCount(Long count) {
        this.count = count;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getVersion() {
        return version;
    }

    public void setVersion(int version) {
        this.version = version;
    }
}
