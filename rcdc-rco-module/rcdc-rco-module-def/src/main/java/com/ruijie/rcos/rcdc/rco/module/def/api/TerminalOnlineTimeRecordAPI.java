package com.ruijie.rcos.rcdc.rco.module.def.api;

import com.ruijie.rcos.rcdc.rco.module.def.api.response.BigScreenPlantTreeResponse;

/**
 * Description: 终端在线时间数据统计
 * Copyright: Copyright (c) 2018
 * Company: Ruijie Co., Ltd.
 * Create Time: 2023/3/5
 *
 * @author xiao'yong'deng
 */
public interface TerminalOnlineTimeRecordAPI {

    /**
     * 查询终端在线数据信息，应用于大屏展示
     * 实现碳排放，节省资源等数据返回
     * @return 返回转化后的资源情况
     *
     */
    BigScreenPlantTreeResponse findPlantTree();
}
