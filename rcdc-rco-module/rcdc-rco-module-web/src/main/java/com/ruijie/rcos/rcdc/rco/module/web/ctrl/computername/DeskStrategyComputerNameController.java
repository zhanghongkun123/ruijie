package com.ruijie.rcos.rcdc.rco.module.web.ctrl.computername;

import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.ruijie.rcos.gss.base.iac.module.annotation.EnableAuthority;
import com.ruijie.rcos.gss.log.module.def.api.BaseAuditLogAPI;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.api.CbbIDVDeskMgmtAPI;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.api.CbbVDIDeskMgmtAPI;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.dto.CbbDeskInfoDTO;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.enums.CbbCloudDeskType;
import com.ruijie.rcos.rcdc.rco.module.def.api.DeskStrategyTciNotifyAPI;
import com.ruijie.rcos.rcdc.rco.module.def.api.UserDesktopMgmtAPI;
import com.ruijie.rcos.rcdc.rco.module.def.api.dto.CloudDesktopDetailDTO;
import com.ruijie.rcos.rcdc.rco.module.web.config.annotation.ApiVersion;
import com.ruijie.rcos.rcdc.rco.module.web.config.annotation.ApiVersions;
import com.ruijie.rcos.rcdc.rco.module.web.config.annotation.Version;
import com.ruijie.rcos.rcdc.rco.module.web.ctrl.clouddesktop.CloudDesktopBusinessKey;
import com.ruijie.rcos.rcdc.rco.module.web.ctrl.computername.request.CheckComputerNameWebRequest;
import com.ruijie.rcos.rcdc.rco.module.web.ctrl.computername.request.EditComputerNameWebRequest;
import com.ruijie.rcos.rcdc.rco.module.web.ctrl.computername.request.GetComputerNameWebRequest;
import com.ruijie.rcos.rcdc.rco.module.web.ctrl.computername.response.GetComputerNameWebResponse;
import com.ruijie.rcos.rcdc.rco.module.web.ctrl.computername.validation.DeskStrategyComputerNameValidation;
import com.ruijie.rcos.rcdc.rco.module.web.ctrl.user.dto.CheckDuplicationDTO;
import com.ruijie.rcos.rcdc.rco.module.web.response.CommonWebResponse;
import com.ruijie.rcos.sk.base.exception.BusinessException;
import com.ruijie.rcos.sk.base.log.Logger;
import com.ruijie.rcos.sk.base.log.LoggerFactory;
import com.ruijie.rcos.sk.base.validation.EnableCustomValidate;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

/**
 *
 * Description: Function Description
 * Copyright: Copyright (c) 2020
 * Company: Ruijie Co., Ltd.
 * Create Time: 2020年10月10日
 *
 * @author wjp
 */
@Api(tags = "云桌面自定义计算机名称修订页面行为")
@Controller
@RequestMapping("/rco/deskStrategy/computerName")
@EnableCustomValidate(validateClass = DeskStrategyComputerNameValidation.class)
public class DeskStrategyComputerNameController {

    private static final Logger LOGGER = LoggerFactory.getLogger(DeskStrategyComputerNameController.class);

    @Autowired
    private CbbVDIDeskMgmtAPI cbbVDIDeskMgmtAPI;

    @Autowired
    private CbbIDVDeskMgmtAPI cbbIDVDeskMgmtAPI;

    @Autowired
    private UserDesktopMgmtAPI userDesktopMgmtAPI;

    @Autowired
    private BaseAuditLogAPI auditLogAPI;

    @Autowired
    private DeskStrategyTciNotifyAPI deskStrategyTciNotifyAPI;

    /**
     * 获取云桌面计算机名称
     *
     * @param webRequest 请求参数
     * @return 返回值
     * @throws BusinessException 业务异常
     */
    @ApiOperation("获取云桌面计算机名称")
    @RequestMapping(value = "detail", method = RequestMethod.POST)
    public CommonWebResponse<GetComputerNameWebResponse> getComputerName(GetComputerNameWebRequest webRequest) throws BusinessException {
        Assert.notNull(webRequest, "webRequest can not be null");

        String computerName = getComputerName(webRequest.getId());
        GetComputerNameWebResponse getComputerNameWebResponse = new GetComputerNameWebResponse(computerName);
        return CommonWebResponse.success(getComputerNameWebResponse);
    }

    private String getComputerName(UUID deskId) throws BusinessException {
        String type = obtainDesktopType(deskId);
        if (CbbCloudDeskType.VDI.name().equals(type)) {
            return cbbVDIDeskMgmtAPI.getDeskVDI(deskId).getComputerName();
        }
        if (CbbCloudDeskType.IDV.name().equals(type)) {
            return cbbIDVDeskMgmtAPI.getDeskIDV(deskId).getComputerName();
        }
        throw new BusinessException(CloudDesktopBusinessKey.RCDC_CLOUDDESKTOP_STRATEGY_TYPE_NOT_EXIST, type);
    }

