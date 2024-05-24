package com.ruijie.rcos.rcdc.rco.module.def.api;

import java.util.List;

import com.ruijie.rcos.rcdc.rco.module.def.api.dto.uws.CbbConfirmQrCodeMobileReqDTO;
import com.ruijie.rcos.rcdc.rco.module.def.api.dto.uws.CbbGetQrCodeReqDTO;
import com.ruijie.rcos.rcdc.rco.module.def.api.dto.uws.CbbQrCodeConfigDTO;
import com.ruijie.rcos.rcdc.rco.module.def.api.dto.uws.CbbQrCodeDTO;
import com.ruijie.rcos.rcdc.rco.module.def.api.dto.uws.CbbQrCodeMobileReqDTO;
import com.ruijie.rcos.rcdc.rco.module.def.api.dto.uws.CbbQrCodeReqDTO;
import com.ruijie.rcos.rcdc.rco.module.def.api.dto.uws.CbbQueryQrCodeReqDTO;
import com.ruijie.rcos.rcdc.rco.module.def.api.enums.CbbQrCodeType;
import com.ruijie.rcos.sk.base.exception.BusinessException;

/**
 * Description: Function Description
 * Copyright: Copyright (c) 2022
 * Company: Ruijie Co., Ltd.
 * Create Time: 2022年01月16日
 *
 * @author xgx
 */
public interface UwsQrCodeAPI {
    /**
     * @api {POST} CbbQrCodeAPI.getQrCode 获取二维码
     * @apiName getQrCode
     * @apiGroup CbbQrCodeAPI
     * @apiDescription 获取二维码
     * @apiParam (请求体字段说明) {CbbQrCodeType="UWS","SF","KRT"} qrCodeType 支持二维码类型，当前仅支持UWS类型
     * @apiParam (请求体字段说明) {String} clientId 客户端标识
     *
     * @apiSuccess (响应字段说明) {String} qrCode 二维码唯一标识
     * @apiSuccess (响应字段说明) {String} content 二维码内容
     * @apiSuccess (响应字段说明) {String} clientId 客户端ID
     * @apiSuccess (响应字段说明) {CbbQrCodeLoginStatus="NO_SCAN","SCANNED","CONFIRMED","LOGIN","INVALID"} status
     * @apiSuccess (响应字段说明) {Long} expireTime 二维码失效时间
     *
     * @apiErrorExample {json} 异常码列表
     *                  {code:rcdc_user_un_support_qr_code_type message:不支持的二维码类型[{0}]}
     *                  {code:rcdc_user_qr_code_type_switch_is_close message:二维码类型[{0}]扫码登录开关已关闭}
     *
     */
    /**
     * 获取二维码
     * 
     * @param cbbGetQrCodeReqDTO qrCodeReqDTO
     * @return 二维码唯一标识
     * @throws BusinessException 业务异常
     */
    CbbQrCodeDTO getQrCode(CbbGetQrCodeReqDTO cbbGetQrCodeReqDTO) throws BusinessException;

    /**
     * @api {POST} CbbQrCodeAPI.refreshQrCode 刷新二维码
     * @apiName refreshQrCode
     * @apiGroup CbbQrCodeAPI
     * @apiDescription 刷新二维码
     * @apiParam (请求体字段说明) {CbbQrCodeType="UWS","SF","KRT"} qrCodeType 支持二维码类型，当前仅支持UWS类型
     * @apiParam (请求体字段说明) {String} clientId 客户端标识
     * @apiParam (请求体字段说明) {String} qrCode 原二维码唯一标识
     *
     * @apiSuccess (响应字段说明) {String} qrCode 二维码唯一标识
     * @apiSuccess (响应字段说明) {String} content 二维码内容
     * @apiSuccess (响应字段说明) {String} clientId 客户端ID
     * @apiSuccess (响应字段说明) {CbbQrCodeLoginStatus="NO_SCAN","SCANNED","CONFIRMED","LOGIN","INVALID"} status
     * @apiSuccess (响应字段说明) {Long} expireTime 二维码失效时间
     *
     * @apiErrorExample {json} 异常码列表
     *                  {code:rcdc_user_un_support_qr_code_type message:不支持的二维码类型[{0}]}
     *                  {code:rcdc_user_qr_code_type_switch_is_close message:二维码类型[{0}]扫码登录开关已关闭}
     *                  {code:rcdc_user_client_qr_code_not_match message:二维码客户端信息不匹配}
     *
     */
    /**
     * 刷新二维码
     * 
     * @param qrCodeReqDTO qrCodeReqDTO
     * @return 新的二维码
     * @throws BusinessException 业务异常
     */
    CbbQrCodeDTO refreshQrCode(CbbQrCodeReqDTO qrCodeReqDTO) throws BusinessException;

