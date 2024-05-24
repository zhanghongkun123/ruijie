package com.ruijie.rcos.rcdc.rco.module.web.ctrl.clouddesktop;

import java.util.UUID;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.hamcrest.CustomMatcher;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;

import com.ruijie.rcos.rcdc.rco.module.def.api.TerminalSelectAPI;
import com.ruijie.rcos.rcdc.rco.module.def.api.dto.ProductDriverDTO;
import com.ruijie.rcos.rcdc.rco.module.def.api.dto.TerminalDTO;
import com.ruijie.rcos.rcdc.rco.module.def.api.request.TerminalSelectPageSearchRequest;
import com.ruijie.rcos.sk.base.exception.BusinessException;
import com.ruijie.rcos.sk.base.junit.SkyEngineRunner;
import com.ruijie.rcos.sk.base.test.ThrowExceptionTester;
import com.ruijie.rcos.sk.modulekit.api.comm.DefaultPageResponse;
import com.ruijie.rcos.sk.webmvc.api.request.IdWebRequest;
import com.ruijie.rcos.sk.webmvc.api.request.PageWebRequest;
import com.ruijie.rcos.sk.webmvc.api.vo.ExactMatch;
import com.ruijie.rcos.sk.webmvc.api.vo.Sort;

import mockit.Expectations;
import mockit.Injectable;
import mockit.Tested;
import mockit.Verifications;

@RunWith(SkyEngineRunner.class)
public class TerminalSelectControllerTest {

    @Tested
    TerminalSelectController terminalSelectController;

    @Injectable
    private TerminalSelectAPI terminalSelectAPI;

    /**
     * 测试参数为空
     */
    @Test
    public void testParamNullError() {
        try {
            ThrowExceptionTester.throwIllegalArgumentException(() -> terminalSelectController.listTerminalModel(null), "request can not be null");
            ThrowExceptionTester.throwIllegalArgumentException(() -> terminalSelectController.listSelectableTerminalIDV(null, null),
                    "request can not be null");
        } catch (Exception e) {
            Assert.fail();
        }
    }


    /**
     * 测试显示驱动列表
     * 
     * @throws BusinessException 业务异常
     */
    @Test
    public void testListTerminalModel() throws BusinessException {
        IdWebRequest idWebRequest = new IdWebRequest();
        UUID id = UUID.randomUUID();
        idWebRequest.setId(id);
        ProductDriverDTO productDriverDTO = new ProductDriverDTO();
        new Expectations() {
            {
                terminalSelectAPI.listSortedTerminalModel(id);
                result = new ProductDriverDTO[] {productDriverDTO};
            }
        };
        terminalSelectController.listTerminalModel(idWebRequest);
        new Verifications() {
            {
                terminalSelectAPI.listSortedTerminalModel(id);
                times = 1;
            }
        };
    }

    /**
     * 测试显示可选择的IDV终端
     * 
     * @throws BusinessException 业务一异常
     */
    @Test
    public void testListSelectableTerminalIDVImageIsNull() throws BusinessException {
        PageWebRequest pageWebRequest = new PageWebRequest();
        ExactMatch exactMatch = new ExactMatch();
        UUID imageId = UUID.randomUUID();
        exactMatch.setName("imageId1");
        exactMatch.setValueArr(new String[] {imageId.toString()});
        ExactMatch[] exactMatchArr = new ExactMatch[] {exactMatch};
        pageWebRequest.setExactMatchArr(exactMatchArr);
        pageWebRequest.setSort(null);
        DefaultPageResponse<TerminalDTO> dtoDefaultPageResponse = new DefaultPageResponse<>();
        dtoDefaultPageResponse.setItemArr(new TerminalDTO[] {new TerminalDTO()});
        new Expectations() {
            {
                terminalSelectAPI.listSelectableTerminal(withArgThat(new CustomMatcher<TerminalSelectPageSearchRequest>("") {
                    @Override
                    public boolean matches(Object o) {
                        Assert.assertNotNull(o);
                        TerminalSelectPageSearchRequest matchRequest = (TerminalSelectPageSearchRequest) o;
                        EqualsBuilder equalsBuilder = new EqualsBuilder();
                        return equalsBuilder.isEquals();
                    }
                }));
                result = dtoDefaultPageResponse;
            }
        };
        terminalSelectController.listSelectableTerminalIDV(pageWebRequest, null);
        new Verifications() {
            {
                terminalSelectAPI.listSelectableTerminal((TerminalSelectPageSearchRequest) any);
                times = 1;
            }
        };
    }


