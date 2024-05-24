package com.ruijie.rcos.rcdc.rco.module.web.ctrl.softwarecontrol;

import com.ruijie.rcos.rcdc.rco.module.def.api.SoftwareControlMgmtAPI;
import com.ruijie.rcos.rcdc.rco.module.def.api.request.PageSearchRequest;
import com.ruijie.rcos.rcdc.rco.module.def.constants.Constants;
import com.ruijie.rcos.rcdc.rco.module.def.softwarecontrol.dto.SoftRelatedSoftStrategyDTO;
import com.ruijie.rcos.rcdc.rco.module.def.softwarecontrol.dto.SoftRelatedSoftStrategyRelatedDTO;
import com.ruijie.rcos.rcdc.rco.module.def.softwarecontrol.dto.SoftwareStrategyRelatedSoftwareDTO;
import com.ruijie.rcos.rcdc.rco.module.def.softwarecontrol.request.SoftStrategyRelatedPageSearchRequest;
import com.ruijie.rcos.rcdc.rco.module.def.softwarecontrol.request.SoftStrategyRelationPageSearchRequest;
import com.ruijie.rcos.rcdc.rco.module.def.softwarecontrol.request.SoftwareStrategyRelatedSoftwarePageSearchRequest;
import com.ruijie.rcos.rcdc.rco.module.web.config.annotation.ApiVersion;
import com.ruijie.rcos.rcdc.rco.module.web.config.annotation.ApiVersions;
import com.ruijie.rcos.rcdc.rco.module.web.config.annotation.Version;
import com.ruijie.rcos.rcdc.rco.module.web.ctrl.aaa.AaaBusinessKey;
import com.ruijie.rcos.rcdc.rco.module.web.ctrl.softwarecontrol.request.SoftwareStrategyRelatedAddWebRequest;
import com.ruijie.rcos.rcdc.rco.module.web.ctrl.softwarecontrol.request.SoftwareStrategyRelatedSoftwareIdArrWebRequest;
import com.ruijie.rcos.rcdc.rco.module.web.response.CommonWebResponse;
import com.ruijie.rcos.sk.base.batch.BatchTaskBuilder;
import com.ruijie.rcos.sk.base.exception.BusinessException;
import com.ruijie.rcos.sk.base.log.Logger;
import com.ruijie.rcos.sk.base.log.LoggerFactory;
import com.ruijie.rcos.sk.base.util.StringUtils;
import com.ruijie.rcos.sk.modulekit.api.comm.DefaultPageResponse;
import com.ruijie.rcos.sk.webmvc.api.request.PageWebRequest;
import com.ruijie.rcos.sk.webmvc.api.session.SessionContext;
import com.ruijie.rcos.sk.webmvc.api.vo.ExactMatch;
import com.ruijie.rcos.sk.webmvc.api.vo.Sort;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.lang3.ArrayUtils;
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
@Api(tags = "软件管控软件策略关联软件管理")
@Controller
@RequestMapping("/rco/softwareStrategyRelatedSoftware")
public class SoftwareStrategyRelatedSoftwareController {

    private static final Logger LOGGER = LoggerFactory.getLogger(SoftwareStrategyRelatedSoftwareController.class);

    @Autowired
    private SoftwareControlMgmtAPI softwareControlMgmtAPI;

    /**
     * *查询
     *
     * @param request        页面请求参数
     * @param sessionContext session信息
     * @return DataResult
     * @throws BusinessException 业务异常
     */
    @RequestMapping(value = "/list", method = RequestMethod.POST)
    @ApiOperation("软件列表")
    public CommonWebResponse<DefaultPageResponse<SoftwareStrategyRelatedSoftwareDTO>> list(PageWebRequest request, SessionContext sessionContext)
            throws BusinessException {
        Assert.notNull(request, "request is null.");
        Assert.notNull(sessionContext, "sessionContext is null.");
        SoftwareStrategyRelatedSoftwarePageSearchRequest softwareStrategyRelatedSoftwarePageSearchRequest =
                new SoftwareStrategyRelatedSoftwarePageSearchRequest(request);
        softwareStrategyRelatedSoftwarePageSearchRequest.setSortArr(
                request.getSort() != null ? Arrays.asList(request.getSort()).toArray(new Sort[0]) : null);
        String keyword = softwareStrategyRelatedSoftwarePageSearchRequest.getSearchKeyword();
        if (StringUtils.hasText(keyword)) {
            softwareStrategyRelatedSoftwarePageSearchRequest.setSearchKeyword(keyword.toLowerCase());
        }
        return CommonWebResponse.success(softwareControlMgmtAPI.
                softwareStrategyRelatedSoftwarePageQuery(softwareStrategyRelatedSoftwarePageSearchRequest));
    }


