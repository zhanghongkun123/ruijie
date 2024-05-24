package com.ruijie.rcos.rcdc.rco.module.web.ctrl.bigscreen;

import com.ruijie.rcos.rcdc.clouddesktop.module.def.api.CbbVDIDeskMgmtAPI;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.dto.CbbDeskDTO;
import com.ruijie.rcos.rcdc.rco.module.def.api.DesktopAPI;
import com.ruijie.rcos.rcdc.rco.module.def.api.dto.bigscreen.DesktopNormalDistributionDTO;
import com.ruijie.rcos.rcdc.rco.module.def.api.request.bigscreen.AbnormalPageRequest;
import com.ruijie.rcos.rcdc.rco.module.def.api.request.bigscreen.DesktopOnlineSituationStatisticsRequest;
import com.ruijie.rcos.rcdc.rco.module.def.api.request.bigscreen.DesktopUsageIdArrRequest;
import com.ruijie.rcos.rcdc.rco.module.def.api.request.bigscreen.GetUserGroupOverviewRequest;
import com.ruijie.rcos.rcdc.rco.module.def.api.request.bigscreen.ListDesktopByUserGroupPageRequest;
import com.ruijie.rcos.rcdc.rco.module.def.api.response.bigscreen.AbnormalDesktopListResponse;
import com.ruijie.rcos.rcdc.rco.module.def.api.response.bigscreen.DeskTopStatusResponse;
import com.ruijie.rcos.rcdc.rco.module.def.api.response.bigscreen.DesktopOnlineSituationStatisticsResponse;
import com.ruijie.rcos.rcdc.rco.module.def.api.response.bigscreen.DesktopUsageResponse;
import com.ruijie.rcos.rcdc.rco.module.def.api.response.bigscreen.ListDesktopResponse;
import com.ruijie.rcos.rcdc.rco.module.def.api.response.bigscreen.UserGroupOverviewResponse;
import com.ruijie.rcos.rcdc.rco.module.web.ctrl.bigscreen.request.bigscreen.AbnormalPageWebRequest;
import com.ruijie.rcos.rcdc.rco.module.web.ctrl.bigscreen.request.bigscreen.GetDesktopNormalDistributionWebRequest;
import com.ruijie.rcos.rcdc.rco.module.web.ctrl.bigscreen.request.bigscreen.GetUserGroupOverviewWebRequest;
import com.ruijie.rcos.rcdc.rco.module.web.ctrl.bigscreen.request.bigscreen.ListDesktopByUserGroupPageWebRequest;
import com.ruijie.rcos.rcdc.rco.module.web.ctrl.bigscreen.vo.AbnormalDesktopVO;
import com.ruijie.rcos.rcdc.rco.module.web.ctrl.user.request.IdArrWebRequest;
import com.ruijie.rcos.rcdc.rco.module.web.util.DateUtil;
import com.ruijie.rcos.sk.base.exception.BusinessException;
import com.ruijie.rcos.sk.base.log.Logger;
import com.ruijie.rcos.sk.base.log.LoggerFactory;
import com.ruijie.rcos.sk.webmvc.api.request.DefaultWebRequest;
import com.ruijie.rcos.sk.webmvc.api.response.DefaultWebResponse;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Description: 大屏监控，云桌面相关controller
 * Copyright: Copyright (c) 2019
 * Company: Ruijie Co., Ltd.
 * Create Time: 2019/8/10 11:44
 *
 * @author zhangyichi
 */
@Controller
@RequestMapping("/rco/desktop")
public class DesktopController {

    private static final Logger LOGGER = LoggerFactory.getLogger(DesktopController.class);

    @Autowired
    private DesktopAPI desktopAPI;

    @Autowired
    private CbbVDIDeskMgmtAPI cbbVDIDeskMgmtAPI;

    private static final Integer DAFAULT_SECOND_AMOUNT = -60;

    /**
     * 获取集群云桌面概要数据
     *
     * @param request 请求
     * @return 返回
     * @throws BusinessException 异常
     */
    @RequestMapping(value = "/getAllDesktopOverview")
    public DefaultWebResponse getAllDesktopOverview(DefaultWebRequest request) throws BusinessException {
        Assert.notNull(request,"request 不能为空");

        return DefaultWebResponse.Builder.success(desktopAPI.getAllDesktopOverview());
    }

    /**
     * 获取部门下所有云桌面一个月内各资源每天均值列表
     *
     * @param request 部门用户组id
     * @return 返回
     * @throws BusinessException 业务异常
     */
    @RequestMapping("/getUserGroupOverview")
    public DefaultWebResponse getUserGroupOverview(GetUserGroupOverviewWebRequest request) throws BusinessException {
        Assert.notNull(request, "request cannot be null!");

        GetUserGroupOverviewRequest apiRequest = new GetUserGroupOverviewRequest();
        BeanUtils.copyProperties(request, apiRequest);
        UserGroupOverviewResponse response = desktopAPI.getUserGroupOverview(apiRequest);
        return DefaultWebResponse.Builder.success(response);
    }

