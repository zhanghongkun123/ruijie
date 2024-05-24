package com.ruijie.rcos.rcdc.rco.module.def.api;

import com.ruijie.rcos.rcdc.rco.module.def.api.dto.user.UserLoginRecordDTO;
import com.ruijie.rcos.rcdc.rco.module.def.api.request.UserLoginRecordPageRequest;
import com.ruijie.rcos.sk.base.exception.BusinessException;
import com.ruijie.rcos.sk.modulekit.api.comm.DefaultPageResponse;
import org.springframework.lang.Nullable;

import java.util.Date;
import java.util.UUID;

/**
 * @author zjy
 * @version 1.0.0
 * @Description 登录记录
 * @createTime 2021-11-02 10:13:00
 */
public interface UserLoginRecordAPI {

    /**
     * @title 分页查询
     * @description
     * @author zjy
     * @updateTime 2021/10/28 16:24
     * @param request 请求参数
     * @return 分页返回数据
     */
    DefaultPageResponse<UserLoginRecordDTO> pageQuery(UserLoginRecordPageRequest request);

    /**
     * 查询最新创建的记录
     * @param createTime createTime
     * @return 记录
     * @throws BusinessException 业务异常
     */
    UserLoginRecordDTO findLastByCreateTime(Date createTime);

    /**
     * 根据桌面删除远程协助缓存
     * @param deskId 桌面
     */
    void deleteRemoteAssistanceCache(String deskId);

}
