package com.ruijie.rcos.rcdc.rco.module.def.api;

import com.ruijie.rcos.rcdc.rco.module.def.api.dto.filedistribution.DistributeFileDTO;
import com.ruijie.rcos.rcdc.rco.module.def.api.request.filedistribution.CreateDistributeFileRequest;
import com.ruijie.rcos.rcdc.rco.module.def.api.request.filedistribution.EditDistributeFileRequest;
import com.ruijie.rcos.sk.base.exception.BusinessException;

import java.util.UUID;

/**
 * Description: 文件分发功能——文件管理API
 * Copyright: Copyright (c) 2022
 * Company: Ruijie Co., Ltd.
 * Create Time: 2022/2/11 15:08
 *
 * @author zhangyichi
 */
public interface FileDistributionFileManageAPI {

    /**
     * 创建新文件记录
     * @param request 创建请求
     * @return 新文件ID
     * @throws BusinessException 业务异常
     */
    UUID createFile(CreateDistributeFileRequest request) throws BusinessException;

    /**
     * 根据文件名查找文件记录
     * @param fileName 文件名
     * @return 文件记录
     */
    DistributeFileDTO findByName(String fileName);

    /**
     * 根据文件ID查找文件记录
     * @param fileId 文件ID
     * @return 文件记录
     */
    DistributeFileDTO findById(UUID fileId);

    /**
     * 编辑文件信息
     * @param request 编辑请求
     * @throws BusinessException 业务异常
     */
    void editFile(EditDistributeFileRequest request) throws BusinessException;

    /**
     * 删除分发文件记录
     * @param fileId 文件ID
     * @param saveDir 文件存放目录
     * @throws BusinessException 业务异常
     */
    void deleteFile(UUID fileId, String saveDir) throws BusinessException;

    /**
     * 根据软件安装包id判定是否正在被文件推送使用
     *
     * @param fileId 软件安装包 id
     * @throws BusinessException 业务异常
     */
    void existsUsed(UUID fileId) throws BusinessException;
}
