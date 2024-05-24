package com.ruijie.rcos.rcdc.rco.module.impl.spi;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.api.CbbClusterServerMgmtAPI;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.api.CbbIDVDeskNetworkControlAPI;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.api.CbbUSBAdvancedSettingMgmtAPI;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.api.CbbUSBDeviceMgmtAPI;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.api.CbbUSBTypeMgmtAPI;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.api.CbbVDIDeskMgmtAPI;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.api.request.usbdevice.CbbUSBDevicePageRequest;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.dto.CbbClusterVirtualIpDTO;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.dto.CbbDeskDTO;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.dto.CbbGetAllUSBTypeDTO;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.dto.CbbUSBDeviceDTO;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.dto.CbbUSBTypeDTO;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.dto.desk.CbbIDVDeskNetworkInfoDTO;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.enums.CbbCloudDeskState;
import com.ruijie.rcos.rcdc.codec.adapter.def.api.CbbTranspondMessageHandlerAPI;
import com.ruijie.rcos.rcdc.codec.adapter.def.dto.CbbDispatcherRequest;
import com.ruijie.rcos.rcdc.codec.adapter.def.dto.CbbResponseShineMessage;
import com.ruijie.rcos.rcdc.codec.adapter.def.spi.CbbDispatcherHandlerSPI;
import com.ruijie.rcos.rcdc.rco.module.def.api.DeskFaultInfoAPI;
import com.ruijie.rcos.rcdc.rco.module.def.api.StatisticsTerminalAPI;
import com.ruijie.rcos.rcdc.rco.module.def.api.WebclientNotifyAPI;
import com.ruijie.rcos.rcdc.rco.module.def.api.dto.CbbDeskFaultInfoDTO;
import com.ruijie.rcos.rcdc.rco.module.def.api.dto.RemoteAssistStateDTO;
import com.ruijie.rcos.rcdc.rco.module.def.api.enums.ComputerStateEnum;
import com.ruijie.rcos.rcdc.rco.module.def.api.enums.RemoteAssistState;
import com.ruijie.rcos.rcdc.rco.module.def.api.request.RemoteAssistHeartbeatNotifyRequest;
import com.ruijie.rcos.rcdc.rco.module.def.api.request.RemoteAssistReportStateNotifyRequest;
import com.ruijie.rcos.rcdc.rco.module.def.api.request.RemoteAssistUserCloseNotifyRequest;
import com.ruijie.rcos.rcdc.rco.module.def.api.request.RemoteAssistUserConfirmNotifyReqeust;
import com.ruijie.rcos.rcdc.rco.module.def.api.response.CbbDeskFaultInfoResponse;
import com.ruijie.rcos.rcdc.rco.module.def.utils.RedLineUtil;
import com.ruijie.rcos.rcdc.rco.module.impl.BusinessKey;
import com.ruijie.rcos.rcdc.rco.module.impl.Constants;
import com.ruijie.rcos.rcdc.rco.module.impl.dao.ComputerBusinessDAO;
import com.ruijie.rcos.rcdc.rco.module.impl.dao.UserDesktopDAO;
import com.ruijie.rcos.rcdc.rco.module.impl.dao.ViewDesktopDetailDAO;
import com.ruijie.rcos.rcdc.rco.module.impl.dto.computer.ComputerBaseInfoDTO;
import com.ruijie.rcos.rcdc.rco.module.impl.dto.vdiedit.GetVmUsbInfoResponseDTO;
import com.ruijie.rcos.rcdc.rco.module.impl.entity.ComputerEntity;
import com.ruijie.rcos.rcdc.rco.module.impl.entity.UserDesktopEntity;
import com.ruijie.rcos.rcdc.rco.module.impl.entity.ViewUserDesktopEntity;
import com.ruijie.rcos.rcdc.rco.module.impl.message.MessageUtils;
import com.ruijie.rcos.rcdc.rco.module.impl.message.computer.AssistantAppComponentPathResponseContent;
import com.ruijie.rcos.rcdc.rco.module.impl.message.computer.BaseResponseContent;
import com.ruijie.rcos.rcdc.rco.module.impl.message.computer.BusinessAction;
import com.ruijie.rcos.rcdc.rco.module.impl.message.computer.CheckFaultResponseContent;
import com.ruijie.rcos.rcdc.rco.module.impl.message.computer.QueryAssistantVersionResponseContent;
import com.ruijie.rcos.rcdc.rco.module.impl.message.computer.QueryFtpInfoResponseContent;
import com.ruijie.rcos.rcdc.rco.module.impl.message.computer.ReportFaultResponseContent;
import com.ruijie.rcos.rcdc.rco.module.impl.service.ComputerBusinessService;
import com.ruijie.rcos.rcdc.rco.module.impl.service.ComputerRemoteAssistMgmtService;
import com.ruijie.rcos.rcdc.rco.module.impl.service.ComputerRemoteAssistNotifyService;
import com.ruijie.rcos.rcdc.rco.module.impl.service.RemoteAssistService;
import com.ruijie.rcos.rcdc.terminal.module.def.enums.CbbTerminalPlatformEnums;
import com.ruijie.rcos.sk.base.crypto.AesUtil;
import com.ruijie.rcos.sk.base.exception.BusinessException;
import com.ruijie.rcos.sk.base.log.Logger;
import com.ruijie.rcos.sk.base.log.LoggerFactory;
import com.ruijie.rcos.sk.modulekit.api.comm.DefaultPageResponse;
import com.ruijie.rcos.sk.modulekit.api.comm.DispatcherImplemetion;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.util.Assert;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * Description: pc纳管业务
 * Copyright: Copyright (c) 2019
 * Company: Ruijie Co., Ltd.
 * Create Time: 2019/12/30 14:28
 *
 * @author ketb
 */