    /**
     * 检测云桌面计算机名称是否冲突
     *
     * @param webRequest 请求参数
     * @return 返回值
     * @throws BusinessException 业务异常
     */
    @ApiOperation("检测云桌面计算机名称是否冲突")
    @RequestMapping(value = "/check", method = RequestMethod.POST)
    @EnableCustomValidate(validateMethod = "checkComputerNameValidate")
    public CommonWebResponse checkComputerName(CheckComputerNameWebRequest webRequest) throws BusinessException {
        Assert.notNull(webRequest, "webRequest can not be null");

        boolean hasDuplication = checkComputerName(webRequest.getId(), webRequest.getComputerName());
        return CommonWebResponse.success(new CheckDuplicationDTO(hasDuplication));
    }

    private boolean checkComputerName(UUID deskId, String computerName) throws BusinessException {
        String type = obtainDesktopType(deskId);
        if (CbbCloudDeskType.VDI.name().equals(type)) {
            CbbDeskInfoDTO cbbDeskInfoDTO = cbbVDIDeskMgmtAPI.findByComputerName(computerName);
            return (cbbDeskInfoDTO != null && !cbbDeskInfoDTO.getDeskId().equals(deskId));
        }
        if (CbbCloudDeskType.IDV.name().equals(type)) {
            CbbDeskInfoDTO cbbDeskInfoDTO = cbbIDVDeskMgmtAPI.findByComputerName(computerName);
            return (cbbDeskInfoDTO != null && !cbbDeskInfoDTO.getDeskId().equals(deskId));
        }
        throw new BusinessException(CloudDesktopBusinessKey.RCDC_CLOUDDESKTOP_STRATEGY_TYPE_NOT_EXIST, type);
    }

    /**
     * 编辑云桌面计算机名称
     *
     * @param webRequest 请求参数
     * @return 返回值
     * @throws BusinessException 业务异常
     */
    @ApiOperation("编辑云桌面计算机名称")
    @ApiVersions({@ApiVersion(value = Version.V1_0_0), @ApiVersion(value = Version.V3_1_1, descriptions = {"分级分权-区分权限，加入权限控制"})})
    @RequestMapping(value = "edit", method = RequestMethod.POST)
    @EnableCustomValidate(validateMethod = "editComputerNameValidate")
    @EnableAuthority
    public CommonWebResponse editComputerName(EditComputerNameWebRequest webRequest) throws BusinessException {
        Assert.notNull(webRequest, "webRequest can not be null");

        String desktopName = obtainDesktopName(webRequest.getId());
        String desktopType = userDesktopMgmtAPI.getImageUsageByDeskId(webRequest.getId());
        try {
            editComputerName(webRequest.getId(), webRequest.getComputerName());
            auditLogAPI.recordLog(ComputerNameBusinessKey.RCO_EDIT_COMPUTER_NAME_SUCCESS_LOG, desktopName, desktopType);
            return CommonWebResponse.success(ComputerNameBusinessKey.RCO_COMPUTER_NAME_MODULE_OPERATE_SUCCESS, new String[] {});
        } catch (BusinessException e) {
            LOGGER.error("编辑云桌面计算机名称失败", e);
            auditLogAPI.recordLog(ComputerNameBusinessKey.RCO_EDIT_COMPUTER_NAME_FAIL_LOG, desktopName, e.getI18nMessage(), desktopType);
            return CommonWebResponse.fail(ComputerNameBusinessKey.RCO_COMPUTER_NAME_MODULE_OPERATE_FAIL, new String[] {e.getI18nMessage()});
        }
    }

    private void editComputerName(UUID deskId, String computerName) throws BusinessException {
        String type = obtainDesktopType(deskId);
        if (CbbCloudDeskType.VDI.name().equals(type)) {
            cbbVDIDeskMgmtAPI.updateVDIDeskComputerName(deskId, computerName);
            return;
        }
        if (CbbCloudDeskType.IDV.name().equals(type)) {
            cbbIDVDeskMgmtAPI.updateIDVDeskComputerName(deskId, computerName);
            //编辑计算机后，通知TCI公用在线终端
            deskStrategyTciNotifyAPI.notifyDeskEditComputerName(deskId,computerName);
            return;
        }
        throw new BusinessException(CloudDesktopBusinessKey.RCDC_CLOUDDESKTOP_STRATEGY_TYPE_NOT_EXIST, type);
    }

    private String obtainDesktopName(UUID deskId) {
        String desktopName = deskId.toString();
        try {
            CloudDesktopDetailDTO cloudDesktopDetailDTO = userDesktopMgmtAPI.getDesktopDetailById(deskId);
            desktopName = cloudDesktopDetailDTO.getDesktopName();
        } catch (BusinessException e) {
            LOGGER.error("获取云桌面名称失败", e);
        }
        return desktopName;
    }

    private String obtainDesktopType(UUID deskId) throws BusinessException {
        String desktopCategory = deskId.toString();
        try {
            CloudDesktopDetailDTO cloudDesktopDetailDTO = userDesktopMgmtAPI.getDesktopDetailById(deskId);
            desktopCategory = cloudDesktopDetailDTO.getDesktopCategory();
        } catch (BusinessException e) {
            LOGGER.error("获取云桌面类型失败", e);
            throw e;
        }
        return desktopCategory;
    }
}
