package com.xiaomai.geek;

import com.xiaomai.geek.common.utils.SecretUtil;

import junit.framework.Assert;

import org.junit.Test;

/**
 * Created by XiaoMai on 2017/4/10 17:02.
 */

public class AESTest {

    @Test
    public void testAES() {
        String key = SecretUtil.generateKey();
        Assert.assertEquals(key, "");
    }
}
