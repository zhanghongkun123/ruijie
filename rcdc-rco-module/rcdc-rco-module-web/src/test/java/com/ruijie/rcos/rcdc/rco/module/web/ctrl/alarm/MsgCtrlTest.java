package com.ruijie.rcos.rcdc.rco.module.web.ctrl.alarm;

import com.ruijie.rcos.base.task.module.def.api.BaseMsgMgmtAPI;
import com.ruijie.rcos.base.task.module.def.api.request.msg.BaseGetMsgPageRequest;
import com.ruijie.rcos.base.task.module.def.api.request.msg.BaseGetMsgRequest;
import com.ruijie.rcos.base.task.module.def.api.request.msg.BaseGetSubMsgPageRequest;
import com.ruijie.rcos.base.task.module.def.dto.msg.BaseMsgDTO;
import com.ruijie.rcos.base.task.module.def.dto.msg.BaseSubMsgDTO;
import com.ruijie.rcos.rcdc.rco.module.web.ctrl.alarm.request.GetMsgPageRequest;
import com.ruijie.rcos.rcdc.rco.module.web.ctrl.alarm.request.GetMsgWebRequest;
import com.ruijie.rcos.rcdc.rco.module.web.ctrl.alarm.request.GetSubMsgPageRequest;
import com.ruijie.rcos.sk.base.exception.BusinessException;
import com.ruijie.rcos.sk.base.test.ThrowExceptionTester;
import com.ruijie.rcos.sk.modulekit.api.comm.DefaultPageResponse;
import com.ruijie.rcos.sk.webmvc.api.response.DefaultWebResponse;
import com.ruijie.rcos.sk.webmvc.api.session.SessionContext;
import com.ruijie.rcos.sk.webmvc.api.vo.ExactMatch;
import mockit.*;
import org.hamcrest.CustomMatcher;
import org.junit.Assert;
import org.junit.Test;

import java.util.UUID;

/**
 * <br>
 * Description: Function Description <br>
 * Copyright: Copyright (c) 2019 <br>
 * Company: Ruijie Co., Ltd. <br>
 * Create Time: 2019/1/5 <br>
 *
 * @author dell
 */
public class MsgCtrlTest {
    @Tested
    private MsgCtrl msgCtrl;

    @Injectable
    private BaseMsgMgmtAPI msgMgmtAPI;


    /**
     * 测试获取消息主表信息，输入参数异常的场景
     *
     * @throws Exception 异常
     */
    @Test
    public void testGetMsgPageParamError() throws Exception {


        ThrowExceptionTester.ThrowableRunnable case1 = () -> msgCtrl.getMsgPage(null, null);
        ThrowExceptionTester.ThrowableRunnable case2 = () -> msgCtrl.getMsgPage(new GetMsgPageRequest(), null);

        ThrowExceptionTester.throwIllegalArgumentException(case1, "getMsgPageRequest can not be null");
        ThrowExceptionTester.throwIllegalArgumentException(case2, "sessionContext can not be null");

        Assert.assertTrue(true);
    }

    /**
     * 测试获取消息列表
     * 
     * @param sessionContext 模拟sessionContext对象
     * @throws BusinessException 异常
     */
    @Test
    public void testGetMsgPage(@Capturing SessionContext sessionContext) throws BusinessException {

        final UUID userId = UUID.randomUUID();

        GetMsgPageRequest getMsgPageRequest = new GetMsgPageRequest();
        final int page = 0;
        getMsgPageRequest.setPage(page);
        final int limit = 100;
        getMsgPageRequest.setLimit(limit);

        final CustomMatcher<BaseGetMsgPageRequest> argumentMatcher = new CustomMatcher<BaseGetMsgPageRequest>("") {
            @Override
            public boolean matches(Object item) {
                BaseGetMsgPageRequest baseGetMsgPageRequest = (BaseGetMsgPageRequest) item;

                return baseGetMsgPageRequest.getIdentityId().equals(userId.toString()) && baseGetMsgPageRequest.getLimit() == limit
                        && baseGetMsgPageRequest.getPage() == page;
            }
        };

        final BaseMsgDTO[] baseMsgDTOArr = {new BaseMsgDTO()};
        final DefaultPageResponse<BaseMsgDTO> msgPage = DefaultPageResponse.Builder.success(1, 1, baseMsgDTOArr);

        new Expectations() {
            {
                sessionContext.getUserId();
                result = userId;

                msgMgmtAPI.getMsgPage(withArgThat(argumentMatcher));
                result = msgPage;
            }
        };

        DefaultWebResponse defaultWebResponse = msgCtrl.getMsgPage(getMsgPageRequest, sessionContext);

        Assert.assertEquals(msgPage, defaultWebResponse.getContent());

        new Verifications() {
            {
                sessionContext.getUserId();
                times = 1;

                msgMgmtAPI.getMsgPage(withArgThat(argumentMatcher));
                times = 1;

            }
        };
    }


    /**
     * 测试获取消息子表信息，输入参数异常的场景
     *
     * @throws Exception 异常
     */
    @Test
    public void testGetSubMsgPageParamError() throws Exception {
        ThrowExceptionTester.ThrowableRunnable case1 = () -> msgCtrl.getSubMsgPage(null);

        ThrowExceptionTester.throwIllegalArgumentException(case1, "getSubMsgPageRequest can not be null");

        Assert.assertTrue(true);
    }