    /**
     * @api {POST} CbbQrCodeAPI.queryQrCode 查询二维码信息
     * @apiName queryQrCode
     * @apiGroup CbbQrCodeAPI
     * @apiDescription 查询二维码信息
     * @apiParam (请求体字段说明) {CbbQrCodeType="UWS","SF","KRT"} qrCodeType 支持二维码类型，当前仅支持UWS类型
     * @apiParam (请求体字段说明) {String} clientId 客户端标识
     * @apiParam (请求体字段说明) {String} qrCode 二维码唯一标识
     *
     * @apiSuccess (响应字段说明) {String} qrCode 二维码唯一标识
     * @apiSuccess (响应字段说明) {String} content 二维码内容
     * @apiSuccess (响应字段说明) {String} clientId 客户端ID
     * @apiSuccess (响应字段说明) {CbbQrCodeLoginStatus="NO_SCAN","SCANNED","CONFIRMED","LOGIN","INVALID"} status
     * @apiSuccess (响应字段说明) {Long} expireTime 二维码失效时间
     * @apiSuccess (响应字段说明) {String} userData 用户信息
     * @apiErrorExample {json} 异常码列表
     *                  {code:rcdc_user_un_support_qr_code_type message:不支持的二维码类型[{0}]}
     *                  {code:rcdc_user_qr_code_type_switch_is_close message:二维码类型[{0}]扫码登录开关已关闭}
     *                  {code:rcdc_user_client_qr_code_not_match message:二维码客户端信息不匹配}
     *
     */
    /**
     * 查询二维码信息
     * 
     * @param queryQrCodeReqDTO queryQrCodeReqDTO
     * @return 二维码信息
     * @throws BusinessException 业务异常
     */
    CbbQrCodeDTO queryQrCode(CbbQueryQrCodeReqDTO queryQrCodeReqDTO) throws BusinessException;

    /**
     * @api {POST} CbbQrCodeAPI.qrLogin 登录
     * @apiName qrLogin
     * @apiGroup CbbQrCodeAPI
     * @apiDescription 登录
     * @apiParam (请求体字段说明) {CbbQrCodeType="UWS","SF","KRT"} qrCodeType 支持二维码类型，当前仅支持UWS类型
     * @apiParam (请求体字段说明) {String} clientId 客户端标识
     * @apiParam (请求体字段说明) {String} qrCode 二维码唯一标识
     *
     * @apiSuccess (响应字段说明) {String} qrCode 二维码唯一标识
     * @apiSuccess (响应字段说明) {String} content 二维码内容
     * @apiSuccess (响应字段说明) {String} clientId 客户端ID
     * @apiSuccess (响应字段说明) {CbbQrCodeLoginStatus="NO_SCAN","SCANNED","CONFIRMED","LOGIN","INVALID"} status
     * @apiSuccess (响应字段说明) {Long} expireTime 二维码失效时间
     * @apiSuccess (响应字段说明) {String} userData 用户信息
     * @apiErrorExample {json} 异常码列表
     *                  {code:rcdc_user_un_support_qr_code_type message:不支持的二维码类型[{0}]}
     *                  {code:rcdc_user_qr_code_type_switch_is_close message:二维码类型[{0}]扫码登录开关已关闭}
     *                  {code:rcdc_user_client_qr_code_not_match message:二维码客户端信息不匹配}
     *                  {code:rcdc_user_qr_code_status_not_expect message:二维码状态[{0}]与预期状态[{1}]不匹配}
     *                  {code:rcdc_user_qr_code_not_exist_or_expire message:二维码不存在或者已过期}
     *
     */
    /**
     * 登录
     * 
     * @param qrCodeReqDTO qrCodeReqDTO
     * @return 二维码信息
     * @throws BusinessException 业务异常
     */
    CbbQrCodeDTO qrLogin(CbbQrCodeReqDTO qrCodeReqDTO) throws BusinessException;


