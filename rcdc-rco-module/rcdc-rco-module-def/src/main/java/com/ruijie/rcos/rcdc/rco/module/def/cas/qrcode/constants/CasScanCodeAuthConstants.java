package com.ruijie.rcos.rcdc.rco.module.def.cas.qrcode.constants;

/**
 * Description: 全局表常量
 * Copyright: Copyright (c) 2021
 * Company: Ruijie Co., Ltd.
 * Create Time: 2021/6/23
 *
 * @author TD
 */
public interface CasScanCodeAuthConstants {

    /**
     * CAS认证配置信息KEY
     */
    String CAS_SCAN_CODE_AUTH = "cas_scan_code_auth";

    /**
     * ID KEY键
     */
    String ID = "id";

    /**
     * 票据
     */
    String TICKET = "ticket";

    /**
     * 验证结果KEY
     */
    String STATUS = "status";

    /**
     * 验证返回用户名KEY
     */
    String USER_NAME = "userName";

    /**
     * 验证返回成功
     */
    String SUCCESS = "SUCCESS";

    /**
     * 验证返回用户工号
     */
    String USER_CODE = "userCode";

    /**
     * RCDC推送CAS扫码认证信息
     */
    String PUSH_CAS_QR_CONFIG = "push_cas_qr_config";
}
