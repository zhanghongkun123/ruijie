package com.ruijie.rcos.rcdc.rco.module.web.ctrl.user.bactchtask;

import com.ruijie.rcos.rcdc.clouddesktop.module.def.api.CbbIDVDeskMgmtAPI;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.api.CbbIDVDeskOperateAPI;
import com.ruijie.rcos.rcdc.rco.module.def.api.UserDesktopMgmtAPI;
import com.ruijie.rcos.rcdc.rco.module.def.api.UserDesktopOperateAPI;
import com.ruijie.rcos.rcdc.rco.module.web.service.CloudDesktopWebService;
import com.ruijie.rcos.rcdc.task.ext.module.def.batch.AbstractProgressAwareBatchTaskHandler;
import com.ruijie.rcos.sk.base.batch.BatchTaskItem;

import java.util.Collection;
import java.util.Iterator;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * 云桌面管理异步任务抽象类
 * Description: Function Description
 * Copyright: Copyright (c) 2018
 * Company: Ruijie Co., Ltd.
 * Create Time: 2019年4月11日
 * 
 * @author zhuangchenwu
 */
public abstract class AbstractDesktopBatchTaskHandler extends AbstractProgressAwareBatchTaskHandler {

    protected CloudDesktopWebService cloudDesktopWebService;

    protected UserDesktopMgmtAPI cloudDesktopMgmtAPI;

    protected UserDesktopOperateAPI cloudDesktopOperateAPI;

    protected CbbIDVDeskOperateAPI cbbIDVDeskOperateAPI;

    protected CbbIDVDeskMgmtAPI cbbIDVDeskMgmtAPI;

    protected String userName;

    protected String desktopName;

    /** 处理的记录数累计值 */
    protected AtomicInteger processItemCount = new AtomicInteger(0);

    protected AbstractDesktopBatchTaskHandler(Collection<? extends BatchTaskItem> batchTaskItemCollection) {
        super(batchTaskItemCollection);
    }

    protected AbstractDesktopBatchTaskHandler(Iterator<? extends BatchTaskItem> batchTaskItemIterator) {
        super(batchTaskItemIterator);
    }

    /**
     * 获取CloudDesktopWebService实例
     * 
     * @param cloudDesktopWebService cloudDesktopWebService实例
     * @return 获取的实例
     */
    public abstract AbstractDesktopBatchTaskHandler setCloudDesktopWebService(CloudDesktopWebService cloudDesktopWebService);

    /**
     * 获取cloudDesktopMgmtAPI实例
     * 
     * @param cloudDesktopMgmtAPI cloudDesktopMgmtAPI实例
     * @return 获取的实例
     */
    public abstract AbstractDesktopBatchTaskHandler setCbbUserDesktopMgmtAPI(UserDesktopMgmtAPI cloudDesktopMgmtAPI);

    /**
     * 获取CbbUserDesktopOperateAPI实例
     * 
     * @param cloudDesktopOperateAPI CbbUserDesktopOperateAPI实例
     * @return 获取的实例
     */
    public abstract AbstractDesktopBatchTaskHandler setCbbUserDesktopOperateAPI(UserDesktopOperateAPI cloudDesktopOperateAPI);

    /**
     * 获取CbbIDVDeskOperateAPI实例
     * 
     * @param cbbIDVDeskOperateAPI CbbIDVDeskOperateAPI
     * @return 获取的实例
     */
    public abstract AbstractDesktopBatchTaskHandler setCbbIDVDeskOperateAPI(CbbIDVDeskOperateAPI cbbIDVDeskOperateAPI);

    /**
     * 获取cbbIDVDeskMgmtAPI实例
     *
     * @param cbbIDVDeskMgmtAPI CbbIDVDeskMgmtAPI
     * @return 获取的实例
     */
    public abstract AbstractDesktopBatchTaskHandler setcbbIDVDeskMgmtAPI(CbbIDVDeskMgmtAPI cbbIDVDeskMgmtAPI);


    @Override
    public boolean supportProgressAware() {
        return false;
    }
}
