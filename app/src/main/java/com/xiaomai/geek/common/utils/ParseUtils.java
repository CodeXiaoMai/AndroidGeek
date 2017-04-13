
package com.xiaomai.geek.common.utils;

/**
 * Created by XiaoMai on 2016/11/30 17:19.
 */
public class ParseUtils {

    /**
     * 把Object转换为int型
     *
     * @param obj
     * @return
     */
    public static int parseToInteger(Object obj) {
        if (obj == null) {
            return 0;
        } else {
            try {
                return Integer.parseInt(obj.toString());
            } catch (Exception e) {
                return 0;
            }
        }

    }

    /**
     * 把Object转换为double型
     * 
     * @param obj
     * @return
     */
    public static double parseToDouble(Object obj) {
        if (obj == null) {
            return 0;
        } else {
            try {
                return Double.parseDouble(obj.toString());
            } catch (Exception e) {
                return 0;
            }
        }
    }

    /**
     * 把Object转换为double型
     *
     * @param obj
     * @return
     */
    public static float parseToFloat(Object obj) {
        if (obj == null) {
            return 0;
        } else {
            try {
                return Float.parseFloat(obj.toString());
            } catch (Exception e) {
                return 0;
            }
        }
    }

    /**
     * 把Object转换为boolean型
     *
     * @param obj
     * @return
     */
    public static boolean parseToBoolean(Object obj) {
        if (obj == null) {
            return false;
        } else {
            try {
                return Boolean.parseBoolean(obj.toString()) || obj.toString().equals("1");
            } catch (Exception e) {
                return false;
            }
        }
    }

    /**
     * 把Object转换为long型
     *
     * @param obj
     * @return
     */
    public static long parseToLong(Object obj) {
        if (obj == null) {
            return 0;
        } else {
            try {
                return Long.parseLong(obj.toString());
            } catch (Exception e) {
                return 0;
            }
        }
    }
}
