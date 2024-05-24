package com.ruijie.springboot;

/**
 * Description: Function Description
 * Copyright: Copyright (c) 2023
 * Company: Ruijie Co., Ltd.
 * Create Time: 2023年10月25日
 *
 * @author 徐国祥
 */
public interface Constants {
    String ENVIROMENT_CLASS_NAME = "com.ruijie.rcos.sk.base.env.Enviroment";

    String ENVIROMENT_IS_DEVELOP_METHOD_NAME = "isDevelop";

    String ENVIROMENT_IS_DEVELOP_METHOD_BODY = "return true;";

    String CONNECTORXMLCONFIGCACHE_CLS_NAME = //
            "com.ruijie.rcos.sk.connectkit.plugin.spring.spring.ConnectorXmlConfigCache";

    String CONNECTORXMLCONFIGCACHE_ADDSERVICECONFIG_METHOD_NAME = "addServiceConfig";

    String CONNECTORXMLCONFIGCACHE_ADDCLIENTCONFIG_METHOD_NAME = "addClientConfig";

    String CONNECTORXMLCONFIGCACHE_COMMON_METHOD_BEFORE_BODY = //
            "if(\"thrift\".equals($2.getProtocol())) return;";

    String FILE_UTIL_CLASS = "com.ruijie.rcos.rcdc.rco.module.impl.util.FileUtil";

    String FILE_UTIL_FILL_PROPERTIES = "fillProperties";

    String FILE_UTIL_FILL_PROPERTIES_BODY = "{" + "java.util.Properties verIni = null;\n" + "        java.io.InputStream is = null;\n"
            + "        try {\n" + "            java.io.File file = new java.io.File($1, $2);\n"
            + "            is = new org.springframework.core.io.ClassPathResource(file.getPath()).getInputStream();\n"
            + "            verIni = new java.util.Properties();\n" + "            verIni.load(is);\n" + "        } catch (Exception e) {\n" +

            "        } finally {\n" + "            if (is != null) {\n" + "                try {\n" + "                    is.close();\n"
            + "                } catch (java.io.IOException e) {\n" + "                    throw new RuntimeException(e);\n" + "                }\n"
            + "            }\n" + "        }\n" + "        return verIni;" + "}";

    String SAMBA_UTILS_CLASS = "com.ruijie.rcos.base.sysmanage.module.impl.service.samba.SambaUtils";

    String SAMBA_UTILS_CLASS_MOUNT = "mount";

    String SAMBA_UTILS_CLASS_MOUNT_BODY = "return;";

    String RCCP_SUBSYSTEMAPI_ADAPTER_CLASS = "com.ruijie.rcos.rcdc.hciadapter.module.impl.rccp.api.RccpSubSystemAPIAdapterImpl";

    String RCCP_SUBSYSTEMAPI_ADAPTER_CLASS_OBTAINVERSION = "obtainVersion";

    String RCCP_SUBSYSTEMAPI_ADAPTER_CLASS_OBTAINVERSION_METHOD = "{" + "java.util.Properties properties = new java.util.Properties();\n" + "\n"
            + "String versionFilePath = $2;" + "String versionKey = $1;" + "        java.io.File file = new java.io.File(versionFilePath);\n"
            + "        java.io.InputStream inStream = null;\n" + "        try {\n"
            + "            inStream = new org.springframework.core.io.ClassPathResource(file.getPath()).getInputStream();\n"
            + "            properties.load(inStream);\n" + "        } catch (java.io.IOException e) {\n"
            + "            throw new RuntimeException(\"加载子系统版本文件异常，文件路径[\" + versionFilePath + \"]\", e);\n" + "        } finally {\n"
            + "            if (null != inStream) {\n" + "                try {\n" + "                    inStream.close();\n"
            + "                } catch (java.io.IOException e) {\n" + "                    throw new RuntimeException(e);\n" + "                }\n"
            + "            }\n" + "        }\n" + "\n" + "        String version = (String) properties.get(versionKey);\n" + "        return version;"
            + "}";
}
