package com.ruijie.rcos.rcdc.rco.module.web.ctrl.terminal;

import com.google.common.collect.ImmutableMap;
import com.ruijie.rcos.gss.base.iac.module.annotation.EnableAuthority;
import com.ruijie.rcos.gss.base.iac.module.annotation.NoAuthUrl;
import com.ruijie.rcos.gss.log.module.def.api.BaseAuditLogAPI;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.BusinessKey;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.api.CbbClusterServerMgmtAPI;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.api.CbbDeskMgmtAPI;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.api.CbbGuestToolLogAPI;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.dto.CbbClusterVirtualIpDTO;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.dto.CbbDeskDTO;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.dto.CbbGtLogCollectStateDTO;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.enums.CbbDesktopSessionType;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.enums.CbbHostOsType;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.enums.GtLogCollectState;
import com.ruijie.rcos.rcdc.rco.module.def.api.ComputerBusinessAPI;
import com.ruijie.rcos.rcdc.rco.module.def.api.DesktopPoolMgmtAPI;
import com.ruijie.rcos.rcdc.rco.module.def.api.UserDesktopMgmtAPI;
import com.ruijie.rcos.rcdc.rco.module.def.api.dto.ComputerDTO;
import com.ruijie.rcos.rcdc.rco.module.def.api.dto.ExportComputerFileInfoDTO;
import com.ruijie.rcos.rcdc.rco.module.def.api.dto.MatchEqual;
import com.ruijie.rcos.rcdc.rco.module.def.api.dto.RcoUserDesktopDTO;
import com.ruijie.rcos.rcdc.rco.module.def.api.enums.ComputerTypeEnum;
import com.ruijie.rcos.rcdc.rco.module.def.api.request.*;
import com.ruijie.rcos.rcdc.rco.module.def.api.response.ComputerInfoResponse;
import com.ruijie.rcos.rcdc.rco.module.def.api.response.GetExportCloudDesktopFileResponse;
import com.ruijie.rcos.rcdc.rco.module.def.desktoppool.dto.DesktopPoolDetailDTO;
import com.ruijie.rcos.rcdc.rco.module.def.utils.PermissionHelper;
import com.ruijie.rcos.rcdc.rco.module.web.config.annotation.ApiVersion;
import com.ruijie.rcos.rcdc.rco.module.web.config.annotation.ApiVersions;
import com.ruijie.rcos.rcdc.rco.module.web.config.annotation.Version;
import com.ruijie.rcos.rcdc.rco.module.web.ctrl.common.utils.TerminalIdMappingUtils;
import com.ruijie.rcos.rcdc.rco.module.web.ctrl.terminal.batchtask.CloseComputerBatchTaskHandler;
import com.ruijie.rcos.rcdc.rco.module.web.ctrl.terminal.batchtask.DeleteComputerBatchTaskHandler;
import com.ruijie.rcos.rcdc.rco.module.web.ctrl.terminal.batchtask.RelieveFaultComputerBatchTaskHandler;
import com.ruijie.rcos.rcdc.rco.module.web.ctrl.terminal.batchtask.WakeUpComputerHandler;
import com.ruijie.rcos.rcdc.rco.module.web.ctrl.terminal.request.*;
import com.ruijie.rcos.rcdc.rco.module.web.ctrl.terminal.validation.ComputerValidation;
import com.ruijie.rcos.rcdc.rco.module.web.ctrl.terminal.vo.ComputerDetailVO;
import com.ruijie.rcos.rcdc.rco.module.web.ctrl.user.UserBusinessKey;
import com.ruijie.rcos.rcdc.rco.module.web.ctrl.user.request.EmptyDownloadWebRequest;
import com.ruijie.rcos.rcdc.rco.module.web.response.CommonWebResponse;
import com.ruijie.rcos.rcdc.terminal.module.def.api.CbbTerminalGroupMgmtAPI;
import com.ruijie.rcos.rcdc.terminal.module.def.api.CbbTerminalOperatorAPI;
import com.ruijie.rcos.rcdc.terminal.module.def.api.dto.CbbTerminalGroupDetailDTO;
import com.ruijie.rcos.sk.base.batch.BatchTaskBuilder;
import com.ruijie.rcos.sk.base.batch.BatchTaskSubmitResult;
import com.ruijie.rcos.sk.base.batch.DefaultBatchTaskItem;
import com.ruijie.rcos.sk.base.exception.BusinessException;
import com.ruijie.rcos.sk.base.i18n.LocaleI18nResolver;
import com.ruijie.rcos.sk.base.log.Logger;
import com.ruijie.rcos.sk.base.log.LoggerFactory;
import com.ruijie.rcos.sk.base.validation.EnableCustomValidate;
import com.ruijie.rcos.sk.modulekit.api.comm.DefaultPageResponse;
import com.ruijie.rcos.sk.modulekit.api.comm.DefaultRequest;
import com.ruijie.rcos.sk.webmvc.api.request.IdWebRequest;
import com.ruijie.rcos.sk.webmvc.api.request.PageWebRequest;
import com.ruijie.rcos.sk.webmvc.api.response.DefaultWebResponse;
import com.ruijie.rcos.sk.webmvc.api.response.DownloadWebResponse;
import com.ruijie.rcos.sk.webmvc.api.session.SessionContext;
import com.ruijie.rcos.sk.webmvc.api.vo.ExactMatch;
import com.ruijie.rcos.sk.webmvc.api.vo.IdLabelEntry;
import com.ruijie.rcos.sk.webmvc.api.vo.Sort;
import com.ruijie.rcos.sk.webmvc.api.vo.Sort.Direction;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Stream;

/**
 * Description: PC管理
 * Copyright: Copyright (c) 2019
 * Company: Ruijie Co., Ltd.
 * Create Time: 2020/1/6 11:34
 *
 * @author ketb
 */
@Api(tags = "PC终端")
@Controller
@RequestMapping("/rco/computer")
@EnableCustomValidate(validateClass = ComputerValidation.class)
public class ComputerController {

    private static final Logger LOGGER = LoggerFactory.getLogger(ComputerController.class);

    @Autowired
    private ComputerBusinessAPI computerBusinessAPI;

    @Autowired
    private CbbTerminalOperatorAPI terminalOperatorAPI;

    @Autowired
    private CbbClusterServerMgmtAPI clusterServerMgmtAPI;

    @Autowired
    private PermissionHelper permissionHelper;

    @Autowired
    private CbbTerminalGroupMgmtAPI terminalGroupMgmtAPI;

    @Autowired
    private BaseAuditLogAPI auditLogAPI;

    @Autowired
    private DesktopPoolMgmtAPI desktopPoolMgmtAPI;

    @Autowired
    private UserDesktopMgmtAPI userDesktopMgmtAPI;

    @Autowired
    private CbbDeskMgmtAPI cbbDeskMgmtAPI;

    @Autowired
    private CbbGuestToolLogAPI cbbGuestToolLogAPI;




