package com.ruijie.rcos.rcdc.rco.module.web.ctrl.clouddesktop;

import com.ruijie.rcos.rcdc.rco.module.def.api.AdminDataPermissionAPI;
import com.ruijie.rcos.rcdc.rco.module.def.api.TerminalSelectAPI;
import com.ruijie.rcos.rcdc.rco.module.def.api.dto.MatchEqual;
import com.ruijie.rcos.rcdc.rco.module.def.api.dto.ProductDriverDTO;
import com.ruijie.rcos.rcdc.rco.module.def.api.dto.TerminalDTO;
import com.ruijie.rcos.rcdc.rco.module.def.api.request.PageSearchRequest;
import com.ruijie.rcos.rcdc.rco.module.def.api.request.TerminalSelectPageSearchRequest;
import com.ruijie.rcos.rcdc.rco.module.def.api.request.aaa.ListTerminalGroupIdRequest;
import com.ruijie.rcos.rcdc.rco.module.def.api.response.aaa.ListTerminalGroupIdResponse;
import com.ruijie.rcos.rcdc.rco.module.def.utils.PermissionHelper;
import com.ruijie.rcos.rcdc.rco.module.web.ctrl.clouddesktop.request.driver.ListHardwareVersionRequest;
import com.ruijie.rcos.rcdc.rco.module.web.service.TerminalGroupHelper;
import com.ruijie.rcos.rcdc.terminal.module.def.api.enums.CbbNetworkModeEnums;
import com.ruijie.rcos.rcdc.terminal.module.def.enums.CbbTerminalPlatformEnums;
import com.ruijie.rcos.sk.base.exception.BusinessException;
import com.ruijie.rcos.sk.modulekit.api.comm.DefaultPageResponse;
import com.ruijie.rcos.sk.webmvc.api.request.IdWebRequest;
import com.ruijie.rcos.sk.webmvc.api.request.PageWebRequest;
import com.ruijie.rcos.sk.webmvc.api.response.DefaultWebResponse;
import com.ruijie.rcos.sk.webmvc.api.response.PageResponseContent;
import com.ruijie.rcos.sk.webmvc.api.session.SessionContext;
import com.ruijie.rcos.sk.webmvc.api.vo.ExactMatch;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.UUID;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * Description: 终端选取控制层
 * Copyright: Copyright (c) 2019
 * Company: Ruijie Co., Ltd.
 * Create Time: 2020/2/4
 *
 * @author songxiang
 */
@Controller
@RequestMapping("/rco/terminal/terminalModel")
public class TerminalSelectController {

    private static final String IMAGE_ID = "imageId";


    @Autowired
    private TerminalSelectAPI terminalSelectAPI;

    @Autowired
    private PermissionHelper permissionHelper;

    @Autowired
    private AdminDataPermissionAPI adminDataPermissionAPI;

