package com.ruijie.rcos.rcdc.rco.module.def.sms.enums;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.ruijie.rcos.rcdc.rco.module.def.sms.MessageHandler;
import com.ruijie.rcos.rcdc.rco.module.def.sms.constnts.SmsAndScanCodeCheckConstants;
import com.ruijie.rcos.rcdc.rco.module.def.sms.dto.HttpResultParserDTO;
import com.ruijie.rcos.rcdc.rco.module.def.utils.SmsConverterUtils;
import com.ruijie.rcos.sk.base.exception.BusinessException;
import org.springframework.util.Assert;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.xml.sax.InputSource;

import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.xpath.*;
import java.io.StringReader;
import java.util.Map;

/**
 * Description: 报文类型
 * Copyright: Copyright (c) 2023
 * Company: Ruijie Co., Ltd.
 * Create Time: 2023/6/8
 *
 * @author TD
 */
public enum MessageType implements MessageHandler {
    
    XML {
        @Override
        public String resolverResponse(HttpResultParserDTO parserDTO, String result) throws BusinessException {
            Assert.notNull(parserDTO, "XML parserDTO can not be null");
            try {
                Assert.hasText(result, "XML result can not be null");
                Document document = DocumentBuilderFactory.newInstance().newDocumentBuilder().parse(new InputSource(new StringReader(result)));
                XPath xpath = XPathFactory.newInstance().newXPath();
                // 编译XPath表达式
                XPathExpression expression = xpath.compile(parserDTO.getSuccessKey());
                // 执行XPath表达式
                Node node = (Node) expression.evaluate(document, XPathConstants.NODE);
                return node.getTextContent();
            } catch (Exception e) {
                throw new BusinessException(SmsAndScanCodeCheckConstants.RCDC_RCO_SMS_RESULT_RESOLVING_FAIL, e);
            }
        }
    },
    
    JSON {
        @Override
        public String resolverResponse(HttpResultParserDTO parserDTO, String result) throws BusinessException {
            Assert.notNull(parserDTO, "JSON parserDTO can not be null");
            ObjectMapper mapper = new ObjectMapper();
            try {
                Assert.hasText(result, "JSON result can not be null");
                JsonNode rootNode = mapper.readTree(result);
                for (String element : parserDTO.getSuccessKey().split("/")) {
                    rootNode = rootNode.findValue(element);
                }
                return rootNode.asText();
            } catch (Exception e) {
                throw new BusinessException(SmsAndScanCodeCheckConstants.RCDC_RCO_SMS_RESULT_RESOLVING_FAIL, e);
            }
        }
    },
    
    STRING {
        @Override
        public String resolverResponse(HttpResultParserDTO parserDTO, String result) throws BusinessException {
            Assert.notNull(parserDTO, "STRING parserDTO can not be null");
            try {
                Assert.hasText(result, "STRING result can not be null");
                Map<String, String> stringMap = SmsConverterUtils.resolvingDataToMap(result, parserDTO.getSuccessKey());
                return stringMap.get(SmsAndScanCodeCheckConstants.STRING_CODE);
            } catch (Exception e) {
                throw new BusinessException(SmsAndScanCodeCheckConstants.RCDC_RCO_SMS_RESULT_RESOLVING_FAIL, e);
            }
        }
    },
}