    /**
     * @api {POST} /rco/computer/list 获取PC终端列表
     * @apiName 获取PC终端列表
     * @apiGroup PC纳管
     * @apiDescription 获取终端列表
     *
     * @apiParam (请求体字段说明) {Number} [page] 页数 0表示首页
     * @apiParam (请求体字段说明) {Number} [limit] 每页查询数量
     * @apiParam (请求体字段说明) {String} searchKeyword 搜索内容
     * @apiParam (请求体字段说明) {Boolean} autoRefresh 搜索内容
     * @apiParam (请求体字段说明) {JSON[]} exactMatchArr 精确匹配体
     * @apiParam (请求体字段说明) {String} exactMatchArr.name 字段名称
     * @apiParam (请求体字段说明) {String[]} exactMatchArr.valueArr 匹配值
     * @apiParam (请求体字段说明) {JSON} sort 排序体
     * @apiParam (请求体字段说明) {String} sort.sortField 排序字段
     * @apiParam (请求体字段说明) {String = "ASC"、"DESC"} sort.direction 排序类型
     *
     * @apiParamExample {json} 请求体示例
     * {
     *      "page":0,
     *      "limit":20,
     *      "searchKeyword": "R",
     *      "autoRefresh"：true，
     *      "exactMatchArr": [{
     *          "name": "terminalGroupId",
     *          "valueArr": ["91877313-7aaa-4965-a5d6-0efbc90784ed"]
     *      }],
     *      "sort": {
     *          "sortField": "name",
     *          "direction": "ASC"
     *      }
     *  }
     *
     * @apiSuccess (响应字段说明) {JSON} content 响应数据
     * @apiSuccess (响应字段说明) {Number} content.total 总条数
     * @apiSuccess (响应字段说明) {JSON[]} content.itemArr 列表数据
     * @apiSuccess (响应字段说明) {JSON} content.itemArr[0] 每列数据
     * @apiSuccess (响应字段说明) {String} content.itemArr[0].id
     * @apiSuccess (响应字段说明) {Number} content.itemArr[0].alias 备注
     * @apiSuccess (响应字段说明) {String} content.itemArr[0].cpu
     * @apiSuccess (响应字段说明) {Number} content.itemArr[0].createTime 创建时间
     * @apiSuccess (响应字段说明) {String} content.itemArr[0].disk 硬盘
     * @apiSuccess (响应字段说明) {String} content.itemArr[0].faultDescription 故障信息
     * @apiSuccess (响应字段说明) {boolean} content.itemArr[0].faultState 故障状态
     * @apiSuccess (响应字段说明) {String} content.itemArr[0].ip
     * @apiSuccess (响应字段说明) {String} content.itemArr[0].mac
     * @apiSuccess (响应字段说明) {String} content.itemArr[0].memory 内存
     * @apiSuccess (响应字段说明) {String} content.itemArr[0].name 计算机名
     * @apiSuccess (响应字段说明) {String} content.itemArr[0].os 操作系统
     * @apiSuccess (响应字段说明) {String} content.itemArr[0].serverIp 服务器ip
     * @apiSuccess (响应字段说明) {String = "ONLINE","OFFLINE"} content.itemArr[0].state PC状态
     * @apiSuccess (响应字段说明) {Boolean} content.itemArr[0].faultState PC报障状态
     * @apiSuccess (响应字段说明) {Number} content.itemArr[0].faultTime 报障时间
     * @apiSuccess (响应字段说明) {String} content.itemArr[0].terminalGroupId PC所属组
     * @apiSuccess (响应字段说明) {String} content.itemArr[0].terminalGroupName PC所属组名称
     * @apiSuccess (响应字段说明) {String} message
     * @apiSuccess (响应字段说明) {String[]} msgArgArr
     * @apiSuccess (响应字段说明) {String} msgKey
     * @apiSuccess (响应字段说明) {String} status 响应状态
     *
     * @apiSuccessExample {json} 成功响应
     * {
     *      "content":{
     *          "total":1,
     *          "itemArr":[{
     *              "id":"a257cf6f-2589-45f0-aa20-8ada46483091",
     *              "alias":"ljm",
     *              "cpu":"Intel(R) Core(TM) i5-6500 CPU @ 3.20GHz",
     *              "createTime":1583824306932,
     *              "disk":"930.52GB",
     *              "faultDescription":"",
     *              "faultState":null,
     *              "ip":"172.20.114.82",
     *              "mac":"A0:8C:FD:F3:D2:34",
     *              "memory":"7.87GB（可用）",
     *              "name":"R09681",
     *              "os":"Windows 10",
     *              "serverIp":null,
     *              "state":"ONLINE",
     *              "faultState":true,
     *              "faultTime": 1591703159064,
     *              "terminalGroupId":"91877313-7aaa-4965-a5d6-0efbc90784ed",
     *              "terminalGroupName":"软终端1组"
     *          }]
     *      },
     *      "message":null,
     *      "msgArgArr":null,
     *      "msgKey":null,
     *      "status":"SUCCESS"
     * }
     *
     * @apiErrorExample {json} 异常响应
     * {
     *     "content": null,
     *     "message": "系统内部错误，请联系管理员",
     *     "msgArgArr": [],
     *     "msgKey": "sk_webmvckit_internal_error",
     *     "status": "ERROR"
     * }
     *
     */

    /**
     * 获取终端信息
     *
     * @param request        请求参数
     * @param sessionContext session信息
     * @return 终端分页列表
     * @throws BusinessException 业务异常
     */
    @RequestMapping(value = "/list")
    public DefaultWebResponse list(PageWebRequest request, SessionContext sessionContext) throws BusinessException {
        Assert.notNull(request, "request can not be null");
        Assert.notNull(sessionContext, "sessionContext can not be null");

        PageSearchRequest pageReq = buildPageSearchRequest(request);

        Boolean isPermissionLimit = checkPermission(request, sessionContext, pageReq);
        if (Boolean.FALSE.equals(isPermissionLimit)) {
            return DefaultWebResponse.Builder.success(ImmutableMap.of("itemArr", Collections.emptyList()));

        }

        DefaultPageResponse<ComputerDTO> response = computerBusinessAPI.pageQuery(pageReq);

        // 构建是否有桌面信息
        buildHasDesk(response);

        return DefaultWebResponse.Builder.success(response);
    }

    private void buildHasDesk(DefaultPageResponse<ComputerDTO> response) {
        for (ComputerDTO computerDTO : response.getItemArr()) {

            try {
                RcoUserDesktopDTO userDesktopDTO = userDesktopMgmtAPI.findByDesktopId(computerDTO.getId());
                computerDTO.setHasDesk(userDesktopDTO != null);
            } catch (BusinessException ex) {
                computerDTO.setHasDesk(Boolean.FALSE);
                if (LOGGER.isDebugEnabled()) {
                    LOGGER.debug("获取云桌面[{}]信息异常,该日志可以忽略", computerDTO.getId());
                }
            }
        }
    }


    /**
     * 批量关闭pc
     *
     * @param request 请求的pc记录id参数
     * @param builder 批量任务创建对象
     * @return 返回成功或失败
     * @throws BusinessException 业务异常
     */
    @RequestMapping(value = "/batchShutdown")
    @ApiOperation("关机")
    @ApiVersions({@ApiVersion(value = Version.V3_1_1, descriptions = {"分级分权-区分权限，关机"})})
    @EnableAuthority
    public DefaultWebResponse batchShutdown(ComputerIdArrWebRequest request, BatchTaskBuilder builder) throws BusinessException {
        Assert.notNull(request, "request must not be null");
        Assert.notNull(builder, "builder must not be null");

        String[] computerIdArr = request.getIdArr();
        Map<UUID, String> idMap = TerminalIdMappingUtils.mapping(computerIdArr);
        UUID[] idArr = TerminalIdMappingUtils.extractUUID(idMap);
        final Iterator<DefaultBatchTaskItem> iterator = Stream.of(idArr).distinct()
                .map(id -> DefaultBatchTaskItem.builder().itemId(id)
                        .itemName(ComputerBusinessKey.RCDC_RCO_COMPUTER_CLOSE_ITEM_NAME, new String[]{}).build())
                .iterator();
        final CloseComputerBatchTaskHandler handler =
                new CloseComputerBatchTaskHandler(idMap, iterator, auditLogAPI);
        handler.setComputerBusinessAPI(computerBusinessAPI);
        handler.setTerminalOperatorAPI(terminalOperatorAPI);

        BatchTaskSubmitResult result = executeShutdownBatchTask(builder, computerIdArr, handler);
        return DefaultWebResponse.Builder.success(result);
    }

    private BatchTaskSubmitResult executeShutdownBatchTask(BatchTaskBuilder builder, String[] computerIdArr,
                                                           final CloseComputerBatchTaskHandler handler) throws BusinessException {
        BatchTaskSubmitResult result;
        if (computerIdArr.length == 1) {
            ComputerIdRequest request = new ComputerIdRequest(UUID.fromString(computerIdArr[0]));
            ComputerInfoResponse response = computerBusinessAPI.getComputerInfoByComputerId(request);
            result = builder.setTaskName(ComputerBusinessKey.RCDC_RCO_COMPUTER_CLOSE_SINGLE_TASK_NAME)
                    .setTaskDesc(ComputerBusinessKey.RCDC_RCO_COMPUTER_CLOSE_SINGLE_TASK_DESC, response.getMac().toUpperCase())
                    .registerHandler(handler).start();
        } else {
            result = builder.setTaskName(ComputerBusinessKey.RCDC_RCO_COMPUTER_CLOSE_TASK_NAME)
                    .setTaskDesc(ComputerBusinessKey.RCDC_RCO_COMPUTER_CLOSE_TASK_DESC).enableParallel()
                    .registerHandler(handler).start();
        }
        return result;
    }