    /**
     * @api {POST} /rco/terminal/terminalModel/list 查询镜像驱动列表
     * @apiName 查询镜像驱动列表
     * @apiGroup 终端镜像选取
     * @apiDescription 根据镜像的ID查询镜像驱动列表
     * @apiParam (请求路径字段说明) {String} imageId 镜像ID，取值为32位的UUID
     * @apiParamExample {json} 请求体示例
     *                  {
     *                  "imageId":"2515cdb2-2a69-49c4-b1c0-a98300cfe5a6"
     *                  }
     * @apiSuccess (响应字段说明) {int} code 响应码
     * @apiSuccess (响应字段说明) {String} message 响应信息
     * @apiSuccess (响应字段说明) {Object[]} data 响应数据
     * @apiSuccess (响应字段说明) {String} data.productModel 驱动名称
     * @apiSuccess (响应字段说明) {String} data.cpuType cpu类型
     * @apiSuccess (响应字段说明) {String} data.productId 产品ID
     * @apiSuccess (响应字段说明) {bool} data.installed 该镜像模板是否成功编辑过该驱动
     * @apiSuccessExample {json} 成功响应
     *                    {
     *                    "content": {
     *                    "dto": [
     *                    {
     *                    "cpuType": "Intel(R) Atom(TM) x5-Z8300 CPU @ 1.44GHz",
     *                    "install": true,
     *                    "productId": "80020071",
     *                    "productModel": "Rain200 V2"
     *                    },
     *                    {
     *                    "cpuType": "Intel(R) Atom(TM) x5-Z8300 CPU @ 1.44GHz",
     *                    "installed": true,
     *                    "productId": "80020061",
     *                    "productModel": "Rain100 V2"
     *                    },
     *                    {
     *                    "cpuType": "ARMv7 Processor rev 0 (v7l) RK3188 CPU, @ 1.6GHz",
     *                    "install": false,
     *                    "productId": "80020091",
     *                    "productModel": "SmartRain100S"
     *                    }
     *                    ],
     *                    "empty": false
     *                    },
     *                    "message": null,
     *                    "msgArgArr": null,
     *                    "msgKey": null,
     *                    "status": "SUCCESS"
     *                    }
     * @apiErrorExample {json} 异常响应
     *                  {
     *                  "content": null,
     *                  "message": "系统内部错误，请联系管理员",
     *                  "msgArgArr": [],
     *                  "msgKey": "sk_webmvckit_internal_error",
     *                  "status": "ERROR"
     *                  }
     *
     */
    /**
     * 镜像模板ID请求
     *
     * @param request 镜像ID
     * @return DefaultWebResponse
     * @throws BusinessException 业务异常
     */
    @RequestMapping("/list")
    public DefaultWebResponse listTerminalModel(IdWebRequest request) throws BusinessException {
        Assert.notNull(request, "request can not be null");
        //由于IDV 需要安装驱动  VOI不需要修改
        ProductDriverDTO[] dtoArr = terminalSelectAPI.listSortedTerminalModel(request.getId());
        PageResponseContent<ProductDriverDTO> pageResponseContent = new PageResponseContent<>(dtoArr, dtoArr.length);
        return DefaultWebResponse.Builder.success(pageResponseContent);
    }

    /**
     * 镜像模板ID、产品型号请求
     *
     * @param request 镜像ID、产品型号请求类
     * @return DefaultWebResponse
     * @throws BusinessException 业务异常
     */
    @RequestMapping("/listHardwareVersion")
    public DefaultWebResponse listHardwareVersion(ListHardwareVersionRequest request) throws BusinessException {
        Assert.notNull(request, "request can not be null");
        ProductDriverDTO[] dtoArr = terminalSelectAPI.listSortedHardwareVersion(request.getId(), request.getProductModel());
        PageResponseContent<ProductDriverDTO> pageResponseContent = new PageResponseContent<>(dtoArr, dtoArr.length);
        return DefaultWebResponse.Builder.success(pageResponseContent);
    }


