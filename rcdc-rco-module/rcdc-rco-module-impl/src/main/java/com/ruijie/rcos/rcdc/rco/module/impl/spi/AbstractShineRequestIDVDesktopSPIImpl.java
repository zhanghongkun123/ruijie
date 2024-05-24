package com.ruijie.rcos.rcdc.rco.module.impl.spi;

import com.alibaba.fastjson.JSON;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.api.CbbIDVDeskMgmtAPI;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.api.CbbImageTemplateMgmtAPI;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.dto.CbbDeskDTO;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.ex.CbbDesktopNotExistsException;
import com.ruijie.rcos.rcdc.codec.adapter.def.dto.CbbDispatcherRequest;
import com.ruijie.rcos.rcdc.codec.adapter.def.spi.CbbDispatcherHandlerSPI;
import com.ruijie.rcos.rcdc.rco.module.impl.spi.common.ShineRequestIDVDeskMessageCode;
import com.ruijie.rcos.rcdc.rco.module.impl.spi.dto.ShineRequestDesktopBaseDTO;
import com.ruijie.rcos.rcdc.rco.module.impl.spi.exception.ShineRequestIDVDesktopException;
import com.ruijie.rcos.rcdc.terminal.module.def.api.CbbTerminalOperatorAPI;
import com.ruijie.rcos.sk.base.exception.BusinessException;
import com.ruijie.rcos.sk.base.log.Logger;
import com.ruijie.rcos.sk.base.log.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.Assert;

import java.util.UUID;

/**
 * Description: Function Description
 * Copyright: Copyright (c) 2018
 * Company: Ruijie Co., Ltd.
 * Create Time: 2020/3/27
 * 
 * @param <T> shine请求参数公共信息类型
 *
 * @author chen zj
 */
abstract class AbstractShineRequestIDVDesktopSPIImpl<T> implements CbbDispatcherHandlerSPI {

    private static final Logger LOGGER = LoggerFactory.getLogger(AbstractShineRequestIDVDesktopSPIImpl.class);

    @Autowired
    protected CbbTerminalOperatorAPI cbbTerminalOperatorAPI;

    @Autowired
    protected CbbImageTemplateMgmtAPI cbbImageTemplateMgmtAPI;

    @Autowired
    private CbbIDVDeskMgmtAPI cbbIDVDeskMgmtAPI;

    @Override
    public void dispatch(CbbDispatcherRequest cbbDispatcherRequest) {
        Assert.notNull(cbbDispatcherRequest, "Param [cbbDispatcherRequest] must not be null");
        Assert.notNull(cbbDispatcherRequest.getData(), "Param [request.data] must not be null");

        T targetDesktopDTO = convertJsonDataToDesktopBaseDTO(cbbDispatcherRequest);
        Assert.notNull(targetDesktopDTO, "Param [targetDesktopDTO] must not be null");

        doDispatch(cbbDispatcherRequest, targetDesktopDTO);
    }

    /**
     * 处理Shine消息
     *
     *
     * @param cbbDispatcherRequest CbbDispatcherRequest
     * @param targetDesktopDTO T
     */
    protected abstract void doDispatch(CbbDispatcherRequest cbbDispatcherRequest, T targetDesktopDTO);

    /**
     * 获取转换的目的对象
     *
     * @return T
     */
    protected abstract Class<T> jsonToDesktopTargetDTO();

    /**
     * json转DTO
     *
     * @param cbbDispatcherRequest CbbDispatcherRequest
     * @return T
     */
    protected T convertJsonDataToDesktopBaseDTO(CbbDispatcherRequest cbbDispatcherRequest) {
        Class<T> jsonToTargetDesktopDTO = jsonToDesktopTargetDTO();
        try {
            return JSON.parseObject(cbbDispatcherRequest.getData(), jsonToTargetDesktopDTO);
        } catch (Exception e) {
            LOGGER.error("Failed to convertJsonData:[{}] to " + jsonToTargetDesktopDTO.getSimpleName() + ", error:", e);
            throw new IllegalArgumentException("Shine上报的数据格式与RCDC定义的不一致，转换出现异常", e);
        }
    }

