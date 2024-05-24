package com.ruijie.rcos.rcdc.rco.module.impl.global;

/**
 * 回收站定期清理
 * <br>
 * Description: Function Description <br>
 * Copyright: Copyright (c) 2017 <br>
 * Company: Ruijie Co., Ltd. <br>
 * Create Time: 2019年6月19日 <br>
 * 
 * @author dan
 */
public interface RecycleBinGlobal {

    /**
     * 是否启用回收站定期清理
     */
    String RECYCLE_BIN_STATE = "recycleBinState";

    /**
     * 回收站清理周期
     */
    String RECYCLE_BIN_CYCLE = "recycleBinPeriod";
}
