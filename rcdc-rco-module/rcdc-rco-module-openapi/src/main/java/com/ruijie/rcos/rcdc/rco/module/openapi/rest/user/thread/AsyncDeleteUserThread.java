package com.ruijie.rcos.rcdc.rco.module.openapi.rest.user.thread;

import java.util.Collections;
import java.util.List;
import java.util.UUID;

import org.apache.commons.lang3.BooleanUtils;

import com.ruijie.rcos.gss.sdk.iac.module.def.api.IacUserMgmtAPI;
import com.ruijie.rcos.gss.sdk.iac.module.def.dto.user.IacUserDetailDTO;
import com.ruijie.rcos.gss.sdk.iac.module.def.enums.IacUserTypeEnum;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.enums.CbbCloudDeskState;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.enums.CbbCloudDeskType;
import com.ruijie.rcos.rcdc.rco.module.def.api.OpenApiTaskInfoAPI;
import com.ruijie.rcos.rcdc.rco.module.def.api.RccmManageAPI;
import com.ruijie.rcos.rcdc.rco.module.def.api.UserDesktopConfigAPI;
import com.ruijie.rcos.rcdc.rco.module.def.api.UserDesktopMgmtAPI;
import com.ruijie.rcos.rcdc.rco.module.def.api.UserDesktopOperateAPI;
import com.ruijie.rcos.rcdc.rco.module.def.api.UserHardwareCertificationAPI;
import com.ruijie.rcos.rcdc.rco.module.def.api.UserMessageAPI;
import com.ruijie.rcos.rcdc.rco.module.def.api.dto.CloudDesktopDTO;
import com.ruijie.rcos.rcdc.rco.module.def.api.request.CloudDesktopShutdownRequest;
import com.ruijie.rcos.rcdc.rco.module.def.api.request.CloudDesktopStartRequest;
import com.ruijie.rcos.rcdc.rco.module.def.constants.ThrowingConsumer;
import com.ruijie.rcos.rcdc.rco.module.def.enums.UserCloudDeskTypeEnum;
import com.ruijie.rcos.rcdc.rco.module.openapi.rest.asynctask.enums.AsyncTaskEnum;
import com.ruijie.rcos.rcdc.rco.module.openapi.rest.user.request.DeleteUserRequest;
import com.ruijie.rcos.sk.base.exception.BusinessException;
import com.ruijie.rcos.sk.base.log.Logger;
import com.ruijie.rcos.sk.base.log.LoggerFactory;
import com.ruijie.rcos.sk.modulekit.api.comm.IdRequest;

/**
 * Description: 异步删除用户
 * Copyright: Copyright (c) 2022
 * Company: Ruijie Co., Ltd.
 * Create Time: 2022/3/31
 *
 * @author TD
 */
public class AsyncDeleteUserThread extends AbstractAsyncUserMgmtThread {

    private static final Logger LOGGER = LoggerFactory.getLogger(AsyncDeleteUserThread.class);

    private IacUserMgmtAPI cbbUserAPI;

    private UserDesktopConfigAPI userDesktopConfigAPI;

    private UserDesktopMgmtAPI userDesktopMgmtAPI;

    private RccmManageAPI rccmManageAPI;

    private UserHardwareCertificationAPI userHardwareCertificationAPI;

    private UserDesktopOperateAPI cloudDesktopOperateAPI;

    private UserMessageAPI userMessageAPI;

    private DeleteUserRequest request;

    public AsyncDeleteUserThread(UUID userId, AsyncTaskEnum action,
                                 OpenApiTaskInfoAPI openApiTaskInfoAPI, DeleteUserRequest request) throws BusinessException {
        super(userId, action, openApiTaskInfoAPI);
        this.request = request;
    }

    @Override
    public void run() {
        try {
            IacUserDetailDTO detailResponse = cbbUserAPI.getUserByName(request.getUserName());
            UUID userId = detailResponse.getId();
            // 删除关联的用户云桌面数据
            LOGGER.info("==== 删除用户关联的信息 ====");
            deleteRelativeData(userId, request.getEnableForceShutdown(), desktopDTO -> {
                String desktopState = desktopDTO.getDesktopState();
                LOGGER.info("删除用户，准备进行云桌面删除，云桌面状态[{}]", desktopState);
                UUID desktopId = desktopDTO.getId();
                switch (CbbCloudDeskState.valueOf(desktopState)) {
                    case SLEEP:
                    case SLEEPING:
                        cloudDesktopOperateAPI.start(new CloudDesktopStartRequest(desktopId));
                        cloudDesktopOperateAPI.shutdown(new CloudDesktopShutdownRequest(desktopId, true));
                        break;
                    case RUNNING:
                        cloudDesktopOperateAPI.shutdown(new CloudDesktopShutdownRequest(desktopId, true));
                        break;
                    default:
                        LOGGER.info("云桌面状态[{}],不做其它业务处理", desktopState);
                        break;
                }
            });
            // 解绑与该用户绑定的云桌面
            LOGGER.info("==== 解除用户与云桌面的绑定信息 ====");
            userDesktopMgmtAPI.unbindCloudDeskFromUser(userId);

            // 删除用户数据
            LOGGER.info("==== 删除用户信息 ====");
            cbbUserAPI.deleteUser(userId);
            // 访客
            if (detailResponse.getUserType() == IacUserTypeEnum.VISITOR) {
                userDesktopConfigAPI.deleteVisitorUserDesktopConfig(userId);
            }
            userDesktopConfigAPI.deleteUserDesktopConfig(userId, UserCloudDeskTypeEnum.IDV);
            // 删除VOI桌面配置
            userDesktopConfigAPI.deleteUserDesktopConfig(userId, UserCloudDeskTypeEnum.VOI);

            // 删除rccm用户集群缓存
            rccmManageAPI.delRccmUserCLuster(Collections.singletonList(request.getUserName()), true);

            saveTaskSuccess();
        } catch (BusinessException e) {
            LOGGER.error("AsyncDeleteUserThread error:", e);
            saveTaskException(e);
        } catch (Exception e) {
            LOGGER.error("AsyncDeleteUserThread Non business error:", e);
            saveTaskUnknownException(e);
        }
    }