@DispatcherImplemetion(Constants.COMPUTER_BUSINESS)
public class ComputerBusinessHandlerSPIImpl implements CbbDispatcherHandlerSPI {

    public static final Logger LOGGER = LoggerFactory.getLogger(ComputerBusinessHandlerSPIImpl.class);

    /**
     * 远程协助接口结果成功标识
     */
    private static final int SUCCESS = 0;

    /**
     * 小助手上传远程协助状态
     */
    private static final String REPORT_STATE_KEY = "state";

    private static final String SIGN_COMMA = ",";

    private static final Integer INT_1 = 1;

    private static final String SIGN_VERTICAL = "|";

    private static final int DEFAULT_DEVICE_LIMIT = 1000;

    @Autowired
    private ComputerBusinessService computerBusinessService;

    @Autowired
    private ComputerBusinessDAO computerBusinessDAO;

    @Autowired
    private CbbTranspondMessageHandlerAPI messageHandlerAPI;

    @Autowired
    private CbbVDIDeskMgmtAPI cbbVDIDeskMgmtAPI;

    @Autowired
    private ComputerRemoteAssistNotifyService remoteAssistNotifyService;

    @Autowired
    private DeskFaultInfoAPI deskFaultInfoAPI;

    @Autowired
    private CbbClusterServerMgmtAPI clusterServerMgmtAPI;

    @Autowired
    private RemoteAssistService remoteAssistService;

    @Autowired
    private UserDesktopDAO userDesktopDAO;

    @Autowired
    private ComputerRemoteAssistMgmtService remoteAssistMgmtService;

    @Autowired
    private CbbIDVDeskNetworkControlAPI cbbIDVDeskNetworkControlAPI;

    @Autowired
    private ViewDesktopDetailDAO viewDesktopDetailDAO;

    @Autowired
    private StatisticsTerminalAPI statisticsTerminalAPI;

    @Autowired
    private WebclientNotifyAPI webclientNotifyAPI;

    @Autowired
    private CbbUSBAdvancedSettingMgmtAPI cbbUSBAdvancedSettingMgmtAPI;

    @Autowired
    private CbbUSBTypeMgmtAPI cbbUSBTypeMgmtAPI;

    @Autowired
    private CbbUSBDeviceMgmtAPI cbbUSBDeviceMgmtAPI;

    @Value("${ftp.password}")
    private String ftpPassword;

