package com.ruijie.rcos.rcdc.rco.module.web.ctrl.deskstrategy.validation;

import com.ruijie.rcos.rcdc.clouddesktop.module.def.enums.CbbStrategyType;
import com.ruijie.rcos.rcdc.rco.module.def.api.DeskStrategyAPI;
import com.ruijie.rcos.rcdc.rco.module.def.deskstrategy.dto.DeskStrategyCheckDTO;
import com.ruijie.rcos.rcdc.rco.module.web.Constants;
import com.ruijie.rcos.rcdc.rco.module.web.ctrl.deskstrategy.request.CreateIDVDeskStrategyWebRequest;
import com.ruijie.rcos.rcdc.rco.module.web.ctrl.deskstrategy.request.CreateThirdPartyDeskStrategyWebRequest;
import com.ruijie.rcos.rcdc.rco.module.web.ctrl.deskstrategy.request.CreateVDIDeskStrategyWebRequest;
import com.ruijie.rcos.rcdc.rco.module.web.ctrl.deskstrategy.request.CreateVOIDeskStrategyWebRequest;
import com.ruijie.rcos.rcdc.rco.module.web.ctrl.deskstrategy.request.UpdateIDVDeskStrategyWebRequest;
import com.ruijie.rcos.rcdc.rco.module.web.ctrl.deskstrategy.request.UpdateThirdPartyDeskStrategyWebRequest;
import com.ruijie.rcos.rcdc.rco.module.web.ctrl.deskstrategy.request.UpdateVDIDeskStrategyWebRequest;
import com.ruijie.rcos.rcdc.rco.module.web.ctrl.deskstrategy.request.UpdateVOIDeskStrategyWebRequest;
import com.ruijie.rcos.rcdc.rco.module.web.ctrl.deskstrategy.utils.DesktopStrategyUtils;
import com.ruijie.rcos.sk.base.exception.BusinessException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import java.util.regex.Pattern;

/**
 * Description: Function Description
 * Copyright: Copyright (c) 2020
 * Company: Ruijie Co., Ltd.
 * Create Time: 2020年10月10日
 *
 * @author wjp
 */
@Service
public class DeskStrategyValidation {

    private static final Logger LOGGER = LoggerFactory.getLogger(DeskStrategyValidation.class);

    /** 计算机名称只能包含[a-z]、[A-Z]、[0-9]或-，必须以字母开头，长度不超过8个字符 */
    private static final Pattern COMPUTER_NAME_PATTERN = Pattern.compile("^[0-9a-zA-Z]([0-9a-zA-Z-]{0,7})$");

    private static final double ZONE = 0.0;

    @Autowired
    private DeskStrategyAPI deskStrategyAPI;

    /**
     * 创建VDI云桌面策略参数校验
     *
     * @param request 请求参数对象
     * @throws BusinessException 异常
     */
    public void createVDIDeskStrategyValidate(CreateVDIDeskStrategyWebRequest request) throws BusinessException {
        Assert.notNull(request, "request is not null");

        DeskStrategyCheckDTO strategyCheckDTO = new DeskStrategyCheckDTO();
        BeanUtils.copyProperties(request, strategyCheckDTO);
        strategyCheckDTO.setStrategyType(CbbStrategyType.VDI);
        // 安全审计
        DesktopStrategyUtils.dealAuditInfo(request.getAuditFileInfo(), request.getAuditPrinterInfo(), strategyCheckDTO);
        deskStrategyAPI.createDeskStrategyValidate(strategyCheckDTO);
        request.setClipBoardMode(strategyCheckDTO.getClipBoardMode());
    }

    /**
     * 创建IDV云桌面策略参数校验
     *
     * @param request 请求参数对象
     * @throws BusinessException 异常
     */
    public void createIDVDeskStrategyValidate(CreateIDVDeskStrategyWebRequest request) throws BusinessException {
        Assert.notNull(request, "request is not null");

        DeskStrategyCheckDTO strategyCheckDTO = new DeskStrategyCheckDTO();
        BeanUtils.copyProperties(request, strategyCheckDTO);
        strategyCheckDTO.setStrategyType(CbbStrategyType.IDV);

        deskStrategyAPI.createDeskStrategyValidate(strategyCheckDTO);
    }

    /**
     * 创建VOI云桌面策略参数校验
     *
     * @param request 请求参数对象
     * @throws BusinessException 异常
     */
    public void createVOIDeskStrategyValidate(CreateVOIDeskStrategyWebRequest request) throws BusinessException {
        Assert.notNull(request, "request is not null");

        DeskStrategyCheckDTO strategyCheckDTO = new DeskStrategyCheckDTO();
        BeanUtils.copyProperties(request, strategyCheckDTO);
        strategyCheckDTO.setStrategyType(CbbStrategyType.VOI);

        deskStrategyAPI.createDeskStrategyValidate(strategyCheckDTO);
    }

