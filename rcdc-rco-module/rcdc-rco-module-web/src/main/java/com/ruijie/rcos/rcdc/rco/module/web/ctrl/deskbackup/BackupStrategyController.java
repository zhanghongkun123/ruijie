package com.ruijie.rcos.rcdc.rco.module.web.ctrl.deskbackup;

import com.alibaba.fastjson.JSONObject;
import com.ruijie.rcos.gss.base.iac.module.annotation.EnableAuthority;
import com.ruijie.rcos.gss.log.module.def.api.BaseAuditLogAPI;
import com.ruijie.rcos.rcdc.backup.module.def.api.CbbBackupAPI;
import com.ruijie.rcos.rcdc.backup.module.def.api.CbbBackupStrategyAPI;
import com.ruijie.rcos.rcdc.backup.module.def.constant.ScheduleConstants;
import com.ruijie.rcos.rcdc.backup.module.def.dto.CbbBackupDataDTO;
import com.ruijie.rcos.rcdc.backup.module.def.dto.CbbBackupStrategyDTO;
import com.ruijie.rcos.rcdc.hciadapter.module.def.api.ExternalStorageMgmtAPI;
import com.ruijie.rcos.rcdc.hciadapter.module.def.dto.externalstorage.ExternalStorageDTO;
import com.ruijie.rcos.rcdc.hciadapter.module.def.dto.request.QueryExternalStorageRequest;
import com.ruijie.rcos.rcdc.hciadapter.module.def.enums.ExternalStorageHealthStateEnum;
import com.ruijie.rcos.rcdc.rco.module.def.api.DeskBackupAPI;
import com.ruijie.rcos.rcdc.rco.module.def.api.request.schedule.RcoScheduleTaskRequest;
import com.ruijie.rcos.rcdc.rco.module.def.timedtasks.dto.CronConvertDTO;
import com.ruijie.rcos.rcdc.rco.module.web.config.annotation.ApiVersion;
import com.ruijie.rcos.rcdc.rco.module.web.config.annotation.ApiVersions;
import com.ruijie.rcos.rcdc.rco.module.web.config.annotation.Version;
import com.ruijie.rcos.rcdc.rco.module.web.ctrl.deskbackup.request.BackupStrategyRequest;
import com.ruijie.rcos.rcdc.rco.module.web.ctrl.deskbackup.vo.BackupStrategyDetailVO;
import com.ruijie.rcos.rcdc.rco.module.web.ctrl.user.UserBusinessKey;
import com.ruijie.rcos.rcdc.rco.module.web.validation.ScheduleValidation;
import com.ruijie.rcos.sk.base.exception.AnnotationValidationException;
import com.ruijie.rcos.sk.base.exception.BusinessException;
import com.ruijie.rcos.sk.base.log.Logger;
import com.ruijie.rcos.sk.base.log.LoggerFactory;
import com.ruijie.rcos.sk.base.quartz.QuartzTaskState;
import com.ruijie.rcos.sk.pagekit.api.PageQueryRequest;
import com.ruijie.rcos.sk.pagekit.api.PageQueryResponse;
import com.ruijie.rcos.sk.webmvc.api.request.IdArrWebRequest;
import com.ruijie.rcos.sk.webmvc.api.request.IdWebRequest;
import com.ruijie.rcos.sk.webmvc.api.response.DefaultWebResponse;
import com.ruijie.rcos.sk.webmvc.api.vo.IdLabelEntry;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.lang3.ArrayUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.util.UUID;

/**
 * Description: 备份策略管理
 * Copyright: Copyright (c) 2024
 * Company: Ruijie Co., Ltd.
 * Create Time: 2024年01月23日
 *
 * @author qiuzy
 */
@Api(tags = "备份策略管理")
@Controller
@RequestMapping("/rco/serverbackup/strategy")
public class BackupStrategyController {

    private static final Logger LOGGER = LoggerFactory.getLogger(ServerBackupController.class);

    @Autowired
    private BaseAuditLogAPI auditLogAPI;

    @Autowired
    ScheduleValidation scheduleValidation;

    @Autowired
    private DeskBackupAPI deskBackupAPI;

    @Autowired
    CbbBackupAPI cbbBackupAPI;

    @Autowired
    private ExternalStorageMgmtAPI externalStorageMgmtAPI;

