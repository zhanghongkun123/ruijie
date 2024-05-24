package com.ruijie.rcos.rcdc.rco.module.web.ctrl.task.dto;

import com.ruijie.rcos.base.task.module.def.dto.ScheduleTaskTypeDTO;

/**
 * Description: Function Description
 * Copyright: Copyright (c) 2020
 * Company: Ruijie Co., Ltd.
 * Create Time: 2020/8/9
 *
 * @author hs
 */
public class ListScheduleTaskTypeDTO {

    private ScheduleTaskTypeDTO[] itemArr;

    public ListScheduleTaskTypeDTO(ScheduleTaskTypeDTO[] itemArr) {
        this.itemArr = itemArr;
    }

    public ScheduleTaskTypeDTO[] getItemArr() {
        return itemArr;
    }

    public void setItemArr(ScheduleTaskTypeDTO[] itemArr) {
        this.itemArr = itemArr;
    }
}
