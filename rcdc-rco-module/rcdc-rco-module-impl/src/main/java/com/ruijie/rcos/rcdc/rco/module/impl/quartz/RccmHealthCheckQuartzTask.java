package com.ruijie.rcos.rcdc.rco.module.impl.quartz;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.api.CbbImageTemplateMgmtAPI;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.dto.CbbImageTemplateDetailDTO;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.dto.image.CbbImageTemplateDTO;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.enums.ImageTemplateState;
import com.ruijie.rcos.rcdc.hciadapter.module.def.api.ImageMgmtAPI;
import com.ruijie.rcos.rcdc.hciadapter.module.def.api.request.image.replication.QueryImageSyncTaskRequest;
import com.ruijie.rcos.rcdc.rco.module.def.api.RccmManageAPI;
import com.ruijie.rcos.rcdc.rco.module.def.api.RcoGlobalParameterAPI;
import com.ruijie.rcos.rcdc.rco.module.def.api.dto.rccm.RccmServerConfigDTO;
import com.ruijie.rcos.rcdc.rco.module.def.api.request.FindParameterRequest;
import com.ruijie.rcos.rcdc.rco.module.def.api.response.FindParameterResponse;
import com.ruijie.rcos.rcdc.rco.module.def.constants.ScheduleTypeCodeConstants;
import com.ruijie.rcos.rcdc.rco.module.def.utils.RedLineUtil;
import com.ruijie.rcos.rcdc.rco.module.impl.BusinessKey;
import com.ruijie.rcos.rcdc.rco.module.impl.Constants;
import com.ruijie.rcos.rcdc.rco.module.impl.RccmRestKey;
import com.ruijie.rcos.rcdc.rco.module.impl.dto.RequestParamDTO;
import com.ruijie.rcos.rcdc.rco.module.impl.rccm.service.RccmManageService;
import com.ruijie.rcos.rcdc.rco.module.impl.spi.common.CommonMessageCode;
import com.ruijie.rcos.rcdc.rco.module.impl.util.RestUtil;
import com.ruijie.rcos.sk.base.crypto.AesUtil;
import com.ruijie.rcos.sk.base.exception.BusinessException;
import com.ruijie.rcos.sk.base.log.Logger;
import com.ruijie.rcos.sk.base.log.LoggerFactory;
import com.ruijie.rcos.sk.base.quartz.Quartz;
import com.ruijie.rcos.sk.base.quartz.QuartzTask;
import com.ruijie.rcos.sk.base.quartz.QuartzTaskContext;
import com.ruijie.rcos.sk.base.sm2.StateMachineFactory;
import com.ruijie.rcos.sk.base.sm2.StateMachineMgmtAgent;
import com.ruijie.rcos.sk.connectkit.api.data.base.RemoteResponse;
import org.apache.commons.lang3.ArrayUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import java.util.*;
import java.util.concurrent.TimeUnit;

/**
 * Description: rcdc与rccm 健康检查任务
 * Copyright: Copyright (c) 2021
 * Company: Ruijie Co., Ltd.
 * Create Time: 2021-10-09
 * 0/5 * * * * ? *
 *
 * @author zqj
 */
@Service
@Quartz(scheduleTypeCode = ScheduleTypeCodeConstants.RCCM_MANAGE_HEART_BEAT_TYPE_CODE, scheduleName = BusinessKey.RCCM_MANAGE_HEART_BEAT_QUARTZ,
        cron = "0/5 * * * * ? *")
public class RccmHealthCheckQuartzTask implements QuartzTask {

    private static final Logger LOGGER = LoggerFactory.getLogger(RccmHealthCheckQuartzTask.class);

    @Autowired
    private RccmManageService rccmManageService;

    @Autowired
    private RestUtil restUtil;

    @Autowired
    private RccmManageAPI rccmManageAPI;

    @Autowired
    private CbbImageTemplateMgmtAPI cbbImageTemplateMgmtAPI;

    @Autowired
    private ImageMgmtAPI imageMgmtAPI;


    @Autowired
    private RcoGlobalParameterAPI rcoGlobalParameterAPI;

    @Autowired
    private StateMachineFactory stateMachineFactory;

    /**
     * 重试3次
     */
    private static final Integer MAX_TRY_COUNT = 3;

    /**
     * 请求次数
     */
    private static Integer COUNT = 0;

