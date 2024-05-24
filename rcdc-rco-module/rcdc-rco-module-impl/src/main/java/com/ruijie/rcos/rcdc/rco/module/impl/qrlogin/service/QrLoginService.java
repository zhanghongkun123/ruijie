package com.ruijie.rcos.rcdc.rco.module.impl.qrlogin.service;

import com.ruijie.rcos.rcdc.rco.module.impl.spi.dto.ShineLoginDTO;
import com.ruijie.rcos.sk.base.exception.BusinessException;
import org.springframework.lang.Nullable;

import java.util.UUID;

/**
 * Description: QrLoginService
 * Copyright: Copyright (c) 2021
 * Company: Ruijie Co., Ltd.
 * Create Time: 2021年5月6日
 *
 * @author zhang.zhiwen
 */
public interface QrLoginService {

    /**
     * 保存代理服务器器存储的ID
     * @param id UUID
     * @param phone 号码（扫码用户账号）
     */
    void saveQrLoginId(UUID id, String phone);

    /**
     * 查询获取用户信息。若用户名为空，则根据id到缓存获取用户名后构造用户信息；若用户名不为空，则直接使用用户名构造
     * @param id UUID
     * @param userName 用户名
     * @return ShineLoginDTO
     * @throws BusinessException BusinessException
     */
    ShineLoginDTO qryLoginDto(String id, @Nullable String userName) throws BusinessException;

}
