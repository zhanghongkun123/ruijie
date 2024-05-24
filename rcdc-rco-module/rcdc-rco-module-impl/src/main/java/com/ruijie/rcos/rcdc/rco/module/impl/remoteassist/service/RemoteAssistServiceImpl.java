package com.ruijie.rcos.rcdc.rco.module.impl.remoteassist.service;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.google.common.collect.Interner;
import com.google.common.collect.Interners;
import com.google.common.collect.Maps;
import com.ruijie.rcos.gss.sdk.iac.module.def.api.IacUserMgmtAPI;
import com.ruijie.rcos.gss.sdk.iac.module.def.dto.user.IacUserDetailDTO;
import com.ruijie.rcos.gss.sdk.iac.module.def.enums.IacUserTypeEnum;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.api.CbbDeskRemoteAssistAPI;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.api.CbbGuestToolMessageAPI;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.api.CbbVDIDeskMgmtAPI;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.dto.*;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.enums.*;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.spi.request.CbbRemoteAssistUserConfirmNotifyReqeust;
import com.ruijie.rcos.rcdc.codec.adapter.def.api.CbbTranspondMessageHandlerAPI;
import com.ruijie.rcos.rcdc.codec.adapter.def.callback.CbbTerminalCallback;
import com.ruijie.rcos.rcdc.codec.adapter.def.dto.CbbShineMessageRequest;
import com.ruijie.rcos.rcdc.codec.adapter.def.dto.CbbShineMessageResponse;
import com.ruijie.rcos.rcdc.rco.module.def.api.DesktopAPI;
import com.ruijie.rcos.rcdc.rco.module.def.api.UserTerminalMgmtAPI;
import com.ruijie.rcos.rcdc.rco.module.def.api.dto.CloudDesktopDetailDTO;
import com.ruijie.rcos.rcdc.rco.module.def.api.dto.RemoteAssistInfoDTO;
import com.ruijie.rcos.rcdc.rco.module.def.api.dto.RemoteAssistNotifyContentDTO;
import com.ruijie.rcos.rcdc.rco.module.def.api.enums.IdvTerminalModeEnums;
import com.ruijie.rcos.rcdc.rco.module.def.api.enums.RemoteAssistState;
import com.ruijie.rcos.rcdc.rco.module.def.api.request.RemoteAssistNotifyRequest;
import com.ruijie.rcos.rcdc.rco.module.def.api.request.RemoteAssistUserResponseExpireTimeNotifyRequest;
import com.ruijie.rcos.rcdc.rco.module.impl.BusinessKey;
import com.ruijie.rcos.rcdc.rco.module.impl.Constants;
import com.ruijie.rcos.rcdc.rco.module.impl.RcdcGuestToolCmdKey;
import com.ruijie.rcos.rcdc.rco.module.impl.dao.DeskFaultInfoDAO;
import com.ruijie.rcos.rcdc.rco.module.impl.dao.UserTerminalDAO;
import com.ruijie.rcos.rcdc.rco.module.impl.dto.CreateDeskRemoteAssistDTO;
import com.ruijie.rcos.rcdc.rco.module.impl.entity.DeskFaultInfoEntity;
import com.ruijie.rcos.rcdc.rco.module.impl.entity.UserDesktopEntity;
import com.ruijie.rcos.rcdc.rco.module.impl.entity.UserTerminalEntity;
import com.ruijie.rcos.rcdc.rco.module.impl.entity.ViewUserDesktopEntity;
import com.ruijie.rcos.rcdc.rco.module.impl.enums.CancelRequestRemoteAssistSrcTypeEnum;
import com.ruijie.rcos.rcdc.rco.module.impl.enums.RemoteAssistTypeEnum;
import com.ruijie.rcos.rcdc.rco.module.impl.remoteassist.constant.RemoteAssistBusinessKey;
import com.ruijie.rcos.rcdc.rco.module.impl.remoteassist.dto.RemoteAssistMessageDTO;
import com.ruijie.rcos.rcdc.rco.module.impl.remoteassist.dto.RemoteAssistStatusDTO;
import com.ruijie.rcos.rcdc.rco.module.impl.remoteassist.request.CancelRequestRemoteAssistRequest;
import com.ruijie.rcos.rcdc.rco.module.impl.service.ComputerRemoteAssistMgmtService;
import com.ruijie.rcos.rcdc.rco.module.impl.service.QueryCloudDesktopService;
import com.ruijie.rcos.rcdc.rco.module.impl.service.RemoteAssistService;
import com.ruijie.rcos.rcdc.rco.module.impl.service.struct.RemoteAssistInfo;
import com.ruijie.rcos.rcdc.rco.module.impl.util.BeanUtil;
import com.ruijie.rcos.rcdc.terminal.module.def.api.CbbTerminalOperatorAPI;
import com.ruijie.rcos.sk.base.concurrent.ThreadExecutors;
import com.ruijie.rcos.sk.base.crypto.AesUtil;
import com.ruijie.rcos.sk.base.exception.BusinessException;
import com.ruijie.rcos.sk.base.i18n.LocaleI18nResolver;
import com.ruijie.rcos.sk.base.log.Logger;
import com.ruijie.rcos.sk.base.log.LoggerFactory;
import com.ruijie.rcos.sk.base.util.BeanCopyUtil;
import com.ruijie.rcos.sk.modulekit.api.bootstrap.SafetySingletonInitializer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import java.util.*;

import static com.ruijie.rcos.rcdc.rco.module.def.BusinessKey.RCDC_RCO_USER_NOT_LOGIN;
import static com.ruijie.rcos.rcdc.rco.module.def.BusinessKey.RCDC_USER_CLOUDDESKTOP_NOT_FOUNT_BY_ID;

/**
 * Description: 远程协助管理器实现 Copyright: Copyright (c) 2018 Company: Ruijie Co.,
 * Ltd. Create Time: 2019/1/22
 *
 * @author artom
 */
@Service
public class RemoteAssistServiceImpl implements RemoteAssistService, SafetySingletonInitializer {

    private static final Logger LOGGER = LoggerFactory.getLogger(RemoteAssistServiceImpl.class);

    public static final Integer IN_REMOTE_ASSIST = 1;

    @Autowired
    private CbbDeskRemoteAssistAPI cbbDeskRemoteAssistAPI;

    @Autowired
    private IacUserMgmtAPI cbbUserAPI;

    @Autowired
    private QueryCloudDesktopService queryCloudDesktopService;

    @Autowired
    private ComputerRemoteAssistMgmtService computerRemoteAssistMgmtService;

    @Autowired
    private UserTerminalDAO userTerminalDAO;

    @Autowired
    private CbbGuestToolMessageAPI guestToolMessageAPI;

    @Autowired
    private CbbTranspondMessageHandlerAPI messageHandlerAPI;

    @Autowired
    private DeskFaultInfoDAO desktopFaultInfoDAO;