    /**
     * @api {POST} CbbQrCodeAPI.scanQrCode 扫码
     * @apiName scanQrCode
     * @apiGroup CbbQrCodeAPI
     * @apiDescription 扫码
     * @apiParam (请求体字段说明) {CbbQrCodeType="UWS","SF","KRT"} qrCodeType 支持二维码类型，当前仅支持UWS类型
     * @apiParam (请求体字段说明) {String} qrCode 二维码唯一标识
     *
     * @apiErrorExample {json} 异常码列表
     *                  {code:rcdc_user_un_support_qr_code_type message:不支持的二维码类型[{0}]}
     *                  {code:rcdc_user_qr_code_type_switch_is_close message:二维码类型[{0}]扫码登录开关已关闭}
     *                  {code:rcdc_user_qr_code_status_not_expect message:二维码状态[{0}]与预期状态[{1}]不匹配}
     *                  {code:rcdc_user_qr_code_not_exist_or_expire message:二维码不存在或者已过期}
     *
     */
    /**
     * 扫码
     *
     * @param qrCodeMobileReq qrCodeMobileReq
     * @throws BusinessException 业务异常
     */
    void scanQrCode(CbbQrCodeMobileReqDTO qrCodeMobileReq) throws BusinessException;

    /**
     * @api {POST} CbbQrCodeAPI.confirmQrLogin 确认登录
     * @apiName confirmQrLogin
     * @apiGroup CbbQrCodeAPI
     * @apiDescription 确认登录
     *
     * @apiParam (请求体字段说明) {CbbConfirmQrCodeMobileReqDTO} confirmQrCodeMobileReqDTO 二维码客户端确认登录请求对象
     * @apiParam (请求体字段说明) {CbbQrCodeType="UWS","SF","KRT"} confirmQrCodeMobileReqDTO.qrCodeType 支持二维码类型，当前仅支持UWS类型
     * @apiParam (请求体字段说明) {String} confirmQrCodeMobileReqDTO.qrCode 二维码唯一标识
     * @apiParam (请求体字段说明) {String} confirmQrCodeMobileReqDTO.[userData] 用户数据
     * @apiErrorExample {json} 异常码列表
     *                  {code:rcdc_user_un_support_qr_code_type message:不支持的二维码类型[{0}]}
     *                  {code:rcdc_user_qr_code_type_switch_is_close message:二维码类型[{0}]扫码登录开关已关闭}
     *                  {code:rcdc_user_qr_code_status_not_expect message:二维码状态[{0}]与预期状态[{1}]不匹配}
     *                  {code:rcdc_user_qr_code_not_exist_or_expire message:二维码不存在或者已过期}
     *
     */
    /**
     * 确认登录
     *
     * @param confirmQrCodeMobileReqDTO confirmQrCodeMobileReqDTO
     * @throws BusinessException 业务异常
     */
    void confirmQrLogin(CbbConfirmQrCodeMobileReqDTO confirmQrCodeMobileReqDTO) throws BusinessException;

    /**
     * @api {POST} CbbQrCodeAPI.cancelQrLogin 取消登录
     * @apiName cancelQrLogin
     * @apiGroup CbbQrCodeAPI
     * @apiDescription 取消登录
     *
     *
     * @apiParam (请求体字段说明) {CbbQrCodeType="UWS","SF","KRT"} qrCodeMobileReq.qrCodeType 支持二维码类型，当前仅支持UWS类型
     * @apiParam (请求体字段说明) {String} qrCodeMobileReq.qrCode 二维码唯一标识
     * @apiErrorExample {json} 异常码列表
     *                  {code:rcdc_user_un_support_qr_code_type message:不支持的二维码类型[{0}]}
     *                  {code:rcdc_user_qr_code_type_switch_is_close message:二维码类型[{0}]扫码登录开关已关闭}
     *                  {code:rcdc_user_qr_code_status_not_expect message:二维码状态[{0}]与预期状态[{1}]不匹配}
     *                  {code:rcdc_user_qr_code_not_exist_or_expire message:二维码不存在或者已过期}
     *
     */
    /**
     * 取消登录
     *
     * @param qrCodeMobileReq qrCodeMobileReq
     * @throws BusinessException 业务异常
     */
    void cancelQrLogin(CbbQrCodeMobileReqDTO qrCodeMobileReq) throws BusinessException;