    /**
     * 分页获取部门下已开机的云桌面列表
     *
     * @param request 部门用户组ID
     * @return 云桌面列表
     * @throws BusinessException 业务异常
     */
    @RequestMapping(value = "/listDesktopByUserGroup")
    public DefaultWebResponse listDesktopByUserGroup(ListDesktopByUserGroupPageWebRequest request) throws BusinessException {
        Assert.notNull(request, "request cannot be null!");

        ListDesktopByUserGroupPageRequest apiRequest = new ListDesktopByUserGroupPageRequest();
        BeanUtils.copyProperties(request, apiRequest);
        ListDesktopResponse apiResponse = desktopAPI.listDesktopByUserGroup(apiRequest);
        return DefaultWebResponse.Builder.success(apiResponse);
    }

    /**
     * 根据云桌面id获取桌面资源使用情况
     *
     * @param request 云桌面ID数组
     * @return 云桌面资源使用情况
     * @throws BusinessException 业务异常
     */
    @RequestMapping("/getDesktopUsage")
    public DefaultWebResponse getDesktopUsage(IdArrWebRequest request) throws BusinessException {
        Assert.notNull(request, "request cannot be null!");

        DesktopUsageIdArrRequest apiRequest = new DesktopUsageIdArrRequest();
        apiRequest.setIdArr(request.getIdArr());
        // 设置获取时段为最近30秒
        Date endTime = new Date();
        apiRequest.setStartTime(DateUtil.computeDate(endTime, Calendar.SECOND, DAFAULT_SECOND_AMOUNT));
        apiRequest.setEndTime(endTime);
        DesktopUsageResponse apiResponse = desktopAPI.getDesktopUsage(apiRequest);
        return DefaultWebResponse.Builder.success(apiResponse);
    }

    /**
     * 桌面资源使用率正太分布建模数据
     *
     * @param webRequest 请求
     * @return DefaultWebResponse 返回
     * @throws BusinessException 业务异常
     */
    @RequestMapping(value = "/getDesktopNormalDistribution")
    public DefaultWebResponse getDesktopNormalDistribution(GetDesktopNormalDistributionWebRequest webRequest) throws BusinessException {
        Assert.notNull(webRequest, "webRequest can not be null");

        DesktopNormalDistributionDTO desktopNormalDistribution = desktopAPI.getDesktopNormalDistribution(webRequest.getResourceType());
        return DefaultWebResponse.Builder.success(desktopNormalDistribution);
    }

    /**
     * 获取异常关注虚机列表
     *
     * @param request 分页请求
     * @return 返回
     * @throws BusinessException 业务异常
     */
    @RequestMapping("/listAbnormalDesktop")
    public DefaultWebResponse listAbnormalDesktop(AbnormalPageWebRequest request) throws BusinessException {
        Assert.notNull(request, "request cannot be null!");

        AbnormalPageRequest apiRequest = new AbnormalPageRequest();
        BeanUtils.copyProperties(request, apiRequest);
        AbnormalDesktopListResponse apiResponse = desktopAPI.listAbnormalDesktop(apiRequest);
        List<String> nameList = apiResponse.getIdList().stream().map(id -> {
            try {
                CbbDeskDTO deskDTO = cbbVDIDeskMgmtAPI.getDeskVDI(id);
                if (null == deskDTO) {
                    return "";
                }
                return deskDTO.getName();
            } catch (BusinessException e) {
                LOGGER.error("获取云桌面数据失败，id[{}], e[{}]", id, e);
                return "";
            }
        }).collect(Collectors.toList());

        AbnormalDesktopVO abnormalDesktopVO = new AbnormalDesktopVO();
        abnormalDesktopVO.setNameList(nameList);
        abnormalDesktopVO.setTotal(apiResponse.getTotal());
        return DefaultWebResponse.Builder.success(abnormalDesktopVO);
    }

    /**
     * 统计云主机状态数量
     * @return  各种状态数量转态
     * @throws BusinessException  业务异常
     */
    @RequestMapping("/statisticsDeskTopStatus")
    public DefaultWebResponse statisticsDeskTopStatus () {
        DeskTopStatusResponse response = desktopAPI.statisticsDeskTopStatus();
        return DefaultWebResponse.Builder.success(response);
    }

    /**
     * 统计云桌面历史运行状态
     * @param request  请求参数
     * @return  返回统计数据
     * @throws BusinessException  业务异常
     */
    @RequestMapping("/desktopHistoryOnlineSituation")
    public DefaultWebResponse statisticsDesktopHistoryOnlineSituation(DesktopOnlineSituationStatisticsRequest request) throws BusinessException {
        Assert.notNull(request, "request cannot be null!");

        DesktopOnlineSituationStatisticsResponse response = desktopAPI.statisticsDesktopHistoryOnlineSituation(request);
        return DefaultWebResponse.Builder.success(response);
    }
}
