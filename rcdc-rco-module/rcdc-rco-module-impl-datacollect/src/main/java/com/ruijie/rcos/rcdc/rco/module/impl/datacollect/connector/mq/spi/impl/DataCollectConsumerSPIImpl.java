package com.ruijie.rcos.rcdc.rco.module.impl.datacollect.connector.mq.spi.impl;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.Assert;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.ruijie.rcos.rcdc.rco.module.def.api.ConfigurationWizardAPI;
import com.ruijie.rcos.rcdc.rco.module.def.api.RcoGlobalParameterAPI;
import com.ruijie.rcos.rcdc.rco.module.def.api.request.FindParameterRequest;
import com.ruijie.rcos.rcdc.rco.module.def.api.request.GetConfigurationWizardRequest;
import com.ruijie.rcos.rcdc.rco.module.def.api.response.GetConfigurationWizardResponse;
import com.ruijie.rcos.rcdc.rco.module.impl.datacollect.connector.mq.DataCollectConstant;
import com.ruijie.rcos.rcdc.rco.module.impl.datacollect.connector.mq.api.DataCollectProducerAPI;
import com.ruijie.rcos.rcdc.rco.module.impl.datacollect.connector.mq.dao.IncrementMarkDAO;
import com.ruijie.rcos.rcdc.rco.module.impl.datacollect.connector.mq.dto.*;
import com.ruijie.rcos.rcdc.rco.module.impl.datacollect.connector.mq.entity.IncrementMarkEntity;
import com.ruijie.rcos.rcdc.rco.module.impl.datacollect.connector.mq.service.CommonExecuteSqlDAOCustomer;
import com.ruijie.rcos.rcdc.rco.module.impl.datacollect.connector.mq.spi.DataCollectConsumerSPI;
import com.ruijie.rcos.rcdc.rco.module.impl.datacollect.connector.mq.util.ZipUtil;
import com.ruijie.rcos.sk.base.exception.BusinessException;
import com.ruijie.rcos.sk.base.log.Logger;
import com.ruijie.rcos.sk.base.log.LoggerFactory;
import com.ruijie.rcos.sk.base.util.StringUtils;
import com.ruijie.rcos.sk.configcenter.ConfigCenterKvAPI;

/**
 * Description: Function Description
 * Copyright: Copyright (c) 2020
 * Company: Ruijie Co., Ltd.
 * Create Time: 2021/6/10
 *
 * @author jarman
 */
public class DataCollectConsumerSPIImpl implements DataCollectConsumerSPI {

    private static final Logger LOGGER = LoggerFactory.getLogger(DataCollectConsumerSPIImpl.class);

    private static final int MAX_SIZE = 10000;

    private static final String WHERE = " where ";

    private static final String MARK = "'0'";

    private static final String REPLACE_MARK = "?";

    private static final String SELECT = "select ";

    private static final String FROM = " from ";

    private static final String LIMIT = " limit ";

    private static final String SPACE = " ";

    private static final String SLASH = "\"";


    @Autowired
    private RcoGlobalParameterAPI rcoGlobalParameterAPI;

    @Autowired
    private DataCollectProducerAPI dataCollectProducerAPI;

    @Autowired
    private CommonExecuteSqlDAOCustomer commonExecuteSqlDAOCustomer;

    @Autowired
    private IncrementMarkDAO incrementMarkDAO;

    @Autowired
    private ConfigCenterKvAPI configCenterKvAPI;

    @Autowired
    private ConfigurationWizardAPI configurationWizardAPI;


