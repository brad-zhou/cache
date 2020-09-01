package com.brad.utils.string;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 字符串常用工具类
 *
 * @author zhangyu
 */
public class StringUtil {

    /**
     * 空字符串
     */
    public final static String EMPTY_STR = "";

    /**
     * 校验字符串是否为空或者空字符串
     *
     * @param str 待校验字符串
     * @return boolean
     */
    public static boolean isNullOrEmpty(String str) {
        return str == null || EMPTY_STR.equals(str);
    }

    /**
     * 校验字符串是否不为空或者空字符串
     *
     * @param str 待校验字符串
     * @return boolean
     */
    public static boolean isNotNullOrEmpty(String str) {
        return !isNullOrEmpty(str);
    }

    /**
     * 判断对象是否空或者空字符串
     *
     * @param obj 待校验的对象
     * @return boolean
     */
    public static boolean isNullOrEmpty(Object obj) {
        return obj == null || EMPTY_STR.equals(String.valueOf(obj).trim());
    }

    /**
     * 获取uuid
     *
     * @return uuid
     */
    public static String generateUUID() {
        return UUID.randomUUID().toString().replace("-", "");
    }

    /**
     * 获取字符串的字节长度
     *
     * @param str 字符串
     * @return 字节长度
     */
    public static int getDefaultByteCount(String str) {
        if (isNullOrEmpty(str)) {
            return 0;
        }
        return str.getBytes().length;
    }

    /**
     * 判断两个字符串是否相等
     *
     * @param str1 字符串1
     * @param str2 字符串2
     * @return Boolean 是否相等
     */
    public static Boolean isEqual(String str1, String str2) {
        if (str1 == null && str2 == null) {
            return true;
        }
        if (str1 != null) {
            return str1.equals(str2);
        }
        return false;
    }

    /**
     * 根据指定字符分割原字符串，并以数组返回分割后的字符串
     *
     * @param str      待分割的字符串
     * @param seprator 指定字符
     * @return 分割后的字符串数组
     */
    public static String[] spiltStringToArray(String str, String seprator) {
        List<String> list = new ArrayList<>();
        if (isNullOrEmpty(str)) {
            return null;
        }
        int index = str.indexOf(seprator);
        if (index >= 0) {
            while (index >= 0) {
                String tem = str.substring(0, index);
                list.add(tem);
                str = str.substring(index + 1);
                index = str.indexOf(seprator);
            }
            if (!isNullOrEmpty(str)) {
                list.add(str);
            }
        } else {
            list.add(str);
        }
        String[] arr = new String[list.size()];
        for (int i = 0; i < list.size(); i++) {
            arr[i] = list.get(i);
        }
        return arr;
    }

    /**
     * 在字符串后面追加指定字符，并指定字符串的长度(如果指定长度小于字符串原长度，则不追加)
     *
     * @param str         待追加的字符串
     * @param totalWidth  指定长度
     * @param paddingChar 要追加的字符
     * @return 追加后的字符串
     */
    public static String padRight(String str, int totalWidth, char paddingChar) {
        if (StringUtil.isNullOrEmpty(str)) {
            str = EMPTY_STR;
        }
        int strLen = str.length();
        if (strLen >= totalWidth) {
            return str;
        }
        StringBuilder sb = new StringBuilder(totalWidth);
        sb.append(str);
        while (sb.length() < totalWidth) {
            sb.append(paddingChar);
        }
        return sb.toString();
    }

    /**
     * 在字符串前面追加指定字符，并指定字符串的长度(如果指定长度小于字符串原长度，则不追加)
     *
     * @param str         待追加的字符串
     * @param totalWidth  指定长度
     * @param paddingChar 要追加的字符
     * @return 追加后的字符串
     */
    public static String padLeft(String str, int totalWidth, char paddingChar) {
        if (StringUtil.isNullOrEmpty(str)) {
            str = EMPTY_STR;
        }
        int strLen = str.length();
        if (strLen >= totalWidth) {
            return str;
        }

        StringBuilder sb = new StringBuilder(totalWidth);
        int len = totalWidth - strLen;
        while (sb.length() < len) {
            sb.append(paddingChar);
        }
        sb.append(str);
        return sb.toString();
    }

    /**
     * 下划线转驼峰
     *
     * @param line
     * @param smallCamel 是否小驼峰
     * @return
     */
    public static String underline2Camel(String line, boolean smallCamel) {
        if (line == null || "".equals(line)) {
            return "";
        }
        StringBuffer sb = new StringBuffer();
        String regex = "([A-Za-z\\d]+)(_)?";
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(line);
        while (matcher.find()) {
            String word = matcher.group();
            sb.append(smallCamel && matcher.start() == 0 ? Character.toLowerCase(word.charAt(0)) : Character.toUpperCase(word.charAt(0)));
            int index = word.lastIndexOf('_');
            if (index > 0) {
                sb.append(word.substring(1, index).toLowerCase());
            } else {
                sb.append(word.substring(1).toLowerCase());
            }
        }
        return sb.toString();
    }

    /**
     * 驼峰转下划线
     *
     * @param line
     * @return
     */
    public static String camel2Underline(String line) {
        if (line == null || "".equals(line)) {
            return "";
        }
        line = String.valueOf(line.charAt(0)).toUpperCase().concat(line.substring(1));
        StringBuffer sb = new StringBuffer();
        String regex = "[A-Z]([a-z\\d]+)?";
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(line);
        while (matcher.find()) {
            String word = matcher.group();
            sb.append(word.toUpperCase());
            sb.append(matcher.end() == line.length() ? "" : "_");
        }
        return sb.toString().toLowerCase();
    }

    /**
     * 判断字符串是否为纯数字(包含浮点型)
     *
     * @param str
     * @return
     */
    public static boolean isNumeric(String str) {
        String reg = "^[0-9]+(.[0-9]+)?$";
        return str.matches(reg);
    }

    /**
     * 统计一个字符串在另一个字符串中出现的次数
     *
     * @param subStr    一个字符串
     * @param sourceStr 另一个字符串
     * @return 次数
     */
    public static int subStringCount(String subStr, String sourceStr) {
        String regex = "[a-zA-Z]+";
        if (isNotNullOrEmpty(subStr)) {
            regex = subStr;
        }
        Pattern expression = Pattern.compile(regex);
        Matcher matcher = expression.matcher(sourceStr);
        int n = 0;
        while (matcher.find()) {
            n++;
        }
        return n;
    }

    /**
     * 判断字符串是否包含中文
     *
     * @param str 字符串
     * @return 是否中文
     */
    public static boolean isContainChinese(String str) {
        Pattern p = Pattern.compile("[\u4e00-\u9fa5]");
        Matcher m = p.matcher(str);
        if (m.find()) {
            return true;
        }
        return false;
    }
}
