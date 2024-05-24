package com.ruijie.rcos.rcdc.rco.module.def.api;

import com.ruijie.rcos.rcdc.rco.module.def.api.request.FindParameterListRequest;
import com.ruijie.rcos.rcdc.rco.module.def.api.request.FindParameterRequest;
import com.ruijie.rcos.rcdc.rco.module.def.api.request.UpdateParameterRequest;
import com.ruijie.rcos.rcdc.rco.module.def.api.response.FindParameterListResponse;
import com.ruijie.rcos.rcdc.rco.module.def.api.response.FindParameterResponse;
import com.ruijie.rcos.sk.modulekit.api.comm.DefaultResponse;
import org.springframework.lang.Nullable;

import java.util.List;

/**
 * <p>Title: AdminMgmtAPI</p>
 * <p>Description: Function Description</p>
 * <p>Copyright: Ruijie Co., Ltd. (c) 2020</p>
 * <p>@Author: xiejian</p>
 * <p>@Date: 2020/1/7 14:51</p>
 */
public interface RcoGlobalParameterAPI {

    /**
     * 根据key查询全局参数
     *
     * @param request 请求
     * @return FindParameterResponse 响应
     */
    FindParameterResponse findParameter(FindParameterRequest request);

    /**
     * 修改全局参数
     *
     * @param request 请求
     * @return DefaultResponse 响应
     */
    DefaultResponse updateParameter(UpdateParameterRequest request);

    /**
     * 保存
     *
     * @param request 请求
     * @return DefaultResponse 响应
     */
    DefaultResponse saveParameter(UpdateParameterRequest request);


    /**
     * 根据key查询全局参数（批量）
     *
     * @param request 请求
     * @return FindParameterResponse 响应
     */
    List<FindParameterListResponse> findParameters(FindParameterListRequest request);

}
