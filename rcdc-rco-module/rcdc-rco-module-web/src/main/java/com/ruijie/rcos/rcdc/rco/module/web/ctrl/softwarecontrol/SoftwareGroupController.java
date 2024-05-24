package com.ruijie.rcos.rcdc.rco.module.web.ctrl.softwarecontrol;

import com.ruijie.rcos.gss.log.module.def.api.BaseAuditLogAPI;
import com.ruijie.rcos.rcdc.rco.module.def.api.SoftwareControlMgmtAPI;
import com.ruijie.rcos.rcdc.rco.module.def.softwarecontrol.dto.SoftwareGroupDTO;
import com.ruijie.rcos.rcdc.rco.module.def.softwarecontrol.enums.SoftwareGroupTypeEnum;
import com.ruijie.rcos.rcdc.rco.module.web.ctrl.aaa.AaaBusinessKey;
import com.ruijie.rcos.rcdc.rco.module.web.ctrl.softwarecontrol.request.CreateSoftwareGroupRequest;
import com.ruijie.rcos.rcdc.rco.module.web.ctrl.softwarecontrol.response.CheckNameDuplicationForSoftwareControlResponse;
import com.ruijie.rcos.rcdc.rco.module.web.response.CommonWebResponse;
import com.ruijie.rcos.sk.base.batch.BatchTaskBuilder;
import com.ruijie.rcos.sk.base.exception.BusinessException;
import com.ruijie.rcos.sk.base.log.Logger;
import com.ruijie.rcos.sk.base.log.LoggerFactory;
import com.ruijie.rcos.sk.pagekit.api.PageQueryWebConfig;
import com.ruijie.rcos.sk.webmvc.api.request.IdArrWebRequest;
import com.ruijie.rcos.sk.webmvc.api.request.IdWebRequest;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.util.UUID;

/**
 * Description: Function Description
 * Copyright: Copyright (c) 2021
 * Company: Ruijie Co., Ltd.
 * Create Time: 2021年6月21日
 *
 * @author lihengjing
 */
@Api(tags = "软件管控软件分组管理")
@Controller
@RequestMapping("/rco/softwareGroup")
@PageQueryWebConfig(url = "/list", dtoType = SoftwareGroupDTO.class)
public class SoftwareGroupController {

    private static final Logger LOGGER = LoggerFactory.getLogger(SoftwareGroupController.class);

    @Autowired
    private SoftwareControlMgmtAPI softwareControlMgmtAPI;

    @Autowired
    private BaseAuditLogAPI auditLogAPI;

    /**
     * 创建软件分组
     *
     * @param createSoftwareGroupRequest 入参
     * @param builder                    批量任务创建对象
     * @return 响应
     * @throws BusinessException 异常抛出
     */
    @ApiOperation("创建软件分组")
    @RequestMapping(value = "create", method = RequestMethod.POST)
    public CommonWebResponse create(CreateSoftwareGroupRequest createSoftwareGroupRequest, BatchTaskBuilder builder) throws BusinessException {
        Assert.notNull(createSoftwareGroupRequest, "createSoftwareGroupRequest must not be null");
        Assert.notNull(builder, "builder must not be null");
        SoftwareGroupDTO softwareGroupDTO = new SoftwareGroupDTO();
        BeanUtils.copyProperties(createSoftwareGroupRequest, softwareGroupDTO);
        softwareControlMgmtAPI.isImportingSoftware();
        softwareControlMgmtAPI.createSoftwareGroup(softwareGroupDTO);
        auditLogAPI.recordLog(SoftwareControlBusinessKey.RCDC_RCO_SOFTWARE_GROUP_CREATE, softwareGroupDTO.getName());
        return CommonWebResponse.success(AaaBusinessKey.RCDC_AAA_OPERATOR_SUCCESS, new String[]{});

    }