    @Override
    public void dispatch(CbbDispatcherRequest request) {
        Assert.notNull(request, "request is not null");
        Assert.hasText(request.getTerminalId(), "terminalId can not empty");

        LOGGER.debug("=====pc纳管业务报文===={}", request.getData());
        // PC纳管业务中，terminalId即为mac，通信报文中的terminalId不是指表记录id
        JSONObject data = convertJsonData(request);
        Assert.notNull(data, "data is not null");
        String businessAction = data.getString(Constants.BUSINESS);
        Assert.notNull(businessAction, "businessAction is not null");
        try {
            switch (businessAction) {
                case BusinessAction.REPORT_COMPUTER_INFO:
                    reportComputerInfo(request);
                    break;
                case BusinessAction.CHECK_FAULT:
                    checkFault(request, data);
                    break;
                case BusinessAction.REPORT_FAULT:
                    reportFault(request, data);
                    break;
                case BusinessAction.RELIEVE_FAULT:
                    relieveFault(request, data);
                    break;
                case BusinessAction.REMOTE_ASSIST_RESULT:
                    handleRemoteAssistResult(request, data);
                    break;
                case BusinessAction.CLOSE_REMOTE_ASSIST:
                    assistStop(request);
                    break;
                case BusinessAction.REMOTE_HEART:
                    handleRemoteHeartBeat(request);
                    break;
                case BusinessAction.ASSISTANT_APP_COMPONENT_PATH:
                    getUpgradeInformation(request);
                    break;
                case BusinessAction.QUERY_ASSISTANT_VERSION:
                    getAssistantVersion(request);
                    break;
                case BusinessAction.QUERY_FTP_INFO:
                    getFtpInfo(request);
                    break;
                case BusinessAction.REMOTE_ASSIST_REPORT_STATE:
                    reportState(request, data);
                    break;
                case BusinessAction.QUERY_USB_INFO:
                    queryUsbInfo(request);
                    break;
                default:
                    LOGGER.warn("未知的业务请求<{}>", businessAction);
                    break;
            }
        } catch (BusinessException e) {
            LOGGER.error("处理业务<{}>异常:", businessAction, e);
            CbbResponseShineMessage cbbResponseShineMessage = MessageUtils.buildErrorResponseMessage(request);
            messageHandlerAPI.response(cbbResponseShineMessage);
        }
    }

    private void reportState(CbbDispatcherRequest request, JSONObject data) throws BusinessException {
        LOGGER.info("PC终端上报远程连接状态：TerminalId = {}, data = {}", request.getTerminalId(), data.toJSONString());
        RemoteAssistState state = RemoteAssistState.valueOf(data.getString(REPORT_STATE_KEY));
        ComputerEntity computerEntity = getComputerEntity(request.getTerminalId());
        RemoteAssistReportStateNotifyRequest notifyRequest = new RemoteAssistReportStateNotifyRequest();
        notifyRequest.setDeskId(computerEntity.getId());
        notifyRequest.setState(state);
        remoteAssistNotifyService.remoteAssistReportState(notifyRequest);
        CbbResponseShineMessage responseMessage = MessageUtils.buildResponseMessage(request, Object.class);
        messageHandlerAPI.response(responseMessage);
    }

    /**
     * 查询ftp账号密码
     * 该方法应该在cbb（terminal）组件中实现，原shine不应该在client写死，应该改为从rcdc获取才符合安全专项要求
     * 目前询问平台还未回复，先在这里简单实现，后续在优化（及移植）
     */
    private void getFtpInfo(CbbDispatcherRequest request) {
        QueryFtpInfoResponseContent responseContent = new QueryFtpInfoResponseContent(BusinessAction.QUERY_FTP_INFO);
        responseContent.setFtpUser(AesUtil.encrypt(Constants.FTP_USER, RedLineUtil.getRealAdminRedLine()));
        responseContent.setFtpPasswd(AesUtil.encrypt(ftpPassword, RedLineUtil.getRealAdminRedLine()));
        CbbResponseShineMessage responseMessage = MessageUtils.buildResponseMessage(request, responseContent);
        messageHandlerAPI.response(responseMessage);
    }

    /**
     * 查询小助手版本号
     */
    private void getAssistantVersion(CbbDispatcherRequest request) {
        QueryAssistantVersionResponseContent responseContent = new QueryAssistantVersionResponseContent(BusinessAction.QUERY_ASSISTANT_VERSION);
        computerBusinessService.getAssistantVersion(responseContent);
        CbbResponseShineMessage responseMessage = MessageUtils.buildResponseMessage(request, responseContent);
        messageHandlerAPI.response(responseMessage);
    }

