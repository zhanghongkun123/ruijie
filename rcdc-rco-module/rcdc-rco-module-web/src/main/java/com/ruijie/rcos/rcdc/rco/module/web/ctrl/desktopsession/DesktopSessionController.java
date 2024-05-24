package com.ruijie.rcos.rcdc.rco.module.web.ctrl.desktopsession;

import com.ruijie.rcos.gss.log.module.def.api.BaseAuditLogAPI;
import com.ruijie.rcos.rcdc.rco.module.def.api.DesktopSessionServiceAPI;
import com.ruijie.rcos.rcdc.rco.module.def.api.ExportAPI;
import com.ruijie.rcos.rcdc.rco.module.def.api.request.DesktopSessionPageSearchRequest;
import com.ruijie.rcos.rcdc.rco.module.def.api.request.PageSearchRequest;
import com.ruijie.rcos.rcdc.rco.module.def.desktopsession.DesktopSessionDTO;
import com.ruijie.rcos.rcdc.rco.module.def.security.auditapply.dto.ExportExcelCacheDTO;
import com.ruijie.rcos.rcdc.rco.module.def.security.auditapply.response.GetExportExcelResponse;
import com.ruijie.rcos.rcdc.rco.module.web.ctrl.desktopsession.batchtask.DeleteDesktopUserSessionBatchTaskHandler;
import com.ruijie.rcos.rcdc.rco.module.web.ctrl.user.UserBusinessKey;
import com.ruijie.rcos.rcdc.rco.module.web.ctrl.user.request.IdArrWebRequest;
import com.ruijie.rcos.rcdc.rco.module.web.response.CommonWebResponse;
import com.ruijie.rcos.sk.base.batch.BatchTaskBuilder;
import com.ruijie.rcos.sk.base.batch.BatchTaskSubmitResult;
import com.ruijie.rcos.sk.base.batch.DefaultBatchTaskItem;
import com.ruijie.rcos.sk.base.exception.BusinessException;
import com.ruijie.rcos.sk.base.log.Logger;
import com.ruijie.rcos.sk.base.log.LoggerFactory;
import com.ruijie.rcos.sk.modulekit.api.comm.DefaultPageResponse;
import com.ruijie.rcos.sk.pagekit.api.PageQueryBuilderFactory;
import com.ruijie.rcos.sk.webmvc.api.request.PageWebRequest;
import com.ruijie.rcos.sk.webmvc.api.response.DefaultWebResponse;
import com.ruijie.rcos.sk.webmvc.api.response.DownloadWebResponse;
import com.ruijie.rcos.sk.webmvc.api.session.SessionContext;
import com.ruijie.rcos.sk.webmvc.api.vo.Sort;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Controller;
import org.springframework.util.Assert;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Stream;

import static com.ruijie.rcos.rcdc.rco.module.web.util.DateUtil.YYYYMMDDHH24MISS;

/**
 * Description: 多会话桌面-会话管理
 * Copyright: Copyright (c) 2024
 * Company: Ruijie Co., Ltd.
 * Create Time: 2024年02月19日
 *
 * @author wangjie9
 */
@Api(tags = "多会话桌面-会话管理")
@Controller
@RequestMapping("/rco/cloudDesktop/session")
public class DesktopSessionController {

    private static final Logger LOGGER = LoggerFactory.getLogger(DesktopSessionController.class);

    private static final String EXPORT_FILE_NAME = "desktopSessionData";

    @Autowired
    private PageQueryBuilderFactory pageQueryBuilderFactory;

    @Autowired
    private DesktopSessionServiceAPI desktopSessionApi;

    @Autowired
    private ExportAPI exportAPI;

    @Autowired
    private BaseAuditLogAPI auditLogAPI;


    /**
     * 会话列表
     * @param request 请求参数
     * @param sessionContext session信息
     * @return 响应
     * @throws BusinessException 业务异常
     */
    @ApiOperation("会话列表")
    @RequestMapping(value = "/list", method = RequestMethod.POST)
    public DefaultWebResponse list(PageWebRequest request, SessionContext sessionContext) throws BusinessException {
        Assert.notNull(request, "request must not be null");
        Assert.notNull(sessionContext, "sessionContext request must not be null");

        PageSearchRequest desktopSessionPageSearchRequest = new DesktopSessionPageSearchRequest(request);
        generateDesktopSortArr(desktopSessionPageSearchRequest.getSortArr());

        DefaultPageResponse<DesktopSessionDTO> response = desktopSessionApi.pageQuery(desktopSessionPageSearchRequest);
        return DefaultWebResponse.Builder.success(response);
    }



    /**
     * 导出会话信息到excel
     * @param request 请求参数
     * @param sessionContext session信息
     * @return 响应
     * @throws BusinessException 业务异常
     */
    @ApiOperation("导出会话信息")
    @RequestMapping(value = "/export", method = RequestMethod.POST)
    public CommonWebResponse export(PageWebRequest request, SessionContext sessionContext) throws BusinessException {
        Assert.notNull(request, "request must not be null");
        Assert.notNull(sessionContext, "sessionContext request must not be null");

        String userId = sessionContext.getUserId().toString();
        PageSearchRequest desktopSessionPageSearchRequest = new DesktopSessionPageSearchRequest(request);
        generateDesktopSortArr(desktopSessionPageSearchRequest.getSortArr());

        desktopSessionApi.exportDataAsync(desktopSessionPageSearchRequest, userId);
        return CommonWebResponse.success();
    }

