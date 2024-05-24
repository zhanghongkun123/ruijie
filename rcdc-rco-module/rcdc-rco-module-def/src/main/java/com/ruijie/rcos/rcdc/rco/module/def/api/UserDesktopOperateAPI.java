package com.ruijie.rcos.rcdc.rco.module.def.api;

import com.ruijie.rcos.rcdc.clouddesktop.module.def.api.request.deskoperate.CbbRestoreDeskRequest;
import com.ruijie.rcos.rcdc.rco.module.def.api.request.CloudDesktopRebootRequest;
import com.ruijie.rcos.rcdc.rco.module.def.api.request.CloudDesktopShutdownRequest;
import com.ruijie.rcos.rcdc.rco.module.def.api.request.CloudDesktopStartRequest;
import com.ruijie.rcos.sk.base.exception.BusinessException;
import com.ruijie.rcos.sk.modulekit.api.comm.IdRequest;

import java.util.List;
import java.util.UUID;


/**
 * Description: 云桌面操作接口
 * Copyright: Copyright (c) 2018
 * Company: Ruijie Co., Ltd.
 * Create Time: 2019/7/10
 *
 * @author Jarman
 */
public interface UserDesktopOperateAPI {

    /**
     ** 云桌面关机
     *
     * @param request 请求参数
     * @throws BusinessException 业务异常
     */
    
    void shutdown(CloudDesktopShutdownRequest request) throws BusinessException;

    /**
     ** 云桌面开机
     *
     * @param request 页面请求参数：uuids
     * @throws BusinessException 业务异常
     */
    
    void start(CloudDesktopStartRequest request) throws BusinessException;

    /**
     ** 还原云桌面
     *
     * @param request 页面请求参数：uuid
     * @throws BusinessException 业务异常
     */
    
    void restore(CbbRestoreDeskRequest request) throws BusinessException;

    /**
     * *云桌面故障恢复
     *
     * @param request id
     * @throws BusinessException 业务异常
     */
    
    void failback(IdRequest request) throws BusinessException;

    /**
     * idv开机
     *
     * @param request 开机参数
     * @Date 2021/12/8 17:09
     * @Author zjy
     * @throws BusinessException 业务异常
     **/
    void startIdv(CloudDesktopStartRequest request) throws BusinessException;


    /**
     * 关机IDV
     *
     * @param request 关机参数
     * @Date 2021/12/8 10:13
     * @Author zjy
     * @throws BusinessException 业务异常
     **/
    void shutdownIdv(CloudDesktopShutdownRequest request) throws BusinessException;

    /**
     * 根据桌面id缓存是否自动进入虚机
     *
     * @param terminalId 终端id
     * @Date 2022/1/18 15:25
     * @Author zjy
     **/
    void updateDeskAutoStartVmCache(String terminalId);

    /**
     * 是否自动进入虚机
     *
     * @param terminalId 终端id
     * @Date 2022/1/18 15:39
     * @Author zjy
     * @return 返回值
     **/
    boolean checkAutoStartVmFromCache(String terminalId);

    /**
     * 删除自动进入虚机标识
     *
     * @param terminalId 终端id
     * @Date 2022/1/18 15:39
     * @Author zjy
     **/
    void deleteDeskAutoStartVmCache(String terminalId);

    /**
     ** 云桌面重启
     *
     * @param request 请求参数
     * @throws BusinessException 业务异常
     */
    void rebootDeskVDI(CloudDesktopRebootRequest request) throws BusinessException;

    /**
     * 第三方桌面重启
     * @param deskId deskId
     * @throws BusinessException 业务异常
     */
    void rebootDeskThird(UUID deskId) throws BusinessException;

    /**
     * 云桌面维护模式
     * @param desktopIds 云桌面id
     * @param isOpen true：打开云桌面维护模式，false：关闭云桌面维护模式
     * @throws BusinessException 业务异常
     */
    void changeDeskMaintenanceModel(List<UUID> desktopIds, Boolean isOpen) throws BusinessException;

    /**
     * 检查云桌面维护模式
     *
     * @param desktopId 云桌面ID
     * @throws BusinessException ex
     */
    void checkDesktopMaintenanceReady(UUID desktopId) throws BusinessException;

    /**
     * 通知应用主机云桌面休眠
     * @param request id
     * @throws BusinessException 业务异常
     */
    void sleepRcaHost(IdRequest request) throws BusinessException;

}
