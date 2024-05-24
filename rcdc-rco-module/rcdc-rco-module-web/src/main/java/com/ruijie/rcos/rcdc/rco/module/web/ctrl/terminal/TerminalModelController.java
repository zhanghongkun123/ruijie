package com.ruijie.rcos.rcdc.rco.module.web.ctrl.terminal;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Stream;

import org.apache.commons.lang3.ArrayUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.Assert;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.google.common.collect.ImmutableMap;
import com.ruijie.rcos.rcdc.rco.module.web.ctrl.terminal.request.TerminalPlatformWebRequest;
import com.ruijie.rcos.rcdc.rco.module.web.ctrl.terminal.validation.TerminalModelValidation;
import com.ruijie.rcos.rcdc.rco.module.web.ctrl.user.dto.IdLabelStringEntry;
import com.ruijie.rcos.rcdc.rco.module.web.response.CommonWebResponse;
import com.ruijie.rcos.rcdc.terminal.module.def.api.CbbTerminalModelAPI;
import com.ruijie.rcos.rcdc.terminal.module.def.api.dto.CbbTerminalModelDTO;
import com.ruijie.rcos.rcdc.terminal.module.def.api.enums.CbbTerminalOsTypeEnums;
import com.ruijie.rcos.sk.base.validation.EnableCustomValidate;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

/**
 * Description: Function Description
 * Copyright: Copyright (c) 2018
 * Company: Ruijie Co., Ltd.
 * Create Time: 2019/12/10
 *
 * @author nt
 */
@Api(tags = "终端型号和类型列表接口")
@Controller
@RequestMapping("/rco/terminal/model")
@EnableCustomValidate(validateClass = TerminalModelValidation.class)
public class TerminalModelController {

    private static final String ITEM_ARR = "itemArr";

    @Autowired
    private CbbTerminalModelAPI cbbTerminalModelAPI;

    /**
     * 获取终端型号列表
     *
     * @param request 请求参数
     * @return DefaultWebResponse
     */
    @ApiOperation("获取终端型号列表")
    @RequestMapping(value = "list", method = RequestMethod.POST)
    public CommonWebResponse listTerminalModel(TerminalPlatformWebRequest request) {
        Assert.notNull(request, "request can not be null");

        CbbTerminalModelDTO[] terminalModelDTOArr = cbbTerminalModelAPI.listTerminalModel(request.getPlatformArr());
        if (ArrayUtils.isEmpty(terminalModelDTOArr)) {
            return CommonWebResponse.success();
        }
        //由于CBB 上报t_cbb_terminal  可能存在重复（现已在上报加锁）
        //产品 再查出终端运行平台类型加去重操作防止出现一模一样的平台类型
        IdLabelStringEntry[] idLabelStringEntryArr = Stream.of(terminalModelDTOArr).filter(distinctByKey(CbbTerminalModelDTO::getProductModel))
                .map(terminalModel -> new IdLabelStringEntry(terminalModel.getProductModel(), terminalModel.getProductModel()))
                .toArray(IdLabelStringEntry[]::new);

        return CommonWebResponse.success(ImmutableMap.of(ITEM_ARR, idLabelStringEntryArr));
    }

    /**
     * 去重复
     * @param keyExtractor
     * @param <T>
     * @return
     */
    private  <T> Predicate<T> distinctByKey(Function<? super T, Object> keyExtractor) {
        Map<Object, Boolean> seen = new ConcurrentHashMap<>();
        return object -> seen.putIfAbsent(keyExtractor.apply(object), Boolean.TRUE) == null;
    }

    /**
     * 获取终端运行平台类型
     * 
     * @param request 请求参数
     * @return DefaultWebResponse
     */
    @ApiOperation("获取终端运行平台类型")
    @RequestMapping(value = "listOsType", method = RequestMethod.POST)
    public CommonWebResponse listTerminalOsType(TerminalPlatformWebRequest request) {
        Assert.notNull(request, "request can not be null");

        List<String> terminalOsTypeList = cbbTerminalModelAPI.listTerminalOsType(request.getPlatformArr());
        if (CollectionUtils.isEmpty(terminalOsTypeList)) {
            return CommonWebResponse.success();
        }

        IdLabelStringEntry[] idLabelStringEntryArr = terminalOsTypeList.stream().map(osType -> {
            IdLabelStringEntry idLabelStringEntry = new IdLabelStringEntry();
            idLabelStringEntry.setId(osType);
            idLabelStringEntry.setLabel(osType);
            if (osType.contains(CbbTerminalOsTypeEnums.KYLIN.name())) {
                idLabelStringEntry.setLabel(convertToFirstUpperCase(CbbTerminalOsTypeEnums.KYLIN.name()));
            }
            return idLabelStringEntry;
        }).toArray(IdLabelStringEntry[]::new);
        return CommonWebResponse.success(ImmutableMap.of(ITEM_ARR, idLabelStringEntryArr));
    }

    /**
     * 将终端运行平台中的首字母转换成大写，其它的全部转换成小写
     * 
     * @param osType 终端运行平台
     * @return 首字母大小，其余小写
     */
    private static String convertToFirstUpperCase(String osType) {
        String first = osType.substring(0, 1);
        String after = osType.substring(1);
        first = first.toUpperCase();
        after = after.toLowerCase();
        return first + after;
    }
}
