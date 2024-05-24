package com.ruijie.rcos.rcdc.rco.module.impl.security.auditapply.spi;

import static com.alibaba.fastjson.serializer.SerializerFeature.WriteNullListAsEmpty;
import static com.alibaba.fastjson.serializer.SerializerFeature.WriteNullNumberAsZero;
import static com.alibaba.fastjson.serializer.SerializerFeature.WriteNullStringAsEmpty;

import java.util.*;
import java.util.stream.Collectors;

import com.google.common.collect.Lists;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.enums.desktoppool.DesktopPoolType;
import com.ruijie.rcos.rcdc.rco.module.def.api.UserDesktopMgmtAPI;
import com.ruijie.rcos.rcdc.rco.module.def.api.dto.CloudDesktopDetailDTO;
import com.ruijie.rcos.rcdc.rco.module.def.security.auditapply.dto.ViewAuditApplyDTO;
import com.ruijie.rcos.rcdc.rco.module.def.security.auditapply.enums.AuditApplyTypeEnum;
import com.ruijie.rcos.sk.pagekit.api.PageQueryBuilderFactory;
import com.ruijie.rcos.sk.pagekit.api.PageQueryResponse;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.math.NumberUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.Assert;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.api.CbbGuestToolMessageAPI;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.dto.CbbGuesttoolMessageDTO;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.spi.CbbGuestToolMessageDispatcherSPI;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.spi.request.CbbGuestToolSPIReceiveRequest;
import com.ruijie.rcos.rcdc.hciadapter.module.def.dto.guesttool.GuesttoolMessageContent;
import com.ruijie.rcos.rcdc.rco.module.def.api.AuditApplyMgmtAPI;
import com.ruijie.rcos.rcdc.rco.module.def.constants.CommonMessageCode;
import com.ruijie.rcos.rcdc.rco.module.def.security.auditapply.dto.AuditApplyDetailDTO;
import com.ruijie.rcos.rcdc.rco.module.def.utils.ListRequestHelper;
import com.ruijie.rcos.rcdc.rco.module.impl.security.auditapply.dto.GetAuditFileApplyGuestToolMsgContentDTO;
import com.ruijie.rcos.rcdc.rco.module.impl.spi.guesttool.GuestToolCmdId;
import com.ruijie.rcos.sk.base.exception.BusinessException;
import com.ruijie.rcos.sk.base.log.Logger;
import com.ruijie.rcos.sk.base.log.LoggerFactory;
import com.ruijie.rcos.sk.modulekit.api.comm.DispatcherImplemetion;
import org.springframework.util.CollectionUtils;

/**
 * Description: 获取安全审计申请单列表
 * Copyright: Copyright (c) 2022
 * Company: Ruijie Co., Ltd.
 * Create Time: 2022/10/26
 *
 * @author WuShengQiang
 */
@DispatcherImplemetion(GuestToolCmdId.RCDC_GT_CMD_ID_GET_AUDIT_APPLY_LIST)
public class GuestToolGetAuditApplyListSPIImpl implements CbbGuestToolMessageDispatcherSPI {

    private static final Logger LOGGER = LoggerFactory.getLogger(GuestToolGetAuditApplyListSPIImpl.class);

    /**
     * JSON转字符串特征
     */
    private static final SerializerFeature[] JSON_FEATURES =
            new SerializerFeature[] {WriteNullListAsEmpty, WriteNullStringAsEmpty, WriteNullNumberAsZero};

    /**
     * 每页大小
     */
    private static final int PAGE_SIZE = 20;

    public static final int DEFAULT_PAGE_SIZE = 1000;

    public static final String DESKTOP_POOL_ID = "desktopPoolId";
    
    public static final String USER_ID = "userId";
    
    public static final String CREATE_TIME = "createTime";

    @Autowired
    private AuditApplyMgmtAPI auditApplyMgmtAPI;

    @Autowired
    private CbbGuestToolMessageAPI cbbGuestToolMessageAPI;
    
    @Autowired
    private UserDesktopMgmtAPI userDesktopMgmtAPI;

    @Autowired
    private PageQueryBuilderFactory pageQueryBuilderFactory;