    /**
     * @api {POST} /rco/computer/relieveFault 取消PC报障
     * @apiName 取消PC报障
     * @apiGroup PC纳管
     * @apiDescription 取消PC报障
     *
     * @apiParam (请求体字段说明) {String[]} idArr PC终端Id数组
     *
     * @apiParamExample {json} 请求体示例
     * {
     *      "idArr": ["a257cf6f-2589-45f0-aa20-8ada46483091"]
     * }
     *
     * @apiSuccess (响应字段说明) {JSON} content 响应数据
     * @apiSuccess (响应字段说明) {String} message
     * @apiSuccess (响应字段说明) {String[]} msgArgArr
     * @apiSuccess (响应字段说明) {String} msgKey
     * @apiSuccess (响应字段说明) {String} status 响应状态
     *
     * @apiSuccessExample {json} 成功响应
     * {
     *      "content":null,
     *      "message":"取消pc报障成功",
     *      "msgArgArr":[],
     *      "msgKey":"rcdc_rco_computer_relieve_fault_success",
     *      "status":"SUCCESS"
     * }
     *
     * @apiErrorExample {json} 异常响应
     * {
     *     "content": null,
     *     "message": "系统内部错误，请联系管理员",
     *     "msgArgArr": [],
     *     "msgKey": "sk_webmvckit_internal_error",
     *     "status": "ERROR"
     * }
     *
     */

    /**
     * 解除故障
     *
     * @param request 请求的pcid参数
     * @param builder 批量任务创建对象
     * @return 返回成功或失败
     * @throws BusinessException 业务异常
     */
    @ApiOperation("取消报障")
    @ApiVersions({@ApiVersion(value = Version.V1_0_0), @ApiVersion(value = Version.V3_1_1, descriptions = {"分级分权-区分权限，加入权限控制"})})
    @RequestMapping(value = "/relieveFault")
    @EnableAuthority
    public DefaultWebResponse relieveFault(RelieveFaultWebRequest request, BatchTaskBuilder builder) throws BusinessException {
        Assert.notNull(request, "request must not be null");
        Assert.notNull(builder, "builder must not be null");

        String[] computerIdArr = request.getIdArr();
        Map<UUID, String> idMap = TerminalIdMappingUtils.mapping(computerIdArr);
        UUID[] idArr = TerminalIdMappingUtils.extractUUID(idMap);
        final Iterator<DefaultBatchTaskItem> iterator =
                Stream.of(idArr).distinct().map(id -> DefaultBatchTaskItem.builder().itemId(id)
                                .itemName(ComputerBusinessKey.RCDC_RCO_COMPUTER_RELIEVE_FAULT, new String[]{}).build())
                        .iterator();
        final RelieveFaultComputerBatchTaskHandler handler = new RelieveFaultComputerBatchTaskHandler(idMap, iterator,
                auditLogAPI);
        handler.setComputerBusinessAPI(computerBusinessAPI);

        BatchTaskSubmitResult result = relieveFaultForPC(builder, computerIdArr, handler);
        return DefaultWebResponse.Builder.success(result);
    }

    private BatchTaskSubmitResult relieveFaultForPC(BatchTaskBuilder builder, String[] computerIdArr,
                                                    final RelieveFaultComputerBatchTaskHandler handler) throws BusinessException {
        BatchTaskSubmitResult result;
        if (computerIdArr.length == 1) {
            handler.setIsBatch(false);
            ComputerIdRequest request = new ComputerIdRequest(UUID.fromString(computerIdArr[0]));
            ComputerInfoResponse response = computerBusinessAPI.getComputerInfoByComputerId(request);
            handler.setPcName(response.getMac());
            result = builder.setTaskName(ComputerBusinessKey.RCDC_RCO_COMPUTER_RELIEVE_FAULT_TASK_NAME)
                    .setTaskDesc(ComputerBusinessKey.RCDC_RCO_COMPUTER_RELIEVE_FAULT_ONE_TASK_DESC, response.getMac())
                    .registerHandler(handler).start();
        } else {
            result = builder.setTaskName(ComputerBusinessKey.RCDC_RCO_COMPUTER_RELIEVE_FAULT_TASK_NAME)
                    .setTaskDesc(ComputerBusinessKey.RCDC_RCO_COMPUTER_RELIEVE_FAULT_TASK_DESC).enableParallel()
                    .registerHandler(handler).start();
        }
        return result;
    }

    /**
     * @api {POST} /rco/computer/getInfo 获取pc详细信息
     * @apiName 获取pc详细信息
     * @apiGroup PC纳管
     * @apiDescription 获取pc详细信息
     *
     * @apiParam (请求体字段说明) {String} id PC终端Id
     *
     * @apiParamExample {json} 请求体示例
     * {
     *      "id": "a257cf6f-2589-45f0-aa20-8ada46483091"
     * }
     *
     * @apiSuccess (响应字段说明) {JSON} content 响应数据
     * @apiSuccess (响应字段说明) {String} content.id
     * @apiSuccess (响应字段说明) {Number} content.alias 备注
     * @apiSuccess (响应字段说明) {String} content.cpu
     * @apiSuccess (响应字段说明) {Number} content.createTime 创建时间
     * @apiSuccess (响应字段说明) {String} content.disk 硬盘
     * @apiSuccess (响应字段说明) {String} content.faultDescription 故障信息
     * @apiSuccess (响应字段说明) {boolean} content.faultState 故障状态
     * @apiSuccess (响应字段说明) {String} content.ip
     * @apiSuccess (响应字段说明) {String} content.mac
     * @apiSuccess (响应字段说明) {String} content.memory 内存
     * @apiSuccess (响应字段说明) {String} content.name 计算机名
     * @apiSuccess (响应字段说明) {String} content.os 操作系统
     * @apiSuccess (响应字段说明) {String} content.serverIp 服务器ip
     * @apiSuccess (响应字段说明) {String = "ONLINE","OFFLINE","FAULT"} content.state PC状态
     * @apiSuccess (响应字段说明) {String} content.terminalGroupId PC所属组
     * @apiSuccess (响应字段说明) {String} content.terminalGroupName PC所属组名称
     * @apiSuccess (响应字段说明) {String} message
     * @apiSuccess (响应字段说明) {String[]} msgArgArr
     * @apiSuccess (响应字段说明) {String} msgKey
     * @apiSuccess (响应字段说明) {String} status 响应状态
     *
     * @apiSuccessExample {json} 成功响应
     * {
     *      "content":{
     *          "id":"a257cf6f-2589-45f0-aa20-8ada46483091",
     *          "alias":"ljm",
     *          "cpu":"Intel(R) Core(TM) i5-6500 CPU @ 3.20GHz",
     *          "createTime":1583824306932,
     *          "disk":"930.52GB",
     *          "faultDescription":"",
     *          "faultState":null,
     *          "ip":"172.20.114.82",
     *          "mac":"A0:8C:FD:F3:D2:34",
     *          "memory":"7.87GB（可用）",
     *          "name":"R09681",
     *          "os":"Windows 10",
     *          "serverIp":"172.28.84.107",
     *          "state":"ONLINE",
     *          "terminalGroupId":null,
     *          "terminalGroupName":null
     *      },
     *      "message":null,
     *      "msgArgArr":null,
     *      "msgKey":null,
     *      "status":"SUCCESS"
     * }
     *
     * @apiErrorExample {json} 异常响应
     * {
     *     "content": null,
     *     "message": "系统内部错误，请联系管理员",
     *     "msgArgArr": [],
     *     "msgKey": "sk_webmvckit_internal_error",
     *     "status": "ERROR"
     * }
     *
     */

    /**
     * 获取pc详细信息
     *
     * @param request 请求参数
     * @return pc详细信息
     * @throws BusinessException 业务异常
     */
    @RequestMapping(value = "/getInfo")
    public DefaultWebResponse getInfo(ComputerIdWebRequest request) throws BusinessException {
        Assert.notNull(request, "request can not be null");
        Assert.notNull(request.getId(), "request id must not be null");

        ComputerIdRequest idRequest = new ComputerIdRequest(request.getId());
        ComputerInfoResponse resp = computerBusinessAPI.getComputerInfoByComputerId(idRequest);
        ComputerDTO dto = new ComputerDTO();
        BeanUtils.copyProperties(resp, dto);
        if (dto.getType() == ComputerTypeEnum.PC) {
            CbbClusterVirtualIpDTO cbbClusterVirtualIpDTO = clusterServerMgmtAPI
                    .getClusterVirtualIp();
            String rcdcIP = cbbClusterVirtualIpDTO.getClusterVirtualIp();
            dto.setServerIp(rcdcIP);
        }
        //构建桌面信息
        buildDeskInfo(request, dto);

        CbbTerminalGroupDetailDTO terminalGroupDTO = terminalGroupMgmtAPI.loadById(resp.getGroupId());
        dto.setTerminalGroupId(terminalGroupDTO.getId());
        dto.setTerminalGroupName(terminalGroupDTO.getGroupName());
        return DefaultWebResponse.Builder.success(dto);
    }