    private void deleteRelativeData(UUID userId, Boolean enableForceShutdown, ThrowingConsumer<CloudDesktopDTO> consumer) throws BusinessException {
        List<CloudDesktopDTO> desktopDTOList = userDesktopMgmtAPI.getAllDesktopByUserId(userId);

        for (CloudDesktopDTO desktopDTO : desktopDTOList) {
            // 强制操作下，不是关机状态的VDI云桌面，做补偿操作
            CbbCloudDeskType cloudDeskType = CbbCloudDeskType.valueOf(desktopDTO.getDesktopCategory());
            if (BooleanUtils.toBoolean(enableForceShutdown)
                    && CbbCloudDeskState.valueOf(desktopDTO.getDesktopState()) != CbbCloudDeskState.CLOSE
                    && cloudDeskType == CbbCloudDeskType.VDI) {
                consumer.accept(desktopDTO);
            }
            userDesktopMgmtAPI.deleteDesktop(desktopDTO.getId(), cloudDeskType);
            // 删除桌面关联用户消息
            userMessageAPI.deleteByDesktopId(new IdRequest(desktopDTO.getCbbId()));
            // 删除桌面关联的应用ISO挂载信息
            userDesktopMgmtAPI.deleteDesktopAppConfig(desktopDTO.getCbbId());
        }
    }

    /**
     * 设置cbbUserAPI
     * 
     * @param cbbUserAPI 用户操作API
     * @return AsyncCreateUserThread
     */
    public AsyncDeleteUserThread setCbbUserAPI(IacUserMgmtAPI cbbUserAPI) {
        this.cbbUserAPI = cbbUserAPI;
        return this;
    }

    /**
     * 设置userDesktopConfigAPI
     * 
     * @param userDesktopConfigAPI 用户桌面操作API
     * @return AsyncCreateUserThread
     */
    public AsyncDeleteUserThread setUserDesktopConfigAPI(UserDesktopConfigAPI userDesktopConfigAPI) {
        this.userDesktopConfigAPI = userDesktopConfigAPI;
        return this;
    }

    /**
     * 设置用户桌面操作API
     * 
     * @param userDesktopMgmtAPI 用户桌面操作API
     * @return AsyncCreateUserThread
     */
    public AsyncDeleteUserThread setUserDesktopMgmtAPI(UserDesktopMgmtAPI userDesktopMgmtAPI) {
        this.userDesktopMgmtAPI = userDesktopMgmtAPI;
        return this;
    }

    /**
     * 设置RCCM操作API
     * 
     * @param rccmManageAPI RCCM操作API
     * @return AsyncDeleteUserThread
     */
    public AsyncDeleteUserThread setRccmManageAPI(RccmManageAPI rccmManageAPI) {
        this.rccmManageAPI = rccmManageAPI;
        return this;
    }

    /**
     * 设置用户硬件特征码相关的API
     * 
     * @param userHardwareCertificationAPI 户硬件特征码相关的API
     * @return AsyncDeleteUserThread
     */
    public AsyncDeleteUserThread setUserHardwareCertificationAPI(UserHardwareCertificationAPI userHardwareCertificationAPI) {
        this.userHardwareCertificationAPI = userHardwareCertificationAPI;
        return this;
    }

    /**
     * 设置云桌面操作API
     * 
     * @param cloudDesktopOperateAPI 云桌面操作API
     * @return AsyncDeleteUserThread
     */
    public AsyncDeleteUserThread setCloudDesktopOperateAPI(UserDesktopOperateAPI cloudDesktopOperateAPI) {
        this.cloudDesktopOperateAPI = cloudDesktopOperateAPI;
        return this;
    }

    public void setUserMessageAPI(UserMessageAPI userMessageAPI) {
        this.userMessageAPI = userMessageAPI;
    }
}
