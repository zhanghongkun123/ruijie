package com.ruijie.rcos.rcdc.rco.module.impl.softwarecontrol.entity;

import com.ruijie.rcos.rcdc.rco.module.def.softwarecontrol.enums.SoftwareGroupTypeEnum;
import com.ruijie.rcos.sk.base.support.EqualsHashcodeSupport;

import javax.persistence.*;
import java.util.UUID;

/**
 * Description: Function Description
 * Copyright: Copyright (c) 2021
 * Company: Ruijie Co., Ltd.
 * Create Time: 2021年6月21日
 *
 * @author lihengjing
 */
@Entity
@Table(name = "t_rco_software_group")
public class RcoSoftwareGroupEntity extends EqualsHashcodeSupport {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;


    /**
     * 软件分组名称
     **/
    private String name;

    /**
     * 软件分组类型
     **/
    @Enumerated(EnumType.STRING)
    private SoftwareGroupTypeEnum groupType;

    /**
     * 软件分组描述
     **/
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