    /**
     * @api {POST} /rco/computer/detail 获取pc编辑详情
     * @apiName 获取pc编辑详情
     * @apiGroup PC纳管
     * @apiDescription 获取pc编辑详情
     *
     * @apiParam (请求体字段说明) {String} id PC终端Id
     *
     * @apiParamExample {json} 请求体示例
     * {
     *      "id": "a257cf6f-2589-45f0-aa20-8ada46483091"
     * }
     *
     * @apiSuccess (响应字段说明) {JSON} content 响应数据
     * @apiSuccess (响应字段说明) {String} content.id
     * @apiSuccess (响应字段说明) {Number} content.alias 备注
     * @apiSuccess (响应字段说明) {String} content.name 计算机名
     * @apiSuccess (响应字段说明) {String} message
     * @apiSuccess (响应字段说明) {String[]} msgArgArr
     * @apiSuccess (响应字段说明) {String} msgKey
     * @apiSuccess (响应字段说明) {String} status 响应状态
     *
     * @apiSuccessExample {json} 成功响应
     * {
     *      "content":{
     *          "id":"a257cf6f-2589-45f0-aa20-8ada46483091",
     *          "alias":"ljm",
     *          "name":"R09681"
     *      },
     *      "message":null,
     *      "msgArgArr":null,
     *      "msgKey":null,
     *      "status":"SUCCESS"
     * }
     *
     * @apiErrorExample {json} 异常响应
     * {
     *     "content": null,
     *     "message": "系统内部错误，请联系管理员",
     *     "msgArgArr": [],
     *     "msgKey": "sk_webmvckit_internal_error",
     *     "status": "ERROR"
     * }
     *
     */

    /**
     * 获取pc编辑详情
     *
     * @param request 请求参数
     * @return pc编辑详情信息
     * @throws BusinessException 业务异常
     */
    @RequestMapping(value = "/detail")
    public DefaultWebResponse detail(ComputerIdWebRequest request) throws BusinessException {
        Assert.notNull(request, "request can not be null");
        Assert.notNull(request.getId(), "request id must not be null");

        ComputerIdRequest idRequest = new ComputerIdRequest(request.getId());
        ComputerInfoResponse resp = computerBusinessAPI.getComputerInfoByComputerId(idRequest);
        ComputerDetailVO detailVO = new ComputerDetailVO();
        BeanUtils.copyProperties(resp, detailVO);

        IdLabelEntry groupEntry = new IdLabelEntry();
        groupEntry.setId(resp.getGroupId());
        groupEntry.setLabel(resp.getGroupName());
        detailVO.setTerminalGroup(groupEntry);

        return DefaultWebResponse.Builder.success(detailVO);
    }

    private void buildDeskInfo(ComputerIdWebRequest request, ComputerDTO detailVO) {
        try {
            CbbDeskDTO deskDTO = cbbDeskMgmtAPI.findById(request.getId());
            if (deskDTO != null) {
                detailVO.setDeskName(deskDTO.getName());
                if (deskDTO.getDesktopPoolId() != null) {
                    DesktopPoolDetailDTO desktopPoolDetail = desktopPoolMgmtAPI.getDesktopPoolDetail(deskDTO.getDesktopPoolId());
                    detailVO.setDeskPoolName(desktopPoolDetail.getName());
                }
            }
        } catch (BusinessException ex) {
            LOGGER.error("获取云桌面[{}]信息异常", request.getId(), ex);
        }
    }

    /**
     * @api {POST} /rco/computer/modifyAlias 修改pc备注名
     * @apiName 修改pc备注名
     * @apiGroup PC纳管
     * @apiDescription 修改pc备注名
     *
     * @apiParam (请求体字段说明) {String} id PC终端Id
     * @apiParam (请求体字段说明) {String} name 计算机名(后端没有接受，不做处理)
     * @apiParam (请求体字段说明) {String} alias 备注
     *
     * @apiParamExample {json} 请求体示例
     * {
     *      "id": "a257cf6f-2589-45f0-aa20-8ada46483091",
     *      "name": "R09681",
     *      "alias": "ljm1",
     * }
     *
     * @apiSuccess (响应字段说明) {JSON} content 响应数据
     * @apiSuccess (响应字段说明) {String} message
     * @apiSuccess (响应字段说明) {String[]} msgArgArr
     * @apiSuccess (响应字段说明) {String} msgKey
     * @apiSuccess (响应字段说明) {String} status 响应状态
     *
     * @apiSuccessExample {json} 成功响应
     * {
     *      "content": null,
     *      "message":null,
     *      "msgArgArr":null,
     *      "msgKey":null,
     *      "status":"SUCCESS"
     * }
     *
     * @apiErrorExample {json} 异常响应
     * {
     *     "content": null,
     *     "message": "系统内部错误，请联系管理员",
     *     "msgArgArr": [],
     *     "msgKey": "sk_webmvckit_internal_error",
     *     "status": "ERROR"
     * }
     *
     */

    /**
     * 修改pc备注名
     *
     * @param request 请求参数
     * @return 修改结果，成功或失败
     * @throws BusinessException 业务异常
     */
    @RequestMapping(value = "/edit")
    @ApiOperation("终端编辑")
    @ApiVersions({@ApiVersion(value = Version.V3_1_1, descriptions = {"分级分权-区分权限，终端编辑"})})
    @EnableAuthority
    public DefaultWebResponse editComputer(EditComputerWebRequest request) throws BusinessException {
        Assert.notNull(request, "request can not be null");

        try {
            ComputerIdRequest idRequest = new ComputerIdRequest(request.getId());
            ComputerInfoResponse response = computerBusinessAPI.getComputerInfoByComputerId(idRequest);
            computerBusinessAPI.editComputer(new EditComputerRequest(response.getId(), request.getAlias(), request.getTerminalGroup()));
            auditLogAPI.recordLog(ComputerBusinessKey.RCDC_RCO_COMPUTER_MODIFY_ALIAS_SUCCESS, response.getMac());
            return DefaultWebResponse.Builder.success(ComputerBusinessKey.RCDC_RCO_COMPUTER_MODIFY_ALIAS_SUCCESS, new String[]{response.getMac()});
        } catch (BusinessException e) {
            auditLogAPI.recordLog(ComputerBusinessKey.RCDC_RCO_COMPUTER_MODIFY_ALIAS_FAIL, request.getId().toString(), e.getI18nMessage());
            throw new BusinessException(UserBusinessKey.RCDC_RCO_MODULE_OPERATE_FAIL, e, e.getI18nMessage());
        }
    }

    /**
     * 请求收集GT日志
     * @param request 终端ID
     * @return 响应
     * @throws BusinessException 业务异常
     */
    @ApiOperation("请求收集GT日志")
    @ApiVersions({@ApiVersion(value = Version.V1_0_0), @ApiVersion(value = Version.V3_1_1, descriptions = {"分级分权-区分权限，加入权限控制"})})
    @RequestMapping("/collectLog")
    @EnableAuthority
    public DefaultWebResponse collectLog(IdWebRequest request) throws BusinessException {
        Assert.notNull(request, "request cannot be null!");
        ComputerIdRequest computerIdRequest = new ComputerIdRequest();
        computerIdRequest.setComputerId(request.getId());
        ComputerInfoResponse computerInfoResponse = null;
        try {
            computerInfoResponse = computerBusinessAPI.getComputerInfoByComputerId(computerIdRequest);
            computerBusinessAPI.collectLog(request.getId());
        } catch (BusinessException e) {
            String tip = request.getId().toString();
            if (computerInfoResponse != null) {
                tip = computerInfoResponse.getName();
            }
            auditLogAPI.recordLog(UserBusinessKey.RCDC_RCO_PC_COLLECT_LOG_ERROR, tip, e.getMessage());
            throw e;
        }

        return DefaultWebResponse.Builder.success();
    }