    /**
     * 验证云桌面是否存在
     * 
     * @param deskId 云桌面ID
     * @return CbbDeskDTO
     * @throws BusinessException 业务异常
     */
    protected CbbDeskDTO validateDesktopExists(UUID deskId) throws BusinessException {
        Assert.notNull(deskId, "Param [deskId] must not be null");

        try {
            return cbbIDVDeskMgmtAPI.getDeskIDV(deskId);
        } catch (BusinessException e) {
            if (e instanceof CbbDesktopNotExistsException) {
                LOGGER.error("系统中不存在ID={" + deskId + "}的云桌面, 异常信息:", e);
                throw new ShineRequestIDVDesktopException(ShineRequestIDVDeskMessageCode.DESK_NOT_EXISTS, e);
            }

            LOGGER.error("查询ID={" + deskId + "}的云桌面出现异常:", e);
            throw e;
        }
    }

    /**
     * 验证桌面关联的镜像是否存在
     *
     * @param cbbDeskDTO 桌面信息
     * @param shineRequestDesktopBaseDTO ShineRequestDesktopBaseDTO
     * @throws ShineRequestIDVDesktopException 不存在异常
     */
    protected void validateDesktopImageTemplateExist(CbbDeskDTO cbbDeskDTO, ShineRequestDesktopBaseDTO shineRequestDesktopBaseDTO)
            throws ShineRequestIDVDesktopException {
        // 验证关联镜像ID是否存在
        if (cbbDeskDTO.getImageTemplateId() == null) {
            LOGGER.error("云桌面[id:{}]未关联镜像, 响应Shine错误码:[{}]", shineRequestDesktopBaseDTO.getId(),
                    ShineRequestIDVDeskMessageCode.DESK_RELEASE_IMAGE_NOT_FOUND);
            throw new ShineRequestIDVDesktopException(ShineRequestIDVDeskMessageCode.DESK_RELEASE_IMAGE_NOT_FOUND);
        }

        try {
            cbbImageTemplateMgmtAPI.getImageTemplateInfo(cbbDeskDTO.getImageTemplateId());
        } catch (Exception e) {
            LOGGER.error("云桌面[id={}]关联的镜像[id={}]不存在, 响应Shine错误码:[{}], 异常信息:{}", cbbDeskDTO.getDeskId(), cbbDeskDTO.getImageTemplateId(),
                    ShineRequestIDVDeskMessageCode.DESK_RELEASE_IMAGE_NOT_FOUND, e);
            throw new ShineRequestIDVDesktopException(ShineRequestIDVDeskMessageCode.DESK_RELEASE_IMAGE_NOT_FOUND, e);
        }
    }

    /**
     * 验证桌面关联的终端是否存在
     *
     * @param terminalId 终端id
     * @param deskId 桌面id
     * @throws ShineRequestIDVDesktopException 不存在异常
     */
    protected void validateDesktopTerminalExist(String terminalId, UUID deskId) throws ShineRequestIDVDesktopException {
        Assert.notNull(terminalId, "Param [terminalId] must not be null");

        try {
            cbbTerminalOperatorAPI.findBasicInfoByTerminalId(terminalId);
        } catch (BusinessException e) {
            LOGGER.error("云桌面[id={}]关联的终端[[terminalId={}]]不存在, 返回Shine错误码:[{}],异常信息:{}", deskId, terminalId,
                    ShineRequestIDVDeskMessageCode.DESK_RELEASE_TERMINAL_NOT_FOUND, e);
            throw new ShineRequestIDVDesktopException(ShineRequestIDVDeskMessageCode.DESK_RELEASE_TERMINAL_NOT_FOUND, e);
        }
    }
}