    /**
     * 测试传入的ID字段不合法的场景
     *
     * @throws Exception 异常
     */
    @Test
    public void testGetSubMsgPageIdError() throws Exception {

        GetSubMsgPageRequest getSubMsgPageRequest = new GetSubMsgPageRequest();
        final int page = 0;
        getSubMsgPageRequest.setPage(page);
        final int limit = 100;
        getSubMsgPageRequest.setLimit(limit);

        getSubMsgPageRequest.setExactMatchArr(null);

        ThrowExceptionTester.throwIllegalArgumentException(() -> msgCtrl.getSubMsgPage(getSubMsgPageRequest), "exactMatchArr can not be null");

        ExactMatch[] exactMatchArr = {};
        getSubMsgPageRequest.setExactMatchArr(exactMatchArr);
        ThrowExceptionTester.throwIllegalArgumentException(() -> msgCtrl.getSubMsgPage(getSubMsgPageRequest), "exactMatchArr can not be empty");

        ExactMatch exactMatch = new ExactMatch();
        exactMatch.setName("id");
        exactMatch.setValueArr(null);

        ExactMatch[] exactMatch2Arr = {exactMatch, exactMatch};
        getSubMsgPageRequest.setExactMatchArr(exactMatch2Arr);
        ThrowExceptionTester.throwIllegalArgumentException(() -> msgCtrl.getSubMsgPage(getSubMsgPageRequest), "valueArr size must be 1");


        ExactMatch exactMatch3 = new ExactMatch();
        exactMatch3.setName("id");
        exactMatch3.setValueArr(new String[] {UUID.randomUUID().toString(), UUID.randomUUID().toString()});
        ExactMatch[] exactMatch3Arr = {exactMatch3};
        getSubMsgPageRequest.setExactMatchArr(exactMatch3Arr);
        ThrowExceptionTester.throwIllegalArgumentException(() -> msgCtrl.getSubMsgPage(getSubMsgPageRequest), "uuidArr length must be 1");

        Assert.assertTrue(true);
    }

    /**
     * 测试获取子消息正常场景
     *
     * @throws BusinessException 异常
     */
    @Test
    public void testGetSubMsgPage() throws BusinessException {
        GetSubMsgPageRequest getSubMsgPageRequest = new GetSubMsgPageRequest();
        final int page = 0;
        getSubMsgPageRequest.setPage(page);
        final int limit = 100;
        getSubMsgPageRequest.setLimit(limit);
        ExactMatch exactMatch = new ExactMatch();
        exactMatch.setName("id");
        final String msgId = UUID.randomUUID().toString();
        exactMatch.setValueArr(new String[] {msgId});
        ExactMatch[] exactMatchArr = {exactMatch};
        getSubMsgPageRequest.setExactMatchArr(exactMatchArr);
        final CustomMatcher<BaseGetSubMsgPageRequest> argumentMatcher = new CustomMatcher<BaseGetSubMsgPageRequest>("") {
            @Override
            public boolean matches(Object item) {
                BaseGetSubMsgPageRequest request = (BaseGetSubMsgPageRequest) item;

                return request.getId().toString().equals(msgId) && request.getLimit() == limit && request.getPage() == page;
            }
        };
        final BaseSubMsgDTO[] baseSubMsgDTOArr = {new BaseSubMsgDTO()};
        final DefaultPageResponse<BaseSubMsgDTO> subMsgPage = DefaultPageResponse.Builder.success(1, 1, baseSubMsgDTOArr);

        new Expectations() {
            {
                msgMgmtAPI.getSubMsgPage(withArgThat(argumentMatcher));
                result = subMsgPage;
            }
        };

        DefaultWebResponse defaultWebResponse = msgCtrl.getSubMsgPage(getSubMsgPageRequest);

        Assert.assertEquals(subMsgPage, defaultWebResponse.getContent());

        new Verifications() {
            {
                msgMgmtAPI.getSubMsgPage(withArgThat(argumentMatcher));
                times = 1;
            }
        };

    }



    /**
     * 测试消息详情当参数为空时
     * 
     * @throws BusinessException 业务异常
     */
    @Test
    public void testMsgDetailWhileArgsIsNull() throws BusinessException {
        try {
            msgCtrl.msgDetail(null);
            Assert.fail();
        } catch (IllegalArgumentException e) {
            Assert.assertEquals(e.getMessage(), "getMsgWebRequest can not be null");
        }

    }

    /**
     * 测试获取消息详情接口
     *
     * @throws BusinessException 业务异常
     */
    @Test
    public void testMsgDetail() throws BusinessException {
        BaseMsgDTO baseMsgDTO = new BaseMsgDTO();
        new Expectations() {
            {
                msgMgmtAPI.getMsg((BaseGetMsgRequest) any);
                result = baseMsgDTO;
            }
        };

        Assert.assertEquals(baseMsgDTO, msgCtrl.msgDetail(new GetMsgWebRequest()).getContent());
    }

}
