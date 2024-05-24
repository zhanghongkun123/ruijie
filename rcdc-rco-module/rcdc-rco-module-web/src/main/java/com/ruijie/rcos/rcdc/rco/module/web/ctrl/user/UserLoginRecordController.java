package com.ruijie.rcos.rcdc.rco.module.web.ctrl.user;

import com.alibaba.fastjson.JSON;
import com.ruijie.rcos.gss.log.module.def.api.BaseAuditLogAPI;
import com.ruijie.rcos.rcdc.rco.module.def.api.ExportAPI;
import com.ruijie.rcos.rcdc.rco.module.def.api.UserLoginRecordAPI;
import com.ruijie.rcos.rcdc.rco.module.def.api.dto.ExportCacheDTO;
import com.ruijie.rcos.rcdc.rco.module.def.api.dto.user.UserLoginRecordDTO;
import com.ruijie.rcos.rcdc.rco.module.def.api.request.ExportRequest;
import com.ruijie.rcos.rcdc.rco.module.def.api.request.UserLoginRecordPageRequest;
import com.ruijie.rcos.rcdc.rco.module.def.api.response.GetExportCacheResponse;
import com.ruijie.rcos.rcdc.rco.module.def.api.response.GetExportFileResponse;
import com.ruijie.rcos.rcdc.rco.module.web.ctrl.user.request.EmptyDownloadWebRequest;
import com.ruijie.rcos.rcdc.rco.module.web.ctrl.user.request.TimePageWebRequest;
import com.ruijie.rcos.rcdc.rco.module.web.ctrl.user.request.UserLoginRecordWebRequest;
import com.ruijie.rcos.rcdc.rco.module.web.response.CommonWebResponse;
import com.ruijie.rcos.sk.base.exception.BusinessException;
import com.ruijie.rcos.sk.base.log.Logger;
import com.ruijie.rcos.sk.base.log.LoggerFactory;
import com.ruijie.rcos.sk.base.validation.EnableCustomValidate;
import com.ruijie.rcos.sk.modulekit.api.comm.DefaultPageResponse;
import com.ruijie.rcos.sk.webmvc.api.response.DefaultWebResponse;
import com.ruijie.rcos.sk.webmvc.api.response.DownloadWebResponse;
import com.ruijie.rcos.sk.webmvc.api.session.SessionContext;
import com.ruijie.rcos.sk.webmvc.api.vo.Sort;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.Assert;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.text.SimpleDateFormat;
import java.util.*;

import static com.ruijie.rcos.rcdc.rco.module.web.util.DateUtil.YYYYMMDDHH24MISS;


/**
 * @author zjy
 * @version 1.0.0
 * @Description 用户登录记录controller
 * @createTime 2021-10-28 15:00:00
 */
@Api(tags = "用户组管理")
@Controller
@RequestMapping("/rco/user/loginRecord")
@EnableCustomValidate(enable = false)
public class UserLoginRecordController {

    private static final Logger LOGGER = LoggerFactory.getLogger(UserLoginRecordController.class);

    private static final String EXPORT_FILE_NAME = "userLoginRecord";

    // 限制:导出60W条记录
    private static final int EXPORT_NUM_LIMIT = 600000;

    private static final Integer SHEET_SIZE = 20000;

    @Autowired
    private UserLoginRecordAPI userLoginRecordAPI;

    @Autowired
    private BaseAuditLogAPI auditLogAPI;

    @Autowired
    private ExportAPI exportAPI;

    /**
     * 查询登录记录
     *
     * @param request        页面请求参数
     * @param sessionContext session信息
     * @return DataResult
     * @throws BusinessException 业务异常
     */
    @RequestMapping(value = "/list")
    public DefaultWebResponse list(@RequestBody TimePageWebRequest request, SessionContext sessionContext) throws BusinessException {
        Assert.notNull(request, "request is null.");
        Assert.notNull(sessionContext, "sessionContext is null.");

        UserLoginRecordPageRequest pageReq = new UserLoginRecordPageRequest(request);
        pageReq.setStartTime(request.getStartTime());
        pageReq.setEndTime(request.getEndTime());
        Sort webSort = request.getSort();
        pageReq.setSortArr(generateSqlSortArr(webSort));
        DefaultPageResponse<UserLoginRecordDTO> resp = userLoginRecordAPI.pageQuery(pageReq);
        return DefaultWebResponse.Builder.success(resp);
    }

