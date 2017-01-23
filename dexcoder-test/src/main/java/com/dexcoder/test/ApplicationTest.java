package com.dexcoder.test;

import com.dexcoder.commons.utils.UUIDUtils;

/**
 * 当工程模块内无任务代码生成时,maven install会通不过
 * 此类的存在只是确保maven install的成功
 */
public class ApplicationTest {

    /**
     * 测试方法
     */
    public void test() {
        System.out.printf("-----------------");
    }

    public static void main(String[] args) {

        long begin = System.currentTimeMillis();

        String str = "markdown内容不能为空";

        StringBuilder sb = new StringBuilder();
        for (int i=0;i<str.length();i++){

            char c = str.charAt(i);

            sb.append((int)c);
        }

        String uuid8 = UUIDUtils.getUUID16(sb.toString().getBytes());
        System.out.println(uuid8);

        System.out.println(sb);

        System.out.println(System.currentTimeMillis() - begin);

    }

}
