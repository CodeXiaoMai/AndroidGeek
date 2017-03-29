
package com.xiaomai.geek.common.constant;

/**
 * Created by XiaoMai on 2017/3/27 11:07.
 */

public class Const {

    // 查询网络的Cache-Control设置
    // (假如请求了服务器并在某时刻返回响应结果，则在max-age规定的秒数内，浏览器将不会发送对应的请求到服务器，数据由缓存直接返回)
    public static final String CACHE_CONTROL_NETWORK = "Cache-Control: public, max-age=3600";

    // 避免出现 HTTP 403
    // Forbidden，参考：http://stackoverflow.com/questions/13670692/403-forbidden-with-java-but-not-web-browser
    public static final String AVOID_HTTP403_FORBIDDEN = "User-Agent: Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.11 (KHTML, like Gecko) Chrome/23.0.1271.95 Safari/537.11";

}