    /**
     * 获取日志收集状态
     * @param request 终端ID
     * @throws BusinessException 业务异常
     * @return 日志收集状态响应
     */
    @RequestMapping("/getCollectLogState")
    public DefaultWebResponse getCollectLogState(IdWebRequest request) throws BusinessException {
        Assert.notNull(request, "request cannot be null!");

        ComputerIdRequest computerIdRequest = new ComputerIdRequest();
        computerIdRequest.setComputerId(request.getId());
        CbbGtLogCollectStateDTO response = null;
        ComputerInfoResponse computerInfoResponse = null;
        try {
            computerInfoResponse = computerBusinessAPI.getComputerInfoByComputerId(computerIdRequest);
            response = cbbGuestToolLogAPI.getLogCollectState(request.getId());
            if (GtLogCollectState.DONE == response.getState()) {
                auditLogAPI.recordLog(UserBusinessKey.RCDC_RCO_PC_COLLECT_LOG_SUCCESS, computerInfoResponse.getName());
            }
            if (GtLogCollectState.FAULT == response.getState()) {
                auditLogAPI.recordLog(UserBusinessKey.RCDC_RCO_PC_COLLECT_LOG_ERROR, computerInfoResponse.getName(), response.getMessage());
            }
        } catch (BusinessException ex) {
            String tip = request.getId().toString();
            if (computerInfoResponse != null) {
                tip = computerInfoResponse.getName();
            }
            auditLogAPI.recordLog(UserBusinessKey.RCDC_RCO_PC_COLLECT_LOG_ERROR, tip, ex.getI18nMessage());
            throw ex;
        }

        return DefaultWebResponse.Builder.success(response);
    }


    /**
     * 下载日志
     * @param request 日志下载请求
     * @return 下载响应
     * @throws BusinessException 业务异常
     * @throws IOException IO异常
     */
    @RequestMapping("/downloadLog")
    public DownloadWebResponse download(DownloadLogWebRequest request) throws BusinessException, IOException {
        Assert.notNull(request, "request cannot be null!");

        String logFilePath =
                cbbGuestToolLogAPI.getLogFilePath(request.getLogFileName());
        String logFileName = request.getLogFileName();
        int lastIndex = logFileName.lastIndexOf('.');
        String logFileNameWithoutSuffix = buildLogFileName(logFileName.substring(0, lastIndex));
        String suffix = logFileName.substring(lastIndex + 1);
        File file = new File(logFilePath);

        return new DownloadWebResponse.Builder()
                .setContentType("application/octet-stream")
                .setName(logFileNameWithoutSuffix, suffix)
                .setFile(file)
                .build();
    }

    private String buildLogFileName(String logFileName) {
        return new StringBuilder(logFileName).insert(logFileName.lastIndexOf('_'), "_computerIp").toString();
    }

    /**
     * @api {POST} /rco/computer/delete 删除pc记录
     * @apiName 删除pc记录
     * @apiGroup PC纳管
     * @apiDescription 删除pc记录，支持批量删除
     *
     * @apiParam (请求体字段说明) {String[]} idArr PC终端Id数组
     *
     * @apiParamExample {json} 请求体示例
     * {
     *      "idArr": ["a257cf6f-2589-45f0-aa20-8ada46483091"]
     * }
     *
     * @apiSuccess (响应字段说明) {JSON} content 响应数据
     * @apiSuccess (响应字段说明) {String} content.taskDesc 任务描述
     * @apiSuccess (响应字段说明) {String} content.taskId 任务ID
     * @apiSuccess (响应字段说明) {String} content.taskName 任务名称
     * @apiSuccess (响应字段说明) {String = "SUCCESS", "FAILURE", "PARTIAL_SUCCESS", "PROCESSING"} content.taskStatus 任务状态
     * @apiSuccess (响应字段说明) {String} message
     * @apiSuccess (响应字段说明) {String[]} msgArgArr
     * @apiSuccess (响应字段说明) {String} msgKey
     * @apiSuccess (响应字段说明) {String} status 响应状态
     *
     * @apiSuccessExample {json} 成功响应
     * {
     *      "content":{
     *          "taskDesc":"正在删除PC",
     *          "taskId":"75e0c0b3-5876-4815-9d4a-0ebe0ef9a10c",
     *          "taskName":"删除PC",
     *          "taskStatus":"PROCESSING"
     *      },
     *      "message":null,
     *      "msgArgArr":null,
     *      "msgKey":null,
     *      "status":"SUCCESS"
     * }
     *
     * @apiErrorExample {json} 异常响应
     * {
     *     "content": null,
     *     "message": "系统内部错误，请联系管理员",
     *     "msgArgArr": [],
     *     "msgKey": "sk_webmvckit_internal_error",
     *     "status": "ERROR"
     * }
     *
     */

    /**
     * 删除pc记录，支持批量删除
     *
     * @param request 请求参数，需要删除的pc记录的id数组
     * @param builder 批量任务创建对象
     * @return 返回结果，成功或失败
     * @throws BusinessException 业务异常
     */
    @RequestMapping(value = "/delete")
    @ApiOperation("删除")
    @ApiVersions({@ApiVersion(value = Version.V3_1_1, descriptions = {"分级分权-区分权限，删除"})})
    @EnableAuthority
    public DefaultWebResponse deleteComputer(DelComputerWebRequest request,
                                             BatchTaskBuilder builder) throws BusinessException {
        Assert.notNull(request, "request can not be null");
        Assert.notNull(builder, "builder can not be null");

        String[] computerIdArr = request.getIdArr();
        Map<UUID, String> idMap = TerminalIdMappingUtils.mapping(computerIdArr);
        UUID[] idArr = TerminalIdMappingUtils.extractUUID(idMap);
        final Iterator<DefaultBatchTaskItem> iterator =
                Stream.of(idArr).distinct().map(id -> DefaultBatchTaskItem.builder().itemId(id)
                                .itemName(ComputerBusinessKey.RCDC_RCO_COMPUTER_DELETE_ITEM_NAME, new String[]{}).build())
                        .iterator();
        final DeleteComputerBatchTaskHandler handler = new DeleteComputerBatchTaskHandler(idMap, iterator, auditLogAPI);
        handler.setComputerBusinessAPI(computerBusinessAPI);

        BatchTaskSubmitResult result = executeDeleteBatchTask(builder, computerIdArr, handler);
        return DefaultWebResponse.Builder.success(result);
    }

    /**
     * 异步导出PC终端数据到excel
     *
     * @param request        request
     * @param sessionContext Session上下文
     * @return result
     * @throws BusinessException 业务异常
     */
    @RequestMapping(value = "/export", method = RequestMethod.POST)
    @ApiOperation("导出PC终端")
    @ApiVersions({@ApiVersion(value = Version.V1_0_0, descriptions = {"分级分权-区分权限，加入权限控制"})})
    @EnableAuthority
    public CommonWebResponse export(PageWebRequest request, SessionContext sessionContext) throws BusinessException {
        Assert.notNull(request, "request is null");
        Assert.notNull(sessionContext, "sessionContext is null");

        PageSearchRequest pageReq = buildPageSearchRequest(request);

        Boolean isPermissionLimit = checkPermission(request, sessionContext, pageReq);
        if (Boolean.FALSE.equals(isPermissionLimit)) {
            throw new BusinessException(ComputerBusinessKey.RCDC_RCO_EXPORT_COMPUTER_EMPTY_DATA_ERROR);

        }
        computerBusinessAPI.exportDataAsync(pageReq, sessionContext.getUserId().toString());
        auditLogAPI.recordLog(ComputerBusinessKey.RCDC_RCO_EXPORT_COMPUTER_SUCCESS_LOG);
        return CommonWebResponse.success();
    }

    /**
     * 获取PC终端导出任务情况
     *
     * @param sessionContext sessionContext
     * @return DataResult
     */
    @ApiOperation("获取导出结果")
    @RequestMapping(value = "getExportResult", method = RequestMethod.POST)
    public CommonWebResponse<ExportComputerFileInfoDTO> getExportResult(SessionContext sessionContext) {
        Assert.notNull(sessionContext, "sessionContext is null");
        String userId = sessionContext.getUserId().toString();
        ExportComputerFileInfoDTO exportComputerFileInfoDTO = computerBusinessAPI.getExportDataCache(userId);
        return CommonWebResponse.success(exportComputerFileInfoDTO);
    }