    /**
     * @api {POST} /rco/terminal/terminalModel/terminalList 在线终端选取
     * @apiName 在线终端选取
     * @apiGroup 终端镜像选取
     * @apiDescription 根据指定的虚机ID修改虚机基本信息
     * @apiParam (请求体字段说明) {Object[]} exactMatchArr 请求内容
     * @apiParam (请求体字段说明) {number{0..}} page 分页个数
     * @apiParam (请求体字段说明) {number{0..}} limit 每页的数量
     * @apiParam (请求体字段说明) {String} [searchKeyword] 模糊查询的关键词
     * @apiParam (请求体字段说明) {Object} [sort] 排序的字段
     * @apiParam (请求体字段说明) {Object} [sort.sortField] 要排序的字段
     * @apiParam (请求体字段说明) {String="ASC","DESC"} [sort.direction] 按照升序还是降序
     * @apiParam (请求体字段说明) {String} exactMatchArr.name 查询过滤字段名称
     * @apiParam (请求体字段说明) {String[]} exactMatchArr.valueArr 查询过滤字段名称对应的取值
     * @apiParam (请求体字段说明) {String[]} exactMatchArr.name 必须填写产品的productType和镜像ID
     * @apiParam (请求体字段说明) {String[]} exactMatchArr.valueArr 必须填写产品ID和镜像的值
     *
     * @apiParamExample {json} 请求体示例
     *                  {
     *                  "exactMatchArr": [
     *                  {
     *                  "name": "productType",
     *                  "valueArr": [
     *                  "80020071"
     *                  ]
     *                  },
     *                  {
     *                  "name": "imageId",
     *                  "valueArr": [
     *                  "4bfe59c9-aba8-47de-b58f-e612b3ef7158"
     *                  ]
     *                  }
     *                  ],
     *                  "page": 0,
     *                  "limit": 5
     *                  }
     * @apiSuccess (响应字段说明) {Object} content 内容
     * @apiSuccess (响应字段说明) {Object} content.dto Dto对象
     * @apiSuccess (响应字段说明) {Object[]} content.dto.itemArr 驱动名称
     * @apiSuccess (响应字段说明) {String} content.dto.itemArr.bindUserName 桌面绑定的用户名
     * @apiSuccess (响应字段说明) {String} content.dto.itemArr.createTime 终端的创建时间
     * @apiSuccess (响应字段说明) {String} content.dto.itemArr.deskIp 桌面的IP
     * @apiSuccess (响应字段说明) {String="PERSONAL","PUBLIC","UNKNOWN"} content.dto.itemArr.idvTerminalMode 终端模式
     * @apiSuccess (响应字段说明) {String} content.dto.itemArr.imageTemplateName 终端镜像
     * @apiSuccess (响应字段说明) {String} content.dto.itemArr.ip 终端IP
     * @apiSuccess (响应字段说明) {String} content.dto.itemArr.lastOnlineTime 最后登录时间
     * @apiSuccess (响应字段说明) {String} content.dto.itemArr.macAddr 终端MAC地址
     * @apiSuccess (响应字段说明) {String} content.dto.itemArr.onlineTime 终端在线时间，单位ms
     * @apiSuccess (响应字段说明) {String="PERSONAL","RECOVERABLE","APP_LAYER"} content.dto.itemArr.pattern 桌面类型
     * @apiSuccess (响应字段说明) {String} content.dto.itemArr.productId 产品ID
     * @apiSuccess (响应字段说明) {String} content.dto.itemArr.productType 产品型号
     * @apiSuccess (响应字段说明) {String} content.dto.itemArr.systemSize 系统盘大小
     * @apiSuccess (响应字段说明) {String} content.dto.itemArr.terminalGroupId 终端组ID
     * @apiSuccess (响应字段说明) {String} content.dto.itemArr.terminalGroupName 终端组名称
     * @apiSuccess (响应字段说明) {String} content.dto.itemArr.terminalName 终端名称
     * @apiSuccess (响应字段说明) {String} content.dto.itemArr.terminalOsType 终端系统类型
     * @apiSuccess (响应字段说明) {String} content.dto.itemArr.bandwidth
     * @apiSuccess (响应字段说明) {String} content.dto.itemArr.bandwidthThreshold
     * @apiSuccess (响应字段说明) {String} content.dto.itemArr.cpuMode
     * @apiSuccess (响应字段说明) {String} content.dto.itemArr.delay
     * @apiSuccess (响应字段说明) {String} content.dto.itemArr.delayThreshold
     * @apiSuccess (响应字段说明) {String} content.dto.itemArr.desktopName
     * @apiSuccess (响应字段说明) {String} content.dto.itemArr.detectState
     * @apiSuccess (响应字段说明) {String} content.dto.itemArr.detectTime
     * @apiSuccess (响应字段说明) {String} content.dto.itemArr.diskSize
     * @apiSuccess (响应字段说明) {String} content.dto.itemArr.enableAutoLogin
     * @apiSuccess (响应字段说明) {String} content.dto.itemArr.enableVisitorLogin
     * @apiSuccess (响应字段说明) {String} content.dto.itemArr.getIpMode
     * @apiSuccess (响应字段说明) {String} content.dto.itemArr.hardwareVersion
     * @apiSuccess (响应字段说明) {String} content.dto.itemArr.ipConflict
     * @apiSuccess (响应字段说明) {String} content.dto.itemArr.lastOfflineTime
     * @apiSuccess (响应字段说明) {String} content.dto.itemArr.memorySize
     * @apiSuccess (响应字段说明) {String} content.dto.itemArr.networkAccessMode
     * @apiSuccess (响应字段说明) {String} content.dto.itemArr.onlineTime
     * @apiSuccess (响应字段说明) {String} content.dto.itemArr.packetLossRate
     * @apiSuccess (响应字段说明) {String} content.dto.itemArr.packetLossRateThreshold
     * @apiSuccess (响应字段说明) {String} content.dto.itemArr.platform
     * @apiSuccess (响应字段说明) {String} content.dto.itemArr.productType
     * @apiSuccess (响应字段说明) {String} content.dto.itemArr.rainOsVersion
     * @apiSuccess (响应字段说明) {String} content.dto.itemArr.rainUpgradeVersion
     * @apiSuccess (响应字段说明) {String} content.dto.itemArr.serialNumber
     * @apiSuccess (响应字段说明) {String} content.dto.itemArr.ssid
     * @apiSuccess (响应字段说明) {Object} content.dto.itemArr.terminalCloudDesktop
     * @apiSuccess (响应字段说明) {Object} content.dto.itemArr.terminalCloudDesktop.allowLocalDisk
     * @apiSuccess (响应字段说明) {Object} content.dto.itemArr.terminalCloudDesktop.deskIp
     * @apiSuccess (响应字段说明) {Object} content.dto.itemArr.terminalCloudDesktop.desktopId
     * @apiSuccess (响应字段说明) {Object} content.dto.itemArr.terminalCloudDesktop.imageTemplateName
     * @apiSuccess (响应字段说明) {Object} content.dto.itemArr.terminalCloudDesktop.pattern
     * @apiSuccess (响应字段说明) {Object} content.dto.itemArr.terminalCloudDesktop.personSize"
     *
     *
     *
     *
     * @apiSuccess (响应字段说明) {String="ONLINE","OFFLINE","UPGRADING"} content.dto.itemArr.terminalState 终端系统类型
     *
     * @apiSuccess (响应字段说明) {String} data.productId 产品ID
     * @apiSuccessExample {json} 成功响应
     *                    {
     *                    "content": {
     *                    "dto": {
     *                    "itemArr": [
     *                    {
     *                    "accessInternet": null,
     *                    "bandwidth": null,
     *                    "bandwidthThreshold": null,
     *                    "bindUserId": null,
     *                    "bindUserName": null,
     *                    "cpuMode": null,
     *                    "createTime": 1585469086194,
     *                    "delay": null,
     *                    "delayThreshold": null,
     *                    "desktopName": null,
     *                    "detectState": null,
     *                    "detectTime": null,
     *                    "diskSize": null,
     *                    "enableAutoLogin": null,
     *                    "enableVisitorLogin": null,
     *                    "getIpMode": null,
     *                    "hardwareVersion": null,
     *                    "id": "58:69:6c:97:d5:95",
     *                    "idvTerminalMode": null,
     *                    "ip": "172.20.114.66",
     *                    "ipConflict": null,
     *                    "lastOfflineTime": null,
     *                    "lastOnlineTime": 1585560679340,
     *                    "macAddr": "58:69:6c:97:d5:95",
     *                    "memorySize": null,
     *                    "networkAccessMode": null,
     *                    "onlineTime": 28842,
     *                    "packetLossRate": null,
     *                    "packetLossRateThreshold": null,
     *                    "platform": null,
     *                    "productType": "RG-Rain410W",
     *                    "productType": "80060036",
     *                    "rainOsVersion": null,
     *                    "rainUpgradeVersion": null,
     *                    "serialNumber": null,
     *                    "ssid": null,
     *                    "terminalCloudDesktop": {
     *                    "allowLocalDisk": null,
     *                    "deskIp": null,
     *                    "desktopId": null,
     *                    "imageTemplateName": null,
     *                    "pattern": null,
     *                    "personSize": null,
     *                    "systemSize": null
     *                    },
     *                    "terminalGroupId": "7769c0c6-473c-4d4c-9f47-5a62bdeb30ba",
     *                    "terminalGroupName": "未分组",
     *                    "terminalGroupNameArr": null,
     *                    "terminalName": "xxxx",
     *                    "terminalOsType": "Linux",
     *                    "terminalState": "ONLINE",
     *                    "wirelessAuthMode": null,
     *                    "wirelessIp": null,
     *                    "wirelessMacAddr": null
     *                    }
     *                    ],
     *                    "total": 1
     *                    },
     *                    "empty": false
     *                    },
     *                    "message": null,
     *                    "msgArgArr": null,
     *                    "msgKey": null,
     *                    "status": "SUCCESS"
     *                    }
     */

