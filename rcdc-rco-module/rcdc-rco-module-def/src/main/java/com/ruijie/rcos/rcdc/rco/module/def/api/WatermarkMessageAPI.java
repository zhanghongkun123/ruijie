package com.ruijie.rcos.rcdc.rco.module.def.api;

import com.ruijie.rcos.rcdc.clouddesktop.module.def.dto.CbbWatermarkConfigDTO;
import com.ruijie.rcos.rcdc.rca.module.def.dto.RcaHostDTO;
import com.ruijie.rcos.rcdc.rco.module.def.api.dto.CloudDesktopDetailDTO;
import com.ruijie.rcos.rcdc.rco.module.def.api.dto.RcaHostDesktopDTO;
import com.ruijie.rcos.sk.base.exception.BusinessException;
import org.springframework.lang.Nullable;

import java.util.List;
import java.util.UUID;


/**
 * Description: Function Description
 * Copyright: Copyright (c) 2018
 * Company: Ruijie Co., Ltd.
 * Create Time: 2019年9月2日
 *
 * @author XiaoJiaXin
 */
public interface WatermarkMessageAPI {

    /**
     * 下发屏幕水印配置
     *
     * @throws BusinessException 业务异常
     */
    void send() throws BusinessException;

    /**
     * 通知配置更新
     *
     * @Date 2022/3/8 10:57
     * @Author zhengjingyong
     **/
    void notifyConfigUpdate();

    /**
     * 发送水印配置到指定桌面列表，水印策略参数传入为空时，将发送全局水印配置
     *
     * @param desktopList     云桌面列表
     * @param watermarkConfig 水印策略
     */
    void sendToDesktopList(List<CloudDesktopDetailDTO> desktopList, @Nullable CbbWatermarkConfigDTO watermarkConfig);

    /**
     * 发送水印配置到派生桌面列表，水印策略参数传入为空时，将发送全局水印配置
     *
     * @param rcaHostDesktopList     派生云桌面列表
     * @param watermarkConfig 水印策略
     */
    void sendToRcaHostDesktopList(List<RcaHostDesktopDTO> rcaHostDesktopList, @Nullable CbbWatermarkConfigDTO watermarkConfig);

    /**
     * 发送水印配置到派生桌面列表，水印策略参数传入为空时，将发送全局水印配置
     *
     * @param rcaHostDesktop     派生云桌面
     * @param multiSessionId     multiSessionId，即gt的userId
     * @param watermarkConfig 水印策略
     */
    void sendToRcaHostDesktopMultiSessionId(RcaHostDesktopDTO rcaHostDesktop, @Nullable UUID multiSessionId,  @Nullable CbbWatermarkConfigDTO watermarkConfig);

    /**
     * 发送水印配置到派生桌面列表，水印策略参数传入为空时，将发送全局水印配置
     *
     * @param rcaHostList  第三方应用主机列表
     * @param watermarkConfig 水印策略
     */
    void sendToRcaHostList(List<RcaHostDTO> rcaHostList, @Nullable CbbWatermarkConfigDTO watermarkConfig);

    /**
     * 发送水印配置到派生桌面列表，水印策略参数传入为空时，将发送全局水印配置
     *
     * @param rcaHostDTO  第三方应用主机
     * @param multiSessionId     multiSessionId，即gt的userId
     * @param watermarkConfig 水印策略
     */
    void sendToRcaHostListMultiSessionId(RcaHostDTO rcaHostDTO, @Nullable UUID multiSessionId, @Nullable CbbWatermarkConfigDTO watermarkConfig);
}
