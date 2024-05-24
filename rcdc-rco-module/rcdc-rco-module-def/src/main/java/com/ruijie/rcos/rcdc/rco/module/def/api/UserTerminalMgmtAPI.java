package com.ruijie.rcos.rcdc.rco.module.def.api;

import com.ruijie.rcos.rcdc.rco.module.def.api.dto.*;
import com.ruijie.rcos.rcdc.rco.module.def.api.dto.ImageDownloadStateDTO;
import com.ruijie.rcos.rcdc.rco.module.def.api.dto.ShineConfigFullSystemDiskDTO;
import com.ruijie.rcos.rcdc.rco.module.def.api.dto.TerminalDTO;
import com.ruijie.rcos.rcdc.rco.module.def.api.dto.TerminalDeskInfoDTO;
import com.ruijie.rcos.rcdc.rco.module.def.api.dto.VisitorSettingDTO;
import com.ruijie.rcos.rcdc.rco.module.def.api.request.PageSearchRequest;
import com.ruijie.rcos.rcdc.rco.module.def.api.request.UserTerminalRequest;
import com.ruijie.rcos.rcdc.terminal.module.def.api.dto.CbbShineTerminalBasicInfo;
import com.ruijie.rcos.rcdc.terminal.module.def.api.enums.CbbNicWorkModeEnums;
import com.ruijie.rcos.rcdc.terminal.module.def.api.enums.CbbTerminalStateEnums;
import com.ruijie.rcos.rcdc.terminal.module.def.enums.CbbTerminalPlatformEnums;
import com.ruijie.rcos.rcdc.terminal.module.def.enums.CbbTerminalStartMode;
import com.ruijie.rcos.sk.base.exception.BusinessException;
import com.ruijie.rcos.sk.modulekit.api.comm.DefaultPageResponse;

import java.util.List;
import java.util.UUID;


/**
 * Description: 终端管理API接口
 * Copyright: Copyright (c) 2018
 * Company: Ruijie Co., Ltd.
 * Create Time: 2018年12月18日
 *
 * @author nt
 */
public interface UserTerminalMgmtAPI {

    /**
     * 分页查询终端
     *
     * @param request 分页查询请求参数
     * @return response 终端列表DTO对象
     */

    DefaultPageResponse<TerminalDTO> pageQuery(PageSearchRequest request);


    /**
     * 分页查询终端
     *
     * @param request 分页查询请求参数
     * @return response 终端列表DTO对象
     * @throws BusinessException 业务异常
     */

    DefaultPageResponse<TerminalDTO> softTerminalPageQuery(PageSearchRequest request) throws BusinessException;

    /**
     * 根据终端绑定的bindUserId获取终端对象
     *
     * @param terminalId 终端id
     * @param userId     终端绑定的用户id
     * @return response 终端信息响应
     */
    TerminalDTO findByTerminalIdAndBindUserId(String terminalId, UUID userId);

    /**
     * 获取终端对象
     *
     * @param terminalId 终端id
     * @return response 终端信息响应
     */
    TerminalDTO findByTerminalId(String terminalId);

    /**
     * 删除终端
     *
     * @param terminalId 删除请求参数
     * @throws BusinessException 业务异常
     */

    void delete(String terminalId) throws BusinessException;

    /**
     * 根据指定ID获取终端对象
     *
     * @param terminalId 请求参数
     * @return response 终端信息响应
     * @throws BusinessException 业务异常
     */
    TerminalDTO getTerminalById(String terminalId) throws BusinessException;

    /**
     * 更新终端访客登录设置
     *
     * @param request 请求参数
     * @throws BusinessException 业务异常
     */
    void updateVisitorSetting(VisitorSettingDTO request) throws BusinessException;

    /**
     * 终端初始化
     *
     * @param terminalId  初始化请求参数
     * @param retainImage 保留镜像
     * @throws BusinessException 业务异常
     */
    void initialize(String terminalId, Boolean retainImage) throws BusinessException;

    /**
     * 编辑终端设置
     *
     * @param request 请求参数
     * @throws BusinessException 业务异常
     */
    void editTerminalSetting(UserTerminalRequest request) throws BusinessException;

    /**
     * 根据终端组类型和分组Id以及终端状态来获取终端列表
     *
     * @param platform        IDV 或者 VDI 或者 APP 或者 PC
     * @param terminalGroupId 终端分组ID
     * @param terminalState   OFFLINE - 离线，ONLINE - 在线
     * @return List<TerminalDTO>
     */
    List<TerminalDTO> queryListByPlatformAndGroupIdAndState(CbbTerminalPlatformEnums platform, UUID terminalGroupId,
                                                            CbbTerminalStateEnums terminalState);