    /**
     * 获取会话信息数据导出任务情况
     *
     * @param sessionContext sessionContext
     * @return DataResult
     */
    @ApiOperation("获取导出结果")
    @RequestMapping(value = "/getExportResult", method = RequestMethod.POST)
    public CommonWebResponse<ExportExcelCacheDTO> getExportResult(SessionContext sessionContext) {
        Assert.notNull(sessionContext, "sessionContext must not be null");

        String userId = sessionContext.getUserId().toString();
        ExportExcelCacheDTO response = desktopSessionApi.getExportDataCache(userId);
        return CommonWebResponse.success(response);
    }

    /**
     * 下载会话信息报表excel
     *
     * @param sessionContext SessionContext
     * @return DataResult
     * @throws BusinessException 业务异常
     */
    @ApiOperation("下载会话信息excel")
    @RequestMapping(value = "/downloadExportData", method = RequestMethod.GET)
    public DownloadWebResponse downloadExportFile(SessionContext sessionContext) throws BusinessException {
        Assert.notNull(sessionContext, "sessionContext must not be null");

        try {
            String userId = sessionContext.getUserId().toString();
            GetExportExcelResponse exportFile = desktopSessionApi.getExportFile(userId);
            DownloadWebResponse.Builder builder = new DownloadWebResponse.Builder();
            SimpleDateFormat df = new SimpleDateFormat(YYYYMMDDHH24MISS);
            String fileName = EXPORT_FILE_NAME + df.format(new Date());
            String suffix = StringUtils.getFilenameExtension(exportFile.getExportFile().getName());
            auditLogAPI.recordLog(UserBusinessKey.RCDC_RCO_DOWNLOAD_DESKTOP_SESSION_INFO_SUCCESS_LOG);
            return builder.setFile(exportFile.getExportFile(), false).setName(fileName, suffix).build();
        } catch (BusinessException e) {
            auditLogAPI.recordLog(UserBusinessKey.RCDC_RCO_DOWNLOAD_DESKTOP_SESSION_INFO_FAIL_LOG, e, e.getI18nMessage());
            throw new BusinessException(UserBusinessKey.RCDC_RCO_DOWNLOAD_DESKTOP_SESSION_INFO_FAIL_LOG, e, e.getI18nMessage());
        }
    }

    /**
     * 注销会话
     *
     * @param request        页面请求参数
     * @param builder        批量任务
     * @return 返回结果
     * @throws BusinessException 业务异常
     */
    @ApiOperation("注销会话")
    @RequestMapping(value = "/logout", method = RequestMethod.POST)
    public DefaultWebResponse logout(IdArrWebRequest request, BatchTaskBuilder builder) throws BusinessException {
        Assert.notNull(request, "request不能为null");
        Assert.notEmpty(request.getIdArr(), "id不能为空");
        Assert.notNull(builder, "BatchTaskBuilder不能为null");
        UUID[] idArr = request.getIdArr();

        // 批量注销会话
        final Iterator<DefaultBatchTaskItem> iterator =
                Stream.of(idArr).distinct()
                        .map(id -> DefaultBatchTaskItem.builder().itemId(id)
                                .itemName(UserBusinessKey.RCDC_RCO_USER_DESTROY_SESSION_ITEM_NAME, new String[]{}).build()).iterator();

        DeleteDesktopUserSessionBatchTaskHandler handler = new DeleteDesktopUserSessionBatchTaskHandler(iterator);
        // 注销单条会话
        BatchTaskSubmitResult result;
        if (idArr.length == 1) {
            result = builder.setTaskName(UserBusinessKey.RCDC_RCO_USER_DESTROY_SESSION_ITEM_NAME)
                    .setTaskDesc(UserBusinessKey.RCDC_RCO_USER_DESTROY_SESSION_TASK_DESC).registerHandler(handler).start();
        } else {
            result = builder.setTaskName(UserBusinessKey.RCDC_RCO_USER_DESTROY_SESSION_ITEM_NAME)
                    .setTaskDesc(UserBusinessKey.RCDC_RCO_USER_DESTROY_SESSION_BATCH_TASK_DESC).enableParallel().registerHandler(handler).start();
        }
        return DefaultWebResponse.Builder.success(result);
    }

    /**
     * 构建排序
     *
     * @param sortArr 排序规则
     * @return 排序信息
     */
    private Sort[] generateDesktopSortArr(@Nullable Sort[] sortArr) {
        if (sortArr == null) {
            Sort sortThird = new Sort();
            sortThird.setSortField("createTime");
            sortThird.setDirection(Sort.Direction.DESC);
            return new Sort[] {sortThird};
        }
        List<Sort> sortList = new ArrayList<>();
        Sort sortFirst = new Sort();
        sortFirst.setSortField("createTime");
        sortFirst.setDirection(Sort.Direction.DESC);
        sortList.add(sortFirst);
        for (Sort sort : sortArr) {
            sortList.add(sort);
        }
        return sortList.toArray(new Sort[sortList.size()]);
    }
}
