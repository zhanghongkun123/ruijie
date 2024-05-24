package com.ruijie.rcos.rcdc.rco.module.web.ctrl.softwarecontrol;

import com.google.common.collect.ImmutableMap;
import com.ruijie.rcos.gss.log.module.def.api.BaseAuditLogAPI;
import com.ruijie.rcos.rcdc.rco.module.def.api.SoftwareControlMgmtAPI;
import com.ruijie.rcos.rcdc.rco.module.def.softwarecontrol.dto.SoftwareDTO;
import com.ruijie.rcos.rcdc.rco.module.def.softwarecontrol.dto.SoftwareGroupDTO;
import com.ruijie.rcos.rcdc.rco.module.def.softwarecontrol.dto.SoftwareStrategyDTO;
import com.ruijie.rcos.rcdc.rco.module.web.ctrl.aaa.AaaBusinessKey;
import com.ruijie.rcos.rcdc.rco.module.web.ctrl.softwarecontrol.request.CheckSoftwareStrategyNameDuplicationRequest;
import com.ruijie.rcos.rcdc.rco.module.web.ctrl.softwarecontrol.request.CreateSoftwareStrategyRequest;
import com.ruijie.rcos.rcdc.rco.module.web.ctrl.softwarecontrol.response.CheckNameDuplicationForSoftwareControlResponse;
import com.ruijie.rcos.rcdc.rco.module.web.response.CommonWebResponse;
import com.ruijie.rcos.rcdc.rco.module.def.service.tree.TreeBuilder;
import com.ruijie.rcos.rcdc.rco.module.def.service.tree.TreeNodeVO;
import com.ruijie.rcos.sk.base.batch.BatchTaskBuilder;
import com.ruijie.rcos.sk.base.exception.BusinessException;
import com.ruijie.rcos.sk.base.log.Logger;
import com.ruijie.rcos.sk.base.log.LoggerFactory;
import com.ruijie.rcos.sk.pagekit.api.PageQueryWebConfig;
import com.ruijie.rcos.sk.webmvc.api.request.IdArrWebRequest;
import com.ruijie.rcos.sk.webmvc.api.request.IdWebRequest;
import com.ruijie.rcos.sk.webmvc.api.response.DefaultWebResponse;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.util.*;

/**
 * Description: Function Description
 * Copyright: Copyright (c) 2021
 * Company: Ruijie Co., Ltd.
 * Create Time: 2021年6月21日
 *
 * @author lihengjing
 */
@Api(tags = "软件管控软件策略管理")
@Controller
@RequestMapping("/rco/softwareStrategy")
@PageQueryWebConfig(url = "/list", dtoType = SoftwareStrategyDTO.class)
public class SoftwareStrategyController {

    private static final Logger LOGGER = LoggerFactory.getLogger(SoftwareStrategyController.class);

    private static final String TREE_FIELD = "itemArr";

    @Autowired
    private SoftwareControlMgmtAPI softwareControlMgmtAPI;

    @Autowired
    private BaseAuditLogAPI auditLogAPI;

    /**
     * 创建软件策略
     *
     * @param createSoftwareStrategyRequest 入参
     * @param builder                       批量任务创建对象
     * @return 响应
     * @throws BusinessException 业务异常
     */
    @ApiOperation("创建软件策略")
    @RequestMapping(value = "create", method = RequestMethod.POST)
    public CommonWebResponse create(CreateSoftwareStrategyRequest createSoftwareStrategyRequest, BatchTaskBuilder builder) throws BusinessException {
        Assert.notNull(createSoftwareStrategyRequest, "createSoftwareStrategyRequest must not be null");
        Assert.notNull(builder, "builder must not be null");
        SoftwareStrategyDTO softwareStrategyDTO = new SoftwareStrategyDTO();
        BeanUtils.copyProperties(createSoftwareStrategyRequest, softwareStrategyDTO);
        softwareControlMgmtAPI.createSoftwareStrategy(softwareStrategyDTO);

        auditLogAPI.recordLog(SoftwareControlBusinessKey.RCDC_RCO_SOFTWARE_STRATEGY_CREATE,
                new String[]{softwareStrategyDTO.getName()});
        return CommonWebResponse.success(AaaBusinessKey.RCDC_AAA_OPERATOR_SUCCESS, new String[]{});

    }

    /**
     * 编辑软件策略
     *
     * @param createSoftwareStrategyRequest 入参
     * @param builder                       批量任务创建对象
     * @return 响应
     * @throws BusinessException 业务异常
     */
    @ApiOperation("编辑软件策略")
    @RequestMapping(value = "edit", method = RequestMethod.POST)
    public CommonWebResponse edit(CreateSoftwareStrategyRequest createSoftwareStrategyRequest, BatchTaskBuilder builder) throws BusinessException {
        Assert.notNull(createSoftwareStrategyRequest, "createSoftwareStrategyRequest must not be null");
        Assert.notNull(builder, "builder must not be null");
        SoftwareStrategyDTO softwareStrategyDTO = new SoftwareStrategyDTO();
        BeanUtils.copyProperties(createSoftwareStrategyRequest, softwareStrategyDTO);
        softwareControlMgmtAPI.editSoftwareStrategy(softwareStrategyDTO);

        auditLogAPI.recordLog(SoftwareControlBusinessKey.RCDC_RCO_SOFTWARE_STRATEGY_EDIT, softwareStrategyDTO.getName());
        return CommonWebResponse.success(AaaBusinessKey.RCDC_AAA_OPERATOR_SUCCESS, new String[]{});

    }