    /**
     * 测试显示可选择的IDV终端,默认排序
     * 
     * @throws BusinessException 业务一异常
     */
    @Test
    public void testListSelectableTerminalIDVSortEmpty() throws BusinessException {
        PageWebRequest pageWebRequest = new PageWebRequest();
        ExactMatch exactMatch = new ExactMatch();
        UUID imageId = UUID.randomUUID();
        exactMatch.setName("imageId");
        exactMatch.setValueArr(new String[] {imageId.toString()});
        ExactMatch[] exactMatchArr = new ExactMatch[] {exactMatch};
        pageWebRequest.setExactMatchArr(exactMatchArr);
        pageWebRequest.setSort(null);
        DefaultPageResponse<TerminalDTO> dtoDefaultPageResponse = new DefaultPageResponse<>();
        dtoDefaultPageResponse.setItemArr(new TerminalDTO[] {new TerminalDTO()});
        new Expectations() {
            {
                terminalSelectAPI.listSelectableTerminal(withArgThat(new CustomMatcher<TerminalSelectPageSearchRequest>("") {
                    @Override
                    public boolean matches(Object o) {
                        Assert.assertNotNull(o);
                        TerminalSelectPageSearchRequest matchRequest = (TerminalSelectPageSearchRequest) o;
                        EqualsBuilder equalsBuilder = new EqualsBuilder();
                        return equalsBuilder.isEquals();
                    }
                }));
                result = dtoDefaultPageResponse;
            }
        };
        terminalSelectController.listSelectableTerminalIDV(pageWebRequest, null);
        new Verifications() {
            {
                terminalSelectAPI.listSelectableTerminal((TerminalSelectPageSearchRequest) any);
                times = 1;
            }
        };
    }

    /**
     * 测试显示可选择的IDV终端,非默认排序
     * 
     * @throws BusinessException 业务一异常
     */
    @Test
    public void testListSelectableTerminalIDVSortNotEmpty() throws BusinessException {
        PageWebRequest pageWebRequest = new PageWebRequest();
        ExactMatch exactMatch1 = new ExactMatch();
        UUID imageId = UUID.randomUUID();
        exactMatch1.setName("imageId");
        exactMatch1.setValueArr(new String[] {imageId.toString()});

        ExactMatch exactMatch2 = new ExactMatch();
        String productId = UUID.randomUUID().toString();
        exactMatch2.setName("productType");
        exactMatch2.setValueArr(new String[] {productId});

        ExactMatch[] exactMatchArr = new ExactMatch[] {exactMatch1, exactMatch2};
        pageWebRequest.setExactMatchArr(exactMatchArr);
        Sort sort = new Sort();
        sort.setSortField("createTime");
        sort.setDirection(Sort.Direction.DESC);
        pageWebRequest.setSort(sort);

        DefaultPageResponse<TerminalDTO> dtoDefaultPageResponse = new DefaultPageResponse<>();
        dtoDefaultPageResponse.setItemArr(new TerminalDTO[] {new TerminalDTO()});
        new Expectations() {
            {
                terminalSelectAPI.listSelectableTerminal(withArgThat(new CustomMatcher<TerminalSelectPageSearchRequest>("") {
                    @Override
                    public boolean matches(Object o) {
                        Assert.assertNotNull(o);
                        TerminalSelectPageSearchRequest matchRequest = (TerminalSelectPageSearchRequest) o;
                        EqualsBuilder equalsBuilder = new EqualsBuilder();
                        return equalsBuilder.isEquals();
                    }
                }));
                result = dtoDefaultPageResponse;
            }
        };
        terminalSelectController.listSelectableTerminalIDV(pageWebRequest, null);
        new Verifications() {
            {
                terminalSelectAPI.listSelectableTerminal((TerminalSelectPageSearchRequest) any);
                times = 1;
            }
        };

    }
}