    @Autowired
    private DesktopAPI desktopAPI;

    @Autowired
    private DeskFaultInfoDAO deskFaultInfoDAO;

    @Autowired
    private CbbVDIDeskMgmtAPI cbbVDIDeskMgmtAPI;

    @Autowired
    private CbbTerminalOperatorAPI terminalOperatorAPI;

    @Autowired
    private UserTerminalMgmtAPI terminalMgmtAPI;

    /**
     * 云桌面ID与远程协助信息键值对
     */
    private Map<UUID, RemoteAssistInfo> remoteAssistInfoMap = Maps.newConcurrentMap();

    /**
     * 延迟启动时间，单位/毫秒
     */
    private static final long INITIAL_DELAY = 1L;

    /**
     * 轮询远程协助是否超时间隔周期时间，单位/毫秒
     */
    private static final long CHECK_PERIOD = 1000L;

    /**
     * 远程协助心跳消息累加次数间隔周期时间，单位/毫秒
     */
    private static final long COUNT_PERIOD = 1000;

    /**
     * 轮询远程协助是否超时线程名称
     */
    private static final String CHECK_REMOTE_ASSIST_EXPIRED_INFO = "check_remote_assist_expired_info";

    /**
     * 轮询远程协助心跳消息是否超时线程名称
     */
    private static final String CHECK_REMOTE_ASSIST_HEARTBEAT_EXPIRED_INFO = "check_remote_assist_heartbeat_expired_info";

    /**
     * 远程协助心跳消息超时时长累加线程名称
     */
    private static final String REMOTE_ASSIST_HEARTBEAT_OVERTIME_COUNT = "remote_assist_heartbeat_overtime_count";

    /**
     * 远程协助请求响应成功
     */
    private static final int INQUIRE_RESULT_SUCCESS = 0;

    /**
     * 远程协助请求响应:远程协助已存在
     */
    private static final int INQUIRE_RESULT_BY_EXIST = 2;

    /**
     * 远程协助心跳消息超时时长，单位/秒
     */
    private static final int HEARTBEAT_OVERTIME_MAX = 10;

    private final Interner<UUID> desktopIdInterner = Interners.newWeakInterner();

    @Override
    public void applyAssist(CreateDeskRemoteAssistDTO dto) throws BusinessException {
        Assert.notNull(dto, "dto must not be null.");

        CloudDesktopDetailDTO cloudDesktopDetailDTO = queryCloudDesktopService.queryDeskDetail(dto.getDeskId());
        LOGGER.info("查询的桌面信息:{}", JSON.toJSONString(cloudDesktopDetailDTO));
        validateDesktop(cloudDesktopDetailDTO);
        CbbDeskInfoDTO cbbDeskInfoDTO = cbbVDIDeskMgmtAPI.getByDeskId(dto.getDeskId());
        if (isRemoteRequested(dto, cloudDesktopDetailDTO, cbbDeskInfoDTO)) {
            validateDeskRemoteAssist(dto);
            // 如果是已请求远程协助的，只需设置远程协助信息即可
            handleDeskRemoteAssistInfo(dto, cloudDesktopDetailDTO);
        } else {
            RemoteAssistInfo info = remoteAssistInfoMap.get(dto.getDeskId());
            if (needCreateNewRemoteAssist(info, dto.getAdminId())) {
                needCreateNewRemoteAssistHandle(dto, cloudDesktopDetailDTO);
            } else {
                noNeedCreateNewRemoteAssistHandle(dto.getDeskId(), dto.getAdminId(), info);
            }
        }
        // 发起远程协助的桌面不是当前正在协助的桌面，需要结束当前正在协助的桌面
        cancelOldRemoteAssistHandle(dto.getDeskId(), dto.getAdminId());
    }

    /**
     * 是否对端已经发起远程协助请求，如EST浮动条
     * @param dto CreateDeskRemoteAssistDTO
     * @param cloudDesktopDetailDTO CloudDesktopDetailDTO
     * @param cbbDeskInfoDTO CbbDeskInfoDTO
     * @return true：对端已经发起远程协助请求，cdc可直接远程
     */
    private boolean isRemoteRequested(CreateDeskRemoteAssistDTO dto, CloudDesktopDetailDTO cloudDesktopDetailDTO, CbbDeskInfoDTO cbbDeskInfoDTO) {

        // 没有主动远程请求记录
        if (!desktopAPI.isRequestRemoteAssist(dto.getDeskId())) {
            return false;
        }

        // 非VDI
        if (!CbbCloudDeskType.VDI.name().equals(cloudDesktopDetailDTO.getDesktopCategory())) {
            return false;
        }

        // HEST要再次发送107，因为107里面EST会判断走带内还是带外，带内还是带外和操作系统还有关系cdc不参与协议的这个规则
        if (CbbEstProtocolType.HEST == cbbDeskInfoDTO.getEstProtocolType()) {
            return false;
        }

        return true;
    }

    /**
     * 判断云桌面是否处于运行中
     *
     * @param cloudDesktopDetailDTO cloudDesktopDetailDTO
     * @throws BusinessException 业务异常
     */
    private void validateDesktop(CloudDesktopDetailDTO cloudDesktopDetailDTO) throws BusinessException {
        if (!CbbCloudDeskState.RUNNING.name().equals(cloudDesktopDetailDTO.getDesktopState())) {
            throw new BusinessException(BusinessKey.RCDC_USER_REMOTE_ASSIST_VM_NORUN);
        }
    }

    private boolean needCreateNewRemoteAssist(RemoteAssistInfo info, UUID adminId) {
        return info == null || info.isFinish() || (info.getState() == RemoteAssistState.WAITING && !info.getAdminId().equals(adminId));
    }

    private void needCreateNewRemoteAssistHandle(CreateDeskRemoteAssistDTO dto, CloudDesktopDetailDTO cloudDesktopDetailDTO)
            throws BusinessException {
        synchronized (desktopIdInterner.intern(dto.getDeskId())) {
            validateDeskRemoteAssist(dto);
            createDeskRemoteAssist(dto, cloudDesktopDetailDTO);
        }
    }

    private void validateDeskRemoteAssist(CreateDeskRemoteAssistDTO dto) throws BusinessException {
        RemoteAssistInfo assistInfo = remoteAssistInfoMap.get(dto.getDeskId());

        if (assistInfo != null && !assistInfo.isFinish()) {
            LOGGER.info("云桌面远程协助信息已存在，云桌面id为[{}]，管理员id为[{}]，管理员名称为[{}]，协助状态为[{}]",
                    dto.getDeskId(), dto.getAdminId(), dto.getAdminName(), assistInfo.getState().name());
            validateWhetherOtherAdminAssistDesk(dto.getAdminId(), assistInfo);
        }

    }