    /**
     * 下载云桌面数据excel
     *
     * @param request        页面请求参数
     * @param sessionContext SessionContext
     * @return DataResult
     * @throws BusinessException 业务异常
     */
    @RequestMapping(value = "/downloadExportData", method = RequestMethod.GET)
    public DownloadWebResponse downloadExportFile(EmptyDownloadWebRequest request, SessionContext sessionContext) throws BusinessException {
        Assert.notNull(request, "request is null");
        Assert.notNull(sessionContext, "sessionContext is null");
        String userId = sessionContext.getUserId().toString();
        GetExportCloudDesktopFileResponse exportFile = computerBusinessAPI.getExportFile(userId);
        DownloadWebResponse.Builder builder = new DownloadWebResponse.Builder();
        SimpleDateFormat df = new SimpleDateFormat("yyyyMMddHHmmss");
        String fileName = "pc终端信息(Computer)" + df.format(new Date());
        return builder.setFile(exportFile.getExportFile(), false).setName(fileName, "xlsx").build();
    }

    private BatchTaskSubmitResult executeDeleteBatchTask(BatchTaskBuilder builder, String[] computerIdArr,
                                                         final DeleteComputerBatchTaskHandler handler) throws BusinessException {
        BatchTaskSubmitResult result;
        if (computerIdArr.length == 1) {
            handler.setIsBatch(false);
            ComputerIdRequest request = new ComputerIdRequest(UUID.fromString(computerIdArr[0]));
            ComputerInfoResponse response = computerBusinessAPI.getComputerInfoByComputerId(request);
            String name = StringUtils.isBlank(response.getMac()) ? request.getComputerId().toString() : response.getMac().toUpperCase();
            handler.setPcName(name);
            result = builder.setTaskName(ComputerBusinessKey.RCDC_RCO_COMPUTER_DELETE_SINGLE_TASK_NAME)
                    .setTaskDesc(ComputerBusinessKey.RCDC_RCO_COMPUTER_DELETE_SINGLE_TASK_DESC, name)
                    .registerHandler(handler).start();
        } else {
            result = builder.setTaskName(ComputerBusinessKey.RCDC_RCO_COMPUTER_DELETE_TASK_NAME)
                    .setTaskDesc(ComputerBusinessKey.RCDC_RCO_COMPUTER_DELETE_TASK_DESC).enableParallel()
                    .registerHandler(handler).start();
        }
        return result;
    }

    /**
     * @api {GET} /rco/computer/downloadAssistantApp/download?fileName=桌面小助手&downloadSpecialId=1584004613000 下载小助手接口
     * @apiName 下载小助手接口
     * @apiGroup PC纳管
     * @apiDescription 下载小助手接口
     *
     * @apiParam (请求路径字段说明) {String} fileName 文件名称 （前端使用）
     * @apiParam (请求路径字段说明) {String} downloadSpecialId 点击下载时间戳（前端使用）
     *
     */

    /**
     * 下载小助手接口
     *
     * @param request 请求参数
     * @return 结果
     * @throws BusinessException 业务异常
     */
    @NoAuthUrl
    @RequestMapping(value = "/downloadAssistantApp/download")
    public DownloadWebResponse downloadAssistantApp(EmptyDownloadWebRequest request) throws BusinessException {
        String appPath = computerBusinessAPI.getAssistantAppDownloadUrl(new DefaultRequest()).getPath();
        Assert.notNull(appPath, "app path can not be null");

        File file = new File(appPath);
        String fileNameWithSuffix = file.getName();
        String fileName = fileNameWithSuffix.substring(0, fileNameWithSuffix.lastIndexOf('.'));
        String suffix = fileNameWithSuffix.substring(fileNameWithSuffix.lastIndexOf('.') + 1);

        DownloadWebResponse.Builder builder = new DownloadWebResponse.Builder();
        return builder.setFile(file, false).setName(fileName, suffix).build();
    }

    /**
     * 下载halo接口
     *
     * @param request 请求参数
     * @return 结果
     * @throws BusinessException 业务异常
     */
    @NoAuthUrl
    @RequestMapping(value = "/halo/download")
    public DownloadWebResponse downloadHaloApp(EmptyDownloadWebRequest request) throws BusinessException {
        String appPath = computerBusinessAPI.getHaloAppDownloadUrl(new DefaultRequest()).getPath();
        Assert.notNull(appPath, "app path can not be null");

        File file = new File(appPath);
        String fileNameWithSuffix = file.getName();
        String fileName = fileNameWithSuffix.substring(0, fileNameWithSuffix.lastIndexOf('.'));
        String suffix = fileNameWithSuffix.substring(fileNameWithSuffix.lastIndexOf('.') + 1);

        DownloadWebResponse.Builder builder = new DownloadWebResponse.Builder();
        return builder.setFile(file, false).setName(fileName, suffix).build();
    }

    /**
     * @api {POST} /rco/computer/updateGroup 批量更新终端分组
     * @apiName 批量更新终端分组
     * @apiGroup PC纳管
     * @apiDescription 批量更新终端分组
     *
     * @apiParam (请求体字段说明) {String[]} idArr PC终端Id数组
     * @apiParam (请求体字段说明) {String} terminalGroupId 终端组
     *
     * @apiParamExample {json} 请求体示例
     * {
     *      "idArr": ["a257cf6f-2589-45f0-aa20-8ada46483091"],
     *      "terminalGroupId": "91877313-7aaa-4965-a5d6-0efbc90784ed"
     * }
     *
     * @apiSuccess (响应字段说明) {JSON} content 响应数据
     * @apiSuccess (响应字段说明) {String} message
     * @apiSuccess (响应字段说明) {String[]} msgArgArr
     * @apiSuccess (响应字段说明) {String} msgKey
     * @apiSuccess (响应字段说明) {String} status 响应状态
     *
     * @apiSuccessExample {json} 成功响应
     * {
     *      "content":null,
     *      "message":"移动终端所属分组，成功[2]条，失败[0]条",
     *      "msgArgArr":["2","0"],
     *      "msgKey":"rcdc_rco_computer_update_terminal_group_result",
     *      "status":"SUCCESS"
     * }
     *
     * @apiErrorExample {json} 异常响应
     * {
     *     "content": null,
     *     "message": "系统内部错误，请联系管理员",
     *     "msgArgArr": [],
     *     "msgKey": "sk_webmvckit_internal_error",
     *     "status": "ERROR"
     * }
     */

    /**
     * 批量更新终端分组
     *
     * @param request 请求参数
     * @return 请求结果
     * @throws BusinessException 业务异常
     */
    @RequestMapping(value = "/updateGroup")
    @ApiOperation("批量更新终端分组")
    @ApiVersions({@ApiVersion(value = Version.V3_1_1, descriptions = {"分级分权-区分权限，批量更新终端分组"})})
    @EnableAuthority
    public DefaultWebResponse moveComputerGroup(UpdateComputerGroupWebRequest request) throws BusinessException {
        Assert.notNull(request, "request can not be null");

        int successNum = 0;
        int failNum = 0;
        UUID[] computerIdArr = request.getIdArr();
        final UUID computerGroupId = request.getTerminalGroupId();
        for (UUID computerId : computerIdArr) {
            boolean isSuccess = moveComputerGroupAddOptLog(computerId, computerGroupId);
            if (isSuccess) {
                successNum++;
            } else {
                failNum++;
            }
        }

        if (failNum == 0) {
            return DefaultWebResponse.Builder
                    .success(ComputerBusinessKey.RCDC_RCO_COMPUTER_UPDATE_TERMINAL_GROUP_RESULT,
                            new String[]{String.valueOf(successNum), String.valueOf(failNum)});
        } else {
            return DefaultWebResponse.Builder
                    .fail(ComputerBusinessKey.RCDC_RCO_COMPUTER_UPDATE_TERMINAL_GROUP_RESULT,
                            new String[]{String.valueOf(successNum), String.valueOf(failNum)});
        }
    }

