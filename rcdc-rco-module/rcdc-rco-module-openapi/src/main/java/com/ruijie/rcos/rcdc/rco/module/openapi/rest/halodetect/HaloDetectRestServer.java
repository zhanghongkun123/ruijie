package com.ruijie.rcos.rcdc.rco.module.openapi.rest.halodetect;

import com.ruijie.rcos.rcdc.hciadapter.module.def.dto.cloudplatform.CloudPlatformDTO;
import com.ruijie.rcos.rcdc.rco.module.def.commonupgrade.dto.GuideImageTemplateDTO;
import com.ruijie.rcos.rcdc.rco.module.def.customerinfo.dto.CustomerInfoDTO;
import com.ruijie.rcos.rcdc.rco.module.def.license.response.ObtainIdvLicenseAuthStateResponse;
import com.ruijie.rcos.rcdc.rco.module.openapi.rest.halodetect.response.CommonWebResponse;
import com.ruijie.rcos.sk.base.exception.BusinessException;
import com.ruijie.rcos.sk.pagekit.api.PageQueryRequest;
import com.ruijie.rcos.sk.pagekit.api.PageQueryResponse;

import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import java.util.List;

/**
 * Description: halo对接，检测接口
 * Copyright: Copyright (c) 2019
 * Company: Ruijie Co., Ltd.
 * Create Time: 2021/3/23 20:01
 *
 * @author ketb
 */
@Path("/haloCheck")
public interface HaloDetectRestServer {

    /**
     * @api {GET} rest/haloCheck/checkGtVersion
     * @apiName 检测镜像gt版本
     * @apiGroup halo对接
     * @apiDescription halo检测镜像gt版本
     *
     * @apiSuccess (响应字段说明) {int} code 响应码
     * @apiSuccess (响应字段说明) {String} message 响应信息
     * @apiSuccess (响应字段说明) {String} status 成功失败状态
     * @apiSuccess (响应字段说明) {JSON} content 响应数据
     * @apiSuccessExample {json} 成功响应
     *                  {
     *                    "content": "",
     *                    "message": "操作成功",
     *                    "msgArgArr": null,
     *                    "msgKey": null,
     *                    "status": "SUCCESS"
     *                }
     *
     * @apiErrorExample {json} 异常响应
     *                  {
     *                  "content": null,
     *                  "message": "系统内部错误，请联系管理员",
     *                  "msgArgArr": [],
     *                  "msgKey": "sk_webmvckit_internal_error",
     *                  "status": "ERROR"
     *                  }
     */
    /**
     * 检测镜像gt版本是否低于服务器gt版本
     * @return 检测结果
     */
    @GET
    @Path("/checkGtVersion")
    CommonWebResponse<String[]> checkGtVersion();

    /**
     * @api {GET} rest/haloCheck/checkRcdcVersion
     * @apiName 检测rcdc版本
     * @apiGroup halo对接
     * @apiDescription halo检测rcdc版本
     *
     * @apiSuccess (响应字段说明) {int} code 响应码
     * @apiSuccess (响应字段说明) {String} message 响应信息
     * @apiSuccess (响应字段说明) {String} status 成功失败状态
     * @apiSuccess (响应字段说明) {JSON} content 响应数据
     * @apiSuccessExample {json} 成功响应
     *                  {
     *                    "content": "",
     *                    "message": "操作成功",
     *                    "msgArgArr": null,
     *                    "msgKey": null,
     *                    "status": "SUCCESS"
     *                }
     *
     * @apiErrorExample {json} 异常响应
     *                  {
     *                  "content": null,
     *                  "message": "系统内部错误，请联系管理员",
     *                  "msgArgArr": [],
     *                  "msgKey": "sk_webmvckit_internal_error",
     *                  "status": "ERROR"
     *                  }
     */
    /**
     * 检测镜像gt版本是否低于服务器gt版本
     * @return 检测结果
     */
    @GET
    @Path("/checkRcdcVersion")
    CommonWebResponse<String[]> checkRCDCVersion();

    /**
     * 检测服务器是否导入idv终端正版授权
     * @return 请求响应
     */
    @GET
    @Path("/checkIdvAuth")
    CommonWebResponse<ObtainIdvLicenseAuthStateResponse> checkIdvAuth();


    /**
     * 导入终端配置文件
     * @return 请求响应
     */
    @GET
    @Path("/importTerminalConfig")
    CommonWebResponse<String[]> importTerminalConfig();


    /**
     * 导入终端配置文件
     * @return 请求响应
     */
    @GET
    @Path("/terminalConfig/checkVersion")
    CommonWebResponse<String> getTerminalConfigVersion();

    /**
     * 查询云平台列表
     *
     * @param request 分页查询
     * @return 云平台列表
     * @throws BusinessException 业务异常
     */
    @POST
    @Path("/cloudPlatform/list")
    PageQueryResponse<CloudPlatformDTO> cloudPlatformList(PageQueryRequest request) throws BusinessException;

    /**
     * 获取客户信息
     *
     * @return 返回值
     **/
    @GET
    @Path("/customInfo")
    CustomerInfoDTO getCustomInfo();



    /**
     * 校验通用组件向导
     * @return 请求响应
     * @throws BusinessException 业务异常
     */
    @GET
    @Path("/commonUpgrade/guide")
    CommonWebResponse<List<GuideImageTemplateDTO>> commonUpgradeGuide() throws BusinessException;

}
