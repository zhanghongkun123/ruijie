package com.ruijie.rcos.rcdc.rco.module.web.ctrl.alarm;

import com.ruijie.rcos.base.task.module.def.api.BaseMsgMgmtAPI;
import com.ruijie.rcos.base.task.module.def.api.request.msg.BaseGetMsgPageRequest;
import com.ruijie.rcos.base.task.module.def.api.request.msg.BaseGetMsgRequest;
import com.ruijie.rcos.base.task.module.def.api.request.msg.BaseGetSubMsgPageRequest;
import com.ruijie.rcos.base.task.module.def.dto.msg.BaseMsgDTO;
import com.ruijie.rcos.rcdc.maintenance.module.def.annotate.NoBusinessMaintenanceUrl;
import com.ruijie.rcos.rcdc.rco.module.def.msg.RcoMsgDTO;
import com.ruijie.rcos.rcdc.rco.module.web.ctrl.alarm.request.GetMsgPageRequest;
import com.ruijie.rcos.rcdc.rco.module.web.ctrl.alarm.request.GetMsgWebRequest;
import com.ruijie.rcos.rcdc.rco.module.web.ctrl.alarm.request.GetSubMsgPageRequest;
import com.ruijie.rcos.rcdc.rco.module.web.util.MsgCodeUtil;
import com.ruijie.rcos.sk.base.exception.BusinessException;
import com.ruijie.rcos.sk.modulekit.api.comm.DefaultPageResponse;
import com.ruijie.rcos.sk.webmvc.api.annotation.NoMaintenanceUrl;
import com.ruijie.rcos.sk.webmvc.api.annotation.NoRefreshSession;
import com.ruijie.rcos.sk.webmvc.api.response.DefaultWebResponse;
import com.ruijie.rcos.sk.webmvc.api.response.DefaultWebResponse.Builder;
import com.ruijie.rcos.sk.webmvc.api.session.SessionContext;
import com.ruijie.rcos.sk.webmvc.api.vo.ExactMatch;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * <br>
 * Description: Function Description <br>
 * Copyright: Copyright (c) 2018 <br>
 * Company: Ruijie Co., Ltd. <br>
 * Create Time: 2018/12/26 <br>
 *
 * @author dell
 */
@Controller
@RequestMapping("/rco/msgct")
public class MsgCtrl {

    @Autowired
    private BaseMsgMgmtAPI baseMsgMgmtAPI;

    /**
     * 获取消息列表
     *
     * @param getMsgPageRequest 获取消息请求参数
     * @param sessionContext 返回消息列表结果
     * @return 消息列表
     * @throws BusinessException 异常
     */
    @RequestMapping(value = "msg/list")
    @NoBusinessMaintenanceUrl
    public DefaultWebResponse getMsgPage(GetMsgPageRequest getMsgPageRequest, SessionContext sessionContext) throws BusinessException {
        Assert.notNull(getMsgPageRequest, "getMsgPageRequest can not be null");
        Assert.notNull(sessionContext, "sessionContext can not be null");

        UUID userId = sessionContext.getUserId();
        BaseGetMsgPageRequest baseGetMsgPageRequest = convertBaseGetMsgPageRequest(getMsgPageRequest, userId);

        DefaultPageResponse<BaseMsgDTO> msgPage = baseMsgMgmtAPI.getMsgPage(baseGetMsgPageRequest);

        Object[] itemArr = msgPage.getItemArr();
        List<RcoMsgDTO> rcoMsgDTOList = new ArrayList<>(itemArr.length);
        Stream.of(itemArr).forEach(dto -> {
            RcoMsgDTO rcoMsgDTO = new RcoMsgDTO();
            BeanUtils.copyProperties(dto, rcoMsgDTO);
            rcoMsgDTO.setMsgCode(MsgCodeUtil.getMsgCode(((BaseMsgDTO) dto).getMsgName()));
            rcoMsgDTOList.add(rcoMsgDTO);
        });

        DefaultPageResponse<RcoMsgDTO> result = new DefaultPageResponse<>();
        result.setTotal(msgPage.getTotal());
        result.setItemArr(rcoMsgDTOList.toArray(new RcoMsgDTO[0]));
        return Builder.success(result);

    }