    @Autowired
    private CbbBackupStrategyAPI cbbBackupStrategyAPI;

    /**
     * @api {POST} /rco/serverbackup/strategy/create 创建备份策略
     * @apiName create
     * @apiGroup strategy
     * @apiDescription 创建备份策略
     * @apiParam (请求字段说明) {String} strategyName 备份名称
     * @apiParam (请求字段说明) {BackupType} backupType 备份对象
     * @apiParam (请求字段说明) {Boolean} strategyState 策略开启/关闭
     * @apiParam (请求字段说明) {TaskCycle} taskCycle 备份周期
     * @apiParam (请求字段说明) {String} scheduleTime 调度时间
     * @apiParam (请求字段说明) {String[]} dayOfWeekArr 一个星期的第几天
     * @apiParam (请求字段说明) {String} day 一个月的第几天
     * @apiParam (请求字段说明) {Long} durationSeconds 持续时间，单位是秒，需要以小时为单位设置
     * @apiParam (请求字段说明) {IdLabelEntry} externalStorageIdLabel 外置存储ID {"id":"","label":""}
     * @apiParam (请求字段说明) {int} maxBackUp 备份保留数量
     * @apiParamExample {json} 请求报文
     *
     *                  {
     *                  "strategyName":"", // 备份名称
     *                  "backupType":"RCDC_BACKUP", // 备份对象
     *                  "strategyState":"true", // true=开启，false=关闭
     *                  "taskCycle":"MONTH", DAY、WEEK、MONTH
     *                  "scheduleTime":"",调度时间
     *                  "dayOfWeekArr":[], //一个星期的第几天
     *                  "day":"", //一个月的第几天
     *                  "durationSeconds":3600, // 单位是秒，需要以小时为单位设置
     *                  "externalStorageIdLabel": {"id":"","label":""}, // 外置存储信息
     *                  "maxBackUp": 3, // 备份保留个数
     *                  }
     *
     * @apiSuccess (响应字段说明){Object} content content
     * @apiSuccess (响应字段说明) {String} content.id 策略ID
     * @apiSuccess (响应字段说明) {String} message 提示信息
     * @apiSuccess (响应字段说明) {String[]} msgArgArr 国际化参数
     * @apiSuccess (响应字段说明) {String} msgKey 国际化key;
     * @apiSuccess (响应字段说明) {Status} status 状态，取值范围：SUCCESS,NO_LOGIN,NO_AUTHORITY,MAINTENANCE_MODE,ERROR;
     * @apiSuccessExample {json} 响应报文
     *                    {
     *                    "content": "8e776868-e8a6-4d5d-8f67-6217719fb11e",
     *                    "message": null,
     *                    "msgArgArr": null,
     *                    "msgKey": null,
     *                    "status": "SUCCESS"
     *                    }
     * @apiUse ErrorResponse
     */
    /**
     * 创建定时备份策略
     *
     * @param request request
     * @return 响应信息
     * @throws BusinessException 业务异常
     */
    @ApiOperation(value = "创建定时备份策略")
    @ApiVersions({@ApiVersion(value = Version.V6_0_50, descriptions = {"创建定时备份策略"})})
    @RequestMapping(value = "/create", method = RequestMethod.POST)
    @EnableAuthority
    public DefaultWebResponse scheduleCreate(BackupStrategyRequest request) throws BusinessException {
        Assert.notNull(request, "request can not be null");

        RcoScheduleTaskRequest scheduleTaskRequest = new RcoScheduleTaskRequest();
        BeanUtils.copyProperties(request, scheduleTaskRequest);
        scheduleTaskRequest.setScheduleTypeCode(ScheduleConstants.DEFAULT_BACKUP_SCHEDULE_NAME);
        scheduleTaskRequest.setTaskName(ScheduleConstants.RCDC_BACKUP);

        try {
            // 定时任务参数校验
            scheduleValidation.scheduleTaskValidate(scheduleTaskRequest);
            // cron表达式转换
            String cronExpression = deskBackupAPI.generateExpression(scheduleTaskRequest);
            CbbBackupStrategyDTO strategyDTO = convertCbbBackupStrategyDTO(request, cronExpression);
            // 校验外置存储是否可用
            ExternalStorageDTO externalStorageDTO = checkExternalStorage(strategyDTO.getExternalStorageId());
            strategyDTO.setExternalStorageName(externalStorageDTO.getName());
            LOGGER.info("服务器定时备份，入参为:[{}]", JSONObject.toJSONString(strategyDTO));
            cbbBackupStrategyAPI.createStrategy(strategyDTO);
            auditLogAPI.recordLog(DeskBackupBusinessKey.RCDC_SERVER_BACKUP_STRATEGY_CREATE_SUCCESS_LOG, request.getName());
        } catch (BusinessException e) {
            auditLogAPI.recordLog(DeskBackupBusinessKey.RCDC_SERVER_BACKUP_STRATEGY_CREATE_FAIL_LOG, e, request.getName(), e.getI18nMessage());
            LOGGER.error("创建服务器定时备份失败", e);
            throw new BusinessException(DeskBackupBusinessKey.RCDC_SERVER_BACKUP_STRATEGY_CREATE_FAIL, e, request.getName(), e.getI18nMessage());
        } catch (AnnotationValidationException e) {
            auditLogAPI.recordLog(DeskBackupBusinessKey.RCDC_SERVER_BACKUP_STRATEGY_CREATE_FAIL_LOG, request.getName(), e.getMessage());
            LOGGER.error("创建服务器定时备份失败", e);
            throw new BusinessException(DeskBackupBusinessKey.RCDC_SERVER_BACKUP_STRATEGY_CREATE_FAIL, e, request.getName(), e.getMessage());
        }

        return DefaultWebResponse.Builder.success(UserBusinessKey.RCDC_RCO_MODULE_OPERATE_SUCCESS, new String[] {});
    }

