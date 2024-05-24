package com.ruijie.rcos.rcdc.rco.module.web.ctrl.sysmanage.response;


import com.ruijie.rcos.rcdc.maintenance.module.def.dto.CbbTaskDTO;
import com.ruijie.rcos.rcdc.maintenance.module.def.dto.enums.BusinessMaintenanceStatusEnums;
import com.ruijie.rcos.rcdc.rco.module.web.ctrl.sysmanage.dto.DeskProgressInfo;
import com.ruijie.rcos.rcdc.rco.module.web.ctrl.sysmanage.dto.ImageProgressInfo;

/**
 *
 * Description: 业务维护模式
 * Copyright: Copyright (c) 2018
 * Company: Ruijie Co., Ltd.
 * Create Time: 2022/7/6
 *
 * @author zhiweiHong
 */
public class BusinessMaintenanceProgressResponse {

    private DeskProgressInfo desk;

    private ImageProgressInfo image;

    private CbbTaskDTO[] taskArr;

    private BusinessMaintenanceStatusEnums status;

    public DeskProgressInfo getDesk() {
        return desk;
    }

    public void setDesk(DeskProgressInfo desk) {
        this.desk = desk;
    }

    public ImageProgressInfo getImage() {
        return image;
    }

    public void setImage(ImageProgressInfo image) {
        this.image = image;
    }


    public BusinessMaintenanceStatusEnums getStatus() {
        return status;
    }

    public void setStatus(BusinessMaintenanceStatusEnums status) {
        this.status = status;
    }

    public CbbTaskDTO[] getTaskArr() {
        return taskArr;
    }

    public void setTaskArr(CbbTaskDTO[] taskArr) {
        this.taskArr = taskArr;
    }
}
