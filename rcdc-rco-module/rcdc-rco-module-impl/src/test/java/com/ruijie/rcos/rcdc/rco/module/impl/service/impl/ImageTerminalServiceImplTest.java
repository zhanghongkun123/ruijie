package com.ruijie.rcos.rcdc.rco.module.impl.service.impl;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;
import java.util.UUID;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.hamcrest.CustomMatcher;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;

import com.google.common.collect.Lists;
import com.ruijie.rcos.rcdc.rco.module.def.api.dto.ImageTemplateTerminalDTO;
import com.ruijie.rcos.rcdc.rco.module.impl.dao.ImageTemplateTerminalDAO;
import com.ruijie.rcos.rcdc.rco.module.impl.entity.ImageTemplateTerminalEntity;
import com.ruijie.rcos.sk.base.junit.SkyEngineRunner;
import com.ruijie.rcos.sk.base.test.ThrowExceptionTester;
import com.ruijie.rcos.sk.modulekit.api.comm.DtoResponse;
import com.ruijie.rcos.sk.modulekit.api.comm.IdRequest;

import mockit.Expectations;
import mockit.Injectable;
import mockit.Tested;
import mockit.Verifications;

/**
 * Description: 终端镜像选取接口实现类
 * Copyright: Copyright (c) 2019
 * Company: Ruijie Co., Ltd.
 * Create Time: 2020/2/26
 *
 * @author songxiang
 */
@RunWith(SkyEngineRunner.class)
public class ImageTerminalServiceImplTest {

    @Tested
    private ImageTerminalServiceImpl imageTerminalService;

    @Injectable
    private ImageTemplateTerminalDAO imageTemplateTerminalDAO;

    /**
     * 测试参数为空
     */
    @Test
    public void testParamNullError() {
        try {
            ThrowExceptionTester.throwIllegalArgumentException(() -> imageTerminalService.queryEditedTerminalListOfImage(null),
                    "imageId must not be null");
            ThrowExceptionTester.throwIllegalArgumentException(() -> imageTerminalService.addEditedTerminalInfoOfImage(null), "dto must not be null");
        } catch (Exception e) {
            Assert.fail();
        }
    }

    /**
     * 测试查询一个镜像已编辑过的终端
     * 
     * @throws IllegalAccessException 反射异常
     */
    @Test
    public void queryEditedTerminalListOfImage() throws IllegalAccessException {
        UUID imageId = UUID.randomUUID();
        ImageTemplateTerminalEntity imageTemplateTerminalEntity = new ImageTemplateTerminalEntity();
        generateDataByReflect(imageTemplateTerminalEntity);
        imageTemplateTerminalEntity.setId(imageId);
        CustomMatcher<IdRequest> customMatcher = new CustomMatcher<IdRequest>("") {

            @Override
            public boolean matches(Object o) {
                Assert.assertNotNull(o);
                IdRequest matchRequest = (IdRequest) o;
                return matchRequest.getId().equals(imageId);
            }
        };

        new Expectations() {
            {
                imageTemplateTerminalDAO.findByImageId(imageId);
                result = Lists.newArrayList(imageTemplateTerminalEntity);
            }
        };

        DtoResponse<ImageTemplateTerminalDTO[]> dtoResponse = imageTerminalService.queryEditedTerminalListOfImage(imageId);
        ImageTemplateTerminalDTO[] dtoArr = dtoResponse.getDto();
        Assert.assertEquals(dtoArr[0].getId(), imageTemplateTerminalEntity.getId());
        Assert.assertEquals(dtoArr[0].getImageId(), imageTemplateTerminalEntity.getImageId());
        Assert.assertEquals(dtoArr[0].getTerminalId(), imageTemplateTerminalEntity.getTerminalId());
    }

    /**
     * 测试增加编辑的终端信息
     * 
     * @throws IllegalAccessException 反射异常
     */
    @Test
    public void addEditedTerminalInfoOfImage() throws IllegalAccessException {
        ImageTemplateTerminalDTO imageTemplateTerminalDTO = new ImageTemplateTerminalDTO();
        generateDataByReflect(imageTemplateTerminalDTO);
        new Expectations() {
            {
                imageTemplateTerminalDAO.save(withArgThat(new CustomMatcher<ImageTemplateTerminalEntity>("") {
                    @Override
                    public boolean matches(Object o) {
                        Assert.assertNotNull(o);
                        ImageTemplateTerminalEntity matchEntity = (ImageTemplateTerminalEntity) o;
                        EqualsBuilder equalsBuilder = new EqualsBuilder();
                        equalsBuilder.append(matchEntity.getImageId(), imageTemplateTerminalDTO.getImageId());
                        equalsBuilder.append(matchEntity.getTerminalId(), imageTemplateTerminalDTO.getTerminalId());
                        return equalsBuilder.isEquals();
                    }
                }));
            }
        };

        imageTerminalService.addEditedTerminalInfoOfImage(imageTemplateTerminalDTO);
        new Verifications() {
            {
                imageTemplateTerminalDAO.save((ImageTemplateTerminalEntity) any);
                times = 1;
            }
        };
    }

    private void generateDataByReflect(Object object) throws IllegalAccessException {
        Class clazz = object.getClass();
        List<Field> fieldList = new ArrayList<>();
        while (clazz != null) {
            fieldList.addAll(new ArrayList<>(Arrays.asList(clazz.getDeclaredFields())));
            clazz = clazz.getSuperclass();
        }
        Random random = new Random();
        Field[] fieldArr = new Field[fieldList.size()];
        fieldList.toArray(fieldArr);
        for (Field field : fieldArr) {
            field.setAccessible(true);
            if (field.getType().isEnum()) {
                Object[] objectArr = field.getType().getEnumConstants();
                field.set(object, objectArr[0]);
                continue;
            }
            if (field.getType().getName().equals(UUID.class.getName())) {
                field.set(object, UUID.randomUUID());
                continue;
            }
            if (field.getType().getName().equals(Integer.class.getName())) {
                field.set(object, new Integer(10));
                continue;
            }
            if (field.getType().getName().equals("int")) {
                field.setInt(object, 10);
                continue;
            }
            if (field.getType().getName().equals("long")) {
                field.setLong(object, 123L);
                continue;
            }
            if (field.getType().getName().equals(String.class.getName())) {

                field.set(object, "abc123" + String.valueOf(random.nextInt(1024)));
                continue;
            }
            if (field.getType().getName().equals(Boolean.class.getName())) {
                field.set(object, true);
                continue;
            }
            if (field.getType().getName().equals("boolean")) {
                field.set(object, true);
                continue;
            }
        }
    }
}