    /**
     * @api {POST} /rco/serverbackup/strategy/edit 编辑备份策略
     * @apiName edit
     * @apiGroup strategy
     * @apiDescription 编辑备份策略
     * @apiParam (请求字段说明) {uuid} id
     * @apiParam (请求字段说明) {String} strategyName 备份名称
     * @apiParam (请求字段说明) {BackupType} backupType 备份对象
     * @apiParam (请求字段说明) {Boolean} strategyState 策略开启/关闭
     * @apiParam (请求字段说明) {TaskCycle} taskCycle 备份周期
     * @apiParam (请求字段说明) {String} scheduleTime 调度时间
     * @apiParam (请求字段说明) {String[]} dayOfWeekArr 一个星期的第几天
     * @apiParam (请求字段说明) {String} day 一个月的第几天
     * @apiParam (请求字段说明) {Long} durationSeconds 持续时间，单位是秒，需要以小时为单位设置
     * @apiParam (请求字段说明) {IdLabelEntry} externalStorageIdLabel 外置存储ID {"id":"","label":""}
     * @apiParam (请求字段说明) {int} maxBackUp 备份保留数量
     * @apiParamExample {json} 请求报文
     *
     *                  {
     *                  “id”:"",
     *                  "strategyName":"", // 备份名称
     *                  "backupType":"RCDC_BACKUP", // 备份对象
     *                  "strategyState":"true", // true=开启，false=关闭
     *                  "taskCycle":"MONTH", DAY、WEEK、MONTH
     *                  "scheduleTime":"",调度时间
     *                  "dayOfWeekArr":[], //一个星期的第几天
     *                  "day":"", //一个月的第几天
     *                  "durationSeconds":3600, // 单位是秒，需要以小时为单位设置
     *                  "externalStorageIdLabel": {"id":"","label":""}, // 外置存储信息
     *                  "maxBackUp": 3, // 备份保留个数
     *                  }
     *
     * @apiSuccess (响应字段说明){Object} content content
     * @apiSuccess (响应字段说明) {String} content.id 策略ID
     * @apiSuccess (响应字段说明) {String} message 提示信息
     * @apiSuccess (响应字段说明) {String[]} msgArgArr 国际化参数
     * @apiSuccess (响应字段说明) {String} msgKey 国际化key;
     * @apiSuccess (响应字段说明) {Status} status 状态，取值范围：SUCCESS,NO_LOGIN,NO_AUTHORITY,MAINTENANCE_MODE,ERROR;
     * @apiSuccessExample {json} 响应报文
     *                    {
     *                    "content": "8e776868-e8a6-4d5d-8f67-6217719fb11e",
     *                    "message": null,
     *                    "msgArgArr": null,
     *                    "msgKey": null,
     *                    "status": "SUCCESS"
     *                    }
     * @apiUse ErrorResponse
     */
    /**
     * 编辑定时备份策略
     *
     * @param request 定时任务参数
     * @return 响应报文
     * @throws BusinessException 业务异常
     */
    @ApiOperation(value = "编辑定时备份策略")
    @ApiVersions({@ApiVersion(value = Version.V6_0_50, descriptions = {"编辑定时备份策略"})})
    @RequestMapping(value = "/edit", method = RequestMethod.POST)
    @EnableAuthority
    public DefaultWebResponse scheduleEdit(BackupStrategyRequest request) throws BusinessException {
        Assert.notNull(request, "ServerScheduleBackupRequest can not be null");
        Assert.notNull(request.getId(), "id can not be null");

        RcoScheduleTaskRequest scheduleTaskRequest = new RcoScheduleTaskRequest();
        BeanUtils.copyProperties(request, scheduleTaskRequest);
        scheduleTaskRequest.setScheduleTypeCode(ScheduleConstants.DEFAULT_BACKUP_SCHEDULE_NAME);
        scheduleTaskRequest.setTaskName(ScheduleConstants.RCDC_BACKUP);

        try {
            // 校验策略是否存在
            cbbBackupStrategyAPI.findStrategyById(request.getId());
            // 定时任务参数校验
            scheduleValidation.scheduleTaskValidate(scheduleTaskRequest);
            // 转换cron
            String cronExpression = deskBackupAPI.generateExpression(scheduleTaskRequest);
            CbbBackupStrategyDTO strategyDTO = convertCbbBackupStrategyDTO(request, cronExpression);
            // 校验外置存储是否可用
            ExternalStorageDTO externalStorageDTO = checkExternalStorage(strategyDTO.getExternalStorageId());
            strategyDTO.setExternalStorageName(externalStorageDTO.getName());
            LOGGER.info("服务器定时备份，入参为:[{}]", JSONObject.toJSONString(strategyDTO));
            cbbBackupStrategyAPI.editStrategy(strategyDTO);
            auditLogAPI.recordLog(DeskBackupBusinessKey.RCDC_SERVER_BACKUP_STRATEGY_EDIT_SUCCESS_LOG, request.getName());
        } catch (BusinessException e) {
            LOGGER.error("编辑服务器定时备份失败", e);
            auditLogAPI.recordLog(DeskBackupBusinessKey.RCDC_SERVER_BACKUP_STRATEGY_EDIT_FAIL_LOG, request.getName(), e.getI18nMessage());
            throw new BusinessException(DeskBackupBusinessKey.RCDC_SERVER_BACKUP_STRATEGY_EDIT_FAIL, e, request.getName(), e.getI18nMessage());
        } catch (AnnotationValidationException e) {
            LOGGER.error("编辑服务器定时备份失败", e);
            auditLogAPI.recordLog(DeskBackupBusinessKey.RCDC_SERVER_BACKUP_STRATEGY_EDIT_FAIL_LOG, request.getName(), e.getMessage());
            throw new BusinessException(DeskBackupBusinessKey.RCDC_SERVER_BACKUP_STRATEGY_EDIT_FAIL, e, request.getName(), e.getMessage());
        }

        return DefaultWebResponse.Builder.success(UserBusinessKey.RCDC_RCO_MODULE_OPERATE_SUCCESS, new String[] {});
    }

