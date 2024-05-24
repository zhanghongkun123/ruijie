package com.ruijie.rcos.rcdc.rco.module.impl.entity;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Version;
import java.util.UUID;

/**
 * Description: 文件分发任务参数实体类
 * Copyright: Copyright (c) 2022
 * Company: Ruijie Co., Ltd.
 * Create Time: 2022/2/16 18:07
 *
 * @author zhangyichi
 */
@Entity
@Table(name = "t_rco_distribute_parameter")
public class DistributeParameterEntity {

    @Id
    private UUID id;

    private String parameter;

    @Version
    private int version;

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getParameter() {
        return parameter;
    }

    public void setParameter(String parameter) {
        this.parameter = parameter;
    }

    public int getVersion() {
        return version;
    }

    public void setVersion(int version) {
        this.version = version;
    }

}