    /**
     * 查询安装包信息（安装包路径、ftp账号密码）
     */
    private void getUpgradeInformation(CbbDispatcherRequest request) {
        CbbResponseShineMessage responseMessage = new CbbResponseShineMessage();
        AssistantAppComponentPathResponseContent responseContent =
                new AssistantAppComponentPathResponseContent(BusinessAction.ASSISTANT_APP_COMPONENT_PATH);
        try {
            CbbClusterVirtualIpDTO cbbClusterVirtualIpDTO = clusterServerMgmtAPI.getClusterVirtualIp();
            String rcdcIP = cbbClusterVirtualIpDTO.getClusterVirtualIp();
            String path = Constants.FTP_HEAD + rcdcIP + ":" + Constants.FTP_PORT + Constants.ASSISTANT_APP_COMPONENT_PATH_FOR_FTP;
            responseContent.setPath(path);
            responseMessage = MessageUtils.buildResponseMessage(request, responseContent);
        } catch (BusinessException e) {
            LOGGER.error("get ftp info fail:", e);
            responseMessage = MessageUtils.buildErrorResponseMessage(request);
        }
        messageHandlerAPI.response(responseMessage);
    }

    private void relieveFault(CbbDispatcherRequest request, JSONObject data) throws BusinessException {
        String deskType = data.getString(Constants.VM_TYPE);
        Assert.hasText(deskType, "vmType不能为空");
        LOGGER.info("取消PC报障，terminalId：[{}]，deskType = {}", request.getTerminalId(), deskType);
        CbbResponseShineMessage<?> responseMessage = new CbbResponseShineMessage<>();
        BaseResponseContent relieveFaultResponseContent = new BaseResponseContent(BusinessAction.RELIEVE_FAULT);
        if (Constants.VM_TYPE_VDI_CLOUDDESKTOP == Integer.parseInt(deskType) || Constants.VM_TYPE_IDV_CLOUDDESKTOP == Integer.parseInt(deskType)
                || Constants.VM_TYPE_VOI_CLOUDDESKTOP == Integer.parseInt(deskType)) {
            // 云桌面取消报障
            CbbDeskFaultInfoResponse response = deskFaultInfoAPI.findFaultInfoByMac(request.getTerminalId());
            if (response == null || response.getCbbDeskFaultInfoDTO() == null) {
                LOGGER.warn("解除云桌面报障时未查询到相应记录...");
                responseMessage = MessageUtils.buildErrorResponseMessage(request);
            } else {
                CbbDeskFaultInfoDTO cbbDeskFaultInfoDTO = response.getCbbDeskFaultInfoDTO();
                cbbDeskFaultInfoDTO.setFaultState(false);
                // 为了不影响排序，故障解除时，将报障时间清空
                cbbDeskFaultInfoDTO.setFaultTime(null);
                cbbDeskFaultInfoDTO.setFaultDescription("");
                deskFaultInfoAPI.save(cbbDeskFaultInfoDTO);
                responseMessage = MessageUtils.buildResponseMessage(request, relieveFaultResponseContent);

                try {
                    if (Constants.VM_TYPE_VDI_CLOUDDESKTOP == Integer.parseInt(deskType)) {
                        // 向网页版客户端发送解除故障消息
                        RemoteAssistStateDTO remoteAssistStateDTO = new RemoteAssistStateDTO(response.getCbbDeskFaultInfoDTO().getDeskId(), false);
                        webclientNotifyAPI.notifyRemoteAssistState(true, remoteAssistStateDTO);
                        // 通知终端浮动条取消请求远程协助
                        UserDesktopEntity userDesk = userDesktopDAO.findByCbbDesktopId(response.getCbbDeskFaultInfoDTO().getDeskId());
                        if (!StringUtils.isEmpty(userDesk.getTerminalId())) {
                            remoteAssistService.syncRemoteAssistRequestStatus(userDesk.getTerminalId(), false);
                        }
                    }
                } catch (NumberFormatException e) {
                    LOGGER.error("通知终端浮动条取消请求远程协助失败", e);
                }
            }
        } else if (Constants.VM_TYPE_PC == Integer.parseInt(deskType)) {
            // pc取消报障
            ComputerEntity computerEntity = getComputerEntity(request.getTerminalId());
            if (computerEntity == null) {
                LOGGER.warn("取消PC报障时未查询到相应记录...");
                responseMessage = MessageUtils.buildErrorResponseMessage(request);
            } else {
                LOGGER.info("桌面状态变更前<{}>", computerEntity.getState());
                computerEntity.setState(ComputerStateEnum.ONLINE);
                // 为了不影响排序，故障解除时，将报障时间清空
                computerEntity.setFaultTime(null);
                computerEntity.setFaultState(false);
                computerEntity.setFaultDescription("");
                computerBusinessService.update(computerEntity, 0);
                responseMessage = MessageUtils.buildResponseMessage(request, relieveFaultResponseContent);
            }
        }
        messageHandlerAPI.response(responseMessage);
    }