    /**
     * 删除软件策略
     *
     * @param idArrWebRequest 入参
     * @param builder         批量任务创建对象
     * @return 响应
     * @throws BusinessException 业务异常
     */
    @ApiOperation("删除软件策略")
    @RequestMapping(value = "delete", method = RequestMethod.POST)
    public CommonWebResponse delete(IdArrWebRequest idArrWebRequest, BatchTaskBuilder builder) throws BusinessException {
        Assert.notNull(idArrWebRequest, "idArrWebRequest must not be null");
        Assert.notNull(builder, "builder can not be null");

        UUID[] idArr = idArrWebRequest.getIdArr();
        Assert.notEmpty(idArr, "idArr can not be null");
        // 批量删除任务
        for (UUID uuid : idArr) {
            softwareControlMgmtAPI.deleteSoftwareStrategy(uuid);
        }
        return CommonWebResponse.success(AaaBusinessKey.RCDC_AAA_OPERATOR_SUCCESS, new String[]{});
    }

    /**
     * 获取软件策略详情
     *
     * @param idWebRequest 入参
     * @return 响应
     * @throws BusinessException 业务异常
     */
    @ApiOperation("获取软件策略详情")
    @RequestMapping(value = {"detail", "getInfo"}, method = RequestMethod.POST)
    public CommonWebResponse<SoftwareStrategyDTO> detail(IdWebRequest idWebRequest) throws BusinessException {
        Assert.notNull(idWebRequest, "idWebRequest must not be null");
        SoftwareStrategyDTO softwareStrategyDTO = softwareControlMgmtAPI.findSoftwareStrategyWrapperById(idWebRequest.getId());
        return CommonWebResponse.success(softwareStrategyDTO);
    }


    /**
     * 检查软件策略名称是否重复
     *
     * @param checkNameDuplicationRequest 入参
     * @return 响应
     */
    @ApiOperation("检查软件策略名称是否重复")
    @RequestMapping(value = "checkNameDuplication", method = RequestMethod.POST)
    public CommonWebResponse<CheckNameDuplicationForSoftwareControlResponse> checkNameDuplication(
            CheckSoftwareStrategyNameDuplicationRequest checkNameDuplicationRequest) {
        Assert.notNull(checkNameDuplicationRequest, "checkNameDuplicationRequest must not be null");
        CheckNameDuplicationForSoftwareControlResponse checkNameDuplicationForSoftwareControlResponse =
                new CheckNameDuplicationForSoftwareControlResponse(false);
        UUID id = checkNameDuplicationRequest.getId();
        String name = checkNameDuplicationRequest.getName();
        Boolean hasDuplication = softwareControlMgmtAPI.checkSoftwareStrategyNameDuplication(id, name);
        checkNameDuplicationForSoftwareControlResponse.setHasDuplication(hasDuplication);
        return CommonWebResponse.success(checkNameDuplicationForSoftwareControlResponse);
    }

    /**
     * 获取软件分组,封装成树形结构
     * 备注: 分组ID拼接"|GROUP",软件ID不变
     *
     * @return 响应
     * @throws BusinessException 业务异常
     */
    @ApiOperation("获取软件分组,封装成树形结构")
    @RequestMapping(value = "tree/list", method = RequestMethod.POST)
    public DefaultWebResponse getTreeList() throws BusinessException {
        List<SoftwareDTO> softwareList = softwareControlMgmtAPI.findAllSoftwareForWeb();
        List<SoftwareGroupDTO> softwareGroupList = softwareControlMgmtAPI.findAllSoftwareGroup();
        List<TreeNodeVO> treeList = new ArrayList<>();
        for (SoftwareDTO softwareDTO : softwareList) {
            TreeNodeVO treeNodeVO = new TreeNodeVO();
            treeNodeVO.setId(softwareDTO.getId().toString());
            treeNodeVO.setLabel(softwareDTO.getName());
            treeNodeVO.setParentId(softwareDTO.getGroupId().toString());
            treeList.add(treeNodeVO);
        }
        for (SoftwareGroupDTO softwareGroupDTO : softwareGroupList) {
            TreeNodeVO treeNodeVO = new TreeNodeVO();
            treeNodeVO.setId(softwareGroupDTO.getId().toString());
            treeNodeVO.setLabel(softwareGroupDTO.getName());
            treeList.add(treeNodeVO);
        }
        return DefaultWebResponse.Builder.success(ImmutableMap.of(TREE_FIELD, new TreeBuilder<>(treeList).build()));
    }

}