    private void createDeskRemoteAssist(CreateDeskRemoteAssistDTO dto, CloudDesktopDetailDTO cloudDesktopDetailDTO) throws BusinessException {
        RemoteAssistInfo newAssistInfo = new RemoteAssistInfo(dto.getDeskId(), dto.getAdminId(), dto.getAdminName());
        // 在发起新的远程协助时，该桌面在缓存中存在记录，则将旧请求的协助状态置为新请求的oldState
        RemoteAssistInfo oldAssistInfo = remoteAssistInfoMap.get(dto.getDeskId());
        if (oldAssistInfo != null) {
            newAssistInfo.setOldState(oldAssistInfo.getState());
        }
        newAssistInfo.setDesktopName(cloudDesktopDetailDTO.getDesktopName());
        newAssistInfo.setRemoteAssistTypeEnum(RemoteAssistTypeEnum.CLOUDDESKTOP);
        remoteAssistInfoMap.put(dto.getDeskId(), newAssistInfo);
        try {
            CbbDeskRemoteAssistDTO cbbReq = new CbbDeskRemoteAssistDTO();
            cbbReq.setId(cloudDesktopDetailDTO.getId());
            cbbReq.setAutoAgree(isAutoAgree(dto, cloudDesktopDetailDTO));
            CbbDeskInfoDTO deskInfoDTO = cbbVDIDeskMgmtAPI.getByDeskId(dto.getDeskId());
            if (deskInfoDTO.getDeskType() == CbbCloudDeskType.THIRD) {
                // 第三方设置voi
                cbbReq.setRemoteAssistType(CbbRemoteAssistType.VOI);
            } else {
                cbbReq.setRemoteAssistType(CbbRemoteAssistType.valueOf(cloudDesktopDetailDTO.getCbbImageType()));
            }

            cbbReq.setClientProtocolType(deskInfoDTO.getEstProtocolType());
            LOGGER.info("发起远程协助请求内容：{}", JSON.toJSONString(cbbReq));
            callCbbRemoteAssistDesk(cloudDesktopDetailDTO.getId(), cbbReq);
        } catch (BusinessException e) {
            LOGGER.error("远程协助出现异常，发起协助的管理员名称：" + dto.getAdminName() + "，管理员id：" + dto.getAdminId() + "，协助的桌面名称："
                    + cloudDesktopDetailDTO.getDesktopName() + "，桌面id：" + dto.getDeskId(), e);
            remoteAssistInfoMap.remove(dto.getDeskId());
            throw e;
        }
    }

    private void handleDeskRemoteAssistInfo(CreateDeskRemoteAssistDTO dto, CloudDesktopDetailDTO cloudDesktopDetailDTO) throws BusinessException {
        RemoteAssistInfo newAssistInfo = new RemoteAssistInfo(dto.getDeskId(), dto.getAdminId(), dto.getAdminName());
        RemoteAssistInfo oldAssistInfo = remoteAssistInfoMap.get(dto.getDeskId());
        if (oldAssistInfo != null) {
            newAssistInfo.setOldState(oldAssistInfo.getState());
        }
        newAssistInfo.setDesktopName(cloudDesktopDetailDTO.getDesktopName());
        newAssistInfo.setRemoteAssistTypeEnum(RemoteAssistTypeEnum.CLOUDDESKTOP);

        obtainAssistInfo(newAssistInfo);

        LOGGER.info("设置远程协助连接信息为：{}", JSON.toJSONString(newAssistInfo));
        remoteAssistInfoMap.put(dto.getDeskId(), newAssistInfo);
    }

    /**
     * 判断是否自动接受远程协助
     * VDI桌面访客、IDV云桌面公共桌面
     *
     * @param dto                   CreateDeskRemoteAssistDTO
     * @param cloudDesktopDetailDTO cloudDesktopDetailDTO
     * @return true:自动接收
     */

    private boolean isAutoAgree(CreateDeskRemoteAssistDTO dto, CloudDesktopDetailDTO cloudDesktopDetailDTO) throws BusinessException {
        if (dto.getAutoAgree() != null && dto.getAutoAgree()) {
            return true;
        }

        if (desktopAPI.isRequestRemoteAssist(cloudDesktopDetailDTO.getId())) {
            return true;
        }
        if (cloudDesktopDetailDTO.getImageUsage() == ImageUsageTypeEnum.APP) {
            return true;
        }

        if (CbbCloudDeskType.IDV.name().equals(cloudDesktopDetailDTO.getDesktopCategory())) {
            UserTerminalEntity userTerminalEntity = userTerminalDAO.findByBindDeskId(cloudDesktopDetailDTO.getId());
            if (userTerminalEntity == null) {
                LOGGER.error("未找对与云桌面[id:{}]对应的终端绑定关系", cloudDesktopDetailDTO.getId());
                throw new BusinessException(RCDC_USER_CLOUDDESKTOP_NOT_FOUNT_BY_ID, String.valueOf(cloudDesktopDetailDTO.getId()));
            }
            return IdvTerminalModeEnums.PUBLIC == userTerminalEntity.getTerminalMode();
        } else if (CbbCloudDeskType.VDI.name().equals(cloudDesktopDetailDTO.getDesktopCategory())) {
            UserDesktopEntity userDesktopEntity = queryCloudDesktopService.checkAndFindById(dto.getDeskId());
            if (Objects.isNull(userDesktopEntity.getUserId())) {
                throw new BusinessException(RCDC_RCO_USER_NOT_LOGIN);
            }
            IacUserDetailDTO userDetail = cbbUserAPI.getUserDetail(userDesktopEntity.getUserId());
            LOGGER.info("远程协助目标用户信息：{}", JSON.toJSONString(userDetail));
            return IacUserTypeEnum.VISITOR == userDetail.getUserType();
        }

        return false;
    }


    private void callCbbRemoteAssistDesk(UUID cbbDesktopId, CbbDeskRemoteAssistDTO cbbReq) throws BusinessException {
        try {
            CbbDeskApplyRemoteAssistResultDTO response = cbbDeskRemoteAssistAPI.applyRemoteAssist(cbbReq);
            validateRemoteAssistInquireResponse(cbbDesktopId, response);
        } catch (Exception e) {
            LOGGER.error("remoteAssist inquire has exception", e);
            throw new BusinessException(BusinessKey.RCDC_USER_REMOTE_ASSIST_INQUIRE_FAIL, e);
        }
    }

    private void validateRemoteAssistInquireResponse(UUID cbbDesktopId, CbbDeskApplyRemoteAssistResultDTO response) {
        if (response.getCode().intValue() != INQUIRE_RESULT_SUCCESS && response.getCode().intValue() != INQUIRE_RESULT_BY_EXIST) {
            throw new RuntimeException("remoteAssist inquire response code is not agree, code is : " + response.getCode().intValue());
        }
        if (!cbbDesktopId.equals(response.getId())) {
            throw new RuntimeException("remoteAssist inquire response cbbDesktopId is illegal, cbbDesktopId is : " + response.getId());
        }
    }

