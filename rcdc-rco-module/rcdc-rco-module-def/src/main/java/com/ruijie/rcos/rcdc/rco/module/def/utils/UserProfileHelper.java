package com.ruijie.rcos.rcdc.rco.module.def.utils;

import com.ruijie.rcos.rcdc.rco.module.def.userprofile.constant.UserProfileBusinessKey;
import com.ruijie.rcos.rcdc.rco.module.def.userprofile.dto.UserProfileChildPathDTO;
import com.ruijie.rcos.rcdc.rco.module.def.userprofile.dto.UserProfilePathDTO;
import com.ruijie.rcos.rcdc.rco.module.def.userprofile.enums.UserProfilePathTypeEnum;
import com.ruijie.rcos.sk.base.exception.BusinessException;
import org.apache.commons.lang3.StringUtils;
import org.springframework.util.Assert;

import java.nio.charset.StandardCharsets;

/**
 * Description: 用户配置工具实现类
 * Copyright: Copyright (c) 2022
 * Company: Ruijie Co., Ltd.
 * Create Time: 2022/4/21
 *
 * @author WuShengQiang
 */
public class UserProfileHelper {

    /**
     * 限制的特定目录-不支持当前目录及子目录
     */
    private static final String[] LIMIT_PATH_ARR = new String[]{
        "c:\\windows\\system32",
        "c:\\windows\\syswow64",
        "c:\\program files (x86)\\rcc-guest-tool",
        "c:\\program files (x86)\\rg-appl",
        "c:\\program files (x86)\\rg-cloudapplicationclient",
        "c:\\program files (x86)\\rg-unifiedworkspace",
        "c:\\program files (x86)\\ruijie_cloud_office_receiver"
    };

    /**
     * 限制的特定目录-不支持当前目录,支持子目录
     */
    private static final String[] LIMIT_SINGLE_PATH_ARR = new String[]{
        "c:\\windows",
        "c:\\program files (x86)"
    };

    /**
     * 支持的注册表前缀
     */
    private static final String[] SUPPORT_REGISTRY_ARR = new String[]{
        "hkey_current_user\\software",
        "hkey_local_machine\\software"
    };

    /**
     * 连续反斜杠正则
     */
    private static final String BACKSLASH_REG = ".*\\\\\\\\.*";

    /**
     * 特殊符号
     */
    private static final String SPECIAL_SYMBOLS_REG = "^.*[/:*?\"|<>]+.*$";

    private static final String SPECIAL_SYMBOLS_REG_FOR_DOC = "^.*[/:?\"|<>]+.*$";

    private static final String TAB = "\t";

    private static final String RETURN = "\r";

    private static final String ENTER = "\n";

    /**
     * 空格
     */
    private static final String SPACE_REG = "\\s*";

    /**
     * 反斜杠
     */
    private static final String BACKSLASH = "\\";

    /**
     * 通配符
     */
    private static final String WILDCARD = "*";

    /**
     * 用于切割的反斜杠
     */
    private static final String CUTTING_BACKSLASH = "\\\\";

    /**
     * C盘根目录
     */
    private static final String DISC = "^([c-dC-D]:\\\\.*)";

    /**
     * 百分号
     */
    private static final String PERCENT = "%";

    /**
     * 两个百分号
     */
    private static final String DOUBLE_PERCENT = "%%";

    /**
     * 地址最小长度
     */
    private static final int MIN_LENGTH = 4;

    /**
     * 地址最大长度
     */
    private static final int MAX_LENGTH = 260;

    /**
     * 通配符最大个数
     */
    private static final int MAX_WILDCARD_NUMBER = 1;


    /**
     * 校验路径
     *
     * @param userProfilePathDTO 路径对象
     * @throws BusinessException 业务异常
     */
    public static void pathValidate(UserProfilePathDTO userProfilePathDTO) throws BusinessException {
        Assert.notNull(userProfilePathDTO, "userProfilePathDTO cannot null");

        UserProfileChildPathDTO[] childPathArr = userProfilePathDTO.getChildPathArr();
        for (UserProfileChildPathDTO dto : childPathArr) {
            String[] pathArr = dto.getPathArr();
            for (String path : pathArr) {
                pathValidate(path, dto.getType());
            }
        }
    }