    private CbbBackupStrategyDTO convertCbbBackupStrategyDTO(BackupStrategyRequest request, String cronExpression) {
        Assert.notNull(request, "request can not be null");
        Assert.notNull(cronExpression, "cronExpression can not be null");

        CbbBackupStrategyDTO backupStrategyDTO = new CbbBackupStrategyDTO();
        BeanUtils.copyProperties(request, backupStrategyDTO);
        QuartzTaskState quartzTaskState = request.getStrategyState() ? QuartzTaskState.START : QuartzTaskState.STOP;
        backupStrategyDTO.setQuartzTaskState(quartzTaskState);
        backupStrategyDTO.setCronExpression(cronExpression);
        backupStrategyDTO.setExternalStorageId(request.getExternalStorageIdLabel().getId());
        backupStrategyDTO.setExternalStorageName(request.getExternalStorageIdLabel().getLabel());
        return backupStrategyDTO;
    }

    /**
     * 校验外置存储是否可用
     *
     * @param externalStorageId 外置存储ID
     * @return ExternalStorageDTO
     * @throws BusinessException 业务异常
     */
    private ExternalStorageDTO checkExternalStorage(UUID externalStorageId) throws BusinessException {
        QueryExternalStorageRequest request = new QueryExternalStorageRequest();
        request.setExternalStorageId(externalStorageId);
        ExternalStorageDTO externalStorageDTO = externalStorageMgmtAPI.findExternalStorageInfoById(request);
        if (externalStorageDTO == null) {
            LOGGER.error("外置存储ID {} 不存在", externalStorageId);
            throw new BusinessException(DeskBackupBusinessKey.RCDC_SERVER_BACKUP_EXT_STORAGE_UNAVAILABLE_FAIL_RESULT,
                    String.valueOf(externalStorageId));
        }

        if (externalStorageDTO.getState() != ExternalStorageHealthStateEnum.HEALTHY) {
            LOGGER.info("根据外置ID[{}]查询外置存储信息[{}]", externalStorageId, JSONObject.toJSONString(externalStorageDTO));
            throw new BusinessException(DeskBackupBusinessKey.RCDC_SERVER_BACKUP_EXT_STORAGE_UNAVAILABLE_FAIL_RESULT,
                    String.valueOf(externalStorageDTO.getName()));
        }

        return externalStorageDTO;
    }

