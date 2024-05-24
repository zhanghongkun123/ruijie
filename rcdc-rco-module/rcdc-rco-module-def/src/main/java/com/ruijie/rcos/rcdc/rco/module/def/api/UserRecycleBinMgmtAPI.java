package com.ruijie.rcos.rcdc.rco.module.def.api;

import com.ruijie.rcos.rcdc.rco.module.def.api.dto.CloudDesktopDTO;
import com.ruijie.rcos.rcdc.rco.module.def.api.request.PageSearchRequest;
import com.ruijie.rcos.sk.base.exception.BusinessException;
import com.ruijie.rcos.sk.modulekit.api.comm.DefaultPageResponse;
import com.ruijie.rcos.sk.modulekit.api.comm.IdRequest;
import org.springframework.lang.Nullable;

import java.util.List;
import java.util.UUID;


/**
 * *云桌面回收站功能接口
 * Description: Function Description
 * Copyright: Copyright (c) 2018
 * Company: Ruijie Co., Ltd.
 * Create Time: 2018年12月28日
 * 
 * @author
 */
public interface UserRecycleBinMgmtAPI {

    /**
     * * 分页查询云桌面
     * 
     * @param request 页面查询请求参数
     * @throws BusinessException 业务异常
     * @return reponse CloudDesktopVO 对象
     */
    
    DefaultPageResponse<CloudDesktopDTO> pageQuery(PageSearchRequest request) throws BusinessException;

    /**
     * * 取得所有回收站桌面
     *
     * @return 所有已删除云桌面列表
     * @throws BusinessException 业务异常
     * @throws BusinessException
     */
    DefaultPageResponse<CloudDesktopDTO> getAll() throws BusinessException;

    /**
     * *删除指定回收站云桌面
     * 
     * @param request id
     * @throws BusinessException 业务异常
     */
    void delete(IdRequest request) throws BusinessException;

    /**
     * 强制将桌面从数据库中删除
     *
     * @param deskId 桌面ID
     * @throws BusinessException 业务异常
     */
    void deleteDeskFromDb(UUID deskId) throws BusinessException;

    /**
     * *云桌面恢复
     * 
     * @param deskId 桌面id
     * @param assignUserId 指定用户id
     * @throws BusinessException 业务异常
     */
    void recover(UUID deskId, @Nullable UUID assignUserId) throws BusinessException;

    /**
     * 根据用户Id集合查询回收站桌面信息
     * @param userIdList 用户ID
     * @return 回收站云桌面
     */
    List<CloudDesktopDTO> getAllDesktopByUserIdList(List<UUID> userIdList);

    /**
     * *指定桌面池-云桌面恢复
     *
     * @param deskId 桌面id
     * @param userIdList 桌面关联用户id列表
     * @param assignDesktopPoolId 指定桌面池
     * @throws BusinessException 业务异常
     */
    void recoverAssignDesktopPool(UUID deskId, @Nullable List<UUID> userIdList, UUID assignDesktopPoolId) throws BusinessException;
}
