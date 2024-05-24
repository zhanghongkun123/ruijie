package com.ruijie.rcos.rcdc.rco.module.def.api;

import com.ruijie.rcos.rcdc.rco.module.def.api.wifi.request.DeleteWifiWhitelistRequest;
import com.ruijie.rcos.rcdc.rco.module.def.api.wifi.request.SaveWifiWhitelistRequest;
import com.ruijie.rcos.rcdc.rco.module.def.api.wifi.response.GetWifiWhitelistResponse;
import com.ruijie.rcos.sk.base.exception.BusinessException;
import org.springframework.lang.NonNull;

import java.util.UUID;

/**
 * @ClassName: WifiWhitelistAPI
 * @Description: wifi白名单API接口
 * @author: zhiweiHong
 * @date: 2020/7/29
 **/
public interface WifiWhitelistAPI {
    /**
     * 更新wifi白名单
     * <p>
     * 如果<code>needApplyToSubgroup</code>为true
     * 则同步下组白名单，且异步通知当前组下的所有在线终端白名单信息
     *
     * @param request 保存白名单实体 {@link SaveWifiWhitelistRequest}
     */
    void updateWifiWhitelist(@NonNull SaveWifiWhitelistRequest request);

    /**
     * 创建wifi白名单
     * 如果白名单集合为空，则直接返回
     *
     * @param request 保存白名单实体 {@link SaveWifiWhitelistRequest}
     */
    void createWifiWhitelist(@NonNull SaveWifiWhitelistRequest request);

    /**
     * 优先删除原先终端组的白名单
     * 异步通知原先分组下的终端新白名单信息
     *
     * @param request 删除终端实体 request {@link DeleteWifiWhitelistRequest}
     */
    void deleteWifiWhitelist(@NonNull DeleteWifiWhitelistRequest request);

    /**
     * 获取当前终端组的白名单信息
     *
     * @param terminalGroupId 终端组ID
     * @return 返回白名单信息 {@link GetWifiWhitelistResponse}
     */
    GetWifiWhitelistResponse getWifiWhitelistByTerminalGroupId(@NonNull UUID terminalGroupId);

    /**
     * 当目标组存在白名单时通知IDV白名单信息
     *
     * @param targetGroupId 目标组ID
     * @param terminalId 终端ID
     * @throws BusinessException 查询不到对应终端时抛出异常
     */
    void notifyIdvIfTargetGroupHasWhitelist(@NonNull UUID targetGroupId, @NonNull String terminalId) throws BusinessException;
}