    /**
     * 创建第三方云桌面策略参数校验
     * @param request 请求参数对象
     * @throws BusinessException 异常
     */
    public void createThirdPartyDeskStrategyValidate(CreateThirdPartyDeskStrategyWebRequest request) throws BusinessException  {
        Assert.notNull(request, "request is not null");

        DeskStrategyCheckDTO strategyCheckDTO = new DeskStrategyCheckDTO();
        BeanUtils.copyProperties(request, strategyCheckDTO);
        strategyCheckDTO.setStrategyType(CbbStrategyType.THIRD);
        strategyCheckDTO.setComputerName(Constants.DESK_TYPE_PC);
        // 水印配置
        DesktopStrategyUtils.dealWatermarkInfo(request.getWatermarkInfo(), strategyCheckDTO::setEnableWatermark,
                strategyCheckDTO::setWatermarkInfo);
        deskStrategyAPI.createDeskStrategyValidate(strategyCheckDTO);
        request.setClipBoardMode(strategyCheckDTO.getClipBoardMode());
    }

    /**
     * 变更VDI云桌面策略信息参数校验
     *
     * @param request 请求参数对象
     * @throws BusinessException 异常
     */
    public void updateVDIDeskStrategyValidate(UpdateVDIDeskStrategyWebRequest request) throws BusinessException {
        Assert.notNull(request, "request is not null");

        DeskStrategyCheckDTO strategyCheckDTO = new DeskStrategyCheckDTO();
        BeanUtils.copyProperties(request, strategyCheckDTO);
        strategyCheckDTO.setStrategyType(CbbStrategyType.VDI);
        // 适配前端存在VGPU的时候，VgpuType不送的情况
        // 安全审计
        DesktopStrategyUtils.dealAuditInfo(request.getAuditFileInfo(), request.getAuditPrinterInfo(), strategyCheckDTO);
        deskStrategyAPI.updateDeskStrategyValidate(strategyCheckDTO);
        request.setClipBoardMode(strategyCheckDTO.getClipBoardMode());
    }

    /**
     * 变更IDV云桌面策略信息参数校验
     *
     * @param request 请求参数对象
     * @throws BusinessException 异常
     */
    public void updateIDVDeskStrategyValidate(UpdateIDVDeskStrategyWebRequest request) throws BusinessException {
        Assert.notNull(request, "request is not null");

        DeskStrategyCheckDTO strategyCheckDTO = new DeskStrategyCheckDTO();
        BeanUtils.copyProperties(request, strategyCheckDTO);
        strategyCheckDTO.setStrategyType(CbbStrategyType.IDV);

        deskStrategyAPI.updateDeskStrategyValidate(strategyCheckDTO);
    }

    /**
     * 变更VOI云桌面策略信息参数校验
     *
     * @param request 请求参数对象
     * @throws BusinessException 异常
     */
    public void updateVOIDeskStrategyValidate(UpdateVOIDeskStrategyWebRequest request) throws BusinessException {
        Assert.notNull(request, "request is not null");

        DeskStrategyCheckDTO strategyCheckDTO = new DeskStrategyCheckDTO();
        BeanUtils.copyProperties(request, strategyCheckDTO);
        strategyCheckDTO.setStrategyType(CbbStrategyType.VOI);

        deskStrategyAPI.updateDeskStrategyValidate(strategyCheckDTO);
    }

    /**
     * 变更第三方云桌面策略信息参数校验
     *
     * @param request 请求参数对象
     * @throws BusinessException 异常
     */
    public void updateThirdPartyDeskStrategyValidate(UpdateThirdPartyDeskStrategyWebRequest request) throws BusinessException {
        Assert.notNull(request, "request is not null");

        DeskStrategyCheckDTO strategyCheckDTO = new DeskStrategyCheckDTO();
        BeanUtils.copyProperties(request, strategyCheckDTO);
        strategyCheckDTO.setStrategyType(CbbStrategyType.THIRD);
        // 水印配置
        DesktopStrategyUtils.dealWatermarkInfo(request.getWatermarkInfo(), strategyCheckDTO::setEnableWatermark, strategyCheckDTO::setWatermarkInfo);

        deskStrategyAPI.updateDeskStrategyValidate(strategyCheckDTO);
        request.setClipBoardMode(strategyCheckDTO.getClipBoardMode());
    }

}
