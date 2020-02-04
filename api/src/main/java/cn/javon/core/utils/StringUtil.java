package cn.javon.core.utils;

import org.apache.commons.lang3.StringUtils;

/**
 * 字符串操作工具类
 *
 * @author Javon
 */
public class StringUtil {

    /**
     * 为空
     *
     * @param str 字符串
     * @return 是否为空
     */
    public static boolean isEmpty(String str) {
        return StringUtils.isEmpty(str);
    }

    /**
     * 不为空
     *
     * @param str 字符串
     * @return 是否不为空
     */
    public static boolean isNotEmpty(String str) {
        return StringUtils.isNotEmpty(str);
    }

}