    /**
     * 获取终端磁盘信息
     *
     * @param terminalId 终端ID
     * @return DefaultPageResponse
     * @throws BusinessException BusinessException
     */
    DefaultPageResponse<TerminalDeskInfoDTO> getTerminalDeskInfoList(String terminalId) throws BusinessException;

    /**
     * 校验terminalId 是否存在于系统中
     *
     * @param terminalIdArr terminalId集合
     * @throws BusinessException 不存在时抛出异常
     */
    void verifyTerminalIdExist(String[] terminalIdArr) throws BusinessException;

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
     * 根据终端ID获取终端特征码
     *
     * @param terminalId 终端id
     * @return 特征码
     * @throws BusinessException 业务异常
     */
    String getTerminalFeatureCode(String terminalId) throws BusinessException;

    /**
     * 根据终端组类型以及终端状态来获取终端列表
     *
     * @param terminalState OFFLINE - 离线，ONLINE - 在线
     * @return List<TerminalDTO>
     */
    List<TerminalDTO> listByState(CbbTerminalStateEnums terminalState);

    /**
     * 根据云桌面ID和终端类型查询部署为TCI的终端是否支持TC引导
     *
     * @param bindDeskId 云桌面ID
     * @param platform   终端类型
     * @return 是否支持TC引导
     */
    Boolean getSupportTcByBindDeskIdAndPlatform(UUID bindDeskId, CbbTerminalPlatformEnums platform);

    /**
     * 根据云桌面ID查询终端ID
     *
     * @param bindDeskId 云桌面ID
     * @return 终端ID
     */
    String getTerminalIdByBindDeskId(UUID bindDeskId);

    /**
     * 唤醒终端
     *
     * @param terminalId 终端ID
     * @throws BusinessException 业务异常
     */
    void wakeUpTerminal(String terminalId) throws BusinessException;

    /**
     * 导入终端信息数据
     *
     * @param cbbShineTerminalBasicInfo 终端信息
     * @throws BusinessException 业务异常
     */
    void importUserTerminal(CbbShineTerminalBasicInfo cbbShineTerminalBasicInfo) throws BusinessException;

    /**
     * 修改终端网卡工作模式
     *
     * @param terminalId  终端ID
     * @param nicWorkMode 网卡工作模式
     * @throws BusinessException 业务异常
     */
    void configTerminalNicWorkMode(String terminalId, CbbNicWorkModeEnums nicWorkMode) throws BusinessException;

    /**
     * 编辑系统盘自动扩容
     *
     * @param terminalId 终端ID
     * @param request    发送给SHINE的请求
     * @throws BusinessException 业务异常
     */
    void configTerminalFullSystemDisk(String terminalId, ShineConfigFullSystemDiskDTO request) throws BusinessException;

    /**
     * 用户变更
     *
     * @param terminalId 终端ID
     * @param bindUserId 用户ID
     * @throws BusinessException 业务异常
     */
    void bindUser(String terminalId, UUID bindUserId) throws BusinessException;

    /**
     * 根据终端ID解绑关联终端的云桌面
     *
     * @param terminalId 终端id
     */
    void unbindDesktopTerminal(String terminalId);

    /**
     * 根据终端组类型和终端状态来获取终端列表
     *
     * @param platform IDV 或者 VDI 或者 APP 或者 PC
     * @param terminalState OFFLINE - 离线，ONLINE - 在线
     * @return List<TerminalDTO>
     */
    List<TerminalDTO> queryListByPlatformAndState(CbbTerminalPlatformEnums platform, CbbTerminalStateEnums terminalState);

    /**
     * 修改终端云服务器地址
     * @param terminalId 终端ip
     * @param serverIp 服务器ip
     * @throws BusinessException 业务异常
     */
    void changeTerminalServerIp(String terminalId, String serverIp) throws BusinessException;


    /**
     * 获取终端下载记录
     *
     * @param terminalId 终端id
     * @return ImageDownloadStateDTO
     */
    ImageDownloadStateDTO getDownImageByTerminalId(String terminalId);

    /**
     * 设置终端开机模式
     * @param terminalId 终端mac
     * @param terminalStartMode 开机模式
     * @throws BusinessException 业务异常
     */
    void configTerminalStartMode(String terminalId, CbbTerminalStartMode terminalStartMode) throws BusinessException;

    /**
     * 重置IDV终端开机模式
     */
    void updateIDVBootTypeToNull();

}