    @Override
    public CbbGuesttoolMessageDTO receive(CbbGuestToolSPIReceiveRequest request) throws BusinessException {
        Assert.notNull(request, "request can not be null");
        CbbGuesttoolMessageDTO requestDto = request.getDto();
        Assert.notNull(requestDto, "requestDto can not be null");
        UUID deskId = requestDto.getDeskId();
        Assert.notNull(deskId, "deskId can not be null");
        LOGGER.info("[CMDID=7014]获取安全审计申请单列表，请求消息： {}", JSON.toJSONString(request));

        CbbGuesttoolMessageDTO responseBody = new CbbGuesttoolMessageDTO();
        responseBody.setPortId(requestDto.getPortId());
        responseBody.setCmdId(requestDto.getCmdId());
        responseBody.setDeskId(requestDto.getDeskId());

        GuesttoolMessageContent guestToolMsgDTO = parseGuestToolMsg(requestDto.getBody(), GuesttoolMessageContent.class);
        GetAuditFileApplyGuestToolMsgContentDTO contentDTO =
                parseGuestToolMsg(JSON.toJSONString(guestToolMsgDTO.getContent()), GetAuditFileApplyGuestToolMsgContentDTO.class);
        contentDTO.setPageIndex(NumberUtils.INTEGER_MINUS_ONE);
        contentDTO.setPageSize(NumberUtils.INTEGER_ZERO);
        contentDTO.setTotal(NumberUtils.INTEGER_ZERO);
        GuesttoolMessageContent msgDTO = new GuesttoolMessageContent();
        msgDTO.setCode(CommonMessageCode.SUCCESS);
        msgDTO.setContent(contentDTO);

        CloudDesktopDetailDTO desktopDetailDTO = userDesktopMgmtAPI.getDesktopDetailById(deskId);
        UUID userId = desktopDetailDTO.getUserId();
        if (Objects.isNull(userId)) {
            LOGGER.info("[CMDID=7014]获取安全审计申请单列表--桌面deskId:{}，未绑定用户直接返回空", deskId);
            responseBody.setBody(JSON.toJSONString(msgDTO, JSON_FEATURES));
            return responseBody;
        }
        List<AuditApplyDetailDTO> applyDetailDTOList;
        if (Objects.equals(desktopDetailDTO.getDesktopPoolType(), DesktopPoolType.DYNAMIC.name())) {
            applyDetailDTOList = queryAuditApplyList(userId, desktopDetailDTO.getDesktopPoolId());
        } else {
            applyDetailDTOList = auditApplyMgmtAPI.findAuditFileApplyByUserIdDesktopId(userId, deskId);
        }
        if (CollectionUtils.isEmpty(applyDetailDTOList)) {
            LOGGER.info("[CMDID=7014]获取安全审计申请单列表为空，deskId:{}，用户ID:{}", deskId, userId);
            responseBody.setBody(JSON.toJSONString(msgDTO, JSON_FEATURES));
            return responseBody;
        }

        // 仅保留文件导出申请，过滤打印机、打印申请记录（目前添加打印机申请客户端并未实现，是由服务端模拟申请实现）
        applyDetailDTOList = applyDetailDTOList.stream().filter(dto -> dto.getApplyType() == AuditApplyTypeEnum.EXPORT).collect(Collectors.toList());

        int total = applyDetailDTOList.size();
        List<List<AuditApplyDetailDTO>> applySubList = ListRequestHelper.subList(applyDetailDTOList, PAGE_SIZE);
        // 计算总页数
        int totalPage = applySubList.size();
        contentDTO.setTotal(total);
        LOGGER.info("[CMDID=7014]根据分页参数截取List分批发送给GT，deskId:{}，用户ID:{}，总条数:{}，总页数:{} ", deskId, userId, total, totalPage);

        int currentPage = 1;
        for (List<AuditApplyDetailDTO> tempList : applySubList) {
            contentDTO.setPageIndex(currentPage);
            contentDTO.setPageSize(tempList.size());
            contentDTO.setApplyList(tempList);

            responseBody.setBody(JSON.toJSONString(msgDTO, JSON_FEATURES));
            cbbGuestToolMessageAPI.asyncRequest(responseBody);
            LOGGER.info("[CMDID=7014]根据分页参数截取List分批发送给GT/镜像，deskId:{}，用户ID:{}，正在发送当前页:{}", deskId, userId, currentPage);
            currentPage++;
        }

        LOGGER.info("[CMDID=7014]根据分页参数截取List分批发送给GT结束，deskId:{}，用户ID:{}", deskId, userId);
        // 最后响应当前页结束标记
        contentDTO.setPageIndex(NumberUtils.INTEGER_MINUS_ONE);
        contentDTO.setPageSize(NumberUtils.INTEGER_ZERO);
        contentDTO.setApplyList(null);
        responseBody.setBody(JSON.toJSONString(msgDTO, JSON_FEATURES));
        return responseBody;
    }

    private <T> T parseGuestToolMsg(String msgBody, Class<T> clz) {
        T bodyMsg;
        try {
            bodyMsg = JSON.parseObject(msgBody, clz);
        } catch (Exception e) {
            throw new IllegalArgumentException("guest tool报文格式错误.data:" + msgBody, e);
        }
        return bodyMsg;
    }

    private List<AuditApplyDetailDTO> queryAuditApplyList(UUID userId, UUID desktopPoolId) throws BusinessException {
        List<AuditApplyDetailDTO> auditApplyList = Lists.newArrayList();
        // 初始化分页0
        int currentPage = 0;
        PageQueryBuilderFactory.RequestBuilder requestBuilder = pageQueryBuilderFactory.newRequestBuilder()
                .setPageLimit(currentPage, DEFAULT_PAGE_SIZE).eq(USER_ID, userId).eq(DESKTOP_POOL_ID, desktopPoolId).desc(CREATE_TIME);
        while (true) {
            requestBuilder.setPageLimit(currentPage, DEFAULT_PAGE_SIZE);
            PageQueryResponse<ViewAuditApplyDTO> pageQueryResponse = auditApplyMgmtAPI.auditApplyPageQuery(requestBuilder.build());
            if (ArrayUtils.isEmpty(pageQueryResponse.getItemArr())) {
                return auditApplyList;
            }
            // 页码数量自增
            currentPage++;
            List<AuditApplyDetailDTO> pageList = Arrays.stream(pageQueryResponse.getItemArr()).map(viewAuditApplyDTO -> {
                AuditApplyDetailDTO applyDetailDTO = new AuditApplyDetailDTO();
                BeanUtils.copyProperties(viewAuditApplyDTO, applyDetailDTO);
                return applyDetailDTO;
            }).collect(Collectors.toList());
            auditApplyList.addAll(pageList);
        }
    }
}