    /**
     * @api {POST} CbbQrCodeAPI.saveQrCodeConfig 保存二维码配置
     * @apiName saveQrCodeConfig
     * @apiGroup CbbQrCodeAPI
     * @apiDescription 保存二维码配置
     *
     *
     * @apiParam (请求体字段说明) {CbbQrCodeType="UWS","SF","KRT"} qrCodeType 二维码类型，当前仅支持UWS类型
     * @apiParam (请求体字段说明) {Integer} order 排序号
     * @apiParam (请求体字段说明) {Boolean} openSwitch 二维码开关，默认为关闭
     * @apiParam (请求体字段说明) {Long} [expireTime] 二维码超时时间，单位毫秒，默认900000毫秒
     * @apiParam (响应体字段说明) {String[..1024]} contentPrefix 二维码内容前缀，
     * @apiParam (请求体字段说明) {String} [advanceConfig] 更多配置
     ** @apiErrorExample {json} 异常码列表
     *                  {code:rcdc_user_un_support_qr_code_type message:不支持的二维码类型[{0}]}
     *
     */
    /**
     * 保存二维码配置
     *
     * @param qrCodeConfigReqDTO qrCodeConfigReqDTO
     * @throws BusinessException 业务异常
     */
    void saveQrCodeConfig(CbbQrCodeConfigDTO qrCodeConfigReqDTO) throws BusinessException;

    /**
     * @api {POST} CbbQrCodeAPI.getQrCodeConfig 获取二维码配置
     * @apiName getQrCodeConfig
     * @apiGroup CbbQrCodeAPI
     * @apiDescription 获取二维码配置
     *
     * @apiParam (请求体字段说明) {CbbQrCodeType="UWS","SF","KRT"} qrCodeType 二维码类型，当前仅支持UWS类型
     *
     * @apiSuccess (响应体字段说明) {CbbQrCodeType="UWS","SF","KRT"} qrCodeType 二维码类型，当前仅支持UWS类型
     * @apiSuccess (响应体字段说明) {Integer} order 排序号
     * @apiSuccess (响应体字段说明) {Boolean} openSwitch 二维码开关，默认为关闭
     * @apiSuccess (响应体字段说明) {Long} expireTime 二维码超时时间，单位毫秒，默认900000毫秒
     * @apiSuccess (响应体字段说明) {String} contentPrefix 二维码内容前缀
     * @apiSuccess (响应体字段说明) {String} advanceConfig 更多配置
     ** @apiErrorExample {json} 异常码列表
     *                  {code:rcdc_user_un_support_qr_code_type message:不支持的二维码类型[{0}]}
     *
     */
    /**
     * 获取二维码配置
     *
     * @param qrCodeType qrCodeType
     * @return 二维码配置
     * @throws BusinessException 业务异常
     */
    CbbQrCodeConfigDTO getQrCodeConfig(CbbQrCodeType qrCodeType) throws BusinessException;

    /**
     * @api {POST} CbbQrCodeAPI.getSupportQrCodeTypeList 获取支持的扫码类型列表，并按order从低到高排序
     * @apiName getSupportQrCodeTypeList
     * @apiGroup CbbQrCodeAPI
     * @apiDescription 获取支持的扫码类型列表，并按order从低到高排序
     */
    /**
     * 获取支持的扫码类型列表，并按order从低到高排序
     * 
     * @return 二维码类型列表
     * @throws BusinessException 业务异常
     */
    List<CbbQrCodeType> getSupportQrCodeTypeList() throws BusinessException;

}