    private void handleRemoteAssistResult(CbbDispatcherRequest request, JSONObject data) throws BusinessException {
        ComputerEntity entity = getComputerEntity(request.getTerminalId());
        if (entity == null) {
            throw new BusinessException(BusinessKey.RCDC_RCO_COMPUTER_NOT_FOUND_COMPUTER, request.getTerminalId().toString());
        }
        RemoteAssistUserConfirmNotifyReqeust notifyReqeust = JSON.parseObject(data.toString(), RemoteAssistUserConfirmNotifyReqeust.class);
        notifyReqeust.setCode(SUCCESS);
        notifyReqeust.setDeskId(entity.getId());
        entity.setAssistPwd(notifyReqeust.getPasswd());
        computerBusinessService.update(entity, 0);
        remoteAssistNotifyService.remoteAssistUserConfirm(notifyReqeust);
    }

    private void assistStop(CbbDispatcherRequest request) throws BusinessException {
        ComputerEntity computerEntity = getComputerEntity(request.getTerminalId());

        RemoteAssistUserCloseNotifyRequest notifyRequest = new RemoteAssistUserCloseNotifyRequest();
        notifyRequest.setDeskId(computerEntity.getId());
        remoteAssistNotifyService.remoteAssistUserClose(notifyRequest);
        remoteAssistMgmtService.setRemoteAssistCloseStatus(computerEntity.getId(), Boolean.FALSE);
        try {
            Thread.sleep(TimeUnit.SECONDS.toMillis(3));
        } catch (InterruptedException e) {
            LOGGER.info("内部业务不会执行到此InterruptedException异常", e);
            Thread.currentThread().interrupt();
        } finally {
            remoteAssistMgmtService.removeRemoteAssistCloseStatus(computerEntity.getId());
        }

        LOGGER.info("assistStop response<{}>", request.getTerminalId());
        CbbResponseShineMessage responseMessage = MessageUtils.buildResponseMessage(request, Object.class);
        messageHandlerAPI.response(responseMessage);
    }

    private void handleRemoteHeartBeat(CbbDispatcherRequest request) throws BusinessException {
        ComputerEntity entity = getComputerEntity(request.getTerminalId());
        RemoteAssistHeartbeatNotifyRequest notifyRequest = new RemoteAssistHeartbeatNotifyRequest();
        notifyRequest.setDeskId(entity.getId());
        remoteAssistNotifyService.remoteAssistHeartbeat(notifyRequest);
    }

    private void reportFault(CbbDispatcherRequest request, JSONObject data) {
        String vmType = data.getString(Constants.VM_TYPE);
        Assert.hasText(vmType, "vmType can not empty");
        LOGGER.info("PC报障，terminalId：[{}],vmType = [{}] ", request.getTerminalId(), vmType);
        CbbResponseShineMessage<?> responseMessage;
        ReportFaultResponseContent reportFaultResponseContent = new ReportFaultResponseContent(BusinessAction.REPORT_FAULT);
        if (Constants.VM_TYPE_VDI_CLOUDDESKTOP == Integer.parseInt(vmType) || Constants.VM_TYPE_IDV_CLOUDDESKTOP == Integer.parseInt(vmType)
                || Constants.VM_TYPE_VOI_CLOUDDESKTOP == Integer.parseInt(vmType)) {
            // 处理云桌面报障
            responseMessage = reportFaultByDeskTop(reportFaultResponseContent, data, request);
            // 通知vdi终端浮动条显示请求远程协助
            if (Constants.VM_TYPE_VDI_CLOUDDESKTOP == Integer.parseInt(vmType)) {
                CbbDeskFaultInfoResponse response = deskFaultInfoAPI.findFaultInfoByMac(request.getTerminalId());
                // 向网页版客户端发送解除故障消息
                RemoteAssistStateDTO remoteAssistStateDTO = new RemoteAssistStateDTO(response.getCbbDeskFaultInfoDTO().getDeskId(), true);
                webclientNotifyAPI.notifyRemoteAssistState(true, remoteAssistStateDTO);
                UserDesktopEntity userDesk = userDesktopDAO.findByCbbDesktopId(response.getCbbDeskFaultInfoDTO().getDeskId());
                if (!StringUtils.isEmpty(userDesk.getTerminalId())) {
                    remoteAssistService.syncRemoteAssistRequestStatus(userDesk.getTerminalId(), true);
                }
            }
        } else if (Constants.VM_TYPE_PC == Integer.parseInt(vmType)) {
            // 处理PC报障
            responseMessage = reportFaultByPC(reportFaultResponseContent, data, request);
        } else {
            // 未知的桌面类型，不予以回复
            LOGGER.warn("未知的桌面类型<{}>", vmType);
            return;
        }
        messageHandlerAPI.response(responseMessage);

    }