    private void noNeedCreateNewRemoteAssistHandle(UUID deskId, UUID adminId, RemoteAssistInfo info) throws BusinessException {
        LOGGER.info("请求远程协助，不创建新的协助请求，当前云桌面状态为[{}]", info.getState().name());
        RemoteAssistInfo assistInfo = remoteAssistInfoMap.get(deskId);
        if (assistInfo == null) {
            LOGGER.error("远程协助信息为空，云桌面id为[{}]", deskId);
            throw new BusinessException(BusinessKey.RCDC_USER_REMOTE_ASSIST_QUERY_NO_INFO);
        }
        validateWhetherOtherAdminAssistDesk(adminId, assistInfo);
        // 远程协助处于等待状态时又收到协助请求，抛出“远程协助繁忙”的提示信息
        if (info.getState() == RemoteAssistState.WAITING) {
            throw new BusinessException(BusinessKey.RCDC_USER_REMOTE_ASSIST_IS_WAITTING_STATE, info.getDesktopName());
        }
        if (info.getState() == RemoteAssistState.IN_REMOTE_ASSIST) {
            // 重新获取下远程协助信息，避免token超时
            obtainAssistInfo(info);
            info.changeState(RemoteAssistState.IN_REMOTE_ASSIST);
        }
    }

    private void validateWhetherOtherAdminAssistDesk(UUID adminId, RemoteAssistInfo assistInfo) throws BusinessException {
        // 其他管理员正在进行远程协助操作
        if (!adminId.equals(assistInfo.getAdminId())) {
            LOGGER.error("其他管理员正在进行远程协助，管理员id[{}]，管理员名称[{}]，桌面名称[{}]", assistInfo.getAdminId(), assistInfo.getAdminName(),
                    assistInfo.getDesktopName());
            throw new BusinessException(BusinessKey.RCDC_USER_REMOTE_ASSIST_BY_OTHER_ADMIN, assistInfo.getAdminName());
        }
    }

    @Override
    public void cancelOldRemoteAssistHandle(UUID newDeskId, UUID adminId) {
        Assert.notNull(newDeskId, "newDeskId must not be null.");
        Assert.notNull(adminId, "adminId must not be null.");

        // 执行取消同一管理员的之前的PC远程协助
        List<RemoteAssistNotifyContentDTO> remoteAssistNotifyContentDTOList = new ArrayList<>();

        // 执行取消同一管理员的远程协助
        remoteAssistInfoMap.forEach((deskId, assistInfo) -> {
            if (isRemoteAssistOtherDesk(newDeskId, adminId, RemoteAssistTypeEnum.CLOUDDESKTOP, assistInfo)) {
                assistInfo.changeState(RemoteAssistState.STOP_ADMIN);
                sendCancelRemoteAssist(deskId);
            }

            if (isRemoteAssistOtherDesk(newDeskId, adminId, RemoteAssistTypeEnum.COMPUTER, assistInfo)
                    && (assistInfo.getState() == RemoteAssistState.AGREE || assistInfo.getState() == RemoteAssistState.IN_REMOTE_ASSIST)) {
                LOGGER.info("notify <{}> cancel remote assist", deskId);
                RemoteAssistNotifyContentDTO remoteAssistNotifyContentDTO = new RemoteAssistNotifyContentDTO(deskId, assistInfo.getAdminId());
                remoteAssistNotifyContentDTOList.add(remoteAssistNotifyContentDTO);
            }

        });

        notify(remoteAssistNotifyContentDTOList);
    }

    private boolean isRemoteAssistOtherDesk(UUID newDeskId, UUID adminId, RemoteAssistTypeEnum remoteAssistTypeEnum, RemoteAssistInfo assistInfo) {
        if (!assistInfo.getAdminId().equals(adminId)) {
            LOGGER.info("判断管理员【{}】是否有远程其他桌面。管理员不一致。newDeskId = {}, remoteAssistTypeEnum={}, assistInfo = {}",
                    adminId, newDeskId, remoteAssistTypeEnum.toString(), assistInfo.toString());
            return false;
        }
        if (assistInfo.getRemoteAssistTypeEnum() != remoteAssistTypeEnum) {
            LOGGER.info("判断管理员【{}】是否有远程其他桌面。远程协助类型不一致。newDeskId = {}, remoteAssistTypeEnum={}, assistInfo = {}",
                    adminId, newDeskId, remoteAssistTypeEnum.toString(), assistInfo.toString());
            return false;
        }
        if (assistInfo.getDeskId().equals(newDeskId)) {
            LOGGER.info("判断管理员【{}】是否有远程其他桌面。远程协助同一桌面。newDeskId = {}, remoteAssistTypeEnum={}, assistInfo = {}",
                    adminId, newDeskId, remoteAssistTypeEnum.toString(), assistInfo.toString());
            return false;
        }
        if (assistInfo.isFinish()) {
            LOGGER.info("判断管理员【{}】是否有远程其他桌面。远程协助处于完成状态。newDeskId = {}, remoteAssistTypeEnum={}, assistInfo = {}",
                    adminId, newDeskId, remoteAssistTypeEnum.toString(), assistInfo.toString());
            return false;
        }
        LOGGER.info("判断管理员【{}】是否有远程其他桌面。newDeskId = {}, remoteAssistTypeEnum={}, assistInfo = {}",
                adminId, newDeskId, remoteAssistTypeEnum.toString(), assistInfo.toString());
        return true;
    }

    @Override
    public RemoteAssistInfo queryAssisInfo(UUID deskId, UUID adminId) throws BusinessException {
        Assert.notNull(deskId, "deskId must not be null.");
        Assert.notNull(adminId, "adminId must not be null.");
        RemoteAssistInfo assist = remoteAssistInfoMap.get(deskId);

        if (assist == null) {
            throw new BusinessException(BusinessKey.RCDC_USER_REMOTE_ASSIST_QUERY_NO_INFO);
        }
        if (assist.getAdminId().equals(adminId)) {
            if (assist.getState() == RemoteAssistState.IN_REMOTE_ASSIST || assist.getState() == RemoteAssistState.AGREE) {
                // 正在协助中，更新时间戳
                assist.updateStamp();
            }
            LOGGER.info("queryAssistInfo state is [{}], deskId is [{}]", assist.getState().name(), deskId);
            return assist;
        }

        RemoteAssistInfo assistInfo = new RemoteAssistInfo(assist.getDeskId(), adminId);
        BeanCopyUtil.copy(assist, assistInfo);

        assistInfo.setAdminId(adminId);
        if (hasRequestRemoteAssist(deskId)) {
            LOGGER.info("直接进行远程协助[{}]", deskId);
            assistInfo.setState(RemoteAssistState.AGREE);
        } else {
            assistInfo.setState(assist.getOldState());
        }
        return assistInfo;
    }