    /**
     * 路径判断
     *
     * @param pathValue 路径
     * @param type      类型
     * @throws BusinessException 异常处理
     */
    public static void pathValidate(String pathValue, UserProfilePathTypeEnum type) throws BusinessException {
        Assert.hasText(pathValue, "path in not null");
        Assert.notNull(type, "type is not null");

        String path = pathValue.toLowerCase();
        // 1.路径长度不小于4位,不大于300个字符,一个中文算3个字符
        if (path.length() < MIN_LENGTH || path.getBytes(StandardCharsets.UTF_8).length > MAX_LENGTH) {
            throw new BusinessException(UserProfileBusinessKey.RCDC_RCO_USER_PROFILE_PATH_LENGTH_VALIDATOR);
        }
        // 2.第一位和最后一位不能使用反斜杠
        if (BACKSLASH.equals(path.substring(path.length() - 1)) || BACKSLASH.equals(path.substring(0, 1))) {
            throw new BusinessException(UserProfileBusinessKey.RCDC_RCO_USER_PROFILE_PATH_LAST_BACKSLASH_VALIDATOR);
        }
        //不能包括tab(\t), 换行(\n)，回车(\r)等转义字符
        if (path.contains(TAB) || path.contains(RETURN) || path.contains(ENTER)) {
            throw new BusinessException(UserProfileBusinessKey.RCDC_RCO_USER_PROFILE_PATH_SPECIAL_TAB_VALIDATOR);
        }
        // 3.限制特定目录
        // 不支持当前目录,支持子目录
        for (String limitPath : LIMIT_SINGLE_PATH_ARR) {
            if (limitPath.equals(path)) {
                throw new BusinessException(UserProfileBusinessKey.RCDC_RCO_USER_PROFILE_PATH_LIMIT_SINGLE_VALIDATOR);
            }
        }
        // 不支持当前目录及子目录
        for (String limitPath : LIMIT_PATH_ARR) {
            if (path.startsWith(limitPath)) {
                // 获取前缀目录的后一位,为空或为反斜杠都属特定目录
                String last = path.substring(limitPath.length());
                if (StringUtils.isEmpty(last) || last.startsWith(BACKSLASH)) {
                    throw new BusinessException(UserProfileBusinessKey.RCDC_RCO_USER_PROFILE_PATH_LIMIT_VALIDATOR);
                }
            }
        }
        // 4.不能包含连续的\\反斜杠
        if (path.matches(BACKSLASH_REG)) {
            throw new BusinessException(UserProfileBusinessKey.RCDC_RCO_USER_PROFILE_PATH_BACKSLASH_VALIDATOR);
        }


        // 5.全部空格不能作为目录, 环境变量也可能有空格目录%  %
        String[] splitArr = path.split(CUTTING_BACKSLASH);
        for (String oldPath : splitArr) {
            String newPath = oldPath.replaceAll(SPACE_REG, "");
            if (StringUtils.isEmpty(newPath) || DOUBLE_PERCENT.equals(newPath)) {
                throw new BusinessException(UserProfileBusinessKey.RCDC_RCO_USER_PROFILE_PATH_SPACE_VALIDATOR);
            }
        }

        String root = splitArr[0];
        String front = root.substring(0, 1);
        String after = root.substring(root.length() - 1);
        // 去掉c盘路径中的:
        String specialPath = path;
        if (!PERCENT.equals(front)) {
            specialPath = path.substring(2);
        }
        switch (type) {
            case FOLDER:
                // 特殊符号检查
                if (specialPath.matches(SPECIAL_SYMBOLS_REG)) {
                    throw new BusinessException(UserProfileBusinessKey.RCDC_RCO_USER_PROFILE_PATH_SPECIAL_VALIDATOR);
                }
                // 开头只支持c盘、d盘和%环境变量%
                if (!path.matches(DISC) && (!PERCENT.equals(front) || !PERCENT.equals(after))) {
                    throw new BusinessException(UserProfileBusinessKey.RCDC_RCO_USER_PROFILE_PATH_ROOT_VALIDATOR);
                }
                break;
            case DOCUMENT:
                // 特殊符号检查
                if (specialPath.matches(SPECIAL_SYMBOLS_REG_FOR_DOC)) {
                    throw new BusinessException(UserProfileBusinessKey.RCDC_RCO_USER_PROFILE_PATH_SPECIAL_FOR_DOC_VALIDATOR);
                }
                if (isConflictWildcard(specialPath)) {
                    throw new BusinessException(UserProfileBusinessKey.RCDC_RCO_USER_PROFILE_PATH_WILDCARD_VALIDATOR);
                }
                // 开头只支持c盘、d盘和%环境变量%
                if (!path.matches(DISC) && (!PERCENT.equals(front) || !PERCENT.equals(after))) {
                    throw new BusinessException(UserProfileBusinessKey.RCDC_RCO_USER_PROFILE_PATH_ROOT_VALIDATOR);
                }
                break;
            case REGISTRY_KEY:
            case REGISTRY_VALUE:
                // 匹配目录前缀,并且存在子目录
                for (String support : SUPPORT_REGISTRY_ARR) {
                    if (path.startsWith(support)) {
                        String suffix = path.substring(support.length());
                        if (StringUtils.isNotEmpty(suffix)) {
                            if (!suffix.startsWith(BACKSLASH) || splitArr.length > 2) {
                                return;
                            }
                        }
                    }
                }
                throw new BusinessException(UserProfileBusinessKey.RCDC_RCO_USER_PROFILE_PATH_PREFIX_VALIDATOR);
            default:
                // 未知的类型
                throw new BusinessException(UserProfileBusinessKey.RCDC_RCO_USER_PROFILE_PATH_UNKNOWN_TYPE_VALIDATOR);
        }
    }

    /**
     * 通配符检查
     *
     * @param path 路径
     * @return 结果
     */
    private static boolean isConflictWildcard(String path) {
        String[] stringDirArr = path.split(CUTTING_BACKSLASH);
        int length = stringDirArr.length;
        for (int i = 0; i < length; i++) {
            //最后文件名部分进行通配符判断
            if (i == length - 1) {
                //只有通配符，不符合条件
                if (stringDirArr[i].equals(WILDCARD)) {
                    return true;
                }

                int count = StringUtils.countMatches(stringDirArr[i], WILDCARD);
                //通配符出现次数大于1次，不符合条件
                if (count > MAX_WILDCARD_NUMBER) {
                    return true;
                }

                //通过校验，与通配符规则不冲突
                return false;
            }

            //非最后文件名部分出现通配符，与限制规则冲突
            if (stringDirArr[i].contains(WILDCARD)) {
                return true;
            }
        }

        return false;
    }
}