    private CbbResponseShineMessage reportFaultByDeskTop(ReportFaultResponseContent reportFaultResponseContent, JSONObject data,
            CbbDispatcherRequest request) {
        try {
            String vmType = data.getString(Constants.VM_TYPE);
            UUID deskId;
            if (Constants.VM_TYPE_IDV_CLOUDDESKTOP == Integer.parseInt(vmType) || Constants.VM_TYPE_VOI_CLOUDDESKTOP == Integer.parseInt(vmType)) {
                // idv/voi 终端存在有线和无线的场景
                deskId = matchIDVDeskIdByMac(request.getTerminalId());
            } else {
                deskId = cbbVDIDeskMgmtAPI.getDeskVDIByMac(request.getTerminalId()).getDeskId();
            }
            handleDeskTopFault(reportFaultResponseContent, data, deskId, request.getTerminalId());
            return MessageUtils.buildResponseMessage(request, reportFaultResponseContent);
        } catch (Exception e) {
            LOGGER.error(String.format("处理云桌面:%s报障出现异常", request.getTerminalId()), e);
            return MessageUtils.buildErrorResponseMessage(request);
        }
    }

    private ReportFaultResponseContent handleDeskTopFault(ReportFaultResponseContent reportFaultResponseContent, JSONObject data, UUID deskId,
            String mac) {
        if (deskId == null) {
            reportFaultResponseContent.setFaultDescription(data.getString(Constants.FAULTDESCRIPTION));
            reportFaultResponseContent.setResult(Constants.RESULT_NOTEXIST_DESKTOP);
        } else {
            CbbDeskFaultInfoResponse response = deskFaultInfoAPI.findFaultInfoByDeskId(deskId);
            CbbDeskFaultInfoDTO cbbDeskFaultInfoDTO;
            if (response == null || response.getCbbDeskFaultInfoDTO() == null) {
                cbbDeskFaultInfoDTO = new CbbDeskFaultInfoDTO();
                cbbDeskFaultInfoDTO.setCreateTime(new Date());
            } else {
                cbbDeskFaultInfoDTO = response.getCbbDeskFaultInfoDTO();
            }
            cbbDeskFaultInfoDTO.setFaultDescription(data.getString(Constants.FAULTDESCRIPTION));
            cbbDeskFaultInfoDTO.setDeskId(deskId);
            cbbDeskFaultInfoDTO.setMac(mac);
            cbbDeskFaultInfoDTO.setFaultState(true);
            cbbDeskFaultInfoDTO.setFaultTime(new Date());
            deskFaultInfoAPI.save(cbbDeskFaultInfoDTO);
            BeanUtils.copyProperties(cbbDeskFaultInfoDTO, reportFaultResponseContent);
            reportFaultResponseContent.setResult(Constants.RESULT_SUCCESS);
            reportFaultResponseContent.setReportTime(new Date());
        }
        return reportFaultResponseContent;
    }


    private CbbResponseShineMessage reportFaultByPC(ReportFaultResponseContent reportFaultResponseContent, JSONObject data,
            CbbDispatcherRequest request) {
        ComputerEntity computerEntity = getComputerEntity(request.getTerminalId());
        if (computerEntity == null) {
            reportFaultResponseContent.setFaultDescription(data.getString(Constants.FAULTDESCRIPTION));
            reportFaultResponseContent.setResult(Constants.RESULT_NOTEXIST_DESKTOP);
        } else {
            computerEntity.setFaultState(true);
            computerEntity.setFaultDescription(data.getString(Constants.FAULTDESCRIPTION));
            computerEntity.setState(ComputerStateEnum.ONLINE);
            computerEntity.setFaultTime(new Date());
            computerBusinessService.update(computerEntity, 0);

            BeanUtils.copyProperties(computerEntity, reportFaultResponseContent);
            reportFaultResponseContent.setResult(Constants.RESULT_SUCCESS);
            reportFaultResponseContent.setReportTime(new Date());
        }
        return MessageUtils.buildResponseMessage(request, reportFaultResponseContent);
    }