    @Override
    public void notifyAssistResult(UUID deskId, CbbRemoteAssistUserConfirmNotifyReqeust request) throws BusinessException {
        Assert.notNull(deskId, "deskId must not be null.");
        Assert.notNull(request, "request must not be null.");

        RemoteAssistInfo info = remoteAssistInfoMap.get(deskId);
        if (info == null) {
            LOGGER.error("can not find info from map. deskId is [{}]", deskId);
            sendCancelRemoteAssist(deskId);
            return;
        }
        synchronized (info) {
            if (shouldHandleNotify(deskId, info)) {
                info.setRemoteConnectChain(request.getRemoteConnectChain());
                userOperateResultHandle(request.getAcceptRemote(), info);
                return;
            }
        }
        info.changeState(RemoteAssistState.FINISH);
        // 收到用户同意远程协助消息时，管理员已经不再是等待接受状态了，需要通知guesttool取消协助
        sendCancelRemoteAssist(deskId);
    }

    @Override
    public boolean hasRequestRemoteAssist(UUID deskId) {
        Assert.notNull(deskId, "diskId must not be null");
        DeskFaultInfoEntity desktopFaultInfo = desktopFaultInfoDAO.findByDeskId(deskId);
        if (desktopFaultInfo == null) {
            return false;
        }
        return desktopFaultInfo.getFaultState();
    }

    @Override
    public int remoteAssistNum() {
        return remoteAssistInfoMap.size();
    }

    @Override
    public void requestRemoteAssist(UUID deskId, String terminalId) throws BusinessException {
        Assert.notNull(deskId, "diskId must not be null");
        Assert.notNull(terminalId, "terminalId must not be null");
        CbbDeskDTO deskInfo = cbbVDIDeskMgmtAPI.getDeskVDI(deskId);
        DeskFaultInfoEntity deskFaultInfoEntity = createDeskFault(deskInfo);
        // 通知小助手显示报障
        sendDeskFaultMessage(terminalId);

    }

    @Override
    public void requestRemoteAssist(UUID deskId) throws BusinessException {
        Assert.notNull(deskId, "diskId must not be null");
        CbbDeskDTO deskInfo = cbbVDIDeskMgmtAPI.getDeskVDI(deskId);
        DeskFaultInfoEntity deskFaultInfoEntity = createDeskFault(deskInfo);
        // 通知小助手显示报障
        sendDeskFaultMessage(deskFaultInfoEntity.getMac());
    }

    private DeskFaultInfoEntity createDeskFault(CbbDeskDTO deskInfo) throws BusinessException {
        UUID deskId = deskInfo.getDeskId();
        DeskFaultInfoEntity deskFaultInfoEntity = deskFaultInfoDAO.findByDeskId(deskId);
        if (deskInfo.getDeskState() == CbbCloudDeskState.RECYCLE_BIN || deskInfo.getDeskState() == CbbCloudDeskState.DELETING) {
            throw new BusinessException(com.ruijie.rcos.rcdc.rco.module.def.BusinessKey.RCDC_USER_CLOUDDESKTOP_DESK_STATE_NOT_ALLOW,
                    deskInfo.getDeskState().name());
        }
        if (deskFaultInfoEntity != null) {
            deskFaultInfoEntity.setFaultState(true);
            deskFaultInfoEntity.setFaultDescription(LocaleI18nResolver.resolve(RemoteAssistBusinessKey.RCDC_USER_REQUEST_REMOTE_ASSIST));
            deskFaultInfoEntity.setFaultTime(new Date());
            deskFaultInfoDAO.save(deskFaultInfoEntity);
        } else {
            deskFaultInfoEntity = new DeskFaultInfoEntity();
            deskFaultInfoEntity.setDeskId(deskId);
            deskFaultInfoEntity.setMac(deskInfo.getDeskMac());
            deskFaultInfoEntity.setFaultState(true);
            deskFaultInfoEntity.setFaultDescription(LocaleI18nResolver.resolve(RemoteAssistBusinessKey.RCDC_USER_REQUEST_REMOTE_ASSIST));
            deskFaultInfoEntity.setFaultTime(new Date());
            deskFaultInfoEntity.setCreateTime(new Date());
            deskFaultInfoDAO.save(deskFaultInfoEntity);
        }
        return deskFaultInfoEntity;
    }


    private void sendDeskFaultMessage(String terminalId) {
        CbbShineMessageRequest shineMessageRequest = CbbShineMessageRequest.create(Constants.ASSISTANT_APP_SHOW_FAULT, terminalId);
        JSONObject content = new JSONObject();
        content.put("content", LocaleI18nResolver.resolve(RemoteAssistBusinessKey.RCDC_USER_REQUEST_REMOTE_ASSIST));
        shineMessageRequest.setContent(content);
        try {
            messageHandlerAPI.syncRequest(shineMessageRequest);
        } catch (Exception e) {
            LOGGER.error("通知小助手失败 终端 id = {}", terminalId, e);
        }
    }

    @Override
    public void cancelRemoteAssist(UUID deskId, String terminalId) {
        Assert.notNull(deskId, "diskId must not be null");
        Assert.notNull(terminalId, "terminalId must not be null");

        DeskFaultInfoEntity deskFaultInfoEntity = deskFaultInfoDAO.findByDeskId(deskId);
        if (deskFaultInfoEntity != null) {
            deskFaultInfoEntity.setFaultState(false);
            deskFaultInfoEntity.setFaultDescription("");
            deskFaultInfoDAO.save(deskFaultInfoEntity);
            // 通知终端小助手取消报障
            try {
                terminalOperatorAPI.relieveFault(terminalId,
                        new CancelRequestRemoteAssistRequest(CancelRequestRemoteAssistSrcTypeEnum.USER));
            } catch (BusinessException e) {
                LOGGER.error("通知小助手取消报障失败，终端 id = {}", terminalId);
            }
        }
    }

    @Override
    public void cancelRemoteAssist(UUID deskId) {
        Assert.notNull(deskId, "diskId must not be null");
        DeskFaultInfoEntity deskFaultInfoEntity = deskFaultInfoDAO.findByDeskId(deskId);
        if (deskFaultInfoEntity != null) {
            deskFaultInfoEntity.setFaultState(false);
            deskFaultInfoEntity.setFaultDescription("");
            deskFaultInfoDAO.save(deskFaultInfoEntity);
            // 通知终端小助手取消报障
            try {
                terminalOperatorAPI.relieveFault(deskFaultInfoEntity.getMac(),
                        new CancelRequestRemoteAssistRequest(CancelRequestRemoteAssistSrcTypeEnum.USER));
            } catch (BusinessException e) {
                LOGGER.error("通知小助手取消报障失败，mac id = {}", deskFaultInfoEntity.getMac());
            }
        }
    }

