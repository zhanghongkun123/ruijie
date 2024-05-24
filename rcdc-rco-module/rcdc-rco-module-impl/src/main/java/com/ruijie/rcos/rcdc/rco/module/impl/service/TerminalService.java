package com.ruijie.rcos.rcdc.rco.module.impl.service;

import java.util.List;
import java.util.UUID;

import com.ruijie.rcos.rcdc.rco.module.def.api.dto.ShineConfigFullSystemDiskDTO;
import com.ruijie.rcos.rcdc.rco.module.def.api.dto.TerminalDTO;
import com.ruijie.rcos.rcdc.rco.module.def.api.dto.VisitorSettingDTO;
import com.ruijie.rcos.rcdc.rco.module.impl.dto.TerminalWorkModeConfigDTO;
import com.ruijie.rcos.rcdc.rco.module.impl.entity.UserTerminalEntity;
import com.ruijie.rcos.rcdc.rco.module.impl.entity.ViewTerminalEntity;
import com.ruijie.rcos.rcdc.rco.module.impl.spi.dto.TerminalSettingDTO;
import com.ruijie.rcos.rcdc.terminal.module.def.api.dto.CbbShineTerminalBasicInfo;
import com.ruijie.rcos.rcdc.terminal.module.def.api.dto.CbbTerminalBizConfigDTO;
import com.ruijie.rcos.sk.base.exception.BusinessException;

/**
 * Description: 终端操作
 * Copyright: Copyright (c) 2018
 * Company: Ruijie Co., Ltd.
 * Create Time: 2018年10月30日
 *
 * @author chenzj
 */
public interface TerminalService {

    /**
     * 根据终端id获取终端信息
     *
     * @param terminalId 终端id
     * @return 终端视图对象
     * @throws BusinessException 业务异常
     */
    ViewTerminalEntity getViewByTerminalId(String terminalId) throws BusinessException;

    /**
     * 根据终端id更新绑定当前使用的用户
     *
     * @param terminalId 终端id
     * @param userId     用户id
     * @throws BusinessException 业务异常
     */
    void setLoginUserOnTerminal(String terminalId, UUID userId) throws BusinessException;

    /**
     * 保存终端信息
     *
     * @param terminalBasicInfo 终端基本信息
     */
    void saveTerminal(CbbShineTerminalBasicInfo terminalBasicInfo);


    /**
     * 删除终端
     *
     * @param terminalId 终端id
     * @throws BusinessException 业务异常
     */
    void delete(String terminalId) throws BusinessException;

    /**
     * 通知终端进行访客登录设置
     *
     * @param request 访客登录设置以及终端id
     * @throws BusinessException 业务异常
     */
    void syncTerminalVisitorSetting(VisitorSettingDTO request) throws BusinessException;

    /**
     * 编辑终端设置
     *
     * @param terminalSettingDTO 终端设置
     * @throws BusinessException 业务异常
     */
    void editTerminalSetting(TerminalSettingDTO terminalSettingDTO) throws BusinessException;

    /**
     * 终端初始化
     *
     * @param terminalId  终端id
     * @param retainImage 保留镜像
     * @throws BusinessException 业务异常
     */
    void initialize(String terminalId, Boolean retainImage) throws BusinessException;

    /**
     * 通知idv放弃本地本地编辑镜像模板
     *
     * @param terminalId 终端id
     * @param imageId    镜像id
     */
    void noticeIdvAbortLocalEditImageTemplate(String terminalId, UUID imageId);

    /**
     * 检测idv终端是否正在本地编辑镜像模板
     *
     * @param terminalId 终端id
     * @throws BusinessException 业务异常
     */
    void checkIdvExistsLocalEditImageTemplate(String terminalId) throws BusinessException;

    /**
     * 唤醒终端
     *
     * @param terminalId 终端ID
     * @throws BusinessException 业务异常
     */
    void wakeUpTerminal(String terminalId) throws BusinessException;

    /**
     * 编辑系统盘自动扩容
     *
     * @param terminalDTO 终端信息
     * @param request     发送给SHINE的请求信息
     * @throws BusinessException 业务异常
     */
    void configTerminalFullSystemDisk(TerminalDTO terminalDTO, ShineConfigFullSystemDiskDTO request) throws BusinessException;

    /**
     * 用户变更
     *
     * @param terminalId 终端ID
     * @param bindUserId 用户ID
     * @throws BusinessException 业务异常
     */
    void bindUser(String terminalId, UUID bindUserId) throws BusinessException;

    /**
     * 根据终端sn查询终端列表
     *
     * @param sn 终端sn
     * @return 终端视图对象
     * @throws BusinessException 业务异常
     */
    List<ViewTerminalEntity> listByTerminalSn(String sn);

    /**
     * 终端初始化完成后清除残留数据
     * @param userTerminalEntity 终端绑定关系
     */
    void initialTerminalCleanData(UserTerminalEntity userTerminalEntity);

    /**
     * 修改终端云服务器地址
     * @param terminalId 终端id
     * @param serverIp 云服务器地址
     * @throws BusinessException 业务异常
     */
    void changeTerminalServerIp(String terminalId, String serverIp) throws BusinessException;

    /**
     * 获取终端工作模式、授权模式
     * 
     * @param terminalWorkModeConfigDTO 终端支持工作模式相关配置
     * @return CbbTerminalBizConfigDTO
     */
    CbbTerminalBizConfigDTO getTerminalSupportMode(TerminalWorkModeConfigDTO terminalWorkModeConfigDTO);
}
