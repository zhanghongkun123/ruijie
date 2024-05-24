package com.ruijie.rcos.rcdc.rco.module.impl.est;

import com.ruijie.rcos.rcdc.clouddesktop.module.def.dto.CbbDispatcherDTO;

/**
 * Description: Function Description
 * Copyright: Copyright (c) 2021
 * Company: Ruijie Co., Ltd.
 * Create Time: 2021/3/31
 *
 * @author lihengjing
 */
public interface EstCommonActionDispatchService {


    /**
     * @api {SPI} EstCommonActionService.dispatchCommonAction EST跟RCDC透传接口
     * @apiName dispatchCommonAction
     * @apiGroup CbbVmEstCommonActionNotifySPI
     * @apiDescription EST跟RCDC透传接口
     *
     * @param dispatcherDTO CbbDispatcherDTO请求对象
     *
     * @apiParam (请求字段说明) {CbbDispatcherDTO} dispatcherDTO CbbDispatcherDTO
     * @apiParam (请求字段说明) {String} dispatcherDTO.dispatcherKey 消息分发key
     * @apiParam (请求字段说明) {String} dispatcherDTO.terminalId 终端id
     * @apiParam (请求字段说明) {String} dispatcherDTO.requestId 请求id
     * @apiParam (请求字段说明) {String} dispatcherDTO.data 内容
     *           {"subAction":"SNAPSHOT_LIST", "currentVmData":{当前云桌面信息},"data":{"matchArr":[{"deskId":"c3fc4a26-e637-4195-ae3a-92f6494080a2"}],
     *           "sortArr":[{"fieldName":"name","direction":"ASC"}],"page":0,"limit":"100"}}
     * @apiParam (请求字段说明) {String} dispatcherDTO.isNewConnection 是否新连接
     */
    void dispatchCommonAction(CbbDispatcherDTO dispatcherDTO);

}