    private void checkFault(CbbDispatcherRequest request, JSONObject data) throws BusinessException {
        String vmType = data.getString(Constants.VM_TYPE);
        Assert.hasText(vmType, "vmType can not empty");

        CheckFaultResponseContent faultResponseContent = new CheckFaultResponseContent(BusinessAction.CHECK_FAULT);
        CbbDeskFaultInfoResponse response;
        final int deskType = Integer.parseInt(vmType);
        switch (deskType) {
            case Constants.VM_TYPE_VDI_CLOUDDESKTOP:
                // vdi需要注意单终端多桌面问题
                CbbDeskDTO deskVDIByMac;
                try {
                    deskVDIByMac = cbbVDIDeskMgmtAPI.getDeskVDIByMac(request.getTerminalId());
                } catch (BusinessException e) {
                    LOGGER.error("获取mac[{}]对应桌面异常", request.getTerminalId());
                    break;
                }
                UUID vdiDeskId = deskVDIByMac.getDeskId();
                response = deskFaultInfoAPI.findFaultInfoByDeskId(vdiDeskId);
                if (response != null && response.getCbbDeskFaultInfoDTO() != null) {
                    BeanUtils.copyProperties(response.getCbbDeskFaultInfoDTO(), faultResponseContent);
                }
                break;
            case Constants.VM_TYPE_IDV_CLOUDDESKTOP:
            case Constants.VM_TYPE_VOI_CLOUDDESKTOP:
                // idv需要注意双网卡场景
                UUID idvDeskId = matchIDVDeskIdByMac(request.getTerminalId());
                response = deskFaultInfoAPI.findFaultInfoByDeskId(idvDeskId);
                if (response != null && response.getCbbDeskFaultInfoDTO() != null) {
                    BeanUtils.copyProperties(response.getCbbDeskFaultInfoDTO(), faultResponseContent);
                }
                break;
            case Constants.VM_TYPE_PC:
                ComputerEntity computerEntity = getComputerEntity(request.getTerminalId());
                if (computerEntity != null) {
                    faultResponseContent.setMac(computerEntity.getMac());
                    faultResponseContent.setFaultDescription(computerEntity.getFaultDescription());
                }
                break;
            default:
                // 未知的桌面类型，不予以回复
                LOGGER.warn("未知的桌面类型<{}>", vmType);
                throw new BusinessException("不支持的云桌面类型:{0}", vmType);
        }

        CbbResponseShineMessage responseMessage = MessageUtils.buildResponseMessage(request, faultResponseContent);
        messageHandlerAPI.response(responseMessage);
    }

    private void reportComputerInfo(CbbDispatcherRequest request) throws BusinessException {
        LOGGER.info("PC接入，terminalId：[{}]", request.getTerminalId());
        ComputerBaseInfoDTO baseInfoDTO = JSON.parseObject(String.valueOf(request.getData()), ComputerBaseInfoDTO.class);
        computerBusinessService.saveComputerInfo(baseInfoDTO);

        BaseResponseContent reportResponseContent = new BaseResponseContent(BusinessAction.REPORT_COMPUTER_INFO);
        // 回复桌面小助手业务是否处理成功
        CbbResponseShineMessage responseMessage = MessageUtils.buildResponseMessage(request, reportResponseContent);
        messageHandlerAPI.response(responseMessage);
        statisticsTerminalAPI.recordTerminalOnlineSituation(request.getTerminalId(), CbbTerminalPlatformEnums.PC);
    }

