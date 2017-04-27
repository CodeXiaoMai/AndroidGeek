
package com.xiaomai.geek.common.utils;

import android.text.TextUtils;
import android.util.Base64;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by XiaoMai on 2017/3/17 10:56.
 */

public class StringUtil {

    /**
     * 替换字符串中的空格，换行，制表符
     * 
     * @param str
     * @return
     */
    public static String replaceAllBlank(String str) {
        if (TextUtils.isEmpty(str))
            return "";
        Pattern pattern = Pattern.compile("\\s*|\t|\r|\n");
        Matcher matcher = pattern.matcher(str);
        return matcher.replaceAll("");
    }

    /**
     * @param str
     * @return
     */
    public static String trimNewLine(String str) {
        if (TextUtils.isEmpty(str))
            return "";
        str = str.trim();
        Pattern pattern = Pattern.compile("\t|\r|\n");
        Matcher matcher = pattern.matcher(str);
        return matcher.replaceAll("");
    }

    public static String base64Decode(String originalString) {
        if (TextUtils.isEmpty(originalString))
            return "";
        return new String(Base64.decode(originalString, Base64.DEFAULT));
    }

}
