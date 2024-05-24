package com.ruijie.rcos.rcdc.rco.module.impl.cache;

/**
 * Description: Function Description
 * Copyright: Copyright (c) 2018
 * Company: Ruijie Co., Ltd.
 * Create Time: 2019/4/12
 *
 * @author Jarman
 */
public enum UserImportStateCache {

    STATE;

    private boolean state;

    /**
     * 是否正在导入中
     * @return true 导入中
     */
    public boolean isImporting() {
        return state;
    }

    /**
     * 添加导入任务
     */
    public void addTask() {
        this.state = true;
    }

    /**
     * 移除导入任务
     */
    public void removeTask() {
        this.state = false;
    }

}