    private UUID matchIDVDeskIdByMac(String mac) throws BusinessException {
        // 去除IDV/VOI增加的标志位
        String realMac = mac.substring(com.ruijie.rcos.rcdc.terminal.module.def.constants.Constants.PC_FLAG.length());
        List<CbbIDVDeskNetworkInfoDTO> networkInfoDTOList = cbbIDVDeskNetworkControlAPI.listIDVDesktopNetworkInfoByMac(realMac);
        List<UUID> cbbDeskIdList = new ArrayList<>();
        networkInfoDTOList.forEach(networkInfo -> cbbDeskIdList.add(networkInfo.getDeskId()));

        List<ViewUserDesktopEntity> userDesktopList =
                viewDesktopDetailDAO.findAllByCbbDesktopIdInAndDeskState(cbbDeskIdList, CbbCloudDeskState.RUNNING.name());
        if (CollectionUtils.isEmpty(userDesktopList)) {
            throw new BusinessException(BusinessKey.RCDC_RCO_COMPUTER_REPORT_FAULT_FAIL_BY_DESK_NOT_EXIST);
        }
        return userDesktopList.get(0).getCbbDesktopId();
    }

    private JSONObject convertJsonData(CbbDispatcherRequest request) {
        String jsonData = String.valueOf(request.getData());
        return JSON.parseObject(jsonData);
    }

    private ComputerEntity getComputerEntity(String terminalId) {
        String pcMac = terminalId.substring(com.ruijie.rcos.rcdc.terminal.module.def.constants.Constants.PC_FLAG.length());
        return computerBusinessDAO.findComputerEntityByMac(pcMac);
    }

    private void queryUsbInfo(CbbDispatcherRequest request) {
        try {
            GetVmUsbInfoResponseDTO responseDTO = new GetVmUsbInfoResponseDTO();
            GetVmUsbInfoResponseDTO.USBFilterInfo usbFilterDTO = responseDTO.new USBFilterInfo();
            // 允许使用其他USB设备，开放所有USB设备类型
            usbFilterDTO.setAllowInfo(getUsbFilterAllowString());
            responseDTO.setUsbFilter(usbFilterDTO);
            responseDTO.setUsbConf(getUsbConfString());

            if (LOGGER.isDebugEnabled()) {
                LOGGER.debug("获取到USB信息：[{}]", responseDTO.toString());
            }

            CbbResponseShineMessage responseMessage = MessageUtils.buildResponseMessage(request, responseDTO);
            messageHandlerAPI.response(responseMessage);

        } catch (Exception e) {
            LOGGER.error("获取USB配置信息失败", e);
            messageHandlerAPI.response(MessageUtils.buildErrorResponseMessage(request));
        }
    }

    private String getUsbFilterAllowString() throws BusinessException {
        CbbUSBTypeDTO[] usbTypeDTOArr = cbbUSBTypeMgmtAPI.getAllUSBType(new CbbGetAllUSBTypeDTO());
        Assert.notNull(usbTypeDTOArr, "usb type list is null!");
        List<UUID> allUSBTypeIdList = Stream.of(usbTypeDTOArr).map(CbbUSBTypeDTO::getId).collect(Collectors.toList());

        StringBuilder strBuilder = new StringBuilder();
        for (UUID usbTypeId : allUSBTypeIdList) {
            CbbUSBDevicePageRequest deviceRequest = new CbbUSBDevicePageRequest();
            deviceRequest.setUsbTypeId(usbTypeId);
            deviceRequest.setPage(0);
            deviceRequest.setLimit(DEFAULT_DEVICE_LIMIT);
            DefaultPageResponse<CbbUSBDeviceDTO> usbDeviceResponse = cbbUSBDeviceMgmtAPI.pageQueryUSBDevice(deviceRequest);
            CbbUSBDeviceDTO[] usbDeviceArr = usbDeviceResponse.getItemArr();
            for (CbbUSBDeviceDTO usbDeviceDTO : usbDeviceArr) {
                strBuilder.append(usbDeviceDTO.getDeviceClass()).append(SIGN_COMMA);
                strBuilder.append(usbDeviceDTO.getFirmFlag()).append(SIGN_COMMA);
                strBuilder.append(usbDeviceDTO.getProductFlag()).append(SIGN_COMMA);
                strBuilder.append(usbDeviceDTO.getReleaseVersion()).append(SIGN_COMMA);
                strBuilder.append(INT_1).append(SIGN_VERTICAL);
            }
        }
        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug("PC小助手镜像编辑允许的设备[{}]", strBuilder.toString());
        }
        return strBuilder.toString();
    }

    private String getUsbConfString() throws BusinessException {
        // 临时虚机没有策略ID，传null返回默认配置
        return cbbUSBAdvancedSettingMgmtAPI.getUsbConfString(null);
    }
}
