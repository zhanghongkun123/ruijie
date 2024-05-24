package com.ruijie.rcos.rcdc.rco.module.openapi.rest.qrlogin;

import com.ruijie.rcos.rcdc.rco.module.openapi.rest.qrlogin.request.QrLoginRestServerRequest;
import com.ruijie.rcos.rcdc.rco.module.openapi.rest.qrlogin.response.QrLoginRestServerResponse;

import javax.ws.rs.POST;
import javax.ws.rs.Path;

/**
 * Description: 代理服务器,登录缓存
 * Copyright: Copyright (c) 2021
 * Company: Ruijie Co., Ltd.
 * Create Time: 2021/4/28 20:01
 *
 * @author zhang.zhiwen
 */
@Path("/qrLogin")
public interface QrLoginRestServer {

    /**
     * 代理服务器向RCDC缓存用户数据
     * @api {POST} rest/qrLogin/saveCache 代理服务器向RCDC缓存用户数据
     * @apiDescription 代理服务器向RCDC缓存用户数据
     * @param request 请求体
     * @apiParamExample {json} 请求体示例
     * {
     * "content":{
     *     "id":"671f6436-3694-44ed-910b-aa7bf401c0db",
     *     "phone":"18155554623",
     *     }
     * }
     * @return 响应
     * {
     *     "content": {
     *         "code": 0,
     *         "data": {
     *             "cache_res": "SUCCESS"
     *         }
     *     }
     * }
     */
    @POST
    @Path("/saveCache")
    QrLoginRestServerResponse saveCache(QrLoginRestServerRequest request);
}