    @Override
    public void collect(CollectRequestDTO collectRequestDTO) throws BusinessException {
        Assert.notNull(collectRequestDTO, "collectRequestDTO cannot be null");
        LOGGER.info("开始执行初始化数据采集任务。。。,请求参数为{}", collectRequestDTO.toString());
        GetConfigurationWizardRequest request = new GetConfigurationWizardRequest();
        GetConfigurationWizardResponse configurationWizard = configurationWizardAPI.getConfigurationWizard(request);
        if (!configurationWizard.getIsJoinUserExperiencePlan()) {
            LOGGER.info("未加入用户体验计划，不上传数据");
            return;
        }
        if (StringUtils.equals(DataCollectConstant.CMC_DATA_COLLECT_KEY, collectRequestDTO.getDataKey())) {
            LOGGER.info("收到MQ采集数据消息:{}", JSON.toJSONString(collectRequestDTO));
            // 读取采集规则
            String collectRule = rcoGlobalParameterAPI.findParameter(new FindParameterRequest(DataCollectConstant.COLLECT_RULE)).getValue();
            LOGGER.info("数据库中的采集规则为{}", collectRule);
            CollectRulesDTO collectRulesDTO = JSON.parseObject(collectRule, CollectRulesDTO.class);
            if (collectRulesDTO == null) {
                LOGGER.warn("数据采集规则为空");
                return;
            }
            List<CollectRuleDTO> collectRulesList = collectRulesDTO.getCollectRuleList();
            // 查询采集数据
            CollectDataContentDTO collectDataContentDTO = new CollectDataContentDTO();
            Optional<String> optional = configCenterKvAPI.get(DataCollectConstant.RCDC_VERSION_KEY, String.class);
            if (!optional.isPresent()) {
                LOGGER.error("未获取到当前系统版本号，不进行数据采集");
                return;
            }
            String versionJsonStr = optional.get();
            LOGGER.info("获取到系统版本号为{}", versionJsonStr);

            JSONObject jsonObject = null;
            try {
                jsonObject = JSON.parseObject(versionJsonStr).getJSONArray(DataCollectConstant.SYSTEM_PRODUCTS_INFO).getJSONObject(0);
            } catch (Exception ex) {
                LOGGER.error("解析配置文件内容发生异常，ex", ex);
                return;
            }

            String productLine = (String) jsonObject.get(DataCollectConstant.SYSTEM_PRODUCT_LINE_KEY);
            String publicVersion = (String) jsonObject.get(DataCollectConstant.SYSTEM_PUBLIC_VERSION_KEY);
            collectDataContentDTO.setProductLine(productLine);
            collectDataContentDTO.setPublicVersion(publicVersion);
            List<SingleDataDTO> dataResultList = new ArrayList<>();
            for (CollectRuleDTO collectRuleDTO : collectRulesList) {
                QueryRuleDTO queryRule = collectRuleDTO.getQueryRule();
                String lastPositionField = StringUtils.EMPTY;
                if (DataCollectConstant.INCREMENT.equals(queryRule.getCollectMethod())) {
                    lastPositionField = queryRule.getLastPositionField();
                }
                SingleDataDTO singleDataDTO = new SingleDataDTO();
                singleDataDTO.setItemKey(collectRuleDTO.getItemKey());
                try {
                    JSONArray jsonArray =
                            commonExecuteSqlDAOCustomer.queryCommonSql(buildSQL(collectRuleDTO), collectRuleDTO.getItemKey(), lastPositionField);
                    if (LOGGER.isDebugEnabled()) {
                        LOGGER.debug("采集到的数据为{}", jsonArray.toJSONString());
                    }
                    singleDataDTO.setData(jsonArray.toJSONString());
                } catch (Exception e) {
                    singleDataDTO.setSqlExcption(e.getMessage());
                    LOGGER.error("数据收集查询异常", e);
                }
                dataResultList.add(singleDataDTO);
            }
            collectDataContentDTO.setDataResultList(dataResultList);
            if (LOGGER.isDebugEnabled()) {
                LOGGER.debug("封装采集后的数据为{}", collectDataContentDTO.toString());
            }

            byte[] deflaterResultArr;
            try {
                deflaterResultArr = deflater(collectDataContentDTO.toString());
                LOGGER.debug("数据压缩成功，压缩后的数据为{}", deflaterResultArr.length);
            } catch (BusinessException e) {
                LOGGER.error("压缩数据出错！", e);
                throw new BusinessException("压缩数据出错！", e);
            }
            CollectDataResponseDTO collectDataResponseDTO = buildCollectResponse(deflaterResultArr);
            collectDataResponseDTO.setAttachment(collectRequestDTO.getAttachment());
            dataCollectProducerAPI.sendData(collectDataResponseDTO);
        }
    }

    private CollectDataResponseDTO buildCollectResponse(byte[] deflaterResult) {
        CollectDataResponseDTO collectDataResponseDTO = new CollectDataResponseDTO();
        collectDataResponseDTO.setContentByteArr(deflaterResult);
        collectDataResponseDTO.setCollectTime(new Date());
        collectDataResponseDTO.setDataType(DataCollectConstant.JSON_STRING);

        return collectDataResponseDTO;
    }

    private byte[] deflater(String originString) throws BusinessException {
        try {
            return ZipUtil.deflater(originString);
        } catch (IOException e) {
            LOGGER.error("压缩数据出错！", e);
            throw new BusinessException("压缩数据出错", e);
        }
    }

    private String buildSQL(CollectRuleDTO collectRuleDTO) {
        QueryRuleDTO queryRule = collectRuleDTO.getQueryRule();
        int maxSize;
        if (queryRule.getMaxSize() == null) {
            maxSize = MAX_SIZE;
        } else {
            maxSize = Integer.parseInt(queryRule.getMaxSize());
        }
        String originField = queryRule.getQueryFields();
        String field = originField.substring(1, originField.length() - 1);
        field = field.replace(SLASH, StringUtils.EMPTY);
        String condition = queryRule.getCondition() == null ? StringUtils.EMPTY : WHERE + queryRule.getCondition() + " ";
        if (DataCollectConstant.INCREMENT.equals(queryRule.getCollectMethod())) {
            IncrementMarkEntity incrementMark = incrementMarkDAO.findByItemKey(collectRuleDTO.getItemKey());
            String mark = MARK;
            if (incrementMark != null) {
                mark = incrementMark.getMark();
            }
            condition = condition.replace(REPLACE_MARK, mark);
        }

        String sort = queryRule.getSort() == null ? StringUtils.EMPTY : SPACE + queryRule.getSort() + SPACE;
        StringBuilder sqlStringBuffer = new StringBuilder();
        sqlStringBuffer.append(SELECT).append(field).append(FROM).append(queryRule.getTableName()).append(condition).append(sort).append(LIMIT)
                .append(maxSize);
        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug("封装采集后的数据为{}", sqlStringBuffer.toString());
        }

        return sqlStringBuffer.toString();
    }

}