    /**
     * 编辑软件分组
     *
     * @param createSoftwareGroupRequest 入参
     * @param builder                    批量任务创建对象
     * @return 响应
     * @throws BusinessException 业务异常
     */
    @ApiOperation("编辑软件分组")
    @RequestMapping(value = "edit", method = RequestMethod.POST)
    public CommonWebResponse edit(CreateSoftwareGroupRequest createSoftwareGroupRequest, BatchTaskBuilder builder) throws BusinessException {
        Assert.notNull(createSoftwareGroupRequest, "createSoftwareGroupRequest must not be null");
        Assert.notNull(builder, "builder must not be null");
        SoftwareGroupDTO softwareGroupDTO = new SoftwareGroupDTO();
        BeanUtils.copyProperties(createSoftwareGroupRequest, softwareGroupDTO);
        softwareControlMgmtAPI.editSoftwareGroup(softwareGroupDTO);
        auditLogAPI.recordLog(SoftwareControlBusinessKey.RCDC_RCO_SOFTWARE_GROUP_EDIT, softwareGroupDTO.getName());
        return CommonWebResponse.success(AaaBusinessKey.RCDC_AAA_OPERATOR_SUCCESS, new String[]{});

    }


    /**
     * 删除软件分组
     *
     * @param idArrWebRequest 入参
     * @param builder         批量任务创建对象
     * @return 响应
     * @throws BusinessException 业务异常
     */
    @ApiOperation("删除软件分组")
    @RequestMapping(value = "delete", method = RequestMethod.POST)
    public CommonWebResponse delete(IdArrWebRequest idArrWebRequest, BatchTaskBuilder builder) throws BusinessException {
        Assert.notNull(idArrWebRequest, "idArrWebRequest must not be null");
        Assert.notNull(builder, "builder can not be null");

        UUID[] idArr = idArrWebRequest.getIdArr();
        Assert.notEmpty(idArr, "idArr can not be null");
        // 批量删除任务
        for (UUID uuid : idArr) {
            softwareControlMgmtAPI.deleteSoftwareGroup(uuid);
        }
        return CommonWebResponse.success(AaaBusinessKey.RCDC_AAA_OPERATOR_SUCCESS, new String[]{});
    }

    /**
     * 获取软件分组详情
     *
     * @param idWebRequest 入参
     * @return 响应
     * @throws BusinessException 业务异常
     */
    @ApiOperation("获取软件分组详情")
    @RequestMapping(value = {"detail", "getInfo"}, method = RequestMethod.POST)
    public CommonWebResponse<SoftwareGroupDTO> detail(IdWebRequest idWebRequest) throws BusinessException {
        Assert.notNull(idWebRequest, "idWebRequest must not be null");
        SoftwareGroupDTO softwareGroupDTO = softwareControlMgmtAPI.findSoftwareGroupById(idWebRequest.getId());
        return CommonWebResponse.success(softwareGroupDTO);
    }

    /**
     * 检查软件分组名称是否重复
     *
     * @param createSoftwareGroupRequest 入参
     * @return 响应
     */
    @ApiOperation("检查软件分组名称是否重复")
    @RequestMapping(value = "checkNameDuplication", method = RequestMethod.POST)
    public CommonWebResponse<CheckNameDuplicationForSoftwareControlResponse> checkNameDuplication(
            CreateSoftwareGroupRequest createSoftwareGroupRequest) {
        Assert.notNull(createSoftwareGroupRequest, "createSoftwareGroupRequest must not be null");
        UUID id = createSoftwareGroupRequest.getId();
        String name = createSoftwareGroupRequest.getName();
        SoftwareGroupTypeEnum groupType = createSoftwareGroupRequest.getGroupType();
        Boolean hasDuplication = softwareControlMgmtAPI.checkSoftwareGroupNameDuplication(id, name, groupType);
        return CommonWebResponse.success(new CheckNameDuplicationForSoftwareControlResponse(hasDuplication));
    }
}