    /**
     * @api {POST} /rco/serverbackup/strategy/detail 获取服务器定时备份策略详情
     * @apiName detail
     * @apiGroup strategy
     * @apiDescription 获取服务器定时备份策略详情
     * @apiParamExample {json} 请求报文
     *
     * @apiSuccess (响应字段说明){Object} content content
     * @apiSuccess (响应字段说明) {String} content.id 策略ID
     * @apiSuccess (响应字段说明) {String} message 提示信息
     * @apiSuccess (响应字段说明) {String[]} msgArgArr 国际化参数
     * @apiSuccess (响应字段说明) {String} msgKey 国际化key;
     * @apiSuccess (响应字段说明) {Status} status 状态，取值范围：SUCCESS,NO_LOGIN,NO_AUTHORITY,MAINTENANCE_MODE,ERROR;
     * @apiSuccessExample {json} 响应报文
     *                    {
     *                    "content": {
     *                    "exists":"true",
     *                    "backupStrategyDetailVO":{
     *                    "id":"",
     *                    "strategyState": true,
     *                    "taskCycle": DAY,
     *                    "scheduleTime": "12:00:00",
     *                    "dayOfWeekArr": [1,2,3],
     *                    "day": 1,
     *                    "durationSeconds": 3600,
     *                    "externalStorageIdLabel": {
     *                    "id":""
     *                    "label":""
     *                    },
     *                    "maxBackUp":10,
     *                    "mergePeriod":3,
     *
     *                    }
     *                    },
     *                    "message": null,
     *                    "msgArgArr": null,
     *                    "msgKey": null,
     *                    "status": "SUCCESS"
     *                    }
     * @apiUse ErrorResponse
     */
    /**
     * 获取服务器定时备份策略详情
     *
     * @param request request
     * @return 响应信息
     * @throws BusinessException ex
     */
    @ApiOperation(value = "获取服务器定时备份策略详情")
    @ApiVersions({@ApiVersion(value = Version.V6_0_50, descriptions = {"获取服务器定时备份策略详情"})})
    @RequestMapping(value = "/detail", method = RequestMethod.POST)
    public DefaultWebResponse detail(IdWebRequest request) throws BusinessException {
        Assert.notNull(request, "idWebRequest can not be null");

        BackupStrategyDetailVO voResponse;
        try {
            CbbBackupStrategyDTO cbbBackupStrategyDTO = cbbBackupStrategyAPI.findStrategyById(request.getId());
            LOGGER.info("定时任务详情", JSONObject.toJSONString(cbbBackupStrategyDTO));
            voResponse = convertFromCbbBackupStrategyDTO(cbbBackupStrategyDTO);

            ExternalStorageDTO externalStorageDTO = this.checkAndGetExternalStorage(cbbBackupStrategyDTO);
            if (externalStorageDTO == null) {
                LOGGER.info("外置存储 [{}] 不存在", cbbBackupStrategyDTO.getExternalStorageName());
                voResponse.setExternalStorageIdLabel(null);
            } else {
                IdLabelEntry idLabelEntry = new IdLabelEntry();
                idLabelEntry.setId(externalStorageDTO.getExtStorageId());
                idLabelEntry.setLabel(externalStorageDTO.getName());
                voResponse.setExternalStorageIdLabel(idLabelEntry);
            }
        } catch (BusinessException e) {
            LOGGER.error("获取定时任务详情失败", e);
            throw new BusinessException(DeskBackupBusinessKey.RCDC_SERVER_BACKUP_STRATEGY_DETAIL_FAIL, e, String.valueOf(request.getId()),
                    e.getI18nMessage());
        }
        return DefaultWebResponse.Builder.success(voResponse);
    }

