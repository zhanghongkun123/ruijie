package com.ruijie.rcos.rcdc.rco.module.def.api;

import com.ruijie.rcos.rcdc.clouddesktop.module.def.dto.CbbDeskDTO;
import com.ruijie.rcos.rcdc.rca.module.def.api.dto.RcaAppPoolBaseDTO;
import com.ruijie.rcos.sk.base.batch.BatchTaskItem;
import com.ruijie.rcos.sk.base.exception.BusinessException;
import org.springframework.lang.Nullable;

/**
 * <p>Title: 应用池操作</p>
 * <p>Description: Function Description</p>
 * <p>Copyright: Ruijie Co., Ltd. (c) 2020</p>
 * <p>@Author: zhengjingyong</p>
 * <p>@Date: 2024/2/2 14:51</p>
 */
public interface RcoAppPoolAPI {


    /**
     * 应用池的规格到桌面
     *
     * @param rcaAppPoolBaseDTO 应用池
     * @param desktopInfo vdi桌面
     * @throws BusinessException 业务异常
     */
    void syncSpec(RcaAppPoolBaseDTO rcaAppPoolBaseDTO, CbbDeskDTO desktopInfo) throws BusinessException;

    /**
     * 应用池的镜像模板到vdi桌面
     *
     * @param rcaAppPoolBaseDTO 应用池
     * @param desktopInfo vdi桌面
     * @param taskItem 任务
     * @throws BusinessException 业务异常
     */
    void syncImageTemplate(RcaAppPoolBaseDTO rcaAppPoolBaseDTO, CbbDeskDTO desktopInfo, @Nullable BatchTaskItem taskItem) throws BusinessException;

    /**
     * 应用池的网络策略到vdi桌面
     *
     * @param rcaAppPoolBaseDTO 应用池
     * @param desktopInfo vdi桌面
     * @throws BusinessException 业务异常
     */
    void syncNetworkStrategy(RcaAppPoolBaseDTO rcaAppPoolBaseDTO, CbbDeskDTO desktopInfo) throws BusinessException;

    /**
     * 应用池的云应用到桌面
     *
     * @param rcaAppPoolBaseDTO 应用池
     * @param desktopInfo vdi桌面
     * @throws BusinessException 业务异常
     */
    void syncMainStrategy(RcaAppPoolBaseDTO rcaAppPoolBaseDTO, CbbDeskDTO desktopInfo) throws BusinessException;



}