    /**
     * 删除软件策略关联软件关系
     *
     * @param idArrWebRequest 入参
     * @param builder         批量任务创建对象
     * @return 响应
     * @throws BusinessException 业务异常
     */
    @ApiOperation("删除软件策略关联软件关系")
    @RequestMapping(value = "delete", method = RequestMethod.POST)
    public CommonWebResponse delete(SoftwareStrategyRelatedSoftwareIdArrWebRequest idArrWebRequest, BatchTaskBuilder builder)
            throws BusinessException {
        Assert.notNull(idArrWebRequest, "idArrWebRequest must not be null");
        Assert.notNull(builder, "builder can not be null");

        String[] idArr = idArrWebRequest.getIdArr();
        Assert.notEmpty(idArr, "idArr can not be null");
        // 批量删除任务

        Map<UUID, List<UUID>> softwareStrategyMap = new HashMap<>(idArr.length);
        for (String uuid : idArr) {
            String[] strategyIdAndSoftwareIdArr = uuid.split("\\|");
            UUID strategyId = UUID.fromString(strategyIdAndSoftwareIdArr[0]);
            UUID softwareId = UUID.fromString(strategyIdAndSoftwareIdArr[1]);
            List<UUID> softwareIdList = softwareStrategyMap.get(strategyId);
            if (softwareIdList == null) {
                softwareIdList = new ArrayList<>();
            }
            softwareIdList.add(softwareId);
            softwareStrategyMap.put(strategyId, softwareIdList);
        }
        for (Map.Entry<UUID, List<UUID>> entry : softwareStrategyMap.entrySet()) {
            softwareControlMgmtAPI.deleteSoftwareFromSoftwareStrategyDetail(entry.getKey(), entry.getValue());
        }

        return CommonWebResponse.success(AaaBusinessKey.RCDC_AAA_OPERATOR_SUCCESS, new String[]{});
    }

    /**
     * 增加软件策略关联软件关系
     *
     * @param idArrWebRequest 入参
     * @param builder         批量任务创建对象
     * @return 响应
     * @throws BusinessException 业务异常
     */
    @ApiOperation("添加软件策略关联软件关系")
    @RequestMapping(value = "add", method = RequestMethod.POST)
    public CommonWebResponse addSoftwareStrategyRelation(SoftwareStrategyRelatedAddWebRequest idArrWebRequest, BatchTaskBuilder builder)
            throws BusinessException {
        Assert.notNull(idArrWebRequest, "idArrWebRequest must not be null");
        Assert.notNull(builder, "builder can not be null");

        Set<UUID> softwareIdList = new HashSet<>();
        for (UUID softwareId : idArrWebRequest.getSoftwareIdArr()) {
            softwareIdList.add(softwareId);
        }

        Set<UUID> softwareStrategyIdList = new HashSet<>();
        for (UUID softwareStrategyId : idArrWebRequest.getSoftwareStrategyIdArr()) {
            softwareStrategyIdList.add(softwareStrategyId);
        }

        softwareControlMgmtAPI.addSoftwareStrategyRelation(softwareIdList, softwareStrategyIdList);
        return CommonWebResponse.success(AaaBusinessKey.RCDC_AAA_OPERATOR_SUCCESS, new String[]{});
    }

    /**
     * 增加根据软件id返回软件策略列表接口
     *
     * @param request        请求参数
     * @param sessionContext session信息
     * @return 获取结果
     * @throws BusinessException 异常
     */
    @ApiOperation("增加根据软件id返回软件策略列表接口 ")
    @ApiVersions({@ApiVersion(value = Version.V3_1_1, descriptions = {"增加根据软件id返回软件策略列表接口"})})
    @RequestMapping(value = "softStrategyPageBySoft/list", method = RequestMethod.POST)
    public CommonWebResponse softStrategyPageBySoft(PageWebRequest request,
                                                    SessionContext sessionContext) throws BusinessException {
        Assert.notNull(request, "request is not null");
        Assert.notNull(sessionContext, "sessionContext is not null");
        PageSearchRequest pageReq = new SoftStrategyRelationPageSearchRequest(request);

        DefaultPageResponse<SoftRelatedSoftStrategyDTO> pageResponse = softwareControlMgmtAPI.pageQuery(pageReq);
        return CommonWebResponse.success(pageResponse);
    }


    /**
     *
     *
     * @param request        请求参数
     * @param sessionContext session信息
     * @return 获取结果
     * @throws BusinessException 异常
     */
    @ApiOperation("增加返回软件策略列表接口,若软件已经关联则给出标识")
    @ApiVersions({@ApiVersion(value = Version.V3_1_1, descriptions = {"增加返回软件策略列表接口,若软件已经关联则给出标识"})})
    @RequestMapping(value = "softStrategyPageByRelated/list", method = RequestMethod.POST)
    public CommonWebResponse softStrategyPageBySelected(PageWebRequest request,
                                                        SessionContext sessionContext) throws BusinessException {
        Assert.notNull(request, "request is not null");
        Assert.notNull(sessionContext, "sessionContext is not null");
        PageSearchRequest pageReq = new SoftStrategyRelatedPageSearchRequest(request);

        UUID softwareId = getSoftwareIdFromRequest(request);
        DefaultPageResponse<SoftRelatedSoftStrategyRelatedDTO> pageResponse = softwareControlMgmtAPI.pageQueryForRelated(pageReq, softwareId);
        return CommonWebResponse.success(pageResponse);
    }


    /**
     * 从request中获取软件id
     * @param webRequest web参数
     * @return
     */
    private UUID getSoftwareIdFromRequest(PageWebRequest webRequest) {
        UUID softwareId = null;
        if (ArrayUtils.isNotEmpty(webRequest.getExactMatchArr())) {
            ExactMatch[] exactMatchArr = webRequest.getExactMatchArr();
            for (ExactMatch exactMatch : exactMatchArr) {
                if (StringUtils.equals(Constants.WEB_REQUEST_SOFTWARE_FIELD_NAME, exactMatch.getName())) {
                    Object[] valueArr = exactMatch.getValueArr();
                    if (valueArr != null && valueArr.length > 0) {
                        softwareId = UUID.fromString(String.valueOf(exactMatch.getValueArr()[0]));
                        break;
                    }
                }
            }
        }
        return softwareId;
    }
}