    /**
     * 添加终端
     *
     * @param request 请求
     * @return DefaultWebResponse
     */
    @RequestMapping(value = "create", method = RequestMethod.POST)
    @ApiOperation("添加终端")
    @ApiVersions({@ApiVersion(value = Version.V6_0_20, descriptions = {"分级分权-区分权限，添加终端"})})
    @EnableCustomValidate(validateMethod = "computerCreateValidate")
    @EnableAuthority
    public DefaultWebResponse create(CreateComputerWebRequest request) {
        Assert.notNull(request, "request can not be null");
        try {
            CreateComputerRequest createComputerRequest = new CreateComputerRequest();
            BeanUtils.copyProperties(request, createComputerRequest);
            computerBusinessAPI.saveComputer(createComputerRequest);
            auditLogAPI.recordLog(ComputerBusinessKey.RCDC_RCO_COMPUTER_CREATE_TERMINAL_SUCCESS_LOG, request.getName());
            return DefaultWebResponse.Builder.success(UserBusinessKey.RCDC_RCO_MODULE_OPERATE_SUCCESS, new String[]{});
        } catch (BusinessException e) {
            auditLogAPI.recordLog(ComputerBusinessKey.RCDC_RCO_COMPUTER_CREATE_TERMINAL_FAIL_LOG, request.getName(), e.getI18nMessage());
            return DefaultWebResponse.Builder.fail(UserBusinessKey.RCDC_RCO_MODULE_OPERATE_FAIL, new String[]{e.getI18nMessage()});
        }

    }

    /**
     * 获取PC终端及分配信息列表
     *
     * @param request        请求参数对象
     * @param sessionContext session信息
     * @return 返回消息记录列表
     * @throws BusinessException 业务异常
     */
    @ApiOperation(value = "获取PC终端及分配信息列表")
    @ApiVersions({@ApiVersion(value = Version.V6_0_20, descriptions = {"获取PC终端及分配信息列表"})})
    @RequestMapping(value = "/listWithAssignment", method = RequestMethod.POST)
    public CommonWebResponse<DefaultPageResponse<ComputerDTO>> getComputerWithAssignmentList(
            PageWebRequest request, SessionContext sessionContext) throws BusinessException {
        Assert.notNull(request, "PageWebRequest 不能为null");
        Assert.notNull(sessionContext, "sessionContext can not be null");

        ComputerPageSearchRequest pageReq = new ComputerPageSearchRequest(request);
        if (pageReq.getSortArr() == null) {
            Sort[] sortArr = generateSortArr();
            pageReq.setSortArr(sortArr);
        } else {
            Sort[] sortArr = generateSortArr(pageReq.getSortArr());
            pageReq.setSortArr(sortArr);
        }
        CommonWebResponse<DefaultPageResponse<ComputerDTO>> result = getDefaultPageResponseCommonWebResponse(request, sessionContext, pageReq);
        if (result != null) {
            return result;
        }

        DefaultPageResponse<ComputerDTO> response = computerBusinessAPI.pageQuery(pageReq);
        for (ComputerDTO computerDTO : response.getItemArr()) {
            if (computerDTO.getWorkModel() != null) {
                buildCanUsedMessage(computerDTO, BusinessKey.RCDC_COMPUTER_HAVE_BEEN_USED);
            } else if (StringUtils.isBlank(computerDTO.getName()) || StringUtils.isBlank(computerDTO.getOs())
                    || StringUtils.isBlank(computerDTO.getIp())) {
                buildCanUsedMessage(computerDTO, BusinessKey.RCDC_COMPUTER_NOT_CAN_USED);
            } else if (pageReq.getSessionType() == CbbDesktopSessionType.MULTIPLE) {
                CbbHostOsType osType = CbbHostOsType.getOsType(computerDTO.getOs());
                if (osType == null) {
                    buildCanUsedMessage(computerDTO, BusinessKey.RCDC_COMPUTER_OS_NOT_SUPPORT);
                } else if (!CbbHostOsType.isMultiSession(osType)) {
                    buildCanUsedMessage(computerDTO, BusinessKey.RCDC_COMPUTER_MULTI_SESSION_OS);
                }
            }
        }
        return CommonWebResponse.success(response);
    }

    private static void buildCanUsedMessage(ComputerDTO computerDTO, String rcdcComputerMultiSessionOs) {
        computerDTO.setCanUsed(Boolean.FALSE);
        computerDTO.setCanUsedMessage(LocaleI18nResolver.resolve(rcdcComputerMultiSessionOs));
    }

    private PageSearchRequest buildPageSearchRequest(PageWebRequest request) {
        PageSearchRequest pageReq = new ComputerPageSearchRequest(request);
        if (pageReq.getSortArr() == null) {
            Sort[] sortArr = generateSortArr();
            pageReq.setSortArr(sortArr);
        } else {
            Sort[] sortArr = generateSortArr(pageReq.getSortArr());
            pageReq.setSortArr(sortArr);
        }
        return pageReq;
    }

    private CommonWebResponse<DefaultPageResponse<ComputerDTO>> getDefaultPageResponseCommonWebResponse(PageWebRequest request,
                                                                 SessionContext sessionContext,
                                                                 PageSearchRequest pageReq) throws BusinessException {
        Boolean isPermissionLimit = checkPermission(request, sessionContext, pageReq);
        if (Boolean.FALSE.equals(isPermissionLimit)) {
            return CommonWebResponse.success();
        }
        // 没有权限返回null
        return null;
    }

    private boolean moveComputerGroupAddOptLog(UUID computerId, UUID terminalGroupId) {
        String computerIdentification = computerId.toString();
        try {
            ComputerInfoResponse response = computerBusinessAPI
                    .getComputerInfoByComputerId(new ComputerIdRequest(computerId));
            computerIdentification = response.getComputerIdForOptLog().toUpperCase();
            moveComputerGroup(computerId, terminalGroupId, response.getMac());
            auditLogAPI.recordLog(ComputerBusinessKey.RCDC_RCO_COMPUTER_UPDATE_TERMINAL_GROUP_SUCCESS_LOG,
                    response.getMac());
            return true;
        } catch (BusinessException e) {
            LOGGER.error("编辑PC" + computerIdentification + "所属分组失败", e);
            auditLogAPI.recordLog(ComputerBusinessKey.RCDC_RCO_COMPUTER_UPDATE_TERMINAL_GROUP_FAIL_LOG,
                    computerIdentification, e.getI18nMessage());
            return false;
        }
    }

    private void moveComputerGroup(UUID computerId, UUID terminalGroupId, String computerName)
            throws BusinessException {
        MoveComputerGroupRequest computerRequest = new MoveComputerGroupRequest();
        computerRequest.setComputerId(computerId);
        computerRequest.setGroupId(terminalGroupId);
        computerRequest.setComputerName(computerName);
        computerBusinessAPI.moveComputerGroup(computerRequest);
    }