    @Override
    public void execute(QuartzTaskContext quartzTaskContext) throws Exception {
        Assert.notNull(quartzTaskContext, "quartzTaskContext can not be null");
        LOGGER.debug("[rcdc与rccm 健康检查任务]：任务开始");
        RccmServerConfigDTO rccmServerConfig = rccmManageService.getRccmServerConfig();
        if (rccmServerConfig != null && Boolean.TRUE.equals(rccmServerConfig.getHasJoin())) {
            RequestParamDTO<Object> requestParamDTO = new RequestParamDTO<>();
            requestParamDTO.setAccount(rccmServerConfig.getAccount());
            if (!StringUtils.isEmpty(rccmServerConfig.getPassword())) {
                String decryptPwd = AesUtil.descrypt(rccmServerConfig.getPassword(), RedLineUtil.getRealAdminRedLine());
                requestParamDTO.setPwd(decryptPwd);
            }
            requestParamDTO.setIp(rccmServerConfig.getServerIp());
            requestParamDTO.setPort(rccmServerConfig.getGatewayPort());
            requestParamDTO.setPath(RccmRestKey.HEART_BEAT);
            COUNT = COUNT + 1;
            try {
                RemoteResponse<JSONObject> remoteResponse = restUtil.onceRequest(requestParamDTO);
                if (CommonMessageCode.SUCCESS == remoteResponse.getContent().getCode()) {
                    // 从不健康变为健康需要推送用户信息
                    if (!rccmServerConfig.getHealth()) {
                        LOGGER.info("rcdc与rccm 更新健康状态：true，并且补偿推送用户信息到RCCM");
                        RccmServerConfigDTO target = new RccmServerConfigDTO();
                        target.setHealth(true);
                        target.setHasNewJoin(Boolean.FALSE);
                        // 连接成功更新信息
                        target.setLastOnlineTime(new Date());
                        if (LOGGER.isDebugEnabled()) {
                            LOGGER.debug("定时任务更新rccm状态为[{}]", JSON.toJSONString(rccmServerConfig));
                        }
                        rccmManageService.updateRccmServerConfig(target);
                        // 补偿推送用户信息
                        rccmManageAPI.pushAllUserToRccm();
                    }
                }
            } catch (BusinessException ex) {
                LOGGER.error("发送心跳失败" + ex.getMessage());
                if (rccmServerConfig.getHealth()) {
                    // 没有超出最大次数，不做处理
                    if (COUNT < MAX_TRY_COUNT) {
                        return;
                    }
                    LOGGER.info("rcdc与rccm 更新健康状态：{}", rccmServerConfig.getHealth());
                    RccmServerConfigDTO target = new RccmServerConfigDTO();
                    target.setHealth(false);
                    target.setHasNewJoin(Boolean.FALSE);
                    rccmManageService.updateRccmServerConfig(target);

                    disconnectProcess(rccmServerConfig);
                    COUNT = 0;
                }
                throw ex;
            }

        } else {
            disconnectProcess(rccmServerConfig);
        }


        LOGGER.debug("[rcdc与rccm 健康检查任务]；任务结束");
    }


    private void disconnectProcess(RccmServerConfigDTO rccmServerConfig) throws BusinessException {


        if (Objects.isNull(rccmServerConfig)) {
            return;
        }

        Date lastOnlineDate = rccmServerConfig.getLastOnlineTime();

        if (lastOnlineDate == null) {
            return;
        }

        Long disconnectDuration = System.currentTimeMillis() - lastOnlineDate.getTime();

        FindParameterResponse response = rcoGlobalParameterAPI.findParameter(new FindParameterRequest(Constants.DISCONNECT_RCENTER_DURATION));
        Long maxDisconnectTime = Long.valueOf(response.getValue());
        if (disconnectDuration < maxDisconnectTime) {
            return;
        }

        List<CbbImageTemplateDTO> imageList = cbbImageTemplateMgmtAPI.listImageTemplateByTemplateStates(
                Collections.singletonList(ImageTemplateState.SYNCING));

        if (CollectionUtils.isEmpty(imageList)) {
            return;
        }

        for (CbbImageTemplateDTO imageTemplateDTO : imageList) {
            UUID imageId = imageTemplateDTO.getId();
            QueryImageSyncTaskRequest request = new QueryImageSyncTaskRequest();
            request.setImageId(imageId);
            request.setPlatformId(imageTemplateDTO.getPlatformId());
            if (imageMgmtAPI.isExistSyncTask(request)) {
                continue;
            }

            ImageTemplateState exceptState = obtainImageState(imageId);
            LOGGER.warn("Rcdc和Rcenter断开连接超过[{}]分钟，且复制任务结束，设置镜像[{}]状态为[{}]",
                    TimeUnit.MINUTES.convert(maxDisconnectTime, TimeUnit.MILLISECONDS),
                    imageId, exceptState);

            StateMachineMgmtAgent[] stateMachineArr = stateMachineFactory.findByResourceId(imageId.toString());
            if (ArrayUtils.isNotEmpty(stateMachineArr)) {
                return;
            }
            cbbImageTemplateMgmtAPI.updatePointedState(imageId, exceptState);
        }

    }

    private ImageTemplateState obtainImageState(UUID imageId) {
        if (rccmManageService.isMaster()) {
            return ImageTemplateState.AVAILABLE;
        }

        List<CbbImageTemplateDetailDTO> versionList = cbbImageTemplateMgmtAPI.listImageTemplateVersionByRootImageId(imageId);

        return CollectionUtils.isEmpty(versionList) ? ImageTemplateState.ERROR : ImageTemplateState.AVAILABLE;
    }


}