    private boolean shouldHandleNotify(UUID deskId, RemoteAssistInfo info) {
        LOGGER.info("remoteAssistInfo state is [{}]", info.getState().name());
        if (info.getState() != RemoteAssistState.WAITING) {
            LOGGER.warn("remote state is not watting, can not remote assist. deskId is [{}], remote state is [{}]", deskId, info.getState());
            return false;
        }
        return true;
    }

    private void userOperateResultHandle(boolean userOperateType, RemoteAssistInfo info) throws BusinessException {
        if (userOperateType) {
            obtainAssistInfo(info);
        } else {
            info.changeState(RemoteAssistState.REJECT);
        }
    }

    private void obtainAssistInfo(RemoteAssistInfo info) throws BusinessException {
        UUID deskId = info.getDeskId();
        try {
            ViewUserDesktopEntity desktopViewEntity = queryCloudDesktopService.checkDesktopExistInDeskViewById(deskId);
            Assert.notNull(desktopViewEntity.getDesktopType(), "desk type is null!");

            CbbObtainDeskRemoteAssistConnectionInfoDTO cbbReq = new CbbObtainDeskRemoteAssistConnectionInfoDTO();
            cbbReq.setId(deskId);
            if (Objects.equals(desktopViewEntity.getDesktopType(), CbbCloudDeskType.THIRD.name())) {
                //第三方桌面设置VOI
                cbbReq.setDeskType(CbbRemoteAssistType.VOI);
                cbbReq.setTerminalId(deskId.toString());
            } else {
                cbbReq.setDeskType(CbbRemoteAssistType.valueOf(desktopViewEntity.getCbbImageType()));
                // 可能取不到，要验证
                cbbReq.setTerminalId(desktopViewEntity.getTerminalId());
            }
            cbbReq.setRemoteConnectChain(info.getRemoteConnectChain());

            CbbDeskRemoteAssistResultDTO cbbResp = cbbDeskRemoteAssistAPI.obtainDeskRemoteAssistInfo(cbbReq);
            info.setAssistIp(cbbResp.getIp());
            info.setAssistPort(cbbResp.getPort());
            info.setAssistToken(cbbResp.getToken());
            info.setServerPort(cbbResp.getServerPort());
            info.setServerAddr(cbbResp.getServerAddr());
            if (!StringUtils.isEmpty(cbbResp.getPwd())) {
                info.setPassword((AesUtil.encrypt(cbbResp.getPwd(), Constants.AES_ASSIST_KEY)));
            }
            info.changeState(RemoteAssistState.AGREE);
            if (cbbResp.getIsCrypto() != null) {
                info.setSsl(cbbResp.getIsCrypto());
            }
        } catch (BusinessException e) {
            info.changeState(RemoteAssistState.START_FAIL);
            LOGGER.error("obtainAssistInfo fail. deskId is : " + deskId, e);
            throw new BusinessException(BusinessKey.RCDC_USER_REMOTE_ASSIST_OBTAIN_CONNECT_INFO_FAIL, e);
        }
    }

    @Override
    public void userCloseAssist(UUID deskId, Integer operateCode) {
        Assert.notNull(deskId, "deskId must not be null.");
        Assert.notNull(operateCode, "operateCode must not be null.");
        RemoteAssistInfo info = remoteAssistInfoMap.get(deskId);
        if (info == null) {
            LOGGER.error("can not find info from map. May be admin");
        } else {
            if (operateCode.equals(0)) {
                LOGGER.info("用户发送了关闭远程协助的消息, deskId[{}]", deskId);
                info.changeState(RemoteAssistState.STOP_USER);
                info.resetVncHeartbeatOvertimeCount();
            } else {
                LOGGER.error("终端远程连接出现异常, deskId[{}]", deskId);
                info.changeState(RemoteAssistState.ERROR);
                info.resetVncHeartbeatOvertimeCount();
            }

        }


    }

    @Override
    public void userAssistStartFail(UUID deskId) {
        Assert.notNull(deskId, "deskId must not be null.");
        RemoteAssistInfo info = remoteAssistInfoMap.get(deskId);
        if (info == null) {
            LOGGER.error("can not find info from map. May be admin");
        } else {
            info.changeState(RemoteAssistState.START_FAIL);
        }
    }

    @Override
    public void adminCancelAssist(UUID deskId, UUID adminId) throws BusinessException {
        Assert.notNull(deskId, "deskId must not be null.");
        Assert.notNull(adminId, "adminId must not be null.");
        RemoteAssistInfo info = remoteAssistInfoMap.get(deskId);
        if (info == null) {
            LOGGER.error("can not find info from map. deskId is [{}]", deskId);
            return;
        }
        if (!adminId.equals(info.getAdminId())) {
            LOGGER.warn("other admin is assist. cancel is forbid. deskId is [{}]", deskId);
            return;
        }
        synchronized (info) {
            info.changeState(RemoteAssistState.STOP_ADMIN);
            sendCancelRemoteAssist(deskId);
        }
    }

    @Override
    public void deskShutdownHandle(UUID deskId) {
        Assert.notNull(deskId, "deskId must not be null");
        RemoteAssistInfo info = remoteAssistInfoMap.get(deskId);
        if (info == null) {
            LOGGER.error("can not find info from map. deskId is [{}]", deskId);
            return;
        }
        info.changeState(RemoteAssistState.FINISH);
    }

    @Override
    public void createVncChannelResult(UUID deskId, UUID adminId) throws BusinessException {
        Assert.notNull(deskId, "deskId must not be null");
        Assert.notNull(adminId, "adminId must not be null");
        RemoteAssistInfo info = remoteAssistInfoMap.get(deskId);
        if (info == null) {
            LOGGER.error("can not find info from map. deskId is [{}]", deskId);
            return;
        }

        try {
            // 未报障：只发111
            // 已报障：要么只发5004，要么如果有发111的话，必须在5004之后发
            afterCreateVncChannelHandle(deskId);
            if (hasRequestRemoteAssist(deskId)) {
                // 终端有请求远程协助时，通知 RAtool 远程建立成功
                syncRemoteAssistStatus(deskId);
            } else {
                cbbDeskRemoteAssistAPI.afterRemoteAssistChannelCreated(deskId);
            }

        } catch (BusinessException e) {
            info.changeState(RemoteAssistState.RESPONSE_TIMEOUT);
            LOGGER.error("createVncChannelResult notify guesttool has exception, deskId is : " + deskId, e);
        }
    }

    /**
     * vnc通道创建成功后的处理->将远程协助状态更新为“在远程协助中”
     *
     * @param newDeskId 新的vnc对应的桌面id
     */
    private void afterCreateVncChannelHandle(UUID newDeskId) {
        RemoteAssistInfo info = remoteAssistInfoMap.get(newDeskId);
        if (info == null) {
            LOGGER.error("can not find info from map. deskId is [{}]", newDeskId);
            return;
        }
        // 只对处于AGREE状态的远程状态更新为IN_REMOTE_ASSIST
        if (info.getState() == RemoteAssistState.AGREE) {
            info.changeState(RemoteAssistState.IN_REMOTE_ASSIST);
        }
    }