    /**
     * @api {POST} /rco/computer/listComputerFault 获取报障PC终端列表
     * @apiName 获取报障PC终端列表
     * @apiGroup PC纳管
     * @apiDescription 获取报障终端列表
     *
     * @apiParam (请求体字段说明) {Number} [page] 页数 0表示首页
     * @apiParam (请求体字段说明) {Number} [limit] 每页查询数量
     * @apiParam (请求体字段说明) {String} searchKeyword 搜索内容
     * @apiParam (请求体字段说明) {Boolean} autoRefresh 搜索内容
     * @apiParam (请求体字段说明) {JSON[]} exactMatchArr 精确匹配体
     * @apiParam (请求体字段说明) {String} exactMatchArr.name 字段名称
     * @apiParam (请求体字段说明) {String[]} exactMatchArr.valueArr 匹配值
     * @apiParam (请求体字段说明) {JSON} sort 排序体
     * @apiParam (请求体字段说明) {String} sort.sortField 排序字段
     * @apiParam (请求体字段说明) {String = "ASC"、"DESC"} sort.direction 排序类型
     *
     * @apiParamExample {json} 请求体示例
     * {
     *      "page":0,
     *      "limit":5,
     *      "searchKeyword": "",
     *      "autoRefresh"：true，
     *      "exactMatchArr": [],
     *      "sort": {}
     *  }
     *
     * @apiSuccess (响应字段说明) {JSON} content 响应数据
     * @apiSuccess (响应字段说明) {Number} content.total 总条数
     * @apiSuccess (响应字段说明) {JSON[]} content.itemArr 列表数据
     * @apiSuccess (响应字段说明) {JSON} content.itemArr[0] 每列数据
     * @apiSuccess (响应字段说明) {String} content.itemArr[0].id
     * @apiSuccess (响应字段说明) {Number} content.itemArr[0].alias 备注
     * @apiSuccess (响应字段说明) {String} content.itemArr[0].cpu
     * @apiSuccess (响应字段说明) {Number} content.itemArr[0].createTime 创建时间
     * @apiSuccess (响应字段说明) {String} content.itemArr[0].disk 硬盘
     * @apiSuccess (响应字段说明) {String} content.itemArr[0].faultDescription 故障信息
     * @apiSuccess (响应字段说明) {boolean} content.itemArr[0].faultState 故障状态
     * @apiSuccess (响应字段说明) {String} content.itemArr[0].ip
     * @apiSuccess (响应字段说明) {String} content.itemArr[0].mac
     * @apiSuccess (响应字段说明) {String} content.itemArr[0].memory 内存
     * @apiSuccess (响应字段说明) {String} content.itemArr[0].name 计算机名
     * @apiSuccess (响应字段说明) {String} content.itemArr[0].os 操作系统
     * @apiSuccess (响应字段说明) {String} content.itemArr[0].serverIp 服务器ip
     * @apiSuccess (响应字段说明) {String = "ONLINE","OFFLINE"} content.itemArr[0].state PC状态
     * @apiSuccess (响应字段说明) {Boolean} content.itemArr[0].faultState PC报障状态
     * @apiSuccess (响应字段说明) {Number} content.itemArr[0].faultTime 报障时间
     * @apiSuccess (响应字段说明) {String} content.itemArr[0].terminalGroupId PC所属组
     * @apiSuccess (响应字段说明) {String} content.itemArr[0].terminalGroupName PC所属组名称
     * @apiSuccess (响应字段说明) {String} message
     * @apiSuccess (响应字段说明) {String[]} msgArgArr
     * @apiSuccess (响应字段说明) {String} msgKey
     * @apiSuccess (响应字段说明) {String} status 响应状态
     *
     * @apiSuccessExample {json} 成功响应
     * {
     *      "content":{
     *          "total":1,
     *          "itemArr":[{
     *              "id":"a257cf6f-2589-45f0-aa20-8ada46483091",
     *              "alias":"ljm",
     *              "cpu":"Intel(R) Core(TM) i5-6500 CPU @ 3.20GHz",
     *              "createTime":1583824306932,
     *              "disk":"930.52GB",
     *              "faultDescription":"",
     *              "faultState":null,
     *              "ip":"172.20.114.82",
     *              "mac":"A0:8C:FD:F3:D2:34",
     *              "memory":"7.87GB（可用）",
     *              "name":"R09681",
     *              "os":"Windows 10",
     *              "serverIp":null,
     *              "state":"ONLINE",
     *              "faultState":true,
     *              "faultTime": 1591703159064,
     *              "terminalGroupId":"91877313-7aaa-4965-a5d6-0efbc90784ed",
     *              "terminalGroupName":"软终端1组"
     *          }]
     *      },
     *      "message":null,
     *      "msgArgArr":null,
     *      "msgKey":null,
     *      "status":"SUCCESS"
     * }
     *
     * @apiErrorExample {json} 异常响应
     * {
     *     "content": null,
     *     "message": "系统内部错误，请联系管理员",
     *     "msgArgArr": [],
     *     "msgKey": "sk_webmvckit_internal_error",
     *     "status": "ERROR"
     * }
     *
     */
    /**
     * 首页查询报障PC
     *
     * @param request        请求参数
     * @param sessionContext session信息
     * @return DefaultWebResponse 响应列表数据
     * @throws BusinessException 业务异常
     */
    @RequestMapping(value = "/listComputerFault")
    public DefaultWebResponse listComputerFault(PageWebRequest request, SessionContext sessionContext) throws BusinessException {
        Assert.notNull(request, "PageWebRequest不能为null");
        Assert.notNull(sessionContext, "sessionContext can not be null");

        PageSearchRequest pageRequest = new ComputerPageSearchRequest(request);
        Boolean isPermissionLimit = checkPermission(request, sessionContext, pageRequest);
        if (Boolean.FALSE.equals(isPermissionLimit)) {
            return DefaultWebResponse.Builder.success(ImmutableMap.of("itemArr", Collections.emptyList()));
        }
        return DefaultWebResponse.Builder.success(computerBusinessAPI.computerFaultPageQuery(pageRequest));
    }

    /**
     * 远程PC唤醒终端
     *
     * @param request 请求
     * @param builder 批量任务创建对象
     * @return 唤醒结果
     * @throws BusinessException 业务异常
     */
    @ApiOperation("PC终端开机")
    @ApiVersions({@ApiVersion(value = Version.V6_0_20, descriptions = {"PC终端开机"})})
    @RequestMapping(value = "wakeUp", method = RequestMethod.POST)
    @EnableAuthority
    public DefaultWebResponse wakeUpComputer(ComputerIdArrWebRequest request, BatchTaskBuilder builder)
            throws BusinessException {
        Assert.notNull(request, "request must not be null");
        Assert.notNull(builder, "builder must not be null");

        Map<UUID, String> idMap = TerminalIdMappingUtils.mapping(request.getIdArr());
        UUID[] idArr = TerminalIdMappingUtils.extractUUID(idMap);
        final Iterator<DefaultBatchTaskItem> iterator = Stream.of(idArr).distinct().map(id -> DefaultBatchTaskItem.builder().itemId(id)
                .itemName(ComputerBusinessKey.RCDC_COMPUTER_WAKE_UP_ITEM_NAME, new String[]{}).build()).iterator();
        WakeUpComputerHandler handler = new WakeUpComputerHandler(idMap, iterator);
        handler.setAuditLogAPI(auditLogAPI);
        handler.setComputerBusinessAPI(computerBusinessAPI);
        String taskDesc = request.getIdArr().length > 1 ? ComputerBusinessKey.RCDC_COMPUTER_WAKE_UP_BATCH_ITEM_DESC :
                ComputerBusinessKey.RCDC_COMPUTER_WAKE_UP_SINGLE_ITEM_DESC;
        BatchTaskSubmitResult result = builder.setTaskName(ComputerBusinessKey.RCDC_COMPUTER_WAKE_UP_TASK_NAME)
                .setTaskDesc(taskDesc).registerHandler(handler).enableParallel().start();

        return DefaultWebResponse.Builder.success(result);
    }

    private Boolean checkPermission(PageWebRequest request, SessionContext session, PageSearchRequest pageReq) throws BusinessException {
        if (!permissionHelper.isAllGroupPermission(session)) {
            UUID[] terminalGroupIdArr = permissionHelper.getTerminalGroupIdArr(session.getUserId());
            List<UUID> terminalGroupIdList = Arrays.asList(terminalGroupIdArr);
            String terminalGroupId = getTerminalGroupId(request);
            if (StringUtils.isEmpty(terminalGroupId)) {
                MatchEqual matchEqual = new MatchEqual();
                matchEqual.setName("terminalGroupId");
                matchEqual.setValueArr(terminalGroupIdArr);
                pageReq.appendCustomMatchEqual(matchEqual);
            } else {
                if (!terminalGroupIdList.contains(UUID.fromString(terminalGroupId))) {
                    return false;
                }
            }
        }
        return true;
    }

    private Sort[] generateSortArr(Sort[] sortArr) {
        List<Sort> sortList = new ArrayList<>();
        Sort sortFirst = new Sort();
        sortFirst.setSortField("faultState");
        sortFirst.setDirection(Direction.DESC);
        sortList.add(sortFirst);
        for (Sort sort : sortArr) {
            sortList.add(sort);
        }
        return sortList.toArray(new Sort[sortList.size()]);
    }

    private Sort[] generateSortArr() {
        Sort sortFirst = new Sort();
        sortFirst.setSortField("faultState");
        sortFirst.setDirection(Direction.DESC);

        Sort sortSecond = new Sort();
        sortSecond.setSortField("state");
        sortSecond.setDirection(Direction.ASC);

        Sort sortThird = new Sort();
        sortThird.setSortField("faultTime");
        sortThird.setDirection(Direction.DESC);

        Sort sortFour = new Sort();
        sortFour.setSortField("createTime");
        sortFour.setDirection(Direction.DESC);

        return new Sort[]{sortFirst, sortSecond, sortThird, sortFour};
    }

    /**
     * 当前前端只会上传一个groupId进行查询
     */
    private String getTerminalGroupId(PageWebRequest request) {
        if (ArrayUtils.isNotEmpty(request.getExactMatchArr())) {
            for (ExactMatch exactMatch : request.getExactMatchArr()) {
                if (exactMatch.getName().equals("terminalGroupId") && exactMatch.getValueArr().length > 0) {
                    return exactMatch.getValueArr()[0];
                }
            }
        }
        return "";
    }
}