    /**
     * 分页列举可选择的IDV镜像终端列表
     *
     * @param request 分页请求
     * @param sessionContext SessionContext
     * @return DefaultWebResponse
     * @throws BusinessException 业务异常
     */
    @RequestMapping(value = "/select/list")
    public DefaultWebResponse listSelectableTerminalIDV(PageWebRequest request, SessionContext sessionContext) throws BusinessException {
        Assert.notNull(request, "request can not be null");
        Assert.notNull(sessionContext, "sessionContext can not be null");

        ExactMatch[] oldExactMatchArr = request.getExactMatchArr();
        if (Objects.isNull(oldExactMatchArr)) {
            throw new BusinessException(CloudDesktopBusinessKey.RCDC_CLOUDDESKTOP_EXACTMATCH_ARR_NOT_EMPTY, new String[] {});
        }
        // 镜像ID
        ExactMatch[] newExactMatchArr =
                Stream.of(oldExactMatchArr).filter(exactMatch -> !exactMatch.getName().equals(IMAGE_ID)).toArray(ExactMatch[]::new);
        request.setExactMatchArr(newExactMatchArr);

        TerminalSelectPageSearchRequest terminalSelectPageSearchRequest = new TerminalSelectPageSearchRequest(request);
        // 选择IDV终端：
        MatchEqual[] oldMatchEqualArr = terminalSelectPageSearchRequest.getMatchEqualArr();
        MatchEqual matchEqualPlatform = new MatchEqual();
        matchEqualPlatform.setName("platform");
        matchEqualPlatform.setValueArr(new Object[] {CbbTerminalPlatformEnums.IDV});

        MatchEqual matchEqualNetwork = new MatchEqual();
        matchEqualNetwork.setName("networkAccessMode");
        matchEqualNetwork.setValueArr(new Object[] {CbbNetworkModeEnums.WIRED});

        MatchEqual matchProxyFilter = new MatchEqual();
        matchProxyFilter.setName("enableProxy");
        matchProxyFilter.setValueArr(new Object[] { false });

        MatchEqual[] newMatchEqualArr = ArrayUtils.add(oldMatchEqualArr, matchEqualPlatform);
        newMatchEqualArr = ArrayUtils.add(newMatchEqualArr, matchEqualNetwork);
        newMatchEqualArr = ArrayUtils.add(newMatchEqualArr, matchProxyFilter);

        terminalSelectPageSearchRequest.setMatchEqualArr(newMatchEqualArr);

        if (!permissionHelper.isAllGroupPermission(sessionContext)) {
            List<String> terminalGroupIdStrList = getPermissionTerminalGroupIdList(sessionContext.getUserId());
            String terminalGroupId = getTerminalGroupId(request);
            if (StringUtils.isEmpty(terminalGroupId)) {
                appendTerminalGroupIdMatchEqual(terminalSelectPageSearchRequest, terminalGroupIdStrList);
            } else {
                if (!terminalGroupIdStrList.contains(terminalGroupId)) {
                    DefaultPageResponse response = new DefaultPageResponse();
                    response.setItemArr(Collections.emptyList().toArray());
                    return DefaultWebResponse.Builder.success(response);
                }
            }
        }
        DefaultPageResponse<TerminalDTO> response = terminalSelectAPI.listSelectableTerminal(terminalSelectPageSearchRequest);
        return DefaultWebResponse.Builder.success(response);
    }