    @Override
    public void estHeartbeatHandle(UUID deskId) {
        Assert.notNull(deskId, "deskId must not be null");
        RemoteAssistInfo info = remoteAssistInfoMap.get(deskId);
        if (info == null) {
            LOGGER.error("can not find info from map. deskId is [{}]", deskId);
            return;
        }
        if (info.getState() != RemoteAssistState.IN_REMOTE_ASSIST) {
            LOGGER.error("vnc heartbeat must send [{}] state. desk id is [{}]", info.getState().name(), deskId);
            return;
        }
        info.resetVncHeartbeatOvertimeCount();
    }

    @Override
    public void safeInit() {
        ThreadExecutors.scheduleAtFixedRate(CHECK_REMOTE_ASSIST_EXPIRED_INFO, this::checkExpiredInfo, INITIAL_DELAY, CHECK_PERIOD);

        ThreadExecutors
                .scheduleAtFixedRate(CHECK_REMOTE_ASSIST_HEARTBEAT_EXPIRED_INFO, this::checkVncHeartbeatOvertimeCount, INITIAL_DELAY, CHECK_PERIOD);

        ThreadExecutors
                .scheduleAtFixedRate(REMOTE_ASSIST_HEARTBEAT_OVERTIME_COUNT, this::increaseVncHeartbeatCountHandle, INITIAL_DELAY, COUNT_PERIOD);
    }

    /**
     * * 清除过期的远程协助信息
     */
    protected void checkExpiredInfo() {
        // 执行清除已过期的远程协助
        remoteAssistInfoMap.forEach((deskId, assistInfo) -> {
            RemoteAssistState state = assistInfo.getState();
            if (assistInfo.getRemoteAssistTypeEnum() == RemoteAssistTypeEnum.CLOUDDESKTOP && assistInfo.isStateExpired()) {
                assistStateExpiredHandle(deskId, assistInfo, state);
            }
            if (assistInfo.getRemoteAssistTypeEnum() == RemoteAssistTypeEnum.CLOUDDESKTOP && assistInfo.isUserResponseExpired()) {
                userResponseExpiredHandle(deskId, assistInfo, state);
            }
        });
        // 执行清除已过期的PC远程协助
        List<RemoteAssistNotifyContentDTO> remoteAssistNotifyContentDTOList = new ArrayList<>();
        List<RemoteAssistInfo> expiredRemoteAssistInfoList = new ArrayList<>();
        remoteAssistInfoMap.forEach((deskId, assistInfo) -> {
            if (isComputerExpired(assistInfo)) {
                LOGGER.info("notify <{}> cancel remote assist", deskId);
                RemoteAssistNotifyContentDTO remoteAssistNotifyContentDTO = new RemoteAssistNotifyContentDTO(deskId, assistInfo.getAdminId());
                remoteAssistNotifyContentDTOList.add(remoteAssistNotifyContentDTO);
                expiredRemoteAssistInfoList.add(assistInfo);
            }

            if (assistInfo.getRemoteAssistTypeEnum() == RemoteAssistTypeEnum.COMPUTER && assistInfo.isUserResponseExpired()
                    && assistInfo.getState() == RemoteAssistState.WAITING) {
                computerUserResponseExpiredTime(deskId);
            }
        });

        notify(remoteAssistNotifyContentDTOList);
        if (!CollectionUtils.isEmpty(expiredRemoteAssistInfoList)) {
            for (RemoteAssistInfo remoteAssistInfo : expiredRemoteAssistInfoList) {
                assistStateExpiredHandle(remoteAssistInfo.getDeskId(), remoteAssistInfo, remoteAssistInfo.getState());
            }
        }

    }

    private boolean isComputerExpired(RemoteAssistInfo assistInfo) {
        boolean isComputer = assistInfo.getRemoteAssistTypeEnum() == RemoteAssistTypeEnum.COMPUTER;
        if (!isComputer) {
            return false;
        }
        boolean isAgreeOrInAssist = assistInfo.getState() == RemoteAssistState.AGREE || assistInfo.getState() == RemoteAssistState.IN_REMOTE_ASSIST;
        if (isAgreeOrInAssist) {
            return false;
        }
        return assistInfo.isStateExpired();
    }

    private void assistStateExpiredHandle(UUID deskId, RemoteAssistInfo assistInfo, RemoteAssistState state) {
        switch (state) {
            // 用户同意远程协助，vnc通道创建超时
            case AGREE:
                // 管理员关闭浏览器后超时
            case IN_REMOTE_ASSIST:
                assistInfo.changeState(RemoteAssistState.STOP_TIMEOUT);
                sendCancelRemoteAssist(deskId);
                break;
            case WAITING:
                break;
            default:
                remoteAssistInfoMap.remove(deskId);
                break;
        }
    }

    private void userResponseExpiredHandle(UUID deskId, RemoteAssistInfo assistInfo, RemoteAssistState state) {
        // 管理员请求远程协助，用户未响应超时
        if (state == RemoteAssistState.WAITING) {
            assistInfo.changeState(RemoteAssistState.STOP_TIMEOUT);
            sendCancelRemoteAssist(deskId);
        }
    }

    private void sendCancelRemoteAssist(UUID deskId) {
        try {
            LOGGER.info("发送取消远程协助消息，cbbDesktopId = [{}]", deskId);
            cbbDeskRemoteAssistAPI.cancelRemoteAssistDesk(deskId);
        } catch (BusinessException e) {
            LOGGER.error("cancelRemoteAssistDeskVDI fail. deskId is : " + deskId, e);
        }
    }

    /**
     * 检测远程协助心跳消息超时次数
     */
    private void checkVncHeartbeatOvertimeCount() {
        remoteAssistInfoMap.forEach((deskId, assistInfo) -> {
            DeskFaultInfoEntity deskFaultInfo = desktopFaultInfoDAO.findByDeskId(deskId);
            if (assistInfo.getOvertimeCount() > HEARTBEAT_OVERTIME_MAX) {
                if (deskFaultInfo != null && Boolean.TRUE.equals(deskFaultInfo.getFaultState())) {
                    return;
                }
                LOGGER.info("reset est heart beat over time.<{}>", deskId);
                assistInfo.changeState(RemoteAssistState.STOP_TIMEOUT);
                assistInfo.resetVncHeartbeatOvertimeCount();
            }
        });
    }

    /**
     * 远程协助心跳消息超时累加次数处理
     */
    private void increaseVncHeartbeatCountHandle() {
        remoteAssistInfoMap.forEach((deskId, assistInfo) -> {
            if (assistInfo.getState() == RemoteAssistState.IN_REMOTE_ASSIST) {
                assistInfo.increaseOvertimeCount();
            }
        });
    }