    /**
     * 获取子消息列表
     *
     * @param getSubMsgPageRequest 获取子消息请求列表
     * @return 返回子消息列表
     * @throws BusinessException 异常
     */
    @RequestMapping(value = "subMsg/list")
    @NoBusinessMaintenanceUrl
    public DefaultWebResponse getSubMsgPage(GetSubMsgPageRequest getSubMsgPageRequest) throws BusinessException {
        Assert.notNull(getSubMsgPageRequest, "getSubMsgPageRequest can not be null");

        BaseGetSubMsgPageRequest baseGetSubMsgPageRequest = new BaseGetSubMsgPageRequest();
        UUID id = resolveId(getSubMsgPageRequest);
        baseGetSubMsgPageRequest.setId(id);
        baseGetSubMsgPageRequest.setLimit(getSubMsgPageRequest.getLimit());
        baseGetSubMsgPageRequest.setPage(getSubMsgPageRequest.getPage());

        return Builder.success(baseMsgMgmtAPI.getSubMsgPage(baseGetSubMsgPageRequest));
    }


    /**
     * 根据任务id获取任务消息对象
     * 
     * @param getMsgWebRequest 请求对象
     * @return 消息对象
     * @throws BusinessException 业务异常
     */
    @RequestMapping(value = "msg/detail")
    @NoRefreshSession
    @NoMaintenanceUrl
    public DefaultWebResponse msgDetail(GetMsgWebRequest getMsgWebRequest) throws BusinessException {
        Assert.notNull(getMsgWebRequest, "getMsgWebRequest can not be null");

        BaseGetMsgRequest baseGetMsgRequest = new BaseGetMsgRequest();
        baseGetMsgRequest.setMsgRelationId(getMsgWebRequest.getMsgRelationId());
        baseGetMsgRequest.setMsgType(getMsgWebRequest.getMsgType());

        BaseMsgDTO baseMsgDTO = baseMsgMgmtAPI.getMsg(baseGetMsgRequest);
        return Builder.success(baseMsgDTO);
    }

    private BaseGetMsgPageRequest convertBaseGetMsgPageRequest(GetMsgPageRequest getMsgPageRequest, UUID userId) {
        BaseGetMsgPageRequest baseGetMsgPageRequest = new BaseGetMsgPageRequest();
        baseGetMsgPageRequest.setIdentityId(userId.toString());
        baseGetMsgPageRequest.setLimit(getMsgPageRequest.getLimit());
        baseGetMsgPageRequest.setMsgTitle(getMsgPageRequest.getSearchKeyword());
        baseGetMsgPageRequest.setPage(getMsgPageRequest.getPage());
        return baseGetMsgPageRequest;
    }

    private UUID resolveId(GetSubMsgPageRequest getSubMsgPageRequest) {
        ExactMatch[] exactMatchArr = getSubMsgPageRequest.getExactMatchArr();

        Assert.notNull(exactMatchArr, "exactMatchArr can not be null");
        Assert.notEmpty(exactMatchArr, "exactMatchArr can not be empty");

        List<ExactMatch> exactMatchList = Arrays.stream(exactMatchArr) //
                .filter(exactMatch -> exactMatch.getName().equals("id")).collect(Collectors.toList());
        Assert.notNull(exactMatchList, " valueArr can not be null");
        Assert.isTrue(exactMatchList.size() == 1, "valueArr size must be 1");

        ExactMatch exactMatch = exactMatchList.get(0);
        UUID[] uuidArr = exactMatch.toUUIDValue();
        Assert.notEmpty(uuidArr, "uuidArr can not be empty");
        Assert.isTrue(uuidArr.length == 1, "uuidArr length must be 1");

        return uuidArr[0];
    }
}