    /**
     * 当前前端只会上传一个groupId进行查询
     */
    private String getTerminalGroupId(PageWebRequest request) {
        if (ArrayUtils.isNotEmpty(request.getExactMatchArr())) {
            for (ExactMatch exactMatch : request.getExactMatchArr()) {
                if (exactMatch.getName().equals("groupId") && exactMatch.getValueArr().length > 0) {
                    return exactMatch.getValueArr()[0];
                }
            }
        }
        return "";
    }

    private List<String> getPermissionTerminalGroupIdList(UUID adminId) throws BusinessException {
        ListTerminalGroupIdRequest listTerminalGroupIdRequest = new ListTerminalGroupIdRequest();
        listTerminalGroupIdRequest.setAdminId(adminId);
        ListTerminalGroupIdResponse listTerminalGroupIdResponse = adminDataPermissionAPI.listTerminalGroupIdByAdminId(listTerminalGroupIdRequest);
        return listTerminalGroupIdResponse.getTerminalGroupIdList();
    }

    private void appendTerminalGroupIdMatchEqual(PageSearchRequest request, List<String> userGroupIdStrList) throws BusinessException {
        List<UUID> uuidList = userGroupIdStrList.stream().filter(idStr -> !idStr.equals(TerminalGroupHelper.TERMINAL_GROUP_ROOT_ID))
                .map(UUID::fromString).collect(Collectors.toList());
        UUID[] uuidArr = uuidList.toArray(new UUID[uuidList.size()]);
        request.appendCustomMatchEqual(new MatchEqual("terminalGroupId", uuidArr));
    }
}