    /**
     * 分页查询 备份策略 列表
     *
     * @param webRequest webRequest
     * @return 分页响应结果
     * @throws BusinessException ex
     */
    @RequestMapping(value = "list")
    public DefaultWebResponse list(PageQueryRequest webRequest) throws BusinessException {
        Assert.notNull(webRequest, "webRequest must not be null");

        PageQueryResponse<CbbBackupStrategyDTO> dtoResponse = cbbBackupStrategyAPI.pageQuery(webRequest);

        if (ArrayUtils.isEmpty(dtoResponse.getItemArr())) {
            return DefaultWebResponse.Builder.success(dtoResponse);
        }

        BackupStrategyDetailVO[] backupStrategyDetailVOArr = new BackupStrategyDetailVO[dtoResponse.getItemArr().length];
        for (int i = 0; i < dtoResponse.getItemArr().length; i++) {
            BackupStrategyDetailVO backupStrategyDetailVO = convertFromCbbBackupStrategyDTO(dtoResponse.getItemArr()[i]);
            backupStrategyDetailVOArr[i] = backupStrategyDetailVO;
        }

        PageQueryResponse<BackupStrategyDetailVO> pageResponse = new PageQueryResponse<>();
        pageResponse.setTotal(dtoResponse.getTotal());
        pageResponse.setItemArr(backupStrategyDetailVOArr);

        return DefaultWebResponse.Builder.success(pageResponse);
    }

    private BackupStrategyDetailVO convertFromCbbBackupStrategyDTO(CbbBackupStrategyDTO backupStrategyDTO) throws BusinessException {
        BackupStrategyDetailVO strategyDetailVO = new BackupStrategyDetailVO();

        BeanUtils.copyProperties(backupStrategyDTO, strategyDetailVO);

        IdLabelEntry idLabelEntry = new IdLabelEntry();
        idLabelEntry.setId(backupStrategyDTO.getExternalStorageId());
        idLabelEntry.setLabel(backupStrategyDTO.getExternalStorageName());
        strategyDetailVO.setExternalStorageIdLabel(idLabelEntry);

        strategyDetailVO.setDurationSeconds(backupStrategyDTO.getDurationSeconds());
        strategyDetailVO.setMaxBackup(backupStrategyDTO.getMaxBackup());

        Boolean enableStrategy = QuartzTaskState.START.equals(backupStrategyDTO.getQuartzTaskState());
        strategyDetailVO.setStrategyState(enableStrategy);

        // 转换cron值
        CronConvertDTO cronConvertDTO = deskBackupAPI.parseCronExpression(backupStrategyDTO.getCronExpression());
        BeanUtils.copyProperties(cronConvertDTO, strategyDetailVO);
        return strategyDetailVO;
    }

    private ExternalStorageDTO checkAndGetExternalStorage(CbbBackupStrategyDTO cbbBackupStrategyDTO) throws BusinessException {
        QueryExternalStorageRequest request = new QueryExternalStorageRequest();
        UUID externalStorageId = cbbBackupStrategyDTO.getExternalStorageId();
        request.setExternalStorageId(externalStorageId);
        ExternalStorageDTO externalStorageDTO;
        try {
            externalStorageDTO = externalStorageMgmtAPI.findExternalStorageInfoById(request);
        } catch (Exception e) {
            LOGGER.error("外置存储：[{}] 信息查询失败", cbbBackupStrategyDTO.getExternalStorageName());
            // 外置存储查询失败视为不存在，返回空
            return null;
        }

        if (externalStorageDTO == null) {
            LOGGER.error("外置存储：[{}] 已删除", cbbBackupStrategyDTO.getExternalStorageName());
            // 外置存储被删除时返回空
            return null;
        }

        return externalStorageDTO;
    }