    /**
     * 异步导出用户报表为excel
     * @param request 导出请求参数
     * @param sessionContext Session上下文
     * @return result
     * @throws BusinessException 业务异常
     */
    @RequestMapping(value = "/export", method = RequestMethod.POST)
    @ApiOperation("导出用户报表")
    public CommonWebResponse export(UserLoginRecordWebRequest request, SessionContext sessionContext) throws BusinessException {
        Assert.notNull(request, "request is null.");
        Assert.notNull(sessionContext, "sessionContext is null.");

        UserLoginRecordPageRequest pageReq = new UserLoginRecordPageRequest(request);
        pageReq.setStartTime(request.getStartTime());
        pageReq.setEndTime(request.getEndTime());
        Sort webSort = request.getSort();
        pageReq.setSortArr(generateSqlSortArr(webSort));
        pageReq.setPage(0);
        pageReq.setLimit(10);
        DefaultPageResponse<UserLoginRecordDTO> resp = userLoginRecordAPI.pageQuery(pageReq);
        long total = resp.getTotal();

        int exportCount = Math.min((int) total , EXPORT_NUM_LIMIT);
        try {
            String userId = sessionContext.getUserId().toString();

            ExportRequest req = new ExportRequest(userId, generateSqlSortArr(null));
            req.setTotalCount((int) total);
            req.setExportTotalCount(exportCount);
            req.setStartTime(request.getStartTime());
            req.setSheetSize(SHEET_SIZE);
            Date endTime = request.getEndTime();
            UserLoginRecordDTO dto = userLoginRecordAPI.findLastByCreateTime(endTime);
            if (Objects.nonNull(dto)) {
                endTime = dto.getCreateTime();
            }

            req.setEndTime(endTime);
            req.setSearchKeyword(request.getSearchKeyword());
            exportAPI.exportDataAsync(req);
            auditLogAPI.recordLog(UserBusinessKey.RCDC_RCO_EXPORT_USER_USE_INFO_SUCCESS_LOG);
        } catch (BusinessException e) {
            auditLogAPI.recordLog(UserBusinessKey.RCDC_RCO_EXPORT_USER_USE_INFO_FAIL_LOG, e, e.getI18nMessage());
            throw new BusinessException(UserBusinessKey.RCDC_RCO_EXPORT_USER_USE_INFO_FAIL_LOG, e, e.getI18nMessage());
        }
        return CommonWebResponse.success();
    }

    private Sort[] generateSqlSortArr(Sort webSort) {

        List<Sort> sortList = new ArrayList<>();

        if (Objects.isNull(webSort)) {
            sortList.add(generateSort("loginTime", Sort.Direction.DESC));
            sortList.add(generateSort("connectTime", Sort.Direction.DESC));
            return sortList.toArray(new Sort[sortList.size()]);
        }

        sortList.add(webSort);
        if (Objects.equals("connectTime", webSort.getSortField())) {
            sortList.add(generateSort("loginTime", webSort.getDirection()));
            return sortList.toArray(new Sort[sortList.size()]);
        }

        if (Objects.equals("loginTime", webSort.getSortField())) {
            sortList.add(generateSort("connectTime", webSort.getDirection()));
            return sortList.toArray(new Sort[sortList.size()]);
        }

        sortList.add(generateSort("loginTime", webSort.getDirection()));
        sortList.add(generateSort("connectTime", webSort.getDirection()));
        return sortList.toArray(new Sort[sortList.size()]);
    }

    private Sort generateSort(String sortField, Sort.Direction direction) {
        Sort sort = new Sort();
        sort.setSortField(sortField);
        sort.setDirection(direction);
        return sort;
    }

    /**
     * 获取用户报表数据导出任务情况
     *
     * @param sessionContext sessionContext
     * @return DataResult
     */
    @ApiOperation("获取导出结果")
    @RequestMapping(value = "/getExportResult", method = RequestMethod.POST)
    public CommonWebResponse<ExportCacheDTO> getExportResult(SessionContext sessionContext) {
        Assert.notNull(sessionContext, "sessionContext must not be null");

        String userId = sessionContext.getUserId().toString();
        GetExportCacheResponse response = exportAPI.getExportDataCache(userId);
        return CommonWebResponse.success(response.getExportCacheDTO());
    }

    /**
     * 下载用户报表excel
     *
     * @param request 页面请求参数
     * @param sessionContext SessionContext
     * @return DataResult
     * @throws BusinessException 业务异常
     */
    @RequestMapping(value = "/downloadExportData", method = RequestMethod.GET)
    public DownloadWebResponse downloadExportFile(EmptyDownloadWebRequest request, SessionContext sessionContext) throws BusinessException {
        Assert.notNull(request, "request must not be null");
        Assert.notNull(sessionContext, "sessionContext must not be null");

        try {
            String userId = sessionContext.getUserId().toString();
            GetExportFileResponse exportFile = exportAPI.getExportFile(userId);
            DownloadWebResponse.Builder builder = new DownloadWebResponse.Builder();
            SimpleDateFormat df = new SimpleDateFormat(YYYYMMDDHH24MISS);
            String fileName = EXPORT_FILE_NAME + df.format(new Date());
            String suffix = StringUtils.getFilenameExtension(exportFile.getExportFile().getName());
            auditLogAPI.recordLog(UserBusinessKey.RCDC_RCO_DOWNLOAD_USER_USE_INFO_SUCCESS_LOG);
            return builder.setFile(exportFile.getExportFile(), false).setName(fileName, suffix).build();
        } catch (BusinessException e) {
            auditLogAPI.recordLog(UserBusinessKey.RCDC_RCO_DOWNLOAD_USER_USE_INFO_FAIL_LOG, e, e.getI18nMessage());
            throw new BusinessException(UserBusinessKey.RCDC_RCO_DOWNLOAD_USER_USE_INFO_FAIL_LOG, e, e.getI18nMessage());
        }
    }
}