    @Override
    public RemoteAssistInfo queryRemoteAssistInfo(UUID deskId) {
        Assert.notNull(deskId, "deskId is not be null");
        return remoteAssistInfoMap.get(deskId);
    }

    @Override
    public void removeRemoteAssistInfo(UUID deskId) {
        Assert.notNull(deskId, "deskId is not be null");
        remoteAssistInfoMap.remove(deskId);
    }

    @Override
    public void createRemoteAssistInfo(UUID deskId, RemoteAssistInfo info) {
        Assert.notNull(deskId, "deskId is not be null");
        Assert.notNull(info, "info is not be null");
        remoteAssistInfoMap.put(deskId, info);
    }

    @Override
    public void updateRemoteAssistInfo(RemoteAssistInfoDTO infoDTO) {
        Assert.notNull(infoDTO, "infoDTO is not be null");
        RemoteAssistInfo oldInfo = remoteAssistInfoMap.get(infoDTO.getDeskId());
        BeanUtil.copyPropertiesIgnoreNull(infoDTO, oldInfo);
        oldInfo.updateStamp();
    }

    @Override
    public void resetVncHeartBeat(UUID deskId) {
        Assert.notNull(deskId, "deskId is not be null");
        RemoteAssistInfo info = remoteAssistInfoMap.get(deskId);
        info.resetVncHeartbeatOvertimeCount();
    }

    @Override
    public void changeAssistState(UUID deskId, RemoteAssistState state) {
        Assert.notNull(deskId, "deskId is not be null");
        Assert.notNull(state, "state is not be null");
        RemoteAssistInfo info = remoteAssistInfoMap.get(deskId);
        info.changeState(state);
    }

    @Override
    public void syncRemoteAssistStatus(UUID deskId) {
        Assert.notNull(deskId, "deskId is not be null");
        RemoteAssistStatusDTO remoteAssistStatusDTO = new RemoteAssistStatusDTO();
        RemoteAssistStatusDTO.Content statusContent = new RemoteAssistStatusDTO.Content();
        statusContent.setConnectStatus(IN_REMOTE_ASSIST);
        remoteAssistStatusDTO.setCode(0);
        remoteAssistStatusDTO.setMessage("");
        remoteAssistStatusDTO.setContent(statusContent);

        CbbGuesttoolMessageDTO cbbGuesttoolMessageDTO = buildSendMessageRequest(remoteAssistStatusDTO);
        cbbGuesttoolMessageDTO.setDeskId(deskId);

        try {
            guestToolMessageAPI.asyncRequest(cbbGuesttoolMessageDTO);
        } catch (BusinessException e) {
            LOGGER.error("远程协助状态信息发送失败", e);
        }

    }

    @Override
    public void syncRemoteAssistRequestStatus(String terminalId, Boolean hasRequest) {
        Assert.notNull(terminalId, "terminalId is not be null");
        Assert.notNull(hasRequest, "hasRequest is not be null");
        RemoteAssistMessageDTO remoteAssistMessageDTO = new RemoteAssistMessageDTO();
        remoteAssistMessageDTO.setHasRequest(hasRequest);
        CbbShineMessageRequest shineMessageRequest = CbbShineMessageRequest.create(Constants.REMOTE_ASSIST_REQUEST_STATUS, terminalId);
        shineMessageRequest.setContent(remoteAssistMessageDTO);
        try {
            messageHandlerAPI.asyncRequest(shineMessageRequest, new CbbTerminalCallback() {
                @Override
                public void success(String terminalId, CbbShineMessageResponse msg) {
                    Assert.notNull(terminalId, "terminalId cannot be null!");
                    Assert.notNull(msg, "msg cannot be null!");
                    LOGGER.info("通知终端远程协助状态成功，terminalId[{}]，信息[{}]", terminalId, msg.toString());
                }

                @Override
                public void timeout(String terminalId) {
                    Assert.notNull(terminalId, "terminalId cannot be null!");
                    LOGGER.error("通知终端远程协助状态超时，terminalId[{}]", terminalId);
                }
            });
        } catch (Exception e) {
            LOGGER.error("通知终端远程协助状态失败 terminal id = {}", terminalId);
        }
    }

    private CbbGuesttoolMessageDTO buildSendMessageRequest(RemoteAssistStatusDTO remoteAssistStatusDTO) {
        final CbbGuesttoolMessageDTO dto = new CbbGuesttoolMessageDTO();
        dto.setBody(JSON.toJSONString(remoteAssistStatusDTO));
        dto.setCmdId(RcdcGuestToolCmdKey.RCDC_RA_TOOL_CMD_ID_REMOTE_ASSIST_STATUS);
        dto.setPortId(RcdcGuestToolCmdKey.RCDC_RA_TOOL_PORT_ID_REMOTE_ASSIST_STATUS);

        return dto;
    }

    private void notify(List<RemoteAssistNotifyContentDTO> remoteAssistNotifyContentDTOList) {
        if (!remoteAssistNotifyContentDTOList.isEmpty()) {
            RemoteAssistNotifyContentDTO[] remoteAssistNotifyContentDTOArr =
                    new RemoteAssistNotifyContentDTO[remoteAssistNotifyContentDTOList.size()];
            remoteAssistNotifyContentDTOArr = remoteAssistNotifyContentDTOList.toArray(remoteAssistNotifyContentDTOArr);
            RemoteAssistNotifyRequest remoteAssistNotifyRequest = new RemoteAssistNotifyRequest();
            remoteAssistNotifyRequest.setRemoteAssistNotifyContentDTOArr(remoteAssistNotifyContentDTOArr);
            notify(remoteAssistNotifyRequest);
        }
    }

    private void notify(RemoteAssistNotifyRequest remoteAssistNotifyRequest) {
        try {
            for (RemoteAssistNotifyContentDTO remoteAssistNotifyContentDTO : remoteAssistNotifyRequest
                    .getRemoteAssistNotifyContentDTOArr()) {
                computerRemoteAssistMgmtService.adminCancelAssist(remoteAssistNotifyContentDTO.getDeskId(),
                        remoteAssistNotifyContentDTO.getAdminId());
            }
        } catch (Exception e) {
            LOGGER.error("管理员创建远程协助联动同步", e);
        }
    }

    private void computerUserResponseExpiredTime(UUID deskId) {
        LOGGER.info("远程协助等待用户回应超时：deskId = {}", deskId);
        RemoteAssistUserResponseExpireTimeNotifyRequest request = new RemoteAssistUserResponseExpireTimeNotifyRequest();
        request.setDeskId(deskId);
        try {
            computerRemoteAssistMgmtService.computerUserResponseExpiredTime(request.getDeskId());
        } catch (Exception e) {
            LOGGER.error("消息通知：远程协助等待用户回应超时, DeskId = {}", request.getDeskId(), e);
        }

    }


}