    /**
     * 删除备份策略
     *
     * @param request request
     * @return 分页响应结果
     * @throws BusinessException ex
     */
    @ApiOperation(value = "删除备份策略")
    @ApiVersions({@ApiVersion(value = Version.V6_0_50, descriptions = {"删除备份策略"})})
    @RequestMapping(value = "/delete", method = RequestMethod.POST)
    @EnableAuthority
    public DefaultWebResponse scheduleDelete(IdArrWebRequest request) throws BusinessException {
        Assert.notNull(request, "request must not be null");

        UUID[] idArr = request.getIdArr();
        for (UUID strategyId : idArr) {
            deleteOne(strategyId);
        }

        return DefaultWebResponse.Builder.success(UserBusinessKey.RCDC_RCO_MODULE_OPERATE_SUCCESS, new String[] {});
    }

    private void deleteOne(UUID strategyId) throws BusinessException {
        String name = this.getStrategyNameElseId(strategyId);
        try {
            cbbBackupStrategyAPI.deleteBackupStrategy(strategyId);
            auditLogAPI.recordLog(DeskBackupBusinessKey.RCDC_SERVER_BACKUP_STRATEGY_DELETE_SUCCESS_LOG, name);
        } catch (BusinessException e) {
            auditLogAPI.recordLog(DeskBackupBusinessKey.RCDC_SERVER_BACKUP_STRATEGY_DELETE_FAIL_LOG, name, e.getI18nMessage());
            LOGGER.error("删除服务器定时备份失败", e);
            throw new BusinessException(DeskBackupBusinessKey.RCDC_SERVER_BACKUP_STRATEGY_DELETE_FAIL, e, name, e.getI18nMessage());
        }
    }

    private String getStrategyNameElseId(UUID id) {
        try {
            CbbBackupStrategyDTO strategyDTO = cbbBackupStrategyAPI.findStrategyById(id);
            return strategyDTO.getName();
        } catch (Exception ex) {
            return id.toString();
        }
    }

    /**
     * 服务器立即备份
     *
     * @param request 请求参数
     * @return 响应
     * @throws BusinessException 业务异常
     */
    @ApiOperation(value = "服务器立即备份")
    @ApiVersions({@ApiVersion(value = Version.V6_0_50, descriptions = {"服务器立即备份"})})
    @RequestMapping(value = "/start", method = RequestMethod.POST)
    public DefaultWebResponse start(IdWebRequest request) throws BusinessException {
        Assert.notNull(request, "request must not be null");

        UUID strategyId = request.getId();
        CbbBackupStrategyDTO strategyDTO = cbbBackupStrategyAPI.findStrategyById(strategyId);

        try {
            CbbBackupDataDTO backupDataDTO = this.buildCbbBackupDataDTO(strategyDTO);
            LOGGER.info("服务器立即备份，入参为:[{}]", JSONObject.toJSONString(backupDataDTO));
            cbbBackupAPI.executeBackup(backupDataDTO);
            auditLogAPI.recordLog(DeskBackupBusinessKey.RCDC_SERVER_BACKUP_CREATE_IMMEDIATELY_SUCCESS_LOG, strategyDTO.getName());
        } catch (BusinessException e) {
            LOGGER.error("创建服务器立即备份失败", e);
            auditLogAPI.recordLog(DeskBackupBusinessKey.RCDC_SERVER_BACKUP_CREATE_IMMEDIATELY_FAIL_LOG, e.getI18nMessage());
            throw new BusinessException(DeskBackupBusinessKey.RCDC_SERVER_BACKUP_IMMEDIATELY_FAIL_RESULT, e, e.getI18nMessage());
        }

        return DefaultWebResponse.Builder.success(UserBusinessKey.RCDC_RCO_MODULE_OPERATE_SUCCESS, new String[] {});
    }

    private CbbBackupDataDTO buildCbbBackupDataDTO(CbbBackupStrategyDTO strategyDTO) {
        CbbBackupDataDTO backupDataDTO = new CbbBackupDataDTO();
        BeanUtils.copyProperties(strategyDTO, backupDataDTO);
        backupDataDTO.setStrategyId(strategyDTO.getId());
        backupDataDTO.setMaxBackupDuration(strategyDTO.getDurationSeconds());
        return backupDataDTO;
    }
}
